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
package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.io.*;
import java.lang.Integer;
import java.util.Vector;
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

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;
import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoStructure;

// For SAX processing
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

public class BJDBMaintainerBean implements SessionBean
{
    private static final int NUM_RECORDS_BEFORE_COMMIT = 100;

    private static final String DB_NAME = "java:comp/env/jdbc/AssessmentDB";
    private static final String DB_USER = "java:comp/env/DBUser";
    private static final String DB_PASSWORD = "java:comp/env/DBPassword";
    private Connection connection = null;
    private Statement stmt = null;

    XMLReader xr;
    AggInfoSAXHandler handler;

    String table_name_string;

    public void ejbCreate() throws CreateException
    {
        try
        {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource)ic.lookup(DB_NAME);
            String username = (String)ic.lookup(DB_USER);
            String password = (String)ic.lookup(DB_PASSWORD);

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
        table_name_string = "";
        String myParser = "org.apache.xerces.parsers.SAXParser";

        try {
            xr = XMLReaderFactory.createXMLReader(myParser);
        }
        catch (Exception e) {
            System.out.println ("Could not create an XML reader");
            e.printStackTrace();
        }

        // Create a SAX handler and then tell the xml reader parser
        // to call the handler's functions when certain conditions occur

        handler = new AggInfoSAXHandler();
        xr.setContentHandler (handler);
        xr.setErrorHandler (handler);

        StringReader sr = new StringReader (updateXML);
        InputSource is = new InputSource (sr);

        try {
            xr.parse (is);
        }
        catch (Exception e) {
            System.out.println ("Could not parse XML string");
        }
    } /* end of updateDatabase */

    private PreparedStatement updateAssessmentData = null;

