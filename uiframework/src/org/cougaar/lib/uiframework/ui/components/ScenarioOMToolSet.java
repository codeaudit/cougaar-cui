package org.cougaar.lib.uiframework.ui.components;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JTextField;

import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.gui.*;

public class ScenarioOMToolSet extends OMToolSet
{

  public ScenarioOMToolSet()
  {
   super();
  }

  protected void addScaleEntry(String command, String info, String entry)
  {

    scaleField = new JTextField(entry);

	  scaleField.setPreferredSize(new Dimension(75, 25));
	  scaleField.setMinimumSize(new Dimension(75, 25));
	  scaleField.setMaximumSize(new Dimension(90, 25));

	  scaleField.setToolTipText(info);
	  scaleField.setMargin(new Insets(0,0,0,0));
        scaleField.setActionCommand(command);
	  scaleField.addActionListener(this);
	  face.add(scaleField);
  }

   public void addMouseModes(MouseDelegator md)
   {
     if (md != null)
     {
       ScenarioMouseModePanel mmp = new ScenarioMouseModePanel(md);
	     face.add(mmp);
     }
	 }

}
