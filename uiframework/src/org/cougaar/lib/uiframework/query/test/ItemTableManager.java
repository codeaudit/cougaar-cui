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

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.core.plugin.SimplePlugin;
import java.util.*;

public class ItemTableManager extends SimplePlugin {
  private Hashtable table = new Hashtable();
  private String[] items = {
    "All Items",
      "Medical",
        "Syringe",
        "Bandage",
      "Subsistence",
        "Bread",
        "Egg",
      "Spare Parts",
        "Automotive",
          "Tire",
          "Spark Plug",
        "Tools",
          "Hammer",
          "Nails",
      "Fuel",
        "Gasoline",
        "Jet Fuel"
    };

  public void setupSubscriptions () {
    publishAdd(new PlanTableWrapper("Item Table", table));
    wake();
  }

  public void execute () {
    System.out.println("ItemTableManager::execute:  BLEEP!");
    // choose a random item from the array and update its values
    updateItem(genRandomItem());

    wakeAfter(1000);
  }

  private void updateItem (String item) {
    ItemRec rec = (ItemRec) table.get(item);
    if (rec == null) {
      rec = new ItemRec(item);
      table.put(item, rec);
    }
    rec.randomUpdate();
  }

  private String genRandomItem () {
    int i = (int) Math.floor(Math.random() * items.length);
    return items[i];
  }

  private static class ItemRec {
    public String name = null;
    public int hits = 0;
    public double value = 0.0;

    public ItemRec (String n) {
      name = n;
    }

    public void randomUpdate () {
      hits++;
      value += Math.random() + Math.random() + Math.random() - Math.random() - Math.random();
    }

    public String toString () {
      return String.valueOf(value);
    }
  }
}