package org.cougaar.lib.uiframework.ui.inventory;

import java.sql.*;
import java.util.*;

public class NSNItemUnits extends Thread
{

  private Connection conn;
  private Statement stmt;
  private String baseSelect = "select units from unitsbynsn where nsn = ";

  private boolean initDone = false;
  private Boolean synchObj = new Boolean (false);

  public NSNItemUnits()
  {

    start();


  }

  public String getUnit (String nsn)
  {

    while (true)    // wait for it
    {
      synchronized (synchObj)
      {
        if (initDone) // if true
          break;
      }

      try
      {
        Thread.sleep (100); // give it a tenth of a second
      }
      catch (InterruptedException iexc)
      {
        // who cares?
      }

    }

    String queryString = baseSelect + "'" + nsn + "'";
    //System.out.println ("query string is: " + queryString);
    ResultSet rs;
    String unit;
    try
    {
      rs = stmt.executeQuery(queryString);
      rs.next();
      unit = rs.getString(1);
      rs.close();

    }

    catch ( SQLException sqexc)
    {
      System.err.println ("Exception: No unit found for NSN item number " + nsn);
      System.err.println (sqexc.toString());

      return new String();
    }

     if (unit != null)
      return unit;
    else
    {
      System.err.println ("No unit found for NSN item number " + nsn);
      return new String();
    }

  }

  public void run ()
  {
     try
    {

      DriverManager.registerDriver(new org.hsql.jdbcDriver());

//      System.out.println("Connecting to the NSN Item Units DB datasource : " );

      conn = DriverManager.getConnection("jdbc:HypersonicSQL:ItemUnitsDB","sa","");

      stmt = conn.createStatement();

      synchronized (synchObj)
      {
        initDone = true;
      }

    }

    catch (SQLException e)
    {
      System.err.println ("SQLException = "+e);
      e.printStackTrace();
    }

    catch (Exception e)
    {
      System.err.println("Non SQL error while loading." );
      e.printStackTrace();
    }
  }


  public static void main (String[] args)
  {

    NSNItemUnits nsnUnits = new NSNItemUnits();

    System.out.println ("testing..");

    System.out.println ("NSN/8970014728983 unit is: " + nsnUnits.getUnit("NSN/8970014728983") );
    System.out.println ("NSN/9150000825636 unit is: " + nsnUnits.getUnit("NSN/9150000825636") );
    System.out.println ("NSN/9150001416770 unit is: " + nsnUnits.getUnit("NSN/9150001416770") );
    System.out.println ("NSN/6515014728648 unit is: " + nsnUnits.getUnit("NSN/6515014728648") );
  }
}
