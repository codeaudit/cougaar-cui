/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
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

import java.util.*;
import java.io.*;

public class NSNItemUnits implements Runnable
{
  private Hashtable itemTable = new Hashtable(1);

  private String file = null;

  public NSNItemUnits(String file)
  {
    this.file = file;

    Thread thread = new Thread(this);
    thread.setPriority(((thread.getPriority()-2) < Thread.MIN_PRIORITY) ? Thread.MIN_PRIORITY : (thread.getPriority()-2));
    thread.start();
  }

  public void run()
  {
    long time = System.currentTimeMillis();
    try
    {
      BufferedReader br = new BufferedReader ( new InputStreamReader (new FileInputStream (file)));
      
      String line = null;
      StringTokenizer stok = null;
      while ((line = br.readLine()) != null)
      {
        stok = new StringTokenizer (line, ",");
        itemTable.put(stok.nextToken(), stok.nextToken());
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
    
    System.out.println(System.currentTimeMillis() - time + "ms");
  }

  public String getUnit(String nsn)
  {
    String unit = (String)itemTable.get(nsn);

    if (unit == null)
    {
      throw(new RuntimeException("No unit type found for " + nsn));
    }

    return(unit);
  }

  public static void main(String[] args)
  {
    System.out.println ("testing..");

    NSNItemUnits nsnUnits = new NSNItemUnits("ItemUnits.txt");

    System.out.println ("NSN/8970014728983 unit is: " + nsnUnits.getUnit("NSN/8970014728983") );
    System.out.println ("NSN/9150000825636 unit is: " + nsnUnits.getUnit("NSN/9150000825636") );
    System.out.println ("NSN/9150001416770 unit is: " + nsnUnits.getUnit("NSN/9150001416770") );
    System.out.println ("NSN/6515014728648 unit is: " + nsnUnits.getUnit("NSN/6515014728648") );
  }
}
