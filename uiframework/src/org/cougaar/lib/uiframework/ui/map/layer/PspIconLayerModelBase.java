

package org.cougaar.lib.uiframework.ui.map.layer;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.net.*;


import com.bbn.openmap.LatLonPoint;

import com.bbn.openmap.Layer;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMPoly;
import com.bbn.openmap.omGraphics.OMRaster;
import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.event.ProjectionEvent;
import com.bbn.openmap.Environment;

import com.bbn.openmap.event.*;

class PspIconLayerModelBase
{
    OMGraphicList markers = new OMGraphicList();

    Hashtable units=new Hashtable();
    Hashtable allIcons = new Hashtable();

    void resetMarkers() {
      markers=new OMGraphicList();   // do not clear()
      units=new Hashtable();         // do not clear()
   }


  OMGraphic findClosest(int x, int y, float limit) {
    return markers.findClosest(x, y, limit);
  }

    UnitTypeDictionary unitTypeDictionary=new UnitTypeDictionary();

/*
        private OMGraphic makeIconGraphic(float lat, float lon, Color color,
                                          String type)
        {
            OMGraphic ret= new VecIcon(lat, lon, color);
            return ret;
        }

*/        
    
         protected String getUnitType(String unitName)
         {
            return unitTypeDictionary.getUnitType(unitName);
         }


        Unit getUnit(OMGraphic g) {
            return (Unit) units.get(g);
        }

        Unit setUnit(Unit unit) { return (Unit) units.put(unit.getGraphic(), unit); }


    String uriString=null;

    PspIconLayerModelBase()
    {
        OMGraphic omgraphic;

        uriString = Environment.get("pspicon.locations.url");

//        System.out.println("PspIconLayerModelBase *** uriString from environment is "+uriString);
        if (uriString==null)
        {
          uriString = new String();

            System.err.println("No PSP url provided in properties file, icons not displayed");
        }

    }



   Iterator markerIterator() {
      Vector vec=markers.getTargets();
      if (vec==null) {
        vec=new Vector();
      }
            return vec.iterator();
    }
  }




