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

package org.cougaar.lib.uiframework.query;

import org.cougaar.lib.uiframework.transducer.elements.Structure;

/**
 *  The main interface for the generic query interpretation package.  The one
 *  method specified below is all that is actually required in order to process
 *  queries.  The query, which may arrive in a number of different forms, must
 *  be parsed and assembled into a Structure instance before it can be used by
 *  this interface.  A "generic" implementation, GenericInterpreter (q.v.), is
 *  provided along with a suite of supporting "generic" components intended to
 *  allow rapid deployment for a variety of uses that conform to its paradigm.
 *  Other "generic" models using different paradigms may appear in the future.
 */
public interface QueryInterpreter {
  /**
   *  Process a query Structure, and return the results.  Specialized
   *  instances may implement this method directly, though most will be derived
   *  from the generic implementation which delegates much of the work to a
   *  collection of pluggable components (e.g., instances of QueryAttribute and
   *  QueryDimension).
   *
   *  @param q a Structure containing the query
   *  @return a Structure containing the results
   *  @throw QueryException if a problem is encountered in processing the query
   */
  public Structure query (Structure q) throws QueryException;
}