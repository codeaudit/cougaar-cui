
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