/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
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