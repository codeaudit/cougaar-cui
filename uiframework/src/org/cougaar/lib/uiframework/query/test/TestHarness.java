/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * </copyright>
 */

package org.cougaar.lib.uiframework.query.test;

import org.cougaar.lib.uiframework.transducer.elements.*;
import org.cougaar.lib.uiframework.transducer.*;
import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.query.*;

import java.util.*;
import java.io.*;

public class TestHarness {
  public static void main (String[] argv) {
    try {
      String queryFile = "query.xml";
      if (argv.length > 0)
        queryFile = argv[0];

      scenario_1(queryFile);
    }
    catch (Exception oh_no) {
      oh_no.printStackTrace();
    }
  }

  private static void scenario_1 (String file) throws Exception {
    GenericInterpreter gi = new GenericInterpreter();
    gi.addDimension(configureDimension());
    gi.addDimension(configureIntDimension());
    gi.addDimension(configureListDimension());
    gi.addAttribute(new PigLatin());
    gi.addAttribute(new HundredMinusSum());
    gi.addAttribute(new VectorSquare());

    Structure query = readQuery(file);

    Structure result = gi.query(query);
    PrettyPrinter pp = new PrettyPrinter(System.out);
    result.generateXml(pp);
    pp.flush();
  }

  private static Structure readQuery (String file) throws Exception {
    XmlInterpreter xint = new XmlInterpreter();
    Structure s = xint.readXml(new FileInputStream(file));
    return s;
  }

  private static QueryDimension configureDimension () throws Exception {
    XmlInterpreter xint = new XmlInterpreter();
    Structure s = xint.readXml(new FileInputStream("dimen.xml"));
    TreeDimension dim = new TreeDimension();
    dim.setName("Items");
    dim.configure(s);
    return dim;
  }

  private static QueryDimension configureIntDimension () throws Exception {
    IntegerDimension dim = new IntegerDimension();
    dim.setName("Days");
    dim.setRoot(new IntDimNode(-10, 11));
    return dim;
  }

  private static QueryDimension configureListDimension () throws Exception {
    OrderlessDimension dim = new OrderlessDimension();
    dim.setName("Dudes");
    ListDimNode node = new ListDimNode("All Dudes");
    node.addMembers(new String[] {"Fred", "Sam", "Tony", "Jeff"});
    dim.setRoot(node);
    return dim;
  }
}