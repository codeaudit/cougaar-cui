
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.util.*;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.lib.planserver.HttpInput;
import org.cougaar.lib.planserver.PSP_BaseAdapter;
import org.cougaar.lib.planserver.PlanServiceContext;
import org.cougaar.lib.planserver.PlanServiceProvider;
import org.cougaar.lib.planserver.PlanServiceUtilities;

import org.cougaar.lib.uiframework.ui.orglocation.plugin.TableWrapper;

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
  // a couple of constants
  private static String SUBORDINATE = "ADMINISTRATIVESUBORDINATE";
  private static String SUPERIOR = "ADMINISTRATIVESUPERIOR";
  private static String SUBORDINATE_OUT = "AdministrativeSubordinate";
  private static String SUPERIOR_OUT = "AdministrativeSuperior";

  // XML headers included at the top of each response
  // for validating parsers:
  // private static String XML_HEADERS =
  //   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
  //   "<!DOCTYPE orgrels SYSTEM \"orgrels.dtd\">";
  // for non-validating parsers:
  private static String XML_HEADERS =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

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

    // For the nonce, we'll do this without any pretense or finesse:
    // if the relationship exists for any period of time, report it.
    Vector snippets = new Vector();
    for (Enumeration e = table.elements(); e.hasMoreElements(); ) {
      TPRelations rels = (TPRelations) e.nextElement();
      String org = rels.getOrgName();
      Iterator i = rels.getAllRelatives(SUPERIOR);
      if (i.hasNext()) {
        while (i.hasNext())
          snippets.add(clusterTag(org, i.next().toString()));
      }
      else {
        snippets.add(clusterTag(org, null));
      }
    }

    // generate the output:  headers, root-level tags, and content
    out.println(XML_HEADERS);
    out.println("<orgrels>");
    for (Iterator i = snippets.iterator(); i.hasNext(); )
      out.println(i.next());
    out.println("</orgrels>");
    out.flush();
  }

  // Generate the XML for a single relationship between two Organizations
  private String clusterTag (String org, String superior) {
    StringBuffer buf = new StringBuffer();
    buf.append("<Cluster ID=\"");
    buf.append(org);
    buf.append("\">");
    if (superior != null) {
      buf.append("<other>");
      buf.append(superior);
      buf.append("</other><relationship>");
      buf.append(SUPERIOR_OUT);
      buf.append("</relationship>");
    }
    buf.append("</Cluster>");
    return buf.toString();
  }

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