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
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.sales.ProcessInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
public class JGoodsMovement extends JPanel implements JPanelView {

    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicSales m_dlSales;
    private TicketParser m_TTP;
    private SentenceList m_sentlocations;
    private ComboBoxValModel m_LocationsModel;
    private ComboBoxValModel m_LocationsModelDes;
    private com.sysfore.pos.purchaseorder.JGoodsMovementLines m_GMlines;
    private DataLogicCustomers dlCustomers;
    private SentenceList taxListSentance;
    private static PurchaseOrderReceipts m_dlPOReceipts;
    private java.util.List<ProductInfoExt> lines = null;
    private String documentNo;
    private static Double productTotalTaxes = new Double(0.0);
    private static Double productSubTotal;
    private static Double productTotal;
    private static String id;
    private java.util.List<GoodsMovementInfo> list;
    private java.util.List<VendorInfo> vendorLines;
    private static boolean changesmade;
    private boolean insert;
    private boolean itemDeleted;
    private int warehouseChange=1;
    private int productMatchIndex;
    private static String currentDocNo;
    private List<GoodsReceiptsInfo> GoodsReceiptlist;
   private java.util.List<LocationInfo> wLocations;
   private java.util.List<WarehouseInfo>whToLines;
   private java.util.List<WarehouseInfo>whFromLines;
    /**
     * Creates new form JGoodsReceipts
     */
    public JGoodsMovement(AppView app) {

        m_App = app;
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlPOReceipts = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        initComponents();
        taxListSentance = m_dlSales.getTaxList();
   //     m_LocationsModel = new ComboBoxValModel();
   //     m_LocationsModelDes = new ComboBoxValModel();
        m_GMlines = new JGoodsMovementLines();
        jPanel5.add(m_GMlines, BorderLayout.CENTER);
        setDocumentNo(generateDocumentNoFromDB());
        populateVendors();

        initFormValues();
        try {
            setJListContents();
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsMovement.class.getName()).log(Level.SEVERE, null, ex);
        }
        lines = new ArrayList<ProductInfoExt>();
        setInsert(true);
        //setPurchaseCount();
    }

    private void clearAddresses() {
       
    }

    private static void deleteGRNLine(GoodsMovementLine line) {

        try {
            m_dlPOReceipts.deleteGoodsReceiptsLinesByProduct(getId(), line.getProductID());
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
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
            //txtVendor.addItem("");
            for (int i = 0; i < vendorLines.size(); i++) {
           //     txtVendor.addItem(vendorLines.get(i).getName());
            }
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 private void populateWarehouse() {

           m_sentlocations = m_dlSales.getLocationsList();
        m_jSourceWarehouse.removeAllItems();
        m_jDetsnWarehouse.removeAllItems();
        try {
            wLocations = m_sentlocations.list();
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsMovement.class.getName()).log(Level.SEVERE, null, ex);
        }
         for (LocationInfo location : wLocations) {
            m_jSourceWarehouse.addItem(location.getName());
            m_jDetsnWarehouse.addItem(location.getName());
        }
    }
    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.GM");
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void activate() throws BasicException {
         warehouseChange=0; 
         System.out.println("warehouseChange in activate="+warehouseChange);
        m_jPanTotals.setVisible(false);
        populateVendors();
        populateWarehouse();
        jListDocumentNo.setSelectedIndex(-1);

        taxListSentance = m_dlSales.getTaxList();
        setDocumentNo(generateDocumentNoFromDB());
        initFormValues();
        try {
            setJListContents();
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        lines = new ArrayList<ProductInfoExt>();
        setInsert(true);
        //setPurchaseCount();
    }


    public void stateToInsert() {
        m_LocationsModel.setSelectedKey(m_App.getInventoryLocation());
        m_LocationsModelDes.setSelectedKey(m_App.getInventoryLocation());
        m_GMlines.clear();
    }

    public boolean deactivate() {
        boolean retVal = false;
        if (isChangesmade()) {
            int res = JOptionPane.showConfirmDialog(this, "Changes has been made to Current Goods Movement, Do you want to save changes");
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
            m_GMlines.deleteLine(index);

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
        warehouseChange=0;
        setDocumentNo(generateDocumentNoFromDB());
        txtDocumentNo.setText(getDocumentNo());
        txtOrderDate.setText(getTodaysDate(new Date()));
        m_jSourceWarehouse.setSelectedIndex(-1);
        m_jDetsnWarehouse.setSelectedIndex(-1);
        btnProcessGRN.setEnabled(false);
//        btnCompleteGRN.setEnabled(false);
       // setPurchaseCount();
        clearProducts();
      // setTxtFieldsZero();
        disableSearchDeleteButton(true);
        //m_jClose.setEnabled(false);
        setChangesmade(false);
        setInsert(true);

    }

    public Double getTaxInfo(String prodTaxCatId) {

        java.util.List<TaxInfo> tInfo = null;
        double retVal;
        try {
            tInfo = m_dlPOReceipts.getTaxListbyProduct(prodTaxCatId).list();
            // tInfo = m_dlPOReceipts.getTaxRateByCategoryId(prodTaxCatId);
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tInfo.size() > 0) {
            retVal = tInfo.get(0).getRate();
        } else {
            retVal = 0.0;
        }
        return retVal;
    }

    public TaxInfo getTaxRate(String prodTaxCatId) {

        java.util.List<TaxInfo> tInfo = null;
        double retVal;
        try {
            tInfo = m_dlPOReceipts.getTaxListbyProduct(prodTaxCatId).list();
        } catch (BasicException ex) {
            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (TaxInfo) tInfo.get(0);
    }

    private void GoodsMovementsActionSave() throws BasicException {
        warehouseChange=0;
        int index = jListDocumentNo.getSelectedIndex();
        String id1 = UUID.randomUUID().toString();
        setId(id1);
        setDocumentNo(txtDocumentNo.getText());
        String dateCreate = txtOrderDate.getText();
        Date dateCreate1 = getFormattedDate(dateCreate);
        String toWarehouse=wLocations.get(m_jDetsnWarehouse.getSelectedIndex()).getID(); 
        String fromWarehouse=wLocations.get(m_jSourceWarehouse.getSelectedIndex()).getID(); 
       //   String total = m_jTotalEuros1.getText();
         //   Double dTotal = (Double) Formats.CURRENCY.parseValue(total);
          //  String subTotal = m_jSubtotalEuros1.getText();
       //     Double dSubTotal = (Double) Formats.CURRENCY.parseValue(subTotal);
         //   String tax = m_jTaxesEuros1.getText();
            List<GoodsMovementLine> lines1 = m_GMlines.getLines();
            
         if (isInsert()) {
                //insert
                String f_Poid = UUID.randomUUID().toString();
                f_Poid = f_Poid.replaceAll("-", "");
                String user = m_App.getAppUserView().getUser().getId();
                m_dlPOReceipts.insertGoodsMovement(f_Poid, getDocumentNo(), dateCreate1, getTotalNotNull(0.0), getSubTotalNotNull(0.0), getTaxNotNull(""), user,toWarehouse,fromWarehouse);
                enableBothButtons(true);
                for (GoodsMovementLine l : lines1) {
                    m_dlPOReceipts.insertGoodsMovementLines(f_Poid, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid(),l.getUnits());
                }
            } else {
                //update
                String grnId = list.get(index).getId();
                m_dlPOReceipts.updateMovement(grnId, getDocumentNo(), dateCreate1, getTotalNotNull(0.0), getSubTotalNotNull(0.0), "",toWarehouse,fromWarehouse);
                for (GoodsMovementLine l : lines1) {
                    // IF PO ALREADY CONTAINS THIS PRODUCT THEN UPDATE ELSE INSERT
                    java.util.List<GoodsMovementProductInfo> infoLines = m_dlPOReceipts.getMovementLinesByPoidAndProduct(grnId, l.getProductID());
                    if (infoLines.size() > 0) {
                        if (infoLines.get(0).getPoid() != null) {
                            m_dlPOReceipts.updateMovementLines(grnId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(),l.getUnits());
                        }
                    } else {
                        m_dlPOReceipts.insertGoodsMovementLines(grnId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid(),l.getUnits());

                    }
                }
                setInsert(false);
            }
         System.out.println(warehouseChange+"warehouseChange in save");
            setJListContents();
            jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
            
            //
            //resetForm();
            //setTotalSetters(0.0);
       // }

    }
private void setTotalOnJlistClick(GoodsMovementInfo grnInfo) {
        m_jTotalEuros1.setText(Formats.CURRENCY.formatValue(grnInfo.getTotal()));
        m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(grnInfo.getSubtotal()));
        m_jTaxesEuros1.setText(grnInfo.getTax());
    }

     private void listAction() {
          System.out.println(warehouseChange+"warehouseChange in list 1st line");
        if (jListDocumentNo.getSelectedIndex() > -1) {
            try {
                int index = jListDocumentNo.getSelectedIndex();
                txtDocumentNo.setText(list.get(index).getDocumentNo());
                String toWarehouseid=list.get(index).gettoAddress().toString();
                String fromWarehouseid=list.get(index).getfromAddress().toString();
                String toWarehouse=null;
                String fromWarehouse=null;
                try {
                     warehouseChange=0;
                     System.out.println(warehouseChange+"warehouseChange in list 2nd line");
                    toWarehouse=dlCustomers.getWarehouseListOnid(toWarehouseid);
                    fromWarehouse=dlCustomers.getWarehouseListOnid(fromWarehouseid);
                     
                } catch (BasicException ex) {
                    Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
                }
                warehouseChange=0;
                m_jSourceWarehouse.setSelectedItem(fromWarehouse);
                System.out.println(warehouseChange+"warehouseChange in list 2nd line");
                m_jDetsnWarehouse.setSelectedItem(toWarehouse);
                txtOrderDate.setText(getFormattedDate2(list.get(index).getCreated().toString()));
                btnSaveGRN.setEnabled(true);
                btnProcessGRN.setEnabled(true);
                setId(list.get(index).getId());
                setCurrentDocNo(list.get(index).getDocumentNo());
                disableSaveButton(true);
                disableSearchDeleteButton(true);
                setPOLines(list.get(index).getId());
                    setChangesmade(false);
                setInsert(false);
                setTotalOnJlistClick(list.get(index));
                System.out.println(warehouseChange+"warehouseChange in list");
                 warehouseChange=0;
            } catch (ArrayIndexOutOfBoundsException ae) {
                System.out.println("arrayindexoutofboundexception");
            }
          }
        warehouseChange=2;
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

        int count = m_GMlines.getCount();
        for (; count > 0;) {
            m_GMlines.deleteLine(--count);
        }
       //   m_jTaxesEuros1.setText("");
   //     m_jSubtotalEuros1.setText("");
       // m_jTotalEuros1.setText("");
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
            docNo = m_dlPOReceipts.getGMDocumentNo().list().get(0).toString();
            if (docNo.equals(null) || docNo.isEmpty() || docNo.equals("")) {
                docNo = "9999";
            }
            ticketDocNoValue = docNo.split("-");
            docNo=ticketDocNoValue[2];
        } catch (BasicException ex) {
            //Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            docNo = "9999";
            //Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        Integer docNoInt = Integer.parseInt(docNo);
        docNoInt = docNoInt + 1;
        StoreName = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo();
        return StoreName+"-"+docNoInt.toString();
    }

    private void GRNActionComplete() throws BasicException {
        setDocumentNo(txtDocumentNo.getText());   
         int index = jListDocumentNo.getSelectedIndex();
        String grnId = list.get(index).getId();
        String sourceLocation =wLocations.get(m_jSourceWarehouse.getSelectedIndex()).getID();
        String destnLocation = wLocations.get(m_jDetsnWarehouse.getSelectedIndex()).getID();
        List<GoodsMovementLine> lines1 = m_GMlines.getLines();
        //setInsert(true);
        m_dlPOReceipts.updateProcess(grnId);
        m_dlPOReceipts.updateSourceGoodsMovement(lines1,sourceLocation);
        m_dlPOReceipts.updateDestnGoodsMovement(lines1,destnLocation);

    //    txtDocumentNo.setText("");
        setJListContents();
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
        JGoodsMovement.productTotalTaxes = productTotalTaxes;
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
        JGoodsMovement.productSubTotal = productSubTotal;
    }

    /**
     * @return the productTotal
     */
    public Double getProductSubTotal() {
        if (productSubTotal != null) {
            return productSubTotal;
        } else {
            return 0.0;
        }
    }

    /**
     * @param productTotal the productTotal to set
     */
    public static void setProductTotal(Double productTotal) {
        JGoodsMovement.productTotal = productTotal;
    }

    private void calculateProductTotal(ProductInfoExt prod, String str) {

//        setTxtFieldsZero();
//        //setting taxes;
//        // taxes = total * multiply * taxrate;
//        double tmpSellingPrice = 0;
//        double tmpTax = 0;
//        double tmpTotals = 0;
//        java.util.List<GoodsReceiptsLine> i = m_polines.getLines();
//        for (GoodsReceiptsLine l : i) {
//            double sellPrice = l.getPrice();
//            double qty = l.getMultiply();
//            String strTax = l.getTaxes();
//            strTax = strTax.substring(0, strTax.length() - 1);
//            double dTax = Double.parseDouble(strTax) / 100;
//            tmpSellingPrice = tmpSellingPrice + (sellPrice * qty);
//            tmpTax = tmpTax + (dTax * sellPrice * qty);
//        }
//        tmpTotals = tmpSellingPrice + tmpTax;
//        setTotals(tmpTotals, tmpSellingPrice, tmpTax);

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

    public static void removeLineFromDB(GoodsMovementLine line) {
        deleteGRNLine(line);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtOrderDate = new javax.swing.JTextField();
        m_jbtnpodate1 = new javax.swing.JButton();
        jLblpodate = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jSourceWarehouse = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        m_jDetsnWarehouse = new javax.swing.JComboBox();
        txtDocumentNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros1 = new javax.swing.JLabel();
        m_jTaxesEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        btnProcessGRN = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnSaveGRN = new javax.swing.JButton();
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

        setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(null);

        jPanel2.setLayout(null);

        txtOrderDate.setEditable(false);
        txtOrderDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtOrderDate);
        txtOrderDate.setBounds(200, 10, 150, 25);

        m_jbtnpodate1.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate1ActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnpodate1);
        m_jbtnpodate1.setBounds(360, 10, 40, 28);

        jLblpodate.setText("Date");
        jLblpodate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.add(jLblpodate);
        jLblpodate.setBounds(0, 10, 100, 20);

        jLabel2.setText("Source Warehouse");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(0, 70, 140, 30);

        m_jSourceWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSourceWarehouseActionPerformed(evt);
            }
        });
        jPanel2.add(m_jSourceWarehouse);
        m_jSourceWarehouse.setBounds(200, 70, 150, 25);

        jLabel3.setText("Destination Warehouse");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(0, 110, 170, 18);

        m_jDetsnWarehouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDetsnWarehouseActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDetsnWarehouse);
        m_jDetsnWarehouse.setBounds(200, 100, 150, 25);

        txtDocumentNo.setEditable(false);
        txtDocumentNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.add(txtDocumentNo);
        txtDocumentNo.setBounds(200, 40, 150, 25);

        jLabel4.setText("Document No.");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(0, 40, 120, 20);

        jPanel3.add(jPanel2);
        jPanel2.setBounds(210, 10, 550, 140);

        jPanel4.setLayout(null);

        m_jTotalEuros1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        m_jTotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros1.setOpaque(true);
        m_jTotalEuros1.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros1.setRequestFocusEnabled(false);

        m_jLblTotalEuros1.setText("Total");

        m_jSubtotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros1.setOpaque(true);
        m_jSubtotalEuros1.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros1.setRequestFocusEnabled(false);

        m_jTaxesEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros1.setOpaque(true);
        m_jTaxesEuros1.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros1.setRequestFocusEnabled(false);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

        m_jLblTotalEuros3.setText("Sub Total");

        org.jdesktop.layout.GroupLayout m_jPanTotalsLayout = new org.jdesktop.layout.GroupLayout(m_jPanTotals);
        m_jPanTotals.setLayout(m_jPanTotalsLayout);
        m_jPanTotalsLayout.setHorizontalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .add(44, 44, 44)
                .add(m_jLblTotalEuros2)
                .add(5, 5, 5)
                .add(m_jTaxesEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jLblTotalEuros3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, m_jLblTotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jSubtotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        m_jPanTotalsLayout.setVerticalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jLblTotalEuros3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, m_jTaxesEuros1, 0, 0, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, m_jLblTotalEuros2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .add(5, 5, 5))
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(m_jSubtotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(m_jTotalEuros1, 0, 0, Short.MAX_VALUE)
                    .add(m_jLblTotalEuros1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(22, 22, 22))
        );

        jPanel4.add(m_jPanTotals);
        m_jPanTotals.setBounds(0, 410, 490, 60);

        btnProcessGRN.setBackground(java.awt.Color.white);
        btnProcessGRN.setText("Process");
        btnProcessGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessGRNActionPerformed(evt);
            }
        });
        jPanel4.add(btnProcessGRN);
        btnProcessGRN.setBounds(270, 370, 110, 30);

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel4.add(jPanel5);
        jPanel5.setBounds(0, 0, 480, 330);

        btnSaveGRN.setText("Save");
        btnSaveGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveGRNActionPerformed(evt);
            }
        });
        jPanel4.add(btnSaveGRN);
        btnSaveGRN.setBounds(150, 370, 110, 30);

        jPanel3.add(jPanel4);
        jPanel4.setBounds(210, 200, 480, 420);

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
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 523, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(jPanel6);
        jPanel6.setBounds(10, 10, 190, 540);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbtnNew.setBackground(java.awt.Color.white);
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
        jPanel8.add(jbtnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 56, -1, -1));

        jbtnReload.setBackground(java.awt.Color.white);
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
        jPanel8.add(jbtnReload, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 112, -1, -1));

        m_jDelete.setBackground(java.awt.Color.white);
        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel8.add(m_jDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 168, 62, 50));

        jEditAttributes.setBackground(java.awt.Color.white);
        jEditAttributes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search22.png"))); // NOI18N
        jEditAttributes.setFocusPainted(false);
        jEditAttributes.setFocusable(false);
        jEditAttributes.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jEditAttributes.setRequestFocusEnabled(false);
        jEditAttributes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditAttributesActionPerformed(evt);
            }
        });
        jPanel8.add(jEditAttributes, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 224, 62, 50));

        m_jEditLine.setBackground(java.awt.Color.white);
        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel8.add(m_jEditLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 280, 62, 50));

        jbtnPrint.setBackground(java.awt.Color.white);
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

        jPanel3.add(jPanel8);
        jPanel8.setBounds(690, 200, 70, 420);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int index = m_GMlines.getSelectedRow();
        if (index > -1) {
            GoodsMovementLine line = m_GMlines.getLine(index);
            deleteGRNLine(line);
            deleteLine(m_GMlines.getSelectedRow());
            //vendorAction();
            setChangesmade(true);
        }
    }//GEN-LAST:event_m_jDeleteActionPerformed

