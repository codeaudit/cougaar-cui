
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