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
package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * A UI control bean that is a button that is labeled with the control's
 * current value.  Upon pressing the button the user is presented with a
 * dialog that contains a JTree from which the user can select a node.
 *
 * This bean has bounded property:  "selectedItem"
 */
public class CTreeButton extends CPullrightButton implements Selector
{
    private CNodeSelector ns = null;

    /**
     * Default constructor.  Create a new tree button with a simple default
     * tree.
     */
    public CTreeButton()
    {
        super();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("parent");
        root.add(new DefaultMutableTreeNode("child1"));
        root.add(new DefaultMutableTreeNode("child2"));

        init(root, root);
    }

    /**
     * Create a new tree button based on the given parameters
     *
     * @param root         the root of the tree to select from
     * @param selectedNode the node to have selected
     */
    public CTreeButton(DefaultMutableTreeNode root,
                       DefaultMutableTreeNode selectedNode)
    {
        super();

        init(root, selectedNode);
    }

    /**
     * Initialize tree button
     */
    private void init(DefaultMutableTreeNode root,
                      DefaultMutableTreeNode selectedNode)
    {
        ns = new CNodeSelector(root);
        ns.setSelectedItem(selectedNode);
        setSelectorControl(ns);
    }

    /**
     * Add a control to be contained in (but not managed by) this control
     *
     * @param control externally managed control
     */
    public void addIncludedControl(Component control)
    {
        ns.addIncludedControl(control);
    }

    /**
     * Set a new root for this tree button.  Sets equivilant selection
     * in new tree if possible.
     *
     * @param root new root for this tree button
     */
    public void setRoot(DefaultMutableTreeNode root)
    {
        ns.setRoot(root);
    }

    /**
     * Set whether the root node of JTree should be shown.
     *
     * @param visible true if root node should be shown.
     */
    public void setRootVisible(boolean visible)
    {
        ns.setRootVisible(visible);
    }

    /**
     * Expand the first level of the JTree
     */
    public void expandFirstLevel()
    {
        ns.expandFirstLevel();
    }

}