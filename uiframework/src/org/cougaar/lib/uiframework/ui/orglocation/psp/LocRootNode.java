
package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.util.*;

import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;

public class LocRootNode extends DimNode {
  private TPLocTable table = null;

  public void setTable (TPLocTable t) {
    table = t;
    setName(t.getName());
  }

  public boolean hasChildren () {
    return table != null && !table.isEmpty();
  }

  public DimNode hasChild (String name) {
    SimpleTPLocation tpl = table.getSchedule(name);
    if (tpl != null)
      return new LocOrgNode(tpl, getDimension());
    return null;
  }

  public Enumeration getChildren () {
    Vector v = new Vector();
    for (Enumeration e = table.getAllSchedules(); e.hasMoreElements(); )
      v.add(new LocOrgNode((SimpleTPLocation) e.nextElement(), getDimension()));
    return v.elements();
  }
}