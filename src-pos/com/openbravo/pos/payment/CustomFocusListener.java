/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppView;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author mateen
 */
class CustomFocusListener implements FocusListener {

    JTextField voucher;
    ArrayList<String> chechnoJT;
    int index;
    AppView app;
    private PurchaseOrderReceipts m_preceipts;

    public CustomFocusListener(JTextField voucher, ArrayList<String> chechnoJT, int index, AppView app) {
        this.voucher = voucher;
        this.chechnoJT = chechnoJT;
        this.index = index;
        this.app = app;
        this.m_preceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");

    }

    @Override
    public void focusGained(FocusEvent fe) {}

    @Override
    public void focusLost(FocusEvent fe) {
        
        int count = m_preceipts.getValidCreditNote(voucher.getText());
        
        if (count >0 ) {
            if (chechnoJT.size() > index) {
                chechnoJT.set(index, voucher.getText());
            } else {
                chechnoJT.add(index, voucher.getText());
            }
        } else if(fe.getOppositeComponent().getClass().getName().endsWith("JTextField")){
            JOptionPane.showMessageDialog(null, "Please enter valid voucher number");
        }

    }
}
