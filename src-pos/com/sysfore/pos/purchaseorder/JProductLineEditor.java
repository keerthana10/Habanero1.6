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

import com.openbravo.basic.BasicException;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.TaxInfo;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author mateen
 */
public class JProductLineEditor extends javax.swing.JDialog {

    private PurchaseOrderProductInfo m_ProdInfo;
    private boolean m_bunitsok;
    private boolean m_bpriceok;
    private PurchaseOrderLine oLine;
    private AppView app;
    private double rate;
    private String taxCatName;
    private int tablePos;

    /**
     * Creates new form JProductLineEdit
     */
    private JProductLineEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /**
     * Creates new form JProductLineEdit
     */
    private JProductLineEditor(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }

    private PurchaseOrderLine init(AppView app, PurchaseOrderLine oLine, int i) throws BasicException {
        // Inicializo los componentes
        initComponents();
        this.oLine = oLine;
        this.app = app;
        this.tablePos = i;
        m_bunitsok = true;
        m_bpriceok = true;

        m_jName.setEnabled(!app.getAppUserView().getUser().hasPermission("com.sysfore.pos.purchaseorder.JPurchaseOrder"));
        m_jPrice.setEnabled(app.getAppUserView().getUser().hasPermission("com.sysfore.pos.purchaseorder.JPurchaseOrder"));
        m_jPriceTax.setEnabled(!app.getAppUserView().getUser().hasPermission("com.sysfore.pos.purchaseorder.JPurchaseOrder"));

        setTxtFields(oLine.getTaxid(), oLine.getTaxes(), JPurchaseOrder.m_jSubtotalEuros1.getText(), JPurchaseOrder.m_jTotalEuros1.getText());

        m_jUnits.addPropertyChangeListener("Edition", new RecalculateUnits());
        m_jPrice.addPropertyChangeListener("Edition", new RecalculatePrice());

        m_jName.addEditorKeys(m_jKeys);
        m_jUnits.addEditorKeys(m_jKeys);
        m_jPrice.addEditorKeys(m_jKeys);
        m_jPriceTax.addEditorKeys(m_jKeys);

        if (m_jName.isEnabled()) {
            m_jName.activate();
        } else {
            m_jUnits.activate();
        }
        getRootPane().setDefaultButton(m_jButtonOK);
        setVisible(true);
        return oLine;
    }

    

