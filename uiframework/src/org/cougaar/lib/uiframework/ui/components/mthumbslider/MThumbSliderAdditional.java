/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components.mthumbslider;

import java.awt.*;

public interface MThumbSliderAdditional {
  public Rectangle getTrackRect();
  public Dimension getThumbSize();
  public int xPositionForValue(int value);
  public int yPositionForValue(int value);
}

