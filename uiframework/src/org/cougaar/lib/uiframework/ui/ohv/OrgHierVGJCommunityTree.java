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
import java.util.*;
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
 * Graphical Tree viewer for an Organization Hierarchy.
 */
public class OrgHierVGJCommunityTree
    extends OrgHierVGJBase implements OrgHierModelViewer
{
    public OrgHierVGJCommunityTree(Collection col) {
	this(new OrgHierModel(col));
    }

    public OrgHierVGJCommunityTree(OrgHierModel ohm) {
	super(ohm);
	init(ohm);
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

  /**
   *  Show this community tree, creating a new window, if desired.
   */
  public void show () {
    show_initialized(wantNewWindow());
  }


  // called by show_initialized in superclass
  public void showGraph(VGJ vgj) {
    vgj.showGraph(false, false, true, ota, false);
  }

  public void showNewGraph(VGJ vgj) {
    vgj.showGraph(false, false, true, ota, true);
  }

  /**
   *  Create and show an org tree
   */
  public static void showOrgGraph (String forOrg) {
    if (forOrg != null)
      (new OrgHierVGJOrgTree(ohm, forOrg)).show();
  }
}



