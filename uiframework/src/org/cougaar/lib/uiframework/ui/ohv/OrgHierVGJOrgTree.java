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
 Graphical Tree viewer for an Organization Hierarchy.
 **/

public class OrgHierVGJOrgTree  implements OrgHierModelViewer {
    static private OrgHierModel ohm; 
    private String textTree; 
    private String delim="\n"; 
    private Stack branchStack = new Stack(); 
    private PrintStream out=System.out; 
    private int DEBUG=40; 
    private VGJ vgj;
    private GraphWindow gw;
    private Graph mygraph;
    private String name;
    
    public OrgHierVGJOrgTree(Collection col, String name) { 
	init(new OrgHierModel(col), name);
    }
    public OrgHierVGJOrgTree(OrgHierModel ohm, String name) {init(ohm, name);}
    private void init(OrgHierModel ohm, String name) {
	String rootId;
	this.ohm=ohm;
	this.name=name;
	Set roots=ohm.generateRoots(); 
	textTree="Top Level"+delim;
	for (Iterator riter=roots.iterator(); riter.hasNext(); ) { 
	    rootId=(String)riter.next(); 
	    textTree+=getBranchDFS(rootId, 1); 
	} 
    } 
    
    public void showRelationshipsAtTime(long time) { 
	OrgHierRelationship ohr; 
	//out.println("Here are the relationships at time: "
	//               +model.getRelationshipsAtTime(time));
	
	Collection rels=ohm.getRelationshipsAtTime(time);
	String timeStr=""+time;
	if (time==Long.MAX_VALUE) { timeStr="[The end of time]";
	} else if	 (time==Long.MIN_VALUE) { timeStr="Epoch";
	}
	out.println("Relationships at time "+timeStr+": ");
	OrgHierVGJOrgTree ohtt=new OrgHierVGJOrgTree(rels, name); 
	ohtt.show(); 
    } 
    

    public static void setDrillDownAttributes(VGJ vgj, Graph mygraph) {
      String drillDownOrgs=RuntimeParameters
              .getLoudSystemProperty("drillDownOrgs",
                                      "CENTCOM-HHC;3ID-HHC");

      StringTokenizer st = new StringTokenizer(drillDownOrgs,";");
      String orgstr;
      while (st.hasMoreTokens()) {
	  orgstr=st.nextToken();
	  System.out.println("org setddattr ddOrg: ["+orgstr+"]");
	  vgj.drillDownNode(mygraph, orgstr, true);
      }
    }

    public void show_dynamic() { 
	out.println("Here is the DYNAMIC OrgHierVGJOrgTree: "); 
	out.println(textTree); 
	
	String rootString="Command Hierarchy"; 
	VGJ vgj=VGJ.create();
	Graph mygraph=new Graph(true);

	int noneID = vgj.getNodeID(mygraph, rootString);
	System.out.println("noneID is "+noneID);

	Node aNode = mygraph.getNodeFromId(noneID);
	System.out.println("getId "+aNode.getId());
	System.out.println("getLabel "+aNode.getLabel());
	System.out.println("getSelected "+aNode.getSelected());

      showSuperiorRelationships(vgj, mygraph);

	    //keys=roots();
	    //while (keys.hasMoreElements()) {
      //  sub = (String)keys.nextElement();
      Set roots=ohm.generateRoots();
      String sub;
      for (Iterator riter=roots.iterator(); riter.hasNext(); ) { 
	sub=(String)riter.next(); 
	      if (DEBUG > 30) {
		  System.out.println("getTreeVGJ vgj.addSuperior(mygraph, sup, sub); sup: "+rootString+" sub: "+sub);
	    }
	      vgj.addSuperior(mygraph, rootString, sub);
	    }

      aNode.setSelected(true);
      System.out.println("getId "+aNode.getId());
      System.out.println("getLabel "+aNode.getLabel());
      System.out.println("getSelected "+aNode.getSelected());
      
      vgj.setGraph(mygraph);
      vgj.showGraph();
      System.out.println("Leaving vgj tree.");
      
      out.println("Finished showing the OrgHierVGJOrgTree."); 

    } 
 
