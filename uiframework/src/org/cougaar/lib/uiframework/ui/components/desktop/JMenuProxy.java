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

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

/***********************************************************************************************************************
<b>Description</b>: This class is used by the Cougaar Desktop applicaiton when a menu proxy that is to be displayed
                    has a different menu name than what is currently on the Cougaar Desktop applicaiton's menu.  It
                    adds a new menu to the Cougaar Desktop applicaiton current menu bar that represents the currently
                    selected application's menu.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class JMenuProxy extends JMenu implements MenuProxy, ActionListener, ChangeListener, PropertyChangeListener, ContainerListener, MenuListener
{
  private JMenu menu = null;
  private Container parent = null;

  private ContainerListener listener = null;

  private Vector componentList = new Vector(0);

  private boolean alwaysDisabled = false;

  public JMenuProxy(JMenu menu, Container parent, ContainerListener listener)
  {
    super(menu.getText());
//    super(menu.getText(), menu.isTearOff());
    setEnabled(menu.isEnabled());
    setIcon(menu.getIcon());
    setMnemonic(menu.getMnemonic());

    this.menu = menu;
    this.parent = parent;
    this.listener = listener;

    parent.add(this);
    
    Component[] components = menu.getMenuComponents();
    for (int i=0; i<components.length; i++)
    {
      try
      {
        MenuProxy component = MenuProxyRegistry.getProxy(components[i], this, listener);
        componentList.add(component);
      }
      catch (Exception e)
      {
        System.out.println(e);
      }
    }
    
    this.addActionListener(this);
    this.addMenuListener(this);
    
    menu.addChangeListener(this);
//    menu.addPropertyChangeListener(this);
    menu.getPopupMenu().addContainerListener(this);
  }

  public void actionPerformed(ActionEvent e)
  {
    menu.doClick();
//    System.out.println("JMenuProxy: " + menu.hashCode());
  }

  public void menuCanceled(MenuEvent e)
  {
  }

  public void menuDeselected(MenuEvent e)
  {
  }

  public void menuSelected(MenuEvent e)
  {
//    menu.doClick();
  }

  public void stateChanged(ChangeEvent e)
  {
    listener.componentAdded(null);
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    listener.componentAdded(null);
  }

  public void componentAdded(ContainerEvent e)
  {
    listener.componentAdded(null);
  }

  public void componentRemoved(ContainerEvent e)
  {
    listener.componentAdded(null);
  }

  public void setAlwaysDisabled(boolean disabled)
  {
    alwaysDisabled = disabled;
    this.setEnabled(menu.isEnabled());
  }

  public void setEnabled(boolean enabled)
  {
    if (alwaysDisabled)
    {
      super.setEnabled(false);
    }
    else
    {
      super.setEnabled(enabled);
    }
  }

  public void dispose()
  {
    for (int i=0, isize=componentList.size(); i<isize; i++)
    {
      ((MenuProxy)componentList.elementAt(i)).dispose();
    }

    this.removeActionListener(this);
    this.removeMenuListener(this);

    menu.removeChangeListener(this);
//    menu.removePropertyChangeListener(this);
    menu.getPopupMenu().removeContainerListener(this);

    parent.remove(this);
  }
}
