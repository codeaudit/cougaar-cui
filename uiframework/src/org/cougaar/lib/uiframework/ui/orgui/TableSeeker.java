
package org.cougaar.lib.uiframework.ui.orgui;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableWrapper;

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