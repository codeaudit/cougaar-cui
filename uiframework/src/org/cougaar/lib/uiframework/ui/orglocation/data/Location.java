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