//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright(C) 2007-2009 Openbravo, S.L.
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

import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.inventory.InventoryRecord;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.sales.ProcessInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * @author mateen
 */
public class JPurchaseInvoice extends JPanel implements JPanelView {
    private static Double totalDiscount;

    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicSales m_dlSales;
    private TicketParser m_TTP;
    private SentenceList m_sentlocations;
    private ComboBoxValModel m_LocationsModel;
    private ComboBoxValModel m_LocationsModelDes;
    private com.sysfore.pos.purchaseorder.JPurchaseInvoiceLines m_polines;
    private DataLogicCustomers dlCustomers;
    private SentenceList taxListSentance;
    private static PurchaseOrderReceipts m_dlPOReceipts;
    List<PurchaseChargesInfo> dTable = new ArrayList<PurchaseChargesInfo>();
     List<PurchaseChargesInfo> dChargeTable = new ArrayList<PurchaseChargesInfo>();
    private java.util.List<Chargesinfo> chargeList = null;
    private java.util.List<PurchaseChargesInfo> purchaseChargeList = null;
    private java.util.List<ProductInfoExt> lines = null;
    private String documentNo;
    private static Double productTotalTaxes = new Double(0.0);
    private static Double productSubTotal;
    private static Double productTotal;
    private static Double chargesTotal;
    private static Double oldchargesTotal;
    private static String id;
    private double discountTotal;
    private java.util.List<PurchaseInvoiceInfo> list;
    private java.util.List<VendorInfo> vendorLines;
    private static boolean changesmade;
    private InventoryTableModel m_inventorylines;
    private ChargesTableModel m_Chargeslines;
    private static boolean insert;
     private int chargeSize = 0;
    private boolean itemDeleted;
    private int productMatchIndex;
    private static String currentDocNo;
    private List<PurchaseInvoiceInfo> PurchaseInvoicelist;
    private String created=null;
    private String selectedPOStatus=null;
    private SimpleDateFormat sdf=null; 
     private String GRNid=null;
     private String[] chargeDetails;
    private int rows;
    private static String totalTax;
    static int setFlag=0;
    private String productTaxid;
    /**
     * Creates new form JPurchaseOrder
     */
    public JPurchaseInvoice(AppView app) {

        m_App = app;
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlPOReceipts = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        initComponents();
        txtStatus.setEnabled(false);
        m_sentlocations = m_dlSales.getLocationsList();
        taxListSentance = m_dlSales.getTaxList();
        m_LocationsModel = new ComboBoxValModel();
        m_LocationsModelDes = new ComboBoxValModel();
        m_polines = new JPurchaseInvoiceLines();
        jPanel5.add(m_polines, BorderLayout.CENTER);
        setDocumentNo(generateDocumentNoFromDB());
        populateVendors();
        initFormValues();
        try {
            setJListContents();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        lines = new ArrayList<ProductInfoExt>();
        setInsert(true);
         DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 240, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Charges");
        columns.addColumn(c);
        c = new TableColumn(1, 90, new DataCellRenderer(javax.swing.SwingConstants.CENTER), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Amount");
        columns.addColumn(c);
       // m_jChargesTable
        m_jChargesTable.setColumnModel(columns);

        m_jChargesTable.getTableHeader().setReorderingAllowed(false);
        m_jChargesTable.setRowHeight(20);
        m_jChargesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_jChargesTable.setIntercellSpacing(new java.awt.Dimension(0, 1));

        
      ///  setPurchaseCount();
    }

    
     private class InventoryTableModel extends AbstractTableModel {

        public ArrayList<Chargesinfo> m_rows = new ArrayList<Chargesinfo>();

        public int getRowCount() {
             //System.out.println ("enetr---getRowCount"+m_rows.size());
            return m_rows.size();
        }

        public int getColumnCount() {
           // System.out.println ("enetr---getColumnCount");
            return 2;
        }

        public String getColumnName(int column) {
            //return AppLocal.getIntString(m_acolumns[column].name);
            return null;
        }

        public Object getValueAt(int row, int column) {
          //  System.out.println ("enetr---getValueAt");
            Chargesinfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getName() );
                case 1:
                    return i.getAmount();

                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
            System.out.println("setValueAt--");
             Chargesinfo i = m_rows.get(row);
             double price = 0;
             setFlag = 0;
            if (column == 1) {

//                System.out.println("i.get--"+i.getAmount());
//                try{
//                    setoldChargesTotal(Double.parseDouble(i.getAmount()));
//                }catch(NullPointerException ex){
//                    setoldChargesTotal(0.0);
//                }
               
              i.setAmount(o.toString());
              setValue();
                System.out.println("getAmount---"+i.getAmount());
                
                  //  price =  price+ Double.parseDouble(i.getAmount());
//                if(!i.getAmount().isEmpty()){
//                    setChargesTotal(Double.parseDouble(i.getAmount()));
//                }
//                   String total = (m_jTotalEuros1.getText()).replaceAll("Rs. ", "");
//                   total = total.replaceAll(",", "");
//                    double piTotal = Double.parseDouble(total);
//                   piTotal = Double.parseDouble(Formats.DoubleValue.formatValue(piTotal));
//                   setTotals(piTotal, getProductSubTotal(), getProductTotalTaxes());
//               // System.out.println("pruice==="+piTotal);

                //return
            }else{
               // i.setRemarks(o.toString());
            }
        }


       // @Override
        public boolean isCellEditable(int row, int column) {
            boolean retVal = false;
            try {

                if (column == 1 ) {
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

        public List<Chargesinfo> getLines() {
            return m_rows;
        }

        public Chargesinfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, Chargesinfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(Chargesinfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, Chargesinfo oLine) {

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
     private class ChargesTableModel extends AbstractTableModel {

        public ArrayList<PurchaseChargesInfo> m_rows = new ArrayList<PurchaseChargesInfo>();

        public int getRowCount() {
             //System.out.println ("enetr---getRowCount"+m_rows.size());
            return m_rows.size();
        }

        public int getColumnCount() {
           // System.out.println ("enetr---getColumnCount");
            return 2;
        }

        public String getColumnName(int column) {
            //return AppLocal.getIntString(m_acolumns[column].name);
            return null;
        }

        public Object getValueAt(int row, int column) {
          //  System.out.println ("enetr---getValueAt");


            PurchaseChargesInfo i = m_rows.get(row);
            switch (column) {
                case 0:
                    return  (i.getName() );
                case 1:
                    return i.getAmount();

                default:
                    return null;
            }

        }

        @Override
        public void setValueAt(Object o, int row, int column) {
             PurchaseChargesInfo i = m_rows.get(row);
             double price = 0;
            if (column == 1) {
               
//                setoldChargesTotal(i.getAmount());
                String chargeAmount = o.toString();
                double amount;
                if(chargeAmount.isEmpty()){
                    amount = 0;
                }else{
                    amount = Double.parseDouble(o.toString());
                }
               i.setAmount(amount);
                setValue();
//                    setChargesTotal(i.getAmount());
//                    setFlag = 1;
//                    String total = (m_jTotalEuros1.getText()).replaceAll("Rs. ", "");
//                    total = total.replaceAll(",", "");
//                    double piTotal = Double.parseDouble(total);
//                   setTotals(piTotal, getProductSubTotal(), getProductTotalTaxes());
               // System.out.println("pruice==="+piTotal);ivate

                //return
            }else{
               // i.setRemarks(o.toString());
            }
        }


       // @Override
        public boolean isCellEditable(int row, int column) {
            boolean retVal = false;
            try {

                if (column == 1 ) {
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

        public List<PurchaseChargesInfo> getLines() {
            return m_rows;
        }

        public PurchaseChargesInfo getRow(int index) {
            return m_rows.get(index);
        }

        public void setRow(int index, PurchaseChargesInfo oLine) {

            m_rows.set(index, oLine);
            fireTableRowsUpdated(index, index);
        }

        public void addRow(PurchaseChargesInfo oLine) {

            insertRow(m_rows.size(), oLine);
        }

        public void insertRow(int index, PurchaseChargesInfo oLine) {

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
     public void addLine(Chargesinfo i) {
        m_inventorylines.addRow(i);
        setSelectedIndex(m_inventorylines.getRowCount() - 1);
    }
       public void addLine(PurchaseChargesInfo i) {
        m_Chargeslines.addRow(i);
        dTable.add(new PurchaseChargesInfo(i.getId(),i.getName(),i.getAmount()));
        if(i.getAmount()!=0){
            dChargeTable.add(new PurchaseChargesInfo(i.getId(),i.getName(),i.getAmount()));
        }

        setSelectedIndex(m_Chargeslines.getRowCount() - 1);
    }
     public void setSelectedIndex(int i) {

        // Seleccionamos
        m_jChargesTable.getSelectionModel().setSelectionInterval(i, i);

        // Hacemos visible la seleccion.
        Rectangle oRect = m_jChargesTable.getCellRect(i, 0, true);
        m_jChargesTable.scrollRectToVisible(oRect);
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
    private void clearAddresses() {
        txtBillAdd.removeAllItems();
        txtShipAdd.removeAllItems();
    }

    private static void deletePurchaseOrderLine(PurchaseInvoiceLine line) {

        try {
            m_dlPOReceipts.deletePurchaseInvoiceLinesByProduct(getId(), line.getProductID());
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChangesmade(true);
        //vendorAction();
    }

    private String getTodaysDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd/MM/yyyy");
            strDate = format.format(date);

        }
        return strDate;
    }

    private void populateVendors() {

        try {
            vendorLines = dlCustomers.getVendorList();
            txtVendor.addItem("");
            for (int i = 0; i < vendorLines.size(); i++) {
                txtVendor.addItem(vendorLines.get(i).getName());
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.PI");
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void activate() throws BasicException {
        //m_cat.loadCatalog();
        java.util.List l = m_sentlocations.list();
        m_LocationsModel = new ComboBoxValModel(l);
        //m_jLocation.setModel(m_LocationsModel); // para que lo refresque
        m_LocationsModelDes = new ComboBoxValModel(l);
        //m_jLocationDes.setModel(m_LocationsModelDes); // para que lo refresque
        stateToInsert();
        txtVendor.removeAllItems();
        populateVendors();
         setChargesTotal(0.0);
        setTotals(0, 0, 0);
        //        initChargesDetails();
//        dTable.clear();
//         setChargesTotal(0.0);
//            setoldChargesTotal(0.0);

        jButton1.setVisible(false);
       setTotalDiscount(0.0);
        jListDocumentNo.setSelectedIndex(-1);

        txtStatus.setEnabled(false);
        m_sentlocations = m_dlSales.getLocationsList();
        taxListSentance = m_dlSales.getTaxList();
        m_LocationsModel = new ComboBoxValModel();
        m_LocationsModelDes = new ComboBoxValModel();
//        m_polines = new JPurchaseInvoiceLines();
//        jPanel5.add(m_polines, BorderLayout.CENTER);
        setDocumentNo(generateDocumentNoFromDB());
     //   populateVendors();
        initFormValues();
        try {
            setJListContents();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        lines = new ArrayList<ProductInfoExt>();
        setInsert(true);
     //   setPurchaseCount();
    }

public void setPurchaseCount(){
    
    int processCount = 0;
     processCount = ProcessInfo.setProcessCount("Purchase Invoice", m_dlSales);
        if(processCount>=10){
            btnSavePI.setEnabled(false);
            btnCompletePI.setEnabled(false);
            jbtnDelete.setEnabled(false);
            m_jClose.setEnabled(false);
//            jLabel1.setVisible(true);
     //       jLabel1.setText("This feature is available for Professional edition.To continue using the same kindly upgrade to Professional Edition.");
             
        }
}
    public void stateToInsert() {
        m_LocationsModel.setSelectedKey(m_App.getInventoryLocation());
        m_LocationsModelDes.setSelectedKey(m_App.getInventoryLocation());
        m_polines.clear();
    }

    public boolean deactivate() {
        boolean retVal = false;
        if (isChangesmade()) {
            int res = JOptionPane.showConfirmDialog(this, "Changes has been made to Current PI, Do you want to save changes");
            if (res == 0) {
                retVal = saveData();
                setChangesmade(false);
            } else if (res == 1) {
                retVal = true;
                setChangesmade(false);
            } else {
                retVal = false;
            }
        } else {
            retVal = true;
        }

        return retVal;
    }

    private void deleteLine(int index) {
        if (index < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            m_polines.deleteLine(index);

            setTotalTxtValuesWithoutClearingAddresses();

        }
    }

    private void setStatusComboModel(JComboBox txtStatus) {
        ComboBoxValModel model = new ComboBoxValModel();
        model.add("Draft");
        model.add("Complete");
        model.setSelectedFirst();
        txtStatus.setModel(model);
    }

    private void initFormValues() {
        txtOrderDate.setText(getTodaysDate(new Date()));
        txtDeliveryDate.setText(getTodaysDate(new Date()));
        setStatusComboModel(txtStatus);
        txtDocumentNo.setText(getDocumentNo());
        txtVendor.setSelectedIndex(0);
        btnSavePI.setEnabled(true);
        btnCompletePI.setEnabled(false);
       // setPurchaseCount();
        initChargesDetails();
        dTable.clear();
        dChargeTable.clear();
        clearProducts();
        setTxtFieldsZero();
        disableSearchDeleteButton(true);
        m_jClose.setEnabled(false);
        setChangesmade(false);
        setInsert(true);
        setoldChargesTotal(0.0);
        setChargesTotal(0.0);
        m_jChargesTable.setEnabled(true);
        setTotalDiscount(0.0);
   
    }
    public void initChargesDetails(){
             chargeDetails = new String[]{
           "Charges", "Amount" };
        try {
            chargeList = m_dlPOReceipts.getChargesList();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargeSize = chargeList.size();
         setStockTableModelAndHeader(m_jChargesTable, chargeSize);
         setRows(chargeSize);
     //    setStockTableData(m_jChargesTable);
         m_inventorylines = new InventoryTableModel();
         m_jChargesTable.setModel(m_inventorylines);
         for(int i =0;i<chargeList.size();i++){
            addLine(new Chargesinfo(chargeList.get(i)));
         }

    }
    public void initCharges(String id){
        chargeDetails = new String[]{
           "Charges", "Amount" };
        int count = 0;
        try {
            count = m_dlPOReceipts.getDocumentNoCount(id);
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(count==0){
            try {
            chargeList = m_dlPOReceipts.getChargesList();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargeSize = chargeList.size();
         setStockTableModelAndHeader(m_jChargesTable, chargeSize);
         setRows(chargeSize);
     //    setStockTableData(m_jChargesTable);
         m_inventorylines = new InventoryTableModel();
         m_jChargesTable.setModel(m_inventorylines);
         for(int i =0;i<chargeList.size();i++){
            addLine(new Chargesinfo(chargeList.get(i)));
         }
        }else{
             purchaseChargeList = m_dlPOReceipts.getPurchaseChargesLines(id);

        chargeSize = purchaseChargeList.size();
         setStockTableModelAndHeader(m_jChargesTable, chargeSize);
         setRows(chargeSize);
       //  setStockTableData(m_jChargesTable);
         m_Chargeslines = new ChargesTableModel();
         m_jChargesTable.setModel(m_Chargeslines);
         for(int i =0;i<purchaseChargeList.size();i++){
            addLine(new PurchaseChargesInfo(purchaseChargeList.get(i)));
             
         }
        }
        setoldChargesTotal(0.0);
           
    }
    private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    private void setStockTableModelAndHeader(JTable table, int size) {
        table.getTableHeader().setPreferredSize(new Dimension(30, 25));
        table.setModel(new DefaultTableModel(chargeDetails, size));



    }
   private void setStockTableData(JTable table) {

//        for (int col = 0; col < stockSize; col++) {
//
//            table.setValueAt(col+1, col, 0);
//        }
        for (int col = 0; col < chargeSize; col++) {
            String chargeName = purchaseChargeList.get(col).getName();
            table.setValueAt(chargeName, col, 0);
        }
        for (int col = 0; col < chargeSize; col++) {
            table.setValueAt(purchaseChargeList.get(col).getAmount(), col, 1);
        }


    }
   public String getPdtTaxid(){
       return productTaxid;
   }
   public void setPdtTaxid(String taxid){
       this.productTaxid = taxid;
   }
    public Double getTaxInfo(String prodTaxCatId) {

        java.util.List<TaxInfo> tInfo = null;
        double retVal;
        try {
            tInfo = m_dlPOReceipts.getTaxListbyProduct(prodTaxCatId).list();
            // tInfo = m_dlPOReceipts.getTaxRateByCategoryId(prodTaxCatId);
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tInfo.size() > 0) {
            retVal = tInfo.get(0).getRate();
            setPdtTaxid(tInfo.get(0).getId());
        } else {
            retVal = 0.0;
            String taxId = null;
            try {
                taxId = m_dlSales.getTaxId();
            } catch (BasicException ex) {
                Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
            setPdtTaxid(taxId);
        }
        return retVal;
    }


    public TaxInfo getTaxRate(String prodTaxCatId) {

        java.util.List<TaxInfo> tInfo = null;
        double retVal;
        try {
            tInfo = m_dlPOReceipts.getTaxListbyProduct(prodTaxCatId).list();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (TaxInfo) tInfo.get(0);
    }
public void setChargesValue(String piId){
    String chargeId;
    String name;
    double amount;
    dTable.clear();
    dChargeTable.clear();;
    for (int r = 0; r < getRows(); r++) {
             name = m_jChargesTable.getValueAt(r, 0).toString();
             chargeId = chargeList.get(r).getId();
             try{
                 amount = Double.parseDouble(m_jChargesTable.getValueAt(r, 1).toString());
             }catch(Exception ex){
                amount=0;
             }
             
              dTable.add(new PurchaseChargesInfo(chargeId, name, amount));
              if(amount!=0){
                  dChargeTable.add(new PurchaseChargesInfo(chargeId, name, amount));
              }
    }
      try {
                m_dlPOReceipts.deletePurchaseInvoiceCharges(id);
            } catch (BasicException ex) {
                Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
    try {
        m_dlPOReceipts.insertPurchaseCharges(dTable, piId);
    } catch (BasicException ex) {
        Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
    }



}
    private void purchaseOrderActionSave() throws BasicException {

        int index = jListDocumentNo.getSelectedIndex();
        String status = txtStatus.getSelectedItem().toString();
        String piId;
        if (status.equals("Draft")) {
            String id1 = UUID.randomUUID().toString();
            setId(id1);
            setDocumentNo(txtDocumentNo.getText());
            String vendor = vendorLines.get(txtVendor.getSelectedIndex() - 1).getId();
            String dateCreate = txtOrderDate.getText();
            Date dateCreate1 = getFormattedDate(dateCreate);

            String dateDelivered = txtDeliveryDate.getText();
            Date dateDelivered1 = getFormattedDate(dateDelivered);

            String total = m_jTotalEuros1.getText();
            Double dTotal = (Double) Formats.CURRENCY.parseValue(total);

            String subTotal = m_jSubtotalEuros1.getText();
            Double dSubTotal = (Double) Formats.CURRENCY.parseValue(subTotal);
            String tax = m_jTaxesEuros1.getText();
             Double dTax = (Double) Formats.CURRENCY.parseValue(tax);
             Double roundOff = dTotal - (dSubTotal+dTax+getChargesTotal()-getTotalDiscount());
            List<PurchaseInvoiceLine> lines1 = m_polines.getLines();
            if (isInsert()) {
                //insert
                String f_Poid = UUID.randomUUID().toString();
                f_Poid = f_Poid.replaceAll("-", "");
                piId = f_Poid;
                String user = m_App.getAppUserView().getUser().getId();
                m_dlPOReceipts.insertPurchaseInvoice(f_Poid, getDocumentNo(), vendor, dateCreate1, dateDelivered1, status, getTotalNotNull(dTotal), getSubTotalNotNull(dSubTotal), getTaxNotNull(tax), user,GRNid,getChargesTotal(),getTotalDiscount(),roundOff);
                enableBothButtons(true);
                     //   ProcessInfo.setProcessInfo("Purchase Invoice",m_dlSales);
                      //  setPurchaseCount();
                for (PurchaseInvoiceLine l : lines1) {
                    m_dlPOReceipts.insertPurchaseInvoiceLines2(f_Poid,l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid(),l.getDiscount());
                }
            } else {
                //update
                String purchaseOrderId = list.get(index).getId();
                piId = purchaseOrderId;
                System.out.println(index);
                m_dlPOReceipts.updatePI(purchaseOrderId, getDocumentNo(), vendor, dateCreate1, dateDelivered1, status, getTotalNotNull(dTotal), getSubTotalNotNull(dSubTotal), tax, getChargesTotal(),getTotalDiscount(),roundOff);
                for (PurchaseInvoiceLine l : lines1) {
                    // IF PO ALREADY CONTAINS THIS PRODUCT THEN UPDATE ELSE insertPOLines
                    java.util.List<PurchaseInvoiceProductInfo> infoLines = m_dlPOReceipts.getPILinesByPoidAndProduct(purchaseOrderId, l.getProductID());
                    if (infoLines.size() > 0) {
                        if (infoLines.get(0).getPoid() != null) {
                            m_dlPOReceipts.updatePurchaseInvoiceLines(purchaseOrderId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(),l.getDiscount());
                        }
                    } else {
                        m_dlPOReceipts.insertPurchaseInvoiceLines2(purchaseOrderId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid(),l.getDiscount());

                    }
                }
                setInsert(false);
            }
            setChargesValue(piId);
            setJListContents();
            setFlag = 0;
            setoldChargesTotal(0.0);
            setChargesTotal(0.0);
            jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
            
            txtVendor.setSelectedIndex(txtVendor.getSelectedIndex());
            //
            //resetForm();
            //setTotalSetters(0.0);
        }

    }

    private Date getFormattedDate(String strDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date newDate = new Date();
        try {
            newDate = (Date) format.parse(strDate);
        } catch (ParseException ex) {
        }
        return newDate;
    }

    private String getFormattedDate2(String strDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        try {
            d = (Date) format.parse(strDate);

        } catch (ParseException ex) {
        }
        return format.format(d);
    }

    private void resetForm() {

        int count = m_polines.getCount();
        for (; count > 0;) {
            m_polines.deleteLine(--count);
        }
        txtVendor.setSelectedIndex(0);
        txtStatus.setSelectedIndex(0);
        m_jTaxesEuros1.setText("");
        m_jSubtotalEuros1.setText("");
        m_jTotalEuros1.setText("");
        m_jDiscountTotal.setText("");
        setDocumentNo(generateDocumentNoFromDB());
        txtDocumentNo.setText(getDocumentNo());
        setId(null);
        setChangesmade(false);
        clearAddresses();
    }

    /**
     * @return the documentNo
     */
    public String getDocumentNo() {
        return documentNo;
    }

    /**
     * @param documentNo the documentNo to set
     */
    private void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    private String generateDocumentNoFromDB() throws NumberFormatException {
        String docNo = null;
        String[] ticketDocNoValue;
        int ticketDocNoInt;
        String ticketDocNo;
        String StoreName;
        try {
            docNo = m_dlPOReceipts.getDocumentPINo().list().get(0).toString();
            if (docNo == null || docNo.isEmpty() || docNo.equals("")) {
                docNo = "9999";
            }
            ticketDocNoValue = docNo.split("-");
            docNo=ticketDocNoValue[2];
        } catch (BasicException ex) {
            //Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            docNo = "9999";
            //Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        Integer docNoInt = Integer.parseInt(docNo);
        docNoInt = docNoInt + 1;
        StoreName = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo();
        return StoreName+"-"+docNoInt.toString();
    }

    private void purchaseOrderActionComplete() throws BasicException {

        int index = jListDocumentNo.getSelectedIndex();
        String status = "Complete";
        txtStatus.setSelectedItem(status);
        setDocumentNo(txtDocumentNo.getText());
        String vendor = vendorLines.get(txtVendor.getSelectedIndex() - 1).getId();
        String dateCreate = txtOrderDate.getText();
        String dateDelivered = txtDeliveryDate.getText();
        String totalT = m_jTotalEuros1.getText();
        Double dTotalT = (Double) Formats.CURRENCY.parseValue(totalT);
        String subTotalT = m_jSubtotalEuros1.getText();
        Double dSubTotal = (Double) Formats.CURRENCY.parseValue(subTotalT);
        String purchaseOrderId = list.get(index).getId();
        String tax = m_jTaxesEuros1.getText();
        String charges = m_jTxtCharges.getText();
        Double chargesValue = (Double) Formats.CURRENCY.parseValue(charges);
        Double dTax = (Double) Formats.CURRENCY.parseValue(tax);
          String discount = m_jDiscountTotal.getText();
        double discountValue = (Double) Formats.CURRENCY.parseValue(discount);
        Double roundOff = dTotalT - (dSubTotal+dTax+chargesValue-discountValue);
        //update
        //update
        m_dlPOReceipts.updatePI(purchaseOrderId, getDocumentNo(), vendor, getFormattedDate(dateCreate), getFormattedDate(dateDelivered), status, dTotalT, dSubTotal, tax,chargesValue,discountValue,roundOff);
        List<PurchaseInvoiceLine> lines1 = m_polines.getLines();
        for (PurchaseInvoiceLine l : lines1) {
            // IF PO ALREADY CONTAINS THIS PRODUCT THEN UPDATE ELSE INSERT
            java.util.List<PurchaseInvoiceProductInfo> infoLines = m_dlPOReceipts.getPurchaseInvoiceLinesByPoidAndProduct(purchaseOrderId, l.getProductID());
            if (infoLines.size() > 0) {
                if (infoLines.get(0).getPoid() != null) {
                    m_dlPOReceipts.updatePurchaseInvoiceLines(purchaseOrderId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(),l.getDiscount());
                }
            } else {
                m_dlPOReceipts.insertPurchaseInvoiceLines2(purchaseOrderId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid(),l.getDiscount());
            }
        }
       
        setJListContents();
        jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
        txtVendor.setSelectedIndex(txtVendor.getSelectedIndex());

        //resetForm();
        //setTotalSetters(0.0);
    }
   /**
     * @return the productTotalTaxes
     */
    public Double getProductTotalTaxes() {
        return productTotalTaxes;
    }

    /**
     * @param productTotalTaxes the productTotalTaxes to set
     */
    public static void setProductTotalTaxes(Double productTotalTaxes) {
        JPurchaseInvoice.productTotalTaxes = productTotalTaxes;
    }
 public static String getTotalTaxes() {
        return totalTax;
    }

    /**
     * @param productTotalTaxes the productTotalTaxes to set
     */
    public static void setTotalTaxes(String totalTax) {
        JPurchaseInvoice.totalTax = totalTax;
    }

    /**
     * @return the productSubTotal
     */
    public Double getProductTotal() {
        if (productTotal != null) {
            return productTotal;
        } else {
            return 0.0;
        }
    }

    /**
     * @param productSubTotal the productSubTotal to set
     */
    public static void setProductSubTotal(Double productSubTotal) {
        JPurchaseInvoice.productSubTotal = productSubTotal;
    }

    /**
     * @return the productTotal
     */
    public static Double getProductSubTotal() {
        if (productSubTotal != null) {
            return productSubTotal;
        } else {
            return 0.0;
        }
    }

     public static void setTotalDiscount(Double totalDiscount) {
        JPurchaseInvoice.totalDiscount = totalDiscount;
    }

    /**
     * @return the productTotal
     */
    public static Double getTotalDiscount() {
        if (totalDiscount != null) {
            return totalDiscount;
        } else {
            return 0.0;
        }
    }

    /**
     * @param productTotal the productTotal to set
     */
    public static void setProductTotal(Double productTotal) {
        JPurchaseInvoice.productTotal = productTotal;
    }

    public static void setChargesTotal(Double chargesTotal) {
        JPurchaseInvoice.chargesTotal = chargesTotal;
    }
    public static Double getChargesTotal(){
        return chargesTotal;
    }
    public static void setoldChargesTotal(Double oldchargesTotal) {
        JPurchaseInvoice.oldchargesTotal = oldchargesTotal;
    }
    public static Double getOldChargesTotal(){
        return oldchargesTotal;
    }

    private void calculateProductTotal(ProductInfoExt prod, String str) {

        setTxtFieldsZero();
        //setting taxes;
        // taxes = total * multiply * taxrate;
        double tmpSellingPrice = 0;
        double tmpTax = 0;
        double tmpTotals = 0;
        java.util.List<PurchaseInvoiceLine> i = m_polines.getLines();
        for (PurchaseInvoiceLine l : i) {
            double sellPrice = l.getPrice();
            double qty = l.getMultiply();
            String strTax = l.getTaxes();
            strTax = strTax.substring(0, strTax.length() - 1);
            double dTax = Double.parseDouble(strTax) / 100;
            tmpSellingPrice = tmpSellingPrice + (sellPrice * qty);
            tmpTax = tmpTax + (dTax * sellPrice * qty);
        }
        tmpTotals = tmpSellingPrice + tmpTax;
        setTotals(tmpTotals, tmpSellingPrice, tmpTax);

    }

    /**
     * @return the id
     */
    public static String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public static void removeLineFromDB(PurchaseInvoiceLine line) {
        deletePurchaseOrderLine(line);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        btnCompletePI = new javax.swing.JButton();
        btnSavePI = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        m_jClose = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtStatus = new javax.swing.JComboBox();
        txtDeliveryDate = new javax.swing.JTextField();
        txtVendor = new javax.swing.JComboBox();
        txtOrderDate = new javax.swing.JTextField();
        m_jbtnpodate1 = new javax.swing.JButton();
        m_jbtnpodeliverydate = new javax.swing.JButton();
        jLblStatus = new javax.swing.JLabel();
        jLblDeliveryDate = new javax.swing.JLabel();
        jLblVendor = new javax.swing.JLabel();
        jLblDocumentNo = new javax.swing.JLabel();
        txtDocumentNo = new javax.swing.JTextField();
        jLblAddress = new javax.swing.JLabel();
        txtBillAdd = new javax.swing.JComboBox();
        jLblAddress1 = new javax.swing.JLabel();
        txtShipAdd = new javax.swing.JComboBox();
        jLblpodate = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListDocumentNo = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnReload = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        jEditAttributes = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        jbtnPrint = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jChargesTable = new javax.swing.JTable();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jTaxesEuros1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jSubtotalEuros1 = new javax.swing.JLabel();
        m_jTotalEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        m_jTotalWithTax = new javax.swing.JLabel();
        m_LblCharge = new javax.swing.JLabel();
        m_jTxtCharges = new javax.swing.JLabel();
        m_jDiscountTotal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(788, 36));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCompletePI.setBackground(new java.awt.Color(255, 255, 255));
        btnCompletePI.setText("Complete");
        btnCompletePI.setEnabled(false);
        btnCompletePI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompletePIActionPerformed(evt);
            }
        });
        jPanel7.add(btnCompletePI, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 110, 25));

        btnSavePI.setBackground(new java.awt.Color(255, 255, 255));
        btnSavePI.setText("Save");
        btnSavePI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePIActionPerformed(evt);
            }
        });
        jPanel7.add(btnSavePI, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 25));

        jbtnDelete.setBackground(new java.awt.Color(255, 255, 255));
        jbtnDelete.setText("Delete");
        jbtnDelete.setFocusPainted(false);
        jbtnDelete.setFocusable(false);
        jbtnDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnDelete.setRequestFocusEnabled(false);
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });
        jPanel7.add(jbtnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 100, 25));

        m_jClose.setBackground(new java.awt.Color(255, 255, 255));
        m_jClose.setText("Close");
        m_jClose.setEnabled(false);
        m_jClose.setFocusPainted(false);
        m_jClose.setFocusable(false);
        m_jClose.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jClose.setRequestFocusEnabled(false);
        m_jClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseActionPerformed(evt);
            }
        });
        jPanel7.add(m_jClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 100, 25));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Create PI from GRN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 20, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Create PI from GRN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 160, -1));

        add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(null);

        txtStatus.setAlignmentX(0.0F);
        txtStatus.setAlignmentY(0.0F);
        txtStatus.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray));
        txtStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStatusActionPerformed(evt);
            }
        });
        jPanel2.add(txtStatus);
        txtStatus.setBounds(140, 100, 120, 20);

