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

public class PolygonElement extends Command
{	int X[],Y[];
	Polygon P;

	public PolygonElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		int n=args.length/4;
		X=new int[n]; Y=new int[n];
		for (int i=0; i<n; i++)
		{	X[i]=makeInt(2*i);
			Y[i]=makeInt(2*i+1);
		}
	}
	
	public String toString ()
	{	String s="Polygon";
		for (int i=0; i<X.length; i++)
			s=s+" ["+X[i]+","+Y[i]+"]";
		return s;
	}
	
	public void scale (CGMDisplay d)
	{	P=new Polygon();
		for (int i=0; i<X.length; i++)
		{	P.addPoint(d.x(X[i]),d.y(Y[i]));
		}
	}
	
	public void paint (CGMDisplay d)
	{	if (d.getFilled())
		{	d.graphics().setColor(d.getFillColor());
			d.graphics().fillPolygon(P);
		}
		else
		{	d.graphics().setColor(d.getFillColor());
			if (!d.getEdge())
				d.graphics().drawPolygon(P);
		}
		if (d.getEdge())
		{	d.graphics().setColor(d.getEdgeColor());
			d.graphics().drawPolygon(P);
		}
	}
}
