/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package mil.darpa.log.alpine.blackjack.assessui.client;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

import org.cougaar.lib.uiframework.ui.util.SelectableHashtable;

/**
 * This class provides knowledge about the metrics that should be included
 * in the assessmentMetrics table someday (e.g. metric units)
 */
public class MetricInfo
{
    public static final String ITEM_UNITS = "Items";
    public static final String ITEM_DAY_UNITS = "Items/Day";
    public static final String UNITLESS = "Unitless";

    public static final String GROUPA = "Group A";
    public static final String GROUPB = "Group B";
    public static final String GROUPC = "Group C";

    private static final String INV_SAF_METRIC = "Inventory Over Target Level";
    private static final String INV_CRITICAL_METRIC = "Inventory Over Critical Level";
    private static final String RES_DEM_METRIC = "Cumulative Resupply Over Cumulative Demand";
    private static final String DEMAND_METRIC = "Demand";
    private static final String DUEIN_METRIC = "DueIn";
    private static final String DUEOUT_METRIC = "DueOut";
    private static final String INVENTORY_METRIC = "Inventory";
    private static final String CRITICAL_LEVEL_METRIC = "Critical Level";
    private static final String TARGET_LEVEL_METRIC = "Target Level";

    public static final String CUMLATIVE_SUM = "CUMULATIVE SUM";
    private static final String[] UNARY_OPS = {CUMLATIVE_SUM};
    private static final String[] BINARY_OPS = {"+", "-", "*", "/"};

    /** Hashtable that maps metric to metric units */
    public static Hashtable metricUnits = createMetricUnits();

    /** Hashtable that describes derived metrics */
    public static Hashtable derivedMetrics = createDerivedMetrics();

    private static Hashtable createMetricUnits()
    {
        Hashtable mu = new Hashtable();

        mu.put(DEMAND_METRIC, ITEM_DAY_UNITS);
        mu.put(DUEIN_METRIC, ITEM_DAY_UNITS);
        mu.put(DUEOUT_METRIC, ITEM_DAY_UNITS);
        mu.put(INVENTORY_METRIC, ITEM_UNITS);
        mu.put(TARGET_LEVEL_METRIC, ITEM_UNITS);
        mu.put(CRITICAL_LEVEL_METRIC, ITEM_UNITS);
        mu.put(INV_SAF_METRIC, UNITLESS);
        mu.put(INV_CRITICAL_METRIC, UNITLESS);
        mu.put(RES_DEM_METRIC, UNITLESS);
        mu.put(GROUPA, ITEM_UNITS);
        mu.put(GROUPB, ITEM_DAY_UNITS);
        mu.put(GROUPC, UNITLESS);

        return mu;
    }

    private static Hashtable createDerivedMetrics()
    {
        Hashtable dm = new Hashtable();

        dm.put(INV_SAF_METRIC, new String[] {INVENTORY_METRIC,
                                             TARGET_LEVEL_METRIC,
                                             "/"});
        dm.put(INV_CRITICAL_METRIC, new String[] {INVENTORY_METRIC,
                                                  CRITICAL_LEVEL_METRIC,
                                                  "/"});
        dm.put(RES_DEM_METRIC, new String[] {DUEIN_METRIC,
                                             CUMLATIVE_SUM,
                                             DEMAND_METRIC,
                                             CUMLATIVE_SUM,
                                             "/"});

        return dm;
    }

    public static boolean isDerived(String metric)
    {
        return derivedMetrics.containsKey(metric);
    }

    public static boolean isUnaryOperator(String s)
    {
        boolean isUnary = false;

        for (int i = 0; i < UNARY_OPS.length; i++)
        {
            if (UNARY_OPS[i].equals(s))
            {
                isUnary = true;
                break;
            }
        }

        return isUnary;
    }

    public static boolean isBinaryOperator(String s)
    {
        boolean isBinary = false;

        for (int i = 0; i < BINARY_OPS.length; i++)
        {
            if (BINARY_OPS[i].equals(s))
            {
                isBinary = true;
                break;
            }
        }

        return isBinary;
    }
}