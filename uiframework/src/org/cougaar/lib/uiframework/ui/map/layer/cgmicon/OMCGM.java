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

import java.util.Vector;

import java.io.DataInputStream;
import java.io.FileInputStream;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;

import org.cougaar.lib.uiframework.ui.map.layer.cgmicon.cgm.*;

import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMCircle;
import com.bbn.openmap.omGraphics.OMLine;
import com.bbn.openmap.omGraphics.OMText;

import com.bbn.openmap.proj.Projection;

import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.proj.ProjMath;

import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.LineType;


public class OMCGM extends OMGraphic
{

  public static final String SQUAD_SIZE="squad";
  public static final String SECTION_SIZE="section";
  public static final String PLATOON_SIZE="platoon";
  public static final String COMPANY_SIZE="company";
  public static final String BATTALION_SIZE="battalion";
  public static final String REGIMENT_SIZE="regiment";
  public static final String BRIGADE_SIZE="brigade";
  public static final String DIVISION_SIZE="division";
  public static final String CORPS_SIZE="corps";
  public static final String ARMY_SIZE="army";

  protected OpenMapCGMDisplay omcgmdisp;

  protected float latOrigin, lonOrigin;  // in radians
  protected float degLat, degLon; // in decimal degrees

  protected int xOrigin, yOrigin;

  // keep track of the decorations on the CGM display
  // like unit strength symbology, labels, and HUD-type clutter
  protected OMGraphicList ogl = new OMGraphicList();

  protected String cgmFileName;
  protected String unitSize = null;

  public OMCGM ()
  {
      super(RENDERTYPE_LATLON, LINETYPE_UNKNOWN, DECLUTTERTYPE_NONE);
  }

  public OMCGM( String cgmFile) throws java.io.IOException
  {
    super(RENDERTYPE_LATLON, LINETYPE_UNKNOWN, DECLUTTERTYPE_NONE);

    cgmFileName = new String (cgmFile);

    CGM cgm = new CGM ();
    cgm.read ( new DataInputStream ( new FileInputStream (cgmFile) ) );
    omcgmdisp = new OpenMapCGMDisplay (cgm);


  }


  public OMCGM( String cgmFile, String unitSizeDesignation) throws java.io.IOException
  {
    super(RENDERTYPE_LATLON, LINETYPE_UNKNOWN, DECLUTTERTYPE_NONE);

    cgmFileName = new String (cgmFile);
    unitSize = new String (unitSizeDesignation);

    CGM cgm = new CGM ();

    cgm.read ( new DataInputStream ( new FileInputStream (cgmFile) ) );
    omcgmdisp = new OpenMapCGMDisplay (cgm);

  }

    //
    // Swiped from OMPoly.java ... heh-heh-heh
    //

    /**
     * Set the location based on a latitude, longitude, and some xy
     * points.
     * The coordinate mode and the polygon setting are the same as in
     * the constructor used.  This is for RENDERTYPE_OFFSET polys.
     *
     * @param latPoint latitude in decimal degrees
     * @param lonPoint longitude in decimal degrees
     * @param units radians or decimal degrees.  Use OMGraphic.RADIANS
     * or OMGraphic.DECIMAL_DEGREES
     * @param xypoints array of x/y points, arranged x, y, x, y, etc.
     */
    public void setLocation(float latPoint, float lonPoint, int units) {

      degLat = latPoint;
      degLon = lonPoint;

      if (units == OMGraphic.DECIMAL_DEGREES)
      {
        latOrigin = ProjMath.degToRad(latPoint);
        lonOrigin = ProjMath.degToRad(lonPoint);
      } else {
        latOrigin = latPoint;
        lonOrigin = lonPoint;
      }

      setNeedToRegenerate(true);
      setRenderType(RENDERTYPE_LATLON);

    }


  public boolean generate (Projection proj)
  {

//    System.out.println ("generate, degLon is: " + degLon);

    /**
     * Establish new x and y origin based on projection.
     *
     * @param proj Projection
     * @return true if generate was successful
     */

    int i, npts;

    if (proj == null)
    {
      System.err.println ("omgraphic: OMCGM: null projection in generate!");
      return false;
    }

    // forward project the radian point
    Point origin = proj.forward( latOrigin, lonOrigin, new Point(0,0), true );//radians

    xOrigin = origin.x;
    yOrigin = origin.y;

    LatLonPoint llp1 = new LatLonPoint (latOrigin, lonOrigin);
    // LatLonPoint llp2 = new LatLonPoint (latOrigin + 0.1, lonOrigin + 0.1);
    LatLonPoint llp2 = new LatLonPoint (latOrigin + 0.3, lonOrigin + 0.3);

    Vector xys = proj.forwardLine(llp1, llp2, LineType.Straight, -1);

    int x[] = (int[]) xys.elementAt(0);
    int y[] = (int[]) xys.elementAt(1);

//        System.out.println ("x range: " + (x[1] - x[0]) );
//        System.out.println ("y range: " + (y[0] - y[1]) );

    omcgmdisp.setOrigin(xOrigin, yOrigin);
    omcgmdisp.scale( x[1] - x[0], y[0] - y[1]);

    if (unitSize != null)
    {
      drawUnitSizeDesignation (unitSize, latOrigin, lonOrigin);
      ogl.generate(proj);
    }


    setNeedToRegenerate(false);

    return true;


  }


