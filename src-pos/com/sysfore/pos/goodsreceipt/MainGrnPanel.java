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
package com.sysfore.pos.goodsreceipt;

//import com.openbravo.pos.inventory.*;
import bsh.ParseException;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author mateen
 */
public class MainGrnPanel extends JPanel implements JPanelView {

    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicSales m_dlSales;
    private DataLogicGoodsReceipt m_dlgoodsreceipt;
    private TicketParser m_TTP;
    //private CatalogSelector m_cat;
    private SentenceList m_sentlocations;
    //private ComboBoxValModel m_LocationsModel;
    //private ComboBoxValModel m_LocationsModelDes;
    private JGoodsReceiptLines m_invlines;
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
    private static boolean changesmade;

    /**
     * Creates new form StockManagement
     */
    public MainGrnPanel(AppView app) {

        m_App = app;
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlgoodsreceipt = (DataLogicGoodsReceipt) m_App.getBean("com.sysfore.pos.goodsreceipt.DataLogicGoodsReceipt");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        initComponents();

        m_jUp.setVisible(false);
        m_jDown.setVisible(false);
        m_jDelete.setVisible(false);
        jEditAttributes.setVisible(false);
        
        // El modelo de locales
        m_sentlocations = m_dlSales.getLocationsList();
        //m_LocationsModel = new ComboBoxValModel();
        //m_LocationsModelDes = new ComboBoxValModel();

        m_ReasonModel = new ComboBoxValModel();
        m_ReasonModel.add(MovementReason.IN_PURCHASE);

//        m_ReasonModel.add(MovementReason.IN_REFUND);
//        m_ReasonModel.add(MovementReason.IN_MOVEMENT);
//        m_ReasonModel.add(MovementReason.OUT_SALE);
//        m_ReasonModel.add(MovementReason.OUT_REFUND);
//        m_ReasonModel.add(MovementReason.OUT_BREAK);
//        m_ReasonModel.add(MovementReason.OUT_MOVEMENT);
//        m_ReasonModel.add(MovementReason.OUT_CROSSING);

        //m_jreason.setModel(m_ReasonModel);
        m_invlines = new JGoodsReceiptLines();
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

        wLocations = m_sentlocations.list();
        stateToInsert();
        m_jpurchaseorders.removeAllItems();
        m_jVendor.removeAllItems();
       // m_jreason.setSelectedIndex(-1);
        m_jLocation.removeAllItems();
        initFormData();
        for (LocationInfo location : wLocations) {
            m_jLocation.addItem(location.getName());
        }
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

    private void purchaseOrderComboAction() {
        // TODO add your handling code here:
        int index = m_jpurchaseorders.getSelectedIndex();

        if (index > 0) {
            m_jVendor.removeAllItems();
            m_jVendor.addItem("");
            index = index - 1;
            m_jVendor.addItem(listPO.get(index).getVendor());
            m_jVendor.setSelectedIndex(1);
            m_invlines.clear();
            prodLines = m_dlgoodsreceipt.getPOLines(listPO.get(index).getId());
            setPoid(listPO.get(index).getDocumentNo());
            ProductInfoExt prod = new ProductInfoExt();
            //int row = 0;
            java.util.List<StockLineInfo> slInfo = m_dlgoodsreceipt.getStockLines(getPoid());
            if (slInfo.size() > 0) {

                for (PurchaseOrderProductInfo prodline : prodLines) {
                    prod.setID(prodline.getProductid());
                    prod.setName(prodline.getProductname());
                    prod.setAttributeSetID(prodline.getProductid());
                    double dbReceivedQty = getReceivedQuantity(getPoid(), prod.getID());
//                    String receivedQtyStatus = getReceivedQuantityStatus(getPoid(), prod.getID());
//                    if(receivedQtyStatus.equals("Y")){
//                        
//                    }else{
//                        //dbReceivedQty = getMaxReceivedQty(getPoid(), prod.getID());
//                    }
//                    double dbPendingQty = getPendingQuantity(getPoid(), prod.getID());
                    if (prodline.getQuantity() - dbReceivedQty > 0) {
                        m_invlines.addLine(new InventoryLine(prod, prodline.getQuantity(), 0, dbReceivedQty, prodline.getQuantity() - dbReceivedQty, prodline.getPrice(), prodline.getQuantity() - dbReceivedQty));
                    }
                    // row = row + 1;
                }
                String re = slInfo.get(0).getReason();
                String loc = slInfo.get(0).getLocationName();
                m_ReasonModel.setSelectedItem(m_ReasonModel.getElementAt(0));
                m_jLocation.setSelectedItem(loc);
            } else {
                for (PurchaseOrderProductInfo prodline : prodLines) {
                    prod.setID(prodline.getProductid());
                    prod.setName(prodline.getProductname());
                    prod.setAttributeSetID(prodline.getProductid());
                    m_invlines.addLine(new InventoryLine(prod, prodline.getQuantity(), 0, 0, prodline.getQuantity(), prodline.getPrice(), prodline.getQuantity()));
                    //row = row + 1;
                }

                //m_jreason.setSelectedIndex(-1);
            }

        } else {
            m_jVendor.removeAllItems();
           // m_jreason.setSelectedIndex(-1);
            m_invlines.clear();
        }
    }

    private void setUnits(double dUnits) {
        int i = m_invlines.getSelectedRow();
        if (i >= 0) {
            InventoryLine inv = m_invlines.getLine(i);
            inv.setMultiply(dUnits);
            m_invlines.setLine(i, inv);
        }
    }

    private void saveData() {
        try {

            Date d = new Date();
            //MovementReason reason = (MovementReason) m_ReasonModel.getSelectedItem();
            int locationIndex = m_jLocation.getSelectedIndex();


//            if (reason == MovementReason.OUT_CROSSING) {
//                // Es una doble entrada
//                saveData(new InventoryRecord(
//                        d, MovementReason.OUT_MOVEMENT,
//                        (LocationInfo) m_LocationsModel.getSelectedItem(),
//                        m_invlines.getLines()));
//                saveData(new InventoryRecord(
//                        d, MovementReason.IN_MOVEMENT,
//                        (LocationInfo) m_LocationsModelDes.getSelectedItem(),
//                        m_invlines.getLines()));
//            } else {
            saveData(new InventoryRecord(
                    d, MovementReason.IN_PURCHASE,
                    (LocationInfo) wLocations.get(locationIndex),
                    m_invlines.getLines()));
//            }
        } catch (BasicException eData) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotsaveinventorydata"), eData);
            msg.show(this);
        }
        try {
            if (!checkMissingProductsInStockDiary(getPoid())) {
                if (checkPOStatusCompleteCondition(getPoid())) {
                    setPurchaseOrderStatusComplete(getPoid());
                }
            }
        } catch (BasicException ex) {
            Logger.getLogger(MainGrnPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jpurchaseorders.removeAllItems();
        ProcessInfo.setProcessInfo("Goods Receipt", m_dlSales);
        initFormData();
        //resetTxtFields();
    }
      private Date getFormattedDate(String strDate) {
          DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date newDate = new Date();
        if(!strDate.equals("")){

            try {
                newDate = (Date) format.parse(strDate);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(MainGrnPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            newDate =null;
        }
        return newDate;
    }


    private void saveData(InventoryRecord rec) throws BasicException {
        Date TpDate = getFormattedDate(m_jTxtTPDate.getText());
        String TpNo = m_jtxtTPNo.getText();
        // A grabar.
        SentenceExec sent = m_dlSales.getStockDiaryInsert();

        for (int i = 0; i < m_invlines.getCount(); i++) {

            InventoryLine inv = rec.getLines().get(i);

//            double dbPending = getPendingQuantity(getPoid(), inv.getProductID());

            if (inv.getUserRqty() > 0) {
                Object[] obj = new Object[]{
                    UUID.randomUUID().toString(),
                    rec.getDate(),
                    rec.getReason().getKey(),
                    rec.getLocation().getID(),
                    inv.getProductID(),
                    inv.getProductAttSetInstId(),
                    inv.getMultiply(),
                    inv.getM_pending(),
                    inv.getUserRqty(),
                    inv.getPrice(),
                    getPoid(),
                    TpNo,
                    TpDate

                };
                if (inv.getIsPendingQtyModified().equals("Y")) {
                    sent.exec(obj);
                    //m_dlgoodsreceipt.changeReceivedStatus("Y", getPoid(), inv.getProductID());
                }
//                else {
//                     m_dlgoodsreceipt.changeReceivedStatus("N", getPoid(), inv.getProductID());
//                }
            }
        }
        printTicket(rec);
        resetTxtFields();

    }

    private void printTicket(InventoryRecord invrec) {

        String sresource = m_dlSystem.getResourceAsXML("Printer.Inventory");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("inventoryrecord", invrec);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    private void initFormData() {
        listPO = m_dlgoodsreceipt.getCompletedPO();
        m_jpurchaseorders.addItem("");
        for (PurchaseOrderInfo po : listPO) {
            m_jpurchaseorders.addItem(po.getDocumentNo());
        }
     //   setGoodsReceiptCount();
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

    private double getReceivedQuantity(String poid, String product) {
        double retVal = 0;
        try {
            retVal = m_dlgoodsreceipt.getReceivedQuantity(poid, product);
        } catch (BasicException ex) {
            Logger.getLogger(MainGrnPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    private void resetTxtFields() {
        m_jpurchaseorders.setSelectedIndex(-1);
        //m_jreason.setSelectedIndex(-1);
        m_jVendor.setSelectedIndex(-1);
        m_jLocation.setSelectedIndex(-1);
        m_invlines.clear();
        m_jtxtTPNo.setText("");
        m_jTxtTPDate.setText("");

    }

    private void disablePendingQtyColumn(int row, int column) {
        m_invlines.disableCell(row, column);
    }

    private void disableOtherComponents() {
        jPanel1.setVisible(false);
       // jLabel3.setText("");
    }

    private boolean isDropDownsValidated() {
        boolean retVal;

        int vendorIndex = m_jVendor.getSelectedIndex();
        //int reasonIndex = m_jreason.getSelectedIndex();
        int warehouseStr = m_jLocation.getSelectedIndex();
        if (vendorIndex <= 0) {
            retVal = false;
            showMsg("Please Select Vendor");
        } 
//        else if (reasonIndex == -1) {
//            retVal = false;
//            showMsg("Please Select Reason");
//        }
        else if (warehouseStr == -1) {
            retVal = false;
            showMsg("Please Select Warehouse");
        } else {
            retVal = true;
        }
        return retVal;
    }

    public void showMsg(String msg) {
        JOptionPane.showMessageDialog(MainGrnPanel.this, msg);

    }

    private void setPurchaseOrderStatusComplete(String poid) {
        m_dlgoodsreceipt.setPurchaseOrderStatusComplete(poid);
    }

    private boolean checkPOStatusCompleteCondition(String id) throws BasicException {
        // if all the products whose pending qty is zero then set that pocomplete status to Y
        boolean matchQty = true;
        List<StockLineInfo> lines = m_dlgoodsreceipt.getStockLines(id);
        for (StockLineInfo line : lines) {
            double receivedQty = m_dlgoodsreceipt.getReceivedQuantity(id, line.getProduct());
            if (receivedQty != line.getAqty()) {
                // if all product id's in pruchaseorderlines table matches with productid's in stockdiary table then set status to false;
                matchQty = false;
                break;
            }
        }

        return matchQty;
    }

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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jVendor = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        m_jLocation = new javax.swing.JComboBox();
        m_jDelete = new javax.swing.JButton();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jEditAttributes = new javax.swing.JButton();
        m_jpurchaseorders = new javax.swing.JComboBox();
        btnSale = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        m_jtxtTPNo = new javax.swing.JTextField();
        jLblDeliveryDate = new javax.swing.JLabel();
        m_jTxtTPDate = new javax.swing.JTextField();
        m_jbtnpodeliverydate = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel3.setLayout(null);

        jLabel1.setText("PO No."); // NOI18N
        jPanel3.add(jLabel1);
        jLabel1.setBounds(20, 40, 150, 20);

        jLabel2.setText("Vendor *");
        jPanel3.add(jLabel2);
        jLabel2.setBounds(20, 70, 150, 20);

        m_jVendor.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        m_jVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jVendorActionPerformed(evt);
            }
        });
        jPanel3.add(m_jVendor);
        m_jVendor.setBounds(170, 70, 150, 20);

        jLabel8.setText(AppLocal.getIntString("label.warehouse")); // NOI18N
        jPanel3.add(jLabel8);
        jLabel8.setBounds(20, 100, 150, 20);

        m_jLocation.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        m_jLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jLocationActionPerformed(evt);
            }
        });
        jPanel3.add(m_jLocation);
        m_jLocation.setBounds(170, 100, 150, 20);

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
        jPanel3.add(m_jDelete);
        m_jDelete.setBounds(800, 290, 62, 50);

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1uparrow22.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel3.add(m_jUp);
        m_jUp.setBounds(800, 170, 62, 50);

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1downarrow22.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel3.add(m_jDown);
        m_jDown.setBounds(800, 230, 62, 50);

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jPanel5);
        jPanel5.setBounds(20, 200, 710, 250);

        jEditAttributes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/colorize.png"))); // NOI18N
        jEditAttributes.setFocusPainted(false);
        jEditAttributes.setFocusable(false);
        jEditAttributes.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jEditAttributes.setRequestFocusEnabled(false);
        jEditAttributes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditAttributesActionPerformed(evt);
            }
        });
        jPanel3.add(jEditAttributes);
        jEditAttributes.setBounds(800, 350, 64, 52);

        m_jpurchaseorders.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        m_jpurchaseorders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jpurchaseordersActionPerformed(evt);
            }
        });
        jPanel3.add(m_jpurchaseorders);
        m_jpurchaseorders.setBounds(170, 40, 150, 20);

        btnSale.setBackground(new java.awt.Color(255, 255, 255));
        btnSale.setText("Save");
        btnSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaleActionPerformed(evt);
            }
        });
        jPanel3.add(btnSale);
        btnSale.setBounds(290, 460, 90, 30);

        jLabel3.setText("TP.No");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(20, 130, 120, 20);

        m_jtxtTPNo.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        jPanel3.add(m_jtxtTPNo);
        m_jtxtTPNo.setBounds(170, 130, 150, 20);

        jLblDeliveryDate.setText("TP Date");
        jLblDeliveryDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel3.add(jLblDeliveryDate);
        jLblDeliveryDate.setBounds(20, 160, 140, 20);

        m_jTxtTPDate.setEditable(false);
        m_jTxtTPDate.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        jPanel3.add(m_jTxtTPDate);
        m_jTxtTPDate.setBounds(170, 160, 150, 20);

        m_jbtnpodeliverydate.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodeliverydate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodeliverydate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodeliverydateActionPerformed(evt);
            }
        });
        jPanel3.add(m_jbtnpodeliverydate);
        m_jbtnpodeliverydate.setBounds(330, 160, 40, 28);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jVendorActionPerformed
    }//GEN-LAST:event_m_jVendorActionPerformed

    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed

        m_invlines.goDown();

    }//GEN-LAST:event_m_jDownActionPerformed

    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed

        m_invlines.goUp();

    }//GEN-LAST:event_m_jUpActionPerformed

