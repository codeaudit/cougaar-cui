package mil.darpa.log.alpine.blackjack.assessui.client;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.components.CDesktopFrame;
import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.util.CougaarUI;
import org.cougaar.lib.uiframework.ui.util.VariableInterfaceManager;

/**
 * This popup is used to launch new cougaar UIs configured based on the
 * location in which the user invoked the popup menu.
 */
public class UILaunchPopup extends JPopupMenu
{
    private Hashtable configuration = new Hashtable();

    /**
     * Default constructor.  Create a new popup filled with selections for
     * launching other UIs.
     */
    public UILaunchPopup()
    {
        ActionListener selectionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    launchUI(((JMenuItem)e.getSource()).getText());
                }
            };
        JMenuItem stoplight = new JMenuItem(UIConstants.STOPLIGHT_UI_NAME);
        stoplight.addActionListener(selectionListener);
        add(stoplight);
        JMenuItem lineplot = new JMenuItem(UIConstants.LINEPLOT_UI_NAME);
        lineplot.addActionListener(selectionListener);
        add(lineplot);
    }

    /**
     * Set a new config property that the new UI should be configured to show.
     *
     * @param configKey the key of the new config property that the new UI
     *                  should be configured to show.
     * @param configValue the value of the new config property that the new UI
     *                    should be configured to show.
     */
    public void setConfigProperty(Object configKey, Object configValue)
    {
        configuration.put(configKey, configValue);
    }

    /**
     * Launch a new UI in the correct type of frame and configured based
     * on the configuration variables.
     *
     * @param uiName name of the UI to launch (use UIConstants)
     */
    public void launchUI(final String uiName)
    {
        final JComponent swingInvoker =
            (getInvoker() instanceof JComponent)?(JComponent)getInvoker():null;
        final Component root =
            (swingInvoker != null) ? swingInvoker.getTopLevelAncestor() : null;
        if (swingInvoker != null)
        {
            Cursor wait = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
            swingInvoker.setCursor(wait);
            root.setCursor(wait);
            if (swingInvoker.getParent() != null)
                swingInvoker.getParent().setCursor(wait);
        }
        (new Thread() {
            public void run()
            {
                // Bring up new UI in the proper type of frame
                JFrame myFrame = findJFrame();
                JFrame newUIsFrame;
                if (myFrame instanceof CDesktopFrame)
                {
                    newUIsFrame = myFrame;
                }
                else
                {
                    newUIsFrame =new CFrame(uiName, false);
                }

                VariableInterfaceManager vim = null;
                CougaarUI newUI = null;
                if (uiName.equals(UIConstants.STOPLIGHT_UI_NAME))
                {
                    StoplightPanel slp = new StoplightPanel(false);
                    newUI = slp;
                    vim = slp.getVariableInterfaceManager();
                }
                else
                {
                    LinePlotPanel lpp = new LinePlotPanel(false);
                    newUI = lpp;
                    vim = lpp.getVariableInterfaceManager();
                }

                Enumeration keys = configuration.keys();
                while (keys.hasMoreElements())
                {
                    Object key = keys.nextElement();
                    vim.getDescriptor(key.toString()).
                        setValue(configuration.get(key));
                }

                if (newUIsFrame instanceof CDesktopFrame)
                {
                    CDesktopFrame cfc = (CDesktopFrame)newUIsFrame;
                    cfc.createInnerFrame(uiName, newUI);
                }
                else
                {
                    newUI.install(newUIsFrame);
                    newUIsFrame.setVisible(true);
                }

                if (getInvoker() != null)
                {
                    Cursor defaultc = Cursor.getDefaultCursor();
                    root.setCursor(defaultc);
                    swingInvoker.setCursor(defaultc);
                    if (swingInvoker.getParent() != null)
                        swingInvoker.getParent().setCursor(defaultc);
                }
            }
        }).start();
    }

    private JFrame findJFrame()
    {
        if (getInvoker() != null)
        {
            Container parent = getInvoker().getParent();
            while (!(parent instanceof JFrame))
            {
                parent = parent.getParent();
            }
            return (JFrame)parent;
        }
        return null;
    }
}