        txtDeliveryDate.setEditable(false);
        txtDeliveryDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtDeliveryDate);
        txtDeliveryDate.setBounds(140, 70, 120, 20);

        txtVendor.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray));
        txtVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVendorActionPerformed(evt);
            }
        });
        jPanel2.add(txtVendor);
        txtVendor.setBounds(390, 10, 120, 20);

        txtOrderDate.setEditable(false);
        txtOrderDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtOrderDate);
        txtOrderDate.setBounds(140, 10, 120, 20);

        m_jbtnpodate1.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate1ActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnpodate1);
        m_jbtnpodate1.setBounds(262, 10, 20, 20);

        m_jbtnpodeliverydate.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodeliverydate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodeliverydate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodeliverydateActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnpodeliverydate);
        m_jbtnpodeliverydate.setBounds(262, 70, 20, 20);

        jLblStatus.setText("Status");
        jLblStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblStatus);
        jLblStatus.setBounds(0, 100, 40, 20);

        jLblDeliveryDate.setText("Expected Delivery Date");
        jLblDeliveryDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblDeliveryDate);
        jLblDeliveryDate.setBounds(0, 70, 140, 20);

        jLblVendor.setText("Vendor *");
        jLblVendor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblVendor);
        jLblVendor.setBounds(300, 10, 80, 20);

        jLblDocumentNo.setText("Document No.");
        jLblDocumentNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblDocumentNo);
        jLblDocumentNo.setBounds(0, 40, 120, 20);

        txtDocumentNo.setEditable(false);
        txtDocumentNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtDocumentNo);
        txtDocumentNo.setBounds(140, 40, 120, 20);

        jLblAddress.setText("Bill Address");
        jLblAddress.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblAddress);
        jLblAddress.setBounds(300, 40, 80, 20);

        txtBillAdd.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray));
        txtBillAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBillAddActionPerformed(evt);
            }
        });
        jPanel2.add(txtBillAdd);
        txtBillAdd.setBounds(390, 40, 120, 20);

        jLblAddress1.setText("Ship Address");
        jLblAddress1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblAddress1);
        jLblAddress1.setBounds(300, 70, 80, 20);

        txtShipAdd.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray));
        txtShipAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtShipAddActionPerformed(evt);
            }
        });
        jPanel2.add(txtShipAdd);
        txtShipAdd.setBounds(390, 70, 120, 20);

        jLblpodate.setText("Date");
        jLblpodate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblpodate);
        jLblpodate.setBounds(0, 10, 80, 20);

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 520, 130));

        jPanel4.setLayout(null);

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel4.add(jPanel5);
        jPanel5.setBounds(0, 0, 480, 310);

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 480, 310));

        jListDocumentNo.setModel(new AbstractListModel() {

            public int getSize() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Object getElementAt(int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        jListDocumentNo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListDocumentNo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListDocumentNoValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListDocumentNo);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbtnNew.setBackground(new java.awt.Color(255, 255, 255));
        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        jbtnNew.setFocusPainted(false);
        jbtnNew.setFocusable(false);
        jbtnNew.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnNew.setMaximumSize(new java.awt.Dimension(62, 50));
        jbtnNew.setMinimumSize(new java.awt.Dimension(62, 50));
        jbtnNew.setPreferredSize(new java.awt.Dimension(62, 50));
        jbtnNew.setRequestFocusEnabled(false);
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });
        jPanel8.add(jbtnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 54, -1, -1));

        jbtnReload.setBackground(new java.awt.Color(255, 255, 255));
        jbtnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jbtnReload.setFocusPainted(false);
        jbtnReload.setFocusable(false);
        jbtnReload.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnReload.setMaximumSize(new java.awt.Dimension(62, 50));
        jbtnReload.setMinimumSize(new java.awt.Dimension(62, 50));
        jbtnReload.setPreferredSize(new java.awt.Dimension(62, 50));
        jbtnReload.setRequestFocusEnabled(false);
        jbtnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnReloadActionPerformed(evt);
            }
        });
        jPanel8.add(jbtnReload, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 109, -1, 44));

        m_jDelete.setBackground(new java.awt.Color(255, 255, 255));
        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelete.setPreferredSize(new java.awt.Dimension(62, 50));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel8.add(m_jDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 158, -1, 44));

        jEditAttributes.setBackground(new java.awt.Color(255, 255, 255));
        jEditAttributes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search22.png"))); // NOI18N
        jEditAttributes.setFocusPainted(false);
        jEditAttributes.setFocusable(false);
        jEditAttributes.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jEditAttributes.setPreferredSize(new java.awt.Dimension(62, 50));
        jEditAttributes.setRequestFocusEnabled(false);
        jEditAttributes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditAttributesActionPerformed(evt);
            }
        });
        jPanel8.add(jEditAttributes, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 207, -1, -1));

        m_jEditLine.setBackground(new java.awt.Color(255, 255, 255));
        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setPreferredSize(new java.awt.Dimension(62, 50));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel8.add(m_jEditLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 262, -1, -1));

        jbtnPrint.setBackground(new java.awt.Color(255, 255, 255));
        jbtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileprint.png"))); // NOI18N
        jbtnPrint.setFocusPainted(false);
        jbtnPrint.setFocusable(false);
        jbtnPrint.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnPrint.setMaximumSize(new java.awt.Dimension(62, 50));
        jbtnPrint.setMinimumSize(new java.awt.Dimension(62, 50));
        jbtnPrint.setPreferredSize(new java.awt.Dimension(62, 50));
        jbtnPrint.setRequestFocusEnabled(false);
        jbtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrintActionPerformed(evt);
            }
        });
        jPanel8.add(jbtnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, -1, -1));

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 140, 70, 320));

        m_jChargesTable.setAutoCreateColumnsFromModel(false);
        m_jChargesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        m_jChargesTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jChargesTableFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(m_jChargesTable);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 460, 270, 140));

        m_jLblTotalEuros2.setText("Taxes");
        jPanel3.add(m_jLblTotalEuros2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 90, 25));

        m_jTaxesEuros1.setBackground(java.awt.Color.white);
        m_jTaxesEuros1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxesEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros1.setOpaque(true);
        m_jTaxesEuros1.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jTaxesEuros1.setRequestFocusEnabled(false);
        jPanel3.add(m_jTaxesEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 490, 140, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("SubTotal"); // NOI18N
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 460, 100, 25));

        m_jSubtotalEuros1.setBackground(java.awt.Color.white);
        m_jSubtotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros1.setOpaque(true);
        m_jSubtotalEuros1.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jSubtotalEuros1.setRequestFocusEnabled(false);
        jPanel3.add(m_jSubtotalEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 460, 140, -1));

        m_jTotalEuros1.setBackground(java.awt.Color.white);
        m_jTotalEuros1.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jTotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros1.setOpaque(true);
        m_jTotalEuros1.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jTotalEuros1.setRequestFocusEnabled(false);
        jPanel3.add(m_jTotalEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 610, 140, -1));

        m_jLblTotalEuros1.setText("Gross Total");
        jPanel3.add(m_jLblTotalEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 610, 110, 25));

        m_jLblTotalEuros3.setText("Total with Tax");
        jPanel3.add(m_jLblTotalEuros3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 520, 90, 25));

        m_jTotalWithTax.setBackground(java.awt.Color.white);
        m_jTotalWithTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalWithTax.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalWithTax.setOpaque(true);
        m_jTotalWithTax.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jTotalWithTax.setRequestFocusEnabled(false);
        jPanel3.add(m_jTotalWithTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 520, 140, -1));

        m_LblCharge.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_LblCharge.setText("Other Charges"); // NOI18N
        jPanel3.add(m_LblCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, 100, 25));

        m_jTxtCharges.setBackground(java.awt.Color.white);
        m_jTxtCharges.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTxtCharges.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTxtCharges.setOpaque(true);
        m_jTxtCharges.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jTxtCharges.setRequestFocusEnabled(false);
        jPanel3.add(m_jTxtCharges, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 550, 140, -1));

        m_jDiscountTotal.setBackground(java.awt.Color.white);
        m_jDiscountTotal.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jDiscountTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jDiscountTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jDiscountTotal.setOpaque(true);
        m_jDiscountTotal.setPreferredSize(new java.awt.Dimension(160, 23));
        m_jDiscountTotal.setRequestFocusEnabled(false);
        jPanel3.add(m_jDiscountTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 580, 140, -1));

        jLabel1.setText("Discount");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 580, 90, 20));

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVendorActionPerformed
        vendorAction();
    }

    private void vendorAction() {
        int j = txtVendor.getSelectedIndex();
        clearAddresses();
        if (j > 0) {
            if (vendorLines.get(j - 1).isIsAddress1BillAddress()) {
                //add address1
                txtBillAdd.addItem(vendorLines.get(j - 1).getBilladdress());
            } else {
                //add billaddress
                txtBillAdd.addItem(vendorLines.get(j - 1).getAddress1());
            }
            if (vendorLines.get(j - 1).isIsAddress2ShipAddress()) {
                //add address2
                txtShipAdd.addItem(vendorLines.get(j - 1).getShipaddress());
            } else {
                // add shipaddress
                txtShipAdd.addItem(vendorLines.get(j - 1).getAddress1());
            }


            setChangesmade(true);
        }

    }//GEN-LAST:event_txtVendorActionPerformed

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int index = m_polines.getSelectedRow();
        
        if (index > -1) {
            PurchaseInvoiceLine line = m_polines.getLine(index);
            double discountPrice = getTotalDiscount()-((line.getPrice()*line.getMultiply())*(line.getDiscount()*0.01));
            System.out.println("discountPrice-setValue-"+discountPrice);
            setTotalDiscount(discountPrice);
            deletePurchaseOrderLine(line);
          
            deleteLine(m_polines.getSelectedRow());
            //vendorAction();
            setChangesmade(true);
            
            setValue();
            if(m_polines.getLines().size()==0)
            {
                try {
                    m_dlPOReceipts.deletePurchaseInvoiceCharges(id);
                } catch (BasicException ex) {
                    Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                setChargesTotal(0.0);
                setoldChargesTotal(0.0);
                initChargesDetails();
                dTable.clear();
                setTotalDiscount(0.0);
            }
            
            //dTable.clear();
        }
    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jbtnpodeliverydateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodeliverydateActionPerformed
        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        txtDeliveryDate.setText(getTodaysDate(date));
        setChangesmade(true);
    }//GEN-LAST:event_m_jbtnpodeliverydateActionPerformed

