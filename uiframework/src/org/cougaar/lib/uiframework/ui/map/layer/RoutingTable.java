/*
 * <copyright>
 * Copyright 1997-2000 Defense Advanced Research Projects Agency (DARPA)
 * and Clark Software Engineering (CSE) This software to be used in
 * accordance with the COUGAAR license agreement.  The license agreement
 * and other information on the Cognitive Agent Architecture (COUGAAR)
 * Project can be found at http://www.cougaar.org or email: info@cougaar.org.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.map.layer;

import java.util.*;
import java.io.Serializable;

public class RoutingTable implements Serializable
{
  Vector slats = null;
  Vector slons = null;
  Vector elats = null;
  Vector elons = null;
  Vector types = null;
  String startTime = null;
  String endTime = null;
  String orgName = null;
  
  
  public RoutingTable(Vector slatitude, Vector slongitude, Vector elatitude, Vector elongitude, String org, String sl, String el, Vector t)
  {
  	orgName = org;
  	slats = slatitude;
  	slons = slongitude;
  	elats = elatitude;
  	elons = elongitude;
  	types = t;
  	startTime = sl;
  	endTime = el;
  }
  public String getOrg()
  {
  	return orgName;
  }
  

}
