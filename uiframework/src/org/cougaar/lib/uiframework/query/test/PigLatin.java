/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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