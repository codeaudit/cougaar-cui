package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EdgeType extends Command
{	int X;
	final int SOLID=1,DASHED=2;

	public EdgeType (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	if (X==SOLID) return "Edge Type SOLID";
		else if (X==DASHED) return "Edge Type DASHED";
		else return "Edge Type "+X;
	}
}
