package mil.darpa.log.alpine.blackjack.assessui.client;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.ui.components.CComboSelector;
import org.cougaar.lib.uiframework.ui.components.CDesktopFrame;
import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.components.CRangeButton;
import org.cougaar.lib.uiframework.ui.components.CMThumbSliderThresholdControl;
import org.cougaar.lib.uiframework.ui.components.CRLabel;
import org.cougaar.lib.uiframework.ui.components.CSliderThresholdControl;
import org.cougaar.lib.uiframework.ui.components.CStoplightTable;
import org.cougaar.lib.uiframework.ui.components.CTreeButton;
import org.cougaar.lib.uiframework.ui.components.CViewFeatureSelectionControl;
import org.cougaar.lib.uiframework.ui.models.DatabaseTableModel;
import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.models.StoplightThresholdModel;
import org.cougaar.lib.uiframework.ui.models.VariableModel;
import org.cougaar.lib.uiframework.ui.util.CougaarUI;
import org.cougaar.lib.uiframework.ui.util.Selector;
import org.cougaar.lib.uiframework.ui.util.SliderControl;
import org.cougaar.lib.uiframework.ui.util.TableSorter;
import org.cougaar.lib.uiframework.ui.util.VariableInterfaceManager;

/**
 * Panel that contains a stoplight chart and controls for selecting what
 * data is viewed.  Controls pull and aggregate two dimensional cross
 * sections of data out of a database that holds a 4 dimensional data table.
 * The dimensions are: Item, Organization, Metric, and Time.
 */
public class StoplightPanel extends JPanel implements CougaarUI
{
    private boolean plaf = false;
    private boolean useMenuButtons = true;
    private final Object[] metrics = DBInterface.metrics;
    private final static int spacing = 5;
    private DatabaseTableModel stoplightTableModel = new DatabaseTableModel();
    private VariableInterfaceManager variableManager;
    private JLabel title = new JLabel("", JLabel.CENTER);
    private CStoplightTable stoplightChart;
    private JPanel thresholdsPanel = null;
    private CViewFeatureSelectionControl viewPanel = null;
    private QueryGenerator queryGenerator = null;
    private static final Vector stoplightMetrics = new Vector();

    /**
     * Create a new stoplight panel
     *
     * @param frame          frame in which this panel will reside.
     * @param plaf           true if pluggable look and feel must be supported
     */
    public StoplightPanel(boolean plaf)
    {
        super(new BorderLayout());
        this.plaf = plaf;
        createComponents();
    }

    /**
     * Create a new stoplight panel
     *
     * @param frame          frame in which this panel will reside.
     * @param plaf           true if pluggable look and feel must be supported
     * @param useMenuButtons true if variable manager should use CMenuButtons
     *                       for variable management; otherwise CComboSelectors
     *                       will be used.
     */
    public StoplightPanel(boolean plaf, boolean useMenuButtons)
    {
        super(new BorderLayout());
        this.plaf = plaf;
        this.useMenuButtons = useMenuButtons;
        createComponents();
    }

    /**
     * Add this panel to the passed in JFrame.  This method is required to
     * implement the CougaarUI interface.
     *
     * @param frame frame to which the panel should be added
     */
    public void install(JFrame frame)
    {
        frame.getContentPane().add(this);
    }

    /**
     * Add this panel to the passed in JInternalFrame.  This method is required
     * to implement the CougaarUI interface.
     *
     * @param frame frame to which the panel should be added
     */
    public void install(JInternalFrame frame)
    {
        frame.getContentPane().add(this);
    }

    /**
     * Returns true if this CougaarUI supports pluggable look and feel.  This
     * method is required to implement the CougaarUI interface.
     *
     * @return true if UI supports pluggable look and feel
     */
    public boolean supportsPlaf()
    {
        return plaf;
    }

