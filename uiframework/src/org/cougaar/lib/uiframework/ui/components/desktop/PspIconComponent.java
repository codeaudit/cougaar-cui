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
import java.awt.Component;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.components.*;

/***********************************************************************************************************************
<b>Description</b>: This class is the implementation of the PSP Icon Cougaar Desktop component.  It provides
                    the display of the PSP Icon map application from within the Cougaar Desktop application.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class PspIconComponent extends ComponentFactory implements DateControllableSliderUI
{
  static
  {
    try
    {
      Class.forName("org.cougaar.lib.uiframework.ui.map.app.ScenarioMap");
    }
    catch (Throwable t)
    {
      throw(new RuntimeException(t.toString()));
    }
  }

	public String getToolDisplayName()
	{
	  return("Psp Icon OpenMap");
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
      CougaarUI ui = (CougaarUI)Class.forName("org.cougaar.lib.uiframework.ui.map.app.ScenarioMap").newInstance();
      ui.install(f);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(new JLabel(t.toString()), BorderLayout.CENTER);
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
    return("Psp Icon OpenMap");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(800, 600));
  }

  public boolean isResizable()
  {
    return(true);
  }

  public SliderProxy getDateControllableSlider()
  {
    return(new CDateSliderProxy(RangeSliderPanel.rangeSlider));
  }
}
