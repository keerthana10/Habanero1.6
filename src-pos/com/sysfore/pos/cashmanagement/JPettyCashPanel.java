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
import com.sysfore.pos.accountingconfig.ExportDetails;
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
public class JPettyCashPanel extends JPanel implements JPanelView,BeanFactoryApp{
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
     List<PettyCashSetupinfo> pettyCashInfo = null;
    private static Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");

    private String user;
    private DataLogicSales m_dlSales;
  //  static int y = 200;
    /** Creates new form JPanelCloseMoney */
    public JPettyCashPanel() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        user = m_App.getAppUserView().getUser().getId();
        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
     //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
   // JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
         initComponents();
        loadPettyCash();
        //jLabel1.setText("");
        setVisible(true);

        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
    }
      public void loadPettyCash(){
     try {
            pettyCashInfo = (List<PettyCashSetupinfo>) PurchaseOrder.getPettyCashSetupDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JFloatCashPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(pettyCashInfo.size()==0){
            m_jTxtPettyAmt.setText("0");
            m_jWeekly.setSelected(false);// TODO add your handling code here:
         //   m_jLblDay.setVisible(false);
            m_jLblResetDate.setVisible(true);
            m_jCboDate.setVisible(true);
            m_jCboDay.setVisible(false);
            m_jMonthly.setSelected(true);
            m_jCboDate.setSelectedIndex(-1);
            m_jCboDay.setSelectedIndex(-1);


        }else{
            boolean autoSelect;
            String resetPeriod;
            resetPeriod = pettyCashInfo.get(0).getResetPeriod();


            m_jTxtPettyAmt.setText((pettyCashInfo.get(0).getPettyCash()).toString());
            if(resetPeriod.equals("M")){
                m_jMonthly.setSelected(true);
                m_jCboDate.setSelectedItem(pettyCashInfo.get(0).getResetDate());
                m_jWeekly.setSelected(false);// TODO add your handling code here:
              //  m_jLblDay.setVisible(false);
                m_jLblResetDate.setVisible(true);
                m_jCboDate.setVisible(true);
                m_jCboDay.setVisible(false);
                m_jMonthly.setSelected(true);
                m_jCboDay.setSelectedIndex(-1);
            }else{
                m_jWeekly.setSelected(true);
                m_jCboDay.setSelectedItem(pettyCashInfo.get(0).getResetDay());
               // m_jLblDay.setVisible(true);
                m_jLblResetDate.setVisible(false);
                m_jCboDate.setVisible(false);
                m_jCboDay.setVisible(true);
                m_jCboDate.setSelectedIndex(-1);
            }
        }
    }
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.PettyCash");
    }

    public void activate() throws BasicException {
          loadPettyCash();
     //     setPettyCashCount();
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
        m_jCboDate = new javax.swing.JComboBox();
        m_jMonthly = new javax.swing.JRadioButton();
        m_jWeekly = new javax.swing.JRadioButton();
        m_jCboDay = new javax.swing.JComboBox();
        m_jLblPettyReset = new javax.swing.JLabel();
        m_jLblResetDate = new javax.swing.JLabel();
        m_jLblPettyAmt = new javax.swing.JLabel();
        m_jTxtPettyAmt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

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
                .addGap(330, 330, 330)
                .addComponent(jbtnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnSave)
                .addGap(128, 128, 128))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jbtnNew)
            .addComponent(jbtnSave)
        );

        m_jCboDate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", " " }));

        m_jMonthly.setText("Monthly");
        m_jMonthly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMonthlyActionPerformed(evt);
            }
        });

        m_jWeekly.setText("Weekly");
        m_jWeekly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jWeeklyActionPerformed(evt);
            }
        });

        m_jCboDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" }));

        m_jLblPettyReset.setText("Petty Cash Reset Period:");

        m_jLblResetDate.setText("Reset Date *");

        m_jLblPettyAmt.setText("Petty Cash Amount *");

        m_jTxtPettyAmt.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(m_jLblPettyAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(m_jTxtPettyAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(m_jLblPettyReset, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(m_jMonthly, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(m_jWeekly, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(m_jLblResetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(107, 107, 107)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(m_jCboDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(m_jCboDay, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jLblPettyAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jTxtPettyAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(m_jLblPettyReset, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(m_jMonthly)
                    .addComponent(m_jWeekly))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(m_jLblResetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(m_jCboDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jCboDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(374, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
        m_jTxtPettyAmt.setText(null);
        m_jWeekly.setSelected(false);// TODO add your handling code here:
      //  m_jLblDay.setVisible(false);
        m_jLblResetDate.setVisible(true);
        m_jCboDate.setVisible(true);
        m_jCboDay.setVisible(false);
        m_jMonthly.setSelected(true);
        m_jCboDate.setSelectedIndex(-1);
        m_jCboDay.setSelectedIndex(-1);

        updateMode = false;
        //String maxPercent = null;
        //try {
        //    String maxId = "" + dlCustomers2.getMaxPercentage();
        //    maxPercent = maxId + "%";
        //} catch (BasicException ex) {
        //    Logger.getLogger(DiscountsEditor.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //m_jDiscountPercentage.setText(maxPercent);
       
}//GEN-LAST:event_jbtnNewActionPerformed

    private void showMessage(MasterCashEditor aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
    }
    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date createdDate = new Date();
        String pettyAmt = m_jTxtPettyAmt.getText();
        String resetPeriod;

        if(m_jMonthly.isSelected()==true){
            resetPeriod = "M";
        }else{
            resetPeriod = "W";
        }
       String resetDate = (String) m_jCboDate.getSelectedItem();
        String resetDay = (String) m_jCboDay.getSelectedItem();

             if(!pNum.matcher(pettyAmt).matches()){
                showMsgRed(this, "Please enter the valid petty cash amount");
                m_jTxtPettyAmt.setText("");
             }
             else{
                if(m_jMonthly.isSelected()==true){
                    if(m_jCboDate.getSelectedIndex() == -1){
                         showMsgRed(this, "Please Select the Reset Date");
                    }else{

                         double pettyCash = Double.parseDouble(pettyAmt);
                         PurchaseOrder.insertPettyCashSetup(pettyCash, createdDate, user,resetPeriod,resetDate, resetDay);
                         insertPettyCash();
                         showMessageGreen(this, "Petty Cash Setup updated successfully");
                    }
                }else{
                    if(m_jWeekly.isSelected()==true){
                         if(m_jCboDay.getSelectedIndex() == -1){
                            showMsgRed(this, "Please Select the Day");

                         }else{
                                 double pettyCash = Double.parseDouble(pettyAmt);
                                 PurchaseOrder.insertPettyCashSetup(pettyCash, createdDate, user,resetPeriod,resetDate, resetDay);
                                 insertPettyCash();
                                  showMessageGreen(this, "Petty Cash Setup updated successfully");
                         }

         }}
             // ProcessInfo.setProcessInfo("Petty Cash",m_dlSales);
            //  setPettyCashCount();
             }

}//GEN-LAST:event_jbtnSaveActionPerformed
  public void setPettyCashCount(){
    
    int processCount = 0;
     processCount = ProcessInfo.setProcessCount("Petty Cash", m_dlSales);
        if(processCount>=10){
              jLabel1.setVisible(true);
              jLabel1.setText("This feature is available for Professional edition.To continue using the same kindly upgrade to Professional Edition.");
              jbtnNew.setEnabled(false);
              jbtnSave.setEnabled(false);
        }
}
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

       if(pettyCashInfo.size()==0){
           balanceAmt = newPettyCash;
           try {
                    PurchaseOrder.insertPettyCash("Reset", "", newPettyCash, 0, balanceAmt, createdDate, user,pettyDate);
                } catch (BasicException ex) {
                    Logger.getLogger(MasterCashEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
       }else{
            double existingPettyCash = pettyCashInfo.get(0).getPettyCash();
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
    private void m_jMonthlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMonthlyActionPerformed
        m_jWeekly.setSelected(false);// TODO add your handling code here:
       // m_jLblDay.setVisible(false);
        m_jLblResetDate.setVisible(true);
        m_jCboDate.setVisible(true);
        m_jCboDay.setVisible(false);
        m_jMonthly.setSelected(true);
        m_jCboDay.setSelectedIndex(-1);
}//GEN-LAST:event_m_jMonthlyActionPerformed

    private void m_jWeeklyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jWeeklyActionPerformed
        m_jMonthly.setSelected(false);// TODO add your handling code here:
       // m_jLblDay.setVisible(true);
        m_jLblResetDate.setVisible(false);
        m_jCboDate.setVisible(false);
        m_jCboDay.setVisible(true);
        m_jWeekly.setSelected(true);
        m_jCboDate.setSelectedIndex(-1);
}//GEN-LAST:event_m_jWeeklyActionPerformed


    
    
     private void showMsgRed(JPettyCashPanel aThis,String msg) {
        JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
    }
 private void showMessageGreen(JPettyCashPanel aThis,String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jSaver;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JComboBox m_jCboDate;
    private javax.swing.JComboBox m_jCboDay;
    private javax.swing.JLabel m_jLblPettyAmt;
    private javax.swing.JLabel m_jLblPettyReset;
    private javax.swing.JLabel m_jLblResetDate;
    private javax.swing.JRadioButton m_jMonthly;
    private javax.swing.JTextField m_jTxtPettyAmt;
    private javax.swing.JRadioButton m_jWeekly;
    // End of variables declaration//GEN-END:variables
    
}
