package org.cougaar.lib.uiframework.ui.map.app;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.util.Vector;
import java.awt.dnd.*;

import com.bbn.openmap.util.Debug;
import com.bbn.openmap.event.*;
import com.bbn.openmap.*;

public class DnDMouseDelegator extends com.bbn.openmap.MouseDelegator
{
  private DragGestureRecognizer dragRec = null;

  /**
  * Construct a MouseDelegator with an associated MapBean.
  * @param map MapBean
  */
  public DnDMouseDelegator()
  {
  }

  public DnDMouseDelegator(MapBean map)
  {
    super(map);
    dragRec = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(map, DnDConstants.ACTION_MOVE, null);
  }

  public void setMap(MapBean map)
  {
    super.setMap(map);
    dragRec = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(map, DnDConstants.ACTION_MOVE, null);
  }

  /**
  * Sets the three default OpenMap mouse modes.
  * These modes are: NavMouseMode (Map Navigation), the
  * SelectMouseMode (MouseEvents go to Layers), and NullMouseMode
  * (MouseEvents are ignored).
  */
/*  public void setDefaultMouseModes()
  {
    MapMouseMode[] modes = new MapMouseMode[3];
    modes[0] = new NavMouseMode(true);
    modes[1] = new DragAndDropMouseMode(true);
    modes[2] = new NullMouseMode();

    setMouseModes(modes);
  }*/

  /**
  * Set the active MapMouseMode.
  * This sets the MapMouseMode of the associated MapBean.
  * @param mm MapMouseMode
  */
  public void setActive(MapMouseMode mm)
  {
    super.setActive(mm);

    if (map != null)
    {
      if (mm instanceof DragGestureListener)
      {
        try
        {
          dragRec.addDragGestureListener((DragGestureListener)mm);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  /**
  * Deactivate the MapMouseMode.
  * @param mm MapMouseMode.
  */
  public void setInactive(MapMouseMode mm)
  {
    super.setInactive(mm);

    if (map != null)
    {
      if (mm instanceof DragGestureListener)
      {
        dragRec.removeDragGestureListener((DragGestureListener)mm);
      }
    }
  }
}
