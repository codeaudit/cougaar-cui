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

import java.awt.Dimension;
import java.io.Serializable;

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

  public void install(CDesktopFrame f);

  public boolean isPersistable();
  public Serializable getPersistedData();
  public void setPersistedData(Serializable data);

  public String getTitle();
  public Dimension getPreferredSize();
  public boolean isResizable();

/*
  public void frameIconified();
  public void frameDeIconified();
  public void frameMaximized();
  public void frameClosed();
*/
}
