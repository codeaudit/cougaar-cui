/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.orgui;

import java.io.*;

import org.cougaar.lib.planserver.HttpInput;
import org.cougaar.lib.planserver.PSP_BaseAdapter;
import org.cougaar.lib.planserver.PlanServiceContext;
import org.cougaar.lib.planserver.PlanServiceProvider;
import org.cougaar.lib.planserver.PlanServiceUtilities;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;

/**
 *  This PSP is a surrogate for the Generic PSP, in that it acts as a container
 *  for the TestAdapter.  Since PSP_GenericReaderWriter only reports a response
 *  when there are objects on the logplan matching the subscription, the
 *  TestAdapter instance may never be stimulated.  PSP_TestAdapter neatly
 *  circumvents this problem.
 */
public class PSP_TestAdapter
    extends PSP_BaseAdapter implements PlanServiceProvider
{
  private TestAdapter adapter = new TestAdapter();

  /**
   *  Process a request from the client.  That is, call upon the contained
   *  TestAdapter to generate test data.
   *  @param out a PrintStream to which output is sent.
   *  @param in the HttpInput of the request
   *  @param psc a PlanServiceContext instance
   *  @param psu a PlanServiceUtilities instance
   */
  public void execute (PrintStream out, HttpInput in, PlanServiceContext psc,
      PlanServiceUtilities psu)
      throws Exception
  {
    synchronized (adapter) {
      adapter.execute(null, GenericLogic.collectionType_ADD);
      adapter.returnVal(out);
    }
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