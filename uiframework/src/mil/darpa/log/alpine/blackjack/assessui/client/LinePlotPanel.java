/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package mil.darpa.log.alpine.blackjack.assessui.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;
import javax.swing.table.TableModel;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.components.CGraphFeatureSelectionControl;
import org.cougaar.lib.uiframework.ui.components.CLinePlotChart;
import org.cougaar.lib.uiframework.ui.components.CRangeButton;
import org.cougaar.lib.uiframework.ui.components.CRLabel;
import org.cougaar.lib.uiframework.ui.components.CRowHeaderTable;
import org.cougaar.lib.uiframework.ui.components.CTreeButton;
import org.cougaar.lib.uiframework.ui.models.DatabaseTableModel;
import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.models.VariableModel;
import org.cougaar.lib.uiframework.ui.util.CougaarUI;
import org.cougaar.lib.uiframework.ui.util.TableSorter;
import org.cougaar.lib.uiframework.ui.util.VariableInterfaceManager;

/**
 * Panel that contains a line plot chart and controls for selecting what
 * data is plotted.  Controls pull and aggregate two dimensional cross
 * sections of data out of a database that holds a 4 dimensional data table.
 * The dimensions are: Item, Organization, Metric, and Time.
 */
public class LinePlotPanel extends JPanel implements CougaarUI
{
    private boolean plaf = true;
    private boolean useMenuButtons = true;
    private DatabaseTableModel databaseTableModel = new DatabaseTableModel();
    private CLinePlotChart chart = new CLinePlotChart(databaseTableModel);
    private final static int spacing = 5;
    private VariableInterfaceManager variableManager;
    private TitledBorder titledBorder;
    private QueryGenerator queryGenerator;

    /**
     * Creates new line plot panel in given frame.
     *
     * @param the frame in which panel will reside
     * @param plaf true if pluggable look and feel must be supported
     */
    public LinePlotPanel(boolean plaf)
    {
        super(new BorderLayout());
        this.plaf = plaf;

        createComponents();
    }

    /**
     * Creates new line plot panel in given frame.
     *
     * @param the frame in which panel will reside
     * @param plaf true if pluggable look and feel must be supported
     * @param useMenuButtons true if variable manager should use CMenuButtons
     *                       for variable management; otherwise CComboSelectors
     *                       will be used.
     */
    public LinePlotPanel(boolean plaf, boolean useMenuButtons)
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
    /**
     * When look and feel or theme is changed, this method is called.  It sets
     * the font scheme based on metal L&F properties.
     */
    public void updateUI()
    {
        super.updateUI();

        if (titledBorder != null)
        {
            titledBorder.setTitleFont(MetalLookAndFeel.getWindowTitleFont());
        }
    }

    /**
     * Creates the components of the line plot UI
     */
    private void createComponents()
    {
        DefaultMutableTreeNode root = DBInterface.metricTree;
        CTreeButton metricTreeButton =
            new CTreeButton(root,
                            (DefaultMutableTreeNode)root.getChildAt(0));
        metricTreeButton.setRootVisible(false);
        metricTreeButton.expandFirstLevel();

        //root = DBInterface.createTree(DBInterface.getTableName("item"));
        root = DBInterface.itemTree;
        CTreeButton itemTreeButton = new CTreeButton(root, root);

        //root = DBInterface.createTree(DBInterface.getTableName("org"));
        root = DBInterface.orgTree;
        CTreeButton orgTreeButton = new CTreeButton(root, root);

        CRangeButton rangeButton =
            new CRangeButton("C", DBInterface.minTimeRange,
                             DBInterface.maxTimeRange, plaf);

        // demo kludge
        rangeButton.setSelectedItem(new RangeModel(-15, 50));
        orgTreeButton.setSelectedItem("2-BDE-3ID-HHC");
        //rangeButton.roundAndSetSliderRange(DBInterface.minTimeRange,
        //                                   DBInterface.maxTimeRange);

        VariableModel[] variables =
        {
            new VariableModel("Metric",
                                   /*new JComboBox(TestDataSource.metrics)*/
                                   metricTreeButton, true,
                                   VariableModel.Y_AXIS, true, 0),
            new VariableModel("Time", rangeButton,
                                   false, VariableModel.X_AXIS, true, 0),
            new VariableModel("Item", itemTreeButton, true,
                                   VariableModel.FIXED, true, 0),
            new VariableModel("Org", orgTreeButton, true,
                                   VariableModel.FIXED, true, 0) /*,
            new VariableModel("Location", new JTreeButton(), true),
            new VariableModel("Pat Cond", new JTreeButton(), true) */
        };

        Border emptyBorder =
           BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing);

        // create a new query generator to update databaseTableModel based
        // on (and triggered by) changes to variable controls.
        queryGenerator = new QueryGenerator(databaseTableModel);
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
                    // Special case for metrics control, if control is not a
                    // independent variable, it must be set to a leaf node.
                    // (because we don't support aggregation across different
                    // metrics)
                    fixVariableValue(vm1);
                    fixVariableValue(vm2);

