
package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.transducer.elements.ListElement;

public class OrderlessDimension extends QueryDimension {
  private OrderlessDimNode root = null;

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