    private void createPreparedStatements (String table_name_value) {
        try {
            updateAssessmentData =
                connection.prepareStatement("UPDATE " + table_name_value +
                    " SET assessmentValue = ? WHERE org = ? AND item = ? " +
                    "AND unitsOfTime >= ? AND unitsOfTime < ?");
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
            ResultSet rs = stmt.executeQuery("SELECT id, table_name FROM assessmentMetrics WHERE name = '" + metric_field_name + "'");

            if (rs.next()) { // get the id value
                metric_id = rs.getInt ("ID");
                table_name_string = rs.getString ("TABLE_NAME");
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
                table_name_string = "";

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

    private void putValuesInTable (String table_name_value,
                                  int org_id,
                                  int item_id,
                                  int start_time,
                                  int end_time,
                                  float rate) {
        int rc;
        int rows_to_update = end_time - start_time;

        try
        {
//System.out.print (" update(" + start_time + " to " + end_time + ")");
            // The effective update is
            // UPDATE table_name_value SET assessmentValue = rate
            // WHERE org = org_id AND item = item_id
            // AND unitsOfTime >= start_time AND unitsOfTime < end_time

            updateAssessmentData.setFloat(1,rate);
            updateAssessmentData.setInt(2,org_id);
            updateAssessmentData.setInt(3,item_id);
            updateAssessmentData.setInt(4,start_time);
            updateAssessmentData.setInt(5,end_time);
            rc = updateAssessmentData.executeUpdate();

//            System.out.print ("u" + rows_to_update);

            // If none of the rows were updated, call the stored
            // procedure to insert the rows
            if (rc != rows_to_update) {
//                System.out.print ("sp" + (end_time - start_time));
                stmt.execute ("call new_" + table_name_value + "(" + org_id +
                 ", " + item_id + ", " + start_time + ", " + (end_time - 1) +
                 ", " + rate + ")");
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

    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext parm1) {}

    // Declare an inner class that extends the default sax handler
    // so methods can be overridden to handle the tags the way that
    // the ui code needs to

    public class AggInfoSAXHandler extends DefaultHandler {

        private final int INT_BLANK = -1;
        private final float FLOAT_BLANK = -1.0f;

        int index = 0;

        int org_id = INT_BLANK;
        int metric_id = INT_BLANK;

        String current_string = "";

        int item_id = INT_BLANK;
        int start_time_in_days = INT_BLANK;
        int end_time_in_days = INT_BLANK;
        float rate_float = FLOAT_BLANK;

        // Called immediately before the document is parsed
        public void startDocument () {
            System.out.println ("*****Beginning SAX Parsing of document*****");

            org_id = INT_BLANK;
            metric_id = INT_BLANK;

            current_string = "";

            item_id = INT_BLANK;
            start_time_in_days = INT_BLANK;
            end_time_in_days = INT_BLANK;
            rate_float = FLOAT_BLANK;

            index = 0;

            try {
                stmt = connection.createStatement();
            }
            catch(SQLException e)
            {
                System.out.println ("SQL error code " + e.getErrorCode());
                System.out.println (e.getMessage());
                throw new EJBException(e);
            }

        } /* end of startDocument */

        // Called immediately after the document has been parsed
        public void endDocument () {
            System.out.println ("");
            System.out.println ("*****Done, processed " + index + " records*****");

            try
            {
                connection.commit();
                if (stmt != null) stmt.close();
                if (updateAssessmentData != null)
                    updateAssessmentData.close();
            }
            catch(SQLException e) {}
        }

        // Called when the beginning of a tag has been parsed
        public void startElement (String uri, String name,
                                  String qName, Attributes atts) {
        }

        // Called when the end of a tag has been parsed.  This is
        // where the majority of the work is performed
        public void endElement (String uri, String name, String qName) {

            if (name.compareTo(AggInfoEncoder.getDataAtomXMLString()) == 0) {

                // If there are records to insert
                if ((item_id != INT_BLANK) &&
                    (start_time_in_days != end_time_in_days) &&
                    (rate_float != FLOAT_BLANK)) {

                    try {

                        putValuesInTable (table_name_string, org_id, item_id, start_time_in_days, end_time_in_days, rate_float);

                        index++;

                        if (index % NUM_RECORDS_BEFORE_COMMIT == 0) {
                            // Save the work in the database
                            connection.commit();
                            System.out.print ("(" + index + ")");
                        }
                    }
                    catch(SQLException e)
                    {
                        System.out.println ("SQL error code " + e.getErrorCode());
                        System.out.println (e.getMessage());
                        throw new EJBException(e);
                    }
                }

                // Reset all the fields
                item_id = INT_BLANK;
                start_time_in_days = INT_BLANK;
                end_time_in_days = INT_BLANK;
                rate_float = FLOAT_BLANK;
            } /* end of if data atom tag */

            // Find out if this is an item string
            else if (name.compareTo(AggInfoStructure.getItemXMLString()) == 0) {
                item_id = getItemID(current_string);
            }

            // Find out if this is a start time string
            else if (name.compareTo(AggInfoStructure.getStartTimeXMLString()) == 0) {
                start_time_in_days = Integer.parseInt (current_string);
            }

            // Find out if this is an end time string
            else if (name.compareTo(AggInfoStructure.getEndTimeXMLString()) == 0) {
                end_time_in_days = Integer.parseInt (current_string);
            }

            // Find out if this is a rate string
            else if (name.compareTo(AggInfoStructure.getRateXMLString()) == 0) {
                rate_float = Float.parseFloat(current_string);
            }

            // Find out if this is a metric string
            else if (name.compareTo(AggInfoEncoder.getMetricXMLString()) == 0) {
                metric_id = getMetricID (current_string);
                System.out.println ("metric is " + current_string +
                                    ", metric id is " + metric_id);
                System.out.println ("Inserting into table "+table_name_string);
                createPreparedStatements(table_name_string);
            }

            // Find out if this is an organization string
            else if (name.compareTo(AggInfoEncoder.getOrgXMLString()) == 0) {
                org_id = getOrgID (current_string);
                System.out.println ("org is " + current_string + ", org_id is " + org_id);
            }

            // Find out if this is a value string
            else if (name.compareTo(AggInfoStructure.getValueXMLString()) == 0) {
                rate_float = Float.parseFloat(current_string);
            }

            // Find out if this is a time string
            else if (name.compareTo(AggInfoStructure.getTimeXMLString()) == 0) {
                start_time_in_days = Integer.parseInt (current_string);
                end_time_in_days = start_time_in_days + 1;
            }

            current_string = "";
        }

        // Called to return the string that is between a start and
        // an end tag
        public void characters (char ch[], int start, int length) {
            String new_st = new String (ch, start, length);

            current_string = current_string.concat (new_st);
        }

    } /* end of AggInfoSAXHandler */

}
