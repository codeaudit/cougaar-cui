
package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.util.*;

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
  private Hashtable idIndex = new Hashtable();

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
        if (m.time > t)
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
  public void add (String id, long t, Location loc) {
    // TimeLocation n = (TimeLocation) idIndex.get(id);
    TimeLocation n = null;
    if (n == null) {
      n = new TimeLocation(id, t, loc);
      // idIndex.put(id, n);
      insertInOrder(n);
    }
    else {
      n.loc.reset(loc.getLatitude(), loc.getLongitude());
      n.time = t;
      shiftPositionOf(n);
    }
  }

  private void shiftPositionOf (TimeLocation n) {
    int k = times.indexOf(n);
    int j = k;
    if (j > 0 && ((TimeLocation) times.elementAt(j - 1)).time > n.time) {
      j--;
      while (j > 0 && ((TimeLocation) times.elementAt(j - 1)).time > n.time)
        j--;
    }
    else {
      while (j < times.size() - 1 &&
          ((TimeLocation) times.elementAt(j + 1)).time < n.time)
      {
        j++;
      }
    }
    times.removeElementAt(k);
    times.insertElementAt(n, j);
  }

  private void insertInOrder (TimeLocation n) {
    times.add(n);
    shiftPositionOf(n);
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
      buf.append(((TimeLocation) i.next()).time);
      while (i.hasNext()) {
        buf.append(", ");
        buf.append(((TimeLocation) i.next()).time);
      }
    }
    buf.append("]");
    return buf.toString();
  }

  // Keep UID, date, and location together
  private static class TimeLocation {
    public String uid = null;
    public long time = -1;
    public Location loc = null;

    public TimeLocation (String id, long t, Location l) {
      uid = id;
      time = t;
      loc = l;
    }
  }
}