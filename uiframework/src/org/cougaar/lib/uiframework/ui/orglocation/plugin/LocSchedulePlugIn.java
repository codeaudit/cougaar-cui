
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import org.w3c.dom.*;

import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;
import java.util.*;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.lib.uiframework.ui.orglocation.data.*;

/**
 *  The LocSchedulePlugIn picks up location schedule information (in the form
 *  of DOMs) as it is published to the logplan and stores the relevant
 *  information in a table for retrieval by clients.
 */
public class LocSchedulePlugIn extends SimplePlugIn {
  // a table of SimpleTPLocations
  private Hashtable tplTable = null;

  // filter for data pertaining to the location of an organization
  private static class ScheduleDomSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      if (obj instanceof PlanObject) {
        Document doc = ((PlanObject) obj).getDocument();
        return doc.getDocumentElement().getNodeName().equals("OrgLocSchedule");
      }
      return false;
    }
  }
  private static UnaryPredicate locData = new ScheduleDomSeeker();

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
   *  Subscribe to the logplan.  In this case, all DOMs containing location
   *  schedule information are collected.  There is an additional subscription
   *  to the TableWrapper (bearing the name "OrgLocTable") in case this is a
   *  rehydration and the table was persisted.
   */
  public void setupSubscriptions () {
    locSubs = (IncrementalSubscription) subscribe(locData);
    tableSubs = (IncrementalSubscription) subscribe(findLocTable);
  }

  /**
   *  Process the location data as it arrives.  Also try to find an
   *  "OrgLocTable" on the logplan.  If one is not found, then one must be
   *  created and published.
   */
  public void execute () {
    // find the table of schedules on the logplan, or else create one
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
        PlanObject po = (PlanObject) e.nextElement();
        Node root = po.getDocument().getDocumentElement();
        String orgName = findChildValue("orgName", root);

        // Create a new SimpleTPLocation and place it in the table, overwriting
        // the existing one, if any.
        SimpleTPLocation tp = new SimpleTPLocation();
        tplTable.put(orgName, tp);
        findScheduleElements(root, tp);
        publishRemove(po);
      }
    }
  }

  private void findScheduleElements (Node n, SimpleTPLocation tp) {
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE &&
          child.getNodeName().equals("TimeLocation"))
      {
        long time = Long.parseLong(findChildValue("startTime", child));
        double lat = Double.parseDouble(findChildValue("latitude", child));
        double lon = Double.parseDouble(findChildValue("longitude", child));
        tp.add(null, time, new Location(lat, lon));
      }
    }
  }

  private String findChildValue (String name, Node n) {
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE &&
          child.getNodeName().equals(name))
      {
        return findNodeText(child);
      }
    }
    System.out.println(
      "LocSchedulePlugIn::findChildValue:  returning \"anonymous\"");
    return "anonymous";
  }

  private static String findNodeText (Node n) {
    StringBuffer buf = new StringBuffer();
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.TEXT_NODE)
        buf.append(child.getNodeValue());
    }
    return buf.toString();
  }
}