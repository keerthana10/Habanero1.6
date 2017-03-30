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
import com.sysfore.pos.hotelmanagement.ServiceChargeMapInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrianromero
 */
public class JTaxMappingPanel extends JPanel implements JPanelView,BeanFactoryApp{
    private static boolean insert;
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
    public DefaultListModel model1 = null;
    public java.util.List<ServiceTaxInfo> serviceTaxList = null;
   public java.util.List<ServiceChargeInfo> serviceChargeList = null;
   public java.util.List<ServiceChargeMapInfo> serviceChargeMapInfo = new ArrayList<ServiceChargeMapInfo>();
   public java.util.List<ServiceTaxMapInfo> serviceTaxMapInfo = new ArrayList<ServiceTaxMapInfo>();
   java.util.List<ServiceChargeInfo> serviceChargeMapList = new ArrayList<ServiceChargeInfo>();
   java.util.List<ServiceTaxInfo> serviceTaxMapList = new ArrayList<ServiceTaxInfo>();
  public java.util.List<TaxMappingInfo> taxMappinglist = null;

     
    public boolean updateMode = false;
        java.util.List<BusinessTypeInfo> businessTypeList;
   // static int x = 400;
  //  static int y = 200;
    /** Creates new form JPanelCloseMoney */
    public JTaxMappingPanel() {
        
        initComponents();
         setInsert(true);
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        
         PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
     //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
   // JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
         initComponents();
        try {
            populateList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        try {
//            populateServiceTaxList();
//            populateServiceChargeList();
//        } catch (BasicException ex) {
//            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
 
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
        return AppLocal.getIntString("Menu.TaxMappingPanel");
    }

    public void activate() throws BasicException {
         setInsert(true);
          populateList();
           populateBusinessType();
           try {
            populateServiceTaxList();
            populateServiceChargeList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    
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
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        m_jBusinessTypeList = new javax.swing.JList();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jLblBusinessType = new javax.swing.JLabel();
        m_cboBusinessType = new javax.swing.JComboBox();
        m_jServiceTaxPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        m_jServiceTaxMap = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jBtnToRightAll = new javax.swing.JButton();
        jBtnToRightOne = new javax.swing.JButton();
        jBtnToLeftOne = new javax.swing.JButton();
        jBtnToLeftAll = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jServiceTaxList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        m_jServiceChargePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        m_jServiceChargeList = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jBtnToRightAll1 = new javax.swing.JButton();
        jBtnToRightOne1 = new javax.swing.JButton();
        jBtnToLeftOne1 = new javax.swing.JButton();
        jBtnToLeftAll1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        m_jServiceChargeMap = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        m_jChkServiceTaxIncluded = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        m_jChkServiceChargeIncluded = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jBusinessTypeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jBusinessTypeListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jBusinessTypeList);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 57, 154, 525));

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

        m_jLblBusinessType.setText("BusinessType");
        add(m_jLblBusinessType, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 60, 123, -1));

        add(m_cboBusinessType, new org.netbeans.lib.awtextra.AbsoluteConstraints(352, 57, 179, -1));

        m_jServiceTaxMap.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane3.setViewportView(m_jServiceTaxMap);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jBtnToRightAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/2rightarrow.png"))); // NOI18N
        jBtnToRightAll.setFocusPainted(false);
        jBtnToRightAll.setFocusable(false);
        jBtnToRightAll.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToRightAll.setRequestFocusEnabled(false);
        jBtnToRightAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToRightAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jPanel4.add(jBtnToRightAll, gridBagConstraints);

