package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class LineType extends Command
{	int X;
	final int SOLID=1,DASHED=2;

	public LineType (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	if (X==SOLID) return "Line Type SOLID";
		else if (X==DASHED) return "Line Type DASHED";
		else return "Line Type "+X;
	}
}
