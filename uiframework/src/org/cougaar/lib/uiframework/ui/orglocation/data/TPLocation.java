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

import java.util.*;

/**
 *  A time-phased location model.  Specific locations are represented by the
 *  Location class, but an implementation of this model reports the location
 *  based on a time parameter (expressed as a date).
 */
public interface TPLocation {
  /**
   *  Report the location understood by this model for the given time.  If the
   *  time is not within the scope of the model, then the behavior is not
   *  specified.  To test whether the time is in the model's scope, use the
   *  method isInScope (q.v.).
   *  @param t the time for which the caller wants to know the model's location
   *  @return the location for time t
   */
  public Location getLocation (long t);

  /**
   *  Check to see if the provided time is within the scope of this model.  If
   *  it is, then the getLocation (q.v.) method can be used to obtain a valid
   *  location for that time.  If the time is not within scope, then it should
   *  be understood that calls to getLocation will not behave predictably.
   *  @param t the time of interest to the caller
   *  @return true iff the time is in this model's scope
   */
  public boolean isInScope (long t);
}