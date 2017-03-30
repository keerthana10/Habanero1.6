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

import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.sales.DiscountRateinfo;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.print.PrintService;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author adrianromero
 */
public class JStockReconciliation extends JPanel implements JPanelView,BeanFactoryApp{
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
    private java.util.List<CategoryDetailsInfo> categoryLines;
//    private java.util.List<ReConsiliationInfo> inventoryid;
    private java.util.List<LocationInfo> locations;
    private ComboBoxValModel m_LocationsModel;
    private String[] stockDetails;
    private ComboBoxValModel m_CategoryModel;
    private java.util.List<StockDetailsInfo> stockList = null;
    private java.util.List<StockDocumentLinesInfo> stockDocumentLinesList = null;
    private DataLogicStockReceipts m_dlStock;
    private InventoryTableModel m_inventorylines;
    private InventoryDocumentModel m_inventoryDocumentlines;
    private int stockSize = 0;
    List<StockReconciliationInfo> dTable = new ArrayList<StockReconciliationInfo>();
    List<StockReconciliationInfo> stockReconcile = new ArrayList<StockReconciliationInfo>();
    private int rows;
    private java.util.List<StockHeaderInfo> stockHeaderList = null;
    private java.util.List<StockDocumentInfo> stockDocumentList = null;
    private boolean[][] editable_cells;
    
