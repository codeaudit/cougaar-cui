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
package org.cougaar.lib.uiframework.ui.util;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 * This interface must be implemented by all User Interface tools that are
 * included in the Cougaar Desktop Frame (CDesktopFrame).
 *
 * @see org.cougaar.lib.uiframework.ui.components.CDesktopFrame
 */
public interface CougaarUI
{
    /**
     * Install this user interface in the passed in JInternalFrame.
     *
     * @param f internal frame to which this user interface should be added
     */
    public void install(JFrame f);

    /**
     * Install this user interface in the passed in JFrame.
     *
     * @param f frame to which this user interface should be added
     */
    public void install(JInternalFrame f);

    /**
     * Returns true if this UI supports pluggable look and feel.  Otherwise,
     * only Metal look and feel support is assumed.
     *
     * @return true if UI supports pluggable look and feel.
     */
    public boolean supportsPlaf();
}