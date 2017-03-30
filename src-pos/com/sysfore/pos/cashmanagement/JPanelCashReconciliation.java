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
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import javax.swing.table.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JRootApp;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List; 

/**
 *
 * @author adrianromero
 */
public class JPanelCashReconciliation extends JPanel implements JPanelView, BeanFactoryApp {
    
    private AppView m_App;
    private AppProperties m_props;
    private DataLogicSystem m_dlSystem;
    
    private ReconciliationModel m_PaymentsToClose = null;
    
    private JRootApp m_RootApp;
    private TicketParser m_TTP;
     private CurrencyConfigLoader currLoad;
    private int rows;
    private TableModel model;
    private CurrencyTableListener currencyListener;
    private PurchaseOrderReceipts m_dlPOReceipts;
    List<DenominationTable> dTable = new ArrayList<DenominationTable>();
    double sum = 0;
    String PosNo;
    String floatAmt;

    /** Creates new form JPanelCloseMoney */
    public JPanelCashReconciliation() throws BeanFactoryException{
    
        initComponents();
        Action doCloseDay = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {

        jScrollPane1.setEnabled(true);
        jScrollPane1.setFocusable(true);
        jTable1.setFocusable(true);
        jTable1.setEnabled(true);
       // jTxtComments.setFocusable(false);
        jTable1.requestFocus();
        
            }
        };

        InputMap imapCloseDay = jTable1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCloseDay.put(KeyStroke.getKeyStroke("F1"), "doCloseDay");
        jTable1.getActionMap().put("doCloseDay",doCloseDay);

        Action doComments = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
        jScrollPane1.setEnabled(false);
        jScrollPane1.setFocusable(false);
        jTable1.setFocusable(false);
        jTable1.setEnabled(false);
         jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
         jTable1.setColumnSelectionAllowed(false);
         jTable1.setRowSelectionAllowed(false);
         jTable1.setEnabled(false);
         jTable1.setEditingColumn(-1);
          jTable1.setEditingRow(-1);
         jTable1.clearSelection();
          jTxtComments.requestFocus();
           
            }
        };
        InputMap imapComments = jTxtComments.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapComments.put(KeyStroke.getKeyStroke("F3"), "doComments");
        jTxtComments.getActionMap().put("doComments",doComments);
        