private void jEditAttributesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditAttributesActionPerformed

    ProductInfoExt prod = JPurchaseProductFinder.showMessage(JPurchaseInvoice.this, m_dlSales);
    Double rate = 0.0;
    
    if(m_polines.getLines().size()!=0){
            m_jChargesTable.setEnabled(true);
        }
    if (prod != null) {

        rate = getTaxInfo(prod.getTaxCategoryID());
        String productTaxCatId = "";
    try {
        productTaxCatId = getPdtTaxid();
        System.out.println("productTaxCatId==="+productTaxCatId);
    } catch (NullPointerException e) {
    }
        //prod.getPriceSellTax(null);
        prod.setTaxCategoryID(Formats.PERCENT.formatValue(rate.doubleValue()));
        if (productAlreadyExists(prod)) {
            PurchaseInvoiceLine i = m_polines.getLine(getProductMatchIndex());
            i.setMultiply(i.getMultiply() + 1);
            m_polines.updateLine(i, getProductMatchIndex());
        } else {

            m_polines.addLine(new PurchaseInvoiceLine(prod, prod.getMultiply(), prod.getPriceBuy(), productTaxCatId));
            //lines.add(prod);
        }
        setChangesmade(true);
          setValue();
        calculateProductTotal(prod, "");
        
//        initChargesDetails();
//        dTable.clear();
//         setChargesTotal(0.0);
//            setoldChargesTotal(0.0);
        vendorAction();

    }
}//GEN-LAST:event_jEditAttributesActionPerformed

