// Decompiled by Decafe PRO - Java Decompiler
// Classes: 1   Methods: 4   Fields: 3

package org.cougaar.lib.uiframework.ui.inventory;

import java.util.*;

public class InventoryChartBaseCalendar extends GregorianCalendar
{

    private static final int baseYear = 2000;
//    private static final TimeZone baseTimeZone = TimeZone.getTimeZone("GMT");
    private static final TimeZone baseTimeZone = TimeZone.getDefault();
    private static final InventoryChartBaseCalendar baseCal = new InventoryChartBaseCalendar();

    public InventoryChartBaseCalendar()
    {
        super(2000, 0, 0);
        setTimeZone(baseTimeZone);
    }

    public static long getBaseTime()
    {
        return baseCal.getTime().getTime();
    }

    public static int getBaseYear()
    {
        return 2000;
    }

}
