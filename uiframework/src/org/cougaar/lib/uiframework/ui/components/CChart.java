/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import javax.swing.plaf.metal.MetalLookAndFeel;

import javax.swing.*;
import javax.swing.event.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.cougaar.lib.uiframework.ui.components.graph.*;

import org.cougaar.lib.uiframework.ui.components.mthumbslider.*;
import org.cougaar.lib.uiframework.ui.components.*;
import org.cougaar.lib.uiframework.ui.models.*;

public class CChart extends javax.swing.JPanel implements ColorProducer, PropertyChangeListener, PlotColors
{
  private PointViewGraph2D graph = new PointViewGraph2D();
  private Axis    xaxis = null;
  private Axis    yaxisLeft = null;
  private Axis    yaxisRight = null;

  private double[] xMinMax = {0.0, 0.0};
  private double[] yMinMax = {0.0, 0.0};

  private CMThumbSliderRangeControl xRC = null;
  private CMThumbSliderRangeControl yRC = null;

  private boolean[] xRangeScrollLock = new boolean[] {false};
  private double[] xScrollSize = new double[] {0.0};

  private boolean[] yRangeScrollLock = new boolean[] {false};
  private double[] yScrollSize = new double[] {0.0};

  private int lastColor = 0;
  private Color[] plotColors = null;

  private boolean autoYRange = false;

//  private final static Color[] defaultColors = {Color.red, Color.blue, Color.green, Color.yellow, Color.orange, Color.cyan, Color.magenta, Color.pink};
//  private final static Color defaultGridColor = Color.red;

  private Color xDividerColor = Color.lightGray;
  private final static Color defaultGridColor = Color.lightGray;
  private final static Color[] defaultColors = {midnightBlue, darkGreen, darkYellow, rust, darkPurple, orange, red};

//  private JLabel title = null;
  private JPanel title = null;
  private String name = null;

  public CChart(String chartTitle, String xLabel, String yLabel, boolean timeAxis)
  {
    this(chartTitle, new JPanel(new BorderLayout()), xLabel, yLabel, timeAxis);
    title.add(new JLabel(chartTitle, SwingConstants.CENTER), BorderLayout.CENTER);
  }

  public CChart(String chartName, JPanel chartTitle, String xLabel, String yLabel, boolean timeAxis)
  {
    title = chartTitle;
    name = chartName;

    setUpXRangeScroller(timeAxis);
    setUpYRangeScroller();


    xaxis.setTitleText(xLabel);
    xaxis.sigDigitDisplay = 1;
    xaxis.exponentDisplayThreshold = 7;
    xaxis.setManualRange(true);

    yaxisLeft.setTitleText(yLabel);
    yaxisLeft.sigDigitDisplay = 1;
    yaxisLeft.exponentDisplayThreshold = 7;
    yaxisLeft.setManualRange(true);

    yaxisRight.setTitleText(yLabel);
    yaxisRight.sigDigitDisplay = 1;
    yaxisRight.exponentDisplayThreshold = 7;
    yaxisRight.setManualRange(true);



    // Set up the graph and x/y axis
    graph.gridOnTop = false;
    graph.attachAxis(xaxis);
    graph.attachAxis(yaxisLeft);
    graph.attachAxis(yaxisRight);
//    graph.borderTop          = 10;
//    graph.borderBottom       = 10;
//    graph.borderLeft         = 10;
//    graph.borderRight        = 10;



    setLayout(new BorderLayout());
    add(title, BorderLayout.NORTH);
    add(graph, BorderLayout.CENTER);
    add(yRC, BorderLayout.WEST);
    add(xRC, BorderLayout.SOUTH);

    // Make sure that the UI L&F/Themes are set up
    doUIUpdate();
  }

  public void setYAxisLabel(String label)
  {
    yaxisLeft.setTitleText(label);
    yaxisRight.setTitleText(label);
  }

  private void setUpXRangeScroller(boolean timeAxis)
  {
    if (timeAxis)
    {
      xaxis = new TimeAndDateAxis();
      xRC = new CMThumbSliderDateAndTimeRangeControl((float)xMinMax[0], (float)(xMinMax[1]));
      xRC.setRange(new RangeModel((float)xMinMax[0], (float)(xMinMax[1])));
    }
    else
    {
      xaxis = new Axis(Axis.BOTTOM);
      xRC = new CMThumbSliderRangeControl((float)xMinMax[0], (float)xMinMax[1]);
      xRC.setRange(new RangeModel((float)xMinMax[0], (float)xMinMax[1]));
    }
    xRC.setDynamicLabelsVisible(false);
    xRC.addPropertyChangeListener("range", new RangeChangeListener(xRC, xMinMax, new Axis[] {xaxis}, xScrollSize, xRangeScrollLock));

    xRC.getSlider().setOrientation(CMThumbSlider.HORIZONTAL);
  }

