/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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
            return Double.class;
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