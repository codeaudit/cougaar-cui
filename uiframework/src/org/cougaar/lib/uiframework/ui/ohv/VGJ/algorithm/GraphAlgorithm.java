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
	File: GraphAlgorithm.java
	5/22/96    Larry Barowski
*/



// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.


package org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm;



import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;



/**
 *	This interface is used to add algorithms to the graph tool.
 *      All that is required is that the algorithm object class
 *	has the function compute, and that it implements GraphAlgorithm.
 * 	</p>Here is the <a href="../algorithm/GraphAlgorithm.java">source</a>.
 *
 *@author	Larry Barowski
**/
public interface GraphAlgorithm
{



/**
 *  Apply the algorithm to graph. The return value should be null
 *  if successful, and an error message if unsuccessful.
 *@param update an object that allows the display to be updated
 *  from within the algorithm
 *@see GraphUpdate
**/
abstract public String compute(Graph graph, GraphUpdate update);

}
