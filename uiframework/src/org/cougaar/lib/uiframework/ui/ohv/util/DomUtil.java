/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
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

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

/**
  Provides DOM utility methods.
**/
public class DomUtil {

   static boolean debug=
     //true;
      false;


    public static Document getDocument(String urlName ){
        try{
                    InputStream fr = (new URL(urlName)).openStream();
                    InputSource is = new InputSource(fr);

                    DOMParser domp = new DOMParser();
                    domp.setErrorHandler(new ErrorHandler(){
                          public void error(SAXParseException exception) {
                             System.err.println("[ErrorHandler.error]: " + exception);
                           }
                           public void fatalError(SAXParseException exception) {
                                 System.err.println("[ErrorHandler.fatalError]: " + exception);
                           }
                           public void warning(SAXParseException exception) {
                                 System.err.println("[ErrorHandler.warning]: " + exception);
                            }
                        }
                    );

                    domp.parse(is);
                    Document doc = domp.getDocument();
                   return doc;
        } catch (Exception ex ) {
           ex.printStackTrace();
        }
        return null;
  }

    int indentlevel=0;
    public static String printValues(Node cel, String tag) {
      return printValues(cel, tag, 0);
    }

    // added to make this work with the older xerces.jar file
    public static void normalize(Node node) {

    	Node kid, next;
    	for (kid = node.getFirstChild(); kid != null; kid = next) {
    		next = kid.getNextSibling();

    		// If kid and next are both Text nodes (but _not_ CDATASection,
    		// which is a subclass of Text), they can be merged.
    		if (next != null
			 && kid.getNodeType() == Node.TEXT_NODE
			 && next.getNodeType() == Node.TEXT_NODE)
    	    {
    			((Text)kid).appendData(next.getNodeValue());
    			node.removeChild(next);
    			next = kid; // Don't advance; there might be another.
    		}

    		// Otherwise it might be an Element, which is handled recursively
    		else if (kid.getNodeType() ==  Node.ELEMENT_NODE) {
		    normalize((Element)kid);
            }
        }

    	// changed() will have occurred when the removeChild() was done,
    	// so does not have to be reissued.

    } // normalize()

    public static String printValues(Node cel, String tag, int indentlevel) {
      String retstr=null;
      NodeList children;
      Node child;
      try {
      indentlevel++;

      System.out.println("into getValues with indentlevel: "+indentlevel);

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

	    // changed to work with the older xerces.jar file
	    normalize(child);
            String nodeval=child.getNodeValue();
            System.out.println("child: nodeType="+child.getNodeType()
              +" nodeval=["+nodeval+"]");
            if (nodeval!=null&&nodeval.equals(tag)) {
              System.out.println("TAG-MATCH");
            }
            printValues(child, tag, indentlevel);
        }
        }
      }



      } finally {
            System.out.println("out of getValues with indentlevel: "+indentlevel);
            indentlevel--;

      }
               return"";
  }
  public static String getTextNodeValue(Element cel) {
    String retstr=null;

    // changed to make this work with the older xerces.jar file
    normalize(cel);
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

  public static String getSingleElementValue(Element cel, String tag) {
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
      return retstr;
    }
}

