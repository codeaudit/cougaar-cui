/* **********************************************************************
 * 
 *    Use, duplication, or disclosure by the Government is subject to
 * 	     restricted rights as set forth in the DFARS.
 *  
 * 			   BBNT Solutions LLC
 * 			      A Part of  
 * 			         GTE      
 * 			  10 Moulton Street
 * 			 Cambridge, MA 02138
 * 			    (617) 873-3000
 *  
 * 	  Copyright 1998, 2000 by BBNT Solutions LLC,
 * 		A part of GTE, all rights reserved.
 *  
 * **********************************************************************
 * 
 * $Source: /opt/rep/cougaar/cui/uiframework/src/org/cougaar/lib/uiframework/ui/components/IconScalePanel.java,v $
 * $Revision: 1.1 $
 * $Date: 2001-06-25 18:27:41 $
 * $Author: mdavis $
 * 
 * **********************************************************************
 */
package org.cougaar.lib.uiframework.ui.components;

import com.bbn.openmap.util.Debug;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import java.io.Serializable;
import java.net.URL;

import com.bbn.openmap.*;
import com.bbn.openmap.event.*;

/**
 * Bean to zoom the Map.
 * <p>
 * This bean is a source for ZoomEvents.  It is a simple widget with a ZoomIn
 * button and a ZoomOut button.  When a button is pressed, the appropriate
 * zoom event is fired to all registered listeners.
 * @see #addZoomListener
 */
public class IconScalePanel extends JPanel implements Serializable
{

    public final static transient String scaleUpCmd = "scaleUp";
    public final static transient String scaleDownCmd = "scaleDown";

    protected transient JButton scaleUpButton, scaleDownButton;

    /**
     * Construct the IconScalePanel.
     */
    public IconScalePanel(ActionListener actlis)
    {
      super();
//  	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	    scaleUpButton = addButton("scaleUp", "Scale Up", scaleUpCmd, actlis);
	    scaleDownButton = addButton("scaleDown", "Scale Down", scaleDownCmd, actlis);
    }


    /**
     * Add the named button to the panel.
     *
     * @param name GIF image name
     * @param info ToolTip text
     * @param command String command name
     *
     */
    protected JButton addButton(String name, String info, String command, ActionListener actlis)
    {
       
	        URL url = IconScalePanel.class.getResource(name + ".gif");
          
	        JButton b = new JButton(new ImageIcon(url, info));
	        b.setToolTipText(info);
	        b.setMargin(new Insets(0,0,0,0));
          b.setActionCommand(command);
	        b.addActionListener(actlis);
	        b.setBorderPainted(false);
	        add(b);
	        return b;
    }

    /*
    public static void main(String args[]) {

	final JFrame frame = new JFrame("IconScalePanel");
	//	frame.setSize(100, 32);
	IconScalePanel zp = new IconScalePanel();
	zp.setZoomInFactor(0.9f);
	zp.setZoomOutFactor(4.5f);
	frame.setVisible(true);
	frame.getContentPane().add(zp);
	frame.setSize(200,100);
	
    }
    */
}
