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

package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.query.QueryException;

import java.util.*;

/**
 *  The generic query interpreter relies on implementations of this class to
 *  interpret parameter ranges expressed within queries.  Each dimension is
 *  named so that it can be distinguished from others and that queries might be
 *  able to refer to it by name.
 */
public abstract class QueryDimension {
  private String name = null;

  /**
   *  Report the name assigned to this dimension
   *  @return the dimension's name
   */
  public String getName () {
    return name;
  }

  /**
   *  Assign the name by which this dimension will be known in queries
   *  @param n the dimension's name
   */
  public void setName (String n) {
    name = n;
  }

  /**
   *  Expose the root of the hierarchical structure used to organize the range
   *  of this dimension.
   *  @return the structure's root node
   */
  public abstract DimNode getRoot ();

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
  public abstract DimPath visitNodes (ListElement le) throws QueryException;

  /**
   *  <p>
   *  Recursively travers the query spec. in parallel with the structure of this
   *  dimension, matching specified points with corresponding "real" points.
   *  Certain types of traversal modes are recognized by this generic algorithm,
   *  though others may be desirable, depending on the nature of the queries or
   *  parameter spaces.  During the traversal, some of the visited nodes will
   *  be designated (by membership in a list) as those for which values are
   *  requested by the query currently under consideration.
   *  </p><p>
   *  The net result of this procedure is to create a prototype ListElement
   *  hierarchy that can be used to construct a result set for this query.
   *  </p>
   *
   *  @param d_node the "real" point in this dimension corresponding to the spec
   *  @param q_node the requested point as per the query
   *  @param seq the sequence of points for which values were requested
   *  @param inheritedMode the traversal mode of the parent node
   *  @return a ListElement as one part of the prototype for the result set
   *  @throw QueryException if the query does not conform to the dimension
   */
  protected ListElement recursiveVisit (
      DimNode d_node, ListElement q_node, VisitSeq seq, String inheritedMode)
      throws QueryException
  {
    ListElement ret = new ListElement();
    ret.addAttribute(
      new Attribute("name", Utilities.findNameAttribute(q_node)));

    // Note:  It is important that the VisitLocus be added to the sequence
    // before the children are processed
    if (inheritedMode == null)
      inheritedMode = "inherit";
    String mode = Utilities.findAttribute("mode", q_node.getAttribute("agg"));
    if (mode == null)
      mode = inheritedMode;
    else if (mode.equals("singleton"))
      throw new QueryException("Invalid use of \"singleton\" mode");
    if (!mode.equals("this"))
      inheritedMode = mode;

    boolean visit_this = mode.equals("this") || mode.equals("inherit") ||
      mode.equals("all") || mode.equals("this and children") ||
      (mode.equals("all leaves") && !d_node.hasChildren()) ||
      ((mode.equals("expressed leaves") || mode.equals("singleton")) &&
        !q_node.hasChildren());

    boolean absolute_mode = mode.equals("all") || mode.equals("all leaves") ||
      mode.equals("children") || mode.equals("this and children") ||
      mode.equals("subordinates");

    if (mode.equals("subordinates"))
      mode = "all";

    if (visit_this)
      seq.addLocus(new VisitLocus(d_node, ret));

    if (absolute_mode)
      visitAllNodes(d_node, seq, mode, ret);
    else
      visitExpNodes(d_node, q_node, seq, mode, ret);

    return ret;
  }

  /**
   *  Visit the children of this node using one of the query-relative modes,
   *  namely "inherit", "this", and "expressed leaves".
   *  @param d_node the point in the dimension as referenced in the query
   *  @param q_node the reference to d_node within the query
   *  @param seq the sequence of points for which values are required
   *  @param mode the traversal mode currently in effect
   *  @param ret the ListElement to be returned by recursiveVisit (q.v.)
   */
  protected void visitExpNodes (DimNode d_node, ListElement q_node,
      VisitSeq seq, String mode, ListElement ret)
      throws QueryException
  {
    // Now process the child nodes
    for (Enumeration e = q_node.getChildren(); e.hasMoreElements(); ) {
      ListElement q_child = ((Element) e.nextElement()).getAsList();
      if (q_child == null)
        throw new QueryException("Invalid Scope for " + getName() +
          "--non-list element found where a list was expected");
      DimNode d_child = d_node.hasChild(Utilities.findNameAttribute(q_child));
      if (d_child == null)
        System.out.println("Invalid point for " + getName() +
          "--value \"" + Utilities.findNameAttribute(q_child) +
          "\" is out of bounds (ignored)");
      else
        ret.addChild(recursiveVisit(d_child, q_child, seq, mode));

      // check for termination due to "singleton" mode
      if (mode.equals("singleton") && seq.getLength() > 0)
        break;
    }
  }

  /**
   *  Visit the children of this node using one of the absolute modes, namely
   *  "all", "children", and "this and children".
   *  @param d_node the point in the dimension
   *  @param seq the sequence of points for which values are required
   *  @param mode the traversal mode currently in effect
   *  @param ret the ListElement to be returned by recursiveVisit (q.v.)
   */
  protected void visitAllNodes (
      DimNode d_node, VisitSeq seq, String mode, ListElement ret)
  {
    // process child nodes
    for (Enumeration e = d_node.getChildren(); e.hasMoreElements(); ) {
      Object o = e.nextElement();
      DimNode d_child = (DimNode) o; // e.nextElement();
      ret.addChild(absoluteVisit(d_child, seq, mode));
    }
  }

  /**
   *  Traverse the dimension's structure and find nodes for which values should
   *  be computed.  This operation is similar to recursiveVisit (q.v.) except
   *  that it ignores the query and proceeds based on the structure of the
   *  dimension alone.  Normally, this method will be executed when a query
   *  selects a traversal mode that is of the "absolute" variety ("all",
   *  "all leaves", "children", and "this and children").
   *  @param d_node the "real" point in this dimension
   *  @param seq the sequence of points for which values were requested
   *  @param inheritedMode the traversal mode of the parent node
   *  @return a ListElement as one part of the prototype for the result set
   */
  protected ListElement absoluteVisit (
      DimNode d_node, VisitSeq seq, String inheritedMode)
  {
    ListElement ret = new ListElement();
    ret.addAttribute(new Attribute("name", d_node.getName()));

    // Note:  It is important that the VisitLocus be added to the sequence
    // before the children are processed
    String mode = inheritedMode;
    if (mode.equals("children") || mode.equals("this and children"))
      mode = "this";

    boolean visit_this = mode.equals("this") || mode.equals("all") ||
      (mode.equals("all leaves") && !d_node.hasChildren());

    boolean stop_here = mode.equals("this");

    if (mode.equals("subordinates"))
      mode = "all";

    if (visit_this)
      seq.addLocus(new VisitLocus(d_node, ret));

    if (!stop_here)
      visitAllNodes(d_node, seq, mode, ret);

    return ret;
  }
}