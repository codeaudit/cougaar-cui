package org.cougaar.lib.uiframework.ui.ohv;

import org.cougaar.lib.uiframework.ui.ohv.util.*;

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

/*
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
*/

/*
import ui.SupKeeper;
import ui.TreeBuilder;
*/
//import dom.DOMParserWrapper;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.*;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.VGJ;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;




 /**
 Graphical Tree viewer for an Organization Hierarchy.
 **/

  public class OrgHierVGJCommunityTree  implements OrgHierModelViewer {
    private OrgHierModel ohm; 
    private String textTree; 
    private String delim="\n"; 
    private Stack branchStack = new Stack(); 
    private PrintStream out=System.out; 
    private int DEBUG=40; 
 	    VGJ vgj;
      GraphWindow gw;
	    Graph mygraph;

    public OrgHierVGJCommunityTree(Collection col) { init(new OrgHierModel(col));}
    public OrgHierVGJCommunityTree(OrgHierModel ohm) {init(ohm);}
    private void init(OrgHierModel ohm) {
      String rootId; 
      this.ohm=ohm; 
      Set roots=ohm.generateRoots(); 
      textTree="Top Level"+delim; 
      for (Iterator riter=roots.iterator(); riter.hasNext(); ) { 
        rootId=(String)riter.next(); 
        textTree+=getBranchDFS(rootId, 1); 
      } 
    } 
 
    public void showRelationshipsAtTime(long time) { 
        OrgHierRelationship ohr; 
        //out.println("Here are the relationships at time: "+model.getRelationshipsAtTime(time));

      Collection rels=ohm.getRelationshipsAtTime(time);
      String timeStr=""+time;
      if (time==Long.MAX_VALUE) { timeStr="[The end of time]";
      } else if  (time==Long.MIN_VALUE) { timeStr="Epoch";
      }
      out.println("Relationships at time "+timeStr+": ");
      OrgHierVGJCommunityTree ohtt=new OrgHierVGJCommunityTree(rels); 
      ohtt.show(); 
    } 
 
    private void show_dynamic() {
      out.println("Here is the DYNAMIC OrgHierVGJCommunityTree: "); 
      out.println(textTree); 
 
      // from supkpr 
	    String rootString="Command Hierarchy"; 
	    VGJ vgj=VGJ.create();
	    Graph mygraph=new Graph(true);

    	//	int noneID = vgj.getNodeID(mygraph, "NONE");
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
 
      // end from supkpr 
      out.println("Finished showing the OrgHierVGJCommunityTree."); 

    } 


    public static void setDrillDownAttributes(VGJ vgj, Graph mygraph) {
	OrgHierVGJOrgTree.setDrillDownAttributes(vgj,  mygraph);
    }

 
    private void show_initialized() {
      out.println("Here is the INITIALIZED OrgHierVGJCommunityTree: "); 
      out.println(textTree); 
 
      int idx=0; 
 
      //String init_filename="C:\\dev\\ui\\kr\\sfp\\stg\\data\\dbjadsup_init.gml";
      String loadClusterInitPath=RuntimeParameters
              .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitPath",
                                      "C:\\data\\default\\ohv\\init");
      String loadClusterInitName=RuntimeParameters
              .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitName",
                                      "\\dbj");
      String loadClusterInitSuffix=RuntimeParameters
              .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitSuffix",
                                      "_init.gml");
//       String drillDownOrgs=RuntimeParameters
//               .getLoudSystemProperty("drillDownOrgs",
//                                       "CENTCOM-HHC;3ID-HHC");
      
 
      // String init_filename="C:\\dev\\ui\\kr\\sfp\\stg\\data\\"+selOrg+"_init.gml";
      String init_filename=loadClusterInitPath+loadClusterInitName+loadClusterInitSuffix;
      // from supkpr
      vgj=VGJ.create();
      gw=new GraphWindow(true);

      mygraph=new Graph();
      gw.loadFile(init_filename, mygraph);
      out.println(idx++);
	    //Graph mygraph=gw.getGraphCopy();
      out.println(idx++);

      vgj.syncWith(mygraph);

      // showSuperiorRelationships(vgj, mygraph);
      // out.println(idx++);

	    vgj.setGraph(mygraph);
      vgj.setCanvasTitle("Overview");
      out.println(idx++);

      System.out.println("community show_init numberofnodes: "+mygraph.numberOfNodes());

      setDrillDownAttributes(vgj, mygraph);
//       StringTokenizer st = new StringTokenizer(drillDownOrgs,";");
//       String orgstr;
//       while (st.hasMoreTokens()) {
// 	  orgstr=st.nextToken();
// 	  System.out.println("community show_init ddOrg: ["+orgstr+"]");
// 	  vgj.drillDownNode(mygraph, orgstr, true);
//       }

