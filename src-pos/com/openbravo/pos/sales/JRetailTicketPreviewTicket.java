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
import com.openbravo.pos.panels.JRetailRePrintFinder;
import com.openbravo.pos.panels.JTicketsFinder;
import com.openbravo.pos.printer.printer.ImageBillPrinter;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TaxMapInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import java.awt.print.PrinterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.WordUtils;

public class JRetailTicketPreviewTicket extends JRetailTicketsBag {
    
    private DataLogicSystem m_dlSystem = null;
    protected DataLogicCustomers dlCustomers = null;

    private DeviceTicket m_TP;    
    private TicketParser m_TTP;    
    private TicketParser m_TTP2; 
    
    private RetailTicketInfo m_ticket;
    private RetailTicketInfo m_ticketCopy;
    
    private JRetailTicketsPreviewDisplay m_TicketsBagTicketBag;
    
    private JRetailTicketPrintEdit m_panelticketedit;
      private SentenceList senttax;
    private ListKeyed taxcollection;
   private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
     private ListKeyed chargecollection;
    private RetailTaxesLogic taxeslogic;
    private  RetailServiceChargesLogic chargeslogic;
    private  RetailSTaxesLogic staxeslogic;
     private  RetailSBTaxesLogic sbtaxeslogic;
     private SentenceList sentcharge;
     private SentenceList sentservicetax;
      private SentenceList sentSBtax;
      //tax logic has been changed to show taxes based on name not by rate 19/10/2016
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              Date uniqueTaxStartdate=null;
   
