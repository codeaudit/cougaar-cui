package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.util.*;

import java.io.Serializable;

import java.awt.dnd.*;

import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.inventory.*;
import org.cougaar.lib.uiframework.ui.components.graph.*;

public class DnDUIComponent extends ComponentFactory implements CougaarDesktopUI
{
  private DnDUI selector = null;
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
    	
      selector = new DnDUI();
      selector.install(f);
    }
    catch(Exception e)
    {
    	
    }
  }
  
  public String getToolDisplayName()
	{
	  return("DnD UI");
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
    return(false);
  }
  
  public Serializable getPersistedData()
  {
  	
  	
    return null;
  }

  public void setPersistedData(Serializable data)
   
  {
  	
  }
  
  public String getTitle()
  {
    return(null);
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(200, 400));
  }

  public boolean isResizable()
  {
    return(true);
  }

}
