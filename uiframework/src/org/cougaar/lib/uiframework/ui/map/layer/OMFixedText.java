package org.cougaar.lib.uiframework.ui.map.layer;

import java.util.Vector;

import java.awt.Font;

import com.bbn.openmap.omGraphics.*;

import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.LineType;

public class OMFixedText extends OMText
{

  private float fixedTextSize;
  public OMFixedText()
  {
    super();
  }

  public OMFixedText (float lat, float lon, float fixedSize, int latOffs, int lonOffs, String text, int just)
  {
    super (lat, lon, latOffs, lonOffs, text, just);

    fixedTextSize = fixedSize;
  }

  // swiped from OMText and modified to keep text same size
  public synchronized boolean generate(Projection proj)
  {
        // HACK synchronized because of various race conditions that need to
        // be sorted out.

        if (proj == null) {
            //Debug.message("omgraphic", "OMText: null projection in generate!");
            return false;
        }

        // flush the cached information about the bounding box.
        polyBounds = null;

        // Although it most definately has bounds, OMText is considered a
        // point object by the projection code.  We need to check to make
        // sure the point is plot-able: if not then don't display it.  This
        // might occur, for instance, if we're using the Orthographic and the
        // point is on the other side of the world.
        switch (renderType) {
        case RENDERTYPE_XY:
            pt = point;
            break;
        case RENDERTYPE_OFFSET:
            if (!proj.isPlotable(lat, lon)) {
/*                if (Debug.debugging("omGraphics"))
                    System.err.println(
                        "OMText.generate(): offset point is not plotable!"); */
                setNeedToRegenerate(true);//so we don't render it!
                return false;
            }
            pt = proj.forward(lat, lon);
            pt.translate(point.x, point.y);
            adjustTextFont (proj);
            break;
        case RENDERTYPE_LATLON:
            if (!proj.isPlotable(lat, lon)) {
/*                if (Debug.debugging("omGraphics"))
                    System.err.println(
                        "OMText.generate(): llpoint is not plotable!"); */
                setNeedToRegenerate(true);//so we don't render it!
                return false;
            }
            pt = proj.forward(lat, lon);
            adjustTextFont (proj);
            break;
        case RENDERTYPE_UNKNOWN:
            System.err.println(
                "OMText.render.generate(): invalid RenderType");
            return false;
        }
	// commented out to compile with 3.6.2
//         if (f == null) {
//             f = DEFAULT_FONT;
//         }

        setNeedToRegenerate(false);
        return true;
    }

  private void adjustTextFont (Projection proj)
  {

    LatLonPoint llp1 = new LatLonPoint (lat, lon);
    LatLonPoint llp2 = new LatLonPoint (lat + fixedTextSize, lon + fixedTextSize);

    Vector xys = proj.forwardLine(llp1, llp2, LineType.Straight, -1);

//    int x[] = (int[]) xys.elementAt(0);
    int y[] = (int[]) xys.elementAt(1);

//        System.out.println ("OMFixedText y range: " + (y[0] - y[1]) );
    int fontSize = (y[0] - y[1]) / 2;         // ( x[1] - x[0], y[0] - y[1]);
    if (fontSize > 0)
      f = new Font ( "SansSerif", Font.BOLD, fontSize );
    else
      setNeedToRegenerate (true); // so we don't render it!


  }
}
