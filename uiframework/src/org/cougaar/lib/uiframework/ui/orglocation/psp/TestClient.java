/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

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
    QueryTemplate qt = new QueryTemplate();
    qt.setDefaults();

    // send the request to the screen
    // echoQuery(qt.getQuery());

    // send the request to the server
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

  private static void echoQuery (Structure query) {
    System.out.println("Query is:");
    PrettyPrinter pp = new PrettyPrinter(System.out);
    query.generateXml(pp);
    pp.flush();
    System.out.println();
  }

  private static void sendQuery (Structure query) {
    try {
      URL u = readUrlFromFile();
      URLConnection conn = u.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);

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

    public void setDefaults () {
      orgName.setValue("All Orgs");
      orgMode.setValue("all leaves");
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
      att.addAttribute(new Attribute("startTime"));
      att.addAttribute(new Attribute("endTime"));
      att.addAttribute(new Attribute("latitude"));
      att.addAttribute(new Attribute("longitude"));

      // attatch a place for the dimensions
      att = new Attribute("dimensions");
      root.addAttribute(att);

      // create the dimension tags
      att.addChild(createDimStub("OrgLocations", orgName, orgMode, null));
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