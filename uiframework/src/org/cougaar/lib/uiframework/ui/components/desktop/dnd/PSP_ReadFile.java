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
import java.io.FileOutputStream;
import java.io.FileInputStream;
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

import org.cougaar.core.cluster.ClusterIdentifier;
import org.cougaar.core.cluster.CollectionSubscription;
import org.cougaar.core.cluster.Subscription;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.asset.TypeIdentificationPG;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.lib.planserver.*;
import org.cougaar.core.util.*;
import org.cougaar.util.*;
import org.cougaar.lib.uiframework.ui.components.desktop.FileNode;
import org.cougaar.lib.uiframework.ui.components.desktop.FileInfo;

import org.cougaar.domain.glm.ldm.*;
import org.cougaar.domain.glm.ldm.*;
import org.cougaar.domain.glm.*;
import org.cougaar.domain.glm.ldm.asset.*;
import org.cougaar.domain.glm.ldm.oplan.*;
import org.cougaar.domain.glm.ldm.plan.*;
import org.cougaar.domain.glm.ldm.policy.*;


public class PSP_ReadFile
  extends PSP_BaseAdapter
  implements PlanServiceProvider, UISubscriber
{
  private String myID;
  public String desiredOrganization;

  public PSP_ReadFile() throws RuntimePSPException {
    super();
  }

  public PSP_ReadFile(String pkg, String id) throws RuntimePSPException {
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

  /** The query data is one of:
    ASSET -- meaning return list of assets
    nomenclature:type id -- return asset matching nomenclature & type id
    UID: -- return asset with matching UID
    */

  private void myExecute( PrintStream out,
		       HttpInput query_parameters,
		       PlanServiceContext psc,
		       PlanServiceUtilities psu) throws Exception 
	{
		System.out.println("%%%% trying to send remote file");
		String command = query_parameters.getBodyAsString();
    command = command.trim();
    System.out.println("POST DATA: " + command);
    
   	//  command equals path node string
  	
  	File remoteFile = new File(command);
  	FileInputStream is = new FileInputStream(remoteFile);
  	
  	
  	int xferLength = 0;
  	int offset = 0;
  	int fileLength = (int) remoteFile.length();
  	//byte[] byteArray = new byte[(int) remoteFile.length()];
	  //is.read(byteArray, offset, len);
	  ObjectOutputStream p = new ObjectOutputStream(out);
	  if(fileLength <= 5000000)  //  xfer in one pass if 10mb or less
	  	{
	  		xferLength = fileLength;
	  		//p = new ObjectOutputStream(out);
	  		byte[] byteArray = new byte[xferLength];
		  	is.read(byteArray);
			  //fos.write(byteArray);
			  Object o = (Object)byteArray;
	    	p.writeObject(o);
        System.out.println("Sent FileNode Object");
        p.close();
	    }
	    else
	    {
  		  xferLength = 5000000;
  		  byte[] byteArray = new byte[xferLength];
  		  int someLength = 0;
  		  byte[] writeArray;
  		  while((someLength = is.read(byteArray)) > 0)
  		  {
  		  	//p = new ObjectOutputStream(out);
  		  	writeArray = new byte[someLength];
  		  	System.arraycopy(byteArray, 0, writeArray, 0, someLength);
	  		 	Object o = (Object)writeArray;
	  		 	p.writeObject(o);
	  		 //p.close();
          System.out.println("Sent FileNode Object " + writeArray.length);
        }
        System.out.println("Finished sending byte array ");
        p.close();
			  
  		}
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

 









