package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Vector;
import javax.swing.*;

import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * A UI control bean that is a button that is labeled with the control's
 * current value.  Upon pressing the button the user is presented with a
 * dialog that contains a MThumbSliderRangeControl with which the user can
 * modify the selected range.  Can be configured to use a SliderRangeControl
 * if dynamic Pluggable look and feel is required.
 *
 * This bean has bounded property:  "selectedItem"
 */
public class CRangeButton extends CPullrightButton implements Selector
{
    private CRangeSelector rs = null;
    private String prefix;

    /**
     * Default constructor.  Creates new range button with range from 0 to 100.
     */
    public CRangeButton()
    {
        super();
        init("C+", 0, 100, false);
    }

    /**
     * Creates new range button based on the given parameters
     *
     * @param prefix the prefix to prepend before range values
     * @param min    the minimum value for the range
     * @param max    the maximum value for the range
     * @param plaf   true if pluggable look and feel is required.
     */
    public CRangeButton(String prefix, int min, int max, boolean plaf)
    {
        super();
        init(prefix, min, max, plaf);
    }

    /**
     * Initialize the range button
     *
     * @param prefix the prefix to prepend before range values
     * @param min    the minimum value for the range
     * @param max    the maximum value for the range
     * @param plaf   true if pluggable look and feel is required.
     */
    private void init(final String prefix, int min, int max, boolean plaf)
    {
        this.prefix = prefix;
        RangeModel range = new RangeModel(min, max);
        rs = new CRangeSelector(plaf, min, max);
        rs.setSelectedItem(range);
        setSelectorControl(rs);

        setText(prefix + min + " to " + prefix + max);
        rs.addPropertyChangeListener("selectedItem",
                                     new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    RangeModel r = (RangeModel)rs.getSelectedItem();
                    setText(prefix + r.getMin() + " to " +prefix + r.getMax());
                }
            });
    }

    // For Testing ...
    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CRangeButton rb = new CRangeButton();
        frame.getContentPane().add(rb, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}