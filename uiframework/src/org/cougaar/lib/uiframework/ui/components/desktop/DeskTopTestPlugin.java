/*
 * <copyright>
 * Copyright 1997-2000 Defense Advanced Research Projects Agency (DARPA)
 * and Clark Software Engineering (CSE) This software to be used in
 * accordance with the COUGAAR license agreement.  The license agreement
 * and other information on the Cognitive Agent Architecture (COUGAAR)
 * Project can be found at http://www.cougaar.org or email: info@cougaar.org.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components.desktop;

import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.asset.AggregateAsset;
import org.cougaar.planning.ldm.asset.AssetGroup;
import org.cougaar.planning.ldm.asset.NewItemIdentificationPG;
//import org.cougaar.glm.ldm.asset.LocationSchedulePG;
import org.cougaar.glm.ldm.asset.MilitaryOrgPG;
import org.cougaar.glm.ldm.plan.GeolocLocation;

import org.cougaar.glm.ldm.asset.Organization;
//import org.cougaar.glm.plugins.TaskUtils;

import org.cougaar.planning.ldm.trigger.*;
import org.cougaar.planning.plugin.legacy.PluginDelegate;

import org.cougaar.util.UnaryPredicate;




/***********************************************************************************************************************
<b>Description</b>: Allocate transport tasks to roles".

@author Frank Cooley, &copy;2000 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/

public class DeskTopTestPlugin extends org.cougaar.planning.plugin.legacy.SimplePlugin
{
	
	
  
// ---------------------------------------------------------------------------------------------------------------------
// Public Member Methods
// ---------------------------------------------------------------------------------------------------------------------

	/*********************************************************************************************************************
  <b>Description</b>: Subscribe to "pack the books" tasks and any changes in the inventory.

	*********************************************************************************************************************/
  public void setupSubscriptions()
  {
    
  }


	  

	/*********************************************************************************************************************
  <b>Description</b>: Called by infrastructure whenever something we are interested in is changed or added.

	*********************************************************************************************************************/
  public void execute()
  {
  	

  }
 
  /*********************************************************************************************************************
  <b>Description</b>: Looks at the Plugin parameters for the debug value.
	*********************************************************************************************************************/
  private void parseParameters()
  {
  	// Look through the Plugin parameters for the packer time
  	//System.out.println("&&&& parsing");
    //Vector pVec = getParameters();
    //if (pVec.size() > 0)
    //{
    	
    	//System.out.println("setting debug to" + locationDebug);
    //}
  }
}
