/*
 * <copyright>
 *  Copyright 1997-2001 Defense Advanced Research Projects Agency (DARPA) and Clark Software Engineering (CSE) This software to be used in accordance with the COUGAAR license agreement
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
package org.cougaar.lib.uiframework.ui.components.graph;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.lang.*;

/***********************************************************************************************************************
<b>Description</b>: Step chart data set.  Extension of the PolygonFillableDataSet to render a step chart based on a
                    given data set.

@author Eric B. Martin, &copy;2000 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class StepDataSet extends PolygonFillableDataSet
{
  private double[] stepData = new double[0];

  /*********************************************************************************************************************
  <b>Description</b>: The the distance (in user defined scale) to interpolate the last point of the data.  Used to show
                      last known step value.

  <br><br><b>Notes</b>:<br>
                    - Defaults to not showing the last known step value
  *********************************************************************************************************************/
  public double endPointLead = 0.0;

  /*********************************************************************************************************************
  <b>Description</b>: Constructor for a new empty bar data set.
  *********************************************************************************************************************/
  public StepDataSet()
  {
    super(true);
    calculateTemp();
  }

  public StepDataSet(double d[], int count, boolean fill) throws Exception
  {
    super(fill);
    appendStepData(d, count);
  }

  /*********************************************************************************************************************
  <b>Description</b>: Constructor for a new step data set with the specifed step data and color.

  <br><b>Notes</b>:<br>
                    - Data is appended as a single dimension array where index i would be the step location (X value)
                      and index i+1 would be the step height (Y value) (e.g. d[0] = step start location, d[1] = step
                      height)
                    - The number of step data points to get from the array is also passed in so more than one step
                      can be added at a time

  <br>
  @param d[] Step data array
  @param count Number of step data points to read from the data array
  @param color Color to be used when rendering the data set
  *********************************************************************************************************************/
  public StepDataSet(double d[], int count, Color color) throws Exception
  {
    super(true);
    linecolor = color;
    appendStepData(d, count);
  }

  public double[] getData()
  {
    return(stepData);
  }

  public int dataPoints()
  {
    return(stepData.length/2);
  }

  public double[] getPoint(int index)
  {
    if ((index < 0) || (index >= (stepData.length/2)))
    {
      return(null);
    }

    return(new double[] {data[index*2+0], data[index*2+1]});
  }

  public double getYmaxInRange(double xMin, double xMax)
  {
    // Go through each data point in the range, comparing them to find the largest Y value
    double yMax = Double.NaN;
    for (int i=0; i<stepData.length; i+=stride)
    {
      if ((xMin <= stepData[i]) && (stepData[i] <= xMax))
      {
        yMax = ((yMax < stepData[i+1]) || (Double.isNaN(yMax))) ? stepData[i+1] : yMax;
      }
    }

    return(yMax);
  }

  /*********************************************************************************************************************
  <b>Description</b>: Adds step data points to the data set.

  <br><b>Notes</b>:<br>
                    - Data is appended as a single dimension array where index i would be the step location (X value)
                      and index i+1 would be the height (Y value) (e.g. d[0] = step start location, d[1] = step height)
                    - The number of step data points to get from the array is also passed in so more than one step
                      can be added at a time

  <br>
  @param d[] Step data array
  @param count Number of step data points to read from the data array
  *********************************************************************************************************************/
  public void appendStepData(double d[], int count) throws Exception
  {
    // Quick and dirty array copy to keep the current set of data consistent
    int lastIndex = resizeStepData(stepData.length + count*2);
    System.arraycopy(d, 0, stepData, lastIndex, count*2);

//     Update the range on Axis that this data is attached to
    range(stride);
    if(xaxis != null) xaxis.resetRange();
    if(yaxis != null) yaxis.resetRange();

    calculateTemp();
  }

  private int resizeStepData(int newSize)
  {
    int previousSize = stepData.length;
    double[] temp = new double[newSize];
    System.arraycopy(stepData, 0, temp, 0, (newSize <= stepData.length) ? newSize : stepData.length);
    stepData = temp;

    return(previousSize);
  }

  /*********************************************************************************************************************
  <b>Description</b>: Changes step height values in the data set.

  <br><b>Notes</b>:<br>
                    - Data is passed as a single dimension array where index i would be the step location (X value)
                      and index i+1 would be the height (Y value) (e.g. d[0] = step start location, d[1] = step height)
                    - The number of step data points to get from the array is calculated based on d.length/2

  <br>
  @param d[] Step data array
  @see #changeStepData(double[], int)
  *********************************************************************************************************************/
  public void changeStepData(double d[])
  {
    changeStepData(d, d.length/2);
  }

  /*********************************************************************************************************************
  <b>Description</b>: Changes step height values in the data set.  This method will search through the current set of
                      step data for X point values that match with each X point specifed in the change array (d[]) and,
                      if the X point value is found, will replace the corresponding Y point value with the changed
                      value.

  <br><b>Notes</b>:<br>
                    - Step start location (X point) values should be unique across this data set, however, this
                      restriction is not enforced
                    - Data is passed as a single dimension array where index i would be the step location (X value)
                      and index i+1 would be the height (Y value) (e.g. d[0] = step start location, d[1] = step height)
                    - The number of step data points to get from the array is also passed in so more than one step
                      can be added at a time

  <br>
  @param d[] Step data array
  @param count Number of step data points to read from the data array
  *********************************************************************************************************************/
  public void changeStepData(double d[], int count)
  {
    for (int i=0; i<count; i++)
    {
      change(d[2*i+0], d[2*i+1]);
    }

//     Update the range on Axis that this data is attached to
    range(stride);
    if(xaxis != null) xaxis.resetRange();
    if(yaxis != null) yaxis.resetRange();

    calculateTemp();
  }

  private void change(double x, double newY)
  {
    for (int i=0; i<stepData.length; i+=2)
    {
      if (stepData[i] == x)
      {
        stepData[i+1] = newY;
        break;
      }
    }
  }

  private double[] temp = null;

  private void calculateTemp()
  {
    int count = (stepData.length/2) -1;
    temp = new double[count*8+4];

    for (int i=0; i<count; i++)
    {
      temp[i*8 + 0] = stepData[i*2+0];
      temp[i*8 + 1] = stepData[i*2+1];
      temp[i*8 + 2] = stepData[(i+1)*2+0];
      temp[i*8 + 3] = stepData[i*2+1];

      temp[i*8 + 4] = stepData[(i+1)*2+0];
      temp[i*8 + 5] = stepData[(i+1)*2+1];
      temp[i*8 + 6] = stepData[(i+1)*2+0];
      temp[i*8 + 7] = stepData[i*2+1];
    }

    temp[count*8 + 0] = stepData[(count)*2+0];
    temp[count*8 + 1] = stepData[(count)*2+1];
    temp[count*8 + 2] = stepData[(count)*2+0] + endPointLead;
    temp[count*8 + 3] = stepData[(count)*2+1];
  }

  protected void draw_lines(Graphics g, Rectangle w)
  {
    if (stepData.length == 0)
    {
      return;
    }

    draw_lines(g, w, temp, temp.length, 0.0);
  }

  protected void range(int stride)
  {
    dxmin = 0.0;
    dxmax = 0.0;
    dymin = 0.0;
    dymax = 0.0;

    if ((stepData == null) || (stepData.length < 2))
    {
      return;
    }

    dxmin = dxmax = stepData[0];
    dymin = dymax = stepData[1];

    for (int i=0; i<stepData.length; i+=2)
    {
      if(dxmax < stepData[i])
      {
        dxmax = stepData[i];
      }
      else if(dxmin > stepData[i])
      {
        dxmin = stepData[i];
      }

      if(dymax < stepData[i+1])
      {
        dymax = stepData[i+1];
      }
      else if(dymin > stepData[i+1])
      {
        dymin = stepData[i+1];
      }
    }

    if(dxmax < stepData[stepData.length -2] + endPointLead)
    {
      dxmax = stepData[stepData.length -2];
    }

    if(xaxis == null)
    {
      xmin = dxmin;
      xmax = dxmax;
    }
    if(yaxis == null)
    {
      ymin = dymin;
      ymax = dymax;
    }
  }

  public double[] getClosestPoint(double x, double y)
  {
    return(getClosestPoint(x, y, false));
  }

  public double[] getClosestPoint(double x, double y, boolean useOffset)
  {
    double xOffset = 0.0;
    double yOffset = 0.0;
    if (useOffset)
    {
      xOffset = xValueOffset;
      yOffset = yValueOffset;
    }

    double point[] = {0.0, 0.0, -1.0};
    double dist2;

    if (stepData.length == 0) return(point);

    double[] temp = new double[4];
    int count = (stepData.length/2) -1;

    temp[0] = stepData[(count)*2+0] + xOffset;
    temp[1] = stepData[(count)*2+1] + yOffset;
    temp[2] = stepData[(count)*2+0] + xOffset + endPointLead;
    temp[3] = stepData[(count)*2+1] + yOffset;

    point[0] = stepData[(count)*2+0];
    point[1] = stepData[(count)*2+1];
    point[2] = calcDist2(temp, x, y);

    for (int i=0; i<count; i++)
    {
      temp[0] = stepData[(i+0)*2+0] + xOffset;
      temp[1] = stepData[(i+0)*2+1] + yOffset;
      temp[2] = stepData[(i+1)*2+0] + xOffset;
      temp[3] = stepData[(i+0)*2+1] + yOffset;

      dist2 = calcDist2(temp, x, y);

      if ((point[2] < 0.0) || ((dist2 >= 0.0) && (dist2 < point[2])))
      {
        point[0] = stepData[i*2+0];
        point[1] = stepData[i*2+1];
        point[2] = dist2;
      }
    }

    return(point);
  }

  private double calcDist2(double[] linePoints, double x, double y)
  {
    if ((linePoints[0] > x) || (linePoints[2] < x) || (linePoints[1] < y) || (linePoints[3] < y))
    {
      return(-1.0);
    }

    linePoints[0] += (linePoints[2] - linePoints[0])/2.0;

    double xdiff = linePoints[0] - x;
    double ydiff = linePoints[1] - y;

    return(xdiff*xdiff + ydiff*ydiff);
  }

