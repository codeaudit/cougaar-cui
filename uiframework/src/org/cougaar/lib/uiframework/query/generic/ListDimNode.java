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
 *  A ListDimNode is the "root" node for an OrderlessDimension, where the name
 *  space is restricted to a finite list of Strings.  A list of the allowed
 *  names is maintained.  When a child is called for, assuming the name is one
 *  of those on the list, a new ListDimNode is created to represent it.  The
 *  new node has an empty membership list, but is endowed with the requested
 *  name.
 */
public class ListDimNode extends OrderlessDimNode {
  private Vector membership = new Vector();

  /**
   *  Create a new ListDimNode with the provided name
   *  @param n the name of this node
   */
  public ListDimNode (String n) {
    super(n);
  }

  /**
   *  Add a Vectorful of names to the list of members
   *  @param v a Vector of String names
   */
  public void addMembers (Vector v) {
    membership.addAll(v);
  }

  /**
   *  Add a single name to the list of members
   *  @param s a String name
   */
  public void addMember (String s) {
    membership.add(s);
  }

  /**
   *  Add an array of names to the membership list
   *  @param a an array of String names
   */
  public void addMembers (String[] a) {
    for (int i = 0; i < a.length; i++)
      membership.add(a[i]);
  }

  /**
   *  Added for the sake of completeness, this method removes all names from
   *  the membership list.
   */
  public void clearMembers () {
    membership.clear();
  }

  /**
   *  Test whether or not this node has children.  Any time its membership list
   *  is non-empty, it potentially has children.
   *  @return true if there are members in the list
   */
  public boolean hasChildren () {
    return membership.size() > 0;
  }

  /**
   *  Return an Enumeration of all children of this node.  Every name on the
   *  membership list is expressed as a new ListDimNode.
   *  @return the virtual child nodes
   */
  public Enumeration getChildren () {
    Vector children = new Vector();
    for (Enumeration e = membership.elements(); e.hasMoreElements(); ) {
      DimNode child = new ListDimNode((String) e.nextElement());
      child.setDimension(getDimension());
      children.add(child);
    }
    return children.elements();
  }

  /**
   *  Fetch a child node by its name.  In this case, the node does not exist
   *  unless called for, so a new virtual child node is created to represent
   *  the named child (if the name is on the membership roster).
   *  @param n the name of the child sought
   *  @return the child node
   */
  public DimNode hasChild (String n) {
    if (n.equals(getName()))
      return this;

    ListDimNode ret = null;
    if (membership.contains(n)) {
      ret = new ListDimNode(n);
      ret.setDimension(getDimension());
    }
    return ret;
  }
}