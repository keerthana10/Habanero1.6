//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.sysfore.pos.purchaseorder;

import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author mateen
 */
public class JGoodsReceiptsLines  extends javax.swing.JPanel {

    private static InventoryTableModel m_inventorylines;
    
    
    /**
     * Creates new form JInventoryLines
     */
    public JGoodsReceiptsLines() {

        initComponents();

        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 40, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Sl. No.");
        columns.addColumn(c);
        c = new TableColumn(1, 215, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue(AppLocal.getIntString("label.item"));
        columns.addColumn(c);
        c = new TableColumn(2, 55, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue(AppLocal.getIntString("label.units"));
        columns.addColumn(c);
        c = new TableColumn(3, 60, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("UOM");
        columns.addColumn(c);
        c = new TableColumn(4, 106, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue(AppLocal.getIntString("label.price"));
        columns.addColumn(c);
       // c = new TableColumn(5, 50, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
      //  c.setHeaderValue(AppLocal.getIntString("label.tax"));
      //  columns.addColumn(c);

        m_tableinventory.setColumnModel(columns);

        m_tableinventory.getTableHeader().setReorderingAllowed(false);
        m_tableinventory.setRowHeight(40);
        m_tableinventory.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_tableinventory.setIntercellSpacing(new java.awt.Dimension(0, 1));

        m_inventorylines = new InventoryTableModel();
        m_tableinventory.setModel(m_inventorylines);
    }

    public void clear() {
        m_inventorylines.clear();
    }

    public void addLine(GoodsReceiptsLine i) {
        m_inventorylines.addRow(i);

        setSelectedIndex(m_inventorylines.getRowCount() - 1);
    }

    public void updateLine(GoodsReceiptsLine i, int row) {
        m_inventorylines.removeRow(row);
        m_inventorylines.addRow(i);

        setSelectedIndex(m_inventorylines.getRowCount() - 1);
    }

    public void deleteLine(int index) {
        m_inventorylines.removeRow(index);

        // Escojo una a seleccionar
        if (index >= m_inventorylines.getRowCount()) {
            index = m_inventorylines.getRowCount() - 1;
        }

        if ((index >= 0) && (index < m_inventorylines.getRowCount())) {
            // Solo seleccionamos si podemos.
            setSelectedIndex(index);
        }
    }

    public void setLine(int index, GoodsReceiptsLine i) {
        m_inventorylines.setRow(index, i);
        setSelectedIndex(index);
    }

    public GoodsReceiptsLine getLine(int index) {
        return m_inventorylines.getRow(index);
    }

    public List<GoodsReceiptsLine> getLines() {
        return m_inventorylines.getLines();
    }

    public int getCount() {
        return m_inventorylines.getRowCount();
    }

    public int getSelectedRow() {
        return m_tableinventory.getSelectedRow();
    }

    public void setSelectedIndex(int i) {

        // Seleccionamos
        m_tableinventory.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_tableinventory.getCellRect(i, 0, true);
        m_tableinventory.scrollRectToVisible(oRect);
    }

    /**
     * @return the multiplychanged
     */
    public boolean isMultiplychanged() {
        return false;
    }

    /**
     * @param multiplychanged the multiplychanged to set
     */
    public static void setMultiplychanged(boolean multiplychanged) {

        double total = 0.0;
        double subtotal = 0.0;
        double taxtotal = 0.0;
        if (multiplychanged) {
            java.util.List<GoodsReceiptsLine> lines = m_inventorylines.getLines();
            for (GoodsReceiptsLine l : lines) {
                double price = l.getPrice();
                String subTxs = l.getTaxes();
                String strTax = subTxs.substring(0, subTxs.length() - 1);
                double tax = Double.parseDouble(strTax);
                double qty = l.getMultiply();
                subtotal = (price * qty) + subtotal;
                taxtotal = ((tax / 100) * price * qty) + taxtotal;
                total = (subtotal + taxtotal);
            }
            JGoodsReceipts.setChangesmade(multiplychanged);
            JGoodsReceipts.setTotals(total, subtotal, taxtotal);
            
        }

    }

    private static class InventoryTableModel extends AbstractTableModel {

        private ArrayList<GoodsReceiptsLine> m_rows = new ArrayList<GoodsReceiptsLine>();

        public int getRowCount() {
            return m_rows.size();
        }

        public int getColumnCount() {
            return 5;
        }

        public String getColumnName(int column) {
            //return AppLocal.getIntString(m_acolumns[column].name);
            return "a";
        }

        public Object getValueAt(int row, int column) {

            GoodsReceiptsLine i = m_rows.get(row);
            switch (column) {

                case 0:
                    return row + 1;
                case 1:
                    return "<html>"+i.getProductName()+"<br /></html>";
                case 2:
                    return "x" + Formats.DOUBLE.formatValue(i.getMultiply());
                case 3:
                    return i.getUom();
                case 4:
                    return Formats.CURRENCY.formatValue(i.getPrice());
               // case 5:
               //     return i.getTaxes();

                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
            GoodsReceiptsLine i = m_rows.get(row);
            if (column == 2) {

                if (o.toString().startsWith("x")) {
                    String trimStr = o.toString().substring(1);
                    double dMul = Double.parseDouble(trimStr);
                    i.setMultiply(dMul);
                    if (dMul <= 0) {
                        removeRowFromPanelAndDatabase(row);
                        m_inventorylines.removeRow(row);
                    }
                } else {
                    try {
                        double dMul = Double.parseDouble(o.toString());
                        i.setMultiply(dMul);
                        if (dMul <= 0) {
                            removeRowFromPanelAndDatabase(row);
                            m_inventorylines.removeRow(row);
                        }
                    } catch (NumberFormatException n) {
                        //JOptionPane.showMessageDialog(this.getClass().getComponentType(), "");
                        i.setMultiply(i.getMultiply());
                    }
                }
                // flag = true, store old subtotal, total, tax, old qty
                // calculate totals with new qty
                setMultiplychanged(true);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {

            if (column == 2) {
                return true;
            } else {
                return false;
            }
        }

        public void clear() {
            int old = getRowCount();
            if (old > 0) {
                m_rows.clear();
                fireTableRowsDeleted(0, old - 1);
            }
        }

        public List<GoodsReceiptsLine> getLines() {
            return m_rows;
        }

        public GoodsReceiptsLine getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, GoodsReceiptsLine oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(GoodsReceiptsLine oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, GoodsReceiptsLine oLine) {

            m_rows.add(index, oLine);
            fireTableRowsInserted(index, index);
        }

        public void removeRow(int row) {
            m_rows.remove(row);
            fireTableRowsDeleted(row, row);
        }
        
        
    }

    private static class DataCellRenderer extends DefaultTableCellRenderer {

        private int m_iAlignment;

        public DataCellRenderer(int align) {
            m_iAlignment = align;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel aux = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            aux.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            aux.setHorizontalAlignment(m_iAlignment);
            if (!isSelected) {
                aux.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
            }
            return aux;
        }
    }

    private static void removeRowFromPanelAndDatabase(int row) {
        GoodsReceiptsLine line = m_inventorylines.getRow(row);
        JGoodsReceipts.removeLineFromDB(line);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        m_tableinventory = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        m_tableinventory.setAutoCreateColumnsFromModel(false);
        m_tableinventory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        m_tableinventory.setFocusable(false);
        m_tableinventory.setRequestFocusEnabled(false);
        m_tableinventory.setShowVerticalLines(false);
        jScrollPane1.setViewportView(m_tableinventory);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable m_tableinventory;
    // End of variables declaration//GEN-END:variables
}
