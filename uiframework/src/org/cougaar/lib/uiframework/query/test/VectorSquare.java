/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.lib.uiframework.query.QueryException;
import org.cougaar.lib.uiframework.query.generic.*;
import java.util.*;

public class VectorSquare extends QueryAttribute {
  public VectorSquare () {
    setName("VectorSquare");
  }

  public String eval (EvaluationLocus l) throws QueryException {
    StringBuffer buf = new StringBuffer("(");
    Enumeration e = l.getCoordinates();
    if (e.hasMoreElements()) {
      buf.append(convert((VisitLocus) e.nextElement()));
      while (e.hasMoreElements()) {
        buf.append(", ");
        buf.append(convert((VisitLocus) e.nextElement()));
      }
    }
    buf.append(")");
    return buf.toString();
  }

  private static String convert (VisitLocus o) {
    if (o.location instanceof IntDimNode) {
      IntDimNode intNode = (IntDimNode) o.location;
      if (intNode.hasChildren())
        return "*";
      else
        return String.valueOf(intNode.getStart());
    }
    else {
      return "-";
    }
  }
}