  private void setUpYRangeScroller()
  {
    yaxisLeft = new Axis(Axis.LEFT);
    yaxisRight = new Axis(Axis.RIGHT);

    yRC = new CMThumbSliderRangeControl((float)yMinMax[0], (float)yMinMax[1]);
    yRC.setRange(new RangeModel((float)yMinMax[0], (float)yMinMax[1]));

    yRC.setDynamicLabelsVisible(false);
    yRC.addPropertyChangeListener("range", new RangeChangeListener(yRC, yMinMax, new Axis[] {yaxisLeft, yaxisRight}, yScrollSize, yRangeScrollLock));

    yRC.getSlider().setOrientation(CMThumbSlider.VERTICAL);
  }

  private class RangeChangeListener implements PropertyChangeListener
  {
    private CMThumbSliderRangeControl rC = null;
    private double[] minMax = null;
    private Axis[] axisList = null;
    private double[] scrollSize = null;
    private boolean[] scrollLock = null;

    public RangeChangeListener(CMThumbSliderRangeControl rC, double[] minMax, Axis[] axisList, double[] scrollSize, boolean[] scrollLock)
    {
      RangeChangeListener.this.rC = rC;
      RangeChangeListener.this.minMax = minMax;
      RangeChangeListener.this.axisList = axisList;
      RangeChangeListener.this.scrollSize = scrollSize;
      RangeChangeListener.this.scrollLock = scrollLock;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
      rangeChanged(rC, minMax, axisList, scrollSize[0], scrollLock[0], RangeChangeListener.this);

      // A functionality hack
      if (rC == xRC)
      {
        recalculateAutoYRange();
      }
    }
  }

  // Must be called when datasets are set visible/not visible (currently no automatic hook available)
  public void recalculateAutoYRange()
  {
    if (autoYRange)
    {
      double yMax = graph.getYmaxInRange(xMinMax[0], xMinMax[1]);
      if (Double.isNaN(yMax))
      {
        yRC.setRange(new RangeModel(0.0f, 1.0f));
      }
      else
      {
        yRC.setRange(new RangeModel(0.0f, (float)(yMax + yMax*0.10)));
      }
    }
  }

  public String getName()
  {
    return(name);
  }

  public void setAutoYRange(boolean value)
  {
    autoYRange = value;
    recalculateAutoYRange();
  }

  public void setDataTipLabel(JLabel label)
  {
    graph.setDataTipLabel(label);
  }

  public void setToolTipDelay(int delay)
  {
    graph.setToolTipDelay(delay);
  }

  public void setXDividers(double x1, double x2)
  {
    if (xaxis.dividers == null)
    {
      xaxis.dividers = new Object[2][2];
    }

    xaxis.dividers[0][0] = new Double(x1);
    xaxis.dividers[0][1] = xDividerColor;

    xaxis.dividers[1][0] = new Double(x2);
    xaxis.dividers[1][1] = xDividerColor;

    graph.repaint();
  }

  public void setShowTitle(boolean show)
  {
    remove(title);

    if (show)
    {
      add(title, BorderLayout.NORTH);
    }

    validate();
  }

  public void setShowXDividers(boolean value)
  {
    xaxis.showDividers = value;
    graph.repaint();
  }

  public void setXMinorTicMarks(int count)
  {
    xaxis.minor_tic_count = count;
    graph.repaint();
  }

  public void setXDividerColor(Color color)
  {
    xDividerColor = color;
    if (xaxis.dividers != null)
    {
      xaxis.dividers[0][1] = xDividerColor;

      xaxis.dividers[1][1] = xDividerColor;

      graph.repaint();
    }
  }

  public void setSuggestedMaxPointDist2(double value)
  {
    graph.suggestedMaxPointDist2 = value;
  }

  public DataSet getClosestDataSet(int x, int y)
  {
    return((DataSet)graph.getClosestPoint(x, y)[1]);
  }

