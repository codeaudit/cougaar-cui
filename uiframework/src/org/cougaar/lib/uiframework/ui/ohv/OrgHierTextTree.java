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

/**
 *  Textual Tree viewer for an Organization Hierarchy.
 */
public class OrgHierTextTree implements OrgHierModelViewer {
    private OrgHierModel ohm;
    private String textTree;
    private String delim="\n";
    private Stack branchStack = new Stack();
    private PrintStream out=System.out;

    public OrgHierTextTree(Collection col) { init(new OrgHierModel(col));}
    public OrgHierTextTree(OrgHierModel ohm) {init(ohm);}
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
      OrgHierTextTree ohtt=new OrgHierTextTree(rels);
      ohtt.show();
    }

    public void show() {
      out.println("Here is the OrgHierTextTree: ");
      out.println(textTree);
      out.println("Finished showing the OrgHierTextTree.");
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
	      if (! str.equals(rootId)) { System.err.println("branchStack.pop()!=root"); }
	    }
	    return rcString;
    }

    public TreeSet getTransitionTimes() {
      return ohm.getTransitionTimes();
    }
    public long getStartTime() { return ohm.getStartTime(); }
    public long getEndTime() { return ohm.getEndTime(); }
}
