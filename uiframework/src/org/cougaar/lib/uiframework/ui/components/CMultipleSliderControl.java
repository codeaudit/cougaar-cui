package org.cougaar.lib.uiframework.ui.components;

import java.beans.*;
import javax.swing.*;

/**
 * Base class for controls that consist of a stack of sliders.  Slider order is
 * enforced (slider(n) must be less than or equal to slider(n+1)).
 */
public class CMultipleSliderControl extends JPanel
{
    private String[] sliderLabels;

    /**
     * Default contructor.  Creates new slider control using default label
     * and min/max values.  Needed for use as bean.
     */
    public CMultipleSliderControl()
    {
        super();
        init(new String[]{"label 1", "label 2"}, 0, 1);
    }

    /**
     * Creates new slider control using the given labels.
     *
     * @param sliderLabels an array of labels for the sliders
     * @param minValue the minimum value for all sliders
     * @param maxValue the maximum value for all sliders
     */
    public CMultipleSliderControl(String[] sliderLabels, float minValue,
                                  float maxValue)
    {
        super();
        init(sliderLabels, minValue, maxValue);
    }

    private void init(String[] sliderLabels, float minValue, float maxValue)
    {
        this.sliderLabels = sliderLabels;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int labelSize =
            getMaxLabelSize(sliderLabels, new CLabeledSlider("",0,0,0));

        for (int i = 0; i < sliderLabels.length; i++)
        {
            CLabeledSlider slider =
                new CLabeledSlider(sliderLabels[i], labelSize,
                                  minValue, maxValue);
            add(slider);
            slider.addPropertyChangeListener("value",
                                             new OrderChangeListener(i));

            // show ticks on the last slider only
            if ((i + 1) == sliderLabels.length)
            {
                slider.setShowTicks(true);
            }
        }
    }

    /**
     * Get the minimum value of these sliders
     *
     * @return the minimum value of these sliders
     */
    public float getMinValue()
    {
        return getSlider(0).getMinValue();
    }

    /**
     * Set the minimum value of these sliders
     *
     * @param minValue the minimum value of these sliders
     */
    public void setMinValue(float minValue)
    {
        for (int i = 0; i < sliderLabels.length; i++)
        {
            getSlider(i).setMinValue(minValue);
        }
    }

    /**
     * Get the maximum value of these sliders
     *
     * @return the maximum value of these sliders
     */
    public float getMaxValue()
    {
        return getSlider(0).getMaxValue();
    }

    /**
     * Set the maximum value of these sliders
     *
     * @param maxValue the maximum value of these sliders
     */
    public void setMaxValue(float maxValue)
    {
        for (int i = 0; i < sliderLabels.length; i++)
        {
            getSlider(i).setMaxValue(maxValue);
        }
    }

    /**
     * When look and feel or theme is changed, this method is called.  It will
     * relayout this component with new label size.
     */
    public void updateUI()
    {
        super.updateUI();

        if (sliderLabels != null)
        {
            for (int i=0; i < sliderLabels.length; i++)
            {
                SwingUtilities.updateComponentTreeUI(getSlider(i));
            }
            int labelSize = getMaxLabelSize(sliderLabels, getSlider(0));
            for (int i=0; i < sliderLabels.length; i++)
            {
                getSlider(i).setLabelWidth(labelSize);
            }
        }
    }

    /**
     * Get a reference to slider at given index
     *
     * @param index index of a slider
     * @return reference to slider at given index
     */
    protected CLabeledSlider getSlider(int index)
    {
        return (CLabeledSlider)getComponent(index);
    }

    private static int getMaxLabelSize(String[] labels, CLabeledSlider ls)
    {
        int maxWidth = 0;
        for (int i=0; i < labels.length; i++)
        {
            int fontWidth = ls.getMinimumLabelWidth(labels[i]);
            maxWidth = Math.max(fontWidth, maxWidth);
        }
        return maxWidth;
    }

    /**
     * Private class used to ensure that the user doesn't select invalid
     * values (slider(n) must be less than or equal to slider(n+1))
     */
    private class OrderChangeListener implements PropertyChangeListener
    {
        int myPosition = 0;

        public OrderChangeListener(int myPosition)
        {
            this.myPosition = myPosition;
        }

        public void propertyChange(PropertyChangeEvent e)
        {
            float myValue = ((CLabeledSlider)getComponent(myPosition)).getValue();

            for (int i = 0; i < myPosition; i++)
            {
                CLabeledSlider otherModel = (CLabeledSlider)getComponent(i);
                if (otherModel.getValue() > myValue)
                {
                    otherModel.setValue(myValue);
                }
            }

            for (int i = myPosition + 1; i < getComponentCount(); i++)
            {
                CLabeledSlider otherModel = (CLabeledSlider)getComponent(i);
                if (otherModel.getValue() < myValue)
                {
                    otherModel.setValue(myValue);
                }
            }
        }
    }
}