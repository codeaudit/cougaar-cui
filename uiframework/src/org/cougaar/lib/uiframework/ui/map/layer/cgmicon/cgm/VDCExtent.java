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

public class VDCExtent extends Command
{	int X1,X2,Y1,Y2;

	public VDCExtent (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X1=makeInt(0);
		Y1=makeInt(1);
		X2=makeInt(2);
		Y2=makeInt(3);
	}
	
	public String toString ()
	{	return "VDC Extent ["+X1+","+Y1+"] ["+X2+","+Y2+"]";
	}
	
	public int[] extent ()
	{	int x[]=new int[4];
		x[0]=X1; x[1]=Y1; x[2]=X2; x[3]=Y2;
		return x;
	}
}
