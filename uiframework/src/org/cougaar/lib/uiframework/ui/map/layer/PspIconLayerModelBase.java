

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

        private void makeInfantryUnit(float lat, float lon, Color color,
                                      String label, String msg, Hashtable data) 
        {
            OMGraphic omgraphic;

            omgraphic = new InfantryVecIcon(lat, lon, color); 
            ((InfantryVecIcon) omgraphic).addToMessage(msg);
            ((InfantryVecIcon) omgraphic).setLabel(label);
            markers.add(omgraphic);
            // omList.add(omgraphic);        

            Unit unit = new Unit(label, omgraphic, data);
            Unit u2 = (Unit) units.put(omgraphic, unit);
            if (u2 != null) {
                System.err.println("units.put returned non-null value-- inserted unit: "
                                   +unit+" key: "+omgraphic+" returned unit: "+u2);
            }
            
        }

        private void makeArmoredUnit(float lat, float lon, Color color, 
                                      String label, String msg, Hashtable data) 
        {
            OMGraphic omgraphic;

            omgraphic = new ArmoredVecIcon(lat, lon, color); 
            ((ArmoredVecIcon) omgraphic).addToMessage(msg);
            ((ArmoredVecIcon) omgraphic).setLabel(label);
            markers.add(omgraphic); 
            // omList.add(omgraphic);        

            Unit unit = new Unit(label, omgraphic, data);
            Unit u2 = (Unit) units.put(omgraphic, unit);
            if (u2 != null) {
                System.err.println("units.put returned non-null value-- inserted unit: "
                                   +unit+" key: "+omgraphic+" returned unit: "+u2);
            }
            
        }

        private OMGraphic makeIconGraphic(float lat, float lon, Color color, 
                                          String type) 
        {
            OMGraphic ret=null;
            if (type.equalsIgnoreCase("Armored")) {
                ret=new ArmoredVecIcon(lat, lon, color);
            }
            if (type.equalsIgnoreCase("Infantry")) {
                ret=new InfantryVecIcon(lat, lon, color);
            }
            if (ret==null) {
                ret=new VecIcon(lat, lon, color);
            }
            return ret;
        }

        
    
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

        System.out.println("XMLlayer.PspIconLayerModelBase *** uriString from environment is "+uriString);
        if (uriString==null)
        {
          uriString = new String();

            System.err.println("No PSP url provided in properties file, icons not displayed");
        }

//        obtainData();

        // System.out.println("leaving State()");

    }

/*
    public void obtainData()
    {

      System.err.println("PspIconLayerModelBase obtainData()");


      try
      {

//             XmlInterpreter xint = new XmlInterpreter();
//             Structure str;

                System.err.println("Attempting to read from URL: "+uriString);
                URL url = new URL(uriString);

                URLConnection urlCon = url.openConnection();

			           // Setup the communication parameters with the specifed URL
                 urlCon.setDoOutput(true);
			           urlCon.setDoInput(true);
			           urlCon.setAllowUserInteraction(false);

			           // Send the parameter (GET/POST data) to the specified URL
			           DataOutputStream server = new DataOutputStream(urlCon.getOutputStream());
			           server.writeBytes("all");
			           server.close();

                 InputStream is = urlCon.getInputStream();
                 ObjectInputStream ois = new ObjectInputStream(is);
                 allIcons = (Hashtable) ois.readObject();

           			 // MapLocationInfo o = (MapLocationInfo) ois.readObject();
                 // String symbol = null;
			           // if (o != null)
			           //   symbol = o.getSymbol();
			           // System.out.println("MapLocationInfo for " + symbol);


//            load(allIcons);

        }

        catch (Exception ex)
        {

          System.err.println ("unable to get location info from LocationCluster plugIn");

          ex.printStackTrace();

        }

   }
*/


   Iterator markerIterator() {
      Vector vec=markers.getTargets();
      if (vec==null) {
        vec=new Vector();
      }
            return vec.iterator();
    }
  }




