/* **********************************************************************
 *
 *  Clark Software Engineering, Ltd.
 *  5100 Springfield St. Ste 308
 *  Dayton, OH 45431-1263
 *  (937) 256-7848
 *
 *  Copyright (C) 2001
 *  This software is subject to copyright protection under the laws of
 *  the United States and other countries.
 *
 */

package org.cougaar.lib.uiframework.ui.map.layer.cgmicon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.bbn.openmap.Layer;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMPoly;
import com.bbn.openmap.event.ProjectionEvent;

//import org.cougaar.lib.uiframework.ui.map.layer.ArmoredVecIcon;
//import org.cougaar.lib.uiframework.ui.map.layer.InfantryVecIcon;


/**
 * Layer objects are components which can be added to the MapBean to
 * make a map.
 * <p>
 * Layers implement the ProjectionListener interface to listen for
 * ProjectionEvents.  When the projection changes, they may need to
 * refetch, regenerate their graphics, and then repaint themselves
 * into the new view.
 */
public class IconLayer extends Layer {

    protected OMGraphicList graphics;


    /**
     * Construct the layer.
     */
    public IconLayer () {
	super();
	graphics = new OMGraphicList(10);
	createGraphics(graphics);
    }

    /**
     * Sets the properties for the <code>Layer</code>.  This allows
     * <code>Layer</code>s to get a richer set of parameters than the
     * <code>setArgs</code> method.
     * @param prefix the token to prefix the property names
     * @param props the <code>Properties</code> object
     */
    public void setProperties(String prefix, java.util.Properties props) {
	super.setProperties(prefix, props);
    }

    /**
     * Invoked when the projection has changed or this Layer has been
     * added to the MapBean.
     * @param e ProjectionEvent
     */    
    public void projectionChanged (ProjectionEvent e) {
	graphics.generate(e.getProjection());
	repaint();
    }

    /**
     * Paints the layer.
     * @param g the Graphics context for painting
     */
    public void paint (Graphics g) {
	graphics.render(g);
    }

    /**
     * Create graphics.
     */
    protected void createGraphics (OMGraphicList list)
    {
	    // NOTE: all this is very non-optimized...

      // Create the icons
      try
      {

        OMCGMIcons cgmicons = new OMCGMIcons ("../config/cgmload.txt");

        OMCGM linfIcon = cgmicons.get ("light infantry");
        linfIcon.setLocation ( (float) 10.0, (float) 0.0, OMGraphic.DECIMAL_DEGREES);
        list.add(linfIcon);

        
        OMCGM armorIcon = cgmicons.get ("armor");
        armorIcon.setLocation( (float) 10.1, (float) 0.0, OMGraphic.DECIMAL_DEGREES);
        list.add(armorIcon);

        OMCGM mechIcon = cgmicons.get ("mechanized infantry");
        mechIcon.setLocation( (float) 10.0, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(mechIcon);

        OMCGM infIcon = cgmicons.get ("infantry");
        infIcon.setLocation( (float) 10.1, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(infIcon);


        OMCGM infIcon2 = infIcon.makeAnother();
        infIcon2.setLocation( (float) 10.1, (float) 0.8, OMGraphic.DECIMAL_DEGREES);
        list.add(infIcon2);


	OMCGM infIcon3 = infIcon.makeAnother();
        infIcon3.setLocation( (float) 10.1, (float) 2.0, OMGraphic.DECIMAL_DEGREES);
        list.add(infIcon3);



        OMCGM icon;

        icon = cgmicons.get ("armored cavalry");
        icon.setLocation( (float) 10.2, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("recon");
        icon.setLocation( (float) 10.3, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("airborne");
        icon.setLocation( (float) 10.4, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("air assault");
        icon.setLocation( (float) 10.5, (float) 1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("field artillery");
        icon.setLocation( (float) 10.5, (float) 1.1, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("air defense");
        icon.setLocation( (float) 10.3, (float) 1.2, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("anti-armor");
        icon.setLocation( (float) 10.2, (float) 0.9, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("aviation");
        icon.setLocation( (float) 10.2, (float) -0.2, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("attack helicopter");
        icon.setLocation( (float) 10.0, (float) -0.1, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("air cavalry");
        icon.setLocation( (float) 10.3, (float) 0.1, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("engineer");
        icon.setLocation( (float) 10.2, (float) 0.3, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("chemical");
        icon.setLocation( (float) 10.3, (float) -0.3, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("signal");
        icon.setLocation( (float) 10.2, (float) -0.3, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("intelligence");
        icon.setLocation( (float) 10.4, (float) -0.3, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("transport");
        icon.setLocation( (float) 10.35, (float) -0.5, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("supply");
        icon.setLocation( (float) 10.25, (float) -0.5, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("maintenance");
        icon.setLocation( (float) 10.2, (float) -0.7, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("subsistence");
        icon.setLocation( (float) 10.4, (float) -1.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("clothing");
        icon.setLocation( (float) 10.2, (float) -2.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("petroleum");
        icon.setLocation( (float) 10.35, (float) -0.9, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("construction materials");
        icon.setLocation( (float) 10.2, (float) -0.9, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("ammunition");
        icon.setLocation( (float) 9.9, (float) -2.2, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);


        icon = cgmicons.get ("personal demand");
        icon.setLocation( (float) 10.1, (float) -2.2, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        
        icon = cgmicons.get ("medical");
        icon.setLocation( (float) 10.0, (float) -2.4, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        icon = cgmicons.get ("major end items");
        icon.setLocation( (float) 10.0, (float) -2.0, OMGraphic.DECIMAL_DEGREES);
        list.add(icon);

        // new CGM icon created via Visio, requires special scaling (sporadic display also)
        icon = cgmicons.get ("medical supply");
        OMCGMbyVisio visIcon = new OMCGMbyVisio (icon);
        visIcon.setLocation( (float) 10.0, (float) -2.2, OMGraphic.DECIMAL_DEGREES);
        list.add(visIcon);

    }

    catch (java.io.IOException ioexc)
    {
        System.err.println (ioexc.toString());
        ioexc.printStackTrace();
    }

  }
}
