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

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.planning.plugin.legacy.SimplePlugin;
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