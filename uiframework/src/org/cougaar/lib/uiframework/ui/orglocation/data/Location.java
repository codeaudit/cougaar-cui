
package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.util.*;

/**
 *  A Location instance represents a location on a map in terms of latitude
 *  and longitude coordinates.
 */
public class Location {
  // the coordinates are stored as a pair of doubles
  private double latitude = 0.0;
  private double longitude = 0.0;

  /**
   *  Create a new Location in terms of its latitude/longitude coordinates
   *  @param lat the latitude of the new Location
   *  @param lon the longitude of the new Location
   */
  public Location (double lat, double lon) {
    latitude = lat;
    longitude = lon;
  }

  /**
   *  Reset the coordinates of this Location instance.
   *  @param lat the new latitude value
   *  @param lon the new Longitude value
   */
  public void reset (double lat, double lon) {
    latitude = lat;
    longitude = lon;
  }

  /**
   *  Report the latitude coordinate currently set for this Location
   *  @return the latitude value
   */
  public double getLatitude () {
    return latitude;
  }

  /**
   *  Report the longitude coordinate currently set for this Location
   *  @return the longitude value
   */
  public double getLongitude () {
    return longitude;
  }

  /**
   *  Create a duplicate of this Location with the same values for the
   *  latitude and longitude coordinates.
   *  @return a copy of this Location
   */
  public Location duplicate () {
    return new Location(latitude, longitude);
  }
}