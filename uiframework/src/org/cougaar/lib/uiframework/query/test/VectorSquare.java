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