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

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.lib.planserver.*;
import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.query.generic.*;
import java.io.*;
import org.cougaar.lib.uiframework.query.*;

public class ItemPSP extends PSP_QueryBase {
  private GenericInterpreter responder = null;

  /**
   *  Provide a reference to the QueryInterpreter to be used by this PSP.  The
   *  abstract algorithm calls upon this method to supply the interpreter when
   *  it is processing a query.
   *  @return the QueryInterpreter implementation
   */
  protected QueryInterpreter getQueryInterpreter () {
    return responder;
  }

  /**
   *  Override this method with code to initialize the QueryInterpreter
   *  instance used by this PSP for answering requests.  In some cases, all of
   *  the initialization can be performed in the implementation class's
   *  constructor.  This method, however, is called after the PluginDelegate
   */
  protected void initQueryInterpreter (PlanServiceContext psc) {
    responder = new GenericInterpreter();
    responder.addAttribute(new ItemGrabber(this));
    responder.addDimension(createItemDimension());
  }

  private QueryDimension createItemDimension () {
    try {
      XmlInterpreter xint = new XmlInterpreter();
      Structure s = xint.readXml(
        new BufferedReader(new StringReader(itemsDimension)));
      TreeDimension dim = new TreeDimension();
      dim.setName("Items");
      dim.configure(s);
      return dim;
    }
    catch (Exception e) {
      System.out.println("Unable to create dimension \"Items\"--" + e);
      e.printStackTrace();
    }
    return null;
  }

  private static final String itemsDimension =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    "<structure>" +
      "<list><a name=\"name\"><val>All Items</val></a>" +
        "<list><a name=\"name\"><val>Medical</val></a>" +
          "<list><a name=\"name\"><val>Syringe</val></a></list>" +
          "<list><a name=\"name\"><val>Bandage</val></a></list>" +
        "</list>" +
        "<list><a name=\"name\"><val>Subsistence</val></a>" +
          "<list><a name=\"name\"><val>Bread</val></a></list>" +
          "<list><a name=\"name\"><val>Egg</val></a></list>" +
        "</list>" +
        "<list><a name=\"name\"><val>Spare Parts</val></a>" +
          "<list><a name=\"name\"><val>Automotive</val></a>" +
            "<list><a name=\"name\"><val>Tire</val></a></list>" +
            "<list><a name=\"name\"><val>Spark Plug</val></a></list>" +
          "</list>" +
          "<list><a name=\"name\"><val>Tools</val></a>" +
            "<list><a name=\"name\"><val>Hammer</val></a></list>" +
            "<list><a name=\"name\"><val>Nails</val></a></list>" +
          "</list>" +
        "</list>" +
        "<list><a name=\"name\"><val>Fuel</val></a>" +
          "<list><a name=\"name\"><val>Gasoline</val></a></list>" +
          "<list><a name=\"name\"><val>Jet Fuel</val></a></list>" +
        "</list>" +
      "</list>" +
    "</structure>";
}