
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

  /**
   *  Respond to an incoming query using the local dimensions and attributes
   */
  public Structure query (Structure q) throws QueryException {
    if (q.getAttribute("query") == null)
      throw new QueryException("Invalid Structure--not identified as a query");

    ListElement body = q.getContentList();
    Hashtable fields = parseFields(body.getAttribute("fields"));
    if (fields.size() == 0)
      throw new QueryException("Invalid Query--no recognized return fields");
    Vector singles = new Vector();
    Vector dimens = new Vector();
    parseDimens(body.getAttribute("dimensions"), singles, dimens);
    if (dimens.size() + singles.size() == 0)
      throw new QueryException("Invalid Query--scope is empty");

    // the structure to be returned
    Structure ret = new Structure();
    ret.addAttribute(new Attribute("result set"));
    // vector of current VisitSeq instances
    Vector params = new Vector();
    // a container for the current VisitLocus instances
    EvaluationLocus locus = new EvaluationLocus();

    // first install the singleton coordinates in the locus
    for (Enumeration e = singles.elements(); e.hasMoreElements(); ) {
      DimPath dp = (DimPath) e.nextElement();
      dp.copyPrototypes();
      VisitSeq seq = dp.getEvaluationSeq();
      if (seq.hasCurrent())
        locus.addCoordinate(seq.getCurrentLocus());
      else
        throw new QueryException("Invalid dimension \"" +
          dp.getDimension().getName() + "\"--singleton must not be empty");
    }

    // If all the dimensions are singletons, then get the attribute values and
    // quit.  Otherwise, traverse the explicit dimensions.
    if (dimens.size() == 0) {
      ret.addAttribute(getFieldValues(fields, locus));
      return ret;
    }

    // Start by creating a copy of the outermost dimension.  This will be the
    // the root member within the return structure.
    DimPath dim = (DimPath) dimens.elementAt(0);
    dim.copyPrototypes();
    ret.addChild(dim.getResultSet());
    params.add(dim.getEvaluationSeq());

    // when the outermost dimension is removed from the stack, we are done
    while (params.size() > 0) {
      // Start by checking for the current position in the top dimension on the
      // stack.  If it does not exist, pop the dimension off the stack.  If it
      // does, process that position.
      VisitSeq seq = (VisitSeq) params.get(params.size() - 1);
      if (!seq.hasCurrent()) {
        params.removeElementAt(params.size() - 1);
        if (locus.size() > 0)
          locus.pop();
      }
      else {
        VisitLocus site = seq.getCurrentLocus();
        locus.addCoordinate(site);
        seq.next();
        // if this is the innermost dimension, calculate attribute values and
        // go on to the next site
        if (params.size() == dimens.size()) {
          site.record.addAttribute(getFieldValues(fields, locus));
          locus.pop();
        }
        // If this is not the innermost dimension, create a spot for the minor
        // dimensions and initialize the outermost one.
        else {
          Attribute minors = new Attribute("minor");
          site.record.addAttribute(minors);
          dim = (DimPath) dimens.elementAt(params.size());
          dim.copyPrototypes();
          minors.addChild(dim.getResultSet());
          params.add(dim.getEvaluationSeq());
        }
      }
    }

    return ret;
  }

  // evaluate the attributes for a given locus
  private Attribute getFieldValues (Hashtable fields, EvaluationLocus locus)
      throws QueryException
  {
    Attribute vals = new Attribute("values");
    for (Enumeration e = fields.keys(); e.hasMoreElements(); ) {
      String key = (String) e.nextElement();
      QueryAttribute a = (QueryAttribute) fields.get(key);
      vals.addAttribute(new Attribute(key, a.eval(locus)));
    }
    return vals;
  }

  // read the "fields" clause of the query and assemble a table of Attributes
  // to be calculated for each expressed point in the query's parameter space
  private Hashtable parseFields (ListElement le) throws QueryException {
    Hashtable ret = new Hashtable();
    for (Enumeration e = le.getAttributes(); e.hasMoreElements(); ) {
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
      else
        ret.put(key, field);
    }

    return ret;
  }

  // read the "dimensions" clause of the query and assemble an ordered list of
  // the dimensions (and scopes therein) that define the query's parameter space
  private void parseDimens (ListElement le, Vector singles, Vector dimens)
      throws QueryException
  {
    for (Enumeration e = le.getChildren(); e.hasMoreElements(); ) {
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
      if (dimen != null && specElement != null) {
        DimPath path = dimen.visitNodes(specElement);
        if (path.isSingleton())
          singles.add(path);
        else
          dimens.add(path);
      }
    }
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