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