/* 
 * <copyright>
 *  Copyright 1997-2003 Clark Software Engineering (CSE)
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

package org.cougaar.lib.uiframework.ui.components.desktop.osm;

import java.util.Vector;

/***********************************************************************************************************************
<b>Description</b>: This class provides event listener support utilities to the Object Storage Manager.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class NotificationSupport
{
  private Vector listenerTypeList = new Vector(0);
  private Vector listenerList = new Vector(0);

	/*********************************************************************************************************************
  <b>Description</b>: Returns a count of the number of listeners this support instance contains.

  <br>
  @return Number of listeners contained by this support instance
	*********************************************************************************************************************/
  public int getListenerCount()
  {
    return(listenerList.size());
  }

	/*********************************************************************************************************************
  <b>Description</b>: Adds a listener to the support instance.

  <br>
  @param listener Listener to add
  @param notificationType Type of events the listener is to be notified of
	*********************************************************************************************************************/
  public void addNotificationListener(NotificationListener listener, long notificationType)
  {
    synchronized(this)
    {
      if (!listenerList.contains(listener))
      {
        listenerList.add(listener);
        listenerTypeList.add(new ListenerType(listener, notificationType));
      }
    }
  }

	/*********************************************************************************************************************
  <b>Description</b>: Removes a listener from the support instance.

  <br><b>Notes</b>:<br>
	                  - Any notes about the method goes here

  <br>
  @param listener Listener to remove
	*********************************************************************************************************************/
  public void removeNotificationListener(NotificationListener listener)
  {
    synchronized(this)
    {
      int index = listenerList.indexOf(listener);
      
      if (index != -1)
      {
        listenerList.remove(index);
        listenerTypeList.remove(index);
      }
    }
  }

	/*********************************************************************************************************************
  <b>Description</b>: Sedns out an event notification to all listerners registered for the specified type of event.

  <br>
  @param event Event to notify listeners with
	*********************************************************************************************************************/
  public void fireNotificationEvent(NotificationEvent event)
  {
    synchronized(this)
    {
      long notificationType = event.getNotificationType();
      for (int i=0, isize=listenerTypeList.size(); i<isize; i++)
      {
        ListenerType lt = (ListenerType)listenerTypeList.elementAt(i);
        if (lt.ofType(notificationType))
        {
          lt.listener.notify(event);
        }
      }
    }
  }
}

/***********************************************************************************************************************
<b>Description</b>: Support class for notification support.  This class stores an individual listener and information
                    about the types of events it is interested in.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
class ListenerType
{
  private long type;
  
  NotificationListener listener;
  
	/*********************************************************************************************************************
  <b>Description</b>: Constructs a ListenerType instance.

  <br>
  @param listener Listener instance
  @param type Type of events the listener is interested in
	*********************************************************************************************************************/
  public ListenerType(NotificationListener listener, long type)
  {
    this.type = type;
    this.listener = listener;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Checks the specified event types against this listener's types.

  <br>
  @param type Event types
  @return True if the listener is interested is all specified events, false otherwise
	*********************************************************************************************************************/
  public boolean ofType(long type)
  {
    return((this.type & type) != 0x00L);
  }
}
