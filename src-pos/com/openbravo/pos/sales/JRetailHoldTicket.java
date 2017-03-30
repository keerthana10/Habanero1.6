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

package com.openbravo.pos.sales;

import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppView; 
import com.openbravo.pos.forms.AppLocal; 
import com.openbravo.pos.printer.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.panels.JRetailHoldTicketsFinder;
import com.openbravo.pos.panels.JRetailRePrintFinder;
import com.openbravo.pos.panels.JTicketsFinder;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoComp;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoStaff;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import static com.openbravo.pos.sales.JRetailTicketsBag.m_App;
import com.openbravo.pos.ticket.FindHoldTicketsInfo;
import com.openbravo.pos.ticket.ResettlePaymentInfo;
//import com.openbravo.pos.ticket.ResettlePaymentInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JRetailHoldTicket extends JRetailTicketsBag {
    
    private DataLogicSystem m_dlSystem = null;
    protected DataLogicCustomers dlCustomers = null;
 private DataLogicReceipts m_dlReceipts = null;
    private DeviceTicket m_TP;    
    private TicketParser m_TTP;    
    private TicketParser m_TTP2; 
    
    private RetailTicketInfo m_ticket;
    private RetailTicketInfo m_ticketCopy;
    
    private JRetailResettleBillDisplay m_TicketsBagTicketBag;
    
    private JRetailHoldBillEdit m_panelticketedit;
      private SentenceList senttax;
    private ListKeyed taxcollection;
   private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
     private ListKeyed chargecollection;
    private RetailTaxesLogic taxeslogic;
    private  RetailServiceChargesLogic chargeslogic;
    private  RetailSTaxesLogic staxeslogic;
     private SentenceList sentcharge;
     private SentenceList sentservicetax;
     String holdTicketId="";
     String tableId="";
     
//     java.util.List<ResettlePaymentInfo> payments=null;
    
   
    /** Creates new form JTicketsBagTicket */
    public JRetailHoldTicket(AppView app, JRetailHoldBillEdit panelticket) {
        
        super(app, panelticket);
        m_panelticketedit = panelticket; 
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        m_dlReceipts=(DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        m_dlSales=(DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        // Inicializo la impresora...
        m_TP = new DeviceTicket();
   
        // Inicializo el parser de documentos de ticket
        m_TTP = new TicketParser(m_TP, m_dlSystem); // para visualizar el ticket
        m_TTP2 = new TicketParser(m_App.getDeviceTicket(), m_dlSystem); // para imprimir el ticket
        
        initComponents();
        
        m_TicketsBagTicketBag = new JRetailResettleBillDisplay(this);
        
//        m_jTicketEditor.addEditorKeys(m_jKeys);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_jPanelTicket.add(m_TP.getDevicePrinter("1").getPrinterComponent(), BorderLayout.CENTER);
        senttax = m_dlSales.getRetailTaxList();
        sentcharge = m_dlSales.getRetailServiceChargeList();
        sentservicetax = m_dlSales.getRetailServiceTaxList();
        senttaxcategories = m_dlSales.getTaxCategoriesList();
        taxcategoriesmodel = new ComboBoxValModel();

    }
    
    public void activate() {
        
        // precondicion es que no tenemos ticket activado ni ticket en el panel
        
        m_ticket = null;
        m_ticketCopy = null;
        m_jRefund.setVisible(false);
//        printTicket();        
        
//        m_jTicketEditor.reset();
    //   m_jTicketEditor.activate();
        
        m_panelticketedit.setRetailActiveTicket(null, null);

     //   jrbSales.setSelected(true);
        
     //   m_jEdit.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.EditTicket"));
     //   m_jRefund.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.RefundTicket"));
       // m_jPrint.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.PrintTicket"));
          java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailTicketPreviewTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        taxeslogic = new RetailTaxesLogic(taxlist,m_App);
        
        //newly added to calculate line level service charge and service tax
        java.util.List<ServiceChargeInfo> chargelist = null;
        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist,m_App);
        
         java.util.List<TaxInfo> sertaxlist = null;
        try {
            sertaxlist = sentservicetax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        staxeslogic = new RetailSTaxesLogic(sertaxlist,m_App);
        
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);

      
        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }
    
    public boolean deactivate() {
        
        // precondicion es que tenemos ticket activado aqui y ticket en el panel        
        m_ticket = null;   
        m_ticketCopy = null;
        return true;       
        // postcondicion es que no tenemos ticket activado ni ticket en el panel
    }
    
    public void deleteTicket() {
        
        if (m_ticketCopy != null) {           
            // Para editar borramos el ticket anterior
            try {               
                m_dlSales.deleteTicket(m_ticketCopy, m_App.getInventoryLocation());
            } catch (BasicException eData) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                msg.show(this);                
            }            
        }
        
        m_ticket = null;
        m_ticketCopy = null;
        resetToTicket(); 
    }    
        
    public void canceleditionTicket() {
        
        m_ticketCopy = null;
        resetToTicket();
    }    
    
    private void resetToTicket() {       
        printTicket();
//        m_jTicketEditor.reset();
 //       m_jTicketEditor.activate();
        m_panelticketedit.setRetailActiveTicket(null, null);
    }
    
    protected JComponent getBagComponent() {
        return m_TicketsBagTicketBag;
    }
    
    protected JComponent getNullComponent() {
        return this;
    }
    
 private void showMessage(JRetailHoldTicket aThis, String msg,Color colour) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg,colour), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
