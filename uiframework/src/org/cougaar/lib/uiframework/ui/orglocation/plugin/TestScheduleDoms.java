
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import com.ibm.xml.parser.*;

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
  private String[] orgnames = new String[] {"Fred" , "Joe", "Sam", "Herbie"};

  // parameters for choosing the time interval
  private long dateBase = DayBaseModel.baseTimeMillis = (new Date()).getTime();
  private long dateRange = 10 * 24 * 60 * 60 * 1000;  // ten days

  public void setupSubscriptions () {
    wake();
  }

  public void execute () {
    generateRandomData();

    wakeAfter(60000); // one minute
  }

  // generate a new location schedule for each of the "organizations"
  private void generateRandomData () {
    for (int i = 0; i < orgnames.length; i++) {
      String orgId = orgnames[i];

      StringReader in = new StringReader(
        formatter.randomDataForTest(orgId, dateBase, dateBase + dateRange));

      publishAdd(new PlanObject((new Parser(".")).readStream(in)));
    }
  }
}