package mil.darpa.log.alpine.blackjack.assessui.client;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.ui.models.DatabaseTableModel;
import org.cougaar.lib.uiframework.ui.models.RangeModel;
import org.cougaar.lib.uiframework.ui.models.VariableModel;
import org.cougaar.lib.uiframework.ui.util.VariableInterfaceManager;

/**
 * Used to feed a DatabaseTableModel with SQL queries based on the selected
 * variable values managed by a VariableInterfaceManager.  This code is domain
 * specific to blackjack applications.  Other versions of this class could
 * be created for other domains and database schemas.
 */
public class QueryGenerator
{
    /** the database table model to set queries on. */
    private DatabaseTableModel dbTableModel;

    /**
     * Creates a new query generator.
     *
     * @param dbTableModel the database table model to set queries on
     */
    public QueryGenerator(DatabaseTableModel dbTableModel)
    {
        this.dbTableModel = dbTableModel;
    }

    /**
     * Queries for metric values for a particular set of organizations.
     * Query results are returned in a hashtable where [key = org string] and
     * [value = metric value].  Sample usage of this method is provided in main
     * function of this class.
     *
     * @param orgs set of organization strings for which metrics will be
     *             returned
     * @param item string representing the item of iterest
     *             (or grouping of items)
     * @param time time at which returned metrics are calculated.
     * @param metric the metric for which values will be returned
     *
     * @return query results in a hashtable where [key = org string] and
     *               [value = metric value]
     */
    public static Hashtable getOrgMetrics(Enumeration orgs, String item,
                                          int time, String metric)
    {
        DatabaseTableModel tm = new DatabaseTableModel();
        StringBuffer query =
            new StringBuffer("SELECT * FROM assessmentData WHERE (");

        // filter data needed based on org, item, metric, time
        query.append(generateWhereClause("org", orgs));
        query.append(" AND " + generateWhereClause("item", item));
        query.append(" AND " + generateWhereClause("metric", metric));
        query.append(" AND "+generateWhereClause("time",String.valueOf(time)));
        query.append(")");

        // fill table model with needed data
        System.out.println(query);
        tm.setDBQuery(query.toString());

        // transform table based on needed x and y axis
        String xColumnName = DBInterface.getColumnName("org");
        String yColumnName = DBInterface.getColumnName("time");
        tm.setXY(tm.getColumnIndex(xColumnName),
                 tm.getColumnIndex(yColumnName),
                 tm.getColumnIndex("assessmentValue"));

        // convert org id headers
        convertColumnHeaderIDsToNames("org", tm);

        // create hashtable that contains needed org name -> value pairs
        Hashtable ht = new Hashtable();
        for (int i = 1; i < tm.getColumnCount(); i++)
        {
            ht.put(tm.getColumnName(i), tm.getValueAt(0, i));
        }

        return ht;
    }

