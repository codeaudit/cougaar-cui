/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * </copyright>
 */
/*
 * File: InputDialog.java
 *
 * 5/29/96   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
 *
*/


package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;



import java.awt.Dialog;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Event;
import java.awt.TextField;
import java.awt.Component;



/**
 *  A dialog class for user input of a single string.
 *  </p>Here is the <a href="../gui/InputDialog.java">source</a>.
 *
**/

public class InputDialog extends Dialog
{
private int	event_id;

private TextField	text;
private Component postTo_;



/**
 *@param event_id_in this event will be posted if the user chooses "OK".
**/
public InputDialog(Frame frame, String title, Component post_to,
		int event_id_in)
	{
	super(frame, "Input", true);

	event_id = event_id_in;
        postTo_ = post_to;

	LPanel p = new LPanel();
	p.addLabel(title, 0, 0, 1.0, 1.0, 1, 0);
	text = p.addTextField(50, 0, 0, 1.0, 1.0, 1, 0);
	p.addButtonPanel("OK Cancel", 0);

	p.finish();
	add("Center", p);
	pack();
	show();
	}


public boolean action(Event event, Object object)
	{
	if(event.target instanceof Button)
		{
		if("OK".equals(object))
			{
			hide();
			dispose();
			postTo_.postEvent(new Event((Object)this, event_id,
					(Object)text.getText()));
			return true;
			}
		else if("Cancel".equals(object))
			{
			hide();
			dispose();
			return true;
			}
		}
	return super.action(event, object);
	}
}
