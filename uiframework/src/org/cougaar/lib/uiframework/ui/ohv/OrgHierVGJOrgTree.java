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
 Graphical Tree viewer for an Organization Hierarchy.
 **/

public class OrgHierVGJOrgTree  
    extends OrgHierVGJBase
    implements OrgHierModelViewer
{

    private String name;
    private RelList relList;
    
    public OrgHierVGJOrgTree(Collection col, String name) { 
	this(new OrgHierModel(col), name);
    }
    public OrgHierVGJOrgTree(OrgHierModel ohm, String name) {super(ohm); 
	this.name=name;
	relList=new RelList(ohm, relListAction);
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
      String showProviderControl=RuntimeParameters
	      .getLoudSystemProperty("showProviderControl",
				      "true");
      System.out.println("showProviderControl: ["+showProviderControl+"]");
      return (showProviderControl!=null 
	      && showProviderControl.trim().equalsIgnoreCase("true"));
    }

    public void showProviderControl() {
	if (wantProviderControl()) {
	    System.out.println("showing provider control.");
	    vgj.addControl(relList);
	}
    }
    
    private boolean wantNewWindow() {
	return true;
    }
    
    String getTitle() { return name+" Community"; }

    public void show_initialized() {
      out.println("Here is the INITIALIZED OrgHierVGJOrgTree: "); 
      String initFileName=getInitFileName();
      String title=getTitle();
      boolean wantNewWindow=wantNewWindow();

      // vgjBase.show_initialized(initFileName, title, wantNewWindow, this);
      show_initialized(wantNewWindow);
      out.println("Finished showing the OrgHierVGJOrgTree.");
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
		    //combo.getSelectedItem();
		    //updateSuperiorRelationships();
		    // get selection from relList
		    // String relType = (String)relList.getSelectedValue();
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



