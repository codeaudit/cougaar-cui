/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */

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
  private TPLocTable tplTable = null;

  // filter for data pertaining to the location of an organization
  private static class ScheduleDomSeeker implements UnaryPredicate {
    public boolean execute (Object obj) {
      if (obj instanceof PlanObject) {
        String type =
          ((PlanObject) obj).getDocument().getDocumentElement().getNodeName();
        return type.equals(Const.SCHEDULE) || type.equals(Const.LOC_TABLE);
      }
      return false;
    }
  }
  private static UnaryPredicate locData = new ScheduleDomSeeker();

  // filter for the table in which location results are to be stored
  private static UnaryPredicate findLocTable = new TableSeeker();

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
          tplTable = (TPLocTable) i.next();
      }
      if (tplTable == null) {
        tplTable = new TPLocTable(Const.TABLE_NAME);
        publishAdd(tplTable);
      }
    }

    if (locSubs.hasChanged()) {
      for (Enumeration e = locSubs.getAddedList(); e.hasMoreElements(); ) {
        PlanObject po = (PlanObject) e.nextElement();
        Element root = po.getDocument().getDocumentElement();
        String rootName = root.getNodeName();
        if (rootName.equals(Const.LOC_TABLE))
          visitScheduleTable(root);
        else if (rootName.equals(Const.SCHEDULE))
          visitSchedule(root);
        else
          System.out.println(
            "LocSchedulePlugIn::execute:  ignoring document of type \"" +
            rootName + "\"");

        publishRemove(po);
      }
    }
  }

  private void visitScheduleTable (Node n) {
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE &&
          child.getNodeName().equals(Const.SCHEDULE))
      {
        visitSchedule(child);
      }
      else {
        System.out.println("LocSchedulePlugIn::visitSchedule:  ignoring \"" +
          child.getNodeName() + "\" tag in input");
      }
    }
  }

  private void visitSchedule (Node n) {
    String orgName = findChildValue(Const.ORG_NAME, n);

    // Create a new SimpleTPLocation and place it in the table, overwriting
    // the existing one, if any.
    SimpleTPLocation tp = new SimpleTPLocation(orgName);
    tplTable.addSchedule(tp);

    // visit the schedule elements
    NodeList nl = n.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      Node child = nl.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE &&
          child.getNodeName().equals(Const.TIME_LOC))
      {
        long start = Long.parseLong(findChildValue(Const.START, child));
        long end = Long.parseLong(findChildValue(Const.END, child));
        double lat = Double.parseDouble(findChildValue(Const.LATITUDE, child));
        double lon = Double.parseDouble(findChildValue(Const.LONGITUDE, child));
        tp.add(start, end, new Location(lat, lon));
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