    public void show_initialized() {
      out.println("Here is the INITIALIZED OrgHierVGJOrgTree: "); 
      out.println(textTree); 
 
      int idx=0; 
 
      String loadClusterInitPath=RuntimeParameters
	      .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJOrgTree.LoadClusterInitPath",
				      "C:\\data\\default\\ohv\\init");
      String loadClusterInitSuffix=RuntimeParameters
	      .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJOrgTree.LoadClusterInitSuffix",
				      "_init.gml");
      String fileSeparator=System.getProperty("file.separator"); // "\\";
      String init_filename=loadClusterInitPath+fileSeparator+name+loadClusterInitSuffix;
      System.out.println("initFilename is ["+init_filename+"]");
      vgj=VGJ.create();
      gw=new GraphWindow(true);

      mygraph=new Graph();
      gw.loadFile(init_filename, mygraph);
      // out.println(idx++);
 
      vgj.syncWith(mygraph);

      vgj.setGraph(mygraph);
      vgj.setCanvasTitle(name+" Community");

      setDrillDownAttributes(vgj, mygraph);
// 	    vgj.drillDownNode(mygraph, "CENTCOM-HHC", true);
// 	    vgj.drillDownNode(mygraph, "DLAHQ", true);
// 	    vgj.drillDownNode(mygraph, "3ID-HHC", true);

      // out.println(idx++);
      showNewGraph(vgj);
      updateSuperiorRelationships();
      //out.println(idx++);
      System.out.println("Leaving vgj tree.");

      // end from supkpr
      out.println("Finished showing the OrgHierVGJOrgTree.");
    }


    private   OrgTreeAction ota = new OrgTreeAction() {
	  public void execute()
	  {
	    System.out.println("in  OrgTreeAction.execute -- update org graph");
	      updateOrgGraph();
	    System.out.println("out OrgTreeAction.execute");
	  }
	  public String getId() { return "Update"; }
      };
    private void showGraph(VGJ vgj) {
	    vgj.showGraph(false, false, false, ota, false);
    }
    private void showNewGraph(VGJ vgj) {
	    vgj.showGraph(false, false, false, ota, true);
    }
      void updateSuperiorRelationships() {
      int idx=100;
	showSuperiorRelationships(vgj, mygraph);
	out.println(idx++);
	showLiveClusters(vgj, mygraph);

	      vgj.setGraph(mygraph);
	out.println(idx++);
	      showGraph(vgj);
	out.println(idx++);

	out.println("Finished updateSuperiorRelationships.");

      }
    public void show() { 
      //show_dynamic(); 
     show_initialized(); 
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


    public void showLiveClusters(VGJ vgj, Graph mygraph) {

      Collection orgs=ohm.getOrgs();

      String liveOrg;
      for (Iterator supit=orgs.iterator(); supit.hasNext(); ) {
	      liveOrg = (String)supit.next();
	vgj.colorNode(mygraph, liveOrg, Color.green);
	    }


    }


    private String indent(int depth)
    {
	    final String str="--------------------------------------------------"+
	      "----------------------------------------------------------------"+
	      "----------------------------------------------------------------";
	    return str.substring(0,depth*2);
    }

    private String getBranchDFS(String rootId, int depth) {
      String rcString=indent(depth)+rootId+delim;
	    HashSet kids;
	    String sub;

      // System.out.println("in getBranchDFS("+rootId+","+depth+") rcS: "+rcString);
	    if (branchStack.contains(rootId)) {
	      rcString=indent(depth)+rootId+" [stopping here due to cycle] "+delim;
	      System.out.println("Number of BranchStack elements: "+branchStack.size()
			       +" branchStr: "+rcString);
	  } else {
	    branchStack.push(rootId);

	    kids = ohm.getSubordinates(rootId);
	if (kids!=null) {
	  for (Iterator kiter=kids.iterator(); kiter.hasNext(); ) {
	    sub=(String)kiter.next();
		  rcString += getBranchDFS(sub,depth+1);
	  }
	}

	      String str = (String)branchStack.pop();
	      if (! str.equals(rootId)) { System.err.println("branchStack.pop()!=root"); }
	    }

      // System.out.println("out getBranchDFS("+rootId+","+depth+") rcS: "+rcString);

	    return rcString;
    } 
 
    public TreeSet getTransitionTimes() {
      return ohm.getTransitionTimes();
    }
    public long getStartTime() { return ohm.getStartTime(); }
    public long getEndTime() { return ohm.getEndTime(); }

    private void updateOrgGraph() {
	System.out.println("vgjorgtree ota updating model");
	ohm = OrgHierApp.updateModel();
	showOrgGraph(name);
    }
    private void showOrgGraph() {
      // selOrg = getSelectedOrg();
      String selOrg = vgj.getSelectedNodeLabel();
      showOrgGraph(selOrg);
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
	  System.out.println("vgjorgtree ota updating model");
	  //	  OrgHierModel ohm = OrgHierApp.updateModel();
        OrgHierVGJOrgTree ohvt=new OrgHierVGJOrgTree(ohm, selOrg);
        ohvt.show();
      }
    }
 
  } 



