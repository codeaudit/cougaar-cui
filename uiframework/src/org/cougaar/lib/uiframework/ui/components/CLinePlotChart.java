/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components;

import java.io.File;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.*;

import org.cougaar.lib.uiframework.ui.components.graph.Axis;
import org.cougaar.lib.uiframework.ui.components.graph.DataSet;
import org.cougaar.lib.uiframework.ui.components.graph.Graph2D;
import org.cougaar.lib.uiframework.ui.components.graph.Markers;
import org.cougaar.lib.uiframework.ui.models.DatabaseTableModel;
import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.models.TestFunctionTableModel;

/**
 * Line plot chart that can get it's data from a table model.  Each row of the
 * table model is graphed as a line.
 */
public class CLinePlotChart extends CChart
{
    private TableModel tm;
    private CChartLegend legend = null;
    private int plotCount = 0;

    /**
     * Default constructor.  Creates empty line chart
     */
    public CLinePlotChart()
    {
        super("TITLE", null, null, false);
        this.tm = new TestFunctionTableModel();
        init();
    }

    /**
     * Creates line chart that shows graph of data in rows of given table model
     *
     * @param tm table model to use for acquiring data
     */
    public CLinePlotChart(TableModel tm)
    {
        super("TITLE", null, null, false);
        this.tm = tm;

        init();
    }

    /**
     * Associate a legend with this chart
     *
     * @param legend to associate with this chart
     */
    public void setLegend(CChartLegend legend)
    {
        this.legend = legend;

        legend.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    repaint();
                }
            });
    }

    /**
     * When look and feel or theme is changed, this method is called.  It sets
     * the graph color and font scheme based on metal L&F properties.
     */
    public void updateUI()
    {
        super.updateUI();

        regeneratePlots();
    }

    /**
     * Initializes the graph
     */
    private void init()
    {
        tm.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e)
                {
                    SwingUtilities.invokeLater(new Runnable() {
                            public void run()
                            {
                                regeneratePlots();
                            }
                        });
                }
            });

        updateUI();
    }

    /**
     * Plots the data in the data array as a single line on graph.
     *
     * @param data        Array containing the (x,y) data pairs.
     * @param np          The number of (x,y) data points. This means that the
     *                    minimum length of the data array is 2*np.
     * @param markerScale The scaling factor for the marker.
     * @param legendText  The text used to describe this line in the legend
     */
    public void plot(double[] data, int np, float markerScale,
                     String legendText)
    {
        // refuse to plot more than 8 lines
        if (plotCount >= 8) return;

        DataSet dataSet = null;
        try
        {
            dataSet = new DataSet(data,np);
            dataSet.dataGroup = "Legend";
            dataSet.dataName = legendText;
            attachDataSet(dataSet);
            if (legend != null)
                legend.addDataSet(dataSet);
            plotCount++;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Recreates plots based on table model
     */
    private void regeneratePlots()
    {
        if (tm == null) return;

        detachAllDataSets();
        if (legend != null)
            legend.removeAllDataSets();
        plotCount = 0;

        // find start of data (i.e. lose headers)
        int columnStart = 1;
        int rowStart = 0;
        search : {
            for (; rowStart < tm.getRowCount(); rowStart++)
            {
                for (; columnStart < tm.getColumnCount() ; columnStart++)
                {
                    Object testValue = tm.getValueAt(rowStart, columnStart);
                    if ((testValue instanceof Float) ||
                        (testValue instanceof Double) ||
                        (testValue.toString().
                            equals(DatabaseTableModel.NO_VALUE)))
                    {
                        break search;
                    }
                }
            }
        }

        int numberOfDataPoints = tm.getColumnCount() - columnStart;
        if (numberOfDataPoints <= 0) return;
        double[] data = new double[numberOfDataPoints * 2];

        if (tm != null)
        {
            for (int row = rowStart; row < tm.getRowCount(); row++)
            {
                for (int column = columnStart; column < tm.getColumnCount();
                     column++)
                {
                    int pointLocation = (column - columnStart) * 2;
                    try
                    {
                        data[pointLocation] = Double.parseDouble(
                            tm.getColumnName(column).toString());
                    }
                    catch(NumberFormatException e)
                    {
                        data[pointLocation] = column;
                    }
                    Object value = tm.getValueAt(row, column);
                    data[pointLocation + 1] = (value instanceof Number) ?
                        ((Number)value).doubleValue():0;
                }
                plot(data, numberOfDataPoints, 1,
                     tm.getValueAt(row, 0).toString());
            }
        }

        // Must set the total range of the slider
        resetTotalRange();
        resetRange();

        revalidate();
        repaint();
        legend.revalidate();
        legend.repaint();
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new CLinePlotChart(), BorderLayout.CENTER);
        frame.setSize(400, 100);
        frame.setVisible(true);
    }
}