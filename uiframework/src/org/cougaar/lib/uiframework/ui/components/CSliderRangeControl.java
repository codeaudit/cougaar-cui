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
 * @see assessment.MThumbSliderRangeControl
 */
public class CSliderRangeControl extends CMultipleSliderControl
{
    private static String[] sliderLabels = {"From", "To"};

    /** Needed for tracking old values for bounded range property */
    private RangeModel range = new RangeModel(0, 30);

    /**
     * Default constructor.  Creates new slider range control with
     * value range from 0 to 2.
     */
    public CSliderRangeControl()
    {
        super(sliderLabels, 0, 30);

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
        return new RangeModel((int)getSlider(0).getValue(),
                              (int)getSlider(1).getValue());
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