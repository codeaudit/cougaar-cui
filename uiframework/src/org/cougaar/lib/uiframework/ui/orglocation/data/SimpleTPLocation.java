
package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.util.*;
import java.io.*;

/**
 *  <p>
 *  SimpleTPLocation is a simple implementation of a time-phased location
 *  model.  The response to a request for the location at a particular Date
 *  is an instance of the Location class (q.v.).
 *  </p><p>
 *  The model is based on a a finite number of data points telling the exact
 *  location at a certain time.  It is presumed that location remains constant
 *  from that point forward until the time of the subsequent data point, if
 *  any.  The model also presumes that for times before the time of the first
 *  data point, the location is the same as that reported in the first point.
 *  </p><p>
 *  Data points can be added or removed as necessary, though each is required
 *  to have a unique ID.  When a duplicate ID is encountered, the newer data
 *  replaces whatever had previously been associated with that ID in the model.
 *  </p>
 */
public class SimpleTPLocation implements TPLocation {
  private Vector times = new Vector();
  // private Hashtable idIndex = new Hashtable();
  private String name = null;

  public String getName () {
    return name;
  }

  public SimpleTPLocation (String n) {
    name = n;
  }

  /**
   *  Retrieve the location understood by this model for the given time.
   *  @param t the time of interest
   *  @return the location of the model at the given time
   */
  public Location getLocation (long t) {
    if (isInScope(t)) {
      TimeLocation n = (TimeLocation) times.elementAt(0);
      for (int i = 1; i < times.size(); i++) {
        TimeLocation m = (TimeLocation) times.elementAt(i);
        if (m.start > t)
          break;
        n = m;
      }
      return n.loc.duplicate();
    }
    else {
      return null;
    }
  }

  /**
   *  Tell if the time supplied by the caller is within the scope of this
   *  time-phased location model.  In this implementation, all time is within
   *  the scope as long as any data point is present for extrapolation.
   *  @param t the time of interest
   *  @return true iff the given time is supported by this model.
   */
  public boolean isInScope (long t) {
    return times.size() > 0;
  }

  /**
   *  Add a new data point to the model.  The unique id associated with the
   *  data point is used to check for previously cached occurrences of the
   *  same information.
   *  @param id the unique id String associated with this data
   *  @param t the starting time for the given location
   *  @param loc the location understood by the model for a period of time
   */
  public void add (long t0, long t1, Location loc) {
    // TimeLocation n = (TimeLocation) idIndex.get(id);
    TimeLocation n = null;
    if (n == null) {
      n = new TimeLocation(t0, t1, loc);
      // idIndex.put(id, n);
      insertInOrder(n);
    }
    else {
      n.loc.reset(loc.getLatitude(), loc.getLongitude());
      n.start = t0;
      n.end = t1;
      shiftPositionOf(n);
    }
  }

  public void toXml (PrintWriter out) {
    Const.openTag(out, Const.SCHEDULE);
    Const.tagElement(out, Const.ORG_NAME, getName());
    for (Iterator i = times.iterator(); i.hasNext(); )
      ((TimeLocation) i.next()).toXml(out);
    Const.closeTag(out, Const.SCHEDULE);
  }

  private void shiftPositionOf (TimeLocation loc) {
    int k = times.indexOf(loc);
    int j = k;
    if (j > 0 && ((TimeLocation) times.elementAt(j - 1)).start > loc.start) {
      j--;
      while (j > 0 && ((TimeLocation) times.elementAt(j - 1)).start > loc.start)
        j--;
    }
    else {
      while (j < times.size() - 1 &&
          ((TimeLocation) times.elementAt(j + 1)).start < loc.start)
      {
        j++;
      }
    }
    times.removeElementAt(k);
    times.insertElementAt(loc, j);
    // reconcile endpoints of this interval with neighbors, if necessary; when
    // in doubt, starting times are accounted more accurate than ending times
    TimeLocation neighbor = null;
    if (j < times.size() - 1) {
      neighbor = (TimeLocation) times.elementAt(j + 1);
      if (neighbor.start < loc.end)
        loc.end = neighbor.start;
    }
    if (j > 0) {
      neighbor = (TimeLocation) times.elementAt(j - 1);
      if (neighbor.end > loc.start)
        neighbor.end = loc.start;
    }
  }

  private void insertInOrder (TimeLocation loc) {
    times.add(loc);
    shiftPositionOf(loc);
  }

  /**
   *  Remove the position information associated with a given unique id String.
   *  This method is not currently implemented.
   *  @param id the unique id to be removed
   */
  public void remove (String id) {
    throw new Error("Not bloody implemented!");
  }

  /**
   *  Give a summary of the state of this location.  In particular, the time
   *  values at which the location changes (the locations themselves can be
   *  added later, if necessary).
   */
  public String toString () {
    StringBuffer buf = new StringBuffer("SimpleTPLocation[");
    Iterator i = times.iterator();
    if (i.hasNext()) {
      buf.append(((TimeLocation) i.next()).start);
      while (i.hasNext()) {
        buf.append(", ");
        buf.append(((TimeLocation) i.next()).start);
      }
    }
    buf.append("]");
    return buf.toString();
  }

  // Keep UID, date, and location together
  private static class TimeLocation {
    public long start = -1;
    public long end = -1;
    public Location loc = null;

    public TimeLocation (long t0, long t1, Location l) {
      start = t0;
      end = t1;
      loc = l;
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
}