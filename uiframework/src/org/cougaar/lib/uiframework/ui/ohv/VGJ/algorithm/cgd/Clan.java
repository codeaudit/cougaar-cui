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
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;
   import java.lang.String;


/**
 * A "Clan" class for CGD.
 * </p>Here is the <a href="../algorithm/cgd/Clan.java">source</a>.
 */
   public class Clan
   {
      public final static int	UNKNOWN = 0;
      public final static int	INDEPENDENT = 1;
      public final static int	LINEAR = 2;
      public final static int	PRIMITIVE = 3;
      public final static int	PSEUDOINDEPENDENT = 4;
      public final static int	SINGLETON = 5;
   
      public int clanType;
      public int id;
      public Set nodes;
      public Set sources;
      public Set sinks;
      public int size;	// number of nodes in the clan
      public int order;	// ordering for clan
   			//  is just the topo. no. from any node
   
      public Clan next;	// For use in stack of clans.
      public Clan listnext;	// For use in list of clans.
   
   
   
      public Clan(int type, Set nodes_in, Set sources_in, Set sinks_in,
      int order_in)
      {
         clanType = type;
         nodes = nodes_in;
         size = nodes.numberOfElements();
         sources = sources_in;
         sinks = sinks_in;
         order = order_in;
         next = listnext = null;
      }
   
   
   
      public String toString()
      {
         String string = new String();
      
         string += new String(" ILPiS").charAt(clanType);
         string += ": ";
         string += nodes.toShortString();
      
         return string;
      }
   
   
   
   
   
   // Translate indexes to ids for graph.
      public String toString(Graph graph)
      {
         String string = new String();
      
         string += new String(" ILPiS").charAt(clanType);
         string += ": ";
      
         string += "(";
         int index;
         boolean first = true;
         int numnodes = graph.numberOfNodes();
         for(index = nodes.first(); index != -1; index = nodes.next())
         {
            if(!first)
               string += ", ";
            else
               first = false;
         
            if(index < numnodes)
               string = string + graph.getNodeFromIndex(index).getId();
            else
               string += "d_" + index;
         
            if(nodes.isElement(index + 1) && nodes.isElement(index + 2))
            {
               string += "-";
               while(nodes.isElement(index + 1))
                  index = nodes.next();
               if(index < numnodes)
                  string += graph.getNodeFromIndex(index).getId();
               else
                  string += "dummy_" + index;
            }
         }
         string = string + ")";
      
         return string;
      }
   }