// 	    vgj.drillDownNode(mygraph, "CENTCOM-HHC", true);
// 	    vgj.drillDownNode(mygraph, "DLAHQ", true);
// 	    vgj.drillDownNode(mygraph, "3ID-HHC", true);
//       System.out.println("afdd community show_init numberofnodes: "+mygraph.numberOfNodes());
	    showLiveClusters(vgj, mygraph);
      System.out.println("af slc community show_init numberofnodes: "+mygraph.numberOfNodes());



      showGraph(vgj);
      out.println(idx++);
 	    System.out.println("Leaving vgj tree.");

      // end from supkpr
      out.println("Finished showing the OrgHierVGJCommunityTree.");
    }

    private   OrgTreeAction ota = new OrgTreeAction() {
          public void execute()
          {
            System.out.println("in community OrgTreeAction.execute -- show org graph");
            showOrgGraph();
            System.out.println("out OrgTreeAction.execute");
          }
          public String getId() { return "Show Community"; }
      };
    private void showOrgGraph() {
      // selOrg = getSelectedOrg();
      String selOrg = vgj.getSelectedNodeLabel();
            System.out.println("selected node: "+selOrg);
      if (selOrg==null) {
        System.err.println("==========================");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("Select an Organization !!!");
        System.err.println("==========================");
      } else {
         //loadInit(selOrg);
      // create and show vgjOrgTree (use my model)
	  System.out.println("vgjcommunitytree ota updating model");
	  OrgHierModel ohm = OrgHierApp.updateModel();
        OrgHierVGJOrgTree ohvt=new OrgHierVGJOrgTree(ohm, selOrg);
        ohvt.show();
      }
    }
    private void loadInit(String selOrg) {
      String loadClusterInitPath=RuntimeParameters
              .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitPath",
                                      "C:\\data\\default\\ohv\\init");
//                                      "C:\\dev\\ui\\kr\\sfp\\stg\\data\\");
      String loadClusterInitSuffix=RuntimeParameters
              .getLoudSystemProperty("org.cougaar.lib.uiframework.ui.ohv.OrgHierVGJCommunityTree.LoadClusterInitSuffix",
                                      "_init.gml");
      // String init_filename="C:\\dev\\ui\\kr\\sfp\\stg\\data\\"+selOrg+"_init.gml";
      String init_filename=loadClusterInitPath+selOrg+loadClusterInitSuffix;
      vgj=VGJ.create();
      gw=new GraphWindow(true);

      mygraph=new Graph();
      gw.loadFile(init_filename, mygraph);
      //out.println(idx++);
	    //Graph mygraph=gw.getGraphCopy();
      //out.println(idx++);

      vgj.syncWith(mygraph);

      // showSuperiorRelationships(vgj, mygraph);
      // out.println(idx++);

	    vgj.setGraph(mygraph);

      //out.println(idx++);
      showGraph(vgj);
      //out.println(idx++);
 	    System.out.println("Leaving vgj loadInit tree.");

      // end from supkpr
      out.println("Finished showing the OrgHierVGJCommunityTree loadInit.");
     }

    private void showGraph(VGJ vgj) {
	    vgj.showGraph(false, false, true, ota, false);
    }
    private void showNewGraph(VGJ vgj) {
	    vgj.showGraph(false, false, true, ota, true);
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

      // end from supkpr
        out.println("Finished updateSuperiorRelationships.");

      }
    public void show() { 
      //show_dynamic(); 
     show_initialized(); 
    } 
    public void showSuperiorRelationships(VGJ vgj, Graph mygraph) {

      /*
      Set roots=ohm.generateRoots();
      for (Iterator riter=roots.iterator(); riter.hasNext(); ) { 
        rootId=(String)riter.next();
        textTree+=getBranchDFS(rootId, 1); 
      }
      */ 
 
    	//Enumeration keys=keys();
      Set sups=ohm.getSuperiors();

    	HashSet hs;
    	String sub, sup;
      for (Iterator supit=sups.iterator(); supit.hasNext(); ) {
//    	while (keys.hasMoreElements()) {
//	      sup = (String)keys.nextElement();
	      sup = (String)supit.next();
  	    hs = ohm.getSubordinates(sup);
  	    if (hs != null) {
      		for (Iterator it=hs.iterator(); it.hasNext();) {
    		    sub = (String)it.next();
    		    if (DEBUG > 30) {
		        	System.out.println("getTreeVGJ vgj.addSuperior(mygraph, sup, sub); sup: "+sup
                  +" sub: "+sub);
    		    }
    		    vgj.addSuperior(mygraph, sup, sub);
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
 
  } 



