package org.cougaar.lib.uiframework.ui.models;

/**
 * A simple range comprised of a minimum and maximum integer.
 */
public class RangeModel
{
    private int min = 0;
    private int max = 0;

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
    }

    /**
     * Set the minimum value for range
     *
     * @param min the new minimum value for range
     */
    public void setMin(int min)
    {
        this.min = min;
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
     * represent range as string
     *
     * @return string representation of range
     */
    public String toString()
    {
        return (String.valueOf(min) + "-" + String.valueOf(max));
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
            if ((r.min == min) && (r.max == max))
            {
                return true;
            }
        }
        return false;
    }
}