
package org.cougaar.lib.uiframework.ui.orglocation.psp;

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;

/**
 *  This QueryAttribute implementation works in conjunction with the
 *  TPLocDimension class to allow a QueryInterpreter to process requests for
 *  Org location data.  It has four possible modes--one for each piece of
 *  information available, namely start time, end time, latitude, and longitude.
 *  Four separate instances, one of each type, should be used by eahc such
 *  QueryInterpreter.
 */
public class TPLocAttribute extends QueryAttribute {
  private static String BOGUS = "Bogus";

  public static int START_TIME = 0;
  public static int END_TIME = 1;
  public static int LATITUDE = 2;
  public static int LONGITUDE = 3;

  private static String[] names =
    new String[] {"startTime", "endTime", "latitude", "longitude"};

  private int type = -1;

  public TPLocAttribute (int thingToGet) {
    type = thingToGet;
    setName(names[type]);
  }

  public String eval (EvaluationLocus l) throws QueryException {
    VisitLocus v = l.getCoordinate(0);
    if (v.location instanceof LocTimeNode) {
      LocTimeNode n = (LocTimeNode) v.location;
      return attributeValue(n.getTimeLoc());
    }
    return BOGUS;
  }

  private String attributeValue (TimeLocation tl) {
    if (type == START_TIME)
      return String.valueOf(DayBaseModel.getDayForMillis(tl.getStart()));
    else if (type == END_TIME)
      return String.valueOf(DayBaseModel.getDayForMillis(tl.getEnd()));
    else if (type == LATITUDE)
      return String.valueOf(tl.getLocation().getLatitude());
    else if (type == LONGITUDE)
      return String.valueOf(tl.getLocation().getLongitude());
    else
      return BOGUS;
  }
}