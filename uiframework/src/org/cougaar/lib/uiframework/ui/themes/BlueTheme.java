package org.cougaar.lib.uiframework.ui.themes;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * This class describes a theme using blue colors.
 *
 * 1.3 11/19/00
 * @author Peter Fischer
 */
public class BlueTheme extends DefaultMetalTheme {

    public String getName() { return "Blue"; }

    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 144); // labels
    private final ColorUIResource primary2 = new ColorUIResource(100, 100, 255);// labels, handles highlight, shadows
    private final ColorUIResource primary3 = new ColorUIResource(100, 100, 255); // text selection

    private final ColorUIResource secondary1 = new ColorUIResource(0, 0, 80); // Inactive window borders (control outlines)
    private final ColorUIResource secondary2 = new ColorUIResource(20, 20, 120); // Secondary shadows
    private final ColorUIResource secondary3 = new ColorUIResource(210, 210, 255);// background

    private final ColorUIResource black = new ColorUIResource(0, 0, 0);
    private final ColorUIResource white = new ColorUIResource(222, 222, 222);

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

    protected ColorUIResource getBlack() { return black; }
    protected ColorUIResource getWhite() { return white; }

    public void addCustomEntriesToTable(UIDefaults table)
    {
        table.put("Button.background", new ColorUIResource(0, 0, 150));
        table.put("Button.foreground", new ColorUIResource(255, 255, 255));
        table.put("ComboBox.background", new ColorUIResource(0, 0, 150));
        table.put("ComboBox.foreground", new ColorUIResource(255, 255, 255));
    }
}
