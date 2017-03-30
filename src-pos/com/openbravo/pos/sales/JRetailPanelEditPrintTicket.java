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
//ivate
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.sales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.BillPromoRuleInfo;
import com.openbravo.pos.forms.BuyGetInfo;
import com.openbravo.pos.forms.BuyGetPriceInfo;
import com.openbravo.pos.forms.BuyGetQtyInfo;
import com.openbravo.pos.forms.CampaignIdInfo;
import com.openbravo.pos.forms.CustomerListInfo;
import com.openbravo.pos.forms.JPrincipalApp;
import com.openbravo.pos.forms.JRootApp;
import com.openbravo.pos.forms.PromoRuleIdInfo;
import com.openbravo.pos.forms.PromoRuleInfo;
import com.openbravo.pos.forms.StartPOS;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentInterface;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoCheque;
import com.openbravo.pos.payment.PaymentInfoFoodCoupon;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoVoucher;
import com.openbravo.pos.payment.PaymentInfoVoucherDetails;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurant;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.ticket.FindTicketsInfo;

import com.openbravo.pos.ticket.ProductInfoExt;

import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.RoundUtils;
import com.sysfore.pos.homedelivery.DeliveryBoyInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import com.sysfore.pos.hotelmanagement.BusinessTypeInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.print.PrintService;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 *
 * @author adrianromero
 */
public abstract class JRetailPanelEditPrintTicket extends JPanel implements JPanelView, BeanFactoryApp, RetailTicketsEditor {
   
    // Variable numerica
    private final static int NUMBERZERO = 0;
    private final static int NUMBERVALID = 1;
    private final static int NUMBER_INPUTZERO = 0;
    private final static int NUMBER_INPUTZERODEC = 1;
    private final static int NUMBER_INPUTINT = 2;
    private final static int NUMBER_INPUTDEC = 3; 
    private final static int NUMBER_PORZERO = 4; 
    private final static int NUMBER_PORZERODEC = 5; 
    private final static int NUMBER_PORINT = 6; 
    private final static int NUMBER_PORDEC = 7; 
    protected JRetailTicketLines m_ticketlines;

    private TicketParser m_TTP;
    public JPrincipalApp m_principalapp = null;
    protected static RetailTicketInfo m_oTicket;
    protected Object m_oTicketExt; 
    String[] args;
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
            
    private JRetailTicketsBag m_ticketsbag;
    
    private SentenceList senttax;
    private ListKeyed taxcollection;
    
    private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    
    private RetailTaxesLogic taxeslogic;
    private String editSaleBillId;
  //  protected JRetailPanelEditPrintButtons m_jbtnconfig;
    
    protected AppView m_App;
    protected DataLogicSystem dlSystem;
    protected DataLogicSales dlSales;
    protected DataLogicCustomers dlCustomers;
    private java.util.List<DeliveryBoyInfo> deliveryBoyLines;
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;
    public java.util.ArrayList<PromoRuleInfo> promoRuleList = null;
    public static java.util.ArrayList<BusinessServiceTaxInfo> serviceTaxList = null;
    public static java.util.ArrayList<BusinessServiceChargeInfo> serviceChargeList = null;
    protected PromoRuleInfo promoDetails;
    public java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList;
    public java.util.ArrayList<PromoRuleIdInfo> pdtRuleIdList;
    public java.util.ArrayList<CampaignIdInfo> campaignIdList;
    public java.util.ArrayList<CampaignIdInfo> pdtCampaignIdList;
    public java.util.ArrayList<BuyGetInfo> pdtBuyGetList;
    public java.util.ArrayList<BuyGetQtyInfo> pdtBuyGetQtyList;
    public java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList;
    public java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
    public java.util.ArrayList<BillPromoRuleInfo> billPromoRuleList;
    public java.util.ArrayList<CustomerListInfo> customerList;
    public java.util.ArrayList<CustomerListInfo> customerListDetails;
    public java.util.ArrayList<ProductInfoExt> productList;
    public java.util.ArrayList<ProductInfoExt> productListValue;
    public java.util.ArrayList<ProductInfoExt> productListDetails;
    double qty =0;
    int buttonPlus=1;
    public double productDiscount=0;
    String selectedProduct;
    private ArrayList<PromoRuleIdInfo> promoRuleLeastList;
    public BuyGetPriceInfo buyGet;
    public String itemChange;
    boolean action1Performed = false;
    boolean action2Performed = false;
    boolean action3Performed = false;
    boolean action4Performed = false;
    boolean action5Performed = false;
     boolean action6Performed = false;
    JPaymentInterface forpayment;
    PaymentInfoCash cash=null;
    PaymentInfoCard card=null;
    PaymentInfoCheque cheque=null;
     PaymentInfoVoucherDetails voucher=null;
    PaymentInfoFoodCoupon foodCoupon=null;
    public PaymentInfoList m_aPaymentInfo ;
    private boolean accepted;
    private JRootApp m_RootApp;
    private static JTextField cusName;
    private static JTextField cusPhoneNo;

    private static JTextField itemName;
    private final Vector<String> vCusName = new Vector<String>();
    private final Vector<String> vCusPhoneNo = new Vector<String>();
    private final Vector<String> vItemName = new Vector<String>();
    private boolean hide_flag = false;
    double totalAmount = 0;
    double totalBillValue;
    int typeId;
    double cashAmount = 0;
    double cardAmount = 0;
    double chequeAmount = 0;
    double voucherAmount = 0;
     double creditAmount = 0;
    double foodCouponAmount = 0;
     public DataLogicReceipts dlReceipts;
    private boolean printerStatus;
    private String editSale;
    private String homeDeliverySale;
    private String pdtId;
    protected kotInfo k_oInfo;
      private java.util.List<KotTicketListInfo> kotlist;
    private java.util.List<kotInfo> kotTicketlist;
    private java.util.List<kotPrintedInfo> kotPrintedlist;
    private Place m_PlaceCurrent;
     static double serviceChargeAmt;
    javax.swing.Timer timer;

        public final static int INTERVAL = 1000;
    /** Creates new form JTicketView */
    public JRetailPanelEditPrintTicket() {
        initComponents ();
      TextListener txtL;
       cusName = (JTextField) m_jCboCustName.getEditor().getEditorComponent();
       cusPhoneNo= (JTextField) m_jCboContactNo.getEditor().getEditorComponent();
       itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();
       m_jTxtItemCode.setFocusable(true);
       m_jTxtCustomerId.setFocusable(true);
       txtL = new TextListener();
       m_jCreditAllowed.setVisible(false);
       m_jCreditAllowed.setSelected(false);
       cusPhoneNo.addFocusListener(txtL);
       cusName.addFocusListener(txtL);

       itemName.addFocusListener(txtL);
       Action doMorething = new AbstractAction() {

       public void actionPerformed(ActionEvent e) {
            m_jPrice.setEnabled(true);
            m_jPrice.setFocusable(true);
            m_jKeyFactory.setFocusable(true);
            m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });

            }
        };

        InputMap imap = m_jPrice.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK), "doMorething");
        m_jPrice.getActionMap().put("doMorething",doMorething);

    //   Action doLastBill = new AbstractAction() {
    //   public void actionPerformed(ActionEvent e) {
     //        m_jLastBillActionPerformed(e);
     //        stateToZero();
     //       }
     //   };

     //   InputMap imapLastBill = m_jLastBill.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
      //  imapLastBill.put(KeyStroke.getKeyStroke("F7"), "doLastBill");
      //  m_jLastBill.getActionMap().put("doLastBill",doLastBill);

               Action cashNoBill = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
             //  cashPayment(3);
              //  setPrinterOn();
              
            }
        };
       InputMap imapCashNoBill = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        imapCashNoBill.put(KeyStroke.getKeyStroke("F1"), "cashNoBill");
        m_jAction.getActionMap().put("cashNoBill",
                cashNoBill);
        // };
         Action cashReceipt = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

            //   cashPayment(2);
            }
        };
         InputMap imapCashReceipt = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCashReceipt.put(KeyStroke.getKeyStroke("F2"), "cashReceipt");
        m_jAction.getActionMap().put("cashReceipt",
                cashReceipt);


        Action docashPrint = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
               cashPayment(1);
            }
        };

        InputMap imapCashPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCashPrint.put(KeyStroke.getKeyStroke("F3"), "docashPrint");
        m_jAction.getActionMap().put("docashPrint",
                docashPrint);
        Action doCardnobill = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
          //m_jCloseShiftActionPerformed(e);

            cardPayment(1);
            }
        };

        InputMap imapCardnoBill = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCardnoBill.put(KeyStroke.getKeyStroke("F4"), "doCardnobill");
        m_jAction.getActionMap().put("doCardnobill",doCardnobill);

         Action doNonChargablePrint = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    closeTicketNonChargable(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        InputMap imapNonChargablePrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapNonChargablePrint.put(KeyStroke.getKeyStroke("F7"), "doNonChargablePrint");
        m_jAction.getActionMap().put("doNonChargablePrint",
                doNonChargablePrint);

        Action doCreditreceipt = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
//            m_jCreditAmount.setText(Double.toString(m_oTicket.getTotal()));
//
//            try {
//                    closeCreditTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
//                } catch (BasicException ex) {
//                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        };
         InputMap imapCreditReceipt = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCreditReceipt.put(KeyStroke.getKeyStroke("F5"), "doCreditreceipt");
        m_jAction.getActionMap().put("doCreditreceipt",doCreditreceipt);

        Action doHomeDeliveryNotPaid = new AbstractAction() {
          public void actionPerformed(ActionEvent e) {
           // m_jHomeDelivery.setSelected(true);
              if(getEditSale()=="Edit"){
            //  getServiceCharge("Y");
              printPartialTotals();
                try {
                    closeTicketHomeDelivery(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          }
        };

        InputMap imapCardPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCardPrint.put(KeyStroke.getKeyStroke("F6"), "doHomeDeliveryNotPaid");
        m_jAction.getActionMap().put("doHomeDeliveryNotPaid",doHomeDeliveryNotPaid);



 Action doLogout = new AbstractAction() {

       public void actionPerformed(ActionEvent e) {
               m_jLogoutActionPerformed(e);

            }
        };

        InputMap imapLog = m_jLogout.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapLog.put(KeyStroke.getKeyStroke("F12"), "doLogout");
        m_jLogout.getActionMap().put("doLogout",
                doLogout);


       Action doupArrow = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {
                m_jUpActionPerformed(e);
            }
        };
        InputMap imapUpArrow = m_jUp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapUpArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0), "doupArrow");
        m_jUp.getActionMap().put("doupArrow",
                doupArrow);

       Action doCustomer = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {

              m_jTxtCustomerId.setFocusable(true);
          
            cusPhoneNo.setFocusable(true);
            cusName.setFocusable(true);
            stateToBarcode();
            }
        };

        InputMap imapCustomer= m_jTxtCustomerId.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCustomer.put(KeyStroke.getKeyStroke("F8"), "doCustomer");
        m_jTxtCustomerId.getActionMap().put("doCustomer",
                doCustomer);



     Action doItem = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {
           itemName.setFocusable(true);
           m_jTxtItemCode.setFocusable(true);
            stateToItem();

            }
        };

        InputMap imapItem= m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapItem.put(KeyStroke.getKeyStroke("F9"), "doItem");
        m_jAction.getActionMap().put("doItem",
                doItem);

        Action doHomeDelivery = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {
            m_jHomeDelivery.setFocusable(true);
            m_jHomeDelivery.setSelected(true);
            m_jHomeDeliveryActionPerformed(e);
            m_jTxtAdvance.setFocusable(true);
            stateToHomeDelivery();
            }
        };

        InputMap imapHomeDelivery= m_jHomeDelivery.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapHomeDelivery.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_MASK), "doHomeDelivery");
        m_jHomeDelivery.getActionMap().put("doHomeDelivery",
                doHomeDelivery);


     Action doPayment = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {
           m_jCash.setFocusable(true);
           m_jCard.setFocusable(true);
           m_jCheque.setFocusable(true);
           m_jtxtChequeNo.setFocusable(true);
           m_jFoodCoupon.setFocusable(true);
           m_jVoucher.setFocusable(true);
           m_jCreditAmount.setFocusable(true);
            stateToPayment();
            }
        };

        InputMap imapPayment= m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapPayment.put(KeyStroke.getKeyStroke("F11"), "doPayment");
        m_jAction.getActionMap().put("doPayment",
                doPayment);

  Action doDownpArrow = new AbstractAction() {
       public void actionPerformed(ActionEvent e) {
               m_jDownActionPerformed(e);

            }
        };

        InputMap imapArrow = m_jDown.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0), "doDownpArrow");
        m_jDown.getActionMap().put("doDownpArrow",
                doDownpArrow);



      itemName.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

         
       if(!itemName.getText().equals("") || !itemName.getText().equals(null)){

        incProductByItemDetails(pdtId);
      //  itemName.setText("");
          m_jCboItemName.setSelectedIndex(-1);

        ArrayList<String> itemCode = new ArrayList<String>();
        ArrayList<String> itemName1 = new ArrayList<String>();

        vItemName.removeAllElements();
        try {
            productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productListDetails) {
            itemCode.add(product.getItemCode());
            itemName1.add(product.getName());
        }

          String [] productName = itemName1.toArray(new String[itemName1.size()]);
          
          for(int i=0;i<itemName1.size();i++){
                  vItemName.addElement(productName[i]);
          }
          itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();

      }else{
            pdtId="";
            ArrayList<String> itemCode = new ArrayList<String>();
            ArrayList<String> itemName1 = new ArrayList<String>();

            vItemName.removeAllElements();
            try {
                productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (ProductInfoExt product : productListDetails) {
                itemCode.add(product.getItemCode());
                itemName1.add(product.getName());
            }

              String [] productName = itemName1.toArray(new String[itemName1.size()]);
               for(int i=0;i<itemName1.size();i++){
                      vItemName.addElement(productName[i]);
              }
          itemName= (JTextField) m_jCboItemName.getEditor().getEditorComponent();

      }
      }

    });

    }
   
    public void setPrinterOn(){
        Boolean status;
        if(getPrinterStatus()== true){
            setPrinterStatus(false);
            jLblPrinterStatus.setText("Printer Off");
        }else{
            setPrinterStatus(true);
            jLblPrinterStatus.setText("Printer On");
        }
    }
    public boolean getPrinterStatus(){
        return printerStatus;
    }
    public void setPrinterStatus(Boolean printerStatus){
        this.printerStatus = printerStatus;
    }
    public void cashPayment(int print){
        int inventoyCount = 0;
        try {
            inventoyCount = dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
         if(inventoyCount==1){
           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
         }else{
           m_aPaymentInfo = new PaymentInfoList();
                     cash= new PaymentInfoCash(m_oTicket.getTotal(),m_oTicket.getTotal(),0);
                    if (cash != null) {
                      m_aPaymentInfo.add(cash);

                   }
                     m_oTicket.setPrintStatus(print);
                try {
                    closeTicketWithButton(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                    activate();
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
         }
    }
    public void cardPayment(int print){
        int inventoyCount = 0;
        try {
            inventoyCount = dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
         if(inventoyCount==1){
           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
         }else{
           m_aPaymentInfo = new PaymentInfoList();
               //if(!m_jCard.getText().equals("")){
                  card = new PaymentInfoCard(m_oTicket.getTotal(),m_oTicket.getTotal());
                    if (card != null) {
                      m_aPaymentInfo.add(card);

                 //  }
               }
                     m_oTicket.setPrintStatus(print);
                try {
                    closeTicketWithButton(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                   // activate();
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }  }
    }
     private void customerFocus() {
       m_jTxtCustomerId.setFocusable(true);
       cusName.setFocusable(true);
       cusPhoneNo.setFocusable(true);
       m_jTxtItemCode.setFocusable(true);
       itemName.setFocusable(true);
       m_jCash.setFocusable(true);
       m_jCard.setFocusable(true);
       m_jCheque.setFocusable(true);
       m_jtxtChequeNo.setFocusable(true);
       m_jPrice.setFocusable(true);
       m_jPrice.setEnabled(true);
       m_jCreditAmount.setFocusable(true);

            }

    public  void setEditSale(String editSale) {
        this.editSale = editSale;
    }
    public String getEditSale(){
        return editSale;
    }
//    public void setHomeDeliverySale(String homeDeliverySale) {
//        this.homeDeliverySale = homeDeliverySale;
//    }
//    public String getHomeDeliverySale(){
//        return homeDeliverySale;
//    }


   class TextListener implements FocusListener
    {
        public void focusLost(FocusEvent e){
            if(e.getSource() == cusName){
                 cusName.setFocusable(true);
              

            } else if(e.getSource() == cusPhoneNo){

            }
            else if(e.getSource() == itemName){

            }

        } // close focusLost()
        public void focusGained(FocusEvent e)
        {
            final String type;
            if(e.getSource() == cusName){
               type = "c";
                 //setModel(new DefaultComboBoxModel(vCusName), "");
               cusName.addKeyListener(new KeyAdapter() {
               public void keyTyped(KeyEvent e) {
               EventQueue.invokeLater(new Runnable() {
               public void run() {
               String text = cusName.getText();
               if(text.length()==0) {
                    m_jCboCustName.hidePopup();
                    setModel(new DefaultComboBoxModel(vCusName), "",type);
                }else{
                       typeId = 1;
                    Vector<String> vCusName = new Vector<String>();
                    ArrayList<String> cusNames = new ArrayList<String>();
                    ArrayList<String> cusPhoneNo = new ArrayList<String>();
                    ArrayList<String> cusId = new ArrayList<String>();
                    try {
                        customerListDetails = (ArrayList<CustomerListInfo>) dlSales.getCustomerListName(text);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (CustomerListInfo cus : customerListDetails) {
                        cusNames.add(cus.getName());
                        cusPhoneNo.add((cus.getPhoneNo()));
                        cusId.add((cus.getCustomerId()));
                    }
                    String [] customerNames = cusNames.toArray(new String[cusNames.size()]);
                      for(int i=0;i<cusNames.size();i++){
                              vCusName.addElement(customerNames[i]);
                      }
                    DefaultComboBoxModel m = getCustNameSuggestedModel(vCusName, text);
                    if(m.getSize()==0 || hide_flag) {
                        m_jCboCustName.hidePopup();
                        hide_flag = false;
                    }else{
                        setModel(m, text,type);
                        m_jCboCustName.showPopup();
                    }
                }
            }
        });
        }
           public void keyPressed(KeyEvent e) {
           String text = cusName.getText();
             int code = e.getKeyCode();

             if(code==KeyEvent.VK_ENTER) {
                if(!vCusName.contains(text)) {
                    vCusName.addElement(text);
                    Collections.sort(vCusName);
                    setModel(getCustNameSuggestedModel(vCusName, text), text,type);
                   
                }
                hide_flag = true;
            }else if(code==KeyEvent.VK_ESCAPE) {
                hide_flag = true;
            }else if(code==KeyEvent.VK_RIGHT) {
                for(int i=0;i<vCusName.size();i++) {
                    String str = vCusName.elementAt(i);
                    if(str.startsWith(text)) {
                        m_jCboCustName.setSelectedIndex(-1);
                        cusName.setText(str);
                        return;
                    }
                }
            }}

          });
      }
     else if(e.getSource() == cusPhoneNo)
      {
       type = "n";
      cusPhoneNo.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
      EventQueue.invokeLater(new Runnable() {
            public void run() {
                String text = cusPhoneNo.getText();
                if(text.length()==0) {
                    m_jCboContactNo.hidePopup();
                    setModel(new DefaultComboBoxModel(vCusPhoneNo), "",type);
                }else{
                    DefaultComboBoxModel m = getContactNoSuggestedModel(vCusPhoneNo, text);
                    if(m.getSize()==0 || hide_flag) {
                        m_jCboContactNo.hidePopup();
                        hide_flag = false;
                    }else{
                        setModel(m, text, type);
                        m_jCboContactNo.showPopup();
                    }
                }
            }
        });
      }

       public void keyPressed(KeyEvent e) {
       String text = cusPhoneNo.getText();
         int code = e.getKeyCode();
             if(code==KeyEvent.VK_ENTER) {
            if(!vCusPhoneNo.contains(text)) {
                vCusPhoneNo.addElement(text);
                Collections.sort(vCusPhoneNo);
                setModel(getContactNoSuggestedModel(vCusPhoneNo, text), text,type);
            }
            hide_flag = true;
        }else if(code==KeyEvent.VK_ESCAPE) {
            hide_flag = true;
        }else if(code==KeyEvent.VK_RIGHT) {
            for(int i=0;i<vCusPhoneNo.size();i++) {
                String str = vCusPhoneNo.elementAt(i);
                if(str.startsWith(text)) {
                   m_jCboContactNo.setSelectedIndex(-1);
                    cusPhoneNo.setText(str);
                    return;
                }
            }
        }
      }
      });

      }

      else if(e.getSource() == itemName){
          type = "m";
          itemName.addKeyListener(new KeyAdapter() {
          public void keyTyped(KeyEvent e) {
          EventQueue.invokeLater(new Runnable() {
                public void run() {
                    String text = itemName.getText();
                    if(text.length()==0) {
                        m_jCboItemName.hidePopup();
                        setModel(new DefaultComboBoxModel(vItemName), "",type);
                    }else{
                        typeId = 2;
                        ArrayList<String> itemCode = new ArrayList<String>();
                        ArrayList<String> itemName = new ArrayList<String>();

                        Vector<String> vItemName = new Vector<String>();
                        try {
                            productList = (ArrayList<ProductInfoExt>) dlSales.getProductName(text);
                        } catch (BasicException ex) {
                            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }

                          for (ProductInfoExt product : productList) {
                            itemCode.add(product.getItemCode());
                            itemName.add(product.getName());
                        }

                          String [] productName = itemName.toArray(new String[itemName.size()]);
                          for(int i=0;i<itemName.size();i++){
                                  vItemName.addElement(productName[i]);
                          }
                        DefaultComboBoxModel m = getItemSuggestedModel(vItemName, text);
                        if(m.getSize()==0 || hide_flag) {
                            m_jCboItemName.hidePopup();
                            hide_flag = false;
                        }else{
                            setModel(m, text,type);
                            m_jCboItemName.showPopup();
                        }
                    }
                }
            });
            }
       public void keyPressed(KeyEvent e) {
       String text = itemName.getText();
         int code = e.getKeyCode();
             if(code==KeyEvent.VK_ENTER) {
            if(!vItemName.contains(text)) {
                vItemName.addElement(text);
                Collections.sort(vItemName);
                setModel(getItemSuggestedModel(vItemName, text), text,type);
            }
            hide_flag = true;
        }else if(code==KeyEvent.VK_ESCAPE) {
            hide_flag = true;
        }else if(code==KeyEvent.VK_RIGHT) {
            for(int i=0;i<vItemName.size();i++) {
                String str = vItemName.elementAt(i);
                if(str.startsWith(text)) {
                    m_jCboItemName.setSelectedIndex(-1);
                    itemName.setText(str);
                    return;
                }
            }
        }
      }
      });

    }
  }

} // close TextListener, inner class

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
      
        // borramos el boton de bascula si no hay bascula conectada
        if (!m_App.getDeviceScale().existsScale()) {
            m_jbtnScale.setVisible(false);
        }
         if(m_App.getProperties().getProperty("machine.ticketsbag").equals("restaurant")){
            m_jbtnPrintBill.setVisible(true);
        }else{
             m_jbtnPrintBill.setVisible(false);
            // jPanel1.setPreferredSize(new java.awt.Dimension(282, 47));
        }
        customerFocus();
        //setPrinterStatus(false);
        m_jPor.setVisible(false);
        m_ticketsbag = getJTicketsPrintBag();
     //   m_oTicket.setSplitValue("");
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);
       
        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JRetailTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);
        
        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);
               
        // Los botones configurables...
     //   m_jbtnconfig = new JRetailPanelEditPrintButtons("Ticket.Buttons", this);
     //   m_jButtonsExt.add(m_jbtnconfig);           
       
        // El panel de los productos o de las lineas...        
