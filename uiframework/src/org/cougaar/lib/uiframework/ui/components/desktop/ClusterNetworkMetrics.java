/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.components.desktop;

import java.util.Vector;

public class ClusterNetworkMetrics
{

  public String clusterName;
  public String ipAddress;
  public double lat;
  public double lon;
  public int xCoord, yCoord;

  public Vector ipRcvPackets = new Vector();
  public Vector ipLostPackets = new Vector();
  public Vector ipSentPackets = new Vector();
  public Vector timeStamp = new Vector();

  public ClusterNetworkMetrics()
  {
  }

  public String toString()
  {
    return new String (        "cluster: " + clusterName + "\n" +
                               "latitude: " + lat + "\n" +
                               "longitude: " + lon + "\n" );
  }

  public boolean pointWithin (int x, int y)
  {
    boolean b = true;

    if (x < xCoord - 5 || x > xCoord + 5)
      b = false;

    if ( y < yCoord - 5 || y > yCoord + 5)
      b = false;

    return b;
  }



}