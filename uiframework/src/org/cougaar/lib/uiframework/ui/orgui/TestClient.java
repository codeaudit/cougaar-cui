/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *  This simple test client connects to a single URL and echoes the response to
 *  standard output.  It can be used to test practically anything that behaves
 *  like a URL, and it does not care if XML is properly formatted or any other
 *  such nonsense.
 */
public class TestClient {
  // a default URL String, in case the user is too lazy to supply one
  private static String DEFAULT_URL =
    "http://localhost:5555/$agg/orgui/orgsub.psp";

  // the URL to which this client will connect
  private String urlString = null;

  /**
   *  Set the URL to which this client will connect.
   *  @param url a String representing the target URL
   */
  public void setUrl (String url) {
    urlString = url;
  }

  // Open the connection to the PSP and return the resulting InputStream
  private InputStream connect () {
    try {
      URL url = new URL(urlString);
      URLConnection conn = url.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(false);
      return conn.getInputStream();
    }
    catch (Exception oh_no) {
      oh_no.printStackTrace();
    }
    return null;
  }

  // read the contents of an InputStream and print them to the screen
  private void echo (InputStream in) {
    if (in == null) {
      System.out.println(
        "TestClient::echo:  No input detected--InputStream is null");
      return;
    }

    try {
      byte[] b = new byte[1];
      for (int n = -1; (n = in.read(b)) != -1; System.out.write(b, 0, n));
      System.out.println();
    }
    catch (Exception oh_no) {
      oh_no.printStackTrace();
    }
  }

  /**
   *  Main algorithm--connect to the prescribed site and download the contents.
   *  Echo those contents to standard out.
   */
  public void go () {
    echo(connect());
  }

  public static void main (String[] argv) {
    TestClient tc = new TestClient();
    if (argv.length > 0)
      tc.setUrl(argv[0]);
    else
      tc.setUrl(DEFAULT_URL);
    tc.go();
  }
}