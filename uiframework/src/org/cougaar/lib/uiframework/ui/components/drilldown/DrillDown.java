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

package org.cougaar.lib.uiframework.ui.components.drilldown;

import java.awt.Component;
import java.awt.event.MouseEvent;


/***********************************************************************************************************************
<b>Description</b>: Interface used to interact with a component that can be drilled down into.  UI Components, or
                    classes with UI components, implement this so the DrillDownStack can interact with the component or
                    other class types.

<br><br><b>Notes</b>:<br>
									- Any notes about the class go here

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public interface DrillDown
{
	/*********************************************************************************************************************
  <b>Description</b>: Called when the DrillDownStack receives a double click on a trigger area.

  <br><b>Notes</b>:<br>
	                  - Returning null will stop the drill down action

  <br>
  @param e Mouse event that triggered the action
  @return Next drill down to make active
	*********************************************************************************************************************/
  public DrillDown getNextDrillDown(MouseEvent e);

	/*********************************************************************************************************************
  <b>Description</b>: Called by the parent drill down to initialize its data.

  <br><b>Notes</b>:<br>
	                  - This is a convience method and is not required to be called

  <br>
  @param data Initialization data
	*********************************************************************************************************************/
  public void setData(Object data);

	/*********************************************************************************************************************
  <b>Description</b>: Called when the DrillDownStack gets a vaild drill down from the parent drill down.

  <br><b>Notes</b>:<br>
	                  - Any child drill down objects should be added to the DrillDownStack within this method
	                  - Returning null will stop the drill down action

  <br>
  @param drillDownStack DrillDownStack object to add child drill downs to
  @return UI component to display for the current drill down
	*********************************************************************************************************************/
  public Component activate(DrillDownStack drillDownStack);
}
