/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv;



import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.net.URL;


import java.io.IOException;

import org.cougaar.domain.planning.ldm.plan.ScheduleImpl;
import org.cougaar.domain.planning.ldm.plan.ScheduleElementImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *  Represents relationships between orgs in a society.
 *  Constructed from a ClusterParser.
 *  Used by an OrgHierModel.
 */
public class OrgHierRelationship extends ScheduleElementImpl {
  public OrgHierRelationship(ClusterParser cp) {
    String other=null;
    hasSuperior=cp.hasSuperior();
    other=cp.getOtherId();
    init(cp.getId(),cp.getStartTime(), cp.getEndTime(), cp.getRelationship(), other);
  }

  public boolean equals(OrgHierRelationship rhs) {
    boolean rc= (id.equals(rhs.id)
          && other.equals(rhs.other)
          && rel.equals(rhs.rel)
          && getStartTime()==rhs.getStartTime()
          && getEndTime()==rhs.getEndTime());
    return rc;
  }

  boolean isValidCluster() { return (id!=null&&id!=""); }
  String getId() { return id; }
  String getOtherId() { return other; }
  String getRelationship() { return rel; }

  private String id=null, other=null, rel=null;
  private boolean hasSuperior=false;
  private void init(String id, long start, long end, String rel, String other) {
    setStartTime(start);
    setEndTime(end);
    this.id=id;
    this.other=other;
    this.rel=rel;
  }
  boolean hasSuperior() {return hasSuperior; }
  boolean hasA(String rel) {return rel!=null&&rel.equalsIgnoreCase(rel)&&other!=null; }
  public String toString() {
    String rc="OrgHierRelationship(start="+getStartTime()+", end="+getEndTime()+", id="+id+", rel="+rel+", other="+other+")";
    return rc;
  }
}


