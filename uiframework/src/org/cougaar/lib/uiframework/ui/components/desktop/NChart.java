package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.util.*;


import javax.swing.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.cougaar.lib.uiframework.ui.components.*;
import org.cougaar.lib.uiframework.ui.components.mthumbslider.*;
import org.cougaar.lib.uiframework.ui.components.graph.*;
import org.cougaar.lib.uiframework.ui.models.RangeModel;

import org.cougaar.lib.uiframework.ui.components.desktop.dnd.*;

public class NChart extends javax.swing.JPanel implements PropertyChangeListener
{
	private double[] xMinMax = {0.0, 200.0};
	private NChartUI nChartUI = null;
	private boolean xRangeScrollLock = false;
  private double xScrollSize = 0.0;
	private int totalCharts;
	public CChart mainChart = null;
	public CChart[] chartElement;
	private CChart[] chartList = null;
	public Vector data = null;
	private JPanel inventoryChartLegend = new JPanel();
	private JPanel chartPanel = new JPanel(new GridBagLayout());
	private CMThumbSliderRangeControl xRC = new CMThumbSliderRangeControl(0.0f, 0.0f);
	private boolean twoUp = true;
  public NChart(int numberOfCharts, NChartUI uiPtr)
  {
  	nChartUI = uiPtr;
  	setLayout(new BorderLayout());
  	totalCharts = numberOfCharts;
  	chartElement = new CChart[numberOfCharts];
  	chartList = new CChart[numberOfCharts];
  	//buildLegends();
  	for(int i = 0; i < numberOfCharts; i++)
  	{
  		chartElement[i] = new CChart("chart " + i, inventoryChartLegend, "x-label" + i, "y-label" + i, false);
  		chartElement[i].setShowXRangeScroller(true);
  		chartElement[i].setShowXDividers(true);
  		chartElement[i].setXScrollerRange(new RangeModel(0, 200));
      chartElement[i].setXAxisSigDigitDisplay(1);
      chartElement[i].setToolTipDelay(0);
      chartList[i] = chartElement[i];
  		  		
  		GridBagConstraints constraints = new GridBagConstraints();

	    constraints.gridx = 0;
	    constraints.gridy = i;
	    constraints.gridwidth = 1;
	    constraints.gridheight = 1;
	    constraints.weightx = 2.0;
	    constraints.weighty = 2.0;
	    constraints.fill = GridBagConstraints.BOTH;
	    
	    chartPanel.add(chartElement[i], constraints);
	    
  	  
    }
    xRC.addPropertyChangeListener("range", new RangeChangeListener(xRC, xMinMax, chartList));
    xRC.getSlider().setOrientation(CMThumbSlider.HORIZONTAL);
    add(chartPanel, BorderLayout.CENTER);
	  add(xRC, BorderLayout.SOUTH);
	  validate();
	  
  }
  
  //
  public void setScrollers()
  {
  	for(int i = 0; i < chartList.length; i++)
  	{
  	  chartList[i].setXScrollerRange(xRC.getRange());
      chartList[i].setShowXDividers(false);
    }
  }
  
  public void setTwoUp(boolean t)
  {
  	twoUp = t;
  }
  
  public void redrawCharts()
  {
  	setDataIntoChart(data);
  }
  
