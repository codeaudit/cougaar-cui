package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.cougaar.lib.uiframework.ui.models.StoplightThresholdModel;

/**
 * Stoplight table bean.  Data cells of table are color coded based on the
 * value in the cell and the threshold values set in the bean.  Either the
 * MThumbSliderThresholdControl or the SliderThresholdControl can be used to
 * dynamically set the color thresholds of this bean.
 */
public class CStoplightTable extends CRowHeaderTable
{
    private StoplightThresholdModel thresholds = new StoplightThresholdModel();
    private boolean showColor = true;
    private boolean showValue = true;
    private StoplightCellRenderer stoplightCellRenderer;

    /**
     * Default constructor.  Creates a stoplight chart with a table model
     * that is a 10 by 10 matrix of random data.
     */
    public CStoplightTable()
    {
        super();

        // without a given table model, just provide random data
        // This is just for bean testing.
        // In the future, no model will be default
        Float[][] data = new Float[10][10];
        String[] columnNames = new String[10];
        Random rand = new Random();
        for (int row = 0; row < data.length; row++)
        {
            columnNames[row] = String.valueOf(row);
            for (int column=0; column < data[row].length; column++)
            {
                data[row][column] = new Float(rand.nextFloat() * 2);
            }
        }
        setModel(new DefaultTableModel(data, columnNames));

        init();
    }

    /**
     * Creates a stoplight chart that gets it's data from the given table
     * model
     *
     * @param tableModel the model to use as data source
     */
    public CStoplightTable(TableModel tableModel)
    {
        super(tableModel);
        init();
    }

    /**
     * Initilize stoplight chart
     */
    private void init()
    {
        stoplightCellRenderer = new StoplightCellRenderer();
        setRowSelectionAllowed(false);
    }

    /**
     * Returns the cell renderer to use for the given table cell.
     *
     * @param row cell row index
     * @param column cell column index
     */
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        Object value = getModel().getValueAt(row, column);
        if ((value instanceof Float) || (value instanceof Integer))
        {
            return stoplightCellRenderer;
        }
        return super.getCellRenderer(row, column);
    }

    /**
     * Set the selected thresholds.
     *
     * @param thresholds new thresholds
     */
    public void setThresholds(StoplightThresholdModel thresholds)
    {
        this.thresholds = thresholds;
        threadedTableUpdate();
    }

    /**
     * Get the selected thresholds
     *
     * @return currently selected thresholds
     */
    public StoplightThresholdModel getThresholds()
    {
        return thresholds;
    }

    /**
     * Set whether color coded is activated.
     *
     * @param showColor boolean telling whether color coding is activated.
     */
    public void setShowColor(boolean showColor)
    {
        this.showColor = showColor;
    }

    /**
     * Set whether data values are displayed.
     *
     * @param showValue boolean telling whether data values are displayed.
     */
    public void setShowValue(boolean showValue)
    {
        this.showValue = showValue;
    }

    /**
     * Associates a control to this table that is used to select features of
     * this table.
     *
     * @param vfc control to associate with table
     */
    public void
        setViewFeatureSelectionControl(final CViewFeatureSelectionControl vfc)
    {
        vfc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if (e.getActionCommand().
                        equals(CViewFeatureSelectionControl.COLOR))
                    {
                        setShowColor(true);
                        setShowValue(false);
                    }
                    else if (e.getActionCommand().
                             equals(CViewFeatureSelectionControl.VALUE))
                    {
                        setShowColor(false);
                        setShowValue(true);
                    }
                    else if (e.getActionCommand().
                             equals(CViewFeatureSelectionControl.BOTH))
                    {
                        setShowColor(true);
                        setShowValue(true);
                    }

                    threadedTableUpdate();
                }
            });
    }

    /**
     * I'm not sure that this helps (need to find a way to reduce number of
     * tableChanged events to a minimum)
     */
    private void threadedTableUpdate()
    {
        (new Thread() {
                public void run()
                {
                    tableChanged(new TableModelEvent(getModel()));
                }
            }).start();
    }

    /**
     * This renderer is used to color code the data cells.
     */
    private class StoplightCellRenderer extends DefaultTableCellRenderer
    {
        private DecimalFormat valueFormat = new DecimalFormat("####.##");

        public StoplightCellRenderer()
        {
            super();

            // enforce black fonts (otherwise L&F themes could make unreadable)
            setForeground(Color.black);
        }

        public Component
            getTableCellRendererComponent(JTable table, Object value,
                                      boolean isSelected, boolean hasFocus,
                                      int row, int column)
        {
            colorRenderer(value);
            super.getTableCellRendererComponent(table, value, isSelected,
                                                hasFocus, row, column);
            if (showValue)
            {
                setValue(valueFormat.format(value));
            }
            else
            {
                setValue("");
            }

            return this;
        }

        private void colorRenderer(Object value)
        {
            Comparable compValue = (Comparable)value;

            if (showColor)
            {
                if ((compValue.compareTo(new Float(thresholds.getGreenMin())) >= 0) &&
                    (compValue.compareTo(new Float(thresholds.getGreenMax())) <= 0))
                {
                    setBackground(Color.green);
                }
                else if ((compValue.compareTo(new Float(thresholds.getYellowMin())) >= 0) &&
                         (compValue.compareTo(new Float(thresholds.getYellowMax())) <= 0))
                {
                    setBackground(Color.yellow);
                }
                else
                {
                    setBackground(Color.red);
                }
            }
            else
            {
                setBackground(Color.white);
            }
        }
    }
}

