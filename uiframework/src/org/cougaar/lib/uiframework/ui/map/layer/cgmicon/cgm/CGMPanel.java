package org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm;

import java.awt.*;
import java.awt.event.*;

public class CGMPanel extends Panel
{	CGMDisplay D;
	int W=0,H=0;
	public CGMPanel (CGMDisplay d)
	{	D=d;
	}
	public void paint (Graphics g)
	{	int W0=getSize().width,H0=getSize().height;
		if (W0!=W || H0!=H)
		{	W=W0; H=H0;
			D.scale(W,H);
		}
		D.frame(g);
		D.paint(g);
	}
}
