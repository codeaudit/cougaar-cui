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
        }
        catch(Exception e)
        {
            System.out.println ("Not getting database connection");
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
        metric_string = myDecoder.startXMLDecoding (updateXML);

        int index = 0;

        int start_time_in_days;
        int end_time_in_days;
        float rate_float;

        try {
            stmt = connection.createStatement();

            int metric_id = getMetricID (metric_string);

            System.out.println ("metric string is " + metric_string);
            System.out.println ("metric id is " + metric_id);

            while (!myDecoder.doneXMLDecoding ()) {

                AggInfoStructure myStruct = myDecoder.getNextDataAtom();

if (index == 0) {
                System.out.print ("Org " + myStruct.getOrg());
}
                System.out.print ("Item " + myStruct.getItem());
                System.out.print (", Rate " + myStruct.getRate());

                int item_id = getItemID (myStruct.getItem());

                int org_id = getOrgID (myStruct.getOrg());

                if (myStruct.getTime() != null) {
                    rate_float = Float.parseFloat (myStruct.getValue());

                    start_time_in_days = convertTimeToDays (myStruct.getTime());
                    end_time_in_days = start_time_in_days + 1;

//              System.out.println ("time in days is " + time_in_days);

//                    putValueInTable (org_id, item_id, time_in_days, metric_id, value_float);
                }
                else {
                    rate_float = Float.parseFloat (myStruct.getRate());

                    start_time_in_days = convertTimeToDays (myStruct.getStartTime());
                    end_time_in_days = convertTimeToDays (myStruct.getEndTime());

//                    putValuesInTable (org_id, item_id, start_time_in_days, end_time_in_days, metric_id, rate_float);
                }

                putValuesInTable (org_id, item_id, start_time_in_days, end_time_in_days, metric_id, rate_float);

                AggregateByOrg (org_id, item_id, metric_id, start_time_in_days, end_time_in_days);

                System.out.println ("");
                System.out.print ("" + index);

                index++;

            } /* end of while */

            System.out.println ("Done, processed " + index + " records");

            // Save the work in the database
            stmt.executeUpdate("COMMIT");

//            AggregateItems (item_list);
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
    }

    private int getOrgID (String org_field_name) {
        int org_id = 0;

        try
        {
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
    }

    private void putValueInTable (int org,
                                 int item,
                                 int time,
                                 int metric,
                                 float value) {
        int rc;

        try
        {
            rc = stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + value + " WHERE org = " + org + " AND item = " + item + " AND unitsOfTime = " + time + " AND metric = " + metric);

            // If the update was not successful, do an insert
            // (rc will contain the number of rows updated by the
            // executeUpdate command.)
            if (rc == 0) {
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
    }

    private void putValuesInTable (int org,
                                  int item,
                                  int start_time,
                                  int end_time,
                                  int metric,
                                  float rate) {
        int rc;
        int rows_to_update = end_time - start_time;

        try
        {
System.out.print (" update(" + start_time + " to " + end_time + ")");
            rc = stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + rate + " WHERE org = " + org + " AND item = " + item + " AND metric = " + metric + " AND unitsOfTime >= " + start_time + " AND unitsOfTime < " + end_time);

            // If all the updates were not successful, do an insert
            // (rc will contain the number of rows updated by the
            // executeUpdate command.)
            if (rc != rows_to_update) {
                for (int time_index = start_time; time_index < end_time; time_index++) {
System.out.print ("insert"+time_index);
                    try
                    {
                        stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + org + ", " + item + ", " + time_index + ", " + metric + ", " + rate + ")");
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

    private void AggregateByOrg (int org_id,
                                 int item_id,
                                 int metric_id,
                                 int start_time,
                                 int end_time) {

        int time_index;
        int parent_org_id = 0;

        try
        {
            System.out.print ("Looking for parent of:" + org_id);

            // See if the current org_list id is in the table already
            ResultSet rs = stmt.executeQuery("SELECT parent FROM assessmentOrgs WHERE id = " + org_id);

            if (rs.next()) { // get the parent id value
                parent_org_id = rs.getInt ("parent");

                // See if the parent_org_id is actually in the table.
                // If it isn't in the table, return, since it means we reached the top of the tree
                ResultSet rs2 = stmt.executeQuery("SELECT parent FROM assessmentOrgs WHERE id = " + parent_org_id);

                // We've reached the top of the tree
                if (!rs2.next()) {
                    System.out.println ("No parent");
                    return;
                }
            } /* end of if */
            else {
                System.out.println ("No parent");
                return;
            }

            System.out.println ("");

            // Aggregate for this parent and item

            for (time_index = start_time; time_index < end_time; time_index++) {
                rs = stmt.executeQuery("SELECT sum(assessmentvalue) FROM assessmentData WHERE item = " + item_id + " and metric = " + metric_id + " and unitsoftime = " + time_index + " and org in (select id from assessmentorgs where parent =  " + parent_org_id + ")");

                if (rs.next()) { // get the sum
                    float sum = rs.getFloat ("sum(assessmentvalue)");
                    int rc;

                    System.out.println ("org " + parent_org_id +
                                        ", item " + item_id +
                                        ", time " + time_index + ", sum is " + sum);

                    rc = stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + sum + " WHERE org = " + parent_org_id + " AND item = " + item_id + " AND metric = " + metric_id + " AND unitsOfTime = " + time_index);

                    // If all the update was not successful, do an insert
                    if (rc != 1) {
                        stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + parent_org_id + ", " + item_id + ", " + time_index + ", " + metric_id + ", " + sum + ")");
                    }
                }
            } /* end of for time_index */
        } /* end of try */
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

        AggregateByOrg (parent_org_id, item_id, metric_id, start_time, end_time);
    } /* end of AggregateByOrganizations */

    private void AggregateByOrganizations (Vector org_list,
                                           Vector item_list,
                                           int metric_id,
                                           int start_time,
                                           int end_time) {

        int org_index, item_index, time_index;
        Vector parent_org_list = new Vector();
        int parent_org_id;
        int item_id;

        try
        {
            System.out.print ("Looking for parents of:");

            // Loop through all the organizations, and make a list of
            // all of the parents

            for (org_index = 0; org_index < org_list.size(); org_index++) {
                System.out.print ((String) org_list.elementAt(org_index) + " ");

                // See if the current org_list id is in the table already
                ResultSet rs = stmt.executeQuery("SELECT parent FROM assessmentOrgs WHERE id = " + org_list.elementAt(org_index));

                if (rs.next()) { // get the parent id value
                    parent_org_id = rs.getInt ("parent");

                    // See if the parent_org_id is actually in the table.
                    // If it isn't in the table, skip this entry, since it means we reached the top of the tree
                    ResultSet rs2 = stmt.executeQuery("SELECT parent FROM assessmentOrgs WHERE id = " + parent_org_id);

                    if (rs2.next() && (parent_org_list.contains ("" + parent_org_id) == false)) {
                        parent_org_list.add ("" + parent_org_id);
                    }
                }
            } /* end of for */

            System.out.println ("");

            // Aggregate for this group of parents

            for (org_index = 0; org_index < parent_org_list.size(); org_index++) {
                parent_org_id = Integer.parseInt ((String) parent_org_list.elementAt(org_index));

                for (item_index = 0; item_index < item_list.size(); item_index++) {
                    item_id = Integer.parseInt ((String) item_list.elementAt(item_index));

                    for (time_index = start_time; time_index < end_time; time_index++) {
                        ResultSet rs = stmt.executeQuery("SELECT sum(assessmentvalue) FROM assessmentData WHERE item = " + item_id + " and metric = " + metric_id + " and unitsoftime = " + time_index + " and org in (select id from assessmentorgs where parent =  " + parent_org_id + ")");

                        if (rs.next()) { // get the sum
                            float sum = rs.getFloat ("sum(assessmentvalue)");
                            int rc;

                            System.out.println ("org " + parent_org_id +
                                                ", item " + item_id +
                                                ", time " + time_index + ", sum is " + sum);

                            rc = stmt.executeUpdate("UPDATE assessmentData SET assessmentValue = " + sum + " WHERE org = " + parent_org_id + " AND item = " + item_id + " AND metric = " + metric_id + " AND unitsOfTime = " + time_index);

                            // If all the update was not successful, do an insert
                            if (rc != 1) {
                                stmt.executeUpdate("INSERT INTO assessmentData VALUES (" + parent_org_id + ", " + item_id + ", " + time_index + ", " + metric_id + ", " + sum + ")");
                            }
                        }
                    } /* end of for time_index */
                } /* end of for item_index */
            } /* end of for org_index */
        } /* end of try */
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

        if (parent_org_list.size() > 0) {
            AggregateByOrganizations (parent_org_list, item_list, metric_id, start_time, end_time);
        }
        else
            System.out.println ("no parents");
    } /* end of AggregateByOrganizations */

    private void AggregateItems (Vector org_list) {

      int index;

      for (index = 0; index < org_list.size(); index++) {
          System.out.print ((String) org_list.elementAt(index) + " ");
      }

      System.out.println ("");

    } /* end of AggregateItems */

    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext parm1) {}
}
