/* 
 * <copyright> 
 *  Copyright 1997-2001 Clark Software Engineering (CSE)
 *  under sponsorship of the Defense Advanced Research Projects 
 *  Agency (DARPA). 
 *  
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the Cougaar Open Source License as published by 
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).  
 *  
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS  
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR  
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF  
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT  
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT  
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL  
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,  
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR  
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.  
 *  
 * </copyright> 
 */

package org.cougaar.lib.uiframework.ui.components.graph;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.lang.*;
import java.io.StreamTokenizer;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

/***********************************************************************************************************************
<b>Description</b>: Extension of Graph2D to provide support for mouse over data tips.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class PointViewGraph2D extends Graph2D
{
  private PointToolTipManager pointTipManager = new PointToolTipManager();

	/*********************************************************************************************************************
  <b>Description</b>: Suggested squared distance from mouse pointer to point to reconize as a data tip.
	*********************************************************************************************************************/
  public double suggestedMaxPointDist2 = 5.0*5.0;

  /**
  *    Attached X Axis which must be registered with this class.
  *    This is one of the axes used to find the drag range.
  *    If no X axis is registered no mouse drag.
  */
  protected Axis xaxis;

  /**
  *    Attached Y Axis which must be registered with this class.
  *    This is one of the axes used to find the drag range.
  *    If no Y axis is registered no mouse drag.
  */
  protected Axis yaxis;


	/*********************************************************************************************************************
  <b>Description</b>: Default construtor.
	*********************************************************************************************************************/
  public PointViewGraph2D()
  {
    super();

    pointTipManager.registerComponent(this);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Set the delay time of displaying a data tip.

  <br>
  @param delay Delay time
	*********************************************************************************************************************/
  public void setToolTipDelay(int delay)
  {
    pointTipManager.setInitialDelay(delay);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Set if data tips are to be shown.

  <br>
  @param show True if data tips are to be  shown, false otherwise
	*********************************************************************************************************************/
  public void setShowDataTips(boolean show)
  {
    pointTipManager.setEnabled(show);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Sets the label the data tip is to be displayed in.

  <br>
  @param label Component to display data tip
	*********************************************************************************************************************/
  public void setDataTipLabel(JLabel label)
  {
    pointTipManager.setDataTipLabel(label);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Create a X axis and attaches it to the chart.

  <br>
  @return Axis that was created
	*********************************************************************************************************************/
  public Axis createXAxis()
  {
    xaxis = super.createAxis(Axis.BOTTOM);
    return xaxis;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Create a Y axis and attaches it to the chart.

  <br>
  @return Axis that was created
	*********************************************************************************************************************/
  public Axis createYAxis()
  {
    yaxis = super.createAxis(Axis.LEFT);
    return yaxis;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Attaches an axis to this chart.

  <br>
  @param a Axis to attach
	*********************************************************************************************************************/
  public void attachAxis(Axis a)
  {
    if(a==null) return;

    super.attachAxis(a);

    if(a.getAxisPos() == Axis.BOTTOM || a.getAxisPos() == Axis.TOP)
    {
      xaxis = a;
    }
    else
    {
      yaxis = a;
    }
  }

  private StringBuffer buffer = new StringBuffer(256);

	/*********************************************************************************************************************
  <b>Description</b>: Gets the data tip text for the specified mouse event.

  <br>
  @param event Mouse event and X/Y coordinates of mouse pointer
  @return Text string representing the data tip at the specified mouse location, or null if there is is no tip
	*********************************************************************************************************************/
  public String getToolTipText(MouseEvent event)
  {
    if (!datarect.contains(new Point(event.getX(), event.getY())))
    {
      return(null);
    }

    String tipText = null;
    Object[] array = getClosestPoint(event.getX(), event.getY());
    double[] point = (double[])array[0];
    DataSet ds = (DataSet)array[1];

    if (point != null)
    {
      buffer.setLength(0);
//      tipText = "(" + xaxis.getPointAsString(point[0]) + "," + yaxis.getPointAsString(point[1]) + ")";
//      if (ds.dataGroup != null)
//      {
//        buffer.append(ds.dataGroup);
//        buffer.append(": ");
//      }

//      if (ds.dataName != null)
//      {
//        buffer.append(ds.dataName);
//        buffer.append(" ");
//      }

      buffer.append(ds.getPointAsString(point[0], point[1]));

//      buffer.append("(");
//      buffer.append(xaxis.getPointAsString(point[0]));
//      buffer.append(",");
//      buffer.append(yaxis.getPointAsString(point[1]));
//      buffer.append(")");
//      tipText = ds.dataGroup + ": " + ds.dataName + " (" + xaxis.getPointAsString(point[0]) + "," + yaxis.getPointAsString(point[1]) + ")";
      tipText = buffer.toString();
    }

    return(tipText);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Gets the closest point, distance and associated data set to the specified mouse corrdinates
                      within the suggestedMaxPointDist2.

  <br>
  @param ix Mouse X coordinate
  @param iy Mouse Y coordinate
  @return Array where Object[0] = double[] where double[0] = X value, double[1] = Y value and Object[1] = data set
	*********************************************************************************************************************/
  public Object[] getClosestPoint(int ix, int iy)
  {
    if (dataset.size() == 0)
    {
      return(new Object[] {null, null});
    }

    DataSet ds;
    double a[] = new double[3];
    double distsq = -1.0;
    double data[] = {-1.0, -1.0};
    Object[] array = new Object[] {data, null};
    double x = xaxis.getDouble(ix);
    double y = yaxis.getDouble(iy);

    for (int i=0; i<dataset.size(); i++)
    {
      ds = (DataSet)dataset.elementAt(i);

      if (!ds.visible) continue;

      a = ds.getClosestPoint(x, y, suggestedMaxPointDist2, true);

      if (a == null) continue;

      if(distsq < 0.0 || distsq > a[2])
      {
        data[0] = a[0];
        data[1] = a[1];
        distsq  = a[2];
        array[1] = ds;
      }
    }

    if ((data[0] == -1.0) && (data[1] == -1.0))
    {
      array[0] = null;
    }

    return(array);
  }
}
