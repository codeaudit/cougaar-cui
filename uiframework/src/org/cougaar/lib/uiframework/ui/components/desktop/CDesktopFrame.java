package org.cougaar.lib.uiframework.ui.components.desktop;

import java.io.Serializable;

import java.awt.Insets;
import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.swing.JMenuBar;
import javax.swing.event.InternalFrameEvent;

public class CDesktopFrame extends javax.swing.JInternalFrame implements javax.swing.event.InternalFrameListener, java.beans.VetoableChangeListener
{
	private CougaarDesktopUI component = null;

  private FrameInfo frameInfo = null;

  private Dimension deiconifiedSize = new Dimension();

	public CDesktopFrame(CDesktopPane desktopPane, FrameInfo info)
	{
		super(null, true, true, true, true);

    frameInfo = info;
    component = frameInfo.getComponent();

    try
    {
			// Need this to ensure frame is painted when dragged past scroll bars in show window contents while dragging
			// see frameDragged()
			addComponentListener(new ListenerAction(this, "frameDragged", new Object[] {}, ListenerAction.componentMoved));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

      // Add the frame to the desktop
			desktopPane.add(this);

      // Set up the Desktop Frame
      // Insets and minimum sizes compensate for the borders and title bar of the frame itself
      Insets frameInsets = getInsets();
			int width = ((getMinimumSize().width > frameInfo.componentSize.width) ? getMinimumSize().width : frameInfo.componentSize.width) + frameInsets.left + frameInsets.right;
			int height = getMinimumSize().height + frameInfo.componentSize.height;
			setSize(width, height);

  		setSelected(info.selected); // Must be set selected after the frame is added to the desktopPane (bug???)
      setResizable(component.isResizable());
      setTitle(component.getTitle());
	    setIcon(frameInfo.iconified);

      // Call start up methods on the component
  		if (component.isPersistable())
  		{
  		  component.setPersistedData(frameInfo.componentData);
  		}
      component.install(this);

	  	setLocation(frameInfo.frameLocation); // Must set the location after the component is installed (bug???)

//      addInternalFrameListener(this);
//      addVetoableChangeListener(this);

			setVisible(true);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
    }
	}

	public void frameDragged()
	{
		repaint();
	}

  public CougaarDesktop getDesktop()
  {
    return(((CDesktopPane)getDesktopPane()).getDesktop());
  }

  public CougaarDesktopUI createTool(String factoryName, Serializable data)
  {
    return(((CDesktopPane)getDesktopPane()).getDesktop().createTool(factoryName, data));
  }

  public FrameInfo getFrameInfo()
  {
    getLocation(frameInfo.frameLocation);
    getContentPane().getSize(frameInfo.componentSize);

    // The menu bar takes space away from the content pane
    if (getJMenuBar() != null)
    {
      frameInfo.componentSize.height += getJMenuBar().getSize().height;
    }

    frameInfo.iconified = isIcon();
    frameInfo.selected = isSelected();

		if (component.isPersistable())
		{
		  frameInfo.componentData = component.getPersistedData();
		}
		else
		{
		  frameInfo.componentData = null;
		}

		return(frameInfo);
	}

  public void internalFrameOpened(InternalFrameEvent e)
  {
//    System.out.println("internalFrameOpened");
  }

  public void internalFrameClosing(InternalFrameEvent e)
  {
//    System.out.println("internalFrameClosing");
  }

  public void internalFrameClosed(InternalFrameEvent e)
  {
//    System.out.println("internalFrameClosed");
  }

  public void internalFrameIconified(InternalFrameEvent e)
  {
//    System.out.println("internalFrameIconified");
  }

  public void internalFrameDeiconified(InternalFrameEvent e)
  {
//    System.out.println("internalFrameDeiconified");
  }

  public void internalFrameActivated(InternalFrameEvent e)
  {
//    System.out.println("internalFrameActivated");
  }

  public void internalFrameDeactivated(InternalFrameEvent e)
  {
//    System.out.println("internalFrameDeactivated");
  }

  public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
  {
  }

  private Dimension viewSize = new Dimension();
  public void setMaximum(boolean toMax) throws PropertyVetoException
  {
    if (toMax)
    {
      getSize(deiconifiedSize);
    }

    super.setMaximum(toMax);

    if (toMax)
    {
      setSize(getDesktop().getDesktopViewSize(viewSize));
    }
    else
    {
      setSize(deiconifiedSize);
    }
  }
}
