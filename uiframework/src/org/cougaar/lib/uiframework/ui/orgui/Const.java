
package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;

import org.cougaar.util.UnaryPredicate;

public abstract class Const {
  // standard XML header line
  public static String XML_HEADER =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  // constant strings used in the XML documents
  public static String ORG_RELS = "orgrels";
  public static String CLUSTER = "Cluster";
  public static String ID_ATTRIBUTE = "ID";
  public static String RELATIVE = "other";
  public static String REL_TYPE = "relationship";
  public static String START = "starttime";
  public static String END = "endtime";

  // name of the relationships table shared by OrgSubPlugIn and PSP_OrgSub
  public static String TABLE_NAME = "OrgRelTable";

  // An inane predicate class for filtering schedules.  This is necessary
  // because some moronic twits won't allow me to get an Enumeration
  private static class True implements UnaryPredicate {
    public boolean execute (Object o) {
      return true;
    }
  }
  public static UnaryPredicate TRUE = new True();

// - - - - - - - Some utilities for creating XML output - - - - - - - - - - - -

  /**
   *  Include a complete XML element with text content.
   *  @param buf the StringBuffer in which XML is being formed
   *  @param name the name of the XML element
   *  @param val the text content of the XML element
   */
  public static void addTag (PrintStream ps, String name, String val) {
    addOpenTag(ps, name);
    ps.print(val);
    addCloseTag(ps, name);
  }

  /**
   *  Include the opening tag of an XML element.
   *  @param buf the StringBuffer in which XML is being formed
   *  @param name the name of the XML element
   */
  public static void addOpenTag (PrintStream ps, String name) {
    addOpenTag(ps, name, null, null);
  }

  /**
   *  Include the opening tag of an XML element with one defined attribute.
   *  @param buf the StringBuffer in which XML is being formed
   *  @param name the name of the XML element
   *  @param attrib the name of the defined attribute
   *  @param val the value of the defined attribute
   */
  public static void addOpenTag (
      PrintStream ps, String name, String attrib, String val)
  {
    ps.print("<");
    ps.print(name);
    if (attrib != null && val != null) {
      ps.print(" ");
      ps.print(attrib);
      ps.print("=\"");
      ps.print(val);
      ps.print("\"");
    }
    ps.print(">");
  }

  /**
   *  Include the closing tag of an XML element.
   *  @param buf the StringBuffer in which XML is being formed
   *  @param name the name of the XML element
   */
  public static void addCloseTag (PrintStream ps, String name) {
    ps.print("</");
    ps.print(name);
    ps.print(">");
  }
}
