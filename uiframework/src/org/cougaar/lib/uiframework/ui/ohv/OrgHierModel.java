package org.cougaar.lib.uiframework.ui.ohv;

//import org.apache.xerces.parsers.DOMParser;
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

/* ============= alp 6.8 ====== 
import alp.ldm.plan.ScheduleImpl;
import alp.ldm.plan.ScheduleElementImpl;
/* ===================  */
/* ============= alp 7 ======   */
import org.cougaar.domain.planning.ldm.plan.ScheduleImpl;
import org.cougaar.domain.planning.ldm.plan.ScheduleElementImpl;
/* ===================  */

import java.util.ArrayList;
import java.util.List;





/**
  Represents an Organization Hierarchy.
**/
  public class OrgHierModel {
    ScheduleImpl orgRelSched;
    OrgHierGenerator ohg;

    public OrgHierModel(Collection c) {
      orgRelSched=new ScheduleImpl(c);
      ohg=new OrgHierGenerator(c);
    }
    public TreeSet getTransitionTimes() {
      TreeSet transitionTimes = null;
    //if (transitionTimes==null) {
        transitionTimes = new TreeSet();
//     orgRelSched.iterator() keeps throwing an exception 
//     so we will use the enumeration instead 
//        for (Iterator it=orgRelSched.iterator(); it.hasNext(); ) {
//          OrgHierRelationship ohr=(OrgHierRelationship)it.next();

        for (Enumeration en=orgRelSched.getAllScheduleElements(); en.hasMoreElements() ; ) {
          OrgHierRelationship ohr=(OrgHierRelationship)en.nextElement();
          transitionTimes.add(new Long(ohr.getStartTime()));
          transitionTimes.add(new Long(ohr.getEndTime()));
        }
    //}
      return transitionTimes;
    }

    public long getStartTime() { return orgRelSched.getStartTime(); }
    public long getEndTime() { return orgRelSched.getEndTime(); }

    public Set generateRoots() {
      return ohg.generateRoots();
    }

    public HashSet getSubordinates(String parent) {
      return ohg.getSubordinates(parent);
    }

    public Set getSubordinates() {
      return ohg.getSubordinates();
    }

    public HashSet getSuperiors(String parent) {
      return ohg.getSuperiors(parent);
    }

    public Set getSuperiors() {
      return ohg.getSuperiors();
    }

    public HashSet getSuperiorsAtTime(String parent, long time) {
      OrgHierGenerator localohg=new OrgHierGenerator(getRelationshipsAtTime(time));
      return localohg.getSuperiors(parent);
    }

    public Collection getRelationshipsAtTime(long time) {
      return orgRelSched.getScheduleElementsWithTime(time);
    }
    public Collection getRelationships() {
      return orgRelSched;
    }
    public Collection getRelationshipTypes() {
      TreeSet rv=new TreeSet();
      Collection col=orgRelSched;
      OrgHierRelationship or;
      String relType;
      for (Iterator citer=col.iterator(); citer.hasNext(); ) {
        or=(OrgHierRelationship)citer.next();
        relType=or.getRelationship();
	if (relType!=null) {
	    rv.add(relType);
	}
      }
      return rv;
    }
    public Collection getRelationshipsOfType(String relType) {
      System.out.println("getRelationshipsOfType "+relType);
      // TreeSet rv=new TreeSet();
      Vector rv=new Vector();
      // Collection col=orgRelSched;
      OrgHierRelationship or;
      // schedule throws an exception whenever iterator() is called
      // so I changed this to enumeration until they fix that
      //for (Iterator citer=col.iterator(); citer.hasNext(); ) {
      //  or=(OrgHierRelationship)citer.next();
      for (Enumeration en=orgRelSched.getAllScheduleElements();
            en.hasMoreElements(); ) {
        or=(OrgHierRelationship)en.nextElement();
        System.out.println("or is "+or);
        if (or.getRelationship()!=null&&relType.equals(or.getRelationship())) {
          System.out.println("Adding it...");
          rv.add(or);
        }
      }
      return rv;
    }
    public Collection getOrgsAtTime(long time) {
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

  }     // end class OrgHierModel


