/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
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
import com.bbn.openmap.omGraphics.OMText;
import com.bbn.openmap.omGraphics.OMRaster;
import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.event.ProjectionEvent;

import com.bbn.openmap.event.*;
import com.bbn.openmap.layer.location.*;

public class IntelVecIcon extends VecIcon
{

    float basepixyf=.1f;
    float basepixxf=.05f;
    float pixyf=basepixyf;
    float pixxf=basepixxf;
    float lw=3;
    float lw2=5;
    int scale=1;
    OMText label;
       OMPoly bbox;

    OMGraphicList ogl=new OMGraphicList();
    String msg="Generic CEWI (Intelligence) Default";
//     public String getMessage() { return msg; }
//     public void setMessage(String str) { msg=str; }
//     public void addToMessage(String str) { msg+=": "+str; }

    public void setLabel(String lab) { label.setData(lab); }

    public String getLabel() { return label.getData(); }

    public IntelVecIcon(float lat, float lon, Color bc)
    {
      this(lat, lon, bc, Color.black, 1);
    }

    public IntelVecIcon(float lat, float lon, Color bc, Color c)
    {
      this(lat, lon, bc, c, 1);
    }

    private void setScale(int scale) {
    this.scale=scale; pixyf=scale*basepixyf; pixxf=scale*basepixxf;
    }

    int getScale() { return scale; }

    void setColor(Color bc) { 	bbox.setFillColor(bc); }

    // Constructor
    IntelVecIcon(float lat, float lon, Color bc, Color c, int sc)
    {

      setScale(sc);

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
//        bbox.setLineWidth(lw);

        OMFixedText cewiText = new OMFixedText ( lat+(pixxf/3),
                                                 lon+(pixyf/5),
                                                 0.05f,
                                                 0,
                                                 0,
                                                 "CEWI",
                                                 new Font ("SansSerif", Font.BOLD, 1),
                                                 OMText.JUSTIFY_LEFT);

        ogl.add(cewiText);
        ogl.add(bbox);

    }
    // OMGraphic requirements
    public float distance(int x, int y) { return ogl.distance(x,y); }
    public void render(Graphics g) { ogl.render(g); }
    public boolean generate(Projection  x) { return ogl.generate(x); }
} // end-class  public IntelVecIcon()