//        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);
       // catcontainer.setVisible(true);
        m_jCalculatePromotion.setVisible(false);
        // El modelo de impuestos
        senttax = dlSales.getRetailTaxList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        
        taxcategoriesmodel = new ComboBoxValModel();    

        // ponemos a cero el estado
        stateToZero();  
      


       m_jCash.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
        m_jCard.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
        m_jCheque.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
    m_jFoodCoupon.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
    m_jVoucher.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
     m_jCreditAmount.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void removeUpdate(DocumentEvent e) {
           setTenderAmount();
         }
         public void insertUpdate(DocumentEvent e) {
           setTenderAmount();
         }


    });
    }
    public void custItemLoad(){
         loadCusDetails();
        loadItemDetails();
        // inicializamos
        m_oTicket = null;
        m_oTicketExt = null;

        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusId = new ArrayList<String>();
      //  ArrayList<String> cusName1 = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusId.add((cus.getCustomerId()));
          //  cusName1.add(cus.getName());
        }
        vCusName.removeAllElements();
        String [] customerNames = cusNames.toArray(new String[cusNames.size()]);
          for(int i=0;i<cusNames.size();i++){
                  vCusName.addElement(customerNames[i]);
          }
        vCusPhoneNo.removeAllElements();
          String [] customerPhoneNo = cusPhoneNo.toArray(new String[cusPhoneNo.size()]);
          for(int i=0;i<cusPhoneNo.size();i++){
                  vCusPhoneNo.addElement(customerPhoneNo[i]);
          }


        ArrayList<String> itemCode = new ArrayList<String>();
        ArrayList<String> itemName = new ArrayList<String>();

        vItemName.removeAllElements();
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

          for (ProductInfoExt product : productList) {
            itemCode.add(product.getItemCode());
            itemName.add(product.getName());
        }
