
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.util.*;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.planserver.HttpInput;
import org.cougaar.lib.planserver.PSP_BaseAdapter;
import org.cougaar.lib.planserver.PlanServiceContext;
import org.cougaar.lib.planserver.PlanServiceProvider;
import org.cougaar.lib.planserver.PlanServiceUtilities;

/**
 *  The PSP_OrgSub class is a class of PSPs for the Aggregation Agent that
 *  report on relationships among the Organizations in a society.  The
 *  information required for it to do its job is provided by the OrgSubPlugIn,
 *  which must also reside on the AggAgent.  The output produced by this class
 *  conforms to the DTD
 *  <pre>
 *    &lt;!ELEMENT orgrels (Cluster*)&gt;
 *    &lt;!ELEMENT Cluster (other, relationship, starttime?, endtime?)&gt;
 *    &lt;!ATTLIST Cluster ID CDATA #REQUIRED&gt;
 *    &lt;!ELEMENT other (#PCDATA)&gt;
 *    &lt;!ELEMENT relationship (#PCDATA)&gt;
 *    &lt;!ELEMENT starttime (#PCDATA)&gt;
 *    &lt;!ELEMENT endtime (#PCDATA)&gt;
 *  </pre>
 *  which is exemplified by the following:
 *  <pre>
 *    &lt;orgrels&gt;
 *      &lt;Cluster ID="1BDE-3ID"&gt;
 *        &lt;other&gt;3-69-ARBN&lt;/other&gt;
 *        &lt;relationship&gt;ADMINISTRATIVESUBORDINATE&lt;/relationship&gt;
 *        &lt;starttime&gt;3975379853&lt;/starttime&gt;
 *        &lt;endtime&gt;3998787991&lt;/endtime&gt;
 *      &lt;/Cluster&gt;
 *      ...
 *    &lt;/orgrels&gt;
 *  </pre>
 */
public class PSP_OrgSub extends PSP_BaseAdapter implements PlanServiceProvider {
  // when no data is available, punt and return this
  private static String NO_DATA_AVAILABLE =
    "<orgrels><!-- NO DATA TABLE FOUND --></orgrels>";

  // predicate for finding the table of relational data
  private UnaryPredicate tableSeeker = new TableSeeker();

  /**
   *  Process a request from the client
   */
  public void execute (PrintStream out, HttpInput in, PlanServiceContext psc,
      PlanServiceUtilities psu)
      throws Exception
  {
    // Snag the table of relational data.  If it doesn't exist, punt.
    Hashtable table = findTable(psc);
    if (table == null) {
      out.println(NO_DATA_AVAILABLE);
      return;
    }

    // Generate the header and open the root-level tag
    out.println(Const.XML_HEADER);
    Const.addOpenTag(out, Const.ORG_RELS);
    out.println();

    // Generate the content
    // For the nonce, we'll do this without any pretense or finesse:
    // if the relationship exists for any period of time, report it.
    Vector snippets = new Vector();
    for (Enumeration i = table.elements(); i.hasMoreElements(); ) {
      TPRelations rels = (TPRelations) i.nextElement();
      String org = rels.getOrgName();
      Enumeration j = rels.getRelationMaps();
      if (i.hasMoreElements()) {
        while (j.hasMoreElements()) {
          RelationTimeMap rtm = (RelationTimeMap) j.nextElement();
          String type = rtm.getType();
          for (Iterator k = rtm.getAllRelatives(); k.hasNext(); )
            sendClusterTag(out, org, k.next().toString(), type);
        }
      }
      else {
        sendClusterTag(out, org, null, null);
      }
    }

    // Close the root-level tag and flush the buffer
    Const.addCloseTag(out, Const.ORG_RELS);
    out.println();
    out.flush();
  }

  // Generate the XML for a single relationship between two Organizations
  private void sendClusterTag (
      PrintStream out, String org, String other, String type)
  {
    Const.addOpenTag(out, Const.CLUSTER, Const.ID_ATTRIBUTE, org);
    if (other != null) {
      Const.addTag(out, Const.RELATIVE, other);
      Const.addTag(out, Const.REL_TYPE, type);
    }
    Const.addCloseTag(out, Const.CLUSTER);
    out.println();
  }

  // Use COUGAAR resources to find the relationship table on the logplan, if it
  // exists.
  private Hashtable findTable (PlanServiceContext psc) {
    Collection c = psc.getServerPlugInSupport().queryForSubscriber(tableSeeker);
    if (!c.isEmpty())
      return ((TableWrapper) c.iterator().next()).getTable();
    return null;
  }

  /**
   *  This PSP does not profess to use a DTD, even though all of its output
   *  adheres to the "structure.dtd".  Consequently, this method returns null.
   *  @return always null
   */
  public String getDTD () {
    return null;
  }

  /**
   *  This PSP does not return HTML documents.
   *  @return always false
   */
  public boolean returnsHTML () {
    return false;
  }

  /**
   *  Okay, so I lie.  It does really return XML, even if it professes not to.
   *  @return always false
   */
  public boolean returnsXML () {
    return false;
  }

  /**
   *  Whatever.
   *  @return always false.
   */
  public boolean test (HttpInput p0, PlanServiceContext p1) {
    return false;
  }
}