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