package org.cougaar.lib.uiframework.ui.components.mthumbslider;

import java.awt.*;

public interface MThumbSliderAdditional {
  public Rectangle getTrackRect();
  public Dimension getThumbSize();
  public int xPositionForValue(int value);
  public int yPositionForValue(int value);
}

