/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice;

import java.io.*;
import java.util.*;

import org.cougaar.domain.planning.ldm.plan.Schedule;
import org.cougaar.domain.planning.ldm.plan.LocationScheduleElement;
import org.cougaar.domain.glm.ldm.plan.GeolocLocation;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.glm.ldm.asset.LocationSchedulePG;
import org.cougaar.domain.glm.plugins.TimeUtils;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.*;

import org.cougaar.lib.uiframework.ui.orglocation.data.*;

/**
 *  The LocationScheduleToXml class does what its name suggests.  It finds an
 *  Organization asset on the logplan and converts its location schedule to an
 *  XML format for transmission across a network connection.  For present
 *  purposes, the only Organization of interest is the one represented by the
 *  host Cluster.  The others probably don't have a LocationSchedulePG, and are
 *  ignored in any event.
 */
public class LocationScheduleToXml extends CustomQueryBaseAdapter {

  // Collect the schedule information in an OrgTimeLocSchedule.  There should
  // never be more than one of these at any given time unless the usage model
  // changes.
  private SimpleTPLocation locSchedule = null;

  /**
   *  Locate and serialize to XML the schedule of locations planned for this
   *  organizaton.  In effect, scan the Organization Assets published on the
   *  logplan of the local Cluster and find the one that represents the same
   *  Cluster.  Then convert that schedule to XML for transportation across the
   *  net.
   *  @param matches the collection of Organization assets on the logplan
   *  @param eventType the published operation applied to the matching Objects
   */
  public void execute (Collection matches, String eventType) {
    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Organization org = (Organization) i.next();
      // Determine whether this organization is the one we're looking for by
      // comparing the owner of the asset to the asset in question.  Foreign
      // organization assets will be ignored; if we're interested in their
      // itineraries, we'll contact them...
      String orgName = org.getUID().getOwner();
      if (org.isSelf()) {
        if (locSchedule != null) {
          System.out.println(
            "LocationScheduleToXml::execute:  more than one schedule for " +
            orgName);
        }
        Schedule s = org.getLocationSchedulePG().getSchedule();

        if (s != null) {
          locSchedule = new SimpleTPLocation(orgName);
          Enumeration e = s.getAllScheduleElements();
          boolean needsHomeLocation = ((org.hasMilitaryOrgPG()) &&
                                       (org.getMilitaryOrgPG().getHomeLocation() != null));
          if (!needsHomeLocation) {
            System.out.println(
            "LocationScheduleToXml::execute:  unable to find home location for " +
            orgName);
          }

          while (e.hasMoreElements()) {
            if (needsHomeLocation) {
              // insert entry for home location
              LocationScheduleElement lse = (LocationScheduleElement)e.nextElement();
              long start = lse.getStartTime() - 10L * TimeUtils.MSEC_PER_DAY;
              addHomeLocationToLocSchedule(org, start, lse.getStartTime());
              needsHomeLocation = false;

              addToLocSchedule(lse);
            } else {
              addToLocSchedule((LocationScheduleElement) e.nextElement());
            }
          }
        }
      }
    }
  }

  private void addToLocSchedule (LocationScheduleElement elt) {
    GeolocLocation location = (GeolocLocation) elt.getLocation();
    double latitude = location.getLatitude().getValue(0);
    double longitude = location.getLongitude().getValue(0);
    long start = elt.getStartTime();
    long end = elt.getEndTime();
    locSchedule.add(start, end, new Location(latitude, longitude));
  }

  private void addHomeLocationToLocSchedule(Organization org, long start, long end) {
    GeolocLocation home = (GeolocLocation) org.getMilitaryOrgPG().getHomeLocation();
    double latitude = home.getLatitude().getValue(0);
    double longitude = home.getLongitude().getValue(0);
    locSchedule.add(start, end, new Location(latitude, longitude));
  }

  /**
   *  Write as an XML stream the schedule recently found in the execute method.
   *  @param out the stream to which XML output is directed
   */
  public void returnVal (OutputStream out) {
    if (locSchedule != null) {
      PrintWriter pw = new PrintWriter(out);
      pw.println(Const.XML_HEAD);
      locSchedule.toXml(pw);
      locSchedule = null;
    }
  }

  /**
   *  Support for testing.  Given an fictitious org name and an interval of
   *  time, this method randomly generates a schedule for the length of the
   *  interval.  The schedule is then formatted as if it were real data and
   *  returned as a String of XML.
   */
  public String randomDataForTest (String orgName, long t0, long t1) {
    int i;

    SimpleTPLocation sched = new SimpleTPLocation(orgName);
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
      sched.add(nexi[i], nexi[i + 1], new Location(lat, lon));
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    pw.println(Const.XML_HEAD);
    sched.toXml(pw);
    return sw.getBuffer().toString();
  }
}
