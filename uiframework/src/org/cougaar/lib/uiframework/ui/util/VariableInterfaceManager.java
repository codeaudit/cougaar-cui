package org.cougaar.lib.uiframework.ui.util;

import java.util.Enumeration;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.cougaar.lib.uiframework.ui.models.VariableModel;

/**
 * Used to manage a set of variable interfaces.  Variable interfaces are
 * either associated with the x or y axis of a graph or they are fixed.
 * Variable interface manager can be used to swap the roles and values of
 * variables either by way of user action or programmatically.  It can also be
 * used with a query generator to parameterize a query that populates a
 * database table model.
 *
 * @see org.cougaar.lib.uiframework.ui.models.DatabaseTableModel
 */
public class VariableInterfaceManager
{
    private VariableModel[] variableDescriptors;
    private Vector variableListeners = new Vector();

    /**
     * Create a new variable interface manager based on given parameters
     *
     * @param variables      variable descriptors for the variables that need
     *                       to be managed
     */
    public VariableInterfaceManager(VariableModel[] variables)
    {
        this.variableDescriptors = variables;

        // Variable comboBox selection logic
        ActionListener cbListener = new SelectionListener();

        // Create a control for each variable
        for (int i = 0; i < variableDescriptors.length; i++)
        {
            final VariableModel v = variableDescriptors[i];

            JPanel variableControl = new JPanel();
            if (v.isHorizontal())
            {
                variableControl.setLayout(
                    new BoxLayout(variableControl, BoxLayout.X_AXIS));
            }
            else
            {
                variableControl.setLayout(new GridLayout(2, 1, 0, 10));
            }

            JComponent variableLabel = null;
            if (v.isSwappable())
            {
                JComboBox selector = createCombo();
                selector.setSelectedItem(v);
                selector.putClientProperty("ID", selector.getSelectedItem());
                selector.addActionListener(cbListener);
                selector.setMinimumSize(new Dimension(0,0));
                JPanel selectorBox = new JPanel(new BorderLayout());
                if (v.isHorizontal())
                {
                    selectorBox.setLayout(
                        new BoxLayout(selectorBox, BoxLayout.X_AXIS));
                    selectorBox.add(selector);
                    selectorBox.add(new JLabel(": "));
                    selectorBox.add(Box.createGlue());
                }
                else
                {
                    selectorBox.add(selector, BorderLayout.CENTER);
                }
                variableControl.add(selectorBox);
                variableLabel = selectorBox;
            }
            else
            {
                variableLabel = new JLabel(v.getName() + ": ");
                variableControl.add(variableLabel);
            }
            if (v.getLabelWidth() != 0)
            {
                variableLabel.setPreferredSize(
                    new Dimension(v.getLabelWidth(),
                                  variableLabel.getPreferredSize().height));
                variableLabel.setMaximumSize(variableLabel.getPreferredSize());
            }

            variableControl.add(v.getSelectComponent());
            v.setControl(variableControl);

            if (v.isHorizontal())
            {
                variableControl.add(Box.createGlue());
            }

            v.getSelector().addPropertyChangeListener("selectedItem",
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e)
                    {
                        for (int x = 0; x < variableListeners.size(); x++)
                        {
                            VariableListener vl = (VariableListener)
                                variableListeners.elementAt(x);
                            vl.variableChanged(v);
                        }
                    }
                });
        }
    }

    /**
     * Set the x axis to a managed variable descriptor
     *
     * @param variableName name of variable to set as x axis variable
     */
    public void setXAxis(String variableName)
    {
        VariableModel xDescriptor = (VariableModel)
            getDescriptors(VariableModel.X_AXIS).nextElement();
        setLocation(xDescriptor, variableName);
    }

    /**
     * Set the y axis to a managed variable descriptor
     *
     * @param variableName name of variable to set as y axis variable
     */
    public void setYAxis(String variableName)
    {
        VariableModel yDescriptor = (VariableModel)
            getDescriptors(VariableModel.Y_AXIS).nextElement();
        setLocation(yDescriptor, variableName);
    }

    /**
     * Get descriptors of a given type (i.e. state)
     *
     * @param type type of descriptors to get (VariableModel.X_AXIS,
     *             VariableModel.Y_AXIS, VariableModel.FIXED)
     * @return list of variable descriptors that match given type
     */
    public Enumeration getDescriptors(int type)
    {
        Vector descriptors = new Vector();

        for (int i = 0; i < variableDescriptors.length; i++)
        {
            if (variableDescriptors[i].getState() == type)
                descriptors.add(variableDescriptors[i]);
        }

        return descriptors.elements();
    }

    /**
     * Get a variable descriptor by name
     *
     * @param name name of descriptor to get
     * @return descriptor that has given name (null if not found)
     */
    public VariableModel getDescriptor(String name)
    {
        for (int i = 0; i < variableDescriptors.length; i++)
        {
            if (name.equals(variableDescriptors[i].getName()))
                return variableDescriptors[i];
        }

        return null;
    }

    /**
     * Add a new variable listener to this manager.  Changes to managed
     * variable values and roles will trigger methods in these listeners
     *
     * @param vl variable listener to add
     */
    public void addVariableListener(VariableListener vl)
    {
        variableListeners.add(vl);
    }

    /**
     * Rmove an installed variable listener from this manager.
     *
     * @param vl variable listener to remove
     */
    public void removeVariableListener(VariableListener vl)
    {
        variableListeners.remove(vl);
    }

    /**
     * Changes to managed variable values and roles will trigger methods in
     * listeners that implement this interface and are added to manager.
     */
    public interface VariableListener
    {
        /**
         * Called when a managed variable value is changed.
         *
         * @param vm the variable that has new value
         */
        public void variableChanged(VariableModel vm);

        /**
         * Called when two managed varables swamp roles/positions via
         * variable management controls.
         *
         * @param vm1 one of the two variables that swapped role and position
         * @param vm2 one of the two variables that swapped role and position
         */
        public void variablesSwapped(VariableModel vm1, VariableModel vm2);
    }

    private JComboBox createCombo()
    {
        JComboBox combo = new JComboBox();
        for (int i = 0; i < variableDescriptors.length; i++)
        {
            if (variableDescriptors[i].isSwappable())
            {
                combo.addItem(variableDescriptors[i]);
            }
        }
        return combo;
    }

    private void setLocation(VariableModel vDescriptor,
                             String variableName)
    {
        if (!vDescriptor.getName().equals(variableName))
        {
            JPanel vControl = vDescriptor.getControl();
            JComboBox vCombo = getComboBox(vControl);
            vCombo.setSelectedItem(getDescriptor(variableName));
        }
    }

    private static JComboBox getComboBox(JPanel panel)
    {
        return (JComboBox)((Container)panel.getComponent(0)).getComponent(0);
    }

    private class SelectionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JComboBox modifiedCB = (JComboBox)e.getSource();
            Boolean react = (Boolean)modifiedCB.getClientProperty("REACT");
            if ((react != null) && !react.booleanValue())
            {
                modifiedCB.putClientProperty("REACT", new Boolean(true));
            }
            else
            {
                VariableModel oldValue =
                    (VariableModel)modifiedCB.getClientProperty("ID");
                VariableModel newValue =
                    (VariableModel)modifiedCB.getSelectedItem();
                modifiedCB.putClientProperty("ID", newValue);

                if (oldValue != newValue)
                {
                    JComboBox relatedCB = getComboBox(newValue.getControl());
                    relatedCB.putClientProperty("REACT", new Boolean(false));
                    relatedCB.setSelectedItem(oldValue);
                    relatedCB.putClientProperty("ID", oldValue);
                    swapDescriptors(oldValue, newValue);

                    for (int i = 0; i < variableListeners.size(); i++)
                    {
                        VariableListener vl = (VariableListener)
                            variableListeners.elementAt(i);
                        vl.variablesSwapped(oldValue, newValue);
                    }
                }
            }
        }

        private void swapDescriptors(VariableModel v1,
                                     VariableModel v2)
        {
            v1.swapLocations(v2);
            swapControls(v1.getControl(), v2.getControl());
        }


        private void swapControls(JPanel panel1, JPanel panel2)
        {
            Component comp1 = panel1.getComponent(1);
            Component comp2 = panel2.getComponent(1);

            panel1.remove(1);
            panel2.remove(1);
            panel1.add(comp2, 1);
            panel2.add(comp1, 1);

            // relayout, repaint
            panel1.revalidate();
            panel2.revalidate();
            panel1.repaint();
            panel2.repaint();
        }
    }
}