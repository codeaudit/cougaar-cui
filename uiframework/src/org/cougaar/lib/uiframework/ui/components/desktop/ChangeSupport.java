/* 
 * <copyright> 
 *  Copyright 1997-2001 Clark Software Engineering (CSE)
 *  under sponsorship of the Defense Advanced Research Projects 
 *  Agency (DARPA). 
 *  
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the Cougaar Open Source License as published by 
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).  
 *  
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS  
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR  
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF  
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT  
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT  
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL  
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,  
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR  
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.  
 *  
 * </copyright> 
 */

package org.cougaar.lib.uiframework.ui.components.desktop;

import java.util.Vector;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/***********************************************************************************************************************
<b>Description</b>: Support class for classes that need to implement listner notification services for ChangeListeners.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class ChangeSupport
{
  private Vector listenerList = new Vector(0);
  
	/*********************************************************************************************************************
  <b>Description</b>: Adds a change listener to the list of listeners.

  <br>
  @param listener Listener to add
	*********************************************************************************************************************/
  public synchronized void addChangeListener(ChangeListener listener)
  {
    if (!listenerList.contains(listener))
    {
      listenerList.add(listener);
    }
  }

	/*********************************************************************************************************************
  <b>Description</b>: Removes a change listener from the list of listeners.

  <br>
  @param listener Listener to remove
	*********************************************************************************************************************/
  public synchronized void removeChangeListener(ChangeListener listener)
  {
    listenerList.remove(listener);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Notifies a listeners that have been added of a change event.

  <br>
  @param source Source of event
	*********************************************************************************************************************/
  public synchronized void fireChangeEvent(Object source)
  {
    ChangeEvent e = new ChangeEvent(source);
    
    for (int i=0, isize=listenerList.size(); i<isize; i++)
    {
      ((ChangeListener)listenerList.elementAt(i)).stateChanged(e);
    }
  }
}