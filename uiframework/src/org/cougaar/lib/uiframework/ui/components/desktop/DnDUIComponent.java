/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
