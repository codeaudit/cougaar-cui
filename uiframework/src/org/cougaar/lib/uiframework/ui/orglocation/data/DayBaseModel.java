/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.orglocation.data;

public class DayBaseModel {
  private static long dayMillis = 24L * 60L * 60L * 1000L;

  public static long baseTimeMillis = 0;

  public static long getMillisForDay (int n) {
    return baseTimeMillis + ((long) n) * dayMillis;
  }

  public static int getDayForMillis (long n) {
    return (int) ((n - baseTimeMillis) / dayMillis);
  }
}
