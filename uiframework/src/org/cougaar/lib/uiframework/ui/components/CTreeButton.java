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
     * Iniitialize tree button
     */
    private void init(DefaultMutableTreeNode root,
                      DefaultMutableTreeNode selectedNode)
    {
        ns = new CNodeSelector(root);
        ns.setSelectedItem(selectedNode);
        setSelectorControl(ns);
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