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

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class NVFileReader
{
  private Hashtable nvPairs = new Hashtable(1);
  
	public NVFileReader(String fileName, String token)
	{
	  loadNVFile(fileName, token);
	}

  public void loadNVFile(String fileName, String token)
  {
    try
    {
      String line = null;
      StringTokenizer stok = null;
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

      while ((line = br.readLine()) != null)
      {
        if (line.trim().length() == 0)
        {
          continue;
        }

        stok = new StringTokenizer (line, token);
        addValue(stok.nextToken().trim(), stok.nextToken().trim());
      }

      br.close();
    }
		catch(FileNotFoundException e)
		{
		}
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  public void saveNVFile(String fileName, String token)
  {
    try
    {
      Vector values = null;
      String parameter = null;
      FileWriter fw = new FileWriter(fileName);

      for (Enumeration e=nvPairs.keys(); e.hasMoreElements();)
      {
        parameter = (String) e.nextElement();
        values = (Vector)nvPairs.get(parameter);
        for (int i=0, isize=values.size(); i<isize; i++)
        {
          fw.write(parameter);
          fw.write(token);
          fw.write((String)values.elementAt(i));
          fw.write("\n");
        }
      }
      
      fw.close();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  public void clear()
  {
    nvPairs.clear();
  }

  public void addValue(String parameter, String value)
  {
    if (value == null)
    {
      return;
    }

    Vector values = null;
    if ((values = (Vector)nvPairs.get(parameter)) == null)
    {
      values = new Vector(1);
      nvPairs.put(parameter, values);
    }

    values.add(value);
  }

  public String getValueAt(String parameter, int index)
  {
	  Vector values = (Vector)nvPairs.get(parameter);
	  if (values == null)
	  {
	    throw(new RuntimeException("Parameter '" + parameter + "' does not exist"));
	  }

    return((String)values.elementAt(index));
  }

	public int getInt(String parameter, int defaultValue)
	{
	  int value = defaultValue;

	  try
	  {
	    value = Integer.parseInt(getValueAt(parameter, 0));
	  }
	  catch (Throwable t)
	  {
	  }

    return(value);
	}

	public boolean getBoolean(String parameter, boolean defaultValue)
	{
	  boolean value = defaultValue;

	  try
	  {
	    value = Boolean.valueOf(getValueAt(parameter, 0)).booleanValue();
	  }
	  catch (Throwable t)
	  {
	  }

    return(value);
	}

	public String getString(String parameter, String defaultValue)
	{
	  String value = defaultValue;

	  try
	  {
	    value = getValueAt(parameter, 0);
	  }
	  catch (Throwable t)
	  {
	  }

    return(value);
	}

	public Vector getStringValues(String parameter, Vector defaultValue)
	{
	  Vector values = (Vector)nvPairs.get(parameter);
	  if (values == null)
	  {
	    return(defaultValue);
	  }

    return((Vector)values.clone());
	}
}
