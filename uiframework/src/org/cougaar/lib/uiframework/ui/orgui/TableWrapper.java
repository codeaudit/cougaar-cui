
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

/**
 *  An instance of this class serves as a container for a Hashtable floating on
 *  the logplan.  It has a name attribute so that subscribers can tell which
 *  table is which, in case there is more than one of them. 
 */
public class TableWrapper {
  private Hashtable table = null;
  private String name = null;

  public String getName () {
    return name;
  }

  public Hashtable getTable () {
    return table;
  }

  public TableWrapper (String n, Hashtable t) {
    table = t;
    name = n;
  }
}