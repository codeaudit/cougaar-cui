/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
	File: OffsetCanvas.java
	5/29/96   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.

*/


   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;


   import java.awt.Dimension;
   import java.awt.Point;
   import java.awt.Canvas;

   import EDU.auburn.VGJ.util.DPoint;
   import EDU.auburn.VGJ.util.DDimension;

/**
 *	This abstract class is used inside a ScrolledPanel, and
 *	implements the required functionality to communicate with it.
 *	</p>Here is the <a href="../gui/OffsetCanvas.java">source</a>.
 *
 *@see		ScrolledPanel
 *@author	Larry Barowski
**/
   public abstract class OffsetCanvas extends Canvas
   {
   
   
   
   
   
   /**
    *	Event id for size change. Subclasses must post an event with this id
    *	when their contents or windows are resized.
    **/
      public static int	RESIZE = 32450;
   
   /**
    *	Event id for changing the label above the OffsetCanvas.
    *	The arg field of the event must contain the string.
    **/
      public static int	LABEL = 32451;
   
   
   
   
   /**
    *	Adjust the offset of the contents of the canvas. These must have the
    *	following meaning: screen position + offset = contents position.
    *	e.g. if xoffset is 10, contents starts 10 pixels to the left of the
    *	screen.
    *
    *@param xoffset horizontal offset
    *@param yoffset vertical offset
    **/
      abstract public void setOffsets(double xoffset, double yoffset, boolean redraw);
   
   
   
   
   
   /**
    *	Return the size of the contents.
    **/
      abstract public DDimension contentsSize();
   
   
   
   
   
   
      abstract public DPoint getOffset()
      ;
   }