//          }
         
          String [] productName = itemName.toArray(new String[itemName.size()]);
          
          for(int i=0;i<itemName.size();i++){
                  vItemName.addElement(productName[i]);
          }
    }
  public void setTenderAmount() {

     if (m_jCash.getText()!=null || m_jCard.getText()!=null || m_jCheque.getText()!=null || m_jFoodCoupon.getText()!=null || m_jVoucher.getText()!=null || m_jCreditAmount.getText()!=null){

         Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
         Pattern pNum1 = Pattern.compile(".");
         if(m_jCash.getText().equals("")){
             cashAmount = 0;
        }else{
             try{
                cashAmount =Double.parseDouble(m_jCash.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid cash amount");
               
             }
         }
        
         if(m_jCard.getText().equals("")){
             cardAmount = 0;
         }else{
              try{
                cardAmount = Double.parseDouble(m_jCard.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid card amount");
             }
           
         }
         if(m_jCheque.getText().equals("")){
             chequeAmount = 0;
         }else{
              try{
                chequeAmount = Double.parseDouble(m_jCheque.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid cheque amount");
             }
            
         }
         if(m_jFoodCoupon.getText().equals("")){
             foodCouponAmount = 0;
         }else{
              try{
                foodCouponAmount = Double.parseDouble(m_jFoodCoupon.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid food coupon amount");
             }

         }
         if(m_jVoucher.getText().equals("")){
             voucherAmount = 0;
         }else{
              try{
                voucherAmount = Double.parseDouble(m_jVoucher.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid voucher amount");
             }

         }
        if(m_jCreditAmount.getText().equals("")){
             creditAmount = 0;
         }else{
              try{
                creditAmount = Double.parseDouble(m_jCreditAmount.getText());
             }catch (NumberFormatException ex){
                 showMessage(this, "Please enter the valid credit amount");
             }

         }
        
         totalAmount = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount;
         totalBillValue = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount+creditAmount;
         m_jTxtTotalPaid.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(totalAmount))));
         try{
          if(m_oTicket.getTotal()< totalBillValue){
              double change = totalBillValue - Math.ceil(m_oTicket.getTotal());
              m_jTxtChange.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(change))));
          }else{
             m_jTxtChange.setText("");
          }
         }catch(NullPointerException ex){
             
         }

     }
  }

       private void setModel(DefaultComboBoxModel mdl, String str, String type) {
           if(type=="c"){
                m_jCboCustName.setModel(mdl);
                cusName.setText(str);
           }else if(type=="n"){
                m_jCboContactNo.setModel(mdl);
                cusPhoneNo.setText(str);
           }else{
                m_jCboItemName.setModel(mdl);
                itemName.setText(str);
           }
    }
    private static DefaultComboBoxModel getSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {    
           // if(s.startsWith(text))
                m.addElement(s);
        }
        return m;
    }
     private static DefaultComboBoxModel getCustNameSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {

           // if(s.startsWith(text))
            String nameLength = cusName.getText();
           if(nameLength.length()>2){
                m.addElement(s);
            }
        }
        return m;
    }
     private static DefaultComboBoxModel getContactNoSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {
            String phoneLength = null;
           
            phoneLength = cusPhoneNo.getText();
         //    if(s.startsWith(text))
            if(phoneLength.length()>5){
                if(s.startsWith(text))
                m.addElement(s);
            }
        }
        return m;

    }
       private static DefaultComboBoxModel getItemSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {
            String itemLength = null;

            itemLength = itemName.getText();
            // if(s.startsWith(text))
            if(itemLength.length()>2){
                 //if(s.startsWith(text))
                m.addElement(s);
            }
        }
        return m;

    }

    public void paymentDetail(){
    m_aPaymentInfo = new PaymentInfoList();

      if(!m_jCash.getText().equals("")){
          totalAmount = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount+creditAmount;
          double change = totalAmount - m_oTicket.getTotal();
          cash= new PaymentInfoCash(m_oTicket.getTotal(),(Double.parseDouble(m_jCash.getText())),change);
            if (cash != null) {
              m_aPaymentInfo.add(cash);

           }
      }
      if(!m_jCard.getText().equals("")){
          card = new PaymentInfoCard(m_oTicket.getTotal(),(Double.parseDouble(m_jCard.getText())));
            if (card != null) {
              m_aPaymentInfo.add(card);

           }
       }

       if(!m_jCheque.getText().equals("")){
          cheque= new PaymentInfoCheque(m_oTicket.getTotal(),(Double.parseDouble(m_jCheque.getText())),null);
            if (cheque != null) {
              m_aPaymentInfo.add(cheque);

           }
        }
    if(!m_jFoodCoupon.getText().equals("")){
          foodCoupon= new PaymentInfoFoodCoupon(m_oTicket.getTotal(),(Double.parseDouble(m_jFoodCoupon.getText())));
            if (foodCoupon != null) {
              m_aPaymentInfo.add(foodCoupon);
             // accepted = true;

           }
        }
    if(!m_jVoucher.getText().equals("")){
          voucher= new PaymentInfoVoucherDetails(m_oTicket.getTotal(),(Double.parseDouble(m_jVoucher.getText())));
            if (voucher != null) {
              m_aPaymentInfo.add(voucher);
           }
        }
    }
    
     private void loadCusDetails(){
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();


        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
      
     
        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }
        m_jCboCustName.setModel(new ComboBoxValModel(cusNames));
        m_jCboContactNo.setModel(new ComboBoxValModel(cusPhoneNo));


    }
     private void loadCustomerDetails(){
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }

    }
    private void loadItemDetails(){
        ArrayList<String> itemName = new ArrayList<String>();

        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

          for (ProductInfoExt product : productList) {
            itemName.add(product.getName());
        }
        m_jCboItemName.setModel(new ComboBoxValModel(itemName));
    }
      private void loadItemList(){
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public Object getBean() {
        return this;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public void activate() {
    
      custItemLoad();

       jLblPrinterStatus.setText("");
     //  m_oTicket.setSplitValue("");
       jPanel8.setVisible(false);
           m_jHomeDelivery.setVisible(false);

        m_jDeliveryBoy.removeAllItems();
        populateDeliveryBoy();
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this); 
        paymentdialogrefund.init(m_App);
        jLabel10.setVisible(false);
        m_jDeliveryBoy.setVisible(false);
        jLabel11.setVisible(false);
        m_jTxtAdvance.setVisible(false);
        m_jCod.setVisible(false);
        m_jCreditAllowed.setVisible(false);
        m_jCreditAmount.setText("0.00");
         m_jHomeDelivery.setSelected(false);
        // impuestos incluidos seleccionado ?
       // m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);
        
        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
        m_jTax.setModel(taxcategoriesmodel);

////        String taxesid = m_jbtnconfig.getProperty("taxcategoryid");
//        if (taxesid == null) {
//            if (m_jTax.getItemCount() > 0) {
//                m_jTax.setSelectedIndex(0);
//            }
//        } else {
//            taxcategoriesmodel.setSelectedKey(taxesid);
//        }              
                
        taxeslogic = new RetailTaxesLogic(taxlist,m_App);
        String businessTypeId = null;

      
          //   getServiceCharge("Order Taking");

       
       
        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }
        
        // Authorization for buttons
//        btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));

   //     m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());  
               
        m_ticketsbag.activate();
        String day = getWeekDay();
        java.util.ArrayList<String> campaignId = new ArrayList<String>();
        try {
            campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        promoDetails = new PromoRuleInfo();
        if(campaignIdList.size()!=0){
        for(int i=0;i<campaignIdList.size();i++){
         campaignId.add("'"+campaignIdList.get(i).getcampaignId()+"'");
     }
       
         StringBuilder b = new StringBuilder();
         Iterator<?> it = campaignId.iterator();
         while (it.hasNext()) {
         b.append(it.next());
         if (it.hasNext()) {
            b.append(',');
          }
        }
        String Id = b.toString();
        if(campaignIdList!=null){
                try {
                    promoRuleIdList = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
        
         
        }
        
        }
        
    }
    public void getServiceCharge(String isHomeDelivery){
        String businessTypeId = null;
        int businessTypeCount = 0;
        try {
            businessTypeCount = dlSales.getBusinessTypeCount(isHomeDelivery);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(businessTypeCount==1){
         try {
                businessTypeId = dlSales.getBusinessTypeId(isHomeDelivery);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serviceTaxList = (ArrayList<BusinessServiceTaxInfo>) dlSales.getBusinessServiceTax(businessTypeId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceChargeList = (ArrayList<BusinessServiceChargeInfo>) dlSales.getBusinessServiceCharge(businessTypeId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public boolean deactivate() {

        return m_ticketsbag.deactivate();
    }
    
    protected abstract JRetailTicketsBag getJTicketsPrintBag();
    protected abstract Component getSouthComponent();
    protected abstract void resetSouthComponent();
     
    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt) {

        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
          m_jPanTotals1.setVisible(false);
          // catcontainer.setVisible(true);
        if (m_oTicket != null) {
           
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.

            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
//                line.setRetailEditPanel(this);

            }

        }
        if(m_jCboCustName.getSelectedItem()!=null){
            m_jCboCustName.setSelectedItem("");
            m_jCboContactNo.setSelectedItem("");
            m_jTxtCustomerId.setText("");
            m_jCboContactNo.setSelectedItem("");
        }
        if(m_jTxtItemCode.getText()!=null){
         //   m_jCboItemCode.setSelectedItem("");
            m_jTxtItemCode.setText("");
            m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
        }
        m_jCash.setText("");
        m_jCheque.setText("");
        m_jCard.setText("");
        m_jtxtChequeNo.setText("");
        m_jFoodCoupon.setText("");
        m_jVoucher.setText("");
        
//        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");
        
        refreshTicket();
        
    }

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt, String editBillId) {
        this.editSaleBillId = editBillId;
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        if(getEditSale()=="Edit"){
           m_jLastBill.setVisible(false);
           m_jBtnKot.setVisible(false);
            m_jbtnPrintBill.setVisible(false);
       }
        if (m_oTicket != null) {
            
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.

            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
          //      line.setRetailEditPanel(this);

            }

        }
        if(m_jCboCustName.getSelectedItem()!=null){
            m_jCboCustName.setSelectedItem("");
            m_jCboContactNo.setSelectedItem("");
            m_jTxtCustomerId.setText("");
            m_jCboContactNo.setSelectedItem("");
        }
        if(m_jTxtItemCode.getText()!=null){
         //   m_jCboItemCode.setSelectedItem("");
            m_jTxtItemCode.setText("");
             m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
        }
        m_jCash.setText("");
        m_jCheque.setText("");
        m_jCard.setText("");
        m_jtxtChequeNo.setText("");
        m_jFoodCoupon.setText("");
        m_jVoucher.setText("");

        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");

        refreshTicket();
    }
    
    public RetailTicketInfo getActiveTicket() {
        return m_oTicket;
    }
    private void setDiscountButtonEnable() {
        int count = m_oTicket.getLinesCount();
        if (count == 0) {
            m_oTicket.setRate("0");
            m_jBtnDiscount.setEnabled(false);
        } else {
            m_jBtnDiscount.setEnabled(true);
        }
    }
    public void refreshTicket() {
     //   System.out.println("refreshTicket");
        CardLayout cl = (CardLayout)(getLayout());
        customerFocus();
        if (m_oTicket == null) {        
            m_ticketlines.clearTicketLines();
           
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null); 
        
            stateToZero();
            
            // Muestro el panel de nulos.
            cl.show(this, "null");  
            resetSouthComponent();

        } else {
            if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {

            }


             String name=m_oTicket.getName(m_oTicketExt);
             if(name.contains("-")){

                String customer[]= null;
                customer =name.split("-");
                cusName.setText(customer[0]);
            }else{
                  cusName.setText("");
            }
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();
            
            // Muestro el panel de tickets.
            cl.show(this, "ticket");
            resetSouthComponent();
            
            // activo el tecleador...
            m_jKeyFactory.setText(null);       
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
           m_jLblCurrentDate.setText(getDateForMe().toString());
           m_jLblUserInfo.setText("    LOGGED IN USER : "+(m_oTicket.getUser()).getName());
           m_jLblTime.setText(getTime().toString()+"        ");
        }
    }
    public String getDateForMe() {
     SimpleDateFormat sdf=new SimpleDateFormat("MMM dd,yyyy");
     Date m_dDate = new Date();
     StringBuffer strb=new StringBuffer();
     strb.append("DATE: ");
     return strb.append(sdf.format(m_dDate).toString()).toString();
    }
    public String getTime() {
     SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
     Date m_dDate = new Date();
     StringBuffer strb=new StringBuffer();
     strb.append("TIME: ");
     return strb.append(sdf.format(m_dDate).toString()).toString();
    }

    public void printPartialTotals(){

      //  setDiscountButtonEnable();
        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount.setText(null);
//          m_jBillDiscount.setText(null);
             m_oTicket.setBillDiscount(0);
             m_jServiceCharge.setText(null);
        } else {
             calculateServiceCharge();
             m_jSubtotalEuros.setText(m_oTicket.printSubTotalValue());
             m_jTotalEuros.setText(m_oTicket.printTotal());
             m_jTaxesEuros.setText(m_oTicket.printTax());
             m_jDiscount.setText(m_oTicket.printDiscount());
             m_jServiceCharge.setText(m_oTicket.printServiceCharge());
              
           }
            
     
        setTenderAmount();
    }
//        public void setTime(final RetailTicketInfo m_oTicket){
//
//            System.out.println("setTime");
//         timer = new Timer(INTERVAL, new ActionListener() {
//        public void actionPerformed(ActionEvent evt) {
//
//           //Refresh the panel
//               jPanel12.revalidate();
//
//               if (m_oTicket.getLinesCount()!=0) {
//                timer.stop();
//               }
//            }
//        });
//
//        timer.start();
//        }

public void calculateServiceCharge(){
         
        //    System.out.println("print-0---"+serviceTaxList.get(0).getRate()+"---"+serviceTaxList.size()+"--"+serviceChargeList.get(0).getRate()+"--"+m_oTicket.getTotal());
         if(getEditSale()=="Edit"){
            int hdCount=0;
            try {
                hdCount = dlSales.getHomeDeliveryCount(m_oTicket.getDocumentNo());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(hdCount==1){
                 getServiceCharge("Home Delivery");
            }else{
                 getServiceCharge("Order Taking");
            }
        }else{
              getServiceCharge("Order Taking");
        }
            if(serviceChargeList.size()!=0){
                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
                     serviceChargeAmt = m_oTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
                }else{
                  serviceChargeAmt = m_oTicket.getSubTotal()*serviceChargeList.get(0).getRate();
                }
                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);

                  m_oTicket.setServiceChargeId(serviceChargeList.get(0).getId());
                
                  m_oTicket.setServiceCharge(serviceChargeAmt);
                  m_oTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
                  if(serviceTaxList.size()!=0){
                      m_oTicket.setServiceTaxId(serviceTaxList.get(0).getId());
                      m_oTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
                      double serviceTax = (m_oTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
                      System.out.println("serviceTaxAmt--"+serviceChargeAmt +"--"+serviceTax);
                      m_oTicket.setServiceTax(serviceTax);
                  }
            }
}

    private void paintTicketLine(int index, RetailTicketLineInfo oLine){
        
        if (executeEventAndRefresh("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {

            m_oTicket.setLine(index, oLine);
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
          
            visorTicketLine(oLine); // Y al visor tambien...
            printPartialTotals();   
            stateToZero();  

            // event receipt
            executeEventAndRefresh("ticket.change");
        }
   }

    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) {   

        System.out.println("addticketline");
        TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(),"N");       
        // addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this,tax, 0,oProduct.getName(),(java.util.Properties) (oProduct.getProperties().clone())));

    }
    
    public void addTicketLine(RetailTicketLineInfo oLine) {

        if (executeEventAndRefresh("ticket.addline", new ScriptArg("line", oLine)) == null) {
        
            if (oLine.isProductCom()) {
                // Comentario entonces donde se pueda
                int i = m_ticketlines.getSelectedIndex();

                // me salto el primer producto normal...
                if (i >= 0 && !m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                // me salto todos los productos auxiliares...
                while (i >= 0 && i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

                if (i >= 0) {
                    m_oTicket.insertLine(i, oLine);
                    m_ticketlines.insertTicketLine(i, oLine); // Pintamos la linea en la vista...                 
                } else {
                    Toolkit.getDefaultToolkit().beep();                                   
                }
            } else {    
                // Producto normal, entonces al finalnewline.getMultiply() 
     
                m_oTicket.addLine(oLine);
                m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la vista...
               
            }
       //   m_oTicket.setRate("0");
       //   m_oTicket.setdAmt(0);
            visorTicketLine(oLine);
//            setTime(m_oTicket);
             m_oTicket.refreshEditTxtFields(1);
            printPartialTotals();   
            stateToZero();  

            // event receipt
            executeEventAndRefresh("ticket.change");
          //  m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
             m_jTxtItemCode.setText("");
             itemName.setText("");
             stateToItem();
        }
    }    
    
    private void removeTicketLine(int i){
       
        if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {
            
            if (m_oTicket.getLine(i).isProductCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);   
            } else {
                // Es un producto normal, lo borro.
                if(m_oTicket.getLine(i).getPromoType().equals("BuyGet") && m_oTicket.getLine(i).getIsCrossProduct().equals("Y") && m_oTicket.getLine(i).getPrice()!=0){
                    String crossProduct = null;
                    if(m_oTicket.getLine(i).getPromoRule().get(0).getIsSku().equals("Y")){
                       crossProduct = m_oTicket.getLine(i).getProductID();
                    }else{   
                        try {
                          crossProduct =  dlSales.getCrossPromoProduct(m_oTicket.getLine(i).getpromoId());

                        } catch (BasicException ex) {
                            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     
                    }

                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                    int index = m_oTicket.getProductIndex(crossProduct);
                    m_oTicket.removeLine(index);
                    m_ticketlines.removeTicketLine(index);

                }else{
                     m_oTicket.removeLine(i);
                     m_ticketlines.removeTicketLine(i);
                }
               
                // Y todos lo auxiliaries que hubiera debajo.
                while(i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i); 
                }
            }

            visorTicketLine(null); // borro el visor 
            printPartialTotals(); // pinto los totales parciales...                           
            stateToZero(); // Pongo a cero    

            // event receipt
            executeEventAndRefresh("ticket.change");
        }
    }
    
    private ProductInfoExt getInputProduct() {
        ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
        oProduct.setReference(null);
        oProduct.setCode(null);
        oProduct.setName("");
        oProduct.setTaxCategoryID(((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem()).getID()); 
        oProduct.setPriceSell(includeTaxes(oProduct.getTaxCategoryID(), getInputValue()));      
        return oProduct;
    }
    
    private double includeTaxes(String tcid, double dValue) {
        if (m_jaddtax.isSelected()) {
            TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getCustomer(),"N");
            double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);      
        } else {
            return dValue;
        }
    }
    
    private double getInputValue() {
        try {
            return Double.parseDouble(m_jPrice.getText());
        } catch (NumberFormatException e){
            return 0.0;
        }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(m_jPor.getText().substring(1));
        } catch (NumberFormatException e){
            return 1.0;
        } catch (StringIndexOutOfBoundsException e){
            return 1.0;
        }
    }
    
    public void stateToZero(){
        m_jPor.setText("");
        m_jPrice.setText("");
        m_sBarcode = new StringBuffer();

        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }
     public void stateToBarcode(){

        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jEnter.setFocusable(false);
        m_jNumberKeys.setFocusable(false);
        m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        m_jbtnScale.setFocusable(false);
        
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        m_jCash.setFocusable(false);
        m_jCard.setFocusable(false);
        m_jCheque.setFocusable(false);
        m_jtxtChequeNo.setFocusable(false);
        m_jFoodCoupon.setFocusable(false);
        m_jVoucher.setFocusable(false);
        m_jDeliveryBoy.setFocusable(false);
        m_jTxtAdvance.setFocusable(false);
        m_jCreditAmount.setFocusable(false);
        m_jHomeDelivery.setFocusable(false);
        m_jCod.setFocusable(false);
        m_jCreditAllowed.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        m_jSplitBtn.setFocusable(false);
           //   JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
    //    catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }
      public void stateToHomeDelivery(){
      cusName.setFocusable(false);
      m_jTxtCustomerId.setFocusable(false);
        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
        m_jNumberKeys.setFocusable(false);
        m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        m_jbtnScale.setFocusable(false);

        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        m_jCash.setFocusable(false);
        m_jCard.setFocusable(false);
        m_jCheque.setFocusable(false);
        m_jtxtChequeNo.setFocusable(false);
        m_jFoodCoupon.setFocusable(false);
        m_jVoucher.setFocusable(false);
       
        m_jCreditAmount.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
     //   catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);


    }
    public void stateToItem(){
        cusName.setFocusable(false);
        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
        m_jNumberKeys.setFocusable(false);
        m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        m_jbtnScale.setFocusable(false);
        m_jTxtCustomerId.setFocusable(false);
        m_jCash.setFocusable(false);
        m_jCard.setFocusable(false);
        m_jCheque.setFocusable(false);
        m_jtxtChequeNo.setFocusable(false);
        m_jFoodCoupon.setFocusable(false);
        m_jVoucher.setFocusable(false);
        m_jDeliveryBoy.setFocusable(false);
        m_jTxtAdvance.setFocusable(false);
        m_jCreditAmount.setFocusable(false);
        m_jHomeDelivery.setFocusable(false);
        m_jCod.setFocusable(false);
        m_jCreditAllowed.setFocusable(false);
//        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        m_jServiceCharge.setFocusable(false);
      //  catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jBtnKot.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
       // m_jSplitBtn.setFocusable(false);
   
    }
        public void stateToPay(){
        cusName.setFocusable(false);
        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
        m_jNumberKeys.setFocusable(false);
        m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        m_jbtnScale.setFocusable(false);
        m_jTxtCustomerId.setFocusable(false);
        m_jCash.setFocusable(false);
        m_jCard.setFocusable(false);
        m_jCheque.setFocusable(false);
        m_jFoodCoupon.setFocusable(false);
        m_jVoucher.setFocusable(false);
        m_jCreditAmount.setFocusable(false);
        m_jtxtChequeNo.setFocusable(false);
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
    }
     public void stateToPayment(){
        m_jTxtCustomerId.setFocusable(false);
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        cusName.setFocusable(false);
        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
        m_jNumberKeys.setFocusable(false);
        m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        m_jbtnScale.setFocusable(false);
        m_jCreditAllowed.setFocusable(false);
   
    }

    private void incProductByCode(String sCode) {
    // precondicion: sCode != null
        
        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {                  
                Toolkit.getDefaultToolkit().beep();                   
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);           
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();           
            new MessageInf(eData).show(this);           
        }
    }
    public void incProductByItemDetails(String id) {
    // precondicion: sCode != null

        try {
            ProductInfoExt oProduct = dlSales.getProductInfo(id);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
//                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);
        }
    }
    private void incProductByCodePrice(String sCode, double dPriceSell) {
    // precondicion: sCode != null
        
        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {                  
                Toolkit.getDefaultToolkit().beep();                   
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);           
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                if (m_jaddtax.isSelected()) {
                    // debemos quitarle los impuestos ya que el precio es con iva incluido...
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(),"N");
                    addTicketLine(oProduct, 1.0, dPriceSell / (1.0 + tax.getRate()));
                } else {
                    addTicketLine(oProduct, 1.0, dPriceSell);
                }
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);               
        }
    }
    public void getCreditDate() {
       Date Currentdate = null;
       Calendar now = Calendar.getInstance();
       int days = Integer.parseInt(m_App.getProperties().getValidity());
       now.add(Calendar.DATE,days);
       DateFormat formatter ;
       formatter = new SimpleDateFormat("dd-MM-yyyy");
       String str_date = (now.get(Calendar.DATE))+ "-"+ (now.get(Calendar.MONTH) + 1)+ "-"+ (now.get(Calendar.YEAR));
            try {
                Currentdate = (Date) formatter.parse(str_date);
                // Currentdate =;
            } catch (java.text.ParseException ex) {
                Logger.getLogger(RetailTicketInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
                // Currentdate =;
          m_oTicket.setNewDate(Currentdate);
       // Currentdate =;


        }
    private void incProduct(ProductInfoExt prod) {
        
        if (prod.isScale() && m_App.getDeviceScale().existsScale()) {
            try {
                Double value = m_App.getDeviceScale().readWeight();
                if (value != null) {
                    incProduct(value.doubleValue(), prod);
                }
            } catch (ScaleException e) {
                Toolkit.getDefaultToolkit().beep();                
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                stateToZero(); 
            }
        } else {
            // No es un producto que se pese o no hay balanza
            incProduct(1.0, prod);
        }
    }
    
    private void incProduct(double dPor, ProductInfoExt prod) {
        // precondicion: prod != null
        addTicketLine(prod, dPor, prod.getPriceSell());       
    }
    public String getWeekDay(){
        String DAY="";
        Calendar cal =Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek)
        {
            case 1:
            DAY="SUNDAY";
            break;
            case 2:
            DAY="MONDAY";
            break;
            case 3:
            DAY="TUESDAY";
            break;
            case 4:
            DAY="WEDNESDAY";
            break;
            case 5:
            DAY="THURSDAY";
            break;
            case 6:
            DAY="FRIDAY";
            break;
            case 7:
            DAY="SATURDAY";
            break;
            }
        return DAY;
    }
    protected void buttonTransition(ProductInfoExt prod) {
      
   buttonPlus=1;
    // precondicion: prod != null
       //   selectedProduct = prod.getID();

         if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(prod);
        } else if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(getInputValue(), prod);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }       
    }

     
    private void stateTransition(char cTrans) throws BasicException {

        if (cTrans == '\n') {
            // Codigo de barras introducido
            if (m_sBarcode.length() > 0) {            
                String sCode = m_sBarcode.toString();
                if (sCode.startsWith("c")) {
                    // barcode of a customers card
                    try {
                        CustomerInfoExt newcustomer = dlSales.findCustomerExt(sCode);
                        if (newcustomer == null) {
                            Toolkit.getDefaultToolkit().beep();                   
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer")).show(this);           
                        } else {
                            m_oTicket.setCustomer(newcustomer);
//                            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
                        }
                    } catch (BasicException e) {
                        Toolkit.getDefaultToolkit().beep();                   
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e).show(this);           
                    }
                    stateToZero();
                } else if (sCode.length() == 13 && sCode.startsWith("250")) {
                    // barcode of the other machine
                    ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
                    oProduct.setReference(null); // para que no se grabe
                    oProduct.setCode(sCode);
                    oProduct.setName("Ticket " + sCode.substring(3, 7));
                    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7, 12)) / 100);   
                    oProduct.setTaxCategoryID(((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem()).getID());
                    // Se anade directamente una unidad con el precio y todo
                    addTicketLine(oProduct, 1.0, includeTaxes(oProduct.getTaxCategoryID(), oProduct.getPriceSell()));
                } else if (sCode.length() == 13 && sCode.startsWith("210")) {
                    // barcode of a weigth product
                    incProductByCodePrice(sCode.substring(0, 7), Double.parseDouble(sCode.substring(7, 12)) / 100);
                } else {
                    incProductByCode(sCode);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } else {
            // otro caracter
            // Esto es para el codigo de barras...
            m_sBarcode.append(cTrans);

            // Esto es para el los productos normales...
            if (cTrans == '\u007f') { 
                stateToZero();

            } else if ((cTrans == '0') 
                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {


                m_jPrice.setText("0");
                
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_INPUTZERO)) { 
                // Un numero entero
                m_jPrice.setText(Character.toString(cTrans));
                m_iNumberStatus = NUMBER_INPUTINT;    
                m_iNumberStatusInput = NUMBERVALID;
            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_INPUTINT)) { 
                // Un numero entero
                m_jPrice.setText(m_jPrice.getText() + cTrans);
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
                m_jPrice.setText("0.");
                m_iNumberStatus = NUMBER_INPUTZERODEC;            
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
                m_jPrice.setText(m_jPrice.getText() + ".");
                m_iNumberStatus = NUMBER_INPUTDEC;

            } else if ((cTrans == '0')
                       && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) { 
                // Un numero decimal
                m_jPrice.setText(m_jPrice.getText() + cTrans);
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) { 
                // Un numero decimal
                m_jPrice.setText(m_jPrice.getText() + cTrans);
                m_iNumberStatus = NUMBER_INPUTDEC;
                m_iNumberStatusInput = NUMBERVALID;

            } else if (cTrans == '*' 
                    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
                m_jPor.setText("x");
                m_iNumberStatus = NUMBER_PORZERO;            
            } else if (cTrans == '*' 
                    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
                m_jPrice.setText("0");
                m_jPor.setText("x");
                m_iNumberStatus = NUMBER_PORZERO;       

            } else if ((cTrans == '0') 
                    && (m_iNumberStatus == NUMBER_PORZERO)) {
                m_jPor.setText("x0");            
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_PORZERO)) { 
                // Un numero entero
                m_jPor.setText("x" + Character.toString(cTrans));
                m_iNumberStatus = NUMBER_PORINT;            
                m_iNumberStatusPor = NUMBERVALID;
            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_PORINT)) { 
                // Un numero entero
                m_jPor.setText(m_jPor.getText() + cTrans);

            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORZERO) {
                m_jPor.setText("x0.");
                m_iNumberStatus = NUMBER_PORZERODEC;            
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORINT) {
                m_jPor.setText(m_jPor.getText() + ".");
                m_iNumberStatus = NUMBER_PORDEC;

            } else if ((cTrans == '0')
                       && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) { 
                // Un numero decimal
                m_jPor.setText(m_jPor.getText() + cTrans);
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) { 
                // Un numero decimal
                m_jPor.setText(m_jPor.getText() + cTrans);
                m_iNumberStatus = NUMBER_PORDEC;            
                m_iNumberStatusPor = NUMBERVALID;  
            
            } else if (cTrans == '\u00a7' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                // Scale button pressed and a number typed as a price
                if (m_App.getDeviceScale().existsScale() && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                    try {
                        Double value = m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            ProductInfoExt product = getInputProduct();
                            addTicketLine(product, value.doubleValue(), product.getPriceSell());
                        }
                    } catch (ScaleException e) {
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                        stateToZero(); 
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }
            } else if (cTrans == '\u00a7' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
                // Scale button pressed and no number typed.
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else if (m_App.getDeviceScale().existsScale()) {
                    try {
                        Double value = m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                            newline.setMultiply(value.doubleValue());
                            newline.setPrice(Math.abs(newline.getPrice()));
                            paintTicketLine(i, newline);
                        }
                    } catch (ScaleException e) {
                        // Error de pesada.
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                        stateToZero(); 
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }      
                
            // Add one product more to the selected line
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {

                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund + button means one unit less
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        paintTicketLine(i, newline);                   
                    }
                    else {
                
            //    ProductInfoExt product = new ProductInfoExt();
                        // add one unit to the selected line
                        newline.setMultiply(newline.getMultiply()+ 1.0);
                       // buttonPlus = 2;

                        paintTicketLine(i, newline);
                         m_oTicket.refreshEditTxtFields(1);
                        //below line is for promotion products
//                        ProductInfoExt product = getInputProduct();
//                        addTicketLine(product, 1.0, product.getPriceSell());

                    }
                }

            // Delete one product of the selected line
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund - button means one unit more
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        if (newline.getMultiply() >= 0) {
                            removeTicketLine(i);
                        } else {
                            paintTicketLine(i, newline);
                        }
                    } else {
                        // substract one unit to the selected line
                        newline.setMultiply(newline.getMultiply() - 1.0);
                       

                        if (newline.getMultiply() > 0.0) {
                           // removeTicketLine(i); // elimino la linea
                       // } else {

                            paintTicketLine(i, newline);
                             m_oTicket.refreshEditTxtFields(1);
                             //below line is for promotion products
//                            ProductInfoExt product = getInputProduct();
//                            addTicketLine(product, newline.getMultiply(), product.getPriceSell());
                        }
                    }
                }

            // Set n products to the selected line
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    double dPor = getPorValue();
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(-dPor);
                        newline.setPrice(Math.abs(newline.getPrice()));
                        paintTicketLine(i, newline);                
                    } else {
                        newline.setMultiply(dPor);
                        newline.setPrice(Math.abs(newline.getPrice()));
                        paintTicketLine(i, newline);
                    }
                }

            // Set n negative products to the selected line
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    double dPor = getPorValue();
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
                        newline.setMultiply(dPor);
                        newline.setPrice(-Math.abs(newline.getPrice()));
                        paintTicketLine(i, newline);
                    }           
                }

            // Anadimos 1 producto
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                ProductInfoExt product = getInputProduct();
                addTicketLine(product, 1.0, product.getPriceSell());
                
            // Anadimos 1 producto con precio negativo
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                ProductInfoExt product = getInputProduct();
                addTicketLine(product, 1.0, -product.getPriceSell());

            // Anadimos n productos
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                ProductInfoExt product = getInputProduct();
                addTicketLine(product, getPorValue(), product.getPriceSell());

            // Anadimos n productos con precio negativo ?
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
                ProductInfoExt product = getInputProduct();
                addTicketLine(product, getPorValue(), -product.getPriceSell());

            // Totals() Igual;
            } else if (cTrans == ' ' || cTrans == '=') {
                if (m_oTicket.getLinesCount() > 0) {
                   
                    if (closeTicket(m_oTicket, m_oTicketExt)) {
                        // Ends edition of current receipt

                        m_ticketsbag.deleteTicket();  
                    } else {
                        // repaint current ticket
                        refreshTicket();
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {

        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {

            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
                if (ticket.getTotal() >= 0.0) {
                    ticket.resetPayments(); //Only reset if is sale
                }

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {

                    // Muestro el total
                    // printTicket("Printer.TicketTotal", ticket, ticketext);
                    System.out.println("enter 3333");

                    // Select the Payments information
                    JPaymentSelect paymentdialog = ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL
                            ? paymentdialogreceipt
                            : paymentdialogrefund;
           //         paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));

                    paymentdialog.setTransactionID(ticket.getTransactionID());

                    if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer())) {

                        // assign the payments selected and calculate taxes.
                        ticket.setPayments(paymentdialog.getSelectedPayments());

                        // Asigno los valores definitivos del ticket...
                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                         ticket.setActiveDay(m_App.getActiveDayIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                              String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){


                      try{
                        try {
                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                        } catch (BasicException ex) {
                            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                            m_oTicket.setDocumentNo(ticketDocument);
                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){

                                getCreditDate();
                          }

                     double creditAmt;
                    try{
                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                    }catch(Exception ex){
                        creditAmt =0;
                    }
                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                            String cod;
                            double advanceissued;
                            String isCredit;
                            String isPaidStatus;

                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                            if(selectedindex!=0){
                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                            }

                            if(m_jCod.isSelected()){
                                cod = "Y";
                                isPaidStatus = "N";
                            }else{
                                cod = "N";
                                isPaidStatus = "Y";
                            }
                             if(creditAmt>0){
                                isCredit = "Y";
                            }else{
                                isCredit = "N";
                            }

                            if(m_jTxtAdvance.getText().equals("")){
                                advanceissued = 0;
                            }else{
                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                            }
                            String file;

                                   if(m_jHomeDelivery.isSelected()){
                                       file = "Printer.HomeDelivery";
                                   }else{
                                        file = "Printer.Bill";
                                   }
                            ticket.setDocumentNo(ticketDocument);
                            saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,isCredit,"N");
                            executeEvent(ticket, ticketext, "ticket.close", new ScriptArg("print", paymentdialog.isPrintSelected()));

                                                   
                            resultok = true;
                           
                        }
                    }
                }
            } catch (TaxesException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotcalculatetaxes"));
                msg.show(this);
                resultok = false;
            }
            ticket.setSplitValue("");
           // reset the payment info
//            ticket.resetTaxes();
          //  ticket.resetPayments();
        }

        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }
    private boolean closeTicket(RetailTicketInfo ticket, Object ticketext) throws BasicException {
        System.out.println("closeTicket");
        boolean resultok = false;
          int inventoyCount = 0;
        try {
            inventoyCount = dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {  
            try {
                //            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
                setTenderAmount();

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
               //    if()
                    double creditAmt;
                    try{
                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                    }catch(Exception ex){
                        creditAmt =0;
                    }

                if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
                        showMessage(this, "Please select the customer");
                }else{
                if(totalBillValue==0 && m_oTicket.getTotal()!=0){
                      showMessage(this, "Please enter the tender types");
                 }else if(totalBillValue<m_oTicket.getTotal()){
                      showMessage(this, "Entered tender amount should be equal to total sale amount");
                 }
                 else{
                  
                    if(m_jCreditAmount.getText().equals("")){
                        creditAmount = 0;
                     }else{
                        creditAmount = Double.parseDouble(m_jCreditAmount.getText());
                     }

                 //   String

                      paymentDetail();
                        // assign the payments selected and calculate taxes.         
                      ticket.setPayments(m_aPaymentInfo.getPayments());

                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                            String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument = null;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){

//
//                      try{
//                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
//                        String[] ticketDocNoValue = ticketDocNo.split("-");
//                            ticketDocNo=ticketDocNoValue[2];
//                        }catch(NullPointerException ex){
//                            ticketDocNo = "10000";
//                        }
//                        if(ticketDocNo!=null){
//                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                            ticketDocNoInt = ticketDocNoInt + 1;
//
//                        }
                         ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticket.getTicketId();
                         }
//                         else{
//                                ticketDocument = "0";
//                         }
                            m_oTicket.setDocumentNo(ticketDocument);
                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                              
                                getCreditDate();
                          }
                           

                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                            String cod;
                            double advanceissued;
                            String isCredit;
                            String isPaidStatus;
                            
                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                            if(selectedindex!=0){
                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                            }

                           
                            if(m_jCod.isSelected()){
                                cod = "Y";
                                isPaidStatus = "N";
                            }else{
                                cod = "N";
                                isPaidStatus = "Y";
                            }
                             if(creditAmt>0){
                                isCredit = "Y";
                            }else{
                                isCredit = "N";
                            }
                           
                            if(m_jTxtAdvance.getText().equals("")){
                                advanceissued = 0;
                            }else{
                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                            }
                          if(!cusName.getText().equals("") &&  creditAmt!=0 && customerList.get(0).getIsCreditCustomer()==0 ){
                              showMessage(this, "Please select the credit customer");
                          }else{
                            if(inventoyCount==1){
                                showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
                            }else{
                                String file;

                                   if(m_jHomeDelivery.isSelected()){
                                       file = "Printer.HomeDelivery";
                                   }else{
                                        file = "Printer.Bill";
                                   }

                                   saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,isCredit,"N");
                                   resultok = true;
                            }
                          }
                                
                        }
                }
                }}

        }
        return resultok;        
    }

        private boolean closeTicketHomeDelivery(RetailTicketInfo ticket, Object ticketext,PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketHomeDelivery");

        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
            try {
                //            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
               // setTenderAmount();

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
               //    if()
                    double creditAmt;
                    try{
                        creditAmt = m_oTicket.getTotal();
                    }catch(Exception ex){
                        creditAmt =0;
                    }

                if((cusName.getText().equals("") || cusName.getText()==null) ){
                        showMessage(this, "Please select the customer");
                }else{
//
                   //   ticket.setPayments(m_aPaymentInfo.getPayments());

                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                            String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){


                      try{
                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                                m_oTicket.setDocumentNo(ticketDocument);
                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                                getCreditDate();
                          }
                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                            double advanceissued;

                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                            if(selectedindex!=0){
                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                            }

                            if(m_jTxtAdvance.getText().equals("")){
                                advanceissued = 0;
                            }else{
                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                            }

                          String file;
                          file = "Printer.HomeDelivery";

                           if(m_oTicket.getLinesCount()==0){
                               showMessage(this, "Please select the products");
                           }else{
                           saveHomeReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,"N","N");

                            m_jCreditAllowed.setVisible(false);
                            m_jCreditAllowed.setSelected(false);
                          resultok = true;
                           }
                }
                         m_ticketsbag.deleteTicket();
                }

                }
       // }
        }

        return resultok;
    }
    private boolean closeTicketNonChargable(RetailTicketInfo ticket, Object ticketext,PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketNonChargable");

        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
            try {
                //            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
               // setTenderAmount();

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
               //    if()
                    double creditAmt;
                    try{
                        creditAmt = m_oTicket.getTotal();
                    }catch(Exception ex){
                        creditAmt =0;
                    }
                    if((cusName.getText().equals("")) ){
                        showMessage(this, "Please select the customer");
                   }else{
                   //   ticket.setPayments(m_aPaymentInfo.getPayments());

                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                            String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){


                      try{
                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                        m_oTicket.setDocumentNo(ticketDocument);
                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                                getCreditDate();
                          }
                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                            double advanceissued;

                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                            if(selectedindex!=0){
                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                            }

                            if(m_jTxtAdvance.getText().equals("")){
                                advanceissued = 0;
                            }else{
                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                            }
                         ticket.setDocumentNo(ticketDocument);
                          String file;
                          file = "Printer.NonChargableBill";
                               
                           if(m_oTicket.getLinesCount()==0){
                               showMessage(this, "Please select the products");
                           }else{
                           saveNonChargableReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,"N","Y");

                            m_jCreditAllowed.setVisible(false);
                            m_jCreditAllowed.setSelected(false);
                           resultok = true;
                           }
                }
                         m_ticketsbag.deleteTicket();
                   }
                 
                }
       // }
        }

        return resultok;
    }

    private boolean closeTicketWithButton(RetailTicketInfo ticket, Object ticketext,PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketWithButton");
        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
            try {
                //            try {
                // reset the payment info9
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
                setTenderAmount();

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                   double creditAmt;
                    try{
                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                    }catch(Exception ex){
                        creditAmt =0;
                    }

                if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
                        showMessage(this, "Please select the customer");
                }else{
                      ticket.setPayments(m_aPaymentInfo.getPayments());

                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                            String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){


                      try{
                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                                m_oTicket.setDocumentNo(ticketDocument);
                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
                                getCreditDate();
                          }
                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                          
                  String homeDelivery;
                    String orderTaking;
                     String cod;
                     String isPaidStatus;

//                if(getHomeDeliverySale().equals("HomeDelivery")){
//                     if(m_jHomeDelivery.isSelected()){
//                         homeDelivery = "Y";
//                         orderTaking = "N";
//                          if(m_jCod.isSelected()){
//                            cod = "Y";
//                            isPaidStatus = "N";
//                          }else{
//                            cod = "N";
//                            isPaidStatus = "Y";
//                          }
//
//                     }else{
//                         homeDelivery = "N";
//                         orderTaking = "Y";
//                         cod = "N";
//                         isPaidStatus = "Y";
//                     }
//                }else{
                    homeDelivery = "N";
                    orderTaking = "N";
                    cod = "N";
                    isPaidStatus = "Y";
           //     }

                        String isCredit;
                        double advanceissued;


                        int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                        if(selectedindex!=0){
                            deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                        }

                        if(creditAmt>0){
                            isCredit = "Y";
                        }else{
                            isCredit = "N";
                        }

                        if(m_jTxtAdvance.getText().equals("")){
                            advanceissued = 0;
                        }else{
                            advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                        }
                      String file;
                       if(m_oTicket.getPrintStatus()==1){
                           //if(getHomeDeliverySale().equals("HomeDelivery")){
                           //    file = "Printer.HomeDelivery";
                          // }else{
                               file = "Printer.Bill";
                        //   }
                           if(m_oTicket.getLinesCount()==0){
                               showMessage(this, "Please select the products");
                           }else{
                           saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,isCredit,"N");
                            m_jCreditAllowed.setVisible(false);
                            m_jCreditAllowed.setSelected(false);
                           resultok = true;
                           }
                       }else if(m_oTicket.getPrintStatus()==2){
                           file = "Printer.Ticket";
                           if(m_oTicket.getLinesCount()==0){
                               showMessage(this, "Please select the products");
                           }else{
                           dlSales.saveDraftTicket(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,homeDelivery,cod,advanceissued,creditAmount,"N",orderTaking);
                            saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"N",file,isCredit,"N");

                            m_jCreditAllowed.setVisible(false);
                            m_jCreditAllowed.setSelected(false);
                            resultok = true;
                           }
                       }

                }
                      //  if(getHomeDeliverySale().equals("Sales")){
                         m_ticketsbag.deleteTicket();
                      //  }
                }
                }
        }
       
           return resultok;
    }

    private boolean closeCreditTicket(RetailTicketInfo ticket, Object ticketext,PaymentInfoList m_aPaymentInfo) throws BasicException {

        boolean resultok = false;

        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
            try {
                //            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
                setTenderAmount();

                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                    int inventoyCount = 0;
                    try {
                        inventoyCount = dlSales.getStopInventoryCount();
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     if(inventoyCount==1){
                       showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
                     }else{
                     double creditAmt;
                     try{
                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                    }catch(Exception ex){
                        creditAmt =0;
                    }

                    if((creditAmt!=0 && cusName.getText().equals(""))){
                            showMessage(this, "Please select the customer");
                    }else if(!cusName.getText().equals("") &&  creditAmt!=0 && customerList.get(0).getIsCreditCustomer()==0 ){
                            showMessage(this, "Please select the credit customer");
                    }else{

                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                            String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){


                      try{
                        ticketDocNo =dlSales.getTicketDocumentNo().list().get(0).toString();
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                             m_oTicket.setDocumentNo(ticketDocument);
                            String chequeNos = m_jtxtChequeNo.getText();
                            String deliveryBoy = "";
                            String homeDelivery;
                            String isCredit;
                             if(m_jHomeDelivery.isSelected()){
                                homeDelivery = "Y";
                            }else{
                                homeDelivery = "N";
                            }
                             if(creditAmt>0){
                                isCredit = "Y";
                            }else{
                                isCredit = "N";
                            }

                               String file;
                               file = "Printer.Bill";
                               
                               if(m_oTicket.getLinesCount()==0){
                                   showMessage(this, "Please select the products");
                               }else{
                               saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,"",0,creditAmt,ticketext,"Y",file,isCredit,"N");
                                m_jCreditAllowed.setVisible(false);
                                m_jCreditAllowed.setSelected(false);
                               resultok = true;
                            
                }
                 }}
                }
        }
        }
        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }
   public void saveReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount,Object ticketext, String status, String file,String isCredit,String nonChargable) {
   //    System.out.println("ticketext---"+ticketext.toString());
       double tipsAmt = Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
         String cod;
         String isPaidStatus;

            homeDelivery = "N";
            orderTaking = "N";
            cod = "N";
            isPaidStatus = "Y";
       // }
        try {
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking,nonChargable);
        } catch (Exception ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
              
              if(getEditSale()=="Edit"){
                RetailTicketInfo editTicket = null;
                try {
                    editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
              }

               setEditSale("");
                try {
                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }

           //     if(getPrinterStatus()==true){
                ///  printTicket(file, ticket, ticketext);
            //    }
//                resultok = true;
                 m_jCreditAllowed.setVisible(false);
                 m_jCreditAllowed.setSelected(false);
                 m_jCash.setText("");
                 m_jCard.setText("");
                 m_jCheque.setText("");
                 m_jFoodCoupon.setText("");
                 m_jVoucher.setText("");
                 m_jTxtTotalPaid.setText("");
                 m_oTicket.setRate("0");
                 m_oTicket.setdAmt(0);
                 m_jTxtCustomerId.setText("");
                 m_jHomeDelivery.setSelected(false);
                 m_jDeliveryBoy.removeAllItems();
                 m_jCreditAmount.setText("0.00");
                 populateDeliveryBoy();
                 m_jDeliveryBoy.setSelectedIndex(0);
                 m_jTxtAdvance.setText("");
                 m_jCod.setSelected(false);
                 m_jCreditAmount.setText("0.00");
                  m_jTxtChange.setText("");
                 jLabel10.setVisible(false);
                 m_jDeliveryBoy.setVisible(false);
                 jLabel11.setVisible(false);
                 m_jTxtAdvance.setVisible(false);
                 m_jCod.setVisible(false);
                  m_oTicket.resetTaxes();
                 m_oTicket.resetPayments();
                 m_jTxtTips.setText("0.00");
                 m_jServiceCharge.setText("");

            activate();

    }
     public void saveHomeReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount,Object ticketext, String status, String file,String isCredit,String nonChargable) {
   //    System.out.println("ticketext---"+ticketext.toString());
       double tipsAmt = Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
         String cod;
         String isPaidStatus;

            homeDelivery = "Y";
            orderTaking = "Y";
            cod = "N";
            isPaidStatus = "Y";
       // }
        try {
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking,nonChargable);
        } catch (Exception ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

              if(getEditSale()=="Edit"){
                RetailTicketInfo editTicket = null;
                try {
                    editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
              }

               setEditSale("");
                try {
                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }

           //     if(getPrinterStatus()==true){
                ///  printTicket(file, ticket, ticketext);
            //    }
//                resultok = true;
                 m_jCreditAllowed.setVisible(false);
                 m_jCreditAllowed.setSelected(false);
                 m_jCash.setText("");
                 m_jCard.setText("");
                 m_jCheque.setText("");
                 m_jFoodCoupon.setText("");
                 m_jVoucher.setText("");
                 m_jTxtTotalPaid.setText("");
                 m_oTicket.setRate("0");
                 m_oTicket.setdAmt(0);
                 m_jTxtCustomerId.setText("");
                 m_jHomeDelivery.setSelected(false);
                 m_jDeliveryBoy.removeAllItems();
                 m_jCreditAmount.setText("0.00");
                 populateDeliveryBoy();
                 m_jDeliveryBoy.setSelectedIndex(0);
                 m_jTxtAdvance.setText("");
                 m_jCod.setSelected(false);
                 m_jCreditAmount.setText("0.00");
                  m_jTxtChange.setText("");
                 jLabel10.setVisible(false);
                 m_jDeliveryBoy.setVisible(false);
                 jLabel11.setVisible(false);
                 m_jTxtAdvance.setVisible(false);
                 m_jCod.setVisible(false);
                  m_oTicket.resetTaxes();
                 m_oTicket.resetPayments();
                 m_jTxtTips.setText("0.00");
                 m_jServiceCharge.setText("");

            activate();

    }
     public void saveNonChargableReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount,Object ticketext, String status, String file,String isCredit,String nonChargable) {
   //    System.out.println("ticketext---"+ticketext.toString());
       double tipsAmt = Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
         String cod;
         String isPaidStatus;


            homeDelivery = "N";
            orderTaking = "N";
            cod = "N";
            isPaidStatus = "Y";
       // }
        try {
            dlSales.saveNonChargableTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking,nonChargable);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

              if(getEditSale()=="Edit"){
                RetailTicketInfo editTicket = null;
                try {
                    editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
              }

               setEditSale("");
                try {
                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }

           //     if(getPrinterStatus()==true){
                  printTicket(file, ticket, ticketext);
            //    }
//                resultok = true;
                 m_jCreditAllowed.setVisible(false);
                 m_jCreditAllowed.setSelected(false);
                 m_jCash.setText("");
                 m_jCard.setText("");
                 m_jCheque.setText("");
                 m_jFoodCoupon.setText("");
                 m_jVoucher.setText("");
                 m_jTxtTotalPaid.setText("");
                 m_oTicket.setRate("0");
                 m_oTicket.setdAmt(0);
                 m_jTxtCustomerId.setText("");
                 m_jHomeDelivery.setSelected(false);
                 m_jDeliveryBoy.removeAllItems();
                 m_jCreditAmount.setText("0.00");
                 populateDeliveryBoy();
                 m_jDeliveryBoy.setSelectedIndex(0);
                 m_jTxtAdvance.setText("");
                 m_jCod.setSelected(false);
                 m_jCreditAmount.setText("0.00");
                  m_jTxtChange.setText("");
                 jLabel10.setVisible(false);
                 m_jDeliveryBoy.setVisible(false);
                 jLabel11.setVisible(false);
                 m_jTxtAdvance.setVisible(false);
                 m_jCod.setVisible(false);
                  m_oTicket.resetTaxes();
                 m_oTicket.resetPayments();
                 m_jTxtTips.setText("0.00");
                 m_jServiceCharge.setText("");

            activate();

    }
   public void saveNotReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy,  double advanceissued, double creditAmount,Object ticketext, String status) {
    double tipsAmt = Double.parseDouble(m_jTxtTips.getText());
    String homeDelivery;
        String orderTaking;
         String cod;
         String isPaidStatus;

//        if(getHomeDeliverySale().equals("HomeDelivery")){
//             if(m_jHomeDelivery.isSelected()){
//                 homeDelivery = "Y";
//                 orderTaking = "N";
//                  if(m_jCod.isSelected()){
//                    cod = "Y";
//                    isPaidStatus = "N";
//                  }else{
//                    cod = "N";
//                    isPaidStatus = "Y";
//                  }
//
//             }else{
//                 homeDelivery = "N";
//                 orderTaking = "Y";
//                 cod = "N";
//                 isPaidStatus = "Y";
//             }
//        }else{
            homeDelivery = "N";
            orderTaking = "N";
            cod = "N";
            isPaidStatus = "Y";
      //  }
      try {
               dlSales.saveRetailTicket(ticket, inventoryLocation,posNo,storeName,ticketDocument,priceInfo, chequeNos,deliveryBoy,homeDelivery,cod,advanceissued,creditAmount,status,"N","Y",tipsAmt,orderTaking,"N");
                } catch (Exception eData) {
                    MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                    msg.show(this);
                }
                try {
                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }

                 m_jCreditAllowed.setVisible(false);
                 m_jCreditAllowed.setSelected(false);
                 m_jCash.setText("");
                  m_jTxtChange.setText("");
                 m_jCard.setText("");
                 m_jCheque.setText("");
                 m_jFoodCoupon.setText("");
                 m_jVoucher.setText("");
                 m_jTxtTotalPaid.setText("");
                 m_oTicket.setRate("0");
                 m_oTicket.setdAmt(0);
                 m_jHomeDelivery.setSelected(false);
                 m_jDeliveryBoy.removeAllItems();
                 populateDeliveryBoy();
                 m_jDeliveryBoy.setSelectedIndex(0);
                 m_jTxtAdvance.setText("");
                 m_jCod.setSelected(false);
                 m_jCod.setVisible(false);
                 m_oTicket.resetTaxes();
                 m_oTicket.resetPayments();
                 m_jTxtCustomerId.setText("");
                 m_jCreditAmount.setText("0.00");
              activate();

    }
      public void clearTxtField(){

       cusName.setText("");
       cusPhoneNo.setText("");
       itemName.setText("");
       m_jCash.setText("");
       m_jCard.setText("");
       m_jCheque.setText("");
       m_jFoodCoupon.setText("");
       m_jVoucher.setText("");
       m_jtxtChequeNo.setText("");
       m_jHomeDelivery.setSelected(false);
       m_jCod.setSelected(false);
       m_jCreditAmount.setText("0.00");
       m_jCod.setVisible(false);
       m_jCreditAllowed.setVisible(false);
       m_jCreditAllowed.setSelected(false);

      }
      public void clearFocus(boolean value){

       cusName.setFocusable(value);
       cusPhoneNo.setFocusable(value);
       m_jTxtItemCode.setFocusable(value);
       m_jTxtCustomerId.setFocusable(value);
       m_jKeyFactory.setFocusable(true);
       m_jKeyFactory.setText(null);
       java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
       m_jPrice.setEnabled(true);
       itemName.setFocusable(value);
       m_jCash.setFocusable(value);
       m_jCard.setFocusable(value);
       m_jCheque.setFocusable(value);
       m_jtxtChequeNo.setFocusable(value);
       m_jFoodCoupon.setFocusable(value);
       m_jCreditAmount.setFocusable(value);
       m_jVoucher.setFocusable(value);
       m_jTxtItemCode.setText("");
         itemName.setText("");

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
      private java.util.List<TicketLineConstructor> getAllLines(RetailTicketInfo ticket, Object ticketext) {


       java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
       allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
       allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
       allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (cusName.getText())));
       
          allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ticketext));
      
      allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));
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
                 allLines.add(new TicketLineConstructor(getSpaces(33)+ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(25 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getTax()))));
              }
          }

      }

      String aCount = ticket.printTicketCount();
      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Charge 6%" + getSpaces(18-serviceCharge.length()) + serviceCharge));

      allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Tax 4.94%" + getSpaces(18-serviceTax.length()) +serviceTax));
    
      allLines.add(new TicketLineConstructor(getSpaces(33)+"Grand Total : " + getSpaces(21-total.length()) + (total)));

      return allLines;
    }

       private java.util.List<TicketLineConstructor> getNonChargeableLines(RetailTicketInfo ticket, Object ticketext) {
          java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
          allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
          allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
          allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (cusName.getText())));
          allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ticketext));
          allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));

     // }
          allLines.add(new TicketLineConstructor(getDottedLine(90)));
          allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price"+getSpaces(9)+"Value(INR)"));
          allLines.add(new TicketLineConstructor(getDottedLine(90)));
          for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = "0.00";
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue+getSpaces(9 - qty.length() + 11 - subValue.length())+total));
        }
          System.out.println("ticket.getTaxes().get(0).getTaxInfo().getName();"+ticket.getTaxes().get(0).getTaxInfo().getName());
          allLines.add(new TicketLineConstructor(getDottedLine(90)));
          allLines.add(new TicketLineConstructor(getSpaces(33)+"Total " + getSpaces(25) + "0.00"));
          allLines.add(new TicketLineConstructor(getSpaces(33)+"Discount " + getSpaces(22)  + "0.00"));

          allLines.add(new TicketLineConstructor(getSpaces(33)+"Total After Discount " +getSpaces(10) + "0.00"));
