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
   *  @param m the OrgHierModel which is being shown
   *  @param g the Graph indicating the visible section of the hierarchy
   *  @param l a receiver for selection events on this Component
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
