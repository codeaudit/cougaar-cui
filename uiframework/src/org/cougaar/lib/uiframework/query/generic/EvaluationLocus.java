
package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  The EvaluationLocus class provides the communications protocol between a
 *  GenericInterpreter and its registered QueryAttributes.  In order to provide
 *  an attribute value, the QueryAttribute needs a list of coordinates (with
 *  respect to the QueryDimensions that form its parameter space) along with
 *  associated evaluation hints, such as may be provided in the query.  Each
 *  such coordinate is embodied as a DimPath instance.
 */
public class EvaluationLocus {
  private Hashtable coordTable = new Hashtable();
  private Vector coordList = new Vector();

  public void addCoordinate (VisitLocus p) {
    // System.out.println("EvaluationLocus::addCoordinate...");
    if (p == null)
      System.out.println("  -> p is null!");
    else if (p.location == null)
      System.out.println("  -> p.location is null!");
    else if (p.location.getDimension() == null)
      System.out.println("  -> p.location.getDimension() is null!");
    else if (p.location.getDimension().getName() == null)
      System.out.println("  -> p.location.getDimension().getName() is null!");
    // else
    //   System.out.println("  -> Everything seems okay.");
    coordTable.put(p.location.getDimension().getName(), p);
    coordList.add(p);
  }

  public VisitLocus getCoordinate (String s) {
    return (VisitLocus) coordTable.get(s);
  }

  public Enumeration getCoordinates () {
    return coordList.elements();
  }

  public void pop () {
    VisitLocus last = (VisitLocus) coordList.elementAt(coordList.size() - 1);
    coordList.remove(last);
    coordTable.remove(last.location.getDimension().getName());
  }

  public int size () {
    return coordList.size();
  }
}