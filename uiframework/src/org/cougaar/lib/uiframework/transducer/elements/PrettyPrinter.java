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
import java.io.*;

/**
 *  A PrettyPrinter is a text formatter designed to keep track of indentations
 *  within a document.  Formatted text is sent to an underlying PrintWriter.
 */
public class PrettyPrinter {
  private int depth = 0;
  private String indentString = null;
  private PrintWriter out = null;

  /**
   *  Create a new PrettyPrinter instance with output directed to the provided
   *  PrintWriter.  The String used for indentations consists of two spaces.
   *  @param o the recipient of the formatted output
   */
  public PrettyPrinter (PrintWriter o) {
    out = o;
    indentString = "  ";
  }

  /**
   *  Create a new PrettyPrinter instance with output directed to the provided
   *  OutputStream.  The String used for indentations consists of two spaces.
   *  @param o the recipient of the formatted output
   */
  public PrettyPrinter (OutputStream o) {
    this(new PrintWriter(o));
  }

  /**
   *  Create a new PrettyPrinter instance with output directed to the provided
   *  PrintWriter.  Also provided by the caller is a String to use in the
   *  output's indentation (it should probably be spaces or tabs).
   *  @param o the recipient of the formatted output
   *  @param i the indentation String
   */
  public PrettyPrinter (PrintWriter o, String i) {
    out = o;
    indentString = i;
  }

  /**
   *  Create a new PrettyPrinter instance with output directed to the provided
   *  OutputStream.  Also provided by the caller is a String to use in the
   *  output's indentation (it should probably be spaces or tabs).
   *  @param o the recipient of the formatted output
   *  @param i the indentation String
   */
  public PrettyPrinter (OutputStream o, String i) {
    this(new PrintWriter(o), i);
  }

  /**
   *  Record an additional level of indentation
   */
  public void indent () {
    depth++;
  }

  /**
   *  Relinquish one level of indentation (unless already at zero)
   */
  public void exdent () {
    if (depth > 0)
      depth--;
  }

  /**
   *  Output a String with the appropriate indentation.  If the String contains
   *  line breaks, then each line is indented by same amount in the output.
   *  @param s the String to be written
   */
  public void print (String s) {
    StringTokenizer tok = new StringTokenizer(s, "\n", true);
    while (tok.hasMoreTokens()) {
      String line = tok.nextToken();
      if (line.equals("\n")) {
        while (tok.hasMoreTokens() && (line = tok.nextToken()).equals("\n"))
          out.println();
      }
      if (!line.equals("\n")) {
        if (line.length() > 0)
          addIndentation();
        out.println(line);
      }
    }
  }

  /**
   *  Insert a blank line into the document.  Levels of indentation are ignored.
   */
  public void println () {
    out.println();
  }

  private void addIndentation () {
    for (int i = 0; i < depth; i++)
      out.print(indentString);
  }

  /**
   *  Flush the buffer in the underlying PrintWriter.
   */
  public void flush () {
    out.flush();
  }

  /**
   *  Close the underlying PrintWriter
   */
  public void close () {
    out.close();
  }
}
