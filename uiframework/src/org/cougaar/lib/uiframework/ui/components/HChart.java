/* 
 * <copyright>
 *  Copyright 1997-2003 Clark Software Engineering (CSE)
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
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.components.graph.*;
import org.cougaar.lib.uiframework.ui.models.*;

/***********************************************************************************************************************
<b>Description</b>: This class is a histogram chart display component that provides X and Y dual-thumb range sliders to
                    narrow or broaden the view of the data on the chart.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class HChart extends javax.swing.JPanel implements ComponentListener
{
  private CChart chart;
  private double barWidth = 1.0;
  
	/*********************************************************************************************************************
  <b>Description</b>: Constructs a chart with the specified title, X/Y labels and bar width.  The chart is displayed as
                      a graph with X and Y axis range sliders, where the X axis and slider have no tic mark labels.

  <br>
  @param title Title of the chart
  @param xLabel X-Axis label of the chart
  @param yLabel Y-Axis label of the chart
  @param barWidth Width of the data bars drawn
	*********************************************************************************************************************/
  public HChart(String title, String xLabel, String yLabel, double barWidth)
  {
    this.barWidth = barWidth;

    chart = new CChart(title, xLabel, yLabel, false);
    chart.setShowXRangeTickLabels(false);
    chart.setShowXAxisTickLabels(false);
    chart.setXAxisSigDigitDisplay(2);
    chart.setYAxisSigDigitDisplay(2);
    chart.setXAxisExponentDisplayThreshold(0);
    chart.setYAxisExponentDisplayThreshold(0);
    chart.setShowRightYAxis(false);
    
    setLayout(new GridLayout(1,1));
    add(chart);
    
    addComponentListener(this);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Attach a data set to the chart.

  <br><b>Notes</b>:<br>
	                  - Any time a data set is attached, the range and range limits will me maximized automatically

  <br>
  @param dataset Data set to attach to the chart
	*********************************************************************************************************************/
  public void attachDataSet(HistogramDataSet dataset)
  {
    chart.attachDataSet(dataset.getDataSet(barWidth));
    dataset.chart = this;

    RangeModel range = chart.getTotalYRange();
//    chart.resetTotalYRange(range.getDMin(), range.getDMax()*1.10);
    chart.resetTotalYRange(0.0, range.getDMax()*1.10);

    range = chart.getTotalXRange();
    chart.resetTotalXRange(range.getDMin() - barWidth, range.getDMax() + barWidth);
    
    chart.resetRange();
  }

	/*********************************************************************************************************************
  <b>Description</b>: Removes all data sets from the chart.
	*********************************************************************************************************************/
  public void detachAllDataSets()
  {
    chart.detachAllDataSets();
  }

	/*********************************************************************************************************************
  <b>Description</b>: Moves the current X range slider such that the specifed bar number of the specified data set is
                      in the center of the slider range.

  <br>
  @param barNumber Bar number to center
  @param dataset Data set the bar is within
	*********************************************************************************************************************/
  public void centerBar(int barNumber, HistogramDataSet dataset)
  {
    double xValue = ((double)barNumber) + ((double)barNumber) * dataset.distanceBetweenBars;
    RangeModel range = chart.getTotalXRange();
    if (xValue <= range.getMax())
    {
      RangeModel currentRange = chart.getXScrollerRange();
      int r2 = (currentRange.getMax() - currentRange.getMin())/2;
      chart.setXScrollerRange(new RangeModel(xValue - r2, xValue + r2));
    }
  }

	/*********************************************************************************************************************
  <b>Description</b>: Resets the Y range limits based on the current data sets attached to the chart.

  <br><b>Notes</b>:<br>
	                  - Any time a data set is changed, this method should be called to adjust the Y range limits
	*********************************************************************************************************************/
  public void resetRangeLimits()
  {
    RangeModel limits = chart.getYScrollerRangeLimit();
    RangeModel currentRange = chart.getYScrollerRange();

    RangeModel range = chart.getTotalYRange();
//    chart.resetTotalYRange(range.getDMin(), range.getDMax()*1.10);
    chart.resetTotalYRange(0.0, range.getDMax()*1.10);
    
    if (limits.getDMax() == currentRange.getDMax())
    {
      chart.setYScrollerRange(new RangeModel(currentRange.getDMin(), range.getDMax()*1.10));
    }
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
  }

  public void componentHidden(ComponentEvent e)
  {
  }
  
  public void componentMoved(ComponentEvent e)
  {
  }
  
  public void componentResized(ComponentEvent e)
  {
    resetRangeLimits();
  }
  
  public void componentShown(ComponentEvent e)
  {
  }


	/*********************************************************************************************************************
  <b>Description</b>: HChart test example.  Use "java org.cougaar.lib.uiframework.ui.components.HChart" to run.
	*********************************************************************************************************************/
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().setLayout(new BorderLayout());
    HChart chart = new HChart("Histogram", null, "Units", 1.0);
    frame.getContentPane().add(chart, BorderLayout.CENTER);
    
    frame.setSize(300, 300);
    frame.show();

    double[] data = new double[10];
    String[] label = new String[10];
    for (int i=0; i<data.length; i++)
    {
      data[i] = (double)i/(double)data.length/2.0;
      label[i] = "Data: " + data[i];
    }

    try
    {
      chart.attachDataSet(new HistogramDataSet(data, label));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }
}
