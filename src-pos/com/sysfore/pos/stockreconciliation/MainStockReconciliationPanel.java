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

//import com.openbravo.pos.inventory.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.JProductAttEdit;
import com.openbravo.pos.sales.ProcessInfo;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.sysfore.pos.purchaseorder.PurchaseOrderInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderProductInfo;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainStockReconciliationPanel extends JPanel implements JPanelView {

    private AppView m_App;
   // private DataLogicGoodsReceipt m_dlgoodsreceipt;
    private TicketParser m_TTP;
    //private CatalogSelector m_cat;
    private SentenceList m_sentlocations;
    //private ComboBoxValModel m_LocationsModel;
    //private ComboBoxValModel m_LocationsModelDes;
    private JStockReconciliaionLines m_invlines;
     private DataLogicStockReceipts m_dlStock;
    private int NUMBER_STATE = 0;
    private int MULTIPLY = 0;
    private static int DEFAULT = 0;
    private static int ACTIVE = 1;
    private static int DECIMAL = 2;
    private java.util.List<PurchaseOrderInfo> listPO = null;
    private ComboBoxValModel m_ReasonModel;
    private java.util.List<PurchaseOrderProductInfo> prodLines;
    private String poid;
    private static boolean exit;
    private java.util.List<LocationInfo> wLocations;
    private java.util.List<CategoryDetailsInfo> categoryLines;
    private java.util.List<LocationInfo> locations;
    private ComboBoxValModel m_LocationsModel;
    private ComboBoxValModel m_CategoryModel;
    private java.util.List<StockDetailsInfo> stockList = null;
   // private com.sysfore.pos.stockreconciliation.JStockReconciliaionLines m_Stocklines;
      private JStockReconciliaionLines m_Stocklines;
    /**
     * Creates new form StockManagement
     */
    public MainStockReconciliationPanel(AppView app) {

        m_App = app;

         m_dlStock = (DataLogicStockReceipts)m_App.getBean("com.sysfore.pos.stockreconciliation.DataLogicStockReceipts");
     //   m_dlgoodsreceipt = (DataLogicGoodsReceipt) m_App.getBean("com.sysfore.pos.goodsreceipt.DataLogicGoodsReceipt");

        initComponents();

      
        
        // El modelo de locales

     
        m_invlines = new JStockReconciliaionLines();

        jPanel5.add(m_invlines, BorderLayout.CENTER);
        initFormData();

        disableOtherComponents();

    }

    public String getTitle() {
        return "Goods Receipts";
    }

    public JComponent getComponent() {
        return this;
    }

    public void activate() throws BasicException {
        //m_cat.loadCatalog();

     //   wLocations = m_sentlocations.list();
        stateToInsert();
       populateDropDown();
    }
private void populateDropDown() {
        try {
            locations = m_dlStock.getLocationsList();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jWarehouse.addItem("");
        for (int i = 0; i < locations.size(); i++) {
            m_jWarehouse.addItem(locations.get(i).getName());
        }

         try {
            categoryLines = m_dlStock.getCategoryList();
        } catch (BasicException ex) {
            Logger.getLogger(JStockReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jItemCategory.addItem("");
         for (int i = 0; i < categoryLines.size(); i++) {
            m_jItemCategory.addItem(categoryLines.get(i).getName());
        }
//        for (int i = 0; i < categoryLines.size(); i++) {
//            m_jItemCategory.addItem(categoryLines.get(i).getName());
//        }

    }
    public void stateToInsert() {
        m_invlines.clear();
    }

    public boolean deactivate() {

        boolean retVal;
        if (isExit()) {
            if (m_invlines.getCount() > 0) {
                int res = JOptionPane.showConfirmDialog(this, LocalRes.getIntString("message.wannasave"), LocalRes.getIntString("title.editor"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    saveData();
                    retVal = true;
                } else if (res == JOptionPane.NO_OPTION) {
                    retVal = true;
                } else {
                    retVal = false;
                }
            } else {
                retVal = true;
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
            m_invlines.deleteLine(index);
        }
    }

   
    private void setUnits(double dUnits) {
//        int i = m_invlines.getSelectedRow();
//        if (i >= 0) {
//            InventoryLine inv = m_invlines.getLine(i);
//            inv.setMultiply(dUnits);
//            m_invlines.setLine(i, inv);
//        }
    }

    private void saveData() {
//        try {
//
//            Date d = new Date();
//            //MovementReason reason = (MovementReason) m_ReasonModel.getSelectedItem();
//         //   int locationIndex = m_jLocation.getSelectedIndex();
//
//
////            if (reason == MovementReason.OUT_CROSSING) {
////                // Es una doble entrada
////                saveData(new InventoryRecord(
////                        d, MovementReason.OUT_MOVEMENT,
////                        (LocationInfo) m_LocationsModel.getSelectedItem(),
////                        m_invlines.getLines()));
////                saveData(new InventoryRecord(
////                        d, MovementReason.IN_MOVEMENT,
////                        (LocationInfo) m_LocationsModelDes.getSelectedItem(),
////                        m_invlines.getLines()));
////            } else {
//            saveData(new InventoryRecord(
//                    d, MovementReason.IN_PURCHASE,
//                    (LocationInfo) wLocations.get(locationIndex),
//                    m_invlines.getLines()));
////            }
//        } catch (BasicException eData) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotsaveinventorydata"), eData);
//            msg.show(this);
//        }
//        try {
//            if (!checkMissingProductsInStockDiary(getPoid())) {
//                if (checkPOStatusCompleteCondition(getPoid())) {
//                    setPurchaseOrderStatusComplete(getPoid());
//                }
//            }
//        } catch (BasicException ex) {
//            Logger.getLogger(MainStockReconciliationPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        m_jpurchaseorders.removeAllItems();
//        ProcessInfo.setProcessInfo("Goods Receipt", m_dlSales);
//        initFormData();
//        //resetTxtFields();
    }

//    private void saveData(InventoryRecord rec) throws BasicException {
//
//        // A grabar.
//        SentenceExec sent = m_dlSales.getStockDiaryInsert();
//
//        for (int i = 0; i < m_invlines.getCount(); i++) {
//
//            InventoryLine inv = rec.getLines().get(i);
//
////            double dbPending = getPendingQuantity(getPoid(), inv.getProductID());
//
//            if (inv.getUserRqty() > 0) {
//                Object[] obj = new Object[]{
//                    UUID.randomUUID().toString(),
//                    rec.getDate(),
//                    rec.getReason().getKey(),
//                    rec.getLocation().getID(),
//                    inv.getProductID(),
//                    inv.getProductAttSetInstId(),
//                    inv.getMultiply(),
//                    inv.getM_pending(),
//                    inv.getUserRqty(),
//                    inv.getPrice(),
//                    getPoid()
//                };
//                if (inv.getIsPendingQtyModified().equals("Y")) {
//                    sent.exec(obj);
//                    //m_dlgoodsreceipt.changeReceivedStatus("Y", getPoid(), inv.getProductID());
//                }
////                else {
////                     m_dlgoodsreceipt.changeReceivedStatus("N", getPoid(), inv.getProductID());
////                }
//            }
//        }
//        printTicket(rec);
//        resetTxtFields();
//
//    }

//    private void printTicket(InventoryRecord invrec) {
//
//        String sresource = m_dlSystem.getResourceAsXML("Printer.Inventory");
//        if (sresource == null) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
//            msg.show(this);
//        } else {
//            try {
//                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
//                script.put("inventoryrecord", invrec);
//                m_TTP.printTicket(script.eval(sresource).toString());
//            } catch (ScriptException e) {
//                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
//                msg.show(this);
//            } catch (TicketPrinterException e) {
//                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
//                msg.show(this);
//            }
//        }
//    }

    private void initFormData() {
       setStockLines();

    }
  private void setStockLines() {

       stockList =  m_dlStock.getStockDetails();
        for (Iterator<StockDetailsInfo> it = stockList.iterator(); it.hasNext();) {
            StockDetailsInfo line = it.next();
              m_Stocklines.addLine(new StockLinesInfo(line.getProductName(), line.getUnits()));

          
        }
    }
    /**
     * @return the poid
     */
    public String getPoid() {
        return poid;
    }

    /**
     * @param poid the poid to set
     */
    public void setPoid(String poid) {
        this.poid = poid;
    }

//    private double getReceivedQuantity(String poid, String product) {
//        double retVal = 0;
//        try {
//            retVal = m_dlgoodsreceipt.getReceivedQuantity(poid, product);
//        } catch (BasicException ex) {
//            Logger.getLogger(MainStockReconciliationPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retVal;
//    }
//
//    private void resetTxtFields() {
//        m_jpurchaseorders.setSelectedIndex(-1);
//        //m_jreason.setSelectedIndex(-1);
//        m_jVendor.setSelectedIndex(-1);
//        m_jLocation.setSelectedIndex(-1);
//        m_invlines.clear();
//
//    }

    private void disablePendingQtyColumn(int row, int column) {
        m_invlines.disableCell(row, column);
    }

    private void disableOtherComponents() {
        jPanel1.setVisible(false);
       // jLabel3.setText("");
    }

//    private boolean isDropDownsValidated() {
//        boolean retVal;
//
//        int vendorIndex = m_jVendor.getSelectedIndex();
//        //int reasonIndex = m_jreason.getSelectedIndex();
//        int warehouseStr = m_jLocation.getSelectedIndex();
//        if (vendorIndex <= 0) {
//            retVal = false;
//            showMsg("Please Select Vendor");
//        }
////        else if (reasonIndex == -1) {
////            retVal = false;
////            showMsg("Please Select Reason");
////        }
//        else if (warehouseStr == -1) {
//            retVal = false;
//            showMsg("Please Select Warehouse");
//        } else {
//            retVal = true;
//        }
//        return retVal;
//    }

    public void showMsg(String msg) {
        JOptionPane.showMessageDialog(MainStockReconciliationPanel.this, msg);

    }

//    private void setPurchaseOrderStatusComplete(String poid) {
//        m_dlgoodsreceipt.setPurchaseOrderStatusComplete(poid);
//    }

//    private boolean checkPOStatusCompleteCondition(String id) throws BasicException {
//        // if all the products whose pending qty is zero then set that pocomplete status to Y
//        boolean matchQty = true;
//        List<StockLineInfo> lines = m_dlgoodsreceipt.getStockLines(id);
//        for (StockLineInfo line : lines) {
//            double receivedQty = m_dlgoodsreceipt.getReceivedQuantity(id, line.getProduct());
//            if (receivedQty != line.getAqty()) {
//                // if all product id's in pruchaseorderlines table matches with productid's in stockdiary table then set status to false;
//                matchQty = false;
//                break;
//            }
//        }
//
//        return matchQty;
//    }

    /**
     * @return the exit
     */
    public static boolean isExit() {
        return exit;
    }

    /**
     * @param aExit the exit to set
     */
    public static void setExit(boolean aExit) {
        exit = aExit;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnSale = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        m_jLblDocument = new javax.swing.JLabel();
        m_jDocumentNo = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jItemCategory = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        m_jWarehouse = new javax.swing.JComboBox();
        m_jBtnNew = new javax.swing.JButton();
        m_jBtnSave = new javax.swing.JButton();
        m_jBtnPrint = new javax.swing.JButton();
        m_jBtnReconcile = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        m_jTxtDate = new javax.swing.JTextField();
        m_jBtnSearch = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(null);

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jPanel5);
        jPanel5.setBounds(20, 190, 570, 260);

        btnSale.setText("Save");
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });
        jPanel3.add(btnSale);
        btnSale.setBounds(290, 460, 140, 40);

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, javax.swing.UIManager.getDefaults().getColor("Button.disabledForeground")));

        m_jLblDocument.setText("Document No");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(m_jLblDocument, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(m_jDocumentNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(383, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblDocument)
                    .add(m_jDocumentNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel4);
        jPanel4.setBounds(0, 0, 637, 60);

        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, javax.swing.UIManager.getDefaults().getColor("Button.disabledForeground")));

        jLabel1.setText("Item Category");

        jLabel2.setText("WareHouse");

        m_jBtnNew.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnNew.setText("New");
        m_jBtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnNewActionPerformed(evt);
            }
        });

        m_jBtnSave.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnSave.setText("Save");

        m_jBtnPrint.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnPrint.setText("Print");

        m_jBtnReconcile.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnReconcile.setText("Reconcile");

        jLabel3.setText("Date");

        m_jTxtDate.setEditable(false);

        m_jBtnSearch.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnSearch.setText("Search");
        m_jBtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnSearchActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(299, 299, 299)
                .add(m_jBtnNew, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(m_jBtnSave)
                .add(6, 6, 6)
                .add(m_jBtnPrint)
                .add(6, 6, 6)
                .add(m_jBtnReconcile))
            .add(jPanel6Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(m_jItemCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(m_jWarehouse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(m_jBtnSearch))
            .add(jPanel6Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(24, 24, 24)
                .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jBtnNew)
                    .add(m_jBtnSave)
                    .add(m_jBtnPrint)
                    .add(m_jBtnReconcile))
                .add(17, 17, 17)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jItemCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jWarehouse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jBtnSearch))
                .add(7, 7, 7)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.add(jPanel6);
        jPanel6.setBounds(10, 71, 640, 100);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
        // TODO add your handling code here:
