package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;

public   class CSplitPane extends javax.swing.JSplitPane
{
  public CSplitPane(int orientation)
  {
    super(orientation);
  }

  public void updateUI()
  {
    int location = getDividerLocation();
    super.updateUI();
    setDividerLocation(location);
  }
}
