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

public class LocTimeNode extends DimNode {
  private static Enumeration EMPTY = new EmptyEnumeration();
  private static String NAME = "TimeLoc";

  private TimeLocation tloc = null;

  public LocTimeNode (TimeLocation tl, QueryDimension d) {
    tloc = tl;
    setName(NAME);
    setDimension(d);
  }

  public TimeLocation getTimeLoc () {
    return tloc;
  }

  public boolean hasChildren () {
    return false;
  }

  public DimNode hasChild (String name) {
    return null;
  }

  public Enumeration getChildren () {
    return EMPTY;
  }

  private static class EmptyEnumeration implements Enumeration {
    public boolean hasMoreElements () {
      return false;
    }

    public Object nextElement () {
      return null;
    }
  }
}