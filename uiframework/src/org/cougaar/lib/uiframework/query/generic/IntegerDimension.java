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