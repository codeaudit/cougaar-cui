/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
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