private void jEditAttributesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditAttributesActionPerformed
     if(m_jSourceWarehouse.getSelectedItem()==null){
                   showMessage(this, "Please select the source warehouse");
                 }
     else
     {
    ProductInfoExt prod = JPurchaseProductFinder.showMessage(JGoodsMovement.this, m_dlSales);
    String fromWarehouse=wLocations.get(m_jSourceWarehouse.getSelectedIndex()).getID(); 
    Double rate = 0.0;
    String productTaxCatId = "";
    try {
        productTaxCatId = prod.getTaxCategoryID();
   if (prod != null) {

        rate = getTaxInfo(prod.getTaxCategoryID());
        //prod.getPriceSellTax(null);
        prod.setTaxCategoryID(Formats.PERCENT.formatValue(rate.doubleValue()));
        if (productAlreadyExists(prod)) {
            GoodsMovementLine i = m_GMlines.getLine(getProductMatchIndex());
            i.setMultiply(i.getMultiply() + 1);
            m_GMlines.updateLine(i, getProductMatchIndex());
        } else {
           Double units=m_dlPOReceipts.getMovementUnits(prod.getID(),fromWarehouse);
           System.out.println("units"+units);
             m_GMlines.addLine(new GoodsMovementLine(prod, prod.getMultiply(), prod.getPriceSell(), productTaxCatId,units));
            
       //     lines.add(prod);
        }
        setChangesmade(true);
        calculateProductTotal(prod, "");
       
       // vendorAction();
    }
    }
     catch (Exception e) {
    }
}//GEN-LAST:event_jEditAttributesActionPerformed
}
private void m_jbtnpodate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate1ActionPerformed

    Date date = JCalendarDialog.showCalendarTime(this, new Date());
    txtOrderDate.setText(getTodaysDate(date));
    setChangesmade(true);

}//GEN-LAST:event_m_jbtnpodate1ActionPerformed

