
package org.cougaar.lib.uiframework.ui.orglocation.psp.xmlservice;

import java.util.*;
import java.text.*;
import java.io.*;

import org.cougaar.domain.glm.ldm.oplan.OrgActivity;
import org.cougaar.domain.glm.ldm.oplan.TimeSpan;
import org.cougaar.domain.glm.ldm.plan.GeolocLocation;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.*;

/**
 *  This adapter class converts OrgActivity instances into an XML format that
 *  stipulates the Organization involved, the location of the activity, and the
 *  period of time during which the activity takes place.  This information is
 *  useful for telling where the Organization is during the activity.
 */
public class OrgActivityToXml extends CustomQueryBaseAdapter {
  // accumulate a list of times and places
  private Vector locations = new Vector();

  // format all dates using the following

  // An inner class used to coordinate the organization, time, and location
  // information.  The uid field is used to identify the OrgActivity instance
  // from which these data originated.
  private static class OrgTimeLocation {
    private DateFormat format =
      new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public String uid = null;
    public String org = null;
    public double lat = -91.0;
    public double lon = -181.0;
    public Date start = null;
    public Date end = null;

    public OrgTimeLocation () {
    }

    public OrgTimeLocation (
        String id, String orgId, double latitude, double longitude,
        Date startDate, Date endDate)
    {
      uid = id;
      org = orgId;
      lat = latitude;
      lon = longitude;
      start = startDate;
      end = endDate;
    }

    private static void addAttribXml (StringBuffer b, String tag, Object val) {
      b.append("<");
      b.append(tag);
      b.append(">");
      b.append(val);
      b.append("</");
      b.append(tag);
      b.append(">");
    }

    private String replaceNulls (Date d) {
      if (d == null)
        return "null";
      else
        return format.format(d);
    }

    private String toXmlString () {
      StringBuffer buf = new StringBuffer("<OrgTimeLoc>");
      addAttribXml(buf, "UID", uid);
      addAttribXml(buf, "orgId", org);
      addAttribXml(buf, "latitude", String.valueOf(lat));
      addAttribXml(buf, "longitude", String.valueOf(lon));
      addAttribXml(buf, "startDate", replaceNulls(start));
      addAttribXml(buf, "thruDate", replaceNulls(end));
      buf.append("</OrgTimeLoc>");
      return buf.toString();
    }
  }

  /**
   *  Process a collection of objects found for this purpose on the logplan.
   *  The results of this operation will be cached and fed out in a subsequent
   *  call to "returnVal".
   *  @param matches the objects that this adapter is interested in
   */
  public void execute (Collection matches) {
    for (Iterator i = matches.iterator(); i.hasNext(); ) {
      Object obj = i.next();
      if (obj instanceof OrgActivity) {
        OrgActivity act = (OrgActivity) obj;
        OrgTimeLocation loc = new OrgTimeLocation();
        loc.uid = act.getUID().toString();
        loc.org = act.getOrgID();
        GeolocLocation geoloc = act.getGeoLoc();
        if (geoloc != null) {
          loc.lat = geoloc.getLatitude().getDegrees();
          loc.lon = geoloc.getLongitude().getDegrees();
        }
        TimeSpan span = act.getTimeSpan();
        if (span != null) {
          loc.start = span.getStartDate();
          loc.end = span.getThruDate();
        }
        locations.add(loc);
      }
    }
  }

  /**
   *  Send out the results of the previous call to "execute" as an XML stream.
   *  @param out an OutputStream to which XML output is directed
   *  @param xsl an optional XSL transform--ignored.
   */
  public void returnVal (OutputStream out, StringBuffer xsl) {
    PrintStream ps = new PrintStream(out);
    ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ps.println("<OrgTimeLocList>");

    for (Iterator i = locations.iterator(); i.hasNext(); )
      ps.println(((OrgTimeLocation) i.next()).toXmlString());

    ps.println("</OrgTimeLocList>");
    ps.flush();
    locations.clear();
  }

  /**
   *  In support of testing, process the provided data and output it as if it
   *  had been received by the "execute" method.  
   */
  public String rawDataForTest (
      String id, String orgId, double latitude, double longitude,
      Date startDate, Date endDate)
  {
    class WriterStream extends OutputStream {
      private Writer out = null;

      public WriterStream (Writer w) {
        out = w;
      }

      public void write (int b) throws IOException {
        out.write(b);
      }
    }

    locations.add(
      new OrgTimeLocation(id, orgId, latitude, longitude, startDate, endDate));

    StringWriter sw = new StringWriter();
    returnVal(new WriterStream(sw), null);
    return sw.getBuffer().toString();
  }
}
