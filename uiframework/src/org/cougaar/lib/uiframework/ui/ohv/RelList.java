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
