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

package org.cougaar.lib.uiframework.transducer.elements;

/**
 *  Element is the abstract base class of all Java classes which take part in
 *  the formation of such Structures as may be manipulated by the classes in
 *  the transducer package.
 */
public abstract class Element {
  /**
   *  The list of characters which should automatically be encoded when being
   *  written as part of an XML file.  The encoding ensures that an Object
   *  restored from the file has the same contents as the one that was written
   *  into it.
   */
  public static final String escapedChars = "<>&\r\n\t";

  /**
   *  Generate an XML representation of this Element and any subordinates it
   *  might have.
   *  @param pp a PrettyPrinter (text formatter) to receive the output.
   */
  public abstract void generateXml (PrettyPrinter pp);

  /**
   *  Report the "symbol" associated with the type of this Element.  The value
   *  is used in support of some database formats.  At present, the return is
   *  always a String consisting of a single capital letter.
   *  @return the subclass type's "symbol"
   */
  public abstract String getSymbol ();

  /**
   *  If this Element is a NullElement instance, provide a reference of the
   *  more specific type.  If not, then indicate as much by giving no reference.
   *  @return this Element as a NullElement, if appropriate.
   */
  public NullElement getAsNull () {
    return null;
  }
     
  /**
   *  If this Element is a ValElement instance, provide a reference of the
   *  more specific type.  If not, then indicate as much by giving no reference.
   *  @return this Element as a ValElement, if appropriate.
   */
  public ValElement getAsValue () {
    return null;
  }

  /**
   *  If this Element is a ListElement instance, provide a reference of the
   *  more specific type.  If not, then indicate as much by giving no reference.
   *  @return this Element as a ListElement, if appropriate.
   */
  public ListElement getAsList () {
    return null;
  }

  /**
   *  If this Element is an Attribute instance, provide a reference of the more
   *  specific type.  If not, then indicate as much by giving no reference.
   *  @return this Element as a Attribute, if appropriate.
   */
  public Attribute getAsAttribute () {
    return null;
  }

  /**
   *  If this Element is a Structure instance, provide a reference of the more
   *  specific type.  If not, then indicate as much by giving no reference.
   *  @return this Element as a Structure, if appropriate.
   */
  public Structure getAsStructure () {
    return null;
  }

  /**
   *  Convert a String to a form more suitable for writing to an XML file by
   *  encoding the characters that would conflict with markup syntax of XML.
   *  @param s the String to be encoded
   *  @return the properly coded String
   */
  public static String xmlize (String s) {
    if (s == null)
      return "";

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (escapedChars.indexOf(c) != -1) {
        buf.append("&#");
        buf.append((int) c);
        buf.append(';');
      }
      else {
        buf.append(c);
      }
    }

    return buf.toString();
  }
}