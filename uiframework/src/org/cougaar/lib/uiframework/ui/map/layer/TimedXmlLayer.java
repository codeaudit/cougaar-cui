package org.cougaar.lib.uiframework.ui.map.layer;

import java.util.*;
import  com.bbn.openmap.Layer;
import com.bbn.openmap.event.*;
import com.bbn.openmap.omGraphics.*;
import com.bbn.openmap.proj.Projection;

public class TimedXmlLayer extends XmlLayerBase implements MapMouseListener {
  long time;
  final static public String EPOCH="Epoch";
  final static public String LAST_TIME="End Time";
  TimedXmlLayerModel myState;

  OMGraphic findClosest(int x, int y, float limit) {
    return myState.findClosest(x,y,limit);
  }
    Iterator markerIterator() {
      return myState.markerIterator();
    }
  Unit getUnit(OMGraphic omgr) {
    return myState.getUnit(omgr);
  }

  public long getTime() { return time; }
   public void setTime(long ltime) { time=ltime; }
  public void setTime(String ltime) {
    System.err.println("Setting time to: "+ltime);
    if (ltime.equalsIgnoreCase(EPOCH)) {
      time=Long.MIN_VALUE;
    } else if (ltime.equalsIgnoreCase(LAST_TIME)) {
      time=Long.MAX_VALUE;
    } else {
      Long tmp=Long.decode(ltime);
      time=(tmp==null)?Long.MIN_VALUE:tmp.longValue();
    }
    System.err.println("Tiem is: "+time);

    myState.setTime(time);
    createGraphics(graphics);
    repaintLayer();
  }

    /**
     * Construct the layer.
     */
    public TimedXmlLayer () {
	super();
	initMyState();
	createGraphics(graphics);
	// worker.start();
    }

    public Collection getTransitionTimes() {
      return myState.getTransitionTimes();
    }
    void initMyState() {
      myState=new TimedXmlLayerModel();
      setTime(1);
    }

    protected void createGraphics (OMGraphicList list) {
	System.out.println("TcG list.size(): "+list.size());
	
	OMGraphic obj=null;
	list.clear();
	for (Iterator it=myState.markerIterator(); it.hasNext(); ) {
	    obj = (OMGraphic)it.next();
	    System.out.println("Tcg adding :"+obj);
	    list.add(obj);
	}
    }

}
