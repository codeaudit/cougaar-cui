/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.ohv.util;

import java.util.Vector;

public  class ArgVector extends Vector {
  private Vector argsVIgnore = new Vector();

  public ArgVector(String args[]) {
    for (int idx=0; idx<args.length; idx++) {
      argsVIgnore.add(args[idx].toLowerCase());
      add(args[idx]);
      System.out.println("Argument #"+(idx+1)+": ["+args[idx]+"]");
    }
  }

  public boolean containsIgnoreCase(String value) {
    return argsVIgnore.contains(value.toLowerCase());
  }
  public boolean contains(String value) {
    return super.contains(value);
  }

  /*     impelement when desired
  public int countStartsWith(String value) {
  }

  public int countStartsWithIgnoreCase(String value) {
  }

  private int countStartsWith(Vector v, String str) {
  }

  public Collection startsWith(String str) {
  }

  public Collection startsWithIgnoreCase(String str) {
  }
  */
}
