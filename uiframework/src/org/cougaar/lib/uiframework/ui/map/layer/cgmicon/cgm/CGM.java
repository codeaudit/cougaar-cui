package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;
import java.util.*;

public class CGM implements Cloneable
{ Vector V;

	public void read (DataInputStream in)
		throws IOException
	{	V=new Vector();
		while (true)
		{	Command c=Command.read(in);
			if (c==null) break;
			V.addElement(c);
		}
	}

	public void paint (CGMDisplay d)
	{	Enumeration e=V.elements();
		while (e.hasMoreElements())
		{	Command c=(Command)e.nextElement();
			c.paint(d);
		}
	}

	public void scale (CGMDisplay d)
	{	Enumeration e=V.elements();
		while (e.hasMoreElements())
		{	Command c=(Command)e.nextElement();
			c.scale(d);
		}
	}

	public int[] extent ()
	{	Enumeration e=V.elements();
		while (e.hasMoreElements())
		{	Command c=(Command)e.nextElement();
			if (c instanceof VDCExtent)
				return ((VDCExtent)c).extent();
		}
		return null;
	}

	public static void main (String args[])
		throws IOException
	{	DataInputStream in=new DataInputStream(
			new FileInputStream(args[0]));
		CGM cgm=new CGM();
		cgm.read(in);
		in.close();
	}

  public Object clone()
  {
    CGM newOne = new CGM();

    newOne.V = new Vector ();
    for (int i=0;i<this.V.size();i++)
    {
      newOne.V.addElement(((Command)this.V.elementAt(i)).clone());
//      System.out.println("Command: " + (Command)newOne.V.elementAt(i));
    }
    return newOne;
  }

}
