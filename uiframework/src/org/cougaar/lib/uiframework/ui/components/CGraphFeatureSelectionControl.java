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