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

public class RectangleElement extends Command
{	int X1,Y1,X2,Y2;
	int X,Y,W,H;

	public RectangleElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X1=makeInt(0);
		Y1=makeInt(1);
		X2=makeInt(2);
		Y2=makeInt(3);
	}
	
	public String toString ()
	{	return "Rectangle ["+X1+","+Y1+"] ["+X2+","+Y2+"]";
	}

	public void scale (CGMDisplay d)
	{	X=d.x(X1); Y=d.y(Y1);
		W=d.x(X2)-X-1; H=d.y(Y2)-Y-1;
	}
	
	public void paint (CGMDisplay d)
	{	if (d.getFilled())
		{	d.graphics().setColor(d.getFillColor());
			d.graphics().fillRect(X,Y,W,H);
		}
		else
		{	d.graphics().setColor(d.getFillColor());
			if (!d.getEdge())
				d.graphics().drawRect(X,Y,W,H);
		}
		if (d.getEdge())
		{	d.graphics().setColor(d.getEdgeColor());
			d.graphics().drawRect(X,Y,W,H);
		}
	}
}
