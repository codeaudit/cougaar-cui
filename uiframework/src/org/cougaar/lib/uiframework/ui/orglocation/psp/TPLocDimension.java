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

package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.util.*;

import org.cougaar.planning.plugin.legacy.PluginDelegate;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.lib.planserver.*;

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;
import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableSeeker;

public class TPLocDimension extends QueryDimension {
  private static UnaryPredicate tableFinder = new TableSeeker();
  private LocRootNode root = new LocRootNode();
  private PluginDelegate plugin = null;
  private boolean initialized = false;

  public TPLocDimension () {
    root.setDimension(this);
  }

  public void setPlugin (PluginDelegate pid) {
    plugin = pid;
  }

  /**
   *  Install a new node as the root of the hierarchy managed by this
   *  dimension.
   *  @param node the new root node
   */
  public void initialize (TPLocTable t) {
    root.setTable(t);
    initialized = true;
  }

  /**
   *  Expose the root of the hierarchical structure used to organize the range
   *  of this dimension.
   *  @return the structure's root node
   */
  public DimNode getRoot () {
    return root;
  }

  /**
   *  Given the ListElement (within a query Structure) that describes the scope
   *  of the query with respect to this dimension, define the structure of the
   *  result.  The DimPath which is returned embodies knowledge about the shape
   *  of the result set as well as the locations within that structure where
   *  calculated data should appear.
   *
   *  @param le the ListElement at the root of this dimension's spec
   *  @return a DimPath instance to define the result set's structure
   */
  public DimPath visitNodes (ListElement le) throws QueryException {
    if (!initialized && plugin != null)
      findTable();

    String q_name = Utilities.findNameAttribute(le);
    String dimenMode = Utilities.findAttribute("mode", le);

    // make room for the results of this operation
    VisitSeq seq = new VisitSeq();
    DimPath dp =
      new DimPath(this, recursiveVisit(root, le, seq, dimenMode), seq);
    dp.setSingleton("singleton".equals(dimenMode));
    return dp;
  }

  private void findTable () {
    Iterator i = plugin.query(tableFinder).iterator();
    if (i.hasNext())
      initialize((TPLocTable) i.next());
  }
}