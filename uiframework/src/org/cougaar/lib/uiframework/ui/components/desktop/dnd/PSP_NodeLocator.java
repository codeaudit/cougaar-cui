/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * </copyright>
 */
 
package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;


import java.io.Serializable;
import java.io.File;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.net.URLConnection;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.lang.reflect.Method;
import java.util.*;

import org.cougaar.core.mts.MessageAddress;
import org.cougaar.core.blackboard.CollectionSubscription;
import org.cougaar.core.blackboard.Subscription;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.asset.TypeIdentificationPG;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.lib.planserver.*;
import org.cougaar.core.util.*;
import org.cougaar.util.*;
import org.cougaar.lib.uiframework.ui.components.desktop.FileNode;

import org.cougaar.glm.ldm.*;
import org.cougaar.glm.ldm.*;
import org.cougaar.glm.*;
import org.cougaar.glm.ldm.asset.*;
import org.cougaar.glm.ldm.oplan.*;
import org.cougaar.glm.ldm.plan.*;
import org.cougaar.glm.ldm.policy.*;


public class PSP_NodeLocator
  extends PSP_BaseAdapter
  implements PlanServiceProvider, UISubscriber
{
  private String myID;
  public String desiredOrganization;

  public PSP_NodeLocator() throws RuntimePSPException {
    super();
  }

  public PSP_NodeLocator(String pkg, String id) throws RuntimePSPException {
    setResourceLocation(pkg, id);
  }

  /* This PSP is referenced directly (in the URL from the client)
     and hence this shouldn't be called.
     */

  public boolean test(HttpInput query_parameters, PlanServiceContext psc) {
    super.initializeTest();
    return false; 
  }

  

  /*
    Called when a request is received from a client.
    Either gets the command ASSET to return the names of all the assets
    that contain a ScheduledContentPG or
    gets the name of the asset to plot from the client request.
  */

  public void execute( PrintStream out,
		       HttpInput query_parameters,
		       PlanServiceContext psc,
		       PlanServiceUtilities psu) throws Exception {
    try {
      myExecute(out, query_parameters, psc, psu);
    } catch (Exception e) {
      e.printStackTrace();
    };
  }

  private void myExecute( PrintStream out,
		       HttpInput query_parameters,
		       PlanServiceContext psc,
		       PlanServiceUtilities psu) throws Exception 
	{
		System.out.println("%%%% trying to send");
		String command = query_parameters.getBodyAsString();
    command = command.trim();
    System.out.println("POST DATA: " + command);
    FileNode rootNode = new FileNode();
    
    
		if(command.equals("sendHelp"))
		{
			File[] childArray = File.listRoots();
	    for(int i = 1; i < childArray.length; i++)
	    {
	    	FileNode nextNode = createTreeModel(childArray[i].getAbsolutePath());
	    	rootNode.add(nextNode);
	    	System.out.println("%%%% node " + childArray[i].getAbsolutePath());
	    }
			
	  	Object o = (Object)rootNode;
		 	ObjectOutputStream p = new ObjectOutputStream(out);
	    p.writeObject(o);
	    System.out.println("Sent FileNode Object");
    }
    else
    {
    	//  command equals path node string
    	
    	FileNode pathNode = createTreeModel(command);
    	Object o = (Object)pathNode;
		 	ObjectOutputStream p = new ObjectOutputStream(out);
	    p.writeObject(o);
	    System.out.println("Sent FileNode Object -> " + command);
    	
    }
	 
  }
  
  private FileNode createTreeModel(String rootPath)
  {
  	File root = new File(rootPath);
  	System.out.println("%%%% file " + root + " length " + root.length());
  	FileNode rootNode = new FileNode(root);
  	System.out.println("%%%% locator node -> " + rootNode);
  	rootNode.explore();
  	System.out.println("%%%% isdirectory " + rootNode.isDirectory());
  	 	
  	return rootNode;
  	
  }
  
  public boolean returnsXML() {
    return true;
  }

  public boolean returnsHTML() {
    return false;
  }

  public String getDTD() {
    return "myDTD";
  }

  /* The UISubscriber interface.
     This PSP doesn't care if subscriptions change
     because it treats each request as a new request.
  */

  public void subscriptionChanged(Subscription subscription) 
  {
  }

  
  

}

 









