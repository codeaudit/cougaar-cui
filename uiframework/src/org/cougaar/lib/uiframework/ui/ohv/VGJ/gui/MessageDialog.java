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






