package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EllipseElement extends Command
{	int X,Y,X1,Y1,X2,Y2;

	public EllipseElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
		Y=makeInt(1);
		X1=makeInt(2);
		Y1=makeInt(3);
		X2=makeInt(4);
		Y2=makeInt(5);
	}
	
	public String toString ()
	{	return "Ellipse ["+X+","+Y+"] ["+X1+","+Y1+"] ["+X2+","+Y2+"]";
	}
}
