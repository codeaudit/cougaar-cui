
package org.cougaar.lib.uiframework.ui.orglocation.data;

public class DayBaseModel {
  public static long baseTimeMillis = 0;

  public static long getMillisForDay (int n) {
    return baseTimeMillis + 24 * 60 * 60 * 1000 * n;
  }

  public static int getDayForMillis (long n) {
    return (int) (n - baseTimeMillis) / (24 * 60 * 60 * 1000);
  }
}