/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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