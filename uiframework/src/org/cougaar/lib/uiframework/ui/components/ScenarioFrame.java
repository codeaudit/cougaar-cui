package org.cougaar.lib.uiframework.ui.components;

import java.awt.Container;
import javax.swing.JMenu;

import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextChild;
import com.bbn.openmap.PropertyConsumer;

import org.cougaar.lib.uiframework.ui.components.CFrame;

// interaface imports
import java.util.Properties;

import com.bbn.openmap.gui.OpenMapFrame;

public class ScenarioFrame extends OpenMapCFrame
//                           implements BeanContextMembershipListener,
//                                      BeanContextChild,
//                                      PropertyConsumer
{
  private static ScenarioFrame me;

  public ScenarioFrame()
  {
    super();
    me = this;
  }

  public static Container getOpenMapContentPane()
  {
    if (me != null)
      return me.getContentPane();
    else
      return null;
  }

  public static Container getOpenMapJMenuBar()
  {
    if (me != null)
      return me.getJMenuBar();
    else
      return null;
  }

  public static void setVisibleFlag (boolean vis)
  {
     if (me != null)
       me.setVisible(vis);
  }

  public static JMenu getCFrameLookAndFeelPulldown ()
  {
    if (me != null)
      return me.getLookAndFeelPulldown();
    else
      return null;

  }

  public static JMenu getCFrameThemesPulldown ()
  {
    if (me != null)
       return me.getThemesPulldown();
    else
       return null;

  }

  //
  // pass through functions to satisfy the interfaces
  //

  // PropertyConsumer interface
/*
  public Properties getProperties( Properties getList)
  {
    return super.getProperties (getList);
  }

  public Properties getPropertyInfo ( Properties list)
  {
    return super.getPropertyInfo (list);
  }

  public String getPropertyPrefix ()
  {
    return super.getPropertyPrefix();
  }

  public void setPropertyPrefix (String prefix)
  {
    super.setPropertyPrefix(prefix);
  }

  public void setProperties( Properties setList)
  {
    super.setProperties (setList);
  }
  public void setProperties( String prefix, Properties setList)
  {
    super.setProperties (prefix, setList);
  }
*/

}