private JPanel getLabelPanel(String msg, Color colour) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(colour);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}
      
    private void readTicket(int iTicketid) {
        
        try {

            RetailTicketInfo ticket =m_dlSales.getRetailHoldTicket(iTicketid);
            if (ticket == null) {
              showMessage(this,"Sorry! This Bill cannot be previewed",Color.RED);
            } else {
              //  payments=m_dlSales.getPaymentInfo(iTicketid);
                m_ticket = ticket;
                m_ticketCopy = null; // se asigna al pulsar el boton de editar o devolver
                 ticket.setM_App(m_App);
                m_jSettle.setEnabled(true);
                printTicket();
             }
            
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"), e);
            msg.show(this);
        }
        
//        m_jTicketEditor.reset();
   //     m_jTicketEditor.activate();
    }
    
    private void printTicket() {
    
        m_TP.getDevicePrinter("1").reset();
        m_jTicketId.setText(m_ticket.getName());
            m_ticket.setModified(false);
            for(int i=0;i<m_ticket.getLinesCount();i++){
                m_ticket.getLine(i).setticketLine(m_ticket);  
            }
            try {
                taxeslogic.calculateTaxes(m_ticket);
               // System.out.println("printservicecharge"+m_ticket.getServiceChargeRate());
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                if(m_ticket.getErrMsg().equals("")){
//                chargeslogic.calculateCharges(m_ticket);  
//                staxeslogic.calculateServiceTaxes(m_ticket); 
//                script.put("charges", chargecollection);
//                script.put("chargeslogic", chargeslogic);
//                script.put("staxeslogic", staxeslogic);
                }
                script.put("ticket", m_ticket);
                script.put("place", m_ticket.getTableName());
                //customising code
                
                //script.put("payments", payments);
                
                //till here
                m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.Bill")).toString());
          } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException eTP) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), eTP);
                msg.show(this);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailTicketsBagTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
      //  }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        jButtonSearch = new javax.swing.JButton();
       m_jSettle = new javax.swing.JButton();
        m_jRefund = new javax.swing.JButton();
//        m_jPrint = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        m_jPanelTicket = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
       // m_jKeys = new com.openbravo.editor.JEditorKeys();
//        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
//        m_jTicketEditor = new com.openbravo.editor.JEditorIntegerPositive();
        jPanel1 = new javax.swing.JPanel();
