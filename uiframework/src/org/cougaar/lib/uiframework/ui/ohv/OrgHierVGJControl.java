/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphCanvas;

public class OrgHierVGJControl {
  static public boolean getMouseDraggingState () {
    return GraphCanvas.getMouseDraggingState();
  }

  static public void setMouseDraggingState (boolean value) {
    GraphCanvas.setMouseDraggingState(value);
  }

  static public boolean getSmallPanelState () {
    return GraphWindow.getSmallPanelState();
  }

  static public void setSmallPanelState (boolean value) {
    GraphWindow.setSmallPanelState(value);
  }
}
