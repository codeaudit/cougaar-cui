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

package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.text.*;
import java.util.*;

import org.w3c.dom.*;

import org.cougaar.util.ConfigFinder;
import org.cougaar.lib.planserver.*;

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.DayBaseModel;

/**
 *  This class is a container for a QueryInterpreter designed to answer queries
 *  about the locations (latitude and longitude) of organizations.  It is
 *  intended to run inside an Aggregation Agent Cluster, where relevant data
 *  is available (provided by a LocSchedulePlugin).
 */
public class PSP_LocQuery extends PSP_QueryBase {
  private GenericInterpreter responder = null;

  /**
   *  Provide a reference to the QueryInterpreter to be used by this PSP.  The
   *  abstract algorithm calls upon this method to supply the interpreter when
   *  it is processing a query.
   *  @return the QueryInterpreter implementation
   */
  protected QueryInterpreter getQueryInterpreter () {
    return responder;
  }

  /**
   *  Override this method with code to initialize the QueryInterpreter
   *  instance used by this PSP for answering requests.  In some cases, all of
   *  the initialization can be performed in the implementation class's
   *  constructor.  This method, however, is called after the PluginDelegate
   *  reference becomes available.
   */
  protected void initQueryInterpreter (PlanServiceContext psc) {
    // set up DayBaseModel
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    try {
      DayBaseModel.baseTimeMillis = sdf.parse("2005/08/15").getTime();
    }
    catch (Exception buzz_off) {
      buzz_off.printStackTrace();
    }

    // create the query interpreter
    responder = new GenericInterpreter();

    // add the attributes
    responder.addAttribute(new TPLocAttribute(TPLocAttribute.START_TIME));
    responder.addAttribute(new TPLocAttribute(TPLocAttribute.END_TIME));
    responder.addAttribute(new TPLocAttribute(TPLocAttribute.LATITUDE));
    responder.addAttribute(new TPLocAttribute(TPLocAttribute.LONGITUDE));

    // Add the dimension
    responder.addDimension(createDimension(psc));

    // Debug mode:
    // echo_queries = true;
    // echo_results = true;
  }

  private QueryDimension createDimension (PlanServiceContext psc) {
    TPLocDimension ret = new TPLocDimension();
    ret.setName("OrgLocations");
    ret.setPlugin(psc.getServerPluginSupport().getDirectDelegate());
    return ret;
  }
}
