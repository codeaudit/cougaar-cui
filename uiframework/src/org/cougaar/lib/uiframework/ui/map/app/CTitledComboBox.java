
package org.cougaar.lib.uiframework.ui.map.app;


import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;



class CTitledComboBox extends JPanel {
    JComboBox combo;
    CTitledComboBox(String label, String[] choices, ActionListener cbl) {
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
