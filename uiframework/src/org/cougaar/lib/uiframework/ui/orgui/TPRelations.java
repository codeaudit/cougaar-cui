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

package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

/**
 *  TPRelations is a class that manages various types of relationships in which
 *  an Organization might participate.  The specific relationship types are
 *  each maintained within RelationTimeMap (q.v.) instances, tabulated by the
 *  relationship type identifiers.
 */
public class TPRelations {
  // in case the relation type is not present, return an empty iterator...
  private static class EmptyIterator implements Iterator {
    public boolean hasNext () {
      return false;
    }

    public Object next () {
      return null;
    }

    public void remove () {
    }
  }
  private static Iterator EMPTY = new EmptyIterator();

  // tabulate the relationships by type
  private Hashtable table = new Hashtable();

  // the name of the organization to which this relationship model applies
  private String orgName = null;

  /**
   *  Create a new relationship model for the identified Organization
   *  @param name a String identifying the Organization
   */
  public TPRelations (String name) {
    orgName = name;
  }

  /**
   *  Tell which Organization is being representer here.
   *  @return a String identifying the Organization
   */
  public String getOrgName () {
    return orgName;
  }

  /**
   *  Add a new relationship to the model.
   *  @param rel a String identifying the relationship type
   *  @param other a String identifying the other participant in the relationship
   *  @param start the millisecond index on which the relationship begins
   *  @param end the millisecond index on which the relationship ends
   */
  public void addRelation (String rel, String other, long start, long end) {
    RelationTimeMap rtm = (RelationTimeMap) table.get(rel);
    if (rtm == null) {
      rtm = new RelationTimeMap(rel);
      table.put(rel, rtm);
    }
    rtm.addSpan(new RelationSpan(other, start, end));
  }

  /**
   *  Clear out all relationships of the provided type.
   *  @param rel a String identifying the type of relationship to be removed
   */
  public void clearRelationsOfType (String rel) {
    table.remove(rel);
  }

  /**
   *  Clear out all relationships to the given Organization.
   *  @param org an identifier for the Org. to which relations should be removed
   */
  public void clearRelationsToOrg (String org) {
    for (Enumeration e = table.elements(); e.hasMoreElements(); )
      ((RelationTimeMap) e.nextElement()).clearRelationsToOrg(org);
  }

  /**
   *  Clear this model of all relationship information.
   */
  public void clear () {
    table.clear();
  }

  /**
   *  Generate a list of all Organizations which are targets of the given
   *  relationship, according to this model, at any time.
   *  @param rel a String identifying the relationship type
   *  @return an Iterator containing identifiers for all relatives of type rel
   */
  public Iterator getRelatives (String rel) {
    RelationTimeMap rtm = (RelationTimeMap) table.get(rel);
    if (rtm != null)
      return rtm.getAllRelatives();
    return EMPTY;
  }

  /**
   *  Generate a list of all relationship schedules held by this table.
   *  @return an Enumeration of RelationTimeMap instances
   */
  public Enumeration getRelationMaps () {
    return table.elements();
  }
}