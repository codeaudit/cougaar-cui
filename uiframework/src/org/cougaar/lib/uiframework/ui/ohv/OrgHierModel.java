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

import org.apache.xerces.parsers.DOMParser;

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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.IOException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.cougaar.planning.ldm.plan.ScheduleImpl;
import org.cougaar.planning.ldm.plan.ScheduleElementImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *  Represents an Organization Hierarchy.
 */
public class OrgHierModel {
  ScheduleImpl orgRelSched;
  OrgHierGenerator ohg;

  public OrgHierModel (Collection c) {
    orgRelSched = new ScheduleImpl(c);
    ohg=new OrgHierGenerator(c);
  }

  public TreeSet getTransitionTimes () {
    TreeSet transitionTimes = null;
    transitionTimes = new TreeSet();

    for (Enumeration en = orgRelSched.getAllScheduleElements(); en.hasMoreElements(); ) {
      OrgHierRelationship ohr = (OrgHierRelationship)en.nextElement();
      transitionTimes.add(new Long(ohr.getStartTime()));
      transitionTimes.add(new Long(ohr.getEndTime()));
    }
    return transitionTimes;
  }

  public long getStartTime () {
    return orgRelSched.getStartTime();
  }

  public long getEndTime () {
    return orgRelSched.getEndTime();
  }

  public Set generateRoots () {
    return ohg.generateRoots();
  }

  public HashSet getSubordinates(String parent) {
    return ohg.getSubordinates(parent);
  }

  public Set getSubordinates () {
    return ohg.getSubordinates();
  }

  public HashSet getSuperiors (String parent) {
    return ohg.getSuperiors(parent);
  }

  public Set getSuperiors () {
    return ohg.getSuperiors();
  }

  public HashSet getSuperiorsAtTime (String parent, long time) {
    OrgHierGenerator localohg=new OrgHierGenerator(getRelationshipsAtTime(time));
    return localohg.getSuperiors(parent);
  }

  public Collection getRelationshipsAtTime (long time) {
    return orgRelSched.getScheduleElementsWithTime(time);
  }

  public Collection getRelationships () {
    return orgRelSched;
  }

  /**
   *  Report all of the relationship types found in the current model.
   */
  public Collection getRelationshipTypes () {
    TreeSet rv=new TreeSet();
    Collection col=orgRelSched;
    OrgHierRelationship or;
    String relType;
    for (Iterator citer = col.iterator(); citer.hasNext(); ) {
      or = (OrgHierRelationship) citer.next();
      relType = or.getRelationship();
      if (relType != null)
        rv.add(relType);
    }
    return rv;
  }

  /**
   *  Report only those relationships types that are found between two members
   *  of the provided set of organizations.
   */
  public Collection getRelationshipTypes (Set org) {
    TreeSet rels = new TreeSet();
    Enumeration e = orgRelSched.getAllScheduleElements();
    while (e.hasMoreElements()) {
      OrgHierRelationship r = (OrgHierRelationship) e.nextElement();
      String t = r.getRelationship();
      if (t != null && org.contains(r.getId()) && org.contains(r.getOtherId()))
        rels.add(t);
    }
    return rels;
  }

  public Collection getRelationshipsOfType (String relType) {
    Vector rv=new Vector();
    OrgHierRelationship or;
    for (Enumeration en=orgRelSched.getAllScheduleElements();
        en.hasMoreElements(); )
    {
      or = (OrgHierRelationship) en.nextElement();
      if (or.getRelationship()!=null&&relType.equals(or.getRelationship()))
        rv.add(or);
    }
    return rv;
  }

  public Collection getOrgsAtTime (long time) {
    TreeSet rv=new TreeSet();
    Collection col=orgRelSched.getScheduleElementsWithTime(time);
    OrgHierRelationship or;
    for (Iterator citer=col.iterator(); citer.hasNext(); ) {
      or=(OrgHierRelationship)citer.next();
      rv.add(or.getId());
    }
    return rv;
  }

  public Collection getOrgs() {
    TreeSet rv=new TreeSet();
    OrgHierRelationship or;
    for (Enumeration en =orgRelSched.getAllScheduleElements(); en.hasMoreElements(); ) {
      or=(OrgHierRelationship)en.nextElement();
      rv.add(or.getId());
    }
    return rv;
  }
}
