//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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
package com.openbravo.pos.payment;

import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JPaymentPaper extends javax.swing.JPanel implements JPaymentInterface {

    private JPaymentNotifier m_notifier;
    private double m_dTicket;
    private double m_dTotal;
    private String m_sPaper; // "paperin", "paperout"
    // private String m_sCustomer; 
    private PurchaseOrderReceipts m_preceipts;
    public AppView app;
    private int click = 0;
    private int x = 8;
    private int y = 5;
    private int width = 270;
    private int height = 67;
    private List<VouchersList> gVoucherList = new ArrayList<VouchersList>();
    public JEnterVocherDetails finder;

    /**
     * Creates new form JPaymentTicket
     */
    public JPaymentPaper(JPaymentNotifier notifier, String sPaper, AppView app) {
        m_notifier = notifier;
        m_sPaper = sPaper;
        this.app = app;
        initComponents();
        m_preceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
    }

    @Override
    public void activate(CustomerInfoExt customerext, double dTotal, String transID) {
        m_dTotal = dTotal;
        m_dTicket = dTotal;
        String amount = "";
        try {
            amount = Formats.CURRENCY.formatValue(finder.getAmount());

        } catch (NullPointerException n) {
            m_jMoneyEuros.setText(amount);
        }
        m_jMoneyEuros.setText(amount);
        //voucherList = new ArrayList<PaymentInfoVoucher>();
        finder = null;
        printState();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    private void printState() {
//        Double value = null;
//        try {
//            value = (Double) Formats.CURRENCY.parseValue(m_jMoneyEuros.getText());
//        } catch (BasicException ex) {
//            Logger.getLogger(JPaymentPaper.class.getName()).log(Level.SEVERE, null, ex);
//            m_notifier.setStatus(true, false);
//        }
//        if (value == null) {
//            m_notifier.setStatus(true, false);
//        } else {
//            if (value >= m_dTicket) {
//                m_notifier.setStatus(true, true);
//            } else {
//                m_notifier.setStatus(true, false);
//            }
//        }
        m_notifier.setStatus(true, false);
    }

    @Override
    public PaymentInfo executePayment() {
        PaymentInfo p = new PaymentInfo() {

            @Override
            public String getName() {
                return getPaymentMethod();
            }

            @Override
            public double getTotal() {
                //return 0;
                System.out.println("finder.getAmount() is "+ finder.getAmount());
                return finder.getAmount();
            }

            @Override
            public String getID() {
                // return "no Id";
                return UUID.randomUUID().toString();
            }

            @Override
            public PaymentInfo copyPayment() {
                return this;
            }

            @Override
            public String getTransactionID() {
                return "asdf";
            }

            @Override
            public String getVoucherNo() {
                return "0";
            }

            @Override
            public ArrayList<VouchersList> getPaymentSplits() {
                ArrayList<VouchersList> al = new ArrayList<VouchersList>();
                for (int i = 0; i < finder.checknodetails.size() - 1; i++) {
                    al.add(new VouchersList(finder.checknodetails.get(i), finder.checkamtdetails.get(i)));
                }
                return al;
            }

            @Override
            public String getPaymentMode() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getDescription() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        return p;
    }

    public List<PaymentInfoVoucher> getVoucherList() {
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLblGiven = new javax.swing.JLabel();
        m_jMoneyEuros = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(null);

        jLblGiven.setText(AppLocal.getIntString("Label.InputCash")); // NOI18N
        jPanel4.add(jLblGiven);
        jLblGiven.setBounds(130, 100, 60, 14);

        m_jMoneyEuros.setBackground(new java.awt.Color(255, 255, 255));
        m_jMoneyEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jMoneyEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jMoneyEuros.setOpaque(true);
        m_jMoneyEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jMoneyEuros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_jMoneyEurosMouseClicked(evt);
            }
        });
        jPanel4.add(m_jMoneyEuros);
        m_jMoneyEuros.setBounds(190, 100, 150, 25);

        jButton1.setText("Add Vouchers");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);
        jButton1.setBounds(190, 150, 150, 23);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jMoneyEurosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jMoneyEurosMouseClicked
    }//GEN-LAST:event_m_jMoneyEurosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        finder = JEnterVocherDetails.getDialog(this, app, JEnterVocherDetails.PAYMENT_CHEQUE, gVoucherList);
        ArrayList<String> chekno = finder.checknodetails;
        ArrayList<Double> amount = finder.checkamtdetails;
        if (chekno.size() > 0) {
            JPaymentSelect.setAddEnabled(true);
            m_jMoneyEuros.setText(Formats.CURRENCY.formatValue(finder.getAmount()));
            if (chekno.size()> 0) {
                clearVoucherList();
                for (int i = 0; i < chekno.size(); i++) {
                    gVoucherList.add(new VouchersList(chekno.get(i), amount.get(i)));
                }
            }
        } else {
            JPaymentSelect.setAddEnabled(false);
        }
        printState();

    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLblGiven;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JLabel m_jMoneyEuros;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPaymentMethod() {
        return "Voucher";
    }

    /**
     * @param gVoucherList the gVoucherList to set
     */
    public void setgVoucherList(List<VouchersList> gVoucherList) {
        this.gVoucherList = gVoucherList;
    }

    private void clearVoucherList() {
        try {
            gVoucherList.clear();
        } catch (NullPointerException n) {
            gVoucherList = new ArrayList<VouchersList>();
        }
    }

    public void setVoucherTotalText(String txt) {
        m_jMoneyEuros.setText(txt);
    }

    public void setFinderNull() {
        finder = null;
    }
}
