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

public class ReadCGM
{	public static void main (String args[])
		throws IOException
	{	DataInputStream in=new DataInputStream(
			new FileInputStream(args[0]));
		while (true)
		{	Command c=Command.read(in);
			if (c==null) break;
			System.out.println(c);
		}
		in.close();
	}
}
