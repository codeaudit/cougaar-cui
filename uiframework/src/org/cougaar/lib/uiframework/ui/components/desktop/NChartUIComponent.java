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
package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.util.*;

import java.io.Serializable;

import java.awt.dnd.*;

import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.inventory.*;
import org.cougaar.lib.uiframework.ui.components.graph.*;

public class NChartUIComponent extends ComponentFactory implements CougaarDesktopUI
{
  private NChartUI selector = null;
  private Vector persistedData = null;
  public void install(JFrame f)
  {
    throw(new RuntimeException("install(JFrame f) not supported"));
  }

  public void install(JInternalFrame f)
  {
    throw(new RuntimeException("install(JInternalFrame f) not supported"));
  }

  public void install(CDesktopFrame f)
  {
    
    try
    {
    	if(persistedData != null)
    	  selector = new NChartUI(persistedData);
    	else
        selector = new NChartUI(5);
      selector.install(f);
    }
    catch(Exception e)
    {
    	
    }
  }
  
  public String getToolDisplayName()
	{
	  return("NChart UI");
	}
  
  public CougaarDesktopUI create()
	{
	  return(this);
	}
  
  public boolean supportsPlaf()
  {
    return(true);
  }
  
  // #DnD -------------------------------------------------------------------

  public boolean isPersistable()
  {
    return(true);
  }
  
  public Serializable getPersistedData()
  {
  	
  	//  convert data to be persisted to serializable object
    
    Vector persisted = new Vector();
    DataSet[] dataSets = null;
		
		Vector currentChartList = (Vector) selector.chart.getDataSets();
		for(int i = 0; i < currentChartList.size(); i++)
		{
			dataSets = (DataSet[])currentChartList.elementAt(i);
		  Vector persistedElement = new Vector(dataSets.length);
		  for(int j = 0; j < dataSets.length; j++)
		  {
		  	DataSet d = dataSets[j];
		  	ChartPersistentData cpd = new ChartPersistentData(d.getData(), d.dataName);
		    if(dataSets[j] instanceof BarDataSet)
		    {
		    	if(((PolygonFillableDataSet)dataSets[j]).polygonFill)
		    	  cpd.setType("FilledBar");
		    	else
		        cpd.setType("Bar");
		    }
		    else if(dataSets[j] instanceof StepDataSet)
		    {
		    	if(((PolygonFillableDataSet)dataSets[j]).polygonFill)
		    	  cpd.setType("FilledStep");
		    	else
		      	cpd.setType("Step");
		    }
		    else if(dataSets[j] instanceof PolygonFillableDataSet)
		    {
		    	if(((PolygonFillableDataSet)dataSets[j]).polygonFill)
		    	  cpd.setType("FilledLine");
		    	else
		    	  cpd.setType("Line");
		    }
		    
		    persistedElement.add(cpd);
		  }
		  persisted.add(persistedElement);
		}
   
    return persisted;
  }

  public void setPersistedData(Serializable data)
   
  {
  	if(data != null)
  	{
	  	Vector incomingData = (Vector) data;
	  	persistedData = new Vector(incomingData.size());
	  	
	  	for(int i = 0; i < incomingData.size(); i++)
	  	{
	       Vector chartData = (Vector) incomingData.elementAt(i);
	       Vector subVector = new Vector(chartData.size());
	       for(int j = 0; j < chartData.size(); j++)
	       {
	         ChartPersistentData cpd = (ChartPersistentData)chartData.elementAt(j);
	         double[] data1 = cpd.getData();
	         String name = cpd.getName();
	         DataSet d1a = null;
	         try
	         {
	         	if(cpd.getType().equals("Bar"))
	         	  d1a = new BarDataSet(data1, data1.length/2, false, selector.barWidth);
	         	else if(cpd.getType().equals("Step"))
	         	  d1a = new StepDataSet(data1, data1.length/2, false);
	         	else if(cpd.getType().equals("FilledBar"))
	         	  d1a = new BarDataSet(data1, data1.length/2, true, selector.barWidth);
	          else if(cpd.getType().equals("FilledStep"))
	         	  d1a = new StepDataSet(data1, data1.length/2, true);
	         	else
		          d1a = new PolygonFillableDataSet(data1, data1.length/2, false);
		        d1a.dataName = name;
		        subVector.add(d1a);
	         }
	         catch(Exception e)
	         {
	         	  e.printStackTrace();
	         }
	       
	         
	       }
	       persistedData.add(subVector);
	    }
	    
    }
  }
  
  public String getTitle()
  {
    return(null);
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(800, 600));
  }

  public boolean isResizable()
  {
    return(true);
  }

}
