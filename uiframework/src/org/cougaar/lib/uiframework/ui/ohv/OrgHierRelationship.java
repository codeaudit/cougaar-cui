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

import org.cougaar.planning.ldm.plan.ScheduleImpl;
import org.cougaar.planning.ldm.plan.ScheduleElementImpl;

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


