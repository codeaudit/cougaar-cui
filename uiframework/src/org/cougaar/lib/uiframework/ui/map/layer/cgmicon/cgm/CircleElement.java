package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class CircleElement extends Command
{	int X1,Y1,R1;
	int X,Y,R;

	public CircleElement (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		X1=makeInt(0);
		Y1=makeInt(1);
		R1=makeInt(2);
	}
	
	public String toString ()
	{	return "Circle ["+X1+","+Y1+"] "+R;
	}
	
	public void scale (CGMDisplay d)
	{	X=d.x(X1); Y=d.y(Y1); R=(int)(d.factorX()*R1);
		X-=R; Y-=R; R=2*R-1;
	}
	
	public void paint (CGMDisplay d)
	{	if (d.getFilled())
		{	d.graphics().setColor(d.getFillColor());
			d.graphics().fillOval(X,Y,R,R);
		}
		else
		{	d.graphics().setColor(d.getFillColor());
			if (!d.getEdge())
				d.graphics().drawOval(X,Y,R,R);
		}
		if (d.getEdge())
		{	d.graphics().setColor(d.getEdgeColor());
			d.graphics().drawOval(X,Y,R,R);
		}
	}
}
