
package org.cougaar.lib.uiframework.query.test;

import java.util.*;

public class PlanTableWrapper {
  private Hashtable content = null;
  private String name = null;

  public PlanTableWrapper (String n, Hashtable t) {
    name = n;
    content = t;
  }

  public String getName () {
    return name;
  }

  public Hashtable getContent () {
    return content;
  }
}