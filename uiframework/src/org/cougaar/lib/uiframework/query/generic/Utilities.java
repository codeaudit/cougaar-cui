
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