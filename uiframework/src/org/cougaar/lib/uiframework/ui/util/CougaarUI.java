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