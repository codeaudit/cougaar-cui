package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Someday this will be a useful wrapper for a JTree.  Currently, it doesn't
 * really add much.  It is a JTree with single selection mode preset.
 */
public class CNodeSelectionControl extends JTree
{
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