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
    private CStoplightTable stoplightChart;

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
        //DefaultMutableTreeNode root =
        //    DBInterface.createTree(DBInterface.getTableName("item"));
        DefaultMutableTreeNode root = DBInterface.itemTree;
        CTreeButton itemTreeButton = new CTreeButton(root, root);

        //root = DBInterface.createTree(DBInterface.getTableName("org"));
        root = DBInterface.orgTree;
        CTreeButton orgTreeButton = new CTreeButton(  root, root);

        CComboSelector metricSelector = new CComboSelector(metrics);

        VariableModel[] variables =
        {
            new VariableModel("Metric", metricSelector, false,
                              VariableModel.FIXED, true, 80),
            new VariableModel("Time", new CRangeButton("C+", 0, 30, plaf),
                              true, VariableModel.X_AXIS, true, 0),
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
        JPanel controlPanel = new JPanel(new GridLayout());

        // create a new query generator to update stoplightTableModel based
        // on (and triggered by) changes to variable controls.
        final QueryGenerator qg = new QueryGenerator(stoplightTableModel);
        variableManager =
            new VariableInterfaceManager(variables, useMenuButtons);
        variableManager.addVariableListener(
            new VariableInterfaceManager.VariableListener() {
                public void variableChanged(VariableModel vm)
                {
                    qg.generateQuery(variableManager);
                }
                public void variablesSwapped(VariableModel vm1,
                                             VariableModel vm2)
                {
                    qg.generateQuery(variableManager);
                }
            });

        // generate initial query based on initial variable settings
        qg.generateQuery(variableManager);

        final CViewFeatureSelectionControl viewPanel =
            new CViewFeatureSelectionControl(BoxLayout.Y_AXIS);
        viewPanel.setBorder(BorderFactory.createTitledBorder("View"));

        JPanel thresholdsPanel;
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

        JPanel fixedVariablesPanel = new JPanel(new FlowLayout());
        fixedVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Fixed Variables"));

        //fixedVariablesPanel.setLayout(new BoxLayout(fixedVariablesPanel,
        //                                            BoxLayout.Y_AXIS));
        Box fvBox = new Box(BoxLayout.Y_AXIS);
        fvBox.add(variableManager.getDescriptor("Metric").getControl());
        fvBox.add(Box.createVerticalStrut(spacing * 2));
        fvBox.add(variableManager.getDescriptor("Org").getControl());
        fixedVariablesPanel.add(fvBox);

        // Create the chart and set chart controls
        JPanel stoplightPanel = new JPanel(new BorderLayout());
        Component xControl =
            ((VariableModel)variableManager.getDescriptors(
            VariableModel.X_AXIS).nextElement()).getControl();
        Component yControl =
            ((VariableModel)variableManager.getDescriptors(
            VariableModel.Y_AXIS).nextElement()).getControl();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=1;
        gbc.insets = new Insets(spacing, spacing, spacing, spacing);
        JPanel axisControlPanel = new JPanel(gbl);
        gbc.anchor=GridBagConstraints.WEST;
        gbl.setConstraints(yControl, gbc);
        axisControlPanel.add(yControl);
        gbc.anchor=GridBagConstraints.CENTER;
        gbl.setConstraints(xControl, gbc);
        axisControlPanel.add(xControl);

        TableSorter sorter = new TableSorter(stoplightTableModel);
        stoplightChart = new CStoplightTable(sorter);
        sorter.addMouseListenerToHeaderInTable(stoplightChart);
        JScrollPane scrolledStoplightChart = new JScrollPane(stoplightChart);
        stoplightPanel.add(axisControlPanel, BorderLayout.NORTH);
        stoplightPanel.add(scrolledStoplightChart, BorderLayout.CENTER);
        stoplightPanel.setBorder(BorderFactory.createEtchedBorder());
        stoplightChart.setViewFeatureSelectionControl(viewPanel);

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

        // high level layout
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.weightx=1;
        gbc.weighty=1;
        JPanel leftPanel = new JPanel(gbl);
        gbc.fill=GridBagConstraints.BOTH;
        gbl.setConstraints(fixedVariablesPanel, gbc);
        leftPanel.add(fixedVariablesPanel);
        gbl.setConstraints(viewPanel, gbc);
        leftPanel.add(viewPanel);
        controlPanel.add(leftPanel);
        controlPanel.add(thresholdsPanel);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        add(stoplightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void doubleClickTableHandler(MouseEvent e)
    {
        int row = stoplightChart.getSelectedRow();
        int column = stoplightChart.getSelectedColumn();

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

        if (column == 0)
        {
            Object value = stoplightTableModel.getValueAt(row, 0);
            ((VariableModel)variableManager.
                getDescriptors(VariableModel.Y_AXIS).nextElement()).
                getSelector().setSelectedItem(value);

            return;
        }

        // Launch line plot chart preconfigured based on double-clicked cell
        JFrame myFrame = findJFrame();
        JFrame linePlotFrame;
        if (myFrame instanceof CDesktopFrame)
        {
            linePlotFrame = myFrame;
        }
        else
        {
            linePlotFrame = new CFrame("Line Plot Chart", plaf);
        }
        LinePlotPanel lpp = new LinePlotPanel(plaf, useMenuButtons);
        VariableInterfaceManager linePlotVIM =
            lpp.getVariableInterfaceManager();

        linePlotVIM.getDescriptor("Metric").setValue("Stoplight Components");
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
            lpDesc.setValue(new RangeModel(Math.max(0, selectedTime - 10),
                                           selectedTime + 10));
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
            cfc.createInnerFrame("Lineplot View" + (plaf?" (PLAF)":""), lpp);
        }
        else
        {
            lpp.install(linePlotFrame);
            linePlotFrame.setVisible(true);
        }
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
        CFrame frame = new CFrame("Stoplight Chart", plaf);
        StoplightPanel slp = new StoplightPanel(plaf);
        slp.getVariableInterfaceManager().getDescriptor("Metric").
            setValue("Supply as Proportion of Demand");
        slp.install(frame);
        frame.setVisible(true);
    }
}