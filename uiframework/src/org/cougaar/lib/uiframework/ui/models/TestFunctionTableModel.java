package org.cougaar.lib.uiframework.ui.models;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;

//import java.sql.*;

/**
  * Test class.  A table model that models two mathmatical functions
  * (sin and cosine).  Used to test table model viewers.
  */
public class TestFunctionTableModel extends DefaultTableModel
{
    private static int rowCount = 2;
    private double[][] data = new double[rowCount][30];

    public TestFunctionTableModel()
    {
        createPlotData();
    }

    private void createPlotData()
    {
        int row = 0;
        for (int column=0; column < data[row].length; column++)
        {
            data[row][column] = Math.sin(column * (360/30));
        }

        row = 1;
        for (int column=0; column < data[row].length; column++)
        {
            data[row][column] = Math.cos(column * (360/30));
        }
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public int getColumnCount()
    {
        return data[0].length + 1;
    }

    public String getColumnName(int columnIndex)
    {
        return String.valueOf(columnIndex);
    }

    public Class getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
        {
            return String.class;
        }
        else
        {
            return Integer.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            return "Row: " + rowIndex;
        }

        return new Double(data[rowIndex][columnIndex - 1]);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        //TODO: Implement this javax.swing.table.TableModel method
    }
}