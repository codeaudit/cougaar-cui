package mil.darpa.log.alpine.blackjack.assessui.util;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Random;

import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.transducer.configs.*;
import org.cougaar.lib.uiframework.transducer.elements.*;

public class BlackjackTableCreator
{
    private static String dbURL;
    private static String dbDriver;
    private static String user;
    private static String password;
    private static boolean randomdata = Boolean.getBoolean("RANDOMDATA");

    public static void main(String[] args)
    {
        if (args.length != 4)
        {
            System.out.println("Usage: java mil.darpa.log.alpine.blackjack." +
                               "assessui.util.BlackjackTableCreator " +
                               "<access|oracle> <dburl> <user> <password>");
            return;
        }

        String databaseType = args[0];
        if (databaseType.equalsIgnoreCase("access"))
        {
            dbURL = "jdbc:odbc:";
            dbDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
        }
        else if (databaseType.equalsIgnoreCase("oracle"))
        {
            dbURL = "jdbc:oracle:thin:@";
            dbDriver = "oracle.jdbc.driver.OracleDriver";
        }
        else
        {
            System.out.println("Don't recognize " + databaseType +
                               " database");
            return;
        }
        dbURL += args[1];
        user = args[2];
        password = args[3];

        Connection con = null;
        Statement stmt = null;
        try
        {
            con = establishConnection(dbURL);
            stmt = con.createStatement();
            createTables(stmt);
            stmt.close();                      //
            con.close();                       // can't have nested
            populateTransducerTables();        // connections in some
            con = establishConnection(dbURL);  // databases.
            stmt = con.createStatement();      //
            populateOtherTables(stmt);
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

    private static Connection establishConnection(String dbURL) throws Exception
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

        try
        {
            stmt.executeUpdate("DROP TABLE assessmentItems");
        }
        catch(SQLException e) {} // it doesn't yet exist; good
        stmt.executeUpdate("CREATE TABLE assessmentItems " +
                                "(id     INTEGER      NOT NULL," +
                                 "parent INTEGER      NOT NULL," +
                                 "name   CHAR(50)     NOT NULL," +
                                 "primary key (id)            )");

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

    private static void populateTransducerTables() throws Exception
    {
        // Organization Tree
        SqlTableMap config = new SqlTableMap();
        config.setDbTable("assessmentOrgs");
        config.setIdKey("id");
        config.setParentKey("parent");
        config.addContentKey("UID", "name");
        //config.addContentKey("annotation", "note");
        config.setPrimaryKeys(new String[] {"keynum"});
        saveInDb(readFromFile("orgTree.xml"), null, config);

        // Item Tree
        config.setDbTable("assessmentItems");
        saveInDb(readFromFile("itemTree.xml"), null, config);
    }

    private static void populateOtherTables(Statement stmt) throws Exception
    {
        String[] metrics = {"Demand", "Days of Supply",
                            "Supply as Proportion of Demand"};

        for (int i=0; i < metrics.length; i++)
        {
            stmt.executeUpdate("INSERT INTO assessmentMetrics VALUES (" + i +
                               ", '" + metrics[i] + "')");
        }

        // Value table
        Random rand = new Random();
        int orgSize = getNumberOfRows(stmt, "assessmentOrgs");
        int itemSize = getNumberOfRows(stmt, "assessmentItems");
        int metricSize = getNumberOfRows(stmt, "assessmentMetrics");
        for (int org=0; org<orgSize; org++)
            for (int item=0; item<itemSize; item++)
                for (int time=0; time<30; time++)
                    for (int metric=0; metric<metricSize; metric++)
                        stmt.executeUpdate("INSERT INTO assessmentData VALUES"
                                           + " (" + org + ", " + item + ", " +
                                           time + ", " + metric + ", " +
                                           (randomdata ?
                                           String.valueOf(rand.nextFloat() * 2)
                                           : "NULL") + ")");
    }

    private static int getNumberOfRows(Statement stmt, String tableName)
        throws SQLException
    {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        int rowCount = 0;
        while (rs.next()) rowCount++;
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
                                  SqlTableMap config)
    {
        MappedTransducer mt = makeTransducer(config);
        mt.openConnection();
        mt.writeToDb(keys, s);
        mt.closeConnection();
    }

    private static MappedTransducer makeTransducer (SqlTableMap config)
    {
        MappedTransducer mt = new MappedTransducer(dbDriver, config);
        mt.setDbParams(dbURL, user, password);
        return mt;
    }
}
