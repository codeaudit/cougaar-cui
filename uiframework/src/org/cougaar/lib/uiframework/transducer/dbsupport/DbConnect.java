/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.transducer.dbsupport;

import java.sql.*;

/**
 *  DbConnect is a base class that provides database services to its subclasses.
 *  Each DbConnect instance manages a single Connection through which all DB
 *  traffic is channeled.
 */
public class DbConnect {
  protected String dbUrl = null;
  protected String dbUser = null;
  protected String dbPassword = null;
  protected Connection conn = null;
  protected String driverClassName = null;

  /**
   *  Create a new DbConnect instance.
   */
  protected DbConnect () {
  }

  /**
   *  Create a new DbConnect instance configured to use a given database driver
   *  class.
   *
   *  @param driverName the fully articulated name of the driver class
   */
  public DbConnect (String driverName) {
    driverClassName = driverName;
    try {
      DriverManager.registerDriver((Driver) Class.forName(driverClassName).newInstance());
    } catch (Exception e) { }
  }

  /**
   *  Specify the parameters for connecting to the database.
   *
   *  @param url the URL for the database server
   *  @param user the username for the database user account
   *  @param password the password for the database user account
   */
  public void setDbParams (String url, String user, String password) {
    dbUrl = url;
    dbUser = user;
    dbPassword = password;
  }

  /**
   *  Create a connection to the database.  The connection, once opened, will
   *  be used for all communication between this DbConnect instance and the
   *  database until such time as the connection is closed.
   */
  public void openConnection () {
    if (conn == null && dbUrl != null && dbUser != null && dbPassword != null)
      try {
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
      }
      catch (Exception bla) {
        bla.printStackTrace();
      }
  }

  /**
   *  Close the connection to the database, if any, and clear the reference.
   *  Any Exceptions that may be raised by this process are caught here.
   */
  public void closeConnection () {
    if (conn != null) {
      try {
        conn.close();
      }
      catch (Exception bugger) {
        bugger.printStackTrace();
      }
      conn = null;
    }
  }

  /**
   *  This is a test utility.  It submits a test query to the database and
   *  prints the results.
   */
  protected void db_test () {
    Statement st = null;
    try {
      st = conn.createStatement();
      ResultSet rs = st.executeQuery("select table_name from user_tables");
      System.out.println("Retrieved rows:");
      while (rs.next())
        System.out.println("  -> " + rs.getString(1));
    }
    catch (Exception bla) {
      bla.printStackTrace();
    }
    finally {
      closeStatement(st);
    }
  }

  /**
   *  This convenience method attempts to close the given statement (if it's
   *  not null) and traps any Exceptions that arise in the process.
   *
   *  @param st the Statement to be closed
   */
  protected void closeStatement (Statement st) {
    try {
      if (st != null)
        st.close();
    }
    catch (Exception stupidFriggingThing) {
      stupidFriggingThing.printStackTrace();
    }
  }
}