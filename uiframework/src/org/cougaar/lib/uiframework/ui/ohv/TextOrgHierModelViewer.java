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

import java.util.ArrayList;
import java.util.List;

/**
 *  Textual viewer for an Organization Hierarchy.
 */
public class TextOrgHierModelViewer implements OrgHierModelViewer {
  private OrgHierModel model;
  private PrintStream out;

  public TextOrgHierModelViewer (OrgHierModel model, PrintStream out) {
    this.model=model;
    this.out=out;
  }

  public void show () {
    out.println("Showing Relationships: ");
    for (Iterator timit=getTransitionTimes().iterator(); timit.hasNext(); ) {
      long time=((Long)timit.next()).longValue();
      showRelationshipsAtTime(time);
    }
    out.println("Done Showing Relationships.");
  }

  public void showRelationshipsAtTime(long time) {
    OrgHierRelationship ohr;

    Collection rels=model.getRelationshipsAtTime(time);
    String timeStr = "" + time;
    if (time == Long.MAX_VALUE) {
      timeStr = "[The end of time]";
    }
    else if (time == Long.MIN_VALUE) {
      timeStr = "Epoch";
    }
    out.println("Relationships at time " + timeStr + ": ");
    Vector checked = new Vector();
    for (Iterator rit = rels.iterator(); rit.hasNext(); ) {
      ohr = (OrgHierRelationship) rit.next();
      if (!checked.contains(ohr.getId())) {
        checked.add(ohr.getId());
        Collection col = model.getSuperiorsAtTime(ohr.getId(), time);
        if (col == null || col.isEmpty()) {
          out.println("    " + ohr.getId() + " is a top-level organization.");
        }
        else {
          out.println("    " + ohr.getId() + " has Superior(s) " + col);
        }
      }
    }
  }

  public TreeSet getTransitionTimes() {
    return model.getTransitionTimes();
  }

  public long getStartTime () {
    return model.getStartTime();
  }

  public long getEndTime () {
    return model.getEndTime();
  }
}
