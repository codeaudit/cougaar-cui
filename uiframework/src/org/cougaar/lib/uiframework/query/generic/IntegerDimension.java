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

package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.query.QueryException;
import java.util.*;

/**
 *  <p>
 *  An IntegerDimension is a QueryDimension specialized to handle linear
 *  parameters whose values are integers.  A pseudo-hierarchy is implicit in
 *  the structure, with sets of contiguous values as the nodes and parent-child
 *  relations among nodes defined by superset-subset relations among sets.
 *  </p><p>
 *  For most purposes, this concrete class will suffice for handling parameters
 *  structured like a set of integers.  However, there are conceivably cases
 *  where additional information is required, such as when the numbers
 *  correspond to members of another discrete structure (e.g., a Calendar).
 *  </p>
 */
public class IntegerDimension extends QueryDimension {
  private IntDimNode root = null;

  /**
   *  Expose the root of the structure held by this IntegerDimension.  In this
   *  case, it should be a range expressing the total extent of the dimension.
   *  For an IntegerDimension, the root should be the only node stored on a
   *  permanent basis, though others may be created in response to specific
   *  queries.
   *  @return the root node of this hierarchy
   */
  public DimNode getRoot () {
    return root;
  }

  /**
   *  Install a new node as the root of the hierarchy managed by this
   *  dimension.
   *  @param node the new root node
   */
  public void setRoot (IntDimNode node) {
    root = node;
    root.setDimension(this);
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
    // make room for the results of this operation
    VisitSeq seq = new VisitSeq();
    String rangeSpec = Utilities.findNameAttribute(le);
    DimNode q_root = root.hasChild(rangeSpec);
    if (q_root == null)
      throw new QueryException(
        "Range \"" + rangeSpec + "\" is not valid for " + getName());

    String dimenMode = Utilities.findAttribute("mode", le);

    DimPath dp =
      new DimPath(this, recursiveVisit(q_root, le, seq, dimenMode), seq);
    dp.setSingleton("singleton".equals(dimenMode));
    return dp;
  }
}