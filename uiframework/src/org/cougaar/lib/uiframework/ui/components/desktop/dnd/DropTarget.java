package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.Point;
import java.util.Vector;

public interface DropTarget
{
  // Vector must contain components
  public Vector getTargetComponents();

  public boolean dropToSubComponents();

  public boolean readyForDrop(Point location);

  // show: True if the target should indicate a Drag & Drop operation is underway, false if not
  // dropable: True if the current drag can be droped on the component
  public void showAsDroppable(boolean show, boolean droppable);

  public void dropData(Object data);

  // Vector must contain DataFlavor objects
  public Vector getSupportedDataFlavors();
}