    int setFlag=0;
    public JStockReconciliation() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        StockDetailsInfo pInfoExt = new StockDetailsInfo();
        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlStock = (DataLogicStockReceipts)m_App.getBean("com.sysfore.pos.stockreconciliation.DataLogicStockReceipts");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        initComponents();
        populateDropDown();
//        //initTableData();
        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 240, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Item");
        columns.addColumn(c);
        c = new TableColumn(1, 90, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("System Qty");
        columns.addColumn(c);
        c = new TableColumn(2, 90, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Physical Qty");
        columns.addColumn(c);
        c = new TableColumn(3, 120, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Variance Volume");
        columns.addColumn(c);
        c = new TableColumn(4, 110, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Variance Value");
        columns.addColumn(c);
        c = new TableColumn(5, 120, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Remarks");
        columns.addColumn(c);

        m_jStocktable.setColumnModel(columns);

        m_jStocktable.getTableHeader().setReorderingAllowed(false);

        m_jStocktable.setRowHeight(50);
        m_jStocktable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_jStocktable.setIntercellSpacing(new java.awt.Dimension(0, 1));

        setVisible(true);
        m_jTxtDocument.setVisible(false);
       
    }
    public void addLine(StockDocumentLinesInfo i) {
        m_inventorylines.addRow(i);
        setSelectedIndex(m_inventorylines.getRowCount() - 1);
    }
 public void adddocumentLine(StockDetailsInfo i) {
       
        m_inventoryDocumentlines.addRow(i);

        setDocumentSelectedIndex(m_inventoryDocumentlines.getRowCount() - 1);
    }
   
    public StockDocumentLinesInfo getLine(int index) {
        return m_inventorylines.getRow(index);
    }

    public List<StockDocumentLinesInfo> getLines() {
        return m_inventorylines.getLines();
    }

    public int getCount() {
        return m_inventorylines.getRowCount();
    }

    public int getSelectedRow() {
        return m_jStocktable.getSelectedRow();
    }

    public void setSelectedIndex(int i) {

        // Seleccionamos
        m_jStocktable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jStocktable.getCellRect(i, 0, true);
        m_jStocktable.scrollRectToVisible(oRect);
    }
public void setDocumentSelectedIndex(int i) {

        // Seleccionamos
        m_jStocktable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jStocktable.getCellRect(i, 0, true);
        m_jStocktable.scrollRectToVisible(oRect);
    }
    /**
     * @return the multiplychanged
     */
    public boolean isMultiplychanged() {

        return false;
    }
     private void initTableData() {


        stockDetails = new String[]{
           "Item", "System Qty", "Physical Qty", "Variance Volume", "Variance Value", "Remarks"
        };
          stockList =  m_dlStock.getStockDetails();
         stockSize = stockList.size();
         setStockTableModelAndHeader(m_jStocktable, stockSize);
         setStockTableData(m_jStocktable);
         setRows(stockSize);
         m_inventoryDocumentlines = new InventoryDocumentModel();
         m_jStocktable.setModel(m_inventoryDocumentlines);
         for(int i =0;i<stockSize;i++){
            adddocumentLine(new StockDetailsInfo(stockList.get(i)));
         }


    }
      private void initDocumentData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
       
      try{
          String documentNo = m_jDocumentNo.getSelectedItem().toString();
 
                 stockHeaderList = m_dlStock.getStockHeaderCategoryInfo(documentNo);

                String date = sdf.format(stockHeaderList.get(0).getStockDate());
                m_jDocumentNo.setSelectedItem(stockHeaderList.get(0).getDocumentNo());
                 m_jTxtDate.setText(date);

                stockDetails = new String[]{
                   "Item", "System Qty", "Physical Qty", "Variance Volume","Variance Value", "Remarks"
                };
              //  String wareHouseid = locations.get(m_jWarehouse.getSelectedIndex()).getID();
                 stockDocumentLinesList =  m_dlStock.getStockLinesInfo(documentNo);
                  // stockList =  m_dlStock.getStockDetails();
                 stockSize = stockDocumentLinesList.size();
                 setStockTableModelAndHeader(m_jStocktable, stockSize);
                 setStockLinesTableData(m_jStocktable);
                 setRows(stockSize);
                 m_inventorylines = new InventoryTableModel();
                 m_jStocktable.setModel(m_inventorylines);
                 for(int i =0;i<stockSize;i++){
                    
                     addLine(new StockDocumentLinesInfo(stockDocumentLinesList.get(i)));
                 }
                 
                  m_jBtnReconcile.setEnabled(false);
                  m_jBtnPrint.setEnabled(true);
                  m_jBtnSave.setEnabled(true);
                  m_jCalculateVariance.setEnabled(true);
             
              }catch(NullPointerException ex){
                  showMessage(this, "Please Select the document No");
              }
    }


     private void initData(){

        stockDetails = new String[]{
           "Item", "System Qty", "Physical Qty", "Variance Volume","Variance Value", "Remarks"
        };

         setStockTableModelAndHeader(m_jStocktable, 0);
         m_jStocktable.removeAll();
         setRows(0);
         m_jDocumentNo.setVisible(true);
         m_jTxtDocument.setVisible(false);
         m_jDocumentNo.setSelectedIndex(-1);
     }
    private void initDocumentcombo() {
        stockDocumentList = m_dlStock.getStockDocumentInfo();
     //   m_jDocumentNo.addItem("");
        m_jDocumentNo.removeAllItems();
        for (StockDocumentInfo stock : stockDocumentList) {
            m_jDocumentNo.addItem(stock.getDocumentNo());
        }
        m_jDocumentNo.setSelectedIndex(-1);
    }



     private static class InventoryTableModel extends AbstractTableModel {

        public ArrayList<StockDocumentLinesInfo> m_rows = new ArrayList<StockDocumentLinesInfo>();

        public int getRowCount() {
             //System.out.println ("enetr---getRowCount"+m_rows.size());
            return m_rows.size();
        }

        public int getColumnCount() {
           // System.out.println ("enetr---getColumnCount");
            return 6;
        }

        public String getColumnName(int column) {
            //return AppLocal.getIntString(m_acolumns[column].name);
            return "a";
        }

        public Object getValueAt(int row, int column) {
          //  System.out.println ("enetr---getValueAt");
            StockDocumentLinesInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getProductName() );
                case 1:
                    return  Formats.DOUBLE.formatValue(i.getSystemQty());
                case 2:
                    return  Formats.DOUBLE.formatValue(i.getPhysicalQty());
                case 3:
                     return Formats.DOUBLE.formatValue(i.getVariance());
                case 4:
                     return Formats.DoubleValue.formatValue(i.getPrice());
                case 5:
                    return i.getRemarks();
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
             StockDocumentLinesInfo i = m_rows.get(row);
            if (column == 2) {
                i.setPhysicalQty(Double.parseDouble(o.toString()));
                //return
            }else{
                i.setRemarks(o.toString());
            }
        }

       // @Override
        public boolean isCellEditable(int row, int column) {
            boolean retVal = false;
            try {
                 
                if (column == 2 || column == 5) {
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

        public List<StockDocumentLinesInfo> getLines() {
            return m_rows;
        }

        public StockDocumentLinesInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, StockDocumentLinesInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(StockDocumentLinesInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, StockDocumentLinesInfo oLine) {

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
private static class InventoryDocumentModel extends AbstractTableModel {

        public ArrayList<StockDetailsInfo> m_rows = new ArrayList<StockDetailsInfo>();

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
          //  System.out.println ("enetr---getValueAt");
            StockDetailsInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getProductName() );
                case 1:
                    return  Formats.DOUBLE.formatValue(i.getUnits());
                case 2:
                    return  i.getPhyQty();
                case 3:
                     return null;
                case 4:
                     return null;
                case 5:
                    return i.getRemarks();
                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
             StockDetailsInfo i = m_rows.get(row);
            if (column == 2) {
             i.setPhyQty(o.toString());
  
            }else{
              i.setRemarks(o.toString());
            }
        }

       // @Override
        public boolean isCellEditable(int row, int column) {
            boolean retVal = false;
            try {
                if (column == 2 || column == 5) {
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

        public List<StockDetailsInfo> getLines() {
            return m_rows;
        }

        public StockDetailsInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, StockDetailsInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(StockDetailsInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, StockDetailsInfo oLine) {

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
   }

  private void showMessage(JStockReconciliation aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
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
                //aux.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
                   aux.setBackground(Color.white);
            }
            return aux;
        }
    }

      private void initStockData(String query) {


        stockDetails = new String[]{
            "Item", "System Qty", "Physical Qty", "Variance Volume","Variance Value", "Remarks"
        };
         stockList = m_dlStock.getStockInfo(query);
         stockSize = stockList.size();
         setStockTableModelAndHeader(m_jStocktable, stockSize);
         setStockTableData(m_jStocktable);
         setRows(stockSize);

    }

     private void setStockTableModelAndHeader(JTable table, int size) {
        table.getTableHeader().setPreferredSize(new Dimension(30, 25));
        table.setModel(new DefaultTableModel(stockDetails, size));



    }
     private void setStockTableData(JTable table) {

       for (int col = 0; col < stockSize; col++) {
            String productName = "<html>"+stockList.get(col).getProductName()+"<br /></html>";
            table.setValueAt(productName, col, 0);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(stockList.get(col).getUnits(), col, 1);
        }
        
        
    }
     private void setStockLinesTableData(JTable table) {

        for (int col = 0; col < stockSize; col++) {
            String productName = "<html>"+stockDocumentLinesList.get(col).getProductName()+"<br /></html>";
            table.setValueAt(productName, col, 0);
           
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(stockDocumentLinesList.get(col).getSystemQty(), col, 1);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(stockDocumentLinesList.get(col).getPhysicalQty(), col, 2);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(stockDocumentLinesList.get(col).getVariance(), col, 3);
        }
        for (int col = 0; col < stockSize; col++) {
             table.setValueAt(stockDocumentLinesList.get(col).getPrice(), col, 4);
        }
        for (int col = 0; col < stockSize; col++) {
            table.setValueAt(stockDocumentLinesList.get(col).getRemarks(), col, 5);
        }

    }

 private void populateDropDown() {
    
        try {
            locations = m_dlStock.getLocationsList();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.StockReconciliation");
    }

    public void activate() throws BasicException {
       Date sysDate = new Date();
       SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm");
       String currentDate = sdf.format(sysDate);
       m_jTxtDate.setText(currentDate);
       initDocumentcombo();
       populateDropDown();
       initData();
       int inventoyCount = 0;
        try {
            inventoyCount = m_dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
       if(inventoyCount==1){
            m_jstart.setEnabled(true);
       }else{
          m_jstart.setEnabled(false);
       }
       m_jstop.setEnabled(true);
     //  m_jstart.setEnabled(false);
       m_jBtnNew.setEnabled(false);
       m_jBtnReconcile.setEnabled(false);
       m_jBtnPrint.setEnabled(false);
       m_jBtnSave.setEnabled(false);
       m_jCalculateVariance.setEnabled(false);

    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jTxtDate = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jStocktable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        m_jLblDocument = new javax.swing.JLabel();
        m_jDocumentNo = new javax.swing.JComboBox();
        m_jTxtDocument = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        m_jBtnNew = new javax.swing.JButton();
        m_jBtnSave = new javax.swing.JButton();
        m_jCalculateVariance = new javax.swing.JButton();
        m_jBtnPrint = new javax.swing.JButton();
        m_jBtnReconcile = new javax.swing.JButton();
        m_jstop = new javax.swing.JButton();
        m_jstart = new javax.swing.JButton();

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(153, 153, 153)));

        jLabel3.setText("Date");

        m_jTxtDate.setEditable(false);

        m_jStocktable.setAutoCreateColumnsFromModel(false);
        m_jStocktable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "System Qty", "Physical Qty", "Variance", "Price", "Remarks"
            }
        )
        {public boolean isCellEditable(int row, int column){return false;}}
    );
    m_jStocktable.setRowHeight(75);
    jScrollPane1.setViewportView(m_jStocktable);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24)
                    .addComponent(m_jTxtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(m_jTxtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(40, 40, 40)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
    jPanel1.setPreferredSize(new java.awt.Dimension(700, 59));
    jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    m_jLblDocument.setText("Document No");
    jPanel1.add(m_jLblDocument, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 28, 85, -1));

    jPanel1.add(m_jDocumentNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 25, 134, -1));

    m_jTxtDocument.setEditable(false);
    jPanel1.add(m_jTxtDocument, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 25, 134, -1));

    jButton1.setBackground(new java.awt.Color(255, 255, 255));
    jButton1.setText("Execute");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 24, -1, 20));

    m_jBtnNew.setBackground(new java.awt.Color(255, 255, 255));
    m_jBtnNew.setText("New");
    m_jBtnNew.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jBtnNewActionPerformed(evt);
        }
    });

    m_jBtnSave.setBackground(new java.awt.Color(255, 255, 255));
    m_jBtnSave.setText("Save");
    m_jBtnSave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jBtnSaveActionPerformed(evt);
        }
    });

    m_jCalculateVariance.setBackground(new java.awt.Color(255, 255, 255));
    m_jCalculateVariance.setText("Calculate Variance");
    m_jCalculateVariance.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jCalculateVarianceActionPerformed(evt);
        }
    });

    m_jBtnPrint.setBackground(new java.awt.Color(255, 255, 255));
    m_jBtnPrint.setText("Print");
    m_jBtnPrint.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jBtnPrintActionPerformed(evt);
        }
    });

    m_jBtnReconcile.setBackground(new java.awt.Color(255, 255, 255));
    m_jBtnReconcile.setText("Reconcile");
    m_jBtnReconcile.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jBtnReconcileActionPerformed(evt);
        }
    });

    m_jstop.setBackground(new java.awt.Color(255, 255, 255));
    m_jstop.setText("Stop Inventory");
    m_jstop.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jstopActionPerformed(evt);
        }
    });

    m_jstart.setBackground(new java.awt.Color(255, 255, 255));
    m_jstart.setText("Start Inventory");
    m_jstart.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            m_jstartActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap(18, Short.MAX_VALUE)
            .addComponent(m_jstart)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jstop)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jBtnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jBtnSave)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jCalculateVariance)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jBtnPrint)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(m_jBtnReconcile)
            .addContainerGap())
        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(19, 19, 19)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(m_jBtnPrint)
                .addComponent(m_jCalculateVariance)
                .addComponent(m_jBtnReconcile)
                .addComponent(m_jBtnSave)
                .addComponent(m_jBtnNew)
                .addComponent(m_jstop)
                .addComponent(m_jstart))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {m_jBtnNew, m_jstart, m_jstop});

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(39, 39, 39)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(34, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(21, Short.MAX_VALUE))
    );
    }// </editor-fold>//GEN-END:initComponents
