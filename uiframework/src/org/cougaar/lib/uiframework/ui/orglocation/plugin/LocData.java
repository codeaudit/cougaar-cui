
package org.cougaar.lib.uiframework.ui.orglocation.plugin;

import java.util.*;

public class LocData {
  public Date startDate = null;
  public Date endDate = null;
  public String org = null;
  public String id = null;
  public double latitude = 0.0;
  public double longitude = 0.0;

  public LocData () {
  }

  public LocData (String i, String o, Date s, Date e, double lat, double lon) {
    startDate = s;
    endDate = e;
    id = i;
    org = o;
    latitude = lat;
    longitude = lon;
  }
}