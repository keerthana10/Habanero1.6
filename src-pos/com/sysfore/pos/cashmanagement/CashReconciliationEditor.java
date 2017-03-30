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
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
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
public class CashReconciliationEditor extends JDialog {
    private static PurchaseOrderReceipts PurchaseOrder;
    
    private AppView m_App;
    private AppProperties m_props;
    private DataLogicSystem m_dlSystem;
    
    private ReconciliationModel m_PaymentsToClose = null;
    
    private TicketParser m_TTP;
     private CurrencyConfigLoader currLoad;
    private int rows;
    private TableModel model;
    private CurrencyTableListener currencyListener;
    //private PurchaseOrderReceipts m_dlPOReceipts;
    List<DenominationTable> dTable = new ArrayList<DenominationTable>();
    double sum = 0;
    String PosNo;
    String floatAmt;
  static int x = 1000;
    static int y = 1000;
    /** Creates new form JPanelCloseMoney */
   
     public CashReconciliationEditor(AppView m_app, JPanel parent) throws BasicException {

    }

    private CashReconciliationEditor(Frame frame, boolean b) {
        super(frame, b);
        setBounds(x, y, 662, 562);
    }

    private CashReconciliationEditor(Dialog dialog, boolean b) {
        super(dialog, b);
        setBounds(x, y, 662, 562);
    }

