/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
