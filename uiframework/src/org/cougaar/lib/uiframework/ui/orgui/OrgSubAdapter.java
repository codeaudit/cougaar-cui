
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.util.*;

import org.cougaar.domain.glm.ldm.Constants;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.planning.ldm.plan.RelationshipImpl;
import org.cougaar.domain.planning.ldm.plan.RelationshipSchedule;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;

public class OrgSubAdapter extends CustomQueryBaseAdapter {
  private static String XML_HEADER =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  private Vector subordinates = new Vector();

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