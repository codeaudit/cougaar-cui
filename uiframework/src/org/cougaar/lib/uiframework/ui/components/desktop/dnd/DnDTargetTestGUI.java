package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Point;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import java.util.Vector;

import org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI;
import org.cougaar.lib.uiframework.ui.components.desktop.CDesktopFrame;

public class DnDTargetTestGUI extends org.cougaar.lib.uiframework.ui.components.desktop.ComponentFactory implements org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI, DropTarget
{
  private JTextArea textArea = new JTextArea();
  private Color background = textArea.getBackground();

  // Drag & Drop supporting class
  private DragAndDropSupport dndSupport = new DragAndDropSupport();



  // ------------------- DragSource Interface ----------------------------  

  public Vector getTargetComponents()
  {
    Vector components = new Vector(1);
    components.add(textArea);
    
    return(components);
  }

  public boolean dropToSubComponents()
  {
    return(true);
  }

  public boolean readyForDrop(Point location)
  {
    return(true);
  }

  public void showAsDroppable(boolean show, boolean droppable)
  {
		if(show)
		{
		  if (droppable)
		  {
			  textArea.setBackground(Color.green);
		  }
		  else
		  {
			  textArea.setBackground(Color.red);
			}
		}
		else
		{
			textArea.setBackground(background);
		}
  }

  public void dropData(Object data)
  {
  	textArea.setText(data.toString());
  }

  public Vector getSupportedDataFlavors()
  {
    Vector flavors = new Vector(1);
    flavors.add(ObjectTransferable.getDataFlavor(String.class));
    
    return(flavors);
  }




  public void install(CDesktopFrame f)
  {
	  JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(textArea, BorderLayout.CENTER);

    f.getContentPane().add(panel);


    // Add the drop target
    dndSupport.addDropTarget(this);
  }







	public String getToolDisplayName()
	{
	  return("DnD Target Test UI");
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
    return("DnD Target Test UI");
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