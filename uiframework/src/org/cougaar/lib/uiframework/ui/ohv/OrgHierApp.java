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
import java.util.Vector;

import org.cougaar.lib.uiframework.ui.ohv.util.*;

/**
 *  Application which displays OrgHierarchies.
 */
public class OrgHierApp {
  // This is a temporary measure--this value was being held as a system
  // property, which is as bad as possible.  Currently it is a static class
  // variable.  Eventually, this will be either an instance class variable or
  // a method parameter, as is appropriate.
  private static String url_for_rels = null;

  /**
   *  Initalizes default values for several properties and then attempts
   *  to load properties from a configuration file.
   */
  public static void initApp () {
    RuntimeParameters myprp=new RuntimeParameters();
    myprp.setProperty("ui.orgView.defaultFontSize", "11");
    myprp.setProperty("ui.orgView.defaultNodeVSpace", "31");
    myprp.setProperty("ui.orgView.defaultNodeHSpace", "4");
    myprp.setProperty("ui.orgView.defaultTestFile", "c:\\data\\deftest.xml");
    myprp.setProperty("ui.orgView.aggURL", "http://localhost:5555/$AGG/agg/demo/GENERIC.PSP?QUERY_ORG_HIERARCHY");

    myprp.load();
    myprp.addToSystemProperties();
  }

  /**
   *  Main executable for running viewer application and test applications.
   */
  public static void main (String[] args) {
    ArgVector argV=new ArgVector(args);

    if (argV.size() == 0) {
	showCommunityView(argV);
    }
    if (argV.contains("testciv")||argV.contains("cview")) {
	showCommunityView(argV);
    }
    if (argV.contains("testdlv")||argV.contains("dynview")) {
      test2(argV);
    }
    if (argV.contains("testdlev")||argV.contains("dynlevview")) {
      testdlev(argV);
    }
    if (argV.contains("editor")) {
      editor(argV);
    }
    if (argV.contains("testTextTree")||argV.contains("texttree")) {
      testTextTree(argV);
    }
  }

  private static Vector parseInput (ArgVector argV) {
    Vector v = new Vector();
    try {
      String xmlFile = null;
      if (argV.size() > 0) {
        if (argV.containsIgnoreCase("defaultTest"))
          xmlFile = (new File(
            System.getProperty("ui.orgView.defaultTestFile"))).toString();
        else if (argV.containsIgnoreCase("useUrlFromProps"))
          xmlFile = System.getProperty("ui.orgView.aggURL");
        else
          xmlFile = argV.firstElement().toString();
      }
      else {
        xmlFile = System.getProperty("ui.orgView.aggURL");
      }

      // set the static class variable, this is still needed sometimes;
      // eventually, this should be removed
      url_for_rels = xmlFile;

      System.out.println("Using URL:  " + xmlFile);
      OrgHierParser ohp = new OrgHierParser(xmlFile);
      System.out.print("Parsing input XML...");
      v = ohp.parse();
      System.out.println("done parsing.");
    }
    catch (Exception eek) {
      System.out.println("Unable to read input--" + eek);
    }

    return v;
  }

  public static void testTextTree (ArgVector argV) {
    System.out.println("Starting testTextTree.");
    try {
      OrgHierRelationship ohr;
      initApp();

      OrgHierModel ohm = new OrgHierModel(parseInput(argV));

      // Show the results through the plain text viewer, if desired
      // (new TextOrgHierModelViewer(ohm, System.out)).show();

      // Show the results through the text tree viewer
      (new OrgHierTextTree(ohm)).show();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      System.out.println("Leaving testTextTree.");
    }
  }

  public static void editor (ArgVector argV) {
    System.out.println("Starting editor.");
    try {
      OrgHierRelationship ohr;
      initApp();

      OrgHierModel ohm = new OrgHierModel(parseInput(argV));

      OrgHierVGJControl.setMouseDraggingState(true);
      OrgHierVGJControl.setSmallPanelState(false);

      if (argV.containsIgnoreCase("dynamic"))
        (new OrgHierVGJDynLayoutTree(ohm)).show();
      else
        (new OrgHierVGJCommunityTree(ohm)).show();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      System.out.println("Leaving editor.");
    }
  }

  public static OrgHierModel updateModel () {
    return new OrgHierModel((new OrgHierParser(url_for_rels)).parse());
  }

  public static void showCommunityView (ArgVector argV) {
    System.out.println("Starting civ.");
    try {
      OrgHierRelationship ohr;
      initApp();

      OrgHierModel ohm = new OrgHierModel(parseInput(argV));

      // Show the data with the text tree viewer, if desired
      // (new OrgHierTextTree(ohm)).show();

      // Show the data with the community tree viewer
      (new OrgHierVGJCommunityTree(ohm)).show();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      System.out.println("Leaving civ.");
    }
  }

  public static void test2 (ArgVector argV) {
    System.out.println("Starting testdlv...");
    try {
      OrgHierRelationship ohr;
      initApp();

      OrgHierModel ohm = new OrgHierModel(parseInput(argV));

      // show the data with the text tree viewer, if desired
      // (new OrgHierTextTree(ohm)).show();

      // show the data with the dynamic tree viewer
      (new OrgHierVGJDynLayoutTree(ohm)).show();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      System.out.println("Leaving testdlv.");
    }
  }

  public static void testdlev(ArgVector argV) {
    System.out.println("Starting testdlv...");
    try {
      OrgHierRelationship ohr;
      initApp();

      OrgHierModel ohm = new OrgHierModel(parseInput(argV));

      // Show the data with the text tree viewer, if desired
      // (new OrgHierTextTree(ohm)).show();

      OrgHierVGJControl.setMouseDraggingState(true);
      OrgHierVGJControl.setSmallPanelState(false);

      OrgHierVGJDynLevelTree ohvt=new OrgHierVGJDynLevelTree(ohm);
      ohvt.show();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      System.out.println("Leaving testdlv.");
    }
  }
}
