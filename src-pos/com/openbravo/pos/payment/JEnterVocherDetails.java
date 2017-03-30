/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JEnterVocherDetails.java
 *
 * Created on 11 Aug, 2010, 10:39:56 AM
 */
package com.openbravo.pos.payment;

import com.openbravo.editor.JEditorCurrencyPositive;
import com.openbravo.editor.JEditorString;
import com.openbravo.pos.forms.AppView;
import com.sysfore.pos.cashmanagement.CreditNoteinfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author ravi
 */
public class JEnterVocherDetails extends javax.swing.JDialog {

    /**
     * Creates new form JEnterVocherDetails
     */
    private AppView app;
    public static final int PAYMENT_CHEQUE = 0;
    public static final int PAYMENT_VOCHER = 1;
    public static final int PAYMENT_CREDITNOTE = 2;
    String paymenttypeflag;
    //ArrayList<com.openbravo.editor.JEditorString> chechno = new ArrayList<com.openbravo.editor.JEditorString>();
    ArrayList<String> chechnoJT = new ArrayList<String>();
    ArrayList<com.openbravo.editor.JEditorCurrencyPositive> checkamt = new ArrayList<com.openbravo.editor.JEditorCurrencyPositive>();
    ArrayList<String> checknodetails = new ArrayList<String>();
    ArrayList<Double> checkamtdetails = new ArrayList<Double>();
    private double damount = 0.0;
    private static boolean returnflag = false;
    //com.openbravo.editor.JEditorString m_Checkno = new com.openbravo.editor.JEditorString();
    JTextField voucherNo = new JTextField(8);
    com.openbravo.editor.JEditorCurrencyPositive m_Checkamt = new com.openbravo.editor.JEditorCurrencyPositive();
    int index;
    private PurchaseOrderReceipts m_preceipts;

