/* 
 * <copyright>
 *  Copyright 1997-2003 Clark Software Engineering (CSE)
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

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.io.Serializable;

import java.util.Vector;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/***********************************************************************************************************************
<b>Description</b>: This class is used to register all ComponentFactory class names and to produce instances of the
                    factories.  It also provides a implementation of the ComponentFactory/CougaarDesktopUI classes
                    to provide a error display within a desktop frame when the requested component factory can not
                    be created.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class ComponentFactoryRegistry extends ComponentFactory implements CougaarDesktopUI
{
  private static Hashtable factoryHash = new Hashtable(1);
  private static Vector factoryList = new Vector(0);

	/*********************************************************************************************************************
  <b>Description</b>: Returns an instance of the specified component factory.

  <br>
  @param name Fully qualified class name of the required factory
  @return Factory instance of the requested factory, or an error message display factory if the factory could not be
                    created
	*********************************************************************************************************************/
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

	/*********************************************************************************************************************
  <b>Description</b>: Returns a list of all of the current factories available.

  <br>
  @return Array of currently available factories
	*********************************************************************************************************************/
  public static ComponentFactory[] getFactoryList()
  {
    return((ComponentFactory[])factoryList.toArray(new ComponentFactory[factoryList.size()]));
  }






  private String errorMessage = null;

	/*********************************************************************************************************************
  <b>Description</b>: Constructs a ComponentFactory/CougaarDesktopUI component that will display an error.  This type
                      of ComponentFactory/CougaarDesktopUI is used when there is an error with a factory.

  <br>
  @param errorMessage Error message to display
	*********************************************************************************************************************/
  public ComponentFactoryRegistry(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

	/*********************************************************************************************************************
  <b>Description</b>: ComponentFactory Implementation.
	*********************************************************************************************************************/
	public String getToolDisplayName()
	{
	  return(errorMessage);
	}

	/*********************************************************************************************************************
  <b>Description</b>: ComponentFactory Implementation.
	*********************************************************************************************************************/
	public CougaarDesktopUI create()
	{
	  return(this);
	}



	/*********************************************************************************************************************
  <b>Description</b>: CougaarUI Implementation.
	*********************************************************************************************************************/
  public boolean supportsPlaf()
  {
    return(true);
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarUI Implementation.
	*********************************************************************************************************************/
  public void install(JFrame f)
  {
    throw(new RuntimeException("install(JFrame f) not supported"));
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarUI Implementation.
	*********************************************************************************************************************/
  public void install(JInternalFrame f)
  {
    throw(new RuntimeException("install(JInternalFrame f) not supported"));
  }





	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public void install(CDesktopFrame f)
  {
    f.getContentPane().setLayout(new BorderLayout());
    f.getContentPane().add(new JLabel(errorMessage), BorderLayout.CENTER);
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public boolean isPersistable()
  {
    return(false);
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public Serializable getPersistedData()
  {
    return(null);
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public void setPersistedData(Serializable data)
  {
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public String getTitle()
  {
    return("Factory Error");
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public Dimension getPreferredSize()
  {
    return(new Dimension(300, 200));
  }

	/*********************************************************************************************************************
  <b>Description</b>: CougaarDesktopUI Implementation.
	*********************************************************************************************************************/
  public boolean isResizable()
  {
    return(true);
  }
}
