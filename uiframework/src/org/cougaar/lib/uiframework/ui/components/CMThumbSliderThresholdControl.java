package org.cougaar.lib.uiframework.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.components.mthumbslider.COrderedLabeledMThumbSlider;
import org.cougaar.lib.uiframework.ui.models.StoplightThresholdModel;

/**
 * Four thumbed slider control bean used for selecting the thresholds that
 * designate color changes on a stoplight chart.  The four sliders are used
 * to select min yellow, min green, max green, and max yellow values.  The
 * area between the thumbs that represents a particular range that should be
 * encoded in a single color is colored with that color.<BR><BR>
 *
 * ---red---|----yellow----|----green----|-----yellow----|----red----<BR><BR>
 *
 * This bean has a bounded property:  "thresholds".
 */
public class CMThumbSliderThresholdControl extends COrderedLabeledMThumbSlider
{
    /** Needed for tracking old values for bounded thresholds property */
    private StoplightThresholdModel thresholds = new StoplightThresholdModel();

    private static final int NUMBER_OF_THUMBS = 4;

    /**
     * Default constructor.  Creates a new threshold slider with a minimum
     * value of 0 and a maximum value of 2.
     */
    public CMThumbSliderThresholdControl()
    {
        super(NUMBER_OF_THUMBS, 0, 2);

        init(0, 2);
    }

    /**
     * Creates a new threshold slider with given minimum and maximum values.
     *
     * @param minValue minimum value for thresholds
     * @param maxValue minimum value for thresholds
     */
    public CMThumbSliderThresholdControl(float minValue, float maxValue)
    {
        super(NUMBER_OF_THUMBS, minValue, maxValue);

        init(minValue, maxValue);
    }

    /**
     * Initialize the threshold slider
     *
     * @param minValue minimum value for thresholds
     * @param maxValue minimum value for thresholds
     */
    private void init(float minValue, float maxValue)
    {
        slider.setFillColorAt(Color.red, 0);
        slider.setFillColorAt(Color.yellow, 1);
        slider.setFillColorAt(Color.green, 2);
        slider.setFillColorAt(Color.yellow, 3);
        slider.setTrackFillColor(Color.red);

        slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e)
                {
                    StoplightThresholdModel newThresholds = getThresholds();
                    if (!thresholds.equals(newThresholds))
                    {
                        firePropertyChange("thresholds", thresholds,
                                           newThresholds);
                        thresholds = newThresholds;
                    }
                }
            });

        evenlyDistributeValues();
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
        slider.setValueAt(toSlider(tr.getYellowMin()), 0);
        slider.setValueAt(toSlider(tr.getGreenMin()), 1);
        slider.setValueAt(toSlider(tr.getGreenMax()), 2);
        slider.setValueAt(toSlider(tr.getYellowMax()), 3);

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
        return new StoplightThresholdModel(fromSlider(slider.getValueAt(0)),
                                           fromSlider(slider.getValueAt(1)),
                                           fromSlider(slider.getValueAt(2)),
                                           fromSlider(slider.getValueAt(3)));
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CMThumbSliderThresholdControl tc = new CMThumbSliderThresholdControl();
        frame.getContentPane().add(tc, BorderLayout.CENTER);
        frame.setSize(400, 100);
        frame.setVisible(true);
    }

}