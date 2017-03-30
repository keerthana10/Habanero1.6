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
package com.sysfore.pos.cashmanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import java.awt.Component;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.util.Date;


public class CreditNoteEditor extends JDialog {

    public javax.swing.JDialog dEdior = null;
    //static DataLogicCustomers dlCustomers2 = null;
    static PurchaseOrderReceipts PurchaseOrder = null;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<CreditNoteinfo> list = null;
    public boolean updateMode = false;
    static int x = 400;
    static int y = 200;
    private Date Currentdate;
     protected AppView app;
     private java.util.List<CreditNoteinfo> CreditNotelist;
   private static DataLogicSystem m_dlSystem;
    private TicketParser m_TTP;
    public CreditNoteEditor(AppView m_app, JPanel parent) throws BasicException {
           
        //super(new JFrame(),true);
       // dlSystem = (DataLogicSystem) m_app.getBean("com.openbravo.pos.forms.DataLogicSystem");
        //dlReceipts = (DataLogicReceipts) m_app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        // dlCustomers = (DataLogicCustomers) m_app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        //init(dlReceipts, m_app, parent);
    }

    private CreditNoteEditor(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, 662, 562);
    }

    private CreditNoteEditor(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, 662, 562);
    }

    public static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {
        //dlCustomers2 = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        CreditNoteEditor.PurchaseOrder=PurchaseOrder;
       // PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        Window window = getWindow(parent);
        CreditNoteEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new CreditNoteEditor((Frame) window, true);
        } else {
            myMsg = new CreditNoteEditor((Dialog) window, true);
        }
        myMsg.init(PurchaseOrder, m_App);
        //showMessage(parent, PurchaseOrder, m_App);
    }

    private static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder) throws BasicException {

        Window window = getWindow(parent);
        CreditNoteEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new CreditNoteEditor((Frame) window, true);
        } else {
            myMsg = new CreditNoteEditor((Dialog) window, true);
        } 
     //   myMsg.init(PurchaseOrder, m_App);
    }

    public void init(final PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {

        initComponents();
        populateList(PurchaseOrder);
          m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        //m_jDiscountPercentage.setText(dlCustomers.getMaxPercentage() + "%");
        m_jCreditNote.setText("");
        setTitle("Credit Note");
        setVisible(true);
        add(m_jCreditNoteList);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jAmount = new javax.swing.JTextField();
        m_jDiscountLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jCreditNoteList = new javax.swing.JList();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        jbtnPrint = new javax.swing.JButton();
        m_jCreditNoteNoLabel = new javax.swing.JLabel();
        m_jCreditNote = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        m_jcustomer = new javax.swing.JTextField();
        m_jStatus = new javax.swing.JLabel();
        m_jTxtStatus = new javax.swing.JTextField();
        m_jValidity = new javax.swing.JLabel();
        m_jTxtValidity = new javax.swing.JTextField();

        m_jAmount.setEditable(false);
        m_jAmount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jDiscountLabel.setText("Amount");

        m_jCreditNoteList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jCreditNoteListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jCreditNoteList);

        jbtnNew.setBackground(new java.awt.Color(255, 255, 255));
        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        jbtnNew.setFocusPainted(false);
        jbtnNew.setFocusable(false);
        jbtnNew.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnNew.setRequestFocusEnabled(false);
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });

        jbtnDelete.setBackground(new java.awt.Color(255, 255, 255));
        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editdelete.png"))); // NOI18N
        jbtnDelete.setFocusPainted(false);
        jbtnDelete.setFocusable(false);
        jbtnDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });

        jbtnSave.setBackground(new java.awt.Color(255, 255, 255));
        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png"))); // NOI18N
        jbtnSave.setFocusPainted(false);
        jbtnSave.setFocusable(false);
        jbtnSave.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnSave.setRequestFocusEnabled(false);
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        jbtnPrint.setBackground(new java.awt.Color(255, 255, 255));
        jbtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
        jbtnPrint.setFocusPainted(false);
        jbtnPrint.setFocusable(false);
        jbtnPrint.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnPrint.setRequestFocusEnabled(false);
        jbtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrintActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jSaverLayout = new org.jdesktop.layout.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSaverLayout.createSequentialGroup()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(330, 330, 330)
                .add(jbtnNew)
                .add(4, 4, 4)
                .add(jbtnDelete)
                .add(4, 4, 4)
                .add(jbtnSave)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jbtnPrint)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSaverLayout.createSequentialGroup()
                .add(jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jbtnNew)
                    .add(jbtnDelete)
                    .add(jbtnSave)
                    .add(jbtnPrint))
                .addContainerGap())
        );

        m_jCreditNoteNoLabel.setText("Credit Note No.");

        m_jCreditNote.setEditable(false);
        m_jCreditNote.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jCreditNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCreditNoteActionPerformed(evt);
            }
        });

        jLabel1.setText("Customer");

        m_jcustomer.setEditable(false);
        m_jcustomer.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jStatus.setText("Status");

        m_jTxtStatus.setEditable(false);
        m_jTxtStatus.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jValidity.setText("Validity");

        m_jTxtValidity.setEditable(false);
        m_jTxtValidity.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(39, 39, 39)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(67, 67, 67)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(34, 34, 34)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jCreditNoteNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(m_jDiscountLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(m_jStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(m_jValidity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(15, 15, 15)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jCreditNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jcustomer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtValidity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 284, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(m_jCreditNoteNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(20, 20, 20)
                        .add(m_jDiscountLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(21, 21, 21)
                        .add(m_jStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(21, 21, 21)
                        .add(m_jValidity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(16, 16, 16)
                        .add(m_jCreditNote, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(m_jcustomer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(m_jAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(m_jTxtStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16)
                        .add(m_jTxtValidity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jCreditNoteListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jCreditNoteListValueChanged
        // TODO add your handling code here:
        if (evt.getValueIsAdjusting()) {
            System.out.println("inside value changed -- list selection event fired");
            updateMode = true;
            String creditNo = null;
            List<CreditNoteinfo> credit = null;
            try {
                creditNo = m_jCreditNoteList.getSelectedValue().toString();
            } catch (Exception ex) {
                System.out.println("unknown exception");
            }
            String amount = null;
            
            try {
               credit = PurchaseOrder.getCreditNotedataList(creditNo);
            } catch (BasicException ex) {
                Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            String status = credit.get(0).getstatus();
            m_jCreditNote.setText(credit.get(0).getcreditNote());
            m_jcustomer.setText(credit.get(0).getcustomer());
            m_jAmount.setText(credit.get(0).getAmount());
            if(status.equals("N")){
                status="Open";
            }else{
                status="Close";
            }
            m_jTxtStatus.setText(status);
         //   System.out.println("enrtr--"+credit.get(0).getValidity().toString());
          //  Date validyDate = credit.get(0).getValidity();
         //   DateFormat formatter ;
          //  formatter = new SimpleDateFormat("dd-MM-yyyy");
            //String str_date = validyDate.getDate()+"-"+validyDate.getMonth()+""+validyDate.getYear();
           // System.out.println("entrr --"+str_date);
//date = (Date)((validyDate.get(Calendar.MONTH) + 1) +"-"+ validyDate.get(Calendar.DATE)+"-"+validyDate.get(Calendar.YEAR));
            m_jTxtValidity.setText(credit.get(0).getValidDateForPrint());
        }
    }//GEN-LAST:event_m_jCreditNoteListValueChanged

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

        if (m_jAmount.getText().equals("")) {
            JOptionPane.showMessageDialog(CreditNoteEditor.this, "Please enter values");
        } else {
            try {
                saveButtonAction();
            } catch (BasicException ex) {
                Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed

        //String maxPercent = null;
        //try {
        //    String maxId = "" + dlCustomers2.getMaxPercentage();
        //    maxPercent = maxId + "%";
        //} catch (BasicException ex) {
        //    Logger.getLogger(DiscountsEditor.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //m_jDiscountPercentage.setText(maxPercent);
        m_jCreditNote.setText("");
        m_jAmount.setText(null);
        m_jCreditNoteList.clearSelection();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jCreditNoteList.getSelectedIndex();
        System.out.println("index is " + index);

        if (index == -1) {
            JOptionPane.showMessageDialog(CreditNoteEditor.this, "Please select discounts in list");
        } else {
            try {
                String val = m_jCreditNoteList.getSelectedValue().toString();
                PurchaseOrder.deleteDiscountLine(val);
                m_jCreditNote.setText("");
                m_jAmount.setText("");

            } catch (BasicException ex) {
                Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                populateList(PurchaseOrder);
            } catch (BasicException ex) {
                Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void m_jCreditNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCreditNoteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_m_jCreditNoteActionPerformed

    private void jbtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrintActionPerformed
        CreditNoteinfo credit = new CreditNoteinfo();
        printTicket("Printer.DuplicateCreditNote",credit);// TODO add your handling code here:
    }//GEN-LAST:event_jbtnPrintActionPerformed


    private void printTicket(String sresourcename, CreditNoteinfo credit) {
      
             String creditNo =  m_jCreditNote.getText();
        try {
            CreditNotelist = PurchaseOrder.getCreditNotedataList(creditNo);
        } catch (BasicException ex) {
            Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
            java.util.List<CreditNoteinfo> CreditNoteinfo =  new java.util.ArrayList<CreditNoteinfo>();
        //try {
            CreditNoteinfo = CreditNotelist;
            //applicationDetails = new ListKeyed<ApplicationInfo>(applicationInfo);
      //  } catch (BasicException ex) {
       //     Logger.getLogger(CreditNoteEditor.class.getName()).log(Level.SEVERE, null, ex);
       // }


          String sresource = m_dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(CreditNoteEditor.this);
        } else {
            try {
                System.out.println("CreditNoteinfo)CreditNoteinfo.get(0)"+ CreditNoteinfo.get(0).getcreditNote()+"---"+CreditNoteinfo.get(0).getValidity());
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("credit", (CreditNoteinfo)CreditNoteinfo.get(0));
                 //script.put("kot", kot);
                m_TTP.printTicket(script.eval(sresource).toString());
             //   script.put("place", ticketext);
               // m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(CreditNoteEditor.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(CreditNoteEditor.this);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jSaver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnPrint;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JTextField m_jAmount;
    private javax.swing.JTextField m_jCreditNote;
    private javax.swing.JList m_jCreditNoteList;
    private javax.swing.JLabel m_jCreditNoteNoLabel;
    private javax.swing.JLabel m_jDiscountLabel;
    private javax.swing.JLabel m_jStatus;
    private javax.swing.JTextField m_jTxtStatus;
    private javax.swing.JTextField m_jTxtValidity;
    private javax.swing.JLabel m_jValidity;
    private javax.swing.JTextField m_jcustomer;
    // End of variables declaration//GEN-END:variables

    public void populateList(PurchaseOrderReceipts purchaseOrder) throws BasicException {

        model = new DefaultListModel();
        m_jCreditNoteList.setModel(model);
        list = purchaseOrder.getCreditNoteList();
        String[] dListId = null;
        String[] dListAmount = null;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("getPercentage " + list.get(i).getcreditNote());
            String listid = list.get(i).getcreditNote();
            model.add(i, listid);
        }
    }

private void saveButtonAction() throws BasicException {

        String creditNo = m_jCreditNote.getText();
        String amount = m_jAmount.getText();
       // double rate = Double.parseDouble(amount);
   
                
                  //  String id = list.get(m_jCreditNoteList.getSelectedIndex()).getId();
                  //  PurchaseOrder.updateDiscountLine(id, creditNo, amount);
                  //  updateMode = false;
                
                populateList(PurchaseOrder);
                m_jCreditNote.setText("");
                m_jAmount.setText("");
         
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

  /*  private boolean checkDiscountNameAvl(String percentage) throws BasicException {
        String name = PurchaseOrder.getDiscountName(percentage);
        if ("NONAME".equalsIgnoreCase(name)) {
            return false;
        } else {
            return true;
        }
    }*/
}
