package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Control used to select view related features of a stoplight chart.
 * Used to select whether chart should use color, value, or both when
 * displaying a data point in a cell.
 */
public class CViewFeatureSelectionControl extends CRadioButtonSelectionControl
{
    /** color selection string */
    public static String COLOR = "Color";

    /** value selection string */
    public static String VALUE = "Value";

    /** both selection string */
    public static String BOTH = "Both";

    private static String[] selections = {COLOR, VALUE, BOTH};

    /**
     * Default constructor.  Create new view feature selection control with
     * horizontal orientation.
     */
    public CViewFeatureSelectionControl()
    {
        super(selections, BoxLayout.X_AXIS);

        setSelectedItem(BOTH);
    }

    /**
     * Create new view feature selection control.
     *
     * @param orientation BoxLayout.X_AXIS or BoxLayout.Y_AXIS
     */
    public CViewFeatureSelectionControl(int orientation)
    {
        super(selections, orientation);

        setSelectedItem(BOTH);
    }
}