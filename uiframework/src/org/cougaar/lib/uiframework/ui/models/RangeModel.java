/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.models;

/**
 * A simple range comprised of a minimum and maximum integer.
 */
public class RangeModel
{
    private int min = 0;
    private int max = 0;

    private float fMin = 0.0f;
    private float fMax = 0.0f;

    /**
     * Create a new range object
     *
     * @param min the minimum value for range.
     * @param max the maximum value for range.
     */
    public RangeModel(int min, int max)
    {
        this.min = min;
        this.max = max;
        fMin = (float)min;
        fMax = (float)max;
    }

    /**
     * Create a new range object
     *
     * @param min the minimum value for range.
     * @param max the maximum value for range.
     */
    public RangeModel(float min, float max)
    {
        fMin = min;
        fMax = max;
        this.min = Math.round(min);
        this.max = Math.round(max);
    }

    /**
     * Set the minimum value for range
     *
     * @param min the new minimum value for range
     */
    public void setMin(int min)
    {
        this.min = min;
        fMin = (float)min;
    }

    /**
     * Get the minimum value for range
     *
     * @return the current minimum value for range
     */
    public int getMin()
    {
        return min;
    }

    /**
     * Set the maximum value for range
     *
     * @param min the new maximum value for range
     */
    public void setMax(int max)
    {
        this.max = max;
        fMax = (float)max;
    }

    /**
     * Get the maximum value for range
     *
     * @return the current maximum value for range
     */
     public int getMax()
    {
        return max;
    }

    /**
     * Set the minimum value for range
     *
     * @param min the new minimum value for range
     */
    public void setFMin(float min)
    {
        fMin = min;
        this.min = Math.round(min);
    }

    /**
     * Get the minimum value for range
     *
     * @return the current minimum value for range
     */
    public float getFMin()
    {
        return(fMin);
    }

    /**
     * Set the maximum value for range
     *
     * @param min the new maximum value for range
     */
    public void setFMax(float max)
    {
        fMax = max;
        this.max = (int)max;
        this.max = Math.round(max);
    }

    /**
     * Get the maximum value for range
     *
     * @return the current maximum value for range
     */
     public float getFMax()
    {
        return(fMax);
    }

    /**
     * represent range as string
     *
     * @return string representation of range
     */
    public String toString()
    {
        return (String.valueOf(fMin) + "-" + String.valueOf(fMax));
    }

    /**
     * check for equality between ranges
     *
     * @param o object to compare with range
     * @return true if range equals given object
     */
    public boolean equals(Object o)
    {
        if (o instanceof RangeModel)
        {
            RangeModel r = (RangeModel)o;
            if ((r.fMin == fMin) && (r.fMax == fMax))
            {
                return true;
            }
        }
        return false;
    }
}
