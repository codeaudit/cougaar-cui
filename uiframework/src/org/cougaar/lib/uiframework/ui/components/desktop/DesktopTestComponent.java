package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class DesktopTestComponent extends ComponentFactory implements CougaarDesktopUI
{
  private int count = 0;

  // Method call order upon creation:
  // getPreferredSize();
  // isResizable();
  // install(CDesktopFrame f);

  // Method call order upon serialization:
  // getPersistedData();
  // frameClosed();

  // Method call order upon rehydration:
  // setPersistedData(Serializable data);
  // install(CDesktopFrame f);
  // frameMinimized();  (If stored as such)
  // frameMaximized();  (If stored as such)

	public String getToolDisplayName()
	{
	  return("Desktop Test Component");
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

  public void install(CDesktopFrame f)
  {
    try
    {
      JButton button = new JButton("Press Me!!!");
  		button.addActionListener(new ListenerAction(this, "buttonPressed", new Object[] {f}, ListenerAction.actionPerformed));
  
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(button, BorderLayout.CENTER);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
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
    return("Test Button " + count++);
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(250, 50));
  }

  public boolean isResizable()
  {
    return(true);
  }

  public void buttonPressed(CDesktopFrame frame)
  {
    frame.setTitle("Test Button " + count++);
  }
}
