/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.map.app;


import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;



public class CTitledComboBox extends JPanel {
    JComboBox combo;
    public CTitledComboBox(String label, String[] choices, ActionListener cbl) {
	super(new FlowLayout());
	setBorder(BorderFactory
		  .createTitledBorder(BorderFactory
				      .createEtchedBorder(), label));


	combo = new JComboBox(choices);
	combo.setSelectedIndex(0);
	addActionListener(cbl);
	add(combo);
    }

    public void addActionListener(ActionListener al) {
	combo.addActionListener(al);
    }
}
