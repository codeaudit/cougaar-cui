
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

/**
 *
 */
public class RelationTimeMap {
  private Vector relations = new Vector();

  private String type = null;

  public RelationTimeMap (String name) {
    type = name;
  }

  public String getType () {
    return type;
  }

  public void addSpan (RelationSpan span) {
    Vector boneyard = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); ) {
      RelationSpan rs = (RelationSpan) i.next();
      if (span.adjoins(rs)) {
        span.subsume(rs);
        boneyard.add(rs);
      }
    }
    relations.removeAll(boneyard);
    relations.add(span);
  }

  public Vector getRelatives (long t) {
    Vector ret = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); ) {
      RelationSpan span = (RelationSpan) i.next();
      if (span.contains(t))
        ret.add(span.getRelative());
    }

    return ret;
  }

  public Iterator getAllRelatives () {
    Set ret = new HashSet();
    for (Iterator i = relations.iterator(); i.hasNext(); )
      ret.add(((RelationSpan) i.next()).getRelative());
    return ret.iterator();
  }

  public void clearRelationsToOrg (String org) {
    Vector boneyard = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); )
      if (((RelationSpan) i.next()).getRelative().equals(org))
        i.remove();
  }
}