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
import java.io.Serializable;

/***********************************************************************************************************************
<b>Description</b>: This interface is implemented by all applications intended to be used within the Cougaar Desktop
                    environment.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public interface CougaarDesktopUI extends org.cougaar.lib.uiframework.ui.util.CougaarUI
{
//  public static final String COUGAAR_DESKTOP_UI_PROP = "CougaarDesktopUI";

  // Method call order upon creation:
  // getPreferredSize();
  // isResizable();
  // install(CDesktopFrame f);

  // Method call order upon serialization:
  // isPersistable();
  // getPersistedData();  (if persistable)
  // frameClosed();  ????????????????

  // Method call order upon rehydration:
  // isPersistable();
  // setPersistedData(Serializable data);  (if persistable)
  // install(CDesktopFrame f);
  // frameMinimized();  (If stored as such)
  // frameMaximized();  (If stored as such)

	/*********************************************************************************************************************
  <b>Description</b>: Called when a CougaarDesktopUI instance is to add its visual display components to the specified
                      desktop frame.

  <br>
  @param f Desktop frame to disply component within
	*********************************************************************************************************************/
  public void install(CDesktopFrame f);

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the component is persitable.

  <br>
  @return True if the component should be persisted, false otherwise
	*********************************************************************************************************************/
  public boolean isPersistable();

	/*********************************************************************************************************************
  <b>Description</b>: Returns persistable data of the component.

  <br>
  @return Persistable data of the component
	*********************************************************************************************************************/
  public Serializable getPersistedData();

	/*********************************************************************************************************************
  <b>Description</b>: Called when a CougaarDesktopUI instance un-persisted and required to re-initialize itself.

  <br>
  @param data Data to use during re-initialization
	*********************************************************************************************************************/
  public void setPersistedData(Serializable data);

	/*********************************************************************************************************************
  <b>Description</b>: Returns the initial frame window title to be displayed.

  <br>
  @return Title text to display
	*********************************************************************************************************************/
  public String getTitle();

	/*********************************************************************************************************************
  <b>Description</b>: Returns preferred initial window dimensions of this component.

  <br>
  @return Preferred initial window size
	*********************************************************************************************************************/
  public Dimension getPreferredSize();

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the component's window should be resizable.

  <br>
  @return True if the component's window should be resizable, false otherwise
	*********************************************************************************************************************/
  public boolean isResizable();

/*
  public void frameIconified();
  public void frameDeIconified();
  public void frameMaximized();
  public void frameClosed();
*/
}
