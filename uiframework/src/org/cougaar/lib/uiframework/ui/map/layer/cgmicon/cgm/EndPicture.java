package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EndPicture extends Command
{	String S;

	public EndPicture (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
	}
	
	public String toString ()
	{	return "End Picture";
	}
}
