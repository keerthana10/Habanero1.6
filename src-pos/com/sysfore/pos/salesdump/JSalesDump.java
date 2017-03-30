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

package com.sysfore.pos.salesdump;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.JPanelTicketEdits;
import com.openbravo.pos.sales.JTicketsBag;
import com.openbravo.pos.sales.JTicketsBagTicket;
import com.openbravo.pos.sales.JTicketsBagTicketBag;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketMergeTaxInfo;
import com.sysfore.pos.accountingconfig.ExportDetails;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import com.sysfore.pos.stockreconciliation.DataLogicStockReceipts;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 *
 * @author adrianromero
 */
public class JSalesDump extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private TicketParser m_TTP;
    protected DataLogicSystem dlSystem;
    static PurchaseOrderReceipts PurchaseOrder = null;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
    public String[] strings = {""};
    public boolean updateMode = false;
    protected DataLogicSales m_dlSales;
    private DataLogicStockReceipts m_dlStock;
    private java.util.List<LocationInfo> locations;
    private ComboBoxValModel m_LocationsModel;
    private String[] stockDetails;
    private ComboBoxValModel m_CategoryModel;
    private java.util.List<SalesInfo> salesList = null;
    private DeviceTicket m_TP;
    private static InventoryTableModel m_Saleslines;
    private int stockSize = 0;
    private TicketInfo m_ticket;
    private TicketInfo m_ticketCopy;
    private int rows;
    private JTicketsBagTicketBag m_TicketsBagTicketBag;
    private JPanelTicketEdits m_panelticketedit;
    int setFlag=0;
    public TicketInfo ticketValue = null;
  //  private static InventoryTableModel m_inventorylines;
    public JSalesDump() {
        
        initComponents();
  

    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        

        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlStock = (DataLogicStockReceipts)m_App.getBean("com.sysfore.pos.stockreconciliation.DataLogicStockReceipts");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
         m_TP = new DeviceTicket();
         m_TicketsBagTicketBag = new JTicketsBagTicketBag();
          m_jMergeBills.setVisible(false);
          m_jSelect.setVisible(false);
       //  m_panelticketedit = new JPanelTicketEdits(this);
        initComponents();
//        populateDropDown();
     DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 100, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Bill No");
        columns.addColumn(c);
        c = new TableColumn(1, 100, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Customer Name");
        columns.addColumn(c);
        c = new TableColumn(2, 213, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Item");
        columns.addColumn(c);
        c = new TableColumn(3, 50, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Qty");
        columns.addColumn(c);
        c = new TableColumn(4, 107, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Total");
        columns.addColumn(c);
        c = new TableColumn(5, 107, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Bill Value");
        columns.addColumn(c);
//         c = new TableColumn(6, 58, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JCheckBox()));
//        c.setHeaderValue("Select");
//        columns.addColumn(c);
        m_jSalesDumpTable.setColumnModel(columns);

        m_jSalesDumpTable.getTableHeader().setReorderingAllowed(false);

        m_jSalesDumpTable.setRowHeight(50);
        m_jSalesDumpTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m_jSalesDumpTable.setIntercellSpacing(new java.awt.Dimension(0, 1));

        setVisible(true);
        m_Saleslines = new InventoryTableModel();
        m_jSalesDumpTable.setModel(m_Saleslines);

      
    }
 
   
    public void setSelectedIndex(int i) {

        // Seleccionamos
        m_jSalesDumpTable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jSalesDumpTable.getCellRect(i, 0, true);
        m_jSalesDumpTable.scrollRectToVisible(oRect);
    }

    private void setCellRenderer(JTable table) {
//        table.getColumn("Select").setCellRenderer(new CustomCheckBoxEditor(table));
    }

    /**
     * @return the multiplychanged
     */
    public boolean isMultiplychanged() {
        return false;
    }




     private static class InventoryTableModel extends AbstractTableModel {

        private ArrayList<SalesInfo> m_rows = new ArrayList<SalesInfo>();

        public int getRowCount() {
            return m_rows.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public String getColumnName(int column) {
           switch (column)
    {
    case 0: return "Bill No." ;
    case 1: return "Customer Name" ;
    case 2: return "Item" ;
    case 3: return "Qty" ;
    case 4: return "Total" ;
    case 5: return "Bill Value" ;
  //  case 6: return "Select" ;
    //case 2: return "Item" ;
    // ...
    }
     return null;

        }

        public Object getValueAt(int row, int column) {

            SalesInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getBillNo() );
                case 1:
                    return (i.getCusName());
                case 2:
                    return (i.getProductName());
                case 3:
                     return (i.getUnits());
                case 4:
                     return (i.getPrice());
                case 5:
                    return (i.getBillValue());
//                case 6:
//                    return Boolean.class;
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
            SalesInfo i = m_rows.get(row);
            if (column == 2) {


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

        public List<SalesInfo> getLines() {
            return m_rows;
        }

        public SalesInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, SalesInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(SalesInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, SalesInfo oLine) {

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

  private void showMessage(JSalesDump aThis, String msg) {
        JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
    }
  
 private void showMessageGreen(JSalesDump aThis,String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
 }
 private JPanel getLabelGreenPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.decode("#206e10"));
     helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
 
  private JPanel getLabelRedPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.red);
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
 private static class DataCellRenderer extends DefaultTableCellRenderer {

        private int m_iAlignment;

        public DataCellRenderer(int align) {
            m_iAlignment = align;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel aux = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            aux.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
           // aux.setVerti
            aux.setHorizontalAlignment(m_iAlignment);
            if (!isSelected) {
                aux.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
            }
            return aux;
        }
    }


      private void initStockData() {


        stockDetails = new String[]{
            "Bill No", "Customer Name", "Item", "Qty", "Total", "Bill Value"
        };
//         stockList = m_dlStock.getStockInfo(query);
        // stockSize = stockList.size();
         setStockTableModelAndHeader(m_jSalesDumpTable, stockSize);
         setStockTableData(m_jSalesDumpTable);
         setRows(stockSize);

    }

     private void setStockTableModelAndHeader(JTable table, int size) {
        table.getTableHeader().setPreferredSize(new Dimension(60, 25));
       // table.get
        table.setModel(new DefaultTableModel(stockDetails, size));
    }
     private void setStockTableData(JTable table) {

//        for (int col = 0; col < stockSize; col++) {
//
//            table.setValueAt(col+1, col, 0);
//       }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(salesList.get(col).getBillNo(), col, 0);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(salesList.get(col).getCusName(), col, 1);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(salesList.get(col).getProductName(), col, 2);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DOUBLE.formatValue(salesList.get(col).getUnits()), col, 3);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getPrice()), col, 4);
        }
         for (int col = 0; col < stockSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(salesList.get(col).getBillValue()), col, 5);
        }
    }
   

 
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.Salesdump");
    }

    public void activate() throws BasicException {
       Date sysDate = new Date();
       SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
       String currentDate = sdf.format(sysDate);
       m_jTxtDate.setText(currentDate);
       stockDetails = new String[]{
           "Bill No", "Customer Name", "Item", "Qty", "Total", "Bill Value"
         };
         setStockTableModelAndHeader(m_jSalesDumpTable, 0);
         setRows(stockSize);
         m_jPaymentMode.setSelectedItem("Cash");
         m_jMergeBills.setVisible(false);
         m_jSelect.setVisible(false);
//
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jSalesDumpTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        m_jTxtDate = new javax.swing.JTextField();
        m_jBtnSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        m_jPaymentMode = new javax.swing.JComboBox();
        m_jPostSales = new javax.swing.JButton();
        m_jMergeBills = new javax.swing.JButton();
        m_jSelect = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        m_jbillNo = new javax.swing.JTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jSalesDumpTable.setAutoCreateColumnsFromModel(false);
        m_jSalesDumpTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        m_jSalesDumpTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        m_jSalesDumpTable.setCellSelectionEnabled(true);
        m_jSalesDumpTable.setEditingColumn(2);
        m_jSalesDumpTable.setEnabled(false);
        m_jSalesDumpTable.setRowHeight(25);
        m_jSalesDumpTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_jSalesDumpTableMouseClicked(evt);
            }
        });
        m_jSalesDumpTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jSalesDumpTablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(m_jSalesDumpTable);
        m_jSalesDumpTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 53, 680, 351));

        jLabel3.setText("Date");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 13, 54, 20));

        m_jTxtDate.setEditable(false);
        jPanel4.add(m_jTxtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 13, 110, -1));

        m_jBtnSearch.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnSearch.setText("Search");
        m_jBtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnSearchActionPerformed(evt);
            }
        });
        jPanel4.add(m_jBtnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 13, -1, -1));

        jLabel1.setText("Tender types");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 13, 77, 20));

        m_jPaymentMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", " " }));
        jPanel4.add(m_jPaymentMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 13, 130, -1));

        m_jPostSales.setBackground(new java.awt.Color(255, 255, 255));
        m_jPostSales.setText("Post to Sales");
        m_jPostSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPostSalesActionPerformed(evt);
            }
        });
        jPanel4.add(m_jPostSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 410, -1, -1));

        m_jMergeBills.setBackground(new java.awt.Color(255, 255, 255));
        m_jMergeBills.setText("Merge Bills");
        m_jMergeBills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMergeBillsActionPerformed(evt);
            }
        });
        jPanel4.add(m_jMergeBills, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 410, -1, -1));

        m_jSelect.setBackground(new java.awt.Color(255, 255, 255));
        m_jSelect.setText("Select All");
        m_jSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSelectActionPerformed(evt);
            }
        });
        jPanel4.add(m_jSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 410, -1, -1));

        jLabel2.setText("Bill No:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 13, 40, 20));
        jPanel4.add(m_jbillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(242, 13, 110, -1));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 32, 700, 450));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jSalesDumpTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jSalesDumpTablePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jSalesDumpTablePropertyChange
