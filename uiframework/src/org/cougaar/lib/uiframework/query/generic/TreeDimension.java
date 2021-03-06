/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.query.QueryException;

import java.util.*;

/**
 *  TreeDimension implements the basic functionality of a node-based,
 *  hierarchical dimension.  Subclasses can implement any special features
 *  desired in a particular dimension, though in most cases (i.e., when the
 *  members of the hierarchy need to carry more information) it will suffice to
 *  extend the TreeDimNode class.
 */
public class TreeDimension extends QueryDimension {
  private TreeDimNode root = null;

  /**
   *  Install the hierarchy of nodes that represent this dimension.
   */
  public void setRoot (TreeDimNode r) {
    root = r;
    initTree(root);
  }

  /**
   *  Recursively initialize nodes in this tree.  In particular, set their
   *  dimension pointer to this.
   */
  protected void initTree (TreeDimNode node) {
    node.setDimension(this);
    for (Enumeration e = node.getChildren(); e.hasMoreElements(); )
      initTree((TreeDimNode) e.nextElement());
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
    String q_name = Utilities.findNameAttribute(le);
    if (!root.getName().equals(q_name))
      throw new QueryException("Invalid Scope for " + getName() +
        "--bad root node:  " + q_name);

    String dimenMode = Utilities.findAttribute("mode", le);

    // make room for the results of this operation
    VisitSeq seq = new VisitSeq();
    DimPath dp =
      new DimPath(this, recursiveVisit(root, le, seq, dimenMode), seq);
    dp.setSingleton("singleton".equals(dimenMode));
    return dp;
  }

  /**
   *  Use a Structure (possibly read from a configuration file) to populate
   *  this parameter space.
   */
  public void configure (Structure s) {
    ListElement le = s.getContentList();
    if (le != null)
      setRoot(recursiveConfigure(le));
    else
      root = null;
  }

  /**
   *  Recursively traverse the provided ListElement hierarchy and create a
   *  hierarchy of TreeDimNodes with parallel structure.
   */
  protected TreeDimNode recursiveConfigure (ListElement le) {
    TreeDimNode ret = configureNode(le);
    for (Enumeration e = le.getChildren(); e.hasMoreElements(); ) {
      ListElement child = ((Element) e.nextElement()).getAsList();
      if (child != null)
        ret.addChild(recursiveConfigure(child));
    }
    return ret;
  }

  /**
   *  Construct a single TreeDimNode from a single ListElement found.  Override
   *  this method to control the class of TreeDimNodes produced and the data
   *  with which they are populated.
   */
  protected TreeDimNode configureNode (ListElement le) {
    TreeDimNode ret = new TreeDimNode();
    ret.setName(Utilities.findNameAttribute(le));
    return ret;
  }
}