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

/***********************************************************************************************************************
<b>Description</b>: This class provides information of a notification event.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class NotificationEvent extends java.util.EventObject
{
	/*********************************************************************************************************************
  <b>Description</b>: Represents a storage event.  This event signals that a new object has been stored.
	*********************************************************************************************************************/
  public static final long STORE             = 0x01L;
	/*********************************************************************************************************************
  <b>Description</b>: Represents an update event.  This event signals that an object has been updated.
	*********************************************************************************************************************/
  public static final long UPDATE            = 0x02L;
	/*********************************************************************************************************************
  <b>Description</b>: Represents an object updated event.  This event signals that an object has been replaced by a
                      new, updated object.
	*********************************************************************************************************************/
  public static final long OBJECT_UPDATE     = 0x04L;
	/*********************************************************************************************************************
  <b>Description</b>: Represents an expiration update event.  This event signals that a new expiration time has been
                      assigned to an object.
	*********************************************************************************************************************/
  public static final long EXPIRATION_UPDATE = 0x08L;
	/*********************************************************************************************************************
  <b>Description</b>: Represents a deletion event.  This event signals that an object has been deleted.
	*********************************************************************************************************************/
  public static final long DELETE            = 0x10L;
	/*********************************************************************************************************************
  <b>Description</b>: Represents all events.  This event is the ORed combination of all events:
                        STORE | UPDATE | OBJECT_UPDATE | EXPIRATION_UPDATE | DELETE
	*********************************************************************************************************************/
  public static final long ANY               = STORE | UPDATE | OBJECT_UPDATE | EXPIRATION_UPDATE | DELETE;
  
  private long notificationType = 0;
  
  private ObjectID oid;
  
  private Object previousObject;

  private long expiration;
  private boolean expirationSet;
  
	/*********************************************************************************************************************
  <b>Description</b>: Constructs a NotificationEvent instance based on the supplied parameters.

  <br><b>Notes</b>:<br>
	                  - The notificationType parameter can be any ORed combination of any of the event types, i.e. one
	                    notification can contain multiple events
	                  - The expiration parameter is only valid if the expirationSet parameter is true

  <br>
  @param oid Object ID of the object the event occurred on
  @param source Actual object referenced by the object ID
  @param previousObject Previous object, if event is of OBJECT_UPDATE type, referenced by the object ID
  @param notificationType Type(s) of event(s)
  @param expiration Expiration time of the object in the form of milliseconds since 1970
  @param expirationSet Indication of validity of expiration time, if true the time is valid and the object has an
           expiration time
	*********************************************************************************************************************/
  public NotificationEvent(ObjectID oid, Object source, Object previousObject, long notificationType, long expiration, boolean expirationSet)
  {
    super(source);
    this.oid = oid;

    this.notificationType = notificationType;

    this.previousObject = previousObject;

    this.expiration = expiration;
    this.expirationSet = expirationSet;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the type(s) of event(s) contained by this notification ORed together.

  <br>
  @return Type(s) of event(s)
	*********************************************************************************************************************/
  public long getNotificationType()
  {
    return(notificationType);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the notification represents a store event.

  <br>
  @return True if this notification represents a store event, false otherwise
	*********************************************************************************************************************/
  public boolean isStore()
  {
    return((notificationType & STORE) != 0x00L);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the notification represents an update event.

  <br>
  @return True if this notification represents an update event, false otherwise
	*********************************************************************************************************************/
  public boolean isUpdate()
  {
    return((notificationType & UPDATE) != 0x00L);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the notification represents an object updated event.

  <br>
  @return True if this notification represents an object updated event, false otherwise
	*********************************************************************************************************************/
  public boolean isObjectUpdate()
  {
    return((notificationType & OBJECT_UPDATE) != 0x00L);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the notification represents an expiration update event.

  <br>
  @return True if this notification represents an expiration update event, false otherwise
	*********************************************************************************************************************/
  public boolean isExpirationUpdate()
  {
    return((notificationType & EXPIRATION_UPDATE) != 0x00L);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the notification represents a deletion event.

  <br>
  @return True if this notification represents a deletion event, false otherwise
	*********************************************************************************************************************/
  public boolean isDelete()
  {
    return((notificationType & DELETE) != 0x00L);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns an indicator as to whether or not the object the event is for has an expiration time.

  <br>
  @return True if this notification event's object has an expiration time
	*********************************************************************************************************************/
  public boolean isExpirationSet()
  {
    return(expirationSet);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the expiration time of the event's object.

  <br><b>Notes</b>:<br>
	                  - The expiration time returned is only valid if the method NotificationEvent.isExpirationSet()
	                    returns true

  <br>
  @return Expiration time of the event's object in the form of milliseconds since 1970
	*********************************************************************************************************************/
  public long getExpiration()
  {
    return(expiration);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the object ID of the object the event references.

  <br>
  @return Object ID of the object the event references
	*********************************************************************************************************************/
  public ObjectID getObjectID()
  {
    return(oid);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the previously referenced object if the event type is an object updated event.

  <br>
  @return Previous value of the object the event references
	*********************************************************************************************************************/
  public Object getPreviousObject()
  {
    return(previousObject);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Retruns the text representation of the event object.

  <br>
  @return Text representation of the event object
	*********************************************************************************************************************/
  public String toString()
  {
    return(oid + ":" + getSource() + ":" + previousObject + ":" + notificationType + ":" + expiration + ":" + expirationSet);
  }
}
