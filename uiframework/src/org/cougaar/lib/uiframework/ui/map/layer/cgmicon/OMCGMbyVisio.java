package org.cougaar.lib.uiframework.ui.map.layer.cgmicon;

import java.util.Vector;

import java.awt.Point;

import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.LineType;

public class OMCGMbyVisio extends OMCGM
{

  private final double VISIO_SCALE_FACTOR=0.8;
  private final float VISIO_OFFSET=(float)0.0002;

  public OMCGMbyVisio()
  {
  }

  public OMCGMbyVisio(OMCGM copyMe)
  {

    if (copyMe.unitSize != null)
         this.unitSize = new String (copyMe.unitSize);

    this.cgmFileName = new String (copyMe.cgmFileName);
    this.omcgmdisp = (OpenMapCGMDisplay) (copyMe.omcgmdisp.makeAnother());

  }

  public boolean generate (Projection proj)
  {

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
    Point origin = proj.forward( latOrigin + VISIO_OFFSET, lonOrigin + VISIO_OFFSET, new Point(0,0), true );//radians

    xOrigin = origin.x;
    yOrigin = origin.y;

    LatLonPoint llp1 = new LatLonPoint (latOrigin, lonOrigin);
    LatLonPoint llp2 = new LatLonPoint (latOrigin + 0.1, lonOrigin + 0.1);

    Vector xys = proj.forwardLine(llp1, llp2, LineType.Straight, -1);

    int x[] = (int[]) xys.elementAt(0);
    int y[] = (int[]) xys.elementAt(1);

//        System.out.println ("x range: " + (x[1] - x[0]) );
//        System.out.println ("y range: " + (y[0] - y[1]) );

    omcgmdisp.setOrigin(xOrigin, yOrigin);

    // Visio makes the icons too big scale them down further
    double adjuster;

    adjuster = (double) x[1] - (double) x[0];
    int adjustedX = (int) ((adjuster * VISIO_SCALE_FACTOR) + 0.5);

    adjuster = (double) y[0] - (double) y[1];
    int adjustedY = (int) ((adjuster * VISIO_SCALE_FACTOR) + 0.5);

//        System.out.println ("adjusted x range: " + adjustedX );
//        System.out.println ("adjusted y range: " + adjustedY );

    omcgmdisp.scale( adjustedX, adjustedY);

    if (unitSize != null)
    {
      drawUnitSizeDesignation (unitSize, latOrigin, lonOrigin);
      ogl.generate(proj);
    }


    setNeedToRegenerate(false);

    return true;

  }

} 