//        if (m_invlines.getCount() == 0) {
//            Toolkit.getDefaultToolkit().beep();
//        } else {
//            if (isDropDownsValidated()) {
//                saveData();
//            }
//        }
    }//GEN-LAST:event_btnSaleActionPerformed

    private void m_jBtnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnSearchActionPerformed
        String categoryName = null;
        String wareHouse;
        String query = null;
        if(m_jItemCategory.getSelectedItem().equals("")){
            categoryName = "";
        }else{
            categoryName = categoryLines.get(m_jItemCategory.getSelectedIndex() - 1).getID();
        }
        if(m_jWarehouse.getSelectedItem().equals("")){
            wareHouse = "";
        }else{
            wareHouse = locations.get(m_jWarehouse.getSelectedIndex() - 1).getID();
        }
        if(categoryName.equals("") && wareHouse.equals("")){
            query ="";
        }else if(!categoryName.equals("") && wareHouse.equals("")){
            query = "AND PRODUCTS.CATEGORY='"+categoryName+"'";
        }else if(categoryName.equals("") && !wareHouse.equals("")){
            query = "AND STOCKCURRENT.LOCATION='"+wareHouse+"'";
        }else{
            query = "AND PRODUCTS.CATEGORY='"+categoryName+"' AND STOCKCURRENT.LOCATION='"+wareHouse+"'";
        }
      //  initStockData(query);
}//GEN-LAST:event_m_jBtnSearchActionPerformed

    private void m_jBtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnNewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton m_jBtnNew;
    private javax.swing.JButton m_jBtnPrint;
    private javax.swing.JButton m_jBtnReconcile;
    private javax.swing.JButton m_jBtnSave;
    private javax.swing.JButton m_jBtnSearch;
    private javax.swing.JComboBox m_jDocumentNo;
    private javax.swing.JComboBox m_jItemCategory;
    private javax.swing.JLabel m_jLblDocument;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JComboBox m_jWarehouse;
    // End of variables declaration//GEN-END:variables

//    private double getPendingQuantity(String poid, String productId) {
//        double retVal = 0;
//        try {
//            retVal = m_dlgoodsreceipt.getPendingQuantity(poid, productId);
//        } catch (BasicException ex) {
//            Logger.getLogger(MainStockReconciliationPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retVal;
//    }
//    private boolean checkMissingProductsInStockDiary(String docNo) {
//        // if products missing return false; else return true;
//        boolean retVal;
//        retVal = m_dlgoodsreceipt.chekMissingProductsInStock(docNo);
//        return retVal;
//    }

  
//    private String getReceivedQuantityStatus(String docno, String productid) {
//
//        return m_dlgoodsreceipt.getReceivedQuantityStatus(poid, productid);
//
//    }
//
//    private double getMaxReceivedQty(String poid, String productid) {
//        return m_dlgoodsreceipt.getMaxReceivedQty(poid, productid);
//    }
}
