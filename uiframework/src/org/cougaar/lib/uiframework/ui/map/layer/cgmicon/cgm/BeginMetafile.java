package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class BeginMetafile extends Command
{	String S;

	public BeginMetafile (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		S=makeString();
	}
	
	public String toString ()
	{	return "Begin Metafile "+S;
	}
}
