package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.io.*;
import java.lang.Integer;
import java.util.Vector;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
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
    private static final String HACK_FOR_SAFETY_LEVEL = "Demand";
    private static final String SAFETY_LEVEL = "Safety Level";

    private static final String DB_NAME = "java:comp/env/jdbc/AssessmentDB";
    private static final String DB_USER = "java:comp/env/DBUser";
    private static final String DB_PASSWORD = "java:comp/env/DBPassword";
    private static final String C_TIME_YEAR = "java:comp/env/CTimeYear";
    private static final String C_TIME_MONTH = "java:comp/env/CTimeMonth";
    private static final String C_TIME_DAY = "java:comp/env/CTimeDay";
    private static final String C_TIME_ZONE = "java:comp/env/CTimeZone";
    private Connection connection = null;
    private Statement stmt = null;

    private static long c_time_sec_int;

    public void ejbCreate() throws CreateException
    {
        try
        {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource)ic.lookup(DB_NAME);
            String username = (String)ic.lookup(DB_USER);
            String password = (String)ic.lookup(DB_PASSWORD);
            String c_time_year_string = (String)ic.lookup(C_TIME_YEAR);
            String c_time_month_string = (String)ic.lookup(C_TIME_MONTH);
            String c_time_day_string = (String)ic.lookup(C_TIME_DAY);
            String c_time_zone_string = (String)ic.lookup(C_TIME_ZONE);

            if (c_time_zone_string == null) {
              c_time_zone_string = "GMT";
            }

            if ((c_time_year_string != null) &&
                (c_time_month_string != null)&&
                (c_time_day_string != null)) {

                TimeZone tz = TimeZone.getTimeZone(c_time_zone_string);

                GregorianCalendar gc = new GregorianCalendar (tz);

System.out.println ("c_time_year is " + c_time_year_string);
System.out.println ("c_time_month is " + c_time_month_string);
System.out.println ("c_time_day is " + c_time_day_string);

                // Month is offset from zero, others are not
                // Last three are hour, minute, second

                gc.set (Integer.parseInt (c_time_year_string),
                        Integer.parseInt (c_time_month_string) - 1,
                        Integer.parseInt (c_time_day_string),
                        0, 0, 0);

                c_time_sec_int = gc.getTime().getTime() / 1000;
            }
            else {
                c_time_sec_int = 0;
            }

System.out.println ("c_time_sec_int is " + c_time_sec_int);

            connection = ds.getConnection(username, password);
            connection.setAutoCommit (false);
        }
        catch(Exception e)
        {
            System.out.println ("Not getting database connection");
            e.printStackTrace();
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
        System.out.println("Entered updateDabase");

        AggInfoDecoder myDecoder = new AggInfoDecoder ();

        String metric_string;
        String org_string;

        myDecoder.startXMLDecoding (updateXML);

        org_string = myDecoder.getOrgFromXML ();
        metric_string = myDecoder.getMetricFromXML ();

        boolean run_safety_level_hack = false;
        int safety_level_metric_id = 0;

        int index = 0;

        int start_time_in_days;
        int end_time_in_days;
        float rate_float;

        try {
            stmt = connection.createStatement();
            createPreparedStatements();

            if (metric_string.compareTo(HACK_FOR_SAFETY_LEVEL) == 0) {
                System.out.println ("******************************");
                System.out.println ("Running hack for safety level!");
                System.out.println ("******************************");
                run_safety_level_hack = true;
                safety_level_metric_id = getMetricID (SAFETY_LEVEL);
                System.out.println ("metric string is " + SAFETY_LEVEL);
                System.out.println ("metric id is " + safety_level_metric_id);
            }

            int metric_id = getMetricID (metric_string);

            int org_id = getOrgID (org_string);

            System.out.println ("Org is " + org_string);
            System.out.println ("metric string is " + metric_string);
            System.out.println ("metric id is " + metric_id);

            while (!myDecoder.doneXMLDecoding ()) {

                AggInfoStructure myStruct = myDecoder.getNextDataAtom();

                System.out.print ("Item " + myStruct.getItem());
                System.out.print (", Rate " + myStruct.getRate());

                int item_id = getItemID (myStruct.getItem());

                // If item not in table, skip it
                if (item_id == -1)
                    continue;

                if (myStruct.getTime() != null) {
                    rate_float = Float.parseFloat (myStruct.getValue());

                    start_time_in_days = convertTimeToDays (myStruct.getTime());
                    end_time_in_days = start_time_in_days + 1;

//              System.out.println ("time in days is " + time_in_days);
                }
                else {
                    rate_float = Float.parseFloat (myStruct.getRate());

                    start_time_in_days = convertTimeToDays (myStruct.getStartTime());
                    end_time_in_days = convertTimeToDays (myStruct.getEndTime());
                }

                putValuesInTable (org_id, item_id, start_time_in_days, end_time_in_days, metric_id, rate_float);

                System.out.println ("");
                System.out.print ("" + index);

                if (run_safety_level_hack) {
                    float multiplier = getOrgSafetyLevelMultiplier (org_id);
                    putValuesInTable (org_id, item_id, start_time_in_days, end_time_in_days, safety_level_metric_id, rate_float * multiplier);
                }

                index++;

                if (index % 100 == 0) {
                    // Save the work in the database
                    connection.commit();
                }
            } /* end of while */

            System.out.println ("Done, processed " + index + " records");

            // Save the work in the database
            connection.commit();
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
                if (updateAssessmentData != null)
                    updateAssessmentData.close();
            }
            catch(SQLException e) {}
        }
    }

    private PreparedStatement updateAssessmentData = null;

    private void createPreparedStatements () {
        try {
            updateAssessmentData =
                connection.prepareStatement("UPDATE assessmentData " +
                    "SET assessmentValue = ? WHERE org = ? AND item = ? " +
                    "AND metric = ? AND unitsOfTime >= ? AND unitsOfTime < ?");
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
    }

    private int getMetricID (String metric_field_name) {
        int metric_id = 0;

        try
        {
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
    }

    private int getItemID (String item_field_name) {
        int item_id = 0;

        try
        {
            // The effective query is
            // SELECT id FROM itemWeights WHERE item_id = item_field_name
            ResultSet rs = stmt.executeQuery ("SELECT id FROM itemWeights WHERE item_id = '" + item_field_name + "'");

            if (rs.next()) { // get the id value
                item_id = rs.getInt ("ID");
            }
            else {
                System.out.println ("Item " + item_field_name + " not in table!");
                item_id = -1;
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
    }

    private int getOrgID (String org_field_name) {
        int org_id = 0;

        try
        {
            // The effective query is
            // SELECT id FROM assessmentOrgs WHERE name = org_field_name

            ResultSet rs = stmt.executeQuery ("SELECT id FROM assessmentOrgs WHERE name = '" + org_field_name + "'");

            if (rs.next()) { // get the id value
                org_id = rs.getInt ("ID");
            }
            else {

                System.out.println ("" + org_field_name + " not found in assessmentOrgs table.  Going to insert new organization in the table");

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
    }

    private void putValuesInTable (int org_id,
                                  int item_id,
                                  int start_time,
                                  int end_time,
                                  int metric_id,
                                  float rate) {
        int rc;
        int rows_to_update = end_time - start_time;

        try
        {
System.out.print (" update(" + start_time + " to " + end_time + ")");
            // The effective update is
            // UPDATE assessmentData SET assessmentValue = rate
            // WHERE org = org_id AND item = item_id AND metric = metric_id
            // AND unitsOfTime >= start_time AND unitsOfTime < end_time

            updateAssessmentData.setFloat(1,rate);
            updateAssessmentData.setInt(2,org_id);
            updateAssessmentData.setInt(3,item_id);
            updateAssessmentData.setInt(4,metric_id);
            updateAssessmentData.setInt(5,start_time);
            updateAssessmentData.setInt(6,end_time);
            rc = updateAssessmentData.executeUpdate();

            // If all the updates were not successful, do an insert
            // (rc will contain the number of rows updated by the
            // executeUpdate command.)
            if (rc != rows_to_update) {
                int num_tobeinserted = rows_to_update - rc;
//                for (int time_index = start_time; time_index < end_time; time_index++) {
                for (int time_index = end_time - 1;
                  time_index >= start_time && num_tobeinserted != 0;
                  time_index--) {
System.out.print ("insert"+time_index);
                    try
                    {
                        stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + org_id + ", " + item_id + ", " + time_index + ", " + metric_id + ", " + rate + ")");
                        num_tobeinserted--;
                    }
                    catch (SQLException e)
                    {
                        System.out.print ("skipping insert");
                    }
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
            System.out.println ("Not an SQL exception");
            throw new EJBException(e);
        }
    }

    private int convertTimeToDays (String time_msecs) {

        // Convert milliseconds to seconds
        long time_msec_long = Long.parseLong (time_msecs);

        long time_sec_int = (long) (time_msec_long / 1000);

        // Normalize the times
        time_sec_int = time_sec_int - c_time_sec_int;

        // 24 hours * 60 minutes * 60 seconds = 86400 seconds in a day
        int time_in_days = (int) (time_sec_int / (long) 86400);

        return (time_in_days);
    }

    private float getOrgSafetyLevelMultiplier (int org_id) {

        float multiplier = 1.0f;
        try
        {
            // The effective query is
            // SELECT id FROM assessmentOrgs WHERE name = org_field_name

            ResultSet rs = stmt.executeQuery ("SELECT SAFETY_LEVEL FROM assessmentOrgs WHERE id = " + org_id);

            if (rs.next()) { // get the id value
                multiplier = rs.getFloat ("SAFETY_LEVEL");
            }
            else {
                multiplier = 0.0f;
            }

            return multiplier;
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
    }

    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext parm1) {}
}
