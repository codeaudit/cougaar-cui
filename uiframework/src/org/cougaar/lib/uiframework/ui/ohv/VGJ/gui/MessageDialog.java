/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
/*
 * File: MessageDialog.java
 *
 * 5/10/96   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
 *
*/


package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;



import java.awt.Dialog;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Event;





/**
 * A dialog class for displaying a message.
 * </p>Here is the <a href="../gui/MessageDialog.java">source</a>.
 *
 *@author	Larry Barowski
**/



public class MessageDialog extends Dialog
{
public MessageDialog(Frame frame, String title, String message,
		boolean modal)
	{
	super(frame, title, modal);

	LPanel p = new LPanel();
	p.addLabel(message, 0, 0, 1.0, 1.0, 0, 0);
	p.addButtonPanel("OK", 0);

	p.finish();
	add("Center", p);
	pack();
	show();
	}


public boolean action(Event event, Object object)
	{
	if(event.target instanceof Button && "OK".equals(object))
		{
		hide();
		dispose();
		return true;
		}
	return false;
	}
}






