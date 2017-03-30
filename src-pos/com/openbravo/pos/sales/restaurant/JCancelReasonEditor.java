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
package com.openbravo.pos.sales.restaurant;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.Reasoninfo;
import com.openbravo.pos.sales.kotInfo;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketInfo;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.util.Date;
import java.util.Properties;

class JCancelReasonEditor extends JDialog {

//    public javax.swing.JDialog dEdior = null;
  //  private Properties dbp = new Properties();
 //   private DataLogicReceipts dlReceipts = null;
  //  private DataLogicCustomers dlCustomers = null;
//    private AppView m_app;
 //   public String[] strings = {""};
 //   public DefaultListModel model = null;
    public java.util.List<Reasoninfo> list = null;
//    public boolean updateMode = false;
    static Component parentLocal = null;
    static RetailTicketInfo tinfoLocal = null;
     static int index = 0;
 //   public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
    private static DataLogicSales localDlSales = null;
    private static DataLogicSystem localDlSystem = null;
    private static AppView m_App;
    private static String localTableName;
    private java.util.List<kotInfo> kotTicketlist;
    private static TicketParser m_TTP;
    public static boolean setFlag =false;
  //  private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 250;
    static String localhomeDelivery;
     static String orderTaking;
     private static Place m_PlaceCurrent;

