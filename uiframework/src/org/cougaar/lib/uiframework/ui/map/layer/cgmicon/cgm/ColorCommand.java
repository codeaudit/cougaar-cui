package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.awt.*;
import java.io.*;

public class ColorCommand extends Command
{	int R,G,B;
	Color C;
	Color Colors[]=
	{	Color.black,Color.white,Color.green,Color.yellow,Color.blue,
		Color.magenta,Color.cyan,Color.red,
		Color.black.brighter(),Color.white.darker(),
		Color.green.darker(),Color.yellow.darker(),Color.blue.darker(),
		Color.magenta.darker(),Color.cyan.darker(),Color.red.darker(),
	};

	public ColorCommand (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		if (args.length>=3)
		{	R=args[0];
			G=args[1];
			B=args[2];
			C=new Color(R,G,B);
		}
		else if (args.length>0 && 
			args[0]>=1 && args[0]<=Colors.length)
		{	C=Colors[args[0]-1];
		}
		else
		{	C=new Color(128,128,128);
		}
	}
	
	public String toString ()
	{	return "Fill Color Input "+R+","+G+","+B;
	}
	
	public void paint (CGMDisplay d)
	{	d.setFillColor(C);
	}
}
