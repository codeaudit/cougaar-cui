
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