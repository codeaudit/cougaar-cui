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
