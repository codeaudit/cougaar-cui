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
 *  The root Element in a conglomerate of Elements should be a Structure.  Like
 *  a ListElement, it is capable of holding named Attributes, but should never
 *  have more than one child Element.
 */
public class Structure extends ListElement {
  /**
   *  The symbol associated with the Structure type, which happens to be "S"
   */
  public static final String SYMBOL = "S";

  /**
   *  An index useful for some database formats
   */
  protected long databaseId = -1;

  /**
   *  Specify the database ID for this Structure
   *  @param id the new database ID
   */
  public void setDatabaseId (long id) {
    databaseId = id;
  }

  /**
   *  Report the database ID currently being used by this Structure
   *  @return the current database ID
   */
  public long getDatabaseId () {
    return databaseId;
  }

  /**
   *  Since this Element is an instance of Structure, provide a reference to
   *  it as the more specific type.
   *  @return a reference to this as a Structure
   */
  public Structure getAsStructure () {
    return this;
  }

  /**
   *  Give a reference to the single child element of this Structure, if it is
   *  a ListElement.  If the child does not exist or is not a ListElement, then
   *  return null.  If there are multiple children, then an exception is thrown.
   *  @return the child ListElement, if any.
   */
  public ListElement getContentList () {
    if (children.size() == 1)
      return  ((Element) children.elementAt(0)).getAsList();
    else if (children.size() > 1)
      throw new RuntimeException("Structure with too many elements");
    else
      return null;
  }

  /**
   *  Generate the XML representation of this Structure
   *  @param pp the PrettyPrinter (text formatter) to which output is directed
   */
  public void generateXml (PrettyPrinter pp) {
    pp.print("<structure>");
    generateSubTags(pp);
    pp.print("</structure>");
  }

  /**
   *  Return the symbol associated with the Structure type.  The symbol is "S".
   *  @return the symbol of a Structure, "S"
   */
  public String getSymbol () {
    return SYMBOL;
  }
}