package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class TextFontIndex extends Command
{	int X;

	public TextFontIndex (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	return "Text Font Index Input "+X;
	}
}
