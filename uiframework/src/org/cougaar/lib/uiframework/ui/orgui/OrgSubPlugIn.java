
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.w3c.dom.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableWrapper;

public class OrgSubPlugIn extends SimplePlugIn {
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
   *
   */
  public void setupSubscriptions () {
    relSubs = (IncrementalSubscription) subscribe(relData);
    tableSubs = (IncrementalSubscription) subscribe(new TableSeeker());
  }

  /**
   *
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
            long start = Long.parseLong(findChildValue("startTime", child));
            long end = Long.parseLong(findChildValue("endTime", child));
            insertRelation(
              superior, "ADMINISTRATIVESUBORDINATE", subordinate, start, end);
            insertRelation(
              subordinate, "ADMINISTRATIVESUPERIOR", superior, start, end);
          }
        }
      }
    }
  }

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