
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

public class PSP_OrgSub extends PSP_BaseAdapter implements PlanServiceProvider {
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
      Iterator i = rels.getAllRelatives("SUBORDINATE");
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
  private String clusterTag (String org, String subordinate) {
    StringBuffer buf = new StringBuffer();
    buf.append("<Cluster ID=\"");
    buf.append(org);
    buf.append("\">");
    if (subordinate != null) {
      buf.append("<other>");
      buf.append(subordinate);
      buf.append("</other><relationship>SUBORDINATE</relationship>");
    }
    buf.append("</cluster>");
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