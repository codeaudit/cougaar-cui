package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class CircularArcElement extends Command
{	int X,Y,SX1,SY1,SX2,SY2,R;

	public CircularArcElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
		Y=makeInt(1);
		SX1=makeInt(2);
		SY1=makeInt(3);
		SX2=makeInt(4);
		SY2=makeInt(5);
		R=makeInt(6);
	}
	
	public String toString ()
	{	return "Circle ["+X+","+Y+"] ["+
			SX1+","+SY1+"] ["+SX2+","+SY2+"] "+R;
	}
}
