/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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
