package mil.darpa.log.alpine.blackjack.assessui.util;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Date;
import java.util.Random;

import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.transducer.configs.*;
import org.cougaar.lib.uiframework.transducer.elements.*;

public class BlackjackTableCreator
{
    private static String dbURL;
    private static String dbDriver;
    private static boolean accessdb = false;
    private static boolean randomdata = Boolean.getBoolean("RANDOMDATA");
    private static boolean createItemTable = Boolean.getBoolean("CREATEITEMS");
    private static boolean createMetricTable=Boolean.getBoolean("CREATEMETRICS");
    private static boolean createOrgTable = Boolean.getBoolean("CREATEORGS");
    private static boolean createDataTable = Boolean.getBoolean("CREATEDATA");
    private static boolean createItemUnitTable =
        Boolean.getBoolean("CREATEITEMUNITTABLE");
    private static int startTime = Integer.getInteger("STARTTIME").intValue();
    private static int endTime = Integer.getInteger("ENDTIME").intValue();

    public static void main(String[] args)
    {
        if (args.length != 4)
        {
            System.out.println("Usage: java mil.darpa.log.alpine.blackjack." +
                               "assessui.util.BlackjackTableCreator " +
                               "<access|oracle> <dburl> <user> <password>");
            return;
        }

        System.out.println("Database Type: " + args[0]);
        System.out.println(" Database URL: " + args[1]);
        System.out.println("  User Accout: " + args[2] + "\n");

        String databaseType = args[0];
        if (databaseType.equalsIgnoreCase("access"))
        {
            dbURL = "jdbc:odbc:";
            dbDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
            accessdb = true;
        }
        else if (databaseType.equalsIgnoreCase("oracle"))
        {
            dbURL = "jdbc:oracle:thin:@";
            dbDriver = "oracle.jdbc.driver.OracleDriver";
            accessdb = false;
        }
        else
        {
            System.out.println("Don't recognize " + databaseType +
                               " database");
            return;
        }
        dbURL += args[1];
        String user = args[2];
        String password = args[3];

        Connection con = null;
        Statement stmt = null;
        try
        {
            con = establishConnection(dbURL, user, password);
            stmt = con.createStatement();
            createTables(stmt);
            stmt.close();                               //
            con.close();                                // can't have nested
            populateTransducerTables(user, password);   // connections in some
            con = establishConnection(dbURL, user, password);  // databases.
            con.setAutoCommit(false);                          //
            populateOtherTables(con);
            con.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            }
            catch(SQLException e){}
        }
    }

    private static Connection
        establishConnection(String dbURL, String user, String password)
        throws Exception
    {
        Connection conOracle = null;

        //
        // Database
        //
         try
        {
            Class.forName(dbDriver);
        }
        catch(Exception e)
        {
            System.out.println("Failed to load driver");
        }

        // Connect to the database
        conOracle = DriverManager.getConnection(dbURL, user, password);

        return conOracle;
    }

    private static void createTables(Statement stmt) throws SQLException
    {
        if (createDataTable)
        {
            try
            {
                stmt.executeUpdate("DROP TABLE assessmentData");
            }
            catch(SQLException e) {/* it doesn't yet exist; good */}
            stmt.executeUpdate("CREATE TABLE assessmentData " +
                                "(org             INTEGER     NOT NULL," +
                                 "item            INTEGER     NOT NULL," +
                                 "unitsOfTime     INTEGER     NOT NULL," +
                                 "metric          INTEGER     NOT NULL," +
                                 "assessmentValue FLOAT," +
                            "primary key (org, item, metric, unitsOfTime))");
        }

        if (createOrgTable)
        {
            try
            {
                stmt.executeUpdate("DROP TABLE assessmentOrgs");
            }
            catch(SQLException e) {} // it doesn't yet exist; good
            stmt.executeUpdate("CREATE TABLE assessmentOrgs " +
                                "(id     INTEGER      NOT NULL," +
                                 "parent INTEGER      NOT NULL," +
                                 "name   CHAR(50)     NOT NULL," +
                                 "primary key (id)            )");

            stmt.executeUpdate ("create index assessmentOrgs_name_index on assessmentOrgs (name)");
        }

        if (createItemTable)
        {
            try
            {
                stmt.executeUpdate("DROP TABLE itemWeights");
            }
            catch(SQLException e) {} // it doesn't yet exist; good
            stmt.executeUpdate("CREATE TABLE itemWeights" +
                                "(id     INTEGER      NOT NULL," +
                                 "item_id CHAR(20)," +
                                 "parent_id INTEGER," +
                                 "parent_id_text CHAR(20)," +
                                 "name   CHAR(70)," +
                                 "weight double," +
                                 "primary key (id)            )");

            stmt.executeUpdate ("create index itemWeights_item_id_index on itemWeights (item_id)");
        }

        if (createMetricTable)
        {
            try
            {
                stmt.executeUpdate("DROP TABLE assessmentMetrics");
            }
            catch(SQLException e) {/* it doesn't yet exist; good */}
            stmt.executeUpdate("CREATE TABLE assessmentMetrics " +
                                "(id     INTEGER      NOT NULL," +
                                 "name   CHAR(50)     NOT NULL," +
                                "primary key (id))");
        }

        if (createItemUnitTable)
        {
            try
            {
                stmt.executeUpdate("DROP TABLE assessmentItemUnits");
            }
            catch(SQLException e) {/* it doesn't yet exist; good */}
            stmt.executeUpdate("CREATE TABLE assessmentItemUnits " +
                                "(nsn        VARCHAR2(32) NOT NULL," +
                                 "unit_issue CHAR(2)      NOT NULL," +
                                "primary key (nsn))");
        }
    }

    private static void populateTransducerTables(String user, String password)
        throws Exception
    {
        // Organization Tree
        SqlTableMap config = new SqlTableMap();
        config.setDbTable("assessmentOrgs");
        config.setIdKey("id");
        config.setParentKey("parent");
        config.addContentKey("UID", "name");
        //config.addContentKey("annotation", "note");
        config.setPrimaryKeys(new String[] {"keynum"});

        if (createOrgTable)
        {
            saveInDb(readFromFile("orgTree.xml"), null, config, user,password);
        }

        if (createItemTable)
        {
            // Item Tree
            config.setDbTable("itemWeights");
            saveInDb(readFromFile("itemTree.xml"), null, config,user,password);
        }
    }

    private static void populateOtherTables(Connection con) throws Exception
    {
        Statement stmt = con.createStatement();

        // metric table
        if (createMetricTable)
        {
            String[] metrics = {"Demand", "Days of Supply",
                                "Supply as Proportion of Demand"};

            for (int i=0; i < metrics.length; i++)
            {
                stmt.executeUpdate("INSERT INTO assessmentMetrics VALUES ("
                                    + i + ", '" + metrics[i] + "')");
            }

            con.commit();
        }

        // item unit table (based on tables from blackjack8)
        if (createItemUnitTable)
        {
            Connection anothercon = null;
            Statement anotherstmt = null;
            ResultSet anotherrs = null;
            try
            {
                // class VIII
                anothercon = establishConnection(dbURL, "blackjack8",
                    (dbURL.endsWith("eiger.alpine.bbn.com:1521:alp") ?
                                    "init1389" : "blackjack8"));
                anotherstmt = anothercon.createStatement();
                anotherrs = anotherstmt.executeQuery(
                    "SELECT nsn, unit_issue FROM catalog_master");
                while(anotherrs.next())
                {
                    stmt.executeUpdate(
                        "INSERT INTO assessmentItemUnits VALUES ('NSN/" +
                        anotherrs.getString(1) + "', '" +
                        anotherrs.getString(2)+"')");
                }
                anotherrs.close();
                anotherstmt.close();
                anothercon.close();
                con.commit();

                // class I
                anothercon = establishConnection(dbURL, "blackjack",
                    (dbURL.endsWith("eiger.alpine.bbn.com:1521:alp") ?
                                    "init1389" : "blackjack"));
                anotherstmt = anothercon.createStatement();
                anotherrs = anotherstmt.executeQuery(
                    "SELECT nsn, ui FROM class1_item");
                while(anotherrs.next())
                {
                    try {
                        stmt.executeUpdate(
                            "INSERT INTO assessmentItemUnits VALUES ('NSN/" +
                            anotherrs.getString(1) + "', '" +
                            anotherrs.getString(2) + "')");
                    }
                    catch (Exception e){System.out.println(e.getMessage());}
                }
                anotherrs.close();
                anotherstmt.close();
                anothercon.close();
                con.commit();

                // class III
                anothercon = establishConnection(dbURL, "icis",
                    (dbURL.endsWith("eiger.alpine.bbn.com:1521:alp") ?
                                    "init1389" : "icis"));
                anotherstmt = anothercon.createStatement();
                anotherrs = anotherstmt.executeQuery(
                    "SELECT nsn, ui FROM header WHERE nsn like '91%'");
                while(anotherrs.next())
                {
                    try {
                        stmt.executeUpdate(
                            "INSERT INTO assessmentItemUnits VALUES ('NSN/" +
                            anotherrs.getString(1) + "', '" +
                            anotherrs.getString(2) + "')");
                    }
                    catch (Exception e){System.out.println(e.getMessage());}
                }
                anotherrs.close();
                anotherstmt.close();
                anothercon.close();
                con.commit();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (anotherrs != null) anotherrs.close();
                    if (anotherstmt != null) anotherstmt.close();
                    if (anothercon != null) anothercon.close();
                }
                catch(SQLException e){}
            }

            con.commit();
        }

        if (createDataTable)
        {
            // Value table
            Random rand = new Random();
            int orgSize = getNumberOfRows(stmt, "assessmentOrgs");
            int itemSize = getNumberOfRows(stmt, "itemWeights");
            int metricSize = getNumberOfRows(stmt, "assessmentMetrics");
            System.out.println("Time Range from "+startTime+" to "+endTime);
            System.out.println(
                "Number of rows to be inserted into assessmentData: " +
            (orgSize * (itemSize+1) * metricSize * (endTime - startTime + 1)));

            PreparedStatement prepStmt = null;
            prepStmt = con.prepareStatement(
                "INSERT INTO assessmentData VALUES (?, ?, ?, ?, ?)");
            for (int org=0; org<orgSize; org++)
            {
                long start = (new Date()).getTime();
                for (int item=0; item<(itemSize+1); item++)
                    for (int time=startTime; time<(endTime+1); time++)
                        for (int metric=0; metric<metricSize; metric++)
                        {
                            // access db does not support prepared statements
                            if (accessdb)
                            {
                                stmt.executeUpdate(
                                    "INSERT INTO assessmentData VALUES"
                                    + " (" + org + ", " + item + ", " +
                                    time + ", " + metric + ", " +
                                    (randomdata ?
                                    String.valueOf(rand.nextFloat() * 2)
                                    : "NULL") + ")");
                            }
                            else
                            {
                                prepStmt.setInt(1, org);
                                prepStmt.setInt(2, item);
                                prepStmt.setInt(3, time);
                                prepStmt.setInt(4, metric);
                                if (randomdata)
                                {
                                    prepStmt.setFloat(5, rand.nextFloat() * 2);
                                }
                                else
                                {
                                    prepStmt.setNull(5, Types.FLOAT);
                                }
                                prepStmt.executeUpdate();
                            }
                        }
                con.commit();
                long secondsPassed = ((new Date()).getTime() - start)/1000;
                long secondsToGo = secondsPassed * (orgSize - org);
                long hoursToGo = secondsToGo / 3600;
                secondsToGo -= (hoursToGo * 3600);
                long minutesToGo = secondsToGo / 60;
                secondsToGo -= (minutesToGo * 60);
                System.out.println("\nCompleted filling data for org #" + org);
                System.out.println("Estimated time to go: " + hoursToGo +
                               " hours, " + minutesToGo + " minutes, and " +
                               secondsToGo + " seconds");
            }
            prepStmt.close();
        }

        stmt.close();
    }

    private static int getNumberOfRows(Statement stmt, String tableName)
        throws SQLException
    {
        ResultSet rs = stmt.executeQuery("SELECT COUNT (*) FROM " + tableName);
        rs.next();
        int rowCount = rs.getInt(1);
        rs.close();
        System.out.println(rowCount + " rows in " + tableName);
        return rowCount;
    }

    private static Structure readFromFile (String fileName) throws Exception
    {
        XmlInterpreter xint = new XmlInterpreter();
        FileInputStream fin = new FileInputStream(fileName);
        Structure s = xint.readXml(fin);
        fin.close();
        return s;
    }

    private static void saveInDb (Structure s, String[] keys,
                                  SqlTableMap config, String user,
                                  String password)
    {
        MappedTransducer mt = makeTransducer(config, user, password);
        mt.openConnection();
        mt.writeToDb(keys, s);
        mt.closeConnection();
    }

    private static MappedTransducer
        makeTransducer(SqlTableMap config, String user, String password)
    {
        MappedTransducer mt = new MappedTransducer(dbDriver, config);
        mt.setDbParams(dbURL, user, password);
        return mt;
    }
}
