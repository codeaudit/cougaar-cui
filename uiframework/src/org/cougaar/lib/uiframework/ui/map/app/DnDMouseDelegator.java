/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
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
  public DnDMouseDelegator(MapBean map)
  {
    super(map);
    dragRec = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(map, DnDConstants.ACTION_MOVE, null);
  }

  /**
  * Sets the three default OpenMap mouse modes.
  * These modes are: NavMouseMode (Map Navigation), the
  * SelectMouseMode (MouseEvents go to Layers), and NullMouseMode
  * (MouseEvents are ignored).
  */
  public void setDefaultMouseModes()
  {
    MapMouseMode[] modes = new MapMouseMode[3];
    modes[0] = new NavMouseMode(true);
    modes[1] = new DragAndDropMouseMode(true);
    modes[2] = new NullMouseMode();

    setMouseModes(modes);
  }

  /**
  * Set the active MapMouseMode.
  * This sets the MapMouseMode of the associated MapBean.
  * @param mm MapMouseMode
  */
  public void setActive(MapMouseMode mm)
  {
    if (Debug.debugging("mousemode"))
    {
      Debug.output("MouseDelegator.setActive(): " + mm.getID());
    }

    if (map != null)
    {
      map.addMouseListener(mm);
      map.addMouseMotionListener(mm);

      if (mm instanceof ProjectionListener)
      {
        map.addProjectionListener((ProjectionListener)mm);
      }

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
    if (map != null)
    {
      map.removeMouseListener(mm);
      map.removeMouseMotionListener(mm);
      if (mm instanceof ProjectionListener) 
      {
        map.removeProjectionListener((ProjectionListener)mm);
      }

      if (mm instanceof DragGestureListener)
      {
        dragRec.removeDragGestureListener((DragGestureListener)mm);
      }
    }
  }
}
