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

import java.util.Vector;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;

public class DesktopInfo implements Serializable
{
  private static final String[] factoryList = new String[]
    {
      "org.cougaar.lib.uiframework.ui.components.desktop.DesktopTestComponent",
//      "My Imaginary Class Name",
      "org.cougaar.lib.uiframework.ui.components.desktop.NChartUIComponent",
      "org.cougaar.lib.uiframework.ui.components.desktop.BlackJackUIComponent",
      "org.cougaar.lib.uiframework.ui.components.desktop.dnd.DnDSourceTestGUI",
      "org.cougaar.lib.uiframework.ui.components.desktop.dnd.DnDTargetTestGUI",
      "org.cougaar.lib.uiframework.ui.components.desktop.PspIconComponent"
    };
  
	public String desktopName = "Cougaar Desktop";

	public int scrollPaneVerticalPosition = 0;
	public int scrollPaneHorizontalPosition = 0;

	public String backgroundImage = "images/CougaarLogo.gif";
	public boolean tiledBackground = false;

	public Vector componentFactories = new Vector(0);

  public FrameInfo[] frameInfoList = null;

  public DesktopInfo()
  {
    for (int i=0; i<factoryList.length; i++)
    {
      componentFactories.add(factoryList[i]);
    }
  }
  
	public void save(String fileName) throws IOException
	{
  	FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
		fos.close();
	}

	public static DesktopInfo load(String fileName) throws IOException
	{
	  DesktopInfo desktopInfo = null;

	  try
	  {
  		FileInputStream fis = new FileInputStream(fileName);
  		ObjectInputStream ois = new ObjectInputStream(fis);
      desktopInfo = (DesktopInfo)ois.readObject();
      fis.close();
      ois.close();
    }
    // We're already in the class so this exception will never occur
    catch (ClassNotFoundException e)
    {
    }

		return(desktopInfo);
	}
}
