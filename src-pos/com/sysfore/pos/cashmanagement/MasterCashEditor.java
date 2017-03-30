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
//    MERCHANTABILFinITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.sysfore.pos.cashmanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.text.ParseException;
import javax.swing.*;
import com.openbravo.pos.forms.DataLogicSystem;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class MasterCashEditor extends JDialog {

    public javax.swing.JDialog dEdior = null;

    private static PurchaseOrderReceipts PurchaseOrder = null;
    public boolean updateMode = false;
    static int x = 400;
    static int y = 200;
    private Date Currentdate;
    protected AppView app;
    private static DataLogicSystem m_dlSystem;
   // private static Pattern pNum = Pattern.compile(".*[0-9].*");
    private static Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");

    private String user;
    private String availableAmt = null;
    List<CashSetupinfo> cashInfo = null;
    public MasterCashEditor(AppView m_app, JPanel parent) throws BasicException {
      
    }

    private MasterCashEditor(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, 662, 562);
    }

    private MasterCashEditor(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, 662, 562);
    }

    public static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {
       
        MasterCashEditor.PurchaseOrder=PurchaseOrder;
        
        // PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        Window window = getWindow(parent);
        MasterCashEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new MasterCashEditor((Frame) window, true);
        } else {
            myMsg = new MasterCashEditor((Dialog) window, true);
        }
            myMsg.init(PurchaseOrder, m_App);
      
    }
