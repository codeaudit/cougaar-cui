/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.transducer.dbsupport;

/**
 *  Generate the SQL for a delete statement.
 */
public class SqlDelete extends ConjClause {
  protected ConjClause table = null;
  protected ConjClause conditions = null;

  public SqlDelete () {
    super(null, SPACE, false);
    table = new ConjClause("from", COMMA, false);
    conditions = new ConjClause("where", " and ", false);

    add("DELETE");
    add(table);
    add(conditions);
  }

  public void setTable (String s) {
    table.clear();
    table.add(s);
  }

  public void addCondition (String s) {
    conditions.add(s);
  }

  public void clear () {
    conditions.clear();
  }
}