package org.cougaar.lib.uiframework.ui.map;

import com.bbn.openmap.*;

import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayer;

public class ScenarioMapBean extends BufferedMapBean
{

  public ScenarioMapBean()
  {
    super();

  }

    public PspIconLayer findPspIconLayer()
    {

      // LayerHandler layerHandler = getLayerHandler();
//      Layer[] layers= map.currentLayers;// layerHandler.getLayers();
      PspIconLayer myLayer=null;
      int idx;

      for(idx=0; idx<currentLayers.length; idx++)
      {
        if (currentLayers[idx] instanceof PspIconLayer)
        {
          myLayer=(PspIconLayer)currentLayers[idx];
          break;
        }
      }

      return myLayer;
    }

}