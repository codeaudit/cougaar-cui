/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import javax.swing.plaf.metal.MetalLookAndFeel;

import javax.swing.JLayeredPane;
import javax.swing.JInternalFrame;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import javax.swing.UIManager;

public class CDesktopPane extends javax.swing.JDesktopPane implements javax.swing.Scrollable, javax.swing.SwingConstants
{
	private CougaarDesktop desktop = null;

	private JPanel backgroundPanel = new JPanel();;

	public CDesktopPane(CougaarDesktop desktop, int desktopWidth, int desktopHeight)
	{
		this.desktop = desktop;

		// Set the total desktop size
		setPreferredSize(new Dimension(desktopWidth, desktopHeight));
		// Set dragging style
//	 	desktop.putClientProperty("JDesktopPane.dragMode", "outline"); // Show window outline while dragging
		putClientProperty("JDesktopPane.dragMode", "faster"); // Show window contents while dragging
		
		backgroundPanel.setSize(desktopWidth, desktopHeight);
		backgroundPanel.setOpaque(false);
	}

  public CougaarDesktop getDesktop()
  {
    return(desktop);
  }

  public void setBackground(String backgroundImage, boolean tiled)
  {
    remove(backgroundPanel);
    backgroundPanel.removeAll();

    if (backgroundImage != null)
    {
  		ImageIcon imageIcon = new ImageIcon(backgroundImage);
  
  		// Add a tiled background image
  		if (tiled)
  		{
        Dimension size = backgroundPanel.getSize();

  			int width = (int)(size.width/imageIcon.getIconWidth());
  			int height = (int)(size.height/imageIcon.getIconHeight());
  			backgroundPanel.setLayout(new GridLayout(height, width));
  			for (int i=0; i<height; i++)
  			{
  				for (int j=0; j<width; j++)
  				{
  					backgroundPanel.add(new JLabel(imageIcon));
  				}
  			}
  		}
  		else // Center the background image on the desktop
  		{
  			backgroundPanel.setLayout(new BorderLayout());
  			backgroundPanel.add(new JLabel(imageIcon), BorderLayout.CENTER);
  		}

  		add(backgroundPanel, JLayeredPane.FRAME_CONTENT_LAYER);
  	}
	}

	/**
	* When look and feel or theme is changed, this method is called.  It
	* overrides a Cougaar theme color for the desktop.
	* (otherwise resulting color combo is extra harsh).
	*/
	public void updateUI()
	{
	  // Some hack
		if ((desktop != null) && (UIManager.getLookAndFeel() instanceof MetalLookAndFeel) && desktop.getCurrentTheme().getName().startsWith("Cougaar"))
		{
			setBackground(Color.gray);
		}
		else
		{
			setBackground(null);
		}

		super.updateUI();
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return(getPreferredSize());
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return(50);
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		if (orientation == VERTICAL)
		{
			return(visibleRect.height);
		}
		else
		{
			return(visibleRect.width);
		}
	}

	public boolean getScrollableTracksViewportWidth()
	{
		return(false);
	}

	public boolean getScrollableTracksViewportHeight()
	{
		return(false);
	}

  public CDesktopFrame[] getAllDesktopFrames()
  {
		JInternalFrame[] ifs = getAllFrames();
		CDesktopFrame[] frameList = new CDesktopFrame[ifs.length];
		for (int i=0; i<ifs.length; i++)
		{
		  frameList[i] = (CDesktopFrame)ifs[i];
		}
		
		return(frameList);
  }

  public CDesktopFrame getSelectedDesktopFrame()
  {
		JInternalFrame selectedFrame = null;
		JInternalFrame[] ifs = getAllFrames();
		for (int i=0; i<ifs.length; i++)
		{
			if (ifs[i].isSelected())
			{
				selectedFrame = ifs[i];
				break;
			}
		}

		return((CDesktopFrame)selectedFrame);
  }
}
