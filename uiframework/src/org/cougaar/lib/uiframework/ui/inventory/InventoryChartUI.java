/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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

package org.cougaar.lib.uiframework.ui.inventory;

import java.util.StringTokenizer;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import org.cougaar.util.OptionPane;
import org.cougaar.util.ThemeFactory;

import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.util.CougaarUI;

public class InventoryChartUI implements CougaarUI
{
  public static InventorySelector inventorySelector = null;

  public static final NSNItemUnits itemUnits = new NSNItemUnits("ItemUnits.txt");

  static int lookAheadDays = 0;
  static boolean tableView = false;

  public InventoryChartUI()
  {
  }

  public void startup(String cluster, String asset, long startTime, long endTime, String inHost, String inPort)
  {
    startup(cluster, asset, startTime, endTime, inHost, inPort, 0);
  }

  public void startup(String cluster, String asset, long startTime, long endTime, String inHost, String inPort, int look)
  {
    //  if cluster is null bring up default table
    //  if asset is null tell selector to show first asset
    //  otherwise pass start and end time to selector

    lookAheadDays = look;
    boolean displayFirstAsset = false;
    String fileName = null;
    String clusterHost = inHost;
    String clusterPort = inPort;
    String hp = clusterHost + ":" + clusterPort;
    if(cluster == null)
      callTheSelector(null, hp, cluster, asset, false, startTime, endTime);
    else if(asset == null)
    {
      displayFirstAsset = true;
      callTheSelector(null, hp, cluster, asset, true, startTime, endTime);
    }
    else
      callTheSelector(null, hp, cluster, asset, false, startTime, endTime);
  }


  public void populate(String cluster, String port)
  {
    if(inventorySelector != null)
      inventorySelector.populate(cluster, port);
  }

  /** If called with an argument, then it is the data filename.
   */
  public static void main(String[] args)
  {
    //ThemeFactory.establishMetalTheme();
    boolean buildFile = false;
    String fileName = null;
    String hp = null;
    String clusterHost = "localhost";
    String clusterPort = "8800";
    String thisCluster = null;
    String thisAsset = null;
    long startTime = 0;
    long endTime = 0;
    int argumentSelector = 0;
    String argument = null;
    if (args.length != 0)
       argument = (String)args[argumentSelector].toUpperCase();

    while(argumentSelector < args.length)
    {
      argument = (String)args[argumentSelector].toUpperCase();
      if(argument.equals("F"))
      {
        hp = clusterHost + ":" + clusterPort;
        fileName = args[argumentSelector + 1];
        argumentSelector += 2;
      }

      else if(argument.equals("H"))
      {
        hp = args[argumentSelector + 1];
        argumentSelector += 2;
      }
      else if(argument.equals("L"))
      {
        lookAheadDays = (new Integer(args[argumentSelector + 1])).intValue();
        argumentSelector += 2;
      }
      else if(argument.equals("T"))
      {
        tableView = true;
        argumentSelector += 1;
        
      }
      else if(argument.equals("C"))
      {
        String clusterAsset = args[argumentSelector + 1];
        StringTokenizer st = new StringTokenizer (args[argumentSelector + 1], ".");
        thisCluster = st.nextToken();
        thisAsset = st.nextToken();
        argumentSelector += 2;
      }
      else
      {
        System.out.println("Invalid Argument - " + args[argumentSelector]);
        break;
      }

    }

    callTheSelector(fileName, hp, thisCluster, thisAsset , false, startTime, endTime);

  }

  public static void callTheSelector(String file, String hp, boolean buildFile)
  {
    long startTime = 0;
    long endTime = 0;
    callTheSelector (file, hp, null, null, false, startTime, endTime);
  }

  public static void callTheSelector(String file, String hp, String cluster, String asset, boolean displayFirstAsset, long start, long end)
  {

      String clusterHost = null;
      String clusterPort = null;
      String msg = "Enter cluster Log Plan Server location as host:port";
      String s = null;
      if(hp == null)
         s = getClusterHostPort(clusterHost + ":" + clusterPort, msg);
      else
        s = hp;

     if(s != null)
     {
        s = s.trim();
        if (s.length() != 0)
        {
          int i = s.indexOf(":");
          if (i != -1)
          {
            clusterHost = s.substring(0, i);
            clusterPort = s.substring(i+1);
          }
        }
      }

     
     CFrame frame = new CFrame("Inventory/Capacity", true);
     inventorySelector = new InventorySelector(clusterHost, clusterPort, file, cluster, asset, start, end);
     inventorySelector.install(frame);
     
  }

  public static String getClusterHostPort(String defaultString, Object msg)
  {
    return null;
  }

  public void install(JFrame frame)
  {
    lookAheadDays = 4;
    inventorySelector = new InventorySelector(null, null, null, null, null, 0, 0);
    inventorySelector.install(frame);
  }

  public void install(JInternalFrame frame)
  {
    lookAheadDays = 4;
    inventorySelector = new InventorySelector(null, null, null, null, null, 0, 0);
    inventorySelector.install(frame);
  }

  public boolean supportsPlaf()
  {
    return(inventorySelector.supportsPlaf());
  }
}
