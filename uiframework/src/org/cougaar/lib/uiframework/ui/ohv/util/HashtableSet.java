/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv.util;

import java.util.*;

/**
 *  Hashtable containing keys which are Strings and values which are HashSets.
 */
public class HashtableSet {
  Hashtable ht=new Hashtable();

  public HashtableSet () {
  }

  public HashSet put (String key, Object value) {
    HashSet hs;
    hs = (HashSet) ht.get(key);
    if (hs == null) {
      hs = new HashSet();
    }
    hs.add(value);
    return (HashSet) ht.put(key, hs);
  }

  public HashSet get (String key) {
    return (HashSet)ht.get(key);
  }

  public Set keySet () {
    return ht.keySet();
  }
}


