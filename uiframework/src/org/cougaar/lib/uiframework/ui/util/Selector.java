package org.cougaar.lib.uiframework.ui.util;

import java.awt.Component;
import java.beans.PropertyChangeListener;

/**
 * Interface to designate a control that can be used to select a single item
 * (Object) in some way.
 */
public interface Selector
{
    /**
     * Get the item that is currently selected.
     *
     * @return the item that is currently selected
     */
    public Object getSelectedItem();

    /**
     * Set the item to be selected.
     *
     * @param the item to be selected.
     */
    public void setSelectedItem(Object selectedItem);

    /**
     * Add a property change listener that is fired whenever a given property
     * changes.
     *
     * @param propertyName the property to listen for changes to.
     * @param al the new property change listener
     */
    public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener al);

    /**
     * Add a property change listener that is fired whenever a property
     * changes.
     *
     * @param al the new property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener al);

    /**
     * Removes a property change listener that is fired whenever a given
     * property changes.
     *
     * @param propertyName the property to listen for changes to.
     * @param al the existing property change listener
     */
    public void removePropertyChangeListener(String propertyName,
                                             PropertyChangeListener al);

    /**
     * Removes a property change listener that is fired whenever a property
     * changes.
     *
     * @param al the existing property change listener
     */
    public void removePropertyChangeListener(PropertyChangeListener al);
}