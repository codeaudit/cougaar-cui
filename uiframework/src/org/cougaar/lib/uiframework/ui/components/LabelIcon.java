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

package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import javax.swing.*;

import java.util.*;

import org.cougaar.lib.uiframework.ui.components.graph.*;

/***********************************************************************************************************************
<b>Description</b>: This class is an Icon displayable component of a data set.  It can be used any where an Icon object
                    is required and uses the data set's color and fill properties as part of the displayed icon.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class LabelIcon implements Icon, PlotColors
{
  private Color[] plotColors = null;
  private final static Color[] defaultColors = {midnightBlue, darkGreen, darkYellow, rust, darkPurple, orange, red, green, blue};

  private DataSet dataSet = null;

  private boolean alwaysVisible = false;

  private int width = 10;
  private int height = 10;

	/*********************************************************************************************************************
  <b>Description</b>: Constructs a label icon with the specified data set and visibility.

  <br>
  @param dataSet Data set to create the icon from
  @param alwaysVisible If the icon should always be visible
	*********************************************************************************************************************/
  public LabelIcon(DataSet dataSet, boolean alwaysVisible)
  {
    this.dataSet = dataSet;
    this.alwaysVisible = alwaysVisible;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the height of the graphics area needed to paint the icon.

  <br>
  @return Height of the graphics area needed to paint the icon
	*********************************************************************************************************************/
  public int getIconHeight()
  {
    return(height);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the width of the graphics area needed to paint the icon.

  <br>
  @return Width of the graphics area needed to paint the icon
	*********************************************************************************************************************/
  public int getIconWidth()
  {
    return(width);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the name of the data set associated with the icon.

  <br>
  @return Name of the data set
	*********************************************************************************************************************/
  public String getName()
  {
    return(dataSet.dataName);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Indicates if the icon should be visible.

  <br>
  @return True if the data set is visible or the icon should always be visible, false otherwise
	*********************************************************************************************************************/
  public boolean isVisible()
  {
    return(dataSet.visible || alwaysVisible);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Paints the label icon in the specified graphics context at the specified cordinates.

  <br>
  @param comp Component with the proper background and foreground colors to use when painting the icon
  @param g Graphics context to draw the icon on
  @param x Upper left hand X coordinate position to draw icon at
  @param y Upper left hand Y coordinate position to draw icon at
	*********************************************************************************************************************/
  public void paintIcon(Component comp, Graphics g, int x, int y)
  {
    Color c = g.getColor();
    g.setColor(comp.getForeground());
    g.drawRect(x, y, width, height);

    if (dataSet.colorNumber != -1)
    {
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

      g.setColor(plotColors[dataSet.colorNumber]);
    }
    else
    {
      g.setColor(dataSet.linecolor);
    }

    if ((dataSet instanceof PolygonFillableDataSet) && (((PolygonFillableDataSet)dataSet).polygonFill))
    {
      if (((PolygonFillableDataSet)dataSet).useFillPattern)
      {
        g.fillPolygon(new int[] {x+1, x+width, x+1}, new int[] {y+1, y+1, y+height}, 3);
        g.setColor(comp.getBackground());
        g.fillPolygon(new int[] {x+width, x+1, x+width}, new int[] {y+1, y+height, y+height}, 3);
      }
      else
      {
        g.fillRect(x+1, y+1, width-1, height-1);
      }
    }
    // Line graph
    else if (dataSet.getClass().equals(DataSet.class) || dataSet.getClass().equals(PolygonFillableDataSet.class))
    {
      g.fillRect(x+1, y+1, width-1, height-1);
    }
    // Bar graph
    else if (dataSet.getClass().equals(BarDataSet.class))
    {
      g.fillRect(x+1, y+1, width-1, height-1);
      g.setColor(comp.getBackground());
      g.fillRect(x+3, y+3, width-5, height-5);
    }
    // Step graph
    else if (dataSet.getClass().equals(StepDataSet.class))
    {
      g.fillRect(x+1, y+1, width-1, height-1);
    }

    g.setColor(c);
  }
}
