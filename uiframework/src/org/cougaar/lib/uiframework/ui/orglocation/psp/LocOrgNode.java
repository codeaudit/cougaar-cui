/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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