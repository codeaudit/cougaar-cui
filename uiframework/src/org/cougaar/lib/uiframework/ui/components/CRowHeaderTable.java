package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;

import org.cougaar.lib.uiframework.ui.util.SelectableHashtable;

/**
 * This is a JTable with an arbitrary number of column and row headers.  The
 * start of the data is found by the row header table from the given table
 * model.  The data must be of type Float (and the headers must not).
 */
public class CRowHeaderTable extends JTable
{
    private static final int MIN_CELL_CHARACTER_WIDTH = 4;
    private int minCellWidth = 0;
    private HeaderCellRenderer headerCellRenderer = new HeaderCellRenderer();
    private JTable cornerHeader = createCornerHeader();
    private JTable rowHeader = createRowHeader();
    protected int rowStart = 1;
    protected int columnStart = 1;

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
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        findDataStart();
        resetMinCellWidth();
        resizeRowHeadersToFit();
    }

    /**
     * Called whenever look and feel is changed.
     */
    public void updateUI()
    {
        super.updateUI();

        resetMinCellWidth();
        resizeRowHeadersToFit();
    }

    private void resetMinCellWidth()
    {
        StringBuffer sampleString = new StringBuffer();
        for (int i = 0 ; i < MIN_CELL_CHARACTER_WIDTH; i++)
        {
            sampleString.append("#");
        }
        minCellWidth =
            getFontMetrics(UIManager.getFont("Label.font")).
                stringWidth(sampleString.toString());
    }

    /**
     * Called when table model changes.  Updates view of model.
     *
     * @param e table model event describing change.
     */
    public void tableChanged(TableModelEvent e)
    {
        findDataStart();
        resizeRowHeadersToFit();

        super.tableChanged(e);

        if (e.getFirstRow() == TableModelEvent.HEADER_ROW)
        {
            // Remove repeated header columns
            TableColumnModel cm = getColumnModel();
            for (int i = 0; i < columnStart + 1; i++)
            {
                if (cm.getColumnCount() > 0)
                {
                    cm.removeColumn(cm.getColumn(0));
                }
            }
        }
    }

    /**
     * Get the table used for rendering row headers for main table
     *
     * @return the table used for rendering row headers for main table
     */
    public JTable getRowHeader()
    {
        return rowHeader;
    }

    /**
     * If this JTable is the viewportView of an enclosing JScrollPane
     * (the usual situation), configure this ScrollPane by, amongst other
     * things, installing the table's rowHeader as the rowHeaderView of
     * the scroll pane.
     */
    protected void configureEnclosingScrollPane()
    {
        super.configureEnclosingScrollPane();

        Container c = getParent();
        if (c instanceof JViewport)
        {
            c = c.getParent();
            if (c instanceof JScrollPane)
            {
                JScrollPane sp = (JScrollPane)c;
                sp.setRowHeaderView(rowHeader);
                sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerHeader);

                sp.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent e)
                        {
                            resizeRowHeadersToFit();
                        }
                    });
            }
        }
    }

    /**
     * Future work:  modify this method to return a JTable that includes all
     * row header columns (instead of just the first one).
     */
    private JTable createRowHeader()
    {
        TableModel tm = new AbstractTableModel() {
                public int getColumnCount()
                {
                    return columnStart + 1;
                }
                public int getRowCount()
                {
                    return getModel().getRowCount();
                }
                public Object getValueAt(int row, int column)
                {
                    return getModel().getValueAt(row, column);
                }
            };
        JTable newRowHeader = new JTable(tm);
        newRowHeader.setDefaultRenderer(Object.class, headerCellRenderer);
        return newRowHeader;
    }

    private JTable createCornerHeader()
    {
        TableModel tm = new AbstractTableModel() {
                public int getColumnCount()
                {
                    return columnStart + 1;
                }
                public int getRowCount()
                {
                    return 1;
                }
                public Object getValueAt(int row, int column)
                {
                    return getModel().getColumnName(column);
                }
            };
        JTable newCornerHeader = new JTable(tm);
        newCornerHeader.setDefaultRenderer(Object.class, headerCellRenderer);
        return newCornerHeader;
    }

    /**
     * Returns the cell renderer to use for the given table cell.
     *
     * @param row cell row index
     * @param column cell column index
     */
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (row < rowStart)
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

        // adjust for first column which is now rendered in seperate row header
        // component.
        if (columnStart > 0) columnStart--;
    }

    private void resizeRowHeadersToFit()
    {
        // Resize row header columns to fit contents
        SwingUtilities.invokeLater(new Runnable() {
                public void run()
                {
                    if ((rowHeader == null)||(getParent() == null)) return;

                    // Promary Row Headers are sized to show contents
                    int headCount = rowHeader.getModel().getColumnCount();
                    int totalWidth = 0;
                    for (int column = 0; column < headCount; column++)
                    {
                        totalWidth +=
                            sizeRowHeaderColumnBasedonContents(column);
                    }
                    totalWidth += 10;
                    rowHeader.setPreferredScrollableViewportSize(
                        new Dimension(totalWidth,
                                      rowHeader.getPreferredSize().height));
                    cornerHeader.setPreferredScrollableViewportSize(
                        new Dimension(totalWidth,
                                      cornerHeader.getPreferredSize().height));

                    // Set data cell widths
                    // (only use horizontal scroll bar if needed)
                    int columnCount = getColumnModel().getColumnCount();
                    int dataWidth = 0;
                    if (columnCount > 0)
                    {
                        int vpWidth = getParent().getSize().width;
                        if ((vpWidth / columnCount) < minCellWidth)
                        {
                            setAutoResizeMode(AUTO_RESIZE_OFF);
                            dataWidth = minCellWidth;
                        }
                        else
                        {
                            setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                            if (!usingJdk13orGreater())
                            {
                                dataWidth = (getSize().width * 4)/columnCount;
                            }
                        }
                    }

                    for (int i = columnStart; i < columnCount; i++)
                    {
                        TableColumn c = getColumnModel().getColumn(i);
                        c.setHeaderRenderer(headerCellRenderer);
                        c.setPreferredWidth(dataWidth);

                    }
                    sizeColumnsToFit(-1);

                    // Ensure that table repaints correctly
                    getTableHeader().resizeAndRepaint();
                    rowHeader.invalidate();
                    invalidate();
                    repaint();
                }
            });
    }

    /**
     * Needed for compatibility with jdk1.2.2
     */
    private static boolean usingJdk13orGreater()
    {
        float versionNumber =
            Float.parseFloat(System.getProperty("java.class.version"));
        return (versionNumber >= 47.0);
    }

    /* not currently being used (but might be useful later)
    private static int sizeColumnBasedonContents(JTable table,int columnIndex)
    {
        TableModel tm = table.getModel();
        TableColumn column = table.getColumnModel().getColumn(columnIndex);

        TableCellRenderer cr = null;
        if (usingJdk13orGreater())
        {
            JTableHeader th = table.getTableHeader();

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

        int maxCellWidth = 0;
        for (int row = 0; row < tm.getRowCount(); row++)
        {
            comp = table.getDefaultRenderer(tm.getColumnClass(columnIndex)).
                    getTableCellRendererComponent(
                        table, tm.getValueAt(row, columnIndex),
                        false, false, row, columnIndex);
            int cellWidth = comp.getPreferredSize().width+table.getRowMargin();
            maxCellWidth = Math.max(maxCellWidth, cellWidth);
        }

        int targetWidth = Math.max(headerWidth, maxCellWidth);
        column.setMinWidth(targetWidth);
        column.setMaxWidth(targetWidth + 25);

        return targetWidth;
    }
    */

    private int sizeRowHeaderColumnBasedonContents(int columnIndex)
    {
        TableModel tm = cornerHeader.getModel();
        TableCellRenderer cr = cornerHeader.getCellRenderer(0, columnIndex);

        Component comp =
            cr.getTableCellRendererComponent(null, tm.getValueAt(0, columnIndex),
                                             false, false, 0, 0);
        int headerWidth = comp.getPreferredSize().width;

        JTable table = rowHeader;
        tm = table.getModel();
        int maxCellWidth = 0;
        for (int row = 0; row < tm.getRowCount(); row++)
        {
            comp = table.getDefaultRenderer(tm.getColumnClass(columnIndex)).
                    getTableCellRendererComponent(
                        table, tm.getValueAt(row, columnIndex),
                        false, false, row, columnIndex);
            int cellWidth = comp.getPreferredSize().width+table.getRowMargin();
            maxCellWidth = Math.max(maxCellWidth, cellWidth);
        }

        int targetWidth = Math.max(headerWidth, maxCellWidth);

        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(targetWidth);
        column.setMaxWidth(targetWidth + 25);
        column = cornerHeader.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(targetWidth);
        column.setMaxWidth(targetWidth + 25);

        return targetWidth;
    }

    private class HeaderCellRenderer extends DefaultTableCellRenderer
    {
        public Component
            getTableCellRendererComponent(JTable table, Object value,
                                          boolean isSelected, boolean hasFocus,
                                          int row, int column)
        {
            prepareComponent();
            String dispValue = (value == null) ? "" : value.toString();
            setText(dispValue);
            setToolTipText(dispValue);
            if (row < rowStart)
            {
                 setHorizontalAlignment(JLabel.CENTER);
            }
            else
            {
                 setHorizontalAlignment(JLabel.LEFT);
            }

            // This special case statement is not generic and should be moved
            // to a domain specific class when time becomes available to
            // rework design.
            if (column <= columnStart)
            {
                if (value instanceof DefaultMutableTreeNode)
                {
                    Object userObject =
                        ((DefaultMutableTreeNode)value).getUserObject();
                    if (userObject instanceof SelectableHashtable)
                    {
                        SelectableHashtable sht =
                            (SelectableHashtable)userObject;
                        String selectedProperty = sht.getSelectedProperty();
                        String tooltipProperty =
                            selectedProperty.equals("UID") ? "ITEM_ID" : "UID";
                        Object tooltip = sht.get(tooltipProperty);
                        if (tooltip != null)
                        {
                            setToolTipText(tooltip.toString());
                        }
                    }
                }
            }

            return this;
        }

        private void prepareComponent()
        {
	    if (CRowHeaderTable.this != null)
            {
                JTableHeader header = getTableHeader();
	        if (header != null)
                {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
	            setFont(header.getFont());
	        }
            }
            setOpaque(true);
	    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        }
    }
}