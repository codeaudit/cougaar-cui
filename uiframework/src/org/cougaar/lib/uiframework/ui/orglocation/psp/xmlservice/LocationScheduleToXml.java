
package org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice;

import java.io.*;
import java.util.*;

import org.cougaar.domain.planning.ldm.plan.Schedule;
import org.cougaar.domain.planning.ldm.plan.LocationScheduleElement;
import org.cougaar.domain.glm.ldm.plan.GeolocLocation;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.glm.ldm.asset.LocationSchedulePG;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.*;

/**
 *  The LocationScheduleToXml class does what its name suggests.  It finds an
 *  Organization asset on the logplan and converts its location schedule to an
 *  XML format for transmission across a network connection.  For present
 *  purposes, the only Organization of interest is the one represented by the
 *  host Cluster.  The implementation of this class attempts to filter out those
 *  that are not of interest by comparing the name of the owner with the name of
 *  the Asset and keeping only those for which the names match.
 */
public class LocationScheduleToXml extends CustomQueryBaseAdapter {
  // Collect the schedule information in an OrgTimeLocSchedule.  There should
  // never be more than one of these at any given time unless the usage model
  // changes.
  private OrgTimeLocSchedule locSchedule = null;

  /**
   *  Locate and serialize to XML the schedule of locations planned for this
   *  organizaton.  In effect, scan the Organization Assets published on the
   *  logplan of the local Cluster and find the one that represents the same
   *  Cluster.  Then convert that schedule to XML for transportation across the
   *  net.
   *  @param matches the collection of Organization assets on the logplan
   */
  public void execute (Collection matches) {
    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Organization org = (Organization) i.next();
      // Determine whether this organization is the one we're looking for by
      // comparing the owner of the asset to the asset in question.  Foreign
      // organization assets will be ignored; if we're interested in their
      // itineraries, we'll contact them...
      String owner = org.getUID().getOwner();
      String orgName = org.getName();
      if (owner.equals(orgName)) {
        if (locSchedule != null) {
          System.out.println(
            "LocationScheduleToXml::execute:  more than one schedule for " +
            orgName);
        }
        Schedule s = org.getLocationSchedulePG().getSchedule();
        if (s != null) {
          locSchedule = new OrgTimeLocSchedule(orgName);
          Enumeration e = s.getAllScheduleElements();
          while (e.hasMoreElements())
            locSchedule.add(
              makeTimeLocation((LocationScheduleElement) e.nextElement()));
        }
      }
    }
  }

  // create a TimeLocation instance from a LocationScheduleElement instance
  private static TimeLocation makeTimeLocation (LocationScheduleElement elt) {
    GeolocLocation location = (GeolocLocation) elt.getLocation();
    double latitude = location.getLatitude().getValue(0);
    double longitude = location.getLongitude().getValue(0);
    long start = elt.getStartTime();
    long end = elt.getEndTime();
    return new TimeLocation(latitude, longitude, start, end);
  }

  /**
   *  Write as an XML stream the schedule recently found in the execute method.
   *  @param out the stream to which XML output is directed
   *  @param xsl the optional xsl transform, which is ignored
   */
  public void returnVal (OutputStream out, StringBuffer xsl) {
    if (locSchedule != null) {
      PrintStream ps = new PrintStream(out);
      ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      ps.println(locSchedule.toXmlString());
      ps.flush();
      locSchedule = null;
    }
  }

  /**
   *  Support for testing
   */
  public String randomDataForTest (String orgName, long t0, long t1) {
    int i;

    OrgTimeLocSchedule sched = new OrgTimeLocSchedule(orgName);
    int n_intervals = (int) Math.floor(
      2.0 + 2.0 * (Math.random() + Math.random() + Math.random()));
    double avg_len = (double) (t1 - t0) / (double) n_intervals;
    long[] nexi = new long[n_intervals + 1];
    nexi[0] = t0;
    nexi[n_intervals] = t1;
    for (i = 1; i < n_intervals; i++)
      nexi[i] = t0 + (long) (avg_len * (i + (Math.random() - 0.5) * 0.9));
    for (i = 0; i < n_intervals; i++) {
      double lat = -90.0 + 180.0 * Math.random();
      double lon = -180.0 + 360.0 * Math.random();
      sched.add(new TimeLocation(lat, lon, nexi[i], nexi[i + 1]));
    }
    return sched.toXmlString();
  }

  // An inner class to coordinate the times and locations that collectively
  // form a location schedule.  Each instance is tagged with the name of the
  // organization whose schedule it represents.
  private static class OrgTimeLocSchedule {
    private Vector elements = new Vector();
    private String orgName = null;

    public OrgTimeLocSchedule (String org) {
      orgName = org;
    }

    public void add (TimeLocation tl) {
      elements.add(tl);
    }

    public String toXmlString () {
      StringBuffer buf = new StringBuffer("<OrgLocSchedule>");
      buf.append("<orgName>");
      buf.append(orgName);
      buf.append("</orgName>");
      for (Iterator i = elements.iterator(); i.hasNext(); )
        buf.append(((TimeLocation) i.next()).toXmlString());
      buf.append("</OrgLocSchedule>");
      return buf.toString();
    }
  }

  // An inner class used to coordinate the time and location information.
  // Collectively, these form a location schedule.
  private static class TimeLocation {
    public double lat = -91.0;
    public double lon = -181.0;
    public long start = 0;
    public long end = -1;

    public TimeLocation () {
    }

    public TimeLocation (
        double latitude, double longitude, long startTime, long endTime)
    {
      lat = latitude;
      lon = longitude;
      start = startTime;
      end = endTime;
    }

    private static void addAttribXml (StringBuffer b, String tag, Object val) {
      b.append("<");
      b.append(tag);
      b.append(">");
      b.append(val);
      b.append("</");
      b.append(tag);
      b.append(">");
    }

    private String toXmlString () {
      StringBuffer buf = new StringBuffer("<TimeLocation>");
      addAttribXml(buf, "latitude", String.valueOf(lat));
      addAttribXml(buf, "longitude", String.valueOf(lon));
      addAttribXml(buf, "startTime", String.valueOf(start));
      addAttribXml(buf, "endTime", String.valueOf(end));
      buf.append("</TimeLocation>");
      return buf.toString();
    }
  }
}