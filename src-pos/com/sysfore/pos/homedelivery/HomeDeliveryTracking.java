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

package com.sysfore.pos.homedelivery;

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
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.sales.DiscountRateinfo;
import com.openbravo.pos.sales.ProcessInfo;
import com.openbravo.pos.sales.RetailTaxesLogic;
import com.openbravo.pos.sales.TaxesException;
import com.sysfore.pos.homedelivery.DeliveryBoyName;
//import com.sysfore.pos.homedelivery.HomeDeliveryUpdate;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
//import com.sysfore.pos.purchaseorder.DeliveryBoyInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
 *
 * @author adrianromero
 */
public class HomeDeliveryTracking extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    private DataLogicCustomers dlCustomers;
   // private HomeDeliveryUpdate HomeUpateinfo;
    private static boolean changesmade;
    private TicketParser m_TTP;
    private String[] columnCInNames;
    private int columnSize = 0;
    ArrayList<HomeDeliveryInfo> Tlists=null;
  private RetailTaxesLogic taxeslogic;
   private String fromDate="";
   private String toDate="";
   private String deliveryBoyName="";
   private String fromDateTrim="";
   private String toDateTrim="";
   private String deliverBoyNameTrim="";
   private String curFromDate="";
   private String curToDate="";
   private String curDeliveryBoy="";
   int row=0;
   int col=0;
   protected DataLogicSales dlSales;
    private SentenceList senttax;
   
    public HomeDeliveryTracking() throws BasicException {
//    m_App = app;
//   dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
       
        //initComponents();  
        
       
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;
        m_dlSystem =  (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        initComponents();
        DefaultTableColumnModel columns = new DefaultTableColumnModel();
        TableColumn c;

        c = new TableColumn(0, 70, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Home Delivery No<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(1, 90, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Bill Date & Time<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(2, 70, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Delivery Boy<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(3, 70, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Bill Amount<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(4, 70, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Amount To be Collected<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(5, 60, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Collected Amount<br /></html>");
        columns.addColumn(c);
         c = new TableColumn(6, 50, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JCheckBox()));
        c.setHeaderValue("<html>Advance issued<br /></html>");
        columns.addColumn(c);
         c = new TableColumn(7, 50, new DataCellRenderer(javax.swing.SwingConstants.RIGHT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Advance Return<br /></html>");
        columns.addColumn(c);
        c = new TableColumn(8, 60, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JTextField()));
        c.setHeaderValue("<html>Status<br /></html>");
        columns.addColumn(c);
         c = new TableColumn(9, 70, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JCheckBox()));
        c.setHeaderValue("<html>Delivered To<br /></html>");
        columns.addColumn(c);
         c = new TableColumn(10, 77, new DataCellRenderer(javax.swing.SwingConstants.LEFT), new DefaultCellEditor(new JCheckBox()));
        c.setHeaderValue("<html>Delivered Date & Time<br /></html>");
        columns.addColumn(c);
        jTable1.setColumnModel(columns);
        jScrollPane1.setColumnHeader(new JViewport() {
      @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = 50;
        return d;
      }
    });
        jTable1.getTableHeader().setReorderingAllowed(false);

        jTable1.setRowHeight(50);
        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));

  
        setVisible(true);
//        m_Saleslines = new InventoryTableModel();
//        m_jSalesDumpTable.setModel(m_Saleslines);

      
    
         setVisible(true);
         jHomeDeliveryNo.setEditable(false);
         jAmtToCollected.setEditable(false);
         jBillDateTime.setEditable(false);
         jBillAmount.setEditable(false);
        //add(m_jDiscountList);
         txtFromOrderDate.setEditable(false);
         txtToDate.setEditable(false);
        // m_jBtnDelivered.setEnabled(false);
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

    private void initTableDataAndFilter() {
     
        columnCInNames = new String[]{
            "Home Delivery No", "Bill Date & Time", "Delivery Boy", "Bill Amount", "Amount To be Collected", "Collected Amount", "Advance issued", "Advance Return","Status","Delivered To","Delivered Date & Time"
        };
       

    }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateNew=sdf.format(date);
  
    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.HomeDelivery");
    }

    public void activate() throws BasicException {
        initTableDataAndFilter();
     m_jCboPayments.setSelectedIndex(-1);
                getDeliveryBoyNames();
                getDeliveryBoys();               
                txtFromOrderDate.setText(dateNew);        
                txtToDate.setText(dateNew);
                String curDate=txtFromOrderDate.getText();
                curDate=curDate.substring(0, 10);
                String currentDate=getTrimFormatted(curDate);
                String deliveryStatus;
                m_jCboStatus.setSelectedItem("Pending");
                String statusvalue = m_jCboStatus.getSelectedItem().toString();
                  if(statusvalue.equals("Pending")){
                      deliveryStatus = "N";
                  }else if(statusvalue.equals("Dispatch")){
                      deliveryStatus = "D";
                  }else{
                      deliveryStatus = "Y";
                  }
                Tlists= dlCustomers.getSearchFromDate(currentDate,deliveryStatus);
                columnSize=Tlists.size();                   
                setCheckInTableModelAndHeader(jTable1, columnSize);
                setCheckInTableData(jTable1); 
                curFromDate=currentDate;
                curToDate=currentDate;
              
                 cleartxtFields();
                 jButtonSave.setEnabled(true);
                 m_jBtnDelivered.setEnabled(false);
                 m_jBtnDispatch.setEnabled(false);
                 jDeliveryDate.setText(dateNew);
                jDeliveryDate.setEnabled(false);
               // m_jBtnDelivered.setEnabled(false);
                senttax = dlSales.getRetailTaxList();
          }
     private String getFormattedDate(Date date, String pattern) {
        java.text.Format format = new SimpleDateFormat(pattern);
        String strDate = format.format(date);
        return strDate;
    }
     

      private void setCheckInTableData(JTable table) {

        for (int col = 0; col < columnSize; col++) {
          String deliveryNo = Tlists.get(col).getHomeDeliveryNO();
            table.setValueAt("<html>"+deliveryNo+"<br /></html>", col, 0);
        }
        for (int col = 0; col < columnSize; col++) {
            String sysDate = getFormattedDate(Tlists.get(col).getBilldate(), "dd-MM-yyyy HH:mm:ss");
            table.setValueAt("<html>"+sysDate+"<br /></html>", col, 1);
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Tlists.get(col).getDeliveryBoy(), col, 2);
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(Tlists.get(col).getBillAmount()), col, 3);
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(Tlists.get(col).getBillAmount()), col, 4);
          
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(Tlists.get(col).getCollectedAmount()), col, 5);
          
        }
         for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(Tlists.get(col).getAdvanceIssued()), col, 6);
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Formats.DoubleValue.formatValue(Tlists.get(col).getAdvanceReturned()), col, 7);
        }
        for (int col = 0; col < columnSize; col++) {
            String status = null;
            if(Tlists.get(col).getStatus().equals("N")){
                status = "Pending";
            }else if(Tlists.get(col).getStatus().equals("Y")){
                status = "Delivered";
            }else{
                status = "Dispatch";
            }
            table.setValueAt(status, col, 8);
        }
        for (int col = 0; col < columnSize; col++) {
            table.setValueAt(Tlists.get(col).getDeliveryTo(), col, 9);
        }
        for (int col = 0; col < columnSize; col++) {
            Date deliveryDate = Tlists.get(col).getDeliveryDate();
            //String delivery;
            if(deliveryDate==null){
            //    delivery="";
                 table.setValueAt((""), col, 10);
            }else{
                 table.setValueAt((sdf.format(deliveryDate)), col, 10);
            }
           
        }

    }

 private void setCheckInTableModelAndHeader(JTable table, int size) {
        table.getTableHeader().setPreferredSize(new Dimension(30, 25));
        table.setModel(new DefaultTableModel(columnCInNames, size));
    }
    public boolean deactivate() {
       
        return true;
    }
    
  public static void setChangesmade(boolean changesmade) {
        HomeDeliveryTracking.changesmade = changesmade;
    }


   

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jHomeDeliveryNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jAmtToCollected = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jAdvanceIssued = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jBillDateTime = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jBillAmount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jCollectAmt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jAdvancesReturn = new javax.swing.JTextField();
        jDeliveryBoyName = new javax.swing.JComboBox();
        jButtonSave = new javax.swing.JButton();
        m_jBtnDelivered = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFromOrderDate = new javax.swing.JTextField();
        jComboDeliveryBoy = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_jbtnpodate1 = new javax.swing.JButton();
        m_jbtnpodate2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        m_jCboStatus = new javax.swing.JComboBox();
        jDeliveredTo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jDeliveryDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        m_jbtnpodate3 = new javax.swing.JButton();
        m_jBtnDispatch = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        m_jCustomer = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jAddress = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        m_jCboPayments = new javax.swing.JComboBox();
        m_jBtnPrint = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(90, 20));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setAutoCreateColumnsFromModel(false);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setEnabled(false);
        jTable1.setRowHeight(50);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OnclickedTableRow(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 740, 290));

        jLabel4.setText("Home Delivery No");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 140, -1));

        jHomeDeliveryNo.setPreferredSize(new java.awt.Dimension(100, 25));
        jHomeDeliveryNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHomeDeliveryNoActionPerformed(evt);
            }
        });
        add(jHomeDeliveryNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 135, 20));

        jLabel5.setText("Delivery Boy");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 140, -1));

        jLabel6.setText("Amount to be Collected");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 160, -1));

        jAmtToCollected.setPreferredSize(new java.awt.Dimension(100, 25));
        add(jAmtToCollected, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 135, 20));

        jLabel7.setText("Advance Issued");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 120, 20));

        jAdvanceIssued.setPreferredSize(new java.awt.Dimension(100, 25));
        add(jAdvanceIssued, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 135, 20));

        jLabel8.setText("Bill Date &Time");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 110, -1));

        jBillDateTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBillDateTimeActionPerformed(evt);
            }
        });
        add(jBillDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 135, 20));

        jLabel9.setText("Bill Amount");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 120, -1));

        jBillAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBillAmountActionPerformed(evt);
            }
        });
        add(jBillAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 135, 20));

        jLabel10.setText("Collected Amount");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 120, -1));
        add(jCollectAmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 135, 20));

        jLabel11.setText("Advanced Returned");
        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 140, -1));
        add(jAdvancesReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, 135, 20));

        jDeliveryBoyName.setPreferredSize(new java.awt.Dimension(100, 25));
        add(jDeliveryBoyName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 135, 20));

        jButtonSave.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 70, -1));

        m_jBtnDelivered.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnDelivered.setText("Delivered");
        m_jBtnDelivered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelivered(evt);
            }
        });
        add(m_jBtnDelivered, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 90, -1));
        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, -10, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("From Date");
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 20));

        txtFromOrderDate.setPreferredSize(new java.awt.Dimension(150, 20));
        txtFromOrderDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromOrderDateActionPerformed(evt);
            }
        });
        jPanel2.add(txtFromOrderDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 80, 20));

        jComboDeliveryBoy.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboDeliveryBoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDeliveryBoyActionPerformed(evt);
            }
        });
        jPanel2.add(jComboDeliveryBoy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 80, 20));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setText("search");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getData(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, -1, 20));

        jLabel3.setText("DeliveryBoy");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 80, 20));

        txtToDate.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel2.add(txtToDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 80, 20));

        jLabel2.setText("To Date");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 50, 20));

        m_jbtnpodate1.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate1ActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnpodate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 10, 30, 20));

        m_jbtnpodate2.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodate2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate2ActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnpodate2, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 10, 30, 20));

        jLabel14.setText("Status");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 50, 20));

        m_jCboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pending", "Dispatch", "Delivered" }));
        jPanel2.add(m_jCboStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 80, 20));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 40));

        jDeliveredTo.setPreferredSize(new java.awt.Dimension(100, 25));
        jDeliveredTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeliveredToActionPerformed(evt);
            }
        });
        add(jDeliveredTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 135, 20));

        jLabel12.setText("Delivered To");
        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 130, 20));

        jDeliveryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeliveryDateActionPerformed(evt);
            }
        });
        add(jDeliveryDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 135, 20));

        jLabel13.setText("Delivered Time & Date");
        add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 140, 20));

        m_jbtnpodate3.setBackground(new java.awt.Color(255, 255, 255));
        m_jbtnpodate3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate3ActionPerformed(evt);
            }
        });
        add(m_jbtnpodate3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 30, 20));

        m_jBtnDispatch.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnDispatch.setText("Delivery Dispatch");
        m_jBtnDispatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnDispatchjButtonDelivered(evt);
            }
        });
        add(m_jBtnDispatch, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 140, -1));

        jLabel15.setText("Customer");
        add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 150, 20));
        add(m_jCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 135, -1));

        jLabel16.setText("Address");
        add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 110, 20));

        m_jAddress.setColumns(20);
        m_jAddress.setRows(5);
        jScrollPane2.setViewportView(m_jAddress);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 140, 60));

        jLabel17.setText("Payment");
        add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 110, 20));

        m_jCboPayments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Cheque", "Credit", " " }));
        add(m_jCboPayments, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 140, -1));

        m_jBtnPrint.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnPrint.setText("Print");
        m_jBtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnPrintActionPerformed(evt);
            }
        });
        add(m_jBtnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 70, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void txtFromOrderDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromOrderDateActionPerformed
        // TODO add your handling code here:
        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        txtFromOrderDate.setText(getTodaysDate(date));
        setChangesmade(true);
    }//GEN-LAST:event_txtFromOrderDateActionPerformed

    private void jBillDateTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBillDateTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBillDateTimeActionPerformed

    private void jBillAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBillAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBillAmountActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

            try {
                onSave();
            } catch (ParseException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }

           // m_jBtnDelivered.setEnabled(true);
      
    }//GEN-LAST:event_jButtonSaveActionPerformed
