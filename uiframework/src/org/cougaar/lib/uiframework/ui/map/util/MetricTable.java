/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
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
