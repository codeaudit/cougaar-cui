package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * This component is a button that invokes a popup menu filled with a list of
 * Selector controls that are contained in pullright menus.
 * The button's label is updated with both the selector selected and the
 * selected value of the selected selector.
 */
public class CMenuButton extends JButton implements Selector
{
    private CComponentMenu selectorMenu;

    private String selectedLabel;
    private JMenu selectedMenu;
    private Selector selectedSelector;

    /**
     * Default constructor.  Creates a new CMenuButton with no selectors.
     */
    public CMenuButton()
    {
        super("Menu Button");

        setSelectorMenu(new CComponentMenu());

        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    selectorMenu.setPreferredSize(
                        new Dimension(getSize().width,
                                      selectorMenu.getPreferredSize().height));
                    Point p = getLocationOnScreen();
                    popupMenu(p.x, p.y);
               }
            });
    }

    /**
     * Pops up a popup menu in the name of this CMenuButton at the given
     * location.
     *
     * @param x x location on screen to display popup
     * @param y y location on screen to display popup (will be adjusted based
     *            on current selection)
     */
    public void popupMenu(int x, int y)
    {
        selectorMenu.setSelectedItem(selectedSelector, false);
        selectorMenu.setLocation(x, y + getYPopupLocation());
        selectorMenu.setInvoker(this);
        selectorMenu.setVisible(true);
    }

    /**
     * When look and feel or theme is changed, this method is called.  It
     * ensures that the associated popup menu's look and feel is updated as
     * well.
     */
    public void updateUI()
    {
        super.updateUI();

        if (selectorMenu != null)
        {
            SwingUtilities.updateComponentTreeUI(selectorMenu);
        }
    }

    private int getYPopupLocation()
    {
        int menuItemHeight = selectedMenu.getPreferredSize().height;
        int menuIndex = 0;

        // find index of currently selected menu item
        Component[] comps = selectedMenu.getParent().getComponents();
        for (int i = 0; i < comps.length; i++)
        {
            if (comps[i] == selectedMenu)
            {
                menuIndex = i;
                break;
            }
        }

        return -menuIndex * menuItemHeight;
    }

    /**
     * Set a remotely created selector menu to be associated with this menu
     * button.  This method allows multiple menu buttons to share a single
     * selector menu.
     *
     * @param selectorMenu the selector menu to associate with this menu button
     */
    public void setSelectorMenu(final CComponentMenu selectorMenu)
    {
        this.selectorMenu = selectorMenu;

        selectorMenu.addPropertyChangeListener(
            "selectedItem", new SelectionUpdateListener(null));

        Component[] comps = selectorMenu.getAddedComponents();
        for (int i = 0; i < comps.length; i++)
        {
            final Selector s = (Selector)comps[i];
            s.addPropertyChangeListener("selectedItem",
                                        new SelectionUpdateListener(s));
            s.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    propertyChangeHandler(null, s);
                }
            });
        }
        updateSelection();
    }

    /**
     * Adds a selector control to the popup menu associated with this button.
     *
     * @param label menu label for this selector
     * @param s the selector to add to the popup menu
     */
    public void addSelector(String label, final Selector s)
    {
        selectorMenu.addComponent(label, (Component)s);
        s.addPropertyChangeListener("selectedItem",
                                    new SelectionUpdateListener(s));
        updateSelection();
    }

    /**
     * Sets the selected selector
     *
     * @param item the selector to set as selected
     */
    public void setSelectedItem(Object item)
    {
        selectorMenu.setSelectedItem(item);
        updateSelection();
    }

    /**
     * Gets the selected selector
     *
     * @return the selector that is currently selected
     */
    public Object getSelectedItem()
    {
        return selectedSelector;
    }

    private class SelectionUpdateListener implements PropertyChangeListener
    {
        private Selector s = null;

        public SelectionUpdateListener(Selector s)
        {
            this.s = s;
        }

        public void propertyChange(PropertyChangeEvent e)
        {
            propertyChangeHandler(e.getSource(), s);
        }
    }

    private void propertyChangeHandler(Object source, Selector s)
    {
        Object invoker = selectorMenu.getInvoker();
        if (invoker == CMenuButton.this)
        {
            // The user has created this event using my control.
            selectorMenu.setInvoker(null);
            if (s != null)
            {
                selectorMenu.setSelectedItem(s);
            }
            updateSelection();
        }
        else if (source == selectedSelector)
        {
            // My selector is being updated via setValue call
            selectorMenu.setSelectedItem(selectedSelector, false);
            updateSelection();
        }
    }

    private void updateSelection()
    {
        if (selectorMenu.getSelectedMenu() != null)
        {
            Selector oldSelectedSelector = selectedSelector;
            selectedLabel = selectorMenu.getSelectedLabel();
            selectedMenu = selectorMenu.getSelectedMenu();
            selectedSelector = (Selector)selectorMenu.getSelectedItem();
            Object selectedValue = selectedSelector.getSelectedItem();

            String value;
            if (selectedValue instanceof RangeModel)
            {
                RangeModel range = (RangeModel)selectedValue;
                value = "C" + (range.getMin() > 0 ? "+" : "") + range.getMin()
                  + " to C" + (range.getMax() > 0 ? "+" : "") + range.getMax();
            }
            else
            {
                value = selectedValue.toString();
            }

            setText(selectedLabel + ":   " + value);
            selectedMenu.getPopupMenu().setVisible(false);
            selectorMenu.setVisible(false);

            if (oldSelectedSelector != selectedSelector)
            {
                firePropertyChange("selectedItem", oldSelectedSelector,
                                   selectedSelector);
            }
        }
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CMenuButton mb = new CMenuButton();
        Selector itemSelector = new CNodeSelector();
        mb.addSelector("Item", itemSelector);
        mb.addSelector("Time", new CRangeSelector(false));
        mb.addSelector("Org", new CNodeSelector());
        mb.addSelector("Metric", new CNodeSelector());
        mb.setSelectedItem(itemSelector);

        mb.addPropertyChangeListener("selectedItem",
                                     new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    System.out.println("Selected Item Changed");
                    System.out.println("old value: " + e.getOldValue());
                    System.out.println("new value: " + e.getNewValue());
                }
            });

        frame.getContentPane().add(mb, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        itemSelector.setSelectedItem("child2");
    }
}