    public static void showMessage(Component parent, PurchaseOrderReceipts PurchaseOrder, AppView m_App) throws BasicException {

        CashReconciliationEditor.PurchaseOrder=PurchaseOrder;
       
        // PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
     //   m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        Window window = getWindow(parent);
        CashReconciliationEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new CashReconciliationEditor((Frame) window, true);
        } else {
            myMsg = new CashReconciliationEditor((Dialog) window, true);
        }
            myMsg.init(PurchaseOrder, m_App);

    }

    public void init(PurchaseOrderReceipts PurchaseOrder,AppView app) throws BeanFactoryException {
        
        m_App = app;
       
        AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
          aconfig.load();
       
         PosNo = aconfig.getProperty("machine.PosNo");
        // floatAmt = aconfig.getProperty("machine.FloatAmt");
        List<CurrencyInfo> info = initLocaleSpecificData();
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        System.out.println("enrtr ---m_jTicketTable");
        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {new FormatsPayment(), Formats.CURRENCY}));
        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

       //CashReconciliationEditor.PurchaseOrder = PurchaseOrder;
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
     private int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }
    public Object getBean() {
        return this;
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
    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseTPV");
    }    
    
    public void activate() throws BasicException {
       
    }   
    
    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }  
    
    private void loadData() throws BasicException {
        

     //   m_jCount.setText(null); // AppLocal.getIntString("label.noticketstoclose");
        m_jCash.setText(null);

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
      // jTable1.setModel(new DefaultTableModel());
      //  m_jsalestable.setModel(new DefaultTableModel());
          
        // LoadData
        m_PaymentsToClose = ReconciliationModel.loadInstance(m_App);
        jTxtTotal.setText(null);
        jTxtDiff.setText(null);
        jTxtComments.setText(null);

        
        if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {


            m_jCash.setText(m_PaymentsToClose.printPaymentsTotal());

            m_jSalesTotal.setText(m_PaymentsToClose.printSalesBase());//.printSalesTotal());
            m_jFloatAmt.setText("Rs."+floatAmt);
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
        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLblSalesAmt = new javax.swing.JLabel();
        m_jSalesTotal = new javax.swing.JTextField();
        m_jCash = new javax.swing.JTextField();
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
        jTxtComments = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        m_jScrollTableTicket.setMinimumSize(new java.awt.Dimension(350, 140));
        m_jScrollTableTicket.setPreferredSize(new java.awt.Dimension(350, 140));

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        jTable1.setModel(new DefaultTableModel());
        jScrollPane1.setViewportView(jTable1);

        jLblSalesAmt.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

        m_jSalesTotal.setEditable(false);
        m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        m_jCash.setEditable(false);
        m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLblbreakup.setText("Tender Breakup");

        jLblcashCollection.setText("Cash Collection");

        m_jFloatAmt.setEditable(false);
        m_jFloatAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLblFloatAmt.setText("Float Amount");

        jLblAmtCollection.setText("Total Amount Collection");

        jBtnReconcile.setText("Cash Reconcile");
        jBtnReconcile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnReconcileActionPerformed(evt);
            }
        });

        jTxtTotal.setEditable(false);

        jBtnTotalCash.setText("Calculate Total Cash");
        jBtnTotalCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTotalCashActionPerformed(evt);
            }
        });

        jLabel1.setText("Total Cash");

        jLabel2.setText("Difference in Cash Amount");

        jTxtDiff.setEditable(false);

        jLblComments.setText("Comments");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(m_jSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLblSalesAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLblFloatAmt)
                .addGap(106, 106, 106)
                .addComponent(m_jFloatAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLblAmtCollection, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(m_jCash, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLblbreakup, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLblcashCollection, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jBtnTotalCash, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTxtDiff, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(jLblComments, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jTxtComments, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(jBtnReconcile, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jSalesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLblSalesAmt)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblFloatAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jFloatAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLblAmtCollection))
                    .addComponent(m_jCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jLblbreakup)
                .addGap(14, 14, 14)
                .addComponent(m_jScrollTableTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLblcashCollection, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnTotalCash))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtDiff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblComments, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTxtComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jBtnReconcile)
                .addGap(865, 865, 865))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

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
        comment =jTxtComments.getText();
        if(comment.equals("")){
            JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.EnterComments"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
        }else{
                try {
                    jBtnReconcileAction();
                    String value = "difference";
                    // String posNo= m_props.getPosNo();
                   //  System.out.println("enrtrr---"+posNo);
                    PurchaseOrder.insertAmt(diffInAmt, comment, value, PosNo);
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
        System.out.println("enrtr strCount"+strCount);

        int count = Integer.parseInt(strCount);
        subTotal = value * count;
        jTable1.setValueAt(subTotal, r, 2);
        sum += subTotal;
        dTable.add(new DenominationTable(value, count, subTotal));

    }
    jTxtTotal.setText(sum + "");
     sum = 0;
    // TODO add your handling code here:
}//GEN-LAST:event_jBtnTotalCashActionPerformed
    
    private void jBtnReconcileAction() {

        try {
            PurchaseOrder.insertReconciliation(dTable, sum, PosNo);
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
                        , "UPDATE CLOSEDDAY SET DATEEND = ? WHERE HOST = ? AND MONEY = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING, Datas.STRING}))
                        .exec(new Object[] {dNow, m_App.getProperties().getHost(), m_App.getActiveDayIndex()});
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotcloseday"), e);
                msg.show(this);
            }


                // Creamos una nueva caja
                m_App.setActiveDay(UUID.randomUUID().toString(), m_App.getActiveDaySequence() + 1, dNow, null);
            try {
                // creamos la caja activa
                m_dlSystem.execInsertDay(new Object[]{m_App.getActiveDayIndex(), m_App.getProperties().getHost(), m_App.getActiveDaySequence(), m_App.getActiveDayDateStart(), m_App.getActiveDayDateEnd()});
            } catch (BasicException ex) {
                Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
            }

                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);
          
                // print report
                printDayPayments("Printer.CloseDay");

                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closedayok"), AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
            //try {
               //loadData();
           // } catch (BasicException ex) {
          //      Logger.getLogger(JPanelCashReconciliation.class.getName()).log(Level.SEVERE, null, ex);
          //  }
        }
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
    private javax.swing.JTextField m_jCash;
    private javax.swing.JTextField m_jFloatAmt;
    private javax.swing.JTextField m_jSalesTotal;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JTable m_jTicketTable;
    // End of variables declaration//GEN-END:variables
    
}
