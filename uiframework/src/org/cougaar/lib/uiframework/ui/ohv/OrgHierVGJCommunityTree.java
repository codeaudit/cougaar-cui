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
import java.awt.Color;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.*;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.VGJ;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;
import org.cougaar.lib.uiframework.ui.ohv.util.*;





 /**
 Graphical Tree viewer for an Organization Hierarchy.
 **/

public class OrgHierVGJCommunityTree 
    extends OrgHierVGJBase
    implements OrgHierModelViewer
{
    
    public OrgHierVGJCommunityTree(Collection col) { 
	this(new OrgHierModel(col));
    }
    
    public OrgHierVGJCommunityTree(OrgHierModel ohm) {	
	super(ohm);
	init(ohm);
 	// vgjBase=this;
    }
    
    public OrgHierVGJBase create(Collection rels) {
	return new OrgHierVGJCommunityTree(rels);
    }
    
    // The following methods support show_initialized
    // They will probably end up being implementations which are called by
    //      a method in the abstract superclass
    String getInitFileName() {
	String loadClusterInitPath=RuntimeParameters
	    .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitPath",
				   "C:/data/default/ohv/init");
	String loadClusterInitSuffix=RuntimeParameters
	    .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitSuffix",
				   "_init.gml");
	String loadClusterInitName=RuntimeParameters
	    .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitName",
				   "/overview");
	return loadClusterInitPath+loadClusterInitName+loadClusterInitSuffix;
    }

    public void showProviderControl() {}
    
    boolean wantNewWindow() {
	return false;
    }
    
    String getTitle() { return "Overview"; }

    public void show_initialized() {
      out.println("Here is the INITIALIZED OrgHierVGJCommunityTree: "); 
      String initFileName=getInitFileName();
      String title=getTitle();
      boolean wantNewWindow=wantNewWindow();

      show_initialized(wantNewWindow);
 
      out.println("Finished showing the OrgHierVGJCommunityTree.");
    }


    // called by show_initialized in superclass
    public void showGraph(VGJ vgj) {
	    vgj.showGraph(false, false, true, ota, false);
    }
    public void showNewGraph(VGJ vgj) {
	    vgj.showGraph(false, false, true, ota, true);
    }


    public static void showOrgGraph(String forOrg) {
	String selOrg=forOrg;
      System.out.println("show org graph for selected node: "+selOrg);
      if (selOrg==null) {
        System.err.println("==========================");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("==========================");
      } else {
      // create and show vgjOrgTree (use my model)
	  System.out.println("vgjcommunitytree ota updating model");
	  OrgHierVGJOrgTree ohvt=new OrgHierVGJOrgTree(ohm, selOrg);
	  ohvt.show();
      }
    }
}



