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

/**
 *  Exceptions raised by QueryInterpreters and their components should be
 *  instances of this class.
 */
public class QueryException extends Exception {
  /**
   *  Construct this QueryException instance with the provided message.
   *  @param s the error message
   */
  public QueryException (String s) {
    super(s);
  }
}