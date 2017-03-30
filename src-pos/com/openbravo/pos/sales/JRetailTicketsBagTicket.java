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
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.panels.JTicketsFinder;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JRetailTicketsBagTicket extends JRetailTicketsBag {
    
    private DataLogicSystem m_dlSystem = null;
    protected DataLogicCustomers dlCustomers = null;

    private DeviceTicket m_TP;    
    private TicketParser m_TTP;    
    private TicketParser m_TTP2; 
    
    private RetailTicketInfo m_ticket;
    private RetailTicketInfo m_ticketCopy;
    
    private JRetailTicketsBagTicketBag m_TicketsBagTicketBag;
    
    private JRetailPanelTicketEdits m_panelticketedit;
      private SentenceList senttax;
    private ListKeyed taxcollection;
   private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;

    private RetailTaxesLogic taxeslogic;
   
    /** Creates new form JTicketsBagTicket */
    public JRetailTicketsBagTicket(AppView app, JRetailPanelTicketEdits panelticket) {
        
        super(app, panelticket);
        m_panelticketedit = panelticket; 
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        
        // Inicializo la impresora...
        m_TP = new DeviceTicket();
   
        // Inicializo el parser de documentos de ticket
        m_TTP = new TicketParser(m_TP, m_dlSystem); // para visualizar el ticket
        m_TTP2 = new TicketParser(m_App.getDeviceTicket(), m_dlSystem); // para imprimir el ticket
        
        initComponents();
        
        m_TicketsBagTicketBag = new JRetailTicketsBagTicketBag(this);
        
        m_jTicketEditor.addEditorKeys(m_jKeys);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_jPanelTicket.add(m_TP.getDevicePrinter("1").getPrinterComponent(), BorderLayout.CENTER);
        senttax = m_dlSales.getRetailTaxList();
        senttaxcategories = m_dlSales.getTaxCategoriesList();

        taxcategoriesmodel = new ComboBoxValModel();

    }
    
    public void activate() {
        
        // precondicion es que no tenemos ticket activado ni ticket en el panel
        
        m_ticket = null;
        m_ticketCopy = null;
        m_jRefund.setVisible(false);
        printTicket();        
        
        m_jTicketEditor.reset();
        m_jTicketEditor.activate();
        
        m_panelticketedit.setRetailActiveTicket(null, null);

        jrbSales.setSelected(true);
        
        m_jEdit.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.EditTicket"));
     //   m_jRefund.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.RefundTicket"));
        m_jPrint.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.PrintTicket"));
          java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);

        taxeslogic = new RetailTaxesLogic(taxlist,m_App);
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
        m_jTicketEditor.reset();
        m_jTicketEditor.activate();
        m_panelticketedit.setRetailActiveTicket(null, null);
    }
    
    protected JComponent getBagComponent() {
        return m_TicketsBagTicketBag;
    }
    
    protected JComponent getNullComponent() {
        return this;
    }
      
    private void readTicket(int iTicketid, int iTickettype) {
        
        try {
            RetailTicketInfo ticket = (iTicketid==-1)
                ? m_dlSales.loadRetailTicket(iTickettype,  m_jTicketEditor.getValueInteger())
                : m_dlSales.loadRetailTicket(iTickettype, iTicketid) ;

            if (ticket == null) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notexiststicket"));
                msg.show(this);
            } else {
                m_ticket = ticket;
                m_ticketCopy = null; // se asigna al pulsar el boton de editar o devolver
                printTicket();
            }
            
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"), e);
            msg.show(this);
        }
        
        m_jTicketEditor.reset();
        m_jTicketEditor.activate();
    }
    
    private void printTicket() {
        
        // imprimo m_ticket
        
        try {
            m_jEdit.setEnabled(
                    m_ticket != null
                    && (m_ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL || m_ticket.getTicketType() == TicketInfo.RECEIPT_REFUND)
                    && m_dlSales.isCashActive(m_ticket.getActiveCash()));
        } catch (BasicException e) {
            m_jEdit.setEnabled(false);
        }
        m_jRefund.setEnabled(m_ticket != null && m_ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL);
        m_jPrint.setEnabled(m_ticket != null);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_TP.getDevicePrinter("1").reset();
        
        if (m_ticket == null) {
            m_jTicketId.setText(null);            
        } else {
            m_jTicketId.setText(m_ticket.getName());
            
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", m_ticket);
                m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.TicketPreview")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException eTP) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), eTP);
                msg.show(this);
            }
        }
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
        jButton2 = new javax.swing.JButton();
        m_jEdit = new javax.swing.JButton();
        m_jRefund = new javax.swing.JButton();
        m_jPrint = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        m_jPanelTicket = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        m_jTicketEditor = new com.openbravo.editor.JEditorIntegerPositive();
        jPanel1 = new javax.swing.JPanel();
        jrbSales = new javax.swing.JRadioButton();
        jrbRefunds = new javax.swing.JRadioButton();

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

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        jButton2.setText(AppLocal.getIntString("label.search")); // NOI18N
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jButton2.setRequestFocusEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        m_jButtons.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 12, 85, 35));

        m_jEdit.setBackground(new java.awt.Color(255, 255, 255));
        m_jEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/edit.png"))); // NOI18N
        m_jEdit.setText(AppLocal.getIntString("button.edit")); // NOI18N
        m_jEdit.setFocusPainted(false);
        m_jEdit.setFocusable(false);
        m_jEdit.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEdit.setRequestFocusEnabled(false);
        m_jEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 12, 85, 35));

        m_jRefund.setBackground(new java.awt.Color(255, 255, 255));
        m_jRefund.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/inbox.png"))); // NOI18N
        m_jRefund.setText(AppLocal.getIntString("button.refund")); // NOI18N
        m_jRefund.setFocusPainted(false);
        m_jRefund.setFocusable(false);
        m_jRefund.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jRefund.setRequestFocusEnabled(false);
        m_jRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jRefundActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jRefund, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 50, 35));

        m_jPrint.setBackground(new java.awt.Color(255, 255, 255));
        m_jPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
        m_jPrint.setText(AppLocal.getIntString("button.print")); // NOI18N
        m_jPrint.setFocusPainted(false);
        m_jPrint.setFocusable(false);
        m_jPrint.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jPrint.setRequestFocusEnabled(false);
        m_jPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPrintActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 12, 85, 35));

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

        m_jKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jKeysActionPerformed(evt);
            }
        });
        jPanel4.add(m_jKeys);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel5.setPreferredSize(new java.awt.Dimension(201, 60));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jButton1.setRequestFocusEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(jButton1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel5.add(m_jTicketEditor, gridBagConstraints);

        jPanel4.add(jPanel5);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.setPreferredSize(new java.awt.Dimension(130, 25));

        buttonGroup1.add(jrbSales);
        jrbSales.setText(AppLocal.getIntString("label.sales")); // NOI18N
        jrbSales.setFocusPainted(false);
        jrbSales.setFocusable(false);
        jrbSales.setRequestFocusEnabled(false);
        jPanel1.add(jrbSales);

        buttonGroup1.add(jrbRefunds);
        jrbRefunds.setText(AppLocal.getIntString("label.refunds")); // NOI18N
        jrbRefunds.setFocusPainted(false);
        jrbRefunds.setFocusable(false);
        jrbRefunds.setRequestFocusEnabled(false);
        jPanel1.add(jrbRefunds);

        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditActionPerformed
                 
        m_ticketCopy = m_ticket;
        String editBillId = m_ticketCopy.getId();
       
        m_TicketsBagTicketBag.showEdit();
        m_panelticketedit.showCatalog();
        System.out.println("m_ticketCopy---edit--"+m_ticketCopy.getRate());
        m_ticketCopy.setRate(m_ticketCopy.getRate());
        m_panelticketedit.setRetailActiveTicket(m_ticket.copyEditTicket(m_ticketCopy.getRate()), null,editBillId);

        
    }//GEN-LAST:event_m_jEditActionPerformedpublic void getServiceCharge(String isHomeDelivery){
  

    private void m_jPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPrintActionPerformed
        
        try {
            //GEN-FIRST:event_m_jPrintActionPerformed
            m_ticket = m_dlSales.loadEditTicketBasedOnBillNo(m_ticket.getDocumentNo());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailTicketsBagTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
               ServiceChargeTaxInfo serviceTax = null;

      //      String paymentId = fti.getPid();
            try {
                serviceTax = m_dlSales.loadServiceChargeTax(m_ticket.getDocumentNo());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_ticket.setServiceCharge(serviceTax.getServiceChargeAmt());
            m_ticket.setServiceChargeRate(serviceTax.getServiceChargerate());
            m_ticket.setServiceTax(serviceTax.getServiceTaxAmt());
            m_ticket.setServiceTaxRate(serviceTax.getServiceTaxrate());

            try {
                taxeslogic.calculateTaxes(m_ticket);
                for (RetailTicketLineInfo line : m_ticket.getLines()) {
                    line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), m_ticket.getCustomer(),"N"));
                }
            } catch (TaxesException ex) {
                Logger.getLogger(JPanelButtons.class.getName()).log(Level.SEVERE, null, ex);
            }
        if (m_ticket != null) {
          java.util.List<TicketLineConstructor> allLines = null;
           com.openbravo.pos.printer.printer.ImagePrinter printer = new ImagePrinter();

             allLines = getAllLines(m_ticket);

         try {
                printer.print(allLines);
            } catch (PrinterException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

        }  
        
    }//GEN-LAST:event_m_jPrintActionPerformed

       private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket) {


       java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
       allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
       allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
       if(ticket.getCustomer()!=null){
          allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (ticket.getCustomer().getName())));
       }
        String tableId = null;
        try {
            try {
                tableId = m_dlSales.getTableId(ticket.getDocumentNo());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailTicketsBagTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException ex) {
            tableId = null;
        }
        if(tableId==null){
          allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ""));
        }else{
                allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + tableId));
          }

      allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));
       allLines.add(new TicketLineConstructor(getDottedLine(90)));
       allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price"+getSpaces(9)+"Value(INR)"));
       allLines.add(new TicketLineConstructor(getDottedLine(90)));
         for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue+getSpaces(7 - qty.length() + 12 - subValue.length())+total));
        }
         String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
         String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
         String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
          String discount = Formats.DoubleValue.formatValue(ticket.getTotalDiscount());
          String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
          String total =  Formats.DoubleValue.formatValue(ticket.getTotal());
      allLines.add(new TicketLineConstructor(getDottedLine(90)));
      allLines.add(new TicketLineConstructor(getSpaces(33)+"Total " + getSpaces(29-subTotal.length()) + (subTotal)));
      allLines.add(new TicketLineConstructor(getSpaces(33)+"Discount " + getSpaces(25-discount.length()) + ("-"+discount)));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Total After Discount " + getSpaces(14 - totalAfrDiscount.length()) + (totalAfrDiscount)));

      if(ticket.getTaxes().size()!=0){
          for(int i=0;i<ticket.getTaxes().size();i++){
              System.out.println("ticket.getTaxes().get(i).getTax()--"+ticket.getTaxes().get(i).getTax());
              if(ticket.getTaxes().get(i).getTax()!=0.00){
                 allLines.add(new TicketLineConstructor(getSpaces(33)+ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(25 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()))));
              }
          }

      }

      String aCount = ticket.printTicketCount();
      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Charge 6%" + getSpaces(18-serviceCharge.length()) + serviceCharge));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Tax 4.94%" + getSpaces(18-serviceTax.length()) +serviceTax));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Grand Total : " + getSpaces(21-total.length()) + (total)));

      return allLines;
    }
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
    private void m_jRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jRefundActionPerformed
        
        java.util.List aRefundLines = new ArrayList();
        
        for(int i = 0; i < m_ticket.getLinesCount(); i++) {
            RetailTicketLineInfo newline = new RetailTicketLineInfo(m_ticket.getLine(i));
            aRefundLines.add(newline);
        } 
        
        m_ticketCopy = null;
        m_TicketsBagTicketBag.showRefund();
        m_panelticketedit.showRefundLines(aRefundLines);
        
        RetailTicketInfo refundticket = new RetailTicketInfo();
        refundticket.setTicketType(TicketInfo.RECEIPT_REFUND);
        refundticket.setCustomer(m_ticket.getCustomer());
        refundticket.setPayments(m_ticket.getPayments());
        m_panelticketedit.setRetailActiveTicket(refundticket, null);
        
    }//GEN-LAST:event_m_jRefundActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        readTicket(-1, jrbSales.isSelected() ? 0 : 1);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed

        readTicket(-1, jrbSales.isSelected() ? 0 : 1);
        
    }//GEN-LAST:event_m_jKeysActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JTicketsFinder finder = JTicketsFinder.getReceiptFinder(this, m_dlSales, dlCustomers);
        finder.setVisible(true);
        FindTicketsInfo selectedTicket = finder.getSelectedCustomer();
        if (selectedTicket == null) {
            m_jTicketEditor.reset();
            m_jTicketEditor.activate();
        } else {
            readTicket(selectedTicket.getTicketId(), selectedTicket.getTicketType());
        }
}//GEN-LAST:event_jButton2ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jrbRefunds;
    private javax.swing.JRadioButton jrbSales;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JButton m_jEdit;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanelTicket;
    private javax.swing.JButton m_jPrint;
    private javax.swing.JButton m_jRefund;
    private com.openbravo.editor.JEditorIntegerPositive m_jTicketEditor;
    private javax.swing.JLabel m_jTicketId;
    // End of variables declaration//GEN-END:variables
    
}
