/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components.mthumbslider;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.cougaar.lib.uiframework.ui.util.SliderControl;

/**
  * This class is used to create muliple thumbed sliders whose value order
  * is enforced (slider(n) must be less than or equal to slider(n+1)).
  * Also, current value labels are added that float above each slider thumb.
  */
public class COrderedLabeledMThumbSlider
    extends JPanel implements SliderControl
{
    protected static int FIDELITY = 1000;
    protected static final int MAJOR_TICK_SPACING = FIDELITY/5;
    protected int numThumbs = 0;
    protected float minValue = 0f;
    protected float maxValue = 0f;
    protected float unit =  0f;

    protected CMThumbSlider slider;
    protected DecimalFormat labelFormat;

    /**
     * Default constructor.  Create a new mulitple thumbed slider with 5
     * thumbs, min value of 0, max value of 1, and unique track colors between
     * each thumb.
     */
    public COrderedLabeledMThumbSlider()
    {
        super(new BorderLayout());

        this.numThumbs = 5;
        initialize(0, 1);

        slider.setFillColorAt(Color.red, 0);
        slider.setFillColorAt(Color.orange, 1);
        slider.setFillColorAt(Color.yellow, 2);
        slider.setFillColorAt(Color.green, 3);
        slider.setFillColorAt(Color.blue, 4);
        slider.setTrackFillColor(Color.magenta);
        evenlyDistributeValues();
    }

    /**
     * Create a new mulitple thumbed slider with the given number of thumbs
     *
     * @param numThumb the number of thumb for slider control.
     * @param minValue the minimum value for this slider
     * @param maxValue the maximum value for this slider
     */
    public COrderedLabeledMThumbSlider(int numThumbs, float minValue,
                                       float maxValue)
    {
        super(new BorderLayout());

        this.numThumbs = numThumbs;
        initialize(minValue, maxValue);
    }

    /**
     * Get the slider control contained by this labeled panel
     *
     * @return the slider control contained by this labeled panel
     */
    public CMThumbSlider getSlider()
    {
        return slider;
    }

    /**
     * Called to initialize component with minimum and maximum range values.
     *
     * @param minValue the minimum value for this slider
     * @param maxValue the maximum value for this slider
     */
    protected void initialize(float minValue, float maxValue)
    {
        slider = new CMThumbSlider(numThumbs);
        slider.setMaximum(FIDELITY);
        slider.setOpaque(false);
        slider.putClientProperty( "JSlider.isFilled", Boolean.TRUE );
        add(slider, BorderLayout.CENTER);

        setSliderRange(minValue, maxValue);

        for (int i = 0; i < numThumbs; i++)
        {
            BoundedRangeModel model = slider.getModelAt(i);
            model.addChangeListener(new OrderChangeListener(i));
        }

        slider.setMinorTickSpacing(MAJOR_TICK_SPACING / 2);
        slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e)
                {
                    validate();
                    repaint();
                }
            });

        adjustValueLabelHeight();
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
        float difference = newMaxValue - newMinValue;
        double log10 = Math.log(10);
        if (difference == 0)
        {
            float adjustment =
                (float)Math.pow(10, Math.log((double)newMaxValue)/log10);
            newMaxValue += adjustment;
            newMinValue -= adjustment;
            difference = newMaxValue - newMinValue;
        }
        float shift =
            (float)Math.pow(0.1, Math.floor(Math.log(difference)/log10));
        newMinValue = (float)(Math.floor(newMinValue * shift) / shift);
        newMaxValue = (float)(Math.ceil(newMaxValue * shift) / shift);
        setSliderRange(newMinValue, newMaxValue);
        return shift;
    }

    /**
     * Adjusts all values such that thumbs are evenly distributed and ordered
     * from first to last.
     */
    public void evenlyDistributeValues()
    {
        float defaultSeperation = (maxValue - minValue)/(numThumbs + 1);
        for (int i = 0; i < numThumbs; i++)
        {
            slider.setValueAt(
                toSlider(minValue + defaultSeperation * (i + 1)), i);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Get the minimum value of this slider
     *
     * @return the minimum value of this slider
     */
    public float getMinValue()
    {
        return minValue;
    }

    /**
     * Set the minimum value of this slider
     *
     * @param minValue the minimum value of this slider
     */
    public void setMinValue(float minValue)
    {
        setSliderRange(minValue, maxValue);
    }

    /**
     * Get the maximum value of this slider
     *
     * @return the maximum value of this slider
     */
    public float getMaxValue()
    {
        return maxValue;
    }

    /**
     * Set the maximum value of this slider
     *
     * @param maxValue the maximum value of this slider
     */
    public void setMaxValue(float maxValue)
    {
        setSliderRange(minValue, maxValue);
    }

    /**
     * Set the minimum and maximum value of this slider.
     *
     * @param minValue the new minimum value of this slider
     * @param maxValue the new maximum value of this slider
     */
    public void setSliderRange(float minValue, float maxValue)
    {
        // Try to maintain the same values if possible
        Vector currentValues = new Vector();
        for (int i = 0; i < numThumbs; i++)
        {
            currentValues.add(new Float(fromSlider(slider.getValueAt(i))));
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
        unit = (maxValue - minValue) / FIDELITY;

        if (Math.abs(maxValue) > 10)
        {
            labelFormat = new DecimalFormat("####");
        }
        else
        {
            labelFormat = new DecimalFormat("##.##");
        }

        int sliderMin = toSlider(minValue);
        int sliderMax = toSlider(maxValue);
        for (int i = 0; i < numThumbs; i++)
        {
            BoundedRangeModel model = slider.getModelAt(i);
            model.setMaximum(sliderMax);
            model.setMinimum(sliderMin);
        }

        Hashtable valueLabels = new Hashtable();
        for (int i = 0; i <= FIDELITY; i += MAJOR_TICK_SPACING)
        {
            valueLabels.put(new Integer(i),
                            new JLabel(labelFormat.format(fromSlider(i))));
        }
        slider.setLabelTable(valueLabels);

        // Set sliders to old current values
        for (int i = 0; i < currentValues.size(); i++)
        {
            Number currentValue = (Number)currentValues.elementAt(i);
            slider.setValueAt(toSlider(currentValue.floatValue()), i);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * When look and feel or theme is changed, this method is called.  It
     * resizes the component based on new font sizes.
     */
    public void updateUI()
    {
        super.updateUI();

        adjustValueLabelHeight();
    }

    protected void adjustValueLabelHeight()
    {
      if (dynamicLabelsVisible)
      {
        if (dynamicLabelStrut != null)
        {
          remove(dynamicLabelStrut);
        }

        // Create space for value labels in north quad. of component
        int fontHeight = getFontMetrics(MetalLookAndFeel.getSystemTextFont()).getHeight();
        dynamicLabelStrut = Box.createVerticalStrut(fontHeight);
        add(dynamicLabelStrut, BorderLayout.NORTH);
      }
      else if (dynamicLabelStrut != null)
      {
        remove(dynamicLabelStrut);
      }
    }

    protected Component dynamicLabelStrut = null;

    protected boolean dynamicLabelsVisible = true;

    public void setDynamicLabelsVisible(boolean visible)
    {
      dynamicLabelsVisible = visible;
      adjustValueLabelHeight();
    }

    public void setDrawTickLabels(boolean draw)
    {
      slider.setPaintLabels(draw);
      slider.setPaintTicks(draw);
    }

    /**
     * Paints floating value labels over thumbs of slider.
     *
     * Also includes a workaround for an appearent Swing bug.  Without this
     * workaround, JSlider will not appear when placed in some types of top
     * level containers (e.g. PopupMenus).  Only first call to this method will
     * invoke workaround functionality.
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setFont(MetalLookAndFeel.getSystemTextFont());
        FontMetrics fm = getFontMetrics(g.getFont());

        if (dynamicLabelsVisible)
        {
        // paint dynamic value labels on component
        for (int i = 0; i < numThumbs; i++)
        {
            String label =labelFormat.format(fromSlider(slider.getValueAt(i)));
            int labelWidth = fm.stringWidth(label);
            int borderOffset = (getBorder() == null) ? 0 :
                                getBorder().getBorderInsets(this).left;
            int thumbXLoc = slider.getThumbXLoc(i) + borderOffset;
            g.drawString(label, thumbXLoc - (labelWidth / 2),
                         (int)slider.getLocation().getY()-5);
        }
        }

        // Swing bug workaround
        if (updateUIAfterPaint)
        {
            SwingUtilities.updateComponentTreeUI(this);
            updateUIAfterPaint = false;
        }
    }
    protected boolean updateUIAfterPaint = true;

    /**
     * get value of thumb at given index.
     *
     * @param index index of thumb
     * @return value of thumb at given index
     */
    public float getValueAt(int index)
    {
        return fromSlider(slider.getValueAt(index));
    }

    /**
     * set value of thumb at given index.
     *
     * @param f new value for thumb at given index
     * @param index index of thumb
    */
    public void setValueAt(float f, int index)
    {
        slider.setValueAt(toSlider(f), index);
    }

    /**
     * Translates from float value to slider's integer value.
     *
     * @param f "value" float value
     * @return slider's integer value
     */
    public int toSlider(float f)
    {
        return Math.round((f - minValue) / unit);
    }

    /**
     * Translates from slider's integer value to float value.
     *
     * @param i slider's integer value
     * @return "value" float value
     */
    public float fromSlider(int i)
    {
        return minValue + (unit * i);
    }

    /**
     * Private class used to ensure that the user doesn't select invalid
     * values (slider(n) must be less than or equal to slider(n+1))
     */
    class OrderChangeListener implements ChangeListener
    {
        int myPosition = 0;

        public OrderChangeListener(int myPosition)
        {
            this.myPosition = myPosition;
        }

        public void stateChanged(ChangeEvent e)
        {
            int myValue = slider.getModelAt(myPosition).getValue();

            for (int i = 0; i < myPosition; i++)
            {
                BoundedRangeModel otherModel = slider.getModelAt(i);
                if (otherModel.getValue() > myValue)
                {
                    otherModel.setValue(myValue);
                }
            }

            for (int i = myPosition + 1; i < numThumbs; i++)
            {
                BoundedRangeModel otherModel = slider.getModelAt(i);
                if (otherModel.getValue() < myValue)
                {
                    otherModel.setValue(myValue);
                }
            }
        }
    }
}
