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
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.util.Selector;
import org.cougaar.lib.uiframework.ui.util.SliderControl;

/**
 * A UI control bean that is a range selection control that implements the
 * Selector interface.
 *
 * This bean has bounded property:  "selectedItem" (of type RangeModel)
 *
 */
public class CRangeSelector extends JPanel implements Selector, SliderControl
{
    private RangeModel range = null;
    private JButton okButton;
    private SliderControl rangeControl;
    private boolean plaf = false;

    /**
     * Default constructor.  Create a new Range Selector that doesn't support
     * pluggable look and feel.
     */
    public CRangeSelector()
    {
        super(new BorderLayout());
        init(0, 1);
    }

    /**
     * Create a new Range Selector
     *
     * @param plaf true if pluggable look and feel must be supported
     */
    public CRangeSelector(boolean plaf)
    {
        super(new BorderLayout());
        this.plaf = plaf;
        init(0, 1);
    }

    /**
     * Create a new Range Selector
     *
     * @param plaf true if pluggable look and feel must be supported
     * @param minRangeValue minimum value for range
     * @param maxRangeValue maximum value for range
     */
    public CRangeSelector(boolean plaf, float minRangeValue,
                          float maxRangeValue)
    {
        this.plaf = plaf;
        init(minRangeValue, maxRangeValue);
    }

    /**
     * Get the minimum value of the range control
     *
     * @return the minimum value of the range control
     */
    public float getMinValue()
    {
        return rangeControl.getMinValue();
    }

    /**
     * Set the minimum value of the range control
     *
     * @param minValue the minimum value of the range control
     */
    public void setMinValue(float minValue)
    {
        rangeControl.setMinValue(minValue);
    }

    /**
     * Get the maximum value of the range control
     *
     * @return the maximum value of the range control
     */
    public float getMaxValue()
    {
        return rangeControl.getMaxValue();
    }

    /**
     * Set the maximum value of the range control
     *
     * @param maxValue the maximum value of the range control
     */
    public void setMaxValue(float maxValue)
    {
        rangeControl.setMaxValue(maxValue);
    }

    /**
     * Adjusts all values such that thumbs are evenly distributed and ordered
     * from first to last.
     */
    public void evenlyDistributeValues()
    {
        rangeControl.evenlyDistributeValues();
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
        return rangeControl.roundAndSetSliderRange(newMinValue, newMaxValue);
    }

    private void init(float minRangeValue, float maxRangeValue)
    {
        setLayout(new BorderLayout(10, 10));
        if (plaf)
        {
            rangeControl =
                new CSliderRangeControl(minRangeValue, maxRangeValue);
        }
        else
        {
            rangeControl =
                new CMThumbSliderRangeControl(minRangeValue, maxRangeValue);
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        okButton = new JButton("OK");
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createGlue());
        add(new JLabel("Set New Range"), BorderLayout.NORTH);
        add((JComponent)rangeControl, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        updateRange();
        resetSize();

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    updateRange();
                }
            });
    }

    /**
     * When look and feel or theme is changed, this method is called.  It
     * ensures that the control size is updated as well.
     */
     public void updateUI()
    {
        super.updateUI();

        resetSize();
    }

    /**
     * Set control to new range
     *
     * @param newSelection new range for control (must be of type RangeModel)
     */
    public void setSelectedItem(Object newSelection)
    {
        if (newSelection instanceof RangeModel)
        {
            RangeModel newRange = (RangeModel)newSelection;
            if (plaf)
            {
                ((CSliderRangeControl)rangeControl).setRange(newRange);
            }
            else
            {
                ((CMThumbSliderRangeControl)rangeControl).setRange(newRange);
            }
            updateRange();
        }
    }

    /**
     * Get currently selected range
     *
     * @return currently selected range (can be cast to type RangeModel)
     */
    public Object getSelectedItem()
    {
        return range;
    }

    private void resetSize()
    {
        if (rangeControl != null)
        {
            ((JComponent)rangeControl).setPreferredSize(null);
            ((JComponent)rangeControl).setPreferredSize(
                new Dimension(500, ((JComponent)rangeControl).
                                    getPreferredSize().height));
        }
    }

    /**
     * Adds an action listener that is fired whenever the user attempts to make
     * a selection (even if the selectedItem property did not change).
     *
     * @param al the new action listener
     */
     public void addActionListener(ActionListener al)
     {
        okButton.addActionListener(al);
     }

    /**
     * Removes a registered action listener.
     *
     * @param al the existing action listener
     */
    public void removeActionListener(ActionListener al)
    {
        okButton.removeActionListener(al);
    }

    private void updateRange()
    {
        RangeModel oldRange = range;
        if (plaf)
        {
            range = ((CSliderRangeControl)rangeControl).getRange();
        }
        else
        {
            range = ((CMThumbSliderRangeControl)rangeControl).getRange();
        }
        firePropertyChange("selectedItem", oldRange, range);
    }
}