private void btnCompletePIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompletePIActionPerformed
    // TODO add your handling code here:
    try {
        if (isOrderNumberValidated()) {
            if (isOrderDateValidated()) {
                if (isDeliveryDateValidated()) {
                    if (isVendorValidated()) {
                        if (isBillAddressValidated()) {
                            if (isShiftAddressValidated()) {
                                if (m_polines.getCount() > 0) {
                                    // TODO add your handling code here:
                                    purchaseOrderActionComplete();
                                    showMessageGreen(this, "Purchase Invoice completed Successfully");
                                    setChangesmade(false);
                                } else {
                                    showMessage(this, "Please select products");
                                }
                            } else {
                                //shift msg
                                showMessage(this, "Please Set Ship Address for Selected Vendor");
                            }
                        } else {
                            //billmsg
                            showMessage(this, "Please Set Bill Address for Selected Vendor");
                        }
                    } else {
                        showMessage(this, "Please select vendor");
                    }
                }
            }
        }
    } catch (BasicException ex) {
        Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
    }

}//GEN-LAST:event_btnCompletePIActionPerformed

private void m_jbtnpodate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate1ActionPerformed

    Date date = JCalendarDialog.showCalendarTime(this, new Date());
    txtOrderDate.setText(getTodaysDate(date));
    setChangesmade(true);

}//GEN-LAST:event_m_jbtnpodate1ActionPerformed

