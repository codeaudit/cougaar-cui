package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.util.Selector;

/**
 * This component is a button that invokes a popup menu filled with a list of
 * Selector controls that are contained in pullright menus.
 * The button's label is updated with both the selector selected and the
 * selected value of the selected selector.
 */
public class CMenuButton extends JButton
{
    private JPopupMenu popupMenu;

    /**
     * Default constructor.  Creates a new CMenuButton with no selectors.
     */
    public CMenuButton()
    {
        super("Menu Button");

        popupMenu = new JPopupMenu();
        popupMenu.setInvoker(this);

        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    popupMenu.setPreferredSize(new Dimension(getSize().width,
                        popupMenu.getPreferredSize().height));
                    popupMenu.show(CMenuButton.this, 0, getSize().height);
               }
            });
    }

    /**
     * Adds a selector control to the popup menu associated with this button.
     *
     * @param label menu label for this selector
     * @param s the selector to add to the popup menu
     */
    public void addSelector(final String label, final Selector s)
    {
        final JMenu menu = new JMenu(label);
        menu.add((Component)s);
        menu.enableInputMethods(false); // Swing bug workaround
        popupMenu.add(menu);

        menu.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e)
                {
                    updateSelection(label, s);
                }
            });

        s.addPropertyChangeListener("selectedItem",
                                    new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e)
                {
                    updateSelection(label, s);
                }
            });
    }

    private void updateSelection(String label, Selector s)
    {
        String variable = label;
        String value = s.getSelectedItem().toString();
        setText(variable + ": " + value);
        popupMenu.setVisible(false);
    }

    /**
     * Main for unit testing.
     *
     * @param args ignored
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CMenuButton mb = new CMenuButton();
        mb.addSelector("Item", new CNodeSelector());
        mb.addSelector("Time", new CRangeSelector(false));
        mb.addSelector("Org", new CNodeSelector());
        mb.addSelector("Metric", new CNodeSelector());
        frame.getContentPane().add(mb, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}