private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    private void m_jBtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnSearchActionPerformed
     Date sysDate = new Date();
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       String currentDate = sdf.format(sysDate);
       String paymentMode = m_jPaymentMode.getSelectedItem().toString();
       String billNo = m_jbillNo.getText();
       String query;

       stockDetails = new String[]{
           "Bill No", "Customer Name", "Item", "Qty", "Total", "Bill Value"
        };
       if(billNo.isEmpty()){
           query = "PY.PAYMENT='"+paymentMode+"' AND R.DATENEW>='"+currentDate+"' ";
       }else{
          query = "PY.PAYMENT='"+paymentMode+"' AND R.DATENEW>='"+currentDate+"' AND T.DOCUMENTNO LIKE  '"+"%"+billNo+"%"+"' ";
       }
        salesList =  m_dlStock.getSalesDumpDetails(query);
         stockSize = salesList.size();
         setStockTableModelAndHeader(m_jSalesDumpTable, stockSize);
         setStockTableData(m_jSalesDumpTable);
    //     setCellRenderer(m_jSalesDumpTable);
         setRows(stockSize);
//         if(paymentmode.equals("Card")){
//             m_jMergeBills.setVisible(false);
//         }else{
//             m_jMergeBills.setVisible(true);
//         }
}//GEN-LAST:event_m_jBtnSearchActionPerformed

    private void m_jSalesDumpTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jSalesDumpTableMouseClicked
         int inventoyCount = 0;
        try {
            inventoyCount = m_dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
        }
         if(inventoyCount==0){
             int row = m_jSalesDumpTable.rowAtPoint(evt.getPoint());
             int col = m_jSalesDumpTable.columnAtPoint(evt.getPoint());
             String billNo = null;
             String date = m_jTxtDate.getText();
             String paymentmode = m_jPaymentMode.getSelectedItem().toString();
             String query;
             String billTotal = m_jSalesDumpTable.getModel().getValueAt(row, 5).toString();
            for(int i=row; i<=row; i++){
                 billNo = m_jSalesDumpTable.getModel().getValueAt(i, 0).toString();

                }

              try {
                TicketInfo ticket =  m_dlSales.loadTicketBasedOnBillNo(billNo);
                if(col==0){
                    JPanelTicketEdits jPanelTicketEdit = new JPanelTicketEdits();
                    jPanelTicketEdit.activate(m_App,billTotal,billNo);

                    JTicketsBagTicket jtbt = new JTicketsBagTicket(m_App,jPanelTicketEdit);
                 //   jtbt.m_ticket = ticket;
                    jtbt.m_ticketCopy = ticket;

                    jtbt.m_TicketsBagTicketBag.showEdit();
                    jtbt.m_panelticketedit.showCatalog();
                    jPanelTicketEdit.setTicket(ticket);
                    jPanelTicketEdit.setPaymentMode(paymentmode);
                    jPanelTicketEdit.setActiveTicket(ticket.copyTicket(), null,m_App);
                    ContentHolder c =  new ContentHolder(new JFrame(),jPanelTicketEdit);

    //                m_App.getBean("com.sysfore.pos.salesdump.JSalesDump");
                     Date sysDate = new Date();
                     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                     String currentDate = sdf.format(sysDate);
                     stockDetails = new String[]{
                           "Bill No", "Customer Name", "Item", "Qty", "Total", "Bill Value"
                        };
                     query = "PY.PAYMENT='"+paymentmode+"' AND R.DATENEW>='"+currentDate+"' ";
                        salesList =  m_dlStock.getSalesDumpDetails(query);
                         stockSize = salesList.size();
                         setStockTableModelAndHeader(m_jSalesDumpTable, stockSize);
                         setStockTableData(m_jSalesDumpTable);
                         //setCellRenderer(m_jSalesDumpTable);
                         setRows(stockSize);
//                         if(paymentmode.equals("Card")){
//                             m_jMergeBills.setVisible(false);
//                         }else{
//                             m_jMergeBills.setVisible(true);
//                         }

                    }

                    else if(col==6){
                   //   m_jSalesDumpTable.isColumnSelected(col);
                        m_jSalesDumpTable.isCellSelected(row, col);

                    }


            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"), e);
                msg.show(this);
            }

         }else{
             showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
         }
    }//GEN-LAST:event_m_jSalesDumpTableMouseClicked

    private void m_jPostSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPostSalesActionPerformed
        String billNo = null;
        for(int i=0;i<stockSize;i++){
             billNo = m_jSalesDumpTable.getModel().getValueAt(i, 0).toString();

            try {
                // m_dlSales.updateTicketCompleted(billNo);
                m_dlSales.updateCompletedTickets(billNo);
            } catch (BasicException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }
        m_jBtnSearchActionPerformed(evt);
       
    }//GEN-LAST:event_m_jPostSalesActionPerformed

    private void m_jMergeBillsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMergeBillsActionPerformed
       String billno;
       String mergeBillNo = null;
       String paymentmode = m_jPaymentMode.getSelectedItem().toString();
         java.util.ArrayList<String> billNoList = new ArrayList<String>();
         // java.util.ArrayList<String> billId1 = new ArrayList<String>();
      //   java.util.ArrayList<String> rowData = new ArrayList<String>();
         String billValueId = null;
         java.util.ArrayList<String> billValueList = new ArrayList<String>();
        for (int col = 0; col < stockSize; col++) {
        if(m_jSalesDumpTable.isRowSelected(col)){
            billno= m_jSalesDumpTable.getValueAt(col, 0).toString();
            String ayarow = Integer.toString(col);
            if(!billNoList.contains(billno)){
            billNoList.add("'"+billno+"'");
            }
           
          }

         StringBuilder b = new StringBuilder();
         Iterator<?> it = billNoList.iterator();
         while (it.hasNext()) {
         b.append(it.next());
         if (it.hasNext()) {
            b.append(',');
          }
        }

        mergeBillNo  = b.toString();
            
        }
         if(mergeBillNo.equals("")){
            showMessage(this, "Please select the bill nos.");
         }else{
        String leastBillNo = null;
        java.util.List<BillIdInfo> billIdList = m_dlStock.getBillIdInfo(mergeBillNo);
        if(billIdList.size()!=0){
        for(int i=0;i<billIdList.size();i++){
         billValueList.add("'"+billIdList.get(i).getId()+"'");
     }
        }
        StringBuilder b1 = new StringBuilder();
         Iterator<?> it1 = billValueList.iterator();
         while (it1.hasNext()) {
         b1.append(it1.next());
         if (it1.hasNext()) {
            b1.append(',');
          }
        }
          String billId = b1.toString();
          TicketInfo ticket = new TicketInfo();
           String dateNew;
          double discountTotal = 0;
          ticket.setBillId(billId);
          Date processDate = null;
            try {
                leastBillNo = m_dlStock.getLeastBillNo(mergeBillNo);
                dateNew = m_dlStock.getDateNew(leastBillNo);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                processDate = sdf.parse(dateNew);
            } catch (ParseException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }

               discountTotal =  m_dlStock.getDiscountTotal(mergeBillNo);
               
            } catch (BasicException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ticketValue = m_dlSales.loadTicketBasedOnBillId(leastBillNo, billIdList,billId);
            } catch (BasicException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<TicketMergeTaxInfo> mergeTaxInfo = null;
            try {
                mergeTaxInfo = m_dlSales.getMergeTaxInfo(billId);
            } catch (BasicException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                m_dlSales.deleteMergeTicket(billId);
                m_dlSales.saveMergeTicket(ticketValue, m_App.getProperties().getPosNo(), leastBillNo, "N",m_App.getInventoryLocation(),paymentmode,m_App.getActiveCashIndex(),m_App.getActiveDayIndex(),mergeTaxInfo,processDate,discountTotal);
                
            } catch (BasicException ex) {
                Logger.getLogger(JSalesDump.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
            m_jBtnSearchActionPerformed(evt);

       // TODO add your handling code here:
         // TODO add your handling code here:
    }//GEN-LAST:event_m_jMergeBillsActionPerformed

    private void m_jSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSelectActionPerformed
       m_jSalesDumpTable.selectAll(); // TODO add your handling code here:
    }//GEN-LAST:event_m_jSelectActionPerformed
  
 private void printTicket() {

        m_TP.getDevicePrinter("1").reset();


            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", m_ticket);
                m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.TicketPreview")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException eTP) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), eTP);
                msg.show(this);
            }
      //  }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jBtnSearch;
    private javax.swing.JButton m_jMergeBills;
    private javax.swing.JComboBox m_jPaymentMode;
    private javax.swing.JButton m_jPostSales;
    private javax.swing.JTable m_jSalesDumpTable;
    private javax.swing.JButton m_jSelect;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JTextField m_jbillNo;
    // End of variables declaration//GEN-END:variables
    
}
