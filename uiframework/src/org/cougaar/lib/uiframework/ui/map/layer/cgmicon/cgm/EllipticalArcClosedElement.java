package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EllipticalArcClosedElement extends Command
{	int X,Y,X1,Y1,X2,Y2,SX1,SY1,SX2,SY2;
	boolean Closed;

	public EllipticalArcClosedElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
		Y=makeInt(1);
		X1=makeInt(2);
		Y1=makeInt(3);
		X2=makeInt(4);
		Y2=makeInt(5);
		SX1=makeInt(6);
		SY1=makeInt(7);
		SX2=makeInt(8);
		SY2=makeInt(9);
		Closed=(makeInt(10)==1);
	}
	
	public String toString ()
	{	return "Ellipse ["+X+","+Y+"] ["+X1+","+Y1+"] ["+X2+","+Y2+"] ["+
			SX1+","+SY1+"] ["+SX2+","+SY2+"] "+
			(Closed?"Closed":"Pie");
	}
}
