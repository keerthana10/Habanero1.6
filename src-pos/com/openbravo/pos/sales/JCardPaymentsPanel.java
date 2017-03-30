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

import com.openbravo.basic.BasicException;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;

import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.payment.JPaymentNotifier;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoList;
import static com.openbravo.pos.sales.JRetailReprintReason.parentLocal;
import static com.openbravo.pos.sales.JRetailReprintReason.ticketno;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.util.RoundUtils;
import com.sysfore.pos.panels.CardTypeInfo;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author archana
 */
public class JCardPaymentsPanel extends JDialog {
     int x = 500;
    int y = 300;
    int width = 250;
    int height = 180;
    static int ticketno=0;
    static Component parentLocal = null;
    private static DataLogicReceipts localDlReceipts = null;
     static RetailTicketInfo tinfoLocal=null;
    public static boolean status=false;
    double tipAmount;
    static JPaymentEditor paymentEditor;
    java.util.List<CardTypeInfo> cardTypeList;
    
    public static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, int ticketnum, RetailTicketInfo ticketInfo, JPaymentEditor editor) {
     localDlReceipts = dlReceipts;
        parentLocal = parent;
       ticketno=ticketnum;
       tinfoLocal=ticketInfo;
       paymentEditor = editor;

       return showMessage(parent, dlReceipts, 1,paymentEditor);
    }

    private static boolean showMessage(Component parent, DataLogicReceipts dlReceipts, int x,JPaymentEditor editor) {

         Window window = getWindow(parent);
        JCardPaymentsPanel myMsg;
        if (window instanceof Frame) {
          myMsg = new JCardPaymentsPanel((Frame) window, true);
        } else {
          myMsg = new JCardPaymentsPanel((Dialog) window, true);
        }
        return myMsg.init(dlReceipts);
    }

    private JCardPaymentsPanel(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JCardPaymentsPanel(Dialog dialog, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    public boolean init(DataLogicReceipts dlReceipts) {
        initComponents();
        setTitle("Other Payments");
         m_jTxtDescription.setVisible(false);
            m_jLblRemarks.setVisible(false);
        m_jCboPaymentMode.removeAllItems();
        try {
            cardTypeList = (List<CardTypeInfo>) dlReceipts.getCardType();
        } catch (BasicException ex) {
            Logger.getLogger(JCardPaymentsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        

            for (CardTypeInfo dis : cardTypeList) {
                m_jCboPaymentMode.addItem(dis.getCardType());

            }
            m_jCboPaymentMode.setSelectedIndex(0);
           setVisible(true);
        return status;
    }


  public void populateCardType(DataLogicReceipts dlReceipts){
          m_jCboPaymentMode.removeAllItems();
        try {
            cardTypeList = (List<CardTypeInfo>) dlReceipts.getCardType();
        } catch (BasicException ex) {
            Logger.getLogger(JCardPaymentsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        

            for (CardTypeInfo dis : cardTypeList) {
                m_jCboPaymentMode.addItem(dis.getCardType());

            }
            m_jCboPaymentMode.setSelectedIndex(-1);
           
    }
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelTips = new javax.swing.JLabel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        m_jCboPaymentMode = new javax.swing.JComboBox();
        m_jLblRemarks = new javax.swing.JLabel();
        m_jTxtDescription = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosing(evt);
            }
        });

        jLabelTips.setText("Payments Mode");

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        m_jCboPaymentMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCboPaymentModeActionPerformed(evt);
            }
        });

        m_jLblRemarks.setText("Description");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(134, 134, 134))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jLabelTips, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(m_jLblRemarks, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(52, 52, 52)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(m_jTxtDescription)
                            .add(m_jCboPaymentMode, 0, 117, Short.MAX_VALUE))
                        .add(102, 102, 102))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelTips)
                    .add(m_jCboPaymentMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jLblRemarks)
                    .add(m_jTxtDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonOk)
                    .add(jButtonCancel))
                .add(24, 24, 24))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-491)/2, (screenSize.height-213)/2, 491, 213);
    }// </editor-fold>//GEN-END:initComponents


    private void WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosing
          status=false;
          dispose();
    }//GEN-LAST:event_WindowClosing

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        //  Double  tipsAmt=Double.parseDouble(m_jTxtTips.getText().toString());
        String description = m_jTxtDescription.getText();
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(description).find();
        if(m_jCboPaymentMode.getSelectedItem().equals("Others")){
             if(m_jTxtDescription.getText().equals("")){
             showMessage(this, "Please enter the description.");
        }else if(hasSpecialChar){
            showMessage(this, "Please enter the valid description.");
            m_jTxtDescription.setText("");
        }else{
                 String paymentMode= m_jCboPaymentMode.getSelectedItem().toString();

             paymentEditor.setOtherPaymentMode(paymentMode);
             paymentEditor.setOtherDescription(description);
             status=true;
             this.dispose();
        }
        }
       
        else{

             String paymentMode= m_jCboPaymentMode.getSelectedItem().toString();

             paymentEditor.setOtherPaymentMode(paymentMode);
             paymentEditor.setOtherDescription(description);
             status=true;
             this.dispose();
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        status=false;
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void m_jCboPaymentModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCboPaymentModeActionPerformed
 if(m_jCboPaymentMode.getSelectedItem().equals("Others")){
            m_jTxtDescription.setVisible(true);
            m_jLblRemarks.setVisible(true);
        }else{
            m_jTxtDescription.setVisible(false);
            m_jLblRemarks.setVisible(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_m_jCboPaymentModeActionPerformed
 
 

    private void showMessage(JCardPaymentsPanel aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
 private JPanel getLabelPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(Color.RED);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelTips;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox m_jCboPaymentMode;
    private javax.swing.JLabel m_jLblRemarks;
    private javax.swing.JTextField m_jTxtDescription;
    // End of variables declaration//GEN-END:variables


    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
    
                          
    

    /**
     * @return the enablity
     */
 }