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