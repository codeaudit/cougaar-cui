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
*
* Date		Author
* 12/4/96	Larry Barowski
*
*/
// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.

package org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.cgd;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Set;





/**
 * A partition class for CGD.
 * </p>Here is the <a href="../algorithm/cgd/Partition.java">source</a>.
 */
public class Partition
{
private int sets_;
private Set allNodes_[];	// set of nodes in this partition
private Set equivTestSet_[];
private Set star_[];		// above set's nodes + descendents (ancestors)
private int type_;		// SIBLING or MATE

public final static int SIBLING = 0;
public final static int MATE = 1;



public Partition(int type, Set child_relation[], Set parent_relation[],
			Set descendent_relation[], Set ancestor_relation[],
			int numNodes_, Set node_subset)
	{
	type_ = type;

	// The number of sets in the partition will be at most the number
	// of nodes in the node subset.
	equivTestSet_ = new Set[node_subset.numberOfElements()];
	star_ = new Set[node_subset.numberOfElements()];
	allNodes_ = new Set[node_subset.numberOfElements()];

	sets_ = 0;

	// For each node, add to a set already found or start a new set.
	int i, j;
	for(i = node_subset.first(); i != -1; i = node_subset.next())
		{
		// Is node i equivalent to the first node of any of the sets?
		Set p;
		if(type_ == SIBLING)
			// p = parents of i
			p = (Set)(parent_relation[i].clone());
		else
			// p = children of i
			p = (Set)(child_relation[i].clone());
		p.intersect(node_subset);
		for(j = 0; j < sets_; j++)
			if(p.equals(equivTestSet_[j]))
				break;
		if(j >= sets_)
			// This is a new set.
			{
			sets_++;
			equivTestSet_[j] = p;
			allNodes_[j] = new Set();
			}

		allNodes_[j].includeElement(i);
		}
	for(j = 0; j < sets_; j++)
		{
		if(allNodes_[j] == null)
			allNodes_[j] = new Set();
		star_[j] = (Set)(allNodes_[j].clone());
		if(type_ == SIBLING)
			star_[j].indexedUnion(descendent_relation, allNodes_[j]);
		else
			star_[j].indexedUnion(ancestor_relation, allNodes_[j]);
		star_[j].intersect(node_subset);
		}
	}




public int size()
	{
	return sets_;
	}



public Set star(int i)
	{
	return star_[i];
	}



public Set members(int i)
	{
	return allNodes_[i];
	}



public String toString()
	{
	String result = new String();

	result += "Size: " + sets_ + "\n";
	int i;
	for(i = 0; i < sets_; i++)
		result += i + "\t" + allNodes_[i].toShortString() + "\n\t" +
				star_[i].toShortString() + "\n";

	return result;
	}
}
