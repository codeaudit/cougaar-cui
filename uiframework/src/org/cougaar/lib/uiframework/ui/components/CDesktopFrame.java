package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * This is a top level application frame used to launch stoplight, line plot,
 * and other Cougaar tools in inner frames.
 */
public class CDesktopFrame extends CFrame
{
    private JDesktopPane desktopPane;

    /**
     * Create new CFrameContainer without any tools (i.e. views).
     *
     * @param title of frame.
     */
    public CDesktopFrame(String title)
    {
        super(title, true);
        addMenus();
        desktopPane = new CDesktopPane();
        getContentPane().add(desktopPane, BorderLayout.CENTER);
    }

    // Menus
    private JMenu viewMenu = null;

    /**
     * Add a new tool to the view pulldown menu
     *
     * @param name       name to use for pulldown option
     * @param mnemonic   character to use for pulldown option mnemonic
     * @param panelClass class of the JPanel to use to create new instances of
     *                   the panel
     */
     public void addTool(String name, char mnemonic, Class panelClass,
                         Object[] constParams)
     {
        createMenuItem(viewMenu, name, mnemonic, "",
                       new CreateViewAction(panelClass, constParams, name));
     }

    /**
     * Creates a new inner frame to parent the given JPanel and adds it to the
     * desktop.
     *
     * @param title title for new inner frame
     * @param panel main panel for new inner frame
     */
    public void createInnerFrame(String title, JPanel panel)
    {
        final JInternalFrame f =
            new JInternalFrame(title, true, true, true, true);
        f.getContentPane().add(panel, BorderLayout.CENTER);
        f.setSize(800, 500);
        desktopPane.add(f, JLayeredPane.PALETTE_LAYER);
        f.setVisible(true);

        // In jdk1.3 new inner frame will automatically be set active
        if (!usingJdk13orGreater())
        {
            try {f.setSelected(true);} catch(Exception e) {}

            SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                        // workaround for appearent jdk1.2 Swing bug
                        f.setSize(801, 500);
                    }
                });
        }
    }

    private class CDesktopPane extends JDesktopPane
    {
        /**
        * When look and feel or theme is changed, this method is called.  It
        * overrides a Cougaar theme color for the desktop.
        * (otherwise resulting color combo is extra harsh).
        */
        public void updateUI()
        {
            if ((UIManager.getLookAndFeel() instanceof MetalLookAndFeel) &&
                (currentTheme.getName().startsWith("Cougaar")))
            {
                setBackground(Color.gray);
            }
            else
            {
                setBackground(null);
            }
            super.updateUI();
        }
    }

     /**
     * Create new menus and menu items
     */
    private void addMenus() {
        JMenu printMenu = new JMenu("Print");
        printMenu.setMnemonic('P');
        fileMenu.remove(0);
        fileMenu.add(printMenu, 0);
        createMenuItem(printMenu, "Entire Desktop", 'D', "",
                       new PrintAction(this, true));
        createMenuItem(printMenu, "Selected View", 'S', "",
                       new PrintAction(this, false));

        // ***** create View Menu
        //viewMenu = (JMenu)menuBar.add(new JMenu("View"));
        viewMenu = (JMenu)menuBar.add(new JMenu("View"), 1);
        viewMenu.setMnemonic('V');
    }

    private class PrintAction extends AbstractAction
    {
        // if false, just the active window
	private CDesktopFrame swingset;
        private boolean wholeDesktop;
        protected PrintAction(CDesktopFrame swingset, boolean wholeDesktop) {
            super("PrintAction");
	    this.swingset = swingset;
            this.wholeDesktop = wholeDesktop;
        }

        public void actionPerformed(ActionEvent e)
        {
            (new Thread() {
                    public void run()
                    {
                        if (wholeDesktop)
                        {
                            printComponent(swingset);
                        }
                        else
                        {
                            JInternalFrame selectedFrame = getSelectedFrame();
                            //    desktopPane.getSelectedFrame() (jdk1.3)

                            if (selectedFrame != null)
                            {
                                printComponent(selectedFrame);
                            }
                        }
                    }
                }).start();
        }
    }

    /**
     * Needed for compatibility with jdk1.2.2
     */
    private boolean usingJdk13orGreater()
    {
        float versionNumber =
            Float.parseFloat(System.getProperty("java.class.version"));
        return (versionNumber >= 47.0);
    }

    private class CreateViewAction extends AbstractAction {
	private Class viewClass;
        private Object[] constParams;
        private String title;
        protected CreateViewAction(Class viewClass, Object[] constParams,
                                   String title)
        {
            super("CreateViewAction");
	    this.viewClass = viewClass;
            this.constParams = constParams;
            this.title = title;
        }

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                Class[] paramTypes = {boolean.class};
	        Constructor c = viewClass.getConstructor(paramTypes);
                JPanel panel = (JPanel)c.newInstance(constParams);
                createInnerFrame(title, panel);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This method is only required when compiling/running under jdk1.2
     * In jdk1.3 you can use: desktopPane.getSelectedFrame()
     */
    private JInternalFrame getSelectedFrame()
    {
        JInternalFrame selectedFrame = null;
        JInternalFrame[] ifs = desktopPane.getAllFrames();
        for (int i=0; i < ifs.length; i++)
        {
            if (ifs[i].isSelected())
            {
                selectedFrame = ifs[i];
                break;
            }
        }
        return selectedFrame;
    }
}