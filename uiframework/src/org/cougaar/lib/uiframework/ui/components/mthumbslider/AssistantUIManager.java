/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.components.mthumbslider;

import javax.swing.*;
import javax.swing.plaf.*;

public class AssistantUIManager {

  public static ComponentUI createUI(JComponent c)  {
    String componentName   = c.getClass().getName();

    int index = componentName.lastIndexOf(".") +1;
    StringBuffer sb = new StringBuffer();
    sb.append( componentName.substring(0, index) );

    String lookAndFeelName = UIManager.getLookAndFeel().getName();
    if (lookAndFeelName.startsWith("CDE/")) {
      lookAndFeelName = lookAndFeelName.substring(4,lookAndFeelName.length());
    }
    sb.append( lookAndFeelName );
    sb.append( componentName.substring(index) );
    sb.append( "UI" );

    ComponentUI componentUI = getInstance(sb.toString());

    if (componentUI == null) {
      sb.setLength(0);
      sb.append( componentName.substring(0, index) );
      sb.append( "Basic");
      sb.append( componentName.substring(index) );
      sb.append( "UI" );
      componentUI = getInstance(sb.toString());
    }

    return componentUI;
  }

  private static ComponentUI getInstance(String name) {
    try {
      return (ComponentUI)Class.forName(name).newInstance();
    }
    catch (ClassNotFoundException ex) {
    }
    catch (IllegalAccessException ex) {
      ex.printStackTrace();
    }
    catch (InstantiationException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static void setUIName(JComponent c) {
    String key = c.getUIClassID();
    String uiClassName = (String)UIManager.get(key);

    if (uiClassName == null) {
      String componentName   = c.getClass().getName();
      int index = componentName.lastIndexOf(".") +1;
      StringBuffer sb = new StringBuffer();
      sb.append( componentName.substring(0, index) );
      String lookAndFeelName = UIManager.getLookAndFeel().getName();
      if (lookAndFeelName.startsWith("CDE/")) {
        lookAndFeelName = lookAndFeelName.substring(4,lookAndFeelName.length());
      }
      sb.append( lookAndFeelName );
      sb.append( key );
      UIManager.put(key, sb.toString());
    }
  }

  public AssistantUIManager() {}
}