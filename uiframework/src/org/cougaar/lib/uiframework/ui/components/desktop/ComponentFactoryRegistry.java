package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.io.Serializable;

import java.util.Vector;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class ComponentFactoryRegistry extends ComponentFactory implements CougaarDesktopUI
{
  private static Hashtable factoryHash = new Hashtable(1);
  private static Vector factoryList = new Vector(0);

  public static ComponentFactory getFactory(String name)
  {
    ComponentFactory factory = (ComponentFactory)factoryHash.get(name);
    if (factory == null)
    {
      try
      {
        Class factoryClass = Class.forName(name);
        factory = (ComponentFactory)factoryClass.newInstance();
      }
      catch (Throwable t)
      {
        t.printStackTrace();
        factory = new ComponentFactoryRegistry("Component factory for \"" + name + "\" not available");
      }
    }

    return(factory);
  }

  public static ComponentFactory[] getFactoryList()
  {
    return((ComponentFactory[])factoryList.toArray(new ComponentFactory[factoryList.size()]));
  }






  private String errorMessage = null;

  public ComponentFactoryRegistry(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

	public String getToolDisplayName()
	{
	  return(errorMessage);
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

  public void install(CDesktopFrame f)
  {
    f.getContentPane().setLayout(new BorderLayout());
    f.getContentPane().add(new JLabel(errorMessage), BorderLayout.CENTER);
  }

  public boolean isPersistable()
  {
    return(false);
  }

  public Serializable getPersistedData()
  {
    return(null);
  }

  public void setPersistedData(Serializable data)
  {
  }

  public String getTitle()
  {
    return("Factory Error");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(300, 200));
  }

  public boolean isResizable()
  {
    return(true);
  }
}
