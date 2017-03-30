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

/**
 *
 * @author archana
 */
public class JTipsDialog extends JDialog {
     int x = 500;
    int y = 300;
    int width = 350;
    int height = 280;
    static int ticketno=0;
    static Component parentLocal = null;
    private static DataLogicSales localDlSales = null;
     static RetailTicketInfo tinfoLocal=null;
    public static boolean status=false;
    double tipAmount;
   
    
    public static void showMessage(Component parent, DataLogicSales dlSales, int ticketnum, RetailTicketInfo ticketInfo) {
     localDlSales = dlSales;
        parentLocal = parent;
       ticketno=ticketnum;
       tinfoLocal=ticketInfo;
        showMessage(parent, dlSales, 1);
    }

    private static void showMessage(Component parent, DataLogicSales dlSales, int x) {

         Window window = getWindow(parent);
        JTipsDialog myMsg;
        if (window instanceof Frame) {
          myMsg = new JTipsDialog((Frame) window, true);
        } else {
          myMsg = new JTipsDialog((Dialog) window, true);
        }
        myMsg.init(dlSales);
    }

    private JTipsDialog(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JTipsDialog(Dialog dialog, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    public void init(DataLogicSales dlSales) {
        initComponents();
        m_jTxtTips.addEditorKeys(m_jkeys);
        m_jTxtTips.setEnabled(true);
        m_jTxtTips.activate();
        setTitle("Tips");
             if( !System.getProperty("os.name").equalsIgnoreCase("Linux")){
            try {
            Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
            } catch (IOException ex) {
            Logger.getLogger(JRetailReprintReason.class.getName()).log(Level.SEVERE, null, ex);
          }
          }  
        setVisible(true);
//        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
//        AppConfig ap = new AppConfig(file);
//        ap.load();
    
    }



  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jkeys = new com.openbravo.editor.JEditorNumberKeys();
        jLabelTips = new javax.swing.JLabel();
        m_jTxtTips = new com.openbravo.editor.JEditorCurrencyPositive();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosing(evt);
            }
        });

        m_jkeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jkeysActionPerformed(evt);
            }
        });

        jLabelTips.setText("Tips");

        m_jTxtTips.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                m_jTxtTipsFocusGained(evt);
            }
        });

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

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabelTips)
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jkeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtTips, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(270, 270, 270))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jTxtTips, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelTips))
                .add(18, 18, 18)
                .add(m_jkeys, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonOk)
                    .add(jButtonCancel))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 274, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(31, 31, 31)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleParent(this);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-294)/2, (screenSize.height-428)/2, 294, 428);
    }// </editor-fold>//GEN-END:initComponents


    private void WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosing
     
          dispose();
    }//GEN-LAST:event_WindowClosing

    private void m_jkeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jkeysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jkeysActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        //  Double  tipsAmt=Double.parseDouble(m_jTxtTips.getText().toString());
           String tipsAmt= m_jTxtTips.getText().toString();
       
           if(!tipsAmt.equals("")){
               
               tinfoLocal.setTipAmt(Double.parseDouble(tipsAmt));
               System.out.println("Tipamount :"+tinfoLocal.getTipAmt());
            
            }
      
        this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
      this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void m_jTxtTipsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jTxtTipsFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jTxtTipsFocusGained
 
 

    private void showMessage(JTipsDialog aThis, String msg) {
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
    private com.openbravo.editor.JEditorCurrencyPositive m_jTxtTips;
    private com.openbravo.editor.JEditorNumberKeys m_jkeys;
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