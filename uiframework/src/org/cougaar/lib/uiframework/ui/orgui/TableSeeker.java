/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.orgui;

import org.cougaar.util.UnaryPredicate;

/**
 *  The predicate that detects the table of Relationships amont the
 *  Organizations.  The same predicate class is used by both the OrgSubPlugIn
 *  and the PSP_OrgSub classes, both of which use the table. 
 */
public class TableSeeker implements UnaryPredicate {
  /**
   *  Detect whether the provided object is what we're looking for.  In this
   *  case, we are looking for TableWrappers with the name "OrgRelTable".
   *  @param an Object from the logplan
   *  @return true iff the Object is a TableWrapper named "OrgRelTable"
   */
  public boolean execute (Object obj) {
    return (obj instanceof TableWrapper) &&
      ((TableWrapper) obj).getName().equals(Const.TABLE_NAME);
  }
}