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

package org.cougaar.lib.uiframework.ui.orgui;

/**
 *  A RelationSpan is a simple class for encoding a period of time and a
 *  relationship with respect to an Organization.  The target Organizaiton is
 *  expressed as a String identifier.
 */
public class RelationSpan {
  private String relative = null;
  private long startTime = 0;
  private long endTime = -1;

  /**
   *  Encode a relationship timespan and target Organization in a new
   *  RelationSpan.
   *  @param other a String that identifies the target Organization
   *  @param start the millisecond index at which the relationship starts
   *  @param end the millisecond index at which the relationship ends
   */
  public RelationSpan (String other, long start, long end) {
    relative = other;
    startTime = start;
    endTime = end;
  }

  /**
   *  Report on which Organization this RelationSpan targets.
   *  @return a String identifying the target Organization
   */
  public String getRelative () {
    return relative;
  }

  /**
   *  Tell when the relationship starts
   *  @return the millisecond index on which the relationship begins
   */
  public long getStartTime () {
    return startTime;
  }

  /**
   *  Tell when the relationship ends
   *  @return the millisecond index on which the relationship ends
   */
  public long getEndTime () {
    return endTime;
  }

  /**
   *  Determine whether the given time is within the time of this RelationSpan
   *  @param t a millisecond index
   *  @return true iff t is within the time span of this relationship
   */
  public boolean contains (long t) {
    return startTime <= t && t < endTime;
  }

  /**
   *  Determine whether another RelationSpan can be seamlessly combined with
   *  this one.  This is the case when the relationships are both to the same
   *  target, and the union of the two timespans is a complete sequence of
   *  consecutive milliseconds (either they overlap, miss overlapping by a
   *  single millisecond).
   *  @param other another RelationSpan being compared to this one.
   *  @return true iff the two spans can be joined without losing information
   */
  public boolean adjoins (RelationSpan other) {
    return other.relative.equals(relative) &&
      other.startTime <= endTime && startTime <= other.endTime;
  }

  /**
   *  Combine another RelationSpan with this one.  This operation is guaranteed
   *  to be safe whenever the adjoins() method returns true on the same target.
   *  @param other another RelationshipSpan to be combined with this one
   */
  public void subsume (RelationSpan other) {
    startTime = Math.min(startTime, other.startTime);
    endTime = Math.max(endTime, other.endTime);
  }
}