/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;
import java.util.*;

public class Utilities {
  public static String findNameAttribute (ListElement le) {
    try {
      return ((ValElement)
        le.getAttribute("name").getChildren().nextElement()).getValue();
    }
    catch (Exception eek) {
      return null;
    }
  }

  public static String findAttribute (String name, ListElement le) {
    try {
      return ((ValElement)
        le.getAttribute(name).getChildren().nextElement()).getValue();
    }
    catch (Exception eek) {
      return null;
    }
  }

  public static ListElement getFirstListChild (ListElement le) {
    Enumeration children = le.getChildren();
    if (children.hasMoreElements())
      return ((Element) children.nextElement()).getAsList();
    return null;
  }
}