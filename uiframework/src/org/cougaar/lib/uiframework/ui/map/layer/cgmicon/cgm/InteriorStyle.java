package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class InteriorStyle extends Command
{	int X;
	final int SOLID=1,EMPTY=2;

	public InteriorStyle (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	if (X==SOLID) return "Interior Style SOLID";
		else if (X==EMPTY) return "Interior Style EMPTY";
		else return "Line Type "+X;
	}
}
