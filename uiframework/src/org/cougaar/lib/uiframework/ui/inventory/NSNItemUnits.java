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
    (new Thread(this)).start();
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
