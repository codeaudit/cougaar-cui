/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */

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