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