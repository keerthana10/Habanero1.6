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

package com.openbravo.pos.sales.shared;

import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import javax.swing.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.customers.JCustomerPanel;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.forms.*; 
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.sysfore.pos.cashmanagement.CloseShiftModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JRetailTicketsBagShared extends JRetailTicketsBag {
    private String m_sCurrentTicket = null;
    private DataLogicReceipts dlReceipts = null;
    private CloseShiftModel m_PaymentsToClose = null;
    private JRootApp m_RootApp;
    private TicketParser m_TTP;
    private RetailTicketsEditor panelticket;
    protected DataLogicSystem m_dlSystem;
    private String businessType;
    /** Creates new form JTicketsBagShared */
    public JRetailTicketsBagShared(AppView app, RetailTicketsEditor panelticket, String businessType) {
        
        super(app, panelticket);
        this.panelticket = panelticket;
        this.businessType = businessType;
        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
     //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        initComponents();
           if(m_App.getAppUserView().getUser().getRole().equals("4")){
            m_jCloseShift.setVisible(false);
            m_jCloseDay.setVisible(false);
        }

   }
//
    public void activate() {
        
        // precondicion es que no tenemos ticket activado ni ticket en el panel
        
        m_sCurrentTicket = null;
         if(m_App.getAppUserView().getUser().getRole().equals("4")){
            m_jCloseShift.setVisible(false);
            m_jCloseDay.setVisible(false);
        }else{
             m_jCloseShift.setVisible(true);
            m_jCloseDay.setVisible(true);
        }
      
        if(!businessType.equals("Sales")){
            m_jListTickets.setVisible(false);
            m_jNewTicket.setVisible(false);
            m_jAddCustomer.setVisible(false);
            m_jCloseShift.setVisible(false);
            m_jCloseDay.setVisible(false);
            m_jDelTicket.setVisible(false);
        }
              selectValidTicket();
       
        // Authorization
        m_jDelTicket.setEnabled(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
       
        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }
    
    public boolean deactivate() {
        
        // precondicion es que tenemos ticket activado aqui y ticket en el panel 
        
        m_sCurrentTicket = null;
        m_panelticket.setRetailActiveTicket(null, null);
        
        return true;
        
        // postcondicion es que no tenemos ticket activado ni ticket en el panel
    }
        
    public void deleteTicket() {          
        m_sCurrentTicket = null;
        selectValidTicket();      
    }
    
    protected JComponent getBagComponent() {
        return this;
    }
    
    protected JComponent getNullComponent() {
        return new JPanel();
    }
   
    private void saveCurrentTicket() {
        
        // save current ticket, if exists,
         if(!businessType.equals("HomeDelivery") || businessType.equals("TakeAway")){
        if (m_sCurrentTicket != null) {

            try {
                dlReceipts.insertRetailSharedTicket(m_sCurrentTicket, m_panelticket.getActiveTicket());
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }  
        }
         }
    }
    
    private void setActiveTicket(String id) throws BasicException{
          
        // BEGIN TRANSACTION
//        SharedTicketNameInfo sharedticket = dlReceipts.getRetailSharedTicket1(id);
//        RetailTicketInfo ticket=sharedticket.getContent();
       // String name= dlReceipts.getRetailSharedTicketName(id);
        RetailTicketInfo ticket = dlReceipts.getRetailSharedTicket(id);
       
        if (ticket == null)  {

            throw new BasicException(AppLocal.getIntString("message.noticket"));
        } else {
             //ticket.setSplitOlderName(name);
            dlReceipts.deleteSharedTicket(id);
            m_sCurrentTicket = id;
            m_panelticket.setRetailActiveTicket(ticket, null);
        } 
        // END TRANSACTION                 
    }
    
    private void selectValidTicket() {
       List<SharedTicketInfo> l = null;
        System.out.println("businessType----"+businessType);
       try {
        if(businessType.equals("HomeDelivery")  || businessType.equals("TakeAway")){
           //  System.out.println("businessType--"+businessType);
             newTicket();
        }else{
           //  System.out.println("businessType1--"+businessType);
           l= dlReceipts.getSharedTicketList();
             if (l.size() == 0) {
                newTicket();
            } else {
                setActiveTicket(l.get(0).getId());
            }
        }
          
        } catch (BasicException e) {
            new MessageInf(e).show(this);
            newTicket();
        }
    }    
    
    private void newTicket() {
        saveCurrentTicket();
        RetailTicketInfo ticket = new RetailTicketInfo();
        m_panelticket.setRetailActiveTicket(ticket, null);
        m_sCurrentTicket = UUID.randomUUID().toString(); // m_fmtid.format(ticket.getId());
       
    }
    
    /** This method is called from within the constructor to
     * initialize the form.y
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jNewTicket = new javax.swing.JButton();
        m_jDelTicket = new javax.swing.JButton();
        m_jListTickets = new javax.swing.JButton();
        m_jAddCustomer = new javax.swing.JButton();
        m_jCloseShift = new javax.swing.JButton();
        m_jCloseDay = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(600, 50));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(620, 50));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jNewTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/new-bill.png"))); // NOI18N
        m_jNewTicket.setMnemonic('n');
        m_jNewTicket.setFocusPainted(false);
        m_jNewTicket.setFocusable(false);
        m_jNewTicket.setMargin(new java.awt.Insets(0, 0, 0, 0));
        m_jNewTicket.setMaximumSize(new java.awt.Dimension(143, 55));
        m_jNewTicket.setMinimumSize(new java.awt.Dimension(95, 47));
        m_jNewTicket.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jNewTicket.setRequestFocusEnabled(false);
        m_jNewTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jNewTicketActionPerformed(evt);
            }
        });
        jPanel1.add(m_jNewTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 5, -1, -1));

        m_jDelTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/cancel-bill.png"))); // NOI18N
        m_jDelTicket.setMnemonic('c');
        m_jDelTicket.setFocusPainted(false);
        m_jDelTicket.setFocusable(false);
        m_jDelTicket.setMargin(new java.awt.Insets(0, 0, 0, 0));
        m_jDelTicket.setMaximumSize(new java.awt.Dimension(143, 55));
        m_jDelTicket.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jDelTicket.setRequestFocusEnabled(false);
        m_jDelTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDelTicketActionPerformed(evt);
            }
        });
        jPanel1.add(m_jDelTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 5, -1, -1));

        m_jListTickets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/switch-bill.png"))); // NOI18N
        m_jListTickets.setMnemonic('s');
        m_jListTickets.setFocusPainted(false);
        m_jListTickets.setFocusable(false);
        m_jListTickets.setMargin(new java.awt.Insets(0, 0, 0, 0));
        m_jListTickets.setMaximumSize(new java.awt.Dimension(143, 55));
        m_jListTickets.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jListTickets.setRequestFocusEnabled(false);
        m_jListTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jListTicketsActionPerformed(evt);
            }
        });
        jPanel1.add(m_jListTickets, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 5, -1, -1));

        m_jAddCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/customer.png"))); // NOI18N
        m_jAddCustomer.setMnemonic('a');
        m_jAddCustomer.setFocusable(false);
        m_jAddCustomer.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jAddCustomerActionPerformed(evt);
            }
        });
        jPanel1.add(m_jAddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 5, -1, -1));

        m_jCloseShift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/close-shift.png"))); // NOI18N
        m_jCloseShift.setMnemonic('t');
        m_jCloseShift.setFocusable(false);
        m_jCloseShift.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jCloseShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseShiftActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCloseShift, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 5, -1, -1));

        m_jCloseDay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/close-day.png"))); // NOI18N
        m_jCloseDay.setMnemonic('y');
        m_jCloseDay.setFocusable(false);
        m_jCloseDay.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jCloseDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseDayActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCloseDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 5, -1, -1));

        add(jPanel1, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jListTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jListTicketsActionPerformed

        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                try {
                    List<SharedTicketInfo> l = dlReceipts.getSharedTicketList();

                    JRetailTicketsBagSharedList listDialog = JRetailTicketsBagSharedList.newJDialog(JRetailTicketsBagShared.this);
                    String id = listDialog.showTicketsList(l); 

                    if (id != null) {
                        saveCurrentTicket();
                        setActiveTicket(id); 
                    }
                } catch (BasicException e) {
                    new MessageInf(e).show(JRetailTicketsBagShared.this);
                    newTicket();
                }                    
            }
        });
        
    }//GEN-LAST:event_m_jListTicketsActionPerformed

    private void m_jDelTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDelTicketActionPerformed
        
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            deleteTicket();
        }  
    }//GEN-LAST:event_m_jDelTicketActionPerformed

    private void m_jNewTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jNewTicketActionPerformed
        newTicket();
        
    }//GEN-LAST:event_m_jNewTicketActionPerformed

    private void m_jAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jAddCustomerActionPerformed
//        m_App.getAppUserView().showTask("com.openbravo.pos.customers.CustomersPanel");
         try {
            JCustomerPanel.showMessage(this, m_App);
        } catch (BasicException ex) {
        }// TODO add your handling code here:
         panelticket.activate();
    }//GEN-LAST:event_m_jAddCustomerActionPerformed

    private void m_jCloseDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseDayActionPerformed
        m_App.getAppUserView().showTask("com.sysfore.pos.cashmanagement.JPanelCashReconciliation");        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCloseDayActionPerformed

    private void m_jCloseShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseShiftActionPerformed

       int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannacloseshift"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
             try {
            m_PaymentsToClose = CloseShiftModel.loadInstance(m_App);
            } catch (BasicException ex) {
                Logger.getLogger(CloseShiftModel.class.getName()).log(Level.SEVERE, null, ex);
            }

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
            if(m_App.getCloseCashsequence()==1)
            {
            m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getCloseCashsequence()+1, dNow, null);
            m_PaymentsToClose.setSeq(1);
            }
            else
            {
            m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getCloseCashsequence(), dNow, null);
            }
                //m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getActiveCashSequence() + 1, dNow, null);
           try {
                // creamos la caja activa
                m_dlSystem.execInsertCash(new Object[]{m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashSequence(), m_App.getProperties().getPosNo(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});
            } catch (BasicException ex) {
                Logger.getLogger(JTicketsBagShared.class.getName()).log(Level.SEVERE, null, ex);
            }

  
                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);

        }else{

        }


    }//GEN-LAST:event_m_jCloseShiftActionPerformed
    
      private void printCloseShift(String report) {

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton m_jAddCustomer;
    private javax.swing.JButton m_jCloseDay;
    private javax.swing.JButton m_jCloseShift;
    private javax.swing.JButton m_jDelTicket;
    private javax.swing.JButton m_jListTickets;
    private javax.swing.JButton m_jNewTicket;
    // End of variables declaration//GEN-END:variables

    
    
}
