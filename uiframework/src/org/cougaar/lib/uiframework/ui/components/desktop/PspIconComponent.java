package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;

public class PspIconComponent extends ComponentFactory implements CougaarDesktopUI
{
  static
  {
    try
    {
      Class.forName("org.cougaar.lib.uiframework.ui.map.app.ScenarioMap");
    }
    catch (Throwable t)
    {
      throw(new RuntimeException(t.toString()));
    }
  }

	public String getToolDisplayName()
	{
	  return("Psp Icon OpenMap");
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
      CougaarUI ui = (CougaarUI)Class.forName("org.cougaar.lib.uiframework.ui.map.app.ScenarioMap").newInstance();
      ui.install(f);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(new JLabel(t.toString()), BorderLayout.CENTER);
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
    return("Psp Icon OpenMap");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(800, 600));
  }

  public boolean isResizable()
  {
    return(true);
  }
}
