package mil.darpa.log.alpine.blackjack.assessui.client;

import java.sql.*;
import java.util.*;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.transducer.configs.*;
import org.cougaar.lib.uiframework.transducer.elements.*;

import org.cougaar.lib.uiframework.ui.util.DBDatasource;
import org.cougaar.lib.uiframework.ui.util.SelectableHashtable;

/**
 * This class is used to extract data from a database that uses the blackjack
 * assessment schema.  The blackjack assessment schema defines four tables:
 * assessmentOgrs, assessmentItems, assessmentMetrics, assessmentValues.
 * The orgs, items, and metrics tables index into the values table.
 * The orgs and items table represent a hierarchy of data items.
 */
public class DBInterface extends DBDatasource
{
    /** Item tree from database */
    public static DefaultMutableTreeNode
        itemTree = createItemTree();  //Uncomment when using itemWeights table
        //itemTree = createTree(getTableName("item"));

    /** Organization tree from database */
    public static DefaultMutableTreeNode
        orgTree = createTree(getTableName("org"));

    /** Minimum time found in time column of assessment data */
    public static int minTimeRange =
        getIntFromQuery("SELECT MIN(" + DBInterface.getColumnName("Time") +
                        ") FROM AssessmentData");

    /** Maximum time found in time column of assessment data */
    public static int maxTimeRange =
        getIntFromQuery("SELECT MAX(" + DBInterface.getColumnName("Time") +
                        ") FROM AssessmentData");

    /** Array of strings that represent blackjack metric types */
    public static final Object[]
        metrics = lookupValues("assessmentMetrics", "name").toArray();
    public static final Object[]
        metricIDs = lookupValues("assessmentMetrics", "id").toArray();

    /** Tree that represents blackjack metric groupings */
    public static DefaultMutableTreeNode metricTree = makeMetricTree();

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
        config.addContentKey("ID", "id");
        //config.addContentKey("annotation", "note");
        config.setPrimaryKeys(new String[] {"keynum"});

        return createTree(restoreFromDb(config));
    }

    /**
     * Gets tree representation of data in specified table.  Table must follow
     * a given schema.
     */
    public static DefaultMutableTreeNode createItemTree()
    {
        String itemTable = getTableName("item");
        SqlTableMap config = new SqlTableMap();
        config.setDbTable(itemTable);
        config.setDbTable(itemTable + ", assessmentItemUnits");
        config.setPrimaryTableName(itemTable);
        config.setJoinConditions("assessmentItemUnits.nsn(+)=" + itemTable + ".item_id");
        config.setIdKey("id");
        config.setParentKey("parent_id");
        config.addContentKey("UID", "name");
        config.addContentKey("ID", "id");
        config.addContentKey("ITEM_ID", "item_id");
        config.addContentKey("UNITS", "unit_issue");
        config.setPrimaryKeys(new String[] {"keynum"});
        Structure trees = restoreFromDb(config);
        System.out.println("Item Tree Read from Database");
        DefaultMutableTreeNode tree = createTree(trees);
        System.out.println("Item Tree Ready");
        return tree;
    }

    private static DefaultMutableTreeNode makeMetricTree()
    {
        DefaultMutableTreeNode p;
        DefaultMutableTreeNode groupB;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        root.add(p = new DefaultMutableTreeNode("Group A"));
        root.add(groupB = new DefaultMutableTreeNode("Group B"));


        for (int i = 0; i < metrics.length; i++)
        {
            Hashtable ht = new SelectableHashtable("UID");
            ht.put("UID", metrics[i]);
            ht.put("ID", metricIDs[i]);

            if ((i == 1) || (i == 3))
            {
                p.add(new DefaultMutableTreeNode(ht));
            }
            else
            {
                groupB.add(new DefaultMutableTreeNode(ht));
            }
        }

        return root;
    }

    private static int getIntFromQuery(String query)
    {
        try
        {
            String intString = (String)
                DBInterface.executeVectorReturnQuery(query).firstElement();
            return Integer.parseInt(intString);
        }
        catch(Exception e)
        {
            return 0;
        }
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
            if (variableDescriptorName.equalsIgnoreCase("Item"))
            {
                tableName = "itemWeights";
            }
            else
            {
                tableName = "assessment" + variableDescriptorName + "s";
            }
        }
        return tableName;
    }

    /**
     * Modify show property on every node of a tree.
     *
     * @param tn root of tree to modify.
     * @param prop new show property for all nodes.
     */
    public static void
        setNewShowProperty(DefaultMutableTreeNode tn, String prop)
    {
        SelectableHashtable ht = (SelectableHashtable)tn.getUserObject();
        ht.setSelectedProperty(prop);

        for (int i = 0; i < tn.getChildCount(); i++)
        {
            DefaultMutableTreeNode ctn =
                (DefaultMutableTreeNode)tn.getChildAt(i);
            setNewShowProperty(ctn, prop);
        }
    }
}