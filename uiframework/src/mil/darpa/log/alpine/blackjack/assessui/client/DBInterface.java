/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
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
        itemTree = createItemTree();

    /** Organization tree from database */
    public static DefaultMutableTreeNode
        orgTree = createTree(getTableName("org"));

    /** Minimum time found in time column of assessment data */
    public static int minTimeRange = getTimeExt(false);

    /** Maximum time found in time column of assessment data */
    public static int maxTimeRange = getTimeExt(true);

    /** Array of strings that represent blackjack metric types */
    // used specialized select for backwards compat. with old dbs
    public static final Vector rawMetrics =
        executeVectorReturnQuery(
            "SELECT name FROM assessmentMetrics WHERE table_name IS NOT NULL");

    /** Hashtable to manage aggregation schemes for each type of metric */
    public static Object[] aggregationSchemeLabels = createAggLabels();
    public static Hashtable aggregationSchemes = createDefaultAggSchemes();

    public static Vector getAllMetrics()
    {
        Vector allMetrics = (Vector)rawMetrics.clone();
        Enumeration keys = MetricInfo.derivedMetrics.keys();
        while (keys.hasMoreElements())
        {
            allMetrics.add(keys.nextElement());
        }
        return allMetrics;
    }

    /** used to get min or max time for all metrics */
    private static int getTimeExt(boolean max)
    {
        Vector metricTables =
            executeVectorReturnQuery("SELECT table_name FROM " +
                                     DBInterface.getTableName("Metric"));
        int time = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        String function = max ? "MAX" : "MIN";
        for (int i = 0; i < metricTables.size(); i++)
        {
            int queryResult = getIntFromQuery("SELECT " + function + "(" +
                               DBInterface.getColumnName("Time") +
                               ") FROM "+metricTables.elementAt(i).toString());
            time = max?Math.max(queryResult, time):Math.min(queryResult, time);
        }

        return time;
    }

    private static Object[] createAggLabels()
    {
        Vector aggLabels = getAllMetrics();
        aggLabels.add(MetricInfo.GROUPA);
        aggLabels.add(MetricInfo.GROUPB);
        aggLabels.add(MetricInfo.GROUPC);

        return aggLabels.toArray();
    }

    /**
     * update aggregation schemes and labels to include latest derived metrics
     */
    public static void updateAggSchemes()
    {
        aggregationSchemeLabels = createAggLabels();

        for (int i = 0; i < aggregationSchemeLabels.length; i++)
        {
            String metric = (String)aggregationSchemeLabels[i];
            if (aggregationSchemes.get(metric) == null)
            {
                String units = (String)MetricInfo.metricUnits.get(metric);
                aggregationSchemes.put(metric, createDefaultAggScheme(units));
            }
        }
    }

    public static Hashtable createDefaultAggSchemes()
    {
        Hashtable aggSchemes = new Hashtable();

        for (int i = 0; i < aggregationSchemeLabels.length; i++)
        {
            String metric = (String)aggregationSchemeLabels[i];
            String units = (String)MetricInfo.metricUnits.get(metric);
            aggSchemes.put(metric, createDefaultAggScheme(units));
        }

        return aggSchemes;
    }

    private static AggregationScheme createDefaultAggScheme(String units)
    {
            int defaultTimeAggregation =
                units.equals(MetricInfo.ITEM_DAY_UNITS) ?
                        AggregationScheme.SUM : AggregationScheme.AVG;;

            return new AggregationScheme(AggregationScheme.SUM,
                                         defaultTimeAggregation,
                                         AggregationScheme.NONE);
    }

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
        config.setPrimaryTableName(itemTable);
        if (!DBTYPE.equalsIgnoreCase("access"))
        {
            config.setJoinConditions("assessmentItemUnits.nsn(+)=" +
                                     itemTable + ".item_id");
            config.setDbTable(itemTable + ", assessmentItemUnits");
        }
        else
        {
            config.setDbTable(itemTable +
                " LEFT JOIN assessmentItemUnits ON assessmentItemUnits.nsn =" +
                itemTable + ".item_id");
        }
        config.setIdKey("id");
        config.setParentKey("parent_id");
        config.addContentKey("UID", "name");
        config.addContentKey("ID", "id");
        config.addContentKey("ITEM_ID", "item_id");
        config.addContentKey("WEIGHT", "weight");
        config.addContentKey("UNITS", "unit_issue");
        config.setPrimaryKeys(new String[] {"keynum"});
        Structure trees = restoreFromDb(config);
        System.out.println("Item Tree Read from Database");
        DefaultMutableTreeNode tree = createTree(trees);
        System.out.println("Item Tree Ready");
        return tree;
    }

    public static DefaultMutableTreeNode makeMetricTree()
    {
        Vector metrics = getAllMetrics();

        DefaultMutableTreeNode p;
        DefaultMutableTreeNode groupB;
        DefaultMutableTreeNode groupC;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        root.add(p = new DefaultMutableTreeNode(MetricInfo.GROUPA));
        root.add(groupB = new DefaultMutableTreeNode(MetricInfo.GROUPB));
        root.add(groupC = new DefaultMutableTreeNode(MetricInfo.GROUPC));

        for (int i = 0; i < metrics.size(); i++)
        {
            String metric = (String)metrics.elementAt(i);

            Hashtable ht = new SelectableHashtable("UID");
            ht.put("UID", metric);
            ht.put("ID", String.valueOf(i));

            Hashtable mu = MetricInfo.metricUnits;
            if (mu.get(metric).equals(mu.get(MetricInfo.GROUPA)))
            {
                p.add(new DefaultMutableTreeNode(ht));
            }
            else if (mu.get(metric).equals(mu.get(MetricInfo.GROUPB)))
            {
                groupB.add(new DefaultMutableTreeNode(ht));
            }
            else if (mu.get(metric).equals(mu.get(MetricInfo.GROUPC)))
            {
                groupC.add(new DefaultMutableTreeNode(ht));
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