/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

/**
 * This is a top level application frame used to launch stoplight, line plot,
 * and other Cougaar tools in their own frames.
 */
public class CFrameLauncher extends CFrame
{
    private CPanel mainPanel;
    private Vector launchedFrames = new Vector();

    /**
     * Default constructor.  Create new launcher without any tools
     * (i.e. views) or title.
     */
    public CFrameLauncher()
    {
        super();
        init();
    }

    /**
     * Create new launcher without any tools (i.e. views).
     *
     * @param title of frame.
     */
    public CFrameLauncher(String title)
    {
        super(title, true);
        init();
    }

    private void init()
    {
        //lafMenu.setEnabled(false);
        mainPanel = new CPanel();
        getContentPane().add(mainPanel);

        // find close and print menu items and remove them.
        for (int i = 0; i < fileMenu.getItemCount(); i++)
        {
            JMenuItem mi = fileMenu.getItem(i);
            if (mi != null)
            {
                String menuLabel = mi.getText();
                if ((menuLabel.equals("Close")) || (menuLabel.equals("Print")))
                {
                    fileMenu.remove(mi);
                    i--;
                }
            }
        }
    }

    /**
     * Add a new tool to the view pulldown menu
     *
     * @param name              name to use for pulldown option
     * @param mnemonic          character to use for pulldown option mnemonic
     * @param cougaarUIClass    class that implements the CougaarUI interface.
     *                          Used to create new instances of UI in reaction
     *                          to user selections.
     * @param constParamClasses array of classes that describe constructor
     *                          parameters that will be used for creating new
     *                          instances of the CougaarUI.
     * @param constParams       array of objects that will be passed into
     *                          constructor when creating new instances of the
     *                          CougaarUI.
     */
     public void addTool(final String name, char mnemonic,
                         final Class cougaarUIClass,
                         final Class[] constParamClasses,
                         final Object[] constParams)
     {
        JButton launcher = new JButton(name);
        mainPanel.add(launcher);
        launcher.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    Cursor wait =
                            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
                    CFrameLauncher.this.setCursor(wait);

                    try
                    {
                        Constructor c =
                            cougaarUIClass.getConstructor(constParamClasses);
                        CougaarUI cougaarUI =
                            (CougaarUI)c.newInstance(constParams);
                        CFrame frame = new CFrame();
                        cougaarUI.install(frame);
                        //frame.getLookAndFeelPulldown().setEnabled(false);
                        frame.setTitle(name);
                        //frame.getLookAndFeelPulldown().
                        //    setEnabled(cougaarUI.supportsPlaf());
                        //frame.getLookAndFeelPulldown().setVisible(false);
                        //frame.getThemesPulldown().setVisible(false);
                        frame.setVisible(true);
                        launchedFrames.add(frame);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        CFrameLauncher.this.setCursor(
                            Cursor.getDefaultCursor());
                    }
                }
            });
    }

    private class CPanel extends JPanel
    {
        /**
        * When look and feel or theme is changed, this method is called.
        */
        public void updateUI()
        {
            super.updateUI();

            if (CFrameLauncher.this != null)
            {
                for (int i = 0; i < launchedFrames.size(); i++)
                {
                    CFrame frame = (CFrame)launchedFrames.elementAt(i);
                    SwingUtilities.updateComponentTreeUI(frame);
                }

                SwingUtilities.invokeLater(new Runnable() {
                        public void run()
                        {
                            CFrameLauncher.this.pack();
                        }
                    });
            }
        }
    }
}