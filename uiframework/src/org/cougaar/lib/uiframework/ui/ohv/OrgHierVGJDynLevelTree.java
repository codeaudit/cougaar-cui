/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
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

import java.util.ArrayList;
import java.util.List;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.VGJ;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;

/**
 * Graphical Tree viewer for an Organization Hierarchy.
 */
public class OrgHierVGJDynLevelTree  implements OrgHierModelViewer {
    private OrgHierModel ohm; 
    private String textTree; 
    private String delim="\n"; 
    private Stack branchStack = new Stack();
    private PrintStream out=System.out; 
    private int DEBUG = 0; 
 
    public OrgHierVGJDynLevelTree(Collection col) { init(new OrgHierModel(col));} 
    public OrgHierVGJDynLevelTree(OrgHierModel ohm) {init(ohm);} 
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
 
      Collection rels=ohm.getRelationshipsAtTime(time);
      String timeStr=""+time;
      if (time==Long.MAX_VALUE) { timeStr="[The end of time]";
      } else if  (time==Long.MIN_VALUE) { timeStr="Epoch";
      }
      out.println("Relationships at time "+timeStr+": ");
      OrgHierVGJDynLevelTree ohtt=new OrgHierVGJDynLevelTree(rels); 
      ohtt.show(); 
    }
 
    public void show_dynamic() { 
      out.println("Here is the DYNAMIC OrgHierVGJDynLevelTree: "); 
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

	System.out.println("ui.ohv.vgjorgtree.showbelow is "
			   +System.getProperty("ui.ohv.vgjorgtree.showbelow"));
	String below=System.getProperty("ui.ohv.vgjorgtree.showbelow");
	show(vgj, mygraph, below);

	// show(vgj, mygraph);

	    //keys=roots();
	    //while (keys.hasMoreElements()) {
      //  sub = (String)keys.nextElement();
	if (below ==null) {
	    System.out.println("below is == null");
	    Set roots=ohm.generateRoots();
	    String sub;
	    for (Iterator riter=roots.iterator(); riter.hasNext(); ) { 
		sub=(String)riter.next(); 
		if (DEBUG > 30) {
		    System.out.println("getTreeVGJ vgj.addSuperior(mygraph, sup, sub); sup: "+rootString+" sub: "+sub);
		}
		vgj.addSuperior(mygraph, rootString, sub);
	    }

	    System.out.println("anode getId "+aNode.getId());
	    System.out.println("getLabel "+aNode.getLabel());
	    System.out.println("getSelected "+aNode.getSelected());

	    
	} else {
		vgj.addSuperior(mygraph, rootString, below);

	    int belowID = vgj.getNodeID(mygraph, below);
	    System.out.println("belowID is "+belowID);

	    aNode = mygraph.getNodeFromId(belowID);
	    System.out.println("below is anode getId "+aNode.getId());
	    System.out.println("getLabel "+aNode.getLabel());
	    System.out.println("getSelected "+aNode.getSelected());
	}

	aNode.setSelected(true);
	    System.out.println("getId "+aNode.getId());
	    System.out.println("getLabel "+aNode.getLabel());
	    System.out.println("getSelected "+aNode.getSelected());

	    vgj.setGraph(mygraph);
	    vgj.showGraph(true, true, false, null);
	    System.out.println("Leaving vgj tree.");
 
      // end from supkpr 
      out.println("Finished showing the OrgHierVGJDynLevelTree."); 

    } 
 
 
    public void show() { 
      show_dynamic(); 
    } 
    public void show(VGJ vgj, Graph mygraph) {
	System.out.println("ui.ohv.vgjorgtree.showbelow is "
			   +System.getProperty("ui.ohv.vgjorgtree.showbelow"));
	String below=System.getProperty("ui.ohv.vgjorgtree.showbelow");
	show(vgj, mygraph, below);
    }
    public void show(VGJ vgj, Graph mygraph, String below) { 
 

    	//Enumeration keys=keys();
      Set sups;
      if (below!=null) {
	  sups=ohm.getSubordinates(below);
      } else {
	  sups=ohm.getSuperiors();
      }

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
		      // to get all, I think yoyu just need to say: 
		      //    if (below!=null) { show(vgj, mygraph, sub) }
		      vgj.addSuperior(mygraph, sup, sub);
      		}
	      }
	      if (below!=null) {
		  vgj.addSuperior(mygraph, below, sup);
		  
	      }
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
      if (! str.equals(rootId))
        System.err.println("branchStack.pop()!=root");
    }
    return rcString;
  }

    public TreeSet getTransitionTimes() {
      return ohm.getTransitionTimes();
    }
    public long getStartTime() { return ohm.getStartTime(); }
    public long getEndTime() { return ohm.getEndTime(); }
}

