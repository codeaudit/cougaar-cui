package org.cougaar.lib.uiframework.ui.components.desktop;
import java.io.Serializable;
public class ChartPersistentData implements Serializable
{
	private double[] data;
	private String name = null;
	private String type = null;
	
	public ChartPersistentData(double[] d, String chartName)
	{
		data = d;
		name = chartName;
		
	}
	
	public double[] getData()
	{
		return data;
	}
	
	public String getName()
	{
		return name;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String t)
	{
		type =t;
	}
	
	
	


}