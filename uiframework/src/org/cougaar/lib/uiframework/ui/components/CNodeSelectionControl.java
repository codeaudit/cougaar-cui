/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Someday this will be a useful wrapper for a JTree.  Currently, it doesn't
 * really add much.
 */
public class CNodeSelectionControl extends JTree
{
    /**
     * Default constructor.  Creates a new CNodeSelectionControl with default
     * contents.
     */
    public CNodeSelectionControl()
    {
        super();

        getSelectionModel().
            setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Create a new JTree using the given tree model for data.
     *
     * @param top root node of tree model
     */
    public CNodeSelectionControl(DefaultMutableTreeNode top)
    {
        super(top);

        getSelectionModel().
            setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * In jdk1.3 this will override a method in JTree and make it so the
     * selection will not change when the selected node's branch is collapsed.
     * (fix for bug #380) This will have no effect under jdk1.2.
     */
    protected boolean removeDescendantSelectedPaths(TreePath path,
                                                    boolean includePath)
    {
        return false;
    }

    /**
     * Someday this will convert from one tree structure to another
     *
     * @param t root node of tree model to be converted.
     * @return root node of converted tree model
     */
    private static DefaultMutableTreeNode convertTree(DefaultMutableTreeNode t)
    {
        return t;
    }
}