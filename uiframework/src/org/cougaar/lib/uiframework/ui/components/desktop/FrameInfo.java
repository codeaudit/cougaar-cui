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

import java.awt.Point;
import java.awt.Dimension;

import java.io.Serializable;

public class FrameInfo implements java.io.Serializable
{
  protected transient CougaarDesktopUI component = null;
  
  protected Serializable componentData = null;
	protected String componentFactory = null;

	protected Point frameLocation = null;
	protected Dimension componentSize = null;
  protected boolean iconified = false;
  protected boolean selected = false;

	public FrameInfo(String factory, Point location, boolean icon, boolean select)
	{
		componentFactory = factory;
		component = ComponentFactoryRegistry.getFactory(componentFactory).create();

		frameLocation = location;
		componentSize = component.getPreferredSize();
		iconified = icon;
		selected = select;
	}

	public FrameInfo(String factory, Serializable data, Point location, boolean icon, boolean select)
	{
	  this(factory, location, icon, select);

		componentData = data;
	}

  public CougaarDesktopUI getComponent()
  {
    if (component == null)
    {
      component = ComponentFactoryRegistry.getFactory(componentFactory).create();
    }

    return(component);
  }
}
