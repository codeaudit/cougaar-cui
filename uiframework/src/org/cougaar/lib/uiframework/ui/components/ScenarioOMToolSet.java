/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JTextField;

import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.gui.*;

public class ScenarioOMToolSet extends OMToolSet
{

  public ScenarioOMToolSet()
  {
   super();
  }

  protected void addScaleEntry(String command, String info, String entry)
  {

    scaleField = new JTextField(entry);

	  scaleField.setPreferredSize(new Dimension(75, 25));
	  scaleField.setMinimumSize(new Dimension(75, 25));
	  scaleField.setMaximumSize(new Dimension(90, 25));

	  scaleField.setToolTipText(info);
	  scaleField.setMargin(new Insets(0,0,0,0));
        scaleField.setActionCommand(command);
	  scaleField.addActionListener(this);
	  face.add(scaleField);
  }


   public void addMouseModes(MouseDelegator md)
   {
     if (md != null)
     {
       ScenarioMouseModePanel mmp = new ScenarioMouseModePanel(md);
	     face.add(mmp);
     }
	 }

}
