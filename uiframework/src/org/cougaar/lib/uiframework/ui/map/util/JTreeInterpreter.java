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

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.BorderLayout;
import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.transducer.elements.*;
import java.awt.event.*;

public class JTreeInterpreter  {

    String title="";
    public String getTitle() { return title; }
  public JTreeInterpreter () {
  }


    public JTree generate(Structure str) {
	DefaultMutableTreeNode top;
	JTree tree=null;

	Attribute atr=str.getAttribute("hierarchy");
	title= getFirstValForAttribute(atr);

	// get first list in structure
	ListElement me = str.getContentList();
	if (me != null) {
	    tree = new JTree(generateBranch(me));
	}
	return tree;
    }

    String getFirstValForAttribute(Attribute atr) {
	String name="";
	Enumeration en1=null;
	ValElement val=null;
	
	if (atr != null) {
	    en1=atr.getChildren();
	}
	if (en1!=null && en1.hasMoreElements()) {
	    val=((Element)en1.nextElement()).getAsValue();
	}
	if (val!=null) {
	    name = val.getValue();
	}
	return name;
    }

    private String getNodeUID(ListElement le) {
	Attribute atr=le.getAttribute("UID");
	return getFirstValForAttribute(atr);
// 	String name="NO_NAME";
// 	Enumeration en1=null;
// 	ValElement val=null;
	
// 	if (atr != null) {
// 	    en1=atr.getChildren();
// 	}
// 	if (en1!=null && en1.hasMoreElements()) {
// 	    val=((Element)en1.nextElement()).getAsValue();
// 	}
// 	if (val!=null) {
// 	    name = val.getValue();
// 	}
// 	return name;
    }

    private DefaultMutableTreeNode generateBranch(ListElement le) {
	DefaultMutableTreeNode branch;
	String name = getNodeUID(le);
	branch = new DefaultMutableTreeNode(name);

	for (Enumeration en=le.getChildren(); en.hasMoreElements(); ) {
	// for each child of le that is a list 
	    ListElement child=((Element)en.nextElement()).getAsList();
	    if (child != null) {
		branch.add(generateBranch(child));
	    }
	}
	return branch;
    }

    public static void main(String[] argv) {
	JFrame frame;
	JTreeInterpreter jti = new JTreeInterpreter();
	
	if (argv.length < 1)
	    System.out.println("Please specify JTree file.");
	else {
	    try {
		XmlInterpreter xint = new XmlInterpreter();
		
		FileInputStream fin = new FileInputStream(argv[0]);
		Structure s = xint.readXml(fin);
		fin.close();
		
		JTree jtree=jti.generate(s);
		if (jtree==null) {
		    System.err.println("NULL JTree.  Make sure that your file parses correctly.");
		} else {
		    frame=new JFrame();

		    frame.getContentPane().add(jtree, BorderLayout.CENTER);
		    frame.setTitle("Cluster Command Structure");
		    frame.pack();
		    frame.setSize(200, 400);
		    frame.setVisible(true);

		    frame.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
				System.exit(0);
			    }
			});
		}
	    } catch (Exception ex) {
		System.err.println("Exception: "+ex.getMessage());
		ex.printStackTrace();
	    }
	}
    }
}
