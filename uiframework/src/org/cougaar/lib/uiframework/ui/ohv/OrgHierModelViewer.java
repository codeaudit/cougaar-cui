/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv;

import java.util.TreeSet;

/**
 *  Interface used by viewers of an Organization Hierarchy.
 */
public interface OrgHierModelViewer {
  public void show ();
  public void showRelationshipsAtTime (long time);
  public TreeSet getTransitionTimes ();
  public long getStartTime ();
  public long getEndTime ();
}