    /** Creates new form JTicketsBagTicket */
    public JRetailTicketPreviewTicket(AppView app, JRetailTicketPrintEdit panelticket) {
        
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
        
        m_TicketsBagTicketBag = new JRetailTicketsPreviewDisplay(this);
        
//        m_jTicketEditor.addEditorKeys(m_jKeys);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_jPanelTicket.add(m_TP.getDevicePrinter("1").getPrinterComponent(), BorderLayout.CENTER);
        senttax = m_dlSales.getRetailTaxList();
        sentcharge = m_dlSales.getRetailServiceChargeList();
        sentservicetax = m_dlSales.getRetailServiceTaxList();
        senttaxcategories = m_dlSales.getTaxCategoriesList();
        sentSBtax = m_dlSales.getRetailSwachBharatTaxList();

        taxcategoriesmodel = new ComboBoxValModel();
         try {
               uniqueTaxStartdate = sdf.parse("2016-10-21");
          } catch (ParseException ex) {
              Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
          }

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
        m_jPrint.setVisible(m_App.getAppUserView().getUser().hasPermission("sales.PrintTicket"));
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
        java.util.List<TaxInfo> sbtaxlist = null;
        try {
            sbtaxlist = sentSBtax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sbtaxeslogic = new RetailSBTaxesLogic(sbtaxlist,m_App);

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
    
            private void showMessage(JRetailTicketPreviewTicket aThis, String msg,Color colour) {
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
      
    private void readTicket(int iTicketid, int iTickettype) {
      //  System.out.println("read ticket");
        try {
            String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
//            RetailTicketInfo ticket = (iTicketid==-1)
//                ? m_dlSales.loadRetailTicket(iTickettype,  m_jTicketEditor.getValueInteger())
//                : m_dlSales.loadRetailTicket(iTickettype, iTicketid) ;

            RetailTicketInfo ticket =m_dlSales.getRetailPrintedTicket(iTicketid);
            if (ticket == null) {
              showMessage(this,"Sorry! This Bill cannot be previewed",Color.RED);
            } else {
                m_ticket = ticket;
                m_ticketCopy = null; // se asigna al pulsar el boton de editar o devolver
                ticket.setM_App(m_App);
                String file;
             file = "Printer.Preview";
               if(m_ticket.getDate().after(uniqueTaxStartdate)) {
                 file="Printer.Bill";
             }
              for(int i=0;i<m_ticket.getLinesCount();i++){
                m_ticket.getLine(i).setticketLine(m_ticket);  
            }
             try {
                                taxeslogic.calculateTaxes(m_ticket);
                            //    chargeslogic.calculateCharges(m_oTicket);
                               // staxeslogic.calculateServiceTaxes(m_oTicket);
                              //  sbtaxeslogic.calculateSwachBharatTaxes(m_oTicket);
                            } catch (TaxesException ex) {
                              //  logger.info("Order NO." + m_ticket.getOrderId() + "exception on clicking print bill calculateTaxes" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                if(storeLocation.equals("BlrIndranagar") || storeLocation.equals("ITPL")){
//                printTicketGeneric(file, ticket, ticket.getTableName());
//            }else{
                printTicket();
//            }
           }
            
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"), e);
            msg.show(this);
        }
        
//        m_jTicketEditor.reset();
   //     m_jTicketEditor.activate();
    }
    
    private void printTicket() {
        

        m_jPrint.setEnabled(m_ticket != null);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_TP.getDevicePrinter("1").reset();
        
//        if (m_ticket == null) {
//            m_jTicketId.setText(null);            
//        } else {
            m_jTicketId.setText(m_ticket.getName());
            m_ticket.setModified(false);
//            for(int i=0;i<m_ticket.getLinesCount();i++){
//                m_ticket.getLine(i).setticketLine(m_ticket);  
//            }
            try {
                taxeslogic.calculateTaxes(m_ticket);
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
               
//                if(m_ticket.getErrMsg().equals("")){
//                chargeslogic.calculateCharges(m_ticket);  
//                staxeslogic.calculateServiceTaxes(m_ticket);
//                sbtaxeslogic.calculateSwachBharatTaxes(m_ticket);
//                script.put("charges", chargecollection);
//                script.put("chargeslogic", chargeslogic);
//                script.put("staxeslogic", staxeslogic);
//                 script.put("sbtaxeslogic", sbtaxeslogic);
//                }
                script.put("ticket", m_ticket);
                script.put("place", m_ticket.getTableName());
                  if(m_ticket.getDate().after(uniqueTaxStartdate)) {
                m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.Bill")).toString());
                  }else{
                       m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.Preview")).toString());
                  }
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
//         private void printTicketGeneric(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
//             java.util.List<TicketLineConstructor> allLines = null;
//         java.util.List<TicketLineConstructor> startallLines = new ArrayList<TicketLineConstructor>();
//         int count =0;
//           com.openbravo.pos.printer.printer.ImageBillPrinter printer = new ImageBillPrinter();
//             allLines = getAllLines(ticket,ticketext);
//        try {
//            printer.print(allLines);
//          
//        } catch (PrinterException ex) {
//            Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

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
    //    m_jEdit = new javax.swing.JButton();
        m_jRefund = new javax.swing.JButton();
        m_jPrint = new javax.swing.JButton();
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

        m_jPrint.setBackground(new java.awt.Color(255, 255, 255));
        m_jPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
        m_jPrint.setText(AppLocal.getIntString("button.print")); // NOI18N
        m_jPrint.setFocusPainted(false);
        m_jPrint.setFocusable(false);
        m_jPrint.setMargin(new java.awt.Insets(0, 0, 0, 0));
        m_jPrint.setRequestFocusEnabled(false);
        m_jPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPrintActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 12, 85, 35));

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

//        buttonGroup1.add(jrbSales);
//        jrbSales.setText(AppLocal.getIntString("label.sales")); // NOI18N
//        jrbSales.setFocusPainted(false);
//        jrbSales.setFocusable(false);
//        jrbSales.setRequestFocusEnabled(false);
//        jPanel1.add(jrbSales);

//        buttonGroup1.add(jrbRefunds);
//        jrbRefunds.setText(AppLocal.getIntString("label.refunds")); // NOI18N
//        jrbRefunds.setFocusPainted(false);
//        jrbRefunds.setFocusable(false);
//        jrbRefunds.setRequestFocusEnabled(false);
//        jPanel1.add(jrbRefunds);

        jPanel3.add(jPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

//        private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket, Object ticketext) {
//
//
//
//      java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
//        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address1")));
//        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address2")));
//        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address3")));
//        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.vatno")));
//        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.strc")));
//        allLines.add(new TicketLineConstructor("User:" + getSpaces(1) + (ticket.printUser())));
//        allLines.add(new TicketLineConstructor(getDottedLine(35)));
//        allLines.add(new TicketLineConstructor("Bill No : " + getSpaces(1) + ticket.printId()));
//        allLines.add(new TicketLineConstructor("Table : " + getSpaces(1) + ticketext));
//        allLines.add(new TicketLineConstructor("Date : " + getSpaces(1) + (ticket.printDateForReceipt()) + getSpaces(2) + "Time: " + getSpaces(1) + (ticket.printTime())));
//        allLines.add(new TicketLineConstructor(getDottedLine(35)));
//
//        allLines.add(new TicketLineConstructor("Item Name" + getSpaces(10) + "Qty" + getSpaces(5) + "Amount"));
//        allLines.add(new TicketLineConstructor(getDottedLine(35)));
//
//        for (RetailTicketLineInfo tLine : ticket.getUniqueLines()) {
//
//            String prodName = tLine.printName();
//            String qty = tLine.printMultiply();
//            //  String subValue = tLine.printPriceLine();
//            // String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
//            String total = Formats.DoubleValue.formatValue(tLine.getSubValueBeforeDiscount());
//            System.out.println("prodname---" + prodName.length());
//            if (prodName.length() > 22) {
//                prodName = WordUtils.wrap(prodName, 22);
//                String[] prodNameArray = prodName.split("\n");
//                for (int i = 0; i < prodNameArray.length - 1; i++) {
//                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
//                }
//                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(23 - prodNameArray[prodNameArray.length - 1].length()) + qty + getSpaces((10 - total.length())) + total));
//
//            } else {
//                allLines.add(new TicketLineConstructor(prodName + getSpaces(23 - prodName.length()) + qty + getSpaces((10 - total.length())) + total));
//
//            }
//
//        }
//
//        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
//        String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
//        //String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
//      //  String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
//      //  String swachBharatTax = Formats.DoubleValue.formatValue(ticket.getSwatchBharatTax());
//        String discount = ticket.printDiscount();//Formats.DoubleValue.formatValue(ticket.getLineDiscountOnCategory());
//        String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
//        String roundoff = Formats.DoubleValue.formatValue(ticket.getRoundOffvalue());
//        String total = Formats.DoubleValue.formatValue(ticket.getTotal());
//        allLines.add(new TicketLineConstructor("SubTotal " + getSpaces(26 - subTotal.length()) + (subTotal)));
//        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
//        allLines.add(new TicketLineConstructor("Discount " + getSpaces(25 - discount.length()) + ("-" + discount)));
//
//        //allLines.add(new TicketLineConstructor("Total After Discount " + getSpaces(3) + (totalAfrDiscount)));
//        String aCount = ticket.printTicketCount();
//        
//        //calling consolidated tax (logic applied to bring erp tax configuration) by Shilpa
//         if (ticket.getTaxes().size() != 0) {
//              //tax logic has been changed to show taxes based on name not by rate 19/10/2016
//              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//              Date uniqueTaxStartdate=null;
//          try {
//               uniqueTaxStartdate = sdf.parse("2016-10-20");
//          } catch (ParseException ex) {
//              Logger.getLogger(JRetailRePrintTicket.class.getName()).log(Level.SEVERE, null, ex);
//          }
//          
//             if(ticket.getDate().after(uniqueTaxStartdate)) {
//              for (Map.Entry<String, Double> entry : ticket.getNametaxMap().entrySet()) {
//             String taxName = entry.getKey();
//             double taxValue =entry.getValue();
//             if(taxValue!=0.00){
//                 allLines.add(new TicketLineConstructor(taxName+ getSpaces(35 - (Formats.DoubleValue.formatValue(taxValue).length()+taxName.length())) + (Formats.DoubleValue.formatValue(taxValue))));
//             }
//             }
//         }else{
//             for (Map.Entry<Double, TaxMapInfo> entry : ticket.getTaxMap().entrySet()) {
//             String taxName = entry.getValue().getName();
//             double taxValue =entry.getValue().getTaxValue();
//             if(taxValue!=0.00){
//                 allLines.add(new TicketLineConstructor(taxName+ getSpaces(35 - (Formats.DoubleValue.formatValue(taxValue).length()+taxName.length())) + (Formats.DoubleValue.formatValue(taxValue))));
//             }
//             }
//           }
//            
//        }
//         allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
//        allLines.add(new TicketLineConstructor("Total Amount : " + getSpaces(20 - total.length()) + (total)));
//        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
//        allLines.add(new TicketLineConstructor("Thank you! Please Visit Us again"));
//        return allLines;
//    }
  

    private void m_jPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPrintActionPerformed
        if (m_ticket != null) {
            JRetailReprintReason.showMessage(this, m_dlSales, m_ticket.getTicketId(), "sales");
             if(JRetailReprintReason.status){
                try {
                    String sresourcename="Printer.Preview";
             if(m_ticket.getDate().after(uniqueTaxStartdate)) {
                 sresourcename="Printer.Bill";
             }
                    String sresource = m_dlSystem.getResourceAsXML(sresourcename);   
                    ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                      script.put("taxes", taxcollection);
                      script.put("taxeslogic", taxeslogic);
                      System.out.println("error message"+m_ticket.getErrMsg());
                       script.put("ticket", m_ticket);
                      script.put("place", m_ticket.getTableName());   
                    m_TTP2.printTicket(script.eval(sresource).toString());
                    showMessage(this,"Bill has been printed successfully",Color.green);  
                } catch (TicketPrinterException ex) {
                    Logger.getLogger(JRetailTicketPreviewTicket.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ScriptException ex) {
                    Logger.getLogger(JRetailTicketPreviewTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
        }
       
        
    }//GEN-LAST:event_m_jPrintActionPerformed

//       private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket) {
//
//
//       java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
//       allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
//       allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
//       if(ticket.getCustomer()!=null){
//          allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (ticket.getCustomer().getName())));
//       }
//        String tableId = null;
//        try {
//            try {
//                tableId = m_dlSales.getTableId(ticket.getDocumentNo());
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailTicketsBagTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (NullPointerException ex) {
//            tableId = null;
//        }
//        if(tableId==null){
//          allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ""));
//        }else{
//                allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + tableId));
//          }
//
//      allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));
//       allLines.add(new TicketLineConstructor(getDottedLine(90)));
//       allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price"+getSpaces(9)+"Value(INR)"));
//       allLines.add(new TicketLineConstructor(getDottedLine(90)));
//         for (RetailTicketLineInfo tLine : ticket.getLines()) {
//            String prodName = tLine.printName();
//            String qty = tLine.printMultiply();
//            String subValue = tLine.printPriceLine();
//            String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
//            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue+getSpaces(7 - qty.length() + 12 - subValue.length())+total));
//        }
//         String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
//    //     String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
//     //    String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
//          String discount = Formats.DoubleValue.formatValue(ticket.getTotalDiscount());
//          String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
//          String total =  Formats.DoubleValue.formatValue(ticket.getTotal());
//      allLines.add(new TicketLineConstructor(getDottedLine(90)));
//      allLines.add(new TicketLineConstructor(getSpaces(33)+"Total " + getSpaces(29-subTotal.length()) + (subTotal)));
//      allLines.add(new TicketLineConstructor(getSpaces(33)+"Discount " + getSpaces(25-discount.length()) + ("-"+discount)));
//
//      allLines.add(new TicketLineConstructor(getSpaces(33)+"Total After Discount " + getSpaces(14 - totalAfrDiscount.length()) + (totalAfrDiscount)));
//
////      if(ticket.getTaxes().size()!=0){
////          for(int i=0;i<ticket.getTaxes().size();i++){
////              System.out.println("ticket.getTaxes().get(i).getTax()--"+ticket.getTaxes().get(i).getTax());
////              if(ticket.getTaxes().get(i).getTax()!=0.00){
////                 allLines.add(new TicketLineConstructor(getSpaces(33)+ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(25 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()))));
////              }
////          }
////
////      }
//
//      String aCount = ticket.printTicketCount();
//              //calling consolidated tax (logic applied to bring erp tax configuration) by Shilpa
//         if (ticket.getTaxes().size() != 0) {
//             for (Map.Entry<Double, TaxMapInfo> entry : ticket.getTaxMap().entrySet()) {
//             String taxName = entry.getValue().getName();
//             double taxValue =entry.getValue().getTaxValue();
//             if(taxValue!=0.00){
//                 allLines.add(new TicketLineConstructor(taxName+ getSpaces(27 - Formats.DoubleValue.formatValue(taxValue).length()) + (Formats.DoubleValue.formatValue(taxValue))));
//             }
//             }
//            
//        }
////      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Charge 6%" + getSpaces(18-serviceCharge.length()) + serviceCharge));
//
// //     allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Tax 4.94%" + getSpaces(18-serviceTax.length()) +serviceTax));
//
//      allLines.add(new TicketLineConstructor(getSpaces(33)+"Grand Total : " + getSpaces(21-total.length()) + (total)));
//
//      return allLines;
//    }
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        readTicket(-1, 1);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed

        readTicket(-1,1);
        
    }//GEN-LAST:event_m_jKeysActionPerformed

private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JRetailRePrintFinder finder = JRetailRePrintFinder.getReceiptFinder(this, m_dlSales, dlCustomers);
        finder.setVisible(true);
        FindTicketsInfo selectedTicket = finder.getSelectedTicket();
        if (selectedTicket != null) {
         readTicket(selectedTicket.getTicketId(), selectedTicket.getTicketType());
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
   // private javax.swing.JButton m_jEdit;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanelTicket;
    private javax.swing.JButton m_jPrint;
    private javax.swing.JButton m_jRefund;
    //private com.openbravo.editor.JEditorIntegerPositive m_jTicketEditor;
    private javax.swing.JLabel m_jTicketId;
    // End of variables declaration//GEN-END:variables
    
}
