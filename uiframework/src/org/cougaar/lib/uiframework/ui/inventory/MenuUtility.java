/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.inventory;

import javax.swing.*;

public class MenuUtility
{
  public static JCheckBoxMenuItem addCheckMenuItem(JMenu menu, String label, char mnemonic, Action action, boolean checked)
  {
    JCheckBoxMenuItem mi = addCheckMenuItem(menu, label, action, checked);
    mi.setMnemonic(mnemonic);

    return(mi);
  }

  public static JCheckBoxMenuItem addCheckMenuItem(JMenu menu, String label, Action action, boolean checked)
  {
    JCheckBoxMenuItem mi = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem(label, checked));
    mi.addActionListener(action);

    if(action == null)
    {
      mi.setEnabled(false);
    }

    return(mi);
  }

  public static JMenuItem addMenuItem(JMenu menu, String label, char mnemonic, Action action)
  {
    JMenuItem mi = addMenuItem(menu, label, action);
    mi.setMnemonic(mnemonic);

    return(mi);
  }

  public static JMenuItem addMenuItem(JMenu menu, String label, Action action)
  {
    JMenuItem mi = (JMenuItem)menu.add(new JMenuItem(label));
    mi.addActionListener(action);

    if(action == null)
    {
      mi.setEnabled(false);
    }

    return(mi);
  }

  public static JCheckBoxMenuItem addCheckMenuItem(JMenu menu, String label, String actionCommand, Action action, boolean checked)
  {
    JCheckBoxMenuItem mi = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem(label, checked));
    mi.addActionListener(action);
    mi.setActionCommand(actionCommand);

    if(action == null)
    {
      mi.setEnabled(false);
    }

    return(mi);
  }

  public static JRadioButtonMenuItem addRadioButtonMenuItem(JMenu menu, String label, Action action, String actionCommand, boolean checked)
  {
    JRadioButtonMenuItem mi = (JRadioButtonMenuItem)menu.add(new JRadioButtonMenuItem(label, checked));
    mi.addActionListener(action);
    mi.setActionCommand(actionCommand);

    if(action == null)
    {
      mi.setEnabled(false);
    }

    return(mi);
  }
}
