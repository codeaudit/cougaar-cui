/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import java.util.*;

public class PlanTableWrapper {
  private Hashtable content = null;
  private String name = null;

  public PlanTableWrapper (String n, Hashtable t) {
    name = n;
    content = t;
  }

  public String getName () {
    return name;
  }

  public Hashtable getContent () {
    return content;
  }
}