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
 *File: ExampleAlg2.java
 *
 * Date      Author
 * 10/9/96   Larry Barowski
 *
 */
// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.



package org.cougaar.lib.uiframework.ui.ohv.VGJ.examplealg;


import java.util.Random;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.GraphUpdate;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.GraphAlgorithm;
import EDU.auburn.VGJ.util.DPoint;

import java.lang.System;


/**
 *	This example randomly moves the nodes. It does this 100 times, and
*  updates the display and waits 5 milliseconds in between.
 *	</p>Here is the <a href="../examplealg/ExampleAlg2.java">source</a>.
 *
**/




public class ExampleAlg2 implements GraphAlgorithm
{

public String compute(Graph graph, GraphUpdate update)
	{
	Node tmpnode;
	DPoint pos;
	Random random = new Random();
	int i;
	double xshift, yshift;

	for(i = 0; i < 100; i++)
		{
		for(tmpnode = graph.firstNode(); tmpnode != null;
				tmpnode = graph.nextNode(tmpnode))
			{
			pos = tmpnode.getPosition();

			xshift = (random.nextDouble() - .5) * 10.0;
			yshift = (random.nextDouble() - .5) * 10.0;

			tmpnode.setPosition(pos.x + xshift, pos.y + yshift);

			}
		update.update(false);
		/* -- Netscape does not allow wait().
		try
			{
			wait(5);
			}
		catch(Exception e)
			;*/
		}
	return null;
	}
}
