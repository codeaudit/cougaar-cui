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

import java.io.*;
import java.util.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.planning.ldm.plan.RelationshipImpl;
import org.cougaar.domain.planning.ldm.plan.RelationshipSchedule;
import org.cougaar.domain.planning.ldm.plan.Role;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;
import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;

/**
 *  This is a class of adapters for the PSP_GenericReaderWriter whose purpose
 *  is to report on relationships between the host Cluster (Organization) and
 *  other Organizations in the society.  In order to work properly, an instance
 *  of this class must be associated with a subscription to Organization assets,
 *  which it will dutifully search for the one that represents the host Cluster
 *  (i.e., one whose isSelf() method returns true).  Results are based on the
 *  RelationshipSchedule of the self-org.
 */
public class OrgSubAdapter extends CustomQueryBaseAdapter {
  // store the results until requested by the container
  protected Vector relations = new Vector();

  // the event to which the current response corresponds
  protected String event = null;

  /**
   *  Given a collection of Organization assets found on the logplan, find the
   *  one that represents the host Cluster and store the useful parts of its
   *  RelationshipSchedule for later retrieval by the returnVal method (q.v.).
   *  For the moment, the type of event being reported (e.g., ADD, REMOVE, or
   *  CHANGE) is not taken into consideration.  For standard polling, this is
   *  of no consequence.
   *
   *  @param matches the Collection of Organizations found on the logplan
   *  @param eventType the type of event being reported
   */
  public void execute (Collection matches, String eventType) {
    // cache the event type for future reference
    event = eventType;
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    // search for the self-org and ignore all others
    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Organization org = (Organization) i.next();
      if (org.isSelf()) {
        String name = org.getItemIdentificationPG().getNomenclature();
        RelationshipSchedule rs = org.getRelationshipSchedule();
        for (Iterator j = rs.filter(Const.TRUE).iterator(); j.hasNext(); ) {
          RelationshipImpl r = (RelationshipImpl) j.next();
          String type = rs.getOtherRole(r).getName();
          String other = ((Organization)
            rs.getOther(r)).getItemIdentificationPG().getNomenclature();
          long start = r.getStartTime();
          long end = r.getEndTime();
          relations.add(new Bond(name, other, type, start, end));
        }
      }
    }
  }

  /**
   *  Starting with the results produced by a previous call to execute (q.v.),
   *  generate the XML response to be sent to the client (probably an AggAgent).
   *  The cached results are cleared after being converted and sent out.
   *  @param out the OutputStream to which output is directed.
   */
  public void returnVal (OutputStream out) {
    // if this is not an ADD, ignore it--this works with standard polling
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    PrintStream ps = new PrintStream(out);
    ps.println(Const.XML_HEADER);
    Const.addOpenTag(ps, Const.ORG_RELS);
    ps.println();
    for (Iterator i = relations.iterator(); i.hasNext(); )
      ((Bond) i.next()).toXml(ps);
    Const.addCloseTag(ps, Const.ORG_RELS);
    ps.println();
    ps.flush();
    relations.clear();
  }

  /**
   *  This class is the internal representation of a relationship between two
   *  Organizations.  It contains the names of the two orgs, the relationship
   *  identifier, and the starting and ending time of the relationship's
   *  duration.  In addition to storing this information, the class also
   *  contains the logic for converting it to an XML representation.
   */
  protected static class Bond {
    private String org = null;
    private String relative = null;
    private String relationship = null;
    private long startTime = 0;
    private long endTime = -1;

    /**
     *  Create a new Bond populated with the relevant data.
     *  @param o the name of the primary organization
     *  @param r the name of the primary organization's relative
     *  @param rel the type of relative that <i>r</i> is to <i>o</i>
     *  @param start the millisecond index on which the relationship starts
     *  @param end the millisecond index on which the relationship ends
     */
    public Bond (String o, String r, String rel, long start, long end) {
      org = o;
      relative = r;
      relationship = rel;
      startTime = start;
      endTime = end;
    }

    /**
     *  Represent this Bond as an XML element (probably as part of a larger
     *  Document).
     *  @return a String containing the XML code for this Bond
     */
    public void toXml (PrintStream ps) {
      Const.addOpenTag(ps, Const.CLUSTER, Const.ID_ATTRIBUTE, org);
      Const.addTag(ps, Const.RELATIVE, relative);
      Const.addTag(ps, Const.REL_TYPE, relationship);
      Const.addTag(ps, Const.START, String.valueOf(startTime));
      Const.addTag(ps, Const.END, String.valueOf(endTime));
      Const.addCloseTag(ps, Const.CLUSTER);
      ps.println();
    }
  }
}