
package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.query.QueryException;

/**
 *  The QueryAttribute class provides the base for implementations that embody
 *  knowledge of how to calculate values for an attribute at points in its
 *  parameter space.  String names are used to distinguish among the various
 *  attribute implementations and to refer to them from within queries.
 */
public abstract class QueryAttribute {
  private String name = null;

  /**
   *  Specify the name of this attribute as it will be known within queries.
   *  @param n the attribute's name
   */
  public void setName (String n) {
    name = n;
  }

  /**
   *  Retrieve the name of this attribute.  This is useful for tabulation
   *  and easy recognition.
   *  @return the attribute's name
   */
  public String getName () {
    return name;
  }

  /**
   *  Given a location within the parameter space of the attribute, the eval
   *  method finds or calculates the corresponding value.  Each instance is
   *  likely to have a different implementation of this method, which may take
   *  into account specific knowledge about the parameter space(s) over which
   *  the attribute is defined.  If the EvaluationLocus is malformed, or the
   *  attribute is unable to produce a value, then a QueryException is thrown.
   */
  public abstract String eval (EvaluationLocus l) throws QueryException;
}