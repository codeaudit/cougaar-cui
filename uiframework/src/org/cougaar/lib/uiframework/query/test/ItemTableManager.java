/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.core.plugin.SimplePlugIn;
import java.util.*;

public class ItemTableManager extends SimplePlugIn {
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