    private void setTxtFields(String taxid, String taxesCat, String subtotal, String total) {
        PurchaseOrderReceipts m_dlPOReceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        List<TaxInfo> tInfo = null;
        TaxInfo taxInfo = null;
        try {
            tInfo = m_dlPOReceipts.getTaxListbyProduct(taxid).list();
        } catch (BasicException ex) {
            Logger.getLogger(JProductLineEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
           taxInfo = tInfo.get(0);
        } catch (IndexOutOfBoundsException n) {
            System.out.println("indexoutofboundexception");
        }
        m_jName.setText(oLine.getProductName());
        m_jUnits.setDoubleValue(oLine.getMultiply());
        setRate(taxInfo.getRate());
        double pricePtax = (oLine.getPrice() * getRate()) + oLine.getPrice();
        m_jPrice.setDoubleValue(oLine.getPrice());
        m_jPriceTax.setDoubleValue(pricePtax);
        setTaxCatName(taxInfo.getName());
        m_jTaxrate.setText(getTaxCatName());
        m_jSubtotal.setText(JPurchaseOrder.m_jSubtotalEuros1.getText());
        m_jTotal.setText(JPurchaseOrder.m_jTotalEuros1.getText());
    }

    private void printTotals() {

        double pquantity = oLine.getMultiply();
        double pprice = oLine.getPrice();
        double subtotal = pquantity * pprice;
        double total = (subtotal * getRate()) + subtotal;
        m_jSubtotal.setText(Formats.CURRENCY.formatValue(subtotal));
        m_jTotal.setText(Formats.CURRENCY.formatValue(total));
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the taxCatName
     */
    public String getTaxCatName() {
        return taxCatName;
    }

    /**
     * @param taxCatName the taxCatName to set
     */
    public void setTaxCatName(String taxCatName) {
        this.taxCatName = taxCatName;
    }

    private class RecalculateUnits implements PropertyChangeListener {

        private double oldQty = oLine.getMultiply();

        public void propertyChange(PropertyChangeEvent evt) {
            Double value = m_jUnits.getDoubleValue();
            if (value == null || value == 0.0) {
                m_bunitsok = false;
            } else {

                oLine.setMultiply(value);
                //JPurchaseOrderLines.setMultiplychanged(true);
                m_bunitsok = true;

                printTotals();
            }

        }
    }

    private class RecalculatePrice implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            Double value = m_jPrice.getDoubleValue();
            if (value == null || value == 0.0) {
                m_bpriceok = false;
            } else {
                oLine.setPrice(value);
                double priceTax = (value * getRate()) + value;
                m_jPriceTax.setDoubleValue(priceTax);
                m_bpriceok = true;
                printTotals();
            }
        }
    }

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    public static PurchaseOrderLine showMessage(Component parent, AppView app, PurchaseOrderLine oLine, int tablePos) throws BasicException {

        Window window = getWindow(parent);

        JProductLineEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JProductLineEditor((Frame) window, true);
        } else {
            myMsg = new JProductLineEditor((Dialog) window, true);
        }
        return myMsg.init(app, oLine, tablePos);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jName = new com.openbravo.editor.JEditorString();
        m_jUnits = new com.openbravo.editor.JEditorDouble();
        m_jPrice = new com.openbravo.editor.JEditorCurrency();
        m_jPriceTax = new com.openbravo.editor.JEditorCurrency();
        m_jTaxrate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        m_jTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        m_jSubtotal = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        m_jButtonOK = new javax.swing.JButton();
        m_jButtonCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("label.editline")); // NOI18N

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.price")); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 80, 90, 14);

        jLabel2.setText(AppLocal.getIntString("label.units")); // NOI18N
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 50, 90, 14);

        jLabel3.setText(AppLocal.getIntString("label.pricetax")); // NOI18N
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 110, 90, 14);

        jLabel4.setText(AppLocal.getIntString("label.item")); // NOI18N
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 20, 90, 14);
        jPanel2.add(m_jName);
        m_jName.setBounds(100, 20, 270, 25);
        jPanel2.add(m_jUnits);
        m_jUnits.setBounds(100, 50, 240, 25);

        m_jPrice.setEnabled(false);
        jPanel2.add(m_jPrice);
        m_jPrice.setBounds(100, 80, 240, 25);

        m_jPriceTax.setEnabled(false);
        jPanel2.add(m_jPriceTax);
        m_jPriceTax.setBounds(100, 110, 240, 25);

        m_jTaxrate.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jTaxrate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxrate.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxrate.setOpaque(true);
        m_jTaxrate.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxrate.setRequestFocusEnabled(false);
        jPanel2.add(m_jTaxrate);
        m_jTaxrate.setBounds(100, 140, 210, 25);

        jLabel5.setText(AppLocal.getIntString("label.tax")); // NOI18N
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 140, 90, 14);

        jLabel6.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 200, 90, 14);

        m_jTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotal.setOpaque(true);
        m_jTotal.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotal.setRequestFocusEnabled(false);
        jPanel2.add(m_jTotal);
        m_jTotal.setBounds(100, 200, 210, 25);

        jLabel7.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        jPanel2.add(jLabel7);
        jLabel7.setBounds(10, 170, 90, 0);

        m_jSubtotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotal.setOpaque(true);
        m_jSubtotal.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotal.setRequestFocusEnabled(false);
        jPanel2.add(m_jSubtotal);
        m_jSubtotal.setBounds(100, 170, 210, 25);

        jPanel5.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        m_jButtonOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        m_jButtonOK.setFocusPainted(false);
        m_jButtonOK.setFocusable(false);
        m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonOK.setRequestFocusEnabled(false);
        m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonOKActionPerformed(evt);
            }
        });
        jPanel1.add(m_jButtonOK);

        m_jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        m_jButtonCancel.setFocusPainted(false);
        m_jButtonCancel.setFocusable(false);
        m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonCancel.setRequestFocusEnabled(false);
        m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(m_jButtonCancel);

        jPanel5.add(jPanel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));
        jPanel4.add(m_jKeys);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-580)/2, (screenSize.height-362)/2, 580, 362);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonCancelActionPerformed

        dispose();

    }//GEN-LAST:event_m_jButtonCancelActionPerformed

    private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonOKActionPerformed

//        printTotals();
        dispose();

    }//GEN-LAST:event_m_jButtonOKActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton m_jButtonCancel;
    private javax.swing.JButton m_jButtonOK;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private com.openbravo.editor.JEditorString m_jName;
    private com.openbravo.editor.JEditorCurrency m_jPrice;
    private com.openbravo.editor.JEditorCurrency m_jPriceTax;
    private javax.swing.JLabel m_jSubtotal;
    private javax.swing.JLabel m_jTaxrate;
    private javax.swing.JLabel m_jTotal;
    private com.openbravo.editor.JEditorDouble m_jUnits;
    // End of variables declaration//GEN-END:variables
}
