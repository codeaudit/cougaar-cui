package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.io.*;

public class FontList extends Command
{	String S[];

	public FontList (int ec, int eid, int l, DataInputStream in)
		throws IOException
	{	super(ec,eid,l,in);
		int count=0,i=0;
		while (i<args.length)
		{	count++; i+=args[i]+1;
		}
		S=new String[count];
		count=0; i=0;
		while (i<args.length)
		{	char a[]=new char[args[i]];
			for (int j=0; j<args[i]; j++)
				a[j]=(char)args[i+j+1];
			S[count]=new String(a);
			count++; i+=args[i]+1;
		}
	}
	
	public String toString ()
	{	String s="Font List: ";
		for (int i=0; i<S.length-1; i++) s=s+S[i]+", ";
		s=s+S[S.length-1];
		return s;
	}
}
