
package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;
import java.util.*;
import org.cougaar.lib.uiframework.query.*;

/**
 *  The GenericInterpreter is an implementation of QueryInterpreter designed
 *  to return tables of results based on the evaluation of "attributes" at
 *  various points in a parameter space defined by one or more "dimensions".
 *  The exact nature of the attributes and dimensions are determined by the
 *  classes that embody their functionality.  When a query references an
 *  attribute or dimension by name, a look-up is used to find the corresponding
 *  implementation.
 */
public class GenericInterpreter implements QueryInterpreter {
  private Hashtable dimensions = new Hashtable();
  private Hashtable attributes = new Hashtable();

  // This inner class represents the parameter space over which a query is to
  // be evaluated.  It has iterator methods that allow the handler to traverse
  // the space and induce calculation of the requisite fields.  Each instance
  // of DimSet can be used once and then must be thrown away.
  private class DimenSet {
    private EvaluationLocus locus = null;
    private DimPath[] paths = null;
    private int majorPath = -1;

    private Structure result = new Structure();
    // Initially, the currentNode is the result set Structure.  Whatever results
    // are generated should be inserted
    private ListElement currentNode = result;
    private boolean still_valid = true;

    /**
     *  Retrieve the Structure built by iterating through the parameter space
     *  and calculating values for the Fields supplied by the handler.
     *  @return the result set Structure
     */
    public Structure getResult () {
      return result;
    }

    /**
     *  Construct an instance of DimenSet by parsing the dimensions clause in
     *  the query.  Once created, it can be made ready for iterating through
     *  its parameter space by calling the init() method.
     *  @param att the part of the query containing the dimension specs
     *  @throws QueryException if the dimension specs are invalid
     */
    public DimenSet (Attribute att) throws QueryException {
      result.addAttribute(new Attribute("result set"));
      paths = new DimPath[att.getChildCount()];
      locus = new EvaluationLocus(paths.length);

      Enumeration e = att.getChildren();
      for (int i = 0; e.hasMoreElements(); i++) {
        ListElement dimSpec = ((Element) e.nextElement()).getAsList();
        String name = null;
        ListElement specElement = null;
        QueryDimension dimen = null;
        if (dimSpec != null) {
          name = Utilities.findNameAttribute(dimSpec);
          specElement = Utilities.getFirstListChild(dimSpec);
        }
        if (name != null)
          dimen = (QueryDimension) dimensions.get(name);
        if (dimen != null && specElement != null)
          paths[i] = dimen.visitNodes(specElement);
        else
          throw new QueryException(
            "unrecognized or badly specified dimension \"" + name + "\"");
      }
    }

    /**
     *  Retrieve the number of dimensions held by this DimenSet
     *  @return the number of dimensions
     */
    public int size () {
      return paths.length;
    }

    /**
     *  Initialize this DimenSet so that it is ready to begin iterating through
     *  the parameter space it represents.  If the operation fails, then it is
     *  because one of the dimensions is empty, and a QueryException is thrown.
     *  @throws QueryException if the parameter space contains no valid points
     */
    public void init () throws QueryException {
      boolean foundMajor = false;
      for (int index = 0; index < paths.length; index++) {
        DimPath path = paths[index];
        path.copyPrototypes();
        if (!path.getEvaluationSeq().hasCurrent())
          throw new QueryException("Scope empty for dimension " +
            path.getDimension().getName());

        VisitLocus vl = path.getEvaluationSeq().getCurrentLocus();
        locus.setCoordinate(index, vl);
        if (!path.isSingleton()) {
          if (!foundMajor) {
            foundMajor = true;
            addMajorDimension(path.getResultSet());
          }
          else {
            addMinorDimension(path.getResultSet());
          }
          currentNode = vl.record;
        }
      }
    }

    /**
     *  Report to the caller whether or not the current configuration of this
     *  DimenSet is valid.  If it is, then field values can be calculated by
     *  calling eval().
     */
    public boolean isValidLeaf () {
      return still_valid;
    }

    /**
     *  Find the next point in the parameter space where the fields should be
     *  evaluated.  Before requesting evaluation, the handler must check to make
     *  sure that the operation succeeded by calling isValidLeaf().
     */
    public void nextLeaf () {
      int index;
      for (index = paths.length - 1; index >= 0; index--) {
        paths[index].getEvaluationSeq().next();
        if (paths[index].getEvaluationSeq().hasCurrent())
          break;
      }

      if (index < 0) {
        // If no such dimension is found, then we have traversed the entire
        // parameter space.  No more valid points are available.
        still_valid = false;
      }
      else if (index < paths.length - 1) {
        // Otherwise, move forward by creating fresh copies of the minor
        // dimensions, if any.  When the operation terminates, currentNode
        // should hold a reference to the ListElement into which values should
        // be inserted next, and locus should be the point in the parameter
        // space at which those values should be calculated.
        VisitLocus vl = paths[index].getEvaluationSeq().getCurrentLocus();
        currentNode = vl.record;
        locus.setCoordinate(index, vl);
        for (index++; index < paths.length; index++) {
          paths[index].copyPrototypes();
          vl = paths[index].getEvaluationSeq().getCurrentLocus();
          locus.setCoordinate(index, vl);
          if (!paths[index].isSingleton()) {
            addMinorDimension(paths[index].getResultSet());
            currentNode = vl.record;
          }
        }
      }
      else {
        VisitLocus vl = paths[index].getEvaluationSeq().getCurrentLocus();
        currentNode = vl.record;
        locus.setCoordinate(index, vl);
      }
    }

