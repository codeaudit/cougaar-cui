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

    /**
     * Set the name of the property to use for toString.
     *
     * @param selectedProperty new name of the property to use for toString.
     */
    public void setSelectedProperty(String selectedProperty)
    {
        this.selectedProperty = selectedProperty;
    }

    /**
     * Get the current name of the property being used for toString.
     *
     * @return the current name of the property being used for toString.
     */
    public String getSelectedProperty()
    {
        return selectedProperty;
    }
}