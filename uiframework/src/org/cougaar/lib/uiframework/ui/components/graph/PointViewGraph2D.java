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

public class PointViewGraph2D extends Graph2D
{
  private PointToolTipManager pointTipManager = new PointToolTipManager();

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


  public PointViewGraph2D()
  {
    super();

    pointTipManager.registerComponent(this);
  }

  public void setToolTipDelay(int delay)
  {
    pointTipManager.setInitialDelay(delay);
  }

  public void setShowDataTips(boolean show)
  {
    pointTipManager.setEnabled(show);
  }

  public void setDataTipLabel(JLabel label)
  {
    pointTipManager.setDataTipLabel(label);
  }

  public Axis createXAxis()
  {
    xaxis = super.createAxis(Axis.BOTTOM);
    return xaxis;
  }

  public Axis createYAxis()
  {
    yaxis = super.createAxis(Axis.LEFT);
    return yaxis;
  }

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
      buffer.append(ds.dataGroup);
      buffer.append(": ");
      buffer.append(ds.dataName);
      buffer.append(" (");
      buffer.append(xaxis.getPointAsString(point[0]));
      buffer.append(",");
      buffer.append(yaxis.getPointAsString(point[1]));
      buffer.append(")");
//      tipText = ds.dataGroup + ": " + ds.dataName + " (" + xaxis.getPointAsString(point[0]) + "," + yaxis.getPointAsString(point[1]) + ")";
      tipText = buffer.toString();
    }

    return(tipText);
  }

  protected Object[] getClosestPoint(int ix, int iy)
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
