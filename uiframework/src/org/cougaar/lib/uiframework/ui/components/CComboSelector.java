package org.cougaar.lib.uiframework.ui.components;

import java.awt.event.*;
import javax.swing.JComboBox;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * This class is a JComboBox that implements the Selector
 * interface.  <BR><BR>
 *
 * This bean has bound property:  "selectedItem"
 */
public class CComboSelector extends JComboBox implements Selector
{
    private Object selectedItem;

    /**
     * Default constructor.  Create a new, empty combo box.
     */
    public CComboSelector()
    {
        super();
        init();
    }

    /**
     * Create a new combo selector filled with the given selection options.
     *
     * @param items array of objects to use as selections.
     */
    public CComboSelector(Object[] items)
    {
        super(items);
        init();
    }

    private void init()
    {
        selectedItem = getSelectedItem();
        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    Object oldSelectedItem = selectedItem;
                    selectedItem = getSelectedItem();
                    firePropertyChange("selectedItem", oldSelectedItem,
                                       selectedItem);
                }
            });
    }

    /**
     * Not needed when compiling/running under jdk1.3
     */
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue)
    {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}