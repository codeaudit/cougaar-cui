package org.cougaar.lib.uiframework.ui.ohv.util;

import org.apache.xerces.parsers.DOMParser;


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.IOException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/*
import ui.SupKeeper;
import ui.TreeBuilder;
*/
//import dom.DOMParserWrapper;

import java.util.ArrayList;
import java.util.List;









/**
  Hashtable containing keys which are Strings and values which are HashSets.
**/

  public class HashtableSet {
    Hashtable ht=new Hashtable();

      public HashtableSet() {}
    public HashSet put(String key, Object value) {
      HashSet hs;
	    hs = (HashSet)ht.get(key);
	    if (hs==null) {
	      hs = new HashSet();
	    }
	    hs.add(value);
      return (HashSet)ht.put(key, hs);
    }


     public HashSet get(String key) {
 	    return (HashSet)ht.get(key);
    }

     public Set keySet() { return ht.keySet(); }
  }