private void jEditAttributesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditAttributesActionPerformed

    int i = m_invlines.getSelectedRow();
    if (i < 0) {
        Toolkit.getDefaultToolkit().beep(); // no line selected
    } else {
        try {
            InventoryLine line = m_invlines.getLine(i);
            JProductAttEdit attedit = JProductAttEdit.getAttributesEditor(this, m_App.getSession());
            attedit.editAttributes(line.getProductAttSetId(), line.getProductAttSetInstId());
            attedit.setVisible(true);
            if (attedit.isOK()) {
                // The user pressed OK
                line.setProductAttSetInstId(attedit.getAttributeSetInst());
                line.setProductAttSetInstDesc(attedit.getAttributeSetInstDescription());
                m_invlines.setLine(i, line);
            }
        } catch (BasicException ex) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindattributes"), ex);
            msg.show(this);
        }
    }
}//GEN-LAST:event_jEditAttributesActionPerformed

    private void m_jpurchaseordersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jpurchaseordersActionPerformed

        purchaseOrderComboAction();

    }//GEN-LAST:event_m_jpurchaseordersActionPerformed

    private void btnSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaleActionPerformed
       int inventoyCount = 0;
        try {
            inventoyCount = m_dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(MainGrnPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       if(inventoyCount==0){
        if (m_invlines.getCount() == 0) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            if (isDropDownsValidated()) {
                saveData();
            }
        }
       }else{
           showMsg("Stock Reconciliation in Progress. Please continue after sometime.");
       }
    }//GEN-LAST:event_btnSaleActionPerformed

    private void m_jLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLocationActionPerformed
        // TODO add your handling code here:
        MainGrnPanel.setExit(true);
    }//GEN-LAST:event_m_jLocationActionPerformed

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        deleteLine(m_invlines.getSelectedRow());
    }//GEN-LAST:event_m_jDeleteActionPerformed
 private String getTodaysDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strDate = format.format(date);

        }
        return strDate;
    }
    private void m_jbtnpodeliverydateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodeliverydateActionPerformed
        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        m_jTxtTPDate.setText(getTodaysDate(date));
        setChangesmade(true);
}//GEN-LAST:event_m_jbtnpodeliverydateActionPerformed
public boolean isChangesmade() {
        return changesmade;
    }

    /**
     * @param changesmade the changesmade to set
     */
    public static void setChangesmade(boolean changesmade) {
        MainGrnPanel.changesmade = changesmade;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSale;
    private javax.swing.JButton jEditAttributes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLblDeliveryDate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JButton m_jDown;
    private javax.swing.JComboBox m_jLocation;
    private javax.swing.JTextField m_jTxtTPDate;
    private javax.swing.JButton m_jUp;
    private javax.swing.JComboBox m_jVendor;
    private javax.swing.JButton m_jbtnpodeliverydate;
    private javax.swing.JComboBox m_jpurchaseorders;
    private javax.swing.JTextField m_jtxtTPNo;
    // End of variables declaration//GEN-END:variables

//    private double getPendingQuantity(String poid, String productId) {
//        double retVal = 0;
//        try {
//            retVal = m_dlgoodsreceipt.getPendingQuantity(poid, productId);
//        } catch (BasicException ex) {
//            Logger.getLogger(MainGrnPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retVal;
//    }
    private boolean checkMissingProductsInStockDiary(String docNo) {
        // if products missing return false; else return true;
        boolean retVal;
        retVal = m_dlgoodsreceipt.chekMissingProductsInStock(docNo);
        return retVal;
    }

    private void setGoodsReceiptCount() {
      int processCount = 0;
      processCount = ProcessInfo.setProcessCount("Goods Receipt", m_dlSales);
        if(processCount>=10){
          btnSale.setEnabled(false);
//          jLabel3.setVisible(true);
      //    jLabel3.setText("This feature is available for Professional edition.To continue using the same kindly upgrade to Professional Edition.");

        }
    }
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
