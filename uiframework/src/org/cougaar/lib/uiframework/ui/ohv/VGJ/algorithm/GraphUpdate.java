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
/*
	File: GraphUpdate.java
	5/22/96    Larry Barowski
*/
// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.


   package org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm;

   import java.awt.Frame;
   import EDU.auburn.VGJ.util.DRect;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;



/**
 *	This interface represents an updatable graph display.
 *	</p>Here is the <a href="../algorithm/GraphUpdate.java">source</a>.
 *
 *@author	Larry Barowski
**/
   public interface GraphUpdate
   {
   
   
   
   /**
   *  Update the display. If adjust_bounds is set, the boundaries
   *  are recomputed (basically, the controls get updated as well
   *  as the display, and this will be slow for large graphs).
   **/
      abstract public void update(boolean adjust_bounds);
   
   
   
   /**
   *  Set the scale value for display, and update the display. scaleval is
   *  interpreted as follows: screen_distance = physical_distance * scaleval.
   **/
      abstract public void scale(double scaleval);
   
   
   
   /**
   *  Update the display and boundaries, and center the graph in the
   *  display window.
   **/
      abstract public void center();
   
   
   
   
   /**
   *  Get the position and dimensions of the display window.
   **/
      abstract public DRect windowRect();
   
   
   
      abstract public double getHSpacing();
      abstract public double getVSpacing();
   
   
   
   /**
   * Get an application Frame from which to pop up windows.
   **/
      abstract public Frame getFrame();
   
   
   
   /**
   * Get the index of the selected node. -1 is returned if no node is selected.
   **/
      abstract public Node getSelectedNode();
   }
