
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
    ConfigFinder cf = psc.getServerPlugInSupport().getDirectDelegate().
      getCluster().getConfigFinder();
    responder = new GenericInterpreter();
    responder.addAttribute(new LocAttribute(this, LocAttribute.LATITUDE));
    responder.addAttribute(new LocAttribute(this, LocAttribute.LONGITUDE));
    responder.addDimension(createOrgDimension(cf));
    responder.addDimension(createTimeDimension());

    // Debug mode:
    // echo_queries = true;
    // echo_results = true;
  }

  private QueryDimension createOrgDimension (ConfigFinder cf) {
    OrderlessDimension dim = new OrderlessDimension();
    dim.setName("Org");
    ListDimNode root = new ListDimNode("All Orgs");
    dim.setRoot(root);

    try {
      Document doc = cf.parseXMLConfigFile("OrgList.xml");
      Element elt = doc.getDocumentElement();
      NodeList nl = elt.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        Node child = nl.item(i);
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          String org = ((Element) child).getAttribute("name");
          if (org != null)
            root.addMember(org);
        }
      }
    }
    catch (Exception oh_no) {
      System.out.println(
        "PSP_LocQuery::createOrgDimension:  no OrgList.xml--use default orgs");
      root.addMembers(new String[] {"3ID", "1BDE", "3-69-ARBN", "3-FSB"});
    }

    return dim;
  }

  private QueryDimension createTimeDimension () {
    IntegerDimension dim = new IntegerDimension();
    dim.setName("Time");
    dim.setRoot(new IntDimNode(-100, 1000));
    return dim;
  }
}
