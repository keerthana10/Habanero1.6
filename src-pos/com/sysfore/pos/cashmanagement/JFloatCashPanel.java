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
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DiscountRateinfo;
import com.openbravo.pos.sales.ProcessInfo;

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
public class JFloatCashPanel extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
   
    private TicketParser m_TTP;
     protected DataLogicSystem dlSystem;
     static PurchaseOrderReceipts PurchaseOrder = null;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    private static Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
    private String user;
    List<FloatCashSetupinfo> floatCashInfo = null;
    private DataLogicSales m_dlSales;
    public JFloatCashPanel() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        user = m_App.getAppUserView().getUser().getId();
        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
   // JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
         initComponents();
  
        setVisible(true);
        //add(m_jDiscountList);
        loadFloatCash();
        // jLabel1.setText("");
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
       
    }
    
    
    public void loadFloatCash(){
     try {
            floatCashInfo = (List<FloatCashSetupinfo>) PurchaseOrder.getFloatCashSetupDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(floatCashInfo.size()==0){
            m_jtxtFloatAmt.setText("0");

        }else{
            m_jTxtDate.setText(floatCashInfo.get(0).getCreatedDate());
            m_jtxtFloatAmt.setText((floatCashInfo.get(0).getFloatCash()).toString());
        }
    }
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.FloatCash");
    }

    public void activate() throws BasicException {
      // setFloatCashCount();
        loadFloatCash();
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
        m_jTxtDate = new javax.swing.JTextField();
        m_jLblDate = new javax.swing.JLabel();
        m_jbtndate = new javax.swing.JButton();
        m_jtxtFloatAmt = new javax.swing.JTextField();
        m_LblFloatAmt = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
                .addGap(299, 299, 299)
                .addComponent(jbtnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSave)
                .addGap(228, 228, 228))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGroup(jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSaverLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbtnNew)
                    .addComponent(jbtnSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jSaver, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, 27));

        m_jTxtDate.setEditable(false);
        m_jTxtDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        add(m_jTxtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 200, 28));

        m_jLblDate.setText("Date");
        add(m_jLblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 90, 20));

        m_jbtndate.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtndate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtndate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtndateActionPerformed(evt);
            }
        });
        add(m_jbtndate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 40, -1));

        m_jtxtFloatAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        add(m_jtxtFloatAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 200, 24));

        m_LblFloatAmt.setText("Float Amount");
        add(m_LblFloatAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 120, 19));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 4, 670, 20));
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
m_jTxtDate.setText(null);
m_jtxtFloatAmt.setText(null);
        //String maxPercent = null;
        //try {
        //    String maxId = "" + dlCustomers2.getMaxPercentage();
        //    maxPercent = maxId + "%";
        //} catch (BasicException ex) {
        //    Logger.getLogger(DiscountsEditor.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //m_jDiscountPercentage.setText(maxPercent);
       
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        String date = m_jTxtDate.getText();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date floatCashDate = null;
        try {
            floatCashDate = format.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        Date createdDate = new Date();
        String floatAmt = m_jtxtFloatAmt.getText();
        String autoFloat;
        String resetPeriod;
        autoFloat = "Y";


       if(!pNum.matcher(floatAmt).matches()){
                showMsgRed(this, "Please enter the valid float amount");
                m_jtxtFloatAmt.setText("");
         }else{
            double floatCash = Double.parseDouble(floatAmt);
            String posNo = m_App.getProperties().getPosNo();

            PurchaseOrder.insertFloatCashSetup(floatCashDate, floatCash,  autoFloat, createdDate, user);
            try {
                int floatCount=0;
                if(!posNo.equals("")){
                    try {
                        floatCount = PurchaseOrder.getFloatCount(posNo);
                    } catch (BasicException ex) {
                        Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  if(floatCount==0){
                    PurchaseOrder.insertFloatAmt(floatCash, posNo);
                  }else{
                    PurchaseOrder.updateFloatCash(floatCash, posNo);
                  }
                 // ProcessInfo.setProcessInfo("Float Cash",m_dlSales);
                //  setFloatCashCount();
                }
            } catch (BasicException ex) {
                Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            showMessage(this, "Float Cash Setup updated successfully");

             }
}//GEN-LAST:event_jbtnSaveActionPerformed



     private void showMsgRed(JFloatCashPanel aThis,String msg) {
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

    private void showMessage(JFloatCashPanel aThis, String msg) {
         JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
    }
     public void setFloatCashCount(){

    int processCount = 0;
     processCount = ProcessInfo.setProcessCount("Float Cash", m_dlSales);
        if(processCount>=10){
              jLabel2.setVisible(true);
              jLabel2.setText("This feature is available for Professional edition.To continue using the same kindly upgrade to Professional Edition.");
              jbtnNew.setEnabled(false);
              jbtnSave.setEnabled(false);
        }
}
    private void m_jbtndateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtndateActionPerformed

        Date date = null;
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        date = JCalendarDialog.showCalendarTime(this, date);
        if (date != null) {
            m_jTxtDate.setText(sdf.format(date).toString());
        }
}//GEN-LAST:event_m_jbtndateActionPerformed
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jSaver;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel m_LblFloatAmt;
    private javax.swing.JLabel m_jLblDate;
    private javax.swing.JTextField m_jTxtDate;
    private javax.swing.JButton m_jbtndate;
    private javax.swing.JTextField m_jtxtFloatAmt;
    // End of variables declaration//GEN-END:variables
    
}