                    updateView();
                }
            });

        // Fixed Variables
        JPanel fixedVariablesPanel = new JPanel();
        fixedVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Fixed Parameters"));
        Box fvBox = new Box(BoxLayout.X_AXIS);
        fvBox.add(variableManager.getDescriptor("Item").getControl());
        fvBox.add(Box.createHorizontalStrut(spacing * 2));
        fvBox.add(variableManager.getDescriptor("Org").getControl());
        fixedVariablesPanel.add(fvBox);

        // Independent Variables
        JPanel independentVariablesPanel = new JPanel();
        independentVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Independent Parameters"));
        Box ivBox = new Box(BoxLayout.X_AXIS);
        ivBox.add(new JLabel("X Axis: "));
        ivBox.add(((VariableModel)variableManager.
                  getDescriptors(VariableModel.X_AXIS).nextElement()).
                  getControl());
        ivBox.add(Box.createHorizontalStrut(spacing * 2));
        ivBox.add(new JLabel("Y Axis: "));
        ivBox.add(((VariableModel)variableManager.
                  getDescriptors(VariableModel.Y_AXIS).nextElement()).
                  getControl());
        independentVariablesPanel.add(ivBox);

        // Graph Feature Selection Control
        CGraphFeatureSelectionControl featureSelectionControl =
            new CGraphFeatureSelectionControl();
        featureSelectionControl.setBorder(
            BorderFactory.createTitledBorder("Graph Features"));
        chart.setGraphFeatureSelectionControl(featureSelectionControl);

        // line plot panel
        //chart.getXAxis().setTitleText("Time (C+)");
        JPanel linePlotPanel = new JPanel(new BorderLayout());
        JPanel xAxisPanel = new JPanel();
        xAxisPanel.add(variableManager.getXAxisLabel());
        //xAxisPanel.add(((VariableModel)variableManager.
        //    getDescriptors(VariableModel.X_AXIS).nextElement()).
        //    getControl(), BorderLayout.SOUTH);
        JPanel yAxisPanel = new JPanel(new GridBagLayout());
        CRLabel yAxisLabel = variableManager.getYAxisLabel();
        yAxisLabel.setOrientation(CRLabel.DOWN_UP);
        yAxisPanel.add(yAxisLabel);
        linePlotPanel.add(chart, BorderLayout.CENTER);
        linePlotPanel.add(yAxisPanel, BorderLayout.WEST);
        linePlotPanel.add(xAxisPanel, BorderLayout.SOUTH);
        titledBorder =
           BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                            variableManager.toString());
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(MetalLookAndFeel.getWindowTitleFont());
        linePlotPanel.setBorder(titledBorder);

        // generate initial query based on initial variable settings
        updateView();

        // table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        TableSorter sorter = new TableSorter(databaseTableModel);
        JTable table = new CRowHeaderTable(sorter);
        sorter.addMouseListenerToHeaderInTable(table);
        JScrollPane scrolledTable = new JScrollPane(table);
        tablePanel.add(scrolledTable, BorderLayout.CENTER);
        tablePanel.setMinimumSize(new Dimension(0, 0));

        // high level layout
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx=1;
        gbc.weighty=1;
        JPanel controlPanel = new JPanel(gbl);
        gbc.fill=GridBagConstraints.BOTH;
        gbl.setConstraints(fixedVariablesPanel, gbc);
        controlPanel.add(fixedVariablesPanel);
        gbl.setConstraints(independentVariablesPanel, gbc);
        controlPanel.add(independentVariablesPanel);
        gbc.weightx=0;
        gbc.weighty=0;
        gbl.setConstraints(featureSelectionControl, gbc);
        controlPanel.add(featureSelectionControl);
        final JSplitPane chartPanel =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           linePlotPanel, tablePanel);
        chartPanel.setOneTouchExpandable(true);
        chartPanel.setDividerLocation(Integer.MAX_VALUE);
        add(chartPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        if (usingJdk13orGreater())
        {
            try
            {
                // Without using reflection, the following line is:
                // chartPanel.setResizeWeight(1);
                // Will not compile under jdk1.2.2 (thus the use of reflection)
                chartPanel.getClass().
                    getMethod("setResizeWeight",
                              new Class[]{double.class}).
                        invoke(chartPanel, new Object[]{new Double(1)});
            }
            catch(Exception e) {e.printStackTrace();}
        }
        else
        {
            // jdk 1.2
            chartPanel.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent e)
                    {
                        chartPanel.setDividerLocation(
                            chartPanel.getSize().height -
                            chartPanel.getDividerSize());
                    }
                });
        }
    }

    private void updateView()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        queryGenerator.generateQuery(variableManager);
        titledBorder.setTitle(variableManager.toString());
        repaint();
        setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Special case for metrics control, if control is not a
     * independent variable, it must be set to a leaf node.
     * (because we don't support aggregation across different metrics)
     */
    private void fixVariableValue(VariableModel vm)
    {
        if (vm.getName().equalsIgnoreCase("Metric") &&
            (vm.getState() == VariableModel.FIXED))
        {
            vm.setValue("Demand");
        }
    }

    /**
     * Needed for compatibility with jdk1.2.2
     */
    private boolean usingJdk13orGreater()
    {
        float versionNumber =
            Float.parseFloat(System.getProperty("java.class.version"));
        return (versionNumber >= 47.0);
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

    private Frame findFrame()
    {
        Container parent = getParent();
        while (!(parent instanceof Frame))
        {
            parent = parent.getParent();
        }
        return (Frame)parent;
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        CFrame frame = new CFrame(UIConstants.LINEPLOT_UI_NAME, false);
        LinePlotPanel lpp = new LinePlotPanel(false);
        VariableInterfaceManager vim = lpp.getVariableInterfaceManager();
        vim.getDescriptor("Org").setValue("23INBN");
        vim.getDescriptor("Item").setValue("Drug");
        vim.getDescriptor("Metric").setValue("Demand");
        vim.setYAxis("Item");

        frame.getContentPane().add(lpp);
        frame.setVisible(true);
    }
}