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

public class MetafileElementList extends Command
{	int X1,X2,X3;

	public MetafileElementList (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X1=makeInt(0);
		X2=makeInt(1);
		X3=makeInt(2);
	}
	
	public String toString ()
	{	return "Metafile Element List "+X1+","+X2+","+X3;
	}
}
