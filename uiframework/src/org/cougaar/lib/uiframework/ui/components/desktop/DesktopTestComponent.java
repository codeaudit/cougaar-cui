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

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/***********************************************************************************************************************
<b>Description</b>: This class is a general test Cougaar Desktop component.  Is is a simple component created to show
                    what needs to be done to build a desktop component.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class DesktopTestComponent extends ComponentFactory implements CougaarDesktopUI
{
  private int count = 0;

  // Method call order upon creation:
  // getPreferredSize();
  // isResizable();
  // install(CDesktopFrame f);

  // Method call order upon serialization:
  // getPersistedData();
  // frameClosed();

  // Method call order upon rehydration:
  // setPersistedData(Serializable data);
  // install(CDesktopFrame f);
  // frameMinimized();  (If stored as such)
  // frameMaximized();  (If stored as such)

	public String getToolDisplayName()
	{
	  return("Desktop Test Component");
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
    try
    {
      JButton button = new JButton("Press Me!!!");
  		button.addActionListener(new ListenerAction(this, "buttonPressed", new Object[] {f}, ListenerAction.actionPerformed));
  
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(button, BorderLayout.CENTER);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
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
    return("Test Button " + count++);
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(250, 50));
  }

  public boolean isResizable()
  {
    return(true);
  }

  public void buttonPressed(CDesktopFrame frame)
  {
    frame.setTitle("Test Button " + count++);
  }
}
