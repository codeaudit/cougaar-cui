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

package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.io.PrintWriter;

public abstract class Const {
  // constant strings used in the XML documents
  public static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  public static String LOC_TABLE = "TPLocTable";
  public static String SCHEDULE = "TPLocation";
  public static String ORG_NAME = "orgName";
  public static String TIME_LOC = "TimeLocation";
  public static String LATITUDE = "latitude";
  public static String LONGITUDE = "longitude";
  public static String START = "startTime";
  public static String END = "endTime";

  // Name of the Org Location Table which is shared by LocSchedulePlugin,
  // PSP_LocQuery, and PSP_LocSummary
  public static String TABLE_NAME = "OrgLocations";

  /**
   *  Utility function for creating an opening XML tags from the names found
   *  above.  Output is sent to a PrintWriter.
   *  @param out the destination for the output
   *  @param tag the name of the tag being generated
   */
  public static void openTag (PrintWriter out, String tag) {
    out.print("<");
    out.print(tag);
    out.print(">");
  }

  /**
   *  Utility function for creating a closing XML tags from the names found
   *  above.  Output is sent to a PrintWriter.
   *  @param out the destination for the output
   *  @param tag the name of the tag being generated
   */
  public static void closeTag (PrintWriter out, String tag) {
    out.print("</");
    out.print(tag);
    out.print(">");
  }

  /**
   *  Utility function for creating complete XML elements from the names found
   *  above and contents supplied by the caller.  As always, output is sent to
   *  a PrintWriter.
   *  @param out the destination for the output
   *  @param tag the name of the tag being generated
   *  @param val the contents of the generated XML element
   */
  public static void tagElement (PrintWriter out, String tag, String val) {
    openTag(out, tag);
    out.print(val);
    closeTag(out, tag);
  }
}