  public void setShowGrid(boolean value)
  {
    graph.drawgrid = value;
    graph.repaint();
  }

  public void setGridOnTop(boolean value)
  {
    graph.gridOnTop = value;
    graph.repaint();
  }

  public void setShowDataTips(boolean value)
  {
    graph.setShowDataTips(value);
  }

  public void setShowLeftYAxis(boolean value)
  {
    yaxisLeft.visible = value;
    graph.repaint();
  }

  public void setShowRightYAxis(boolean value)
  {
    yaxisRight.visible = value;
    graph.repaint();
  }

  public void setShowXRangeScroller(boolean value)
  {
    if (value)
    {
      add(xRC, BorderLayout.SOUTH);
      validate();
    }
    else
    {
      remove(xRC);
      validate();
    }
  }

  public void setShowXRangeTickLabels(boolean value)
  {
    xRC.setDrawTickLabels(value);
  }

  public void setXRangeScrollLock(boolean value)
  {
    xRangeScrollLock[0] = value;

    if (xRangeScrollLock[0])
    {
      xScrollSize[0] = xRC.getRange().getFMax() - xRC.getRange().getFMin();
    }
  }

  public void setShowYRangeScroller(boolean value)
  {
    if (value)
    {
      add(yRC, BorderLayout.WEST);
      validate();
    }
    else
    {
      remove(yRC);
      validate();
    }
  }

  public void setShowYRangeTickLabels(boolean value)
  {
    yRC.setDrawTickLabels(value);
  }

  public void setYRangeScrollLock(boolean value)
  {
    yRangeScrollLock[0] = value;

    if (yRangeScrollLock[0])
    {
      yScrollSize[0] = yRC.getRange().getFMax() - yRC.getRange().getFMin();
    }
  }

  public void setUseCDate(boolean value)
  {
    if (xaxis instanceof TimeAndDateAxis)
    {
      ((TimeAndDateAxis)xaxis).useCDate = value;
      ((CMThumbSliderDateAndTimeRangeControl)xRC).setUseCDate(value);

      graph.repaint();
    }
  }

  public void attachDataSet(DataSet dataSet)
  {
    if (dataSet instanceof StackableBarDataSet)
    {
      // Attach the data set to the graph and axes
      ((StackableBarDataSet)dataSet).reAttachBarBataSets(graph, xaxis, yaxisLeft, yaxisRight, this);
      ((StackableBarDataSet)dataSet).resetDataSetColors();
    }
    else
    {
      if (dataSet.automaticallySetColor)
      {
        dataSet.linecolor = getColor(dataSet.linecolor, dataSet.colorNumber);
      }

      // Attach the data set to the graph and axes
      graph.attachDataSet(dataSet);
      xaxis.attachDataSet(dataSet);
      yaxisLeft.attachDataSet(dataSet);
      yaxisRight.attachDataSet(dataSet);

    }

    recalculateAutoYRange();
    resetYRangeScroller();
  }

  public void detachAllDataSets()
  {
    lastColor = 0;

    graph.detachDataSets();
    xaxis.detachAll();
    yaxisLeft.detachAll();
    yaxisRight.detachAll();
  }

  public void setXAxisExponentDisplayThreshold(int numDigits)
  {
    xaxis.exponentDisplayThreshold = numDigits;
  }

  public void setYAxisExponentDisplayThreshold(int numDigits)
  {
    yaxisLeft.exponentDisplayThreshold = numDigits;
    yaxisRight.exponentDisplayThreshold = numDigits;
  }

  public void setXAxisSigDigitDisplay(int numDigits)
  {
    xaxis.sigDigitDisplay = numDigits;
  }

  public void setYAxisSigDigitDisplay(int numDigits)
  {
    yaxisLeft.sigDigitDisplay = numDigits;
    yaxisRight.sigDigitDisplay = numDigits;
  }

  public DataSet[] getDataSets()
  {
    return(graph.getDataSetList());
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    graph.repaint();
  }

  public RangeModel getXScrollerRange()
  {
    return(xRC.getRange());
  }

  public RangeModel getYScrollerRange()
  {
    return(yRC.getRange());
  }

  public void setXScrollerRange(RangeModel range)
  {
    xRC.setRange(range);
  }

  public void setYScrollerRange(RangeModel range)
  {
    yRC.setRange(range);
  }

  public RangeModel getTotalXRange()
  {
    return(new RangeModel((float)graph.getXmin(), (float)graph.getXmax()));
  }

