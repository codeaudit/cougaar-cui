/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.components.desktop;

import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;

import org.cougaar.lib.uiframework.ui.components.graph.DataSet;

public class TableModel extends AbstractTableModel 
{
	  private boolean DEBUG = true;
        String[] columnNames = {"Time", "Value"};
        Object[][] data = 
        {
            {"", ""}, 
             
            {"", ""} 
            
        };
        
        public DataSet tblDataSet = null;
        
        public TableModel()
        {
        	//System.out.println("%%%% tablemodel constructor");
        }
        public TableModel(DataSet dataSet)
        {
        	//System.out.println("%%%% tablemodel constructor " + tblDataSet);
        	tblDataSet = dataSet;
        	double arrayData[] = dataSet.getData();
        	data = new Object[arrayData.length/2][2];
        	//System.out.println("%%%% array " + arrayData);
        	//System.out.println("%%%% dataset " + tblDataSet);
        	int j = 0;
        	for(int i = 0; i < arrayData.length;i = i + 2)
        	{
        		//System.out.println("%%%% loop " + arrayData[i]);
        		data[j][0] = new Double(arrayData[i]);
        		data[j][1] = new Double(arrayData[i+1]);
        		j++;
        	}
        }
        public void setData(DataSet dataSet)
        {
        	double arrayData[] = dataSet.getData();
        	columnNames[0] = dataSet.xAxisLabel;
        	columnNames[1] = dataSet.yAxisLabel;
        	//System.out.println("%%%% xaxis " + columnNames[1]);
        	data = new Object[arrayData.length/2][2];
        	//System.out.println("%%%% array " + arrayData);
        	int j = 0;
        	for(int i = 0; i < arrayData.length;i = i + 2)
        	{
        		//System.out.println("%%%% loop " + arrayData[i]);
        		data[j][0] = new Double(arrayData[i]);
        		data[j][1] = new Double(arrayData[i+1]);
        		j++;
        	}
        	fireTableStructureChanged();
        }
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) { 
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of " 
                                   + value.getClass() + ")");
            }

            if (data[0][col] instanceof Double                        
                    && !(value instanceof Double)) {                  
                //With JFC/Swing 1.1 and JDK 1.2, we need to create    
                //an Integer from the value; otherwise, the column     
                //switches to contain Strings.  Starting with v 1.3,   
                //the table automatically converts value to an Integer,
                //so you only need the code in the 'else' part of this 
                //'if' block.                                          
                //XXX: See TableEditDemo.java for a better solution!!!
                try {
                    data[row][col] = new Double(value.toString());
                    fireTableCellUpdated(row, col);
                } catch (NumberFormatException e) {
                    System.out.println(
                        "The \"" + getColumnName(col)
                        + "\" column accepts only integer values.");
                }
            } else {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }

            if (DEBUG) {
                //System.out.println("New value of data:");
                printDebugData();
            }
        }
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
  }