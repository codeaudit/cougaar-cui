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

package org.cougaar.lib.uiframework.transducer.elements;

import java.util.*;

/**
 *  ListElement is a base type for Elements that are capable of managing a list
 *  of child Elements.  This is a concrete class, and instances of it form the
 *  backbone of Structures formed from Element instances.
 */
public class ListElement extends Element {
  /**
   *  The symbol for ListElements is "L"
   */
  public static final String SYMBOL = "L";

  /**
   *  A table of Attributes (q.v.), indexed by their names.
   */
  protected Hashtable attributes = new Hashtable();

  /**
   *  An ordered list of child Elements.
   */
  protected Vector children = new Vector();

  /**
   *  Construct a new ListElement.
   */
  public ListElement () {
  }

  /**
   *  Since this is a ListElement, return a reference as such.
   *  @return a reference to this ListElement Object
   */
  public ListElement getAsList () {
    return this;
  }

  /**
   *  Add an Attribute to this ListElement.  It will be added to a table where
   *  it can be found by its name.
   *  @param a the Attribute to be added
   */
  public void addAttribute (Attribute a) {
    if (attributes != null)
      attributes.put(a.getName(), a);
  }

  /**
   * Remove an Attribute from this ListElement.
   * @param name the name of the Attribute to remove.
   * @return the attribute that was removed.
   */
  public Attribute removeAttribute(String name) {
    return (Attribute) attributes.remove(name);
  }

  /**
   *  Get a list of all the Attributes currently associated with this
   *  ListElement.
   *  @return an Enumeration containing the Attributes
   */
  public Enumeration getAttributes () {
    return attributes.elements();
  }

  /**
   *  Look-up a name in the Attribute table and return the results, if any.
   *  If no Attribute is found, then return null.
   *  @param name the name of the Attribute sought
   *  @return the named Attribute, if found, or null
   */
  public Attribute getAttribute (String name) {
    return (Attribute) attributes.get(name);
  }

  /**
   *  Add a child Element at the end of the current list of children.
   *  @param l child Element to be inserted
   */
  public void addChild (Element l) {
    children.add(l);
  }

  /**
   *  Insert a child Element into the list of children at the specified index.
   *  The index is valid if it is between 0 and the current number of children
   *  (inclusive).
   *  @param index the position within the list where the new child should go
   *  @param elt the new child Element to be inserted
   */
  public void insertChild (int index, Element elt) {
    children.insertElementAt(elt, index);
  }

  /**
   *  Replace the child Element at the specified index with another Element
   *  provided by the caller.  The index is valid if it is at least 0 but less
   *  than the current number of children.
   *  @param index the position within the list where the new child should go
   *  @param elt the new child Element to be inserted
   */
  public void setChildAt (int index, Element elt) {
    if (children.size() <= index)
      children.setSize(index + 1);
    children.setElementAt(elt, index);
  }

  /**
   *  Get a list of all children currently found in this ListElement.
   *  @return an Enumeration of the child Elements
   */
  public Enumeration getChildren () {
    return children.elements();
  }

  /**
   *  Report the number of children currently held by this ListElement.
   *  @return the number of child Elements
   */
  public int getChildCount () {
    return children.size();
  }

  /**
   *  Tell whether or not this node has any children.
   *  @return true if and only if this ListElement has at least one child.
   */
  public boolean hasChildren () {
    return children.size() > 0;
  }

  /**
   *  Traverse the set of Attributes and child Elements associated with this
   *  ListElement and generate their XML representation.  Subclasses may use
   *  this method to perform this common operation.
   *  @param pp the PrettyPrinter (text formatter) to which output is directed
   */
  protected void generateSubTags (PrettyPrinter pp) {
    pp.indent();
    for (Enumeration e = attributes.elements(); e.hasMoreElements(); )
      ((Attribute) e.nextElement()).generateXml(pp);
    for (Enumeration e = children.elements(); e.hasMoreElements(); )
      ((Element) e.nextElement()).generateXml(pp);
    pp.exdent();
  }

  /**
   *  Generate the XML representation of this ListElement
   *  @param pp the PrettyPrinter (text formatter) to which output is directed
   */
  public void generateXml (PrettyPrinter pp) {
    pp.print("<list>");
    generateSubTags(pp);
    pp.print("</list>");
  }

  /**
   *  Report the "symbol" associated with the ListElement type.  For proper
   *  instances of this class, the symbol is "L".
   *  @return the ListElement symbol, namely "L"
   */
  public String getSymbol () {
    return SYMBOL;
  }
}
