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

import java.io.*;

public class CircularArcClosedElement extends Command
{	int X,Y,SX1,SY1,SX2,SY2,R;
	boolean Closed;

	public CircularArcClosedElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
		Y=makeInt(1);
		SX1=makeInt(2);
		SY1=makeInt(3);
		SX2=makeInt(4);
		SY2=makeInt(5);
		R=makeInt(6);
		Closed=(makeInt(7)==1);
	}
	
	public String toString ()
	{	return "Circle ["+X+","+Y+"] ["+
			SX1+","+SY1+"] ["+SX2+","+SY2+"] "+R+" "+
			(Closed?"Closed":"Pie");
	}
}
