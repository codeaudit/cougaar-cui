/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
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

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.bbn.openmap.Environment;
import com.bbn.openmap.gui.*;

import org.cougaar.lib.uiframework.ui.map.app.ScenarioMap;
import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayer;
import org.cougaar.lib.uiframework.ui.map.layer.RouteJdbcConnector;

public class ScenarioFileMenu extends FileMenu implements MenuBarMenu
{

    public void createAndAdd()
    {
        add(new AboutMenuItem());
        
        if(Environment.isApplet()) {
            return;
        }

        // Not ready yet
        //add(createSavePropertiesMenuItem());
        JMenu saveMenu = new JMenu("Save As..");
        saveMenu.add(createSaveAsJpegMenuItem());

        add(createSerializeMenu());
        add(createDeserializeMenu());

        // Not ready yet.
        //saveMenu.add(createSaveAsGifMenuItem());
        add(saveMenu);
        add(new JSeparator());
        add(createExitMenu());
    } 

    public JMenuItem createSerializeMenu()
    {
      JMenuItem serializeMenuItem = new JMenuItem("Serialize...");
	    serializeMenuItem.addActionListener(new ActionListener() {
          public void actionPerformed (ActionEvent ae)
          {
            JFileChooser chooser = new JFileChooser();
            ExtensionFileFilter filter = new ExtensionFileFilter();
            filter.addExtension("ser");
            filter.setDescription("Serialization Files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(getParent());
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

              String fullFile = chooser.getSelectedFile().getAbsolutePath();

              if (! fullFile.endsWith (".ser") )
                 fullFile += ".ser";

              System.out.println("You chose to save to this file: " + fullFile );
               try
               {
                 PspIconLayer pLayer = ScenarioMap.mapBean.findPspIconLayer();

                 Vector saveAll = new Vector(2);
                 saveAll.setSize (2);
                 saveAll.setElementAt (pLayer.myState.mostRecentlyLoaded, 0);
                 saveAll.setElementAt (RouteJdbcConnector.getRtsHash(), 1);

                 pLayer.save( fullFile, saveAll);

               }
               catch (java.io.IOException ioexc)
               {
                 System.err.println ("error serializing PspIconLayer: " + ioexc.toString() );
                 ioexc.printStackTrace();
               }
            }
          }
        });

      return serializeMenuItem;

    }

    public JMenuItem createDeserializeMenu()
    {
        JMenuItem deserializeMenuItem = new JMenuItem("Load Serialized...");
        deserializeMenuItem.addActionListener(new ActionListener()
        {
          public void actionPerformed (ActionEvent ae)
          {
            JFileChooser chooser = new JFileChooser();
            ExtensionFileFilter filter = new ExtensionFileFilter();
            filter.addExtension("ser");
            filter.setDescription("Serialization Files");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(getParent());
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
              System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath() );

              try
              {
                java.util.Hashtable loadUp;

                PspIconLayer pLayer = ScenarioMap.mapBean.findPspIconLayer();
                java.io.FileInputStream fis = new java.io.FileInputStream ( chooser.getSelectedFile().getAbsolutePath() );

                Object resObj = pLayer.restore (fis);
                if (resObj instanceof Vector)
                {
                  // restore the new way
                  Vector saveAll = (Vector) resObj;
                  loadUp = (java.util.Hashtable) saveAll.get(0);

                  java.util.Hashtable rtsHash = (java.util.Hashtable) saveAll.get(1);
                  RouteJdbcConnector.setRtsHash (rtsHash);
                }

                else
                {
                  // restore the old way
                  loadUp = (java.util.Hashtable) resObj;
                }

                pLayer.myState.load (loadUp);

                pLayer.setTime (null); // reset the current time
                pLayer.repaint();
              }

              catch (Exception anyExc)
              {
                System.err.println ("Error reading serialized file: " + chooser.getSelectedFile().getName() );
                anyExc.printStackTrace();
              }
            }
          }
        });

        return deserializeMenuItem;
    }


}
