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
import org.cougaar.lib.uiframework.ui.components.CRowHeaderTable;
import org.cougaar.lib.uiframework.ui.components.CTreeButton;
import org.cougaar.lib.uiframework.ui.models.DatabaseTableModel;
import org.cougaar.lib.uiframework.ui.models.VariableModel;
import org.cougaar.lib.uiframework.ui.util.TableSorter;
import org.cougaar.lib.uiframework.ui.util.VariableInterfaceManager;

/**
 * Panel that contains a line plot chart and controls for selecting what
 * data is plotted.  Controls pull and aggregate two dimensional cross
 * sections of data out of a database that holds a 4 dimensional data table.
 * The dimensions are: Item, Organization, Metric, and Time.
 */
public class LinePlotPanel extends JPanel
{
    private DatabaseTableModel databaseTableModel = new DatabaseTableModel();
    private CLinePlotChart chart = new CLinePlotChart(databaseTableModel);
    private final static int spacing = 5;
    private VariableInterfaceManager variableManager;
    private TitledBorder titledBorder =
        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                       "Line Plot Chart", TitledBorder.CENTER,
                                       TitledBorder.DEFAULT_POSITION,
                                       MetalLookAndFeel.getWindowTitleFont());

    /**
     * Creates new line plot panel in given frame.
     *
     * @param the frame in which panel will reside
     * @param plaf true if pluggable look and feel must be supported
     */
    public LinePlotPanel(boolean plaf)
    {
        super(new BorderLayout());

        createComponents(plaf);
    }

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
     *
     * @param plaf true if pluggable look and feel must be supported
     */
    private void createComponents(boolean plaf)
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

        VariableModel[] variables =
        {
            new VariableModel("Metric",
                                   /*new JComboBox(TestDataSource.metrics)*/
                                   metricTreeButton, true,
                                   VariableModel.Y_AXIS, true, 0),
            new VariableModel("Time",
                                   new CRangeButton("C+", 0, 30, plaf),
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
        final QueryGenerator qg = new QueryGenerator(databaseTableModel);
        variableManager = new VariableInterfaceManager(variables);
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

        // Fixed Variables
        JPanel fixedVariablesPanel = new JPanel(new FlowLayout());
        fixedVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Fixed Parameters"));
        fixedVariablesPanel.add(
            variableManager.getDescriptor("Item").getControl());
        fixedVariablesPanel.add(Box.createHorizontalStrut(spacing * 2));
        fixedVariablesPanel.add(
            variableManager.getDescriptor("Org").getControl());

        // Independent Variables
        JPanel independentVariablesPanel = new JPanel(new FlowLayout());
        independentVariablesPanel.setBorder(
            BorderFactory.createTitledBorder("Independent Parameter"));
        //independentVariablesPanel.add(((VariableModel)variableManager.
        //                             getDescriptors(VariableModel.X_AXIS).
        //                             nextElement()).getControl());
        //independentVariablesPanel.add(Box.createHorizontalStrut(spacing * 2));
        independentVariablesPanel.add(((VariableModel)variableManager.
                                     getDescriptors(VariableModel.Y_AXIS).
                                     nextElement()).getControl());

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
        xAxisPanel.add(((VariableModel)variableManager.
            getDescriptors(VariableModel.X_AXIS).nextElement()).
            getControl(), BorderLayout.SOUTH);
        linePlotPanel.setBorder(titledBorder);
        linePlotPanel.add(chart, BorderLayout.CENTER);
        linePlotPanel.add(xAxisPanel, BorderLayout.SOUTH);

        // table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        TableSorter sorter = new TableSorter(databaseTableModel);
        JTable table = new CRowHeaderTable(sorter);
        sorter.addMouseListenerToHeaderInTable(table);
        JScrollPane scrolledTable = new JScrollPane(table);
        tablePanel.add(scrolledTable, BorderLayout.CENTER);
        tablePanel.setMinimumSize(new Dimension(0, 0));

        // high level layout
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(fixedVariablesPanel, BorderLayout.WEST);
        controlPanel.add(independentVariablesPanel, BorderLayout.CENTER);
        controlPanel.add(featureSelectionControl, BorderLayout.EAST);
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
        CFrame frame = new CFrame("Line Plot Chart", false);
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