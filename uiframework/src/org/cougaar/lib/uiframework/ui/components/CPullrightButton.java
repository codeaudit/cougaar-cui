package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Vector;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * A button that when pushed will present a pullright menu containing a
 * selection control.  The label of the button is automatically set to the
 * selected item in the selection control.
 *
 * This bean has bounded property:  "selectedItem".
 */
public class CPullrightButton extends JButton implements Selector
{
    private Selector selectorControl = null;
    private JPopupMenu pullright = null;

    public CPullrightButton()
    {
        super();
    }

    /**
     * When look and feel or theme is changed, this method is called.  It
     * ensures that the child dialog's look and feel is updated as well.
     */
    public void updateUI()
    {
        super.updateUI();

        if (pullright != null)
        {
            SwingUtilities.updateComponentTreeUI(pullright);
        }
    }

    /**
     * Pops up a popup menu in the name of this CPullrightButton at the given
     * location.
     *
     * @param x x location on screen to display popup
     * @param y y location on screen to display popup
     */
    public void popupMenu(int x, int y)
    {
        pullright.setLocation(x, y);
        pullright.setInvoker(this);
        pullright.setVisible(true);
    }

    /**
     * Set the control used for making selection
     *
     * @param selector the selection control
     */
     public void setSelectorControl(final Selector selectorControl)
     {
        this.selectorControl = selectorControl;
        setText(selectorControl.getSelectedItem().toString());
        pullright = new PullrightMenu(this, (Component)selectorControl);

        selectorControl.addPropertyChangeListener("selectedItem",
                                     new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    setText(selectorControl.getSelectedItem().toString());
                }
            });

        selectorControl.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    pullright.setVisible(false);
                }
            });

        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    Point p = getLocationOnScreen();
                    popupMenu(p.x + getSize().width, p.y);
                }
             });
     }

    /**
     * Get the control used for making selection
     *
     * @return the selection control
     */
     public Selector getSelectorControl()
     {
        return selectorControl;
     }

    /**
     * Get the currently selected node.
     *
     * @return the currently selected node
     *         (can cast to type DefaultMutableTreeNode)
     */
    public Object getSelectedItem()
    {
        return selectorControl.getSelectedItem();
    }

    /**
     * Set the selected node.
     *
     * @param selectedItem the new node
     *                     (can be of type String or DefaultMutableTreeNode)
     */
    public void setSelectedItem(Object selectedItem)
    {
        selectorControl.setSelectedItem(selectedItem);
    }

    /**
     * Add a property change listener that will be fired whenever a property is
     * changed.
     *
     * @param pcl the new property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        super.addPropertyChangeListener(pcl);

        if (selectorControl != null)
        {
            selectorControl.addPropertyChangeListener(pcl);
        }
    }

    /**
     * Remove a property change listener
     *
     * @param pcl the property change listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        super.removePropertyChangeListener(pcl);

        if (selectorControl != null)
        {
            selectorControl.removePropertyChangeListener(pcl);
        }
    }

    /**
     * Add a property change listener that will be fired whenever the
     * given property is changed.
     *
     * @param name the name of the property to listen for changes in.
     * @param pcl the new property change listener
     */
    public void addPropertyChangeListener(String name,
                                          PropertyChangeListener pcl)
    {
        super.addPropertyChangeListener(name, pcl);

        if (selectorControl != null)
        {
            selectorControl.addPropertyChangeListener(name, pcl);
        }
    }

    /**
     * Remove a property change listener
     *
     * @param name the name of the property to listen for changes in.
     * @param pcl the new property change listener
     */
    public void removePropertyChangeListener(String name,
                                             PropertyChangeListener pcl)
    {
        super.removePropertyChangeListener(name, pcl);

        if (selectorControl != null)
        {
            selectorControl.removePropertyChangeListener(name, pcl);
        }
    }

    /**
     * This private class defines the JPopup menu that is used to display
     * selection control.
     */
    private class PullrightMenu extends JPopupMenu
    {
        public PullrightMenu(Component parent, Component control)
        {
            super("Select Node");
            setInvoker(parent);
            setBorderPainted(true);
            add(control);
        }

        public void cancel()
        {
            firePopupMenuCanceled();
            setVisible(false);
        }

        /**
         * Messaged when the menubar selection changes to activate or
         * deactivate this menu. This implements the
         * <code>javax.swing.MenuElement</code> interface.
         * Overrides <code>MenuElement.menuSelectionChanged</code>.
         *
         * @param isIncluded  true if this menu is active, false if
         *        it is not
         * @see MenuElement#menuSelectionChanged(boolean)
         */
        /*
        public void menuSelectionChanged(boolean isIncluded)
        {
            if (getInvoker() instanceof JButton)
            {
                JButton inv = (JButton)getInvoker();
                System.out.println(inv);
                if (inv.hasFocus())
                    return;
            }
            super.menuSelectionChanged(isIncluded);
        }
        */

    }

    /**
     * main for unit test
     *
     * @param String[] args ignored
     */
    public static void main(String[] args)
    {
        final CPullrightButton prb = new CPullrightButton();
        CSliderSelector ss =
            new CSliderSelector("Set New Time:", new String[]{"C"}, 0, 100);
        prb.setSelectorControl(ss);
        ss.addPropertyChangeListener("selectedItem",
                                     new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    Number n = (Number)((Vector)e.getNewValue()).elementAt(0);
                    prb.setText("C" + ((n.intValue() > 0) ? "+" : "")  +
                                n.intValue());
                }
            });
        Vector defaultValue = new Vector();
        defaultValue.add(new Integer(50));
        ss.setSelectedItem(defaultValue);

        JFrame frame = new JFrame();
        frame.getContentPane().add(prb);
        frame.pack();
        frame.setVisible(true);
    }
}