/* 
 * <copyright> 
 *  Copyright 1997-2001 Clark Software Engineering (CSE)
 *  under sponsorship of the Defense Advanced Research Projects 
 *  Agency (DARPA). 
 *  
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the Cougaar Open Source License as published by 
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).  
 *  
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS  
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR  
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF  
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT  
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT  
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL  
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,  
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR  
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.  
 *  
 * </copyright> 
 */

package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.Point;
import java.awt.Dimension;

import java.io.Serializable;

/***********************************************************************************************************************
<b>Description</b>: This class stores current information about each desktop frame window within the desktop.  It is
                    used by the DesktopInfo class to store/retrieve information about the current application.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class FrameInfo implements java.io.Serializable
{
  protected transient CougaarDesktopUI component = null;
  
  protected Serializable componentData = null;
	protected String componentFactory = null;

	protected Point frameLocation = null;
	protected Dimension componentSize = null;
  protected boolean iconified = false;
  protected boolean selected = false;

	/*********************************************************************************************************************
  <b>Description</b>: Constructs a frame information instance with the specified values.

  <br>
  @param factory Factory name of the CougaarDesktopUI component
  @param location Location of the component's window within the desktop scroll pane
  @param icon Indicator as to whether or not the component's window is iconified
  @param select Indicator as to whether or not the component's window is selected
	*********************************************************************************************************************/
	public FrameInfo(String factory, Point location, boolean icon, boolean select)
	{
		componentFactory = factory;
		component = ComponentFactoryRegistry.getFactory(componentFactory).create();

		frameLocation = location;
		componentSize = component.getPreferredSize();
		iconified = icon;
		selected = select;
	}

	/*********************************************************************************************************************
  <b>Description</b>: Constructs a frame information instance with the specified values.

  <br>
  @param factory Factory name of the CougaarDesktopUI component
  @param data Component application data
  @param location Location of the component's window within the desktop scroll pane
  @param icon Indicator as to whether or not the component's window is iconified
  @param select Indicator as to whether or not the component's window is selected
	*********************************************************************************************************************/
	public FrameInfo(String factory, Serializable data, Point location, boolean icon, boolean select)
	{
	  this(factory, location, icon, select);

		componentData = data;
	}

	/*********************************************************************************************************************
  <b>Description</b>: Returns the CougaarDesktopUI component specified by this information object.

  <br><b>Notes</b>:<br>
	                  - Unless the component has already been created, the component factory is used to create the
	                    component

  <br>
  @return CougaarDesktopUI component specified by this information object
	*********************************************************************************************************************/
  public CougaarDesktopUI getComponent()
  {
    if (component == null)
    {
      component = ComponentFactoryRegistry.getFactory(componentFactory).create();
    }

    return(component);
  }
}
