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
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.sysfore.pos.purchaseorder.PurchaseProductRenderer;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class RetailReceiptAddonList extends JDialog {
private static DataLogicSystem m_dlSystem;
private ProductInfoExt m_ReturnProduct;
 private ListProvider lpr;
private ArrayList<ProductInfoExt> list=null;
    private String primaryid=null;
    AppUser appUser = null;
    private String hiddenname;
    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicSales dlSales = null;
    private static AppView m_App;
    private static String menuId;
    public String[] strings = {""};
    public DefaultListModel model = null;
   // public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    public JPanel parent = null;
    static RetailTicketInfo tinfoLocal = null;
    public static JRetailPanelTicket addon;
    public static JRetailPanelTakeAway addonTakeAway;
    static Component parentLocal = null;
    int x = 530;
    int y = 270;
    int width = 320;
    int height = 50;

    private RetailReceiptAddonList(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, width, height);

    }

    private RetailReceiptAddonList(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, width, height);
    }

    public static ProductInfoExt showMessage(Component parent, DataLogicCustomers dlCustomers,AppView app, JRetailPanelTicket jaddon,String pname,String menupId) throws BasicException {
      //  tinfoLocal = tinfo;
        parentLocal = parent;
        addon = jaddon;
        m_App=app;
        menuId=menupId;
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
      dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
       return showMessage(parent, dlCustomers,pname);
    }
public static ProductInfoExt showMessage(Component parent, DataLogicCustomers dlCustomers,AppView app, JRetailPanelTakeAway jaddon,String pname,String menupId) throws BasicException {
      //  tinfoLocal = tinfo;
        parentLocal = parent;
        addonTakeAway = jaddon;
        m_App=app;
        menuId=menupId;
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
      dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
       return showMessage(parent, dlCustomers,pname);
    }
    private static ProductInfoExt showMessage(Component parent, DataLogicCustomers dlSales,String pname) {

        Window window = getWindow(parent);
        RetailReceiptAddonList myMsg;
        if (window instanceof Frame) {
            myMsg = new RetailReceiptAddonList((Frame) window, true);
        } else {
            myMsg = new RetailReceiptAddonList((Dialog) window, true);
        }
         
       return myMsg.init(dlSales,pname);

    }
    
     private static class MyListData extends javax.swing.AbstractListModel {
        
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        public int getSize() {
            return m_data.size();
        } 
    }

    public ProductInfoExt init(DataLogicCustomers dlCustomers,String pName) {

        initComponents();
        Window window = getWindow(parent);
       
//        ArrayList<String> itemName = new ArrayList<String>();   
//        try {
//            list =(ArrayList<ProductInfoExt>)dlCustomers.getAddonProduct(pName);
//        } catch (BasicException ex) {
//            Logger.getLogger(RetailReceiptAddonList.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        
//        for(ProductInfoExt product:list)
//        {
//          itemName.add(product.getName());
//        }
        if(m_App.getProperties().getProperty("machine.menustatus").equals("false")){
         lpr = new ListProviderCreator(dlCustomers.getAddonProduct(pName));
        }else{
         lpr = new ListProviderCreator(dlCustomers.getMenuAddonProduct(pName,menuId));  
        }
         jListProducts.setCellRenderer(new PurchaseProductRenderer());
         getRootPane().setDefaultButton(m_jBtnOk); 
            
        try {
            jListProducts.setModel(new MyListData(lpr.loadData()));
            if (jListProducts.getModel().getSize() > 0) {
                jListProducts.setSelectedIndex(0);
            }
        } catch (BasicException ex) {
            Logger.getLogger(RetailReceiptAddonList.class.getName()).log(Level.SEVERE, null, ex);
        }
        // jAddonList.setModel(new ComboBoxValModel(itemName));
         setTitle("Addon Products");
        // m_ReturnProduct = null;
         setVisible(true);
         return m_ReturnProduct;

    }
        
        
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jBtnOk = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListProducts = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(200, 200));

        jPanel1.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jBtnOk.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnOk.setText("Ok");
        m_jBtnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnOkActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 75, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, 90, -1));

        jListProducts.setPreferredSize(new java.awt.Dimension(50, 50));
        jListProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListProductssMouseClicked(evt);
            }
        });
        jListProducts.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListProductssValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListProducts);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 260, 290));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleParent(this);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-347)/2, (screenSize.height-471)/2, 347, 471);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        m_ReturnProduct=null;
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void m_jBtnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnOkActionPerformed
        m_ReturnProduct = (ProductInfoExt) jListProducts.getSelectedValue();
        dispose();      
    }//GEN-LAST:event_m_jBtnOkActionPerformed

    private void jListProductssMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListProductssMouseClicked
        if (evt.getClickCount() == 2) {
            m_ReturnProduct = (ProductInfoExt) jListProducts.getSelectedValue();
            dispose();
        }
    }//GEN-LAST:event_jListProductssMouseClicked

    private void jListProductssValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListProductssValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jListProductssValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JList jListProducts;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jBtnOk;
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

   

    private void closeDialog() {
        this.dispose();
    }
    
    
}
