package org.cougaar.lib.uiframework.ui.util;

public interface SliderControl
{
    /**
     * Get the minimum value of the slider control
     *
     * @return the minimum value of the slider control
     */
    public float getMinValue();

    /**
     * Set the minimum value of the slider control
     *
     * @param minValue the minimum value of the slider control
     */
    public void setMinValue(float minValue);

    /**
     * Get the maximum value of the slider control
     *
     * @return the maximum value of the slider control
     */
    public float getMaxValue();

    /**
     * Set the maximum value of the slider control
     *
     * @param maxValue the maximum value of the slider control
     */
    public void setMaxValue(float maxValue);

    /**
     * Adjusts all values such that thumbs are evenly distributed and ordered
     * from first to last.
     */
    public void evenlyDistributeValues();

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
    public float roundAndSetSliderRange(float newMinValue, float newMaxValue);
}