package org.cougaar.lib.uiframework.ui.components.mthumbslider;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
  * This class is used to create muliple thumbed sliders whose value order
  * is enforced (slider(n) must be less than or equal to slider(n+1)).
  * Also, current value labels are added that float above each slider thumb.
  */
public class COrderedLabeledMThumbSlider extends JPanel
{
    private static final int MAJOR_TICK_SPACING = 10;
    private int numThumbs = 0;
    private float minValue = 0f;
    private float unit =  0f;

    protected CMThumbSlider slider;
    private DecimalFormat labelFormat;

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

        slider = new CMThumbSlider(numThumbs);
        slider.setOpaque(false);
        slider.putClientProperty( "JSlider.isFilled", Boolean.TRUE );
        add(slider, BorderLayout.CENTER);

        initialize(minValue, maxValue);
    }

    /**
     * Called to initialize component with minimum and maximum range values.
     *
     * @param minValue the minimum value for this slider
     * @param maxValue the maximum value for this slider
     */
    private void initialize(float minValue, float maxValue)
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

        int sliderMin = toSlider(minValue);
        int sliderMax = toSlider(maxValue);
        for (int i = 0; i < numThumbs; i++)
        {
            BoundedRangeModel model = slider.getModelAt(i);
            model.setMaximum(sliderMax);
            model.setMinimum(sliderMin);
            model.addChangeListener(new OrderChangeListener(i));
        }

        Hashtable valueLabels = new Hashtable();
        for (int i = 0; i <= 100; i += MAJOR_TICK_SPACING)
        {
            JLabel newLabel = new JLabel(labelFormat.format(fromSlider(i)));
            valueLabels.put(new Integer(i), newLabel);
        }
        slider.setLabelTable(valueLabels);
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
     * When look and feel or theme is changed, this method is called.  It
     * resizes the component based on new font sizes.
     */
    public void updateUI()
    {
        super.updateUI();

        adjustValueLabelHeight();
    }

    private void adjustValueLabelHeight()
    {
        // Create space for value labels in north quad. of component
        int fontHeight =
            getFontMetrics(MetalLookAndFeel.getSystemTextFont()).getHeight();

        add(Box.createVerticalStrut(fontHeight), BorderLayout.NORTH);
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

        // Swing bug workaround
        if (updateUIAfterPaint)
        {
            SwingUtilities.updateComponentTreeUI(this);
            updateUIAfterPaint = false;
        }
    }
    private boolean updateUIAfterPaint = true;

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