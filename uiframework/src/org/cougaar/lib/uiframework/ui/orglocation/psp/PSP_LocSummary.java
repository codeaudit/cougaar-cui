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

package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.io.*;
import java.util.*;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.planserver.HttpInput;
import org.cougaar.lib.planserver.PSP_BaseAdapter;
import org.cougaar.lib.planserver.PlanServiceContext;
import org.cougaar.lib.planserver.PlanServiceProvider;
import org.cougaar.lib.planserver.PlanServiceUtilities;

import org.cougaar.lib.uiframework.ui.orglocation.data.Const;
import org.cougaar.lib.uiframework.ui.orglocation.data.TPLocTable;
import org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice.*;
import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableSeeker;

/**
 *  This PSP_LocSummary class is a PSP that gives a full summary of the org
 *  location data being stored on an AggAgent.  The work of formatting of XML
 *  is delegated to an instance of LocationScheduleToXml, which performs that
 *  task as a matter of course.
 */
public class PSP_LocSummary
    extends PSP_BaseAdapter
    implements PlanServiceProvider
{
  // re-use the functionality of the adapter class in formatting the XML
  private LocationScheduleToXml xmlFormatter =
    new LocationScheduleToXml();

  // filter for the table in which location results are being stored
  private static UnaryPredicate tableSeeker = new TableSeeker();

  /**
   *  Process a request from the client.  The logplan should contain a table of
   *  location schedules for organizations managed by this cluster.  Find it
   *  and generate an XML digest to be returned to the client.
   *  @param out the PrintStream to which output is to be directed
   *  @param in the incoming request from the client
   *  @param psc an interface to the environment of this PSP
   *  @param psu it's probably good for something, but who knows what?
   */
  public void execute (PrintStream out, HttpInput in, PlanServiceContext psc,
      PlanServiceUtilities psu)
      throws Exception
  {
    Collection c = psc.getServerPlugInSupport().queryForSubscriber(tableSeeker);
    Iterator i = c.iterator();
    if (i.hasNext()) {
      out.println(Const.XML_HEAD);
      TPLocTable table = (TPLocTable) i.next();
      table.toXml(new PrintWriter(out));
    }
    else {
      System.out.println("PSP_LocSummary::execute:  No table found");
    }
    if (i.hasNext()) {
      System.out.println("PSP_LocSummary::execute:  Two tables?  What gives?");
    }
  }

  // find a table of location information on the local logplan
  private TPLocTable findTable (PlanServiceContext psc) {
    Collection c = psc.getServerPlugInSupport().queryForSubscriber(tableSeeker);
    if (!c.isEmpty())
      return (TPLocTable) c.iterator().next();
    return null;
  }

  /**
   *  This PSP does not profess to use a DTD, even though all of its output
   *  adheres to the "structure.dtd".  Consequently, this method returns null.
   *  @return always null
   */
  public String getDTD () {
    return null;
  }

  /**
   *  This PSP does not return HTML documents.
   *  @return always false
   */
  public boolean returnsHTML () {
    return false;
  }

  /**
   *  Okay, so I lie.  It does really return XML, even if it professes not to.
   *  @return always false
   */
  public boolean returnsXML () {
    return false;
  }

  /**
   *  Whatever.
   *  @return always false.
   */
  public boolean test (HttpInput p0, PlanServiceContext p1) {
    return false;
  }
}