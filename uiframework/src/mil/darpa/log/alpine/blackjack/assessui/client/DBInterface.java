package mil.darpa.log.alpine.blackjack.assessui.client;

import java.sql.*;
import java.util.*;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.transducer.configs.*;
import org.cougaar.lib.uiframework.transducer.elements.*;

import org.cougaar.lib.uiframework.ui.util.DBDatasource;

/**
 * This class is used to extract data from a database that uses the blackjack
 * assessment schema.  The blackjack assessment schema defines four tables:
 * assessmentOgrs, assessmentItems, assessmentMetrics, assessmentValues.
 * The orgs, items, and metrics tables index into the values table.
 * The orgs and items table represent a hierarchy of data items.
 */
public class DBInterface extends DBDatasource
{
    /** Array of strings that represent blackjack metric types */
    public static final Object[]
        metrics = lookupValues("assessmentMetrics", "name").toArray();

    /** Tree that represents blackjack metric groupings */
    public static DefaultMutableTreeNode metricTree = makeMetricTree();

    /** item tree */
    public static DefaultMutableTreeNode
        itemTree = createTree(getTableName("item"));

    /** org tree */
    public static DefaultMutableTreeNode
        orgTree = createTree(getTableName("org"));

    /**
     * Gets tree representation of data in specified table.  Table must follow
     * a given schema.
     */
    public static DefaultMutableTreeNode createTree(String table)
    {
        SqlTableMap config = new SqlTableMap();
        config.setDbTable(table);
        config.setIdKey("id");
        config.setParentKey("parent");
        config.addContentKey("UID", "name");
        //config.addContentKey("annotation", "note");
        config.setPrimaryKeys(new String[] {"keynum"});

        return createTree(restoreFromDb(config));
    }

    private static DefaultMutableTreeNode makeMetricTree()
    {
        DefaultMutableTreeNode p;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        root.add(p = new DefaultMutableTreeNode("Stoplight Components"));
        p.add(new DefaultMutableTreeNode(metrics[0]));
        p.add(new DefaultMutableTreeNode(metrics[1]));
        root.add(p = new DefaultMutableTreeNode("Group A"));
        for (int i = 2; i < metrics.length; i++)
        {
            p.add(new DefaultMutableTreeNode(metrics[i]));
        }

        return root;
    }

    /**
     * get column name that corresponds to variable descriptor name
     *
     * @param variableDescriptorName name of variable descriptor
     * @return name of column that corresponds to variable desciptor name
     */
    public static String getColumnName(String variableDescriptorName)
    {
        String columnName;
        if (variableDescriptorName.equalsIgnoreCase("time"))
        {
            columnName = "unitsOfTime";
        }
        else
        {
            columnName = variableDescriptorName;
        }
        return columnName;
    }

    /**
     * get table name that corresponds to variable descriptor name
     *
     * @param variableDescriptorName name of variable descriptor
     * @return name of table that corresponds to variable desciptor name
     */
    public static String getTableName(String variableDescriptorName)
    {
        String tableName = null;

        if (!variableDescriptorName.equalsIgnoreCase("time"))
        {
            tableName = "assessment" + variableDescriptorName + "s";
        }
        return tableName;
    }
}