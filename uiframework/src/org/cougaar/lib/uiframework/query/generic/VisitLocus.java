
package org.cougaar.lib.uiframework.query.generic;

import org.cougaar.lib.uiframework.transducer.elements.*;

public class VisitLocus {
  public DimNode location = null;
  public ListElement record = null;

  public VisitLocus (DimNode l, ListElement r) {
    location = l;
    record = r;
  }
}