package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * A UI control bean that is a range selection control that implements the
 * Selector interface.
 *
 * This bean has bounded property:  "selectedItem" (of type RangeModel)
 *
 */
public class CRangeSelector extends JPanel implements Selector
{
    private RangeModel range = null;
    private JButton okButton;
    private JPanel rangeControl;
    private boolean plaf = false;

    /**
     * Create a new Range Selector
     *
     * @param plaf true if pluggable look and feel must be supported
     */
    public CRangeSelector(boolean plaf)
    {
        super(new BorderLayout());
        this.plaf = plaf;

        setLayout(new BorderLayout(10, 10));
        if (plaf)
        {
            rangeControl = new CSliderRangeControl();
        }
        else
        {
            rangeControl = new CMThumbSliderRangeControl();
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        okButton = new JButton("OK");
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createGlue());
        add(new JLabel("Set New Range"), BorderLayout.NORTH);
        add(rangeControl, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        updateRange();
        resetSize();

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    updateRange();
                }
            });
    }

    /**
     * When look and feel or theme is changed, this method is called.  It
     * ensures that the control size is updated as well.
     */
     public void updateUI()
    {
        super.updateUI();

        resetSize();
    }

    /**
     * Set control to new range
     *
     * @param newSelection new range for control (must be of type RangeModel)
     */
    public void setSelectedItem(Object newSelection)
    {
        if (newSelection instanceof RangeModel)
        {
            RangeModel newRange = (RangeModel)newSelection;
            if (plaf)
            {
                ((CSliderRangeControl)rangeControl).setRange(newRange);
            }
            else
            {
                ((CMThumbSliderRangeControl)rangeControl).setRange(newRange);
            }
            updateRange();
        }
    }

    /**
     * Get currently selected range
     *
     * @return currently selected range (can be cast to type RangeModel)
     */
    public Object getSelectedItem()
    {
        return range;
    }

    private void resetSize()
    {
        if (rangeControl != null)
        {
            rangeControl.setPreferredSize(null);
            rangeControl.setPreferredSize(
                new Dimension(500, rangeControl.getPreferredSize().height));
        }
    }

    private void updateRange()
    {
        RangeModel oldRange = range;
        if (plaf)
        {
            range = ((CSliderRangeControl)rangeControl).getRange();
        }
        else
        {
            range = ((CMThumbSliderRangeControl)rangeControl).getRange();
        }
        firePropertyChange("selectedItem", oldRange, range);
    }
}