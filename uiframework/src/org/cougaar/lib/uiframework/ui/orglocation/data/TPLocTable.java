
package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.util.*;
import java.io.*;

public class TPLocTable {
  private Hashtable table = new Hashtable();
  private String name = null;

  public TPLocTable (String n) {
    name = n;
  }

  public String getName () {
    return name;
  }

  public boolean isEmpty () {
    return table.isEmpty();
  }

  public void addSchedule (SimpleTPLocation tp) {
    table.put(tp.getName(), tp);
  }

  public SimpleTPLocation removeSchedule (String org) {
    return (SimpleTPLocation) table.remove(org);
  }

  public SimpleTPLocation getSchedule (String org) {
    return (SimpleTPLocation) table.get(org);
  }

  public Enumeration getAllSchedules () {
    return table.elements();
  }

  public void toXml (PrintWriter out) {
    Const.openTag(out, Const.LOC_TABLE);
    for (Iterator i = table.entrySet().iterator(); i.hasNext(); )
      ((SimpleTPLocation) ((Map.Entry) i.next()).getValue()).toXml(out);
    Const.closeTag(out, Const.LOC_TABLE);
    out.flush();
  }
}