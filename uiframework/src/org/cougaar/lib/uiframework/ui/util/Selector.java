/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.util;

import java.awt.Component;
import java.awt.event.ActionListener;
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

    /**
     * Adds an action listener that is fired whenever the user attempts to make
     * a selection (even if the selectedItem property did not change).
     *
     * @param al the new action listener
     */
     public void addActionListener(ActionListener al);

    /**
     * Removes a registered action listener.
     *
     * @param al the existing action listener
     */
     public void removeActionListener(ActionListener al);
}