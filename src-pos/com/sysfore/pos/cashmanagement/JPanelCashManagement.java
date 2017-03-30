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
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;

import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;

/**
 *
 * @author adrianromero
 */
public class JPanelCashManagement extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    
    private CloseShiftModel m_PaymentsToClose = null;
    
    private TicketParser m_TTP;
     protected DataLogicSystem dlSystem;
     static PurchaseOrderReceipts PurchaseOrder = null;
    private BankDepositModel m_PaymentsToBank = null;
    protected DataLogicCustomers dlCustomers;
    /** Creates new form JPanelCloseMoney */
    public JPanelCashManagement() {
        
        initComponents();                   
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        
         PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
     //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
   // JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
       
    }
    
      
    private void printPayments(String report) {
        
        String sresource = m_dlSystem.getResourceAsXML(report);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
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
        return AppLocal.getIntString("Menu.CashManagement");
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

        jBtnCloseShift = new javax.swing.JButton();
        jBtnCredit = new javax.swing.JButton();
        jBtnPetty = new javax.swing.JButton();
        jBtnFloatCash = new javax.swing.JButton();
        jBtnMasterCash = new javax.swing.JButton();
        m_jBtnBankSlip = new javax.swing.JButton();
        m_jAddDiscount = new javax.swing.JButton();
        m_jBtnExpenseReport = new javax.swing.JButton();

        jBtnCloseShift.setText("Close Shift");
        jBtnCloseShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCloseShiftActionPerformed(evt);
            }
        });

        jBtnCredit.setText("Credit Note");
        jBtnCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCreditActionPerformed(evt);
            }
        });

        jBtnPetty.setText("Petty Cash");
        jBtnPetty.setMaximumSize(new java.awt.Dimension(83, 23));
        jBtnPetty.setMinimumSize(new java.awt.Dimension(83, 23));
        jBtnPetty.setPreferredSize(new java.awt.Dimension(83, 23));
        jBtnPetty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPettyActionPerformed(evt);
            }
        });

        jBtnFloatCash.setText("Float Cash Setup");
        jBtnFloatCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFloatCashActionPerformed(evt);
            }
        });

        jBtnMasterCash.setText("Master Cash Setup");
        jBtnMasterCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMasterCashActionPerformed(evt);
            }
        });

        m_jBtnBankSlip.setText("Generate Bank Slip");
        m_jBtnBankSlip.setMaximumSize(new java.awt.Dimension(83, 23));
        m_jBtnBankSlip.setMinimumSize(new java.awt.Dimension(83, 23));
        m_jBtnBankSlip.setPreferredSize(new java.awt.Dimension(83, 23));
        m_jBtnBankSlip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnBankSlipActionPerformed(evt);
            }
        });

        m_jAddDiscount.setText("Add Discount");
        m_jAddDiscount.setPreferredSize(new java.awt.Dimension(170, 30));
        m_jAddDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jAddDiscountActionPerformed(evt);
            }
        });

        m_jBtnExpenseReport.setText("Petty Expense Report");
        m_jBtnExpenseReport.setMaximumSize(new java.awt.Dimension(83, 23));
        m_jBtnExpenseReport.setMinimumSize(new java.awt.Dimension(83, 23));
        m_jBtnExpenseReport.setPreferredSize(new java.awt.Dimension(83, 23));
        m_jBtnExpenseReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnExpenseReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtnCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jBtnMasterCash, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtnFloatCash, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jBtnPetty, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtnCloseShift, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(m_jBtnBankSlip, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(m_jAddDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(m_jBtnExpenseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnMasterCash, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnFloatCash, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnPetty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jBtnCloseShift, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(m_jBtnBankSlip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jAddDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(m_jBtnExpenseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(136, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jBtnExpenseReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnExpenseReportActionPerformed
        m_App.getAppUserView().showTask("/com/openbravo/reports/pettyExpense.bs"); // TODO add your handling code here:
}//GEN-LAST:event_m_jBtnExpenseReportActionPerformed

    private void m_jBtnBankSlipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnBankSlipActionPerformed
        try {
            m_PaymentsToBank = BankDepositModel.loadInstance(m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }

        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaGenerateSlip"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            printBankSlip("Printer.BankDeposit");

        }// TODO add your handling code here:
}//GEN-LAST:event_m_jBtnBankSlipActionPerformed

    private void jBtnPettyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPettyActionPerformed
        try {
            PettyCashEditor.showMessage(JPanelCashManagement.this, PurchaseOrder, m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jBtnPettyActionPerformed

    private void jBtnMasterCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMasterCashActionPerformed
        try {
            MasterCashEditor.showMessage(JPanelCashManagement.this, PurchaseOrder, m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashManagement.class.getName()).log(Level.SEVERE, null, ex);
        } // TODO add your handling code here:
}//GEN-LAST:event_jBtnMasterCashActionPerformed

    private void jBtnCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCreditActionPerformed
        try {
            CreditNoteEditor.showMessage(JPanelCashManagement.this, PurchaseOrder, m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnCreditActionPerformed

    private void jBtnFloatCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFloatCashActionPerformed
        try {
            FloatCashEditor.showMessage(JPanelCashManagement.this, PurchaseOrder, m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jBtnFloatCashActionPerformed

    private void m_jAddDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jAddDiscountActionPerformed
        boolean mgrStatus = false;
        try {
            mgrStatus = getOwnerStatus(dlCustomers);

            if (mgrStatus) {

                DiscountsEditor.showMessage(JPanelCashManagement.this, dlCustomers, m_App);
            } else {
                JOptionPane.showMessageDialog(JPanelCashManagement.this, "Owner Privilages required for this operation");
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
}//GEN-LAST:event_m_jAddDiscountActionPerformed

    private void jBtnCloseShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCloseShiftActionPerformed
        // try {

        //   List<SharedTicketInfo> pendingBillsList = dlReceipts.getSharedTicketList();
        // int listSize =pendingBillsList.size();
        // CustomerInfoExt customerName = m_panelticket.getActiveTicket().getCustomer();
        // if(listSize!=0 || customerName!=null)
        // {
        //    JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
        //}
        // else
        // {
        //     loggedUserId = m_App.getAppUserView().getUser().getUserInfo().getId();
        //  try {


        //  dlSystem.peoplelogInsert(UUID.randomUUID().toString(), loggedUserId, getDateTime(), "CloseShift");

        // SentenceExec sent = m_dlSystem.peoplelogInsert();
        //  } catch (BasicException ex) {
        //    Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
        //  }



        try {
            m_PaymentsToClose = CloseShiftModel.loadInstance(m_App);
        } catch (BasicException ex) {
            Logger.getLogger(CloseShiftModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaclosecash"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {

            Date dNow = new Date();
            printCloseShift("Printer.CloseShift");
            try {
                // Cerramos la caja si esta pendiente de cerrar.
                if (m_App.getActiveCashDateEnd() == null) {
                    new StaticSentence(m_App.getSession()
                            , "UPDATE CLOSEDCASH SET DATEEND = ? WHERE HOST = ? AND MONEY = ? AND POSNO=?"
                            , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING}))
                            .exec(new Object[] {dNow, m_App.getProperties().getHost(), m_App.getActiveCashIndex(),m_App.getProperties().getPosNo()});
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }


            // Creamos una nueva caja
            if(m_App.getCloseCashsequence()==1) {
                m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getCloseCashsequence()+1, dNow, null);
                m_PaymentsToClose.setSeq(1);
            } else {
                m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getCloseCashsequence(), dNow, null);
            }
            //m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getActiveCashSequence() + 1, dNow, null);
            try {
                // creamos la caja activa
                m_dlSystem.execInsertCash(new Object[]{m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashSequence(), m_App.getProperties().getPosNo(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});
            } catch (BasicException ex) {
                Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
            }

            // ponemos la fecha de fin
            //  m_PaymentsToClose.setDateEnd(dNow);

            // print report


            // Mostramos el mensaje
            JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
            //  m_jCloseDay.setEnabled(true);

        }


        // }

        // } catch (BasicException ex) {
        //     Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
        //}


        // TODO add your handling code here:
}//GEN-LAST:event_jBtnCloseShiftActionPerformed
 private boolean getOwnerStatus(DataLogicCustomers dlCustomers) throws BasicException {

        String user = m_App.getAppUserView().getUser().getName();
        //String userId = m_oTicket.getUser().getId();

        String role = dlCustomers.getRolebyName(user);
        if ("Admin".equalsIgnoreCase(role)) {
            return true;
        } else {
            return false;
        }
    }      private void printBankSlip(String report) {

        String sresource = m_dlSystem.getResourceAsXML(report);
        /*application = dlSales.getApplicationDetails();
            java.util.List<ApplicationInfo> applicationInfo =  new java.util.ArrayList<ApplicationInfo>();
        try {
            applicationInfo = application.list();
            //applicationDetails = new ListKeyed<ApplicationInfo>(applicationInfo);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToBank);
               // script.put("posinfo", (ApplicationInfo)applicationInfo.get(0));
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }
    private void printCloseShift(String report) {

        String sresource = m_dlSystem.getResourceAsXML(report);
      //  application = dlSales.getApplicationDetails();
      //      java.util.List<ApplicationInfo> applicationInfo =  new java.util.ArrayList<ApplicationInfo>();
     //   try {
     //       applicationInfo = application.list();
            //applicationDetails = new ListKeyed<ApplicationInfo>(applicationInfo);
      //  } catch (BasicException ex) {
      //      Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
      //  }

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
//                 script.put("posinfo", (ApplicationInfo)applicationInfo.get(0));
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCloseShift;
    private javax.swing.JButton jBtnCredit;
    private javax.swing.JButton jBtnFloatCash;
    private javax.swing.JButton jBtnMasterCash;
    private javax.swing.JButton jBtnPetty;
    private javax.swing.JButton m_jAddDiscount;
    private javax.swing.JButton m_jBtnBankSlip;
    private javax.swing.JButton m_jBtnExpenseReport;
    // End of variables declaration//GEN-END:variables
    
}
