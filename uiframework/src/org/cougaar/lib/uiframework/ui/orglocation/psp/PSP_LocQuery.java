
package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.util.*;

import org.w3c.dom.*;

import org.cougaar.util.ConfigFinder;
import org.cougaar.lib.planserver.*;

import org.cougaar.lib.uiframework.query.*;
import org.cougaar.lib.uiframework.query.generic.*;

/**
 *  This class is a container for a QueryInterpreter designed to answer queries
 *  about the locations (latitude and longitude) of organizations.  It is
 *  intended to run inside an Aggregation Agent Cluster, where relevant data
 *  is available (provided by a LocSchedulePlugIn).  The organizations
 *  recognized by the resident QueryInterpreter are supplied in a configuration
 *  file called "OrgList.xml", which should be formatted like so:
 *  <pre>
 *    &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *    &lt;org name="All Orgs"&gt;
 *      &lt;org name="First Org"/&gt;
 *      &lt;org name="Second Org"/&gt;
 *      &lt;org name="Third Org"/&gt;
 *      ...
 *    &lt;/org&gt;
 *  </pre>
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
   *  constructor.  This method, however, is called after the PlugInDelegate
   *  reference becomes available.
   */
  protected void initQueryInterpreter (PlanServiceContext psc) {
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
    ret.setPlugIn(psc.getServerPlugInSupport().getDirectDelegate());
    return ret;
  }
}
