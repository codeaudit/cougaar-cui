package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.*;

/**
 * The class can be used to create a control that is comprised of a set of
 * labeled radio buttons.<BR><BR>
 *
 * This bean has bounded property:  "selectedItem"
 */
public class CRadioButtonSelectionControl extends JPanel
{
    private ButtonGroup radioButtons = new ButtonGroup();
    private Object currentSelection = null;

    /**
     * Default constructor.  Create a radio button selection control with
     * default selections and horizontal orientation.
     */
    public CRadioButtonSelectionControl()
    {
        super(new FlowLayout());
        init(new String[]{"selection 1", "selection 2"} , BoxLayout.X_AXIS);
    }

    /**
     * Create a new radio button selection control
     *
     * @param selections  array of strings that will be used to label the radio
     *                    buttons.
     * @param orientation BoxLayout.X_AXIS or BoxLayout.Y_AXIS
     */
    public CRadioButtonSelectionControl(String[] selections, int orientation)
    {
        super(new FlowLayout());
        init(selections, orientation);
    }

    private void init(String[] selections, int orientation)
    {
        Box box = new Box(orientation);

        for (int i = 0; i < selections.length; i++)
        {
            JRadioButton radioButton = new JRadioButton(selections[i]);
            radioButton.setActionCommand(selections[i]);
            radioButtons.add(radioButton);
            box.add(radioButton);
        }

        add(box);

        currentSelection = getSelectedItem();
        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    Object oldSelection = currentSelection;
                    currentSelection = getSelectedItem();
                    firePropertyChange("selectedItem", oldSelection,
                                       currentSelection);
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

    /**
     * Set the selected radio button
     *
     * @param the radio button string to select
     */
    public void setSelectedItem(Object selectedItem)
    {
        Enumeration buttons = radioButtons.getElements();

        while (buttons.hasMoreElements())
        {
            JRadioButton radioButton = (JRadioButton)buttons.nextElement();
            if (radioButton.getText().equals(selectedItem))
            {
                Object oldSelection = currentSelection;
                radioButton.setSelected(true);
                currentSelection = getSelectedItem();
                firePropertyChange(
                    "selectedItem", oldSelection, currentSelection);
            }
        }
    }

    /**
     * Get the selected radio button
     *
     * @param a string representing the selected radio button
     */
    public Object getSelectedItem()
    {
        Enumeration buttons = radioButtons.getElements();

        while (buttons.hasMoreElements())
        {
            JRadioButton radioButton = (JRadioButton)buttons.nextElement();
            if (radioButton.isSelected())
            {
                return radioButton.getText();
            }
        }

        return null;
    }

    /**
     * Added an action listener that will fire when a radio button is selected
     *
     * @param al the action listener to add
     */
    public void addActionListener(ActionListener al)
    {
        Enumeration buttons = radioButtons.getElements();

        while (buttons.hasMoreElements())
        {
            JRadioButton radioButton = (JRadioButton)buttons.nextElement();
            radioButton.addActionListener(al);
        }
    }

    /**
     * Remove an action listener
     *
     * @param al the action listener to remove
     */
    public void removeActionListener(ActionListener al)
    {
        Enumeration buttons = radioButtons.getElements();

        while (buttons.hasMoreElements())
        {
            JRadioButton radioButton = (JRadioButton)buttons.nextElement();
            radioButton.removeActionListener(al);
        }
    }
}