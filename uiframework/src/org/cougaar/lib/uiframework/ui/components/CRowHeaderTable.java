package org.cougaar.lib.uiframework.ui.components;

import java.awt.Component;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 * This is a JTable with an arbitrary number of column and row headers.  The
 * start of the data is found by the row header table from the given table
 * model.  The data must be of type Float (and the headers must not).
 */
public class CRowHeaderTable extends JTable
{
    private TableCellRenderer headerCellRenderer = new HeaderCellRenderer();
    private int rowStart = 1;
    private int columnStart = 1;

    /**
     * Default constructor.  Create new row header table.
     */
    public CRowHeaderTable()
    {
        super();
    }

    /**
     * Create new row header table using the given table model for data.
     *
     * @param tm the table model to use for data.
     */
    public CRowHeaderTable(TableModel tm)
    {
        super(tm);

        findDataStart();
        resizeRowHeadersToFit();
        tm.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e)
                {
                    findDataStart();
                    resizeRowHeadersToFit();
                }
            });
    }

    /**
     * Returns the cell renderer to use for the given table cell.
     *
     * @param row cell row index
     * @param column cell column index
     */
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if ((row < rowStart) || (column < columnStart))
        {
            return headerCellRenderer;
        }
        else
        {
            return super.getCellRenderer(row, column);
        }
    }

    private void findDataStart()
    {
        TableModel tm = getModel();

        // find start of data (i.e. end of headers)
        search : {
            for (rowStart = 0; rowStart < tm.getRowCount(); rowStart++)
            {
                for (columnStart = 0; columnStart < tm.getColumnCount();
                     columnStart++)
                {
                    Object value = tm.getValueAt(rowStart, columnStart);
                    if ((value instanceof Float) ||
                        (value.toString().equals("N/A")))
                    {
                        break search;
                    }
                }
            }
        }
    }

    private void resizeRowHeadersToFit()
    {
        // Resize row header columns to fit contents
        SwingUtilities.invokeLater(new Runnable() {
                public void run()
                {
                    // Row Headers are sized to show contents
                    for (int i = 0; i < columnStart; i++)
                    {
                        sizeColumnBasedonContents(i);
                    }

                    int columnCount = getColumnModel().getColumnCount();

                    int dataWidth = 0;
                    if (!usingJdk13orGreater())
                    {
                        dataWidth = (getSize().width * 4)/columnCount; //jdk1.2
                    }
                    for (int i = columnStart; i < columnCount; i++)
                    {
                        getColumnModel().getColumn(i).
                            setPreferredWidth(dataWidth);
                    }

                    sizeColumnsToFit(-1);

                    if (!usingJdk13orGreater())
                    {
                        // jdk1.2 bug workaround
                        getTableHeader().resizeAndRepaint();
                        invalidate();
                        repaint();
                    }
                }
            });
    }

    /**
     * Needed for compatibility with jdk1.2.2
     */
    private boolean usingJdk13orGreater()
    {
        float versionNumber =
            Float.parseFloat(System.getProperty("java.class.version"));
        return (versionNumber >= 47.0);
    }

    private void sizeColumnBasedonContents(int columnIndex)
    {
        TableModel tm = getModel();
        TableColumn column = getColumnModel().getColumn(columnIndex);

        TableCellRenderer cr = null;
        if (usingJdk13orGreater())
        {
            JTableHeader th = getTableHeader();

            try
            {
                // Without using reflection, the following line is:
                // ct = th.getDefaultRenderer()
                // Will not compile under jdk1.2.2 (thus the use of reflection)
                cr = (TableCellRenderer)th.getClass().
                    getMethod("getDefaultRenderer", null).invoke(th, null);
            }
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            cr = column.getHeaderRenderer(); // jdk1.2
        }

        Component comp =
            cr.getTableCellRendererComponent(null, column.getHeaderValue(),
                                             false, false, 0, 0);
        int headerWidth = comp.getPreferredSize().width;

        comp = getDefaultRenderer(tm.getColumnClass(columnIndex)).
                getTableCellRendererComponent(
                    this, tm.getValueAt(rowStart, columnIndex),
                    false, false, rowStart, columnIndex);
        int cellWidth = comp.getPreferredSize().width;

        int targetWidth = Math.max(headerWidth, cellWidth);
        column.setMinWidth(targetWidth);
        column.setMaxWidth(targetWidth + 25);
    }

    private class HeaderCellRenderer extends DefaultTableCellRenderer
    {
        public Component
            getTableCellRendererComponent(JTable table, Object value,
                                          boolean isSelected, boolean hasFocus,
                                          int row, int column)
        {
	    if (table != null)
            {
                JTableHeader header = table.getTableHeader();
	        if (header != null)
                {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
	            setFont(header.getFont());
	        }
            }

            setText((value == null) ? "" : value.toString());
	    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            if (row < rowStart)
            {
                setHorizontalAlignment(JLabel.CENTER);
            }
            else
            {
                setHorizontalAlignment(JLabel.LEFT);
            }

            return this;
        }
    }
}