private void btnProcessGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessGRNActionPerformed

    if(m_jSourceWarehouse.getSelectedItem().equals("")){
         showMessage(this, "Please select the source warehouse");
    }else if(m_jDetsnWarehouse.getSelectedItem().equals("")){
        showMessage(this, "Please select the destination warehouse");
    }else if(m_jSourceWarehouse.getSelectedItem().equals(m_jDetsnWarehouse.getSelectedItem())){
        showMessage(this, "Source warehouse and destination warehouse should not be the same.");
    }else{
     if (m_GMlines.getCount() > 0) {
            try {
                GRNActionComplete();
            } catch (BasicException ex) {
                Logger.getLogger(JGoodsMovement.class.getName()).log(Level.SEVERE, null, ex);
            }
              showMessageGreen(this, "Goods movement completed successfully");
              //setChangesmade(false);
             initFormValues();

              } else {
               showMessage(this, "Please select products");
    } 
    }
    //    try {
//        // TODO add your handling code here:
//
//          GoodsReceiptsActionSave();
//          showMessageGreen(this, "Goods Receipts saved Successfully");
//
//          setChangesmade(false);
//
//
//    } catch (BasicException ex) {
//        Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
//    }

}//GEN-LAST:event_btnProcessGRNActionPerformed

private void jbtnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReloadActionPerformed
    // Add your handling code here:
    refreshAction();
}//GEN-LAST:event_jbtnReloadActionPerformed

