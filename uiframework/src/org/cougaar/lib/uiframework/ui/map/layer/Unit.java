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
 * $Source: /opt/rep/cougaar/cui/uiframework/src/org/cougaar/lib/uiframework/ui/map/layer/Unit.java,v $
 * $Revision: 1.2 $
 * $Date: 2001-02-23 21:56:55 $
 * $Author: krotherm $
 * 
 * **********************************************************************
 */

package org.cougaar.lib.uiframework.ui.map.layer;

import java.io.*;
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
//import com.bbn.openmap.layer.location.*;
// import assessment.*;
import org.cougaar.lib.uiframework.transducer.XmlInterpreter;
import org.cougaar.lib.uiframework.transducer.elements.Structure;


public class Unit {
	String label;
	OMGraphic graphic;
	Hashtable data;
	

	Unit(String str, OMGraphic omg, Hashtable ht) {
	    label=str;
	    graphic=omg;
	    data=ht;
	}
	Float getData(String metric) { 
	    Float ret=null;
	    Object fl=data.get(metric);
	    if (fl!=null && fl instanceof Float) ret=(Float)fl;
	    return ret;
	}
	OMGraphic getGraphic() { return graphic; }
	void setColor(Color c) { 
	    // yuk yuk yuk
	    if (graphic instanceof VecIcon) {
		((VecIcon)graphic).setColor(c); 
	    } else if (graphic instanceof InfantryVecIcon) {
		((InfantryVecIcon)graphic).setColor(c); 
	    }
	}
	String getLabel() { return label; }
    }

