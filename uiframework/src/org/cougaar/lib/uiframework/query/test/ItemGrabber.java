/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.util.UnaryPredicate;
import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.query.*;

import java.util.*;

public class ItemGrabber extends QueryAttribute {
  private PSP_QueryBase lpSupport = null;

  private static class TableSeeker implements UnaryPredicate {
    public boolean execute (Object o) {
      return (o instanceof PlanTableWrapper) &&
        ((PlanTableWrapper) o).getName().equals("Item Table");
    }
  }
  private UnaryPredicate itemTable = new TableSeeker();

  public ItemGrabber (PSP_QueryBase base) {
    lpSupport = base;
    setName("Quantity");
  }

  public String eval (EvaluationLocus l) throws QueryException {
    VisitLocus coord = l.getCoordinate("Items");
    String name = coord.location.getName();

    Hashtable t = null;
    Object thing = null;
    Collection lpStuff = lpSupport.getFromLogplan(itemTable);
    Iterator i = lpStuff.iterator();
    if (i.hasNext())
      t = ((PlanTableWrapper) i.next()).getContent();
    if (i.hasNext())
      System.out.println("More than one Item Table on the plan--what gives?");

    if (t == null)
      System.out.println("Item Table not found");
    else
      thing = t.get(name);

    if (thing != null)
      return thing.toString();
    return "null";
  }
}