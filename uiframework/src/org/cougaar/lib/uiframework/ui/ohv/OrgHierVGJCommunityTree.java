/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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