private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    private void m_jCalculateVarianceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCalculateVarianceActionPerformed
  //  jTxtTotal.setText("");
    double subTotal = 0;
    String strCount = null;
    double value = 0;
    double physicalQty = 0;// = Double.parseDouble(m_jStocktable.getValueAt(r, 2).toString());
    String documentNo;
    String remarks;
    double variance;
    double price;
      try{
       documentNo= m_jDocumentNo.getSelectedItem().toString();
      }catch(NullPointerException ex){

          documentNo = m_jTxtDocument.getText();
      }


       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

       Date stockDate = null;
       String date = m_jTxtDate.getText();
        try {
            stockDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
          double physicalQtyValue;
        String id;
          for (int r = 0; r < getRows(); r++) {
            String item = m_jStocktable.getValueAt(r, 0).toString();
            if(setFlag==0){
                id = stockList.get(r).getProductId();
            }else{
                id = stockDocumentLinesList.get(r).getProductId();
            }
            double systemQty = Double.parseDouble(m_jStocktable.getValueAt(r, 1).toString());
           try{

               strCount = m_jStocktable.getValueAt(r, 2).toString();
               physicalQtyValue = Double.parseDouble(m_jStocktable.getValueAt(r, 2).toString());
           }catch(NullPointerException ex){
               physicalQtyValue = 0;
           }

        double priceValue = 0;
        stockList =  m_dlStock.getStockDetails();
        double itemprice = stockList.get(r).getPrice();
        variance = systemQty - physicalQtyValue;
    if(variance>0){
            priceValue = itemprice * variance;
        }else{
            priceValue = 0;
        }
          try{
                remarks = m_jStocktable.getValueAt(r, 5).toString();
           }catch(NullPointerException ex){
               remarks = "";
            }
             
              dTable.add(new StockReconciliationInfo(item, systemQty, physicalQtyValue,variance,remarks,priceValue,id));
          
          }
         try {
            m_dlStock.insertStockReconciliation(dTable, documentNo,stockDate); // TODO add your handling code here:
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        //initData();
         //  m_jDocumentNo.setVisible(true);
           initDocumentcombo();
           m_jDocumentNo.setVisible(true);

           m_jDocumentNo.setSelectedItem(documentNo);

          initDocumentData();

           m_jBtnPrint.setEnabled(true);
           m_jBtnReconcile.setEnabled(true);
          
    }//GEN-LAST:event_m_jCalculateVarianceActionPerformed

    private void m_jBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnSaveActionPerformed
      String  categoryName;
      String wareHouse;
      String documentNo;
       String remarks;
      double variance;
      double price;
   //    dTable.clear();
      //String documentNo =  setDocumentNo();
      try{
       documentNo= m_jDocumentNo.getSelectedItem().toString();
      }catch(NullPointerException ex){

          documentNo = m_jTxtDocument.getText();
      }
     
   
       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

       Date stockDate = null;
       String date = m_jTxtDate.getText();
        try {
            stockDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
          double physicalQtyValue;
        String id;
          for (int r = 0; r < getRows(); r++) {
            String item = m_jStocktable.getValueAt(r, 0).toString();
            if(setFlag==0){
                id = stockList.get(r).getProductId();
            }else{
                id = stockDocumentLinesList.get(r).getProductId();
            }
            double systemQty = Double.parseDouble(m_jStocktable.getValueAt(r, 1).toString());
           try{
                physicalQtyValue = Double.parseDouble(m_jStocktable.getValueAt(r, 2).toString());
           }catch(NullPointerException ex){
               physicalQtyValue = 0;
           }
       
         try{
                variance = Double.parseDouble(m_jStocktable.getValueAt(r, 3).toString());
           }catch(NullPointerException ex){
               variance = 0;
           }
            try{
                price = Double.parseDouble(m_jStocktable.getValueAt(r, 4).toString());
           }catch(NullPointerException ex){
               price = 0;
           }

              try{
                remarks = m_jStocktable.getValueAt(r, 5).toString();
           }catch(NullPointerException ex){
               remarks = "";
           }
              dTable.add(new StockReconciliationInfo(item, systemQty, physicalQtyValue,variance,remarks,price,id));

    }
        try {
            m_dlStock.insertStockReconciliation(dTable, documentNo,stockDate); // TODO add your handling code here:
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
           initData();
           m_jDocumentNo.setVisible(true);
           initDocumentcombo();
           m_jTxtDocument.setVisible(false);      
          
           m_jBtnPrint.setEnabled(true);
          
    }//GEN-LAST:event_m_jBtnSaveActionPerformed
public void saveData(){

      String documentNo;
       String remarks;
      double variance;
      double price;
      //String documentNo =  setDocumentNo();
      try{
       documentNo= m_jDocumentNo.getSelectedItem().toString();
      }catch(NullPointerException ex){

          documentNo = m_jTxtDocument.getText();
      }


       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

       Date stockDate = null;
       String date = m_jTxtDate.getText();
        try {
            stockDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
          double physicalQtyValue;
        String id;
          for (int r = 0; r < getRows(); r++) {
            String item = m_jStocktable.getValueAt(r, 0).toString();
            if(setFlag==0){
                id = stockList.get(r).getProductId();
            }else{
                id = stockDocumentLinesList.get(r).getProductId();
            }
            double systemQty = Double.parseDouble(m_jStocktable.getValueAt(r, 1).toString());
           try{
                physicalQtyValue = Double.parseDouble(m_jStocktable.getValueAt(r, 2).toString());
           }catch(NullPointerException ex){
               physicalQtyValue = 0;
           }

         try{
                variance = Double.parseDouble(m_jStocktable.getValueAt(r, 3).toString());
           }catch(NullPointerException ex){
               variance = 0;
           }
            try{
                price = Double.parseDouble(m_jStocktable.getValueAt(r, 4).toString());
           }catch(NullPointerException ex){
               price = 0;
           }

              try{
                remarks = m_jStocktable.getValueAt(r, 5).toString();
           }catch(NullPointerException ex){
               remarks = "";
           }
              System.out.println("id---"+id);

              dTable.add(new StockReconciliationInfo(item, systemQty, physicalQtyValue,variance,remarks,price,id));

    }
        try {
            m_dlStock.insertStockReconciliation(dTable, documentNo,stockDate); // TODO add your handling code here:
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
           initData();
           m_jDocumentNo.setVisible(true);
           initDocumentcombo();
           m_jTxtDocument.setVisible(false);

           m_jBtnPrint.setEnabled(true);
           dTable.clear();
}
    private void m_jBtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnNewActionPerformed
        m_jDocumentNo.setVisible(false);
        m_jTxtDocument.setVisible(true);
        String documentNo = setDocumentNo();
        m_jTxtDocument.setText(documentNo);
        initTableData();
        m_jstart.setEnabled(false);
        m_jBtnReconcile.setEnabled(false);
        m_jBtnPrint.setEnabled(false);
        m_jBtnSave.setEnabled(true);
        
        setFlag=0;
   
    }//GEN-LAST:event_m_jBtnNewActionPerformed

    private void m_jBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnPrintActionPerformed
     StockHeaderInfo stock = new StockHeaderInfo();
     String documentNo;
     try{
         documentNo = m_jDocumentNo.getSelectedItem().toString();
     }catch(NullPointerException ex){
         documentNo = m_jTxtDocument.getText();
     }
     List<StockDocumentLinesInfo> lines =  m_dlStock.getStockLinesInfo(documentNo);
     stock.setLines(lines);
     printReport("/com/openbravo/reports/stockReconciliation", stock);// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnPrintActionPerformed

    private void m_jBtnReconcileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnReconcileActionPerformed
        double physicalQtyValue;
        String remarks;
        double variance;
        double price;

          for (int r = 0; r < getRows(); r++) {
            String item = m_jStocktable.getValueAt(r, 0).toString();
            String id = stockDocumentLinesList.get(r).getProductId();
            double systemQty = Double.parseDouble(m_jStocktable.getValueAt(r, 1).toString());
           try{
                physicalQtyValue = Double.parseDouble(m_jStocktable.getValueAt(r, 2).toString());
           }catch(NullPointerException ex){
               physicalQtyValue = 0;
           }
       
         try{
                variance = Double.parseDouble(m_jStocktable.getValueAt(r, 3).toString());
           }catch(NullPointerException ex){
               variance = 0;
           }
             try{
                price = Double.parseDouble(m_jStocktable.getValueAt(r, 4).toString());
           }catch(NullPointerException ex){
               price = 0;
           }
       try{
            remarks = m_jStocktable.getValueAt(r, 5).toString();
       }catch(NullPointerException ex){
           remarks = "";
       }
        stockReconcile.add(new StockReconciliationInfo(item, systemQty, physicalQtyValue,variance,remarks,price,id));
          }
        String wareHouse = null;
        try {

            wareHouse = m_dlStock.getWarehouse();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        String documentNo = m_jDocumentNo.getSelectedItem().toString();
        
        try {
           //  m_dlStock.updateReconcile(documentNo);
            m_dlStock.updateStockReconciliation(stockReconcile, wareHouse); // TODO add your handling code here:
           
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
           new StaticSentence(m_App.getSession(), "UPDATE STOCKRECONCILIATION SET RECONCILE = 'Y' WHERE DOCUMENTNO=? ", new SerializerWriteBasic(new Datas[]{Datas.STRING})).exec(new Object[]{documentNo});
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        initDocumentcombo();
        initData();
        m_jstart.setEnabled(true);
        m_jBtnReconcile.setEnabled(false);
        m_jBtnPrint.setEnabled(false);
         m_jBtnSave.setEnabled(false);
        m_jCalculateVariance.setEnabled(false);
        m_jstop.setEnabled(false);
        m_jBtnNew.setEnabled(false);
    }//GEN-LAST:event_m_jBtnReconcileActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        initDocumentData();// TODO add your handling code here:
       
        setFlag = 1;
}//GEN-LAST:event_jButton1ActionPerformed

    private void m_jstartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jstartActionPerformed
        try {
            dlCustomers.updateInventory("N");
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jstop.setEnabled(true);
        m_jstart.setEnabled(false);
    }//GEN-LAST:event_m_jstartActionPerformed

    private void m_jstopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jstopActionPerformed
        try {
           
        int stockcount = dlCustomers.getConsiliationCount();
           ///int i= inventoryid.get(0).getCount();
           if(stockcount==0)
           {
               dlCustomers.insertConsiliation(UUID.randomUUID().toString(), "N");
           }
           else
           {
              dlCustomers.updateInventory("Y");
           }
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jstop.setEnabled(false);
        m_jBtnNew.setEnabled(true);

    }//GEN-LAST:event_m_jstopActionPerformed
   private void printReport(String resourcefile, StockHeaderInfo stock) {

        String documentNo;
        try{
            documentNo = m_jDocumentNo.getSelectedItem().toString();
         }catch(NullPointerException ex){
             documentNo = m_jTxtDocument.getText();
         }
        java.util.List<StockHeaderInfo> stockValueList =  new java.util.ArrayList<StockHeaderInfo>();

         stockHeaderList = m_dlStock.getStockHeaderCategoryInfo(documentNo);

         stockValueList = stockHeaderList;
        try {

            JasperReport jr = null;

            InputStream in = (InputStream) getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
                jr = JasperCompileManager.compileReport(jd);
            } else {
                // read the compiled reporte
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }
            // Construyo el mapa de los parametros.
            Map reportparams = new HashMap();
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }
       
            Map reportfields = new HashMap();

            reportfields.put("stockData", stockValueList.get(0));
            reportfields.put("StockHeader", stock);

            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[] { reportfields } ));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printer.2"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }

    }
   
 private String setDocumentNo(){
            int documentNo = 0;
        try {
            documentNo = m_dlStock.getDocumentNo();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(documentNo==0){
            documentNo = 100001;
        }else{
            documentNo = documentNo+1;
        }

        return (Integer.toString(documentNo));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jBtnNew;
    private javax.swing.JButton m_jBtnPrint;
    private javax.swing.JButton m_jBtnReconcile;
    private javax.swing.JButton m_jBtnSave;
    private javax.swing.JButton m_jCalculateVariance;
    private javax.swing.JComboBox m_jDocumentNo;
    private javax.swing.JLabel m_jLblDocument;
    private static javax.swing.JTable m_jStocktable;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JTextField m_jTxtDocument;
    private javax.swing.JButton m_jstart;
    private javax.swing.JButton m_jstop;
    // End of variables declaration//GEN-END:variables
    
}
