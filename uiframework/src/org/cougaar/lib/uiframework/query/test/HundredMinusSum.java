
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