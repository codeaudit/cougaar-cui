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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Vector;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.util.Selector;
import org.cougaar.lib.uiframework.ui.util.SliderControl;

/**
 * A UI control bean that is a button that is labeled with the control's
 * current value.  Upon pressing the button the user is presented with a
 * dialog that contains a MThumbSliderRangeControl with which the user can
 * modify the selected range.  Can be configured to use a SliderRangeControl
 * if dynamic Pluggable look and feel is required.
 *
 * This bean has bounded property:  "selectedItem"
 */
public class CRangeButton
    extends CPullrightButton implements Selector, SliderControl
{
    private CRangeSelector rs = null;
    private String prefix;

    /**
     * Default constructor.  Creates new range button with range from 0 to 100.
     */
    public CRangeButton()
    {
        super();
        init("C", 0, 100, false);
    }

    /**
     * Creates new range button based on the given parameters
     *
     * @param prefix the prefix to prepend before range values
     * @param min    the minimum value for the range
     * @param max    the maximum value for the range
     * @param plaf   true if pluggable look and feel is required.
     */
    public CRangeButton(String prefix, int min, int max, boolean plaf)
    {
        super();
        init(prefix, min, max, plaf);
    }

    /**
     * Initialize the range button
     *
     * @param prefix the prefix to prepend before range values
     * @param min    the minimum value for the range
     * @param max    the maximum value for the range
     * @param plaf   true if pluggable look and feel is required.
     */
    private void init(final String prefix, int min, int max, boolean plaf)
    {
        this.prefix = prefix;
        RangeModel range = new RangeModel(min, max);
        rs = new CRangeSelector(plaf, min, max);
        rs.setSelectedItem(range);
        setSelectorControl(rs);

        updateText();
        rs.addPropertyChangeListener("selectedItem",
                                     new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    updateText();
                }
            });
    }

    private void updateText()
    {
        RangeModel r = (RangeModel)rs.getSelectedItem();
        setText(prefix + (r.getMin() > 0 ? "+" : "") + r.getMin() + " to " +
                prefix + (r.getMax() > 0 ? "+" : "") + r.getMax());
    }

    /**
     * Get the minimum value of the range control
     *
     * @return the minimum value of the range control
     */
    public float getMinValue()
    {
        return rs.getMinValue();
    }

    /**
     * Set the minimum value of the range control
     *
     * @param minValue the minimum value of the range control
     */
    public void setMinValue(float minValue)
    {
        rs.setMinValue(minValue);
    }

    /**
     * Get the maximum value of the range control
     *
     * @return the maximum value of the range control
     */
    public float getMaxValue()
    {
        return rs.getMaxValue();
    }

    /**
     * Set the maximum value of the range control
     *
     * @param maxValue the maximum value of the range control
     */
    public void setMaxValue(float maxValue)
    {
        rs.setMaxValue(maxValue);
    }

    /**
     * Adjusts all values such that thumbs are evenly distributed and ordered
     * from first to last.
     */
    public void evenlyDistributeValues()
    {
        rs.evenlyDistributeValues();
    }

    /**
     * Adjusts min and max values to nice, round numbers that divide nicely
     * by 10. (for nice tick labels)
     *
     * @param newMinValue the minimum value that must be selectable on this
     *                    slider
     * @param newMaxValue the maximum value that must be selectable on this
     *                    slider
     * @return a value that represents the decimal shift used to adjust values
     *         (e.g. 0.001, 100, 1000)
     */
    public float roundAndSetSliderRange(float newMinValue, float newMaxValue)
    {
        return rs.roundAndSetSliderRange(newMinValue, newMaxValue);
    }

    // For Testing ...
    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CRangeButton rb = new CRangeButton();
        frame.getContentPane().add(rb, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}