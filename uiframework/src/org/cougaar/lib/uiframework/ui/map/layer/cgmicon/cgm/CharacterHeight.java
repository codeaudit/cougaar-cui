package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class CharacterHeight extends Command
{	int X;

	public CharacterHeight (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X=makeInt(0);
	}
	
	public String toString ()
	{	return "Character Height "+X;
	}
	
	public void scale (CGMDisplay d)
	{	d.setTextSize((int)(d.factorY()*X));
	}
}
