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

package org.cougaar.lib.uiframework.transducer.dbsupport;

import java.util.Date;
import java.text.*;

/**
 *  SqlExecute is a base class containing the commonalities of the SqlInsert
 *  and SqlUpdate classes.  It has methods allowing various types of values to
 *  be associated with field names and inserted into the respective statements.
 *  The implementation of the addLiteral method depends on the nature of the
 *  statement being formed, and thus is an abstract method.  Other insertion
 *  methods rely on its implementation to do their work correctly.
 */
public abstract class SqlExecute extends ConjClause {
  /**
   *  Used to represent the boolean TRUE value
   */
  protected static final String YES = "'Y'";

  /**
   *  Used to represent the boolean FALSE value
   */
  protected static final String NO = "'N'";

  /**
   *  A formatter that expresses Dates with accuracy to the nearest second.  A
   *  corresponding oracle date format is able to interpret these expressions.
   */
  protected static final SimpleDateFormat hiFiJavaDateFormat =
    new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  /**
   *  An oracle date format that recognizes date expressions accurate to the
   *  nearest second.  A corresponding Java Date formatter produces date
   *  expressions in the appropriate format.
   */
  protected static final String hiFiOracleDateFormat = "'MM/DD/YYYY HH24:MI:SS'";

  /**
   *  A formatter that expresses Dates with accuracy to the nearest day.  A
   *  corresponding oracle date format is able to interpret these expressions.
   */
  protected static SimpleDateFormat loFiJavaDateFormat =
    new SimpleDateFormat( "dd'-'MMM'-'yyyy" );

  /**
   *  An oracle date format that interprets date expressions accurate to the
   *  nearest day.  A corresponding Java Date formatter produces date
   *  expressions in the appropriate format.
   */
  protected static final String loFiOracleDateFormat = "'DD-MON-YYYY'";

  /**
   *  Static method formatDate is a utility function that generates a formatted
   *  date expression from a Date.  It also handles the unpleasantries, such as
   *  nulls and synchronization on the formatter.
   *  @param d a Date to be formatted
   *  @param df the formatter that produces the desired date expressions
   */
  protected static String formatDate (Date d, DateFormat df) {
    if (d == null)
      return null;
    synchronized (df) {
      return df.format(d);
    }
  }

  /**
   *  Create a new executable SQL statement.  The caller must supply the basic
   *  elements of a ConjClause construction, namely a header, a conjunction,
   *  and a boolean indicating whether parentheses are to be included.
   *  @param head the header String
   *  @param conj the conjunction String
   *  @param parens true if and only if the expression should be in parentheses
   */
  protected SqlExecute (String head, String conj, boolean parens) {
    super(head, conj, parens);
  }

  /**
   *  Place a field name and an exact String value into this SQL statement
   *  @param field the name of the field
   *  @param val the String value
   */
  public abstract void addLiteral (String field, String val);

  /**
   *  Associate a numeric value (expressed as a String) with the given column.
   *  Note that if the database is expecting a number and the provided value is
   *  not a number, it may get upset.
   *  @param field the name of the column
   *  @param val a String representation of the numeric value
   */
  public void addNumber (String field, String val) {
    addLiteral(field, val);
  }

  /**
   *  Associate a numeric value (encased in a Number instance) with the given
   *  column.
   *  @param field the name of the column
   *  @param val the Number instance holding the numeric value
   */
  public void addNumber (String field, Number val) {
    addLiteral(field, val.toString());
  }

  /**
   *  Associate a numeric value (in this case a floating-point number) with the
   *  given column.
   *  @param field the name of the column
   *  @param val the numeric value expressed as a double (or float) type
   */
  public void addNumber (String field, double val) {
    addLiteral(field, String.valueOf(val));
  }

  /**
   *  Associate a numeric value (in this case an integer) with the given column.
   *  @param field the name of the column
   *  @param val the numeric value expressed as a long (or int, short, etc.)
   */
  public void addNumber (String field, long val) {
    addLiteral(field, String.valueOf(val));
  }

  /**
   *  Add a String value to the given column.  The appropriate quotation marks
   *  are automatically supplied.
   *  @param field the name of the column
   *  @param val the value being inserted
   */
  public void addString (String field, String val) {
    addLiteral(field, quote(val));
  }

  /**
   *  Associate a boolean value with the given column.  In this case, the
   *  boolean TRUE or FALSE is encoded as character "Y" or "N", respectively.
   *  @param field the name of the affected column
   *  @param val the boolean value being inserted
   */
  public void addYorN (String field, boolean val) {
    addLiteral(field, (val ? YES : NO));
  }

  /**
   *  Add a "high-fidelity" date value to the statement.  Dates so encoded are
   *  accurate to the nearest second.
   *  @param field the name of the column associated with the date
   *  @param d the Date value being inserted
   */
  public void addHiFiDate (String field, Date d) {
    StringBuffer buf = new StringBuffer("TO_DATE(");
    buf.append(quote(formatDate(d, hiFiJavaDateFormat)));
    buf.append(COMMA);
    buf.append(hiFiOracleDateFormat);
    buf.append(")");
    addLiteral(field, buf.toString());
  }

  /**
   *  Add a "low-fidelity" date value to the statement.  Dates so encoded are
   *  accurate to the nearest day.
   *  @param field the name of the column associates with the date
   *  @param d the Date value being inserted
   */
  public void addLoFiDate (String field, Date d) {
    StringBuffer buf = new StringBuffer("TO_DATE(");
    buf.append(quote(formatDate(d, loFiJavaDateFormat)));
    buf.append(COMMA);
    buf.append(loFiOracleDateFormat);
    buf.append(")");
    addLiteral(field, buf.toString());
  }

  /**
   *  Associate a "null" value with a field in this statement.
   *  @param field the name of the null field
   */
  public void addNull (String field) {
    addLiteral(field, NULL_STRING);
  }

  /**
   *  Associate "null" values with a series of fields as supplied by the caller
   *  @param field an array of names of fields slated to have "null" values
   */
  public void addNulls (String[] field) {
    if (field == null)
      return;

    for (int i = 0; i < field.length; i++)
      addNull(field[i]);
  }
}