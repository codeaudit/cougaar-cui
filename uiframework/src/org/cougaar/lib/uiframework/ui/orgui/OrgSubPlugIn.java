
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.w3c.dom.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.aggagent.ldm.PlanObject;

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
  // store the information in a hashtable keyed by organization
  private Hashtable table = null;

  // filter for data pertaining to the relations of an organization
  private static class RelationDomSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      if (obj instanceof PlanObject) {
        String root =
          ((PlanObject) obj).getDocument().getDocumentElement().getNodeName();
        return root.equals(Const.ORG_RELS);
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
        publishAdd(new TableWrapper(Const.TABLE_NAME, table));
      }
    }

    if (relSubs.hasChanged()) {
      for (Enumeration e = relSubs.getAddedList(); e.hasMoreElements(); ) {
        PlanObject po = (PlanObject) e.nextElement();
        visitOrgRels(po.getDocument().getDocumentElement());
        publishRemove(po);
      }
    }
  }

  private void visitOrgRels (Node n) {
    System.out.println("OrgSubPlugIn::visitOrgRels");
    NodeList orgs = n.getChildNodes();
    for (int i = 0; i < orgs.getLength(); i++) {
      System.out.println("  -> child #" + i);
      Node child = orgs.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE &&
          child.getNodeName().equals(Const.CLUSTER))
      {
        System.out.println("  - -> Visiting " + child.getNodeName());
        visitRelation((Element) child);
      }
      else {
        System.out.println("  - -> Ignoring " + child.getNodeName());
      }
    }
  }

  // process a single relationship--the relationship is logged from the
  // perspective of both of the participants, just in case
  private void visitRelation (Element n) {
    System.out.println("OrgSubPlugIn::visitRelation");
    String id = n.getAttribute(Const.ID_ATTRIBUTE);
    String relative = findChildValue(Const.RELATIVE, n);
    String role = findChildValue(Const.REL_TYPE, n);
    long start = Long.parseLong(findChildValue(Const.START, n));
    long end = Long.parseLong(findChildValue(Const.END, n));
    insertRelation(id, role, relative, start, end);
    insertRelation(relative, converse(role), id, start, end);
  }

  // get the converse of the role
  // currently, only superior/subordinate relationships are supported
  private String converse (String role) {
    if (role.equals(Const.SUBORDINATE))
      return Const.SUPERIOR;
    else if (role.equals(Const.SUPERIOR))
      return Const.SUBORDINATE;
    System.out.println("OrgSubPlugIn::converse:  No converse for " + role);
    return "<<Converse of " + role + ">>";
  }

  // Add a relationship to the table.
  private void insertRelation (
      String org, String rel, String other, long start, long end)
  {
    System.out.println("OrgSubPlugIn::insertRelation:  " +
      org + " " + rel + " " + other + " " + start + " " + end);
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
