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
package org.cougaar.lib.uiframework.ui.components;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * A UI control used to select features of a graph.  Currently only supports
 * toggling the show grid option.
 */
public class CGraphFeatureSelectionControl extends JPanel
{
    /** the checkbox used to get user input on whether to show grid. */
    private JCheckBox showGridCB = new JCheckBox("Show Grid");

    /** default constructor.  Will create new control. */
    public CGraphFeatureSelectionControl()
    {
        super();

        add(showGridCB);
    }

    /**
     * Set selected state of show grid checkbox.
     *
     * @param showGrid new state of show grid checkbox.
     */
    public void setShowGrid(boolean showGrid)
    {
        showGridCB.setSelected(showGrid);
    }

    /**
     * Get selected state of show grid checkbox.
     *
     * @return the state of show grid checkbox.
     */
    public boolean getShowGrid()
    {
        return showGridCB.isSelected();
    }

    /**
     * Add action listener that fires whenever any graph feature is changed
     *
     * @param al action listener to add
     */
    public void addActionListener(ActionListener al)
    {
        showGridCB.addActionListener(al);
    }

    /**
     * Remove action listener that fires whenever any graph feature is changed
     *
     * @param al action listener to remove
     */
    public void removeActionListener(ActionListener al)
    {
        showGridCB.removeActionListener(al);
    }
}