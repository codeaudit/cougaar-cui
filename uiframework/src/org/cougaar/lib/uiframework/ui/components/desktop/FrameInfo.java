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
