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
package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ObjectTransferable implements Transferable
{
  private static DataFlavor javaLocalObjectFlavor = null;
  private static Vector flavorList = new Vector(1);

  static
  {
    try
    {
      javaLocalObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
    }
    catch (ClassNotFoundException e)
    {
      // This should never happen
      e.printStackTrace();
    }
  }
  
  public DataFlavor[] dataFlavorList = null;
  private Object data = null;

  public ObjectTransferable(Object object)
  {
  	data = object;
    
    dataFlavorList = getAllDataFlavors(data.getClass());
  }

  private static final synchronized DataFlavor[] getAllDataFlavors(Class type)
  {
    flavorList.clear();
    // For some reason, we need this flavor to present in the list for this to work
    flavorList.add(javaLocalObjectFlavor);

    addDataFlavors(type);
    
    return((DataFlavor[])flavorList.toArray(new DataFlavor[flavorList.size()]));
  }

  private static final synchronized void addDataFlavors(Class type)
  {
    Class[] interfaces = type.getInterfaces();
    for (int i=0; i<interfaces.length; i++)
    {
      addDataFlavors(interfaces[i]);
    }
    
    if (type.getSuperclass() != null)
    {
      addDataFlavors(type.getSuperclass());
    }
    
    flavorList.add(getDataFlavor(type));
  }

  public synchronized DataFlavor[] getTransferDataFlavors()
  {
    return(dataFlavorList);
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    for (int i=0; i<dataFlavorList.length; i++)
    {
      if (flavor.equals(dataFlavorList[i]))
      {
        return(true);
      }
    }

    return(false);
  }

  public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if (isDataFlavorSupported(flavor))
		{
		  return(data);
		}

	  throw(new UnsupportedFlavorException (flavor));
  }

  public static final DataFlavor getDataFlavor(Class classType)
  {
    DataFlavor flav = null;

/*    try
    {
      /*System.out.println(DataFlavor.javaJVMLocalObjectMimeType);
      System.out.println(DataFlavor.javaRemoteObjectMimeType);
      System.out.println(DataFlavor.javaRemoteObjectMimeType);
      System.out.println(new DataFlavor(classType, classType.getName()).getMimeType());*/

      // application/x-java-jvm-local-objectref
      // application/x-java-remote-object
      // application/x-java-remote-object
      // application/x-java-serialized-object; class=java.lang.String
      flav = new DataFlavor("application/x-java-jvm-local-objectref; class=" + classType.getName(), classType.getName());
      //System.out.println(flav.getMimeType());
/*    }
    catch (ClassNotFoundException e)
    {
      // This shouldn't happen since we already have the class object
      e.printStackTrace();
    }
*/
  	return(flav);
  }
}
