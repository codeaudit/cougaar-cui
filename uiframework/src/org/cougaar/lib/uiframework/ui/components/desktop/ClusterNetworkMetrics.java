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