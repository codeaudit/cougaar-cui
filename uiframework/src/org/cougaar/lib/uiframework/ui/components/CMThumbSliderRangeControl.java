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

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.components.mthumbslider.COrderedLabeledMThumbSlider;
import org.cougaar.lib.uiframework.ui.models.RangeModel;

/**
 * A two thumbed slider bean that is used for selecting a range between two
 * values.  Area between thumbs that represents the selected range is
 * highlighted red.
 *
 * This bean has a bounded property:  "range"
 */
public class CMThumbSliderRangeControl extends COrderedLabeledMThumbSlider
{
    /** Needed for tracking old values for bounded thresholds property */
    private RangeModel range = new RangeModel(0, 30);

    private static final int NUMBER_OF_THUMBS = 2;

    /**
     * Default constructor.  Creates new range slider with minimum value 0 and
     * maximum value 30.
     */
    public CMThumbSliderRangeControl()
    {
        super(NUMBER_OF_THUMBS, 0, 30);

        init(0, 30);
    }

    /**
     * Creates a new range slider with given minimum and maximum values.
     *
     * @param minValue minimum setting for range
     * @param maxValue maximum value for range
     */
    public CMThumbSliderRangeControl(float minValue, float maxValue)
    {
        super(NUMBER_OF_THUMBS, minValue, maxValue);

        init(minValue, maxValue);
    }

    /**
     * Initialize range control.
     *
     * @param minValue minimum setting for range
     * @param maxValue maximum value for range
     */
    private void init(float minValue, float maxValue)
    {
        slider.setFillColorAt(Color.red, 1);

        setRange(range);

        slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e)
                {
                    RangeModel newRange = getRange();
                    if (!range.equals(newRange))
                    {
                        firePropertyChange("range", range, newRange);
                        range = newRange;
                    }
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

    /**
     * Set selected range
     *
     * @param range new range
     */
    public void setRange(RangeModel range)
    {
        slider.setValueAt(toSlider(range.getFMin()), 0);
        slider.setValueAt(toSlider(range.getFMax()), 1);

        RangeModel newRange = getRange();
        firePropertyChange("range", range, newRange);
        range = newRange;
    }

    /**
     * Get selected range
     *
     * @return the currently selected range.
     */
    public RangeModel getRange()
    {
        return new RangeModel(fromSlider(slider.getValueAt(0)), fromSlider(slider.getValueAt(1)));
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CMThumbSliderRangeControl rc = new CMThumbSliderRangeControl();
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setSize(400, 100);
        frame.setVisible(true);
    }

}