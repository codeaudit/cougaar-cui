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

package org.cougaar.lib.uiframework.ui.map.util;

//import java.io.*;
import java.util.*;
//import javax.swing.*;
//import javax.swing.event.*;
//import javax.swing.tree.*;
//import java.awt.BorderLayout;
//import org.cougaar.lib.uiframework.transducer.*;
//import org.cougaar.lib.uiframework.transducer.elements.*;
//import java.awt.event.*;

public class MetricTable {
    Hashtable col=new Hashtable();
    public Float put(String str, String val) throws NumberFormatException, NullPointerException {
	return  (Float) col.put(str, new Float(val));
    }
    public Float get(String str) {
	return (Float) col.get(str);
    }
    public String toString() { return col.toString(); }
    public Hashtable asHashtable() { return col; }  // yuk, but it is just for convenience for a short time
}
