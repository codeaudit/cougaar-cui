/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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

import java.io.*;

public class TimeLocation {
  private long start = -1;
  private long end = -1;
  private Location loc = null;

  public TimeLocation (long t0, long t1, Location l) {
    start = t0;
    end = t1;
    loc = l;
  }

  public long getStart () {
    return start;
  }

  public void setStart (long s) {
    start = s;
  }

  public long getEnd () {
    return end;
  }

  public void setEnd (long e) {
    end = e;
  }

  public Location getLocation () {
    return loc;
  }

  public void toXml (PrintWriter o) {
    Const.openTag(o, Const.TIME_LOC);
    Const.tagElement(o, Const.START, String.valueOf(start));
    Const.tagElement(o, Const.END, String.valueOf(end));
    Const.tagElement(o, Const.LATITUDE, String.valueOf(loc.getLatitude()));
    Const.tagElement(o, Const.LONGITUDE, String.valueOf(loc.getLongitude()));
    Const.closeTag(o, Const.TIME_LOC);
  }
}