//        Action doComments = new AbstractAction() {
//        public void actionPerformed(ActionEvent e) {
//        jTxtComments.setFocusable(true);
//        jScrollPane1.setEnabled(false);
//        jScrollPane1.setFocusable(false);
//        jTable1.setFocusable(false);
//        jTable1.setEnabled(false);
//         jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//    jTable1.setColumnSelectionAllowed(true);
//    jTable1.setRowSelectionAllowed(false);
//
//    jTable1.clearSelection();
//      //  jTable1.removeAll();
//
//
//            }
//        };
//
//        InputMap imapComments = jTxtComments.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapComments.put(KeyStroke.getKeyStroke("F3"), "doComments");
//        jTxtComments.getActionMap().put("doComments",doComments);
    }
    
    public void init(AppView app) throws BeanFactoryException {
        
        m_App = app;
        AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
        aconfig.load();
        PosNo = aconfig.getProperty("machine.PosNo");
        List<CurrencyInfo> info = initLocaleSpecificData();
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);

        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {new FormatsPayment(), Formats.CURRENCY}));
        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jTicketTable.setRowHeight(24);

        this.m_dlPOReceipts = (PurchaseOrderReceipts) app.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        initComponents();

        if (info.size() > 0) {
            String[] currencyList = info.get(0).getDenominations().split(",");
            setRows(currencyList.length);
            Object[] obj = {"Denomination", "Count", "Total"};
            jTable1.setModel(new DefaultTableModel(obj, getRows()));

            jTable1.setRowHeight(20);
            populateJtable(currencyList);
            currencyListener = new CurrencyTableListener(jTable1, currencyList);
            jTable1.getModel().addTableModelListener(currencyListener);
            
          //  jTable1.setColumnSelectionAllowed(true);
        //  jTable1.setRowSelectionAllowed(false);
//            jTable1.addColumnSelectionInterval(1, 1);
//            jTable1.requestFocus();
         //    jTable1.changeSelection(1, 1, true, false);
//                jTable1.editCellAt(1, 1);
//                jTable1.transferFocus();
          
          //  jTable1.addFocusListener(null);
        }else{
            //jLblDenominations.setText("Currency not available for country " + Locale.getDefault().getCountry());
        }
     
        jLblComments.setVisible(false);
        jTxtComments.setVisible(false);
      /*  m_jsalestable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.STRING, Formats.CURRENCY, Formats.CURRENCY, Formats.CURRENCY}));
        m_jsalestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollSales.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jsalestable.getTableHeader().setReorderingAllowed(false);         
        m_jsalestable.setRowHeight(25);
        m_jsalestable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); */
    }

      private List<CurrencyInfo> initLocaleSpecificData() {
       // jLblDenominations.setText("Currency " + Locale.getDefault().getCountry());
        currLoad = new CurrencyConfigLoader(Locale.getDefault().getCountry(), null, m_App);
        java.util.List<CurrencyInfo> list = null;
        try {
            list = currLoad.loadCurrencyArray();
        } catch (BasicException ex) {
            Logger.getLogger(CurrencyLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;

    }
    private void populateJtable(String[] currencyList) {
        model = jTable1.getModel();
        for (int r = 0; r < getRows(); r++) {
            model.setValueAt(currencyList[r], r, 0);                
        }
       
    }
    private void populateJtableValue(String[] currencyList,String[] currencyCount,String[] currencyValue) {
        model = jTable1.getModel();
        for (int r = 0; r < getRows(); r++) {
            model.setValueAt(currencyList[r], r, 0);
            model.setValueAt(currencyCount[r], r, 1);
            model.setValueAt(currencyValue[r], r, 2);
        }

    }
     private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    public Object getBean() {
        return this;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseTPV");
    }    
    
    public void activate() throws BasicException {
        loadData();
        m_jLblDiscount.setVisible(false);
m_jDiscount.setVisible(false);
    }   
    
    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }  
    
    private void loadData() throws BasicException {
        

        m_jPrintCash.setEnabled(false);
        m_jCloseCash.setEnabled(false);
        m_jDiscount.setText(null);
        m_jCash.setText(null);
        m_jCredit.setText(null);
         m_jSalesTotal.setText(null);
         m_jTicketTable.setModel(new DefaultTableModel());
         List<CurrencyInfo> info = initLocaleSpecificData();
         if (info.size() > 0) {
            String[] currencyList = info.get(0).getDenominations().split(",");
            setRows(currencyList.length);
            Object[] obj = {"Denomination", "Count", "Total"};
            jTable1.setModel(new DefaultTableModel(obj, getRows()));
            jTable1.setRowHeight(20);
            populateJtable(currencyList);
            currencyListener = new CurrencyTableListener(jTable1, currencyList);
            jTable1.getModel().addTableModelListener(currencyListener);
        }

        m_PaymentsToClose = ReconciliationModel.loadInstance(m_App);
        jTxtTotal.setText(null);
        jTxtDiff.setText(null);
        jTxtComments.setText(null);

        
        if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {

            m_jPrintCash.setEnabled(true);
            m_jCloseCash.setEnabled(true);

            m_jCash.setText(m_PaymentsToClose.printAmt());

            m_jSalesTotal.setText(m_PaymentsToClose.printSalesTotal());//.printSalesBase());//.printSalesTotal());

         //   m_jDiscount.setText(m_PaymentsToClose.printDiscountAmt());
            m_jCredit.setText(m_PaymentsToClose.printCreditAmt());

            m_jFloatAmt.setText(m_PaymentsToClose.printFloatAmt());
        }          
        
        m_jTicketTable.setModel(m_PaymentsToClose.getPaymentsModel());
                
        TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(150);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(150);
        jColumns.getColumn(1).setResizable(false);
      
        
       
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
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jCloseCash = new javax.swing.JButton();
        m_jPrintCash = new javax.swing.JButton();
        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLblSalesAmt = new javax.swing.JLabel();
        m_jSalesTotal = new javax.swing.JTextField();
        m_jDiscount = new javax.swing.JTextField();
        jLblbreakup = new javax.swing.JLabel();
        jLblcashCollection = new javax.swing.JLabel();
        m_jFloatAmt = new javax.swing.JTextField();
        jLblFloatAmt = new javax.swing.JLabel();
        jLblAmtCollection = new javax.swing.JLabel();
        jBtnReconcile = new javax.swing.JButton();
        jTxtTotal = new javax.swing.JTextField();
        jBtnTotalCash = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTxtDiff = new javax.swing.JTextField();
        jLblComments = new javax.swing.JLabel();
        m_jLblDiscount = new javax.swing.JLabel();
        m_jCash = new javax.swing.JTextField();
        m_jCancel = new javax.swing.JButton();
        jTxtComments = new javax.swing.JTextField();
        m_jLblCredit = new javax.swing.JLabel();
        m_jCredit = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(810, 768));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jCloseCash.setText(AppLocal.getIntString("Button.CloseCash")); // NOI18N
        m_jCloseCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseCashActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCloseCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(594, 1485, -1, -1));

        m_jPrintCash.setText(AppLocal.getIntString("Button.PrintCash")); // NOI18N
        m_jPrintCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPrintCashActionPerformed(evt);
            }
        });
        jPanel1.add(m_jPrintCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(533, 1485, -1, -1));

        m_jScrollTableTicket.setEnabled(false);
        m_jScrollTableTicket.setFocusable(false);
        m_jScrollTableTicket.setMinimumSize(new java.awt.Dimension(350, 140));
        m_jScrollTableTicket.setPreferredSize(new java.awt.Dimension(350, 140));
        m_jScrollTableTicket.setRequestFocusEnabled(false);

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        jPanel1.add(m_jScrollTableTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 176, 360, 81));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new DefaultTableModel());
        jTable1.setEditingColumn(0);
        jTable1.setEditingRow(0);
        jTable1.setRowSelectionAllowed(false);
        jTable1.setSurrendersFocusOnKeystroke(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 294, 360, 200));

        jLblSalesAmt.setText("Total Sales"); // NOI18N
        jLblSalesAmt.setFocusable(false);
        jLblSalesAmt.setRequestFocusEnabled(false);
        jPanel1.add(jLblSalesAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 14, 164, -1));

        m_jSalesTotal.setEditable(false);
        m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jSalesTotal.setFocusable(false);
        m_jSalesTotal.setRequestFocusEnabled(false);
        jPanel1.add(m_jSalesTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 150, 25));

        m_jDiscount.setEditable(false);
        m_jDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jDiscount.setFocusable(false);
        m_jDiscount.setRequestFocusEnabled(false);
        jPanel1.add(m_jDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, 150, 25));

        jLblbreakup.setText("Tender Breakup");
        jLblbreakup.setFocusable(false);
        jPanel1.add(jLblbreakup, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 148, 138, -1));

        jLblcashCollection.setText("Cash Collection");
        jLblcashCollection.setFocusable(false);
        jPanel1.add(jLblcashCollection, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 264, 125, 22));

        m_jFloatAmt.setEditable(false);
        m_jFloatAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jFloatAmt.setText("  ");
        m_jFloatAmt.setFocusable(false);
        m_jFloatAmt.setRequestFocusEnabled(false);
        jPanel1.add(m_jFloatAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 46, 150, 25));

        jLblFloatAmt.setText("Float Amount");
        jLblFloatAmt.setFocusable(false);
        jPanel1.add(jLblFloatAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 48, -1, 18));

        jLblAmtCollection.setText("Total Collection");
        jLblAmtCollection.setFocusable(false);
        jPanel1.add(jLblAmtCollection, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 115, 164, -1));

        jBtnReconcile.setBackground(new java.awt.Color(255, 255, 255));
        jBtnReconcile.setMnemonic('r');
        jBtnReconcile.setText("Cash Reconcile");
        jBtnReconcile.setFocusable(false);
        jBtnReconcile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnReconcileActionPerformed(evt);
            }
        });
        jPanel1.add(jBtnReconcile, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 626, 170, 25));

        jTxtTotal.setEditable(false);
        jTxtTotal.setFocusable(false);
        jTxtTotal.setRequestFocusEnabled(false);
        jPanel1.add(jTxtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 514, 150, 25));

        jBtnTotalCash.setBackground(new java.awt.Color(255, 255, 255));
        jBtnTotalCash.setMnemonic('t');
        jBtnTotalCash.setText("Calculate Total Cash");
        jBtnTotalCash.setFocusable(false);
        jBtnTotalCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTotalCashActionPerformed(evt);
            }
        });
        jPanel1.add(jBtnTotalCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(423, 514, 150, 25));

        jLabel1.setText("Total Cash");
        jLabel1.setFocusable(false);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 514, 160, 20));

        jLabel2.setText("Difference in Cash Amount");
        jLabel2.setFocusable(false);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 551, 160, 20));

        jTxtDiff.setEditable(false);
        jTxtDiff.setEnabled(false);
        jTxtDiff.setFocusable(false);
        jPanel1.add(jTxtDiff, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 549, 150, 25));

        jLblComments.setText("Comments");
        jLblComments.setFocusable(false);
        jPanel1.add(jLblComments, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 589, 100, 20));

        m_jLblDiscount.setText("Total Discount");
        m_jLblDiscount.setFocusable(false);
        jPanel1.add(m_jLblDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 130, 110, 20));

        m_jCash.setEditable(false);
        m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCash.setFocusable(false);
        m_jCash.setRequestFocusEnabled(false);
        jPanel1.add(m_jCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 115, 150, 25));

        m_jCancel.setBackground(new java.awt.Color(255, 255, 255));
        m_jCancel.setMnemonic('c');
        m_jCancel.setText("Cancel");
        m_jCancel.setFocusable(false);
        m_jCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCancelActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 626, 170, 25));
        jPanel1.add(jTxtComments, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 583, 150, 25));

        m_jLblCredit.setText("Credit Collecton");
        m_jLblCredit.setFocusable(false);
        jPanel1.add(m_jLblCredit, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 110, 20));

        m_jCredit.setEditable(false);
        m_jCredit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCredit.setFocusable(false);
        m_jCredit.setRequestFocusEnabled(false);
        jPanel1.add(m_jCredit, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 150, 25));

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jCloseCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseCashActionPerformed
        // TODO add your handling code here:
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaclosecash"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            
            Date dNow = new Date();
            
            try {               
                // Cerramos la caja si esta pendiente de cerrar.
                if (m_App.getActiveCashDateEnd() == null) {
                    new StaticSentence(m_App.getSession()
                        , "UPDATE CLOSEDCASH SET DATEEND = ? WHERE HOST = ? AND MONEY = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING, Datas.STRING}))
                        .exec(new Object[] {dNow, m_App.getProperties().getHost(), m_App.getActiveCashIndex()}); 
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }
            
            try {
                // Creamos una nueva caja          
                m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getActiveCashSequence() + 1, dNow, null);
                
                // creamos la caja activa      
                m_dlSystem.execInsertCash(
                        new Object[] {m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashSequence(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});                  
               
                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);
                
                // print report
                printPayments("Printer.CloseCash");
                
                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }
            
            try {
                loadData();
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("label.noticketstoclose"), e);
                msg.show(this);
            }
        }         
    }//GEN-LAST:event_m_jCloseCashActionPerformed

