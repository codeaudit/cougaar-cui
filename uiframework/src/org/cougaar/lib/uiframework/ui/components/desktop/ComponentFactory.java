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

package org.cougaar.lib.uiframework.ui.components.desktop;

/***********************************************************************************************************************
<b>Description</b>: Abstract class definition of a Component Factory.  A Component Factory creates an instance of a
                    specific CougaarDesktopUI component type.

<br><br><b>Notes</b>:<br>
									- Extending classes must have a default constructor

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public abstract class ComponentFactory implements java.io.Serializable
{
	/*********************************************************************************************************************
  <b>Description</b>: Returns the text to be displayed in the Cougaar Desktop application's Tool menu.

  <br>
  @return Tool description to display
	*********************************************************************************************************************/
	public abstract String getToolDisplayName();

	/*********************************************************************************************************************
  <b>Description</b>: Creates and returns a specific CougaarDesktopUI component type instance.

  <br>
  @return Newly created CougaarDesktopUI component
	*********************************************************************************************************************/
	public abstract CougaarDesktopUI create();
}
