/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

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

  /**
   *  Report the name given to this TableWrapper.  Different tables should have
   *  different names so that they can be told apart.
   *  @return the table's name
   */
  public String getName () {
    return name;
  }

  /**
   *  Retrieve the table wrapped within this TableWrapper.
   *  @return the resident Hashtable
   */
  public Hashtable getTable () {
    return table;
  }

  /**
   *  Wrap a Hashtable with a new TableWrapper and affix the associated name.
   *  @param n the name of the resident table
   *  @param t the Hashtable being wrapped
   */
  public TableWrapper (String n, Hashtable t) {
    table = t;
    name = n;
  }
}