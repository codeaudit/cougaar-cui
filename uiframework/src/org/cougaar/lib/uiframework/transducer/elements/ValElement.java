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
 *  A ValElement is an Element whose purpose is to encapsulate raw data values
 *  (i.e., Strings).
 */
public class ValElement extends Element {
  /**
   *  The symbol associated with the ValElement type, namely "V".
   */
  public static final String SYMBOL = "V";

  /**
   *  The value encapsulated by this ValElement
   */
  protected String value = null;

  /**
   *  Construct a new ValElement with no initial contents.
   */
  public ValElement () {
  }

  /**
   *  Construct a new ValElement with the provided value as its contents.
   *  @param v the data to be encapsulated
   */
  public ValElement (String v) {
    value = v;
  }

  /**
   *  Since this Element is a ValElement instance, return a reference to it as
   *  the more specific type.
   *  @return a reference to this as a ValElement
   */
  public ValElement getAsValue () {
    return this;
  }

  /**
   *  Install a new String value as the contents of this ValElement.
   *  @param v the new content String
   */
  public void setValue (String v) {
    value = v;
  }

  /**
   *  Retrieve the value contained within this ValElement
   *  @return the encapsulated value.
   */
  public String getValue () {
    return value;
  }

  /**
   *  Generate an XML representation of this ValElement.
   *  @param pp the PrettyPrinter (text formatter) to which output is directed
   */
  public void generateXml (PrettyPrinter pp) {
    pp.print("<val>" + xmlize(value) + "</val>");
  }

  /**
   *  Report the symbol associated with the ValElement type, namely "V".
   *  @return the symbol of a ValElement, "V"
   */
  public String getSymbol () {
    return SYMBOL;
  }
}