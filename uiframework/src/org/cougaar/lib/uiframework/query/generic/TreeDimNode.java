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

public class TreeDimNode extends DimNode {
  private Hashtable childMap = new Hashtable();
  private Vector childList = new Vector();

  public void addChild (TreeDimNode node) {
    String name = node.getName();
    TreeDimNode current = (TreeDimNode) childMap.get(name);
    if (current != null)
      childList.setElementAt(node, childList.indexOf(current));
    else
      childList.add(node);
    childMap.put(name, node);
  }

  public void removeChild (String s) {
    TreeDimNode node = (TreeDimNode) childMap.remove(s);
    if (node != null)
      childList.remove(node);
  }

  public void removeChildAt (int i) {
    TreeDimNode node = (TreeDimNode) childList.elementAt(i);
    childList.remove(node);
    childMap.remove(node.getName());
  }

  public boolean hasChildren () {
    return childList.size() > 0;
  }

  public DimNode hasChild (String name) {
    return (DimNode) childMap.get(name);
  }

  public Enumeration getChildren () {
    return childList.elements();
  }
}