package org.cougaar.lib.uiframework.ui.ohv;

//import org.apache.xerces.parsers.DOMParser;
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

import org.cougaar.lib.uiframework.ui.ohv.util.DomUtil;


import java.util.ArrayList;
import java.util.List;

/**
  Parses a cluster Element and determines whether the Element contains a
  Superior relationship which is of interest.  Currently, "AdministrativeSuperior"
  relationships are of interest.
**/
  class ClusterParser {
    String id=null, other=null, rel=null;
    long startTime=-1;
    long endTime=-1;
    final boolean debug=
    //true;
     false;
  String indent1="  ";
  String indent2=indent1+indent1;

    public boolean equals(Object o) {
      return (o instanceof ClusterParser && o.equals(this));
    }
    public boolean equals(ClusterParser cp) {
      boolean rc=id.equals(cp.id);
      rc=rc&&other.equals(cp.other);
      rc=rc&&rel.equals(cp.rel);
      rc=rc&&startTime==cp.startTime;
      rc=rc&&endTime==cp.endTime;
      return rc;
    }

    public String getId() { return id; }
    public boolean hasSuperior() { return (rel!=null&&rel.equalsIgnoreCase("AdministrativeSuperior")); }
    public String getSuperior() {
      String sup=null;
      if (hasSuperior()) {
        sup=other;
      }
      return sup;
    }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public String getOtherId() { return other; }
    public String getRelationship() { return rel; }


    public ClusterParser(Element cel) {
        id=cel.getAttribute("ID");
        //getValues(cel, "other");
        //getValues(cel, "relationship");
        other=DomUtil.getSingleElementValue(cel, "other");
        rel=DomUtil.getSingleElementValue(cel,"relationship");
        String startString=DomUtil.getSingleElementValue(cel,"starttime");
        String endString=DomUtil.getSingleElementValue(cel,"endtime");

        if (startString!=null) {
          startTime=Long.parseLong(startString);
        } else {
          startTime=Long.MIN_VALUE;
        }

        if (endString!=null) {
          endTime=Long.parseLong(endString);
        } else {
          endTime=Long.MAX_VALUE;
        }

        if (debug) {
          System.out.println(indent2+"ID is ["+id+"]");
          System.out.println(indent2+"other is ["+other+"]");
          System.out.println(indent2+"rel is ["+rel+"]");
        }
    }
    public boolean isValidCluster() {
      return (id!=null);
    }

    void printAttributes(Node node) {
      if (node.hasAttributes()) {
        System.out.println("I have attributes");
      }
    }
      /*
    int indentlevel=0;
    String getValues(Node cel, String tag) {
      String retstr=null;
      NodeList children;
      Node child;
      try {
      indentlevel++;

      System.out.println("into getValues with indentlevel: "+indentlevel);
      printAttributes(cel);

      if (cel.hasChildNodes()) {
        children = cel.getChildNodes();
        if ( children != null ) {
          int len = children.getLength();
          System.out.println("children.length: "+len);
          for ( int idx = 0; idx < len; idx++ ) {
            child=children.item(idx);
            if (child.getNodeType()==Node.ELEMENT_NODE
              && ((Element)child).getTagName().equals(tag)
            ){
              System.out.println("TAG-MATCH");
            }
            child.normalize();
            String nodeval=child.getNodeValue();
            System.out.println("child: nodeType="+child.getNodeType()
              +" nodeval=["+nodeval+"]");
            if (nodeval!=null&&nodeval.equals(tag)) {
              System.out.println("TAG-MATCH");
            }
            getValues(child, tag);
            //retstr=child.getNodeValue();
            //break;
          //}
        }
        }
      }



      } finally {
            System.out.println("out of getValues with indentlevel: "+indentlevel);
            indentlevel--;

      }
               return"";
  }
  String getTextNodeValue(Element cel) {
    String retstr=null;
      cel.normalize();
      NodeList children = cel.getChildNodes();
      Node child;
      int len=children.getLength();
      if (debug) {
        System.out.println("tnv:"+len);
      }

      if ( children != null ) {
        for ( int idx = 0; idx < len; idx++ ) {
          child=children.item(idx);
          if (child.getNodeType()==Node.TEXT_NODE){
              retstr=child.getNodeValue().trim();
              break;
          }
        }
      }

    return retstr;
  }

    String getSingleElementValue(Element cel, String tag) {
      String retstr=null;
      NodeList children = cel.getChildNodes();
      Node child;
      if (debug) {
       System.out.println(children.getLength());
      }

       int len=children.getLength();
     if ( children != null ) {
        for ( int idx = 0; idx < len; idx++ ) {
          child=children.item(idx);
          if (child.getNodeType()==Node.ELEMENT_NODE
              && ((Element)child).getTagName().equals(tag)
              ){
              retstr=getTextNodeValue((Element)child);
              if (debug) {
                System.out.println("child: nodeType="+child.getNodeType()
                  +" nodeval=["+child.getNodeValue()+"]");
                System.out.println("retstr="+retstr);
              }
            break;
          }
        }
      }
            /*
      NodeList nodes=cel.getElementsByTagName(tag);
      System.out.println(nodes.getLength());
      if (nodes.getLength()>0) {
        String retstr2=((Element)nodes.item(0)).getNodeValue();
        System.out.println("retstr2="+retstr2);
	}     *//*
      return retstr;
    }
		*/

  }

