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