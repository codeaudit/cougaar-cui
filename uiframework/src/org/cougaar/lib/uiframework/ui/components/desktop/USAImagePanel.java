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

import java.util.Enumeration;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.applet.Applet;
import java.awt.*;
import java.net.URL;

public class USAImagePanel extends JPanel
{
  USAImageMapComponent myComp;

  USAImagePanel(USAImageMapComponent imComp)
  {
    super();

    myComp = imComp;
  }

  protected void paintComponent (Graphics g)
  {

    super.paintComponent(g);

    Toolkit tk = Toolkit.getDefaultToolkit();

    Image usaIm = tk.getImage ("smallConus.gif");

    if (usaIm != null)
      g.drawImage (usaIm, 0,0, this); // Component, in our superclass, implements ImageObserver
    else
      System.err.println ( "USA image file not found!");

    Font font = new Font ("Arial", Font.BOLD, 12);
    g.setFont(font);

    synchronized (myComp.mapLocationObjects)
    {
      Enumeration eKeys = myComp.mapLocationObjects.keys();
      while (eKeys.hasMoreElements())
      {
        ClusterNetworkMetrics cnm = (ClusterNetworkMetrics) myComp.mapLocationObjects.get(eKeys.nextElement());
        if (cnm.xCoord != 0 && cnm.yCoord != 0)
        {
          String dispText = new String (cnm.clusterName);
          g.drawString(dispText, cnm.xCoord + 10, cnm.yCoord + 5);
          Image xspot = tk.getImage("smalldot.gif");

          int imgX = cnm.xCoord, imgY = cnm.yCoord;
          if (cnm.xCoord > 4)
            imgX = imgX -=5;
          if (cnm.yCoord > 4)
            imgY -= 5;
          g.drawImage(xspot, imgX, imgY, this);
        }
      }
    }

/*
    String om = new String ("OrderManager");
    Font font = new Font ("Arial", Font.BOLD, 8);
    g.setFont(font);
    g.drawString(om,100, 100);

    Image xspot = tk.getImage("selectX.gif");
    g.drawImage(xspot, 80, 95, this);
*/

  }


}