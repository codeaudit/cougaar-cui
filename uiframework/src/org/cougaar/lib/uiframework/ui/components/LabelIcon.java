package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import javax.swing.*;

import java.util.*;

import org.cougaar.lib.uiframework.ui.components.graph.*;

public class LabelIcon implements Icon, PlotColors
{
  private Color[] plotColors = null;
  private final static Color[] defaultColors = {midnightBlue, darkGreen, darkYellow, rust, darkPurple, orange, red};

  private DataSet dataSet = null;

  private boolean alwaysVisible = false;

  private int width = 10;
  private int height = 10;

  public LabelIcon(DataSet dataSet, boolean alwaysVisible)
  {
    this.dataSet = dataSet;
    this.alwaysVisible = alwaysVisible;
  }

  public int getIconHeight()
  {
    return(height);
  }

  public int getIconWidth()
  {
    return(width);
  }

  public String getName()
  {
    return(dataSet.dataName);
  }

  public boolean isVisible()
  {
    return(dataSet.visible || alwaysVisible);
  }

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
