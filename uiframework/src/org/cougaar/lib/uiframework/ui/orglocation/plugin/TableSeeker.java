
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.uiframework.ui.orglocation.data.TPLocTable;

import org.cougaar.lib.uiframework.ui.orglocation.data.Const;

/**
 *  The predicate that detects the table of Locations.  The same predicate
 *  class is used by LocSchedulePlugIn and the PSP classes that use the table.
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