private void btnSavePIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePIActionPerformed
    try {
        // TODO add your handling code here:
      
        if (isOrderNumberValidated()) {
            if (isOrderDateValidated()) {
                if (isDeliveryDateValidated()) {
                    if (isVendorValidated()) {
                        if (isBillAddressValidated()) {
                            if (isShiftAddressValidated()) {
                                purchaseOrderActionSave();
                                  m_dlPOReceipts.updateInvoiceStatus("Y",GRNid);
                                  showMessageGreen(this, "Purchase Invoice saved Successfully");
                              //  JOptionPane.showMessageDialog(this, "Purchase Invoice saved Successfully");
                                setChangesmade(false);
                            } else {
                                showMessage(this, "Please Set Shift Address for Selected Vendor");

                            }
                        } else {
                            showMessage(this, "Please Set Bill Address for Selected Vendor");
                        }
                    } else {
                        showMessage(this, "Please select vendor");
                    }
                }
            }
        }
    } catch (BasicException ex) {
        Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
    }

}//GEN-LAST:event_btnSavePIActionPerformed

private void txtStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStatusActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtStatusActionPerformed

private void jListDocumentNoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListDocumentNoValueChanged
    // TODO add your handling code here:
    if (!evt.getValueIsAdjusting()) {
        clearProducts();
        clearAddresses();
        dTable.clear();
        listAction();
    }
}//GEN-LAST:event_jListDocumentNoValueChanged

private void jbtnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReloadActionPerformed
    // Add your handling code here:
    refreshAction();
}//GEN-LAST:event_jbtnReloadActionPerformed

private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
    // Add your handling code here:
    newAction();

}//GEN-LAST:event_jbtnNewActionPerformed

private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
    // TODO add your handling code here:
    deleteAction();
}//GEN-LAST:event_jbtnDeleteActionPerformed

private void txtBillAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBillAddActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtBillAddActionPerformed

private void txtShipAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtShipAddActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtShipAddActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
double discountPrice = 0;
        int i = m_polines.getSelectedRow();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                JProductPurchaseLineEditor.showMessage(this, m_App, m_polines.getLine(i), i);
                m_polines.updateLine(m_polines.getLine(i), i);
                for(int j=0;j<m_polines.getLines().size();j++){
                  discountPrice = discountPrice + ((m_polines.getLine(j).getPrice() * m_polines.getLine(j).getMultiply())*(m_polines.getLine(j).getDiscount()*0.01));

                }
 
        
