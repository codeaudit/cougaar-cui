
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import com.ibm.xml.parser.*;

import org.cougaar.util.ConfigFinder;
import org.cougaar.core.plugin.SimplePlugIn;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.lib.uiframework.ui.orglocation.data.DayBaseModel;
import org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice.LocationScheduleToXml;

/**
 *  This plugin simulates the presence of an AggAgent contacting various
 *  clusters to determine their location schedules.  Simulated data is formatted
 *  by an instance of LocationScheduleToXml, which is presumably the PSP that
 *  would be contacted to obtain that situation in a real scenario.  In this
 *  case, however, the data is published directly to the local logplan in stead
 *  of being sent over a network connection.
 */
public class TestScheduleDoms extends SimplePlugIn {
  private LocationScheduleToXml formatter = new LocationScheduleToXml();
  // names of the organizations
  private Vector orgNames = new Vector();

  // parameters for choosing the time interval
  private long dateBase = DayBaseModel.baseTimeMillis = (new Date()).getTime();
  private long dateRange = 10 * 24 * 60 * 60 * 1000;  // ten days

  public void setupSubscriptions () {
    readOrgNames();
    wakeAfter(1000);
  }

  public void execute () {
    generateRandomData();

    wakeAfter(60000); // one minute
  }

  // generate a new location schedule for each of the "organizations"
  private void generateRandomData () {
    System.out.println();
    System.out.println("TestScheduleDoms::generateRandomData");
    for (Iterator i = orgNames.iterator(); i.hasNext(); ) {
      String orgId = (String) i.next();

      StringReader in = new StringReader(
        formatter.randomDataForTest(orgId, dateBase, dateBase + dateRange));

      publishAdd(new PlanObject((new Parser(".")).readStream(in)));
    }
  }

  private void readOrgNames () {
    ConfigFinder cf = getDelegate().getCluster().getConfigFinder();

    try {
      Document doc = cf.parseXMLConfigFile("OrgList.xml");
      Element elt = doc.getDocumentElement();
      NodeList nl = elt.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        Node child = nl.item(i);
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          String org = ((Element) child).getAttribute("name");
          if (org != null) {
            orgNames.add(org);
            System.out.println("TestScheduleDoms::readOrgNames:  got " + org);
          }
        }
      }
    }
    catch (Exception oh_no) {
      System.out.println(
        "TestScheduleDoms::readOrgNames:  no OrgList.xml--use default orgs");
      orgNames.add("3ID");
      orgNames.add("1BDE");
      orgNames.add("3-69-ARBN");
      orgNames.add("3-FSB");
    }
  }
}