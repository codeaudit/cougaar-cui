/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
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