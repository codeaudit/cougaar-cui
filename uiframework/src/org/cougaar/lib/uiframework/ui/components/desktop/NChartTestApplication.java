package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.util.*;

import java.io.Serializable;

import java.awt.dnd.*;

import javax.swing.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.inventory.*;
import org.cougaar.lib.uiframework.ui.components.graph.*;

public class NChartTestApplication
{
	private NChartUI selector = null;
	public JFrame frame = new JFrame();
  public NChartTestApplication()
  {
  	selector = new NChartUI(5);
    selector.init(frame);
    frame.setSize(800, 600);
    frame.show();
  }
  public static void main(String[] args)
	{
		NChartTestApplication ncta = new NChartTestApplication();
		
		
	}
}