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

package org.cougaar.lib.uiframework.query.generic;

/**
 *  A VisitSeq is an ordered sequence of nodes within a given dimension.
 *  Specifically, they represent the ones for which values should be calculated
 *  based on the query that was submitted.
 */
public class VisitSeq {
  private static class Link {
    public Link next = null;
    public VisitLocus locus = null;

    public Link (VisitLocus v) {
      locus = v;
    }
  }

  // variables associated with permanent state
  private Link first = null;
  private Link last = null;
  private int length = 0;

  // variables that change during iteration
  private Link cursor = null;

  /**
   *  A shallow copy of the data structure.  Regardless of this instance's
   *  state at the time of the call, the copy is initialized so that its first
   *  element is "current".
   *  @return a copy of this sequence
   */
  public VisitSeq duplicate () {
    VisitSeq ret = new VisitSeq();
    for (Link q = first; q != null; q = q.next)
      ret.addLocus(q.locus);
    return ret;
  }

  /**
   *  During an iteration through the list, this reports on which element is
   *  under the "cursor" at the time of the call.
   *  @return the current locus
   */
  public VisitLocus getCurrentLocus () {
    if (cursor != null)
      return cursor.locus;
    return null;
  }

  /**
   *  Advance the iterating "cursor" to the next element in the list, if any.
   */
  public void next () {
    if (cursor != null)
      cursor = cursor.next;
  }

  /**
   *  Return the interating "cursor" to the initial position, if it exists.
   */
  public void reset () {
    cursor = first;
  }

  /**
   *  Report whether the iterating "cursor" rests on an actual element.  This
   *  method returns false when all elements have been visited during an
   *  iteration.
   *  @return true iff the cursor rests on a valid element
   */
  public boolean hasCurrent () {
    return cursor != null;
  }

  /**
   *  Add a new locus to this list (during the construction phase, mainly).
   *  @param v the new locus
   */
  public void addLocus (VisitLocus v) {
    Link p = new Link(v);
    if (last == null)
      cursor = first = p;
    else
      last.next = p;

    last = p;
    length++;
  }

  /**
   *  Report the number of places that this sequence visits.
   *  @return the length of the sequence
   */
  public int getLength () {
    return length;
  }
}