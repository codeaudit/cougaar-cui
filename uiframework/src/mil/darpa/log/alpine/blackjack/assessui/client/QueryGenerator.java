package mil.darpa.log.alpine.blackjack.assessui.client;

import java.awt.Component;
import java.util.Enumeration;
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
        VariableModel xAxis = (VariableModel)vim.
                    getDescriptors(VariableModel.X_AXIS).nextElement();
        VariableModel yAxis = (VariableModel)vim.
                    getDescriptors(VariableModel.Y_AXIS).nextElement();

        System.out.println("New Query ->");
        System.out.println("X Axis: " + xAxis + " - " + xAxis.getValue());
        System.out.println("Y Axis: " + yAxis + " - " + yAxis.getValue());

        Enumeration vds = vim.getDescriptors(VariableModel.FIXED);
        while(vds.hasMoreElements())
        {
            VariableModel v = (VariableModel)vds.nextElement();
            System.out.println("Fixed: " + v + " - " + v.getValue());
        }

        //
        // build sql query from general query data
        //

        StringBuffer query =
            new StringBuffer("SELECT * FROM assessmentData WHERE (");
        boolean transpose = false;

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
                                       timeHeaderColumn);
        }

        // transform based on needed X and Y variables
        String xDescName = xAxis.getName();
        String yDescName = yAxis.getName();
        String xColumnName = DBInterface.getColumnName(xDescName);
        String yColumnName = DBInterface.getColumnName(yDescName);
        dbTableModel.setXY(dbTableModel.getColumnIndex(xColumnName),
                           dbTableModel.getColumnIndex(yColumnName),
                           dbTableModel.getColumnIndex("assessmentValue"));

        //
        // convert column and row header ids to names
        //
        if (!xDescName.equalsIgnoreCase("Time"))
        {
            Vector oldColumnHeaders = new Vector();
            for (int column = 1; column < dbTableModel.getColumnCount();
                 column++)
            {
                oldColumnHeaders.add(dbTableModel.getColumnName(column));
            }
            Enumeration newColumnHeaders = DBInterface.
                        lookupValues(DBInterface.getTableName(xColumnName),
                                     "id", "name",
                                     oldColumnHeaders.elements()).elements();

            int columnCount = 1;
            while (newColumnHeaders.hasMoreElements())
            {
                String name = newColumnHeaders.nextElement().toString().trim();
                dbTableModel.setColumnName(columnCount++, name);
            }
        }

        if (!yDescName.equalsIgnoreCase("Time"))
        {
            Vector oldRowHeaders = new Vector();
            for (int row = 0; row < dbTableModel.getRowCount(); row++)
            {
                oldRowHeaders.add(dbTableModel.getValueAt(row, 0));
            }
            Enumeration newRowHeaders = DBInterface.
                lookupValues(DBInterface.getTableName(yColumnName), "id",
                             "name", oldRowHeaders.elements()).elements();
            int rowCount = 0;
            while (newRowHeaders.hasMoreElements())
            {
                dbTableModel.setValueAt(
                    newRowHeaders.nextElement().toString().trim(),
                    rowCount++, 0);
            }
        }

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
        StringBuffer whereClause =  new StringBuffer();
        String varName = v.getName();

        if (varName.equals("Time")) // time is a special case
        {
            RangeModel timeRange = (RangeModel)v.getValue();
            whereClause.append("(unitsOfTime >= ");
            whereClause.append(timeRange.getMin());
            whereClause.append(" AND unitsOfTime <= ");
            whereClause.append(timeRange.getMax());
            whereClause.append(")");
        }
        else if (v.getValue() instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)v.getValue();
            if ((v.getState() == v.FIXED) || (node.isLeaf()))
            {
                whereClause.append(varName + " = " + DBInterface.
                    lookupValue(DBInterface.getTableName(varName), "name",
                                "id", node.toString()));
            }
            else
            {
                Enumeration children = node.children();
                Enumeration childIDs = DBInterface.
                  lookupValues(DBInterface.getTableName(varName), "name",
                               "id", children).elements();
                whereClause.append("(");
                whereClause.append(
                   createDelimitedList(childIDs, varName + " = ", "", " OR "));
                whereClause.append(")");
            }
        }
        else
        {
            whereClause.
                append(varName + " = " + DBInterface.
                       lookupValue(DBInterface.getTableName(varName),
                                   "name", "id", v.getValue().toString()));

        }
        return whereClause.toString();
    }

    /**
     * Aggregate the rows of the table model based on the child list of the
     * given tree node.
     *
     * @param node         node under which to aggregate
     * @param headerColumn index of column that contains row headers to match
     *                     with tree elements.
     */
    private void aggregateTreeRows(DefaultMutableTreeNode node,
                                   int headerColumn)
    {
        for (int i = 0; i < node.getChildCount(); i++)
        {
            DefaultMutableTreeNode tn =
                (DefaultMutableTreeNode)node.getChildAt(i);
            dbTableModel.aggregateRows(getLeafList(tn).elements(),
                                       tn.getUserObject().toString(),
                                       headerColumn);
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
}