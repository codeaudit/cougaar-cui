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

import java.beans.*;
import javax.swing.JFrame;

import org.cougaar.lib.uiframework.ui.models.RangeModel;

/**
 * A control bean used for selecting a range between to floats using sliders.
 * The two sliders are used to select a minimum and maximum value
 *
 * This bean has a bounded property:  "range".
 *
 * @see CMThumbSliderRangeControl
 */
public class CSliderRangeControl extends CMultipleSliderControl
{
    private static String[] sliderLabels = {"From", "To"};

    /** Needed for tracking old values for bounded range property */
    private RangeModel range = new RangeModel(0, 1);

    /**
     * Default constructor.  Creates new slider range control with
     * value range from 0 to 1.
     */
    public CSliderRangeControl()
    {
        super(sliderLabels, 0, 1);

        init();
    }

    /**
     * Creates new slider range control with value range based on
     * given parameters.
     *
     * @param minValue the minimum range value
     * @param maxValue the maximum range value
     */
    public CSliderRangeControl(float minValue, float maxValue)
    {
        super(sliderLabels, minValue, maxValue);

        init();
    }

    /**
     * Initialize the slider range control
     */
    private void init()
    {
        for (int i = 0; i < sliderLabels.length; i++)
        {
            CLabeledSlider slider = getSlider(i);

            // fire property change event when slider is adjusted
            slider.addPropertyChangeListener("value",
                                             new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e)
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

        setRange(range);
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
     * Set the selected range.
     *
     * @param r new range
     */
    public void setRange(RangeModel r)
    {
        getSlider(0).setValue(r.getMin());
        getSlider(1).setValue(r.getMax());

        RangeModel newRange = getRange();
        firePropertyChange("range", range, newRange);
        range = newRange;
    }

    /**
     * Get the selected range
     *
     * @return currently selected range
     */
    public RangeModel getRange()
    {
        return new RangeModel(Math.round(getSlider(0).getValue()),
                              Math.round(getSlider(1).getValue()));
    }


    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Range Slider Test");
        java.awt.Container c = frame.getContentPane();
        c.setLayout(new java.awt.GridLayout(0, 1));
        c.add(new CSliderRangeControl(0f, 1f));
        c.add(new CSliderRangeControl(0f, 200f));
        c.add(new CSliderRangeControl(200f, 1000f));
        frame.pack();
        frame.setVisible(true);
    }
}