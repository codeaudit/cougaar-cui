/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.themes;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * This class describes a theme using offical Cougaar colors.
 *
 * 1.3 12/21/00
 * @author Peter Fischer
 */
public class CougaarTheme extends DefaultMetalTheme
{
    public String getName() { return "Cougaar"; }

    private final FontUIResource controlFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 14);
    private final FontUIResource systemFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 12); // JTree Font
    private final FontUIResource windowTitleFont = new FontUIResource("Trebuchet MS", Font.BOLD, 16); // nothing automatic
    private final FontUIResource userFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 12); // table data, input data
    private final FontUIResource smallFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 10); // 10nothing automatic

    private final ColorUIResource primary1 = new ColorUIResource(255, 255, 102); // labels (Axis)
    private final ColorUIResource primary2 = new ColorUIResource(192, 192, 192);// Graph Framework, Axis Values, selected text
    private final ColorUIResource primary3 = new ColorUIResource(100, 100, 100); // text selection, tool tip background

    private final ColorUIResource secondary1 = new ColorUIResource(150, 150, 150); // Inactive window borders (control outlines)
    private final ColorUIResource secondary2 = new ColorUIResource(100, 100, 100); // Secondary shadows
    private final ColorUIResource secondary3 = new ColorUIResource(25, 25, 25);// background

    private final ColorUIResource black = new ColorUIResource(248, 248, 248); // Control labels
    private final ColorUIResource white = new ColorUIResource(0, 0, 0);

    public FontUIResource getControlTextFont() { return controlFont;}
    public FontUIResource getSystemTextFont() { return systemFont;}
    public FontUIResource getUserTextFont() { return userFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getWindowTitleFont() { return windowTitleFont;}
    public FontUIResource getSubTextFont() { return smallFont;}

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

    protected ColorUIResource getBlack() { return black; }
    protected ColorUIResource getWhite() { return white; }

    public ColorUIResource getMenuSelectedBackground() { return secondary1; }

    public void addCustomEntriesToTable(UIDefaults table)
    {
        //Border blackLineBorder = new BorderUIResource(new EtchedBorder(getBlack(), getBlack()));
	//table.put( "TitledBorder.border", blackLineBorder);

        table.put("ComboBox.selectionBackground", secondary1);

        table.put("Graph.dataSeries1", new ColorUIResource(80, 147, 220));
        table.put("Graph.dataSeries2", new ColorUIResource(128, 192, 12));
        table.put("Graph.dataSeries3", new ColorUIResource(194, 96, 6));
        table.put("Graph.dataSeries4", new ColorUIResource(0, 170, 162));
        table.put("Graph.dataSeries5", new ColorUIResource(164, 127, 60));
        table.put("Graph.dataSeries6", new ColorUIResource(154, 154, 246));
        table.put("Graph.dataSeries7", new ColorUIResource(153, 204, 0));
        table.put("Graph.dataSeries8", new ColorUIResource(175, 213, 255));
        table.put("Graph.dataSeries9", new ColorUIResource(0, 180, 0));
        table.put("Graph.dataSeries10", new ColorUIResource(183, 180, 113));

        table.put("Graph.grid", primary3);
    }
}
