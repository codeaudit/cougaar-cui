package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EdgeWidthMode extends Command
{	int X;

	public EdgeWidthMode (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	return "Edge Width "+X;
	}
}