    /**
     * Creates the components of the stoplight UI
     */
    private void createComponents()
    {
        stoplightMetrics.add("Supply as Proportion of Demand");

        //DefaultMutableTreeNode root =
        //    DBInterface.createTree(DBInterface.getTableName("item"));
        DefaultMutableTreeNode root = DBInterface.itemTree;
        CTreeButton itemTreeButton = new CTreeButton(root, root);

        //root = DBInterface.createTree(DBInterface.getTableName("org"));
        root = DBInterface.orgTree;
        CTreeButton orgTreeButton = new CTreeButton(  root, root);

        CComboSelector metricSelector = new CComboSelector(metrics);

        CRangeButton rangeButton =
            new CRangeButton("C", DBInterface.minTimeRange,
                             DBInterface.maxTimeRange, plaf);
        rangeButton.roundAndSetSliderRange(DBInterface.minTimeRange,
                                           DBInterface.maxTimeRange);

        VariableModel[] variables =
        {
            new VariableModel("Metric", metricSelector, false,
                              VariableModel.FIXED, true, 80),
            new VariableModel("Time", rangeButton, true,
                              VariableModel.X_AXIS, true, 0),
            new VariableModel("Item", itemTreeButton, true,
                              VariableModel.Y_AXIS, true, 0),
            new VariableModel("Org", orgTreeButton, true,
                              VariableModel.FIXED, true, 80) //,
            //new VariableModel("Location", new JTreeButton(), true),
            //new VariableModel("Pat Cond", new JTreeButton(), true)
        };

        //Border etchedBorder = BorderFactory.createEtchedBorder();
        Border emptyBorder =
           BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing);

        // create a new query generator to update stoplightTableModel based
        // on (and triggered by) changes to variable controls.
        queryGenerator = new QueryGenerator(stoplightTableModel);
        variableManager =
            new VariableInterfaceManager(variables, useMenuButtons);
        variableManager.addVariableListener(
            new VariableInterfaceManager.VariableListener() {
                public void variableChanged(VariableModel vm)
                {
                    updateView();
                }
                public void variablesSwapped(VariableModel vm1,
                                             VariableModel vm2)
                {
                    updateView();
                }
            });