  public RangeModel getTotalYRange()
  {
    return(new RangeModel((float)graph.getYmin(), (float)graph.getYmax()));
  }

  public void resetTotalRange()
  {
    xRC.setSliderRange((float)graph.getXmin(), (float)graph.getXmax());
//    yRC.setSliderRange((float)graph.getYmin(), (float)graph.getYmax());
    yRC.setSliderRange(0.0f, (float)graph.getYmax() + (float)(graph.getYmax()*0.10));
  }

  public void resetYRangeScroller()
  {
    double yMax = graph.getYmaxInRange(graph.getXmin(), graph.getXmax());
    if (Double.isNaN(yMax))
    {
      yRC.setSliderRange(0.0f, 1.0f);
      yRC.setRange(new RangeModel(0.0f, 1.0f));
    }
    else
    {
      yRC.setSliderRange(0.0f, (float)(yMax + yMax*0.10));
      yRC.setRange(new RangeModel(0.0f, (float)(yMax + yMax*0.10)));
    }
  }

  public void resetTotalXRange(float min, float max)
  {
    xRC.setSliderRange(min, max);
  }

  public void resetTotalYRange(float min, float max)
  {
    yRC.setSliderRange(min, max);
  }

  public void resetRange()
  {
//    xRC.setRange(new RangeModel((int)graph.getXmin(), (int)graph.getXmax()));
    xRC.setRange(new RangeModel((float)xRC.getMinValue(), (float)xRC.getMaxValue()));
//    yRC.setRange(new RangeModel((int)graph.getYmin(), (int)graph.getYmax()));
//    yRC.setRange(new RangeModel(0, (int)graph.getYmax()));
    yRC.setRange(new RangeModel((float)yRC.getMinValue(), (float)yRC.getMaxValue()));
  }

  public StackableBarDataSet createStackableBarDataSet(double width)
  {
    StackableBarDataSet dataSet = new StackableBarDataSet(graph, xaxis, yaxisLeft, yaxisRight, width, this);

    return(dataSet);
  }

  public void setTitle(String tileString)
  {
    title.removeAll();
    title.add(new JLabel(tileString, SwingConstants.CENTER), BorderLayout.CENTER);
  }

  public void setBaseTime(long time)
  {
    if (xaxis instanceof TimeAndDateAxis)
    {
      ((TimeAndDateAxis)xaxis).baseTime = time;
      ((CMThumbSliderDateAndTimeRangeControl)xRC).setBaseTime(time);
    }
  }

  public void setTimeScale(long scale)
  {
    if (xaxis instanceof TimeAndDateAxis)
    {
      ((TimeAndDateAxis)xaxis).timeScale = scale;
      ((CMThumbSliderDateAndTimeRangeControl)xRC).setTimeScale(scale);
    }
  }

  public void setCDate(long date)
  {
    if (xaxis instanceof TimeAndDateAxis)
    {
      ((TimeAndDateAxis)xaxis).cDate = date;
      ((CMThumbSliderDateAndTimeRangeControl)xRC).setCDate(date);
      graph.repaint();
    }
  }

  public void updateUI()
  {
    super.updateUI();

    doUIUpdate();
  }

  public void doUIUpdate()
  {
    if (graph == null)
    {
      return;
    }

    if (UIManager.get("Graph.dataSeries1") == null)
    {
        plotColors = defaultColors;
    }
    else
    {
      Vector colorList = new Vector();
      int i = 1;
      Object nextResource = null;
      while ((nextResource = UIManager.get("Graph.dataSeries" + i++)) != null)
      {
          colorList.add(nextResource);
      }

      plotColors = (Color[])colorList.toArray(new Color[colorList.size()]);
    }

    Color backgroundColor = (Color)MetalLookAndFeel.getControl();
    Color foregroundColor = (Color)MetalLookAndFeel.getControlTextColor();
    Font controlTextFont = MetalLookAndFeel.getControlTextFont();
    Font subControlTextFont = MetalLookAndFeel.getSubTextFont();

    graph.setGraphBackground(backgroundColor);
    graph.setDataBackground(backgroundColor);
    graph.framecolor = foregroundColor;
    if (UIManager.get("Graph.grid") != null)
    {
      graph.gridcolor = (Color)UIManager.get("Graph.grid");
      setXDividerColor(graph.gridcolor);
    }
    else
    {
      graph.gridcolor = defaultGridColor;
      setXDividerColor(defaultGridColor);
    }

    xaxis.setLabelFont(subControlTextFont);
    xaxis.setTitleFont(controlTextFont);
    xaxis.setTitleColor(foregroundColor);
    xaxis.axiscolor = foregroundColor;

    yaxisLeft.setLabelFont(subControlTextFont);
    yaxisLeft.setTitleFont(controlTextFont);
    yaxisLeft.setTitleColor(foregroundColor);
    yaxisLeft.axiscolor = foregroundColor;

    yaxisRight.setLabelFont(subControlTextFont);
    yaxisRight.setTitleFont(controlTextFont);
    yaxisRight.setTitleColor(foregroundColor);
    yaxisRight.axiscolor = foregroundColor;

    resetDataSetColors();

    // Update the sliders in case they are not visible (added to the layout)
    xRC.getSlider().updateUI();
    yRC.getSlider().updateUI();
  }

