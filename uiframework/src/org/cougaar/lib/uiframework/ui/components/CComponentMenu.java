package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * A popup menu bean that manages a set of components.  Each component has an
 * associated menu item in the popup menu.  The components are rendered as
 * pullright options from the menu items.  The popup menu can be used
 * to select a component by clicking on the components menu label.<BR><BR>
 *
 * This bean has bounded property:  "selectedItem" (the selected component)
 */
public class CComponentMenu extends JPopupMenu implements Selector
{
    private JMenu selectedMenu;

    /**
     * Default constructor.  Creates a new, empty CComponentMenu.
     */
    public CComponentMenu()
    {
        super();
    }

    /**
     * Adds a component as a pullright option to the popup menu.
     *
     * @param label menu label for this selector
     * @param c the component to add to the popup menu
     */
    public void addComponent(final String label, final Component c)
    {
        final JMenu menu = new JMenu(label);
        menu.add(c);
        menu.enableInputMethods(false); // Swing bug workaround
        add(menu);

        menu.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e)
                {
                    updateSelection(menu, true);
                }
            });

        selectedMenu = menu;
    }

    /**
     * Sets the selected component.  A selectedItem property change event will
     * fire based on this change.
     *
     * @param item either the component to select or the label associated with
     *             the component to select
     */
    public void setSelectedItem(Object item)
    {
        setSelectedItem(item, true);
    }

    /**
     * Sets the selected component
     *
     * @param item either the component to select or the label associated with
     *             the component to select
     * @param firePropChange if true, will fire a selectedItem property change
     *                       event based on this change.
     */
    public void setSelectedItem(Object item, boolean firePropChange)
    {
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++)
        {
            JMenu menu = (JMenu)comps[i];
            Selector s = (Selector)menu.getPopupMenu().getComponent(0);
            if (((item instanceof Component) && (s == item)) ||
                (item.toString().equalsIgnoreCase(menu.getText())))
            {
                updateSelection(menu, firePropChange);
                break;
            }
        }
    }

    /**
     * Gets the currently selected component
     *
     * @return the currently selected component(can be cast to Component class)
     */
    public Object getSelectedItem()
    {
        return selectedMenu.getPopupMenu().getComponent(0);
    }

    /**
     * Gets the menu label associated with the currently selected component.
     *
     * @return the menu label associated with the currently selected component.
     */
    public String getSelectedLabel()
    {
        return selectedMenu.getText();
    }

    /**
     * Get the menu item that is currently selected
     *
     * @return the menu item that is currently selected
     */
    public JMenu getSelectedMenu()
    {
        return selectedMenu;
    }

    /**
     * Gets an array of all components that have been added to this component
     * menu.
     *
     * @return an array of all components that have been added to this
     *         component menu.
     */
    public Component[] getAddedComponents()
    {
        Component[] comp = getComponents();

        for (int i = 0; i < comp.length; i++)
        {
            comp[i] = ((JMenu)comp[i]).getPopupMenu().getComponent(0);
        }

        return comp;
    }

    private void updateSelection(JMenu selectedMenu, boolean firePropChange)
    {
        if (selectedMenu != this.selectedMenu)
        {
            Component oldComponent = (Component)getSelectedItem();
            this.selectedMenu = selectedMenu;
            if (firePropChange)
            {
                firePropertyChange("selectedItem", oldComponent,
                                   getSelectedItem());
            }
        }
    }
}