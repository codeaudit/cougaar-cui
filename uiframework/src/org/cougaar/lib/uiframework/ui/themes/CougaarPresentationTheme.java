/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
public class CougaarPresentationTheme extends CougaarTheme
{
    public String getName() { return "Cougaar Presentation"; }

    private final FontUIResource controlFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 20);
    private final FontUIResource systemFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 18); // JTree Font
    private final FontUIResource windowTitleFont = new FontUIResource("Trebuchet MS", Font.BOLD, 32); // nothing automatic
    private final FontUIResource userFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 18); // table data, input data
    private final FontUIResource smallFont = new FontUIResource("Trebuchet MS", Font.PLAIN, 16); // 10nothing automatic

    public FontUIResource getControlTextFont() { return controlFont;}
    public FontUIResource getSystemTextFont() { return systemFont;}
    public FontUIResource getUserTextFont() { return userFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getWindowTitleFont() { return windowTitleFont;}
    public FontUIResource getSubTextFont() { return smallFont;}
}
