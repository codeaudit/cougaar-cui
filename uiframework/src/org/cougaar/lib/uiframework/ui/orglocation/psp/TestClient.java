
package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.net.*;
import java.io.*;

import org.cougaar.lib.uiframework.transducer.XmlInterpreter;
import org.cougaar.lib.uiframework.transducer.elements.*;

/**
 *  A test stimulus for the PSP_LocQuery class.  It requires a configuration
 *  file called test_url.txt, which should contain the URL of an active instance
 *  of the aforementioned PSP.  This class makes a few assumptions about the
 *  prevailing parameter space, namely that the dimensions are called "Org" and
 *  "Time" and that the root of the "Org" dimension is called "All Orgs".
 */
public class TestClient {
  private static String DEFAULT_URL =
    "http://localhost:5555/$Stuff/locations/query.psp";

  public static void main (String[] argv) {
    if (argv.length == 0) {
      printUsage();
      return;
    }
    String day = null;
    String org = null;
    boolean got_all = false;
    boolean got_single = false;

    for (int i = 0; i < argv.length; i++) {
      String arg = argv[i];
      if (arg.equals("all")) {
        if (day != null || org != null) {
          printUsage();
          return;
        }
        else
          got_all = true;
      }
      else if (arg.equals("single")) {
        got_single = true;
      }
      else if (arg.startsWith("org=")) {
        if (org != null || got_all) {
          printUsage();
          return;
        }
        else
          org = arg.substring(4);
      }
      else if (arg.startsWith("day=")) {
        if (day != null || got_all) {
          printUsage();
          return;
        }
        else
          day = arg.substring(4);
      }
    }

    QueryTemplate qt = new QueryTemplate();

    if (org != null)
      qt.setOrg(org);
    else
      qt.setDefaultOrg();

    if (got_single) {
      if (day == null) {
        printUsage();
        return;
      }
      qt.setTimes(Integer.parseInt(day), true);
    }
    else {
      if (day != null)
        qt.setTimes(Integer.parseInt(day));
      else
        qt.setDefaultTimes();
    }

    sendQuery(qt.getQuery());
  }

  private static URL readUrlFromFile () throws MalformedURLException {
    try {
      BufferedReader bufr = new BufferedReader(new InputStreamReader(
        new FileInputStream("test_url.txt")));
      String line = null;
      while ((line = bufr.readLine()) != null) {
        if (line.length() > 0) {
          try {
            return new URL(line);
          }
          catch (MalformedURLException mfe) {
          }
        }
      }
    }
    catch (Exception eek) {
    }
    System.out.println(
      "TestClient::readUrlFromFile:  Unable to read \"test_url.txt\".\n" +
      "  defaulting to " + DEFAULT_URL);
    return new URL(DEFAULT_URL);
  }

  private static void printUsage () {
    System.out.println("Usage (at least one argument must be supplied):");
    System.out.println("  [org=<org>] [day=<day>] :  specify the org and/or day");
    System.out.println("  all : get data for all orgs and all days");
  }

  private static void sendQuery (Structure query) {
    try {
      URL u = readUrlFromFile();
      URLConnection conn = u.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);

      // send the request
      PrettyPrinter pp = new PrettyPrinter(conn.getOutputStream());
      query.generateXml(pp);
      pp.flush();
      pp.close();

      // echo the response
      System.out.println("Response is:");
      byte[] b = new byte[1];
      InputStream in = conn.getInputStream();
      int n;
      while ((n = in.read(b)) != -1)
        System.out.write(b, 0, n);
      System.out.println();

      // parse the response
      /*
      XmlInterpreter xin = new XmlInterpreter();
      Structure response = xin.readXml(conn.getInputStream());
      pp = new PrettyPrinter(System.out);
      response.generateXml(pp);
      pp.flush();
      pp.close();
      */
    }
    catch (Exception eek) {
      System.out.println("Bad connection:  " + eek);
      eek.printStackTrace();
    }
  }

  // A QueryTemplate automatically creates the query Structure with all of the
  // standard parts installed.  It also maintains hooks to the places where
  // specializations may be inserted.
  private static class QueryTemplate {
    private Structure query = new Structure();
    private ValElement orgMode = new ValElement();
    private ValElement orgName = new ValElement();
    private ValElement timeMode = new ValElement("all leaves");
    private ValElement timeRange = new ValElement();
    private ListElement timeNode = new ListElement();
    private Attribute singletonMode = new Attribute("mode", "singleton");

    public void setOrg (String s) {
      orgName.setValue(s);
      orgMode.setValue("inherit");
    }

    public void setDefaultOrg () {
      orgName.setValue("All Orgs");
      orgMode.setValue("all leaves");
    }

    public void setTimes (int d0, int d1) {
      timeRange.setValue(d0 + " " + d1);
    }

    public void setTimes (int d) {
      setTimes(d, d);
    }

    public void setTimes (int d, boolean single) {
      setTimes(d, d);
      if (single)
        timeNode.addAttribute(singletonMode);
    }

    public void setDefaultTimes () {
      setTimes(0, 9);
    }

    public Structure getQuery () {
      return query;
    }

    public QueryTemplate () {
      // install the top-level components
      query.addAttribute(new Attribute("query"));
      ListElement root = new ListElement();
      query.addChild(root);

      // install the fields
      Attribute att = new Attribute("fields");
      root.addAttribute(att);
      att.addAttribute(new Attribute("latitude"));
      att.addAttribute(new Attribute("longitude"));

      // attatch a place for the dimensions
      att = new Attribute("dimensions");
      root.addAttribute(att);

      // create a stub org dimension
      att.addChild(createDimStub("Org", orgName, orgMode, null));

      // create a stub time dimension
      att.addChild(
        createDimStub("Time", timeRange, timeMode, timeNode));
    }

    private static ListElement createDimStub (
        String name, ValElement val, ValElement mode, ListElement root)
    {
      ListElement dim = new ListElement();

      Attribute att = new Attribute("name", name);
      dim.addAttribute(att);

      ListElement spec = (root == null ? new ListElement() : root);
      dim.addChild(spec);
      spec.addAttribute(new Attribute("name", val));

      if (mode != null) {
        att = new Attribute("agg");
        att.addAttribute(new Attribute("mode", mode));
        spec.addAttribute(att);
      }

      return dim;
    }
  }
}