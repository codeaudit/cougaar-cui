/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.components;

import java.awt.event.*;
import javax.swing.JComboBox;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * This class is a JComboBox that implements the Selector
 * interface.  <BR><BR>
 *
 * This bean has bound property:  "selectedItem"
 */
public class CComboSelector extends JComboBox implements Selector
{
    private Object selectedItem;

    /**
     * Default constructor.  Create a new, empty combo box.
     */
    public CComboSelector()
    {
        super();
        init();
    }

    /**
     * Create a new combo selector filled with the given selection options.
     *
     * @param items array of objects to use as selections.
     */
    public CComboSelector(Object[] items)
    {
        super(items);
        init();
    }

    private void init()
    {
        selectedItem = getSelectedItem();
        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    Object oldSelectedItem = selectedItem;
                    selectedItem = getSelectedItem();
                    if (selectedItem == null)
                    {
                        selectedItem = oldSelectedItem;
                        setSelectedItem(selectedItem);
                    }
                    firePropertyChange("selectedItem", oldSelectedItem,
                                       selectedItem);
                }
            });
    }

    /**
     * Not needed when compiling/running under jdk1.3
     */
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue)
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}