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

import org.cougaar.lib.uiframework.ui.ohv.util.DomUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *  Parses OrgHier xml and provides a vector of OrgHierRelationship objects.
 *  Collaborates with ClusterParser(s) and  OrgHierRelationship.
 */
public class OrgHierParser {
  String indent1="  ";
  String indent2=indent1+indent1;

  String urlName;
  public OrgHierParser(String urlName) {
    this.urlName=urlName;
  }

  public Vector parse() {
    NodeList clusters;
    ClusterParser cp;
    Vector rcVect = new Vector();

    try {
      Document document = DomUtil.getDocument(urlName);

      clusters = document.getElementsByTagName("Cluster");
      System.out.println("Length of clusters NodeList: "+clusters.getLength());

      for (int idx = 0; idx < clusters.getLength(); idx++) {
        cp = new ClusterParser((Element) clusters.item(idx));
        if (cp.isValidCluster())
          rcVect.add(new OrgHierRelationship(cp));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return rcVect;
  }

  public static TreeSet getTransitionTimes(ScheduleImpl sched) {
    TreeSet transitionTimes = null;
      transitionTimes = new TreeSet();
      for (Iterator it=sched.iterator(); it.hasNext(); ) {
        OrgHierRelationship ohr=(OrgHierRelationship)it.next();
        transitionTimes.add(new Long(ohr.getStartTime()));
        transitionTimes.add(new Long(ohr.getEndTime()));
      }
    return transitionTimes;
  }

// - - - - - - - Testing Code below this point - - - - - - - - - - - - - - - - -
  public static void main(String[] args) {
    test(args);
  }

  public static void test(String[] args) {
    String xmlFile;
    OrgHierRelationship ohr;
    xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
    OrgHierParser orgHierParser = new OrgHierParser(xmlFile);

    Vector v = orgHierParser.parse();

    System.out.println("Finished parsing.");

    System.out.println();
    System.out.println("OrgHierModel output: ");
    OrgHierModel ohm=new OrgHierModel(v);
    System.out.println("Start time: "+ohm.getStartTime());
    System.out.println("Transition times: "+ohm.getTransitionTimes());
    System.out.println("End time: "+ohm.getEndTime());
    System.out.println("Finished OrgHierModel output.");

    System.out.println();
    System.out.println("OrgHierModelViewer output: ");
    OrgHierModelViewer ohmv=new TextOrgHierModelViewer(ohm, System.out);
    ohmv.show();
    System.out.println("Finished OrgHierModelViewer output.");

    OrgHierTextTree ohtt=new OrgHierTextTree(ohm);
    ohtt.show();
  }
}


