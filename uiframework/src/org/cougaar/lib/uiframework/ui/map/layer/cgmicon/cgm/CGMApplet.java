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

import java.awt.*;
import java.io.*;
import java.net.*;
import java.applet.*;

public class CGMApplet extends Applet
{	public void init ()
	{	String file=getParameter("File");
		if (file==null) return;
		setBackground(new Color(244,244,242));
		try
		{	URL url=new URL(getCodeBase(),file);
			DataInputStream in=new DataInputStream(url.openStream());
			CGM cgm=new CGM();
			cgm.read(in);
			in.close();
			setLayout(new BorderLayout());
			CGMDisplay d=new CGMDisplay(cgm);
			CGMPanel p=new CGMPanel(d);
			add("Center",p);
		}
		catch (Exception e)
		{	System.out.println(e);
			return;
		}
		repaint();
	}
}
