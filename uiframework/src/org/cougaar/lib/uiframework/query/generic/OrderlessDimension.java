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

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.transducer.elements.ListElement;

/**
 *  An OrderlessDimension is a QueryDimension with no particular structure,
 *  though it can still be treated as a hierarchy.  By default, it is assumed
 *  that any String may be the name of a valid node and that any node may be
 *  treated as a child of any other node.  Specialized DimNode subclasses may
 *  provide some restrictions on the name space or parent-child relationships.
 */
public class OrderlessDimension extends QueryDimension {
  private OrderlessDimNode root = null;

  /**
   *  Set the provided OrderlessDimNode as the root of this OrderlessDimension's
   *  membership hierarchy.
   */
  public void setRoot (OrderlessDimNode node) {
    root = node;
    root.setDimension(this);
  }

  /**
   *  Expose the root of the hierarchical structure used to organize the range
   *  of this dimension.  In this case, no organization is imposed.
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