package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.beans.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A bean that creates a swing slider with a static label and a dynamic label.
 * The dynamic label shows the current value selected.  This slider supports
 * setting of floating point values as well as integer values.
 *
 * This bean has a bounded property: "value".
 */
public class CLabeledSlider extends JPanel
{
    /** the spacing between major tick marks on slider */
    private static final int MAJOR_TICK_SPACING = 10;

    /** the static label */
    private JLabel label;

    /** the slider */
    private JSlider slider;

    /** panel that holds the static and dynamic labels */
    private JPanel labelPanel;

    /** format used to format the dynamic value label */
    private DecimalFormat labelFormat;

    /** minimum value of slider */
    private float minValue = 0f;

    /** the floating point value that corresponds to 1 unit on the slider */
    private float unit =  0f;

    /**
     * default constructor.  Creates new labeled slider with "No Name" as label
     * and range from 0 to 2.
     */
    public CLabeledSlider()
    {
        super(new BorderLayout());

        init("No Name", 100, 0, 2);
    }

    /**
     * Creates new labeled slider.
     *
     * @param labelString the static label for the slider
     * @param labelWidth the width of the area to reserve for the label.
     *                   Needed for lining up the labeled sliders on top of
     *                   each other.
     * @param minValue   The minimum value of the slider
     * @param maxValue   The maximum value of the slider
     */
    public CLabeledSlider(String labelString, int labelWidth, float minValue,
                         float maxValue)
    {
        super(new BorderLayout());

        init(labelString, labelWidth, minValue, maxValue);
    }

    /**
     * Sets the static label of the slider
     *
     * @param labelString new static label
     */
    public void setLabel(String labelString)
    {
        label.setText(labelString);
    }

    /**
     * Sets the width of the static label
     *
     * @param labelWidth the width of the area to reserve for the label.
     *                   Needed for lining up the labeled sliders on top of
     *                   each other.
     */
    public void setLabelWidth(int labelWidth)
    {
        labelPanel.setPreferredSize(null);
        labelPanel.setPreferredSize(
            new Dimension(labelWidth,
                          (int)labelPanel.getPreferredSize().getHeight()));
    }

    /**
     * Calculate the minimum label width for given string
     *
     * @param labelString label to calculate with for
     * @return the minimum width for this label
     */
     public int getMinimumLabelWidth(String labelString)
     {
        int minLabelWidth =
            getFontMetrics(label.getFont()).stringWidth(labelString+"00000");
        return minLabelWidth;
     }

    /**
     * Gets the static label of the slider
     *
     * @return labelString the static label
     */
    public String getLabel()
    {
        return label.getText();
    }

    /**
     * Initializes new labeled slider.
     *
     * @param labelString the static label for the slider
     * @param labelWidth the width of the area to reserve for the label.
     *                   Needed for lining up the labeled sliders on top of
     *                   each other.
     * @param minValue   The minimum value of the slider
     * @param maxValue   The maximum value of the slider
     */
    private void init(String labelString, int labelWidth, float minValue,
                      float maxValue)
    {
        this.minValue = minValue;
        unit = (maxValue - minValue) / 100f;

        if (Math.abs(maxValue) > 10)
        {
            labelFormat = new DecimalFormat("####");
        }
        else
        {
            labelFormat = new DecimalFormat("##.##");
        }

        labelPanel = new JPanel(new BorderLayout());
        label = new JLabel(labelString + ": ");
        label.setVerticalAlignment(JLabel.TOP);
        labelPanel.add(label, BorderLayout.WEST);
        final JLabel valueLabel = new JLabel();
        labelPanel.add(valueLabel, BorderLayout.CENTER);
        setLabelWidth(labelWidth);
        valueLabel.setVerticalAlignment(JLabel.TOP);
        valueLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(labelPanel, BorderLayout.WEST);

        slider = new JSlider(0, 100);
        Hashtable valueLabels = new Hashtable();
        for (int i = 0; i <= 100; i += MAJOR_TICK_SPACING)
        {
            valueLabels.put(new Integer(i),
                            new JLabel(labelFormat.format(fromSlider(i))));
        }
        slider.setLabelTable(valueLabels);
        add(slider, BorderLayout.CENTER);

        valueLabel.setText(getStringValue());
        slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e)
                {
                    float oldValue = Float.parseFloat(valueLabel.getText());
                    valueLabel.setText(getStringValue());
                    LabeledSlider:firePropertyChange("value", oldValue,
                                                     getValue());
                }
            });
    }

    /**
     * Gets formated string representation of the value of slider
     *
     * return string representation of the value of slider
     */
    private String getStringValue()
    {
        return labelFormat.format(getValue());
    }

    /**
     * Show labeled tick marks under slider.
     */
    public void showTicks()
    {
        showTicks(slider);
    }

    /**
     * Returns the value at which the slider is currently set.
     *
     * return the value at which the slider is currently set
     */
    public float getValue()
    {
        return fromSlider(slider.getValue());
    }

    /**
     * Sets the value of the slider
     *
     * @param value the new value for the slider.
     */
    public void setValue(float value)
    {
        float oldValue = getValue();
        slider.setValue(toSlider(value));
        firePropertyChange("value", oldValue, getValue());
    }

    /**
     * Adds a change listener to the slider
     *
     * @param changeListener change listener to add to slider
     */
    public void addChangeListener(ChangeListener changeListener)
    {
        slider.addChangeListener(changeListener);
    }

    /**
     * Remove a change listener from the slider
     *
     * @param changeListener change listener to remove from slider
     */
    public void removeChangeListener(ChangeListener changeListener)
    {
        slider.removeChangeListener(changeListener);
    }

    /**
     * Configures the slider to show it's tick marks
     *
     * @param slider the slider to configure
     */
    private static void showTicks(JSlider slider)
    {
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
    }

    /**
     * Translates from float value to slider's integer value.
     *
     * @param f "value" float value
     * @return slider's integer value
     */
    private int toSlider(float f)
    {
        return (int)((f - minValue) / unit);
    }

    /**
     * Translates from slider's integer value to float value.
     *
     * @param i slider's integer value
     * @return "value" float value
     */
    private float fromSlider(int i)
    {
        return minValue + (unit * i);
    }

    /**
     * main for unit testing
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Slider Test");
        Container c = frame.getContentPane();
        c.setLayout(new GridLayout(0, 1));
        c.add(new CLabeledSlider("Test", 80, 0f, 1f));
        c.add(new CLabeledSlider("Test", 80, 0f, 200f));
        c.add(new CLabeledSlider("Test", 80, 200f, 1000f));
        frame.pack();
        frame.setVisible(true);
    }
}