    private JEnterVocherDetails(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    private JEnterVocherDetails(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }

    private void VoucherMouseClicked(MouseEvent evt) {
        System.out.println("inside mouseecent");
        m_Checkamt.setEnabled(true);
        m_Checkamt.activate();
    }

    public boolean init(AppView app, int flag, List pd) {

        initComponents();
        this.app = app;
        index = 0;
        this.m_preceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        restoreVoucherList(pd);

        jPanel4.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        javax.swing.JLabel lblCheckNo = new javax.swing.JLabel("Voucher No.");
        jPanel4.add(lblCheckNo);
//        m_Checkno.setPreferredSize(new java.awt.Dimension(140, 25));
//        m_Checkno.addEditorKeys(m_jkeys);
//        m_Checkno.activate();
//        jPanel4.add(m_Checkno);
//        chechno.add(m_Checkno);
        voucherNo.setPreferredSize(new java.awt.Dimension(140, 25));
        jPanel4.add(voucherNo);
        voucherNo.addFocusListener(new CustomFocusListener(voucherNo, chechnoJT, index, app));
        javax.swing.JLabel lblCheckAmt = new javax.swing.JLabel("Amt.");
        jPanel4.add(lblCheckAmt);
        m_Checkamt.setPreferredSize(new java.awt.Dimension(140, 25));
        m_Checkamt.addEditorKeys(m_jkeys);
        //m_Checkamt.activate();
        jPanel4.add(m_Checkamt);
        checkamt.add(m_Checkamt);
        //m_Checkamt.setEnabled(true);
        jPanel4.validate();
        setVisible(true);
        return true;
    }

    public void restoreVoucherList(List pd) {
        ArrayList<VouchersList> paymentdetailspre = (ArrayList<VouchersList>) pd;
        if (paymentdetailspre != null) {
            Iterator cno = paymentdetailspre.iterator();
            while (cno.hasNext()) {
                VouchersList pds = (VouchersList) cno.next();
                jPanel4.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                javax.swing.JLabel lelCheckNo = new javax.swing.JLabel("Voucher No.");
                jPanel4.add(lelCheckNo);
                //com.openbravo.editor.JEditorString m_Checkno = new com.openbravo.editor.JEditorString();
//                m_Checkno.setPreferredSize(new java.awt.Dimension(140, 25));
//                m_Checkno.addEditorKeys(m_jkeys);
//                m_Checkno.activate();
//                m_Checkno.setText(pds.getVoucher());
//                jPanel4.add(m_Checkno);
//                chechno.add(m_Checkno);
                JTextField voucherNo2 = new JTextField(8);
                voucherNo2.setPreferredSize(new java.awt.Dimension(140, 25));
                voucherNo2.setText(pds.getVoucher());
                chechnoJT.add(index, pds.getVoucher());
                voucherNo2.addFocusListener(new CustomFocusListener(voucherNo2, chechnoJT, index, app));
                jPanel4.add(voucherNo2);
                javax.swing.JLabel lelCheckAmt = new javax.swing.JLabel("Amt.");
                jPanel4.add(lelCheckAmt);
                com.openbravo.editor.JEditorCurrencyPositive m_Checkamt = new com.openbravo.editor.JEditorCurrencyPositive();
                m_Checkamt.setPreferredSize(new java.awt.Dimension(140, 25));
                m_Checkamt.addEditorKeys(m_jkeys);
                m_Checkamt.activate();
                m_Checkamt.setDoubleValue(pds.getAmount());
                jPanel4.add(m_Checkamt);
                checkamt.add(m_Checkamt);
                jPanel4.validate();
                index++;
            }

        }

    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(JEnterVocherDetails.this, msg);
    }

    public static JEnterVocherDetails getDialog(Component parent, AppView app, int flag, List pd) {

        Window window = getWindow(parent);
        JEnterVocherDetails evd;
        if (window instanceof Frame) {
            evd = new JEnterVocherDetails((Frame) window, true);
        } else {
            evd = new JEnterVocherDetails((Dialog) window, true);
        }
        if (evd.init(app, flag, pd)) {
            return evd;
        } else {
            return null;
        }

    }

    protected static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    public void AddCheckDetails() {
        jPanel4.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        javax.swing.JLabel lelCheckNo = new javax.swing.JLabel("Voucher No.");
        jPanel4.add(lelCheckNo);
//        com.openbravo.editor.JEditorString m_Checkno = new com.openbravo.editor.JEditorString();
//        m_Checkno.setPreferredSize(new java.awt.Dimension(140, 25));
//        m_Checkno.addEditorKeys(m_jkeys);
//        m_Checkno.activate();
//        jPanel4.add(m_Checkno);
//        chechno.add(m_Checkno);
        JTextField voucherNo3 = new JTextField(8);
        voucherNo3.setPreferredSize(new java.awt.Dimension(140, 25));
        index++;
        voucherNo3.addFocusListener(new CustomFocusListener(voucherNo3, chechnoJT, index, app));
        System.out.println("index is " + index);
        jPanel4.add(voucherNo3);
        javax.swing.JLabel lelCheckAmt = new javax.swing.JLabel("Amt.");
        jPanel4.add(lelCheckAmt);
        com.openbravo.editor.JEditorCurrencyPositive m_Checkamt = new com.openbravo.editor.JEditorCurrencyPositive();
        m_Checkamt.setPreferredSize(new java.awt.Dimension(140, 25));
        m_Checkamt.addEditorKeys(m_jkeys);
        m_Checkamt.activate();
        jPanel4.add(m_Checkamt);
        checkamt.add(m_Checkamt);
        jPanel4.validate();

    }

    private void initComponents() {

        CompletePane = new javax.swing.JPanel();
        LeftPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButtonOK1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        RightPanel = new javax.swing.JPanel();
        m_jkeys = new com.openbravo.editor.JEditorKeys();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        CompletePane.setName("CompletePane");
        CompletePane.setPreferredSize(new java.awt.Dimension(650, 450));
        CompletePane.setLayout(new java.awt.BorderLayout());
        LeftPanel.setName("LeftPanel");
        LeftPanel.setPreferredSize(new java.awt.Dimension(400, 450));
        LeftPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(350, 250));
        jScrollPane1.setName("jScrollPane1");
        jScrollPane1.setPreferredSize(new java.awt.Dimension(375, 250));
        jPanel4.setName("jPanel4");
        jPanel4.setPreferredSize(new java.awt.Dimension(358, 750));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));
        jScrollPane1.setViewportView(jPanel4);
        LeftPanel.add(jScrollPane1);
        jPanel3.setMinimumSize(new java.awt.Dimension(150, 40));
        jPanel3.setName("jPanel3");
        jPanel3.setPreferredSize(new java.awt.Dimension(350, 40));
        jButton1.setText("More");
        jButton1.setName("jButton1");
        jButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);
        LeftPanel.add(jPanel3);
        jPanel7.setName("jPanel7");
        jPanel7.setPreferredSize(new java.awt.Dimension(350, 42));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
        jButtonOK1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Payment_ok.png")));
        jButtonOK1.setName("jButtonOK1");
        jButtonOK1.setPreferredSize(new java.awt.Dimension(100, 40));
        jButtonOK1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOK1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonOK1);
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Payment_cancel.png"))); // NOI18N
        jButton4.setName("jButton4");
        jButton4.setPreferredSize(new java.awt.Dimension(100, 40));
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        m_Checkamt.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VoucherMouseClicked(evt);
            }
        });
        jPanel7.add(jButton4);
        LeftPanel.add(jPanel7);
        CompletePane.add(LeftPanel, java.awt.BorderLayout.WEST);
        RightPanel.setName("RightPanel");
        RightPanel.setPreferredSize(new java.awt.Dimension(225, 424));
        m_jkeys.setName("m_jkeys");
        javax.swing.GroupLayout RightPanelLayout = new javax.swing.GroupLayout(RightPanel);
        RightPanel.setLayout(RightPanelLayout);
        RightPanelLayout.setHorizontalGroup(
                RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(RightPanelLayout.createSequentialGroup().addContainerGap().addComponent(m_jkeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(21, Short.MAX_VALUE)));
        RightPanelLayout.setVerticalGroup(
                RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(RightPanelLayout.createSequentialGroup().addComponent(m_jkeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(151, Short.MAX_VALUE)));
        CompletePane.add(RightPanel, java.awt.BorderLayout.EAST);
        getContentPane().add(CompletePane, java.awt.BorderLayout.WEST);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 650) / 2, (screenSize.height - 450) / 2, 650, 450);

    }

    private void jButtonOK1ActionPerformed(java.awt.event.ActionEvent evt) {

//        Iterator<JEditorString> cno = chechno.iterator();
//        while (cno.hasNext()) {
//            JEditorString p = (JEditorString) cno.next();
//            //if(p == null ||p.equals(""))
//            System.out.println("P value:" + p.getText());
//            if (p.getText() == null || p.getText().equals("")) {
//                System.out.println("null value---------->");
//            } else {
//                checknodetails.add(p.getText());
//            }
//        }

        if (!isVoucherAmountExceeds()) {

            Iterator<String> cno = chechnoJT.iterator();
            while (cno.hasNext()) {
                String p = (String) cno.next();
                //if(p == null ||p.equals(""))
                System.out.println("P value:" + p.toString());
                if (p.toString() == null || p.toString().equals("")) {
                    System.out.println("null value---------->");
                } else {
                    checknodetails.add(p.toString());
                }
            }
            Iterator<JEditorCurrencyPositive> camt = checkamt.iterator();
            while (camt.hasNext()) {
                JEditorCurrencyPositive p = (JEditorCurrencyPositive) camt.next();

                //checkamtdetails.add(new Double(p.getDoubleValue()));
                if (p.getDoubleValue() == null || p.getDoubleValue().equals("")) {
                    System.out.println("null value---------->");
                } else {
                    checkamtdetails.add(p.getDoubleValue());
                }

            }

            if (checknodetails.size() == checkamtdetails.size()) {

                Iterator icno = checknodetails.iterator();
                Iterator icamt = checkamtdetails.iterator();
                Iterator total = checkamtdetails.iterator();
                while (total.hasNext()) {
                    Double tot = (Double) total.next();
                    if (tot != null) {
                        damount += tot.doubleValue();
                    }
                }
                returnflag = true;
                dispose();
            } else {
                try {
                    System.out.println("checknodetails" + checknodetails + "--" + checkamtdetails);
                    String str[] = null;
                    str = paymenttypeflag.split("No.");
                    if (checkamtdetails.size() != 0 && checknodetails.size() == 0) {
                        JOptionPane.showMessageDialog(this, "Please Enter " + str[0] + "No.");

                    } else {
                        JOptionPane.showMessageDialog(this, "Please Enter " + str[0] + "Amount");
                    }
                } catch (Exception ex) {
                    checknodetails.clear();
                    checkamtdetails.clear();
                }
                checknodetails.clear();
                checkamtdetails.clear();
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        //moreaction
        AddCheckDetails();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }
    private javax.swing.JPanel CompletePane;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonOK1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private com.openbravo.editor.JEditorKeys m_jkeys;

    public double getAmount() {
        return damount;
    }

    private boolean isVoucherAmountExceeds() {
        
        boolean retVal = false;
        Iterator<String> cno = chechnoJT.iterator();
        Iterator<JEditorCurrencyPositive> camt = checkamt.iterator();
        for (; cno.hasNext() && camt.hasNext();) {
            String v_No = (String) cno.next();
            String v_Amt = camt.next().getText();
            if (isValidVoucherNoEntered(v_No) && isVaildAmtEntered(v_Amt)) {

                double e_amt = Double.parseDouble(v_Amt);
                List<CreditNoteinfo> info = m_preceipts.getValidOpenCreditNoteList(v_No);
                if (info.size() > 0) {
                    String strAmount = info.get(0).getAmount();
                    double d_amt = Double.parseDouble(strAmount);
                    if (e_amt > d_amt) {
                        JOptionPane.showMessageDialog(this, "Entered amount is exceeding the limit for voucher " + v_No );
                        retVal = true;
                        break;
                    } else {
                        retVal = false;
                    }
                }

            }
        }

        return retVal;
    }

    public boolean isValidVoucherNoEntered(String voucherNo) {
        boolean retVal = false;
        if (voucherNo.isEmpty() || voucherNo == null) {
            retVal = false;
            JOptionPane.showMessageDialog(this, "Please enter valid voucher number");

        } else {
            retVal = true;
        }

        return retVal;
    }

    private boolean isVaildAmtEntered(String v_Amt) {
        boolean retVal = false;
        try {
            Double.parseDouble(v_Amt);
            retVal = true;
        } catch (Exception e) {
            retVal = false;
            JOptionPane.showMessageDialog(this, "Please enter voucher amount");
        }
        return retVal;
    }
}
