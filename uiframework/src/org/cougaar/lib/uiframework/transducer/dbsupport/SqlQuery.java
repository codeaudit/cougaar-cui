/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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

/**
 *  An SqlQuery is a special case of a ConjClause with exactly four elements,
 *  each of which is itself a ConjClause.  Additional methods provide direct
 *  access to each of the four subclauses.  The subclauses are based on the
 *  features normally found in an SQL statement, namely, a list of selected
 *  fields, a list of tables, an AND-conjoined condition clause, and a list of
 *  sorting keys.
 */
public class SqlQuery extends ConjClause {
  private static final String SELECT = "SELECT";
  private static final String FROM = "from";
  private static final String WHERE = "where";
  private static final String ORDER_BY = "order by";

  private ConjClause selects = null;
  private ConjClause tables = null;
  private ConjClause conditions = null;
  private ConjClause orderings = null;

  /**
   *  Create a new SqlQuery instance.  Initially, the SqlQuery is empty.
   */
  public SqlQuery () {
    super(null, SPACE, false);
    selects = new ConjClause(SELECT, COMMA, false);
    tables = new ConjClause(FROM, COMMA, false);
    conditions = new ConjClause(WHERE, " and ", false);
    orderings = new ConjClause(ORDER_BY, COMMA, false);

    add(selects);
    add(tables);
    add(conditions);
    add(orderings);
  }

  /**
   *  Add a selected field to this query
   *  @param s an object whose String representation is the field name
   */
  public void addSelection (Object s) {
    selects.add(s);
  }

  /**
   *  Add a table to this SqlQuery
   *  @param s an object whose String representation is the table name
   */
  public void addTable (Object s) {
    tables.add(s);
  }

  /**
   *  Add a condition to this SqlQuery.  This may, for instance, be another
   *  ConjClause.
   *  @param s an object whose toString method gives an SQL logical expression
   */
  public void addCondition (Object s) {
    conditions.add(s);
  }

  /**
   *  Add a sorting key to this query.
   *  @param s an object whose String representation is a sorting key
   */
  public void addOrdering (Object s) {
    orderings.add(s);
  }

  /**
   *  Clear this SqlQuery in preparation for making a new query
   */
  public void clear () {
    selects.clear();
    tables.clear();
    conditions.clear();
    orderings.clear();
  }
}