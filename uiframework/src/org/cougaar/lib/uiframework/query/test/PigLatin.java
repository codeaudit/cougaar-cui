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

package org.cougaar.lib.uiframework.query.test;
                   
import org.cougaar.lib.uiframework.query.QueryException;
import org.cougaar.lib.uiframework.query.generic.*;
import java.util.*;

/**
 *  PigLatin is an Attribute of one hierarchical argument; its value is the
 *  dimension name and node name converted to Pig Latin.  This Attribute is to
 *  be used for testing purposes only.
 */
public class PigLatin extends QueryAttribute {
  public PigLatin () {
    setName("PigLatin");
  }

  public String eval (EvaluationLocus locus) throws QueryException {
    StringBuffer buf = new StringBuffer();
    Enumeration e = locus.getCoordinates();
    if (e.hasMoreElements()) {
      buf.append(pigLatinNode(((VisitLocus) e.nextElement()).location));
      while (e.hasMoreElements()) {
        buf.append("; ");
        buf.append(pigLatinNode(((VisitLocus) e.nextElement()).location));
      }
    }
    return buf.toString();
  }

  private static String pigLatinNode (DimNode n) {
    return latinize(n.getDimension().getName()) + " " + latinize(n.getName());
  }

  private static String latinize (String s) {
    if (s == null)
      return "--";
    else {
      StringTokenizer tok = new StringTokenizer(s, " ", false);
      if (tok.hasMoreTokens()) {
        StringBuffer buf = new StringBuffer(latinizeWord(tok.nextToken()));
        while (tok.hasMoreTokens()) {
          buf.append(" ");
          buf.append(latinizeWord(tok.nextToken()));
        }
        return buf.toString();
      }
      else {
        return "ay";
      }
    }
  }

  private static String latinizeWord (String s) {
    return s.substring(1) + s.substring(0, 1) + "ay";
  }
}