package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class EdgeVisibility extends Command
{	boolean Visibel;

	public EdgeVisibility (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		Visibel=(makeInt(0)!=0);
	}
	
	public String toString ()
	{	return "Edge Visibility "+
			(Visibel?"On":"Off");
	}
	
	public void paint (CGMDisplay d)
	{	d.setEdge(Visibel);
	}
}
