package org.cougaar.lib.uiframework.ui.ohv;

import java.util.TreeSet;

/**
  Interface used by viewers of an Organization Hierarchy.
**/
  public interface OrgHierModelViewer {

    public void show() ;
    public void showRelationshipsAtTime(long time) ;
    public TreeSet getTransitionTimes();

    public long getStartTime();
    public long getEndTime() ;
    // OrgHierModel getModel();


  }    // end interface OrgHierModelViewer
