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

public class ScenarioRemoteNodeMenu extends AbstractOpenMapMenu
    implements MenuBarMenu {

    private String defaultText = "Remote Node";
    private int defaultMnemonic= 'R';

    private JDialog changeLInfoDialog = null;
    private JTextField urlField = new JTextField();

    public ScenarioRemoteNodeMenu() {
        super();
        setText(defaultText);
        setMnemonic(defaultMnemonic);
        createAndAdd();
    }
    
    /** Create and add default menu items */
    public void createAndAdd()
    {

        changeLInfoDialog = new JDialog((JFrame)null, "LocInfo Node", true);
        changeLInfoDialog.setResizable(false);
        changeLInfoDialog.getContentPane().setLayout(new BorderLayout());
        changeLInfoDialog.getContentPane().add(getChangeLINodePanel(), BorderLayout.CENTER);
        changeLInfoDialog.pack();



        JMenuItem chgli = add(new JMenuItem("Change Location Info Node"));
        chgli.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//              changeLInfoDialog.setLocationRelativeTo(frame);

              changeLInfoDialog.show();

            }
        });
        chgli.setActionCommand("changeLINode");

    }

  private JPanel getChangeLINodePanel()
  {
    JPanel panel = new JPanel(new GridLayout(3, 4));
    JButton button = new JButton("OK");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            String urlText = urlField.getText();
            // tell the psp icon keep alive about the new location
            ScenarioMap.mapBean.findPspIconLayer().myState.changeLocationInfoNode(urlText);
           }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          changeLInfoDialog.hide();
        }
      });

    panel.add(new JLabel(" ", SwingConstants.CENTER));
    panel.add(new JLabel("Change Location Info Node", SwingConstants.CENTER));

    panel.add(new JLabel("New URI"));
    panel.add(urlField);
    panel.add(button,BorderLayout.SOUTH);
    
    return(panel);
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
