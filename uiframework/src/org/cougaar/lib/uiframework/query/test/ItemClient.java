/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import java.net.*;
import java.io.*;

public class ItemClient {
  private String url = "http://localhost:5555/items/QUERY.PSP";
  private String query =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    "<structure>" +
      "<a name=\"query\"/>" +
      "<list>" +
        "<a name=\"fields\">" +
          "<a name=\"PigLatin\"/>" +
        "</a>" +
        "<a name=\"dimensions\">" +
          "<list>" +
            "<a name=\"name\"><val>Items</val></a>" +
            "<list>" +
              "<a name=\"name\"><val>All Items</val></a>" +
              "<a name=\"agg\">" +
                "<a name=\"mode\"><val>all leaves</val></a>" +
              "</a>" +
            "</list>" +
          "</list>" +
        "</a>" +
      "</list>" +
    "</structure>";

  public void go () {
    try {
      setUpQuery();

      // open (and configure) the connection
      URL u = new URL(url);
      URLConnection conn = u.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);

      // send the request
      PrintWriter out = new PrintWriter(conn.getOutputStream());
      out.println(query);
      out.flush();
      out.close();

      // read the response
      BufferedReader in =
        new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      while ((line = in.readLine()) != null)
        System.out.println(line);
    }
    catch (Exception bugger) {
      System.out.println("Bugger--" + bugger);
      bugger.printStackTrace();
    }
  }

  private void setUpQuery () throws IOException {
    StringBuffer buf = new StringBuffer();
    BufferedReader in = new BufferedReader(new InputStreamReader(
      new FileInputStream("d:/home/alpine/gui/queries/test/query8.xml")));
    String line = null;
    while ((line = in.readLine()) != null)
      buf.append(line);
    query = buf.toString();
  }

  public static void main (String[] argv) {
    (new ItemClient()).go();
  }
}