  public void swapCharts(int chart1Index, int chart2Index)
  {
  	if(chart1Index < data.size()&& chart2Index < data.size())
  	{
	  	Vector newData = (Vector)data.clone();
	  	for(int i = 0; i < data.size(); i++)
	  	{
	  		if(i == chart1Index)
	  		  newData.setElementAt(data.elementAt(i), chart2Index);
	  		else if(i == chart2Index)
	  		  newData.setElementAt(data.elementAt(i), chart1Index);
	  		else
	  		  newData.setElementAt(data.elementAt(i), i);
	  		
	  	}
	  	data = newData;
	  	setDataIntoChart(data);
	 }
  }
  
  
  public void removeCharts(int chartIndex)
  {
  	if(data.size() == 1)
  	  return;
  	data.removeElementAt(chartIndex);
  	setDataIntoChart(data);
  }
  
  
  public void setVisible(DataSet dataSet, boolean visible)
  {
    dataSet.visible = visible;
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].resetYRangeScroller();
      chartList[i].recalculateAutoYRange();
    }
    repaint();
  }
  
  public void detachAllDataSets()
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].detachAllDataSets();
    }

    
  }
  
  //
  public void setDataIntoChart(Vector dataVector)
  {
  	chartPanel.removeAll();
  	data = dataVector;
  	for(int i = 0; i < totalCharts; i++)
  	{
  		remove(chartList[i]);
  	}
  	int numberOfCharts = dataVector.size();
  	setLayout(new BorderLayout());
  	totalCharts = numberOfCharts;
  	chartElement = new CChart[numberOfCharts];
  	chartList = new CChart[numberOfCharts];
  	//buildLegends();
  	int xPos = 0;
  	int yPos = 0;
  	
  	for(int i = 0; i < numberOfCharts; i++)
  	{
  		
  		chartElement[i] = new CChart("chart " + i, inventoryChartLegend, "x-label" + i, "y-label" + i, false);
  		new DragSourceDropTarget(chartElement[i]);
  		chartElement[i].setShowXRangeScroller(true);
  		chartElement[i].setShowXDividers(true);
  		chartElement[i].setXScrollerRange(new RangeModel(0, 200));
      chartElement[i].setXAxisSigDigitDisplay(1);
      chartElement[i].setToolTipDelay(0);
      chartList[i] = chartElement[i];
  		  		
  		GridBagConstraints constraints = new GridBagConstraints();

	    constraints.gridx = xPos;
	    constraints.gridy = yPos;
	    constraints.gridwidth = 1;
	    constraints.gridheight = 1;
	    constraints.weightx = 2.0;
	    constraints.weighty = 2.0;
	    constraints.fill = GridBagConstraints.BOTH;
	    
	    chartPanel.add(chartElement[i], constraints);
	    if(twoUp)
	    {
		    if(xPos == 1)
	  	  {
	  	    xPos = 0;
	  	    yPos++;
	  	  }
	  	  else
	  	  {
	  	    xPos = 1;
	  	  }
  	  }
  	  else
  	  {
  	  	yPos++;
  	  }
  	  
    }
    
	  validate();
	  
	  displayDataSets(dataVector);
	  setLayout(new BorderLayout());
	  RangeModel range = chartElement[0].getTotalXRange();
    xRC.addPropertyChangeListener("range", new RangeChangeListener(xRC, xMinMax, chartList));
    xRC.getSlider().setOrientation(CMThumbSlider.HORIZONTAL);
    
    xRC.setSliderRange(range.getMin(), range.getMax());     
    xRC.setRange(new RangeModel(10, 30));
    
    setScrollers();
    setXRangeScrollLock(true);
    add(chartPanel, BorderLayout.CENTER);
	  add(xRC, BorderLayout.SOUTH);
	  setShowRightYAxis(false);	
	  
	  	  
  }
  
  public void displayDataSets(Vector myData)
  {
  	for(int i = 0; i < myData.size(); i++)
  	{
  		Vector dataForChart = (Vector)myData.elementAt(i);
  		for(int j = 0; j < dataForChart.size(); j++)
  		{
  			DataSet d = (DataSet)dataForChart.elementAt(j);
  			attachDataSet(d, i);
  			
  		}
  		
  	}
  	resetTR();
  	resetR();
  }
  
  public void attachDataSet(DataSet dataSet, int chartNumber)
  {
    chartList[chartNumber].attachDataSet(dataSet);
    
    //buildLegends();
  }
  
  public Vector getDataSets()
  {
    Vector list = new Vector(0);
    for (int i=0; i<chartList.length; i++)
    {
    	list.add(chartList[i].getDataSets());
    }

    return(list);
  }
  
  
  public void setAutoYRange(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setAutoYRange(value);
    }
  }

  public void setShowTitle(boolean show)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setShowTitle(show);
    }
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

  public void setShowLeftYAxis(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setShowLeftYAxis(value);
    }
  }

  public void setShowRightYAxis(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setShowRightYAxis(value);
    }
  }
  
  
  

  public void setShowYRangeScroller(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setShowYRangeScroller(value);
    }
  }

  public void setShowYRangeTickLabels(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setShowYRangeTickLabels(value);
    }
  }

  public void setYRangeScrollLock(boolean value)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].setYRangeScrollLock(value);
    }
  }
  
  public void propertyChange(PropertyChangeEvent e)
  {
    for (int i=0; i<chartList.length; i++)
    {
      chartList[i].repaint();
    }
  }
  
  private class RangeChangeListener implements PropertyChangeListener
  {
    private CMThumbSliderRangeControl rC = null;
    private double[] minMax = null;
    private CChart[] chartList = null;

    public RangeChangeListener(CMThumbSliderRangeControl rC, double[] minMax, CChart[] chartList)
    {
      RangeChangeListener.this.rC = rC;
      RangeChangeListener.this.minMax = minMax;
      RangeChangeListener.this.chartList = chartList;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
      rangeChanged(rC, minMax, chartList, xScrollSize, xRangeScrollLock, RangeChangeListener.this);
    }
  }
  
  // -----------------------------------------------------------------------------------------------

  private void rangeChanged(CMThumbSliderRangeControl rC, double[] minMax, CChart[] chartList, double scrollSize, boolean scrollLock, PropertyChangeListener listener)
  {
    double currentMin = rC.getRange().getMin();
    double currentMax = rC.getRange().getMax();

    if ((currentMin == currentMax) || ((minMax[0] == currentMin) && (minMax[1] == currentMax)))
    {
      return;
    }

    if (scrollLock)
    {
      rC.removePropertyChangeListener("range", listener);
      if (minMax[0] != currentMin)
      {
        rC.setRange(new RangeModel((int)currentMin, (int)(currentMin + scrollSize)));
      }
      else if (minMax[1] != currentMax)
      {
        rC.setRange(new RangeModel((int)(currentMax - scrollSize), (int)currentMax));
      }
      else
      {
        rC.setRange(new RangeModel((int)minMax[0], (int)minMax[1]));
      }
      rC.addPropertyChangeListener("range", listener);
    }

    minMax[0] = rC.getRange().getMin();
    minMax[1] = rC.getRange().getMax();
    
    
    for(int i = 0; i < chartList.length; i++)
    {
    	chartList[i].setXScrollerRange(new RangeModel((int)minMax[0], (int)minMax[1]));
    }
    
  }
  
  public void resetR()
  {
  	for(int i = 0; i < totalCharts; i++)
  	{
  	  chartList[i].resetRange();
  	}
  }
  public void resetTR()
  {
  	for(int i = 0; i < totalCharts; i++)
  	{
  	  chartList[i].resetTotalRange();
  	}
  }
  
  
  
  public void setXRangeScrollLock(boolean value)
  {
    xRangeScrollLock = value;

    if (xRangeScrollLock)
    {
      xScrollSize = xRC.getRange().getMax() - xRC.getRange().getMin();
    }
  }
  
  
  
  private class DragSourceDropTarget implements DragSource, DropTarget
  {
  		// Drag & Drop supporting class
    private DragAndDropSupport dndSupport = new DragAndDropSupport();
    private Color background = null;
  	CChart chart = null;
  	
  	public DragSourceDropTarget(CChart cChart)
  	{
  		chart = cChart;
  		background = chart.getBackground();
  		// Add the drag source
      dndSupport.addDragSource(this);
     // Add the drop target
      dndSupport.addDropTarget(this);
  	}
  	
  



  // ------------------- DragSource Interface ----------------------------  

	  public Vector getSourceComponents()
	  {
	    Vector components = new Vector(1);
	    components.add(chart);
	    
	    return(components);
	  }
	
	  public boolean dragFromSubComponents()
	  {
	    return(true);
	  }
	
	  public Object getData(Point location)
	  {
	    return(chart.getClosestDataSet(location.x, location.y));
	  }
	
	  public void dragDropEnd(boolean success)
	  {
  	}
	
		// ------------------- DragSource Interface ----------------------------  
	
	  public Vector getTargetComponents()
	  {
	    Vector components = new Vector(1);
	    components.add(chart);
	    
	    return(components);
	  }
	
	  public boolean dropToSubComponents()
	  {
	    return(true);
	  }
	
	  public boolean readyForDrop(Point location)
	  {
	    return(true);
	  }
	
	  public void showAsDroppable(boolean show, boolean droppable)
	  {
			if(show)
			{
			  if (droppable)
			  {
				  chart.setBackground(Color.green);
			  }
			  else
			  {
				  chart.setBackground(Color.red);
				}
			}
			else
			{
				chart.setBackground(background);
			}
	  }
	
	  public void dropData(Object data)
	  {
	  	try
	  	{
	  	  chart.attachDataSet((DataSet)data);
	  	  DataSet[] dsArray = chart.getDataSets();
	  	  nChartUI.setDataSetMenu();
	  	}
    	  catch(Exception e)
  	  {
	  	e.printStackTrace();
	    }
	  	
	  }
	
	  public Vector getSupportedDataFlavors()
	  {
	    Vector flavors = new Vector(1);
	    flavors.add(ObjectTransferable.getDataFlavor(DataSet.class));
	    
	    return(flavors);
	  }
  	
  }
  
  
  
}
