package org.cougaar.lib.uiframework.ui.ohv;


//import org.cougaar.lib.uiframework.ui.ohv.VGJ.VGJ;
//import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Graph;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphWindow;
import org.cougaar.lib.uiframework.ui.ohv.VGJ.gui.GraphCanvas;
//import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.Node;


  public class OrgHierVGJControl {

      static public boolean getMouseDraggingState()
        { return GraphCanvas.getMouseDraggingState(); }
      static public void setMouseDraggingState(boolean value)
        { GraphCanvas.setMouseDraggingState(value); }
      
      static public boolean getSmallPanelState()
        { return GraphWindow.getSmallPanelState(); }
      static public void setSmallPanelState(boolean value)
        { GraphWindow.setSmallPanelState(value); }


  }



