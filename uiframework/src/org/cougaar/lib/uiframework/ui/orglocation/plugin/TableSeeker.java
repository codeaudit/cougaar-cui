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

package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.uiframework.ui.orglocation.data.TPLocTable;

import org.cougaar.lib.uiframework.ui.orglocation.data.Const;

/**
 *  The predicate that detects the table of Locations.  The same predicate
 *  class is used by LocSchedulePlugin and the PSP classes that use the table.
 */
public class TableSeeker implements UnaryPredicate {
  /**
   *  Detect whether the provided object is what we're looking for.  In this
   *  case, we are looking for TableWrappers with the name "OrgLocTable".
   *  @param an Object from the logplan
   *  @return true iff the Object is a TableWrapper named "OrgLocTable"
   */
  public boolean execute (Object obj) {
    return (obj instanceof TPLocTable) &&
      ((TPLocTable) obj).getName().equals(Const.TABLE_NAME);
  }
}
