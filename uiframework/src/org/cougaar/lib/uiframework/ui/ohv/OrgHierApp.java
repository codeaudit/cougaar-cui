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


import java.util.ArrayList;
import java.util.List;

import org.cougaar.lib.uiframework.ui.ohv.util.*;

/**
  Application which displays OrgHierarchies.
**/
public class OrgHierApp {

  final boolean debug=
    //true;
     false;



  public static void initApp(Object obj) {
  	RuntimeParameters myprp=new RuntimeParameters(obj);
	myprp.setProperty("ui.orgView.defaultFontSize", "11");
	myprp.setProperty("ui.orgView.defaultNodeVSpace", "31"); 
	myprp.setProperty("ui.orgView.defaultNodeHSpace", "4");
	myprp.setProperty("ui.orgView.defaultTestFile", "c:\\data\\deftest.xml");
	myprp.setProperty("ui.orgView.aggURL", "http://localhost:5555/$AGG/agg/demo/GENERIC.PSP?QUERY_ORG_HIERARCHY");

	myprp.load();
	myprp.addToSystemProperties();

  }

  public static void main(String[] args) {
    ArgVector argsV=new ArgVector(args);

    if (argsV.contains("testciv")) {
      test1(args);
    }
    if (argsV.contains("testdlv")) {
      test2(args);
    }
    if (argsV.contains("testdlev")) {
      testdlev(args);
    }
    if (argsV.contains("editor")) {
      editor(argsV);
    }
    if (argsV.contains("testTextTree")) {
      testTextTree(argsV);
    }
  }

  public static void testTextTree(ArgVector argV) {
      System.out.println("Starting testTextTree.");
      try {
	  String xmlFile;
	  OrgHierRelationship ohr;
	  initApp(new OrgHierApp());
	  xmlFile = "file:/c:/alp_workspace/xerces-1_2_0/data/personal.xml";
	  xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
	  xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/deftestb.xml";
	  xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/defTestTime.xml";
	  xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/dbjconfadm.xml";
	  
	  try {
	      if (argV.size() >0) {
		  // if using arguments, the url must be the first argument if you want it on the cmdline
		  if (argV.containsIgnoreCase("defaultTest")) {
		      xmlFile = System.getProperty("ui.orgView.defaultTestFile");
		      File file=new File(xmlFile);
		      xmlFile=file.toURL().toString();
		  } else if (argV.containsIgnoreCase("useUrlFromProps")) {
		      xmlFile = System.getProperty("ui.orgView.aggURL");
		  } else {
		      xmlFile = argV.firstElement().toString();
		  }
	      } else {
		  xmlFile = System.getProperty("ui.orgView.aggURL");
	      }
	      System.out.println("Using URL: "+xmlFile);
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
	      
	      
	  } catch (Exception ex) { ex.printStackTrace(); }
      } finally {
	  System.out.println("Leaving testTextTree.");
      }
  }

  public static void editor(ArgVector argV) {
    System.out.println("Starting editor.");
    try {
    String xmlFile;
    OrgHierRelationship ohr;
    initApp(new OrgHierApp());
    xmlFile = "file:/c:/alp_workspace/xerces-1_2_0/data/personal.xml";
    xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/deftestb.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/defTestTime.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/dbjconfadm.xml";

    try {
    if (argV.size() >0) {
      if (argV.containsIgnoreCase("defaultTest")) {
        xmlFile = System.getProperty("ui.orgView.defaultTestFile");
        File file=new File(xmlFile);
        xmlFile=file.toURL().toString();
      } else {
        xmlFile = argV.firstElement().toString();
      }
    } else {
      xmlFile = System.getProperty("ui.orgView.aggURL");
    }
    System.out.println("Using URL: "+xmlFile);
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

    OrgHierVGJControl.setMouseDraggingState(true);
    OrgHierVGJControl.setSmallPanelState(false);

    if (argV.containsIgnoreCase("dynamic")) {
      System.err.println("dynamic");
      OrgHierVGJDynLayoutTree ohvt=new OrgHierVGJDynLayoutTree(ohm);
      ohvt.show();
    } else {
      System.err.println("community");
      OrgHierVGJCommunityTree ohvt=new OrgHierVGJCommunityTree(ohm);
      ohvt.show();
    }

    } catch (Exception ex) { ex.printStackTrace(); }
    } finally {
      System.out.println("Leaving editor.");
    }
  }

