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
   import EDU.auburn.VGJ.util.DDimension;
   import EDU.auburn.VGJ.util.DPoint;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
   import java.lang.String;



/**
 * A "Clan" tree class for CGD.
 * </p>Here is the <a href="../algorithm/cgd/ClanTree.java">source</a>.
 */
   public class ClanTree
   {
      public ClanTree	parent;
      public ClanTree	firstChild;
      public ClanTree   nextSibling;
      public Clan	clan;
   
      public double minx, maxx, centerx;
   
      public ClanTree leftSibling;
      public DDimension size;
      public DPoint position;
      public double extraheight;
      public boolean dummy;
   
      public int heightInTree;
   
   
      public ClanTree()
      {
         parent = firstChild = nextSibling = null;
         clan = null;
         size = new DDimension(0, 0);
         position = new DPoint(0, 0);
         extraheight = 0.0;
      }
   
   
   
      public String toString()
      {
         return toString_(0, null);
      }
   
   
   
   // Translate indexes to ids for graph.
      public String toString(Graph graph)
      {
         return toString_(0, graph);
      }
   
   
   
   
      private String toString_(int indent, Graph graph)
      {
         String string = new String();
      
         int i;
         for(i = 0; i < indent; i++)
            string += "   ";
      
         if(graph != null)
            string += clan.toString(graph);
         else
            string += clan.toString();
         if(firstChild != null)
         {
            string += "\n";
         
            for(i = 0; i < indent + 1; i++)
               string += "   ";
            string += "(\n";
         
            ClanTree tmpclan;
            for(tmpclan = firstChild; tmpclan != null;
            tmpclan = tmpclan.nextSibling)
            {
               string += tmpclan.toString_(indent + 1, graph);
               string += "\n";
            }
            for(i = 0; i < indent + 1; i++)
               string += "   ";
            string += ")";
         }
         return string;
      }
   
   
   
      public void fixLinear(Set node_subset, Set child_relation[],
      Set parent_relation[])
      {
         ClanTree child;
      
         for(child = firstChild; child != null; child = child.nextSibling)
            child.fixLinear(node_subset, child_relation, parent_relation);
      
         if(clan.clanType != Clan.PRIMITIVE)
            return;
      
         boolean is_linear = true;
      
         ClanTree last = firstChild;
         ClanTree cur;
      
         for(cur = last.nextSibling; is_linear && cur != null;
         cur = last.nextSibling)
         {
         	// If the current source is not a child
         	// of the last sink, or the last sink
         	// is not a parent of the current source,
         	// then this is not a linear clan.
         
            Set last_children = new Set();
            last_children.union(child_relation[last.clan.sinks.first()]);
            last_children.intersect(node_subset);
            Set cur_parents = new Set();
            cur_parents.union(parent_relation[cur.clan.sources.first()]);
            cur_parents.intersect(node_subset);
         
            if(!(cur.clan.nodes.isSubset(last_children)) ||
            !(last.clan.nodes.isSubset(cur_parents)))
            {
               is_linear = false;
               break;
            }
            last = cur;
         }
         if(is_linear)
            clan.clanType = Clan.LINEAR;
      }
   
   
   
   
      public int numberOfChildren()
      {
         int num = 0;
         ClanTree tmpnode;
         for(tmpnode = firstChild; tmpnode != null;
         tmpnode = tmpnode.nextSibling)
            num++;
         return num;
      }
   
   
   }
