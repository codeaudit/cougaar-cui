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

import javax.swing.*;

import org.cougaar.lib.uiframework.ui.components.desktop.dnd.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.inventory.*;
import org.cougaar.lib.uiframework.ui.components.mthumbslider.*;

public class BlackJackUIComponent extends ComponentFactory implements CougaarDesktopUI, DragSource, DropTarget
{
  private InventorySelector selector = null;

  private StartUpInfo startUpInfo = null;

  private CDesktopFrame containingFrame = null;

  private Vector components = new Vector(1);
  private Vector flavors = new Vector(1);

  public void install(CDesktopFrame f)
  {
    containingFrame = f;

    if (startUpInfo == null)
    {
      startUpInfo = new StartUpInfo();
    }

    selector = new InventorySelector(startUpInfo.host, startUpInfo.port, startUpInfo.file, startUpInfo.cluster, startUpInfo.asset, 0, 0);
    selector.install(f);

    setDropTargets(f.getContentPane());

    flavors.add(ObjectTransferable.getDataFlavor(Vector.class));

    DragAndDropSupport dndSupport = new DragAndDropSupport();

    dndSupport.addDragSource(this);
    dndSupport.addDropTarget(this);
  }

  private void setDropTargets(Component parent)
  {
  	if ((parent instanceof Container) && (!(parent instanceof COrderedLabeledMThumbSlider)))
  	{
  		Component[] componentList = ((Container)parent).getComponents();
  		for (int i=0; i<componentList.length; i++)
  		{
  			setDropTargets(componentList[i]);
  		}
  	}
  	
  	if (!(parent instanceof COrderedLabeledMThumbSlider))
  	{
      components.add(parent);
    }
  }

  // ------------- DragSource Support -------------------

  public Vector getSourceComponents()
  {
    return(components);
  }

  public boolean dragFromSubComponents()
  {
    return(false);
  }

  public Object getData(Point location)
  {
    return(selector);
  }

  public void dragDropEnd(boolean success)
  {
  }



  // ------------- DragSource Support -------------------

  public Vector getTargetComponents()
  {
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
    // Do nothing here
  }

  public void dropData(Object droppedData)
  {
    Vector nameList = (Vector)droppedData;
    
    if (nameList.size() <1)
    {
      return;
    }

    StartUpInfo info = new StartUpInfo((StartUpInfo)getPersistedData());
    String factoryName = getClass().getName();

    // Set the first one in the current chart
    selector.setSelectedCluster((String)nameList.elementAt(0));

    for (int i=1, isize=nameList.size(); i<isize; i++)
    {
      // Keep the asset the same, but change the cluster
      info.cluster = (String)nameList.elementAt(i);
      containingFrame.createTool(factoryName, info);
    }
  }

  public Vector getSupportedDataFlavors()
  {
    return(flavors);
  }






	public String getToolDisplayName()
	{
	  return("BlackJack UI");
	}

	public CougaarDesktopUI create()
	{
	  return(this);
	}

  public boolean supportsPlaf()
  {
    return(true);
  }

  public void install(JFrame f)
  {
    throw(new RuntimeException("install(JFrame f) not supported"));
  }

  public void install(JInternalFrame f)
  {
    throw(new RuntimeException("install(JInternalFrame f) not supported"));
  }

  public boolean isPersistable()
  {
    return(true);
  }

  public Serializable getPersistedData()
  {
    startUpInfo.host = selector.getClusterHost();
    startUpInfo.port = selector.getClusterPort();
    startUpInfo.file = selector.getFileName();
    startUpInfo.cluster = selector.getSelectedCluster();
    startUpInfo.asset = selector.getSelectedAsset();

    return(startUpInfo);
  }

  public void setPersistedData(Serializable data)
  {
    startUpInfo = (StartUpInfo)data;
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

class StartUpInfo implements Serializable
{
  public String host = null;
  public String port = null;
  public String file = null;
  public String cluster = null;
  public String asset = null;

  public StartUpInfo()
  {
  }

  public StartUpInfo(String host, String port, String file, String cluster, String asset)
  {
    this.host = host;
    this.port = port;
    this.file = file;
    this.cluster = cluster;
    this.asset = asset;
  }

  public StartUpInfo(StartUpInfo info)
  {
    host = info.host;
    port = info.port;
    file = info.file;
    cluster = info.cluster;
    asset = info.asset;
  }
  
  public String toString()
  {
    return(host + ":" + port + ":" + file + ":" + cluster + ":" + asset);
  }
}