private static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder) throws BasicException {

        Window window = getWindow(parent);
        MasterCashEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new MasterCashEditor((Frame) window, true);
        } else {
            myMsg = new MasterCashEditor((Dialog) window, true);
        }
      // myMsg.init(PurchaseOrder);
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
    public void init(PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {

        initComponents();
        user = m_App.getAppUserView().getUser().getId();
       
       
        try {
            cashInfo =  (List<CashSetupinfo>) PurchaseOrder.getCashSetupDetails();
            } catch (BasicException ex) {
                Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        if(cashInfo.size()==0){
            m_jtxtFloatAmt.setText("0");
            m_jTxtPettyAmt.setText("0");
            m_jWeekly.setSelected(false);// TODO add your handling code here:
            m_jLblDay.setVisible(false);
            m_jLblResetDate.setVisible(true);
            m_jCboDate.setVisible(true);
            m_jCboDay.setVisible(false);
            m_jMonthly.setSelected(true);
            m_jCboDate.setSelectedIndex(-1);
            m_jCboDay.setSelectedIndex(-1);

        }else{
            boolean autoSelect;
            String resetPeriod;
            resetPeriod = cashInfo.get(0).getResetPeriod();
            if(cashInfo.get(0).getAutoFloat().equals("Y")){
                autoSelect=true;
            }else{
                autoSelect=false;
            }
            m_jTxtDate.setText(cashInfo.get(0).getCreatedDate());
            m_jtxtFloatAmt.setText((cashInfo.get(0).getFloatCash()).toString());
            m_jTxtPettyAmt.setText((cashInfo.get(0).getPettyCash()).toString());
            jCbAuto.setSelected(autoSelect);
            if(resetPeriod.equals("M")){
                m_jMonthly.setSelected(true);
                m_jCboDate.setSelectedItem(cashInfo.get(0).getResetDate());
                m_jWeekly.setSelected(false);// TODO add your handling code here:
                m_jLblDay.setVisible(false);
                m_jLblResetDate.setVisible(true);
                m_jCboDate.setVisible(true);
                m_jCboDay.setVisible(false);
                m_jMonthly.setSelected(true);
                m_jCboDay.setSelectedIndex(-1);
            }else{
                m_jWeekly.setSelected(true);
                m_jCboDay.setSelectedItem(cashInfo.get(0).getResetDay());
                m_jLblDay.setVisible(true);
                m_jLblResetDate.setVisible(false);
                m_jCboDate.setVisible(false);
                m_jCboDay.setVisible(true);
                m_jCboDate.setSelectedIndex(-1);
            }
        }
     
        setTitle("Master Cash Setup");
        setVisible(true);


    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jLblDate = new javax.swing.JLabel();
        m_LblFloatAmt = new javax.swing.JLabel();
        m_jtxtFloatAmt = new javax.swing.JTextField();
        m_jLblPettyAmt = new javax.swing.JLabel();
        m_jTxtPettyAmt = new javax.swing.JTextField();
        m_jTxtDate = new javax.swing.JTextField();
        m_jbtndate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jCbAuto = new javax.swing.JCheckBox();
        m_jLblPettyReset = new javax.swing.JLabel();
        m_jWeekly = new javax.swing.JRadioButton();
        m_jMonthly = new javax.swing.JRadioButton();
        m_jLblDay = new javax.swing.JLabel();
        m_jCboDay = new javax.swing.JComboBox();
        m_jLblResetDate = new javax.swing.JLabel();
        m_jCboDate = new javax.swing.JComboBox();

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

        m_LblFloatAmt.setText("Float Amount");

        m_jtxtFloatAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jLblPettyAmt.setText("Petty Cash Amount");

        m_jTxtPettyAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jTxtDate.setEditable(false);
        m_jTxtDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        m_jbtndate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtndate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtndateActionPerformed(evt);
            }
        });

        jLabel1.setText("Float Amount Setting Up Automatically");

        m_jLblPettyReset.setText("Petty Cash Reset Period:");

        m_jWeekly.setText("Weekly");
        m_jWeekly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jWeeklyActionPerformed(evt);
            }
        });

        m_jMonthly.setText("Monthly");
        m_jMonthly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMonthlyActionPerformed(evt);
            }
        });

        m_jLblDay.setText("Day");

        m_jCboDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" }));

        m_jLblResetDate.setText("Reset Date");

        m_jCboDate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", " " }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(39, 39, 39)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_jLblDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(130, 130, 130)
                .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(m_jbtndate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_LblFloatAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(100, 100, 100)
                .add(m_jtxtFloatAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jCbAuto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_jLblPettyAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(100, 100, 100)
                .add(m_jTxtPettyAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(m_jLblPettyReset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(59, 59, 59)
                .add(m_jMonthly, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(m_jWeekly, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(120, 120, 120)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblResetDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jLblDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(107, 107, 107)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jCboDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCboDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jSaver, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jbtndate))
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_LblFloatAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jtxtFloatAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCbAuto))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblPettyAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtPettyAmt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(16, 16, 16)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(m_jLblPettyReset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jMonthly)
                    .add(m_jWeekly))
                .add(17, 17, 17)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jLblResetDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jLblDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(m_jCboDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCboDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents
   private Date getFormattedDate(String strDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date newDate =  new Date();
//        try {
//            newDate = (Date) format.parse(strDate);
//        } catch (ParseException ex) {
//        }
        return newDate;
    }
    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        String date = m_jTxtDate.getText();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date pettyCashDate = null;
        try {
            pettyCashDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        Date createdDate = new Date();
        String floatAmt = m_jtxtFloatAmt.getText();
        String pettyAmt = m_jTxtPettyAmt.getText();
        String autoFloat;
        String resetPeriod;
          
        if(jCbAuto.isSelected()==false){
            autoFloat = "N";
        }else{
            autoFloat = "Y";
        }
        if(m_jMonthly.isSelected()==true){
            resetPeriod = "M";
        }else{
            resetPeriod = "W";
        }
       String resetDate = (String) m_jCboDate.getSelectedItem();
        String resetDay = (String) m_jCboDay.getSelectedItem();
       if(!pNum.matcher(floatAmt).matches()){
                showMessage(this, "Please enter the valid float amount");
                m_jtxtFloatAmt.setText("");
         }else{
             if(!pNum.matcher(pettyAmt).matches()){
                showMessage(this, "Please enter the valid petty cash amount");
                m_jTxtPettyAmt.setText("");
             }
             else{
                if(m_jMonthly.isSelected()==true){
                    if(m_jCboDate.getSelectedIndex() == -1){
                         showMessage(this, "Please Select the Reset Date");
                    }else{
                         double floatCash = Double.parseDouble(floatAmt);
                         double pettyCash = Double.parseDouble(pettyAmt);
                         PurchaseOrder.insertMasterCashSetup(floatCash, pettyCash, autoFloat, createdDate, user,pettyCashDate,resetPeriod,resetDate, resetDay);
                         insertPettyCash();
                         showMessage(this, "Cash Setup updated successfully");
                    }
                }else{
                    if(m_jWeekly.isSelected()==true){
                         if(m_jCboDay.getSelectedIndex() == -1){
                             showMessage(this, "Please Select the Day");
                         
                         }else{
                                 double floatCash = Double.parseDouble(floatAmt);
                                 double pettyCash = Double.parseDouble(pettyAmt);
                                 PurchaseOrder.insertMasterCashSetup(floatCash, pettyCash, autoFloat, createdDate, user,pettyCashDate,resetPeriod,resetDate, resetDay);
                                 insertPettyCash();
                                 showMessage(this, "Cash Setup updated successfully");
                         }
               
             }
         }}}
     
}//GEN-LAST:event_jbtnSaveActionPerformed
    public void insertPettyCash(){
      
       double newPettyCash = Double.parseDouble(m_jTxtPettyAmt.getText());
       double availableAmt = 0;
       double balanceAmt;
       Date createdDate = new Date();
       Date pettyDate = null;
       SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
       String pettyCashDate = format.format(createdDate).toString();
        try {
            pettyDate = format.parse(pettyCashDate);
        } catch (ParseException ex) {
            Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

       if(cashInfo.size()==0){
           balanceAmt = newPettyCash;
           try {
                    PurchaseOrder.insertPettyCash("Reset", "", newPettyCash, 0, balanceAmt, createdDate, user,pettyDate);
                } catch (BasicException ex) {
                    Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
       }else{
            double existingPettyCash = cashInfo.get(0).getPettyCash();
             try {
                availableAmt = Double.parseDouble(PurchaseOrder.getBalanceAmt().list().get(0).toString());
            } catch (BasicException ex) {
                Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(existingPettyCash!=newPettyCash){
                balanceAmt = (newPettyCash-existingPettyCash)+availableAmt;
                try {
                    PurchaseOrder.insertPettyCash("Reissue", "", newPettyCash, 0, balanceAmt, createdDate, user,pettyDate);
                } catch (BasicException ex) {
                    Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private void showMessage(MasterCashEditor aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
    }
    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed

      
        m_jTxtDate.setText(null);
        //m_jTxtAvailAmt.setText(null);
        m_jTxtPettyAmt.setText(null);
        m_jWeekly.setSelected(false);// TODO add your handling code here:
        m_jLblDay.setVisible(false);
        m_jLblResetDate.setVisible(true);
        m_jCboDate.setVisible(true);
        m_jCboDay.setVisible(false);
        m_jMonthly.setSelected(true);
        m_jCboDate.setSelectedIndex(-1);
        m_jCboDay.setSelectedIndex(-1);
       
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

    private void m_jWeeklyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jWeeklyActionPerformed
        m_jMonthly.setSelected(false);// TODO add your handling code here:
        m_jLblDay.setVisible(true);
        m_jLblResetDate.setVisible(false);
        m_jCboDate.setVisible(false);
        m_jCboDay.setVisible(true);
        m_jWeekly.setSelected(true);
        m_jCboDate.setSelectedIndex(-1);
    }//GEN-LAST:event_m_jWeeklyActionPerformed

    private void m_jMonthlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMonthlyActionPerformed
        m_jWeekly.setSelected(false);// TODO add your handling code here:
        m_jLblDay.setVisible(false);
        m_jLblResetDate.setVisible(true);
        m_jCboDate.setVisible(true);
        m_jCboDay.setVisible(false);
        m_jMonthly.setSelected(true);
        m_jCboDay.setSelectedIndex(-1);
    }//GEN-LAST:event_m_jMonthlyActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCbAuto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jSaver;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel m_LblFloatAmt;
    private javax.swing.JComboBox m_jCboDate;
    private javax.swing.JComboBox m_jCboDay;
    private javax.swing.JLabel m_jLblDate;
    private javax.swing.JLabel m_jLblDay;
    private javax.swing.JLabel m_jLblPettyAmt;
    private javax.swing.JLabel m_jLblPettyReset;
    private javax.swing.JLabel m_jLblResetDate;
    private javax.swing.JRadioButton m_jMonthly;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JTextField m_jTxtPettyAmt;
    private javax.swing.JRadioButton m_jWeekly;
    private javax.swing.JButton m_jbtndate;
    private javax.swing.JTextField m_jtxtFloatAmt;
    // End of variables declaration//GEN-END:variables


    

}
