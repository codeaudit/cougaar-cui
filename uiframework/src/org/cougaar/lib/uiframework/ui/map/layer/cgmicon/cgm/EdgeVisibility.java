/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
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
