
/*
 *File: VGJ.java
 *
 * Date      Author
 * 2/16/97   Larry Barowski
 *
 * The following comment is to comply with GPLv2:
 *    This source file was modified during February 2001.
 */


   package org.cougaar.lib.uiframework.ui.ohv.VGJ;



   import java.awt.GridLayout;
   import java.awt.Event;
   import java.awt.event.WindowAdapter;
   import java.awt.event.WindowEvent;
   import java.awt.Button;
   import java.awt.Graphics;
   import java.awt.Color;
   import java.awt.Frame;
   import java.applet.Applet;

   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;

   import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphCanvas;  // temp
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.examplealg.ExampleAlg2;

   import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.tree.TreeAlgorithm;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.cgd.CGDAlgorithm;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.shawn.Spring;
   import org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.cartegw.BiconnectGraph;

   import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.*;
   import java.lang.System;
   import java.io.StringBufferInputStream;
   import java.util.Vector;


   import java.util.Hashtable; // kwr
import EDU.auburn.VGJ.util.DDimension;       // kwr
   import java.awt.Component;

/**
 *	The VGJ applet. It is a big button that pops up VGJ graph
 *  editor windows.
 *	</p>Here is the <a href="../VGJ.java">source</a>.
 *
 *@author	Larry Barowski
**/




   public class VGJ extends Applet
   {
      private int appCount_ = 0;
      private boolean isApp_ = false;

      private String MY_GRAPH_BUTTON="Start a New Graph Window";
     private String NEW_GRAPH_BUTTON="Start my Graph Window";
     private String EXIT_BUTTON="Exit";

      public void init()
      {
         if (mygraph==null)
            mygraph=new Graph(true);

         if(isApp_)
         {
            setLayout(new GridLayout(3,1));
            add(new Button(NEW_GRAPH_BUTTON));
            add(new Button(MY_GRAPH_BUTTON));
            add(new Button(EXIT_BUTTON));
         }
         else
         {
            setLayout(new GridLayout(2,1));
            add(new Button(NEW_GRAPH_BUTTON));
            add(new Button(MY_GRAPH_BUTTON));
         }

	 Node.setToolkit(getToolkit());
	 if(!isApp_) {
	    Node.setContext(getCodeBase());
	    GraphWindow.setContext(getCodeBase());
	 }
	 validate();
         show();
      }
   
   
//     static             Graph mygraph=new Graph(true);
     Graph mygraph=null;
     GraphWindow myGw=null;


     String canvastitle="";
      public void setCanvasTitle(String value) {
        canvastitle=value;
        if (myGw!=null) myGw.setCanvasTitle(value);
      }
     public void setGraph(Graph ingraph)
     {
      mygraph=ingraph;
      if (frame!=null) { frame.repaint();  }
      if (myGw!=null) { myGw.repaint(); }
     }

     VGJ()
     {
      mygraph=new Graph(true);
      }

      public void showGraph()
      {
        showGraph(false, false, false, null, false);
      }
      public void showGraph(boolean dynamicLayoutDesired,
                            boolean deleteFirstNode,
                            boolean exitOnWindowClose, org.cougaar.lib.uiframework.ui.ohv.OrgTreeAction ota)
      {
        showGraph(dynamicLayoutDesired, deleteFirstNode, exitOnWindowClose,
                  ota, false);
      }
      public void showGraph(boolean dynamicLayoutDesired,
                            boolean deleteFirstNode,
                            boolean exitOnWindowClose, org.cougaar.lib.uiframework.ui.ohv.OrgTreeAction ota,
                            boolean makeNewWindow)
      {
                  int firstNode;
        if (myGw!=null&&!makeNewWindow) { 
	    // myGw.pack(); 
	    myGw.show(); 
	    return; 
	}
               myGw=buildWindow_(mygraph);
               myGw.setCanvasTitle(canvastitle);
             if (exitOnWindowClose) {
               System.out.println("Adding window listener.");
               myGw.addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent e) {
                  System.err.println("Window closing, so we are done. Bye.");
                  System.exit(0);
                 }
               });
             } else {
               System.out.println("Adding dispose window listener.");
               myGw.addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent e) {
                  System.err.println("Window closing, but siblings live on.");
                  myGw.dispose();
                 }
               });
                               

             }
             System.out.println("showgraph adding ota: "+ota);
             myGw.addOTActionListener(ota);


                if (mygraph.isDirected()&& mygraph.numberOfNodes()>0) {
                  //TreeAlgorithm talg = new TreeAlgorithm('d');
                  //gw.addAlgorithm(talg, "Tree", "Tree Down");
                  if (dynamicLayoutDesired||deleteFirstNode) {
                    firstNode = mygraph.firstNodeIndex();
                    if (dynamicLayoutDesired) {
                      myGw.selectNodeFromIndex(firstNode);
                      myGw.applyAlgorithm("Tree.Tree Down");
                      //gw.applyAlgorithm("CGD.CGD");
                      try {
                        Thread.sleep(100);
                      } catch (Exception e) {}
                    }
                    if (deleteFirstNode) {
                      //mygraph.removeNode(firstNode);
                      myGw.deleteNode(firstNode);
                    }
                    myGw.center();
                  } // end if (dynamicLayoutDesired||deleteFirstNode) 

                  }

     }
         String frameTitle="Org Chart";

      public static void main(String args[])
      {
	         if (args.length == 0) {
	             drawMyGraph();
	             return;
	         }
        String frameTitle="Org Chart";
         Frame frame = new Frame(frameTitle);
         VGJ vgj = new VGJ();
         vgj.isApp_ = true;
         vgj.init();
         vgj.start();

         frame.add("Center", vgj);
         frame.resize(200, 300);
	 frame.validate();
         frame.show();

         if(args.length > 0) {
            String filename = null;
            int selected = -1;
            String layout = null;

            for(int i = 0; i < args.length; i++) {
               if(args[i].equals("-f") && i < args.length)
                  filename = args[++i];
               else if(args[i].equals("-s") && i < args.length)
                  selected = Integer.parseInt(args[++i]);
               else if(args[i].equals("-l") && i < args.length)
                  layout = args[++i];
            }

            if(filename != null) {
               GraphWindow gw = vgj.buildWindow_();
               gw.loadFile(filename);
               if(selected != -1)
                  gw.selectNode(selected);
               if(layout != null)
                  gw.applyAlgorithm(layout);
            }
         } else {

            Vector myvec=new Vector();
            Graph mygraph=new Graph(true);   // mygraph used to be a static attrib; when I changed that, I added this line to allow compilation; this line has never been tested
            addSuperior(mygraph, "a", "b", myvec);
            int aid = ((Integer)myvec.get(0)).intValue();
            int bid = ((Integer)myvec.get(1)).intValue();

            addSuperior(mygraph, aid, "c", myvec);
            int cid = ((Integer)myvec.get(2)).intValue();
            addSuperior(mygraph, "d", cid, myvec);
            int did = ((Integer)myvec.get(3)).intValue();

            addSuperior(mygraph, aid, did, myvec);
           addSuperior(mygraph, bid, "e", myvec);
           addSuperior(mygraph, cid, "f", myvec);
           addSuperior(mygraph, did, "g", myvec);
            addSuperior(mygraph, did, "h", myvec);

            Node aNode = mygraph.getNodeFromId(aid);
            aNode.setSelected(true);

            GraphWindow gw=new GraphWindow(mygraph);
         }
      }

      Frame frame;
      public static VGJ create()
      {
         VGJ vgj = new VGJ();
         vgj.isApp_ = true;
          vgj.frame = new Frame("Cougaar Graph");

         /*
         vgj.init();
         vgj.start();
         frame.add("Center", vgj);
         frame.resize(200, 300);
         frame.validate();
         frame.show();          /* */

         return vgj;
      }

       // static class NodeNames {
      class NodeNames {
        Hashtable nodeNames = new Hashtable();
        int currx=0;
        int curry=150;
        int xdelta=40;
        int ydelta=20;
        //DDimension nodeSizeBox=new DDimension(90,20);
         int height =GraphCanvas.getLoudSystemProperty("ui.orgView.defaultNodeHeight", 90, 89);
         int width=GraphCanvas.getLoudSystemProperty("ui.orgView.defaultNodeWidth", 130, 129);
        DDimension nodeSizeBox=new DDimension(width, height);

        void syncWith(Graph mygraph) {
          Node tmpNode;
          for (tmpNode=mygraph.firstNode(); tmpNode!=null;
	              tmpNode=mygraph.nextNode(tmpNode)) {
            System.out.println("syncWith Adding node labeled: "+tmpNode.getLabel()
                  +" ID: "+tmpNode.getId()
                  +" idx: "+tmpNode.getIndex());
            add(tmpNode);
          }
        }

        private void add(Node node) {
          add(node.getLabel(), node.getId());
        }
        private void add(String name, int id) {
              nodeNames.put(name, new Integer(id));
        }

        /**
         * returns id for given node name (creating it if necessary)
        **/
        int getNodeID(Graph mygraph, String name)
        {
            int id1;
            Integer oid1=(Integer)nodeNames.get(name);

            if (oid1==null) {
              id1=mygraph.insertNode();
              Node node1= mygraph.getNodeFromId(id1);
              node1.setLabel(name);
              //node1.setLabelPosition("Inside");
              node1.setLabelPosition("C");
              // node1.setShape(Node.RECTANGLE);
              node1.setShape(Node.OVAL);
              node1.setPosition(currx, curry);
              node1.setBoundingBox(nodeSizeBox);
              currx+=xdelta; curry+=ydelta;  xdelta*=-1;
              // nodeNames.put(name, new Integer(id1));
              add(node1);
            } else {
              id1=oid1.intValue();
            }
            return id1;
        }
        /**
         * returns true if a node for the given node name exists
        **/
      boolean hasNode(String name)  {
            int id1;
            Integer oid1=(Integer)nodeNames.get(name);
            return (oid1!=null);
      }
      } // end class NodeNames


       // static NodeNames nodeNames=new VGJ.NodeNames();
       NodeNames nodeNames=new VGJ.NodeNames();

        public int getNodeID(Graph mygraph, String name)
        {
        return nodeNames.getNodeID(mygraph, name);
        }

        public void syncWith(Graph mygraph)
        {
          nodeNames.syncWith(mygraph);
        }


      public static void drawMyGraph()
      {
         Frame frame = new Frame("COUGAAR Graph");
         VGJ vgj = new VGJ();
         vgj.isApp_ = true;

         // follwoing displays the 3 button frame
         //vgj.init();
         //vgj.start();
        // frame.add("Center", vgj);
        // frame.resize(200, 300);
        // frame.validate();
        // frame.show();


         /*
            Vector myvec=new Vector();
            addSuperior(mygraph, "a", "b", myvec);
            int aid = ((Integer)myvec.get(0)).intValue();
            int bid = ((Integer)myvec.get(1)).intValue();

            addSuperior(mygraph, aid, "c", myvec);
            int cid = ((Integer)myvec.get(2)).intValue();
            addSuperior(mygraph, "d", cid, myvec);
            int did = ((Integer)myvec.get(3)).intValue();

            addSuperior(mygraph, aid, did, myvec);
           addSuperior(mygraph, bid, "e", myvec);
           addSuperior(mygraph, cid, "f", myvec);
           addSuperior(mygraph, did, "g", myvec);
            addSuperior(mygraph, did, "h", myvec);

            Node aNode = mygraph.getNodeFromId(aid);
            aNode.setSelected(true);

            GraphWindow gw=new GraphWindow(mygraph);
            */

            vgj.addSuperior("a", "b");
            vgj.addSuperior("a", "c");
            vgj.addSuperior("d", "cccc");

            vgj.addSuperior("a", "d");
           vgj.addSuperior("b", "e");
           vgj.addSuperior("c", "f");
           vgj.addSuperior("d", "g");
            vgj.addSuperior("d", "h");

            vgj.showGraph();
//            Node aNode = mygraph.getNodeFromId(nodeNames.getNodeID(mygraph, "a"));
            //int id=vgj.getNodeID("a");
            //Node aNode = mygraph.getNodeFromId(id);

            // GraphWindow gw=new GraphWindow(mygraph);

      }

      int getNodeID(String idStr)
      {
        return nodeNames.getNodeID(mygraph, idStr);
      }
      public void drawTest()
      {
            addSuperior(mygraph, "a", "b");
            addSuperior(mygraph, "a", "c");
            addSuperior(mygraph, "d", "cccc");

            addSuperior(mygraph, "a", "d");
           addSuperior(mygraph, "b", "e");
           addSuperior(mygraph, "c", "f");
           addSuperior(mygraph, "d", "g");
            addSuperior(mygraph, "d", "h");

            Node aNode = mygraph.getNodeFromId(nodeNames.getNodeID(mygraph, "a"));
            aNode.setSelected(true);

            GraphWindow gw=new GraphWindow(mygraph);

      }
      /*
        Add link.  If a necessary node is missing, create it.
      */
      public void addSuperior(Graph mygraph, String sup, String sub) {
            int id1, id2;
            int idx1, idx2;
              Node aNode;
              id1 = nodeNames.getNodeID(mygraph, sup);
              aNode = mygraph.getNodeFromId(id1);
              idx1=aNode.getIndex();

              id2 = nodeNames.getNodeID(mygraph, sub);
              aNode = mygraph.getNodeFromId(id2);
              idx2=aNode.getIndex();
              System.err.print("addSuperior "+sup+" "+sub);
              System.err.println("supid "+id1+" subid "+id2);
              System.err.println("supidx "+idx1+" subidx "+idx2);
           mygraph.insertEdge(idx1, idx2);
            }

      public void clearLinks() {
        mygraph.removeAllEdges();
      }


      /*
        Do not create nodes.  If they do not exist, ignore this link.
      */
      public void addSuperiorLink(Graph mygraph, String sup, String sub) {
            int id1, id2;
            int idx1, idx2;
              Node aNode;
              if (nodeNames.hasNode(sup) && nodeNames.hasNode(sub)) {
                id1 = nodeNames.getNodeID(mygraph, sup);
                aNode = mygraph.getNodeFromId(id1);
                idx1=aNode.getIndex();

                id2 = nodeNames.getNodeID(mygraph, sub);
                aNode = mygraph.getNodeFromId(id2);
                idx2=aNode.getIndex();
                System.err.print("addSuperiorLink "+sup+" "+sub);
                System.err.println("supid "+id1+" subid "+id2);
                System.err.println("supidx "+idx1+" subidx "+idx2);
                mygraph.insertEdge(idx1, idx2);
              }
      }
      public void colorNode(Graph mygraph, String nodeLabel, Color color) {
        colorNode(mygraph, nodeLabel, color, false);
      }
      public boolean drillDownNode(Graph mygraph, String nodeLabel, boolean hasDrillDown) {
            int id1;
            int idx1;
	    boolean prevVal=false;
            if (nodeNames.hasNode(nodeLabel)) {
              Node aNode;
              id1 = nodeNames.getNodeID(mygraph, nodeLabel);
              aNode = mygraph.getNodeFromId(id1);
              idx1=aNode.getIndex();
              prevVal=aNode.setDrillDownOn(hasDrillDown); 

              System.err.print("drillDownNode "+nodeLabel+" ");
              System.err.print("id "+id1+" idx "+idx1);
              System.err.println(" newval "+hasDrillDown);
            }
	    return prevVal;
      }
      public void colorNode(Graph mygraph, String nodeLabel, Color color, boolean create) {
            int id1;
            int idx1;
            if (create||nodeNames.hasNode(nodeLabel)) {
              Node aNode;
              id1 = nodeNames.getNodeID(mygraph, nodeLabel);
              aNode = mygraph.getNodeFromId(id1);
              idx1=aNode.getIndex();
              aNode.setColor(color);

              System.err.print("colorNode "+nodeLabel+" ");
              System.err.print("id "+id1+" idx "+idx1);
              System.err.println(" color "+color);
            }
      }
            /* */

      public void addSuperior(String sup, String sub) {
            int id1, id2;
              id1 = nodeNames.getNodeID(mygraph, sup);
              id2 = nodeNames.getNodeID(mygraph, sub);
           mygraph.insertEdge(id1, id2);
            }

      static void addSuperior(Graph mygraph, String sup, String sub, Vector ids) {
            int id1, id2;
              id1 = mygraph.insertNode();
              Node node1= mygraph.getNodeFromId(id1);
              node1.setLabel(sup);


              id2 = mygraph.insertNode();
               Node node2= mygraph.getNodeFromId(id2);
               node2.setLabel(sub);


           mygraph.insertEdge(id1, id2);
           ids.add(new Integer(id1));
           ids.add(new Integer(id2));
            }

      static void addSuperior(Graph mygraph, int supid, String sub, Vector ids) {
            int id1, id2;
              id1 = supid;

              id2 = mygraph.insertNode();
               Node node2= mygraph.getNodeFromId(id2);
               node2.setLabel(sub);
            ids.add(new Integer(id2));

           mygraph.insertEdge(id1, id2);
            }

     static void addSuperior(Graph mygraph, String sup, int subid, Vector ids) {
            int id1, id2;
              id1 = mygraph.insertNode();
              Node node1= mygraph.getNodeFromId(id1);
              node1.setLabel(sup);
           ids.add(new Integer(id1));

              id2 = subid;

           mygraph.insertEdge(id1, id2);
            }

     void addSuperior(int sup, int subid) {
            int id1, id2;
              id1 = sup;
              id2 = subid;

           mygraph.insertEdge(id1, id2);
            }

     static void addSuperior(Graph mygraph, int sup, int subid, Vector ids) {
            int id1, id2;
              id1 = sup;
              id2 = subid;

           mygraph.insertEdge(id1, id2);
            }


      public boolean action(Event event, Object what)
      {
         if(event.target instanceof Button)
            if(((String)what).equals(EXIT_BUTTON))
            {
               System.exit(0);
            }
            else if(((String)what).equals(NEW_GRAPH_BUTTON))
            {
                GraphWindow gw=buildWindow_(mygraph);
                if (mygraph.isDirected()) {
                  //TreeAlgorithm talg = new TreeAlgorithm('d');
                  //gw.addAlgorithm(talg, "Tree", "Tree Down");
                  int firstNode = mygraph.firstNodeIndex();
                  gw.selectNode(firstNode);
                  gw.applyAlgorithm("Tree.Tree Down");
                  //gw.center();
                  // mygraph.removeNode(firstNode);
                  // gw.deleteNode(firstNode);
                  //gw.show();

                }

            }
            else
            {
                buildWindow_();
            }
         return super.action(event, what);
      }
   


      public String getSelectedNodeLabel() {
        String retstr=null;
        if (myGw!=null)
          retstr=myGw.getSelectedNodeLabel();
         return retstr;
      }


      Vector controlsToAdd=new Vector();
      public void addControl(Component control) {
        if (myGw!=null)
          myGw.addControl(control);
        controlsToAdd.add(control);

      }


      private GraphWindow buildWindow_()
      {
        return buildWindow_(null);
      }
       private GraphWindow buildWindow_(Graph ingraph)
      {
            	// Bring up an undirected graph editor window.
            
            	// The parameter to GraphWindow() indicates directed
            	// or undirected.
              GraphWindow graph_editing_window ;
              if (ingraph==null) {
                  graph_editing_window = new GraphWindow(true);
               } else {
                  graph_editing_window = new GraphWindow(ingraph);
               }
            
            	// Here the algorithms are added.
               graph_editing_window.addAlgorithmMenu("Tree");
            
               ExampleAlg2 alg2 = new ExampleAlg2();
               graph_editing_window.addAlgorithm(alg2, "Random");
            
               TreeAlgorithm talg = new TreeAlgorithm('d');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Down");

               talg = new TreeAlgorithm('u');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Up");
            
               talg = new TreeAlgorithm('l');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Left");
            
               talg = new TreeAlgorithm('r');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Right");
            
            
               graph_editing_window.addAlgorithmMenu("CGD");
            
               CGDAlgorithm calg = new CGDAlgorithm();
               graph_editing_window.addAlgorithm(calg, "CGD", "CGD");
            
               calg = new CGDAlgorithm(true);
               graph_editing_window.addAlgorithm(calg, "CGD",
                  "show CGD parse tree");
            
               Spring spring = new Spring();
               graph_editing_window.addAlgorithm(spring, "Spring");
            
               graph_editing_window.addAlgorithmMenu("Biconnectivity");
               BiconnectGraph make_biconnect = new BiconnectGraph(true);
               graph_editing_window.addAlgorithm (make_biconnect, 
                  "Biconnectivity", "Remove Articulation Points");
               BiconnectGraph check_biconnect = new BiconnectGraph(false);
               graph_editing_window.addAlgorithm (check_biconnect, 
                  "Biconnectivity", "Find Articulation Points");

               //String frameTitle="VGJ v1.03";
               String frameTitle="Org Chart";
               if (appCount_++ == 0)
                  graph_editing_window.setTitle(frameTitle);
               else
                  graph_editing_window.setTitle(frameTitle + ": "
                     + appCount_);
            
               graph_editing_window.addControls(controlsToAdd);
               graph_editing_window.pack();
               graph_editing_window.show();

               return graph_editing_window;
      }


   }







