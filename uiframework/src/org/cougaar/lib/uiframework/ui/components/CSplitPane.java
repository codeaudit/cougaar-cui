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

package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;

/***********************************************************************************************************************
<b>Description</b>: An extension of the swing JSplitPane class that attempts to correct divider location problems
                    when switching look and feel.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class CSplitPane extends javax.swing.JSplitPane
{
	/*********************************************************************************************************************
  <b>Description</b>: Constructs a split pane with the specified orientation.  Possible orientation values are the
                      same as for the javax.swing.JSplitPane class.

  <br>
  @param orientation Split orientation
	*********************************************************************************************************************/
  public CSplitPane(int orientation)
  {
    super(orientation);
  }

	/*********************************************************************************************************************
  <b>Description</b>: Overridden method to attempt to correct display problems when switching look and feel.
	*********************************************************************************************************************/
  public void updateUI()
  {
    int location = getDividerLocation();
    super.updateUI();
    setDividerLocation(location);
  }
}
