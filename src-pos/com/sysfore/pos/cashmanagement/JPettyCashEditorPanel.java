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

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DiscountRateinfo;

import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author adrianromero
 */
public class JPettyCashEditorPanel extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;

     protected DataLogicSystem dlSystem;
     static PurchaseOrderReceipts PurchaseOrder = null;

    public boolean updateMode = false;
    private static Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
    private String user;
    List<FloatCashSetupinfo> floatCashInfo = null;
    private static Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
    private String availableAmt = null;
    //private static Pattern pNum = Pattern.compile(".*[0-9].*");

    public JPettyCashEditorPanel() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        m_App = app;
        initComponents();
        user = app.getAppUserView().getUser().getId();
        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
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
       
        m_jTxtAvailAmt.setText(availableAmt);

        setVisible(true);


    }
    
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.Expenses");
    }

    public void activate() throws BasicException {
     
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


   

    private class FormatsPayment extends Formats {
        protected String formatValueInt(Object value) {
            return AppLocal.getIntString("transpayment." + (String) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }         
    }    
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jLblPettyCash = new javax.swing.JLabel();
        m_jTxtPettyCash = new javax.swing.JTextField();
        m_jTxtDate = new javax.swing.JTextField();
        m_jCboStatus = new javax.swing.JComboBox();
        m_jTxtAvailAmt = new javax.swing.JTextField();
        m_jTxtAmt = new javax.swing.JTextField();
        m_jTxtReason = new javax.swing.JTextField();
        m_jLblReason = new javax.swing.JLabel();
        m_jLblAmt = new javax.swing.JLabel();
        m_LblAvailAmt = new javax.swing.JLabel();
        jLblStatus = new javax.swing.JLabel();
        m_jLblDate = new javax.swing.JLabel();
        m_jbtndate = new javax.swing.JButton();

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

        javax.swing.GroupLayout jSaverLayout = new javax.swing.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(288, 288, 288)
                .addComponent(jbtnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSave)
                .addGap(170, 170, 170))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jSaverLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnSave)
                    .addComponent(jbtnNew))
                .addContainerGap())
        );

        m_jLblPettyCash.setText("Petty Cash");

        m_jTxtPettyCash.setEditable(false);
        m_jTxtPettyCash.setPreferredSize(new java.awt.Dimension(200, 24));

        m_jTxtDate.setEditable(false);
        m_jTxtDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        m_jCboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Return", "Issue" }));
        m_jCboStatus.setMinimumSize(new java.awt.Dimension(74, 24));
        m_jCboStatus.setPreferredSize(new java.awt.Dimension(79, 24));

        m_jTxtAvailAmt.setEditable(false);
        m_jTxtAvailAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jTxtAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jTxtReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        m_jLblReason.setText("Reason");

        m_jLblAmt.setText("Amount");

        m_LblAvailAmt.setText("Available Amount");

        jLblStatus.setText("Status");

        m_jLblDate.setText("Date");

        m_jbtndate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtndate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtndateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(m_jLblPettyCash, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(m_jTxtPettyCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(m_jLblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(m_jTxtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(m_jbtndate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(m_jLblReason, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(m_jTxtReason, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(m_LblAvailAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(m_jTxtAvailAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(60, 60, 60)
                                    .addComponent(m_jCboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(m_jLblAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(70, 70, 70)
                                    .addComponent(m_jTxtAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jLblPettyCash, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(m_jTxtPettyCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jLblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jTxtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jbtndate))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jCboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_LblAvailAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jTxtAvailAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jLblAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jTxtAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jLblReason, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jTxtReason, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(278, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
    m_jTxtReason.setText(null);
        m_jTxtDate.setText(null);
        //m_jTxtAvailAmt.setText(null);
        m_jTxtAmt.setText(null);
//        m_jtxtIssued.setText(null);
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

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
        Date pettyCashDate = new Date();
        try {
            pettyCashDate = format.parse(date);
            System.out.println("pettyCashDate--"+pettyCashDate);
        } catch (ParseException ex) {
            Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        balanceAmt = Double.parseDouble(m_jTxtAvailAmt.getText());


//        String issuedTo = m_jtxtIssued.getText();
        if(date.equals("")){
            showMsgRed(this, "Please enter the date");
        }else{
              if(amount.equals("")){
                 showMsgRed(this, "Please enter the amount");
                 m_jTxtAmt.setText("");
              }
              else if(!pNum.matcher(amount).matches()){
                 showMsgRed(this, "Please enter the valid amount");
                 m_jTxtAmt.setText("");
              }

              else{
                    if(Reason.equals("")){
                        showMsgRed(this, "Please enter the reason");
                        m_jTxtReason.setText("");
                    }
                    else if (!p.matcher(Reason).matches())
                    {
                        showMsgRed(this, "Please enter the valid reason");
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
                                showMsgRed(this, "Entered amount is greater than available amount");
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
}
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

}//GEN-LAST:event_jbtnSaveActionPerformed

    private void m_jbtndateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtndateActionPerformed

        Date date = null;
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        date = JCalendarDialog.showCalendarTime(this, date);
        if (date != null) {
            m_jTxtDate.setText(sdf.format(date).toString());
        }
}//GEN-LAST:event_m_jbtndateActionPerformed



     private void showMsgRed(JPettyCashEditorPanel aThis,String msg) {
        JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
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

  private JPanel getLabelRedPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.red);
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}

    private void showMessage(JPettyCashEditorPanel aThis, String msg) {
       JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    } 

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
    
}
