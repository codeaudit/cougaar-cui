package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.Dimension;
import java.io.Serializable;

public interface CougaarDesktopUI extends org.cougaar.lib.uiframework.ui.util.CougaarUI
{
//  public static final String COUGAAR_DESKTOP_UI_PROP = "CougaarDesktopUI";

  // Method call order upon creation:
  // getPreferredSize();
  // isResizable();
  // install(CDesktopFrame f);

  // Method call order upon serialization:
  // isPersistable();
  // getPersistedData();  (if persistable)
  // frameClosed();  ????????????????

  // Method call order upon rehydration:
  // isPersistable();
  // setPersistedData(Serializable data);  (if persistable)
  // install(CDesktopFrame f);
  // frameMinimized();  (If stored as such)
  // frameMaximized();  (If stored as such)

  public void install(CDesktopFrame f);

  public boolean isPersistable();
  public Serializable getPersistedData();
  public void setPersistedData(Serializable data);

  public String getTitle();
  public Dimension getPreferredSize();
  public boolean isResizable();

/*
  public void frameIconified();
  public void frameDeIconified();
  public void frameMaximized();
  public void frameClosed();
*/
}
