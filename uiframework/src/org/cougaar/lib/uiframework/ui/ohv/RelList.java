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

import java.util.*;
import java.awt.event.ItemListener;
import java.awt.Choice;

import org.cougaar.lib.uiframework.ui.ohv.VGJ.graph.*;

/**
 *  List of Relationship types in the model.
 */
public class RelList extends Choice {
  /**
   *  Create a selection widget for a user to select the type of relationship
   *  to be shown in the GUI.  This constructor automatically configures the
   *  selections to be those available in the supplied OrgHierModel.
   *  @param model the OrgHierModel which is being shown
   *  @param itemListener a receiver for selection events on this Component
   */
  public RelList (OrgHierModel model, ItemListener itemListener) {
    for (Iterator i = model.getRelationshipTypes().iterator(); i.hasNext(); )
      add((String) i.next());

    addItemListener(itemListener);
  }

  /**
   *  Create a selection widget for a user to select the type of relationship
   *  to be shown in the GUI.  This constructor automatically configures the
   *  selections to be those available in the supplied OrgHierModel and visible
   *  in this portion of the hierarchy.
   *  @param model the OrgHierModel which is being shown
   *  @param g the Graph indicating the visible section of the hierarchy
   *  @param itemListener a receiver for selection events on this Component
   */
  public RelList (OrgHierModel m, Graph g, ItemListener l) {
    TreeSet names = new TreeSet();
    for (Node n = g.firstNode(); n != null; n = g.nextNode(n))
      names.add(n.getLabel());

    for (Iterator i = m.getRelationshipTypes(names).iterator(); i.hasNext(); )
      add((String) i.next());

    addItemListener(l);
  }
}
