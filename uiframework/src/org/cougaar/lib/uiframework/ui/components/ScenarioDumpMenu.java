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
package org.cougaar.lib.uiframework.ui.components;

import java.beans.*;
import java.beans.beancontext.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import com.bbn.openmap.*;
import com.bbn.openmap.image.*;

import com.bbn.openmap.gui.*;

import org.cougaar.lib.uiframework.ui.map.app.ScenarioMap;
import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayer;

import org.cougaar.glm.map.MapLocationInfo;
import org.cougaar.planning.ldm.plan.LocationScheduleElement;
import org.cougaar.glm.ldm.plan.GeolocLocationImpl;
import org.cougaar.planning.ldm.measure.Latitude;
import org.cougaar.planning.ldm.measure.Longitude;

public class ScenarioDumpMenu extends AbstractOpenMapMenu
    implements MenuBarMenu {
    final int LOCATIONS = 0;
    final int RELATIONSHIPS = 1;

    private String defaultText = "Debug";
    private int defaultMnemonic= 'D';

    private JDialog changeLInfoDialog = null;
    private JTextField urlField = new JTextField();

    public ScenarioDumpMenu()
    {
        super();
        setText(defaultText);
        setMnemonic(defaultMnemonic);
        createAndAdd();
    }
    
    /** Create and add default menu items */
    public void createAndAdd()
    {

        JMenuItem miloc = add(new JMenuItem("Dump Locations"));
        miloc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              String command = e.getActionCommand();
              PspIconLayer pl = ScenarioMap.getMapBean(getRootPane()).findPspIconLayer();
//              PspIconLayer pl = ScenarioMap.mapBean.findPspIconLayer();
              Hashtable mlih = pl.myState.mostRecentlyLoaded;
              dumpToOutput (mlih, LOCATIONS);
            }
        });
        miloc.setActionCommand("dumpLocs");

        JMenuItem mirel = add(new JMenuItem("Dump Relationships"));
        mirel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                String command = e.getActionCommand();
                PspIconLayer pl = ScenarioMap.getMapBean(getRootPane()).findPspIconLayer();
                Hashtable mlih = pl.myState.mostRecentlyLoaded;
                dumpToOutput (mlih, RELATIONSHIPS);
            }
        });
        miloc.setActionCommand("dumpRels");

    }

    private void dumpToOutput (Hashtable ht, int flag)
    {

      if (ht == null || ht.keys() == null)
        return;
            
//      System.out.println ("The raw hashtable \n\t" + ht);
      for (Enumeration eht = ht.keys(); eht.hasMoreElements(); )
      {
        String cid = (String) eht.nextElement();

        MapLocationInfo mli = (MapLocationInfo) ht.get(cid);

        if (flag == RELATIONSHIPS)
        {
           System.out.println ("\nRelationships received from LocationCluster");
           System.out.println (cid);
           Vector relVec = mli.getRelationshipSchedule();
           for (int ii = 0; ii < relVec.size(); ii ++)
           {
             System.out.println ("\t" + relVec.get(ii) );
           }
           System.out.println (" ");
        }
        else if (flag == LOCATIONS)
        {
           System.out.println ("\nLocations received from LocationCluster");
           System.out.println (cid);
           Vector schedVec = mli.getScheduleElements();
           for (int ii = 0; ii < schedVec.size(); ii ++)
           {
             LocationScheduleElement locSched = (LocationScheduleElement) schedVec.get(ii);
             GeolocLocationImpl loc = (GeolocLocationImpl) locSched.getLocation();
             Latitude lat = loc.getLatitude();
             Longitude lon = loc.getLongitude();

             System.out.println ("\tstart time \t" + locSched.getStartDate().toString());
             System.out.println ("\tlat lon    \t" + lat.getDegrees() + " " + lon.getDegrees() );
             System.out.println ("\tend time   \t" + locSched.getEndDate().toString() + "\n");
           }
        }
      }
     }

    /**
     * This method does nothing, but is required as a part of
     * MenuInterface
     */
    public void findAndUnInit(Iterator it){}

    /**
     * This method does nothing, but is required as a part of
     * MenuInterface
     */
    public void findAndInit(Iterator it){}

    /**
     * When this method is called, it sets the given BeanContext on
     * menu items that need to find objects to get their work done.
     * Note: Menuitems are not added to beancontext
     */
    public void setBeanContext(BeanContext in_bc) throws PropertyVetoException {
        super.setBeanContext(in_bc);
        if(!Environment.isApplication()) { //running as an Applet
            return;
        }
//        if (spMenu != null) spMenu.setMapHandler(getMapHandler());
//        if (jpegMenuItem != null) jpegMenuItem.setMapHandler(getMapHandler());
//        if (gifMenuItem != null) gifMenuItem.setMapHandler(getMapHandler());
    }
}
