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
