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
<b>Description</b>: This class is used exclusively by the Object Storage manager and its supporting classes to store
                    each object and its associated information, such as expriation time.

<br><b>Notes</b>:<br>
                  - This class also generates the ObjectID instance associated with the object

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class ObjectStorage
{
  // Make these package visible for faster access

  ObjectID oid = new ObjectID();
  int hashCode = oid.hashCode();

  // Forget some of the initializers since they are supposed to take extra time
  Object object;
  Object previousObject;
  long expiration;
  boolean expirationSet;

  long actionType = NotificationEvent.STORE;

	/*********************************************************************************************************************
  <b>Description</b>: Constructor for building an ObjectStorage instance representing an object without an expiration
                      time.

  <br>
  @param object Object to be represented
	*********************************************************************************************************************/
  ObjectStorage(Object object)
  {
    this.object = object;

    this.expirationSet = false;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Constructor for building an ObjectStorage instance representing an object with an expiration
                      time.

  <br>
  @param object Object to be represented
  @param expiration Object's expiration time in the form of milliseconds since 1970
	*********************************************************************************************************************/
  ObjectStorage(Object object, long expiration)
  {
    this.object = object;

    this.expiration = expiration;
    this.expirationSet = true;
  }

	/*********************************************************************************************************************
  <b>Description</b>: Returns the hash code of the object ID associated with the object.

  <br><b>Notes</b>:<br>
	                  - The hashcode of the object ID should be unique, so this object can be used in a hashtable

  <br>
  @return Hashcode of the object ID associated with the object
	*********************************************************************************************************************/
  public int hashCode()
  {
    return(hashCode);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Commits the pending transactions on this object and generates an event for the transactions.

  <br>
  @return Event describing the commited transactions, or null if there were no pending transactions
	*********************************************************************************************************************/
  NotificationEvent commit()
  {
    NotificationEvent event = null;

    if (actionType != 0x00L)
    {
      event = new NotificationEvent(oid, object, previousObject, actionType, expiration, expirationSet);

      actionType = 0x00L;
      previousObject = null;
    }

    return(event);
  }
}