private void m_jPrintCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPrintCashActionPerformed

    // print report
    printPayments("Printer.PartialCash");
    
}//GEN-LAST:event_m_jPrintCashActionPerformed

private void jBtnReconcileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnReconcileActionPerformed


    double tendercashAmt = m_PaymentsToClose.getCashAmt();
    double totalCashAmt =Double.parseDouble(jTxtTotal.getText());
    //String diffInAmt =Double.toString(tendercashAmt-totalCashAmt);
    double diffInAmt = tendercashAmt-totalCashAmt;
    String comment = null;

    if(tendercashAmt!= totalCashAmt){
  
        jTxtDiff.setText(Double.toString(diffInAmt));
        jLblComments.setVisible(true);
        jTxtComments.setVisible(true);
        jTxtComments.setFocusable(true);
        comment =jTxtComments.getText();
        if(comment.equals("")){
            JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.EnterComments"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
        }else{
                try {
                    jBtnReconcileAction();
                    String value = "difference";
                    // String posNo= m_props.getPosNo();
                   //  System.out.println("enrtrr---"+posNo);
                    m_dlPOReceipts.insertAmt(diffInAmt, comment, value, PosNo);
                } catch (BasicException ex) {
                    Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
                }
             
        
        }
        

    }else{
     jBtnReconcileAction();
    }
        
}//GEN-LAST:event_jBtnReconcileActionPerformed

