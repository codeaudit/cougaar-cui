
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.w3c.dom.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableWrapper;

/**
 *  <p>
 *  This PlugIn class is designed to fit into an Aggregation Agent and there
 *  manage a table of relationships among the Organizations in a society.  Data
 *  is expected to come in the form produced by OrgSubAdapter (q.v.), which
 *  matches the subscription maintained by this type of PlugIn.  The data
 *  structure obtained by compiling inputs from the various Clusters is
 *  published on the logplan, where it may be accessed by any interested
 *  parties.  In particular, PSP_OrgSub is a PSP that reports results to the
 *  Aggregation Agent's clients.
 *  </p><p>
 *  Currently, only superior/subordinate relationships are supported.
 */
public class OrgSubPlugIn extends SimplePlugIn {
  // a couple of constants
  private static String SUBORDINATE = "ADMINISTRATIVESUBORDINATE";
  private static String SUPERIOR = "ADMINISTRATIVESUPERIOR";

  // store the information in a hashtable keyed by organization
  private Hashtable table = null;

  // filter for data pertaining to the relations of an organization
  private static class RelationDomSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      if (obj instanceof PlanObject) {
        Document doc = ((PlanObject) obj).getDocument();
        return doc.getDocumentElement().getNodeName().equals("OrgRelations");
      }
      return false;
    }
  }
  private static UnaryPredicate relData = new RelationDomSeeker();

  // subscriptions for stuff on the logplan
  private IncrementalSubscription relSubs = null;
  private IncrementalSubscription tableSubs = null;

  /**
   *  Subscribe to the logplan for relational data coming in from the society
   *  and for the locally maintained relationship table. If the table exists
   *  before this PlugIn has had a chance to create one, then, possibly, the
   *  Node is being restored after going down.  Another possibility is that two
   *  or more instances of this class are operating on the host Cluster.
   */
  public void setupSubscriptions () {
    relSubs = (IncrementalSubscription) subscribe(relData);
    tableSubs = (IncrementalSubscription) subscribe(new TableSeeker());
  }

  /**
   *  Process incoming relational data and create or find the locally
   *  maintained relationship table.
   */
  public void execute () {
    // find the table of schedules on the logplan, or else create one
    if (table == null) {
      if (tableSubs.hasChanged()) {
        Iterator i = tableSubs.getAddedCollection().iterator();
        if (i.hasNext())
          table = ((TableWrapper) i.next()).getTable();
      }
      if (table == null) {
        table = new Hashtable();
        publishAdd(new TableWrapper("OrgRelTable", table));
      }
    }

    if (relSubs.hasChanged()) {
      for (Enumeration e = relSubs.getAddedList(); e.hasMoreElements(); ) {
        PlanObject po = (PlanObject) e.nextElement();
        NodeList rels = po.getDocument().getDocumentElement().getChildNodes();
        for (int i = 0; i < rels.getLength(); i++) {
          Node child = rels.item(i);
          if (child.getNodeType() == Node.ELEMENT_NODE &&
              child.getNodeName().equals("OrgRelationship"))
          {
            String superior = findChildValue("superior", child);
            String subordinate = findChildValue("subordinate", child);
            long start = Long.parseLong(findChildValue("start", child));
            long end = Long.parseLong(findChildValue("end", child));
            insertRelation(
              superior, SUBORDINATE, subordinate, start, end);
            insertRelation(
              subordinate, SUPERIOR, superior, start, end);
          }
        }
      }
    }
  }

  // Add a relationship to the table.
  private void insertRelation (
      String org, String rel, String other, long start, long end)
  {
    TPRelations tpr = (TPRelations) table.get(org);
    if (tpr == null) {
      tpr = new TPRelations(org);
      table.put(org, tpr);
    }
    tpr.addRelation(rel, other, start, end);
  }

  // Search the children of a node for one of a particular name and extract
  // its contents as a String value; for purposes of the search, non-ELEMENT
  // nodes are ignored.
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
      "OrgSubPlugIn::findChildValue:  returning \"anonymous\"");
    return "anonymous";
  }

  // Search the children of a node looking for text content.  The values of all
  // TEXT children are concatenated and returned.  Non-TEXT nodes are ignored.
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