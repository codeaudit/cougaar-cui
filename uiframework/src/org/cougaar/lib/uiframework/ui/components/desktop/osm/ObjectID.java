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

import java.net.InetAddress;
import java.rmi.server.UID;

/***********************************************************************************************************************
<b>Description</b>: This class combines the network IP address of the machine which it is generated on and a UID
                    instance which is intended to produce a unique ID.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class ObjectID implements java.lang.Cloneable, java.io.Serializable
{
  private UID uid;
  private InetAddress inetAddress;
  private int hashCode;
  
	/*********************************************************************************************************************
  <b>Description</b>: Constructs an ObjectID instance and generates a hash code for this instance.
	*********************************************************************************************************************/
  ObjectID()
  {
    try
    {
      uid = new UID();
      inetAddress = InetAddress.getLocalHost();
      hashCode = (inetAddress.toString() + uid.toString()).hashCode();
    }
    catch (Throwable t)
    {
      throw(new RuntimeException("Rethrown exception: " + t.toString()));
    }
  }

	/*********************************************************************************************************************
  <b>Description</b>: Overrides Object.equals() to compare two ObjectID instances to determine if they refer to the 
                      same UID and network IP address.

  <br>
  @param obj Instance of ObjectID to compare to the current instance
  @return True if the ObjectIDs are equivilant, false otherwise
	*********************************************************************************************************************/
  public boolean equals(Object obj)
  {
    ObjectID oid = (ObjectID)obj;
    
    return(this.uid.equals(oid.uid) && this.inetAddress.equals(oid.inetAddress));
  }

	/*********************************************************************************************************************
  <b>Description</b>: Overrides Object.hashCode() to provide a unique hash code for the ObjectID instance.

  <br><b>Notes</b>:<br>
	                  - The hash code generated is calculated by the toString concatenation of the UID instance and the
	                    network IP address and the hashCode() method is called on the resulting string.

  <br>
  @return Hash code of the ObjectID instance
	*********************************************************************************************************************/
  public int hashCode()
  {
    return(hashCode);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Provides text representation of the ObjectID instance in the form of a network IP address and
                      an UID.

  <br>
  @return Text representation of the ObjectID instance
	*********************************************************************************************************************/
  public String toString()
  {
    return(inetAddress + ": " + uid);
  }
}
