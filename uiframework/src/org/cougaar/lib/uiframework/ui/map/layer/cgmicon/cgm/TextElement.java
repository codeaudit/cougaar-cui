/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.awt.*;
import java.io.*;

public class TextElement extends Command
{	int X,Y;
	String S;
	Font F;

	public TextElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
		Y=makeInt(1);
		S=makeString(6);
	}
	
	public String toString ()
	{	return "Text Element "+X+","+Y+": "+S;
	}

	public void scale (CGMDisplay d)
	{	F=new Font("Dialog",Font.PLAIN,d.getTextSize());
		System.out.println(d.getTextSize());
	}

	public void paint (CGMDisplay d)
	{	d.graphics().setFont(F);
		d.graphics().drawString(S,d.x(X),d.y(Y));
	}	
}
