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

import java.awt.Toolkit;
import java.awt.Rectangle;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.cougaar.lib.uiframework.ui.components.CFrame;

public class DesktopConfig
{
	public static final int desktopWidth = 2000;
	public static final int desktopHeight = 2000;

	public static final int verticalScrollBarUnitIncrement = 50;
	public static final int horizontalScrollBarUnitIncrement = 50;

  private transient CougaarDesktop desktop = null;

  private NVFileReader desktopConfigParameters = null;
  // Desktop config
	public int xLocation = 50;
	public int yLocation = 50;
	public int width = Toolkit.getDefaultToolkit().getScreenSize().width - 100;
	public int height = Toolkit.getDefaultToolkit().getScreenSize().height - 100;

	public boolean autoSaveDesktop = false;
	public boolean snapWindowToCenter = true;

  public String currentLookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

	public String currentDesktopFileName = null;

//	public Vector componentFactoryClass = null;

	public DesktopConfig(String configFileName, CougaarDesktop desktop)
	{
	  this.desktop = desktop;

	  desktopConfigParameters = new NVFileReader(configFileName, "=");

	  xLocation = desktopConfigParameters.getInt("xLocation", xLocation);
	  yLocation = desktopConfigParameters.getInt("yLocation", yLocation);
	  width = desktopConfigParameters.getInt("width", width);
	  height = desktopConfigParameters.getInt("height", height);

	  autoSaveDesktop = desktopConfigParameters.getBoolean("autoSaveDesktop", autoSaveDesktop);
	  snapWindowToCenter = desktopConfigParameters.getBoolean("snapWindowToCenter", snapWindowToCenter);

	  currentLookAndFeel = desktopConfigParameters.getString("currentLookAndFeel", currentLookAndFeel);

	  currentDesktopFileName = desktopConfigParameters.getString("currentDesktopFileName", currentDesktopFileName);

//	  componentFactoryClass = desktopConfigParameters.getStringValues("componentFactoryClass", new Vector(0));

//	  ComponentFactoryRegistry.loadFactoryClasses(componentFactoryClass);
	}

	public void save(String configFileName)
	{
		Rectangle bounds = desktop.getBounds();
		xLocation = bounds.x;
		yLocation = bounds.y;
		width = bounds.width;
		height = bounds.height;

	  desktopConfigParameters.clear();

    desktopConfigParameters.addValue("xLocation", "" + xLocation);
    desktopConfigParameters.addValue("yLocation", "" + yLocation);
    desktopConfigParameters.addValue("width", "" + width);
    desktopConfigParameters.addValue("height", "" + height);

    desktopConfigParameters.addValue("autoSaveDesktop", "" + autoSaveDesktop);
    desktopConfigParameters.addValue("snapWindowToCenter", "" + snapWindowToCenter);

    desktopConfigParameters.addValue("currentLookAndFeel", desktop.getCurrentLookAndFeel());

    desktopConfigParameters.addValue("currentDesktopFileName", currentDesktopFileName);
/*
	  for (int i=0, isize=componentFactoryClass.size(); i<isize; i++)
	  {
      desktopConfigParameters.addValue("componentFactoryClass", (String)componentFactoryClass.elementAt(i));
	  }
*/
	  desktopConfigParameters.saveNVFile(configFileName, "=");
	}
}