  public void render (Graphics g)
  {

//     System.out.println ("render, degLon is: " + degLon);

     if (getNeedToRegenerate() || !isVisible())
       return;

     if (unitSize != null)
     {
       ogl.render(g);
     }

     omcgmdisp.paint(g);

  }

   public float distance (int x, int y)
   {
     return (float) 1.0;
   }

   public OMCGM makeAnother()
   {

     try
     {

       OMCGM mycopy = new OMCGM();
       if (this.unitSize != null)
         mycopy.unitSize = new String (this.unitSize);
       mycopy.cgmFileName = new String (this.cgmFileName);
       mycopy.omcgmdisp = (OpenMapCGMDisplay) (this.omcgmdisp.makeAnother());

       return mycopy;

     }

     catch (Throwable ioexc)
     {
       System.err.println (ioexc.toString());
       ioexc.printStackTrace();
     }

     return null;

   }

   protected void drawUnitSizeDesignation (String unitSize, float xCoord, float yCoord)
   {

//       System.out.println ( "unit size is: " + unitSize);
     if (xCoord < 0.0)
       xCoord = (float) 360.0 - xCoord;

     if (yCoord < 0.0)
       yCoord = (float) 360.0 - yCoord;

     if ( unitSize.equals( SQUAD_SIZE) )
     {

       OMCircle dot1= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.05, (float) 0.005);
/*
       try {
       dot1.setlinepaint ( Color.black);
       } catch (Exception ex) {
	   ex.printStackTrace();
       }
*/
       //dot1.setfillpaint(Color.black);

       ogl.add(dot1);


     }

     else if ( unitSize.equals( SECTION_SIZE) )
     {

       OMCircle dot1= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.04, (float) 0.005);
       OMCircle dot2= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.06, (float) 0.005);
/* openmap 4.0 specific
       try {
	   dot1.setlinepaint ( Color.black);
	   dot2.setlinepaint ( Color.black);
       } catch (Exception ex) {
	   ex.printStackTrace();
       }
*/
       //dot1.setfillpaint(Color.black);
       //dot2.setfillpaint(Color.black);

       ogl.add(dot1);
       ogl.add(dot2);

     }

     else if ( unitSize.equals( PLATOON_SIZE) )
     {

       OMCircle dot1= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.03, (float) 0.005);
       OMCircle dot2= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.05, (float) 0.005);
       OMCircle dot3= new OMCircle ( degLat + (float) 0.08, degLon + (float)0.07, (float) 0.005);
/* openmap 4.0 specific
       try {
       dot1.setlinepaint ( Color.black);
       dot2.setlinepaint ( Color.black);
       dot3.setlinepaint ( Color.black);
       } catch (Exception ex) {
	   ex.printStackTrace();
       }

       dot1.setfillpaint(Color.black);
       dot2.setfillpaint(Color.black);
       dot3.setfillpaint(Color.black);
*/
       ogl.add(dot1);
       ogl.add(dot2);
       ogl.add(dot3);

     }

     else if ( unitSize.equals (COMPANY_SIZE) )
     {

       float lineLon = degLon + (float) 0.05;
       OMLine line1 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       ogl.add(line1);

     }

     else if ( unitSize.equals (BATTALION_SIZE) )
     {

       float lineLon = degLon + (float) 0.04;
       OMLine line1 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       lineLon = degLon + (float) 0.06;
       OMLine line2 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       ogl.add (line1);
       ogl.add (line2);

     }

     else if ( unitSize.equals (REGIMENT_SIZE) )
     {

       float lineLon = degLon + (float) 0.03;
       OMLine line1 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       lineLon = degLon + (float) 0.05;
       OMLine line2 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       lineLon = degLon + (float) 0.07;
       OMLine line3 = new OMLine (degLat + (float) 0.0675, lineLon, degLat + (float) 0.085, lineLon, OMLine.LINETYPE_STRAIGHT );

       ogl.add(line1);
       ogl.add(line2);
       ogl.add(line3);

     }

     else if ( unitSize.equals (BRIGADE_SIZE) )
     {

       drawX ( degLat + (float) 0.0675, degLon + (float) 0.05 );

     }
     else if ( unitSize.equals (DIVISION_SIZE) )
     {

       drawX ( degLat + (float) 0.0675, degLon + (float) 0.04 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.06 );

     }

     else if ( unitSize.equals (CORPS_SIZE) )
     {

       drawX ( degLat + (float) 0.0675, degLon + (float) 0.03 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.05 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.07 );

     }

     else if ( unitSize.equals (ARMY_SIZE) )
     {

       drawX ( degLat + (float) 0.0675, degLon + (float) 0.02 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.04 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.06 );
       drawX ( degLat + (float) 0.0675, degLon + (float) 0.08 );

     }
   }

   private void drawX (float lat, float lon)
   {
      float llat = lat;
      float ulat = lat + (float) 0.0175;

      float llon = lon - (float) 0.0075;
      float rlon = lon + (float) 0.0075;

      OMLine line1 = new OMLine (llat, llon, ulat, rlon, OMLine.LINETYPE_STRAIGHT );

      OMLine line2 = new OMLine (llat, rlon, ulat, llon, OMLine.LINETYPE_STRAIGHT );

      ogl.add (line1);
      ogl.add (line2);

   }

   public void showCGMCommands()
   {
    omcgmdisp.showCGMCommands();
    }

    public void changeColor(Color oldc, Color newc)
    {
      omcgmdisp.changeColor(oldc,newc);
    }

}
