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


import java.util.ArrayList;
import java.util.List;

import org.cougaar.lib.uiframework.ui.ohv.util.ArgVector;
import org.cougaar.lib.uiframework.ui.ohv.util.*;

/**
  Application which edits initial files.
**/
public class OrgHierEditorApp {

  final boolean debug=
    //true;
     false;


  public static void initApp() {
  	RuntimeParameters myprp=new RuntimeParameters();
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

      editor(argsV);
  }

  public static void editor(ArgVector argV) {
    System.out.println("Starting editor.");
    try {
    String xmlFile;
    OrgHierRelationship ohr;
    initApp();

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

    OrgHierModel ohm=new OrgHierModel(v);

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

}
