package org.cougaar.lib.uiframework.ui.util;

import java.util.Hashtable;

/**
 * A hashtable whose toString method returns the value of a specified property.
 * Used in JTreeInterpreter for user data object in generated
 * DefaultMutableTreeNodes.
 */
public class SelectableHashtable extends Hashtable
{
    private String selectedProperty = null;

    /**
     * Create a new selectable hashtable whose toString method will return
     * the value of a specified property.
     *
     * @param selectedProperty property to use for toString
     */
    public SelectableHashtable(String selectedProperty)
    {
        this.selectedProperty = selectedProperty;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    public String toString()
    {
        return get(selectedProperty).toString();
    }
}