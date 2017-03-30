/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.creditsale;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.model.Column;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JPanelView;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author shilpa
 */
public class CreditSales extends javax.swing.JPanel implements JPanelView {
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
    /**
     * Creates new form CreditSales
     */
    public CreditSales(AppView app) throws BeanFactoryException  {
         this.m_App = app;
     dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
         this.dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        initComponents();
        jTextactualpay.setFocusable(true);
        
        jTextactualpay.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setPayment();
          }
         public void removeUpdate(DocumentEvent e) {
           setPayment();
         }
         public void insertUpdate(DocumentEvent e) {
           setPayment();
          }

          


    });
        
       
    }
    
      private void setPayment() {
                if (jTextactualpay.getText()!=null ){

         Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
           Pattern pNum1 = Pattern.compile(".");
         if(jTextactualpay.getText().equals("")){
             actualpay = 0;

        }else{
             try{
                 actualpay=Double.parseDouble(jTextactualpay.getText());
                 
             }catch (NumberFormatException ex){
                 showMessage(this, "Please Enter The Valid Payment");
//                   jTextactualpay.setText(null);
               
             }
         }
            }
                 }
      
    public static void setChangesmade(boolean changesmade) {
        CreditSales.changesmade = changesmade;
    }
     
     private String getTodaysDate(Date date) {

        String strDate = "";
        if (date != null) {
            java.text.Format format = new SimpleDateFormat("dd/MM/yyyy");
            strDate = format.format(date);

           }
        return strDate;
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
     //System.out.println(Clines.get(i).getName());
     
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
     private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
private void showMessage(CreditSales aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, msg);
    }
    
    private void setDataTableModelAndHeader(JTable table, int size) {
        
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
            jTable3.setValueAt( Creditlines.get(col).getBillamount(), col, 2);
        } 
       for (int col = 0; col <  CreditlinesSize; col++) {
            DecimalFormat df = new DecimalFormat("#######0.00");
            Double dec=Creditlines.get(col).getCreditamount();
            double output =  Double.valueOf(df.format(dec)).doubleValue();
            jTable3.setValueAt(output, col, 3);
            System.out.println(output);
            Total=Total+Creditlines.get(col).getCreditamount();
        }
       System.out.println(Total);
      String secondDouble = Double.toString(Total);
       
      jTotal.setText(secondDouble);
     
    }
     
     
     
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jcustomerComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        customerfield = new javax.swing.JTextField();
        m_jBtnProcess = new javax.swing.JButton();
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
        billnofield = new javax.swing.JTextField();
        jTotal = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Customer Name");
        jLabel1.setVerifyInputWhenFocusTarget(false);
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 10, 110, 20));

        jcustomerComboBox.setVerifyInputWhenFocusTarget(false);
        jcustomerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcustomerComboBoxActionPerformed(evt);
            }
        });
        add(jcustomerComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 131, 20));

        jLabel2.setText("Bill No.");
        jLabel2.setVerifyInputWhenFocusTarget(false);
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 50, 20));

        jButton1.setBackground(java.awt.Color.white);
        jButton1.setText("Search");
        jButton1.setVerifyInputWhenFocusTarget(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 5, 86, 30));

        jLabel3.setText("Customer Name");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        customerfield.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        add(customerfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 131, -1));

        m_jBtnProcess.setBackground(java.awt.Color.white);
        m_jBtnProcess.setText("Process");
        m_jBtnProcess.setMaximumSize(new java.awt.Dimension(60, 30));
        m_jBtnProcess.setMinimumSize(new java.awt.Dimension(60, 30));
        m_jBtnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnProcessActionPerformed(evt);
            }
        });
        add(m_jBtnProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 5, 86, 30));

        jLabel4.setText("Tendor Type ");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jTendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTendorActionPerformed(evt);
            }
        });
        add(jTendor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 131, 20));

        jChequeno.setText("Cheque No.");
        add(jChequeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, -1, -1));

        jTextcheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextchequeActionPerformed(evt);
            }
        });
        add(jTextcheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 131, -1));

        jactual.setText("Actual Payment");
        add(jactual, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jTextactualpay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextactualpayActionPerformed(evt);
            }
        });
        add(jTextactualpay, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 131, -1));

        jBank.setText("Bank Name");
        add(jBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, -1, -1));

        jTextbank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextbankActionPerformed(evt);
            }
        });
        add(jTextbank, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 131, -1));

        jChequedate.setText("Cheque Date");
        add(jChequedate, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, -1, -1));

        jTextdate.setEnabled(false);
        jTextdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextdateActionPerformed(evt);
            }
        });
        add(jTextdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 131, -1));

        jLabel9.setText("Remarks");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));
        add(jTextremark, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 131, -1));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill No.", "       Bill Date & Time", "Bill Amount", "Out Standing Amount", "Payment"
            }
        ));
        jScrollPane3.setViewportView(jTable3);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(80);
        jTable3.getColumnModel().getColumn(1).setResizable(false);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(220);
        jTable3.getColumnModel().getColumn(2).setResizable(false);
        jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable3.getColumnModel().getColumn(3).setResizable(false);
        jTable3.getColumnModel().getColumn(3).setPreferredWidth(230);
        jTable3.getColumnModel().getColumn(4).setResizable(false);
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(150);

        add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 199, 715, 230));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 440, 70, -1));

        m_jbtnpodate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnpodate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnpodate1ActionPerformed(evt);
            }
        });
        add(m_jbtnpodate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 40, -1));

        billnofield.setVerifyInputWhenFocusTarget(false);
        add(billnofield, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 131, -1));

        jTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        add(jTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 440, 90, 15));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 760, 5));
    }// </editor-fold>//GEN-END:initComponents

    private void jTextchequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextchequeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextchequeActionPerformed

    private void jTextbankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextbankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextbankActionPerformed

    private void jTextdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextdateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       // System.out.println(" getId();   "+  Clines.get(jcustomerComboBox.getSelectedIndex()-1).getId());
        
       if(jcustomerComboBox.getSelectedIndex()==-1)
       {
            showMsg("Please select the customer");
       }
       else
        
        //System.out.println(name);
       {
        String name= jcustomerComboBox.getSelectedItem().toString();
        String idvalue =Clines.get(jcustomerComboBox.getSelectedIndex()).getId().toString();
        String billno=(String) billnofield.getText();
      try {
           // 
            if(billno.isEmpty())
            {
           Creditlines=dlCustomers.getCreditSale2List(idvalue,billno);
            }
            else
            {
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
      setChangesmade(true);
    }//GEN-LAST:event_m_jbtnpodate1ActionPerformed

    private void jTendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTendorActionPerformed
       try{
        String tendorType= jTendor.getSelectedItem().toString();
        if(tendorType.equals("Card"))
        {
         jBank.setVisible(true);
         jTextbank.setVisible(true);
         jChequeno.setVisible(false); 
         jTextcheque.setVisible(false);
         jChequedate.setVisible(false); 
         jTextdate.setVisible(false);
         m_jbtnpodate1.setVisible(false);
        }
        else if(tendorType.equals("Cheque"))
        {
           jBank.setVisible(true);
           jTextbank.setVisible(true);
           jChequeno.setVisible(true); 
           jTextcheque.setVisible(true);
           jChequedate.setVisible(true); 
           jTextdate.setVisible(true);
           m_jbtnpodate1.setVisible(true); 
        }
        else
        {
         jBank.setVisible(false);
           jTextbank.setVisible(false);
           jChequeno.setVisible(false); 
           jTextcheque.setVisible(false);
           jChequedate.setVisible(false); 
           jTextdate.setVisible(false);
           m_jbtnpodate1.setVisible(false); 
        }
       }
        catch(Exception e)
        {  
            
        }
        
    }//GEN-LAST:event_jTendorActionPerformed

    private void m_jBtnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnProcessActionPerformed
      tendor=jTendor.getSelectedItem().toString();
    // setfields();
     String actual=jTextactualpay.getText().toString();
     if(actual.equals(null)||actual.isEmpty()||actual.equals("0"))
    {
   showMsg("Please enter the actual payment");
    }
    else{
         if(tendor.equals(null)||tendor.isEmpty())
   {
     showMsg("Please enter the tendor type");
   }
    else
         {
             jTendor.setEditable(false);
             ActiveInfo actpojo;
             actualpay = Double.parseDouble(actual);
             double val=0;
             int col=0;
             String billval=null;
            
      for ( col = 0; col <  CreditlinesSize; col++) {
          Double outstandingval=Creditlines.get(col).getCreditamount();
          if(actualpay>Total)
          {
              showMsg("Actual payment should not be greater than the total bill amount");
              break;
          }
          else if(actualpay==0)
          {
              break;
          }
          if(col==0 && actualpay<outstandingval)
          {
             double act=outstandingval-actualpay;
             jTable3.setValueAt( act , col, 3);
             Total=Total-actualpay;
             String secondDouble = Double.toString(Total);
             jTotal.setText( secondDouble);
             actpojo=new ActiveInfo();
             billval=Creditlines.get(col).getBillno().toString();
             String primaryval=Creditlines.get(col).getId().toString();
         //    actpojo.setId(primaryval);
             actpojo.setStatus(billval);
             actpojo.setCredit(act);
             actpojo.setTendorType(tendor);
             actpojo.setBankname(jTextbank.getText().toString());
                  System.out.println(jTextbank.getText().toString());
                  actpojo.setChequeno(jTextcheque.getText().toString());
                  String cdate=jTextdate.getText().toString();
                  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                  Date d1 = null;
                  if(!cdate.equals(""))
                  {
              try {
                  d1 = formatter.parse(cdate);
              } catch (ParseException ex) {
                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
              }
                  }
                  actpojo.setChequedate(d1);
                  actpojo.setRemarks(jTextremark.getText().toString());
//              try {
//                dlCustomers.setCreditAllValue(actpojo,primaryval);
//          } catch (BasicException ex) {
//                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
//              }

              break;
          }
        if(actualpay>= outstandingval )
        {

            jTable3.setValueAt( 0, col, 3);
              Total=Total-outstandingval;
              String secondDouble = Double.toString(Total);
             jTotal.setText(secondDouble);
             billval=Creditlines.get(col).getBillno().toString();
              System.out.println( billval);
             actpojo=new ActiveInfo();
           String  primaryval=Creditlines.get(col).getId().toString();
           System.out.println(primaryval);
         //    actpojo.setId( primaryval);
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
                   if(!cdate.equals(""))
                   {
              try {
                 d1 = formatter.parse(cdate);
              } catch (ParseException ex) {
                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
              }
                   }
                   actpojo.setChequedate(d1);
                   actpojo.setRemarks(jTextremark.getText().toString());
//              try {
//                  dlCustomers.setCreditAllValue(actpojo,primaryval);
//              } catch (BasicException ex) {
//                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
//              }
            actualpay=actualpay-outstandingval;

        }
        else
        {
           jTable3.setValueAt((outstandingval-actualpay) , col, 3);
           Total=Total-(outstandingval-actualpay);
           String secondDouble = Double.toString(Total);
           jTotal.setText( secondDouble);
           billval=Creditlines.get(col).getBillno().toString();
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
             if(!cdate.equals(""))
             {
              try {
                  d1 = formatter.parse(cdate);
              } catch (ParseException ex) {
                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
              }
             }
             actpojo.setChequedate(d1);
                  actpojo.setRemarks(jTextremark.getText().toString());
//              try {
//                  dlCustomers.setCreditAllValue(actpojo,primaryval);
//              } catch (BasicException ex) {
//                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
//              }

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
 // jTendor.setSelectedIndex(-1);

    }
    }

  
    }//GEN-LAST:event_m_jBtnProcessActionPerformed
 
    private void setfields(ActiveInfo actpojo)
    {
       
       actpojo.setTendorType(tendor);


             actpojo.setBankname(jTextbank.getText().toString());
                  System.out.println(jTextbank.getText().toString());
                  actpojo.setChequeno(jTextcheque.getText().toString());
                  String cdate=jTextdate.getText().toString();
                  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                  Date d1 = null;
              try {
                  d1 = formatter.parse(cdate);
              } catch (ParseException ex) {
                  Logger.getLogger(CreditSales.class.getName()).log(Level.SEVERE, null, ex);
              }
                  actpojo.setChequedate(d1);
                  actpojo.setRemarks(jTextremark.getText().toString());
    }
    
    private void saveCreditTicket() {
       String customerId =  Clines.get(jcustomerComboBox.getSelectedIndex() - 1).getId();
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
            System.out.println("enter  size="+Creditlines.size());
             customerfield.setText(name);
             customerfield.setEditable(false);
              
            setDataTableModelAndHeader(jTable3,CreditlinesSize);
            setCheckInSaleTableData(jTable3,Creditlines);
            String Tendor =jTendor.getSelectedItem().toString();
    }
    private void jcustomerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcustomerComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcustomerComboBoxActionPerformed

    private void jTextactualpayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextactualpayActionPerformed
         String tendor=jTendor.getSelectedItem().toString();
          if(tendor.equals(null)||tendor.isEmpty())
    {
   showMsg("Please Enter The Valid Tendor Type");
    }
          else{      
        String actual=jTextactualpay.getText().toString();
        actualpay = Double.parseDouble(actual);
        for (int col = 0; col <  CreditlinesSize; col++) {
          Double outstandingval=Creditlines.get(col).getCreditamount();
           if(actualpay>Total)
          {
              showMsg("Pay Should Not Be More Than Total Bill");
              break;
          }
           if(actualpay==0)
          {
               jTable3.setValueAt(0 , col, 4);
              break;
          }
            if(col==0 && actualpay<outstandingval)
          {
              jTable3.setValueAt(actualpay, col, 4);
              actualpay=outstandingval-actualpay;
             break;
          }
               else if(actualpay>=outstandingval)
          {
             
             jTable3.setValueAt(outstandingval,col, 4);
           actualpay=actualpay-outstandingval;
          }
      else
            {
                
             jTable3.setValueAt((actualpay),col, 4); 
             break;
            }
            }
    
     
        
    }//GEN-LAST:event_jTextactualpayActionPerformed
           
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
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
 
    @Override
    public String getTitle() {
       return AppLocal.getIntString("Menu.creditSales");
    }

    @Override
    public void activate() throws BasicException {
        
        
      //  throw new UnsupportedOperationException("Not supported yet.");
         jChequeno.setVisible(false);
         jTextcheque.setVisible(false);
         jChequedate.setVisible(false); 
         jTextdate.setVisible(false); 
         m_jbtnpodate1.setVisible(false);
          jBank.setVisible(false);
         jTextbank.setVisible(false);
        TendorValue();
       populate();
       jTextactualpay.setText("");
 columnCInNames = new String[]{
           "Bill No.", "Bill Date & Time", "Bill Amount", "Out Standing Amount",  "Payment"
        };
          setDataTableModelAndHeader(jTable3,0);

        
         
                 
    }

    @Override
    public boolean deactivate() {
        return true;
    }

    @Override
    public JComponent getComponent() {
         return this;
    }


  
}