    // Insert a "minor" dimension into the result set, using the syntax
    // prescribed by the GenericInterpreter language.  It is inserted into its
    // container as the child of an Attribute named "minor".
    private void addMinorDimension (ListElement elt) {
      Attribute minors = new Attribute("minor");
      currentNode.addAttribute(minors);
      minors.addChild(elt);
    }

    // Insert a "major" dimension into the result set.  In each result, there
    // should be at most one of these, it's parent must be the result set
    // Structure, and it is inserted as a child ListElement.
    private void addMajorDimension (ListElement elt) {
      currentNode.addChild(elt);
    }

    /**
     *  Retrieve a set of values for the fields specified and install them
     *  within the burgeoning result set Structure.  The syntax is part of the
     *  GenericInterpreter language.
     *  @param f a set of fields to be evaluated
     */
    public void eval (FieldSet f) throws QueryException {
      Attribute vals = new Attribute("values");
      for (Enumeration e = f.getFields(); e.hasMoreElements(); ) {
        FieldRec rec = (FieldRec) e.nextElement();
        vals.addAttribute(new Attribute(rec.returnName, rec.field.eval(locus)));
      }
      currentNode.addAttribute(vals);
    }
  }

  // A record containing the QueryAttribute and the name by which it should be
  // called in the result set.  This class serves as the medium of communication
  // between a FieldSet and a DimenSet.
  private static class FieldRec {
    public QueryAttribute field = null;
    public String returnName = null;

    public FieldRec (QueryAttribute f, String n) {
      field = f;
      returnName = n;
    }
  }

  // This inner class represents a set of fields to be computed at various
  // points in a specified parameter space.  An instance creates itself from the
  // "fields" specification found in the query
  private class FieldSet {
    private Vector fields = new Vector();

    /**
     *  Create a new FieldSet instance by parsing the fields clause in the
     *  query.
     *  @param att the part of the query containing the field specs
     *  @throws QueryException if the query specs are invalid
     */
    public FieldSet (Attribute att) throws QueryException {
      for (Enumeration e = att.getAttributes(); e.hasMoreElements(); ) {
        Attribute spec = (Attribute) e.nextElement();
        String key = spec.getName();
        String attName = key;
        Enumeration children = spec.getChildren();
        ValElement val = null;
        if (children.hasMoreElements())
          val = ((Element) children.nextElement()).getAsValue();
        if (val != null)
          attName = val.getValue();
        QueryAttribute field = (QueryAttribute) attributes.get(attName);
        if (field == null)
          throw new QueryException("Invalid Query--attribute " + attName +
            " is not defined");

        fields.add(new FieldRec(field, key));
      }
    }

    /**
     *  Tell whether any fields were gathered during the parsing phase.  If
     *  not, then there is no point in using this FieldSet to answer a query.
     *  @return true iff there are no fields in this FieldSet
     */
    public boolean isEmpty () {
      return fields.size() == 0;
    }

    /**
     *  Produce an Enumeration of the fields contained in this FieldSet
     *  @return the fields
     */
    public Enumeration getFields () {
      return fields.elements();
    }
  }
   

  /**
   *  Respond to an incoming query using the local dimensions and attributes
   *  @param q the query
   *  @return the result set
   *  @throws QueryException if the query is invalid or cannot be answered
   */
  public Structure query (Structure q) throws QueryException {
    if (q.getAttribute("query") == null)
      throw new QueryException("Invalid Structure--not identified as a query");

    ListElement body = q.getContentList();
    FieldSet fields = new FieldSet(body.getAttribute("fields"));
    if (fields.isEmpty())
      throw new QueryException("Invalid Query--no recognized return fields");
    DimenSet dimens = new DimenSet(body.getAttribute("dimensions"));
    if (dimens.size() == 0)
      throw new QueryException("Invalid Query--scope is empty");

    for (dimens.init(); dimens.isValidLeaf(); dimens.nextLeaf())
      dimens.eval(fields);

    return dimens.getResult();
  }

  /**
   *  Install a new dimension into this query interpreter.  The dimension is
   *  tabulated under the name it provides for itself.
   */
  public void addDimension (QueryDimension d) {
    String name = null;
    if (d != null && (name = d.getName()) != null)
      dimensions.put(name, d);
  }

  /**
   *  Install a new attribute into this query interpreter.  The attribute is
   *  tabulated under the name it provides for itself.
   */
  public void addAttribute (QueryAttribute a) {
    String name = null;
    if (a != null && (name = a.getName()) != null)
      attributes.put(name, a);
  }
}