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

/* ============= alp 6.8 ====== 
import alp.ldm.plan.ScheduleImpl;
import alp.ldm.plan.ScheduleElementImpl;
/* ===================  */
/* ============= alp 7 ======  */ 
import org.cougaar.domain.planning.ldm.plan.ScheduleImpl;
import org.cougaar.domain.planning.ldm.plan.ScheduleElementImpl;
/* ===================  */



import org.cougaar.lib.uiframework.ui.ohv.util.DomUtil;
import java.util.ArrayList;
import java.util.List;

/**
  Parses OrgHier xml and provides a vector of OrgHierRelationship objects.
  Collaborates with ClusterParser(s) and  OrgHierRelationship.
**/
public class OrgHierParser {

  final boolean debug=
    //true;
     false;

  // DOMParser parser = new DOMParser();
  // Document document;
  // NodeList clusters;

  String indent1="  ";
  String indent2=indent1+indent1;

  String urlName;
  public OrgHierParser(String urlName) {
    this.urlName=urlName;
  }

  public Vector parse() {
    //Element cel;
    //String id, rel, other;
    NodeList clusters;
    ClusterParser cp;
    Vector rcVect=new Vector();

    try {
      //DOMParserWrapper parser = new dom.wrappers.DOMParser();
      //document = parser.parse(urlName);

      Document document = DomUtil.getDocument(urlName);


      clusters = document.getElementsByTagName("Cluster");
      System.out.println("Length of clusters NodeList: "+clusters.getLength());

      System.out.println("The nodes: ");
      for (int idx=0; idx<clusters.getLength(); idx++) {

        System.out.println();
        System.out.print(indent1+"Node "+(idx+1)+": ");
        //Node ncel=clusters.item(idx);
        //System.out.println(indent2+"ncel.gettype is ["+ncel.getNodeType()+"]");
        cp=new ClusterParser((Element)clusters.item(idx));
        //cel = (Element) clusters.item(idx);
        //id=cel.getAttribute("ID");
        //other=cel.getNamespaceURI();
        //rel=cel.getNodeValue();
        if (cp.isValidCluster()) {
          System.out.println(indent2+"ID is ["+cp.getId()+"]");
          if (cp.hasSuperior()) {
            System.out.println(indent2+"\tSuperior is ["+cp.getSuperior()+"]");
          }
            System.out.println(indent2+"\tStart Time is ["+cp.getStartTime()+"]");
            System.out.println(indent2+"\tEnd Time is ["+cp.getEndTime()+"]");

          rcVect.add(new OrgHierRelationship(cp));
        }
        
        //System.out.println(indent2+"NodeType is ["+cel.getNodeType()+"]");
        //System.out.println(indent2+"NamespaceUR is ["+other+"]");
        //System.out.println(indent2+"NodeValue is ["+rel+"]");
        // System.out.println("done parsing");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return rcVect;
}
    /*
    public static Document getDocument(String urlName ){
        try{
                    InputStream fr = (new URL(urlName)).openStream();
                    InputSource is = new InputSource(fr);

                    DOMParser domp = new DOMParser();
                    domp.setErrorHandler(new ErrorHandler(){
                          public void error(SAXParseException exception) {
                             System.err.println("[ErrorHandler.error]: " + exception);
                           }
                           public void fatalError(SAXParseException exception) {
                                 System.err.println("[ErrorHandler.fatalError]: " + exception);
                           }
                           public void warning(SAXParseException exception) {
                                 System.err.println("[ErrorHandler.warning]: " + exception);
                            }
                        }
                    );

                    domp.parse(is);
                    Document doc = domp.getDocument();
                    return doc;
        } catch (Exception ex ) {
           ex.printStackTrace();
        }
        return null;
  }
    */

  public static TreeSet getTransitionTimes(ScheduleImpl sched) {
    TreeSet transitionTimes = null;
    //if (transitionTimes==null) {
      transitionTimes = new TreeSet();
      for (Iterator it=sched.iterator(); it.hasNext(); ) {
        OrgHierRelationship ohr=(OrgHierRelationship)it.next();
        transitionTimes.add(new Long(ohr.getStartTime()));
        transitionTimes.add(new Long(ohr.getEndTime()));
      }
    //}
    return transitionTimes;
  }


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


