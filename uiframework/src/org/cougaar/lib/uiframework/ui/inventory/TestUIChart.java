/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.inventory;


public class TestUIChart
{

  public static void main(String[] args)
  {
     //InventoryChartUI icui = new InventoryChartUI("3-FSB",  "MEAL READY-TO-EAT:NSN/8970001491094", System.currentTimeMillis(), System.currentTimeMillis());
     //InventoryChartUI icui = new InventoryChartUI("3-FSB",  null, 994291200, 995155200);
     InventoryChartUI icui = new InventoryChartUI();
     //icui.startup("3-FSB",  null, 994291200, 995155200);
     //icui.startup(null,  null, 0, 0, null, null);
     icui.startup("3-FSB",  null, 994291200, 995155200, "65.84.104.67", "5555", 4);
     //icui.populate("3-FSB",  "MEAL READY-TO-EAT:NSN/8970001491094");
     //InventoryChartUI icui = new InventoryChartUI(null,  null, 0, 0);
     //InventoryChartUI icui = new InventoryChartUI(null,  null, 994291200, 995155200);

  }
}
