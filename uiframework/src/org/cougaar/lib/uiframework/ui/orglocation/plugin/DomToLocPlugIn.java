
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;
import java.text.*;
import org.w3c.dom.*;

import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.aggagent.ldm.PlanObject;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;
import org.cougaar.lib.uiframework.transducer.ChildEnumerator;

/**
 *  This PlugIn class is responsible for converting timestamped org location
 *  data (which is published on the logplan as a series of <OrgTimeLocList>
 *  XML DOMs) into a series of records that can be processed by the resident
 *  LocDataPlugIn.
 */
public class DomToLocPlugIn extends SimplePlugIn {
  private static class DomSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      return obj instanceof PlanObject;
    }
  }
  private static UnaryPredicate domTest = new DomSeeker();

  private DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

  IncrementalSubscription domSource = null;

  /**
   *  Set up the subscriptions.  In this case, subscribe to the PlanObjects,
   *  which are wrappers for DOMs.
   */
  public void setupSubscriptions () {
    domSource = (IncrementalSubscription) subscribe(domTest);
  }

  /**
   *  Process the DOMs that have been added to the logplan.  As it is processed,
   *  each one is removed and replaced with equivalent instances of LocData.
   */
  public void execute () {
    for (Enumeration e = domSource.getAddedList(); e.hasMoreElements(); ) {
      PlanObject po = (PlanObject) e.nextElement();
      visitNode(po.getDocument().getDocumentElement());
      publishRemove(po);
    }
  }

  private void visitNode (Node n) {
    if (n.getNodeType() == Node.TEXT_NODE)
      return;

    String name = n.getNodeName();
    if (name.equals("OrgTimeLocList"))
      visitOrgTimeLocList(n);
    else if (name.equals("OrgTimeLoc"))
      visitOrgTimeLoc(n);
    else
      System.out.println(
        "DomToLocPlugIn::visitNode:  invalid \"" + name + "\" tag in the DOM");
  }

  private void visitOrgTimeLocList (Node n) {
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++)
      visitNode(nl.item(i));
  }

  private void visitOrgTimeLoc (Node n) {
    try {
      LocData data = new LocData();
      NodeList nl = n.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
        includeChildText(data, nl.item(i));
      publishAdd(data);
    }
    catch (Exception eek) {
      eek.printStackTrace();
    }
  }

  private void includeChildText (LocData data, Node n) throws Exception {
    if (n.getNodeType() != Node.ELEMENT_NODE)
      return;

    String name = n.getNodeName();
    String text = getNodeText(n);
    if (name.equals("UID"))
      data.id = text;
    else if (name.equals("orgId"))
      data.org = text;
    else if (name.equals("startDate"))
      data.startDate = parseDate(text);
    else if (name.equals("thruDate"))
      data.endDate = parseDate(text);
    else if (name.equals("latitude"))
      data.latitude = Double.parseDouble(text);
    else if (name.equals("longitude"))
      data.longitude = Double.parseDouble(text);
    else
      System.out.println(
        "DomToLocPlugIn::includeChildText:  unknown tag \"" + name + "\"");
  }

  private String getNodeText (Node n) {
    NodeList nl = n.getChildNodes();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.TEXT_NODE)
        buf.append(child.getNodeValue());
    }
    return buf.toString();
  }

  private Date parseDate (String text) throws ParseException {
    synchronized (format) {
      return format.parse(text);
    }
  }
}