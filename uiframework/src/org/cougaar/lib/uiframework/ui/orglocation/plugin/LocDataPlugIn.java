
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;
import java.util.*;

import org.cougaar.lib.uiframework.ui.orglocation.data.*;

/**
 *  This PlugIn manages a time-phased location model for a collection of
 *  organizations.  A table of such models is maintained and keyed using the
 *  names of the participant organizations.
 */
public class LocDataPlugIn extends SimplePlugIn {
  // a table of SimpleTPLocations
  private Hashtable tplTable = null;

  // filter for data pertaining to the location of an organization
  private static class LocDataSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      return obj instanceof LocData;
    }
  }
  private static UnaryPredicate locData = new LocDataSeeker();

  private static class MyTableSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      return (obj instanceof TableWrapper) &&
        ((TableWrapper) obj).getName().equals("OrgLocTable");
    }
  }
  private static UnaryPredicate findLocTable = new MyTableSeeker();

  // subscribe to the locData and findLocTable predicates
  private IncrementalSubscription locSubs = null;
  private IncrementalSubscription tableSubs = null;

  /**
   *  Subscribe to the logplan.  In this case, all objects of type locData are
   *  collected.  These represent the timestamped org locations as to be
   *  inserted in to the table.  Also subscribe to the TableWrapper in case
   *  this is a rehydration.
   */
  public void setupSubscriptions () {
    locSubs = (IncrementalSubscription) subscribe(locData);
    tableSubs = (IncrementalSubscription) subscribe(findLocTable);
  }

  /**
   *  Process the location data as it arrives.  Also try to find an
   *  "OrgLocTable" on the logplan.  If one is not found, then one must be
   *  created.
   */
  public void execute () {
    if (tplTable == null) {
      if (tableSubs.hasChanged()) {
        Iterator i = tableSubs.getAddedCollection().iterator();
        if (i.hasNext())
          tplTable = ((TableWrapper) i.next()).getTable();
      }
      if (tplTable == null) {
        tplTable = new Hashtable();
        publishAdd(new TableWrapper("OrgLocTable", tplTable));
      }
    }

    if (locSubs.hasChanged()) {
      for (Enumeration e = locSubs.getAddedList(); e.hasMoreElements(); ) {
        LocData data = (LocData) e.nextElement();
        SimpleTPLocation tp = (SimpleTPLocation) tplTable.get(data.org);
        if (tp == null) {
          tp = new SimpleTPLocation();
          tplTable.put(data.org, tp);
        }

        tp.add(data.id, data.startDate,
          new Location(data.latitude, data.longitude));
      }
    }
  }
}