
package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

public class ListDimNode extends OrderlessDimNode {
  private Vector membership = new Vector();

  public ListDimNode (String n) {
    super(n);
  }

  public void addMembers (Vector v) {
    membership.addAll(v);
  }

  public void addMember (String s) {
    membership.add(s);
  }

  public void addMembers (String[] a) {
    for (int i = 0; i < a.length; i++)
      membership.add(a[i]);
  }

  public void clearMembers () {
    membership.clear();
  }

  public boolean hasChildren () {
    return membership.size() > 0;
  }

  public Enumeration getChildren () {
    Vector children = new Vector();
    for (Enumeration e = membership.elements(); e.hasMoreElements(); ) {
      DimNode child = new ListDimNode((String) e.nextElement());
      child.setDimension(getDimension());
      children.add(child);
    }
    return children.elements();
  }

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