
/*
 * <copyright>
 * Copyright 1997-2000 Defense Advanced Research Projects Agency (DARPA)
 * and Clark Software Engineering (CSE) This software to be used in
 * accordance with the COUGAAR license agreement.  The license agreement
 * and other information on the Cognitive Agent Architecture (COUGAAR)
 * Project can be found at http://www.cougaar.org or email: info@cougaar.org.
 * </copyright>
 */package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.util.*;

import java.io.Serializable;

import java.awt.dnd.*;

import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.inventory.*;
import org.cougaar.lib.uiframework.ui.components.graph.*;


/***********************************************************************************************************************
<b>Description</b>: NChartUIComponent GUI component for NChart example.

<br><br><b>Notes</b>:<br>
									Provides the component factory to NChart.

@author Frank Cooley, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/

public class NChartUIComponent extends ComponentFactory implements CougaarDesktopPropertiesUI
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
    	  selector = new NChartUI(persistedData, f);
    	else
        selector = new NChartUI(6, f);
      //selector.install(f);
    }
    catch(RuntimeException e)
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
    return("NChartUI");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(900, 600));
  }

  public boolean isResizable()
  {
    return(true);
  }
  
  public String getProperties()
  {
    String msg = "Author - Frank Cooley";
  	msg += '\n';
  	msg += "Company - Clark Software Engineering LTD.";
  	msg += '\n';
  	msg += "User dir - " + System.getProperty("user.dir");
    return msg;
  }

}
