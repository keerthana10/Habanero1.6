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
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import java.awt.Component;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class PettyCashEditor extends JDialog {

    public javax.swing.JDialog dEdior = null;

    private static PurchaseOrderReceipts PurchaseOrder = null;
    public boolean updateMode = false;
    static int x = 400;
    static int y = 200;
    private Date Currentdate;
    protected AppView app;
    private static DataLogicSystem m_dlSystem;
    private static Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
    //private static Pattern pNum = Pattern.compile(".*[0-9].*");
    private static Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
    private String user;
    private String availableAmt = null;

    public PettyCashEditor(AppView m_app, JPanel parent) throws BasicException {
     
    }

    private PettyCashEditor(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, 662, 562);
    }

    private PettyCashEditor(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, 662, 562);
    }

    public static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {
       
        PettyCashEditor.PurchaseOrder=PurchaseOrder;
        // PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        Window window = getWindow(parent);
        PettyCashEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new PettyCashEditor((Frame) window, true);
        } else {
            myMsg = new PettyCashEditor((Dialog) window, true);
        }
            myMsg.init(PurchaseOrder, m_App);
      
    }

    public void init(PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {

        initComponents();
        user = m_App.getAppUserView().getUser().getId();
        String pettyCash = null;
        try {
            pettyCash = PurchaseOrder.getPettyCash().list().get(0).toString();
            if (pettyCash == null || pettyCash.isEmpty() || pettyCash.equals("")) {
                pettyCash = "0";
            }
        } 
        catch (Exception ex) {
            pettyCash = "0";
          }
           try {
            availableAmt = PurchaseOrder.getBalanceAmt().list().get(0).toString();
            if (availableAmt == null || availableAmt.isEmpty() || availableAmt.equals("")) {
                availableAmt = "0";
            }
        }
        catch (Exception ex) {
            availableAmt = "0";
         }
       
         m_jTxtPettyCash.setText(pettyCash.toString());
        // Double availableAmt = PurchaseOrder.getBalanceAmt();
         System.out.println("enrtr---"+availableAmt);
       if(availableAmt == "0"){
         availableAmt =pettyCash;
      }
         /*if(availableAmt==null){
             availableAmt = pettyCash;
         }*/
        m_jTxtAvailAmt.setText(availableAmt);
        setTitle("Petty Cash");
        setVisible(true);


    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jTxtReason = new javax.swing.JTextField();
        m_jLblReason = new javax.swing.JLabel();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jLblDate = new javax.swing.JLabel();
        jLblStatus = new javax.swing.JLabel();
        m_LblAvailAmt = new javax.swing.JLabel();
        m_jTxtAvailAmt = new javax.swing.JTextField();
        m_jLblAmt = new javax.swing.JLabel();
        m_jTxtAmt = new javax.swing.JTextField();
        m_jCboStatus = new javax.swing.JComboBox();
        m_jTxtDate = new javax.swing.JTextField();
        m_jbtndate = new javax.swing.JButton();
        m_jLblPettyCash = new javax.swing.JLabel();
        m_jTxtPettyCash = new javax.swing.JTextField();

        m_jTxtReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jLblReason.setText("Reason");

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

        org.jdesktop.layout.GroupLayout jSaverLayout = new org.jdesktop.layout.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSaverLayout.createSequentialGroup()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(330, 330, 330)
                .add(jbtnNew)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jbtnSave)
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSaverLayout.createSequentialGroup()
                .add(jSaverLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jbtnNew)
                    .add(jbtnSave))
                .addContainerGap())
        );

        m_jLblDate.setText("Date");

        jLblStatus.setText("Status");

        m_LblAvailAmt.setText("Available Amount");

        m_jTxtAvailAmt.setEditable(false);
        m_jTxtAvailAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jLblAmt.setText("Amount");

        m_jTxtAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jCboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Return", "Issue" }));
        m_jCboStatus.setMinimumSize(new java.awt.Dimension(74, 24));
        m_jCboStatus.setPreferredSize(new java.awt.Dimension(79, 24));

        m_jTxtDate.setEditable(false);
        m_jTxtDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        m_jbtndate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtndate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtndateActionPerformed(evt);
            }
        });

        m_jLblPettyCash.setText("Petty Cash");

        m_jTxtPettyCash.setEditable(false);
        m_jTxtPettyCash.setPreferredSize(new java.awt.Dimension(200, 24));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(39, 39, 39)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_jLblPettyCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(m_jTxtPettyCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_jLblDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(50, 50, 50)
                .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(m_jbtndate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(m_jLblReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jTxtReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(m_LblAvailAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(20, 20, 20)
                        .add(m_jTxtAvailAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLblStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(60, 60, 60)
                        .add(m_jCboStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(m_jLblAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(70, 70, 70)
                        .add(m_jTxtAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(24, 24, 24)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblPettyCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(m_jTxtPettyCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jbtndate))
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLblStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCboStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_LblAvailAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtAvailAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(152, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents
   private Date getFormattedDate(String strDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date newDate = new Date();
        try {
            newDate = (Date) format.parse(strDate);
        } catch (ParseException ex) {
        }
        return newDate;
    }
    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

        String date = m_jTxtDate.getText();
        Date created = new Date();
        int status = m_jCboStatus.getSelectedIndex();
        String statusValue = null;
        double amt;
        double balanceAmt = 0;
        String Reason = m_jTxtReason.getText();
        String amount = m_jTxtAmt.getText();
        double pettyCashamt =  Double.parseDouble(m_jTxtPettyCash.getText());

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date pettyCashDate = null;
        try {
            pettyCashDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        balanceAmt = Double.parseDouble(m_jTxtAvailAmt.getText());
        

//        String issuedTo = m_jtxtIssued.getText();
        if(date.equals("")){
            showMessage(this, "Please enter the date");
        }else{
              if(amount.equals("")){
                 showMessage(this, "Please enter the amount");
                 m_jTxtAmt.setText("");
              }
              else if(!pNum.matcher(amount).matches()){
                 showMessage(this, "Please enter the valid amount");
                 m_jTxtAmt.setText("");
              }     
              
              else{
                    if(Reason.equals("")){
                        showMessage(this, "Please enter the reason");
                        m_jTxtReason.setText("");
                    }
                    else if (!p.matcher(Reason).matches())
                    {
                        showMessage(this, "Please enter the valid reason");
                         m_jTxtReason.setText("");
                    }
                   
                 else{
                      amt = Double.parseDouble(m_jTxtAmt.getText());
                      if(status == 0){                          
                            statusValue="Return";
                            balanceAmt = balanceAmt + amt;
                            insertPettyCash(statusValue, Reason, pettyCashamt, amt, balanceAmt, created, user,pettyCashDate);
                      }else{
                            if(amt>balanceAmt){
                                showMessage(this, "Entered amount is greater than available amount");
                            }
                            else{
                                statusValue="Issue";
                                balanceAmt = balanceAmt - amt;
                                insertPettyCash(statusValue, Reason, pettyCashamt, amt, balanceAmt, created, user, pettyCashDate);
//                                PurchaseOrder.insertPettyCash(statusValue, Reason, pettyCashamt, amt, balanceAmt, pettyDate, user);
//                                m_jTxtReason.setText(null);
//                                m_jTxtDate.setText(null);
//                                m_jTxtAmt.setText(null);
//                                String newBalanceamt = PurchaseOrder.getBalanceAmt().list().get(0).toString();
//                                m_jTxtAvailAmt.setText(newBalanceamt);
//                                String newpettyCash = PurchaseOrder.getPettyAmt().list().get(0).toString();
//                                m_jTxtPettyCash.setText(newpettyCash);
                            }
                      }                    
                 }
            }
        }      
}//GEN-LAST:event_jbtnSaveActionPerformed
    public void insertPettyCash(String statusValue, String Reason, double pettyCashamt, double amt, double balanceAmt, Date created, String user, Date pettyCashDate){
        try {
            PurchaseOrder.insertPettyCash(statusValue, Reason, pettyCashamt, amt, balanceAmt, created, user,pettyCashDate);
        } catch (BasicException ex) {
            Logger.getLogger(PettyCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jTxtReason.setText(null);
        m_jTxtDate.setText(null);
        m_jTxtAmt.setText(null);
        String newBalanceamt = null;
        try {
            newBalanceamt = PurchaseOrder.getBalanceAmt().list().get(0).toString();
        } catch (BasicException ex) {
            Logger.getLogger(PettyCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jTxtAvailAmt.setText(newBalanceamt);
        String newpettyCash = null;
        try {
            newpettyCash = PurchaseOrder.getPettyAmt().list().get(0).toString();
        } catch (BasicException ex) {
            Logger.getLogger(PettyCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jTxtPettyCash.setText(newpettyCash);
    }
    private void showMessage(PettyCashEditor aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
    }
    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed

        m_jTxtReason.setText(null);
        m_jTxtDate.setText(null);
        //m_jTxtAvailAmt.setText(null);
        m_jTxtAmt.setText(null);
//        m_jtxtIssued.setText(null);
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void m_jbtndateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtndateActionPerformed

        Date date = null;
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        date = JCalendarDialog.showCalendarTime(this, date);
        if (date != null) {
            m_jTxtDate.setText(sdf.format(date).toString());
        }   
}//GEN-LAST:event_m_jbtndateActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLblStatus;
    private javax.swing.JPanel jSaver;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel m_LblAvailAmt;
    private javax.swing.JComboBox m_jCboStatus;
    private javax.swing.JLabel m_jLblAmt;
    private javax.swing.JLabel m_jLblDate;
    private javax.swing.JLabel m_jLblPettyCash;
    private javax.swing.JLabel m_jLblReason;
    private javax.swing.JTextField m_jTxtAmt;
    private javax.swing.JTextField m_jTxtAvailAmt;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JTextField m_jTxtPettyCash;
    private javax.swing.JTextField m_jTxtReason;
    private javax.swing.JButton m_jbtndate;
    // End of variables declaration//GEN-END:variables


    private static Window getWindow(Component parent) {

        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

}
