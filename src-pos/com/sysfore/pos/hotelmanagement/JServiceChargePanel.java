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

package com.sysfore.pos.hotelmanagement;

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
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DiscountRateinfo;

import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.io.File;

/**
 *
 * @author adrianromero
 */
public class JServiceChargePanel extends JPanel implements JPanelView,BeanFactoryApp{
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
    public java.util.List<ServiceChargeInfo> list = null;
    public boolean updateMode = false;
   // static int x = 400;
  //  static int y = 200;
    /** Creates new form JPanelCloseMoney */
    public JServiceChargePanel() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        
        PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        initComponents();
        try {
            populateList();
        } catch (BasicException ex) {
            Logger.getLogger(JServiceChargePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //m_jDiscountPercentage.setText(dlCustomers.getMaxPercentage() + "%");
        m_jServiceCharge.setText("");

        setVisible(true);
        //add(m_jDiscountList);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
    }
    
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.ServiceCharge");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        m_jServiceChargeList = new javax.swing.JList();
        m_jDiscountLabel = new javax.swing.JLabel();
        m_jDiscountLabel1 = new javax.swing.JLabel();
        m_jServiceCharge = new javax.swing.JTextField();
        m_jCreditAccount = new javax.swing.JTextField();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jLblDebit = new javax.swing.JLabel();
        m_jLblDebit1 = new javax.swing.JLabel();
        m_jServiceRate = new javax.swing.JTextField();
        m_jDebitAccount = new javax.swing.JTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jServiceChargeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jServiceChargeListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jServiceChargeList);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 57, 154, 284));

        m_jDiscountLabel.setText("Rate(%)");
        add(m_jDiscountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 119, 70, 24));

        m_jDiscountLabel1.setText("Name");
        add(m_jDiscountLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 80, 76, 19));

        m_jServiceCharge.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jServiceCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jServiceChargeActionPerformed(evt);
            }
        });
        add(m_jServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(371, 77, 200, 24));

        m_jCreditAccount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        add(m_jCreditAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 200, 24));

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

        javax.swing.GroupLayout jSaverLayout = new javax.swing.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(330, 330, 330)
                .addComponent(jbtnNew)
                .addGap(4, 4, 4)
                .addComponent(jbtnDelete)
                .addGap(4, 4, 4)
                .addComponent(jbtnSave))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jbtnNew)
            .addComponent(jbtnDelete)
            .addComponent(jbtnSave)
        );

        add(jSaver, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 11, 536, 40));

        m_jLblDebit.setText("Debit Account");
        add(m_jLblDebit, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 161, 100, 20));

        m_jLblDebit1.setText("Credit Account");
        add(m_jLblDebit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 200, 106, 20));

        m_jServiceRate.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        add(m_jServiceRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(371, 119, 200, 24));

        m_jDebitAccount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        add(m_jDebitAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, 200, 24));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jServiceChargeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jServiceChargeListValueChanged
        // TODO add your handling code here:
        if (evt.getValueIsAdjusting()) {
            updateMode = true;
            String serviceName = null;
            try {
                serviceName = m_jServiceChargeList.getSelectedValue().toString();
            } catch (Exception ex) {
                System.out.println("unknown exception");
            }
           // String rate = null;
            try {
              //  rate = dlCustomers.getDiscountLine(serviceName);
                list = dlCustomers.getServiceCharge(serviceName);
            } catch (BasicException ex) {
                Logger.getLogger(JServiceChargePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jServiceCharge.setText(list.get(0).getName());
            double rated = Double.parseDouble(list.get(0).getRate());
            rated *= 100;
            m_jServiceRate.setText(Formats.DOUBLE.formatValue(rated));
                m_jDebitAccount.setText(list.get(0).getDebitAccount());
                m_jCreditAccount.setText(list.get(0).getCreditAccount());
         
        }
    }//GEN-LAST:event_m_jServiceChargeListValueChanged

    private void m_jServiceChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jServiceChargeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_m_jServiceChargeActionPerformed

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
     m_jServiceCharge.setText("");
        m_jServiceRate.setText(null);
        m_jDebitAccount.setText(null);
        m_jCreditAccount.setText(null);
        m_jServiceChargeList.clearSelection();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jServiceChargeList.getSelectedIndex();
        System.out.println("index is " + index);

        if (index == -1) {
            JOptionPane.showMessageDialog(JServiceChargePanel.this, "Please select service charge in list");
        } else {
            try {
                String val = m_jServiceChargeList.getSelectedValue().toString();
                dlCustomers.deleteServiceCharge(val);
                m_jServiceCharge.setText("");
                m_jServiceRate.setText("");
                m_jDebitAccount.setText("");
                m_jCreditAccount.setText("");

            } catch (BasicException ex) {
                Logger.getLogger(JServiceChargePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JServiceChargePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        if (m_jServiceCharge.getText().equals("")) {
            JOptionPane.showMessageDialog(JServiceChargePanel.this, "Please enter the name");
        }
        else if (m_jServiceRate.getText().equals("")) {
            JOptionPane.showMessageDialog(JServiceChargePanel.this, "Please enter the rate");
        } else {
            try {
                saveButtonAction();
            } catch (BasicException ex) {
                Logger.getLogger(JServiceChargePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed
 public void populateList() throws BasicException {

        model = new DefaultListModel();
        m_jServiceChargeList.setModel(model);
        list = dlCustomers.getServiceChargeList();
        String[] dListId = null;
        String[] dListRate = null;
        for (int i = 0; i < list.size(); i++) {
             String listid = list.get(i).getName();
            model.add(i, listid);
        }
    }

private void saveButtonAction() throws BasicException {

        String name = m_jServiceCharge.getText();
        String serviceCharge = m_jServiceRate.getText();
        try{
        double rate = Double.parseDouble(serviceCharge);
        String debitAccount =m_jDebitAccount.getText();
        String creditAccount= m_jCreditAccount.getText();;
        
        if (name != null && serviceCharge != null && rate >= 0 ) {

            boolean avl = checkServiceNameAvl(name);
            try {
                list = dlCustomers.getServiceChargeList();
                Double serviceChargeAmt = rate / 100;
                if (updateMode == false) {
                    if (avl == false) {
                        dlCustomers.insertServiceCharge(UUID.randomUUID().toString(), name, serviceChargeAmt,debitAccount,creditAccount);
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered service charge name already exists");
                    }
                } else {
                    String id = list.get(m_jServiceChargeList.getSelectedIndex()).getId();
                    dlCustomers.updateServiceCharge(id, name, serviceChargeAmt,debitAccount,creditAccount);
                    updateMode = false;
                }
                populateList();
                m_jServiceCharge.setText("");
                m_jServiceRate.setText("");
                m_jDebitAccount.setText("");
                m_jCreditAccount.setText("");
            } catch (NumberFormatException npe) {
                JOptionPane.showMessageDialog(this, "Please enter the valid rate");
              //  m_jServiceCharge.setText("");
                m_jServiceRate.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Your msg.");
        }} catch (NumberFormatException npe) {
                JOptionPane.showMessageDialog(this, "Please enter the valid rate");
              //  m_jServiceCharge.setText("");
                m_jServiceRate.setText("");
            }
    }


    private boolean checkServiceNameAvl(String serviceName) throws BasicException {
        String name = dlCustomers.getServiceChargeName(serviceName);
        if ("NONAME".equalsIgnoreCase(name)) {
            return false;
        } else {
            return true;
        }
    } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jSaver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JTextField m_jCreditAccount;
    private javax.swing.JTextField m_jDebitAccount;
    private javax.swing.JLabel m_jDiscountLabel;
    private javax.swing.JLabel m_jDiscountLabel1;
    private javax.swing.JLabel m_jLblDebit;
    private javax.swing.JLabel m_jLblDebit1;
    private javax.swing.JTextField m_jServiceCharge;
    private javax.swing.JList m_jServiceChargeList;
    private javax.swing.JTextField m_jServiceRate;
    // End of variables declaration//GEN-END:variables
    
}
