package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoStructure;
import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoDecoder;

public class BJDBMaintainerBean implements SessionBean
{
    private static final String DB_NAME = "java:comp/env/jdbc/AssessmentDB";
    private static final String DB_USER = "java:comp/env/DBUser";
    private static final String DB_PASSWORD = "java:comp/env/DBPassword";
    private Connection connection = null;

    private static final int BEGIN_TIME_SEC = 994204800;

    public void ejbCreate() throws CreateException
    {
        try
        {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource)ic.lookup(DB_NAME);
            String username = (String)ic.lookup(DB_USER);
            String password = (String)ic.lookup(DB_PASSWORD);
            connection = ds.getConnection(username, password);
        }
        catch(Exception e)
        {
            throw new CreateException("Could not create BJDBMaintainer: " +
                                      e.getMessage());
        }
    }

    public void ejbRemove()
    {
        try
        {
            connection.close();
        }
        catch(SQLException e){/*I tried*/}
    }

    public void updateDatabase(String updateXML) throws RemoteException
    {
//        System.out.println("XML: " + updateXML);

        AggInfoDecoder myDecoder = new AggInfoDecoder ();
        myDecoder.startXMLDecoding (updateXML);
        int index = 0;

        while (!myDecoder.doneXMLDecoding ()) {

            AggInfoStructure myStruct = myDecoder.getNextDataAtom();

/*
            System.out.println ("Org is " + myStruct.getOrg());
            System.out.println ("Item is " + myStruct.getItem());
            System.out.println ("Time is " + myStruct.getTime());
            System.out.println ("Start Time is " + myStruct.getStartTime());
            System.out.println ("End Time is " + myStruct.getEndTime());
            System.out.println ("Fieldname is " + myStruct.getFieldname());
            System.out.println ("Value is " + myStruct.getValue());
            System.out.println ("Rate is " + myStruct.getRate());
*/

            int metric_id = getMetricID (myStruct.getFieldname());
//            System.out.println ("metric id is " + metric_id);

            int item_id = getItemID (myStruct.getItem());
//            System.out.println ("item id is " + item_id);

            int org_id = getOrgID (myStruct.getOrg());
//            System.out.println ("org id is " + org_id);

            if (myStruct.getTime() != null) {

              float value_float = Float.parseFloat (myStruct.getValue());

              int time_in_days = convertTimeToDays (myStruct.getTime());

//              System.out.println ("time in days is " + time_in_days);

              putValueInTable (org_id, item_id, time_in_days, metric_id, value_float);
            }
            else {
              float rate_float = Float.parseFloat (myStruct.getRate());

              int start_time_in_days = convertTimeToDays (myStruct.getStartTime());
              int end_time_in_days = convertTimeToDays (myStruct.getEndTime());

//              System.out.println ("start time in days is " + start_time_in_days);
//              System.out.println ("end time in days is " + end_time_in_days);

              putValuesInTable (org_id, item_id, start_time_in_days, end_time_in_days, metric_id, rate_float);
            }

            index++;

        } /* end of while */

        System.out.println ("Done, processed " + index + " records");
        Statement stmt = null;

        try {
            stmt = connection.createStatement();

            // Save the work in the database
            stmt.executeUpdate("COMMIT");
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        finally {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private int getMetricID (String metric_field_name) {
        Statement stmt = null;
        int metric_id = 0;

        try
        {
            stmt = connection.createStatement();

            // See if the metric_field_name is in the table already
            ResultSet rs = stmt.executeQuery("SELECT id FROM assessmentMetrics WHERE name = '" + metric_field_name + "'");

            if (rs.next()) { // get the id value
                metric_id = rs.getInt ("ID");
            }
            else {

                // If it wasn't in the table, find the maximum id
                rs = stmt.executeQuery("SELECT max(id) FROM assessmentMetrics");

                if (rs.next()) {
                    metric_id = rs.getInt("MAX(ID)");
                    metric_id++;
                }
                else {
                    // this is the first row in the table
                    metric_id = 0;
                }

                stmt.executeUpdate("INSERT INTO assessmentMetrics VALUES (" + metric_id + ", '" + metric_field_name + "')");

            }
            return metric_id;
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        catch(Exception e)
        {
            throw new EJBException(e);
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private int getItemID (String item_field_name) {
        Statement stmt = null;
        int item_id = 0;

        try
        {
            stmt = connection.createStatement();

            // See if the item_field_name is in the table already
            ResultSet rs = stmt.executeQuery("SELECT id FROM assessmentItems WHERE name = '" + item_field_name + "'");

            if (rs.next()) { // get the id value
                item_id = rs.getInt ("ID");
            }
            else {

                // If it wasn't in the table, find the maximum id
                rs = stmt.executeQuery("SELECT max(id) FROM assessmentItems");

                if (rs.next()) {
                    item_id = rs.getInt("MAX(ID)");
                    item_id++;
                }
                else {
                    // this is the first row in the table
                    item_id = 0;
                }

                // Insert the item with the top of the tree as the parent
                stmt.executeUpdate("INSERT INTO assessmentItems VALUES (" + item_id + ", 0, '" + item_field_name + "')");

            }
            return item_id;
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        catch(Exception e)
        {
            throw new EJBException(e);
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private int getOrgID (String org_field_name) {
        Statement stmt = null;
        int org_id = 0;

        try
        {
            stmt = connection.createStatement();

            // See if the org_field_name is in the table already
            ResultSet rs = stmt.executeQuery("SELECT id FROM assessmentOrgs WHERE name = '" + org_field_name + "'");

            if (rs.next()) { // get the id value
                org_id = rs.getInt ("ID");
            }
            else {

                // If it wasn't in the table, find the maximum id
                rs = stmt.executeQuery("SELECT max(id) FROM assessmentOrgs");

                if (rs.next()) {
                    org_id = rs.getInt("MAX(ID)");
                    org_id++;
                }
                else {
                    // this is the first row in the table
                    org_id = 0;
                }

                // Insert the item with the top of the tree as the parent
                stmt.executeUpdate("INSERT INTO assessmentOrgs VALUES (" + org_id + ", 0, '" + org_field_name + "')");

            }
            return org_id;
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        catch(Exception e)
        {
            throw new EJBException(e);
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private void putValueInTable (int org,
                                 int item,
                                 int time,
                                 int metric,
                                 float value) {
        Statement stmt = null;

        try
        {
            stmt = connection.createStatement();

            // See if this value is in the table already
            ResultSet rs = stmt.executeQuery("SELECT assessmentValue FROM assessmentData WHERE org = " + org + " AND item = " + item + " AND unitsOfTime = " + time + " AND metric = " + metric);

            // If it's in the table, update the value
            if (rs.next()) {
                stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + value + " WHERE org = " + org + " AND item = " + item + " AND unitsOfTime = " + time + " AND metric = " + metric);
            }
            // Insert the new value
            else {
                stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + org + ", " + item + ", " + time + ", " + metric + ", " + value + ")");
            }
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        catch(Exception e)
        {
            throw new EJBException(e);
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private void putValuesInTable (int org,
                                  int item,
                                  int start_time,
                                  int end_time,
                                  int metric,
                                  float rate) {
        Statement stmt = null;

        try
        {
            stmt = connection.createStatement();

            for (int time_index = start_time; time_index <= end_time; time_index++) {
                // See if this value is in the table already
                ResultSet rs = stmt.executeQuery("SELECT assessmentValue FROM assessmentData WHERE org = " + org + " AND item = " + item + " AND unitsOfTime = " + time_index + " AND metric = " + metric);

                // If it's in the table, update the value
                if (rs.next()) {
                    stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + rate + " WHERE org = " + org + " AND item = " + item + " AND unitsOfTime = " + time_index + " AND metric = " + metric);
                }
                // Insert the new value
                else {
                    stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + org + ", " + item + ", " + time_index + ", " + metric + ", " + rate + ")");
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println ("SQL error code " + e.getErrorCode());
            System.out.println (e.getMessage());
            throw new EJBException(e);
        }
        catch(Exception e)
        {
            throw new EJBException(e);
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
            }
            catch(SQLException e) {}
        }
    }

    private int convertTimeToDays (String time_msecs) {

      // Convert milliseconds to seconds
      long time_msec_long = Long.parseLong (time_msecs);

      int time_sec_int = (int) (time_msec_long / 1000);

      // Normalize the times
      time_sec_int = time_sec_int - BEGIN_TIME_SEC;

      // 24 hours * 60 minutes * 60 seconds = 86400 seconds in a day, and
      // then round to the nearest whole day
      int time_in_days = (int) (time_sec_int / 86400.0 + 0.5);

      return (time_in_days);
    }

    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext parm1) {}
}