//        initChargesDetails();bsetPo
//        dTable.clear();
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }
        }  
        System.out.println("discountPrice---"+discountPrice);
        setTotalDiscount(discountPrice);
        setValue();
        setTotalTxtValuesWithoutClearingAddresses();

    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseActionPerformed
        // TODO add your handling code here:
        closePOAction();
    }//GEN-LAST:event_m_jCloseActionPerformed
        private void printReport(String resourcefile, PurchaseInvoiceInfo purchase) {
        String document =  txtDocumentNo.getText();
        
        PurchaseInvoicelist = m_dlPOReceipts.getPI(document);
  //      List<PurchaseChargesInfo> chargeslines = m_dlPOReceipts.getPurchaseCharges(PurchaseInvoicelist.get(0).getId());

    //     purchase.setChargeLines(chargeslines);

        java.util.List<PurchaseInvoiceInfo> PurchaseInvoiceInfo =  new java.util.ArrayList<PurchaseInvoiceInfo>();

        PurchaseInvoiceInfo = PurchaseInvoicelist;
       
        try {

            JasperReport jr;

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
         //    reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }

            Map reportfields = new HashMap();
            reportfields.put("purchaseData", PurchaseInvoiceInfo.get(0));
            reportfields.put("purchase", purchase);


            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[] { reportfields } ));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printer.2"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
    }
    private void jbtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrintActionPerformed
        String store=m_App.getProperties().getStoreName();
         PurchaseInvoiceInfo purchase = new PurchaseInvoiceInfo();
         List<PurchaseInvoiceLine> lines = m_polines.getLines();
         PurchaseChargesInfo purchaseCharge = new PurchaseChargesInfo();
         List<PurchaseChargesInfo> chargeslines = dChargeTable;
         purchase.setLines(lines);
         purchase.setStore(store);
        // purchase.setChargeLines(chargeslines);

      //  printTicket("Printer.PurchaseOrder", purchase);
     printReport("/com/openbravo/reports/purchaseinvoice", purchase);// TODO add your handling code here:
     dChargeTable.clear();
    }//GEN-LAST:event_jbtnPrintActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      
         
        //initFormValues();
       // resetForm()
        
        try{
        JSelectPurchaseOrder.showMessage(JPurchaseInvoice.this, dlCustomers, m_App,this);
        
     //      jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
        }catch(Exception ex){
            
        }
    // setChangesmade(true);  
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      try{
        JSelectGRN.showMessage(JPurchaseInvoice.this, dlCustomers, m_App,this);
       }catch(Exception ex){
       m_jChargesTable.setEnabled(true);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
public void setValue(){
      Double discountSum = 0.0;
      Double chargesSum = 0.0;
       
        String discountValue = null;
        String chargesValue = null;
        String oldValue;
        if (m_jChargesTable.getCellEditor() == null) {
           oldValue ="0";
        } else {
            try{
                oldValue = (m_jChargesTable.getValueAt(m_jChargesTable.getSelectedRow(),m_jChargesTable.getSelectedColumn())).toString();
            }catch(NullPointerException ex){
                oldValue = "0";
            }

    }
        if(!oldValue.isEmpty()){
         setoldChargesTotal(Double.parseDouble(oldValue));
        }else{
            setoldChargesTotal(0.0);
        }
        
    for(int i = 0; i < m_jChargesTable.getRowCount(); i++){
        if(chargeList.get(i).getIsDiscount().equals("Y")){
       try{   
            discountValue = m_jChargesTable.getValueAt(i, 1).toString();
        }catch(NullPointerException ex){
            discountValue = "0";
        }
        //}
       
        if(!discountValue.isEmpty()){
                discountSum=discountSum+Double.parseDouble(discountValue);
        }
        }else{
            try{   
            chargesValue = m_jChargesTable.getValueAt(i, 1).toString();
        }catch(NullPointerException ex){
            chargesValue = "0";
        }
        if(!chargesValue.isEmpty()){
                chargesSum=chargesSum+Double.parseDouble(chargesValue);
        }
        }
        }

        System.out.println("summl = "+discountSum+"==="+getOldChargesTotal()+"--"+chargesSum+getTotalDiscount());
        setChargesTotal(chargesSum-discountSum);

        System.out.println("getCharges--"+getChargesTotal()+"===");
        setTotals(getProductSubTotal() + getProductTotalTaxes(), getProductSubTotal(), getProductTotalTaxes());
}
    private void m_jChargesTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jChargesTableFocusLost
      setValue();
        //  Double summl = 0.0;
//        System.out.println("focuslost");
//        String data;
//        String oldValue;
//        if (m_jChargesTable.getCellEditor() == null) {
//            System.out.println("Not Edited");
//            setoldChargesTotal(0.0);
//        } else {
//            try{
//                 oldValue = (m_jChargesTable.getValueAt(m_jChargesTable.getSelectedRow(),m_jChargesTable.getSelectedColumn())).toString();
//            }catch(NullPointerException ex){
//                oldValue = "0";
//            }
//
//            //if(!oldValue.isEmpty()){
//                setoldChargesTotal(Double.parseDouble(oldValue));
////            }else{
////                setoldChargesTotal(0.0);
////            }
//            System.out.println("aaaaa---"+m_jChargesTable.getValueAt(m_jChargesTable.getSelectedRow(),m_jChargesTable.getSelectedColumn()));
//    }
//
//    for(int i = 0; i < m_jChargesTable.getRowCount(); i++){
//        try{
//
//            data = m_jChargesTable.getValueAt(i, 1).toString();
//
//        }catch(NullPointerException ex){
//            data = "0";
//        }
//if(!data.isEmpty()){
//        summl=summl+Double.parseDouble(data);
//}
//    }
//     System.out.println("summl = "+summl+"==="+getOldChargesTotal());
//setChargesTotal(summl);
//        System.out.println("getCharges--"+getChargesTotal());
//        //  m_jTotalEuros1.setText("0.0");// TODO add your handling code here:
    }//GEN-LAST:event_m_jChargesTableFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompletePI;
    private javax.swing.JButton btnSavePI;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jEditAttributes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLblAddress;
    private javax.swing.JLabel jLblAddress1;
    private javax.swing.JLabel jLblDeliveryDate;
    private javax.swing.JLabel jLblDocumentNo;
    private javax.swing.JLabel jLblStatus;
    private javax.swing.JLabel jLblVendor;
    private javax.swing.JLabel jLblpodate;
    private javax.swing.JList jListDocumentNo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnPrint;
    private javax.swing.JButton jbtnReload;
    private javax.swing.JLabel m_LblCharge;
    private javax.swing.JTable m_jChargesTable;
    private javax.swing.JButton m_jClose;
    private javax.swing.JButton m_jDelete;
    public static javax.swing.JLabel m_jDiscountTotal;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    public static javax.swing.JLabel m_jSubtotalEuros1;
    public static javax.swing.JLabel m_jTaxesEuros1;
    public static javax.swing.JLabel m_jTotalEuros1;
    public static javax.swing.JLabel m_jTotalWithTax;
    public static javax.swing.JLabel m_jTxtCharges;
    private javax.swing.JButton m_jbtnpodate1;
    private javax.swing.JButton m_jbtnpodeliverydate;
    private javax.swing.JComboBox txtBillAdd;
    private javax.swing.JTextField txtDeliveryDate;
    private javax.swing.JTextField txtDocumentNo;
    private javax.swing.JTextField txtOrderDate;
    private javax.swing.JComboBox txtShipAdd;
    private javax.swing.JComboBox txtStatus;
    private javax.swing.JComboBox txtVendor;
    // End of variables declaration//GEN-END:variables

    private void setJListContents() throws BasicException {

        list = m_dlPOReceipts.getPurchaseInvoices();
        AbstractListModel model = new AbstractListModel() {

            public int getSize() {
        
            return list.size();
            }

            public Object getElementAt(int i) {
                return list.get(i).getDocumentNo() + " - " + list.get(i).getStatus();
            }
        };
        jListDocumentNo.setModel(model);
    }

     private void setJListContentsPOS() throws BasicException {

       list = m_dlPOReceipts.getPurchaseInvoices();
        AbstractListModel model = new AbstractListModel() {

            public int getSize() {
        
            return list.size();
            }

            public Object getElementAt(int i) {
                return list.get(i).getDocumentNo() + " - " + list.get(i).getStatus();
            }
        };
        jListDocumentNo.setModel(model);
    }
    private void listAction() {
        if (jListDocumentNo.getSelectedIndex() > -1) {
            try {
                int index = jListDocumentNo.getSelectedIndex();
                txtDocumentNo.setText(list.get(index).getDocumentNo());
                String vendorId = list.get(index).getVendor();
                txtVendor.setSelectedItem(vendorId);
                txtOrderDate.setText(list.get(index).getCreated().toString());
                txtDeliveryDate.setText(getFormattedDate2(list.get(index).getDelivered().toString()));
                String selectedPOStatus = list.get(index).getStatus();
                btnSavePI.setEnabled(true);
                btnCompletePI.setEnabled(true);
                setId(list.get(index).getId());
                setCurrentDocNo(list.get(index).getDocumentNo());
                if (selectedPOStatus.equals("Draft")) {
                    txtStatus.setSelectedIndex(0);
                    disableSaveButton(true);
                    disableSearchDeleteButton(true);
                    m_jClose.setEnabled(false);
                    m_jChargesTable.setEnabled(true);
                    setPOLines(list.get(index).getId(), selectedPOStatus);
                    setChangesmade(false);
                } else {
                    txtStatus.setSelectedIndex(1);
                    disableCompleteButton();
                    disableSearchDeleteButton(false);
                    m_jChargesTable.setEnabled(false);
                    m_jClose.setEnabled(true);
                    setPOLines(list.get(index).getId(), selectedPOStatus);
                    setChangesmade(false);
                }
                setInsert(false);
                setTotalOnJlistClick(list.get(index));
                initCharges(list.get(index).getId());
            } catch (ArrayIndexOutOfBoundsException ae) {
                System.out.println("arrayindexoutofboundexception");
            }

        }
    }
