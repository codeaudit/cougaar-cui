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
