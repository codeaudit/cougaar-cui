
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

public class TPRelations {
  // in case the relation type is not present, return an empty iterator...
  private static class EmptyIterator implements Iterator {
    public boolean hasNext () {
      return false;
    }

    public Object next () {
      return null;
    }

    public void remove () {
    }
  }
  private static Iterator EMPTY = new EmptyIterator();

  private Hashtable table = new Hashtable();

  private String orgName = null;

  public TPRelations (String name) {
    orgName = name;
  }

  public String getOrgName () {
    return orgName;
  }

  public void addRelation (String rel, String other, long start, long end) {
    RelationTimeMap rtm = (RelationTimeMap) table.get(rel);
    if (rtm == null) {
      rtm = new RelationTimeMap(rel);
      table.put(rel, rtm);
    }
    rtm.addSpan(new RelationSpan(other, start, end));
  }

  public void clearRelationsOfType (String rel) {
    table.remove(rel);
  }

  public void clearRelationsToOrg (String org) {
    for (Enumeration e = table.elements(); e.hasMoreElements(); )
      ((RelationTimeMap) e.nextElement()).clearRelationsToOrg(org);
  }

  public void clear () {
    table.clear();
  }

  public Iterator getAllRelatives (String rel) {
    RelationTimeMap rtm = (RelationTimeMap) table.get(rel);
    if (rtm != null)
      return rtm.getAllRelatives();
    return EMPTY;
  }
}