package org.cougaar.lib.uiframework.ui.components;

import java.util.Vector;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import com.bbn.openmap.gui.MenuPanel;

import com.bbn.openmap.Environment;

import org.cougaar.lib.uiframework.ui.map.app.ScenarioMap;
import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayer;
import org.cougaar.lib.uiframework.ui.map.layer.RouteJdbcConnector;

public class ScenarioMenuPanel extends MenuPanel
{

  ScenarioMap scenMap;

  public ScenarioMenuPanel (ScenarioMap smap)
  {
    scenMap = smap;
  }

    public JDialog getAboutBox ()
    {
      if (aboutBox == null) {
          aboutBox = createAboutBox();
          aboutBox.getContentPane().setLayout(new BorderLayout());
          aboutBox.getContentPane().add(createCopyrightViewer(),
                                        BorderLayout.CENTER);
          aboutBox.getContentPane().add(
                  createAboutControls(aboutBox),
                  BorderLayout.SOUTH
                  );
          aboutBox.pack();
      }
      
      return(aboutBox);
    }

   /**
     * Create and add the File menu.
     */
    protected void createFileMenu () {
        // temp variables
        JCheckBoxMenuItem cb;
        JRadioButtonMenuItem rb;
        JMenuItem mi;

        // File Menu
        JMenu file = (JMenu) add(new JMenu("File"));
        file.setMnemonic('F');
        mi = (JMenuItem) file.add(new JMenuItem("About"));
        mi.setMnemonic('t');
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              JDialog aboutBox = getAboutBox();

                aboutBox.setVisible(true);
            }
        });

        file.add(new JSeparator());
        mi = (JMenuItem) file.add(new JMenuItem("Open"));
        mi.setMnemonic('O');
        mi.setEnabled(false);

        JMenuItem saveMenu = (JMenuItem) file.add(new JMenuItem("Save As..."));
        saveMenu.addActionListener(new ActionListener()
        {
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
                 PspIconLayer pLayer = scenMap.findPspIconLayer();

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

        JMenuItem restoreMenu = (JMenuItem) file.add(new JMenuItem("Restore..."));
        restoreMenu.addActionListener(new ActionListener()
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

                PspIconLayer pLayer = scenMap.findPspIconLayer();
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


        file.add(new JSeparator());
        if (Environment.isApplication()) {
            mi = (JMenuItem) file.add(new JMenuItem("Quit"));
//         mi.setMnemonic('U');
            mi.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // HACK - need to call shutdown() on mapbean
                    // actually we should broadcast a shutdown event so thato ther
                    // gui components can clean up, and maybe only one can call
                    // exit.

                    System.exit(0);
                }
            });
        }
    }

}