/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  An orderless discrete dimension can use nodes of this type.  In this
 *  implementation, there is no restriction whatsoever on the virtual hierarchy.
 *  The node does not advertise any children, but it will dynamically create a
 *  child for any name upon request.
 */
public class OrderlessDimNode extends DimNode {
  public OrderlessDimNode (String n) {
    setName(n);
  }

  public boolean hasChildren () {
    return false;
  }

  public DimNode hasChild (String name) {
    OrderlessDimNode node = new OrderlessDimNode(name);
    node.setDimension(getDimension());
    return node;
  }

  public Enumeration getChildren () {
    return empty;
  }

  public static class EmptyEnumeration implements Enumeration {
    public boolean hasMoreElements () {
      return false;
    }

    public Object nextElement () {
      return null;
    }
  }
  private static Enumeration empty = new EmptyEnumeration();
}