private void jBtnTotalCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTotalCashActionPerformed
    jTxtTotal.setText("");
    double subTotal = 0;
    String strCount = null;
   // jTable1.setColumnSelectionInterval(0, 0);
    double value = 0;
    for (int r = 0; r < getRows(); r++) {

        value = Double.parseDouble(jTable1.getValueAt(r, 0).toString());
        try {
            strCount = jTable1.getValueAt(r, 1).toString();
        } catch (NullPointerException e) {
           
            strCount = "0";
           
        }
        if(strCount.isEmpty() || strCount.equals(null)){
            strCount = "0";
        }

        int count = Integer.parseInt(strCount);
        subTotal = value * count;
        jTable1.setValueAt(subTotal, r, 2);
        sum += subTotal;
        dTable.add(new DenominationTable(value, count, subTotal));

    }

//      try {
//            m_dlPOReceipts.insertReconciliation(dTable, sum, PosNo);
//        } catch (BasicException ex) {
//            Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
//        }
       jTable1.removeAll();
//       List<CurrencyValueInfo> info1 = null;
//        try {
//            info1 = m_dlPOReceipts.getCurrencyValue();
//        } catch (BasicException ex) {
//            Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if (info1.size() > 0) {
//            String[] currencyList = info1.get(0).getDenominations().split(",");
//            String[] currencyCount = Integer.toString(info1.get(0).getcurrencyCount()).split(",");
//            String[] currentValue = Double.toString(info1.get(0).getcurrencyCount()).split(",");
//            setRows(currencyList.length);
//            Object[] obj = {"Denomination", "Count", "Total"};
//            jTable1.setModel(new DefaultTableModel(obj, getRows()));
//            jTable1.setRowHeight(20);
//            populateJtableValue(currencyList,currencyCount,currentValue);
//            try {
//                m_dlPOReceipts.deleteDenomination();
//                //   jTable1.getModel().addTableModelListener(currencyListener);
//                //   jTable1.getModel().addTableModelListener(currencyListener);
//            } catch (BasicException ex) {
//                Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    jTxtTotal.setText(sum + "");
     sum = 0;


    // TODO ajTable1dd your handling code here:
}//GEN-LAST:event_jBtnTotalCashActionPerformed

