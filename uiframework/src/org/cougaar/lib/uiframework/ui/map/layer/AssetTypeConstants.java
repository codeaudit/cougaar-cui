/* 
 * <copyright>
 * Copyright 2003 BBNT Solutions, LLC
 * under sponsorship of the Defense Advanced Research Projects Agency (DARPA).

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Cougaar Open Source License as published by
 * DARPA on the Cougaar Open Source Website (www.cougaar.org).

 * THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 * PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 * IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 * ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 * HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 * DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 * TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.map.layer;

// Constants here are in place of references here
//import org.cougaar.mlm.ui.grabber.connect.DGPSPConstants;

// which were using numbers from
//import org.cougaar.mlm.ui.psp.transit.data.population.ConveyancePrototype;

/**
 * Constants used by <code>RouteJdbcConnector</code> and 
 * <code>PspIconLayerModel</code> and <code>PspIconLayer</code>, duplicating constants from 
 * the <code>datagrabber</code> module (not open-source). The original 
 * usage was to refer to <code>DGPSPConstants.CONV_TYPE_SHIP</code> and 
 * <code>.CONV_TYPE_PLANE</code>. These in turn really are 
 * <code>ConveyancePrototype.ASSET_TYPE_SHIP</code>, etc. If the 
 * constants are changed in the <code>datagrabber</code> module, 
 * they will also be changed here.
 * @see RouteJdbcConnector
 * @see PspIconLayerModel
 * @see PspIconLayer
 **/
public interface AssetTypeConstants {
    //types:
  /**none of the below**/
  int ASSET_TYPE_UNKNOWN = 0;
  /**specific moving conveyances (trucks, etc)**/
  int ASSET_TYPE_TRUCK = 1;
  int ASSET_TYPE_TRAIN = 2;
  int ASSET_TYPE_PLANE = 3;
  int ASSET_TYPE_SHIP = 4;
  /**catch-all for other moving conveyances (tanks, etc)**/
  int ASSET_TYPE_SELF_PROPELLABLE = 5;
  /**non-moving "conveyances" that one can assign to**/
  int ASSET_TYPE_DECK = 6;
  int ASSET_TYPE_PERSON = 7;
  int ASSET_TYPE_FACILITY = 8;
}
