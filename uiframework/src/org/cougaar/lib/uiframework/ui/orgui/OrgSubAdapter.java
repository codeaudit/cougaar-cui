
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.util.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.planning.ldm.plan.RelationshipImpl;
import org.cougaar.domain.planning.ldm.plan.RelationshipSchedule;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;

/**
 *  This is a class of adapters for the PSP_GenericReaderWriter whose purpose
 *  is to report on relationships between the host Cluster (Organization) and
 *  other Organizations in the society.  In order to work properly, an instance
 *  of this class must be associated with a subscription to Organization assets,
 *  which it will dutifully search for the one that represents the host Cluster
 *  (i.e., one whose isSelf() method returns true).  Results are based on the
 *  RelationshipSchedule of the self-org.
 */
public class OrgSubAdapter extends CustomQueryBaseAdapter {
  // attatch this header to every XML output
  private static String XML_HEADER =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  // store the results until requested by the container
  private Vector subordinates = new Vector();

  /**
   *  Given a collection of Organization assets found on the logplan, find the
   *  one that represents the host Cluster and store the useful parts of its
   *  RelationshipSchedule for later retrieval by the returnVal method (q.v.).
   *  For the moment, the type of event being reported (e.g., ADD, REMOVE, or
   *  CHANGE) is not taken into consideration.  For standard polling, this is
   *  of no consequence.
   *
   *  @param matches the Collection of Organizations found on the logplan
   *  @param eventType the type of event being reported
   */
  public void execute (Collection matches, String eventType) {
    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Organization org = (Organization) i.next();
      if (org.isSelf()) {
        String name = org.getItemIdentificationPG().getNomenclature();
        RelationshipSchedule rs = org.getRelationshipSchedule();
        Collection c = rs.getMatchingRelationships(
          Constants.Role.ADMINISTRATIVESUBORDINATE);
        for (Iterator rels = c.iterator(); rels.hasNext(); ) {
          RelationshipImpl r = (RelationshipImpl) rels.next();
          Organization sub = (Organization) rs.getOther(r);
          String subName = sub.getItemIdentificationPG().getNomenclature();
          long start = r.getStartTime();
          long end = r.getEndTime();
          subordinates.add(new Pair(name, subName, start, end));
        }
      }
    }
  }

  /**
   *  Starting with the results produced by a previous call to execute (q.v.),
   *  generate the XML response to be sent to the client (probably an AggAgent).
   *  The cached results are cleared after being converted and sent out.
   *  @param out the OutputStream to which output is directed.
   */
  public void returnVal (OutputStream out) {
    PrintStream ps = new PrintStream(out);
    ps.println(XML_HEADER);
    ps.println("<OrgRelations>");
    for (Iterator i = subordinates.iterator(); i.hasNext(); )
      ps.println(((Pair) i.next()).toXml());
    ps.println("</OrgRelations>");
    ps.flush();
    subordinates.clear();
  }

  // This class embodies a pair of Organizations in a superior/subordinate
  // relationship with each other.  Time constraints on the relationship are
  // also included, as is the logic for expressing the contents in XML.
  private static class Pair {
    private String superior = null;
    private String subordinate = null;
    private long startTime = 0;
    private long endTime = -1;

    public Pair (String sup, String sub, long start, long end) {
      superior = sup;
      subordinate = sub;
      startTime = start;
      endTime = end;
    }

    public String toXml () {
      StringBuffer buf = new StringBuffer("<OrgRelationship><superior>");
      buf.append(superior);
      buf.append("</superior><subordinate>");
      buf.append(subordinate);
      buf.append("</subordinate><start>");
      buf.append(startTime);
      buf.append("</start><end>");
      buf.append(endTime);
      buf.append("</end></OrgRelationship>");
      return buf.toString();
    }
  }
}