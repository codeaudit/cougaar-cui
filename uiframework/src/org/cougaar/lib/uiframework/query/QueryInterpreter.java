/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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
   *  @throws QueryException if a problem is encountered in processing the query
   */
  public Structure query (Structure q) throws QueryException;
}