    public static void showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, String OrderTaking,  String homeDelivery,DataLogicSales dlSales,DataLogicSystem dlSystem, AppView app, String placeName, Place m_PlaceCurrent) {
        localDlReceipts = dlReceipts;
        localDlSales = dlSales;
        parentLocal = parent;
        tinfoLocal = tinfo;
        localDlSystem = dlSystem;
         orderTaking = OrderTaking;
        localhomeDelivery=homeDelivery;
        localTableName = placeName;
        JCancelReasonEditor.m_PlaceCurrent = m_PlaceCurrent;
        m_App = app;
        setFlag =false;
      //  index=tindex;
      m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);
        showMessage(parent, dlReceipts, 1);
    }

    private static void showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JCancelReasonEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JCancelReasonEditor((Frame) window, true);
        } else {
            myMsg = new JCancelReasonEditor((Dialog) window, true);
        }
        myMsg.init(dlReceipts);
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
    private JCancelReasonEditor(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JCancelReasonEditor(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }



    public void init(DataLogicReceipts dlReceipts) {

        initComponents();
     try {
          //  m_jReason.addItem("");
            java.util.List<Reasoninfo> reasonList = dlReceipts.getReasonList();
            for (Reasoninfo dis : reasonList) {
                m_jReason.addItem(dis.getReason());

            }
            m_jReason.setSelectedIndex(-1);
        } catch (BasicException ex) {
            Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

   
      

        setTitle("Reason Editor");
        setVisible(true);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
        

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jReasonLbl = new javax.swing.JLabel();
        m_jbtnOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTxtReason = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        m_jReason = new javax.swing.JComboBox();
        m_jCancel = new javax.swing.JButton();

        m_jReasonLbl.setText("Comment");

        m_jbtnOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnOk.setText("Ok");
        m_jbtnOk.setMaximumSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.setMinimumSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jbtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnOkActionPerformed(evt);
            }
        });

        m_jTxtReason.setColumns(20);
        m_jTxtReason.setRows(5);
        jScrollPane1.setViewportView(m_jTxtReason);

        jLabel1.setText("Reason");

        m_jReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReasonActionPerformed(evt);
            }
        });

        m_jCancel.setText("Cancel");
        m_jCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .add(m_jReasonLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(m_jbtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(m_jReason, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(m_jReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 18, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jReasonLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jbtnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCancel))
                .add(16, 16, 16))
        );

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnOkActionPerformed
   String reason = m_jTxtReason.getText();
     int reasonIndex = m_jReason.getSelectedIndex();
       tinfoLocal.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
           tinfoLocal.setActiveCash(m_App.getActiveCashIndex());
           tinfoLocal.setActiveDay(m_App.getActiveDayIndex());
           tinfoLocal.setDate(new Date()); //
            String ticketDocument;
            ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+tinfoLocal.getTicketId();
         if (reasonIndex == -1) {

                JOptionPane.showMessageDialog(null, "Please select the reason");
            }
        else{
         Reasoninfo reasonInfo = new Reasoninfo();

         String reasonItem = m_jReason.getSelectedItem().toString();
            System.out.println("enrtrr "+reasonItem+"---"+reasonInfo.getStatus());
         String reasonId = null;
            try {
                System.out.println("enrtrr1 "+reasonItem);
                reasonId = localDlReceipts.getReasonId(reasonItem);
                System.out.println("get reasonid--"+reasonId);
            } catch (BasicException ex) {
                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
             java.util.List<RetailTicketLineInfo> panelLines = tinfoLocal.getLines();
            System.out.println("enter---"+tinfoLocal.getLinesCount());
            try {
                  localDlSales.saveRetailCancelTicket(tinfoLocal, m_App.getProperties().getStoreName(),ticketDocument,orderTaking, m_App.getInventoryLocation(),reason, reasonId, m_App.getProperties().getPosNo(),localhomeDelivery);
            } catch (BasicException ex) {
                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
             String kotTicketId = null;
                Integer kotCount=0;
                int kotTicket=0;

            try {
                kotTicketId = (localDlReceipts.getkotTicketId("Y"));

            } catch (BasicException ex) {
                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
              if(kotTicketId==null){
                kotTicket=1;
            }else{
                   kotTicket=Integer.parseInt(kotTicketId);
                   kotTicket = kotTicket+1;
            }
//            try {
//                localDlReceipts.deleteKot(tinfoLocal.getId());
//            } catch (BasicException ex) {
//                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
//            }
            for(int i=0;i<tinfoLocal.getLinesCount();i++){
                   localDlReceipts.insertCancelledKot(tinfoLocal.getId(),tinfoLocal.getDate(),tinfoLocal.getTicketId(),panelLines.get(i).getProductID(), "Y",(1*panelLines.get(i).getMultiply()) ,kotTicket,"Y",reason,reasonId,tinfoLocal.getPlaceId(),tinfoLocal.getUser().getId(),tinfoLocal.getAccountDate());
            }
             try {
                kotTicketlist = localDlReceipts.getKot(tinfoLocal.getId());
             } catch (BasicException ex) {
                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
             }
             kotInfo kInfolist =  new kotInfo();
             kInfolist.setKotId(kotTicket);

             tinfoLocal.setKotLines(kotTicketlist);
            
//             try {
//                localDlReceipts.updateKotIsprinted(tinfoLocal.getId());
//              } catch (BasicException ex) {
//                Logger.getLogger(JCancelReasonEditor.class.getName()).log(Level.SEVERE, null, ex);
//
//        }

            setFlag =true;
            this.dispose();
        }

}//GEN-LAST:event_m_jbtnOkActionPerformed
private void printKotTicket(String sresourcename, RetailTicketInfo ticket, kotInfo kot) {


        String sresource = localDlSystem.getResourceAsXML(sresourcename);

        kot.setkotName("Cancelled Order");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JCancelReasonEditor.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                 script.put("kot", kot);
                  script.put("place", localTableName);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JCancelReasonEditor.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JCancelReasonEditor.this);
            }
        }
    }

    private void m_jReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jReasonActionPerformed

    private void m_jCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCancelActionPerformed
        setFlag =false;
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCancelActionPerformed

    public static boolean getFlag() {
        return setFlag;
    }

    public void setFlag(boolean setFlag) {
        this.setFlag = setFlag;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jCancel;
    private javax.swing.JComboBox m_jReason;
    private javax.swing.JLabel m_jReasonLbl;
    private javax.swing.JTextArea m_jTxtReason;
    private javax.swing.JButton m_jbtnOk;
    // End of variables declaration//GEN-END:variables

}

   

    /**
     * @return the enablity
     */
  
    /**
     * @param enablity the enablity to set
     */
   
