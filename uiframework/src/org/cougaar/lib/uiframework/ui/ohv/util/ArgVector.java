package org.cougaar.lib.uiframework.ui.ohv.util;

import java.util.Vector;

public  class ArgVector extends Vector {
    private Vector argsVIgnore=new Vector();
    public ArgVector(String args[]) {
      for (int idx=0; idx<args.length; idx++) {
        argsVIgnore.add(args[idx].toLowerCase());
        add(args[idx]);
        System.out.println("Argument #"+(idx+1)+": ["+args[idx]+"]");
      }
    }
    public boolean containsIgnoreCase(String value)
      { return argsVIgnore.contains(value.toLowerCase()); }
    public boolean contains(String value)
      { return super.contains(value); }

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
