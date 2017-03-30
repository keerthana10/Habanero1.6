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
package com.sysfore.pos.stockreconciliation;

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
public class JStockReconciliaionLines extends javax.swing.JPanel {

    private InventoryTableModel m_inventorylines;

    /**
     * Creates new form JInventoryLines
     */
    public JStockReconciliaionLines() {

        initComponents();

        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 180, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("SL.No");
        columns.addColumn(c);
        c = new TableColumn(1, 135, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Item");
        columns.addColumn(c);
        c = new TableColumn(2, 135, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("System Qty");
        columns.addColumn(c);
        c = new TableColumn(3, 155, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Physical Qty");
        columns.addColumn(c);
        c = new TableColumn(4, 135, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Variance");
        columns.addColumn(c);
        c = new TableColumn(5, 85, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Remarks");
        columns.addColumn(c);
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

    public void addLine(StockLinesInfo i) {
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

    public void setLine(int index, StockLinesInfo i) {
        m_inventorylines.setRow(index, i);
        setSelectedIndex(index);
    }

    public StockLinesInfo getLine(int index) {
        return m_inventorylines.getRow(index);
    }

    public List<StockLinesInfo> getLines() {
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

    public void goDown() {

        int i = m_tableinventory.getSelectionModel().getMaxSelectionIndex();
        if (i < 0) {
            i = 0; // No hay ninguna seleccionada
        } else {
            i++;
            if (i >= m_inventorylines.getRowCount()) {
                i = m_inventorylines.getRowCount() - 1;
            }
        }

        if ((i >= 0) && (i < m_inventorylines.getRowCount())) {
            // Solo seleccionamos si podemos.

            setSelectedIndex(i);
        }
    }

    public void goUp() {
        int i = m_tableinventory.getSelectionModel().getMinSelectionIndex();
        if (i < 0) {
            i = m_inventorylines.getRowCount() - 1; // No hay ninguna seleccionada
        } else {
            i--;
            if (i < 0) {
                i = 0;
            }
        }

        if ((i >= 0) && (i < m_inventorylines.getRowCount())) {
            // Solo seleccionamos si podemos.
            setSelectedIndex(i);
        }
    }

    public void disableCell(int row, int column) {
        System.out.println("disabling cell");
    }

    private static class InventoryTableModel extends AbstractTableModel {

        private ArrayList<StockLinesInfo> m_rows = new ArrayList<StockLinesInfo>();

        public int getRowCount() {
            return m_rows.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public String getColumnName(int column) {
            //return AppLocal.getIntString(m_acolumns[column].name);
            return "a";
        }

        public Object getValueAt(int row, int column) {

            StockLinesInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  i.getProductName();
                case 1:
                    return "x" + Formats.DOUBLE.formatValue(i.getUnits());
                case 2:
                 //   return "x" + Formats.DOUBLE.formatValue(i.getUserRqty());
                case 3:
                  //  return "x" + Formats.DOUBLE.formatValue(i.getM_received());
                case 4:
                  //  return "x" + Formats.DOUBLE.formatValue(i.getM_pending());
                case 5:
                 //   return Formats.CURRENCY.formatValue(i.getPrice());
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
            StockLinesInfo i = m_rows.get(row);
            if (column == 2) {

//                if (o.toString().startsWith("x")) {
//                    String trimStr = o.toString().substring(1);
//                    try {
//                        Double rQty = Double.parseDouble(trimStr);
//                        if (rQty <= 0) {
//                            i.setM_received(i.getM_received());
//                            i.setUserRqty(0);
//                            showMessage("Receiving Quantity cannot be less then zero");
//                        } else if (rQty > i.getPermanentPendingQty()) {
//                            i.setM_received(i.getM_received());
//                            i.setUserRqty(0);
//                            showMessage("Entered Quantity cannot be greater then pending quantity");
//                        } else {
//                            if (rQty > i.getMultiply()) {
//                                i.setM_received(i.getM_received());
//                                i.setUserRqty(0);
//                            } else {
//                                i.setUserRqty(rQty.intValue());
//                                i.setM_pending(i.getPermanentPendingQty() - rQty);
//                                i.setIsPendingQtyModified("Y");
//                                MainGrnPanel.setExit(true);
//                            }
//                        }
//                    } catch (NumberFormatException n) {
//                        i.setM_received(i.getM_received());
//                        i.setUserRqty(0);
//                        showMessage("Please Enter Valid Quantity");
//                    }
//
//                } else {
//                    try {
//                        Double rQty = Double.parseDouble(o.toString());
//                        if (rQty <= 0) {
//                            i.setM_received(i.getM_received());
//                            i.setUserRqty(0);
//                            showMessage("Receiving Quantity cannot be less then zero");
//                        } else if (rQty > i.getPermanentPendingQty()) {
//                            i.setM_received(i.getM_received());
//                            i.setUserRqty(0);
//                            showMessage("Entered Quantity cannot be greater then pending quantity");
//                        } else {
//
//                            if (rQty > i.getMultiply()) {
//                                i.setM_received(i.getM_received());
//                                i.setUserRqty(0);
//                                showMessage("Entered Quantity cannot be greater then pending quantity");
//                            } else {
//                                i.setUserRqty(rQty.intValue());
//                                i.setM_pending(i.getPermanentPendingQty() - rQty);
//                                i.setIsPendingQtyModified("Y");
//                                MainGrnPanel.setExit(true);
//                            }
//                        }
//                    } catch (NumberFormatException n) {
//                        i.setM_received(i.getM_received());
//                        i.setUserRqty(0);
//                        showMessage("Please Enter Valid Quantity");
//                    }
//                }

            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            boolean retVal = false;
            try {
                if (column == 2) {
                    retVal = true;
                } else {
                    retVal = false;
                }
            } catch (NullPointerException n) {
            }
            return retVal;
        }

        public void clear() {
            int old = getRowCount();
            if (old > 0) {
                m_rows.clear();
                fireTableRowsDeleted(0, old - 1);
            }
        }

        public List<StockLinesInfo> getLines() {
            return m_rows;
        }

        public StockLinesInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, StockLinesInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(StockLinesInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, StockLinesInfo oLine) {

            m_rows.add(index, oLine);
            fireTableRowsInserted(index, index);
        }

        public void removeRow(int row) {
            m_rows.remove(row);
            fireTableRowsDeleted(row, row);
        }

        private void showMessage(String msg) {
            JOptionPane.showMessageDialog(null, msg);
        }
//        private void refresh(int row, int column) {
//          refresh(row, column);
//          fireTableRowsUpdated(row, column);
//        }
    }

    private static class DataCellRenderer extends DefaultTableCellRenderer {

        private int m_iAlignment;

        public DataCellRenderer(int align) {
            m_iAlignment = align;
        }

        @Override
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

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

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
