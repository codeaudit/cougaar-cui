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