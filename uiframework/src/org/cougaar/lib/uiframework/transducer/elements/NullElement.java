
package org.cougaar.lib.uiframework.transducer.elements;

/**
 *  A NullElement is an Element used as a placeholder for content that isn't
 *  there.
 */
public class NullElement extends Element {
  /**
   *  The symbol associated with the NullElement type.  That is, "N".
   */
  public static final String SYMBOL = "N";

  /**
   *  Since this Element is a NullElement instance, supply a reference to it
   *  as the more specific type.
   */
  public NullElement getAsNull () {
    return this;
  }

  /**
   *  Generate the XML representation of this Element.
   *  @param pp the PrettyPrinter (text formatter) to which output is directed
   */
  public void generateXml (PrettyPrinter pp) {
    pp.print("<null/>");
  }

  /**
   *  Report the symbol of this type of Element, which happens to be "N".
   *  @return the symbol of a NullElement, "N"
   */
  public String getSymbol () {
    return SYMBOL;
  }
}