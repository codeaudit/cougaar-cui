
package org.cougaar.lib.uiframework.query;

import org.cougaar.lib.uiframework.transducer.XmlInterpreter;
import org.cougaar.lib.uiframework.transducer.elements.Structure;
import org.cougaar.lib.uiframework.transducer.elements.PrettyPrinter;

import org.cougaar.core.plugin.PlugInDelegate;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.lib.planserver.*;

import java.util.*;
import java.io.*;

/**
 *  The PSP_QueryBase is the base class for PSPs that respond to HTTP POST
 *  requests by using a QueryInterpreter implementation.  The generic algorithm
 *  handles most of work involved in getting the query and parsing the XML and
 *  so forth.  Subclasses are responsible for creating and configuring the
 *  QueryInterpreter.
 */
public abstract class PSP_QueryBase
    extends PSP_BaseAdapter implements PlanServiceProvider
{
  // the header that prefaces an XML document
  private static final String XML_HEADER =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  // standard error response in case the request method is not POST
  private static final String NON_POST_REQUEST = "<structure>" +
    "<a name=\"error\"/><val>Request was not a POST</val></structure>";

  // standard error response in case the request is not a well-formed Structure
  private static final String BAD_STRUCTURE = "<structure><a name=\"error\">" +
    "<val>Invalid request:  Structure is malformed</val></a></structure>";

  // Keep track of the first call.  Some operations need only be performed once.
  private boolean firstCall = true;

  // Use an XmlInterpreter instance to translate incoming XML to Structures
  private XmlInterpreter xint = new XmlInterpreter();

  /**
   *  If this flag is true, then this PSP_QueryBase will echo to System.out an
   *  XML copy of every query it receives.  By default, it is false, but a
   *  subclass instance can set it to true to aid in debugging.
   */
  protected boolean echo_queries = false;

  /**
   *  If this flag is true, then this PSP_QueryBase will echo to System.out an
   *  XML copy of every result set it produces.  By default, it is false, but a
   *  subclass instance can set it to true to aid in debugging.
   */
  protected boolean echo_results = false;

  /**
   *  Cache a reference to a PlugInDelegate, as obtained through the
   *  PlanServiceContext supplied through the first call to execute.  This
   *  reference is used by the local QueryInterpreter for all of its access to
   *  the logplan.
   */
  protected PlugInDelegate plugin = null;

  /**
   *  <p>
   *  Field a request from an HTTP client.  The request should be posed as an
   *  HTTP POST, and its body should contain the XML for a Structure.  The
   *  first time this method is called, a reference to a PlugInDelegate is
   *  obtained and cached locally for future use.  Additionally, some
   *  implementation-specific initialization code is executed.
   *  </p><p>
   *  The typical execute call reads the body of the POST, parses it as an XML
   *  document and passes it to the QueryInterpreter.  A result set or error
   *  message is returned to the client, in either case conforming to the DTD,
   *  "structure.dtd".
   *  </p>
   */
  public void execute (PrintStream out, HttpInput in, PlanServiceContext psc,
      PlanServiceUtilities psu)
      throws Exception
  {
    if (firstCall) {
      plugin = psc.getServerPlugInSupport().getDirectDelegate();
      initQueryInterpreter();
      firstCall = false;
    }

    if (!in.isPostRequest()) {
      out.println(XML_HEADER);
      out.println(NON_POST_REQUEST);
      out.flush();
      return;
    }

    Structure query = null;
    try {
      query = xint.readXml(new ByteArrayInputStream(in.getBody()));
    }
    catch (Exception badness) {
      out.println(XML_HEADER);
      out.println(BAD_STRUCTURE);
      out.flush();
      return;
    }

    // maybe echo the received query for debugging purposes
    if (echo_queries) {
      System.out.println();
      System.out.println("PSP_QueryBase::execute:  received query:");
      PrettyPrinter pp = new PrettyPrinter(System.out);
      query.generateXml(pp);
      pp.flush();
    }

    Structure result = null;
    try {
      result = getQueryInterpreter().query(query);
    }
    catch (QueryException qe) {
      out.println(XML_HEADER);
      out.println(
        "<structure><a name=\"error\"/><val>Can't process query</val><val>");
      out.print(qe);
      out.println("</val></structure>");
      return;
    }

    // maybe echo the generated response for debugging purposes
    if (echo_results) {
      System.out.println();
      System.out.println("PSP_QueryBase::execute:  returning response:");
      PrettyPrinter pp = new PrettyPrinter(System.out);
      result.generateXml(pp);
      pp.flush();
    }

    PrettyPrinter pp = new PrettyPrinter(out);
    result.generateXml(pp);
    pp.flush();
  }

  /**
   *  Override this method with code to initialize the QueryInterpreter
   *  instance used by this PSP for answering requests.  In some cases, all of
   *  the initialization can be performed in the implementation class's
   *  constructor.  This method, however, is called after the PlugInDelegate
   *  reference is available, which may be required for some initializations.
   *  The default implementation does nothing.
   */
  protected void initQueryInterpreter () {
  }

  /**
   *  Provide a reference to the QueryInterpreter to be used by this PSP.  The
   *  abstract algorithm calls upon this method to supply the interpreter when
   *  it is processing a query.
   *  @return the QueryInterpreter implementation
   */
  protected abstract QueryInterpreter getQueryInterpreter ();

  /**
   *  Use a reference to the local PlugIn as a means of accessing the Cluster's
   *  logplan.  This method is exposed to allow the QueryInterpreter, which may
   *  have access to this PSP_queryAdapter, to generate responses based Objects
   *  found on the logplan.
   */
  public Collection getFromLogplan (UnaryPredicate p) {
    return plugin.query(p);
  }

  /**
   *  This PSP does not profess to use a DTD, even though all of its output
   *  adheres to the "structure.dtd".  Consequently, this method returns null.
   *  @return always null
   */
  public String getDTD () {
    return null;
  }

  /**
   *  This PSP does not return HTML documents.
   *  @return always false
   */
  public boolean returnsHTML () {
    return false;
  }

  /**
   *  Okay, so I lie.  It does really return XML, even if it professes not to.
   *  @return always false
   */
  public boolean returnsXML () {
    return false;
  }

  /**
   *  Whatever.
   *  @return always false.
   */
  public boolean test (HttpInput p0, PlanServiceContext p1) {
    return false;
  }
}