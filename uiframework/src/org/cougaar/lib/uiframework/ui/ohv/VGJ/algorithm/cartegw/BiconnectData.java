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
//
// ********************************************************************
// **
// **	Author		: cartegw@humsci.auburn.edu	Gerald Carter
// **	Date Created	: 961021
// **
// **	filename	: BiconnectData.java
// **
// **	Description	
// **	-----------
// **	Data object for storing node information used in implementing 
// **	J.E. Hopcroft's depth-first traversal of a graph to determine 
// **	the Articulation Points of the graph.
// **
// **	The private members of the class are as follows...
// **	
// **	int	num; 		// the number of the node as determined 
// **				// by the DFS
// **	int	low;		// lowest numbered node reachable by n 
// **				// edges and at most on back edge
// **	boolean visited;	// has the node been previously visited?
// **	boolean articulationPt;	// is the node an articulation point?
// **
// **	------------
// **	Modification
// **	------------
// **
// ********************************************************************
//
// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.

package org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.cartegw;

/**
 *  --- Description
        Data object for generic information field in Graph class.  Used by class 
        BiconnectGraph.
 * </p>Here is the <a href="../algorithm/cartegw/BiconnectData.java">source</a>.
 */

public class BiconnectData {
   private int		num;
   private int		low;
   private boolean	articulationPt;
   private boolean 	visited;
   private int		childCount;
   private int		node_index;

   // Default constructor
   public BiconnectData () {
      num = -1;
      low = -1;
      articulationPt = false;
      visited = false;
      childCount = 0;
      node_index = -1;
   }

   // Set and Get methods
   public boolean SetNumber ( int x ) { num = x; return ( true ); }
   public int GetNumber () { return ( num ); }
   public boolean SetLow ( int x ) { low = x; return ( true ); }
   public int GetLow () { return ( low ); }
   public boolean SetArticulationPoint ( boolean x ) 
      { articulationPt = x; return ( true ); }
   public boolean IsArticulationPoint () { return ( articulationPt ); }
   public boolean SetVisited ( boolean x ) { visited = x; return ( true ); }
   public boolean Visited () { return ( visited ); }
   public int GetChildCount () { return ( childCount ); }
   public int SetChildCount ( int x ) { childCount = x; return ( childCount ); }
   public int SetIndex ( int x ) { return ( node_index = x ); }
   public int GetIndex ( ) { return ( node_index ); }
}

// ******* end of BiconnectData.java **********************************
// ********************************************************************