//        jrbSales = new javax.swing.JRadioButton();
   //     jrbRefunds = new javax.swing.JRadioButton();

        setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jButtons.setPreferredSize(new java.awt.Dimension(506, 56));
        m_jButtons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
       

        m_jTicketId.setBackground(java.awt.Color.white);
        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 15, -1, 30));

        jButtonSearch.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButtonSearch.setText(AppLocal.getIntString("label.search")); // NOI18N
        jButtonSearch.setFocusPainted(false);
        jButtonSearch.setFocusable(false);
        jButtonSearch.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonSearch.setRequestFocusEnabled(false);
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        m_jButtons.add(jButtonSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 12, 85, 35));

        m_jSettle.setBackground(new java.awt.Color(255, 255, 255));
        m_jSettle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Settle-Bill.png"))); // NOI18N
        //m_jSettle.setText(AppLocal.getIntString("button.edit")); // NOI18N
        m_jSettle.setFocusPainted(false);
        m_jSettle.setFocusable(false);
        m_jSettle.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jSettle.setRequestFocusEnabled(false);
        m_jSettle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSettleActionPerformed(evt);
            }

        });
       m_jButtons.add(m_jSettle, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 12, 85, 35));
       m_jSettle.setEnabled(false);
//        m_jEdit.setBackground(new java.awt.Color(255, 255, 255));
//        m_jEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/edit.png"))); // NOI18N
//        m_jEdit.setText(AppLocal.getIntString("button.edit")); // NOI18N
//        m_jEdit.setFocusPainted(false);
//        m_jEdit.setFocusable(false);
//        m_jEdit.setMargin(new java.awt.Insets(8, 14, 8, 14));
//        m_jEdit.setRequestFocusEnabled(false);
//        m_jEdit.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m_jEditActionPerformed(evt);
//            }
//        });
//        m_jButtons.add(m_jEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 12, 85, 35));

//        m_jRefund.setBackground(new java.awt.Color(255, 255, 255));
//        m_jRefund.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/inbox.png"))); // NOI18N
//        m_jRefund.setText(AppLocal.getIntString("button.refund")); // NOI18N
//        m_jRefund.setFocusPainted(false);
//        m_jRefund.setFocusable(false);
//        m_jRefund.setMargin(new java.awt.Insets(8, 14, 8, 14));
//        m_jRefund.setRequestFocusEnabled(false);
//        m_jRefund.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m_jRefundActionPerformed(evt);
//            }
//        });
//        m_jButtons.add(m_jRefund, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 50, 35));

//        m_jPrint.setBackground(new java.awt.Color(255, 255, 255));
//        m_jPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
//        m_jPrint.setText(AppLocal.getIntString("button.print")); // NOI18N
//        m_jPrint.setFocusPainted(false);
//        m_jPrint.setFocusable(false);
//        m_jPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
//        m_jPrint.setRequestFocusEnabled(false);
//        m_jPrint.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m_jPrintActionPerformed(evt);
//            }
//        });
//        m_jButtons.add(m_jPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 12, 85, 35));

        m_jOptions.add(m_jButtons, java.awt.BorderLayout.WEST);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        m_jOptions.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanelTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jPanelTicket.setLayout(new java.awt.BorderLayout());
        add(m_jPanelTicket, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(175, 276));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

//        m_jKeys.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m_jKeysActionPerformed(evt);
//            }
//        });
//        jPanel4.add(m_jKeys);

      //  jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
      //  jPanel5.setPreferredSize(new java.awt.Dimension(201, 60));
      //  jPanel5.setLayout(new java.awt.GridBagLayout());

//        jButton1.setBackground(new java.awt.Color(255, 255, 255));
//        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
//        jButton1.setFocusPainted(false);
//        jButton1.setFocusable(false);
//        jButton1.setMargin(new java.awt.Insets(8, 14, 8, 14));
//        jButton1.setRequestFocusEnabled(false);
//        jButton1.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton1ActionPerformed(evt);
//            }
//        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
//        jPanel5.add(jButton1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
     //   jPanel5.add(m_jTicketEditor, gridBagConstraints);

      //  jPanel4.add(jPanel5);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.setPreferredSize(new java.awt.Dimension(130, 25));


        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    
   private String getDottedLine(int len) {
        String dotLine = "";
        for (int i = 0; i < len; i++) {
            dotLine = dotLine + "-";
        }
        return dotLine;
    }

    private String getSpaces(int len) {
        String spaces = "";
        for (int i = 0; i < len; i++) {
            spaces = spaces + " ";
        }
        return spaces;
    }