        // stoplight settings panel
        viewPanel = new CViewFeatureSelectionControl(BoxLayout.X_AXIS);
        viewPanel.setBorder(BorderFactory.createTitledBorder("View"));
        metricSelector.addPropertyChangeListener("selectedItem",
                                                 new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent e)
                {
                    String newSelectedMetric = e.getNewValue().toString();
                    updateStoplightViewOptions(newSelectedMetric);
                }
            });

        if (plaf)
        {
            thresholdsPanel = new CSliderThresholdControl(0f, 2f);
        }
        else
        {
            thresholdsPanel = new CMThumbSliderThresholdControl(0f, 2f);
        }
        thresholdsPanel.setBorder(
            BorderFactory.createTitledBorder("Color Thresholds"));

        // generate initial query based on initial variable settings
        updateView();

        JPanel fixedVariablesPanel = new JPanel(new FlowLayout());
        fixedVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Fixed Variables"));
        Box fvBox = new Box(BoxLayout.X_AXIS);
        fvBox.add(variableManager.getDescriptor("Metric").getControl());
        fvBox.add(Box.createHorizontalStrut(spacing * 2));
        fvBox.add(variableManager.getDescriptor("Org").getControl());
        fixedVariablesPanel.add(fvBox);


        JPanel independentVariablesPanel = new JPanel(new FlowLayout());
        independentVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Independent Variables"));
        Box ivBox = new Box(BoxLayout.X_AXIS);
        Component xControl =
            ((VariableModel)variableManager.getDescriptors(
            VariableModel.X_AXIS).nextElement()).getControl();
        Component yControl =
            ((VariableModel)variableManager.getDescriptors(
            VariableModel.Y_AXIS).nextElement()).getControl();
        ivBox.add(new JLabel("X Axis: "));
        ivBox.add(xControl);
        ivBox.add(Box.createHorizontalStrut(spacing * 2));
        ivBox.add(new JLabel("Y Axis: "));
        ivBox.add(yControl);
        independentVariablesPanel.add(ivBox);


        // Create the chart and set chart controls
        JPanel stoplightPanel = new JPanel(new BorderLayout());
        CRLabel xAxisLabel = variableManager.getXAxisLabel();
        JPanel xAxisPanel = new JPanel(new GridBagLayout());
        xAxisPanel.add(xAxisLabel);
        stoplightPanel.add(xAxisPanel, BorderLayout.NORTH);
        CRLabel yAxisLabel = variableManager.getYAxisLabel();
        yAxisLabel.setOrientation(CRLabel.DOWN_UP);
        JPanel yAxisPanel = new JPanel(new GridBagLayout());
        yAxisPanel.add(yAxisLabel);
        stoplightPanel.add(yAxisPanel, BorderLayout.WEST);

        TableSorter sorter = new TableSorter(stoplightTableModel);
        stoplightChart = new CStoplightTable(sorter);
        sorter.addMouseListenerToHeaderInTable(stoplightChart);
        JScrollPane scrolledStoplightChart = new JScrollPane(stoplightChart);
        //stoplightPanel.add(axisControlPanel, BorderLayout.NORTH);
        stoplightPanel.add(scrolledStoplightChart, BorderLayout.CENTER);
        stoplightPanel.setBorder(BorderFactory.createEtchedBorder());
        stoplightChart.setViewFeatureSelectionControl(viewPanel);
        updateStoplightViewOptions(
            metricSelector.getSelectedItem().toString());

        // associate threshold control with stoplight chart
        if (plaf)
        {
            stoplightChart.setThresholds((
                (CSliderThresholdControl)thresholdsPanel).getThresholds());
        }
        else
        {
            stoplightChart.setThresholds((
               (CMThumbSliderThresholdControl)thresholdsPanel).getThresholds());
        }
        thresholdsPanel.addPropertyChangeListener("thresholds",
                                                  new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent e)
                {
                    stoplightChart.setThresholds((StoplightThresholdModel)
                                                 e.getNewValue());
                }
            });

        MouseListener doubleClickTableListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent event) {
                    if (event.getClickCount() == 2)
                    {
                        doubleClickTableHandler(event);
                    }
                }
            };
        stoplightChart.addMouseListener(doubleClickTableListener);
        stoplightChart.getTableHeader().
            addMouseListener(doubleClickTableListener);
        stoplightChart.getRowHeader().
            addMouseListener(doubleClickTableListener);

        // high level layout
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        JPanel controlPanel = new JPanel(new BorderLayout());
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=0;
        gbc.weighty=0;
        JPanel topControlPanel = new JPanel(gbl);
        gbc.fill=GridBagConstraints.BOTH;
        gbl.setConstraints(viewPanel, gbc);
        topControlPanel.add(viewPanel);
        gbc.weightx=1;
        gbc.weighty=1;
        gbl.setConstraints(thresholdsPanel, gbc);
        topControlPanel.add(thresholdsPanel);
        JPanel bottomControlPanel = new JPanel(gbl);
        gbl.setConstraints(fixedVariablesPanel, gbc);
        bottomControlPanel.add(fixedVariablesPanel);
        gbl.setConstraints(independentVariablesPanel, gbc);
        bottomControlPanel.add(independentVariablesPanel);
        controlPanel.add(topControlPanel, BorderLayout.NORTH);
        controlPanel.add(bottomControlPanel, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);
        add(stoplightPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void updateStoplightViewOptions(String newSelectedMetric)
    {
        if (stoplightMetrics.contains(newSelectedMetric))
        {
            viewPanel.setSelectedItem(CViewFeatureSelectionControl.COLOR);
        }
        else
        {
            viewPanel.setSelectedItem(CViewFeatureSelectionControl.VALUE);
        }
    }

    private void updateView()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        queryGenerator.generateQuery(variableManager);
        updateThresholdExtents();
        title.setText(variableManager.toString());
        setCursor(Cursor.getDefaultCursor());
    }

    private void doubleClickTableHandler(MouseEvent e)
    {
        int row = stoplightChart.getSelectedRow();
        int column = stoplightChart.
            convertColumnIndexToModel(stoplightChart.getSelectedColumn());

        if (e.getSource().equals(stoplightChart.getTableHeader()))
        {
            TableColumnModel columnModel = stoplightChart.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int hIndex =
                stoplightChart.convertColumnIndexToModel(viewColumn);
            Object value = stoplightTableModel.getColumnName(hIndex);
            ((VariableModel)variableManager.
                getDescriptors(VariableModel.X_AXIS).nextElement()).
                getSelector().setSelectedItem(value);
            return;
        }

        if (e.getSource().equals(stoplightChart.getRowHeader()))
        {
            row = stoplightChart.getRowHeader().getSelectedRow();
            Object value = stoplightTableModel.getValueAt(row, 0);
            ((VariableModel)variableManager.
                getDescriptors(VariableModel.Y_AXIS).nextElement()).
                getSelector().setSelectedItem(value);

            return;
        }

        // Launch line plot chart preconfigured based on double-clicked cell
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        JFrame myFrame = findJFrame();
        JFrame linePlotFrame;
        if (myFrame instanceof CDesktopFrame)
        {
            linePlotFrame = myFrame;
        }
        else
        {
            linePlotFrame = new CFrame(UIConstants.LINEPLOT_UI_NAME, plaf);
        }
        LinePlotPanel lpp = new LinePlotPanel(plaf, useMenuButtons);
        VariableInterfaceManager linePlotVIM =
            lpp.getVariableInterfaceManager();

        linePlotVIM.getDescriptor("Metric").
            setValue(UIConstants.STOPLIGHT_UI_NAME + " Components");
        linePlotVIM.setYAxis("Metric");

        // set stoplight's fixed variable values in line plot
        Enumeration fixedDescs =
            variableManager.getDescriptors(VariableModel.FIXED);
        while (fixedDescs.hasMoreElements())
        {
            VariableModel v =(VariableModel)fixedDescs.nextElement();
            String vName = v.getName();
            if (!vName.equals("Metric"))
            {
                linePlotVIM.getDescriptor(vName).setValue(v.getValue());
            }
        }

        // set stoplight's y variable value in line plot
        VariableModel yDesc = (VariableModel)variableManager.
            getDescriptors(VariableModel.Y_AXIS).nextElement();
        String yDescName = yDesc.getName();
        VariableModel lpDesc = linePlotVIM.getDescriptor(yDescName);
        Object selectedYValue = stoplightTableModel.getValueAt(row, 0);
        if (yDescName.equals("Time"))
        {
            int selectedTime = Integer.parseInt(selectedYValue.toString());
            lpDesc.setValue(
                new RangeModel(selectedTime - 10, selectedTime + 10));
        }
        else
        {
            lpDesc.setValue(selectedYValue);
        }

        // set stoplight's x variable value in line plot
        VariableModel xDesc = (VariableModel)variableManager.
            getDescriptors(VariableModel.X_AXIS).nextElement();
        String xDescName = xDesc.getName();
        lpDesc = linePlotVIM.getDescriptor(xDescName);
        Object selectedXValue = stoplightTableModel.getColumnName(column);
        if (xDescName.equals("Time"))
        {
            int selectedTime = Integer.parseInt(selectedXValue.toString());
            lpDesc.setValue(new RangeModel(Math.max(0, selectedTime - 10),
                                           selectedTime + 10));
        }
        else
        {
            lpDesc.setValue(selectedXValue);
        }

        if (linePlotFrame instanceof CDesktopFrame)
        {
            CDesktopFrame cfc = (CDesktopFrame)linePlotFrame;
            cfc.createInnerFrame(UIConstants.LINEPLOT_UI_NAME +
                                 (plaf?" (PLAF)":""), lpp);
        }
        else
        {
            lpp.install(linePlotFrame);
            linePlotFrame.setVisible(true);
        }
        setCursor(Cursor.getDefaultCursor());
    }


    /**
     * Returns the variable interface manager for this UI.  This can be used
     * to configure the selected values and roles of the dimension variables
     * (Item, Organization, Metric, and Time) programmatically.
     *
     * @return the variable interface manager for this UI
     */
     public VariableInterfaceManager getVariableInterfaceManager()
    {
        return variableManager;
    }

    private JFrame findJFrame()
    {
        Container parent = getParent();
        while (!(parent instanceof JFrame))
        {
            parent = parent.getParent();
        }
        return (JFrame)parent;
    }

    private void updateThresholdExtents()
    {
        // find minimum and maximum values in table
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (int row = 0; row < stoplightTableModel.getRowCount(); row++)
        {
            for (int column = 1; column < stoplightTableModel.getColumnCount();
                 column++)
            {
                Object valueObj = stoplightTableModel.getValueAt(row, column);
                if (valueObj instanceof Number)
                {
                    float value = ((Number)valueObj).floatValue();
                    minValue = Math.min(minValue, value);
                    maxValue = Math.max(maxValue, value);
                }
            }
        }

        float newShift = ((SliderControl)thresholdsPanel).
            roundAndSetSliderRange(minValue, maxValue);
        // if new slider range was modified by an exponential amount,
        // redistribute threshold values
        if (newShift != shift)
        {
            ((SliderControl)thresholdsPanel).evenlyDistributeValues();
            shift = newShift;
        }
    }
    private float shift = 0;

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        if ((System.getProperty("DBTYPE") == null) ||
            (System.getProperty("DBURL") == null) ||
            (System.getProperty("DBUSER") == null) ||
            (System.getProperty("DBPASSWORD") == null))
        {
            System.out.println("You need to set the following system property"+
                               " variables:  DBTYPE, DBURL, DBUSER, and " +
                               "DBPASSWORD");
            return;
        }

        boolean plaf = Boolean.getBoolean("PLAF");
        CFrame frame = new CFrame(UIConstants.STOPLIGHT_UI_NAME, plaf);
        StoplightPanel slp = new StoplightPanel(plaf);
        slp.getVariableInterfaceManager().getDescriptor("Metric").
            setValue("Supply as Proportion of Demand");
        slp.install(frame);
        frame.setVisible(true);
    }
}