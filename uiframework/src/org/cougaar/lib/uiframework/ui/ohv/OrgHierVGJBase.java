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
 *  Helper classs for Graphical Tree viewers for an Organization Hierarchy.
 */

abstract public class OrgHierVGJBase {
    static protected OrgHierModel ohm;
    protected String textTree;
    protected String delim="\n";
    protected Stack branchStack = new Stack();
    protected int DEBUG = 0;
    protected VGJ vgj;
    protected GraphWindow gw;
    protected Graph mygraph;

    abstract public void showGraph(VGJ vgj);
    abstract public void showNewGraph(VGJ vgj);

    abstract String getInitFileName() ;
    abstract String getTitle() ;
    abstract OrgHierVGJBase create(Collection rels) ;

  /**
   *  Show any additional controls required by the specific class of UI.
   *  Subclasses should provide the implementation for this function.
   */
  abstract public void showProviderControl ();

  /**
   *  Report to the caller the name of this view, if any.  By default, there
   *  is no name.  Subclasses that support names will want to override this
   *  default implementation.
   *  @return null, by default.
   */
  public String getName () {
    return null;
  }

    public OrgHierVGJBase(OrgHierModel ohm) {
      init(ohm);
    }

    protected void init(OrgHierModel ohm) {
      this.ohm=ohm;
    }

    public void showRelationshipsAtTime (long time) {
	OrgHierRelationship ohr;

	Collection rels = ohm.getRelationshipsAtTime(time);
	String timeStr = "" + time;
	if (time == Long.MAX_VALUE)
          timeStr="[The end of time]";
        else if (time == Long.MIN_VALUE)
          timeStr="Epoch";

	OrgHierVGJBase newOhvt=create(rels);
	show();
    }

    public void setDrillDownAttributes (
        VGJ vgj, Graph mygraph, String name)
    {
      String drillDownOrgs = RuntimeParameters.getLoudSystemProperty(
        "drillDownOrgs", "");

      StringTokenizer st = new StringTokenizer(drillDownOrgs,";");
      String orgstr;
      while (st.hasMoreTokens()) {
        orgstr = st.nextToken();
        // Don't allow drill-down for the node whose view is being displayed
        if (!orgstr.equals(name)) {
          vgj.drillDownNode(mygraph, orgstr, true);
        }
      }
    }

  protected void createVisuals () {
    vgj = VGJ.create();
    gw = new GraphWindow(true);
    mygraph = new Graph();
  }

  protected void updateGraph () {
    gw.loadFile(getInitFileName(), mygraph);
    vgj.syncWith(mygraph);
    vgj.setGraph(mygraph);
    vgj.setCanvasTitle(getTitle());
    setDrillDownAttributes(vgj, mygraph, getName());
  }

  protected void showVisuals (boolean wantNewWindow) {
    showProviderControl();
    if (wantNewWindow)
      showNewGraph(vgj);
    else
      showGraph(vgj);

    updateSuperiorRelationships();
  }

  /**
   *  Show this org hierarchy view.  Subclasses should implement this as is
   *  consistent with their properties.
   */
  abstract public void show ();

  /**
   *  Show this org hierarchy view, optionally creating a new window in the
   *  process.
   */
  protected void show_initialized (boolean wantNewWindow) {
    createVisuals();
    updateGraph();
    showVisuals(wantNewWindow);
  }

    protected OrgTreeAction ota = new OrgTreeAction() {
	  public void execute () {
	    updateOrgGraph();
	  }

	  public String getId () {
            return "Update";
          }
      };


    void updateSuperiorRelationships() {
	showSuperiorRelationships(vgj, mygraph);
	showLiveClusters(vgj, mygraph);
	vgj.setGraph(mygraph);
	showGraph(vgj);
    }

    void updateRelationships (String relType) {
      showRelationships(vgj, mygraph, relType);
      showLiveClusters(vgj, mygraph);
      vgj.setGraph(mygraph);
      showGraph(vgj);
    }

     public void updateOrgGraph() {
       System.out.println("vgjorgtree ota updating model");
       ohm = OrgHierApp.updateModel();
       updateGraph();
       showVisuals(false);
     }

    public void showSuperiorRelationships(VGJ vgj, Graph mygraph) {
      Set sups=ohm.getSuperiors();

      HashSet hs;
      String sub, sup;
      for (Iterator supit=sups.iterator(); supit.hasNext(); ) {
	  sup = (String)supit.next();
	  hs = ohm.getSubordinates(sup);
	  if (hs != null) {
	      for (Iterator it=hs.iterator(); it.hasNext();) {
		  sub = (String)it.next();
		  if (DEBUG > 30) {
		      System.out.println("getTreeVGJ vgj.addSuperior(mygraph, sup, sub); sup: "+sup
					 +" sub: "+sub);
		  }
		  vgj.addSuperiorLink(mygraph, sup, sub);
	      }
	  }
      }
    }


    public void showRelationships(VGJ vgj, Graph mygraph, String relType) {
      Collection ohrs=ohm.getRelationshipsOfType(relType);

      if (DEBUG > 30) {
        System.out.println("showRelationships of type ["+relType
            +"] Entires: "+ohrs.size());
      }

      vgj.clearLinks();

      OrgHierRelationship ohr;
    	HashSet hs;
    	String sub, sup;
      for (Iterator rit=ohrs.iterator(); rit.hasNext(); ) {
	      ohr = (OrgHierRelationship)rit.next();
        sub=ohr.getId();
        sup=ohr.getOtherId();
    		    if (DEBUG > 30) {
		        	System.out.println("getTreeVGJ vgj.addLink(mygraph, sup, sub); sup: "+sup
                  +" sub: "+sub);
    		    }
    		    vgj.addSuperiorLink(mygraph, sup, sub);
	    }
    }

    public void showLiveClusters(VGJ vgj, Graph mygraph) {

      Collection orgs=ohm.getOrgs();

      String liveOrg;
      for (Iterator supit=orgs.iterator(); supit.hasNext(); ) {
	      liveOrg = (String)supit.next();
	vgj.colorNode(mygraph, liveOrg, Color.green);
	    }


    }


    public TreeSet getTransitionTimes() {
      return ohm.getTransitionTimes();
    }
    public long getStartTime() { return ohm.getStartTime(); }
    public long getEndTime() { return ohm.getEndTime(); }

  protected void showOrgGraph() {
    String selOrg = vgj.getSelectedNodeLabel();
    showOrgGraph(selOrg);
  }

  public static void showOrgGraph(String forOrg) {
    OrgHierVGJCommunityTree.showOrgGraph(forOrg);
  }
}



