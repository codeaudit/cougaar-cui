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

import java.util.StringTokenizer;

/**
 * Graphical Tree viewer for an Organization Hierarchy.
 */
public class OrgHierVGJOrgTree
    extends OrgHierVGJBase implements OrgHierModelViewer
{
  private String name;
  private RelList relList;

  /**
   *  Report to the caller the name of this view, if any.
   */
  public String getName () {
    return name;
  }

  public OrgHierVGJOrgTree (Collection col, String name) {
    this(new OrgHierModel(col), name);
  }

  public OrgHierVGJOrgTree (OrgHierModel ohm, String forOrg) {
    super(ohm);
    name = forOrg;
  }

    public OrgHierVGJBase create(Collection rels) {
	return new OrgHierVGJOrgTree(rels, name);
    }

    // The following methods support show_initialized
    // They will probably end up being implementations which are called by
    //      a method in the abstract superclass
    String getInitFileName() {
	String loadClusterInitPath=RuntimeParameters
	    .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJOrgTree.LoadClusterInitPath",
				   "C:/data/default/ohv/init");
	String loadClusterInitSuffix=RuntimeParameters
	    .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJOrgTree.LoadClusterInitSuffix",
				   "_init.gml");
	String fileSeparator=System.getProperty("file.separator"); // "\\";
	return loadClusterInitPath+fileSeparator+name+loadClusterInitSuffix;
    }

    private boolean wantProviderControl() {
      String showProviderControl = RuntimeParameters.getLoudSystemProperty(
        "showProviderControl", "true");
      return showProviderControl != null &&
        showProviderControl.trim().equalsIgnoreCase("true");
    }

  /**
   *  Show any additional controls needed by this class of UI.
   */
  public void showProviderControl () {
    if (wantProviderControl()) {
      relList = new RelList(ohm, mygraph, relListAction);
      vgj.addControl(relList);
    }
  }

    private boolean wantNewWindow() {
	return true;
    }

    String getTitle() { return name+" Community"; }

  /**
   *  Show this org tree and create a new window, if desired.
   */
  public void show () {
    show_initialized(wantNewWindow());
  }

    // Called by method in superclass
    public void showGraph(VGJ vgj) {
	    vgj.showGraph(false, false, false, ota, false);
    }
    public void showNewGraph(VGJ vgj) {
	    vgj.showGraph(false, false, false, ota, true);
    }


    ItemListener relListAction=new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		System.out.println("in relListAction updrel "+e.getStateChange());
		System.out.println("in relListAction updrel "+e.getItem());
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    System.out.println("in relListAction updrel selected");
		    String relType = (String) e.getItem();
		    System.out.println("   reltype is "+relType);
		    // update graph based on relType
		    updateRelationships(relType);

		    // set title here
		    vgj.setCanvasTitle(name+" Community with "+relType+" Relationships");

		    System.out.println("out relListAction");
		}
	    }
	};

}