/*  public double[] getClosestPoint(double x, double y, boolean useOffset)
  {
    double xOffset = 0.0;
    double yOffset = 0.0;
    if (useOffset)
    {
      xOffset = xValueOffset;
      yOffset = yValueOffset;
    }

    double point[] = {0.0, 0.0, -1.0};
    int i;
    double xdiff, ydiff, dist2;

    if (stepData.length == 0) return(point);

    xdiff = stepData[0] + xOffset - x;
    ydiff = stepData[1] + yOffset - y;

    point[0] = stepData[0] + xOffset;
    point[1] = stepData[1] +
     yOffset;
    point[2] = xdiff*xdiff + ydiff*ydiff;

    for(i=stride; i<stepData.length-1; i+=stride)
    {
        xdiff = stepData[i  ] + xOffset - x;
        ydiff = stepData[i+1] + yOffset - y;

        dist2 = xdiff*xdiff + ydiff*ydiff;

        if(dist2 < point[2])
        {
            point[0] = stepData[i  ];
            point[1] = stepData[i+1];
            point[2] = dist2;
        }
     }

     return point;
  }*/

  public double[] getClosestPoint(double x, double y, double maxDist2, boolean useOffset)
  {
    double point[] = getClosestPoint(x, y, useOffset);

//    if ((maxDist2 < point[2]) || (0 > point[2]))
    if (0 > point[2])
    {
      return(null);
    }

    return(point);
  }
}