private void m_jCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCancelActionPerformed
        m_App.getAppUserView().showTask("com.openbravo.pos.sales.JRetailPanelTicketSales");    // TODO add your handling code here:
}//GEN-LAST:event_m_jCancelActionPerformed
    
    private void jBtnReconcileAction() {

        try {
            m_dlPOReceipts.insertReconciliation(dTable, sum, PosNo);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
            m_PaymentsToClose = ReconciliationModel.loadInstance(m_App);
        } catch (BasicException ex) {
            Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
        }

       int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannacloseday"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {

            Date dNow = new Date();

            try {
                // Cerramos la caja si esta pendiente de cerrar.
                if (m_App.getActiveDayDateEnd() == null) {
                    new StaticSentence(m_App.getSession()
                        , "UPDATE CLOSEDDAY SET DATEEND = ? WHERE HOST = ? AND MONEY = ? AND POSNO = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING}))
                        .exec(new Object[] {dNow, m_App.getProperties().getHost(), m_App.getActiveDayIndex(),m_App.getProperties().getPosNo()});
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotcloseday"), e);
                msg.show(this);
            }


                // Creamos una nueva caja
                m_App.setActiveDay(UUID.randomUUID().toString(), m_App.getActiveDaySequence() + 1, dNow, null);
            try {

                // creamos la caja activa
                m_dlSystem.execInsertDay(new Object[]{m_App.getActiveDayIndex(), m_App.getProperties().getHost(), m_App.getActiveDaySequence(),m_App.getProperties().getPosNo(), m_App.getActiveDayDateStart(), m_App.getActiveDayDateEnd()});
            } catch (BasicException ex) {
                Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
            }

                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);
          
                // print report
           //     printDayPayments("Printer.CloseDay");

                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closedayok"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
            try {
                loadData();
                //try {
                //loadData();
                // } catch (BasicException ex) {
                //      Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
                //  }
            } catch (BasicException ex) {
                Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//         m_RootApp = null;
//        m_RootApp = (JRootApp) m_App;
//        m_RootApp.closeAppView();
       m_App.getAppUserView().showTask("com.openbravo.pos.sales.JRetailPanelTicketSales");  
    }
   private void printDayPayments(String report) {

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
                script.put("payments", m_PaymentsToClose);
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
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnReconcile;
    private javax.swing.JButton jBtnTotalCash;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLblAmtCollection;
    private javax.swing.JLabel jLblComments;
    private javax.swing.JLabel jLblFloatAmt;
    private javax.swing.JLabel jLblSalesAmt;
    private javax.swing.JLabel jLblbreakup;
    private javax.swing.JLabel jLblcashCollection;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTxtComments;
    private javax.swing.JTextField jTxtDiff;
    private javax.swing.JTextField jTxtTotal;
    private javax.swing.JButton m_jCancel;
    private javax.swing.JTextField m_jCash;
    private javax.swing.JButton m_jCloseCash;
    private javax.swing.JTextField m_jCredit;
    private javax.swing.JTextField m_jDiscount;
    private javax.swing.JTextField m_jFloatAmt;
    private javax.swing.JLabel m_jLblCredit;
    private javax.swing.JLabel m_jLblDiscount;
    private javax.swing.JButton m_jPrintCash;
    private javax.swing.JTextField m_jSalesTotal;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JTable m_jTicketTable;
    // End of variables declaration//GEN-END:variables
    
}