    /**
     * Generate a SQL query based on the selected variable values referenced
     * by the given variable interface manager.  Set this query in the
     * query generator's database table model.  Further manipulate the
     * result set as required through the database table model transformation
     * methods.
     *
     * @param vim the variable interface manager from which the variable
     *            value selections will be gathered.
     */
    public void generateQuery(VariableInterfaceManager vim)
    {
        System.out.println(vim);

        //
        // build sql query from general query data
        //
        VariableModel xAxis = (VariableModel)vim.
                    getDescriptors(VariableModel.X_AXIS).nextElement();
        VariableModel yAxis = (VariableModel)vim.
                    getDescriptors(VariableModel.Y_AXIS).nextElement();

        StringBuffer query =
            new StringBuffer("SELECT * FROM assessmentData WHERE (");

        // filter data needed based on org, item, metric, time
        query.append(generateWhereClause(vim.getDescriptor("Org")));
        query.append(" AND "+generateWhereClause(vim.getDescriptor("Item")));
        query.append(" AND "+generateWhereClause(vim.getDescriptor("Metric")));
        query.append(" AND "+generateWhereClause(vim.getDescriptor("Time")));
        query.append(")");

        // order result set
        query.append(" ORDER BY unitsOfTime, org, item, metric");

        System.out.println(query);
        dbTableModel.setDBQuery(query.toString(), 4, 1);

        // aggregation across a time range must be done at the client (here)
        // other aggregation has already been taken care of before DB table.
        /*
        DatabaseTableModel.Combiner combiner =
            new DatabaseTableModel.Combiner() {
                public Object combine(Object obj1, Object obj2)
                {
                    Object combinedObject = null;
                    if (obj1 instanceof Float)
                    {
                        float f1 = ((Float)obj1).floatValue();
                        float f2 = ((Float)obj2).floatValue();
                        float f1Badness = Math.abs(f1 - 1);
                        float f2Badness = Math.abs(f2 - 1);
                        combinedObject = (f1Badness > f2Badness) ? obj1 : obj2;
                    }
                    else
                    {
                        combinedObject = obj1;
                    }

                    return combinedObject;
                }
            };
        */
        DatabaseTableModel.Combiner combiner =
            new DatabaseTableModel.Combiner() {
                public Object combine(Object obj1, Object obj2)
                {
                    Object combinedObject = null;
                    if (obj1 instanceof Float)
                    {
                        float f1 = ((Float)obj1).floatValue();
                        float f2 = ((Float)obj2).floatValue();
                        combinedObject = new Float(f1 + f2);
                    }
                    else
                    {
                        combinedObject = obj1;
                    }

                    return combinedObject;
                }
            };
        VariableModel timeDescriptor = vim.getDescriptor("Time");
        if (timeDescriptor.getState() == VariableModel.FIXED)
        {
            RangeModel timeRange = (RangeModel)timeDescriptor.getValue();
            int timeHeaderColumn = dbTableModel.getColumnIndex("unitsOfTime");
            int[] significantColumns = {dbTableModel.getColumnIndex("org"),
                                        dbTableModel.getColumnIndex("item"),
                                        dbTableModel.getColumnIndex("metric")};
            dbTableModel.aggregateRows(significantColumns,
                                       timeRange.toString(),
                                       timeHeaderColumn, combiner);
        }

        // transform based on needed X and Y variables
        String xDescName = xAxis.getName();
        String yDescName = yAxis.getName();
        String xColumnName = DBInterface.getColumnName(xDescName);
        String yColumnName = DBInterface.getColumnName(yDescName);
        dbTableModel.setXY(dbTableModel.getColumnIndex(xColumnName),
                           dbTableModel.getColumnIndex(yColumnName),
                           dbTableModel.getColumnIndex("assessmentValue"));

        // convert column and row header ids to names
        convertColumnHeaderIDsToNames(xDescName, dbTableModel);
        convertRowHeaderIDsToNames(yDescName, dbTableModel);

        dbTableModel.fireTableChangedEvent(
            new TableModelEvent(dbTableModel, TableModelEvent.HEADER_ROW));
    }