  private void resetDataSetColors()
  {
    lastColor = 0;

    if (graph != null)
    {
      DataSet[] dataSetList = graph.getDataSetList();
      for (int i=0; i<dataSetList.length; i++)
      {
        if (dataSetList[i] instanceof StackableBarDataSet)
        {
          ((StackableBarDataSet)dataSetList[i]).resetDataSetColors();
        }
        else
        {
          if (dataSetList[i].automaticallySetColor)
          {
            dataSetList[i].linecolor = getColor(dataSetList[i].linecolor, dataSetList[i].colorNumber);
          }
        }
      }
    }
  }

  public Color getColor(Color currentColor, int colorNumber)
  {
    if ((plotColors != null) && (colorNumber > -1) && (colorNumber < plotColors.length))
    {
      currentColor = plotColors[colorNumber];
    }
    else if (plotColors != null)
    {
      currentColor = plotColors[lastColor%plotColors.length];
      lastColor = (lastColor%plotColors.length == 0) ? 1 : lastColor +1;
    }

    return(currentColor);
  }

  private void rangeChanged(CMThumbSliderRangeControl rC, double[] minMax, Axis[] axisList, double scrollSize, boolean scrollLock, PropertyChangeListener listener)
  {
    double currentMin = rC.getRange().getFMin();
    double currentMax = rC.getRange().getFMax();

    if ((currentMin == currentMax) || ((minMax[0] == currentMin) && (minMax[1] == currentMax)))
    {
      return;
    }

    if (scrollLock)
    {
      rC.removePropertyChangeListener("range", listener);
      if (minMax[0] != currentMin)
      {
        rC.setRange(new RangeModel((float)currentMin, (float)(currentMin + scrollSize)));
      }
      else if (minMax[1] != currentMax)
      {
        rC.setRange(new RangeModel((float)(currentMax - scrollSize), (float)currentMax));
      }
      else
      {
        rC.setRange(new RangeModel((float)minMax[0], (float)minMax[1]));
      }
      rC.addPropertyChangeListener("range", listener);
    }

    minMax[0] = rC.getRange().getFMin();
    minMax[1] = rC.getRange().getFMax();

    // Must set min and max
    for (int i=0; i<axisList.length; i++)
    {
      axisList[i].minimum = minMax[0];
      axisList[i].maximum = minMax[1];
    }

    graph.repaint();
  }
  
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter()
    {
      public void windowClosed(WindowEvent e)
      {
        System.exit(0);
      }
    });

    frame.getContentPane().setLayout(new BorderLayout());
    CChart chart = new CChart("chartTitle", "xLabel", "yLabel", false);
    frame.getContentPane().add(chart, BorderLayout.CENTER);
    
    double[] data = new double[10*2];
    data[0] = 0.0;
    data[1] = 1.3;
    for (int i=2; i<data.length; i+=2)
    {
      data[i] = (double)i;
      data[i+1] = (double)i/(double)data.length/2.0;
    }

    try
    {
      DataSet dataSet = new PolygonFillableDataSet(data, data.length/2, false);
      chart.attachDataSet(dataSet);
      chart.resetTotalRange();
      chart.resetRange();

      chart.setXAxisSigDigitDisplay(2);
      chart.setYAxisSigDigitDisplay(2);
      chart.setXAxisExponentDisplayThreshold(0);
      chart.setYAxisExponentDisplayThreshold(0);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
    
    frame.setSize(400, 400);
    frame.show();
  }
}
