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
    public static final String[] UNARY_OPS = {CUMLATIVE_SUM};
    public static final String[] BINARY_OPS = {"+", "-", "*", "/"};

    /** Hashtable that maps metric to metric units */
    public static Hashtable metricUnits = createMetricUnits();

    /** Hashtable that describes derived metrics */
    public static Hashtable derivedMetrics = createDerivedMetrics();

    public static Hashtable createMetricUnits()
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

    public static Hashtable createDerivedMetrics()
    {
        Hashtable dm = new Hashtable();

        Vector f = new Vector();
        f.add(INVENTORY_METRIC);
        f.add(TARGET_LEVEL_METRIC);
        f.add("/");
        dm.put(INV_SAF_METRIC, f);

        f = new Vector();
        f.add(INVENTORY_METRIC);
        f.add(CRITICAL_LEVEL_METRIC);
        f.add("/");
        dm.put(INV_CRITICAL_METRIC, f);

        f = new Vector();
        f.add(DUEIN_METRIC);
        f.add(CUMLATIVE_SUM);
        f.add(DEMAND_METRIC);
        f.add(CUMLATIVE_SUM);
        f.add("/");
        dm.put(RES_DEM_METRIC, f);

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