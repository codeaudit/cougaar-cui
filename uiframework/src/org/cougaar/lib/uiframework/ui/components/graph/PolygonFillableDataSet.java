/*
 * <copyright>
 * Copyright 1997-2000 Defense Advanced Research Projects Agency (DARPA)
 * and Clark Software Engineering (CSE) This software to be used in
 * accordance with the COUGAAR license agreement.  The license agreement
 * and other information on the Cognitive Agent Architecture (COUGAAR)
 * Project can be found at http://www.cougaar.org or email: info@cougaar.org.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components.graph;

import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.util.*;
import java.lang.*;

/***********************************************************************************************************************
<b>Description</b>: Polygon fillable chart data set.  Extension of the DataSet to render a line chart based on a given
                    data set and provide functionality to fill in below the line with the color of the data set.

@author Eric B. Martin, &copy;2000 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class PolygonFillableDataSet extends DataSet
{
  /*********************************************************************************************************************
  <b>Description</b>: Flag to indicate if the data set should use polygon filling to produce a solid graph rendering
                      instead of a line (wire frame) rendering of the graph.

  <br><br><b>Notes</b>:<br>
                    - polygonFill is false by default
  *********************************************************************************************************************/
  public boolean polygonFill = false;
  public boolean useFillPattern = false;

  public boolean adjustLineWidthInsets = false;

  /*********************************************************************************************************************
  <b>Description</b>: Default constructor.
  *********************************************************************************************************************/
  public PolygonFillableDataSet()
  {
    super();
  }

  public PolygonFillableDataSet(double d[], int n, boolean fill) throws Exception
  {
    super(d, n);
    polygonFill = fill;
  }

  /*********************************************************************************************************************
  <b>Description</b>: Constructor that sets the polygonFill flag of this data set.

  <br>
  @param fill Indicates if this data set should use polygon fill for rendering
  *********************************************************************************************************************/
  public PolygonFillableDataSet(boolean fill)
  {
    super();
    polygonFill = fill;
  }

  public void draw_data(Graphics g, Rectangle bounds)
  {
    if (!visible) return;

    if (xaxis != null)
    {
      xmax = xaxis.maximum;
      xmin = xaxis.minimum;
    }

    if (yaxis != null)
    {
      ymax = yaxis.maximum;
      ymin = yaxis.minimum;
    }

    xrange = xmax - xmin;
    yrange = ymax - ymin;

    if (clipping) g.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);

    Color c = g.getColor();

    if (linecolor != null)
    {
      g.setColor(linecolor);
    }

    if( linestyle != DataSet.NOLINE )
    {
      draw_lines(g, bounds);
    }

    g.setColor(c);
  }

  protected void draw_lines(Graphics g, Rectangle w)
  {
    draw_lines(g, w, data, length, 0.0);
  }


  protected void draw_lines(Graphics g, Rectangle w, double[] pointData, int dataLength, double height)
  {
    int i;
    int j;
    boolean inside0 = false;
    boolean inside1 = false;
    double x,y;
    int x0 = 0 , y0 = 0;
    int x1 = 0 , y1 = 0;
    //     Calculate the clipping rectangle
    Rectangle clip = g.getClipBounds();
    int xcmin = clip.x;
    int xcmax = clip.x + clip.width;
    int ycmin = clip.y;
    int ycmax = clip.y + clip.height;


    //    Is there any data to draw? Sometimes the draw command will
    //    will be called before any data has been placed in the class.
    if ( pointData == null || dataLength < stride )
    {
      return;
    }

    //    Is the first point inside the drawing region ?
    if ( (inside0 = inside(pointData[0] + xValueOffset, pointData[1] + yValueOffset)) )
    {
      x0 = (int)(w.x + ((pointData[0] + xValueOffset - xmin)/xrange)*w.width);
      y0 = (int)(w.y + (1.0 - (pointData[1] + yValueOffset + height - ymin)/yrange)*w.height);

      if (polygonFill)
      {
//        if (x0 < xcmin || x0 > xcmax || y0 < ycmin)
        if (x0 < xcmin || x0 > xcmax)
        {
          inside0 = false;
        }
      }
      else
      {
        if (x0 < xcmin || x0 > xcmax || y0 < ycmin || y0 > ycmax)
        {
          inside0 = false;
        }
      }
    }


    for (i=stride; i<dataLength; i+=stride)
    {
      //        Is this point inside the drawing region?
      inside1 = inside( pointData[i] + xValueOffset, pointData[i+1] + yValueOffset);


// #CSE# 02/15/2001 Fix line through rectangle whose points are ouside rectangle
      if (!inside0 && !inside1 && insideRect(pointData[i-stride] + xValueOffset, pointData[i-stride+1] + yValueOffset, pointData[i] + xValueOffset, pointData[i+1] + yValueOffset))
      {
        inside0 = true;

        x0 = (int)(w.x + ((pointData[i-stride] + xValueOffset - xmin)/xrange)*w.width);
        y0 = (int)(w.y + (1.0 - (pointData[i-stride+1]  + yValueOffset - ymin)/yrange)*w.height);
      }


      //        If one point is inside the drawing region calculate the second point
      if ( inside1 || inside0 )
      {
        x1 = (int)(w.x + ((pointData[i] + xValueOffset - xmin)/xrange)*w.width);
        y1 = (int)(w.y + (1.0 - (pointData[i+1] + yValueOffset + height - ymin)/yrange)*w.height);

        if (polygonFill)
        {
//          if ( x1 < xcmin || x1 > xcmax || y1 < ycmin)
          if ( x1 < xcmin || x1 > xcmax)
          {
            inside1 = false;
          }
        }
        else
        {
          if ( x1 < xcmin || x1 > xcmax || y1 < ycmin || y1 > ycmax)
          {
            inside1 = false;
          }
        }
      }

      //        If the second point is inside calculate the first point if it
      //        was outside
      if ( !inside0 && inside1 )
      {
        x0 = (int)(w.x + ((pointData[i-stride] + xValueOffset - xmin)/xrange)*w.width);
        y0 = (int)(w.y + (1.0 - (pointData[i-stride+1] + yValueOffset + height - ymin)/yrange)*w.height);
      }

      //        If either point is inside draw the segment
      if ( inside0 || inside1 )
      {
        if (polygonFill)
        {
          if (x0 != x1)
          {
            g.fillPolygon(new int[] {x0, x0, x1, x1}, new int[] {ycmax, y0, y1, ycmax}, 4);

            if ((g instanceof Graphics2D) && useFillPattern)
            {
              Graphics2D g2D = (Graphics2D)g;
              Color c = g2D.getColor();
              g2D.setColor(Color.white);

              int numStripes = 8;
              int stripeThickness = 4;

              int y0pixel = y0;
              int y1pixel = y1;
              int yMAX = yaxis.getInteger(0.0 + yValueOffset);
              int yStep = (yMAX-(y0<y1 ? y0 : y1))/numStripes;
              yStep = (yStep == 0) ? (1 + stripeThickness) : (yStep + stripeThickness);

              do
              {
                y0pixel += yStep;
                g2D.fillPolygon(new int[] {x0, x0, x1, x1}, new int[] {y0pixel, y0pixel-stripeThickness, y1pixel, y1pixel+stripeThickness}, 4);
                y1pixel += yStep;
              }
              while ((y0pixel < ycmax) && (y0pixel < yMAX));

              g2D.setColor(c);
            }
          }
        }
        else
        {
          if (g instanceof Graphics2D)
          {
            Graphics2D graphics2D = (Graphics2D)g;
            Stroke stroke = graphics2D.getStroke();
            BasicStroke newStroke = new BasicStroke(lineThickness);
            graphics2D.setStroke(newStroke);

            // A quick hack EBM
            if (adjustLineWidthInsets)
            {
              if (y0>y1)
              {
                g.drawLine(x0+(int)((lineThickness)/2),y0,x1+(int)((lineThickness)/2),y1);
              }
              else if (y0<y1)
              {
                g.drawLine(x0-(int)((lineThickness+1)/2),y0,x1-(int)((lineThickness+1)/2),y1);
              }
              else if (x0<x1)
              {
                g.drawLine(x0+(int)((lineThickness)/2),y0,x1-(int)((lineThickness+1)/2),y1);
              }
              else if (x0>x1)
              {
                g.drawLine(x0-(int)((lineThickness+1)/2),y0,x1+(int)((lineThickness)/2),y1);
              }
            }
            else
            {
              g.drawLine(x0,y0,x1,y1);
            }

            graphics2D.setStroke(stroke);
          }
          else
          {
            g.drawLine(x0,y0,x1,y1);
          }
        }
      }

      /*
      **        The reason for the convolution above is to avoid calculating
      **        the points over and over. Now just copy the second point to the
      **        first and grab the next point
      */
      inside0 = inside1;
      x0 = x1;
      y0 = y1;
    }
  }


  protected boolean inside(double x, double y)
  {
    if (polygonFill)
    {
      if ((x >= xmin) && (x <= xmax) && (y >= ymin))
      {
        return true;
      }
    }
    else
    {
      if ((x >= xmin) && (x <= xmax) && (y >= ymin) && (y <= ymax))
      {
        return true;
      }
    }

    return false;
  }
}
