/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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