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

package com.sysfore.pos.creditsale;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.JPanelTicketEdits;
import com.openbravo.pos.sales.JTicketsBag;
import com.openbravo.pos.sales.JTicketsBagTicket;
import com.openbravo.pos.sales.JTicketsBagTicketBag;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketMergeTaxInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import com.sysfore.pos.stockreconciliation.DataLogicStockReceipts;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 *
 * @author adrianromero
 */
public class JCreditSales extends JPanel implements JPanelView,BeanFactoryApp{
    private static boolean changesmade;
 private AppView m_App;
 private DataLogicCustomers dlCustomers;
 private java.util.List<CustomerPojo> Clines=null;
 private java.util.List<TicketsPojo> Tlines=null;
 private java.util.List<CreditSalePojo> Creditlines=null;
 private ComboBoxValModel m_CModel;
 private int  CreditlinesSize=0;
 private String[] columnCInNames=new String[]{
             "Bill No.", "Bill Date & Time", "Bill Amount", "Out Standing Amount",  "Payment"
        };
 protected DataLogicSales dlSales;
 private double Total=0.00000;;
 private  String datev;
 double actualpay=0;
 String tendor=null;

  //  private static InventoryTableModel m_inventorylines;
    public JCreditSales() {
        
        initComponents();
  

    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        

       this.m_App = app;
     dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
         this.dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        initComponents();
     DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 100, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Bill No.");
        columns.addColumn(c);
        c = new TableColumn(1, 120, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Bill Date & Time");
        columns.addColumn(c);
        c = new TableColumn(2, 110, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Bill Amount");
        columns.addColumn(c);
        c = new TableColumn(3, 100, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Outstanding Amount");
        columns.addColumn(c);
        c = new TableColumn(4, 87, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("Payment");
        columns.addColumn(c);
       
        jTable3.setColumnModel(columns);

        jTable3.getTableHeader().setReorderingAllowed(false);

        jTable3.setRowHeight(50);
        jTable3.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable3.setIntercellSpacing(new java.awt.Dimension(0, 1));

        setVisible(true);

    }
  public void populate()
    {
     try {
            Clines = dlCustomers.getCustomersList();
          jcustomerComboBox.removeAllItems();
          jTotal.setText(null);
          customerfield.setText(null);

  for(int i=0;i<Clines.size();i++)
  {
        jcustomerComboBox.setSelectedIndex(-1);
         jcustomerComboBox.addItem(Clines.get(i).getName());

}



          } catch (BasicException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
     public void TendorValue()
     {
       // jcustomerComboBox.addItem("N");
        jTendor.removeAllItems();
        jTendor.addItem("");
        jTendor.addItem("Cash");
        jTendor.addItem("Cheque");
         jTendor.addItem("Card");
     }

 private static class DataCellRenderer extends DefaultTableCellRenderer {

        private int m_iAlignment;

        public DataCellRenderer(int align) {
            m_iAlignment = align;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel aux = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            aux.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
           // aux.setVerti
            aux.setHorizontalAlignment(m_iAlignment);
            if (!isSelected) {
                aux.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
            }
            return aux;
        }
    }

 
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.creditSales");
    }

    public void activate() throws BasicException {
        TendorValue();
        populate();
        setDataTableModelAndHeader(jTable3,0);
        jTextdate.setText(null);
        jTextcheque.setText(null);
        jTextremark.setText(null);
        jTextbank.setText(null);
        jTextactualpay.setText(null);
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        customerfield = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTendor = new javax.swing.JComboBox();
        jChequeno = new javax.swing.JLabel();
        jTextcheque = new javax.swing.JTextField();
        jactual = new javax.swing.JLabel();
        jTextactualpay = new javax.swing.JTextField();
        jBank = new javax.swing.JLabel();
        jTextbank = new javax.swing.JTextField();
        jChequedate = new javax.swing.JLabel();
        jTextdate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextremark = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        m_jbtnpodate1 = new javax.swing.JButton();
        jTotal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jcustomerComboBox = new javax.swing.JComboBox();
        m_jBtnProcess = new javax.swing.JButton();
        billnofield = new javax.swing.JTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("Customer Name");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        customerfield.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jPanel1.add(customerfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 131, -1));

        jLabel4.setText("Tendor Type ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jTendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTendorActionPerformed(evt);
            }
        });
        jPanel1.add(jTendor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 131, 20));

        jChequeno.setText("Cheque No.");
        jPanel1.add(jChequeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, -1, -1));

        jTextcheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextchequeActionPerformed(evt);
            }
        });
        jPanel1.add(jTextcheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 131, -1));

        jactual.setText("Actual Payment");
        jPanel1.add(jactual, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jTextactualpay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextactualpayActionPerformed(evt);
            }
        });
        jPanel1.add(jTextactualpay, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 131, -1));

        jBank.setText("Bank Name");
        jPanel1.add(jBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, -1, -1));
        jPanel1.add(jTextbank, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 131, -1));

        jChequedate.setText("Cheque Date");
        jPanel1.add(jChequedate, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, -1, -1));

        jTextdate.setEnabled(false);
        jPanel1.add(jTextdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 131, -1));

        jLabel9.setText("Remarks");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));
        jPanel1.add(jTextremark, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 131, -1));

        jTable3.setAutoCreateColumnsFromModel(false);
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill No.", "       Bill Date & Time", "Bill Amount", "Out Standing Amount", "Payment"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 199, 720, 230));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 440, 70, -1));

        m_jbtnpodate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate1ActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnpodate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 40, -1));

        jTotal.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 440, 110, 15));

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Bill No.");
        jLabel2.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 50, 20));

        jLabel1.setText("Customer Name");
        jLabel1.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 10, 110, 20));

        jButton1.setBackground(java.awt.Color.white);
        jButton1.setText("Search");
        jButton1.setVerifyInputWhenFocusTarget(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 5, 86, 30));

        jcustomerComboBox.setVerifyInputWhenFocusTarget(false);
        jcustomerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcustomerComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(jcustomerComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 131, 20));

        m_jBtnProcess.setBackground(java.awt.Color.white);
        m_jBtnProcess.setText("Process");
        m_jBtnProcess.setMaximumSize(new java.awt.Dimension(60, 30));
        m_jBtnProcess.setMinimumSize(new java.awt.Dimension(60, 30));
        m_jBtnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnProcessActionPerformed(evt);
            }
        });
        jPanel2.add(m_jBtnProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 5, 86, 30));

        billnofield.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(billnofield, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 131, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 5, 741, 40));

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 743, 470));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 770, 497));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jBtnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnProcessActionPerformed
        tendor=jTendor.getSelectedItem().toString();
        // setfields();
        String actual=jTextactualpay.getText().toString();
        if(actual.equals(null)||actual.isEmpty()||actual.equals("0")) {
            showMsg(this,"Please enter the actual payment");
        } else{
            if(tendor.equals(null)||tendor.isEmpty()) {
                showMsg(this,"Please enter the tendor type");
            } else {
                jTendor.setEditable(false);
                ActiveInfo actpojo;
                actualpay = Double.parseDouble(actual);
                double val=0;
                int col=0;
                String billval=null;
                 String ticketId=null;
                for ( col = 0; col <  CreditlinesSize; col++) {
                    Double outstandingval=Creditlines.get(col).getCreditamount();
                    if(actualpay>Total) {
                        showMsg(this,"Actual payment should not be greater than the total bill amount");
                        break;
                    } else if(actualpay==0) {
                        break;
                    }
                    if(col==0 && actualpay<outstandingval) {
                        double act=outstandingval-actualpay;
                        jTable3.setValueAt( act , col, 3);
                        Total=Total-actualpay;
                        String secondDouble = Double.toString(Total);
                        jTotal.setText( secondDouble);
                        actpojo=new ActiveInfo();
                        billval=Creditlines.get(col).getBillno().toString();
                        ticketId=Creditlines.get(col).getTicketId().toString();

                        String primaryval=Creditlines.get(col).getId().toString();
                        //    actpojo.setId(primaryval);
                        actpojo.setStatus(billval);
                        actpojo.setCredit(act);
                        actpojo.setTendorType(tendor);
                        actpojo.setBankname(jTextbank.getText().toString());
                        actpojo.setChequeno(jTextcheque.getText().toString());
                        String cdate=jTextdate.getText().toString();
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date d1 = null;
                        if(!cdate.equals("")) {
                            try {
                                d1 = formatter.parse(cdate);
                            } catch (ParseException ex) {
                                Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        actpojo.setChequedate(d1);
                        actpojo.setRemarks(jTextremark.getText().toString());
                        
                        try {
                            dlCustomers.setCreditAllValue(actpojo,primaryval);
                            dlCustomers.updateCreditInvoice(ticketId);
                        } catch (BasicException ex) {
                            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        

                        break;
                    }
                    if(actualpay>= outstandingval ) {

                        jTable3.setValueAt( 0, col, 3);
                        Total=Total-outstandingval;
                        String secondDouble = Double.toString(Total);
                        jTotal.setText(secondDouble);
                        billval=Creditlines.get(col).getBillno().toString();
                         ticketId=Creditlines.get(col).getTicketId().toString();
                        actpojo=new ActiveInfo();
                        String  primaryval=Creditlines.get(col).getId().toString();
                        actpojo.setStatus(billval);
                        actpojo.setCredit(0);
                        actpojo.setTendorType(tendor);
                        try {
                            dlCustomers.setActivevALUE(actpojo,primaryval);
                        } catch (BasicException ex) {
                            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        actpojo.setBankname(jTextbank.getText().toString());
                        actpojo.setChequeno(jTextcheque.getText().toString());
                        datev=(jTextdate.getText().toString());
                        String cdate=jTextdate.getText().toString();
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date d1 = null;
                        if(!cdate.equals("")) {
                            try {
                                d1 = formatter.parse(cdate);
                            } catch (ParseException ex) {
                                Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        actpojo.setChequedate(d1);
                        actpojo.setRemarks(jTextremark.getText().toString());
                        try {
                            dlCustomers.setCreditAllValue(actpojo,primaryval);
                             dlCustomers.updateCreditInvoice(ticketId);
                        } catch (BasicException ex) {
                            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        actualpay=actualpay-outstandingval;

                    } else {
                        jTable3.setValueAt((outstandingval-actualpay) , col, 3);
                        Total=Total-(outstandingval-actualpay);
                        String secondDouble = Double.toString(Total);
                        jTotal.setText( secondDouble);
                        billval=Creditlines.get(col).getBillno().toString();
                         ticketId=Creditlines.get(col).getTicketId().toString();
                        actpojo=new ActiveInfo();
                        String primaryval=Creditlines.get(col).getId().toString();
                        //  actpojo.setId( primaryval);
                        actpojo.setStatus(billval);
                        actpojo.setTendorType(tendor);
                        Double money=outstandingval-actualpay;
                        actpojo.setCredit(money);
                        actpojo.setBankname(jTextbank.getText().toString());
                        actpojo.setChequeno(jTextcheque.getText().toString());
                        String cdate=jTextdate.getText().toString();
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date d1 = null;
                        if(!cdate.equals("")) {
                            try {
                                d1 = formatter.parse(cdate);
                            } catch (ParseException ex) {
                                Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        actpojo.setChequedate(d1);
                        actpojo.setRemarks(jTextremark.getText().toString());
                        try {
                            dlCustomers.setCreditAllValue(actpojo,primaryval);
                             dlCustomers.updateCreditInvoice(ticketId);
                        } catch (BasicException ex) {
                            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;


                    }

                }
                saveCreditTicket();
                jTextdate.setText(null);
                jTextcheque.setText(null);
                jTextremark.setText(null);
                jTextbank.setText(null);
                jTextactualpay.setText(null);
                setGridValues();
                showMessageGreen(this,"Processed Successfully.");

                // jTendor.setSelectedIndex(-1);

            }
        }
    }//GEN-LAST:event_m_jBtnProcessActionPerformed

    private void jcustomerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcustomerComboBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jcustomerComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     
        if(jcustomerComboBox.getSelectedIndex()==-1) {
            showMsg(this,"Please select the customer");
        } else
  {
            String name= jcustomerComboBox.getSelectedItem().toString();
            String idvalue =Clines.get(jcustomerComboBox.getSelectedIndex()).getId().toString();
            String billno=(String) billnofield.getText();
            try {
                //
                if(billno.isEmpty()) {
                    Creditlines=dlCustomers.getCreditSale2List(idvalue,billno);
                } else {
                    Creditlines=dlCustomers.getCreditSaleList(idvalue,billno);
                }
                CreditlinesSize=Creditlines.size();
                customerfield.setText(name);
                customerfield.setEditable(false);

                setDataTableModelAndHeader(jTable3,CreditlinesSize);
                setCheckInSaleTableData(jTable3,Creditlines);
                String Tendor =jTendor.getSelectedItem().toString();


            } catch (BasicException ex) {
                Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void m_jbtnpodate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate1ActionPerformed
        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        jTextdate.setText(getTodaysDate(date));
        setChangesmade(true);        // TODO add your handling code here:
}//GEN-LAST:event_m_jbtnpodate1ActionPerformed

    private void jTextactualpayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextactualpayActionPerformed
        String tendor=jTendor.getSelectedItem().toString();
        if(tendor.equals(null)||tendor.isEmpty()) {
            showMsg(this,"Please Enter The Valid Tendor Type");
        } else{
            String actual=jTextactualpay.getText().toString();
            actualpay = Double.parseDouble(actual);
            for (int col = 0; col <  CreditlinesSize; col++) {
                Double outstandingval=Creditlines.get(col).getCreditamount();
                if(actualpay>Total) {
                    showMsg(this,"Pay Should Not Be More Than Total Bill");
                    break;
                }
                if(actualpay==0) {
                    jTable3.setValueAt(0 , col, 4);
                    break;
                }
                if(col==0 && actualpay<outstandingval) {
                    jTable3.setValueAt(Formats.DoubleValue.formatValue(actualpay), col, 4);
                    actualpay=outstandingval-actualpay;
                    break;
                } else if(actualpay>=outstandingval) {

                    jTable3.setValueAt(Formats.DoubleValue.formatValue(outstandingval),col, 4);
                    actualpay=actualpay-outstandingval;
                } else {

                    jTable3.setValueAt((Formats.DoubleValue.formatValue(actualpay)),col, 4);
                    break;
                }
            }  }

    }//GEN-LAST:event_jTextactualpayActionPerformed

    private void jTextchequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextchequeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextchequeActionPerformed

    private void jTendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTendorActionPerformed
        try{
            String tendorType= jTendor.getSelectedItem().toString();
            if(tendorType.equals("Card")) {
                jBank.setVisible(true);
                jTextbank.setVisible(true);
                jChequeno.setVisible(false);
                jTextcheque.setVisible(false);
                jChequedate.setVisible(false);
                jTextdate.setVisible(false);
                m_jbtnpodate1.setVisible(false);
            } else if(tendorType.equals("Cheque")) {
                jBank.setVisible(true);
                jTextbank.setVisible(true);
                jChequeno.setVisible(true);
                jTextcheque.setVisible(true);
                jChequedate.setVisible(true);
                jTextdate.setVisible(true);
                m_jbtnpodate1.setVisible(true);
            } else {
                jBank.setVisible(false);
                jTextbank.setVisible(false);
                jChequeno.setVisible(false);
                jTextcheque.setVisible(false);
                jChequedate.setVisible(false);
                jTextdate.setVisible(false);
                m_jbtnpodate1.setVisible(false);
            }
        } catch(Exception e) {

        }
}//GEN-LAST:event_jTendorActionPerformed




 private void showMessageGreen(JCreditSales aThis,String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
 }
 private JPanel getLabelGreenPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.decode("#206e10"));
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}

  private JPanel getLabelRedPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.red);
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
 private void showMsg(JCreditSales aThis, String msg) {
       JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
     }
     
private void showMessage(CreditSales aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
    }  private void setDataTableModelAndHeader(JTable table, int size) {

        table.getTableHeader().setPreferredSize(new Dimension(30, 25));
        table.setModel(new DefaultTableModel(columnCInNames, size));

       }

       private void setCheckInSaleTableData(JTable jTable3, List<CreditSalePojo> Creditlines) {
       Total=0;
          for (int col = 0; col <  CreditlinesSize; col++) {
            jTable3.setValueAt( Creditlines.get(col).getBillno(), col, 0);
        }
           for (int col = 0; col <  CreditlinesSize; col++) {
            Date billdate=Creditlines.get(col).getBilldate();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy '/' hh:mm a");
                jTable3.setValueAt(  formatter.format(billdate), col, 1);
        }
       for (int col = 0; col <  CreditlinesSize; col++) {
            jTable3.setValueAt( Formats.DoubleValue.formatValue(Creditlines.get(col).getBillamount()), col, 2);
        }
       for (int col = 0; col <  CreditlinesSize; col++) {
            DecimalFormat df = new DecimalFormat("#######0.00");
            Double dec=Creditlines.get(col).getCreditamount();
            double output =  Double.valueOf(df.format(dec)).doubleValue();
            jTable3.setValueAt(Formats.DoubleValue.formatValue(output), col, 3);
            System.out.println(output);
            Total=Total+Creditlines.get(col).getCreditamount();
        }
       System.out.println(Total);
      String secondDouble = Double.toString(Total);

      jTotal.setText(secondDouble);

    }



private void saveCreditTicket() {
       String customerId =  Clines.get(jcustomerComboBox.getSelectedIndex()).getId();
       String closeCash = m_App.getActiveCashIndex();
       String closeDay = m_App.getActiveDayIndex();
       String posNo = m_App.getProperties().getPosNo();
       Date sysDate = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String currentDate = sdf.format(sysDate);
        try {
            sysDate = sdf.parse(currentDate);
        } catch (ParseException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }
       double total = Double.parseDouble(jTextactualpay.getText());
       String tenderType = jTendor.getSelectedItem().toString();
       String chequeNO = jTextcheque.getText().toString();
        try {
            dlSales.saveCreditTicket(posNo,closeCash,closeDay,sysDate,total,tenderType,chequeNO);
        } catch (BasicException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void setGridValues(){
         String billno=(String) billnofield.getText();
         String name= jcustomerComboBox.getSelectedItem().toString();
        String idvalue =Clines.get(jcustomerComboBox.getSelectedIndex()).getId().toString();
        try {
            Creditlines=dlCustomers.getCreditSale2List(idvalue,billno);
        } catch (BasicException ex) {
            Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
        }
            CreditlinesSize=Creditlines.size();
           
             customerfield.setText(name);
             customerfield.setEditable(false);

            setDataTableModelAndHeader(jTable3,CreditlinesSize);
            setCheckInSaleTableData(jTable3,Creditlines);
            String Tendor =jTendor.getSelectedItem().toString();
    }
    public static void setChangesmade(boolean changesmade) {
        JCreditSales.changesmade = changesmade;
    }

     private String getTodaysDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd/MM/yyyy");
            strDate = format.format(date);

           }
        return strDate;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField billnofield;
    private javax.swing.JTextField customerfield;
    private javax.swing.JLabel jBank;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jChequedate;
    private javax.swing.JLabel jChequeno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JComboBox jTendor;
    private javax.swing.JTextField jTextactualpay;
    private javax.swing.JTextField jTextbank;
    private javax.swing.JTextField jTextcheque;
    private javax.swing.JTextField jTextdate;
    private javax.swing.JTextField jTextremark;
    private javax.swing.JLabel jTotal;
    private javax.swing.JLabel jactual;
    private javax.swing.JComboBox jcustomerComboBox;
    private javax.swing.JButton m_jBtnProcess;
    private javax.swing.JButton m_jbtnpodate1;
    // End of variables declaration//GEN-END:variables
    
}
