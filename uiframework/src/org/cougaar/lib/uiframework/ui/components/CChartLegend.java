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
import java.util.*;

import javax.swing.*;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.cougaar.lib.uiframework.ui.components.graph.*;
import org.cougaar.lib.uiframework.ui.components.*;

/***********************************************************************************************************************
<b>Description</b>: A legend component for chart data sets with the capability to notify property listeners when the
                    legend sets data set visibility on and off.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class CChartLegend extends JPanel
{
  private Hashtable dataSetGroups = new Hashtable(1);

  private Color foregroundColor = null;
  private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/*********************************************************************************************************************
  <b>Description</b>: Default legend constructor.
	*********************************************************************************************************************/
  public CChartLegend()
  {
    updateUI();
  }

	/*********************************************************************************************************************
  <b>Description</b>: Adds a data set to the legend.

  <br>
  @param dataSet Data set to add to the legend
	*********************************************************************************************************************/
  public void addDataSet(DataSet dataSet)
  {
    Vector group = null;
    if ((group = (Vector)dataSetGroups.get(dataSet.dataGroup)) == null)
    {
      group = new Vector(1);
      dataSetGroups.put(dataSet.dataGroup, group);
    }

    group.add(dataSet);

    resetCheckBoxes();
  }

	/*********************************************************************************************************************
  <b>Description</b>: Removes all of the data sets from the legend.
	*********************************************************************************************************************/
  public void removeAllDataSets()
  {
    dataSetGroups.clear();
    resetCheckBoxes();
  }

  public void updateUI()
  {
    super.updateUI();

    resetCheckBoxes();
  }

  public void resetCheckBoxes()
  {
    if (dataSetGroups == null)
    {
      return;
    }

    removeAll();

    Vector panels = new Vector(0);
    GridBagConstraints constraints = new GridBagConstraints();

    Vector group = null;
    String groupName = null;
    DataSet dataSet = null;
    JPanel panel = null;
    JPanel itemPanel = null;
    JLabel label = null;
    JCheckBox checkBox = null;
    DataSetCheckBoxListener listenerIcon = null;
    GridBagLayout groupLayout = null;

    for (Enumeration keys = dataSetGroups.keys(); keys.hasMoreElements();)
    {
      groupName = (String)keys.nextElement();
      group = (Vector)dataSetGroups.get(groupName);

      panel = new JPanel(new GridLayout(group.size()+1, 1));
      label = new JLabel(groupName, SwingConstants.CENTER);
      panel.add(label);

      constraints.anchor = GridBagConstraints.WEST;
      for (int j=0, jsize=group.size(); j<jsize; j++)
      {
        dataSet = (DataSet)group.elementAt(j);

        checkBox = new JCheckBox(dataSet.dataName, dataSet.visible);
        listenerIcon = new DataSetCheckBoxListener(dataSet, checkBox);
        checkBox.addActionListener(listenerIcon);

        groupLayout = new GridBagLayout();
        itemPanel = new JPanel(groupLayout);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        itemPanel.add(new JLabel(" ", listenerIcon, SwingConstants.LEADING), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        itemPanel.add(checkBox, constraints);

        panel.add(itemPanel);
      }

      panels.add(panel);
    }

    GridBagLayout layout = new GridBagLayout();
    constraints.anchor = GridBagConstraints.NORTH;
    setLayout(layout);
    for (int i=0, isize=panels.size(); i<isize; i++)
    {
      constraints.gridx = i;
      constraints.gridy = 0;
      constraints.weightx = 1.0;
      add((Component)panels.elementAt(i), constraints);
    }

    validate();
  }

	/*********************************************************************************************************************
  <b>Description</b>: Add a property change listener to be notified when the legend changes data set visibility.

  <br>
  @param propertyChangeListener Listener to add
	*********************************************************************************************************************/
  public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Remove a property change listener.

  <br>
  @param propertyChangeListener Listener to remove
	*********************************************************************************************************************/
  public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
  }

  class DataSetCheckBoxListener implements ActionListener, Icon
  {
    private DataSet dataSet = null;
    private JCheckBox checkBox = null;

    private int width = 10;
    private int height = 10;

    public DataSetCheckBoxListener(DataSet dataSet, JCheckBox checkBox)
    {
      this.dataSet = dataSet;
      this.checkBox = checkBox;
    }

    public void actionPerformed(ActionEvent e)
    {
      dataSet.visible = checkBox.isSelected();
      propertyChangeSupport.firePropertyChange(dataSet.dataName, !dataSet.visible, dataSet.visible);
    }

    public int getIconHeight()
    {
      return(height);
    }

    public int  getIconWidth()
    {
      return(width);
    }

    public void paintIcon(Component comp, Graphics g, int x, int y)
    {
      boolean filled = ((dataSet instanceof PolygonFillableDataSet) && ((PolygonFillableDataSet)dataSet).polygonFill);

      Color c = g.getColor();
      g.setColor(checkBox.getForeground());
      g.drawRect(x, y, width, height);

      g.setColor(dataSet.linecolor);

      if ((dataSet instanceof PolygonFillableDataSet) && (((PolygonFillableDataSet)dataSet).polygonFill))
      {
        if (((PolygonFillableDataSet)dataSet).useFillPattern)
        {
          g.fillPolygon(new int[] {1, width-1, 1}, new int[] {1, 1, height-1}, 3);
          g.setColor(comp.getBackground());
          g.fillPolygon(new int[] {width-1, 1, width-1}, new int[] {1, height-1, height-1}, 3);
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
}
