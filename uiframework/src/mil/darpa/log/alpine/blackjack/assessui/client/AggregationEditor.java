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
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.*;

public class AggregationEditor extends JDialog
{
    private JList metricList;
    private JComboBox orgSelector;
    private JComboBox timeSelector;
    private JComboBox itemSelector;
    private Hashtable localAggregationSchemes = null;
    private static final int spacing = 10;
    private int closeAction = JOptionPane.CANCEL_OPTION;

    private AggregationEditor(Frame owner, final String selectedMetric)
    {
        super(owner, true);
        setTitle("Aggregation Editor");
        setSize(500, 250);
        localAggregationSchemes =
            copyAggregationSchemes(DBInterface.aggregationSchemes);
        createComponents();
        setLocationRelativeTo(owner);
        SwingUtilities.invokeLater(new Runnable() {
                public void run()
                {
                    setMetric(selectedMetric);
                }
            });
        show();
    }

    public static int showDialog(Frame owner, String selectedMetric)
    {
        AggregationEditor ae = new AggregationEditor(owner, selectedMetric);
        return ae.getCloseAction();
    }

    public int getCloseAction()
    {
        return closeAction;
    }

    private Hashtable copyAggregationSchemes(Hashtable orgAggSchemes)
    {
        Hashtable newAggSchemes = new Hashtable();
        Enumeration keys = DBInterface.aggregationSchemes.keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            AggregationScheme orgAS = (AggregationScheme)orgAggSchemes.get(key);
            AggregationScheme newAS =
                new AggregationScheme(orgAS.orgAggregation,
                                      orgAS.timeAggregation,
                                      orgAS.itemAggregation);
            newAggSchemes.put(key, newAS);
        }

        return newAggSchemes;
    }

    private void setMetric(String selectedMetric)
    {
        metricList.setSelectedValue(selectedMetric, true);
    }

    private void createComponents()
    {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // Create JList of aggregation scheme keys
        metricList = new JList(DBInterface.aggregationSchemeLabels);
        metricList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sml = new JScrollPane(metricList);

        // Create aggregation scheme editor panel
        Object[] orgAggSelection =
            {AggregationScheme.getLabelString(AggregationScheme.SUM),
             AggregationScheme.getLabelString(AggregationScheme.MIN),
             AggregationScheme.getLabelString(AggregationScheme.MAX),
             AggregationScheme.getLabelString(AggregationScheme.AVG)};
        Object[] timeAggSelection =
            {AggregationScheme.getLabelString(AggregationScheme.SUM),
             AggregationScheme.getLabelString(AggregationScheme.MIN),
             AggregationScheme.getLabelString(AggregationScheme.MAX),
             AggregationScheme.getLabelString(AggregationScheme.AVG),
             AggregationScheme.getLabelString(AggregationScheme.FONE)};
        Object[] itemAggSelection=
            {AggregationScheme.getLabelString(AggregationScheme.NONE),
             AggregationScheme.getLabelString(AggregationScheme.SUM),
             AggregationScheme.getLabelString(AggregationScheme.MIN),
             AggregationScheme.getLabelString(AggregationScheme.MAX),
             AggregationScheme.getLabelString(AggregationScheme.AVG),
             AggregationScheme.getLabelString(AggregationScheme.FONE),
             AggregationScheme.getLabelString(AggregationScheme.WAVG)};
        JPanel aggPanel = new JPanel();
        aggPanel.setBorder(
            BorderFactory.createTitledBorder("Metric's Aggregation Scheme"));
        aggPanel.setLayout(new BoxLayout(aggPanel, BoxLayout.Y_AXIS));
        Box orgBox = new Box(BoxLayout.X_AXIS);
        JLabel orgLabel = new JLabel("Organization: ", JLabel.RIGHT);
        orgBox.add(orgLabel);
        orgBox.add(orgSelector = new JComboBox(orgAggSelection));
        aggPanel.add(Box.createVerticalStrut(spacing));
        aggPanel.add(orgBox);
        Dimension labelSize = orgLabel.getPreferredSize();
        Box timeBox = new Box(BoxLayout.X_AXIS);
        JLabel timeLabel = new JLabel("Time: ", JLabel.RIGHT);
        timeLabel.setPreferredSize(labelSize);
        timeBox.add(timeLabel);
        timeBox.add(timeSelector = new JComboBox(timeAggSelection));
        aggPanel.add(Box.createVerticalStrut(spacing));
        aggPanel.add(timeBox);
        Box itemBox = new Box(BoxLayout.X_AXIS);
        JLabel itemLabel = new JLabel("Item: ", JLabel.RIGHT);
        itemLabel.setPreferredSize(labelSize);
        itemBox.add(itemLabel);
        itemBox.add(itemSelector = new JComboBox(itemAggSelection));
        aggPanel.add(Box.createVerticalStrut(spacing));
        aggPanel.add(itemBox);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(
          BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing));
        JButton resetButton = new JButton("Reset to defaults");
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createGlue());
        JButton okButton = new JButton("OK");
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(spacing));
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        // High Level Layout
        c.add(sml, BorderLayout.CENTER);
        c.add(aggPanel, BorderLayout.EAST);
        c.add(buttonPanel, BorderLayout.SOUTH);

        // Event Handling
        metricList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e)
                {
                    updateAggregationScheme();
                }
            });

        orgSelector.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e)
                {
                    AggregationScheme as = getSelectedScheme();
                    as.orgAggregation = as.getAggregationMethod(
                        (String)orgSelector.getSelectedItem());
                }
            });

        timeSelector.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e)
                {
                    AggregationScheme as = getSelectedScheme();
                    as.timeAggregation = as.getAggregationMethod(
                        (String)timeSelector.getSelectedItem());
                }
            });

        itemSelector.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e)
                {
                    AggregationScheme as = getSelectedScheme();
                    as.itemAggregation = as.getAggregationMethod(
                        (String)itemSelector.getSelectedItem());
                }
            });

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    DBInterface.aggregationSchemes =
                        copyAggregationSchemes(localAggregationSchemes);
                    closeAction = JOptionPane.OK_OPTION;
                    dispose();
                }
            });

        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    closeAction = JOptionPane.CANCEL_OPTION;
                    dispose();
                }
            });

        resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    localAggregationSchemes =
                        DBInterface.createDefaultAggSchemes();
                    updateAggregationScheme();
                }
            });
    }

    private AggregationScheme getSelectedScheme()
    {
        return (AggregationScheme)
            localAggregationSchemes.get(metricList.getSelectedValue());
    }

    private void updateAggregationScheme()
    {
        AggregationScheme as = getSelectedScheme();
        orgSelector.setSelectedItem(as.getLabelString(as.orgAggregation));
        timeSelector.setSelectedItem(as.getLabelString(as.timeAggregation));
        itemSelector.setSelectedItem(as.getLabelString(as.itemAggregation));

        String metric = (String)metricList.getSelectedValue();

        boolean unitlessMetric =
            MetricInfo.metricUnits.get(metric).equals(MetricInfo.UNITLESS);
        itemSelector.setEnabled(unitlessMetric);
    }

    /**
     * main for unit testing
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        if (AggregationEditor.showDialog(frame, "Demand") ==
            JOptionPane.OK_OPTION)
        {
            System.out.println("OK Selected");
        }
    }
}