/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
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

package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  IntDimNode is the base class for a range of integers.  Though its interface
 *  conforms to that of a node within a hierarchical structure, its
 *  implementation is such that it manages a virtual hierarchy based on the
 *  integers within its purview.  The "name" of a range is actually a String
 *  indicating its extent.
 *  </p><p>
 *  Within the DimNode API, an IntDimNode behaves as follows:
 *  <ul>
 *    <li>hasChildren reports true for any range containing two or more values;</li>
 *    <li>hasChild, for a String that can be interpreted as an integer range,
 *          returns a dynamically fabricated child with that range, if it is a
 *          subset of the node's range;</li>
 *    <li>getChildren returns an enumeration containing a singleton range for
 *          each integer contained in the node's range.</li>
 *  </ul>
 */
public class IntDimNode extends DimNode {
  private int start = 0;
  private int end = -1;

  /**
   *  Construct a range of integers with the default initial configuration
   */
  public IntDimNode () {
  }

  /**
   *  Construct a range of integers whose extent is chosen by parsing the given
   *  String.  In this implementation, the parser looks for one or two integers
   *  expressed as tokens in a String.  The number or numbers, if found, are
   *  interpreted as the beginning and end of a range of consecutive integers.
   *  @param s the range spec. for this IntDimNode
   */
  public IntDimNode (String s) {
    setName(s);
    StringTokenizer tok = new StringTokenizer(s);
    if (tok.hasMoreTokens())
      start = Integer.parseInt(tok.nextToken());
    if (tok.hasMoreTokens())
      end = Integer.parseInt(tok.nextToken());
    else
      end = start;
  }

  /**
   *  Construct an IntDimNode with a single contained value, as specified by
   *  the caller.
   *  @param n the one value contained in this range
   */
  public IntDimNode (int n) {
    this(n, n);
  }

  /**
   *  Construct an IntDimNode with values ranging between those provided by the
   *  caller (inclusive).
   *  @param n1 the beginning of the range (i.e., the least value contained)
   *  @param n2 the end of the range (i.e., the greatest value contained)
   */
  public IntDimNode (int n1, int n2) {
    start = n1;
    end = n2;
    if (n1 == n2)
      setName(String.valueOf(n1));
    else if (n1 < n2)
      setName(n1 + " " + n2);
    else
      throw new RuntimeException("Creating invalid range from \"" + n1 +
        "\" to \"" + n2 + "\"");
  }

  /**
   *  Retrieve the losest member of the range represented by this node.
   *  @return the left endpoint
   */
  public int getStart () {
    return start;
  }

  /**
   *  Retrieve the greatest member of the ragne represented by this node.
   *  @return the right endpoint
   */
  public int getEnd () {
    return end;
  }

  /**
   *  Report whether this node has children.  In this case, true is reported
   *  if the range contains two or more integer values (which may be separated
   *  into two or more subranges).
   *  @return true iff this range can be subdivided
   */
  public boolean hasChildren () {
    return end > start;
  }

  /**
   *  Find a child node corresponding to a given specification, if possible.  In
   *  this case, assuming the spec. is a valid integer range and, moreover, that
   *  it is a nonempty subrange of this one, return a dynamically generated,
   *  virtual child node.  If the supposed child is not valid, then return null.
   *  @param s the candidate range spec for the child node
   *  @return the corresponding IntDimNode, if applicable, or null
   */
  public DimNode hasChild (String s) {
    IntDimNode child = new IntDimNode(s);
    if (start <= child.start && child.start <= child.end && child.end <= end &&
        start < end)
    {
      child.setDimension(getDimension());
      return child;
    }
    return null;
  }

  /**
   *  Create an Enumeration containing the children of this range.  Actually,
   *  only the "real" children (i.e., the singleton integer values) are
   *  returned.
   *  @return an Enumeration of the values in this range
   */
  public Enumeration getChildren () {
    if (hasChildren())
      return new IntDimNodeChildren(start, end, getDimension());
    return new IntDimNodeChildren(getDimension());
  }

  // Inner class used to enumerate the individual elements of an integer range.
  private static class IntDimNodeChildren implements Enumeration {
    private QueryDimension dimension = null;
    private int start = 0;
    private int finish = -1;

    private int current = 0;

    // create an empty list of children
    public IntDimNodeChildren (QueryDimension d) {
      dimension = d;
    }

    // create a list of the values between n1 and n2, inclusive
    public IntDimNodeChildren (int n1, int n2, QueryDimension d) {
      start = n1;
      finish = n2;
      dimension = d;
      current = start;
    }

    public Object nextElement () {
      if (current <= finish) {
        IntDimNode ret = new IntDimNode(current++);
        ret.setDimension(dimension);
        return ret;
      }
      return null;
    }

    public boolean hasMoreElements () {
      return current <= finish;
    }
  }
}