public void PIAction(String id) {
      // jListDocumentNo.setSelectedIndex((jListDocumentNo.getSelectedIndex())+1);
         //  {
     newAction();
     setDocumentNo(generateDocumentNoFromDB());
      list=m_dlPOReceipts.getGRNValue(id);
      GRNid=id;
             txtDocumentNo.setText(getDocumentNo());
             System.out.println(getDocumentNo());
             String vendorId = list.get(0).getVendor();
             txtVendor.setSelectedItem(vendorId);
             Calendar cal = Calendar.getInstance();
             sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             created=getFormattedDate2(sdf.format(cal.getTime()));
             txtOrderDate.setText(created);
             System.out.println(created);
             txtDeliveryDate.setText(created);
             selectedPOStatus = list.get(0).getStatus();
             btnCompletePI.setEnabled(true);
            setId(list.get(0).getId());
            setCurrentDocNo(list.get(0).getDocumentNo());
               txtStatus.setSelectedIndex(0);
                 disableCompleteButton();
                 disableSearchDeleteButton(true);
                m_jClose.setEnabled(true);
                setGRNLinesfromPI(list.get(0).getId(), "Draft");
                 setChangesmade(false);
              setInsert(true);
               setTotalOnJlistClick(list.get(0));
      
           int index = jListDocumentNo.getSelectedIndex();
        try {
            setJListContents();
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    // jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
        btnSavePI.setEnabled(true);
        //setChangesmade(true);  
       }
//public void PIAction(String id) {
//      // jListDocumentNo.setSelectedIndex((jListDocumentNo.getSelectedIndex())+1);
//         //  {
//     newAction();
//     setDocumentNo(generateDocumentNoFromDB());
//      list=m_dlPOReceipts.getPOValue(id);
//      poid=id;
//        System.out.println(id);
//        System.out.println("from database"+list.get(0).getId());
//
//
//             System.out.println(list.get(0).getVendor());
//             txtDocumentNo.setText(getDocumentNo());
//             System.out.println(getDocumentNo());
//             String vendorId = list.get(0).getVendor();
//             txtVendor.setSelectedItem(vendorId);
//             Calendar cal = Calendar.getInstance();
//             sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//             created=getFormattedDate2(sdf.format(cal.getTime()));
//             txtOrderDate.setText(created);
//             System.out.println(created);
//             txtDeliveryDate.setText(created);
//             selectedPOStatus = list.get(0).getStatus();
//             btnCompletePI.setEnabled(true);
//            setId(list.get(0).getId());
//            setCurrentDocNo(list.get(0).getDocumentNo());
//               txtStatus.setSelectedIndex(0);
//                 disableCompleteButton();
//                 disableSearchDeleteButton(true);
//                m_jClose.setEnabled(true);
//                setPOLinesfromPI(list.get(0).getId(), "Draft");
//                 setChangesmade(false);
//              setInsert(true);
//               setTotalOnJlistClick(list.get(0));
//
//           int index = jListDocumentNo.getSelectedIndex();
//        try {
//            setJListContents();
//        } catch (BasicException ex) {
//            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    // jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
//        btnSavePI.setEnabled(true);
//        //setChangesmade(true);
//       }
    private void newAction() {
        initFormValues();
        resetForm();
        resetJlistPosition();
        
    }

    private void deleteAction() {
        int index = jListDocumentNo.getSelectedIndex();
        if (index > -1) {
            if (list.get(index).getStatus().equals("Draft")) {
                int res = JOptionPane.showConfirmDialog(this, "Are sure you want to delete this Purchase Invoice");
                if (res == 0) {
                    try {
                        m_dlPOReceipts.deletePurchaseInvoiceLines(list.get(index).getId());
                        m_dlPOReceipts.deletePurchaseInvoice(list.get(index).getId());

                    } catch (BasicException ex) {
                        Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        setJListContents();
                    } catch (BasicException ex) {
                        //Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
                        setDocumentNo(generateDocumentNoFromDB());
                        initFormValues();
                    }
                    setDocumentNo(generateDocumentNoFromDB());
                    initFormValues();
                    initChargesDetails();
                    dTable.clear();
                    setTotalDiscount(0.0);
                    m_jDiscountTotal.setText(null);
                }
            } else {
                //JOptionPane.showMessageDialog(this, "Completed Purchase Invoice cannot be cancelled");
                showMessageGreen(this, "Completed Purchase Invoice cannot be cancelled");
            }
        }
    }

    private void refreshAction() {
        setDocumentNo(generateDocumentNoFromDB());
        //setChangesmade(false);
//        try {
//            setJListContents();
//        } catch (BasicException ex) {
//            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void disableSaveButton(boolean b) {
        btnSavePI.setEnabled(b);

    }

    private void disableCompleteButton() {
        btnCompletePI.setEnabled(false);
        btnSavePI.setEnabled(false);
    }

    private void setPOLines(String doc, String selectedPOStatus) {

        java.util.List<PurchaseInvoiceProductInfo> poLines = m_dlPOReceipts.getPurchaseInvoiceLines(doc);
        for (Iterator<PurchaseInvoiceProductInfo> it = poLines.iterator(); it.hasNext();) {
            PurchaseInvoiceProductInfo line = it.next();
            ProductInfoExt pInfoExt = new ProductInfoExt();
            pInfoExt.setID(line.getProductid());
            pInfoExt.setName(line.getProductname());
            pInfoExt.setPriceBuy(line.getPrice());
            pInfoExt.setTaxCategoryID(line.getTaxid());
            pInfoExt.setMultiply(line.getQuantity());
            pInfoExt.setCategoryID(line.getTaxCatId());
            pInfoExt.setUom(line.getUom());
            double discount = (line.getDiscount());
            m_polines.addLine(new PurchaseInvoiceLine(pInfoExt,discount));
        }
    }
//    private void setPOLinesfromPI(String doc, String selectedPOStatus) {
//
//        java.util.List<PurchaseOrderProductInfo> poLines = m_dlPOReceipts.getPOLines(doc);
//        for (Iterator<PurchaseOrderProductInfo> it = poLines.iterator(); it.hasNext();) {
//            PurchaseOrderProductInfo line = it.next();
//            ProductInfoExt pInfoExt = new ProductInfoExt();
//            pInfoExt.setID(line.getProductid());
//            pInfoExt.setName(line.getProductname());
//            pInfoExt.setPriceSell(line.getPrice());
//            pInfoExt.setTaxCategoryID(line.getTaxid());
//            pInfoExt.setMultiply(line.getQuantity());
//            pInfoExt.setCategoryID(line.getTaxCatId());
//            m_polines.addLine(new PurchaseInvoiceLine(pInfoExt));
//        }
//    }
 private void setGRNLinesfromPI(String doc, String selectedPOStatus) {

        java.util.List<GoodsReceiptsProductInfo> poLines = m_dlPOReceipts.getGRNLines(doc);
        for (Iterator<GoodsReceiptsProductInfo> it = poLines.iterator(); it.hasNext();) {
            GoodsReceiptsProductInfo line = it.next();
            ProductInfoExt pInfoExt = new ProductInfoExt();
            pInfoExt.setID(line.getProductid());
            pInfoExt.setName(line.getProductname());
            pInfoExt.setPriceBuy(line.getPrice());
            pInfoExt.setTaxCategoryID(line.getTaxid());
            pInfoExt.setMultiply(line.getQuantity());
            pInfoExt.setCategoryID(line.getTaxCatId());
            pInfoExt.setUom(line.getUom());
            m_polines.addLine(new PurchaseInvoiceLine(pInfoExt));
        }
    }
    private void clearProducts() {
        m_polines.clear();
    }

    private void setTotalOnJlistClick(PurchaseInvoiceInfo poInfo) {
       m_jTotalEuros1.setText(Formats.CURRENCY.formatValue(poInfo.getTotal()));
       m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(poInfo.getSubtotal()));
       m_jDiscountTotal.setText(Formats.CURRENCY.formatValue(poInfo.getDiscountTotal()));
       m_jTaxesEuros1.setText(poInfo.getTax());
       String tax = poInfo.getTax();
       tax = tax.replaceAll("Rs. ", "");
       m_jTotalWithTax.setText(Formats.CURRENCY.formatValue(Double.parseDouble(tax)+poInfo.getSubtotal()));
       m_jTxtCharges.setText(Formats.CURRENCY.formatValue(poInfo.getChargesTotal()));
       setProductSubTotal(poInfo.getSubtotal());
       setTotalTaxes(poInfo.getTax());
    }
  
    public static void setTotals(double total1, double subtotal1, double tax1) {
        setProductTotalTaxes(tax1);
        setProductSubTotal(subtotal1);
        setProductTotal(total1);
        double oldCharge;
        double charges = getChargesTotal();
       
        try{
            oldCharge = getOldChargesTotal();
        }catch(NullPointerException ex){
            oldCharge = 0;
        }
       // double oldCharge = getOldChargesTotal();
         m_jDiscountTotal.setText(Double.toString(getTotalDiscount()));
         m_jTotalEuros1.setText(Formats.CURRENCY.formatValue(total1+charges-getTotalDiscount()));
        
         System.out.println("charges==="+charges);
         m_jTxtCharges.setText(Formats.CURRENCY.formatValue(charges));
        if(setFlag ==0){
            m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(subtotal1));
            m_jTaxesEuros1.setText(Formats.CURRENCY.formatValue(tax1));
            m_jTotalWithTax.setText(Formats.CURRENCY.formatValue(subtotal1+tax1));

           
        }else{
            m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(getProductSubTotal()));
            m_jTaxesEuros1.setText(getTotalTaxes());
            String tax = getTotalTaxes();
            tax = tax.replaceAll("Rs. ", "");
            m_jTotalWithTax.setText(Formats.CURRENCY.formatValue(getProductSubTotal()+Double.parseDouble(tax)));
            
        }

    }

    private void setTxtFieldsZero() {
        m_jTotalEuros1.setText(null);
        m_jSubtotalEuros1.setText(null);
        m_jTaxesEuros1.setText(null);
        m_jTotalWithTax.setText(null);
        m_jDiscountTotal.setText(null);
        m_jTxtCharges.setText(null);
        clearAddresses();
    }

    private void disableSearchDeleteButton(boolean b) {
        jEditAttributes.setEnabled(b);
        m_jDelete.setEnabled(b);

    }

    private double formatTaxes(String cTax) {
        String tax = cTax.substring(0, cTax.length() - 1);
        Double dTax = Double.parseDouble(tax);
        return dTax.doubleValue();
    }

    /**
     * @return the changesmade
     */
    public boolean isChangesmade() {
        return changesmade;
    }

    /**
     * @param changesmade the changesmade to set
     */
    public static void setChangesmade(boolean changesmade) {
        JPurchaseInvoice.changesmade = changesmade;
    }

    private boolean saveData() {
        try {
            String purchaseOrderId = list.get(jListDocumentNo.getSelectedIndex()).getId();
            String vendor = txtVendor.getSelectedItem().toString();
            String dateCreate = txtOrderDate.getText();
            String dateDelivered = txtDeliveryDate.getText();
            String total = m_jTotalEuros1.getText();
            Double dTotal = (Double) Formats.CURRENCY.parseValue(total);
            String subTotal = m_jSubtotalEuros1.getText();
            Double dSubTotal = (Double) Formats.CURRENCY.parseValue(subTotal);
            String tax = m_jTaxesEuros1.getText();
             Double dTax = (Double) Formats.CURRENCY.parseValue(tax);
            String status = txtStatus.getSelectedItem().toString();
             Double roundOff = dTotal - (dSubTotal+dTax+getChargesTotal()-getTotalDiscount());
          
            m_dlPOReceipts.updatePI(purchaseOrderId, documentNo, vendor, getFormattedDate(dateCreate), getFormattedDate(dateDelivered), status, dTotal, dSubTotal, tax,getChargesTotal(),getTotalDiscount(),roundOff);
            m_dlPOReceipts.deletePurchaseInvoiceLines(purchaseOrderId);
            List<PurchaseInvoiceLine> lines1 = m_polines.getLines();
            for (PurchaseInvoiceLine l : lines1) {
                m_dlPOReceipts.insertPOLines2(purchaseOrderId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid());
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChangesmade(false);
        return true;
    }

    /**
     * @return the insert
     */
    public static boolean isInsert() {
        return insert;
    }

    /**
     * @param insert the insert to set
     */
    public static void setInsert(boolean insert) {
        JPurchaseInvoice.insert = insert;
    }

    private boolean productAlreadyExists(ProductInfoExt prod) {

        boolean retVal = false;
        java.util.List<PurchaseInvoiceLine> l = m_polines.getLines();
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getProductID().equals(prod.getID())) {
                retVal = true;
                setProductMatchIndex(i);
                break;
            }
        }
        return retVal;
    }

    /**
     * @return the productMatchIndex
     */
    public int getProductMatchIndex() {
        return productMatchIndex;
    }

    /**
     * @param productMatchIndex the productMatchIndex to set
     */
    public void setProductMatchIndex(int productMatchIndex) {
        this.productMatchIndex = productMatchIndex;
    }

    private boolean isVendorValidated() {
        boolean retVal = false;
        if (txtVendor.getSelectedIndex() > 0) {
            retVal = true;
        } else {
            retVal = false;
        }
        return retVal;
    }

    /**
     * @return the itemDeleted
     */
    public boolean isItemDeleted() {
        return itemDeleted;
    }

    /**
     * @param itemDeleted the itemDeleted to set
     */
    public void setItemDeleted(boolean itemDeleted) {
        this.itemDeleted = itemDeleted;
    }

    private void setTotalTxtValuesWithoutClearingAddresses() {
        m_jTotalEuros1.setText(null);
        m_jSubtotalEuros1.setText(null);
        m_jTaxesEuros1.setText(null);
        setProductSubTotal(0.0);
        java.util.List<PurchaseInvoiceLine> totalLine = m_polines.getLines();
        double subtotal = 0;
        double tax1 = 0;
        if (totalLine.size() > 0) {
            // calculating subtotal for eachproduct
            for (int i = 0; i < totalLine.size(); i++) {
                subtotal = totalLine.get(i).getPrice() * totalLine.get(i).getMultiply();
                setProductSubTotal(getProductSubTotal() + subtotal);

            }
            // calculating taxes for eachproduct
            setProductTotalTaxes(0.0);
            for (int i = 0; i < totalLine.size(); i++) {
                String tStr = totalLine.get(i).getTaxes();
                tStr = tStr.substring(0, tStr.length() - 1);
                double tDouble = Double.parseDouble(tStr) / 100;
                double taxperproduct = totalLine.get(i).getPrice() * tDouble * totalLine.get(i).getMultiply();
                setProductTotalTaxes(getProductTotalTaxes() + taxperproduct);
            }
//            String tStr = totalLine.get(0).getTaxes();
//            tStr = tStr.substring(0, tStr.length() - 1);
//            double tDouble = Double.parseDouble(tStr) / 100;
//            
//            setLoclaTaxRate(tDouble);
//            double totaltaVxes1 = getLoclaTaxRate() * getProductSubTotal();
//            double producttotal1 = getProductSubTotal() + totaltaxes1;
            setTotals(getProductSubTotal() + getProductTotalTaxes(), getProductSubTotal(), getProductTotalTaxes());
        } else {
             setChargesTotal(0.0);
            setTotals(0, 0, 0);
           
        }
    }

    /**
     * @return the currentDocNo
     */
    public static String getCurrentDocNo() {
        return currentDocNo;
    }

    /**
     * @param currentDocNo the currentDocNo to set
     */
    public void setCurrentDocNo(String currentDocNo) {
        this.currentDocNo = currentDocNo;
    }

    private boolean isBillAddressValidated() {
        boolean retVal = false;
        if (txtBillAdd.getSelectedIndex() >= 0) {
            retVal = true;
        }
        return retVal;
    }

    private boolean isShiftAddressValidated() {
        boolean retVal = false;
        if (txtShipAdd.getSelectedIndex() >= 0) {
            retVal = true;
        }
        return retVal;
    }

    private void setTotalSetters(double zero) {
        setProductSubTotal(zero);
        setProductTotal(zero);
        setProductTotalTaxes(zero);
    }

    private boolean isOrderNumberValidated() {
        boolean retVal = true;
        String docNo = txtDocumentNo.getText();
        if (docNo.isEmpty() || docNo == null) {
            showMessage(this, "Please Enter Valid Document Number");
            retVal = false;
        }
        return retVal;
    }

    private boolean isOrderDateValidated() {

        boolean retVal = true;
        String oDate = txtOrderDate.getText().toString();
        if (oDate.isEmpty() || oDate == null) {
            showMessage(this, "Please Enter Valid Date");
            retVal = false;
        } else {
            Date newDate = null;
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                newDate = (Date) format.parse(txtOrderDate.getText());
                String validDate = format.format(newDate);
                if (!validDate.equals(oDate)) {
                    showMessage(this, "Please Enter Valid Date");
                    retVal = false;
                }
            } catch (ParseException ex) {
                showMessage(this, "Please Enter Valid Date");
                retVal = false;
            }
        }
        return retVal;
    }

    private boolean isDeliveryDateValidated() {

        boolean retVal = true;
        String dDate = txtDeliveryDate.getText().toString();
        if (dDate.isEmpty() || dDate == null) {
            showMessage(this, "Please Enter Valid Delivery Date");
            retVal = false;
        } else {
            Date newDate = null;
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                newDate = (Date) format.parse(txtDeliveryDate.getText());
                String validDate = format.format(newDate);
                if (!validDate.equals(dDate)) {
                    showMessage(this, "Please Enter Valid Date");
                    retVal = false;
                }
            } catch (ParseException ex) {
                showMessage(this, "Please Enter Valid Date");
                retVal = false;
            }
        }
        return retVal;
    }

    private void showMessage(JPurchaseInvoice aThis, String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
private JPanel getLabelPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(Color.RED);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}