        jBtnToRightOne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1rightarrow.png"))); // NOI18N
        jBtnToRightOne.setFocusPainted(false);
        jBtnToRightOne.setFocusable(false);
        jBtnToRightOne.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToRightOne.setRequestFocusEnabled(false);
        jBtnToRightOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToRightOneActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jBtnToRightOne, gridBagConstraints);

        jBtnToLeftOne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1leftarrow.png"))); // NOI18N
        jBtnToLeftOne.setFocusPainted(false);
        jBtnToLeftOne.setFocusable(false);
        jBtnToLeftOne.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToLeftOne.setRequestFocusEnabled(false);
        jBtnToLeftOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToLeftOneActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jBtnToLeftOne, gridBagConstraints);

        jBtnToLeftAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/2leftarrow.png"))); // NOI18N
        jBtnToLeftAll.setFocusPainted(false);
        jBtnToLeftAll.setFocusable(false);
        jBtnToLeftAll.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToLeftAll.setRequestFocusEnabled(false);
        jBtnToLeftAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToLeftAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jBtnToLeftAll, gridBagConstraints);

        m_jServiceTaxList.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane2.setViewportView(m_jServiceTaxList);

        javax.swing.GroupLayout m_jServiceTaxPanelLayout = new javax.swing.GroupLayout(m_jServiceTaxPanel);
        m_jServiceTaxPanel.setLayout(m_jServiceTaxPanelLayout);
        m_jServiceTaxPanelLayout.setHorizontalGroup(
            m_jServiceTaxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_jServiceTaxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        m_jServiceTaxPanelLayout.setVerticalGroup(
            m_jServiceTaxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_jServiceTaxPanelLayout.createSequentialGroup()
                .addGroup(m_jServiceTaxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(m_jServiceTaxPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, m_jServiceTaxPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                    .addGroup(m_jServiceTaxPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        add(m_jServiceTaxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 146, 450, 180));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1182, 352, -1, -1));

        m_jServiceChargeList.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane4.setViewportView(m_jServiceChargeList);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jBtnToRightAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/2rightarrow.png"))); // NOI18N
        jBtnToRightAll1.setFocusPainted(false);
        jBtnToRightAll1.setFocusable(false);
        jBtnToRightAll1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToRightAll1.setRequestFocusEnabled(false);
        jBtnToRightAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToRightAll1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jPanel5.add(jBtnToRightAll1, gridBagConstraints);

        jBtnToRightOne1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1rightarrow.png"))); // NOI18N
        jBtnToRightOne1.setFocusPainted(false);
        jBtnToRightOne1.setFocusable(false);
        jBtnToRightOne1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToRightOne1.setRequestFocusEnabled(false);
        jBtnToRightOne1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToRightOne1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jBtnToRightOne1, gridBagConstraints);

        jBtnToLeftOne1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1leftarrow.png"))); // NOI18N
        jBtnToLeftOne1.setFocusPainted(false);
        jBtnToLeftOne1.setFocusable(false);
        jBtnToLeftOne1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToLeftOne1.setRequestFocusEnabled(false);
        jBtnToLeftOne1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToLeftOne1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jBtnToLeftOne1, gridBagConstraints);

        jBtnToLeftAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/2leftarrow.png"))); // NOI18N
        jBtnToLeftAll1.setFocusPainted(false);
        jBtnToLeftAll1.setFocusable(false);
        jBtnToLeftAll1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jBtnToLeftAll1.setRequestFocusEnabled(false);
        jBtnToLeftAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnToLeftAll1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel5.add(jBtnToLeftAll1, gridBagConstraints);

        m_jServiceChargeMap.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane5.setViewportView(m_jServiceChargeMap);

        javax.swing.GroupLayout m_jServiceChargePanelLayout = new javax.swing.GroupLayout(m_jServiceChargePanel);
        m_jServiceChargePanel.setLayout(m_jServiceChargePanelLayout);
        m_jServiceChargePanelLayout.setHorizontalGroup(
            m_jServiceChargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_jServiceChargePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        m_jServiceChargePanelLayout.setVerticalGroup(
            m_jServiceChargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, m_jServiceChargePanelLayout.createSequentialGroup()
                .addGroup(m_jServiceChargePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );

        add(m_jServiceChargePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 407, 490, 170));

        jLabel3.setText("Is Tax Included");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 119, 113, 21));
        add(m_jChkServiceTaxIncluded, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 119, -1, -1));

        jLabel4.setText("Is Tax Included");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 116, 21));
        add(m_jChkServiceChargeIncluded, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Service Charge");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 370, 23));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Service Tax");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 94, 370, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jBusinessTypeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jBusinessTypeListValueChanged
        // TODO add your handling code here:
       listAction();
       setInsert(false);
    }//GEN-LAST:event_m_jBusinessTypeListValueChanged
