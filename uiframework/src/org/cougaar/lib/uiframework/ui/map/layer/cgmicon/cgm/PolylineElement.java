package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.awt.*;
import java.io.*;

public class PolylineElement extends Command
{	int X[],Y[];
	int X0[],Y0[];

	public PolylineElement (int ec, int eid, int l, DataInputStream in)
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
	{	String s="Polyline";
		for (int i=0; i<X.length; i++)
			s=s+" ["+X[i]+","+Y[i]+"]";
		return s;
	}

	public void scale (CGMDisplay d)
	{	X0=new int[X.length]; Y0=new int[X.length];
		for (int i=0; i<X.length; i++)
		{	X0[i]=d.x(X[i]);
			Y0[i]=d.y(Y[i]);
		}
	}
	
	public void paint (CGMDisplay d)
	{	d.graphics().setColor(d.getLineColor());
		d.graphics().drawPolyline(X0,Y0,X0.length);
	}
}
