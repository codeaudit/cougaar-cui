package org.cougaar.lib.uiframework.ui.components;

import java.io.File;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.*;

import org.cougaar.lib.uiframework.ui.components.graph.Axis;
import org.cougaar.lib.uiframework.ui.components.graph.DataSet;
import org.cougaar.lib.uiframework.ui.components.graph.Graph2D;
import org.cougaar.lib.uiframework.ui.components.graph.Markers;
import org.cougaar.lib.uiframework.ui.models.TestFunctionTableModel;

/**
 * Line plot chart that can get it's data from a table model.  Each row of the
 * table model is graphed as a line.
 */
public class CLinePlotChart extends Graph2D
{
    private TableModel tm;
    private Axis    xaxis;
    private Axis    yaxis_left;
    private Axis    yaxis_right;
    private int plotCount = 0;
    private Object[] plotColor;
    private final static Object[] defaultColors = {Color.red, Color.blue,
                                                   Color.black, Color.orange,
                                                   Color.magenta, Color.gray};

    /**
     * Default constructor.  Creates empty line chart
     */
    public CLinePlotChart()
    {
        super();
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
        super();
        this.tm = tm;

        init();
    }

    /**
     * Get a reference to the x axis.
     *
     * @return a reference to the x axis.
     */
    public Axis getXAxis()
    {
        return xaxis;
    }

    /**
     * When look and feel or theme is changed, this method is called.  It sets
     * the graph color and font scheme based on metal L&F properties.
     */
    public void updateUI()
    {
        super.updateUI();

        setDataBackground(MetalLookAndFeel.getControl());

        if (UIManager.get("Graph.dataSeries1") == null)
        {
            plotColor = defaultColors;
        }
        else
        {
            Vector plotColors = new Vector();
            int i = 1;
            Object nextResource = null;
            while ((nextResource =
                    UIManager.get("Graph.dataSeries" + i++)) != null)
            {
                plotColors.add(nextResource);
            }
            plotColor = plotColors.toArray();
        }

        Color color;
        if ((color = (Color)UIManager.get("Graph.grid")) != null)
            gridcolor = color;

        if (xaxis != null)
        {
            xaxis.setLabelFont(MetalLookAndFeel.getSubTextFont()); // tick labels
            xaxis.setTitleFont(MetalLookAndFeel.getControlTextFont());
        }

        if (yaxis_left != null)
        {
            yaxis_left.setLabelFont(MetalLookAndFeel.getSubTextFont()); // tick labels
            yaxis_left.setTitleFont(MetalLookAndFeel.getControlTextFont());
        }
    }

    /**
     * Initializes the graph
     */
    private void init()
    {
        URL markerURL;

        /*
        **      Get the passed parameters
        */

        String mfile    = "marker.txt";

        /*
        **      Create the Graph instance and modify the default behaviour
        */
        drawzero = true;
        drawgrid = true;
        borderTop = 10;

        /*
        **      Load a file containing Marker definitions
        */
        try
        {
           markerURL = (new File(mfile)).toURL();
           setMarkers(new Markers(markerURL));
        } catch(Exception e)
        {
            try
            {
                markerURL = new URL("file:///D:/Alpine/Assessment/" + mfile);
                setMarkers(new Markers(markerURL));
            }
            catch (Exception e2)
            {
                System.out.println("Failed to create Marker URL!");
            }
        }

        // create xaxis
        xaxis = createAxis(Axis.BOTTOM);

        // create yaxis
        yaxis_left = createAxis(Axis.LEFT);

        /*
        **      Attach the second data set to the Right Axis
        */
        /*
        yaxis_right = graph.createAxis(Axis.RIGHT);
        yaxis_right.attachDataSet(data2);
        yaxis_right.setTitleText("y=x^3");
        yaxis_right.setTitleFont(new Font("TimesRoman",Font.PLAIN,20));
        yaxis_right.setLabelFont(new Font("Helvetica",Font.PLAIN,15));
        yaxis_right.setTitleColor(new Color(100,100,255) );
        */

        regeneratePlots();

        tm.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e)
                {
                    regeneratePlots();
                }
            });
    }

    /**
     * Associates a control to this graph that is used to select graph features
     *
     * @param gfc control to associate with graph
     */
    public void
       setGraphFeatureSelectionControl(final CGraphFeatureSelectionControl gfc)
    {
        gfc.setShowGrid(drawgrid);

        gfc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    drawgrid = gfc.getShowGrid();
                    revalidate();
                    repaint();
                }
            });
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
        DataSet dataSet = loadDataSet(data,np);

        Color color = (Color)plotColor[(plotCount % plotColor.length)];
        dataSet.linecolor = color;
        dataSet.marker    = (plotCount % 8 ) + 1;
        dataSet.markerscale = markerScale;
        dataSet.markercolor = color;
        dataSet.legend(200,50 + (15 * plotCount), legendText);
        dataSet.legendColor(color);
        dataSet.legendFont(MetalLookAndFeel.getSubTextFont());
        plotCount++;

        /*
        **      Attach all data set to the Xaxis
        */
        xaxis.attachDataSet(dataSet);
        //xaxis.setTitleText("Xaxis");

        /*
        **      Attach all data set to the Left Y Axis
        */
        yaxis_left.attachDataSet(dataSet);
        //yaxis_left.setTitleText("y=6x10{^4}x^2");
    }

    /**
     * Recreates plots based on table model
     */
    private void regeneratePlots()
    {
        detachDataSets();

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
                        (testValue instanceof Double))
                    {
                        break search;
                    }
                }
            }
        }

        plotCount = 0;
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
                    data[pointLocation + 1] =
                        ((Number)tm.getValueAt(row, column)).doubleValue();
                }
                plot(data, numberOfDataPoints, 1,
                     (String)tm.getValueAt(row, 0));
            }
        }
        revalidate();
        repaint();
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