if(ticket.getTaxes().size()!=0){
              for(int i=0;i<ticket.getTaxes().size();i++){
                   if(ticket.getTaxes().get(i).getTax()!=0.00){
                     allLines.add(new TicketLineConstructor(getSpaces(33)+ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(31-ticket.getTaxes().get(i).getTaxInfo().getName().length())  + "0.00"));
              }
          }

      }
          String aCount = ticket.printTicketCount();
          allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Charge 6%" + getSpaces(14) + "0.00"));

          allLines.add(new TicketLineConstructor(getSpaces(33)+"Service Tax 4.94%" + getSpaces(14) + "0.00"));
         
          

        allLines.add(new TicketLineConstructor(getSpaces(33)+"Grand Total : " + getSpaces(17) + "0.00"));

        //allLines.add(new TicketLineConstructor(announce3));
        return allLines;
    }
       private void addlinesaddBlankLines(int count, java.util.List<TicketLineConstructor> allLines) {
        for (int i = 0; i < count; i++) {
            allLines.add(new TicketLineConstructor(""));
        }
    }
    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
         java.util.List<TicketLineConstructor> allLines = null;
           com.openbravo.pos.printer.printer.ImagePrinter printer = new ImagePrinter();
        if(sresourcename.equals("Printer.Bill")){
             allLines = getAllLines(ticket,ticketext);
             
        }else if(sresourcename.equals("Printer.NonChargableBill")){
            allLines = getNonChargeableLines(ticket,ticketext);
        }
         try {
                printer.print(allLines);
                
            } catch (PrinterException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    
    private void printReport(String resourcefile, RetailTicketInfo ticket, Object ticketext) {
        
        try {     
         
            JasperReport jr;
           
            InputStream in = getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {      
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));            
                jr = JasperCompileManager.compileReport(jd);    
            } else {
                // read the compiled reporte
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }
           
            // Construyo el mapa de los parametros.
            Map reportparams = new HashMap();
            // reportparams.put("ARG", params);
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(resourcefile + ".properties"));
            } catch (MissingResourceException e) {
            }
            reportparams.put("TAXESLOGIC", taxeslogic); 
            
            Map reportfields = new HashMap();
            reportfields.put("TICKET", ticket);
            reportfields.put("PLACE", ticketext);

            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[] { reportfields } ));
            
            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printername"));
            
            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);
            
        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }               
    }


    public void visorTicketLine(RetailTicketLineInfo oLine){
        if (oLine == null) { 
             m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {                 
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelEditPrintTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelEditPrintTicket.this);
            }
        } 
    }    


    private void showMessage(JRetailPanelEditPrintTicket aThis, String msg) {
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
    
    private Object evalScript(ScriptObject scr, String resource, ScriptArg... args) {
        
        // resource here is guaratied to be not null
         try {
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            return scr.evalScript(dlSystem.getResourceAsXML(resource), args);                
        } catch (ScriptException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"), e);
            msg.show(this);
            return msg;
        } 
    }
        
    public void evalScriptAndRefresh(String resource, ScriptArg... args) {
     
        if (resource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
            msg.show(this);            
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            evalScript(scr, resource, args);   
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
        }
    }  
    
    public void printTicket(String resource) {
        printTicket(resource, m_oTicket, m_oTicketExt);
    }
    
    public Object executeEventAndRefresh(String eventkey, ScriptArg ... args) {
        
        String resource =null;
                //m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            Object result = evalScript(scr, resource, args);   
            refreshTicket();
            setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }
   
    private Object executeEvent(RetailTicketInfo ticket, Object ticketext, String eventkey, ScriptArg ... args) {
        
        String resource =null;
               // m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(ticket, ticketext);
            return evalScript(scr, resource, args);
        }
    }
    
    public String getResourceAsXML(String sresourcename) {
        return dlSystem.getResourceAsXML(sresourcename);
    }

    public BufferedImage getResourceAsImage(String sresourcename) {
        return dlSystem.getResourceAsImage(sresourcename);
    }
    
    public void setSelectedIndex(int i) {
        
        if (i >= 0 && i < m_oTicket.getLinesCount()) {
            m_ticketlines.setSelectedIndex(i);
        } else if (m_oTicket.getLinesCount() > 0) {
            m_ticketlines.setSelectedIndex(m_oTicket.getLinesCount() - 1);
        }    
    }


    
    public static class ScriptArg {
        private String key;
        private Object value;
        
        public ScriptArg(String key, Object value) {
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
    }
    
    public class ScriptObject {
        
        private RetailTicketInfo ticket;
        private Object ticketext;
        
        private int selectedindex;
        
        private ScriptObject(RetailTicketInfo ticket, Object ticketext) {
            this.ticket = ticket;
            this.ticketext = ticketext;
        }
        
        public double getInputValue() {
            if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                return JRetailPanelEditPrintTicket.this.getInputValue();
            } else {
                return 0.0;
            }
        }
        
        public int getSelectedIndex() {
            return selectedindex;
        }
        
        public void setSelectedIndex(int i) {
            selectedindex = i;
        }  
        
        public void printReport(String resourcefile) {
            JRetailPanelEditPrintTicket.this.printReport(resourcefile, ticket, ticketext);
        }
       
        
        public void printTicket(String sresourcename) {
            JRetailPanelEditPrintTicket.this.printTicket(sresourcename, ticket, ticketext);
        }              
        
        public Object evalScript(String code, ScriptArg... args) throws ScriptException {
           
            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
            script.put("ticket", ticket);
            script.put("place", ticketext);
            script.put("taxes", taxcollection);
            script.put("taxeslogic", taxeslogic);             
            script.put("user", m_App.getAppUserView().getUser());
            script.put("sales", this);

            // more arguments
            for(ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }             

            return script.eval(code);
        }            
    }
      private void populateDeliveryBoy() {

        try {
            deliveryBoyLines = dlCustomers.getDeliveryBoyList();
            m_jDeliveryBoy.addItem("");
            for (int i = 0; i < deliveryBoyLines.size(); i++) {
                m_jDeliveryBoy.addItem(deliveryBoyLines.get(i).getName());
            }
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/** This method is called from within the constructor to
     * Tinitialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        retailTicketLineInfo1 = new com.openbravo.pos.ticket.RetailTicketLineInfo();
        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jPanelScripts = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jLastBill = new javax.swing.JButton();
        m_jLogout = new javax.swing.JButton();
        m_jbtnPrintBill = new javax.swing.JButton();
        m_jSettleBill = new javax.swing.JButton();
        m_jBtnKot = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        m_jLblUserInfo = new javax.swing.JLabel();
        m_jLblCurrentDate = new javax.swing.JLabel();
        m_jLblTime = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        m_jCboCustName = new javax.swing.JComboBox();
        m_jCboContactNo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        m_jTxtCustomerId = new javax.swing.JTextField();
        m_jSearchCustomer = new javax.swing.JButton();
        m_jHomeDelivery = new javax.swing.JCheckBox();
        m_jCreditAllowed = new javax.swing.JCheckBox();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        m_jLblItemName = new javax.swing.JLabel();
        m_jLblItemCode = new javax.swing.JLabel();
        m_jCboItemName = new javax.swing.JComboBox();
        m_jTxtItemCode = new javax.swing.JTextField();
        m_jDeliveryBoy = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        m_jTxtAdvance = new javax.swing.JTextField();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jCalculatePromotion = new javax.swing.JButton();
        m_jBtnDiscount = new javax.swing.JButton();
        m_jAction = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        m_jSplitBtn = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        m_jNumberKeys = new com.openbravo.beans.JNumberKeys();
        jPanel9 = new javax.swing.JPanel();
        m_jPrice = new javax.swing.JLabel();
        m_jEnter = new javax.swing.JButton();
        m_jKeyFactory = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        m_jTax = new javax.swing.JComboBox();
        m_jPor = new javax.swing.JLabel();
        m_jaddtax = new javax.swing.JToggleButton();
        jLblPrinterStatus = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jDiscount = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        m_jServiceCharge = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        catcontainer = new javax.swing.JPanel();
        m_jPanTotals1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jLblCash = new javax.swing.JLabel();
        m_jLblCard = new javax.swing.JLabel();
        m_jLblCheque = new javax.swing.JLabel();
        m_jCard = new javax.swing.JTextField();
        m_jCheque = new javax.swing.JTextField();
        m_jCash = new javax.swing.JTextField();
        m_jLblChequeNo = new javax.swing.JLabel();
        m_jtxtChequeNo = new javax.swing.JTextField();
        m_jLbVoucher = new javax.swing.JLabel();
        m_jVoucher = new javax.swing.JTextField();
        m_jLblFoodCoupon = new javax.swing.JLabel();
        m_jFoodCoupon = new javax.swing.JTextField();
        m_jCod = new javax.swing.JCheckBox();
        m_jCreditAmount = new javax.swing.JTextField();
        m_jLblCreditAmount = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        m_jTxtTips = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jTxtTotalPaid = new javax.swing.JLabel();
        m_jTxtChange = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 204, 153));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jButtons.setPreferredSize(new java.awt.Dimension(8, 10));
        m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

        m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setPreferredSize(new java.awt.Dimension(520, 47));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jbtnScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        m_jbtnScale.setText(AppLocal.getIntString("button.scale")); // NOI18N
        m_jbtnScale.setFocusPainted(false);
        m_jbtnScale.setFocusable(false);
        m_jbtnScale.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnScale.setRequestFocusEnabled(false);
        m_jbtnScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnScaleActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnScale, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 10, -1));

        m_jLastBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/last-bill.png"))); // NOI18N
        m_jLastBill.setFocusable(false);
        m_jLastBill.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jLastBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jLastBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jLastBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, -1, -1));

        m_jLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/logout.png"))); // NOI18N
        m_jLogout.setFocusable(false);
        m_jLogout.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(m_jLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, -1, -1));

        m_jbtnPrintBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Print-bill.png"))); // NOI18N
        m_jbtnPrintBill.setPreferredSize(new java.awt.Dimension(95, 40));
        m_jbtnPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnPrintBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, -1, -1));

        m_jSettleBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Settle-Bill.png"))); // NOI18N
        m_jSettleBill.setPreferredSize(new java.awt.Dimension(75, 25));
        m_jSettleBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSettleBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jSettleBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 95, 40));

        m_jBtnKot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/KOT.png"))); // NOI18N
        m_jBtnKot.setMnemonic('i');
        m_jBtnKot.setFocusPainted(false);
        m_jBtnKot.setFocusable(false);
        m_jBtnKot.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnKot.setRequestFocusEnabled(false);
        m_jBtnKot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnKotActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnKot, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 95, 40));

        m_jButtonsExt.add(jPanel1);

        m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

        m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

        m_jPanelBag.setFocusable(false);
        m_jPanelBag.setPreferredSize(new java.awt.Dimension(800, 35));
        m_jPanelBag.setRequestFocusEnabled(false);
        m_jPanelBag.setLayout(new java.awt.BorderLayout());
        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setPreferredSize(new java.awt.Dimension(1024, 70));
        jPanel6.setLayout(new java.awt.BorderLayout());

        m_jLblUserInfo.setText("    jLabel2");
        m_jLblUserInfo.setFocusable(false);
        m_jLblUserInfo.setPreferredSize(new java.awt.Dimension(340, 16));
        m_jLblUserInfo.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblUserInfo, java.awt.BorderLayout.LINE_START);

        m_jLblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jLblCurrentDate.setText("jLabel2");
        m_jLblCurrentDate.setFocusable(false);
        m_jLblCurrentDate.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblCurrentDate.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblCurrentDate, java.awt.BorderLayout.CENTER);

        m_jLblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setText("                                                                                       jLabel2");
        m_jLblTime.setFocusable(false);
        m_jLblTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblTime.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblTime, java.awt.BorderLayout.LINE_END);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/header-right.png"))); // NOI18N
        jLabel3.setAutoscrolls(true);
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel3.setMaximumSize(new java.awt.Dimension(1450, 61));
        jLabel3.setMinimumSize(new java.awt.Dimension(1024, 61));
        jLabel3.setPreferredSize(new java.awt.Dimension(1024, 45));
        jLabel3.setRequestFocusEnabled(false);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel6.add(jLabel3, java.awt.BorderLayout.NORTH);

        m_jOptions.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel8.setPreferredSize(new java.awt.Dimension(1024, 40));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CUSTOMER NAME");

        m_jCboCustName.setEditable(true);
        m_jCboCustName.setToolTipText("Customer Name");
        m_jCboCustName.setMinimumSize(new java.awt.Dimension(123, 20));
        m_jCboCustName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCboCustNameActionPerformed(evt);
            }
        });

        m_jCboContactNo.setEditable(true);
        m_jCboContactNo.setToolTipText("Contact No");
        m_jCboContactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCboContactNoActionPerformed(evt);
            }
        });

        jLabel5.setText("CONTACT NO");

        jLabel6.setText("CUSTOMER ID");

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        m_jTxtCustomerId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jTxtCustomerId.setMinimumSize(new java.awt.Dimension(123, 20));
        m_jTxtCustomerId.setPreferredSize(new java.awt.Dimension(123, 20));
        m_jTxtCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtCustomerIdActionPerformed(evt);
            }
        });

        m_jSearchCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Cussearch.png"))); // NOI18N
        m_jSearchCustomer.setMnemonic('u');
        m_jSearchCustomer.setFocusable(false);
        m_jSearchCustomer.setPreferredSize(new java.awt.Dimension(39, 33));
        m_jSearchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSearchCustomerActionPerformed(evt);
            }
        });

        m_jHomeDelivery.setText("Home Delivery");
        m_jHomeDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jHomeDeliveryActionPerformed(evt);
            }
        });

        m_jCreditAllowed.setText("Credit Allowed");
        m_jCreditAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCreditAllowedActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(m_jTxtCustomerId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(11, 11, 11)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(9, 9, 9)
                        .add(m_jCboContactNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(m_jCboCustName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(m_jSearchCustomer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jHomeDelivery, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jCreditAllowed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(533, 533, 533))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(jPanel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jSearchCustomer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel8Layout.createSequentialGroup()
                                .add(4, 4, 4)
                                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(m_jTxtCustomerId, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(m_jCboContactNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(m_jCboCustName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(m_jHomeDelivery)
                            .add(m_jCreditAllowed))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        m_jCboContactNo.getAccessibleContext().setAccessibleDescription(null);

        m_jOptions.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(204, 204, 204)));
        jPanel10.setPreferredSize(new java.awt.Dimension(803, 35));

        m_jLblItemName.setText("ITEM NAME");

        m_jLblItemCode.setText("ITEM CODE");

        m_jCboItemName.setEditable(true);
        m_jCboItemName.setAutoscrolls(true);
        m_jCboItemName.setMaximumSize(new java.awt.Dimension(123, 20));
        m_jCboItemName.setMinimumSize(new java.awt.Dimension(123, 20));
        m_jCboItemName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                m_jCboItemNameItemStateChanged(evt);
            }
        });
        m_jCboItemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCboItemNameActionPerformed(evt);
            }
        });

        m_jTxtItemCode.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jTxtItemCode.setMinimumSize(new java.awt.Dimension(123, 20));
        m_jTxtItemCode.setPreferredSize(new java.awt.Dimension(123, 20));
        m_jTxtItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtItemCodeActionPerformed(evt);
            }
        });

        jLabel10.setText("Delivery Boy");

        jLabel11.setText("Advance Issued");

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(m_jLblItemCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(m_jTxtItemCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(m_jLblItemName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9)
                .add(m_jCboItemName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(m_jDeliveryBoy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(m_jTxtAdvance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(538, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(m_jLblItemCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel10Layout.createSequentialGroup()
                .add(7, 7, 7)
                .add(m_jTxtItemCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel10Layout.createSequentialGroup()
                .add(7, 7, 7)
                .add(m_jLblItemName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel10Layout.createSequentialGroup()
                .add(7, 7, 7)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jCboItemName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jDeliveryBoy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10)
                    .add(jLabel11)
                    .add(m_jTxtAdvance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        m_jPanTicket.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        m_jPanelCentral.setFocusable(false);
        m_jPanelCentral.setRequestFocusEnabled(false);
        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel2.setMinimumSize(new java.awt.Dimension(66, 338));
        jPanel2.setPreferredSize(new java.awt.Dimension(61, 400));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/arow-1.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jUp.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jUp.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel2.add(m_jUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, -1, -1));

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/arow-2.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jDown.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jDown.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 47, -1, -1));

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/edit12.png"))); // NOI18N
        m_jEditLine.setMnemonic('e');
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel2.add(m_jEditLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 141, 51, 42));

        m_jCalculatePromotion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/pramo.png"))); // NOI18N
        m_jCalculatePromotion.setMnemonic('f');
        m_jCalculatePromotion.setFocusPainted(false);
        m_jCalculatePromotion.setFocusable(false);
        m_jCalculatePromotion.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jCalculatePromotion.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jCalculatePromotion.setRequestFocusEnabled(false);
        m_jCalculatePromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCalculatePromotionActionPerformed(evt);
            }
        });
        jPanel2.add(m_jCalculatePromotion, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 289, -1, -1));

        m_jBtnDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/percentage.png"))); // NOI18N
        m_jBtnDiscount.setMnemonic('i');
        m_jBtnDiscount.setFocusPainted(false);
        m_jBtnDiscount.setFocusable(false);
        m_jBtnDiscount.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnDiscount.setRequestFocusEnabled(false);
        m_jBtnDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnDiscountActionPerformed(evt);
            }
        });
        jPanel2.add(m_jBtnDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 189, 51, 42));

        m_jAction.setBorder(null);
        m_jAction.setBorderPainted(false);
        m_jAction.setPreferredSize(new java.awt.Dimension(10, 2));
        m_jAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jActionActionPerformed(evt);
            }
        });
        jPanel2.add(m_jAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 242, -1, 14));

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/close2.png"))); // NOI18N
        m_jDelete.setMnemonic('d');
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jDelete.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 93, -1, -1));

        m_jSplitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editcut1.png"))); // NOI18N
        m_jSplitBtn.setMnemonic('f');
        m_jSplitBtn.setFocusPainted(false);
        m_jSplitBtn.setFocusable(false);
        m_jSplitBtn.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jSplitBtn.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jSplitBtn.setRequestFocusEnabled(false);
        m_jSplitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSplitBtnActionPerformed(evt);
            }
        });
        jPanel2.add(m_jSplitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 237, -1, -1));

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jPanelCentral.add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel15.setFocusable(false);
        jPanel15.setPreferredSize(new java.awt.Dimension(779, 4));
        jPanel15.setRequestFocusEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1322, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 4, Short.MAX_VALUE)
        );

        m_jPanelCentral.add(jPanel15, java.awt.BorderLayout.PAGE_START);
        m_jPanelCentral.add(jLayeredPane1, java.awt.BorderLayout.CENTER);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jContEntries.setFocusable(false);
        m_jContEntries.setRequestFocusEnabled(false);
        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setLayout(new javax.swing.BoxLayout(m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

        m_jNumberKeys.setFocusable(false);
        m_jNumberKeys.setRequestFocusEnabled(false);
        m_jNumberKeys.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                m_jNumberKeysKeyPerformed(evt);
            }
        });
        m_jPanEntries.add(m_jNumberKeys);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel9.setPreferredSize(new java.awt.Dimension(211, 50));
        jPanel9.setLayout(new java.awt.BorderLayout());

        m_jPrice.setDisplayedMnemonic('b');
        m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPrice.setLabelFor(m_jKeyFactory);
        m_jPrice.setAlignmentY(0.0F);
        m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPrice.setIconTextGap(0);
        m_jPrice.setMinimumSize(new java.awt.Dimension(162, 20));
        m_jPrice.setOpaque(true);
        m_jPrice.setPreferredSize(new java.awt.Dimension(162, 40));
        m_jPrice.setRequestFocusEnabled(false);
        jPanel9.add(m_jPrice, java.awt.BorderLayout.LINE_START);

        m_jEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode.png"))); // NOI18N
        m_jEnter.setFocusPainted(false);
        m_jEnter.setFocusable(false);
        m_jEnter.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jEnter.setPreferredSize(new java.awt.Dimension(51, 40));
        m_jEnter.setRequestFocusEnabled(false);
        m_jEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnterActionPerformed(evt);
            }
        });
        jPanel9.add(m_jEnter, java.awt.BorderLayout.LINE_END);

        m_jPanEntries.add(jPanel9);

        m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setBorder(null);
        m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
        m_jKeyFactory.setRequestFocusEnabled(false);
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });
        m_jPanEntries.add(m_jKeyFactory);

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.NORTH);

        jPanel3.setPreferredSize(new java.awt.Dimension(228, 100));

        m_jTax.setFocusable(false);
        m_jTax.setRequestFocusEnabled(false);

        m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPor.setFocusable(false);
        m_jPor.setOpaque(true);
        m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        m_jPor.setRequestFocusEnabled(false);

        m_jaddtax.setText("+");
        m_jaddtax.setFocusPainted(false);
        m_jaddtax.setFocusable(false);
        m_jaddtax.setRequestFocusEnabled(false);

        jLblPrinterStatus.setForeground(new java.awt.Color(255, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
        );

        m_jContEntries.add(jPanel3, java.awt.BorderLayout.CENTER);

        m_jPanTicket.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

        jPanel12.setFocusable(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(600, 27));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("DISCOUNT");
        jPanel12.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2, 80, 23));

        m_jDiscount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jDiscount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jDiscount.setFocusable(false);
        m_jDiscount.setOpaque(true);
        m_jDiscount.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jDiscount.setRequestFocusEnabled(false);
        jPanel12.add(m_jDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 2, 80, 23));

        m_jLblTotalEuros3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros3.setText("SUB TOTAL");
        jPanel12.add(m_jLblTotalEuros3, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 2, 80, 23));

        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros.setFocusable(false);
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);
        jPanel12.add(m_jSubtotalEuros, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 2, 80, 23));

        m_jLblTotalEuros2.setBackground(new java.awt.Color(255, 255, 255));
        m_jLblTotalEuros2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros2.setText("TAXES");
        jPanel12.add(m_jLblTotalEuros2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 2, 60, 23));

        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros.setFocusable(false);
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);
        jPanel12.add(m_jTaxesEuros, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 2, 80, 23));

        jLabel13.setText("SERVICE CHARGE");
        jPanel12.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 2, 110, 23));

        m_jServiceCharge.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jServiceCharge.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jServiceCharge.setFocusable(false);
        m_jServiceCharge.setOpaque(true);
        m_jServiceCharge.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jServiceCharge.setRequestFocusEnabled(false);
        jPanel12.add(m_jServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 2, 85, 23));

        m_jPanTicket.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel4.setPreferredSize(new java.awt.Dimension(1024, 193));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        catcontainer.setLayout(new java.awt.BorderLayout());
        jPanel4.add(catcontainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 715, 160));

        m_jPanTotals1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(204, 204, 204)));
        m_jPanTotals1.setFocusable(false);
        m_jPanTotals1.setMaximumSize(new java.awt.Dimension(1000, 158));
        m_jPanTotals1.setPreferredSize(new java.awt.Dimension(700, 130));
        m_jPanTotals1.setRequestFocusEnabled(false);

        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel11.setPreferredSize(new java.awt.Dimension(988, 15));

        jLabel2.setText("TENDER TYPES");
        jLabel2.setRequestFocusEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .add(11, 11, 11)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 223, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        m_jLblCash.setLabelFor(m_jCash);
        m_jLblCash.setText("CASH");

        m_jLblCard.setLabelFor(m_jCard);
        m_jLblCard.setText("CARD");

        m_jLblCheque.setText("CHEQUE");

        m_jCard.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jCard.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jCard.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jCardFocusLost(evt);
            }
        });

        m_jCheque.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCheque.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jCheque.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jCheque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jChequeFocusLost(evt);
            }
        });

        m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCash.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jCash.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jCash.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jCashFocusLost(evt);
            }
        });

        m_jLblChequeNo.setText("CHEQUE NUMBERS");

        m_jtxtChequeNo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jtxtChequeNo.setPreferredSize(new java.awt.Dimension(123, 25));

        m_jLbVoucher.setText("VOUCHER");

        m_jVoucher.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jVoucher.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jVoucher.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jVoucher.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jVoucherFocusLost(evt);
            }
        });

        m_jLblFoodCoupon.setText("FOOD COUPON");

        m_jFoodCoupon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jFoodCoupon.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jFoodCoupon.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jFoodCoupon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jFoodCouponFocusLost(evt);
            }
        });

        m_jCod.setText("COD");

        m_jCreditAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jCreditAmount.setText("0.00");
        m_jCreditAmount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jCreditAmount.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jCreditAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jCreditAmountFocusLost(evt);
            }
        });

        m_jLblCreditAmount.setText("CREDIT AMOUNT");

        jLabel12.setText("TIPS");

        m_jTxtTips.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jTxtTips.setText("0.00");
        m_jTxtTips.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null));
        m_jTxtTips.setPreferredSize(new java.awt.Dimension(113, 25));

        org.jdesktop.layout.GroupLayout m_jPanTotals1Layout = new org.jdesktop.layout.GroupLayout(m_jPanTotals1);
        m_jPanTotals1.setLayout(m_jPanTotals1Layout);
        m_jPanTotals1Layout.setHorizontalGroup(
            m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotals1Layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotals1Layout.createSequentialGroup()
                        .add(m_jLblCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(9, 9, 9)
                        .add(m_jCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(14, 14, 14)
                        .add(m_jLblFoodCoupon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(15, 15, 15)
                        .add(m_jFoodCoupon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(m_jCod))
                    .add(m_jPanTotals1Layout.createSequentialGroup()
                        .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(m_jPanTotals1Layout.createSequentialGroup()
                                .add(m_jLblCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(29, 29, 29)
                                .add(m_jCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(14, 14, 14)
                                .add(m_jLblChequeNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(5, 5, 5)
                                .add(m_jtxtChequeNo, 0, 1, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, m_jPanTotals1Layout.createSequentialGroup()
                                .add(m_jLblCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(29, 29, 29)
                                .add(m_jCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(14, 14, 14)
                                .add(m_jLbVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(65, 65, 65)
                                .add(m_jVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(18, 18, 18)
                        .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jLblCreditAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(m_jTxtTips, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(m_jCreditAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))))
                .addContainerGap())
            .add(jPanel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
        );
        m_jPanTotals1Layout.setVerticalGroup(
            m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotals1Layout.createSequentialGroup()
                .add(jPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCash, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jLblFoodCoupon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(m_jFoodCoupon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(m_jCod)))
                .add(6, 6, 6)
                .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jLbVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jVoucher, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(m_jLblCreditAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(m_jCreditAmount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotals1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(m_jtxtChequeNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(m_jTxtTips, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jLblCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jLblChequeNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.add(m_jPanTotals1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 714, 160));

        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel7.setMaximumSize(new java.awt.Dimension(700, 158));
        jPanel7.setPreferredSize(new java.awt.Dimension(320, 158));

        m_jLblTotalEuros1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jLblTotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros1.setText("TOTAL SALES");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("TOTAL PAID");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("CHANGE");

        m_jTotalEuros.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setFocusable(false);
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);

        m_jTxtTotalPaid.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jTxtTotalPaid.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTxtTotalPaid.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTxtTotalPaid.setFocusable(false);
        m_jTxtTotalPaid.setOpaque(true);
        m_jTxtTotalPaid.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTxtTotalPaid.setRequestFocusEnabled(false);

        m_jTxtChange.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        m_jTxtChange.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTxtChange.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTxtChange.setFocusable(false);
        m_jTxtChange.setOpaque(true);
        m_jTxtChange.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTxtChange.setRequestFocusEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(m_jLblTotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(19, 19, 19)
                        .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblTotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 1, 780, 160));

        jPanel14.setFocusable(false);
        jPanel14.setPreferredSize(new java.awt.Dimension(1024, 5));
        jPanel14.setRequestFocusEnabled(false);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/footer2.png"))); // NOI18N
        jLabel9.setMaximumSize(new java.awt.Dimension(1450, 33));
        jLabel9.setPreferredSize(new java.awt.Dimension(1450, 37));

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel14Layout.createSequentialGroup()
                .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1526, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 163, 1550, 30));

        m_jPanContainer.add(jPanel4, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
        try {
            stateTransition('\u00a7');
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jNumberKeysKeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_m_jNumberKeysKeyPerformed
        try {
            stateTransition(evt.getKey());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jNumberKeysKeyPerformed

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped

        m_jKeyFactory.setText(null);
        try {
            stateTransition(evt.getKeyChar());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
             String selectedProduct = m_oTicket.getLine(i).getProductID();
          
          /*  try {
                kotTicketlist = dlReceipts.getKot(m_oTicket.getId());
            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }*/
             int kotCount=0;
            try {
                kotCount = dlReceipts.getkotIsprinted(selectedProduct, m_oTicket.getId());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
             if(kotCount!=0){
                JReasonEditor.showMessage(this, dlReceipts, m_oTicket,selectedProduct, i,"lineDelete");
             }
             //if(m_oTicket.getLine(i).getCancelStatus()=="N"){
                   removeTicketLine(i); // elimino la linea
             //}// elimino la linea
            
            m_oTicket.refreshEditTxtFields(0);
          if(m_oTicket.getLinesCount()==0){
              m_jCash.setText("");
              m_jCard.setText("");
              m_jCheque.setText("");
              m_jFoodCoupon.setText("");
              m_jVoucher.setText("");
              m_jtxtChequeNo.setText("");
              m_jTxtChange.setText("");
              m_oTicket.refreshEditTxtFields(0);
              m_jServiceCharge.setText("Rs. 0.00");
              m_jTaxesEuros.setText("Rs. 0.00");
              m_jTotalEuros.setText("Rs. 0.00");
          }else{
                m_oTicket.refreshEditTxtFields(1);
          }
           
        }   

       m_jTxtItemCode.setText("");
       itemName.setText("");
    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed
       
        m_ticketlines.selectionUp();
    
    }//GEN-LAST:event_m_jUpActionPerformed

    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed
     
        m_ticketlines.selectionDown();
     
    }//GEN-LAST:event_m_jDownActionPerformed


    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed

        int i = m_ticketlines.getSelectedIndex();

        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                RetailTicketLineInfo newline = JRetailProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
                if (newline != null) {
                    // line has been modified
                    //    new TicketLineInfo(promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this);
                    // newline.setButton(2);
                    //  newline.setIndex(i);
                    paintTicketLine(i, newline);
                    m_oTicket.refreshEditTxtFields(1);
                    //below line is for promotion products
//                    ProductInfoExt product = getInputProduct();
//                    addTicketLine(product, 1.0, product.getPriceSell());
                }
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }
        }
    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEnterActionPerformed
        try {
            stateTransition('\n');
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_m_jEnterActionPerformed

    private void m_jCboCustNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCboCustNameActionPerformed
        if(action1Performed == true || action2Performed == true || action3Performed == true)
            return;
        action1Performed = true;

        String name = null;
        int index = m_jCboCustName.getSelectedIndex();
      
       String id = null;
       
        if(index!=-1){
         //   loadCusDetails();
            if(typeId==1){
            try {
                    customerListDetails = (ArrayList<CustomerListInfo>) dlSales.getCustomerListName(cusName.getText());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                id = customerListDetails.get(0).getId();
            }else{
                 loadCustomerDetails();
                id = customerList.get(index).getId();
            }
            name = "ID = '"+id+"'";
            try {
                customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerDetails(name);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jCboCustName.setSelectedItem(customerList.get(0).getName());
            if(customerList.get(0).getPhoneNo()==null){
                m_jCboContactNo.setSelectedIndex(-1);
            }else{
                m_jCboContactNo.setSelectedItem(customerList.get(0).getPhoneNo());
            }
            if(customerList.get(0).getCustomerId()==null){
//                m_jCboCusId.setSelectedIndex(-1);
                m_jTxtCustomerId.setText("");
            }else{
                m_jTxtCustomerId.setText(customerList.get(0).getCustomerId());
               // m_jCboCusId.setSelectedItem(customerList.get(0).getCustomerId());
            }

            try {
                  m_oTicket.setCustomer(m_jCboCustName.getSelectedItem() == null ? null : dlSales.loadCustomerExt(customerList.get(0).getId()));
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(customerList.get(0).getIsCreditCustomer()==1){
            m_jCreditAllowed.setVisible(true);
            m_jCreditAllowed.setSelected(true);
        }else{
            m_jCreditAllowed.setVisible(false);
        }
        action1Performed = false;
}//GEN-LAST:event_m_jCboCustNameActionPerformed

    private void m_jCboContactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCboContactNoActionPerformed

        if(action1Performed == true || action2Performed == true || action3Performed == true)
            return;
        action2Performed = true;
        int index = m_jCboContactNo.getSelectedIndex();//-1;
        loadCustomerDetails();
        if(index!=-1){

            String id = customerList.get(index).getId();
            String phoneNo = (String) m_jCboContactNo.getSelectedItem();
            String name = "PHONE = '"+phoneNo+"'";
            try {
                customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerDetails(name);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jTxtCustomerId.setText(customerList.get(0).getCustomerId());
//            m_jCboCusId.setSelectedItem(customerList.get(0).getCustomerId());
            m_jCboCustName.setSelectedItem(customerList.get(0).getName());
            m_jCboContactNo.setSelectedItem(customerList.get(0).getPhoneNo());

             try {
            m_oTicket.setCustomer(m_jCboContactNo.getSelectedItem() == null ? null : dlSales.loadCustomerExt(customerList.get(0).getId()));
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customerList.get(0).getIsCreditCustomer()==1){
            m_jCreditAllowed.setVisible(true);
            m_jCreditAllowed.setSelected(true);
        }else{
            m_jCreditAllowed.setVisible(false);
             m_jCreditAllowed.setSelected(false);
        }
        }
        
       //clearFocus(false);
        action2Performed = false;
}//GEN-LAST:event_m_jCboContactNoActionPerformed

    private void m_jTxtCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtCustomerIdActionPerformed
//if(action1Performed == true || action2Performed == true || action3Performed == true)
//            return;
//        action3Performed = true;
//        int index = m_jCboCusId.getSelectedIndex();//-1;
//        loadCustomerDetails();
//        if(index!=-1){
         //   loadCusDetails();
           String id = m_jTxtCustomerId.getText();
           int customerCount =0;
        try {
            customerCount = dlSales.getCustomerCount(id);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customerCount!=0){
            String name = "CUSTOMERID = '"+id+"'";
            try {
                customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerDetails(name);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jCboCustName.setSelectedItem(customerList.get(0).getName());
            m_jCboContactNo.setSelectedItem(customerList.get(0).getPhoneNo());
            m_jTxtCustomerId.setText(id);

            try {
                m_oTicket.setCustomer( m_jTxtCustomerId.getText() == null ? null : dlSales.loadCustomerExt(customerList.get(0).getId()));
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            //clearFocus(false);
            
        }else{
               showMessage(this, "Please enter the valid customer id");
               m_jTxtCustomerId.setFocusable(true);
        }
        if(customerList.get(0).getIsCreditCustomer()==1){
            m_jCreditAllowed.setVisible(true);
            m_jCreditAllowed.setSelected(true);
        }else{
            m_jCreditAllowed.setVisible(false);
              m_jCreditAllowed.setSelected(false);
        }
           


//        action3Performed = false;
    }//GEN-LAST:event_m_jTxtCustomerIdActionPerformed

    private void m_jTxtItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtItemCodeActionPerformed
        int index = m_jCboItemName.getSelectedIndex();//-1;
       String itemCode = null;
    //   productList = null;
       itemCode = m_jTxtItemCode.getText();
       int itemCount = 0;
        try {
            itemCount = dlSales.getItemCount(itemCode);
        } catch (BasicException ex) {
             showMessage(this, "Please enter the valid item code");
             m_jTxtItemCode.setFocusable(true);
         //   Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
       if(itemCount!=0){

           try {
                productListValue =  (ArrayList<ProductInfoExt>) dlSales.getProductInfoByItemCode(itemCode);
           } catch (BasicException ex) {
               Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
           }

           m_jCboItemName.setSelectedItem(productListValue.get(0).getName());
         incProductByItemDetails(productListValue.get(0).getID());
         m_jTxtItemCode.setText("");
         itemName.setText("");
       }else{
            showMessage(this, "Please enter the valid item code");
             m_jTxtItemCode.setFocusable(true);
       }
       
       //action4Performed = false;// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_m_jTxtItemCodeActionPerformed

    private void m_jCboItemNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_m_jCboItemNameItemStateChanged
        if(evt.getStateChange() == evt.SELECTED){

        }// TODO add your handling code here:
    }//GEN-LAST:event_m_jCboItemNameItemStateChanged

    private void m_jLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLogoutActionPerformed
       //  m_RootApp = null;
         
     
        m_RootApp = (JRootApp) m_App;
//        StartPOS.main(args);
         m_RootApp.closeAppView();
       
      //m_RootApp.tryToClose();
          //StartPOS.main(args);

}//GEN-LAST:event_m_jLogoutActionPerformed

    private void m_jCalculatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCalculatePromotionActionPerformed
        ArrayList<BuyGetPriceInfo> leastProductList = new ArrayList<BuyGetPriceInfo>();
        StringBuilder b = new StringBuilder();
        StringBuilder b1 = new StringBuilder();
        try {
            dlSales.deleteTempTicketlines();
            dlSales.saveRetailTempTicketlines(m_oTicket);
            java.util.ArrayList<String> campaignId = new ArrayList<String>();

            pdtCampaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getPdtCampaignId();
            for(int i=0;i<pdtCampaignIdList.size();i++){
                campaignId.add("'"+pdtCampaignIdList.get(i).getcampaignId()+"'");
            }

            Iterator<?> it = campaignId.iterator();
            while (it.hasNext()) {
                b.append(it.next());
                if (it.hasNext()) {
                    b.append(',');
                }
            }
            String promoCampaignId = b.toString();
            if(!promoCampaignId.equals("")){
                double price =0;
                double taxAmount = 0;
                pdtBuyGetList = (ArrayList<BuyGetInfo>) dlSales.getbuyGetTotalQty(promoCampaignId);
                for(int i=0;i<pdtBuyGetList.size();i++){
                    pdtBuyGetQtyList = (ArrayList<BuyGetQtyInfo>) dlSales.getbuyGetQty(pdtBuyGetList.get(i).getCampaignId(), pdtBuyGetList.get(i).getQty());


                    if(pdtBuyGetQtyList.size()!=0){
                        pdtBuyGetPriceList = (ArrayList<BuyGetPriceInfo>) dlSales.getbuyGetLeastPrice(pdtBuyGetList.get(i).getCampaignId(),  pdtBuyGetQtyList.get(0).getQty());
                        pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                        for(int j=0;j<pdtBuyGetPriceList.size();j++){
                            price = price + pdtBuyGetPriceList.get(j).getPrice();
                            taxAmount = taxAmount + pdtBuyGetPriceList.get(j).getTaxRate();
                            if(pdtLeastPriceList.size()==0){
                                pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                            }else {
                                boolean flag = false;
                                for(int k= 0; k < pdtLeastPriceList.size(); k++){
                                    if(pdtLeastPriceList.get(k).getProductID() == pdtBuyGetPriceList.get(j).getProductID()) {
                                        BuyGetPriceInfo info = pdtLeastPriceList.get(k);
                                        info.setQuantity((info.getQuantity()+1));
                                        pdtLeastPriceList.remove(k);
                                        pdtLeastPriceList.add(info);
                                        flag = true;
                                        break;
                                    }
                                }
                                if(flag == false) {
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                                }
                            }
                        }
                        leastProductList.addAll(pdtLeastPriceList);
                    }

                }
                setPriceInfo(leastProductList);
                m_oTicket.setLeastValueDiscount(price);
                m_oTicket.setTaxValue(taxAmount);

            }
            m_oTicket.billValuePromotion(promoRuleIdList,dlSales);
            printPartialTotals();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
}//GEN-LAST:event_m_jCalculatePromotionActionPerformed

    private void m_jBtnDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDiscountActionPerformed

        String user = m_oTicket.getUser().getName();
        String type = "Editsales";
        try {
            String role = dlCustomers.getRolebyName(user);

            if ("Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role)) {// for admin
                m_oTicket.setdPerson(m_oTicket.getUser().getId());
                JRateEditor.showMessage(JRetailPanelEditPrintTicket.this, dlReceipts, m_oTicket, role,type);
            } else if ((role.equalsIgnoreCase("Cashier")) || (role.equalsIgnoreCase("Syscashier")) ) {//cashier
                JDiscountAssign.showMessage(JRetailPanelEditPrintTicket.this, dlCustomers, m_App, m_oTicket);
            } else {
                JOptionPane.showMessageDialog(jPanel1, "Manager Privilages required for this operation");
            }
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
 // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnDiscountActionPerformed

    private void m_jCashFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jCashFocusLost
        try{
             m_jCash.setText(Formats.DoubleValue.formatValue(new Double(cashAmount)));        // TODO add your handling code here:
        }catch(Exception e){

        }
       
    }//GEN-LAST:event_m_jCashFocusLost

    private void m_jCardFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jCardFocusLost
       try{
            m_jCard.setText(Formats.DoubleValue.formatValue(new Double(cardAmount)));   // TODO add your handling code here:
        }catch(Exception e){

        }
    }//GEN-LAST:event_m_jCardFocusLost

    private void m_jChequeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jChequeFocusLost
      try{
        m_jCheque.setText(Formats.DoubleValue.formatValue(new Double(chequeAmount)));
       }catch(Exception e){

        }
    }//GEN-LAST:event_m_jChequeFocusLost

    private void m_jLastBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLastBillActionPerformed


            //List<LastTicketInfo> linfo;
            FindTicketsInfo fti = null;
            try {
                fti = dlSales.getLatestTicketId();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            RetailTicketInfo ticket = null;
            ServiceChargeTaxInfo serviceTax = null;
            try {
                ticket = dlSales.loadRetailTicket(fti.getTicketType(), fti.getTicketId());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
      //      String paymentId = fti.getPid();
            try {
                serviceTax = dlSales.loadServiceChargeTax(ticket.getDocumentNo());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            ticket.setServiceCharge(serviceTax.getServiceChargeAmt());
            ticket.setServiceChargeRate(serviceTax.getServiceChargerate());
            ticket.setServiceTax(serviceTax.getServiceTaxAmt());
            ticket.setServiceTaxRate(serviceTax.getServiceTaxrate());

            try {
                taxeslogic.calculateTaxes(ticket);
                for (RetailTicketLineInfo line : ticket.getLines()) {
                    line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), ticket.getCustomer(),"N"));
                }
            } catch (TaxesException ex) {
                Logger.getLogger(JPanelButtons.class.getName()).log(Level.SEVERE, null, ex);
            }

           // getPaymentLists(fti.getPid(), ticket);
            if (ticket != null) {
                try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("ticket", ticket);

                 printTicket("Printer.Bill", ticket, m_oTicketExt);
//                try {
//                    // script.put("place", ticketext);
//                    m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.DuplicateBill")).toString());
//                } catch (TicketPrinterException ex) {
//                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
                catch (ScriptException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jLastBillActionPerformed

    private void m_jVoucherFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jVoucherFocusLost
         try{
            m_jVoucher.setText(Formats.DoubleValue.formatValue(new Double(voucherAmount)));// TODO add your handling code here:
        }catch(Exception e){

        }
    }//GEN-LAST:event_m_jVoucherFocusLost

    private void m_jFoodCouponFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jFoodCouponFocusLost
        try{
            m_jFoodCoupon.setText(Formats.DoubleValue.formatValue(new Double(foodCouponAmount)));// TODO add your handling code here:
        }catch(Exception e){

        }
    }//GEN-LAST:event_m_jFoodCouponFocusLost

    private void m_jSearchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSearchCustomerActionPerformed
        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.search(m_oTicket.getCustomer());
        finder.setVisible(true);

        try {
            m_oTicket.setCustomer(finder.getSelectedCustomer() == null
                    ? null
                    : dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()));
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
            msg.show(this);
        }
        if(finder.getSelectedCustomer() != null){
            m_jTxtCustomerId.setText(finder.getSelectedCustomer().getCustomerId());
            m_jCboContactNo.setSelectedItem(finder.getSelectedCustomer().getPhone());
            m_jCboCustName.setSelectedItem(finder.getSelectedCustomer().getName());
        }
        refreshTicket();        // TODO add your handling code here:
}//GEN-LAST:event_m_jSearchCustomerActionPerformed

    private void m_jHomeDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jHomeDeliveryActionPerformed
      if(m_jHomeDelivery.isSelected()){
        jLabel10.setVisible(true);
        m_jDeliveryBoy.setVisible(true);
        jLabel11.setVisible(true);
        m_jTxtAdvance.setVisible(true);
        m_jCod.setVisible(true);
        m_jCreditAmount.setEnabled(false);
     //    getServiceCharge("Y");
      }else{
        jLabel10.setVisible(false);
        m_jDeliveryBoy.setVisible(false);
        jLabel11.setVisible(false);
        m_jTxtAdvance.setVisible(false);
        m_jCod.setVisible(false);
        m_jCreditAmount.setEnabled(true);
        // getServiceCharge("N");
      }
      printPartialTotals();
         // TODO add your handling code here:
    }//GEN-LAST:event_m_jHomeDeliveryActionPerformed

    private void m_jCreditAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jCreditAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCreditAmountFocusLost

    private void m_jCreditAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCreditAllowedActionPerformed
         // m_jCreditAllowed.setSelected(true);        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCreditAllowedActionPerformed

    private void m_jActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jActionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jActionActionPerformed

    private void m_jCboItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCboItemNameActionPerformed
          if(action4Performed == true || action5Performed == true )
            return;
        action5Performed = true;
        String id = null;
        int index = m_jCboItemName.getSelectedIndex();//-1;

        if(index!=-1){
                if(typeId==2){
            try {
                    productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductName(itemName.getText());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                id = productListDetails.get(0).getID();
            }else{
                loadItemList();
                id = productList.get(index).getID();
            }
                try {
                    productList = (ArrayList<ProductInfoExt>) dlSales.getProductInfoById(id);
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                }

                m_jTxtItemCode.setText(productList.get(0).getItemCode());
                m_jCboItemName.setSelectedItem(productList.get(0).getName());
                pdtId = productList.get(0).getID();
        }else{
            itemName.setText(null);
            pdtId = "";
        }


        action5Performed = false;// TODO add your handling code here:// TODO add your handling code here:

}//GEN-LAST:event_m_jCboItemNameActionPerformed

    private void m_jBtnKotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnKotActionPerformed
         String count = null;
         String kotTicketId = null;
         Integer kotCount=0;
         int kotTicket=0;

            try {
                kotTicketId = (dlReceipts.getkotTicketId("Y"));

            } catch (BasicException ex) {
                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(kotTicketId==null){
                kotTicket=1;
            }else{
                   kotTicket=Integer.parseInt(kotTicketId);
                   kotTicket = kotTicket+1;
            }
            k_oInfo = new kotInfo();
            System.out.println("entrr----"+kotTicket);
          //  k_oInfo.setKotId(kotTicket);
             kotInfo kInfolist =  new kotInfo();
             kInfolist.setKotId(kotTicket);


              RetailTicketLineInfo panelTicket = null;
              kotPrintedInfo dbTicket = null;
              Iterator<RetailTicketLineInfo> it1 = null;
              Iterator<kotPrintedInfo> it2 =null;
              RetailTicketInfo info = getActiveTicket();

              java.util.List<kotPrintedInfo> kPrintedInfolist = null;

              java.util.List<RetailTicketLineInfo> panelLines = info.getLines();
        try {
            kPrintedInfolist = dlReceipts.getisPrintedKot(m_oTicket.getId());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

              for(int i=0;i<panelLines.size();i++){
                   boolean verify = false;
                  for(int k=0;k<kPrintedInfolist.size();k++){

                  if(panelLines.get(i).getProductID().equals(kPrintedInfolist.get(k).getProductId()) && kPrintedInfolist.get(k).getisCancelled().equals("N"))
                  {

                      if((panelLines.get(i).getMultiply()-kPrintedInfolist.get(k).getQty())!=0){
                          dlReceipts.insertPrintedKot(m_oTicket.getId(),m_oTicket.getDate(),m_oTicket.getTicketId(),panelLines.get(i).getProductID(), "N",(panelLines.get(i).getMultiply()-kPrintedInfolist.get(k).getQty()) ,kotTicket,m_oTicket.getPlaceId(),panelLines.get(i).getInstruction(),m_oTicket.getUser().getId());
                        }
                      verify = true;
                      break;

                    //  dlReceipts.insertPrintedKot(m_oTicket.getId(),panelLines.get(i).getProductID(), "N", panelLines.get(i).getMultiply(),kotTicket);
                  }
              }
                  if(verify==false){

                      dlReceipts.insertPrintedKot(m_oTicket.getId(),m_oTicket.getDate(),m_oTicket.getTicketId(),panelLines.get(i).getProductID(), "N", panelLines.get(i).getMultiply(),kotTicket,m_oTicket.getPlaceId(),panelLines.get(i).getInstruction(),m_oTicket.getUser().getId());
                  }
              }
               try {
                    kotTicketlist = dlReceipts.getKot(m_oTicket.getId());
                 } catch (BasicException ex) {
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                 }

                  m_oTicket.setKotLines(kotTicketlist);
                  
                  if(kotTicketlist.size()!=0){
                    printKotTicket("Printer.Kot", m_oTicket,kInfolist,m_oTicketExt);
                  }
                  try {
                    dlReceipts.updateKotIsprinted(m_oTicket.getId());
                  } catch (BasicException ex) {
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);

        }
            JRetailTicketsBagRestaurant.setNewTicket();
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnKotActionPerformed

    private void m_jbtnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnPrintBillActionPerformed

        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrint"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
        try {
            m_oTicket.setTicketId(dlSales.getNextTicketIndex().intValue());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        String file;
            file = "Printer.Bill";
        try {
            //            try {
            // reset the payment info
            taxeslogic.calculateTaxes(m_oTicket);
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
          String ticketDocNo = null;
          Integer ticketDocNoInt = null;
          String ticketDocument;
                   try{
                        try {
                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                        } catch (BasicException ex) {
                            Logger.getLogger(JRetailPanelEditPrintTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo=ticketDocNoValue[2];
                        }catch(NullPointerException ex){
                            ticketDocNo = "10000";
                        }
                        if(ticketDocNo!=null){
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                        m_oTicket.setDocumentNo(ticketDocument);
        //if(getPrinterStatus()==true){
            printTicket(file, m_oTicket, m_oTicketExt);
        }else{

        }
        //}        // TODO add your handling code here:
    }//GEN-LAST:event_m_jbtnPrintBillActionPerformed

    private void m_jSettleBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSettleBillActionPerformed
        catcontainer.setVisible(false); // TODO add your handling code here:
        m_jPanTotals1.setVisible(true);
        m_jCash.setFocusable(true);
        m_jCard.setFocusable(true);
        m_jCheque.setFocusable(true);
        m_jtxtChequeNo.setFocusable(true);
        m_jFoodCoupon.setFocusable(true);
        m_jVoucher.setFocusable(true);
        m_jCreditAmount.setFocusable(true);
        stateToPayment();
}//GEN-LAST:event_m_jSettleBillActionPerformed

    private void m_jSplitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSplitBtnActionPerformed
       m_oTicket.setSplitValue("Split");
        if (m_oTicket.getLinesCount() > 0) {
            RetailReceiptSplit splitdialog = RetailReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlSales, dlCustomers, taxeslogic);

            RetailTicketInfo ticket1 = m_oTicket.copyTicket();
            RetailTicketInfo ticket2 = new RetailTicketInfo();
            ticket2.setCustomer(m_oTicket.getCustomer());

            if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {
                    if (closeSplitTicket(ticket2, m_oTicketExt)) {
                        // already checked  that number of lines > 0
                        setRetailActiveTicket(ticket1, m_oTicketExt); // set result ticket
                    }
              
            }
        }
// TODO add your handling code here:
    }//GEN-LAST:event_m_jSplitBtnActionPerformed
 private void printKotTicket(String sresourcename, RetailTicketInfo ticket, kotInfo kot, Object ticketExt) {


        String sresource = dlSystem.getResourceAsXML(sresourcename);

        kot.setkotName("Kitchen Order Ticket");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelEditPrintTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                 script.put("kot", kot);
                  script.put("place", ticketExt);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelEditPrintTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelEditPrintTicket.this);
            }
        }
    }

    public java.util.ArrayList<BuyGetPriceInfo> getPriceInfo(){
        return pdtBuyGetPriceList;
    }
    public void setPriceInfo(java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList){
        this.pdtBuyGetPriceList = pdtBuyGetPriceList;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel catcontainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel jLblPrinterStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton m_jAction;
    private javax.swing.JButton m_jBtnDiscount;
    private javax.swing.JButton m_jBtnKot;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JButton m_jCalculatePromotion;
    private javax.swing.JTextField m_jCard;
    private javax.swing.JTextField m_jCash;
    private javax.swing.JComboBox m_jCboContactNo;
    private javax.swing.JComboBox m_jCboCustName;
    private javax.swing.JComboBox m_jCboItemName;
    private javax.swing.JTextField m_jCheque;
    private javax.swing.JCheckBox m_jCod;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JCheckBox m_jCreditAllowed;
    private javax.swing.JTextField m_jCreditAmount;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JComboBox m_jDeliveryBoy;
    public static javax.swing.JLabel m_jDiscount;
    private javax.swing.JButton m_jDown;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEnter;
    private javax.swing.JTextField m_jFoodCoupon;
    private javax.swing.JCheckBox m_jHomeDelivery;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JButton m_jLastBill;
    private javax.swing.JLabel m_jLbVoucher;
    private javax.swing.JLabel m_jLblCard;
    private javax.swing.JLabel m_jLblCash;
    private javax.swing.JLabel m_jLblCheque;
    private javax.swing.JLabel m_jLblChequeNo;
    private javax.swing.JLabel m_jLblCreditAmount;
    private javax.swing.JLabel m_jLblCurrentDate;
    private javax.swing.JLabel m_jLblFoodCoupon;
    private javax.swing.JLabel m_jLblItemCode;
    private javax.swing.JLabel m_jLblItemName;
    private javax.swing.JLabel m_jLblTime;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JLabel m_jLblUserInfo;
    private javax.swing.JButton m_jLogout;
    private com.openbravo.beans.JNumberKeys m_jNumberKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanTotals1;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JPanel m_jPanelScripts;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JLabel m_jPrice;
    private javax.swing.JButton m_jSearchCustomer;
    public static javax.swing.JLabel m_jServiceCharge;
    private javax.swing.JButton m_jSettleBill;
    private javax.swing.JButton m_jSplitBtn;
    public static javax.swing.JLabel m_jSubtotalEuros;
    private javax.swing.JComboBox m_jTax;
    public static javax.swing.JLabel m_jTaxesEuros;
    public static javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JTextField m_jTxtAdvance;
    private javax.swing.JLabel m_jTxtChange;
    private javax.swing.JTextField m_jTxtCustomerId;
    private javax.swing.JTextField m_jTxtItemCode;
    private javax.swing.JTextField m_jTxtTips;
    private javax.swing.JLabel m_jTxtTotalPaid;
    private javax.swing.JButton m_jUp;
    private javax.swing.JTextField m_jVoucher;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnPrintBill;
    private javax.swing.JButton m_jbtnScale;
    private javax.swing.JTextField m_jtxtChequeNo;
    private com.openbravo.pos.ticket.RetailTicketLineInfo retailTicketLineInfo1;
    // End of variables declaration//GEN-END:variables

}
