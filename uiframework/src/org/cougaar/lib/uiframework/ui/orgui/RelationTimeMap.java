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

package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

/**
 *  A RelationTimeMap is a model for the time-phased relationships of an
 *  Organization with other Organizations.  All of the relationships of a
 *  particular type can be stored in a single instance of RelationTimeMap.
 */
public class RelationTimeMap {
  // store the list of relationships
  private Vector relations = new Vector();

  // a String identifier for the type of relationship represented here
  private String type = null;

  /**
   *  Create a new RelationTimeMap for managing relationships of the given type.
   *  @param name the type of relationship to be modeled here
   */
  public RelationTimeMap (String name) {
    type = name;
  }

  /**
   *  Retrieve the type of relationship modeled by this RelationTimeMap
   *  instance.
   *  @return a String identifying the relationship type
   */
  public String getType () {
    return type;
  }

  /**
   *  Incorporate a new RelationSpan into this RelationTimeMap model.  If the
   *  given span overlaps with another one for the same target Organization,
   *  then the two are combined.  Redundant entries are removed.
   *  @param span the new RelationSpan to be added to this model
   */
  public void addSpan (RelationSpan span) {
    Vector boneyard = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); ) {
      RelationSpan rs = (RelationSpan) i.next();
      if (span.adjoins(rs)) {
        span.subsume(rs);
        boneyard.add(rs);
      }
    }
    relations.removeAll(boneyard);
    relations.add(span);
  }

  /**
   *  Generate a list of all Organizations which are targets of the kind of
   *  relationship maintained by thsi model at the given time index.
   *  @param t the millisecond index of interest to the caller
   *  @return a Vector containing identifiers for relatives found for time t
   */
  public Vector getRelatives (long t) {
    Vector ret = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); ) {
      RelationSpan span = (RelationSpan) i.next();
      if (span.contains(t))
        ret.add(span.getRelative());
    }

    return ret;
  }

  /**
   *  Generate a list of all Organizations which are targets of the kind of
   *  relationship maintained by this model at any time.
   *  @return an Iterator containing all relatives in this model
   */
  public Iterator getAllRelatives () {
    Set ret = new HashSet();
    for (Iterator i = relations.iterator(); i.hasNext(); )
      ret.add(((RelationSpan) i.next()).getRelative());
    return ret.iterator();
  }

  /**
   *  Remove all accumulated relationship data pertaining to the given
   *  Organization.
   *  @param org an identifier for the Organization to be removed
   */
  public void clearRelationsToOrg (String org) {
    Vector boneyard = new Vector();
    for (Iterator i = relations.iterator(); i.hasNext(); )
      if (((RelationSpan) i.next()).getRelative().equals(org))
        i.remove();
  }
}