//    private void m_jRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jRefundActionPerformed
//        
//        java.util.List aRefundLines = new ArrayList();
//        
//        for(int i = 0; i < m_ticket.getLinesCount(); i++) {
//            RetailTicketLineInfo newline = new RetailTicketLineInfo(m_ticket.getLine(i));
//            aRefundLines.add(newline);
//        } 
//        
//        m_ticketCopy = null;
//        m_TicketsBagTicketBag.showRefund();
//        m_panelticketedit.showRefundLines(aRefundLines);
//        
//        RetailTicketInfo refundticket = new RetailTicketInfo();
//        refundticket.setTicketType(TicketInfo.RECEIPT_REFUND);
//        refundticket.setCustomer(m_ticket.getCustomer());
//        refundticket.setPayments(m_ticket.getPayments());
//        m_panelticketedit.setRetailActiveTicket(refundticket, null);
//        
//    }//GEN-LAST:event_m_jRefundActionPerformed

    
    
 
  private void m_jSettleActionPerformed(ActionEvent evt) {
           java.util.List<ResettlePaymentInfo> paymentList=JHoldTicketsPaymentEditor.showMessage(this, m_dlReceipts, m_ticket);
           if(paymentList!=null){
           closeTicket(m_ticket,paymentList);
            m_jSettle.setEnabled(false);
            m_TP.getDevicePrinter("1").reset();
          }
    }
       
    
    
    
    
   
  
private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JRetailHoldTicketsFinder finder = JRetailHoldTicketsFinder.getHoldTickets(this, m_dlSales, dlCustomers);
        finder.setVisible(true);
        FindHoldTicketsInfo selectedTicket = finder.getSelectedTicket();
        if (selectedTicket != null) {
           holdTicketId= selectedTicket.getId();
           tableId=selectedTicket.getTableId();
         readTicket(selectedTicket.getTicketId());
        }
}//GEN-LAST:event_jButton2ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    //private javax.swing.JPanel jPanel5;
   // private javax.swing.JRadioButton jrbRefunds;
    //private javax.swing.JRadioButton jrbSales;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JButton m_jSettle;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanelTicket;
   // private javax.swing.JButton m_jPrint;
    private javax.swing.JButton m_jRefund;
    //private com.openbravo.editor.JEditorIntegerPositive m_jTicketEditor;
    private javax.swing.JLabel m_jTicketId;
    // End of variables declaration//GEN-END:variables

    private void closeTicket(RetailTicketInfo m_ticket, java.util.List<ResettlePaymentInfo> paymentList) {
        String inventoryLocation=m_App.getInventoryLocation();
       String posNo=m_App.getProperties().getPosNo();
        String storeName=m_App.getProperties().getStoreName();
        m_ticket.setPlaceid(tableId);
        String ticketDocument=m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+m_ticket.getTicketId();;
         m_ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
         m_ticket.setActiveCash(m_App.getActiveCashIndex());
         m_ticket.setActiveDay(m_App.getActiveDayIndex());
         m_ticket.setDate(new Date()); // Le pongo la fecha de cobro
         String accDate=new SimpleDateFormat("yyyy-MM-dd").format(m_App.getActiveDayDateStart());
         Date dateValue=java.sql.Date.valueOf(accDate);
         m_ticket.setAccountDate(dateValue);

        try {
            m_dlSales.saveRetailHoldTicket(m_ticket, inventoryLocation, posNo, storeName, ticketDocument, paymentList, "", "", "N", "N", 0, 0.0, "Y", "N", "Y", 0, "Y","N",holdTicketId);
        showMessage(this, "Bill Settled Successfully", Color.green);
        } catch (Exception ex) {
            Logger.getLogger(JRetailHoldTicket.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