public void onSave() throws ParseException
{
        try {
           
            String homeDeliveryNo=jHomeDeliveryNo.getText();
            String deliveryBoyUpdatedName=(String)jDeliveryBoyName.getSelectedItem(); 
            String deliveryBoyID = null;
            ArrayList<DeliveryBoyId> alist;
            if(!deliveryBoyUpdatedName.equals("")){
                alist=dlCustomers.getDeliveryBoyID(deliveryBoyUpdatedName);
                deliveryBoyID=alist.get(0).getId();
            }
          
            double advanceIssuedUpdatedValue=Double.parseDouble(jAdvanceIssued.getText());
            double collectedAmountUpdatedValue=Double.parseDouble(jCollectAmt.getText());
            double advancesReturnUpdatedValue=Double.parseDouble(jAdvancesReturn.getText());
            String deliveryDateUpdatedValue=jDeliveryDate.getText();           
            String deliveredToUpdatedValue=jDeliveredTo.getText();
            String hdCusAddress = m_jAddress.getText();
           
            HomeDeliveryInfo data=new HomeDeliveryInfo();
            data.setHomeDeliveryNO(homeDeliveryNo);
            data.setDeliveryBoy(deliveryBoyID);       
            data.setAdvanceIssued(advanceIssuedUpdatedValue);           
            data.setDeliveryTo(deliveredToUpdatedValue);           
            data.setCollectedAmount(collectedAmountUpdatedValue);            
            data.setAdvanceReturned(advancesReturnUpdatedValue);
            data.setHdCusAddress(hdCusAddress);
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date newDate =null;
            try {
                newDate = (Date) format.parse(jDeliveryDate.getText());
            } catch (ParseException ex) {
            }
            data.setDeliveryDate(newDate);
            int updateRecord=0;            
            updateRecord=dlCustomers.updateHomeTrackingInfo(data); 
             String status = m_jCboStatus.getSelectedItem().toString();
                  String deliveryStatus;
                  if(status.equals("Pending")){
                      deliveryStatus = "N";
                      
                  }else if(status.equals("Dispatch")){
                      deliveryStatus = "D";
                     
                  }else{
                      deliveryStatus = "Y";
                     
                  }
                  activate();
          

             } catch (BasicException ex) {
           Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
       }
 
   
    
}
  private Date getFormattedDate(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = new Date();
        try {
            newDate = (Date) format.parse(strDate);
        } catch (ParseException ex) {
        }
        return newDate;
    }
    private void m_jbtnpodate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate1ActionPerformed


         Date date = JCalendarDialog.showCalendarTime(this, new Date());
         txtToDate.setText(getTodaysDate(date));
         setChangesmade(true);
    }//GEN-LAST:event_m_jbtnpodate1ActionPerformed

    private void m_jbtnpodate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate2ActionPerformed

        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        
        txtFromOrderDate.setText(getTodaysDate(date));
        setChangesmade(true);
    }//GEN-LAST:event_m_jbtnpodate2ActionPerformed

    private void OnclickedTableRow(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OnclickedTableRow
        // TODO add your handling code here:

         row = jTable1.rowAtPoint(evt.getPoint());
         col = jTable1.columnAtPoint(evt.getPoint());
          double creditAmt = 0;
         
            for(int i=row; i<=row; i++)
            for(int j=0; j<=0; ){
                String homeDeleivery = jTable1.getModel().getValueAt(i, j).toString();
               homeDeleivery =  homeDeleivery.replaceAll("<html>", "");
               homeDeleivery =  homeDeleivery.replaceAll("<br /></html>", "");
              
                jHomeDeliveryNo.setText(homeDeleivery);
               
                creditAmt = Tlists.get(i).getCreditAmount();
                m_jCustomer.setText(Tlists.get(i).getCusName());
                if(Tlists.get(i).getHdCusAddress()==null){
                    m_jAddress.setText(Tlists.get(i).getCustomerAdress());
                }else{
                     m_jAddress.setText(Tlists.get(i).getHdCusAddress());
                }
                
                String billdate = jTable1.getModel().getValueAt(i, ++j).toString();
                billdate =  billdate.replaceAll("<html>", "");
                billdate =  billdate.replaceAll("<br /></html>", "");
                jBillDateTime.setText(billdate);
                try{
                String deliveryBoyName=jTable1.getModel().getValueAt(i, ++j).toString();
                jDeliveryBoyName.setSelectedItem(deliveryBoyName);
                }catch(NullPointerException ex){
                     jDeliveryBoyName.setSelectedItem("");
                }
                String BillAmount=""+jTable1.getModel().getValueAt(i, ++j);
                jBillAmount.setText(BillAmount);
                if(Tlists.get(i).getIsCod().equals("N")){
                    jCollectAmt.setText(BillAmount);
                }else{
                    jCollectAmt.setText("0");
                }
                double billAmount = Double.parseDouble(BillAmount);
                double amtToBeCollected = billAmount;//-creditAmt;
                String collectedAmount=""+jTable1.getModel().getValueAt(i, ++j);
                jAmtToCollected.setText(Formats.DoubleValue.formatValue(amtToBeCollected));
                String amount = jTable1.getModel().getValueAt(i, ++j).toString();
                jCollectAmt.setText(amount);
                jAdvanceIssued.setText(jTable1.getModel().getValueAt(i, ++j).toString());
                jAdvancesReturn.setText(jTable1.getModel().getValueAt(i, ++j).toString());
                String deliveryStatus=jTable1.getModel().getValueAt(i, ++j).toString();
                if(deliveryStatus.equals("Pending")){
                    jButtonSave.setEnabled(true);
                    m_jBtnDelivered.setEnabled(false);
                    m_jBtnDispatch.setEnabled(true);
                }
                else if(deliveryStatus.equals("Dispatch")){
                    jButtonSave.setEnabled(true);
                    m_jBtnDelivered.setEnabled(true);
                    m_jBtnDispatch.setEnabled(true);
                   
                }else{
                    jButtonSave.setEnabled(false);
                    m_jBtnDelivered.setEnabled(false);
                    m_jBtnDispatch.setEnabled(false);
                }
                String DeliveredTo;
                try{
                    DeliveredTo = jTable1.getModel().getValueAt(i,++j).toString();
                }catch(NullPointerException ex){
                    DeliveredTo = "";
                }

                jDeliveredTo.setText(DeliveredTo);
                String deliveryDate = jTable1.getModel().getValueAt(i, ++j).toString();
                if(deliveryDate.equals("")){
                    jDeliveryDate.setText(dateNew);
                }else{
                     jDeliveryDate.setText(deliveryDate);
                }
               
//
            }
           

       
           

    }//GEN-LAST:event_OnclickedTableRow
 private void printTicket(String sresourcename, RetailTicketInfo ticket) {
        java.util.List<TicketLineConstructor> allLines = null;
        com.openbravo.pos.printer.printer.ImagePrinter printer = new ImagePrinter();
        allLines = getHomeDeliveryLines(ticket);
         try {
            printer.print(allLines);
        } catch (PrinterException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
 private java.util.List<TicketLineConstructor> getHomeDeliveryLines(RetailTicketInfo ticket) {
      java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
       allLines.add(new TicketLineConstructor("Home Delivery No:" + getSpaces(3) + ticket.getDocumentNo()));
       allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(10) + (ticket.printDate())));
       allLines.add(new TicketLineConstructor("Customer:" + getSpaces(11) + (m_jCustomer.getText())));
       allLines.add(new TicketLineConstructor("Captain:" + getSpaces(12) + (ticket.printUser())));
       allLines.add(new TicketLineConstructor("Phone No.:" + getSpaces(10) + (ticket.printPhoneNo())));
       allLines.add(new TicketLineConstructor("Address:" + getSpaces(12) + m_jAddress.getText()));


       allLines.add(new TicketLineConstructor(getDottedLine(90)));
       allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price"+getSpaces(9)+"Value(INR)"));
       allLines.add(new TicketLineConstructor(getDottedLine(90)));
         for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue+getSpaces(7 - qty.length() + 12 - subValue.length())+total));
        }
          System.out.println("ticket.getTaxes().get(0).getTaxInfo().getName();"+ticket.getTaxes().get(0).getTaxInfo().getName());

          String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
          String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
          String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
          String discount = Formats.DoubleValue.formatValue(ticket.getTotalDiscount());
          String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
          String total =  Formats.DoubleValue.formatValue(ticket.getTotal());
       allLines.add(new TicketLineConstructor(getDottedLine(90)));
       allLines.add(new TicketLineConstructor(getSpaces(33)+"Total " + getSpaces(29-subTotal.length()) + (subTotal)));
        allLines.add(new TicketLineConstructor(getSpaces(33)+"Discount " + getSpaces(25-discount.length()) + ("-"+discount)));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Total After Discount " + getSpaces(14 - totalAfrDiscount.length()) + (totalAfrDiscount)));

      if(ticket.getTaxes().size()!=0){
          for(int i=0;i<ticket.getTaxes().size();i++){
              System.out.println("ticket.getTaxes().get(i).getTax()--"+ticket.getTaxes().get(i).getTax());
              if(ticket.getTaxes().get(i).getTax()!=0.00){
                 allLines.add(new TicketLineConstructor(getSpaces(33)+ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(26 - ticket.getTaxes().get(i).getTaxInfo().getName().length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()))));
              }
          }

      }
       String aCount = ticket.printTicketCount();
       allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Charge 6%" + getSpaces(17-serviceCharge.length()) + serviceCharge));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Tax 4.94%" + getSpaces(18-serviceTax.length()) + serviceTax));
     

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Grand Total : " + getSpaces(21-total.length()) + total));
       return allLines;
    }
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
    private void jDeliveredToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeliveredToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jDeliveredToActionPerformed

    private void jDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeliveryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jDeliveryDateActionPerformed

    private void getData(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getData
        try {
            // TODO add your handling code here:
           searchData();
          cleartxtFields();
        } catch (ParseException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BasicException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_getData
public void cleartxtFields(){
        jHomeDeliveryNo.setText("");
        jDeliveryBoyName.setSelectedIndex(-1);
        jAmtToCollected.setText("");
        jAdvanceIssued.setText("");
        jDeliveredTo.setText("");
        jBillDateTime.setText("");
        jBillAmount.setText("");
        jCollectAmt.setText("");
        jAdvancesReturn.setText("");
        jDeliveryDate.setText("");
        m_jCustomer.setText("");
        m_jAddress.setText("");
}
    public void getDeliveryBoyNames() throws BasicException
{

     ArrayList<DeliveryBoyName> name=dlCustomers.getDeliveryBoyList1();
     jComboDeliveryBoy.removeAllItems();
     jComboDeliveryBoy.addItem("");     
     for(int i=0; i<name.size(); i++){         
      jComboDeliveryBoy.addItem(name.get(i).getName());
     }
    
}
public void getDeliveryBoys() throws BasicException
{
     
      ArrayList<DeliveryBoyName> name=dlCustomers.getDeliveryBoyList1();
      jDeliveryBoyName.removeAllItems();
      jDeliveryBoyName.addItem("");     
      for(int i=0; i<name.size(); i++){         
      jDeliveryBoyName.addItem(name.get(i).getName());
    
     }
    
}




    private void jButtonDelivered(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelivered
        // TODO add your handling code here:
        String isPaid = null;
        String DeliveredNo=jHomeDeliveryNo.getText();

        try {
            isPaid = dlCustomers.getIsPaidStatus(DeliveredNo);
        } catch (BasicException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }
        double collectedAmt = Double.parseDouble(jCollectAmt.getText());
        double amtToBeCollected = Double.parseDouble(jAmtToCollected.getText());
        if(jCollectAmt.getText().equals("") || jCollectAmt.getText().equals("0.0")){
            showMsg("Please enter the collected amount");
        }else if(collectedAmt<amtToBeCollected){
            showMsg("Collected amount should be greater than or equal to amount to be collected");
            jCollectAmt.setText("");
        }
        else if(jDeliveryDate.getText().equals("")){
             showMsg("Please enter the delivered date");
        }else if(m_jCboPayments.getSelectedIndex()==-1 && isPaid.equals("N")){
//            if(isPaid.equals("N")){
                showMsg("Please select the payment mode");
          //  }
            
        }else{
          String statusvalue = m_jCboStatus.getSelectedItem().toString();

          String deliveryStatus;
          if(statusvalue.equals("Pending")){
              deliveryStatus = "N";
          }else if(statusvalue.equals("Dispatch")){
              deliveryStatus = "D";
          }else{
              deliveryStatus = "Y";
          }
            String status="Y";
            int updatedStatus = 0;

           
            if(isPaid.equals("Y")){
                try {
                     dlCustomers.updateDeliveredStatus(DeliveredNo, status);
                } catch (BasicException ex) {
                    Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                if(m_jCboPayments.getSelectedItem()=="Credit"){
                    String cusId = Tlists.get(0).getCusId();
                    String ticketId = Tlists.get(0).getTicketId();
                    Date sysDate = new Date();
                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   String currentDate = sdf.format(sysDate);
                    try {
                        sysDate = sdf.parse(currentDate);
                    } catch (ParseException ex) {
                        Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    double total = Double.parseDouble(jBillAmount.getText());
                    try {
                        dlSales.saveHomeCreditSale(ticketId, DeliveredNo, sysDate, total, cusId);
                         dlCustomers.updateHomeCreditStatus(DeliveredNo, status);
                    } catch (BasicException ex) {
                        Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    saveHomeDeliveryTicket();

                    try {
                        dlCustomers.updateHomeDelivery(DeliveredNo, status);
                    } catch (BasicException ex) {
                        Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            try {
                Tlists = dlCustomers.getSearchFromDate(curFromDate, deliveryStatus);
            } catch (BasicException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
            columnSize=Tlists.size();
            setCheckInTableModelAndHeader(jTable1, columnSize);
            setCheckInTableData(jTable1);
            if(columnSize!=0){
               setCheckInTableModelAndHeader(jTable1, columnSize);
               setCheckInTableData(jTable1);
            }else{
                //showMsg("No Records are update");
            }
             cleartxtFields();
            try {
                activate();
            } catch (BasicException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   
        
    }//GEN-LAST:event_jButtonDelivered
private void saveHomeDeliveryTicket() {
       String closeCash = m_App.getActiveCashIndex();
       String closeDay = m_App.getActiveDayIndex();
       String posNo = m_App.getProperties().getPosNo();
       Date sysDate = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String currentDate = sdf.format(sysDate);
        try {
            sysDate = sdf.parse(currentDate);
        } catch (ParseException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }
       double total = Double.parseDouble(jBillAmount.getText());
       String tenderType = m_jCboPayments.getSelectedItem().toString();
       String chequeNO = "";
        try {
            dlSales.saveCreditTicket(posNo,closeCash,closeDay,sysDate,total,tenderType,chequeNO);
        } catch (BasicException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void jComboDeliveryBoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDeliveryBoyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboDeliveryBoyActionPerformed

    private void m_jbtnpodate3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnpodate3ActionPerformed
        // TODO add your handling code here:
        Date date = JCalendarDialog.showCalendarTime(this, new Date());
        jDeliveryDate.setText(getTodaysDate(date));
        setChangesmade(true);
    }//GEN-LAST:event_m_jbtnpodate3ActionPerformed

    private void jHomeDeliveryNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHomeDeliveryNoActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jHomeDeliveryNoActionPerformed

    private void m_jBtnDispatchjButtonDelivered(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDispatchjButtonDelivered
       String DeliveredNo=jHomeDeliveryNo.getText();
    String deliveryStatus;
    String statusvalue = m_jCboStatus.getSelectedItem().toString();
      if(statusvalue.equals("Pending")){
          deliveryStatus = "N";
      }else if(statusvalue.equals("Dispatch")){
          deliveryStatus = "D";
      }else{
          deliveryStatus = "Y";
      }
        String status="D";
        try {
        int updatedStatus=dlCustomers.updateDeliveredStatus(DeliveredNo, status);
        if((curFromDate.equals(curToDate))){
        Tlists= dlCustomers.getSearchFromDate(curFromDate,deliveryStatus);
        columnSize=Tlists.size();
        setCheckInTableModelAndHeader(jTable1, columnSize);
        setCheckInTableData(jTable1);
        if(columnSize!=0){
           setCheckInTableModelAndHeader(jTable1, columnSize);
           setCheckInTableData(jTable1);
        }else{
           // showMsg("No Records are update");
        }
         jButtonSave.setEnabled(false);
      }


        } catch (BasicException ex) {
            Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
        }

        cleartxtFields();
       // }
         // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnDispatchjButtonDelivered

    private void m_jBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnPrintActionPerformed
         RetailTicketInfo ticket = null;
         ServiceChargeTaxInfo serviceTax = null;
            try {
                ticket = dlSales.loadHomeDeliveryTicket(jHomeDeliveryNo.getText());
            } catch (BasicException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceTax = dlSales.loadServiceChargeTax(jHomeDeliveryNo.getText());
            } catch (BasicException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
            ticket.setServiceCharge(serviceTax.getServiceChargeAmt());
            ticket.setServiceChargeRate(serviceTax.getServiceChargerate());
            ticket.setServiceTax(serviceTax.getServiceTaxAmt());
            ticket.setServiceTaxRate(serviceTax.getServiceTaxrate());
            java.util.List<TaxInfo> taxlist = null;
            try {
                taxlist = senttax.list();
            } catch (BasicException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
            taxeslogic = new RetailTaxesLogic(taxlist,m_App);
            try {
                taxeslogic.calculateTaxes(ticket);
                for (RetailTicketLineInfo line : ticket.getLines()) {
                    line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), ticket.getCustomer(),"Y"));
                }
            } catch (TaxesException ex) {
                Logger.getLogger(HomeDeliveryTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
            String file = "Printer.DuplicateHomeDelivery";
            printTicket(file,ticket);
           // getPaymentLists(fti.getPid(), ticket);
            //O add your handling code here:
    }//GEN-LAST:event_m_jBtnPrintActionPerformed
 

    private Date convertDBDate(String date) throws ParseException
    {
  
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = parser.parse(date);
        return convertedDate;
    }
    
    
  private String getTodaysDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
            strDate = format.format(date);

        }
        return strDate;
    }
   private String getTodaysDate1(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd-MM-yyyy");
            strDate = format.format(date);

        }
        return strDate;
    }
  
  
    private String getDateBaseDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("yyyy-MM-dd");
            strDate = format.format(date);

        }
        return strDate;
    } 


 public void setDefaultCurrentDate()
  {
      DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        txtFromOrderDate.setText(getTodaysDate1(date));
        setChangesmade(true);   
  }
  private String getTrimFormatted(String date) {
        String retVal = "";
        String[] str = date.split("-");
        for (int i = str.length; i > 0; i--) {
            retVal = retVal + str[i - 1] + "-";
        }
        return retVal.substring(0, retVal.length() - 1);
    }
  
   public  String toddMMyy(Date day){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(day);
        System.out.println(date);
        return date;
    }
   
   
    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
  
  
  public void searchData() throws ParseException, BasicException
  {
    
      fromDate=txtFromOrderDate.getText();

      toDate=txtToDate.getText();
      String status = m_jCboStatus.getSelectedItem().toString();
      String deliveryStatus;
      if(status.equals("Pending")){
          deliveryStatus = "N";
          m_jBtnDispatch.setEnabled(true);
          jButtonSave.setEnabled(true);
          m_jBtnDelivered.setEnabled(false);
      }else if(status.equals("Dispatch")){
          deliveryStatus = "D";
          m_jBtnDelivered.setEnabled(true);
          m_jBtnDispatch.setEnabled(true);
          jButtonSave.setEnabled(true);
      }else{
          deliveryStatus = "Y";
           m_jBtnDispatch.setEnabled(false);
          jButtonSave.setEnabled(false);
          m_jBtnDelivered.setEnabled(false);
      }
     if((!toDate.equals(""))){
        String toDatetxt=(txtToDate.getText()).substring(0, 10);    
        toDate=getTrimFormatted(toDatetxt); 
        toDateTrim=toDate;
     }
     if(!fromDate.equals(""))
     {
      String fromDatetxt= (txtFromOrderDate.getText()).substring(0, 10);
      fromDate=getTrimFormatted(fromDatetxt);
      fromDateTrim=fromDate;
     }     
     deliveryBoyName=(String)jComboDeliveryBoy.getSelectedItem();
     deliverBoyNameTrim=deliveryBoyName;
      if(fromDate.equals(toDate) && (!fromDate.equals("")) && (!toDate.equals("")) && (!deliveryBoyName.equals("")))
     {  
       
        String newDate=toDate;
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = df.parse(newDate);
        cal.setTime(date);
        //adding one day to current date
        cal.add(Calendar.DAY_OF_MONTH, 1);
        date = cal.getTime();
         
        toDate=getDateBaseDate(date);
       
        toDateTrim=toDate;
        Tlists=dlCustomers.getSearchDate1(fromDate,toDate,deliveryBoyName,deliveryStatus);
        columnSize=Tlists.size();
      //  if(columnSize==0)
       //showMsg("No Records");
        setCheckInTableModelAndHeader(jTable1, columnSize);
        setCheckInTableData(jTable1);
     }else if(fromDate.equals(toDate) && (!fromDate.equals("")) && (!toDate.equals("")))
     {  
       
        String newDate=toDate;
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = df.parse(newDate);
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        date = cal.getTime();
          
        toDate=getDateBaseDate(date);
       
        toDateTrim=toDate;
        Tlists= dlCustomers.getSearchDate(fromDate,toDate,deliveryStatus);
        columnSize=Tlists.size();
           setCheckInTableModelAndHeader(jTable1, columnSize);
           setCheckInTableData(jTable1);
     //   }
     } else if((!fromDate.equals("")) && (!toDate.equals("")) && (!deliveryBoyName.equals("")))
     {     
      
        Tlists=dlCustomers.getSearchDate1(fromDate,toDate,deliveryBoyName,deliveryStatus);
        columnSize=Tlists.size();
        setCheckInTableModelAndHeader(jTable1, columnSize);
        setCheckInTableData(jTable1);        
       // }
     }else if((!fromDate.equals("")) && (!toDate.equals("")) && (deliveryBoyName.equals("")))
     {
      
        Tlists=dlCustomers.getSearchDate(fromDate,toDate,deliveryStatus);
        columnSize=Tlists.size();
        setCheckInTableModelAndHeader(jTable1, columnSize);
        setCheckInTableData(jTable1); 
     }
     else if((!fromDate.equals("")) && (toDate.equals("")) && (deliveryBoyName.equals("")))
     {  
         
     
        Tlists= dlCustomers.getSearchFromDate(fromDate,deliveryStatus);
        if(columnSize==0)
        setCheckInTableModelAndHeader(jTable1, columnSize);
        setCheckInTableData(jTable1);
     }else if((fromDate.equals("")) && (toDate.equals("")) && (!deliveryBoyName.equals("")))
     {
        
         Tlists=dlCustomers.getBasedOnDeliveryBoyName(deliveryBoyName);
         columnSize=Tlists.size();
         setCheckInTableModelAndHeader(jTable1, columnSize);
         setCheckInTableData(jTable1);
     
     }else if((!fromDate.equals("")) && (toDate.equals("")) && (!deliveryBoyName.equals("")))
     {  
       
         Tlists=dlCustomers.getSearchFromAndName(fromDate,deliveryBoyName,deliveryStatus);
         columnSize=Tlists.size();
         setCheckInTableModelAndHeader(jTable1, columnSize);
         setCheckInTableData(jTable1);
         
    }
     else if((fromDate.equals("")) && (!toDate.equals("")) && (deliveryBoyName.equals("")))
         showMsg("Sorry please enter from date and to date");
     else showMsg("please enter date"); 
      
  }

  
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jAdvanceIssued;
    private javax.swing.JTextField jAdvancesReturn;
    private javax.swing.JTextField jAmtToCollected;
    private javax.swing.JTextField jBillAmount;
    private javax.swing.JTextField jBillDateTime;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JTextField jCollectAmt;
    private javax.swing.JComboBox jComboDeliveryBoy;
    private javax.swing.JTextField jDeliveredTo;
    private javax.swing.JComboBox jDeliveryBoyName;
    private javax.swing.JTextField jDeliveryDate;
    private javax.swing.JTextField jHomeDeliveryNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea m_jAddress;
    private javax.swing.JButton m_jBtnDelivered;
    private javax.swing.JButton m_jBtnDispatch;
    private javax.swing.JButton m_jBtnPrint;
    private javax.swing.JComboBox m_jCboPayments;
    private javax.swing.JComboBox m_jCboStatus;
    private javax.swing.JTextField m_jCustomer;
    private javax.swing.JButton m_jbtnpodate1;
    private javax.swing.JButton m_jbtnpodate2;
    private javax.swing.JButton m_jbtnpodate3;
    private javax.swing.JTextField txtFromOrderDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
}
