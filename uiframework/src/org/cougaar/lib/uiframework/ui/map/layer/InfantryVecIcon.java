/* **********************************************************************
 * 
 *  BBNT Solutions LLC, A part of GTE
 *  10 Moulton St.
 *  Cambridge, MA 02138
 *  (617) 873-2000
 * 
 *  Copyright (C) 1998, 2000
 *  This software is subject to copyright protection under the laws of 
 *  the United States and other countries.
 * 
 * **********************************************************************
 * 
 * $Source: /opt/rep/cougaar/cui/uiframework/src/org/cougaar/lib/uiframework/ui/map/layer/Attic/InfantryVecIcon.java,v $
 * $Revision: 1.1 $
 * $Date: 2001-02-15 13:20:58 $
 * $Author: krotherm $
 * 
 * **********************************************************************
 */

package org.cougaar.lib.uiframework.ui.map.layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import com.bbn.openmap.LatLonPoint;

import com.bbn.openmap.Layer;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMPoly;
import com.bbn.openmap.omGraphics.OMRaster;
import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.event.ProjectionEvent;

import com.bbn.openmap.event.*;
import com.bbn.openmap.layer.location.*;

//public class InfantryVecIcon extends OMGraphic {
// changing the following makes the casting to vecicon work, 
public class InfantryVecIcon extends VecIcon {
    float basepixyf=.2f;
    float basepixxf=.1f;
    float pixyf=basepixyf;
    float pixxf=basepixxf;
    float lw=3;
    float lw2=5;
    int scale=1;
    OMText label;
 	OMPoly bbox;


    OMGraphicList ogl=new OMGraphicList();
    String msg="Generic Infantry Default";
//     public String getMessage() { return msg; }
//     public void setMessage(String str) { msg=str; }
//     public void addToMessage(String str) { msg+=": "+str; }
    public void setLabel(String lab) { label.setData(lab); }
    public String getLabel() { return label.getData(); }
    InfantryVecIcon(float lat, float lon, Color bc) {
 	this(lat, lon, bc, Color.black, 1);
    }

    InfantryVecIcon(float lat, float lon, Color bc, Color c) {
 	this(lat, lon, bc, c, 1);
    }

    private void setScale(int scale) { 
	this.scale=scale; pixyf=scale*basepixyf; pixxf=scale*basepixxf; 
    }
    int getScale() { return scale; }
    void setColor(Color bc) { 	bbox.setFillColor(bc); }
    InfantryVecIcon(float lat, float lon, Color bc, Color c, int sc) {
	setScale(sc);

	label=new OMText(lat+(pixxf/2), lon+pixyf, 3, 1, "1BDE", 
				OMText.JUSTIFY_LEFT);
	ogl.add(label);

	bbox = new OMPoly(  
				  new float [] {
				      lat, lon, 
				      lat, lon+pixyf, 
				      lat+pixxf, lon+pixyf, 
				      lat+pixxf, lon,
				      lat, lon
				  },
				  OMGraphic.DECIMAL_DEGREES,
				  OMGraphic.LINETYPE_STRAIGHT
				  );
	bbox.setFillColor(bc);
	bbox.setLineColor(c);
	bbox.setLineWidth(lw);
	
	OMPoly poly = new OMPoly(  
				 new float [] {
				     lat, lon, lat+pixxf, lon+pixyf, 
				     lat, lon+pixyf, lat+pixxf, lon,
				 },
				 OMGraphic.DECIMAL_DEGREES,
				 OMGraphic.LINETYPE_STRAIGHT
				 );
	poly.setLineColor(c);
	poly.setLineWidth(lw2);
	ogl.add(poly);
	ogl.add(bbox);
	
    }
    // OMGraphic requirements
    public float distance(int x, int y) { return ogl.distance(x,y); }
    public void render(Graphics g) { ogl.render(g); }
    public boolean generate(Projection  x) { return ogl.generate(x); }
} // end-class
