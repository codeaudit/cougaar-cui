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

/**
 *  HundredMinusSum is a testing module for testing an integer-based linear
 *  parameter.
 */
public class HundredMinusSum extends QueryAttribute {
  public HundredMinusSum () {
    setName("HundredMinusSum");
  }
  
  public String eval (EvaluationLocus l) throws QueryException {
    int s = 100;
    VisitLocus vl = null;
    for (Enumeration coords = l.getCoordinates(); coords.hasMoreElements(); ) {
      vl = (VisitLocus) coords.nextElement();
      if (vl.location instanceof IntDimNode)
        break;
    }
    if (vl != null) {
      DimNode n = vl.location;
      if (n.hasChildren()) {
        for (Enumeration e = n.getChildren(); e.hasMoreElements(); ) {
          DimNode child = (DimNode) e.nextElement();
          try {
            s -= Integer.parseInt(child.getName());
          }
          catch (Exception bad_number) {
            System.out.println("HundredMinusSum:  Not a number: \"" +
              child.getName() + "\"");
          }
        }
      }
      else {
        s -= Integer.parseInt(n.getName());
      }
    }
    return String.valueOf(s);
  }
}