  public static void test1(String[] args) {
    System.out.println("Starting testciv.");
    try {
    String xmlFile;
    OrgHierRelationship ohr;
    initApp(new OrgHierApp());
    xmlFile = "file:/c:/alp_workspace/xerces-1_2_0/data/personal.xml";
    xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/deftestb.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/defTestTime.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/dbjconfadm.xml";

    try {
    if (args.length>0) {
      if (args[0].equalsIgnoreCase("defaultTest")) {
        xmlFile = System.getProperty("ui.orgView.defaultTestFile");
        File file=new File(xmlFile);
        xmlFile=file.toURL().toString();
      } else {
        xmlFile = args[0];
      }
    } else {
      xmlFile = System.getProperty("ui.orgView.aggURL");
    }
    System.out.println("Using URL: "+xmlFile);
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

//    OrgHierVGJOrgTree ohvt=new OrgHierVGJOrgTree(ohm);
//    ohvt.show();

    OrgHierVGJCommunityTree ohvt=new OrgHierVGJCommunityTree(ohm);
    ohvt.show();

    } catch (Exception ex) { ex.printStackTrace(); }
    } finally {
      System.out.println("Leaving testciv.");
    }
  }

  public static void test2(String[] args) {
    System.out.println("Starting testdlv with args ["+args+"]");
    try {
    String xmlFile;
    OrgHierRelationship ohr;
    initApp(new OrgHierApp());
    xmlFile = "file:/c:/alp_workspace/xerces-1_2_0/data/personal.xml";
    xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/deftestb.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/defTestTime.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/dbjconfadm.xml";

    try {
    if (args.length>0) {
      if (args[0].equalsIgnoreCase("defaultTest")) {
        xmlFile = System.getProperty("ui.orgView.defaultTestFile");
        File file=new File(xmlFile);
        xmlFile=file.toURL().toString();
      } else {
        xmlFile = args[0];
      }
    } else {
      xmlFile = System.getProperty("ui.orgView.aggURL");
    }
    System.out.println("Using URL: "+xmlFile);
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

    OrgHierVGJDynLayoutTree ohvt=new OrgHierVGJDynLayoutTree(ohm);
    ohvt.show();

    } catch (Exception ex) { ex.printStackTrace(); }
    } finally {
      System.out.println("Leaving testdlv with args ["+args+"]");
    }
  }



  public static void testdlev(String[] args) {
    System.out.println("Starting testdlv with args ["+args+"]");
    try {
    String xmlFile;
    OrgHierRelationship ohr;
    initApp(new OrgHierApp());
    xmlFile = "file:/c:/alp_workspace/xerces-1_2_0/data/personal.xml";
    xmlFile = "file:/c:/dev/ui/kr/sfp/defTest.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/deftestb.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/defTestTime.xml";
    xmlFile = "file:/c:/JBuilder3/myprojects/xcs/data/dbjconfadm.xml";

    try {
    if (args.length>0) {
      if (args[0].equalsIgnoreCase("defaultTest")) {
        xmlFile = System.getProperty("ui.orgView.defaultTestFile");
        File file=new File(xmlFile);
        xmlFile=file.toURL().toString();
      } else {
        xmlFile = args[0];
      }
    } else {
      xmlFile = System.getProperty("ui.orgView.aggURL");
    }
    System.out.println("Using URL: "+xmlFile);
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

    OrgHierVGJControl.setMouseDraggingState(true);
    OrgHierVGJControl.setSmallPanelState(false);

    OrgHierVGJDynLevelTree ohvt=new OrgHierVGJDynLevelTree(ohm);
    ohvt.show();

    } catch (Exception ex) { ex.printStackTrace(); }
    } finally {
      System.out.println("Leaving testdlv with args ["+args+"]");
    }
  }




}




