package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class ColorSelectionMode extends Command
{	int X;

	public ColorSelectionMode (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	return "Color Selection Mode "+X;
	}
}
