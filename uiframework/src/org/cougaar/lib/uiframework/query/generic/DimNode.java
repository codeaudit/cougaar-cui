
package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  DimNode is the base class for nodes used to construct QueryDimension
 *  hierarchies.  
 */
public abstract class DimNode {
  private QueryDimension dim = null;
  private String name = null;

  public QueryDimension getDimension () {
    return dim;
  }

  public void setDimension (QueryDimension d) {
    dim = d;
  }

  public String getName () {
    return name;
  }

  public void setName (String n) {
    name = n;
  }

  public abstract boolean hasChildren ();

  public abstract DimNode hasChild (String name);

  public abstract Enumeration getChildren ();
}