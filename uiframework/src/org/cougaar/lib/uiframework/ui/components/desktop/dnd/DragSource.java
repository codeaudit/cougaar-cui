package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.Point;
import java.util.Vector;

public interface DragSource
{
  // Vector must contain components
  public Vector getSourceComponents();

  public boolean dragFromSubComponents();

  public Object getData(Point location);
  
  public void dragDropEnd(boolean success);
}
