package org.cougaar.lib.uiframework.ui.components;

import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.models.StoplightThresholdModel;

/**
 * A control bean used for selecting the thresholds that designate color
 * changes on a stoplight chart.  The four sliders are used to select
 * min yellow, min green, max green, and max yellow values.
 *
 * This bean has a bounded property:  "thresholds".
 *
 * @see assessment.MThumbSliderThresholdControl
 */
public class CSliderThresholdControl extends CMultipleSliderControl
{
    private static String[] sliderLabels =
        {"Min Yellow", "Min Green", "Max Green", "Max Yellow"};

    /** Needed for tracking old values for bounded thresholds property */
    private StoplightThresholdModel thresholds = new StoplightThresholdModel();

    /**
     * Default constructor.  Creates new slider threshold control with
     * threshold range from 0 to 2.
     */
    public CSliderThresholdControl()
    {
        super(sliderLabels, 0, 2);

        init(0, 2 );
    }

    /**
     * Creates new slider threshold control with threshold range based on
     * given parameters.
     *
     * @param minValue the minimum threshold value
     * @param maxValue the maximum threshold value
     */
    public CSliderThresholdControl(float minValue, float maxValue)
    {
        super(sliderLabels, minValue, maxValue);

        init(minValue, maxValue);
    }

    /**
     * Initialize the slider threshold control
     *
     * @param minValue the minimum threshold value
     * @param maxValue the maximum threshold value
     */
    private void init(float minValue, float maxValue)
    {
        for (int i = 0; i < sliderLabels.length; i++)
        {
            CLabeledSlider slider = getSlider(i);

            // fire property change event when slider is adjusted
            slider.addPropertyChangeListener("value",
                                             new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e)
                    {
                        StoplightThresholdModel newThresholds =getThresholds();
                        if (!thresholds.equals(newThresholds))
                        {
                            firePropertyChange("thresholds", thresholds,
                                               newThresholds);
                            thresholds = newThresholds;
                        }
                    }
                });
        }

        float defaultSeperation = (maxValue-minValue)/(sliderLabels.length+1);
        setThresholds(
            new StoplightThresholdModel(minValue + defaultSeperation,
                                        minValue + defaultSeperation * 2,
                                        minValue + defaultSeperation * 3,
                                        minValue + defaultSeperation * 4));
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
     * Set the selected thresholds.
     *
     * @param tr new thresholds
     */
    public void setThresholds(StoplightThresholdModel tr)
    {
        getSlider(0).setValue(tr.getYellowMin());
        getSlider(1).setValue(tr.getGreenMin());
        getSlider(2).setValue(tr.getGreenMax());
        getSlider(3).setValue(tr.getYellowMax());

        StoplightThresholdModel newThresholds = getThresholds();
        firePropertyChange("thresholds", thresholds, newThresholds);
        thresholds = newThresholds;
    }

    /**
     * Get the selected thresholds
     *
     * @return currently selected thresholds
     */
    public StoplightThresholdModel getThresholds()
    {
        return
          new StoplightThresholdModel(getSlider(0).getValue(),
                                      getSlider(1).getValue(),
                                      getSlider(2).getValue(),
                                      getSlider(3).getValue());
    }


    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Slider Test");
        java.awt.Container c = frame.getContentPane();
        c.setLayout(new java.awt.GridLayout(0, 1));
        c.add(new CSliderThresholdControl(0f, 1f));
        c.add(new CSliderThresholdControl(0f, 200f));
        c.add(new CSliderThresholdControl(200f, 1000f));
        frame.pack();
        frame.setVisible(true);
    }
}