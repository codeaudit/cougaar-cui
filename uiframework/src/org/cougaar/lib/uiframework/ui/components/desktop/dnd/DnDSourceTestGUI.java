/* 
 * <copyright> 
 *  Copyright 1997-2001 Clark Software Engineering (CSE)
 *  under sponsorship of the Defense Advanced Research Projects 
 *  Agency (DARPA). 
 *  
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the Cougaar Open Source License as published by 
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).  
 *  
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS  
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR  
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF  
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT  
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT  
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL  
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,  
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR  
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.  
 *  
 * </copyright> 
 */

package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import java.util.Vector;

import org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI;
import org.cougaar.lib.uiframework.ui.components.desktop.CDesktopFrame;

/***********************************************************************************************************************
<b>Description</b>: This class is a test Cougaar Desktop component for drag and drop capabilities in the Cougaar
                    Desktop application.  This is the source component which a text string can be dragged from to the
                    DnDTargetTestGUI desktop component.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public class DnDSourceTestGUI extends org.cougaar.lib.uiframework.ui.components.desktop.ComponentFactory implements org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI, DragSource
{
  private JTextArea textArea = new JTextArea();

  // Drag & Drop supporting class
  private DragAndDropSupport dndSupport = new DragAndDropSupport();



  // ------------------- DragSource Interface ----------------------------  

  public Vector getSourceComponents()
  {
    Vector components = new Vector(1);
    components.add(textArea);
    
    return(components);
  }

  public boolean dragFromSubComponents()
  {
    return(true);
  }

  public Object getData(Component componentAt, Point location)
  {
    return(textArea.getText());
  }

  public void dragDropEnd(boolean success)
  {
  }




  public void install(CDesktopFrame f)
  {
	  JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(textArea, BorderLayout.CENTER);

    f.getContentPane().add(panel);

    // Add the drag source
    dndSupport.addDragSource(this);
  }





	public String getToolDisplayName()
	{
	  return("DnD Source Test UI");
	}

	public CougaarDesktopUI create()
	{
	  return(this);
	}

  public boolean supportsPlaf()
  {
    return(true);
  }

  public void install(JFrame f)
  {
    throw(new RuntimeException("install(JFrame f) not supported"));
  }

  public void install(JInternalFrame f)
  {
    throw(new RuntimeException("install(JInternalFrame f) not supported"));
  }

  public boolean isPersistable()
  {
    return(false);
  }

  public Serializable getPersistedData()
  {
    return(null);
  }

  public void setPersistedData(Serializable data)
  {
  }

  public String getTitle()
  {
    return("DnD Source Test UI");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(200, 200));
  }

  public boolean isResizable()
  {
    return(true);
  }
}