private void showMessageGreen(JPurchaseInvoice aThis, String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis, getLabelGreenPanel(msg), "Message",
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
    private Double getTotalNotNull(Double dTotal) {
        if (dTotal == null) {
            dTotal = new Double(0);
        }
        return dTotal;
    }

    private Double getSubTotalNotNull(Double dSubTotal) {
        if (dSubTotal == null) {
            dSubTotal = new Double(0);
        }
        return dSubTotal;
    }

    private String getTaxNotNull(String tax) {
        if (tax == null || tax == "") {
            tax = Formats.CURRENCY.formatValue(0);
        }
        return tax;
    }

    private void resetJlistPosition() {
       jListDocumentNo.clearSelection();
    }

    private void enableBothButtons(boolean b) {
        btnCompletePI.setEnabled(b);
        btnSavePI.setEnabled(b);
    }

    private void closePOAction() {
        int index = jListDocumentNo.getSelectedIndex();
        if (index > -1) {
            int res = JOptionPane.showConfirmDialog(this, "Are sure you want to close this Purchase Invoice");
            if (res == 0) {
                try {
                    m_dlPOReceipts.closePurchaseInvoice(list.get(index).getId());
                } catch (BasicException ex) {
                    Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    setJListContents();
                } catch (BasicException ex) {
                    //Logger.getLogger(JPurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
                    setDocumentNo(generateDocumentNoFromDB());
                    initFormValues();
                }
                setDocumentNo(generateDocumentNoFromDB());
                initFormValues();
            }

        }
    }
}
