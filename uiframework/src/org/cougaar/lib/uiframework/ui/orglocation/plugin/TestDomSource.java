
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import com.ibm.xml.parser.*;

import org.cougaar.core.plugin.SimplePlugIn;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice.OrgActivityToXml;
import org.cougaar.lib.uiframework.ui.orglocation.data.DayBaseModel;

/**
 *  This PlugIn class acts as a source of test data for the Org. Location sensor
 *  in the Agg. Agent.  Location info is randomly generated for a finite set of
 *  imaginary organizations.
 */
public class TestDomSource extends SimplePlugIn {
  private OrgActivityToXml formatter = new OrgActivityToXml();
  private int serialNumber = 10000;

  // names of the organizations
  private String[] orgnames = new String[] {"Fred", "Joe", "Sam", "Herbie"};

  // parameters for choosing the time interval
  private long dateBase = DayBaseModel.baseTimeMillis = (new Date()).getTime();
  private long dateRange = 5 * 24 * 60 * 60 * 1000;  // five days
  private long intervalRange = 24 * 60 * 60 * 1000;  // one day

  public void setupSubscriptions () {
    wake();
  }

  public void execute () {
    publishAdd(generateRandomData());

    wakeAfter(3000);
  }

  private Object generateRandomData () {
    String uid = String.valueOf(serialNumber++);
    String orgId = orgnames[(int) Math.floor(Math.random() * orgnames.length)];
    double latitude = Math.random() * 180.0 - 90.0;
    double longitude = Math.random() * 360.0 - 180.0;
    long t0 = dateBase + (long) Math.floor(Math.random() * dateRange);
    long t1 = t0 + (long) Math.floor(Math.random() * intervalRange);

    StringReader in = new StringReader(
      formatter.rawDataForTest(
        uid, orgId, latitude, longitude, new Date(t0), new Date(t1)));

    publishAdd(new PlanObject((new Parser(".")).readStream(in)));

    throw new Error("Not bloody implemented!");
  }
}