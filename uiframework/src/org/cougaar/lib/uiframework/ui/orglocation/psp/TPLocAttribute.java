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