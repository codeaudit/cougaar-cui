
package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.util.*;

import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;

public class LocOrgNode extends DimNode {
  private SimpleTPLocation tploc = null;

  public LocOrgNode (SimpleTPLocation tp, QueryDimension d) {
    tploc = tp;
    setName(tploc.getName());
    setDimension(d);
  }

  public boolean hasChildren () {
    return !tploc.isEmpty();
  }

  /**
   *  There is no way to access the schedule elements by name, so this method
   *  always returns null.
   *  @param name the name of the hypothetical child node--ignored.
   *  @return always return null
   */
  public DimNode hasChild (String name) {
    return null;
  }

  public Enumeration getChildren () {
    Vector v = new Vector();
    for (Enumeration e = tploc.getLocationElements(); e.hasMoreElements(); )
      v.add(new LocTimeNode((TimeLocation) e.nextElement(), getDimension()));
    return v.elements();
  }
}