public void clearFields(){
      m_cboBusinessType.setSelectedIndex(-1);
       m_jChkServiceTaxIncluded.setSelected(false);
       m_jChkServiceChargeIncluded.setSelected(false);
       try {
            populateList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            populateServiceTaxList();
            populateServiceChargeList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultListModel ServiceTaxMapModel = (DefaultListModel) m_jServiceTaxMap.getModel();
        ServiceTaxMapModel.removeAllElements();
        DefaultListModel ServiceChargeMapModel = (DefaultListModel) m_jServiceChargeMap.getModel();
        ServiceChargeMapModel.removeAllElements();
}
    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed
     clearFields();
       setInsert(true);
   //    m_jServiceTaxMap.removeAll();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jBusinessTypeList.getSelectedIndex();
        System.out.println("index is " + index);

        if (index == -1) {
    //        JOptionPane.showMessageDialog(JBusinessTypePanel.this, "Please select the business type");
        } else {
            try {
                String id = taxMappinglist.get(index).getId();
                dlCustomers.deleteTaxMappingLines(id);
                dlCustomers.deleteTaxMapping(id);           
                clearFields();
                updateMode = false;

            } catch (BasicException ex) {
                Logger.getLogger(JBusinessTypePanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
       

        if (m_cboBusinessType.getSelectedItem()==null) {
            JOptionPane.showMessageDialog(JTaxMappingPanel.this, "Please select the business type");
        } else {
            try {
                saveButtonAction();
                 clearFields();
            } catch (BasicException ex) {
                Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed

    private void jBtnToRightAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToRightAllActionPerformed
        try {
            populateServiceTaxMapList();
         } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jBtnToRightAllActionPerformed

    private void jBtnToRightOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToRightOneActionPerformed
        try {
            populateServiceTaxSingleMapList();
            /* RetailTicketLineInfo[] lines = receiptone.getSelectedLinesUnit();
            if (lines != null) {
            receipttwo.addSelectedLines(lines);
            }*/
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jBtnToRightOneActionPerformed

    private void jBtnToLeftOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToLeftOneActionPerformed
     int index = m_jBusinessTypeList.getSelectedIndex();
     String id = taxMappinglist.get(index).getId();
        java.util.List<ServiceTaxMappedInfo> serviceMapInfo = new ArrayList<ServiceTaxMappedInfo>();
         String businessType = businessTypeList.get(m_cboBusinessType.getSelectedIndex()).getId();
        try {
            serviceMapInfo = dlCustomers.getServiceTaxMapList(businessType);

        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }


        DefaultListModel model = (DefaultListModel) m_jServiceTaxMap.getModel();
        int selectedIndex = m_jServiceTaxMap.getSelectedIndex();
      
        if (selectedIndex != -1) {
            model.remove(selectedIndex);
        }
        try {
            dlCustomers.deleteTaxLines(id, serviceMapInfo.get(selectedIndex).getId());

        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       serviceMapInfo.remove(selectedIndex);
        try {
            getServiceTaxMapList(businessType);
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       serviceTaxMapInfo.remove(selectedIndex);
        
       

    }//GEN-LAST:event_jBtnToLeftOneActionPerformed

    private void jBtnToLeftAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToLeftAllActionPerformed
         model = new DefaultListModel();
         m_jServiceTaxMap.setModel(model);
         model.removeAllElements();
    /*    RetailTicketLineInfo[] lines = receipttwo.getSelectedLines();
        if (lines != null) {
            receiptone.addSelectedLines(lines);
        }*/
    }//GEN-LAST:event_jBtnToLeftAllActionPerformed

    private void jBtnToRightAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToRightAll1ActionPerformed
        try {
            populateServiceChargeMapList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }//GEN-LAST:event_jBtnToRightAll1ActionPerformed

    private void jBtnToRightOne1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToRightOne1ActionPerformed
        try {
            populateServiceChargeSingleMapList();
            // TODO add your handling code here:
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnToRightOne1ActionPerformed

    private void jBtnToLeftOne1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToLeftOne1ActionPerformed
 int index = m_jBusinessTypeList.getSelectedIndex();
     String id = taxMappinglist.get(index).getId();
        java.util.List<ServiceChargeMappedInfo> serviceMapInfo = new ArrayList<ServiceChargeMappedInfo>();
         String businessType = businessTypeList.get(m_cboBusinessType.getSelectedIndex()).getId();
        try {
            serviceMapInfo = dlCustomers.getServiceChargeMapList(businessType);

        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }


       DefaultListModel model = (DefaultListModel) m_jServiceChargeMap.getModel();
        int selectedIndex = m_jServiceChargeMap.getSelectedIndex();

        if (selectedIndex != -1) {
            model.remove(selectedIndex);
        }
        try {
            dlCustomers.deleteTaxLines(id, serviceMapInfo.get(selectedIndex).getId());

        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
       serviceMapInfo.remove(selectedIndex);

        try {
            getServiceChargeMapList(businessType);
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

       
       
        serviceChargeMapInfo.remove(selectedIndex);
    }//GEN-LAST:event_jBtnToLeftOne1ActionPerformed

    private void jBtnToLeftAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnToLeftAll1ActionPerformed
        model = new DefaultListModel();
         m_jServiceChargeMap.setModel(model);
         model.removeAllElements(); // TODO add your handling code here:
    }//GEN-LAST:event_jBtnToLeftAll1ActionPerformed
    public void populateBusinessType(){
          m_cboBusinessType.removeAllItems();
        try {
            businessTypeList = dlCustomers.getBusinessTypeList();
        } catch (BasicException ex) {
            Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            for (BusinessTypeInfo dis : businessTypeList) {
                m_cboBusinessType.addItem(dis.getName());

            }
            m_cboBusinessType.setSelectedIndex(-1);
    }

    public void populateList() throws BasicException {

        model = new DefaultListModel();
        m_jBusinessTypeList.setModel(model);
        taxMappinglist = dlCustomers.getTaxMapping();
        String[] dListId = null;

        for (int i = 0; i < taxMappinglist.size(); i++) {
             String listid = taxMappinglist.get(i).getBusinessType();
            model.add(i, listid);
        }
    }
 public void populateServiceTaxList() throws BasicException {

       model = new DefaultListModel();
        m_jServiceTaxList.setModel(model);
        serviceTaxList = dlCustomers.getServiceTaxList();
        String[] dListId = null;
    
        for (int i = 0; i < serviceTaxList.size(); i++) {
             String listid = serviceTaxList.get(i).getName();
            model.add(i, listid);
        }
    }
  public void getServiceTaxMapList(String businessTypeId) throws BasicException {
        java.util.List<ServiceTaxMappedInfo> serviceMapInfo = new ArrayList<ServiceTaxMappedInfo>();
       model = new DefaultListModel();
        m_jServiceTaxMap.setModel(model);

        serviceMapInfo = dlCustomers.getServiceTaxMapList(businessTypeId);
  
        for (int i = 0; i < serviceMapInfo.size(); i++) {
             String listid = serviceMapInfo.get(i).getName();
            model.add(i, listid);
        }
    }
   public void getServiceChargeMapList(String businessTypeId) throws BasicException {

       model = new DefaultListModel();
        m_jServiceChargeMap.setModel(model);
        java.util.List<ServiceChargeMappedInfo> serviceMapInfo = new ArrayList<ServiceChargeMappedInfo>();

        serviceMapInfo = dlCustomers.getServiceChargeMapList(businessTypeId);
        String[] dListId = null;

        for (int i = 0; i < serviceMapInfo.size(); i++) {
             String listid = serviceMapInfo.get(i).getName();
            model.add(i, listid);
        }
    }
public void populateServiceChargeList() throws BasicException {

       model = new DefaultListModel();
        m_jServiceChargeList.setModel(model);
        serviceChargeList = dlCustomers.getServiceChargeList();
    //    String[] dListId = null;

        for (int i = 0; i < serviceChargeList.size(); i++) {
             String listid = serviceChargeList.get(i).getName();
            model.add(i, listid);
        }
    }
public void populateServiceChargeMapList() throws BasicException {

       model = new DefaultListModel();
        m_jServiceChargeMap.setModel(model);
        java.util.List<ServiceChargeInfo> serviceChargeMapList = new ArrayList<ServiceChargeInfo>();

        serviceChargeMapList =  serviceChargeList;
        for (int i = 0; i <serviceChargeMapList.size(); i++) {
             String taxId = serviceChargeMapList.get(i).getId();
             String listid = serviceChargeMapList.get(i).getName();
             serviceChargeMapInfo.add(new ServiceChargeMapInfo(taxId,listid));
             model.add(i, listid);
        }
    }
public void populateServiceTaxMapList() throws BasicException {

       model = new DefaultListModel();
    //    model1 = new DefaultListModel();
        m_jServiceTaxMap.setModel(model);
    //     m_jServiceTaxList.setModel(model1);
        java.util.List<ServiceTaxInfo> serviceTaxMapList = new ArrayList<ServiceTaxInfo>();

        serviceTaxMapList =  serviceTaxList;
        for (int i = 0; i <serviceTaxMapList.size(); i++) {
              String taxId = serviceTaxMapList.get(i).getId();
              String listid = serviceTaxMapList.get(i).getName();
              serviceTaxMapInfo.add(new ServiceTaxMapInfo(taxId,listid));
              model.add(i, listid);
        }
      
       //for (int i = 0; i <serviceTaxList.size(); i++) {
       //      String listid = serviceTaxList.get(i).getName();
           // model1.removeAllElements();
      //  }
    }

public void populateServiceTaxSingleMapList() throws BasicException {
        model = new DefaultListModel();

        m_jServiceTaxMap.setModel(model);
        serviceTaxMapList =  serviceTaxList;
        int index = m_jServiceTaxList.getSelectedIndex();
         String listid = serviceTaxMapList.get(index).getName();
         String taxId = serviceTaxMapList.get(index).getId();

         serviceTaxMapInfo.add(new ServiceTaxMapInfo(taxId,listid));
       //  serviceChargeMapInfo = java.util.List<ServiceChargeMapInfo> new ServiceChargeMapInfo(listid,taxId);
         System.out.println("serviceChargeMapInfo----"+serviceChargeMapInfo.size()+"==="+listid);
          model.removeAllElements();
            for(int i=0;i<serviceTaxMapInfo.size();i++){
                  model.add(i, serviceTaxMapInfo.get(i).getName());
            }

     //   }

       //for (int i = 0; i <serviceTaxList.size(); i++) {
       //      String listid = serviceTaxList.get(i).getName();
           // model1.removeAllElements();
      //  }
    }
public void populateServiceChargeSingleMapList() throws BasicException {

       model = new DefaultListModel();

        m_jServiceChargeMap.setModel(model);
        serviceChargeMapList =  serviceChargeList;
        int index = m_jServiceChargeList.getSelectedIndex();
         String listid = serviceChargeMapList.get(index).getName();
         String taxId = serviceChargeMapList.get(index).getId();

         serviceChargeMapInfo.add(new ServiceChargeMapInfo(taxId,listid));
       //  serviceChargeMapInfo = java.util.List<ServiceChargeMapInfo> new ServiceChargeMapInfo(listid,taxId);
         System.out.println("serviceChargeMapInfo----"+serviceChargeMapInfo.size()+"==="+listid);
          model.removeAllElements();
            for(int i=0;i<serviceChargeMapInfo.size();i++){
                  model.add(i, serviceChargeMapInfo.get(i).getName());
            }

     //   }

       //for (int i = 0; i <serviceTaxList.size(); i++) {
       //      String listid = serviceTaxList.get(i).getName();
           // model1.removeAllElements();
      //  }
    }

 public static boolean isInsert() {
        return insert;
    }

    /**
     * @param insert the insert to set
     */
    public static void setInsert(boolean insert) {
        JTaxMappingPanel.insert = insert;
    }

private void saveButtonAction() throws BasicException {
 int index = m_jBusinessTypeList.getSelectedIndex();
        String businessType = businessTypeList.get(m_cboBusinessType.getSelectedIndex()).getId();
        String serviceTax;
        String serviceTaxIncluded;
        String serviceCharge;
        String serviceChargeIncluded;
      
        if(m_jChkServiceTaxIncluded.isSelected()==true){
            serviceTaxIncluded = "Y";
        }else{
            serviceTaxIncluded = "N";
        }
      
        if(m_jChkServiceChargeIncluded.isSelected()==true){
            serviceChargeIncluded = "Y";
        }else{
            serviceChargeIncluded = "N";
        }
        if(isInsert()){
            String id = UUID.randomUUID().toString();
            id = id.replaceAll("-", "");
            dlCustomers.insertTaxMapping(id, businessType, serviceTaxIncluded,  serviceChargeIncluded);
      //  if(m_jChkServiceTax.isSelected()==true){
          for (int i=0;i<serviceTaxMapInfo.size();i++) {
            dlCustomers.insertTaxMappingLines(id,serviceTaxMapInfo.get(i).getId(),"Y");
       //  }
        }
       // if(m_jChkServiceCharge.isSelected()==true){
            for (int i=0;i<serviceChargeMapInfo.size();i++) {
             dlCustomers.insertTaxMappingLines(id,serviceChargeMapInfo.get(i).getId(),"N");
        // }
        }
        }else{
            String id = taxMappinglist.get(index).getId();
            dlCustomers.updateTaxMapping(id, businessType, serviceTaxIncluded, serviceChargeIncluded);
         //   dlCustomers.deleteTaxMappingLines(id);
            System.out.println("serviceTaxMapInfo.size()--"+serviceTaxMapInfo.size());
        //    if(serviceTaxMapInfo.size()==0){
        //        getServiceTaxMapList(businessType);
        //    }

            for (int i=0;i<serviceTaxMapInfo.size();i++) {
                int taxCount = dlCustomers.getTaxCount(serviceTaxMapInfo.get(i).getId(),id);
                if(taxCount==0){
                  dlCustomers.insertTaxMappingLines(id,serviceTaxMapInfo.get(i).getId(),"Y");
                }
       //  }
        }
           //  if(serviceChargeMapInfo.size()==0){
            //    getServiceChargeMapList(businessType);
          //  }
       // if(m_jChkServiceCharge.serviceChargeMapInfoisSelected()==true){
            for (int i=0;i<serviceChargeMapInfo.size();i++) {
                int chargeCount = dlCustomers.getChargeCount(serviceChargeMapInfo.get(i).getId(),id);
                if(chargeCount==0){
                     dlCustomers.insertTaxMappingLines(id,serviceChargeMapInfo.get(i).getId(),"N");
                }
        // }
        }

        }
          populateList();
          serviceChargeMapInfo.clear();
          serviceTaxMapInfo.clear();
          //serviceChargeMapInfo = new ServiceChargeMapInfo("","");
  /*      String name = m_jBusinessType.getText();
     
        if (name != null) {

            boolean avl = checkBusinessTypeAvl(name);
            try {
                list = dlCustomers.getBusinessTypeList();
           
                if (updateMode == false) {
                    if (avl == false) {
                        dlCustomers.insertBusinessType(UUID.randomUUID().toString(), name);
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered business type is already exists");
                    }
                } else {
                    String id = list.get(m_jBusinessTypeList.getSelectedIndex()).getId();
                    dlCustomers.updateBusinessType(id, name);
                    updateMode = false;
                }
                populateList();
                m_jBusinessType.setText("");
             } catch (NumberFormatException npe) {
                JOptionPane.showMessageDialog(this, "Enter valid business type");
                m_jBusinessType.setText("");
             }
        } else {
            JOptionPane.showMessageDialog(this, "Your msg.");
        }*/
    }
    private void listAction() {
        if (m_jBusinessTypeList.getSelectedIndex() > -1) {
                 int index = m_jBusinessTypeList.getSelectedIndex();
                m_cboBusinessType.setSelectedItem(taxMappinglist.get(index).getBusinessType());
            //    if(taxMappinglist.get(index).getIsServiceTax().equals("Y")){
           //        m_jChkServiceTax.setSelected(true);
                    try {
                        populateServiceTaxList();
                        getServiceTaxMapList(taxMappinglist.get(index).getBusinessTypeId());
                    } catch (BasicException ex) {
                        Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
             //   }else{
               //     m_jChkServiceTax.setSelected(false);
             //   }
                if(taxMappinglist.get(index).getIsServiceTaxIncluded().equals("Y")){
                    m_jChkServiceTaxIncluded.setSelected(true);
                }else{
                    m_jChkServiceTaxIncluded.setSelected(false);
                }
              //  if(taxMappinglist.get(index).getIsServiceCharge().equals("Y")){
               //    m_jChkServiceCharge.setSelected(true);
                     try {
                        populateServiceChargeList();
                        getServiceChargeMapList(taxMappinglist.get(index).getBusinessTypeId());
                    } catch (BasicException ex) {
                        Logger.getLogger(JTaxMappingPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

              //  }else{
              //      m_jChkServiceCharge.setSelected(false);
             //   }
                if(taxMappinglist.get(index).getIsServiceChargeIncluded().equals("Y")){
                    m_jChkServiceChargeIncluded.setSelected(true);
                }else{
                    m_jChkServiceChargeIncluded.setSelected(false);
                }
        }
    }

    private boolean checkBusinessTypeAvl(String businessType) throws BasicException {
        String name = dlCustomers.getBusinessTypeName(businessType);
        if ("NONAME".equalsIgnoreCase(name)) {
            return false;
        } else {
            return true;
        }
    } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnToLeftAll;
    private javax.swing.JButton jBtnToLeftAll1;
    private javax.swing.JButton jBtnToLeftOne;
    private javax.swing.JButton jBtnToLeftOne1;
    private javax.swing.JButton jBtnToRightAll;
    private javax.swing.JButton jBtnToRightAll1;
    private javax.swing.JButton jBtnToRightOne;
    private javax.swing.JButton jBtnToRightOne1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jSaver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JComboBox m_cboBusinessType;
    private javax.swing.JList m_jBusinessTypeList;
    private javax.swing.JCheckBox m_jChkServiceChargeIncluded;
    private javax.swing.JCheckBox m_jChkServiceTaxIncluded;
    private javax.swing.JLabel m_jLblBusinessType;
    private javax.swing.JList m_jServiceChargeList;
    private javax.swing.JList m_jServiceChargeMap;
    private javax.swing.JPanel m_jServiceChargePanel;
    private javax.swing.JList m_jServiceTaxList;
    private javax.swing.JList m_jServiceTaxMap;
    private javax.swing.JPanel m_jServiceTaxPanel;
    // End of variables declaration//GEN-END:variables
    
}
