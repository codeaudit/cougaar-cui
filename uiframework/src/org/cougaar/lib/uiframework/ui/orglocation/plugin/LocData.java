
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;

public class LocData {
  public long startTime = 0;
  public long endTime = -1;
  public String org = null;
  public String id = null;
  public double latitude = 0.0;
  public double longitude = 0.0;

  public LocData () {
  }

  public LocData (String i, String o, long s, long e, double lat, double lon) {
    startTime = s;
    endTime = e;
    id = i;
    org = o;
    latitude = lat;
    longitude = lon;
  }
}