private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
    // Add your handling code here:
    warehouseChange=0;
   newAction();

}//GEN-LAST:event_jbtnNewActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed

        int i = m_GMlines.getSelectedRow();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                JGMProductEditor.showMessage(this, m_App, m_GMlines.getLine(i), i);
                m_GMlines.updateLine(m_GMlines.getLine(i), i);
                setTotalTxtValuesWithoutClearingAddresses();
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }
        }
    }//GEN-LAST:event_m_jEditLineActionPerformed
private void printReport(String resourcefile, GoodsReceiptsInfo grn) {
//        String document =  txtDocumentNo.getText();
//
//        GoodsReceiptlist = m_dlPOReceipts.getGRN(document);
//
//        java.util.List<GoodsReceiptsInfo> GoodsReceiptsInfo =  new java.util.ArrayList<GoodsReceiptsInfo>();
//
//        GoodsReceiptsInfo = GoodsReceiptlist;
//
//        try {
//
//            JasperReport jr;
//
//            InputStream in = (InputStream) getClass().getResourceAsStream(resourcefile + ".ser");
//            if (in == null) {
//                // read and compile the report
//                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
//                jr = JasperCompileManager.compileReport(jd);
//            } else {
//                // read the compiled reporte
//                ObjectInputStream oin = new ObjectInputStream(in);
//                jr = (JasperReport) oin.readObject();
//                oin.close();
//            }
//
//            // Construyo el mapa de los parametros.
//            Map reportparams = new HashMap();
//         //    reportparams.put("ARG", params);
//            try {
//                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
//            } catch (MissingResourceException e) {
//            }
//          //  reportparams.put("document", document);
//            Map reportfields = new HashMap();
//            reportfields.put("purchaseData", GoodsReceiptsInfo.get(0));
//            reportfields.put("purchase", grn);
//
//
//            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[] { reportfields } ));
//
//            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printer.2"));
//
//            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);
//
//        } catch (Exception e) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
//            msg.show(this);
//        }
    }
    private void jbtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrintActionPerformed
        String store=m_App.getProperties().getStoreName();
        GoodsReceiptsInfo purchase = new GoodsReceiptsInfo();
        // List<GoodsReceiptsLine> lines = m_GMlines.getLines();
        // purchase.setStore(store);
         //purchase.setLines(lines);

      //  printTicket("Printer.PurchaseOrder", purchase);
     printReport("/com/openbravo/reports/goodsReceipts", purchase);// TODO add your handling code here:
    }//GEN-LAST:event_jbtnPrintActionPerformed

    private void jListDocumentNoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListDocumentNoValueChanged
        // TODO add your handling code here:
        if (!evt.getValueIsAdjusting()) {
            clearProducts();
            clearAddresses();
                  listAction();
        }
}//GEN-LAST:event_jListDocumentNoValueChanged

    private void m_jSourceWarehouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSourceWarehouseActionPerformed
        // TODO add your handling code here:
       //  clearProducts();
        //System.out.println(warehouseChange+"warehouseChange in source");
         String fromWarehouse=null;
     if(warehouseChange!=0 || warehouseChange==1){
        int res = JOptionPane.showConfirmDialog(this, "Changes in the Warehouse removes the product information, Do you want to change the warehouse");
            if (res == 0) {
            
                     try
                        {
                        clearProducts();
                        // fromWarehouse=m_jSourceWarehouse.getSelectedItem().;
                        int index = jListDocumentNo.getSelectedIndex();
                        String grnId = list.get(index).getId();
                        m_dlPOReceipts.deleteMovementlines(grnId);
                       }
                  catch(Exception e)
                      {
                        System.out.println(e.toString());
                      }
        }  
         else
            {
               int index = jListDocumentNo.getSelectedIndex();
                 String grnId = list.get(index).getId();
              try {
                   System.out.println(grnId);
                 fromWarehouse = m_dlPOReceipts.getWarehouse(grnId).toString();
                   System.out.println(fromWarehouse);
                    m_jSourceWarehouse.setSelectedItem(fromWarehouse);
                   // jListDocumentNo.setSelectedIndex(jListDocumentNo.getLastVisibleIndex());
              } catch (BasicException ex) {
                 Logger.getLogger(JGoodsMovement.class.getName()).log(Level.SEVERE, null, ex);
             }
              
            }
                
    }//GEN-LAST:event_m_jSourceWarehouseActionPerformed
    }
    private void btnSaveGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveGRNActionPerformed
         try {
        // TODO add your handling code here:
             int status=0;
    if (isOrderNumberValidated()) {        
        if (isOrderDateValidated()) {
                 if(m_jSourceWarehouse.getSelectedItem()==null){
                   showMessage(this, "Please select the source warehouse");
                 }else if(m_jDetsnWarehouse.getSelectedItem()==null){
                   showMessage(this, "Please select the destination warehouse");
                 }else if(m_jSourceWarehouse.getSelectedItem().equals(m_jDetsnWarehouse.getSelectedItem())){
                    showMessage(this, "Source warehouse and destination warehouse should not be the same.");
                 }else{
                     System.out.println(m_GMlines.getCount()+"m_GMlines.getCount()");
             for(int i=0;i<m_GMlines.getCount();i++)
              {
             System.out.println(m_GMlines.getCount()+"m_GMlines.getCount() inside loop");
                Double availUnits=m_GMlines.getLine(i).getUnits();
                System.out.println(availUnits+"availUnits");
                Double reqUnits=m_GMlines.getLine(i).getMultiply();
                System.out.println(reqUnits+"reqUnits");
                if(availUnits<reqUnits)
                {
                    showMessage(this, "Available Quantity is less than Required Quantity");
                    status=1;
                    break;
                }
              } 
               
                 if(status==0)
                 {
                      GoodsMovementsActionSave();
                       showMessageGreen(this, "Goods Movement saved Successfully");
                       // warehouseChange=true;
                       setChangesmade(false);
                 }
                   }
           }
      } 
    } catch (BasicException ex) {
        Logger.getLogger(JGoodsMovement.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_btnSaveGRNActionPerformed

    private void m_jDetsnWarehouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDetsnWarehouseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jDetsnWarehouseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcessGRN;
    private javax.swing.JButton btnSaveGRN;
    private javax.swing.JButton jEditAttributes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnPrint;
    private javax.swing.JButton jbtnReload;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JComboBox m_jDetsnWarehouse;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JComboBox m_jSourceWarehouse;
    public static javax.swing.JLabel m_jSubtotalEuros1;
    public static javax.swing.JLabel m_jTaxesEuros1;
    public static javax.swing.JLabel m_jTotalEuros1;
    private javax.swing.JButton m_jbtnpodate1;
    private javax.swing.JTextField txtDocumentNo;
    private javax.swing.JTextField txtOrderDate;
    // End of variables declaration//GEN-END:variables

    private void setJListContents() throws BasicException {

        list = m_dlPOReceipts.getMovement();
        AbstractListModel model = new AbstractListModel() {

            public int getSize() {
                return list.size();
            }

            public Object getElementAt(int i) {
                return list.get(i).getDocumentNo();
            }
        };
        jListDocumentNo.setModel(model);
    }

    

    private void newAction() {
        initFormValues();
        resetForm();
        resetJlistPosition();
    }

  
    private void refreshAction() {
        setDocumentNo(generateDocumentNoFromDB());
        //setChangesmade(false);
//        try {
//            setJListContents();
//        } catch (BasicException ex) {
//            Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void disableSaveButton(boolean b) {
        btnProcessGRN.setEnabled(b);

    }

    private void disableCompleteButton() {
       btnSaveGRN.setEnabled(false);
        btnProcessGRN.setEnabled(false);
    }

    private void setPOLines(String doc) {

        java.util.List<GoodsMovementProductInfo> poLines = m_dlPOReceipts.getMovementLines(doc);
        for (Iterator<GoodsMovementProductInfo> it = poLines.iterator(); it.hasNext();) {
            GoodsMovementProductInfo line = it.next();
            ProductInfoExt pInfoExt = new ProductInfoExt();
            pInfoExt.setID(line.getProductid());
            pInfoExt.setName(line.getProductname());
            pInfoExt.setPriceSell(line.getPrice());
            pInfoExt.setTaxCategoryID(line.getTaxid());
            pInfoExt.setMultiply(line.getQuantity());
            pInfoExt.setCategoryID(line.getTaxCatId());
            pInfoExt.setUom(line.getUom());
            Double units=line.getUnits();
            //m_GMlines.getLine(i).getUnits();
            m_GMlines.addLine(new GoodsMovementLine(pInfoExt,units));
        }
    }

    private void clearProducts() {
        m_GMlines.clear();
    }

//    private void setTotalOnJlistClick(GoodsReceiptsInfo grnInfo) {
//        m_jTotalEuros1.setText(Formats.CURRENCY.formatValue(grnInfo.getTotal()));
//        m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(grnInfo.getSubtotal()));
//        m_jTaxesEuros1.setText(grnInfo.getTax());
//    }

//    public static void setTotals(double total1, double subtotal1, double tax1) {
//        setProductTotalTaxes(tax1);
//        setProductSubTotal(subtotal1);
//        setProductTotal(total1);
//        m_jTotalEuros1.setText(Formats.CURRENCY.formatValue(total1));
//        m_jSubtotalEuros1.setText(Formats.CURRENCY.formatValue(subtotal1));
//        m_jTaxesEuros1.setText(Formats.CURRENCY.formatValue(tax1));
//    }

//    private void setTxtFieldsZero() {
//        m_jTotalEuros1.setText(null);
//        m_jSubtotalEuros1.setText(null);
//        m_jTaxesEuros1.setText(null);
//        clearAddresses();
//    }

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

     private boolean isOrderNumberValidated() {
        boolean retVal = true;
        String docNo = txtDocumentNo.getText();
        if (docNo.isEmpty() || docNo == null) {
            showMessage(this, "Please Enter Valid Document Number");
            retVal = false;
        }
        return retVal;
    }
    /**
     * @param changesmade the changesmade to set
     */
    public static void setChangesmade(boolean changesmade) {
        JGoodsMovement.changesmade = changesmade;
    }

    private boolean saveData() {
    //    try {
            String grnId = list.get(jListDocumentNo.getSelectedIndex()).getId();
     //       String vendor = txtVendor.getSelectedItem().toString();
            String dateCreate = txtOrderDate.getText();
      //      String dateDelivered = txtDeliveryDate.getText();
        //    String total = m_jTotalEuros1.getText();
          //  Double dTotal = (Double) Formats.CURRENCY.parseValue(total);
          //  String subTotal = m_jSubtotalEuros1.getText();
        //    Double dSubTotal = (Double) Formats.CURRENCY.parseValue(subTotal);
         //   String tax = m_jTaxesEuros1.getText();
       //     String status = txtStatus.getSelectedItem().toString();
      //      m_dlPOReceipts.updateGRN(grnId, documentNo, vendor, getFormattedDate(dateCreate), getFormattedDate(dateDelivered), status, dTotal, dSubTotal, tax);
      //      m_dlPOReceipts.deleteGRNLines(grnId);
            List<GoodsMovementLine> lines1 = m_GMlines.getLines();
            for (GoodsMovementLine l : lines1) {
       //         m_dlPOReceipts.insertGRNLines(grnId, l.getProductID(), l.getMultiply(), l.getPrice(), l.getTaxes(), l.getTaxid());
            }
     //   } catch (BasicException ex) {
        //    Logger.getLogger(JGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
       // }
        setChangesmade(false);
        return true;
    }

    /**
     * @return the insert
     */
    public boolean isInsert() {
        return insert;
    }

    /**
     * @param insert the insert to set
     */
    public void setInsert(boolean insert) {
        this.insert = insert;
    }

   
    private boolean productAlreadyExists(ProductInfoExt prod) {

        boolean retVal = false;
        java.util.List<GoodsMovementLine> l = m_GMlines.getLines();
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
//        m_jTotalEuros1.setText(null);
//        m_jSubtotalEuros1.setText(null);
//        m_jTaxesEuros1.setText(null);
//        setProductSubTotal(0.0);
//        java.util.List<GoodsReceiptsLine> totalLine = m_GMlines.getLines();
//        double subtotal = 0;
//        double tax1 = 0;
//        if (totalLine.size() > 0) {
//            // calculating subtotal for eachproduct
//            for (int i = 0; i < totalLine.size(); i++) {
//                subtotal = totalLine.get(i).getPrice() * totalLine.get(i).getMultiply();
//                setProductSubTotal(getProductSubTotal() + subtotal);
//
//            }
//            // calculating taxes for eachproduct
//            setProductTotalTaxes(0.0);
//            for (int i = 0; i < totalLine.size(); i++) {
//                String tStr = totalLine.get(i).getTaxes();
//                tStr = tStr.substring(0, tStr.length() - 1);
//                double tDouble = Double.parseDouble(tStr) / 100;
//                double taxperproduct = totalLine.get(i).getPrice() * tDouble * totalLine.get(i).getMultiply();
//                setProductTotalTaxes(getProductTotalTaxes() + taxperproduct);
//            }
////            String tStr = totalLine.get(0).getTaxes();
////            tStr = tStr.substring(0, tStr.length() - 1);
////            double tDouble = Double.parseDouble(tStr) / 100;
////
////            setLoclaTaxRate(tDouble);
////            double totaltaxes1 = getLoclaTaxRate() * getProductSubTotal();
////            double producttotal1 = getProductSubTotal() + totaltaxes1;
//            setTotals(getProductSubTotal() + getProductTotalTaxes(), getProductSubTotal(), getProductTotalTaxes());
//        } else {
//            setTotals(0, 0, 0);
//        }
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

 
    private void setTotalSetters(double zero) {
        setProductSubTotal(zero);
        setProductTotal(zero);
        setProductTotalTaxes(zero);
    }

   

    private boolean isOrderDateValidated() {

        boolean retVal = true;
        String oDate = txtOrderDate.getText().toString();
        if (oDate.isEmpty() || oDate == null) {
            showMessage(this, "Please enter the valid date");
            retVal = false;
        } else {
            Date newDate = null;
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                newDate = (Date) format.parse(txtOrderDate.getText());
                String validDate = format.format(newDate);
                if (!validDate.equals(oDate)) {
                    showMessage(this, "Enter the valid  date");
                    retVal = false;
                }
            } catch (ParseException ex) {
                showMessage(this, "Enter the valid date");
                retVal = false;
            }
        }
        return retVal;
    }


    private void showMessage(JGoodsMovement aThis, String msg) {
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

private void showMessageGreen(JGoodsMovement aThis, String msg) {
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
       btnProcessGRN.setEnabled(b);
        btnProcessGRN.setEnabled(b);
    }

    
}
