
package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  The EvaluationLocus class provides the communications protocol between a
 *  GenericInterpreter and its registered QueryAttributes.  In order to provide
 *  an attribute value, the QueryAttribute needs a list of coordinates (with
 *  respect to the QueryDimensions that form its parameter space) along with
 *  associated evaluation hints, such as may be provided in the query.  Each
 *  such coordinate is embodied as a VisitLocus instance.
 */
public class EvaluationLocus {
  private Hashtable coordTable = new Hashtable();
  private VisitLocus[] coordList = null;

  /**
   *  Create a new EvaluationLocus using the specified array to hold the
   *  coordinates.
   *  @param array an array of VisitLocus instances
   */
  public EvaluationLocus (VisitLocus[] array) {
    coordList = array;
  }

  /**
   *  Create a new EvaluationLocus with the specified size.  Initially, all of
   *  the coordinates are null.
   *  @param n the size of the EvaluationLocus
   */
  public EvaluationLocus (int n) {
    coordList = new VisitLocus[n];
  }

  /**
   *  Specify one component of the EvaluationLocus by its index number.
   *  @param n the position within the parameter list
   *  @param p the value being inserted
   */
  public void setCoordinate (int n, VisitLocus p) { 
    coordTable.put(p.location.getDimension().getName(), p);
    coordList[n] = p;
  }

  /**
   *  Retrieve one component of the EvaluationLocus by its dimension's name.
   *  If there is no such component, this method returns null.
   *  <br><br>
   *  Note:  This doesn't work properly for queries where an individual
   *  dimension is used more than once.
   *  @param s the dimension name
   *  @return the coordinate with respect to the specified dimension
   */
  public VisitLocus getCoordinate (String s) {
    return (VisitLocus) coordTable.get(s);
  }

  /**
   *  Retrieve one component of the EvaluationLocus by its positional index
   *  within the coordinate list.
   *  <br><br>
   *  Note:  The order of appearance of the coordinates is dependent on the
   *  order in which they appear in the query.  For implementations that depend
   *  on the coordinates being in a certain order it is also necessary to
   *  enforce the same ordering in the submitted queries.
   *  @param n the position of the desired coordinate
   */
  public VisitLocus getCoordinate (int n) {
    return coordList[n];
  }

  /**
   *  Get an Enumeration containing the coordinates in this EvaluationLocus.
   *  The order in which they are visited is the same as the indexing used in
   *  getCoordinate(int), and is dependent on the query.
   *  @return the list of coordinates
   */
  public Enumeration getCoordinates () {
    return new CoordEnumeration();
  }

  /**
   *  Give the size of this locus, which is the number of coordinates that it
   *  contains.
   *  @return the number of coordinates
   */
  public int size () {
    return coordList.length;
  }

  // An inner class that can iterate through the elements of the associated
  // EvaluationLocus instance.
  private class CoordEnumeration implements Enumeration {
    private int index = 0;

    public boolean hasMoreElements () {
      return index < coordList.length;
    }

    public Object nextElement () {
      return coordList[index++];
    }
  }
}