    /**
     * Generate a SQL where clause to constrain a result set based on the
     * contents of the given variable descriptor.
     *
     * @param v variable descriptor to used to formulate the where clause
     * @return the generated where clause
     */
    private String generateWhereClause(VariableModel v)
    {
        String whereClause = null;
        String varName = v.getName();

        if (varName.equals("Time")) // time is a special case
        {
            RangeModel timeRange = (RangeModel)v.getValue();
            StringBuffer timeWhereClause = new StringBuffer();
            timeWhereClause.append("(unitsOfTime >= ");
            timeWhereClause.append(timeRange.getMin());
            timeWhereClause.append(" AND unitsOfTime <= ");
            timeWhereClause.append(timeRange.getMax());
            timeWhereClause.append(")");
            whereClause = timeWhereClause.toString();
        }
        else if (v.getValue() instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)v.getValue();
            if ((v.getState() == v.FIXED) || (node.isLeaf()))
            {
                whereClause = generateWhereClause(varName, node.toString());
            }
            else
            {
                Enumeration children = node.children();
                whereClause = generateWhereClause(varName, children);
            }
        }
        else
        {
            whereClause =generateWhereClause(varName, v.getValue().toString());
        }
        return whereClause;
    }

    private static String generateWhereClause(String varName,
                                              Enumeration neededValues)
    {
        StringBuffer whereClause =  new StringBuffer();
        String columnName = DBInterface.getColumnName(varName);

        whereClause.append("(");

        Enumeration neededIDs;
        if (varName.equalsIgnoreCase("time"))
        {
            neededIDs = neededValues;
        }
        else
        {
            neededIDs = DBInterface.
                lookupValues(DBInterface.getTableName(varName), "name",
                             "id", neededValues).elements();
        }
        whereClause.append(
            createDelimitedList(neededIDs, columnName + " = ", "", " OR "));
        whereClause.append(")");

        return whereClause.toString();
    }

    private static String generateWhereClause(String varName,
                                              String neededValue)
    {
        StringBuffer whereClause = new StringBuffer();
        String columnName = DBInterface.getColumnName(varName);

        whereClause.append(columnName);
        whereClause.append(" = ");
        if (varName.equalsIgnoreCase("time"))
        {
            whereClause.append(neededValue);
        }
        else
        {
            whereClause.append(
                DBInterface.lookupValue(DBInterface.getTableName(varName),
                                       "name", "id", neededValue));
        }

        return whereClause.toString();
    }

    private static void convertColumnHeaderIDsToNames(String varName,
                                                      DatabaseTableModel tm)
    {
        if (!varName.equalsIgnoreCase("time"))
        {
            Vector oldColumnHeaders = new Vector();
            for (int column = 1; column < tm.getColumnCount(); column++)
            {
                oldColumnHeaders.add(tm.getColumnName(column));
            }
            Enumeration newColumnHeaders = DBInterface.
                lookupValues(DBInterface.getTableName(varName), "id", "name",
                             oldColumnHeaders.elements()).elements();

            int columnCount = 1;
            while (newColumnHeaders.hasMoreElements())
            {
                String name = newColumnHeaders.nextElement().toString().trim();
                tm.setColumnName(columnCount++, name);
            }
        }
    }

    private static void convertRowHeaderIDsToNames(String varName,
                                                   DatabaseTableModel tm)
    {
        if (!varName.equalsIgnoreCase("Time"))
        {
            Vector oldRowHeaders = new Vector();
            for (int row = 0; row < tm.getRowCount(); row++)
            {
                oldRowHeaders.add(tm.getValueAt(row, 0));
            }
            Enumeration newRowHeaders = DBInterface.
                lookupValues(DBInterface.getTableName(varName), "id",
                             "name", oldRowHeaders.elements()).elements();
            int rowCount = 0;
            while (newRowHeaders.hasMoreElements())
            {
                tm.setValueAt(
                    newRowHeaders.nextElement().toString().trim(),
                    rowCount++, 0);
            }
        }
    }

    /**
     * Aggregate the rows of the table model based on the child list of the
     * given tree node.
     *
     * @param node         node under which to aggregate
     * @param headerColumn index of column that contains row headers to match
     *                     with tree elements.
     * @param combiner     the object used to combine two values into one.
     */
    private void aggregateTreeRows(DefaultMutableTreeNode node,
                                   int headerColumn,
                                   DatabaseTableModel.Combiner combiner)
    {
        for (int i = 0; i < node.getChildCount(); i++)
        {
            DefaultMutableTreeNode tn =
                (DefaultMutableTreeNode)node.getChildAt(i);
            dbTableModel.aggregateRows(getLeafList(tn).elements(),
                                       tn.getUserObject().toString(),
                                       headerColumn, combiner);
        }
    }

    /**
     * Creates a delimited list string based on given parameters.
     *
     * @param items objects that will be converted to strings as the
     *              elements of the new delimited list
     * @param prefix prefix to include before each element in the list
     * @param postfix postfix to include after each element in the list
     * @param delimiter delimiter to include between the elements of the list
     * @return the generated delimited list string
     */
    private static String createDelimitedList(Enumeration items, String prefix,
                                              String postfix, String delimiter)
    {
        StringBuffer list = new StringBuffer();
        while (items.hasMoreElements())
        {
            Object item = items.nextElement();
            list.append(prefix + item.toString() + postfix);
            if (items.hasMoreElements()) list.append(delimiter);
        }
        return list.toString();
    }

    /**
     * Returns a vector of all the leaf nodes under the given tree node.
     *
     * @param tn tree node to traverse
     * @return vector of all the leaf nodes under the given tree node
     */
    private static Vector getLeafList(TreeNode tn)
    {
        Vector leafList = new Vector();

        if (tn.isLeaf())
        {
            leafList.add(tn);
        }
        else
        {
            Enumeration children = tn.children();
            while (children.hasMoreElements())
            {
                TreeNode node = (TreeNode)children.nextElement();
                leafList.addAll(getLeafList(node));
            }
        }
        return leafList;
    }

    /**
     * Example use of static getOrgMetrics method
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        if ((System.getProperty("DBTYPE") == null) ||
            (System.getProperty("DBURL") == null) ||
            (System.getProperty("DBUSER") == null) ||
            (System.getProperty("DBPASSWORD") == null))
        {
            System.out.println("You need to set the following property" +
                               " variables:  DBTYPE, DBURL, DBUSER, and " +
                               "DBPASSWORD");
            return;
        }

        // create some type of enumeration of organization strings
        Vector orgsOfInterest = new Vector();
        orgsOfInterest.add("All Units");
        orgsOfInterest.add("11INBR");
        orgsOfInterest.add("06INBN");
        orgsOfInterest.add("09ATBN");
        orgsOfInterest.add("22MTBN");
        orgsOfInterest.add("10ARBR");

        for (int time = 0; time < 29; time++)
        {
            // call static method in query generator to get a hashtable that
            // contains a value for each org of interest
            Hashtable ht =
                QueryGenerator.getOrgMetrics(orgsOfInterest.elements(),
                                             "All Items", time, "Demand");
            System.out.println("\n Demand values for all items at time C+" +
                               time);
            printHashtable(ht);
        }
    }

    /**
     * for debug
     */
    private static void printHashtable(Hashtable ht)
    {
        System.out.println("----------");
        Enumeration htKeys = ht.keys();
        while (htKeys.hasMoreElements())
        {
            Object key = htKeys.nextElement();
            System.out.println(key + ": " + ht.get(key));
        }
        System.out.println("----------");
    }
}