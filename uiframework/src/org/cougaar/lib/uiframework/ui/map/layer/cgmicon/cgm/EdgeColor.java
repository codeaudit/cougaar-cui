package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.awt.*;
import java.io.*;

public class EdgeColor extends ColorCommand
{	public EdgeColor (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
	}
	
	public String toString ()
	{	return "Edge Color Input "+R+","+G+","+B;
	}
	
	public void paint (CGMDisplay d)
	{	d.setEdgeColor(C);
	}
}
