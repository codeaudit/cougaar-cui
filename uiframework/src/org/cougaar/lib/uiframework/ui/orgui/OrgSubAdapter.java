
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.util.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.planning.ldm.plan.RelationshipImpl;
import org.cougaar.domain.planning.ldm.plan.RelationshipSchedule;
import org.cougaar.domain.planning.ldm.plan.Role;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;
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
  protected static String XML_HEADER =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  // this Vector contains the Roles that we are interested in
  protected Vector roles = new Vector();

  // store the results until requested by the container
  protected Vector relations = new Vector();

  // the event to which the current response corresponds
  protected String event = null;

  /**
   *  Create a new OrgSubAdapter.  Also, initialize the roles Vector.
   */
  public OrgSubAdapter () {
    roles.add(Constants.Role.ADMINISTRATIVESUBORDINATE);
  }

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
    // cache the event type for future reference
    event = eventType;
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Organization org = (Organization) i.next();
      if (org.isSelf()) {
        String name = org.getItemIdentificationPG().getNomenclature();
        RelationshipSchedule rs = org.getRelationshipSchedule();
        for (Iterator j = roles.iterator(); j.hasNext(); ) {
          Role role = (Role) j.next();
          Collection relatives = rs.getMatchingRelationships(role);
          for (Iterator rels = relatives.iterator(); rels.hasNext(); ) {
            RelationshipImpl r = (RelationshipImpl) rels.next();
            Organization rel = (Organization) rs.getOther(r);
            String relName = rel.getItemIdentificationPG().getNomenclature();
            long start = r.getStartTime();
            long end = r.getEndTime();
            relations.add(new Bond(name, relName, role.getName(), start, end));
          }
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
    // if this is not an ADD, ignore it--this works with standard polling
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    // System.out.println("   [[");
    // System.out.println("OrgSubAdapter::returnVal:  giving output...");
    PrintStream ps = new PrintStream(out);
    println(ps, XML_HEADER);
    println(ps, "<" + Const.ORG_RELS + ">");
    for (Iterator i = relations.iterator(); i.hasNext(); )
      println(ps, ((Bond) i.next()).toXml());
    println(ps, "</" + Const.ORG_RELS + ">");
    // System.out.println("   ]]");
    ps.flush();
    relations.clear();
  }

  private static void println (PrintStream ps, String s) {
    // System.out.println(s);
    ps.println(s);
  }

  protected static class Bond {
    private String org = null;
    private String relative = null;
    private String relationship = null;
    private long startTime = 0;
    private long endTime = -1;

    public Bond (String o, String r, String rel, long start, long end) {
      org = o;
      relative = r;
      relationship = rel;
      startTime = start;
      endTime = end;
    }

    public String toXml () {
      StringBuffer buf = new StringBuffer();
      addOpenTag(buf, Const.CLUSTER, Const.ID_ATTRIBUTE, org);
      addTag(buf, Const.RELATIVE, relative);
      addTag(buf, Const.REL_TYPE, relationship);
      addTag(buf, Const.START, String.valueOf(startTime));
      addTag(buf, Const.END, String.valueOf(endTime));
      addCloseTag(buf, Const.CLUSTER);
      return buf.toString();
    }

    private static void addTag (StringBuffer buf, String name, String val) {
      addOpenTag(buf, name);
      buf.append(val);
      addCloseTag(buf, name);
    }

    private static void addOpenTag (StringBuffer buf, String name) {
      addOpenTag(buf, name, null, null);
    }

    private static void addOpenTag (
        StringBuffer buf, String name, String attrib, String val)
    {
      buf.append("<");
      buf.append(name);
      if (attrib != null && val != null) {
        buf.append(" ");
        buf.append(attrib);
        buf.append("=\"");
        buf.append(val);
        buf.append("\"");
      }
      buf.append(">");
    }

    private static void addCloseTag (StringBuffer buf, String name) {
      buf.append("</");
      buf.append(name);
      buf.append(">");
    }
  }
}