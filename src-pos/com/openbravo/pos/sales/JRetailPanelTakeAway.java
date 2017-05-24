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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.ParseException;
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
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.catalog.JCatalogTab;
import com.openbravo.pos.catalog.JRetailCatalog;
import com.openbravo.pos.catalog.JRetailCatalogTab;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppConfig;
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
import com.openbravo.pos.inventory.BeanInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentInterface;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentDetails;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoCheque;
import com.openbravo.pos.payment.PaymentInfoChequeDetails;
import com.openbravo.pos.payment.PaymentInfoComp;
import com.openbravo.pos.payment.PaymentInfoFoodCoupon;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoOtherPayment;
import com.openbravo.pos.payment.PaymentInfoStaff;
import com.openbravo.pos.payment.PaymentInfoVcloud;
import com.openbravo.pos.payment.PaymentInfoVoucher;
import com.openbravo.pos.payment.PaymentInfoVoucherDetails;
import com.openbravo.pos.printer.printer.ImageBillPrinter;
import com.openbravo.pos.printer.printer.ImagePrinter;
import com.openbravo.pos.printer.printer.KotBillPrinter;
import com.openbravo.pos.printer.printer.KotImagePrinter;

import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurant;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.NameTaxMapInfo;

import com.openbravo.pos.ticket.ProductInfoExt;

import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.RoundUtils;
import com.openbravo.pos.util.ThumbNailBuilder;
import com.openbravo.pos.util.ThumbNailBuilderPopularItems;
import com.sysfore.pos.homedelivery.DeliveryBoyInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import com.sysfore.pos.hotelmanagement.BusinessTypeInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import com.sysfore.pos.panels.PosActionsInfo;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import javax.print.PrintService;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author adrianromero
 */
public abstract class JRetailPanelTakeAway extends JPanel implements JPanelView, BeanFactoryApp, RetailTicketsEditor {

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
    protected RetailTicketInfo m_oTicket;
    protected int kotaction = 0;
    protected int kotprintIssue = 0;
    protected Object m_oTicketExt;
    String[] args;
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
    public static DefaultListModel taxModel = null;
    private JRetailTicketsBag m_ticketsbag;
    private SentenceList senttax;
    private ListKeyed taxcollection;
    private SentenceList sentcharge;
    private ListKeyed chargecollection;
    private SentenceList sentsertax;
    private ListKeyed sertaxcollection;
    private SentenceList sentsbtax;
    private ListKeyed sbtaxcollection;
    java.util.List<ServiceChargeInfo> chargelist = null;
    java.util.List<TaxInfo> sertaxlist = null;
    java.util.List<TaxInfo> sbTaxlist = null;
    private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    private static RetailTaxesLogic taxeslogic;
    private static RetailServiceChargesLogic chargeslogic;
    private static RetailSTaxesLogic staxeslogic;
    private static RetailSBTaxesLogic sbtaxeslogic;
    private String editSaleBillId;
    protected JRetailPanelTakeAwayButtons m_jbtnconfig;
    String[] textLines;
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
    public int served = 0;
    public java.util.ArrayList<ProductInfoExt> productListDetails;
    double qty = 0;
    int buttonPlus = 1;
    private Border empty;
    public double productDiscount = 0;
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
    PaymentInfoCash cash = null;
    PaymentInfoCard card = null;
    PaymentInfoVcloud vCloud = null;
    PaymentInfoOtherPayment otherPayment = null;
    PaymentInfoStaff staff = null;
    PaymentInfoComp comp = null;
    // PaymentInfoCheque cheque=null;
    PaymentInfoChequeDetails chequetransaction = null;
    PaymentInfoVoucherDetails voucher = null;
    PaymentInfoFoodCoupon foodCoupon = null;
    private java.util.List<ProductionPrinterInfo> printerInfo;
    public PaymentInfoList m_aPaymentInfo;
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
    //protected kotInfo k_oInfo;
    private java.util.List<KotTicketListInfo> kotlist;
    private java.util.List<RetailTicketLineInfo> kotTicketlist;
    private java.util.List<kotPrintedInfo> kotPrintedlist;
    private Place m_PlaceCurrent;
    static double serviceChargeAmt;
    javax.swing.Timer timer;
    private ThumbNailBuilderPopularItems tnbbutton;
    protected EventListenerList listeners = new EventListenerList();
    public boolean cancelStatus = false;
    String text = new String();
    public final static int INTERVAL = 1000;
    private boolean closePayment = false;
    String roleName = null;
    private int IsSteward = 0;
    private String addonId = null;
    private int primaryAddon = 0;
    Logger logger = Logger.getLogger("MyLog");
    Logger kotlogger = Logger.getLogger("KotLog");
    Logger printlogger = Logger.getLogger("PrintLog");
    Logger settlelogger = Logger.getLogger("SettleLog");
    FileHandler fh1;
    FileHandler fh2;
    FileHandler fh3;
    private String menuStatus = "";
    private String day = null;
    private String menuId = null;
    private java.util.List<MenuInfo> currentMenuList = null;
    private Map<String, DiscountInfo> discountMap = null;
    String storeName = "";
    private RefreshTicket autoRefreshTicket = new RefreshTicket();
    private Timer RefreshTicketTimer = new Timer(1200, autoRefreshTicket);
    private String tableId = null;
    private java.util.ArrayList<PosActionsInfo> posActions;
   

    /**
     * Creates new form JTicketView
     */
    public JRetailPanelTakeAway() {
        initComponents();

        tnbbutton = new ThumbNailBuilderPopularItems(110, 57, "com/openbravo/images/bluetoit.png");
        TextListener txtL;
        // cusName = (JTextField) m_jCboCustName.getEditor().getEditorComponent();
        // cusPhoneNo= (JTextField) m_jCboContactNo.getEditor().getEditorComponent();
        itemName = (JTextField) m_jCboItemName.getEditor().getEditorComponent();
        m_jTxtItemCode.setFocusable(true);
        // m_jTxtCustomerId.setFocusable(true);
        txtL = new TextListener();
        // m_jCreditAllowed.setVisible(false);
        // m_jCreditAllowed.setSelected(false);
//       cusPhoneNo.addFocusListener(txtL);
        //cusName.addFocusListener(txtL);

        itemName.addFocusListener(txtL);
        Action doMorething = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //    m_jPrice.setEnabled(true);
                //     m_jPrice.setFocusable(true);
                m_jKeyFactory.setFocusable(true);
                m_jKeyFactory.setText(null);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        m_jKeyFactory.requestFocus();
                    }
                });

            }
        };

//        InputMap imap = m_jPrice.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        //     imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK), "doMorething");
        //    m_jPrice.getActionMap().put("doMorething",doMorething);

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
                //  cashPayment(1);
            }
        };

        InputMap imapCashPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCashPrint.put(KeyStroke.getKeyStroke("F3"), "docashPrint");
        m_jAction.getActionMap().put("docashPrint",
                docashPrint);
        Action doCardnobill = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //m_jCloseShiftActionPerformed(e);
                //     cardPayment(1);
            }
        };

        InputMap imapCardnoBill = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCardnoBill.put(KeyStroke.getKeyStroke("F4"), "doCardnobill");
        m_jAction.getActionMap().put("doCardnobill", doCardnobill);

        Action doNonChargablePrint = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    closeTicketNonChargable(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                } catch (BasicException ex) {
                    logger.info("exception in closeTicketNonChargable" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
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
//                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        };
        InputMap imapCreditReceipt = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCreditReceipt.put(KeyStroke.getKeyStroke("F5"), "doCreditreceipt");
        m_jAction.getActionMap().put("doCreditreceipt", doCreditreceipt);

        Action doHomeDeliveryNotPaid = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // m_jHomeDelivery.setSelected(true);
                if (getEditSale() == "Edit") {
                    //  getServiceCharge("Y");
                    printPartialTotals();
                    try {
                        closeTicketHomeDelivery(m_oTicket, m_oTicketExt, m_aPaymentInfo);
                    } catch (BasicException ex) {
                        logger.info("exception in closeTicketHomeDelivery" + ex.getMessage());
                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        InputMap imapCardPrint = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapCardPrint.put(KeyStroke.getKeyStroke("F6"), "doHomeDeliveryNotPaid");
        m_jAction.getActionMap().put("doHomeDeliveryNotPaid", doHomeDeliveryNotPaid);



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
//                m_jUpActionPerformed(e);
            }
        };
        InputMap imapUpArrow = m_jPlus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapUpArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "doupArrow");
        m_jPlus.getActionMap().put("doupArrow",
                doupArrow);

//       Action doCustomer = new AbstractAction() {
//       public void actionPerformed(ActionEvent e) {
//
////              m_jTxtCustomerId.setFocusable(true);
//
//        //    cusPhoneNo.setFocusable(true);
//       //     cusName.setFocusable(true);
//            stateToBarcode();
//            }
//        };

//        InputMap imapCustomer= m_jTxtCustomerId.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapCustomer.put(KeyStroke.getKeyStroke("F8"), "doCustomer");
//        m_jTxtCustomerId.getActionMap().put("doCustomer",
//                doCustomer);



        Action doItem = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                itemName.setFocusable(true);
                m_jTxtItemCode.setFocusable(true);
                stateToItem();

            }
        };

        InputMap imapItem = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapItem.put(KeyStroke.getKeyStroke("F9"), "doItem");
        m_jAction.getActionMap().put("doItem",
                doItem);

//        Action doHomeDelivery = new AbstractAction() {
//       public void actionPerformed(ActionEvent e) {
////            m_jHomeDelivery.setFocusable(true);
//        //    m_jHomeDelivery.setSelected(true);
//      //      m_jHomeDeliveryActionPerformed(e);
//      //      m_jTxtAdvance.setFocusable(true);
//            stateToHomeDelivery();
//            }
//        };
//
//        InputMap imapHomeDelivery= m_jHomeDelivery.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        imapHomeDelivery.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.ALT_MASK), "doHomeDelivery");
//        m_jHomeDelivery.getActionMap().put("doHomeDelivery",
//                doHomeDelivery);
//

        Action doPayment = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//           m_jCash.setFocusable(true);
                //       m_jCard.setFocusable(true);
                //       m_jCheque.setFocusable(true);
                //       m_jtxtChequeNo.setFocusable(true);
                //       m_jFoodCoupon.setFocusable(true);
                //      m_jVoucher.setFocusable(true);
                //      m_jCreditAmount.setFocusable(true);
                stateToPayment();
            }
        };

        InputMap imapPayment = m_jAction.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapPayment.put(KeyStroke.getKeyStroke("F11"), "doPayment");
        m_jAction.getActionMap().put("doPayment",
                doPayment);

        Action doDownpArrow = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//               m_jDownActionPerformed(e);
            }
        };

        InputMap imapArrow = m_jMinus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imapArrow.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "doDownpArrow");
        m_jMinus.getActionMap().put("doDownpArrow",
                doDownpArrow);



        itemName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                if (!itemName.getText().equals("") || !itemName.getText().equals(null)) {

                    incProductByItemDetails(pdtId);
                    //  itemName.setText("");
//          m_jCboItemName.setSelectedIndex(-1);

                    ArrayList<String> itemCode = new ArrayList<String>();
                    ArrayList<String> itemName1 = new ArrayList<String>();

                    vItemName.removeAllElements();
                    try {
                        productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (ProductInfoExt product : productListDetails) {
                        itemCode.add(product.getItemCode());
                        itemName1.add(product.getName());
                    }

                    String[] productName = itemName1.toArray(new String[itemName1.size()]);

                    for (int i = 0; i < itemName1.size(); i++) {
                        vItemName.addElement(productName[i]);
                    }
                    itemName = (JTextField) m_jCboItemName.getEditor().getEditorComponent();

                } else {
                    pdtId = "";
                    ArrayList<String> itemCode = new ArrayList<String>();
                    ArrayList<String> itemName1 = new ArrayList<String>();

                    vItemName.removeAllElements();
                    try {
                        productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (ProductInfoExt product : productListDetails) {
                        itemCode.add(product.getItemCode());
                        itemName1.add(product.getName());
                    }

                    String[] productName = itemName1.toArray(new String[itemName1.size()]);
                    for (int i = 0; i < itemName1.size(); i++) {
                        vItemName.addElement(productName[i]);
                    }
                    itemName = (JTextField) m_jCboItemName.getEditor().getEditorComponent();

                }
            }
        });

    }

    public void setPrinterOn() {
        Boolean status;
        if (getPrinterStatus() == true) {
            setPrinterStatus(false);
            jLblPrinterStatus.setText("Printer Off");
        } else {
            setPrinterStatus(true);
            jLblPrinterStatus.setText("Printer On");
        }
    }

    public boolean getPrinterStatus() {
        return printerStatus;
    }

    public void setPrinterStatus(Boolean printerStatus) {
        this.printerStatus = printerStatus;
    }

    public void cashPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter cashPayment");
//       int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();
        cash = new PaymentInfoCash(ticket.getTakeAwayTotal(), ticket.getTakeAwayTotal(), 0);
        if (cash != null) {
            m_aPaymentInfo.add(cash);

        }
        ticket.setPrintStatus(print);
        try {
            logger.info("Before close ticket button");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("After close ticket button");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cashPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   }
    }

    public void otherPayment(int print, RetailTicketInfo ticket, String paymentType, String otherPaymentMode, String description) {
        logger.info("Enter otherPayment");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        otherPayment = new PaymentInfoOtherPayment(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal(), paymentType, otherPaymentMode, description);
        if (otherPayment != null) {
            m_aPaymentInfo.add(otherPayment);


        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void vCloudPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter vCloudPayment");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }A
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        vCloud = new PaymentInfoVcloud(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal());
        if (vCloud != null) {
            m_aPaymentInfo.add(vCloud);


        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //   }

    public void vOtherPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter vOtherPayment");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        vCloud = new PaymentInfoVcloud(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal());
        if (vCloud != null) {
            m_aPaymentInfo.add(vCloud);


        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //   }

    public void cardPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter cardPayment");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        card = new PaymentInfoCard(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal());
        if (card != null) {
            m_aPaymentInfo.add(card);


        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //   }

    public void chequePayment(int print, RetailTicketInfo ticket, String chequeNo) {
//       int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();
        chequetransaction = new PaymentInfoChequeDetails(ticket.getTakeAwayTotal(), ticket.getTakeAwayTotal(), chequeNo);
        if (chequetransaction != null) {
            m_aPaymentInfo.add(chequetransaction);

        }
        ticket.setPrintStatus(print);
        try {
            logger.info("chequePayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("chequePayment after close ticket button action");
            //  activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in chequePayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  }
    }

    public void staffPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter staffPayment method");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        staff = new PaymentInfoStaff(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal());
        if (staff != null) {
            m_aPaymentInfo.add(staff);

        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("staffPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("staffPayment after close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in staffPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //}

    public void complimentaryPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter complimentaryPayment method");
//        int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        m_aPaymentInfo = new PaymentInfoList();

        comp = new PaymentInfoComp(m_oTicket.getTakeAwayTotal(), m_oTicket.getTakeAwayTotal());
        if (comp != null) {
            m_aPaymentInfo.add(comp);

        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("complimentaryPayment Before close ticket button action");
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("complimentaryPayment after close ticket button action");
            // activate();
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in complimentaryPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }  //}
    }

    public void voucherPayment(int print, RetailTicketInfo ticket, String voucherNo) {
        logger.info("Enter voucherPayment method");
        int inventoyCount = 0;
        try {
            inventoyCount = dlSales.getStopInventoryCount();
        } catch (BasicException ex) {
            logger.info("exception in voucherPayment" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (inventoyCount == 1) {
            showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
        } else {
            m_aPaymentInfo = new PaymentInfoList();
            voucher = new PaymentInfoVoucherDetails(ticket.getTakeAwayTotal(), ticket.getTakeAwayTotal(), voucherNo);
            if (voucher != null) {
                m_aPaymentInfo.add(voucher);

            }
            ticket.setPrintStatus(print);
            try {
                logger.info("voucherPayment Before close ticket button action");
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("voucherPayment after close ticket button action");
                //activate();
            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in voucherPayment closeTicketWithButton" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void customerFocus() {
//       m_jTxtCustomerId.setFocusable(true);
//       cusName.setFocusable(true);r
        //   cusPhoneNo.setFocusable(true);
        m_jTxtItemCode.setFocusable(true);
        itemName.setFocusable(true);
//       m_jCash.setFocusable(true);
        //     m_jCard.setFocusable(true);
        //     m_jCheque.setFocusable(true);
        //     m_jtxtChequeNo.setFocusable(true);
//       m_jPrice.setFocusable(true);
        //   m_jPrice.setEnabled(true);
        //    m_jCreditAmount.setFocusable(true);

    }

    public void setEditSale(String editSale) {
        this.editSale = editSale;
    }

    public String getEditSale() {
        return editSale;
    }
//    public void setHomeDeliverySale(String homeDeliverySale) {
//        this.homeDeliverySale = homeDeliverySale;
//    }
//    public String getHomeDeliverySale(){
//        return homeDeliverySale;
//    }

    private void populateDiscount(Map<String, DiscountInfo> discountMap) {
        System.out.println("populateDiscount");
        if (discountMap != null) {
            Set<String> keys = discountMap.keySet();
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_oTicket.getLine(i).setDiscountrate("");
                // String catId=m_oTicket.getLine(i).getProductCategoryID();
                // String parentCatId=dlReceipts.getParentCategory(catId);
                String parentCatId = m_oTicket.getLine(i).getParentCatId();
                String discount = "";
                //Checking whether discount assigned for any product category 
                if (keys.contains(parentCatId)) {
                    //if its a rate
                    if (!discountMap.get(parentCatId).getDiscountRate().equals("") && !discountMap.get(parentCatId).getDiscountRate().equals(null)) {
                        discount = discountMap.get(parentCatId).getDiscountRate();
                        m_oTicket.getLine(i).setDiscountrate(discount);
                    }//if its a amount 
                    else {
                        discount = discountMap.get(parentCatId).getDiscountValue();
                        m_oTicket.getLine(i).setDiscountrate(discount);
                    }
                    //  m_oTicket.setLineDiscountRate(m_oTicket.getLine(i),discount);
                }
            }
        }
    }

    class TextListener implements FocusListener {

        public void focusLost(FocusEvent e) {
            if (e.getSource() == cusName) {
                cusName.setFocusable(true);


            } else if (e.getSource() == cusPhoneNo) {
            }
//            else if(e.getSource() == itemName){
//
//            }

        } // close focusLost()

        public void focusGained(FocusEvent e) {
            final String type;
            if (e.getSource() == cusName) {
                type = "c";
                //setModel(new DefaultComboBoxModel(vCusName), "");
                cusName.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = cusName.getText();
                                if (text.length() == 0) {
                                    //         m_jCboCustName.hidePopup();
                                    setModel(new DefaultComboBoxModel(vCusName), "", type);
                                } else {
                                    typeId = 1;
                                    Vector<String> vCusName = new Vector<String>();
                                    ArrayList<String> cusNames = new ArrayList<String>();
                                    ArrayList<String> cusPhoneNo = new ArrayList<String>();
                                    ArrayList<String> cusId = new ArrayList<String>();
                                    try {
                                        customerListDetails = (ArrayList<CustomerListInfo>) dlSales.getCustomerListName(text);
                                    } catch (BasicException ex) {
                                        logger.info("exception in focusGained" + ex.getMessage());
                                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    for (CustomerListInfo cus : customerListDetails) {
                                        cusNames.add(cus.getName());
                                        cusPhoneNo.add((cus.getPhoneNo()));
                                        cusId.add((cus.getCustomerId()));
                                    }
                                    String[] customerNames = cusNames.toArray(new String[cusNames.size()]);
                                    for (int i = 0; i < cusNames.size(); i++) {
                                        vCusName.addElement(customerNames[i]);
                                    }
                                    DefaultComboBoxModel m = getCustNameSuggestedModel(vCusName, text);
                                    if (m.getSize() == 0 || hide_flag) {
//                        m_jCboCustName.hidePopup();
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
                                        //               m_jCboCustName.showPopup();
                                    }
                                }
                            }
                        });
                    }

                    public void keyPressed(KeyEvent e) {
                        String text = cusName.getText();
                        int code = e.getKeyCode();

                        if (code == KeyEvent.VK_ENTER) {
                            if (!vCusName.contains(text)) {
                                vCusName.addElement(text);
                                Collections.sort(vCusName);
                                setModel(getCustNameSuggestedModel(vCusName, text), text, type);

                            }
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < vCusName.size(); i++) {
                                String str = vCusName.elementAt(i);
                                if (str.startsWith(text)) {
//                        m_jCboCustName.setSelectedIndex(-1);
                                    cusName.setText(str);
                                    return;
                                }
                            }
                        }
                    }
                });
            } else if (e.getSource() == cusPhoneNo) {
                type = "n";
                cusPhoneNo.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = cusPhoneNo.getText();
                                if (text.length() == 0) {
//                    m_jCboContactNo.hidePopup();
                                    setModel(new DefaultComboBoxModel(vCusPhoneNo), "", type);
                                } else {
                                    DefaultComboBoxModel m = getContactNoSuggestedModel(vCusPhoneNo, text);
                                    if (m.getSize() == 0 || hide_flag) {
                                        //            m_jCboContactNo.hidePopup();
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
                                        //        m_jCboContactNo.showPopup();
                                    }
                                }
                            }
                        });
                    }

                    public void keyPressed(KeyEvent e) {
                        String text = cusPhoneNo.getText();
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_ENTER) {
                            if (!vCusPhoneNo.contains(text)) {
                                vCusPhoneNo.addElement(text);
                                Collections.sort(vCusPhoneNo);
                                setModel(getContactNoSuggestedModel(vCusPhoneNo, text), text, type);
                            }
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < vCusPhoneNo.size(); i++) {
                                String str = vCusPhoneNo.elementAt(i);
                                if (str.startsWith(text)) {
                                    //     m_jCboContactNo.setSelectedIndex(-1);
                                    cusPhoneNo.setText(str);
                                    return;
                                }
                            }
                        }
                    }
                });

            } else if (e.getSource() == itemName) {
                type = "m";
                itemName.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = itemName.getText();
                                if (text.length() == 0) {
                                    m_jCboItemName.hidePopup();
                                    setModel(new DefaultComboBoxModel(vItemName), "", type);
                                } else {
                                    typeId = 2;
                                    ArrayList<String> itemCode = new ArrayList<String>();
                                    ArrayList<String> itemName = new ArrayList<String>();

                                    Vector<String> vItemName = new Vector<String>();
                                    try {
                                        productList = (ArrayList<ProductInfoExt>) dlSales.getProductName(text);
                                    } catch (BasicException ex) {
                                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    for (ProductInfoExt product : productList) {
                                        itemCode.add(product.getItemCode());
                                        itemName.add(product.getName());
                                    }

                                    String[] productName = itemName.toArray(new String[itemName.size()]);
                                    for (int i = 0; i < itemName.size(); i++) {
                                        vItemName.addElement(productName[i]);
                                    }
                                    DefaultComboBoxModel m = getItemSuggestedModel(vItemName, text);
                                    if (m.getSize() == 0 || hide_flag) {
                                        m_jCboItemName.hidePopup();
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
                                        m_jCboItemName.showPopup();
                                    }
                                }
                            }
                        });
                    }

                    public void keyPressed(KeyEvent e) {
                        String text = itemName.getText();
                        int code = e.getKeyCode();
                        if (code == KeyEvent.VK_ENTER) {
                            if (!vItemName.contains(text)) {
                                vItemName.addElement(text);
                                Collections.sort(vItemName);
                                setModel(getItemSuggestedModel(vItemName, text), text, type);
                            }
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_ESCAPE) {
                            hide_flag = true;
                        } else if (code == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < vItemName.size(); i++) {
                                String str = vItemName.elementAt(i);
                                if (str.startsWith(text)) {
//                    m_jCboItemName.setSelectedIndex(-1);
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
//            m_jbtnScale.setVisible(false);
        }
        if (m_App.getProperties().getProperty("machine.ticketsbag").equals("restaurant")) {
            m_jbtnPrintBill.setVisible(true);
        } else {
            m_jbtnPrintBill.setVisible(false);
            // jPanel1.setPreferredSize(new java.awt.Dimension(282, 47));
        }
        customerFocus();
        menuStatus = m_App.getProperties().getProperty("machine.menustatus");
        //setPrinterStatus(false);
        m_jPor.setVisible(false);
        m_ticketsbag = getJTicketsBag();
        //m_oTicket.setSplitValue("");
        m_oTicket.setCancelTicket(false);
        System.out.println("init----m_oTicket.getCancelTicket()--" + m_oTicket.getCancelTicket());
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);

        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JRetailTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);

        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        // Los botones configurables...
        m_jbtnconfig = new JRetailPanelTakeAwayButtons("Ticket.Buttons", this);
        m_jButtonsExt.add(m_jbtnconfig);

        // El panel de los productos o de las lineas...        
        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);
        catcontainer.setVisible(true);
        m_jCalculatePromotion.setVisible(false);
        // El modelo de impuestos
        senttax = dlSales.getRetailTaxList();
        sentcharge = dlSales.getRetailServiceChargeList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        sentsertax = dlSales.getRetailServiceTaxList();
        sentsbtax = dlSales.getRetailSwachBharatTaxList();
        taxcategoriesmodel = new ComboBoxValModel();

        // ponemos a cero el estado
        stateToZero();

        //creating log file for kot
        String logpath = m_App.getProperties().getProperty("machine.kotlogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh1 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        kotlogger.addHandler(fh1);
        SimpleFormatter formatter = new SimpleFormatter();
        fh1.setFormatter(formatter);


        //creating log file for print bill

        logpath = m_App.getProperties().getProperty("machine.printlogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh2 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        printlogger.addHandler(fh2);
        fh2.setFormatter(formatter);

        //creating log file for settle bill
        logpath = m_App.getProperties().getProperty("machine.settlelogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh3 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        settlelogger.addHandler(fh3);
        fh3.setFormatter(formatter);

//       m_jCash.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//        m_jCard.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//        m_jCheque.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//    m_jFoodCoupon.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//    m_jVoucher.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
//     m_jCreditAmount.getDocument().addDocumentListener(new DocumentListener() {
//         public void changedUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void removeUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//         public void insertUpdate(DocumentEvent e) {
//           setTenderAmount();
//         }
//
//
//    });
    }

    public void custItemLoad() {
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
            logger.info("exception in custItemLoad" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusId.add((cus.getCustomerId()));
            //  cusName1.add(cus.getName());
        }
        vCusName.removeAllElements();
        String[] customerNames = cusNames.toArray(new String[cusNames.size()]);
        for (int i = 0; i < cusNames.size(); i++) {
            vCusName.addElement(customerNames[i]);
        }
        vCusPhoneNo.removeAllElements();
        String[] customerPhoneNo = cusPhoneNo.toArray(new String[cusPhoneNo.size()]);
        for (int i = 0; i < cusPhoneNo.size(); i++) {
            vCusPhoneNo.addElement(customerPhoneNo[i]);
        }


        ArrayList<String> itemCode = new ArrayList<String>();
        ArrayList<String> itemName = new ArrayList<String>();

        vItemName.removeAllElements();
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in productlist" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productList) {
            itemCode.add(product.getItemCode());
            itemName.add(product.getName());
        }
//          }

        String[] productName = itemName.toArray(new String[itemName.size()]);

        for (int i = 0; i < itemName.size(); i++) {
            vItemName.addElement(productName[i]);
        }
    }

    public void setTenderAmount() {
//     if (m_jCash.getText()!=null || m_jCard.getText()!=null || m_jCheque.getText()!=null || m_jFoodCoupon.getText()!=null || m_jVoucher.getText()!=null || m_jCreditAmount.getText()!=null){
//
//         Pattern pNum = Pattern.compile("[+-]?[\\d,]+\\.?\\d+");
//         Pattern pNum1 = Pattern.compile(".");
//         if(m_jCash.getText().equals("")){
//             cashAmount = 0;
//        }else{
//             try{
//                cashAmount =Double.parseDouble(m_jCash.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid cash amount");
//
//             }
//         }
//
//         if(m_jCard.getText().equals("")){
//             cardAmount = 0;
//         }else{
//              try{
//                cardAmount = Double.parseDouble(m_jCard.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid card amount");
//             }
//
//         }
//         if(m_jCheque.getText().equals("")){
//             chequeAmount = 0;
//         }else{
//              try{
//                chequeAmount = Double.parseDouble(m_jCheque.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid cheque amount");
//             }
//
//         }
//         if(m_jFoodCoupon.getText().equals("")){
//             foodCouponAmount = 0;
//         }else{
//              try{
//                foodCouponAmount = Double.parseDouble(m_jFoodCoupon.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid food coupon amount");
//             }
//
//         }
//         if(m_jVoucher.getText().equals("")){
//             voucherAmount = 0;
//         }else{
//              try{
//                voucherAmount = Double.parseDouble(m_jVoucher.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid voucher amount");
//             }
//
//         }
//        if(m_jCreditAmount.getText().equals("")){
//             creditAmount = 0;
//         }else{
//              try{
//                creditAmount = Double.parseDouble(m_jCreditAmount.getText());
//             }catch (NumberFormatException ex){
//                 showMessage(this, "Please enter the valid credit amount");
//             }
//
//         }
//
//         totalAmount = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount;
//         totalBillValue = cashAmount+chequeAmount+cardAmount+foodCouponAmount+voucherAmount+creditAmount;
//         m_jTxtTotalPaid.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(totalAmount))));
//         try{
//          if(m_oTicket.getTotal()< totalBillValue){
//              double change = totalBillValue - Math.ceil(m_oTicket.getTotal());
//              m_jTxtChange.setText(Formats.CURRENCY.formatValue(new Double(Double.toString(change))));
//          }else{
//             m_jTxtChange.setText("");
//          }
//         }catch(NullPointerException ex){
//
//         }
//
//     }
    }

    private void setModel(DefaultComboBoxModel mdl, String str, String type) {
        if (type == "c") {
//                m_jCboCustName.setModel(mdl);
            cusName.setText(str);
        } else if (type == "n") {
            //   m_jCboContactNo.setModel(mdl);
            cusPhoneNo.setText(str);
        } else {
//                m_jCboItemName.setModel(mdl);
//                itemName.setText(str);
        }
    }

    private static DefaultComboBoxModel getSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            // if(s.startsWith(text))
            m.addElement(s);
        }
        return m;
    }

    private static DefaultComboBoxModel getCustNameSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {

            // if(s.startsWith(text))
            String nameLength = cusName.getText();
            if (nameLength.length() > 2) {
                m.addElement(s);
            }
        }
        return m;
    }

    private static DefaultComboBoxModel getContactNoSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            String phoneLength = null;

            phoneLength = cusPhoneNo.getText();
            //    if(s.startsWith(text))
            if (phoneLength.length() > 5) {
                if (s.startsWith(text)) {
                    m.addElement(s);
                }
            }
        }
        return m;

    }

    private static DefaultComboBoxModel getItemSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            String itemLength = null;

            itemLength = itemName.getText();
            // if(s.startsWith(text))
            if (itemLength.length() > 2) {
                //if(s.startsWith(text))
                m.addElement(s);
            }
        }
        return m;

    }

    public void paymentDetail(double cashAmount, double cardAmount, double voucherAmount, double vCloudAmount, String cardType, String description) {
        m_aPaymentInfo = new PaymentInfoList();


        totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount + vCloudAmount;
        double change = totalAmount - m_oTicket.getTakeAwayTotal();
        cash = new PaymentInfoCash(m_oTicket.getTakeAwayTotal(), cashAmount, change);
        if (cash != null) {
            m_aPaymentInfo.add(cash);

        }
        otherPayment = new PaymentInfoOtherPayment(m_oTicket.getTakeAwayTotal(), cardAmount, "Card", cardType, description);
        if (otherPayment != null) {
            m_aPaymentInfo.add(otherPayment);
        }
        voucher = new PaymentInfoVoucherDetails(m_oTicket.getTakeAwayTotal(), voucherAmount);
        if (voucher != null) {
            m_aPaymentInfo.add(voucher);
        }
        vCloud = new PaymentInfoVcloud(m_oTicket.getTakeAwayTotal(), vCloudAmount);
        if (vCloud != null) {
            m_aPaymentInfo.add(vCloud);
        }

        try {
            closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void paymentDetail(double cashAmount, double cardAmount) {
        m_aPaymentInfo = new PaymentInfoList();


        totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount;
        double change = totalAmount - m_oTicket.getTakeAwayTotal();
        cash = new PaymentInfoCash(m_oTicket.getTakeAwayTotal(), cashAmount, change);
        if (cash != null) {
            m_aPaymentInfo.add(cash);

        }
        card = new PaymentInfoCard(m_oTicket.getTakeAwayTotal(), cardAmount);
        if (card != null) {
            m_aPaymentInfo.add(card);


        }
        try {
            closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCusDetails() {
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();


        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in loadCusDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }


        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }
//        m_jCboCustName.setModel(new ComboBoxValModel(cusNames));
        //  m_jCboContactNo.setModel(new ComboBoxValModel(cusPhoneNo));


    }

    private void loadCustomerDetails() {
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in loadCustomerDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }


        for (CustomerListInfo cus : customerList) {
            cusNames.add(cus.getName());
            cusPhoneNo.add((cus.getPhoneNo()));
            cusName1.add(cus.getName());
        }

    }

    private void loadItemDetails() {
        ArrayList<String> itemName = new ArrayList<String>();

        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in loadItemDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productList) {
            itemName.add(product.getName());
        }
        m_jCboItemName.setModel(new ComboBoxValModel(itemName));
    }

    private void loadItemList() {
        try {
            productList = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
        } catch (BasicException ex) {
            logger.info("exception in loadItemList" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public Object getBean() {
        return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public void activate() {
        logger.info("inside activate method");
        m_jServiceTax.setVisible(false);
        m_jServiceTaxLbl.setVisible(false);
        m_jSwachBharat.setVisible(false);
        m_jSwachBharatLbl.setVisible(false);
        //     m_jServiceCharge.setVisible(false);
        //    m_jServiceChargeLbl.setVisible(false);
        custItemLoad();
        System.out.println(" before in map testing 4" + m_oTicket);
        showProductPanel();
        enablePosActions();
        m_jbtnScale.setVisible(false);
        jLblPrinterStatus.setText("");
        m_jBtnBillOnHold.setVisible(false);
        m_jSplitBtn.setVisible(false);
        m_jBtnCancelBill.setVisible(false);
        // m_oTicket.setSplitValue("");
//        String servedStatus = m_App.getProperties().getProperty("machine.servedstatus");
//        if (servedStatus.equals("true")) {
//            m_jBtnServed.setVisible(true);
//        } else {
            m_jBtnServed.setVisible(false);
            m_jKot.setVisible(false);
       // }
        //  m_jBtnCancelBill.setVisible(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        //Added new logic to change tax calculations based on store name
        storeName = m_App.getProperties().getProperty("machine.StoreName");
        populateDeliveryBoy();
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(m_App);
        //    jLabel10.setVisible(false);
        //    m_jDeliveryBoy.setVisible(false);
//        jLabel11.setVisible(false);
        //   m_jTxtAdvance.setVisible(false);
//        m_jCod.setVisible(false);
        //    m_jCreditAllowed.setVisible(false);
        //      m_jCreditAmount.setText("0.00");
        //      m_jHomeDelivery.setSelected(false);
        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            logger.info("exception in activate taxcategorieslist" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcategoriescollection = new ListKeyed<TaxCategoryInfo>(taxcategorieslist);

        taxcategoriesmodel = new ComboBoxValModel(taxcategorieslist);
        m_jTax.setModel(taxcategoriesmodel);

        String taxesid = m_jbtnconfig.getProperty("taxcategoryid");
        if (taxesid == null) {
            if (m_jTax.getItemCount() > 0) {
                m_jTax.setSelectedIndex(0);
            }
        } else {
            taxcategoriesmodel.setSelectedKey(taxesid);
        }

        taxeslogic = new RetailTaxesLogic(taxlist, m_App);
        String businessTypeId = null;


        //   getServiceCharge("Order Taking");

        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist, m_App);

        try {
            sertaxlist = sentsertax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        sertaxcollection = new ListKeyed<TaxInfo>(sertaxlist);
        staxeslogic = new RetailSTaxesLogic(sertaxlist, m_App);
        try {
            sbTaxlist = sentsbtax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sbtaxcollection = new ListKeyed<TaxInfo>(sbTaxlist);
        sbtaxeslogic = new RetailSBTaxesLogic(sbTaxlist, m_App);

        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }

        // Authorization for buttons
//        btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        //   m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
//        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        //     m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));

        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());
        logger.info("before m_ticketsbag activate method");

        m_ticketsbag.activate();
        String day = getWeekDay();
        logger.info("after m_ticketsbag activate method");
//        java.util.ArrayList<String> campaignId = new ArrayList<String>();
//        try {
//            campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        promoDetails = new PromoRuleInfo();
//        if(campaignIdList.size()!=0){
//        for(int i=0;i<campaignIdList.size();i++){
//         campaignId.add("'"+campaignIdList.get(i).getcampaignId()+"'");
//     }
//
//         StringBuilder b = new StringBuilder();
//         Iterator<?> it = campaignId.iterator();
//         while (it.hasNext()) {
//         b.append(it.next());
//         if (it.hasNext()) {
//            b.append(',');
//          }
//        }
//        String Id = b.toString();
//        if(campaignIdList!=null){
//                try {
//                    promoRuleIdList = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);
//                } catch (BasicException ex) {
//                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//        }
//
//        }
        String role = m_App.getAppUserView().getUser().getRole();
        try {
            roleName = dlReceipts.getRoleByUser(role);
        } catch (BasicException ex) {
            logger.info("exception in roleName" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("roleName in activate" + roleName);
//            if(roleName.equals("Steward"))
//            {
//                m_jSettleBill.setVisible(false);
//                m_jSplitBtn.setVisible(false);
//                m_jbtnPrintBill.setVisible(false);
//                m_jBtnDiscount.setVisible(false);
//
//            }else if(roleName.equals("Bartender")){
//               m_jSettleBill.setVisible(false);
//               m_jSplitBtn.setVisible(false);
//                m_jbtnPrintBill.setVisible(true);
//               m_jBtnDiscount.setVisible(false);
//            }else{
//                 m_jSettleBill.setVisible(true);
//                m_jSplitBtn.setVisible(true);
//                m_jbtnPrintBill.setVisible(true);
//                m_jLogout.setVisible(true);
//                m_jBtnDiscount.setVisible(true);
//            }
//
        logger.info("After main activate method");
    }

    public void getServiceCharge(String isHomeDelivery) {
        String businessTypeId = null;
        int businessTypeCount = 0;
        try {
            businessTypeCount = dlSales.getBusinessTypeCount(isHomeDelivery);
        } catch (BasicException ex) {
            logger.info("exception in getServiceCharge" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (businessTypeCount == 1) {
            try {
                businessTypeId = dlSales.getBusinessTypeId(isHomeDelivery);
            } catch (BasicException ex) {
                logger.info("exception in businessTypeId" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serviceTaxList = (ArrayList<BusinessServiceTaxInfo>) dlSales.getBusinessServiceTax(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceTaxList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceChargeList = (ArrayList<BusinessServiceChargeInfo>) dlSales.getBusinessServiceCharge(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceChargeList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //method is called on logout action and closing the application
    public boolean deactivate() {
        int i = 0;
        if (m_oTicket != null) {
            logger.info("within deactivate ");
            //if no kot done    
            if (m_oTicket.getOrderId() == 0) {
                logger.info("Order No." + m_oTicket.getOrderId() + "deleting 0 order no. Bill in deactivate method");
                m_ticketsbag.deleteTicket();
            }//if kot done but cancelled all lines
            else if (m_oTicket.getOrderId() != 0 && m_oTicket.getLinesCount() == 0) {
                m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                m_oTicket.setActiveCash(m_App.getActiveCashIndex());
                m_oTicket.setActiveDay(m_App.getActiveDayIndex());
                m_oTicket.setDate(new Date()); //
                String ticketDocument;
                ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_oTicket.getTicketId();
                String reason = "Splitted with zero lines/cancelled all kot lines";
                try {
                    dlSales.saveRetailCancelTicket(m_oTicket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                } catch (BasicException ex) {
                    logger.info("ORDER NO. " + m_oTicket.getOrderId() + "exception in saveRetailCancelTicket in deactivate method" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (m_oTicket.getSplitValue().equals("")) {
                    logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot bill in deactivate method");
                    m_ticketsbag.deleteTicket();
                } else {
                    try {
                        logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot splitted Bill in deactivate method");
                        dlReceipts.deleteSharedSplitTicket(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                    } catch (BasicException ex) {
                        logger.info("ORDER NO. " + m_oTicket.getOrderId() + "exception in deleteSharedSplitTicket in deactivate method" + ex.getMessage());
                        Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }//delete all non kot items
            else {
                //    if(IsSteward==0){
                while (i < m_oTicket.getLinesCount()) {
                    if (m_oTicket.getLine(i).getIsKot() == 0) {
                        removeTicketLine(i);
                        i = 0;
                    } else {
                        i++;
                    }
                }
                m_oTicket.refreshTxtTakeAwayFields(0);
                if (m_oTicket.getLinesCount() == 0) {
                    logger.info("Order No." + m_oTicket.getOrderId() + "deleting zero lines Bill in deactivate method");
                    m_ticketsbag.deleteTicket();
                }
                //  }
            }
        }
        RefreshTicketTimer.stop();
        System.out.println("TIMER STOPPED ");
        return m_ticketsbag.deactivate();
    }

    protected abstract JRetailTicketsBag getJTicketsBag();

    protected abstract Component getSouthComponent();

    protected abstract void resetSouthComponent();

    protected abstract void resetSouthComponent(String value);

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt) {
        m_oTicket = oTicket;
        m_jCboItemName.setEnabled(true);
        m_jTxtItemCode.setEnabled(true);
        m_oTicketExt = oTicketExt;
        kotprintIssue = 0;
//          m_jPanTotals1.setVisible(false);
        catcontainer.setVisible(true);
        if (m_oTicket != null) {
            JRetailTicketsBagRestaurant.stopReloadTimer();
            m_oTicket.setM_App(m_App);
            tableId = m_oTicket.getPlaceId();
            RefreshTicketTimer.start();
            kotaction = 0;
            m_oTicket.setTableName("");
            try {
                logger.info("No. of Running Tables : " + dlReceipts.getSharedTicketCount());
            } catch (BasicException ex) {
                logger.info("exception in getSharedTicketCount" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (m_oTicket.getSplitValue().equals("Split")) {
                m_oTicket.setTicketOpen(true);
                m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
                System.out.println(m_oTicket.getSplitSharedId() + m_oTicket.getSplitSharedId());
                try {
                    dlReceipts.updateSharedTicket(m_oTicket.getPlaceId(), m_oTicket);
                } catch (BasicException ex) {
                    logger.info("exception in setRetailActiveTicket while updatesharedticket" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("split value testing issue IN ACTIVE TICKET " + m_oTicket.getSplitValue());
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.
            String accDate = new SimpleDateFormat("yyyy-MM-dd").format(m_App.getActiveDayDateStart());
            Date dateValue = java.sql.Date.valueOf(accDate);
            m_oTicket.setAccountDate(dateValue);
//           int orderId = 0;
//            if(m_oTicket.getOrderId() == 0){
//                try {
//                    orderId = dlSales.getNextTicketOrderNumber();
//                } catch (BasicException ex) {
//                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                m_oTicket.setOrderId(orderId);
//            }      

            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                System.out.println("line testing" + line.getPreparationStatus());
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setRetailTakeAwayPanel(this);

            }

            m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
//             if(m_oTicket.getParentId()!=null)
//             {
//                 setKotAndServedOnSplit(m_oTicket);
//             }

        }



        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");


        refreshTicket();

    }

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt, String editBillId) {
        this.editSaleBillId = editBillId;
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        if (getEditSale() == "Edit") {
            //     m_jLastBill.setVisible(false);
            m_jEraser.setVisible(false);
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
                line.setRetailTakeAwayPanel(this);

            }

        }
//        if(m_jCboCustName.getSelectedItem()!=null){
//            m_jCboCustName.setSelectedItem("");
//            m_jCboContactNo.setSelectedItem("");
//            m_jTxtCustomerId.setText("");
//            m_jCboContactNo.setSelectedItem("");
//        }
        if (m_jTxtItemCode.getText() != null) {
            //   m_jCboItemCode.setSelectedItem("");
            m_jTxtItemCode.setText("");
            m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
        }
        //   m_jCash.setText("");
//        m_jCheque.setText("");
        //    m_jCard.setText("");
        //    m_jtxtChequeNo.setText("");
        //    m_jFoodCoupon.setText("");
        //    m_jVoucher.setText("");

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
        CardLayout cl = (CardLayout) (getLayout());
        customerFocus();
        if (m_oTicket == null) {
            m_ticketlines.clearTicketLines();

            m_jSubtotalEuros1.setText(null);
            m_jTaxesEuros1.setText(null);
            m_jTotalEuros.setText(null);

            stateToZero();

            // Muestro el panel de nulos.
            cl.show(this, "null");
            // resetSouthComponent();  //commented to test performance

        } else {
            if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
            }


            String name = m_oTicket.getName(m_oTicketExt);
//             if(name.contains("-")){
//
//                String customer[]= null;
//                customer =name.split("-");
//                cusName.setText(customer[0]);
//            }else{
//                  cusName.setText("");
//            }
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();

            // Muestro el panel de tickets.
            cl.show(this, "ticket");
            //   resetSouthComponent();   //commented to test performance

            // activo el tecleador...
            m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
            m_jLblCurrentDate.setText(getDateForMe().toString());
            m_jUser.setText((m_oTicket.getUser()).getName());
            m_jLblTime.setText(getTime().toString() + "        ");
//           m_jTable.setText(m_oTicket.getName(m_oTicketExt) );
        }
    }

    public String getLogDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public String getDateForMe() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date m_dDate = new Date();
        StringBuffer strb = new StringBuffer();
        strb.append("DATE: ");
        return strb.append(sdf.format(m_dDate).toString()).toString();
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date m_dDate = new Date();
        StringBuffer strb = new StringBuffer();
        strb.append("LOGGED IN TIME: ");
        return strb.append(sdf.format(m_dDate).toString()).toString();
    }

    private void showProductPanel() {

        // Load product panel
        java.util.List<ProductInfoExt> product = null;
        try {
            if (menuStatus.equals("false")) {
                product = dlSales.getPopularProduct("Y");
            } else {
                day = getWeekDay();
                currentMenuList = dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                product = dlSales.getMenuPopularProduct("Y", menuId);
            }
            //  m_productsset.put(id, product);
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in showProductPanel" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }


        JRetailCatalogTab jcurrTab = new JRetailCatalogTab();
        jcurrTab.applyComponentOrientation(getComponentOrientation());
        m_jProducts.add(jcurrTab, "PRODUCT.");

        // Add products
        for (ProductInfoExt prod : product) {
            tnbbutton = new ThumbNailBuilderPopularItems(97, 57, "com/openbravo/images/bluepopulartoit.png");
            jcurrTab.addPopularItemButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
        }

        // selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

        CardLayout cl = (CardLayout) (m_jProducts.getLayout());
        cl.show(m_jProducts, "PRODUCT.");

    }

    private String getProductLabel(ProductInfoExt product) {

        // if (pricevisible) {
        // if (taxesincluded) {
        //   TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID());
        //   return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax(tax);
        // } else {
        //     return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
        //  }
        // } else {
        return "<html><center>" + product.getName() + "<br>";
        // }
    }

    private class SelectedAction implements ActionListener {

        private ProductInfoExt prod;

        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }

        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);
        }
    }

    protected void fireSelectedProduct(ProductInfoExt prod) {
        System.out.println(" fireSelectedProduct for popular item");
        kotaction = 1;
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        for (int i = 0; i < l.length; i++) {
            if (e == null) {
                e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
            }
            ((ActionListener) l[i]).actionPerformed(e);
        }
        incProduct(prod);
    }

    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }

    public void printPartialTotals() {

        //  setDiscountButtonEnable();
        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros1.setText(null);
            m_jTaxesEuros1.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount1.setText(null);
            m_jPromoDiscount.setText(null);
//          m_jBillDiscount.setText(null);
            m_oTicket.setBillDiscount(0);
//             m_jServiceCharge.setText(null);
            m_jServiceTax.setText(null);
//                m_jTaxList.removeAll();
            m_jTaxList.setModel(new DefaultListModel());
            m_jServiceTaxLbl.setVisible(false);
            m_jSwachBharat.setText(null);
            m_jSwachBharatLbl.setVisible(false);
        } else {

//                calculateServiceCharge();
            try {
                System.out.println("m_oTicket.iscategoryDiscount()" + m_oTicket.iscategoryDiscount());
                if (m_oTicket.getDiscountMap() != null && m_oTicket.iscategoryDiscount()) {
                    populateDiscount(m_oTicket.getDiscountMap());
                }
                // populateServiceChargeList();
                // populateServiceTaxList();
                populateTaxList();
                // populateSwachBharatTaxList();
            } catch (BasicException ex) {
                logger.info("exception in populateTaxList " + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("m_oTicket.printSubTotalValue()---" + m_oTicket.printSubTotalValue());
            //m_jSubtotalEuros1.setText(m_oTicket.printSubTotalValue());
            m_jSubtotalEuros1.setText(m_oTicket.printSubTotalValueBeforeDiscount());
            m_jTaxesEuros1.setText(m_oTicket.printTax());
            m_jTotalEuros.setText(m_oTicket.printTakeAwayTotal());
            m_jDiscount1.setText(m_oTicket.printDiscount());
            m_jPromoDiscount.setText(m_oTicket.printPromoDiscount());
            //  m_jDiscount1.setText(m_oTicket.printLineDiscount());
//                if(m_oTicket.printServiceCharge()!=null){
//                    //jLabel13.setVisible(true);
//                    m_jServiceCharge.setText("0.00");
//                }
            if (m_oTicket.printServiceTax() != null) {
                //   m_jServiceTaxLbl.setVisible(true);
                //   m_jServiceTax.setText(m_oTicket.printServiceTax());
            }
            if (m_oTicket.printSwachBharatTax() != null) {
                //    m_jSwachBharatLbl.setVisible(true);
                //  m_jSwachBharat.setText(m_oTicket.printSwachBharatTax());
            }

        }


        setTenderAmount();
    }
    //consolidating taxes based on rate developed by Shilpa

    private void consolidateTaxes(RetailTicketInfo ticket) {
        Map<String, NameTaxMapInfo> taxMap = new HashMap<String, NameTaxMapInfo>();
        double rate;
        String taxName;
        double taxValue;
        for (int i = 0; i < ticket.getTaxes().size(); i++) {
            rate = ticket.getTaxes().get(i).getTaxInfo().getRate();
            taxName = ticket.getTaxes().get(i).getTaxInfo().getSplitName();
//            if(taxName.startsWith("KRISHI")){
//                rate =0.0021;
//            }
            taxValue = ticket.getTaxes().get(i).getRetailTax();
            String strTaxValue = String.format("%.2f", taxValue);
//            System.out.println("strTaxValue==="+strTaxValue);
            taxValue = Double.parseDouble(strTaxValue);
            if (taxMap.get(taxName) == null) {
                taxMap.put(taxName, new NameTaxMapInfo(taxName, taxValue));
            } else {
                taxValue = taxValue + taxMap.get(taxName).getTaxValue();
                taxMap.put(taxName, new NameTaxMapInfo(taxName, taxValue));
            }

        }
        ticket.setNametaxMap(taxMap);
    }

    public void populateTaxList() throws BasicException {
        try {
            System.out.println("populating tax list");
            taxeslogic.calculateTakeAwayTaxes(m_oTicket);
        } catch (TaxesException ex) {
            logger.info("exception in populateTaxList " + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxModel = new DefaultListModel();
        m_jTaxList.setModel(taxModel);
        empty = BorderFactory.createEmptyBorder();
        m_jTaxList.setBorder(empty);
        //added newly to calcuate taxes based on erp tax category by Shilpa
        consolidateTaxes(m_oTicket);
        int i = 0;
        for (Map.Entry<String, NameTaxMapInfo> entry : m_oTicket.getNametaxMap().entrySet()) {
            String taxName = entry.getValue().getName();
            String taxValue = String.format("%.2f", entry.getValue().getTaxValue());
            String taxEntry = "<html>"
                    + "<style type=\\\"text/css\\\">"
                    + "body { margin: 0px auto; }\\n"
                    + "td { "
                    + "padding: 0px;"
                    + "margin: 0px;"
                    + "}"
                    + "</style>"
                    + "<body>"
                    + "	<table style=\"width:150px\">"
                    + "		<tr bgcolor=grey>"
                    + "			<td width=\"120px\" align=\"left\">" + taxName + "</td>"
                    + "			<td width=\"30px\" align=\"right\">" + taxValue + "</td>"
                    + "		</tr>		"
                    + "	</table>"
                    + "<body>"
                    + "</html>";

            taxModel.add(i, taxEntry);
            i = i + 1;
        }


//        for (int i = 0; i < m_oTicket.getTaxes().size(); i++) {
//            System.out.println("getPercentage " + m_oTicket.getTaxes().get(i).getRetailTax());
//            //Name of the tax
//            String taxName =m_oTicket.getTaxes().get(i).getTaxInfo().getName();
//            //decimal value of tax with two significant digits
//            String taxValue = String.format("%.2f",m_oTicket.getTaxes().get(i).getRetailTax());
//            //String variable holding HTML code to render two strings, in a table row within two columns.
//            String taxEntry = "<html>"+
//                    "<style type=\\\"text/css\\\">"+
//                    "body { margin: 0px auto; }\\n"+
//                    "td { "+
//                    "padding: 0px;"+
//                    "margin: 0px;"+
//                    "}"+
//                    "</style>"+
//                    "<body>"+
//                    "	<table style=\"width:150px\">"+
//                    "		<tr bgcolor=grey>"+
//                    "			<td width=\"120px\" align=\"left\">"+taxName+"</td>"+
//                    "			<td width=\"30px\" align=\"right\">"+taxValue+"</td>"+
//                    "		</tr>		"+
//                    "	</table>"+
//                    "<body>"+
//                    "</html>";
//            
//            taxModel.add(i, taxEntry);
//        }
    }

//public void populateServiceChargeList() throws BasicException {
//System.out.println("populating service charge list");
//m_oTicket.setStoreName(storeName);
//double chargeTotal =0.0;
//String ServiceChargeTotal=null;
//    try {
//            chargeslogic.calculateCharges(m_oTicket);
//        } catch (TaxesException ex) {
//            logger.info("exception in populateServiceChargeList "+ex.getMessage()+";");
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    System.out.println("total charge"+m_oTicket.getCharges().size());
//      for (int i = 0; i < m_oTicket.getCharges().size(); i++) {
//         //Name of the servicecharge
//            String chargeName =m_oTicket.getCharges().get(i).getServiceChargeInfo().getName();
//            //decimal value of tax with two significant digits
//            String chargeValue = String.format("%.2f",m_oTicket.getCharges().get(i).getRetailSCharge());
//            chargeTotal=chargeTotal+Double.valueOf(chargeValue);
//         }
//      System.out.println("chargeTotal"+chargeTotal);
//     ServiceChargeTotal= Formats.CURRENCY.formatValue(chargeTotal);
//        m_oTicket.setServiceCharge(chargeTotal);
//      m_jServiceCharge.setText(ServiceChargeTotal);
//    }
//public void populateSwachBharatTaxList() throws BasicException {
//System.out.println("populating populateSwachBharatTaxList tax list"+m_oTicket.getStoreName());
//double staxTotal =0.0;
//String SwatchTaxTotal=null;
//    try {
//            sbtaxeslogic.calculateTakeAwaySwachBharatTaxes(m_oTicket);
//        } catch (TaxesException ex) {
//            logger.info("exception in populateSwachBharatTaxList "+ex.getMessage()+";");
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//
//      for (int i = 0; i < m_oTicket.getSwachBharatTaxes().size(); i++) {
//         //Name of the servicecharge
//            String staxName =m_oTicket.getSwachBharatTaxes().get(i).getTaxInfo().getName();
//             System.out.println("staxName"+staxName);
//            //decimal value of tax with two significant digits
//            String staxValue = String.format("%.2f",m_oTicket.getSwachBharatTaxes().get(i).getRetailSwachBharatTax());
//           staxTotal=staxTotal+Double.valueOf(staxValue);
//         //   System.out.println("Double ServiceTaxTotal"+m_oTicket.getServiceTaxes().get(i).getRetailServiceTax()+" "+staxTotal+" "+staxValue);
//         }
//     SwatchTaxTotal= Formats.CURRENCY.formatValue(staxTotal);
//       System.out.println("Double ServiceTaxTotal"+SwatchTaxTotal);
//         m_oTicket.setSwatchBharatTax(staxTotal);
//         m_jSwachBharat.setText(SwatchTaxTotal);
//    }
//public void populateServiceTaxList() throws BasicException {
//System.out.println("populating service tax list"+m_oTicket.getStoreName());
//double staxTotal =0.0;
//String ServiceTaxTotal=null;
//    try {
//            staxeslogic.calculateTakeAwayServiceTaxes(m_oTicket);
//        } catch (TaxesException ex) {
//            logger.info("exception in populateServiceTaxList "+ex.getMessage()+";");
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
//    
//      for (int i = 0; i < m_oTicket.getServiceTaxes().size(); i++) {
//         //Name of the servicecharge
//            String staxName =m_oTicket.getServiceTaxes().get(i).getTaxInfo().getName();
//             System.out.println("staxName"+staxName);
//            //decimal value of tax with two significant digits
//            String staxValue = String.format("%.2f",m_oTicket.getServiceTaxes().get(i).getRetailServiceTax());
//           staxTotal=staxTotal+Double.valueOf(staxValue);
//            System.out.println("Double ServiceTaxTotal"+m_oTicket.getServiceTaxes().get(i).getRetailServiceTax()+" "+staxTotal+" "+staxValue); 
//         }
//     ServiceTaxTotal= Formats.CURRENCY.formatValue(staxTotal);
//       System.out.println("Double ServiceTaxTotal"+ServiceTaxTotal);
//         m_oTicket.setServiceTax(staxTotal);
//         m_jServiceTax.setText(ServiceTaxTotal);
//    }
//public void calculateServiceCharge(){
//         
////            System.out.println("print-0---"+serviceTaxList.get(0).getRate()+"---"+serviceTaxList.size()+"--"+serviceChargeList.get(0).getRate()+"--"+m_oTicket.getTotal());
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(m_oTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceCharge"+ex.getMessage());
//                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = m_oTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = m_oTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  m_oTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//                
//                  m_oTicket.setServiceCharge(serviceChargeAmt);
//                  m_oTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      m_oTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      m_oTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (m_oTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      System.out.println("serviceTaxAmt--"+serviceChargeAmt +"--"+serviceTax+"---"+m_oTicket.getSubTotal()+"---"+serviceChargeAmt);
//                      m_oTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
//
//      public void calculateServiceChargeSplitTicket(RetailTicketInfo splitTicket){
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(splitTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceChargeSplitTicket "+ex.getMessage());
//                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = splitTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = splitTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  splitTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//                
//                  splitTicket.setServiceCharge(serviceChargeAmt);
//                  splitTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      splitTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      splitTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (splitTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      splitTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
//public void calculateServiceCharge(RetailTicketInfo retail){
//
////            System.out.println("print-0---"+serviceTaxList.get(0).getRate()+"---"+serviceTaxList.size()+"--"+serviceChargeList.get(0).getRate()+"--"+m_oTicket.getTotal());
//         if(getEditSale()=="Edit"){
//            int hdCount=0;
//            try {
//                hdCount = dlSales.getHomeDeliveryCount(m_oTicket.getDocumentNo());
//            } catch (BasicException ex) {
//                logger.info("exception in calculateServiceCharge "+ex.getMessage());
//                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            if(hdCount==1){
//                 getServiceCharge("Home Delivery");
//            }else{
//                 getServiceCharge("Order Taking");
//            }
//        }else{
//              getServiceCharge("Order Taking");
//        }
//            if(serviceChargeList.size()!=0){
//                if(serviceChargeList.get(0).getIsTaxincluded().equals("Y")){
//                     serviceChargeAmt = m_oTicket.getTotalBeforeTax()*serviceChargeList.get(0).getRate();
//                }else{
//                  serviceChargeAmt = m_oTicket.getSubTotal()*serviceChargeList.get(0).getRate();
//                }
//                 System.out.println("getTotalBeforeTax---"+ serviceChargeAmt);
//
//                  m_oTicket.setServiceChargeId(serviceChargeList.get(0).getId());
//
//                  m_oTicket.setServiceCharge(serviceChargeAmt);
//                  m_oTicket.setServiceChargeRate(serviceChargeList.get(0).getRate());
//                  if(serviceTaxList.size()!=0){
//                      m_oTicket.setServiceTaxId(serviceTaxList.get(0).getId());
//                      m_oTicket.setServiceTaxRate(serviceTaxList.get(0).getRate());
//                      double serviceTax = (m_oTicket.getSubTotal()+serviceChargeAmt)*serviceTaxList.get(0).getRate();
//                      System.out.println("serviceTaxAmt--"+serviceChargeAmt +"--"+serviceTax);
//                      m_oTicket.setServiceTax(serviceTax);
//                  }
//            }
//}
    private void paintTicketLine(int index, RetailTicketLineInfo oLine) {
        System.out.println("oLine" + oLine.getIsKot());
        if (executeEventAndRefresh("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            m_oTicket.setLine(index, oLine);
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
            visorTicketLine(oLine); // Y al visor tambien...
            if (getServedStatus() == 0) {
                printPartialTotals();
                stateToZero();
                executeEventAndRefresh("ticket.change");
            }
        }
    }

    private void paintKotTicketLine(int index, RetailTicketLineInfo oLine) {
        System.out.println("oLine" + oLine.getIsKot());
        if (executeEventAndRefreshForKot("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            System.out.println("executeEventAndRefreshForKot");
            m_oTicket.setLine(index, oLine);
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
            visorTicketLine(oLine); // Y al visor tambien...
//            if(getServedStatus()==0){
//           executeEventAndRefresh("ticket.change");
//            }
        }
    }

    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) {
        kotaction = 1;
        System.out.println("product preparation time" + oProduct.getPreparationTime());
        TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "Y");
        ServiceChargeInfo charge = null;// chargeslogic.getChargeInfo(oProduct.getServiceChargeID(), m_oTicket.getCustomer());
        System.out.println("charge value in addticket" + oProduct.getServiceChargeID() + m_oTicket.getCustomer());
        TaxInfo sertax = staxeslogic.getTaxInfo(oProduct.getServiceTaxID(), m_oTicket.getCustomer());
        TaxInfo sbTax = sbtaxeslogic.getTaxInfo(oProduct.getSwachBharatTaxId(), m_oTicket.getCustomer());
        addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 0, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, sbTax, 0, null,false));
        //addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 0, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, sbTax, 0, null));
        // System.out.println("status values"+m_oTicket.getLine(0).getKdsPrepareStatus());
        addonId = null;
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
            // m_oTicket.setRate("0");
            // m_oTicket.setdAmt(0);
            //m_oTicket.refreshTxtFields(1);
            visorTicketLine(oLine);
//            setTime(m_oTicket);
            printPartialTotals();
            stateToZero();

            // event receipt
            executeEventAndRefresh("ticket.change");
            //  m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
            m_jTxtItemCode.setText("");
//             itemName.setText("");
            stateToItem();
        }
    }

    private void removeTicketLine(int i) {

        if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {

            if (m_oTicket.getLine(i).isProductCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);
            } else {
                // Es un producto normal, lo borro.
                if (m_oTicket.getLine(i).getPromoType().equals("BuyGet") && m_oTicket.getLine(i).getIsCrossProduct().equals("Y") && m_oTicket.getLine(i).getPrice() != 0) {
                    String crossProduct = null;
                    if (m_oTicket.getLine(i).getPromoRule().get(0).getIsSku().equals("Y")) {
                        crossProduct = m_oTicket.getLine(i).getProductID();
                    } else {
                        try {
                            crossProduct = dlSales.getCrossPromoProduct(m_oTicket.getLine(i).getpromoId());

                        } catch (BasicException ex) {
                            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in removeTicketLine" + ex.getMessage());
                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                    int index = m_oTicket.getProductIndex(crossProduct);
                    m_oTicket.removeLine(index);
                    m_ticketlines.removeTicketLine(index);

                } else {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }

                // Y todos lo auxiliaries que hubiera debajo.
                while (i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
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
            TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getCustomer(), "Y");
            double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);
        } else {
            return dValue;
        }
    }

    private double getInputValue() {
        //   try {
//            return Double.parseDouble(m_jPrice.getText());
        //  } catch (NumberFormatException e){
        return 0.0;
        //   }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(m_jPor.getText().substring(1));
        } catch (NumberFormatException e) {
            return 1.0;
        } catch (StringIndexOutOfBoundsException e) {
            return 1.0;
        }
    }

    public void stateToZero() {
        m_jPor.setText("");
//        m_jPrice.setText("");
        m_sBarcode = new StringBuffer();

        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }

    public void stateToBarcode() {

        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //  m_jEnter.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);

        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
//        m_jCash.setFocusable(false);
//        m_jCard.setFocusable(false);
        // m_jCheque.setFocusable(false);
        //  m_jtxtChequeNo.setFocusable(false);
        //   m_jFoodCoupon.setFocusable(false);
        //    m_jVoucher.setFocusable(false);
//        m_jDeliveryBoy.setFocusable(false);
        //   m_jTxtAdvance.setFocusable(false);
        //    m_jCreditAmount.setFocusable(false);
        //     m_jHomeDelivery.setFocusable(false);
        //  m_jCod.setFocusable(false);
        //     m_jCreditAllowed.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        m_jSplitBtn.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
//        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }

    public void stateToHomeDelivery() {
        cusName.setFocusable(false);
//      m_jTxtCustomerId.setFocusable(false);
//        cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //   m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);

//        itemName.setFocusable(false);
//        m_jTxtItemCode.setFocusable(false);
//        m_jCash.setFocusable(false);
//        m_jCard.setFocusable(false);
        //     m_jCheque.setFocusable(false);
        //      m_jtxtChequeNo.setFocusable(false);
        //      m_jFoodCoupon.setFocusable(false);
        //      m_jVoucher.setFocusable(false);

        //     m_jCreditAmount.setFocusable(false);
        //   JRetalTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
//        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);


    }

    public void stateToItem() {
//        cusName.setFocusable(false);
        //  cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        //   m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //     m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //   m_jPrice.setText("");
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
//        m_jTxtCustomerId.setFocusable(false);
//        m_jCash.setFocusable(false);
        //     m_jCard.setFocusable(false);
        //    m_jCheque.setFocusable(false);
        //    m_jtxtChequeNo.setFocusable(false);
        //     m_jFoodCoupon.setFocusable(false);
        //     m_jVoucher.setFocusable(false);
        //  m_jDeliveryBoy.setFocusable(false);
        //   m_jTxtAdvance.setFocusable(false);
        //    m_jCreditAmount.setFocusable(false);
        //   m_jHomeDelivery.setFocusable(false);
        // m_jCod.setFocusable(false);
        //    m_jCreditAllowed.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
//        m_jServiceCharge.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
        // m_jSplitBtn.setFocusable(false);

    }

    public void stateToPay() {
        //  cusName.setFocusable(false);
        //   cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
//        m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //   m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
        //    m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
//        m_jTxtCustomerId.setFocusable(false);
//        m_jCash.setFocusable(false);
        //   m_jCard.setFocusable(false);
        //   m_jCheque.setFocusable(false);
        // m_jFoodCoupon.setFocusable(false);
        //     m_jVoucher.setFocusable(false);
        //      m_jCreditAmount.setFocusable(false);
        //      m_jtxtChequeNo.setFocusable(false);
//        itemName.setFocusable(false);
        //   m_jTxtItemCode.setFocusable(false);
    }

    public void stateToPayment() {
        //   m_jTxtCustomerId.setFocusable(false);
//        itemName.setFocusable(false);
//        m_jTxtItemCode.setFocusable(false);
        //       cusName.setFocusable(false);
        //     cusPhoneNo.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        //    m_jPrice.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        //    m_jEnter.setFocusable(false);
//        m_jBtnCalculate.setFocusable(false);
//        m_jNumberKeys.setFocusable(false);
//        m_jPrice.setText("");
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
//        m_jbtnScale.setFocusable(false);
        //     m_jCreditAllowed.setFocusable(false);

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
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "Y");
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
        now.add(Calendar.DATE, days);
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + (now.get(Calendar.YEAR));
        try {
            Currentdate = (Date) formatter.parse(str_date);
            // Currentdate =;
        } catch (java.text.ParseException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + " exception in getCreditDate" + ex.getMessage());
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

    public String getWeekDay() {
        String DAY = "";
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                DAY = "SUNDAY";
                break;
            case 2:
                DAY = "MONDAY";
                break;
            case 3:
                DAY = "TUESDAY";
                break;
            case 4:
                DAY = "WEDNESDAY";
                break;
            case 5:
                DAY = "THURSDAY";
                break;
            case 6:
                DAY = "FRIDAY";
                break;
            case 7:
                DAY = "SATURDAY";
                break;
        }
        return DAY;
    }

    protected void buttonTransition(ProductInfoExt prod) {
        kotaction = 1;
        System.out.println("buttonTransition testing");
        buttonPlus = 1;
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
//                m_jPrice.setText("0");
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
                // Un numero entero
                //       m_jPrice.setText(Character.toString(cTrans));
                m_iNumberStatus = NUMBER_INPUTINT;
                m_iNumberStatusInput = NUMBERVALID;
            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_INPUTINT)) {
                // Un numero entero
                //      m_jPrice.setText(m_jPrice.getText() + cTrans);
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
                //    m_jPrice.setText("0.");
                m_iNumberStatus = NUMBER_INPUTZERODEC;
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
                //    m_jPrice.setText(m_jPrice.getText() + ".");
                m_iNumberStatus = NUMBER_INPUTDEC;

            } else if ((cTrans == '0')
                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
                // Un numero decimal
                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
                // Un numero decimal
                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
                m_iNumberStatus = NUMBER_INPUTDEC;
                m_iNumberStatusInput = NUMBERVALID;

            } else if (cTrans == '*'
                    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
                m_jPor.setText("x");
                m_iNumberStatus = NUMBER_PORZERO;
            } else if (cTrans == '*'
                    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
                //  m_jPrice.setText("0");
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
                if (i < 0) {
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
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund + button means one unit less
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        paintTicketLine(i, newline);
                    } else {

                        //    ProductInfoExt product = new ProductInfoExt();
                        // add one unit to the selected line
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        // buttonPlus = 2;

                        paintTicketLine(i, newline);
                        //    m_oTicket.refreshTxtFields(1);
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
                if (i < 0) {
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund - button means one unit more
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        if (newline.getMultiply() >= 0) {
                            removeTicketLine(i);
                        } else {
                            paintTicketLine(i, newline);
                        }
                    } else {
                        // substract one unit to the selected line
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        // m_oTicket.refreshTxtFields(1);

                        if (newline.getMultiply() > 0.0) {
                            // removeTicketLine(i); // elimino la linea
                            // } else {

                            paintTicketLine(i, newline);
                            // m_oTicket.refreshTxtFields(1);
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
                if (i < 0) {
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
                if (i < 0) {
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

                    if (closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo)) {
                        // Ends edition of current receipt
                        if (!m_oTicket.getSplitValue().equals("Split")) {
                            m_ticketsbag.deleteTicket();
                        } else {
                            //  m_ticketsbag.deleteTicket(m_oTicket.getSplitSharedId());
                        }

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
////    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {
////
////        boolean resultok = false;
////        JPaymentEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, ticket,this);
////        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
////
////            try {
////                // reset the payment info
////                taxeslogic.calculateServiceTaxes(ticket);
////                if (ticket.getTotal() >= 0.0) {
////                    ticket.resetPayments(); //Only reset if is sale
////                }
////
////                if (executeEvent(ticket, ticketext, "ticket.total") == null) {
////
////                    // Muestro el total
////                    // printTicket("Printer.TicketTotal", ticket, ticketext);
////                    System.out.println("enter 3333");
////
////                    // Select the Payments information
////                    JPaymentSelect paymentdialog = ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL
////                            ? paymentdialogreceipt
////                            : paymentdialogrefund;
////                    paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));
////
////                    paymentdialog.setTransactionID(ticket.getTransactionID());
////
////                    if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer())) {
////
////                        // assign the payments selected and calculate taxes.
////                        ticket.setPayments(paymentdialog.getSelectedPayments());
////
////                        // Asigno los valores definitivos del ticket...
////                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
////                        ticket.setActiveCash(m_App.getActiveCashIndex());
////                         ticket.setActiveDay(m_App.getActiveDayIndex());
////                        ticket.setDate(new Date()); // Le pongo la fecha de cobro
////
////                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
////                              String ticketDocNo = null;
////                            Integer ticketDocNoInt = null;
////                            String ticketDocument;
////                         if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL){
////
////
////                      try{
////                        try {
////                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
////                        } catch (BasicException ex) {
////                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
////                        }
////                        String[] ticketDocNoValue = ticketDocNo.split("-");
////                            ticketDocNo=ticketDocNoValue[2];
////                        }catch(NullPointerException ex){
////                            ticketDocNo = "10000";
////                        }
////                        if(ticketDocNo!=null){
////                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
////                            ticketDocNoInt = ticketDocNoInt + 1;
////
////                        }
////                        ticketDocument = m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
////                         }else{
////                                ticketDocument = "0";
////                         }
////                            m_oTicket.setDocumentNo(ticketDocument);
////                          if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND){
////
////                                getCreditDate();
////                          }
////
////                     double creditAmt;
////             //       try{
//////                        creditAmt = Double.parseDouble(m_jCreditAmount.getText());
////              //      }catch(Exception ex){
////                        creditAmt =0;
////               //     }
////                            String chequeNos = "";//m_jtxtChequeNo.getText();
////                            String deliveryBoy = "";
////                            String cod;
////                            double advanceissued;
////                            String isCredit;
////                            String isPaidStatus;
////
//////                            int selectedindex = m_jDeliveryBoy.getSelectedIndex();
//////                            if(selectedindex!=0){
//////                                deliveryBoy =deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
//////                            }
////
////                           // if(m_jCod.isSelected()){
////                           //     cod = "Y";
////                            //    isPaidStatus = "N";
////                           // }else{
////                                cod = "N";
////                                isPaidStatus = "Y";
////                          //  }
////                             if(creditAmt>0){
////                                isCredit = "Y";
////                            }else{
////                                isCredit = "N";
////                            }
////
//////                            if(m_jTxtAdvance.getText().equals("")){
////                                advanceissued = 0;
//////                            }else{
//////                                advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
//////                            }
////                            String file;
////
////                            //       if(m_jHomeDelivery.isSelected()){
////                                 //      file = "Printer.HomeDelivery";
////                                 //  }else{
////                                        file = "Printer.Bill";
////                                //   }
////                            ticket.setDocumentNo(ticketDocument);
////                            saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"Y",file,isCredit,"N");
////                            executeEvent(ticket, ticketext, "ticket.close", new ScriptArg("print", paymentdialog.isPrintSelected()));
////
////
////                            resultok = true;
////
////                        }
////                    }
////                }
////            } catch (TaxesException e) {
////                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotcalculatetaxes"));
////                msg.show(this);
////                resultok = false;
////            }
////            ticket.setSplitValue("");
////           // reset the payment info
//////            ticket.resetTaxes();
////          //  ticket.resetPayments();
////        }
////
////         cancelled the ticket.total script
////         or canceled the payment dialog
////         or canceled the ticket.close script
////
////        return resultok;
////    }

    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {
        ticket.setActiveDay(m_App.getActiveDayIndex());
        ticket.setActiveCash(m_App.getActiveCashIndex());
        System.out.println("in closeSplitTicket==============================");
        boolean completed = JPaymentEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, ticket, this, "TakeAway");
        System.out.println("completed" + completed);
        return completed;
    }

    public void settleBill(double totalBillValue, double cash, double card) {
        this.totalBillValue = totalBillValue;
        paymentDetail(cash, card);

    }

    public void settleBill(double totalBillValue, double cash, double card, double voucherAmount, double vCloudAmount, String cardType, String description) {
        this.totalBillValue = totalBillValue;
        paymentDetail(cash, card, voucherAmount, vCloudAmount, cardType, description);

    }

    public boolean getClosePayment() {
        return closePayment;
    }

    public void setClosePayment(boolean closePayment) {
        this.closePayment = closePayment;
    }

    private boolean closeTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("closeTicket");
        boolean resultok = false;
//          int inventoyCount = 0;
//        try {
//            inventoyCount = dlSales.getStopInventoryCount();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
////         if(inventoyCount==1){
//           showMessage(this,"Stock Reconciliation in Progress. Please continue after sometime.");
//         }else{
        if (totalBillValue == 0 && m_oTicket.getTotal() != 0) {
            showMessage(this, "Please enter the tender types");
            setClosePayment(false);
        } else if (totalBillValue < m_oTicket.getTotal()) {
            System.out.println(totalBillValue + "totalBillValue" + m_oTicket.getTotal() + "m_oTicket.getTotal()");
            showMessage(this, "Entered tender amount should be equal to total sale amount");
            setClosePayment(false);
        } else {
            //  if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {  
            try {
                //            try {
                // reset the payment info
                chargeslogic.calculateCharges(m_oTicket);
                taxeslogic.calculateTakeAwayTaxes(ticket);
            } catch (TaxesException ex) {
                logger.info("Order No." + m_oTicket.getOrderId() + " exception while calculateTaxes in closeticket " + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
//                if (ticket.getTotal()>=0.0){
//                    ticket.resetPayments(); //Only reset if is sale
//                }
            //     setTenderAmount();

            if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                //    if()
                double creditAmt;
                //  try{
                //      creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                //  }catch(Exception ex){
                creditAmt = 0;
                //   }

                // if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
                //          showMessage(this, "Please select the customer");
                //  }else{


//                    if(m_jCreditAmount.getText().equals("")){
                creditAmount = 0;
                //   }else{
                //       creditAmount = Double.parseDouble(m_jCreditAmount.getText());
                //     }

                //   String

                //  paymentDetail();
                // assign the payments selected and calculate taxes.         


                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date()); // Le pongo la fecha de cobro
                ticket.setPayments(m_aPaymentInfo.getPayments());
                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                    //String[] ticketDocNoValue;
                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument = null;
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


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
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
                    }
//                         else{
//                                ticketDocument = "0";
//                         }
                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {

                        getCreditDate();
                    }


                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";
                    String cod;
                    double advanceissued;
                    String isCredit;
                    String isPaidStatus;
                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    cod = "N";
                    isPaidStatus = "Y";
                    if (creditAmt > 0) {
                        isCredit = "Y";
                    } else {
                        isCredit = "N";
                    }

                    advanceissued = 0;
                    String file;
                    file = "Printer.TakeAway";
                    saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                    logger.info("bill has been settled");
                    resultok = true;
                }
//                       setClosePayment(true);
//                       if(!ticket.getSplitValue().equals("Split")){
//                            logger.info("deleting splitted settled bill ");    
//                            m_ticketsbag.deleteTicket();
//                       }else{
//                          logger.info("deleting  settled bill ");     
//                         dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//                      }
            }
            //  }

        }
        return resultok;
    }

    private boolean closeTicketHomeDelivery(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketHomeDelivery");

        boolean resultok = false;

        //   if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info
            chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTakeAwayTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        // setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            //    if()
            double creditAmt;
            try {
                creditAmt = m_oTicket.getTotal();
            } catch (Exception ex) {
                creditAmt = 0;
            }

            if ((cusName.getText().equals("") || cusName.getText() == null)) {
                showMessage(this, "Please select the customer");
            } else {
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
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


                        try {
                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                            String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo = ticketDocNoValue[2];
                        } catch (NullPointerException ex) {
                            ticketDocNo = "10000";
                        }
                        if (ticketDocNo != null) {
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
                    } else {
                        ticketDocument = "0";
                    }
                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        getCreditDate();
                    }
                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";
                    double advanceissued;

                    //   int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                    //     if(selectedindex!=0){
                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    //  }

                    //if(m_jTxtAdvance.getText().equals("")){
                    advanceissued = 0;
                    //  }else{
                    //      advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                    //   }

                    String file;
                    file = "Printer.HomeDelivery";

                    if (m_oTicket.getLinesCount() == 0) {
                        showMessage(this, "Please select the products");
                    } else {
                        saveHomeReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "N");

                        //   m_jCreditAllowed.setVisible(false);
                        //   m_jCreditAllowed.setSelected(false);
                        resultok = true;
                    }
                }
                if (!m_oTicket.getSplitValue().equals("Split")) {
                    m_ticketsbag.deleteTicket();
                } else {
                    // m_ticketsbag.deleteTicket(m_oTicket.getSplitSharedId());
                }
            }

        }
        // }
        //  }

        return resultok;
    }

    private boolean closeTicketNonChargable(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        System.out.println("closeTicketNonChargable");

        boolean resultok = false;

        //   if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info
            chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTakeAwayTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTakeAwayTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        // setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            //    if()
            double creditAmt;
            try {
                creditAmt = m_oTicket.getTakeAwayTotal();
            } catch (Exception ex) {
                creditAmt = 0;
            }
            if ((cusName.getText().equals(""))) {
                showMessage(this, "Please select the customer");
            } else {
                //   ticket.setPayments(m_aPaymentInfo.getPayments());

                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date()); // Le pongo la fecha de cobro

                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                    //String[] ticketDocNoValue;
                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument;
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


                        try {
                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                            String[] ticketDocNoValue = ticketDocNo.split("-");
                            ticketDocNo = ticketDocNoValue[2];
                        } catch (NullPointerException ex) {
                            ticketDocNo = "10000";
                        }
                        if (ticketDocNo != null) {
                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
                            ticketDocNoInt = ticketDocNoInt + 1;

                        }
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
                    } else {
                        ticketDocument = "0";
                    }
                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                        getCreditDate();
                    }
                    String chequeNos = "";//m_jtxtChequeNo.getText();
                    String deliveryBoy = "";
                    double advanceissued;

                    //      int selectedindex = m_jDeliveryBoy.getSelectedIndex();
                    // if(selectedindex!=0){
                    deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                    //  }

                    //if(m_jTxtAdvance.getText().equals("")){
                    advanceissued = 0;
                    // }else{
                    //     advanceissued = Double.parseDouble(m_jTxtAdvance.getText());
                    // }
                    ticket.setDocumentNo(ticketDocument);
                    String file;
                    file = "Printer.NonChargableBill";

                    if (m_oTicket.getLinesCount() == 0) {
                        showMessage(this, "Please select the products");
                    } else {
                        saveNonChargableReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "Y");

                        //m_jCreditAllowed.setVisible(false);
                        //  m_jCreditAllowed.setSelected(false);
                        resultok = true;
                    }
                }

            }

        }
        // }
        //  }

        return resultok;
    }

    private boolean closeTicketWithButton(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("enter the closeTicketWithButton method" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        boolean resultok = false;

        //  if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info9
            //  chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTakeAwayTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("exception while calculateTaxes in closeTicketWithButton " + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTakeAwayTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        //  setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            double creditAmt;
            //       try{
            //       creditAmt = Double.parseDouble(m_jCreditAmount.getText());
            //      }catch(Exception ex){
            creditAmt = 0;
            //     }

            //  / if((m_jHomeDelivery.isSelected() && cusName.getText().equals("")) || (creditAmt!=0 && cusName.getText().equals(""))){
            //       showMessage(this, "Please select the customer");
            //  }else{
            ticket.setPayments(m_aPaymentInfo.getPayments());

            ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket.setActiveCash(m_App.getActiveCashIndex());
            ticket.setDate(new Date()); // Le pongo la fecha de cobro

            if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                //String[] ticketDocNoValue;
                String ticketDocNo = null;
                Integer ticketDocNoInt = null;
                String ticketDocument = null;
                if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {

                    ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
                }

                ticket.setDocumentNo(ticketDocument);
                if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
                    getCreditDate();
                }
                String chequeNos = "";//m_jtxtChequeNo.getText();
                String deliveryBoy = "";

                String homeDelivery;
                String orderTaking;
                String cod;
                String isPaidStatus;
                homeDelivery = "N";
                orderTaking = "N";
                cod = "N";
                isPaidStatus = "Y";
                String isCredit;
                double advanceissued;
                deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
                if (creditAmt > 0) {
                    isCredit = "Y";
                } else {
                    isCredit = "N";
                }

                advanceissued = 0;
                String file;
                if (ticket.getPrintStatus() == 1) {
                    file = "Printer.TakeAway";
                    if (ticket.getLinesCount() == 0) {
                        showMessage(this, "Please select the products");
                    } else {
                        logger.info("Before saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                        logger.info("After saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        resultok = true;
                    }
                }
//                       else if(ticket.getPrintStatus()==2){
//                           file = "Printer.Ticket";
//                           if(ticket.getLinesCount()==0){
//                               showMessage(this, "Please select the products");
//                           }else{
//                                  logger.info("Before saveReceipt if ticket.getPrintStatus()==2"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                                  dlSales.saveDraftTicket(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,homeDelivery,cod,advanceissued,creditAmount,"N",orderTaking);
//                                  saveReceipt(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,getPriceInfo(), chequeNos,deliveryBoy,advanceissued,creditAmt,ticketext,"N",file,isCredit,"N");
//                                  logger.info("After saveReceipt if ticket.getPrintStatus()==2"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                                 resultok = true;
//                           }
//                                      
//                       }

            }

//                      if(!ticket.getSplitValue().equals("Split")){
//                           logger.info("deleting splitted settled bill");
//                            m_ticketsbag.deleteTicket();
//                       }else{
//                         logger.info("deleting settled bill");
//                         dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//                      }



        }
        //  }
        logger.info("End of closeticketButton" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        return resultok;
    }

    private boolean closeCreditTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {

        boolean resultok = false;

//        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {
        try {
            //            try {
            // reset the payment info
            chargeslogic.calculateCharges(m_oTicket);
            taxeslogic.calculateTakeAwayTaxes(ticket);
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTakeAwayTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }
        setTenderAmount();

        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            int inventoyCount = 0;
            try {
                inventoyCount = dlSales.getStopInventoryCount();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inventoyCount == 1) {
                showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
            } else {
                double creditAmt;
                //    try{
                ///     creditAmt = Double.parseDouble(m_jCreditAmount.getText());
                // }catch(Exception ex){
                creditAmt = 0;
                // }

                if ((creditAmt != 0 && cusName.getText().equals(""))) {
                    showMessage(this, "Please select the customer");
                } else if (!cusName.getText().equals("") && creditAmt != 0 && customerList.get(0).getIsCreditCustomer() == 0) {
                    showMessage(this, "Please select the credit customer");
                } else {

                    ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                    ticket.setActiveCash(m_App.getActiveCashIndex());
                    ticket.setDate(new Date()); // Le pongo la fecha de cobro

                    if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                        //String[] ticketDocNoValue;
                        String ticketDocNo = null;
                        Integer ticketDocNoInt = null;
                        String ticketDocument;
                        if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {


                            try {
                                ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
                                String[] ticketDocNoValue = ticketDocNo.split("-");
                                ticketDocNo = ticketDocNoValue[2];
                            } catch (NullPointerException ex) {
                                ticketDocNo = "10000";
                            }
                            if (ticketDocNo != null) {
                                ticketDocNoInt = Integer.parseInt(ticketDocNo);
                                ticketDocNoInt = ticketDocNoInt + 1;

                            }
                            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
                        } else {
                            ticketDocument = "0";
                        }
                        m_oTicket.setDocumentNo(ticketDocument);
                        String chequeNos = "";///m_jtxtChequeNo.getText();
                        String deliveryBoy = "";
                        String homeDelivery;
                        String isCredit;
                        // if(m_jHomeDelivery.isSelected()){
                        homeDelivery = "Y";
                        ///  }else{
                        //      homeDelivery = "N";
                        //  }
                        if (creditAmt > 0) {
                            isCredit = "Y";
                        } else {
                            isCredit = "N";
                        }

                        String file;
                        file = "Printer.Bill";

                        if (m_oTicket.getLinesCount() == 0) {
                            showMessage(this, "Please select the products");
                        } else {
                            saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, "", 0, creditAmt, ticketext, "Y", file, isCredit, "N");
                            //     m_jCreditAllowed.setVisible(false);
                            //     m_jCreditAllowed.setSelected(false);
                            resultok = true;

                        }
                    }
                }
            }
        }
        //   }
        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;
    }

    public void saveReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;

        homeDelivery = "N";
        orderTaking = "Y";
        cod = "N";
        isPaidStatus = "Y";
        logger.info("inside save receipt");
        m_oTicket.setServiceCharge(0.00);
        m_oTicket.setCharges(null);
        try {
            dlSales.saveRetailTakeAwayTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
            logger.info("settled successfully");
            //added here to settle the bill
            String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
            if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("ITPL")) {
                printTicketGeneric("Printer.Bill", m_oTicket, m_oTicketExt);
            } else {
                printTicket("Printer.Bill", m_oTicket, m_oTicketExt);
            }
            logger.info("bill has been printed");
            m_oTicket.setPrinted(true);
            logger.info("End Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

            m_jTxtTotalPaid.setText("");
            m_oTicket.setRate("0");
            m_oTicket.setdAmt(0);
            taxModel.removeAllElements();
            populateDeliveryBoy();
            m_jTxtChange.setText("");
            m_oTicket.resetCharges();
            m_oTicket.resetTaxes();
            m_oTicket.resetPayments();
//                 m_jServiceCharge.setText("");
            activate();
            logger.info("After activate method");
            setClosePayment(true);
            if (!ticket.getSplitValue().equals("Split")) {
                logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after settle bill");
                m_ticketsbag.deleteTicket();
            } else {
                logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after splitted settle bill ");
                dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
            }
        } catch (Exception ex) {
            logger.info("Order No." + ticket.getOrderId() + " exception while calling saveRetailTicket " + ex.getMessage());
            showMessage(this, "Settlement is not happened! Please resettle the bill");
        }
        logger.info("After save receipt completed ");
        //executes only in edit sales
        if (getEditSale() == "Edit") {
            RetailTicketInfo editTicket = null;
            try {
                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
            } catch (BasicException ex) {
                logger.info("exception while edit ticket" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
            } catch (BasicException ex) {
                logger.info("exception while deleting ticket in savereceipt method" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//till here editsales
        setEditSale("");
//                try {
//                    dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//                 } catch (BasicException ex) {
//                     logger.info("exception while calling insertRetailTicket "+ex.getMessage());
//                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            String printrequired=m_App.getProperties().getProperty("machine.printrequired");
//             if(printrequired.equals("true")){
//                 printTicket(file, ticket, ticketext);
//              }
//                 m_jTxtTotalPaid.setText("");
//                 m_oTicket.setRate("0");
//                 m_oTicket.setdAmt(0);
//                 taxModel.removeAllElements();
//                 populateDeliveryBoy();
//                 m_jTxtChange.setText("");
//                 m_oTicket.resetTaxes();
//                 m_oTicket.resetPayments();
//                 m_jServiceCharge.setText("");
//                activate();
//                logger.info("After activate method");

    }

    public void saveHomeReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        //    System.out.println("ticketext---"+ticketext.toString());
        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
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
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
        } catch (Exception ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (getEditSale() == "Edit") {
            RetailTicketInfo editTicket = null;
            try {
                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        setEditSale("");
        try {
            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        //     if(getPrinterStatus()==true){
        ///  printTicket(file, ticket, ticketext);
        //    }
//                resultok = true;
        //   m_jCreditAllowed.setVisible(false);
        //    m_jCreditAllowed.setSelected(false);
//                 m_jCash.setText("");
        //          m_jCard.setText("");
        //          m_jCheque.setText("");
        //          m_jFoodCoupon.setText("");
        //          m_jVoucher.setText("");
        m_jTxtTotalPaid.setText("");
        m_oTicket.setRate("0");
        m_oTicket.setdAmt(0);
        //   m_jTxtCustomerId.setText("");
        //    m_jHomeDelivery.setSelected(false);
        //    m_jDeliveryBoy.removeAllItems();
        //        m_jCreditAmount.setText("0.00");
        populateDeliveryBoy();
        //     m_jDeliveryBoy.setSelectedIndex(0);
        //     m_jTxtAdvance.setText("");
        //     m_jCod.setSelected(false);
        //     m_jCreditAmount.setText("0.00");
        m_jTxtChange.setText("");
        //   jLabel10.setVisible(false);
//                 m_jDeliveryBoy.setVisible(false);
        //     jLabel11.setVisible(false);
        //    m_jTxtAdvance.setVisible(false);
        //     m_jCod.setVisible(false);
        m_oTicket.resetCharges();
        m_oTicket.resetTaxes();
        m_oTicket.resetPayments();
        //      m_jTxtTips.setText("0.00");
//                 m_jServiceCharge.setText("");

        activate();

    }

    public void saveNonChargableReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        //    System.out.println("ticketext---"+ticketext.toString());
        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
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
            dlSales.saveNonChargableTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (getEditSale() == "Edit") {
            RetailTicketInfo editTicket = null;
            try {
                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        setEditSale("");
        try {
            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        //     if(getPrinterStatus()==true){
        printTicket(file, ticket, ticketext);
        //    }
//                resultok = true;
        //    m_jCreditAllowed.setVisible(false);
        //  m_jCreditAllowed.setSelected(false);
        // m_jCash.setText("");
        //  m_jCard.setText("");
        //   m_jCheque.setText("");
        //   m_jFoodCoupon.setText("");
        //   m_jVoucher.setText("");
        m_jTxtTotalPaid.setText("");
        m_oTicket.setRate("0");
        m_oTicket.setdAmt(0);
        //   m_jTxtCustomerId.setText("");
        //  m_jHomeDelivery.setSelected(false);
        //  m_jDeliveryBoy.removeAllItems();
        //   m_jCreditAmount.setText("0.00");
        populateDeliveryBoy();
        //   m_jDeliveryBoy.setSelectedIndex(0);
        //    m_jTxtAdvance.setText("");
        //  m_jCod.setSelected(false);
        //  m_jCreditAmount.setText("0.00");
        //   m_jTxtChange.setText("");
        //    jLabel10.setVisible(false);
        //    m_jDeliveryBoy.setVisible(false);
        //    jLabel11.setVisible(false);
        //   m_jTxtAdvance.setVisible(false);
        //   m_jCod.setVisible(false);
        m_oTicket.resetCharges();
        m_oTicket.resetTaxes();
        m_oTicket.resetPayments();
        //  m_jTxtTips.setText("0.00");
//                 m_jServiceCharge.setText("");

        activate();

    }

    public void saveNotReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status) {
        double tipsAmt = 0;// Double.parseDouble(m_jTxtTips.getText());
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
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, "N", "Y", tipsAmt, orderTaking, "N");
        } catch (Exception eData) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
            msg.show(this);
        }
        try {
            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        //      m_jCreditAllowed.setVisible(false);
        ///     m_jCreditAllowed.setSelected(false);
//                 m_jCash.setText("");
        m_jTxtChange.setText("");
        //    m_jCard.setText("");
        //   m_jCheque.setText("");
        //    m_jFoodCoupon.setText("");
        //    m_jVoucher.setText("");
        m_jTxtTotalPaid.setText("");
        m_oTicket.setRate("0");
        m_oTicket.setdAmt(0);
        //   m_jHomeDelivery.setSelected(false);
        //   m_jDeliveryBoy.removeAllItems();
        populateDeliveryBoy();
        //    m_jDeliveryBoy.setSelectedIndex(0);
        //    m_jTxtAdvance.setText("");
        //   m_jCod.setSelected(false);
        //   m_jCod.setVisible(false);
        m_oTicket.resetCharges();
        m_oTicket.resetTaxes();
        m_oTicket.resetPayments();
        //    m_jTxtCustomerId.setText("");
//                 m_jCreditAmount.setText("0.00");
        activate();

    }

    public void clearTxtField() {
        //    cusName.setText("");
        //    cusPhoneNo.setText("");
//       itemName.setText("");
        //    m_jCash.setText("");
        //     m_jCard.setText("");
        //     m_jCheque.setText("");
        //    m_jFoodCoupon.setText("");
        //    m_jVoucher.setText("");
        //     m_jtxtChequeNo.setText("");
        //  m_jHomeDelivery.setSelected(false);
        //    m_jCod.setSelected(false);
        //   m_jCreditAmount.setText("0.00");
        //   m_jCod.setVisible(false);
        //  m_jCreditAllowed.setVisible(false);
        //   m_jCreditAllowed.setSelected(false);
    }

    public void clearFocus(boolean value) {

        //cusName.setFocusable(value);
        //  cusPhoneNo.setFocusable(value);
        m_jTxtItemCode.setFocusable(value);
        //   m_jTxtCustomerId.setFocusable(value);
        m_jKeyFactory.setFocusable(true);
        m_jKeyFactory.setText(null);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_jKeyFactory.requestFocus();
            }
        });
//       m_jPrice.setEnabled(true);
//       itemName.setFocusable(value);
//       m_jCash.setFocusable(value);
        //     m_jCard.setFocusable(value);
        //      m_jCheque.setFocusable(value);
        //      m_jtxtChequeNo.setFocusable(value);
        //      m_jFoodCoupon.setFocusable(value);
        //      m_jCreditAmount.setFocusable(value);
        //     m_jVoucher.setFocusable(value);
        m_jTxtItemCode.setText("");
//         itemName.setText("");

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
        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address1")));
        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address2")));
        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.address3")));
        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.vatno")));
        allLines.add(new TicketLineConstructor(m_App.getProperties().getProperty("machine.strc")));
        allLines.add(new TicketLineConstructor("User:" + getSpaces(1) + (ticket.printUser())));
        allLines.add(new TicketLineConstructor(getDottedLine(35)));
        allLines.add(new TicketLineConstructor("Bill No : " + getSpaces(1) + ticket.printId()));
        //allLines.add(new TicketLineConstructor("Table : " +getSpaces(1) + ticketext));
        allLines.add(new TicketLineConstructor("Date : " + getSpaces(1) + (ticket.printDateForReceipt()) + getSpaces(2) + "Time: " + getSpaces(1) + (ticket.printTime())));
        allLines.add(new TicketLineConstructor(getDottedLine(35)));

        allLines.add(new TicketLineConstructor("Item Name" + getSpaces(10) + "Qty" + getSpaces(5) + "Amount"));
        allLines.add(new TicketLineConstructor(getDottedLine(35)));

        for (RetailTicketLineInfo tLine : ticket.getUniqueLines()) {

            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            //  String subValue = tLine.printPriceLine();
            // String total = Formats.DoubleValue.formatValue(tLine.getSubValue());
            String total = Formats.DoubleValue.formatValue(tLine.getSubValueBeforeDiscount());
            System.out.println("prodname---" + prodName.length());
            if (prodName.length() > 22) {
                prodName = WordUtils.wrap(prodName, 22);
                String[] prodNameArray = prodName.split("\n");
                for (int i = 0; i < prodNameArray.length - 1; i++) {
                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
                }
                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(23 - prodNameArray[prodNameArray.length - 1].length()) + qty + getSpaces((10 - total.length())) + total));

            } else {
                allLines.add(new TicketLineConstructor(prodName + getSpaces(23 - prodName.length()) + qty + getSpaces((10 - total.length())) + total));

            }
//            if(prodName.length()>22){
//              String prodName1 = prodName.substring(0, 22);
//               String prodName2= prodName.substring(22);
            // System.out.println("prodName1--"+prodName1+"---"+prodName2);
//                 allLines.add(new TicketLineConstructor(qty + getSpaces(3) + prodName1+getSpaces(18 - prodName1.length()+total.length())+ total));
//                 allLines.add(new TicketLineConstructor(getSpaces(4) + prodName2));
//            }else{
            // allLines.add(new TicketLineConstructor(prodName + getSpaces(24 - prodName.length()) + qty + getSpaces(3) + (7 - total.length())+total));
            //}
        }

        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
        //    String serviceCharge = Formats.DoubleValue.formatValue(ticket.getServiceCharge());
        //    String serviceTax = Formats.DoubleValue.formatValue(ticket.getServiceTax());
        //   String swachBharatTax = Formats.DoubleValue.formatValue(ticket.getSwatchBharatTax());
        String discount = Formats.DoubleValue.formatValue(ticket.getLineDiscountOnCategory());
        String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
        String roundoff = Formats.DoubleValue.formatValue(ticket.getRoundOffvalue());
        String total = Formats.DoubleValue.formatValue(ticket.getTakeAwayTotal());
        allLines.add(new TicketLineConstructor("SubTotal " + getSpaces(27 - subTotal.length()) + (subTotal)));
        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Discount " + getSpaces(25 - discount.length()) + ("-" + discount)));

        //allLines.add(new TicketLineConstructor("Total After Discount " + getSpaces(3) + (totalAfrDiscount)));
        String aCount = ticket.printTicketCount();
        //allLines.add(new TicketLineConstructor("Service Charge" + getSpaces(21-serviceCharge.length()) + serviceCharge));
//       if(ticket.getCharges().size()!=0){
//             for(int i=0;i<ticket.getCharges().size();i++){
//              System.out.println("ticket.getTaxes().get(i).getTax()--"+ticket.getCharges().get(i).getRetailSCharge());
//              if(ticket.getCharges().get(i).getRetailSCharge()!=0.00){
//                 allLines.add(new TicketLineConstructor(ticket.getCharges().get(i).getServiceChargeInfo().getName() + getSpaces(15 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getRetailTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getCharges().get(i).getRetailSCharge()))));
//           
//          }
//       }

        //calling consolidated tax (logic applied to bring erp tax configuration) by Shilpa
        if (ticket.getTaxes().size() != 0) {
            for (Map.Entry<String, NameTaxMapInfo> entry : m_oTicket.getNametaxMap().entrySet()) {
                String taxName = entry.getValue().getName();;
                double taxValue = entry.getValue().getTaxValue();
                if (taxValue != 0.00) {
                    allLines.add(new TicketLineConstructor(taxName + getSpaces(35 - (Formats.DoubleValue.formatValue(taxValue).length() + taxName.length())) + (Formats.DoubleValue.formatValue(taxValue))));
                }
            }

        }
//       if(ticket.getTaxes().size()!=0){
//          for(int i=0;i<ticket.getTaxes().size();i++){
//              System.out.println("ticket.getTaxes().get(i).getTax()--"+ticket.getTaxes().get(i).getTax());
//              if(ticket.getTaxes().get(i).getTax()!=0.00){
//                 allLines.add(new TicketLineConstructor(ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(27 - Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getRetailTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getTaxes().get(i).getRetailTax()))));
//              }
//          }
//
//      }
//
//          if(ticket.getServiceTaxes().size()!=0){
//          for(int i=0;i<ticket.getServiceTaxes().size();i++){
//              System.out.println("ticket.getServiceTaxes().get(i).getTax()--"+ticket.getServiceTaxes().get(i).getRetailServiceTax());
//              if(ticket.getServiceTaxes().get(i).getRetailServiceTax()!=0.00){
//                 allLines.add(new TicketLineConstructor(ticket.getServiceTaxes().get(i).getTaxInfo().getName() + getSpaces(17 - Formats.DoubleValue.formatValue(ticket.getServiceTaxes().get(i).getRetailServiceTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getServiceTaxes().get(i).getRetailServiceTax()))));
//              }
//          }
//
//      }
//     // allLines.add(new TicketLineConstructor("Service Tax" + getSpaces(24-serviceTax.length()) +serviceTax));
//      //allLines.add(new TicketLineConstructor("Swach Bharat Tax" + getSpaces(19-swachBharatTax.length()) +swachBharatTax));
//       if(ticket.getSwachBharatTaxes().size()!=0){
//          for(int i=0;i<ticket.getSwachBharatTaxes().size();i++){
//              System.out.println("ticket.getServiceTaxes().get(i).getTax()--"+ticket.getSwachBharatTaxes().get(i).getRetailSwachBharatTax());
//              if(ticket.getSwachBharatTaxes().get(i).getRetailSwachBharatTax()!=0.00){
//                 allLines.add(new TicketLineConstructor(ticket.getSwachBharatTaxes().get(i).getTaxInfo().getName() + getSpaces(11 - Formats.DoubleValue.formatValue(ticket.getSwachBharatTaxes().get(i).getRetailSwachBharatTax()).length()) + (Formats.DoubleValue.formatValue(ticket.getSwachBharatTaxes().get(i).getRetailSwachBharatTax()))));
//              }
//          }
//
//      }
        // allLines.add(new TicketLineConstructor("Round Off: " + getSpaces(24-roundoff.length()) + roundoff));
        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Total Amount : " + getSpaces(20 - total.length()) + (total)));
        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Thank you! Please Visit Us again"));
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
        allLines.add(new TicketLineConstructor("Description" + getSpaces(17) + "Qty" + getSpaces(14) + "Price" + getSpaces(9) + "Value(INR)"));
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        for (RetailTicketLineInfo tLine : ticket.getLines()) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String subValue = tLine.printPriceLine();
            String total = "0.00";
            allLines.add(new TicketLineConstructor(prodName + getSpaces(28 - prodName.length()) + qty + getSpaces(15 - qty.length() + 7 - subValue.length()) + subValue + getSpaces(9 - qty.length() + 11 - subValue.length()) + total));
        }
        System.out.println("ticket.getTaxes().get(0).getTaxInfo().getName();" + ticket.getTaxes().get(0).getTaxInfo().getName());
        allLines.add(new TicketLineConstructor(getDottedLine(90)));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total " + getSpaces(25) + "0.00"));
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Discount " + getSpaces(22) + "0.00"));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Total After Discount " + getSpaces(10) + "0.00"));
        if (ticket.getTaxes().size() != 0) {
            for (int i = 0; i < ticket.getTaxes().size(); i++) {
                if (ticket.getTaxes().get(i).getTax() != 0.00) {
                    allLines.add(new TicketLineConstructor(getSpaces(33) + ticket.getTaxes().get(i).getTaxInfo().getName() + getSpaces(31 - ticket.getTaxes().get(i).getTaxInfo().getName().length()) + "0.00"));
                }
            }

        }
        String aCount = ticket.printTicketCount();
        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Charge 6%" + getSpaces(14) + "0.00"));

        allLines.add(new TicketLineConstructor(getSpaces(33) + "Service Tax 4.94%" + getSpaces(14) + "0.00"));



        allLines.add(new TicketLineConstructor(getSpaces(33) + "Grand Total : " + getSpaces(17) + "0.00"));

        //allLines.add(new TicketLineConstructor(announce3));
        return allLines;
    }

    private void addlinesaddBlankLines(int count, java.util.List<TicketLineConstructor> allLines) {
        for (int i = 0; i < count; i++) {
            allLines.add(new TicketLineConstructor(""));
        }
    }
//    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
//        String sresource = dlSystem.getResourceAsXML(sresourcename);
//
//        if (sresource == null) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
//            msg.show(JRetailPanelTakeAway.this);
//        } else {
//            try {
//                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
//                script.put("taxes", taxcollection);
//                script.put("charges", chargecollection);
//                script.put("taxeslogic", taxeslogic);
//                script.put("chargeslogic", chargeslogic);
//                script.put("staxeslogic", staxeslogic);
//                script.put("ticket", ticket);
//                script.put("place", ticketext);
//                m_TTP.printTicket(script.eval(sresource).toString());
//            } catch (ScriptException e) {
//                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
//                msg.show(JRetailPanelTakeAway.this);
//            } catch (TicketPrinterException e) {
//                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
//                msg.show(JRetailPanelTakeAway.this);
//            }
//        }
//
//
//    }
//    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
//             java.util.List<TicketLineConstructor> allLines = null;
//         java.util.List<TicketLineConstructor> startallLines = new ArrayList<TicketLineConstructor>();
//         int count =0;
//           com.openbravo.pos.printer.printer.ImageBillPrinter printer = new ImageBillPrinter();
//        if(sresourcename.equals("Printer.Bill")){
//             allLines = getAllLines(ticket,ticketext);
//
//        }else if(sresourcename.equals("Printer.NonChargableBill")){
//            allLines = getNonChargeableLines(ticket,ticketext);
//        }
////        try {
//                       int divideLines = allLines.size()/48;
//                       int remainder = allLines.size()%48;
//                        System.out.println("divideLines---"+divideLines+"--"+remainder);
//                       int value = 48;
//                       int k=0;
//                        if(divideLines>0){
//                       for(int i=0;i<divideLines;i++){
//                           for(int j=k;j<value;j++){
//
//                                startallLines.add(new TicketLineConstructor(allLines.get(j).getLine()));
//                                System.out.println("allLines.get(j).getLine()--"+allLines.get(j).getLine());
//                           }
//                            try {
//                                printer.print(startallLines);
//                            } catch (PrinterException ex) {
//                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                           if(allLines.size()>48){
////                            try {
////                                Thread.sleep(20000);
////                            } catch (InterruptedException ex) {
////                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////                            }
//                           int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrintcontinue"), AppLocal.getIntString("message.title"), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
//                           if (res == JOptionPane.OK_OPTION) {
//                               k=value;
//                               value =value+48;
//                               startallLines = new ArrayList<TicketLineConstructor>();
//                               System.out.println("startallLinest--"+startallLines.size());
//                                startallLines.clear();
//                           }else{
//                               break;
//                           }
//                           }
//
//                       }}
//                        System.out.println("value---"+value+k+"--"+remainder);
//                      if(remainder>0){
//                             startallLines = new ArrayList<TicketLineConstructor>();
//                               System.out.println("startallLinest--"+startallLines.size());
//                                startallLines.clear();
//                       for(int m=k;m<k+remainder;m++){
//
//                            startallLines.add(new TicketLineConstructor(allLines.get(m).getLine()));
//                            System.out.println("allLines.get(j).getLine()--"+allLines.get(m).getLine());
//                       }
//                        try {
//                            printer.print(startallLines);
//                        } catch (PrinterException ex) {
//                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//             }
////            printer.print(allLines);
////        } catch (PrinterException ex) {
////            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
//

    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
        String sresource = dlSystem.getResourceAsXML(sresourcename);

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTakeAway.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                //  script.put("charges", chargecollection);
                script.put("taxeslogic", taxeslogic);
                //    script.put("chargeslogic", chargeslogic);
                // script.put("staxeslogic", staxeslogic);
                //  script.put("sbtaxeslogic", sbtaxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTakeAway.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTakeAway.this);
            }
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

            JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, new JRMapArrayDataSource(new Object[]{reportfields}));

            PrintService service = ReportUtils.getPrintService(m_App.getProperties().getProperty("machine.printername"));

            JRPrinterAWT300.printPages(jp, 0, jp.getPages().size() - 1, service);

        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
        }
    }

    public void visorTicketLine(RetailTicketLineInfo oLine) {
        if (oLine == null) {
            m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelTakeAway.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelTakeAway.this);
            }
        }
    }

    private void showMessage(JRetailPanelTakeAway aThis, String msg) {
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

    public int getServedStatus() {
        return served;
    }

    public void setServedStatus(int served) {
        this.served = served;
    }

    public void printTicket(String resource) {
        printTicket(resource, m_oTicket, m_oTicketExt);
    }

    public Object executeEventAndRefresh(String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
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

    public Object executeEventAndRefreshForKot(String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(m_oTicket, m_oTicketExt);
            scr.setSelectedIndex(m_ticketlines.getSelectedIndex());
            Object result = evalScript(scr, resource, args);
            setSelectedIndex(scr.getSelectedIndex());
            return result;
        }
    }

    private Object executeEvent(RetailTicketInfo ticket, Object ticketext, String eventkey, ScriptArg... args) {

        String resource = m_jbtnconfig.getEvent(eventkey);
        if (resource == null) {
            return null;
        } else {
            ScriptObject scr = new ScriptObject(ticket, ticketext);
            return evalScript(scr, resource, args);
        }
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
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
                return JRetailPanelTakeAway.this.getInputValue();
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
            JRetailPanelTakeAway.this.printReport(resourcefile, ticket, ticketext);
        }

        public void printTicket(String sresourcename) {
            JRetailPanelTakeAway.this.printTicket(sresourcename, ticket, ticketext);
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
            for (ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }

            return script.eval(code);
        }
    }

    private void populateDeliveryBoy() {
//        try {
//            deliveryBoyLines = dlCustomers.getDeliveryBoyList();
//            m_jDeliveryBoy.addItem("");
//            for (int i = 0; i < deliveryBoyLines.size(); i++) {
//                m_jDeliveryBoy.addItem(deliveryBoyLines.get(i).getName());
//            }
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * This method is called from within the constructor to Tinitialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jPanelScripts = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jLogout = new javax.swing.JButton();
        m_jbtnPrintBill = new javax.swing.JButton();
        m_jSettleBill = new javax.swing.JButton();
        m_jSplitBtn = new javax.swing.JButton();
        m_jBtnCancelBill = new javax.swing.JButton();
        m_jBtnBillOnHold = new javax.swing.JButton();
        m_jBtnDiscount = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jLblUserInfo = new javax.swing.JLabel();
        m_jUser = new javax.swing.JLabel();
        m_jLblCurrentDate = new javax.swing.JLabel();
        m_jLblTime = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        m_jLblBillNo = new javax.swing.JLabel();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddon = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        m_jLblItemCode = new javax.swing.JLabel();
        m_jTxtItemCode = new javax.swing.JTextField();
        m_jLblItemName = new javax.swing.JLabel();
        m_jCboItemName = new javax.swing.JComboBox();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jPlus = new javax.swing.JButton();
        m_jMinus = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jCalculatePromotion = new javax.swing.JButton();
        m_jAction = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        m_jKot = new javax.swing.JButton();
        m_jBtnServed = new javax.swing.JButton();
        m_jEraser = new javax.swing.JButton();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        catcontainer = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jTxtTotalPaid = new javax.swing.JLabel();
        m_jTxtChange = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        m_jTax = new javax.swing.JComboBox();
        m_jPor = new javax.swing.JLabel();
        m_jaddtax = new javax.swing.JToggleButton();
        jLblPrinterStatus = new javax.swing.JLabel();
        m_jKeyFactory = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jSubtotalEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros4 = new javax.swing.JLabel();
        m_jTaxesEuros1 = new javax.swing.JLabel();
        m_jLblTotalEuros5 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        m_jLblTotalEuros6 = new javax.swing.JLabel();
        m_jDiscount1 = new javax.swing.JLabel();
        m_jTotalEuros = new javax.swing.JLabel();
        jTaxPanel = new javax.swing.JPanel();
        m_jServiceTaxLbl = new javax.swing.JLabel();
        m_jServiceTax = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTaxList = new javax.swing.JList();
        m_jSwachBharatLbl = new javax.swing.JLabel();
        m_jSwachBharat = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        m_jPromoDiscount = new javax.swing.JLabel();
        m_jProducts = new javax.swing.JPanel();

        setBackground(new java.awt.Color(222, 232, 231));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setBackground(new java.awt.Color(222, 232, 231));
        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jButtons.setBackground(new java.awt.Color(222, 232, 231));
        m_jButtons.setPreferredSize(new java.awt.Dimension(4, 10));
        m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

        m_jPanelScripts.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setBackground(new java.awt.Color(222, 232, 231));
        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(222, 232, 231));
        jPanel1.setPreferredSize(new java.awt.Dimension(620, 47));
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
        jPanel1.add(m_jbtnScale, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 5, -1));

        m_jLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1logout.png"))); // NOI18N
        m_jLogout.setToolTipText("Logout");
        m_jLogout.setFocusable(false);
        m_jLogout.setPreferredSize(new java.awt.Dimension(40, 40));
        m_jLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(m_jLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 0, -1, -1));

        m_jbtnPrintBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Print-Bill.png"))); // NOI18N
        m_jbtnPrintBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jbtnPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnPrintBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 0, -1, -1));

        m_jSettleBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Settle-Bill.png"))); // NOI18N
        m_jSettleBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jSettleBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSettleBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jSettleBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, -1, -1));

        m_jSplitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/190-40-BUTTON.png"))); // NOI18N
        m_jSplitBtn.setMnemonic('f');
        m_jSplitBtn.setFocusPainted(false);
        m_jSplitBtn.setFocusable(false);
        m_jSplitBtn.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jSplitBtn.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jSplitBtn.setRequestFocusEnabled(false);
        m_jSplitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSplitBtnActionPerformed(evt);
            }
        });
        jPanel1.add(m_jSplitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        m_jBtnCancelBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/can-bill.png"))); // NOI18N
        m_jBtnCancelBill.setToolTipText("Cancel the Bill");
        m_jBtnCancelBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCancelBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancelBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnCancelBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        m_jBtnBillOnHold.setBackground(new java.awt.Color(130, 130, 65));
        m_jBtnBillOnHold.setForeground(new java.awt.Color(153, 153, 136));
        m_jBtnBillOnHold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/HOLD-BILL.png"))); // NOI18N
        m_jBtnBillOnHold.setToolTipText("");
        m_jBtnBillOnHold.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnBillOnHold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnBillOnHoldActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnBillOnHold, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 90, 40));

        m_jBtnDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/bill-discount.png"))); // NOI18N
        m_jBtnDiscount.setMnemonic('i');
        m_jBtnDiscount.setToolTipText("Add Discount");
        m_jBtnDiscount.setFocusPainted(false);
        m_jBtnDiscount.setFocusable(false);
        m_jBtnDiscount.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jBtnDiscount.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnDiscount.setRequestFocusEnabled(false);
        m_jBtnDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnDiscountActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        m_jButtonsExt.add(jPanel1);

        m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

        m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

        m_jPanelBag.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanelBag.setFocusable(false);
        m_jPanelBag.setPreferredSize(new java.awt.Dimension(800, 35));
        m_jPanelBag.setRequestFocusEnabled(false);
        m_jPanelBag.setLayout(new java.awt.BorderLayout());
        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(80, 102, 116));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setPreferredSize(new java.awt.Dimension(1024, 90));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(222, 232, 231));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/1header-habanero.png"))); // NOI18N
        jLabel3.setAutoscrolls(true);
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel3.setMaximumSize(new java.awt.Dimension(1450, 61));
        jLabel3.setMinimumSize(new java.awt.Dimension(1024, 61));
        jLabel3.setPreferredSize(new java.awt.Dimension(1024, 45));
        jLabel3.setRequestFocusEnabled(false);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1551, -1));

        m_jLblUserInfo.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblUserInfo.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblUserInfo.setText("   LOGGED IN USER:");
        m_jLblUserInfo.setFocusable(false);
        m_jLblUserInfo.setPreferredSize(new java.awt.Dimension(340, 16));
        m_jLblUserInfo.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblUserInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 140, 50));

        m_jUser.setForeground(new java.awt.Color(252, 248, 0));
        m_jUser.setText("jLabel6");
        jPanel6.add(m_jUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 70, 50));

        m_jLblCurrentDate.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblCurrentDate.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblCurrentDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblCurrentDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Date-icon.png"))); // NOI18N
        m_jLblCurrentDate.setFocusable(false);
        m_jLblCurrentDate.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblCurrentDate.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblCurrentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 45, 180, 45));

        m_jLblTime.setBackground(new java.awt.Color(80, 102, 160));
        m_jLblTime.setForeground(new java.awt.Color(255, 255, 255));
        m_jLblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Clock-icon.png"))); // NOI18N
        m_jLblTime.setText("                                                                                       jLabel2");
        m_jLblTime.setFocusable(false);
        m_jLblTime.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        m_jLblTime.setPreferredSize(new java.awt.Dimension(300, 16));
        m_jLblTime.setRequestFocusEnabled(false);
        jPanel6.add(m_jLblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 45, 230, 45));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bill No:");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 40, 50, 50));

        m_jLblBillNo.setForeground(new java.awt.Color(252, 248, 0));
        jPanel6.add(m_jLblBillNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, 90, 50));

        m_jOptions.add(jPanel6, java.awt.BorderLayout.NORTH);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBackground(new java.awt.Color(222, 232, 231));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(222, 232, 231));
        jPanel10.setPreferredSize(new java.awt.Dimension(803, 48));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonAddon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Add-on.png"))); // NOI18N
        jButtonAddon.setPreferredSize(new java.awt.Dimension(90, 40));
        jButtonAddon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddonActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonAddon, new org.netbeans.lib.awtextra.AbsoluteConstraints(793, 2, 90, 40));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1Category.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(888, 2, 90, 40));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/subcategory.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(983, 2, 90, 40));

        m_jLblItemCode.setText("ITEM CODE");
        jPanel10.add(m_jLblItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 90, 20));

        m_jTxtItemCode.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        m_jTxtItemCode.setMinimumSize(new java.awt.Dimension(123, 20));
        m_jTxtItemCode.setPreferredSize(new java.awt.Dimension(123, 20));
        m_jTxtItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtItemCodeActionPerformed(evt);
            }
        });
        jPanel10.add(m_jTxtItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, 20));

        m_jLblItemName.setText("ITEM NAME");
        jPanel10.add(m_jLblItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, 20));

        m_jCboItemName.setEditable(true);
        m_jCboItemName.setAutoscrolls(true);
        m_jCboItemName.setMaximumSize(new java.awt.Dimension(123, 20));
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
        jPanel10.add(m_jCboItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 340, -1));

        m_jPanTicket.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        m_jPanelCentral.setFocusable(false);
        m_jPanelCentral.setRequestFocusEnabled(false);
        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(222, 232, 231));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel2.setMinimumSize(new java.awt.Dimension(66, 338));
        jPanel2.setPreferredSize(new java.awt.Dimension(61, 400));

        m_jPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TPlus.png"))); // NOI18N
        m_jPlus.setToolTipText("Increase selected item quantity by one");
        m_jPlus.setFocusPainted(false);
        m_jPlus.setFocusable(false);
        m_jPlus.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jPlus.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jPlus.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jPlus.setRequestFocusEnabled(false);
        m_jPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPlusActionPerformed(evt);
            }
        });

        m_jMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TMinus.png"))); // NOI18N
        m_jMinus.setToolTipText("Decrease selected item's quantity by one");
        m_jMinus.setFocusPainted(false);
        m_jMinus.setFocusable(false);
        m_jMinus.setMaximumSize(new java.awt.Dimension(51, 42));
        m_jMinus.setMinimumSize(new java.awt.Dimension(51, 42));
        m_jMinus.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jMinus.setRequestFocusEnabled(false);
        m_jMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jMinusActionPerformed(evt);
            }
        });

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TEdit.png"))); // NOI18N
        m_jEditLine.setMnemonic('e');
        m_jEditLine.setToolTipText("Edit Properties of selected item");
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });

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

        m_jAction.setBorder(null);
        m_jAction.setBorderPainted(false);
        m_jAction.setPreferredSize(new java.awt.Dimension(10, 2));
        m_jAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jActionActionPerformed(evt);
            }
        });

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/Tclose.png"))); // NOI18N
        m_jDelete.setMnemonic('d');
        m_jDelete.setToolTipText("Remove total quantity of selected item");
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

        m_jKot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TKOT.png"))); // NOI18N
        m_jKot.setToolTipText("Send All Fresh Items to Kitchen");
        m_jKot.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jKot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnKotActionPerformed(evt);
            }
        });

        m_jBtnServed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/servedsymbol.png"))); // NOI18N
        m_jBtnServed.setToolTipText("Serve Selected Item");
        m_jBtnServed.setPreferredSize(new java.awt.Dimension(91, 73));
        m_jBtnServed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnServedActionPerformed(evt);
            }
        });

        m_jEraser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/TDelete.png"))); // NOI18N
        m_jEraser.setMnemonic('i');
        m_jEraser.setToolTipText("Clear All Non KOT Items");
        m_jEraser.setFocusPainted(false);
        m_jEraser.setFocusable(false);
        m_jEraser.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEraser.setRequestFocusEnabled(false);
        m_jEraser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEraserActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPlus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jMinus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jDelete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jCalculatePromotion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(112, 112, 112)
                        .add(m_jAction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jEditLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(m_jPlus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(m_jMinus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(m_jDelete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(m_jEditLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(61, 61, 61)
                        .add(m_jAction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jCalculatePromotion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jPanelCentral.add(jPanel5, java.awt.BorderLayout.LINE_END);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jContEntries.setFocusable(false);
        m_jContEntries.setPreferredSize(new java.awt.Dimension(501, 500));
        m_jContEntries.setRequestFocusEnabled(false);
        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setMinimumSize(new java.awt.Dimension(508, 500));
        m_jPanEntries.setPreferredSize(new java.awt.Dimension(495, 525));
        m_jPanEntries.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        catcontainer.setPreferredSize(new java.awt.Dimension(300, 200));
        catcontainer.setLayout(new java.awt.BorderLayout());
        m_jPanEntries.add(catcontainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 508, 510));

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.PAGE_START);

        m_jPanTicket.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

        jPanel12.setBackground(new java.awt.Color(222, 232, 231));
        jPanel12.setFocusable(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(600, 5));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(300, 402));
        jPanel12.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -500, 989, 509));

        m_jPanTicket.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(222, 232, 231));
        jPanel4.setPreferredSize(new java.awt.Dimension(1024, 210));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel7.setMaximumSize(new java.awt.Dimension(700, 158));
        jPanel7.setPreferredSize(new java.awt.Dimension(320, 158));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("TOTAL PAID");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("CHANGE");

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

        m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setBorder(null);
        m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(4, 4));
        m_jKeyFactory.setRequestFocusEnabled(false);
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jKeyFactory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(65, 65, 65)
                        .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jLblPrinterStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jKeyFactory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(21, 21, 21)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jaddtax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(3, 3, 3)
                        .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(19, 19, 19)
                        .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 392, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(270, 270, 270)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtTotalPaid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtChange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1325, 1, 170, 160));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel8.setPreferredSize(new java.awt.Dimension(1551, 193));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Bill Details");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 188, -1));

        m_jSubtotalEuros1.setBackground(new java.awt.Color(255, 255, 255));
        m_jSubtotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros1.setFocusable(false);
        m_jSubtotalEuros1.setOpaque(true);
        m_jSubtotalEuros1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jSubtotalEuros1.setRequestFocusEnabled(false);
        jPanel8.add(m_jSubtotalEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 80, 23));

        m_jLblTotalEuros4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros4.setText("SUB TOTAL");
        jPanel8.add(m_jLblTotalEuros4, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 37, 94, 23));

        m_jTaxesEuros1.setBackground(new java.awt.Color(255, 255, 255));
        m_jTaxesEuros1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros1.setFocusable(false);
        m_jTaxesEuros1.setOpaque(true);
        m_jTaxesEuros1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTaxesEuros1.setRequestFocusEnabled(false);
        jPanel8.add(m_jTaxesEuros1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 80, 23));

        m_jLblTotalEuros5.setBackground(new java.awt.Color(255, 255, 255));
        m_jLblTotalEuros5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros5.setText("TAXES");
        jPanel8.add(m_jLblTotalEuros5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 60, 23));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("DISCOUNT");
        jPanel8.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, 23));

        m_jLblTotalEuros6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jLblTotalEuros6.setText("TOTAL SALES");
        jPanel8.add(m_jLblTotalEuros6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 80, 20));

        m_jDiscount1.setBackground(new java.awt.Color(255, 255, 255));
        m_jDiscount1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jDiscount1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jDiscount1.setFocusable(false);
        m_jDiscount1.setOpaque(true);
        m_jDiscount1.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jDiscount1.setRequestFocusEnabled(false);
        jPanel8.add(m_jDiscount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 80, 23));

        m_jTotalEuros.setBackground(new java.awt.Color(255, 255, 255));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setFocusable(false);
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        jPanel8.add(m_jTotalEuros, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 80, 23));

        jTaxPanel.setBackground(new java.awt.Color(255, 255, 255));
        jTaxPanel.setPreferredSize(new java.awt.Dimension(202, 157));
        jTaxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jServiceTaxLbl.setText("SERVICE TAX");
        jTaxPanel.add(m_jServiceTaxLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 100, 23));

        m_jServiceTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTaxPanel.add(m_jServiceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 73, 23));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tax Breakup");
        jTaxPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 11, 194, -1));

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(m_jTaxList);

        jTaxPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 37, 200, 140));

        m_jSwachBharatLbl.setText("SWACHH BHARAT TAX");
        jTaxPanel.add(m_jSwachBharatLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 130, 20));

        m_jSwachBharat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTaxPanel.add(m_jSwachBharat, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 70, 20));

        jPanel8.add(jTaxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 1, -1, 178));

        jLabel6.setText("PROMO DISCOUNT");
        jPanel8.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 120, 20));

        m_jPromoDiscount.setBackground(new java.awt.Color(255, 255, 255));
        m_jPromoDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPromoDiscount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPromoDiscount.setFocusable(false);
        m_jPromoDiscount.setOpaque(true);
        m_jPromoDiscount.setPreferredSize(new java.awt.Dimension(123, 25));
        m_jPromoDiscount.setRequestFocusEnabled(false);
        jPanel8.add(m_jPromoDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 80, 23));

        jPanel4.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 1, 434, -1));

        m_jProducts.setBackground(new java.awt.Color(255, 255, 255));
        m_jProducts.setLayout(new java.awt.CardLayout());
        jPanel4.add(m_jProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 1, 551, 190));

        m_jPanContainer.add(jPanel4, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped

        m_jKeyFactory.setText(null);
        try {
            stateTransition(evt.getKeyChar());
        } catch (BasicException ex) {
            logger.info("Order No." + m_oTicket.getOrderId() + " exception while calling stateTransition" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed
        //   if(m_oTicket.getParentId()==null)  {
        int i = m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            String addonVal = m_oTicket.getLine(i).getAddonId();
            int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
            if (kotStatus != 0) {
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {

                    JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineDelete");
                    if (JReasonEditor.getCancel() == true) {
                        String sessionId = null;
                        try {
                            //   sessionId = dlReceipts.getTakeAwayFloorId(m_App.getProperties().getProperty("machine.Floor"));
                            java.util.List<BeanInfo> takeawayInfo = dlReceipts.getTakeAwayInfoList();
                            sessionId = takeawayInfo.get(0).getName();
                            printerInfo = dlReceipts.getPrinterInfo(sessionId);
                        } catch (BasicException ex) {
                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        printCancelKotTicket(m_oTicket, m_oTicket.getLine(i), m_oTicketExt, printerInfo);
                        removeTicketLine(i);
                        //adding the logic of deleting the addon items
                        if (addonVal != null && primaryAddon == 1) {
                            int j = 0;
                            while (j < m_oTicket.getLinesCount()) {
                                if (addonVal.equals(m_oTicket.getLine(j).getAddonId())) {
                                    removeTicketLine(j);
                                    j = 0;
                                } else {
                                    j++;
                                }
                            }
                        }

                        m_oTicket.refreshTxtTakeAwayFields(0);
                        if (m_oTicket.getLinesCount() == 0) {
                            m_jTxtChange.setText("");
//                        m_jServiceCharge.setText("Rs. 0.00");
                            m_jTaxesEuros1.setText("Rs. 0.00");
                            m_jTotalEuros.setText("Rs. 0.00");
                            refreshTicket();
                            taxModel.removeAllElements();
                        }
                    }
                } else {
                    showMessage(this, "Item is sent to kot.Only Cashier or Manager can cancel the line.");
                }
            } else {
                removeTicketLine(i);
                if (addonVal != null && primaryAddon == 1) {
                    int j = 0;
                    while (j < m_oTicket.getLinesCount()) {
                        if (addonVal.equals(m_oTicket.getLine(j).getAddonId())) {
                            removeTicketLine(j);
                            j = 0;
                        } else {
                            j++;
                        }
                    }
                }
                m_oTicket.refreshTxtTakeAwayFields(0);
                if (m_oTicket.getLinesCount() == 0) {
                    m_jTxtChange.setText("");
                    //m_oTicket.refreshTxtFields(0);
//              m_jServiceCharge.setText("Rs. 0.00");
                    m_jTaxesEuros1.setText("Rs. 0.00");
                    m_jTotalEuros.setText("Rs. 0.00");
                    refreshTicket();
                    taxModel.removeAllElements();
                }

            }

        }
        m_jTxtItemCode.setText("");
        itemName.setText("");
        //  }
    }//GEN-LAST:event_m_jDeleteActionPerformed
    private synchronized void printCancelKotTicket(RetailTicketInfo ticket, RetailTicketLineInfo kotLine, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo) {
        java.util.List<TicketLineConstructor> allLines = null;
        String productionAreaName = "";
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        com.openbravo.pos.printer.printer.KotBillPrinter printerKot = new KotBillPrinter();
        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        logger.info("cancel  kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

        for (int j = 0; j < printerInfo.size(); j++) {
            if (printerInfo.get(j).getProductionAreaType().equals(kotLine.getProductionAreaType())) {
                productionAreaName = printerInfo.get(j).getName();
                allLines = getCancelKotLines(ticket, ticketExt, kotLine, productionAreaName);
                try {
                    if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("BlrKoramangala") || storeLocation.equals("Chennai") || storeLocation.equals("Hyderabad")) {
                        printer.printKot(allLines, printerInfo.get(j).getPath());
                    } else {
                        printerKot.print(allLines, printerInfo.get(j).getPath());
                    }
                    break;
                } catch (PrinterException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private java.util.List<TicketLineConstructor> getCancelKotLines(RetailTicketInfo ticket, Object ticketext, RetailTicketLineInfo kotLine, String productionAreaName) {
        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        allLines.add(new TicketLineConstructor("\n"));
        allLines.add(new TicketLineConstructor("\n"));
        allLines.add(new TicketLineConstructor("\n"));
        allLines.add(new TicketLineConstructor("\n"));
        allLines.add(new TicketLineConstructor("\n"));
        allLines.add(new TicketLineConstructor("          " + "Cancel Kot Ticket"));
        allLines.add(new TicketLineConstructor("          " + productionAreaName));
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(16 - (ticket.printDate().substring(0, 12).length()))));
        allLines.add(new TicketLineConstructor("Time: " + (ticket.printTime()) + getSpaces(16 - (ticket.printTime().length())) + "Table No:" + getSpaces(1) + ticket.getName(m_oTicketExt)));
        allLines.add(new TicketLineConstructor("User Name: " + (m_oTicket.getUser()).getName()));
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        allLines.add(new TicketLineConstructor("Description                     " + "Qty"));
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        String prodName = kotLine.printName();
        prodName = prodName.replaceAll("&amp;", "&");
        // String qty = Formats.DoubleValue.formatValue(tLine.printQty());
        String qty = kotLine.printMultiply();
        if (prodName.length() > 30) {
            prodName = WordUtils.wrap(prodName, 30);
            String[] prodNameArray = prodName.split("\n");
            for (int i = 0; i < prodNameArray.length - 1; i++) {
                allLines.add(new TicketLineConstructor(prodNameArray[i]));
            }

            allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(33 - prodNameArray[prodNameArray.length - 1].length()) + qty));

        } else {
            allLines.add(new TicketLineConstructor(prodName + getSpaces(33 - prodName.length()) + qty));

        }
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        return allLines;

    }
    private void m_jPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPlusActionPerformed
        //  if(m_oTicket.getParentId()==null)  {
        //    m_ticketlines.selectionUp();
        int i = m_ticketlines.getSelectedIndex();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        System.out.println(getActiveTicket().getLine(i).getIsKot() + "plus testing" + m_ticketlines);
        if (m_oTicket.getLine(i).getIsKot() != 1) {
            newline.setMultiply(newline.getMultiply() + 1.0);
            paintTicketLine(i, newline);
        }
        System.out.println("printing instruction" + newline.getInstruction());
        //   }
    }//GEN-LAST:event_m_jPlusActionPerformed

    private void m_jMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMinusActionPerformed
        int i = m_ticketlines.getSelectedIndex();
        String addonVal = m_oTicket.getLine(i).getAddonId();
        int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        if (newline.getMultiply() > 1.0) {
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            if (kotStatus != 0) {
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {
                    JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineMinus");
                    if (JReasonEditor.getCancel() == true) {
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        if (addonVal != null && primaryAddon == 1) {
                            int j = 0;
                            while (j < m_oTicket.getLinesCount()) {
                                if (addonVal.equals(m_oTicket.getLine(j).getAddonId()) && m_oTicket.getLine(j).getPrimaryAddon() == 0) {
                                    removeTicketLine(j);
                                    j = 0;
                                } else {
                                    j++;
                                }
                            }
                        }
                    }
                } else {
                    showMessage(this, "Item is sent to kot.Only Cashier or Manager can cancel the line.");
                }
            } else {
                newline.setMultiply(newline.getMultiply() - 1.0);
                if (addonVal != null && primaryAddon == 1) {
                    int j = 0;
                    while (j < m_oTicket.getLinesCount()) {
                        if (addonVal.equals(m_oTicket.getLine(j).getAddonId()) && m_oTicket.getLine(j).getPrimaryAddon() == 0) {
                            removeTicketLine(j);
                            j = 0;
                        } else {
                            j++;
                        }
                    }
                }

            }


            if (newline.getMultiply() > 0.0) {
                paintTicketLine(i, newline);
            }
        }
    }//GEN-LAST:event_m_jMinusActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
        int i = m_ticketlines.getSelectedIndex();
        if (m_oTicket.getLine(i).getIsKot() == 0) {
            if (i < 0) {
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
                        //   m_oTicket.refreshTxtFields(1);
                        //below line is for promotion products
//                    ProductInfoExt product = getInputProduct();
//                    addTicketLine(product, 1.0, product.getPriceSell());
                    }
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                }
            }
        }
    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLogoutActionPerformed
        //  m_RootApp = null;
        logger.info("Start Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        getActiveTicket().setTicketOpen(false);
        try {
            dlReceipts.updateSharedTicket(getActiveTicket().getPlaceId(), getActiveTicket());
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  logout updating shared ticket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_RootApp = (JRootApp) m_App;
        m_RootApp.closeAppView();
        logger.info("End Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));


}//GEN-LAST:event_m_jLogoutActionPerformed

    private void m_jCalculatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCalculatePromotionActionPerformed
        //        ArrayList<BuyGetPriceInfo> leastProductList = new ArrayList<BuyGetPriceInfo>();
//        StringBuilder b = new StringBuilder();
//        StringBuilder b1 = new StringBuilder();
        //Least value promotion          
//        try {
//            dlSales.deleteTempTicketlines();
//            System.out.println("m_oTicket----" + m_oTicket.getLine(0).getCampaignId());
//            dlSales.saveRetailTempTicketlines(m_oTicket);
//            java.util.ArrayList<String> campaignId = new ArrayList<String>();
//            m_oTicket.setLeastValueDiscount(0.00);
//            pdtCampaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getPdtCampaignId();
//            for (int i = 0; i < pdtCampaignIdList.size(); i++) {
//                campaignId.add("'" + pdtCampaignIdList.get(i).getcampaignId() + "'");
//            }
//
//            Iterator<?> it = campaignId.iterator();
//            while (it.hasNext()) {
//                b.append(it.next());
//                if (it.hasNext()) {
//                    b.append(',');
//                }
//            }
//            String promoCampaignId = b.toString();
//            if (!promoCampaignId.equals("")) {
//                double price = 0;
//                double taxAmount = 0;
//                double serviceTaxAmount = 0;
//                double sbtaxAmount = 0;
//
//                pdtBuyGetList = (ArrayList<BuyGetInfo>) dlSales.getbuyGetTotalQty(promoCampaignId);
//
//                for (int i = 0; i < pdtBuyGetList.size(); i++) {
//
//                    pdtBuyGetQtyList = (ArrayList<BuyGetQtyInfo>) dlSales.getbuyGetQty(pdtBuyGetList.get(i).getCampaignId(), pdtBuyGetList.get(i).getQty());
//                    int sumOfBuyGet = (int) pdtBuyGetQtyList.get(0).getBuyQty() + (int) pdtBuyGetQtyList.get(0).getQty();
//                    int remaining = 0;
//                    remaining = pdtBuyGetList.get(0).getQty() / (int) sumOfBuyGet;
//                    int qty = remaining * pdtBuyGetQtyList.get(0).getQty();
//                    if (pdtBuyGetQtyList.size() != 0) {
//                        pdtBuyGetPriceList = (ArrayList<BuyGetPriceInfo>) dlSales.getbuyGetLeastPrice(pdtBuyGetList.get(i).getCampaignId(), qty);
//                        pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
//                        for (int j = 0; j < pdtBuyGetPriceList.size(); j++) {
//                            price = price + pdtBuyGetPriceList.get(j).getPrice();
////                            taxAmount = taxAmount + pdtBuyGetPriceList.get(j).getTaxRate();
////                            serviceTaxAmount = serviceTaxAmount + pdtBuyGetPriceList.get(j).getServiceTax();
////                            sbtaxAmount = sbtaxAmount + pdtBuyGetPriceList.get(j).getSbtax();
//                            if (pdtLeastPriceList.size() == 0) {
//                                pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
//                            } else {
//                                boolean flag = false;
//                                for (int k = 0; k < pdtLeastPriceList.size(); k++) {
//                                    if (pdtLeastPriceList.get(k).getProductID() == pdtBuyGetPriceList.get(j).getProductID()) {
//                                        BuyGetPriceInfo info = pdtLeastPriceList.get(k);
//                                        info.setQuantity((info.getQuantity() + 1));
//                                        pdtLeastPriceList.remove(k);
//                                        pdtLeastPriceList.add(info);
//                                        flag = true;
//                                        break;
//                                    }
//                                }
//                                if (flag == false) {
//                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
//                                }
//                            }
//                        }
//                        leastProductList.addAll(pdtLeastPriceList);
//                    }
//
//                }
//                setPriceInfo(leastProductList);
//                m_oTicket.setLeastValueDiscount(price);
//
//                calculateLeastValueDiscount(pdtLeastPriceList);
//                m_oTicket.setPromoAction(true);
//   m_oTicket.refreshTxtTakeAwayFields(1);
//        }
//            //  m_oTicket.billValuePromotion(promoRuleIdList,dlSales);
//            printPartialTotals();
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }       
}//GEN-LAST:event_m_jCalculatePromotionActionPerformed

    private void m_jBtnDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDiscountActionPerformed
        if (m_oTicket.getLinesCount() != 0) {
            try {
                logger.info("Start Discount Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                String categoryDiscount = m_App.getProperties().getProperty("machine.categorydiscount");
                String user = m_oTicket.getUser().getName();
                String type = "sales";
                String role = dlCustomers.getRolebyName(user);
                if (categoryDiscount.equals("false")) {
                    //Billwise Discount
                    m_oTicket.setCategoryDiscount(false);

                    if ("Cashier".equalsIgnoreCase(role)) {// for admin
                        boolean login = JDiscountAssign.showMessage(JRetailPanelTakeAway.this, dlCustomers, m_App, m_oTicket);
                        if (login) {
                            m_oTicket.setdPerson(m_oTicket.getUser().getId());
                            JRateEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, m_oTicket, role, type);

                        }
                    } else {
                        m_oTicket.setdPerson(m_oTicket.getUser().getId());
                        //Billwise Discount
                        JRateEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, m_oTicket, role, type);

                    }


                } else {
                    //Line level Discount
                    m_oTicket.setCategoryDiscount(true);
                    if ("Cashier".equalsIgnoreCase(role)) {// for admin
                        boolean login = JDiscountAssign.showMessage(JRetailPanelTakeAway.this, dlCustomers, m_App, m_oTicket);
                        if (login) {
                            m_oTicket.setdPerson(m_oTicket.getUser().getId());
                            //Billwise Discount
                            //JRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, role,type);
                            //Linewise Discount
                            java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
                            Map<String, DiscountInfo> dRateMap = new HashMap();
                            for (DiscountRateinfo dis : list) {
                                dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
                            }
                            discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, m_oTicket, "", list, dRateMap);
                            m_oTicket.setDiscountMap(discountMap);
                            if (discountMap != null) {
                                populateDiscount(discountMap);
                            }

                        }
                    } else {
                        m_oTicket.setdPerson(m_oTicket.getUser().getId());
                        java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
                        Map<String, DiscountInfo> dRateMap = new HashMap();
                        for (DiscountRateinfo dis : list) {
                            dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
                        }
                        discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, m_oTicket, "", list, dRateMap);
                        m_oTicket.setDiscountMap(discountMap);
                        if (discountMap != null) {
                            populateDiscount(discountMap);
                        }

                    }


                }
                populateTaxList();
                m_oTicket.refreshTxtTakeAwayFields(1);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_m_jBtnDiscountActionPerformed

    private void m_jActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jActionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jActionActionPerformed

    public final synchronized void kotDisplay() throws BasicException {
        kotprintIssue = 0;
        logger.info("kotaction value" + kotaction);
        int orderId = 0;
        if (m_oTicket.getOrderId() == 0) {
            if (m_oTicket.getLinesCount() != 0) {
                orderId = dlSales.getNextTicketOrderNumber();
                m_oTicket.setOrderId(orderId);
            }
        }

        final int kotTicket;
        int kotTicketId = 0;
        kotTicketId = dlSales.getNextKotIndex();
        if (kotTicketId == 0) {
            kotTicket = 1;
        } else {
            kotTicket = kotTicketId;
            //  kotTicket = kotTicket+1;
        }
        RetailTicketInfo info = getActiveTicket();
        java.util.List<kotPrintedInfo> kPrintedInfolist = null;
        final java.util.List<RetailTicketLineInfo> panelLines = info.getLines();
           final java.util.List<RetailTicketLineInfo> panelNonKotLines = new ArrayList();

        //Transaction t = new Transaction(m_App.getSession()) { 
        //     @Override
        //    protected Object transact() throws BasicException {
        String sessionId = null;
                  String tableId=null;
                  System.out.println("m_App.getProperties().getProperty("+m_App.getProperties().getProperty("machine.Floor"));
//                 sessionId = dlReceipts.getTakeAwayFloorId(m_App.getProperties().getProperty("machine.Floor"));
                 java.util.List<BeanInfo> takeawayInfo=dlReceipts.getTakeAwayInfoList();
                  tableId=takeawayInfo.get(0).getId();
        m_oTicket.setPlaceid(tableId);
                  sessionId=takeawayInfo.get(0).getName();
        printerInfo = dlReceipts.getPrinterInfo(sessionId);
                  for(int i=0;i<panelLines.size();i++){
                        if(panelLines.get(i).getIsKot()==0){
                            String tbl_orderitemId= UUID.randomUUID().toString();
                            tbl_orderitemId=tbl_orderitemId.replaceAll("-", "");
                panelLines.get(i).setTbl_orderId(tbl_orderitemId);
                panelLines.get(i).setKotid(kotTicket);
                panelLines.get(i).setKotdate(m_oTicket.getDate());
                            System.out.println("m_oTicket.getDate()"+m_oTicket.getDate());
                panelLines.get(i).setKottable(m_oTicket.getPlaceId());
                panelLines.get(i).setKotuser(m_oTicket.getUser().getId());
                panelNonKotLines.add(panelLines.get(i));
                          //  panelLines.get(i).setIsKot(1);
            }

//                        if(panelLines.get(i).getPreparationStatus()!=3){
//                            panelLines.get(i).setPreparationStatus(4);
//                        }
        }
//                  Object[] values = new Object[] {m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket,m_oTicket.getSplitSharedId(),m_oTicket.isPrinted(),m_oTicket.isListModified()};
//                  Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING,Datas.BOOLEAN,Datas.BOOLEAN};
//                  new PreparedSentence(m_App.getSession()
//                , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ? WHERE ID = ? AND SPLITID=? "
//                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 4, 5, 0,3})).exec(values);
//                  logger.info("updating shared tickets in kotaction successfully");  
                  printRetailKotTicket("Printer.Kot", m_oTicket,panelNonKotLines,m_oTicketExt, printerInfo,kotTicket);    
//                  for(int i=0;i<panelLines.size();i++){
//                   if(panelLines.get(i).getPreparationStatus()!=3){
//                        paintKotTicketLine(i,  panelLines.get(i));
//                        }
//                  }
               //   return null;

               // }
            //   };
//            try{
//            t.execute();
//             logger.info("kot transaction happened successfully"); 
//             kotaction=0;
//            } catch (Exception e) {
//                 logger.info("kot transaction failed"+e.getMessage()); 
//                for(int i=0;i<panelNonKotLines.size();i++){
//                     panelNonKotLines.get(i).setIsKot(0);
//                     panelNonKotLines.get(i).setPreparationStatus(0);
//                  }
//                kotaction=1;
//               showMessage(this, "please re try the KOT action");  
//            }
           Runtime.getRuntime().gc();
            System.out.println("current time after kot"+new Date());
    }



private synchronized void printRetailKotTicket(String sresourcename, RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> kot, Object ticketExt,java.util.List<ProductionPrinterInfo> printerInfo,int kotTicket) {
        java.util.List<TicketLineConstructor> allLines = null;
     logger.info("start printing the kot"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        com.openbravo.pos.printer.printer.KotBillPrinter printerKot = new KotBillPrinter();
        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        kotTicketlist = kot;
      for(int j=0;j<printerInfo.size();j++){
          java.util.List<RetailTicketLineInfo> uniqueProductionAreas=new ArrayList<RetailTicketLineInfo>();
          for(int i=0;i<kotTicketlist.size();i++){
           if(printerInfo.get(j).getProductionAreaType().equals(kotTicketlist.get(i).getProductionAreaType())){
                    uniqueProductionAreas.add(kotTicketlist.get(i));
                    kotTicketlist.get(i).setProductionArea(printerInfo.get(j).getProductionArea());
                }
            }
          logger.info("kot print count based on production areas"+uniqueProductionAreas.size());
            //  System.out.println("unique---"+uniqueProductionAreas.get(j).printName());
            System.out.println("uniqueProductionAreas:"+uniqueProductionAreas.size());
             if(uniqueProductionAreas.size()!=0){
                 allLines = getRetailAllLines(ticket,ticketExt,uniqueProductionAreas,kotTicket);    
                try {
                if(storeLocation.equals("BlrIndranagar") || storeLocation.equals("BlrKoramangala") || storeLocation.equals("Chennai") || storeLocation.equals("Hyderabad")){
                        printer.printKot(allLines, printerInfo.get(j).getPath());
                  }else{
                        printerKot.print(allLines, printerInfo.get(j).getPath());
                    }
                    //   kotprintIssue=0;
                    // kotlogger.info("items printed successfully "+"By "+m_oTicket.printUser()+""+" in POS System "+m_App.getProperties().getPosNo()+" Table name "+m_oTicketExt.toString());
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        // kotlogger.info(uniqueProductionAreas.get(i).getProductName());
                        kotlogger.info("KOT Printed Successfully " + "," + "Username: " + m_oTicket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(1);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(4);
                        }

                    }
                } catch (PrinterException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + " The printer action" + ex.getMessage());
                    kotprintIssue = 1;
                    System.out.println("within the catch of printer");
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        // kotlogger.info("items failed to print "+"By "+m_oTicket.printUser()+""+" in POS System "+m_App.getProperties().getPosNo()+" Table name "+m_oTicketExt.toString());
                        //  kotlogger.info("items failed to print :"+uniqueProductionAreas.get(i).getProductName());
                        logger.info("KOT Print Failed  " + "," + "Username: " + m_oTicket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(0);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(0);
                        }
                    }
                    kotaction = 1;
                    showMessage(this, "KOT action not happened! Please retry");
                }
            }

        }

        for (int i = 0; i < ticket.getLinesCount(); i++) {
            paintKotTicketLine(i, ticket.getLine(i));
        }
//        Object[] values = new Object[] {m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket,m_oTicket.getSplitSharedId(),m_oTicket.isPrinted(),m_oTicket.isListModified()};
//        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING,Datas.BOOLEAN,Datas.BOOLEAN};
//        try {
//            new PreparedSentence(m_App.getSession()
//          , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ? WHERE ID = ? AND SPLITID=? "
//          , new SerializerWriteBasicExt(datas, new int[] {1, 2, 4, 5, 0,3})).exec(values);
//        } catch (BasicException ex) {
//            logger.info("Order NO."+m_oTicket.getOrderId()+" exception in  printRetailKotTicket updating shared ticket"+ex.getMessage());
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
        logger.info("kot lines passing to print" + kotTicketlist.size());
        logger.info("end printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }

    private void printTicketGeneric(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
        java.util.List<TicketLineConstructor> allLines = null;
        java.util.List<TicketLineConstructor> startallLines = new ArrayList<TicketLineConstructor>();
        int count = 0;
        com.openbravo.pos.printer.printer.ImageBillPrinter printer = new ImageBillPrinter();
        if (sresourcename.equals("Printer.Bill")) {
            allLines = getAllLines(ticket, ticketext);

        } else if (sresourcename.equals("Printer.NonChargableBill")) {
            allLines = getNonChargeableLines(ticket, ticketext);
        }
        //try {
        //printer.print(allLines);
        //        try {
        int divideLines = allLines.size() / 48;
        int remainder = allLines.size() % 48;
        System.out.println("divideLines---" + divideLines + "--" + remainder);
        int value = 48;
        int k = 0;
        if (divideLines > 0) {
            for (int i = 0; i < divideLines; i++) {
                for (int j = k; j < value; j++) {

                    startallLines.add(new TicketLineConstructor(allLines.get(j).getLine()));
                    System.out.println("allLines.get(j).getLine()--" + allLines.get(j).getLine());
                }
                try {
                    printer.print(startallLines);
                } catch (PrinterException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (allLines.size() > 48) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrintcontinue"), AppLocal.getIntString("message.title"), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (res == JOptionPane.OK_OPTION) {
                        k = value;
                        value = value + 48;
                        startallLines = new ArrayList<TicketLineConstructor>();
                        System.out.println("startallLinest--" + startallLines.size());
                        startallLines.clear();
                    } else {
                        break;
                    }
                }

            }
        }
        System.out.println("value---" + value + k + "--" + remainder);
        if (remainder > 0) {
            startallLines = new ArrayList<TicketLineConstructor>();
            System.out.println("startallLinest--" + startallLines.size());
            startallLines.clear();
            for (int m = k; m < k + remainder; m++) {

                startallLines.add(new TicketLineConstructor(allLines.get(m).getLine()));
                System.out.println("allLines.get(j).getLine()--" + allLines.get(m).getLine());
            }
            try {
                printer.print(startallLines);
            } catch (PrinterException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//                        try {
//                            printer.print(allLines);
//
//                        } catch (PrinterException ex) {
//                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                        }
    }

    private java.util.List<TicketLineConstructor> getRetailAllLines(RetailTicketInfo ticket, Object ticketext, java.util.List<RetailTicketLineInfo> kot, int kotTicket) {

        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        allLines.add(new TicketLineConstructor("          KITCHEN ORDER TICKET"));
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(16 - (ticket.printDate().substring(0, 12).length())) + "Kot No: " + kotTicket));
        allLines.add(new TicketLineConstructor("Time: " + (ticket.printTime()) + getSpaces(16 - (ticket.printTime().length())) + "Table No:" + getSpaces(1) + "Takeaway"));
        allLines.add(new TicketLineConstructor("User Name: " + (m_oTicket.getUser()).getName()));
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        for (RetailTicketLineInfo tLine : kot) {
            String prodName = tLine.printName();
            // String qty = Formats.DoubleValue.formatValue(tLine.printQty());
            String qty = tLine.printMultiply();
            qtySum = qtySum + tLine.getMultiply();
            String instruction = tLine.printInstruction();
            if (prodName.length() > 30) {
                prodName = WordUtils.wrap(prodName, 30);
                String[] prodNameArray = prodName.split("\n");
                for (int i = 0; i < prodNameArray.length - 1; i++) {
                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
                }

                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(33 - prodNameArray[prodNameArray.length - 1].length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            } else {
                allLines.add(new TicketLineConstructor(prodName + getSpaces(33 - prodName.length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            }

        }
        logger.info("sum of kot quantities = " + qtySum);
        return allLines;
    }

    private void m_jEraserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEraserActionPerformed
        //  if(m_oTicket.getParentId()==null)  {
        int kotStatus = 0;
        int kotCount = 0;
        int nonKotCount = 0;
        int linesCount = m_oTicket.getLinesCount();
        for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
            kotStatus = m_oTicket.getLine(i).getIsKot();
            if (kotStatus == 1) {
                kotCount++;
            } else {
                nonKotCount++;
            }
        }
        if (kotCount == linesCount && linesCount != 0) {
            showMessage(this, "All Items are sent to kot");
        } else if (nonKotCount == linesCount) {
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_oTicket.getLines().clear();
            }
            refreshTicket();
            //m_oTicket.getLines().remove(i);
            taxModel.removeAllElements();
        } else {
            int i = 0;
            while (i < m_oTicket.getLinesCount()) {
                if (m_oTicket.getLine(i).getIsKot() == 0) {
                    removeTicketLine(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            m_oTicket.refreshTxtTakeAwayFields(0);
        }
        kotaction = 0;
        //  } 
    }//GEN-LAST:event_m_jEraserActionPerformed

    private void m_jbtnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnPrintBillActionPerformed
//        logger.info("Start Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
//        try {
//            if (m_oTicket.getLinesCount() != 0) {
//                if (kotaction == 1) {
//                    kotDisplay();
//                } else {
//                    Runtime.getRuntime().gc();
//                }
//                serveAllLines();
//                if (kotprintIssue == 0) {
//                    int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrint"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (res == JOptionPane.YES_OPTION) {
//                        try {
//                            logger.info("Start Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                            logger.info("No. of Line Items during Print Bill : " + m_oTicket.getLinesCount());
//                            doPrintValidation();
//                        } catch (BasicException ex) {
//                            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill doPrintValidation" + ex.getMessage());
//                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        String file;
//                        file = "Printer.Bill";
//                        m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
//                        try {
//                            taxeslogic.calculateTakeAwayTaxes(m_oTicket);
//                            // chargeslogic.calculateCharges(m_oTicket);
//                            //     staxeslogic.calculateTakeAwayServiceTaxes(m_oTicket);
//                            //   sbtaxeslogic.calculateTakeAwaySwachBharatTaxes(m_oTicket);
//                        } catch (TaxesException ex) {
//                            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill calculateTaxes" + ex.getMessage());
//                            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                        }
////                            calculateServiceCharge();
//                        if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("ITPL")) {
//                            printTicketGeneric(file, m_oTicket, m_oTicketExt);
//                        } else {
//                            printTicket(file, m_oTicket, m_oTicketExt);
//                        }
//                        logger.info("bill has been printed");
//                        m_oTicket.setPrinted(true);
//                        Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
//                        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
//                        new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ? WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                        logger.info("End Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//                    }
//                }
//            }
//            if (kotprintIssue == 0) {
//                if (roleName.equals("Bartender")) {
//                    logger.info("Role Bartender");
//                    IsSteward = 1;
//                    JRetailTicketsBagRestaurant.setNewTicket();
//                    m_RootApp = (JRootApp) m_App;
//                    m_RootApp.closeAppView();
//                }
//            }
//        } catch (BasicException ex) {
//            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        logger.info("End Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jbtnPrintBillActionPerformed

    private void printBill() {
        logger.info("Start Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

        try {
            if (m_oTicket.getLinesCount() != 0) {
                if (kotaction == 1) {
                    kotDisplay();
                } else {
                    Runtime.getRuntime().gc();
                }
                serveAllLines();
                //   if(kotprintIssue==0){
                //  int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrint"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //    if (res == JOptionPane.YES_OPTION) {
                try {
                    logger.info("Start Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                    logger.info("No. of Line Items during Print Bill : " + m_oTicket.getLinesCount());
                    doPrintValidation();
                } catch (BasicException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill doPrintValidation" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }
//                        String file;
//                        file = "Printer.Bill";
                m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
                try {
                    taxeslogic.calculateTakeAwayTaxes(m_oTicket);
                    // chargeslogic.calculateCharges(m_oTicket);
                    //     staxeslogic.calculateTakeAwayServiceTaxes(m_oTicket);
                    //   sbtaxeslogic.calculateTakeAwaySwachBharatTaxes(m_oTicket);
                } catch (TaxesException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill calculateTaxes" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("End Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }

    private void m_jSettleBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSettleBillActionPerformed
        logger.info("m_jSettleBillActionPerformed");
        logger.info("Start Settle Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sysDate = new Date();
        m_RootApp = (JRootApp) m_App;
        String closeTime = m_App.getProperties().getProperty("machine.closetime");
        String currentDate = format.format(sysDate).toString() + " " + closeTime;
        String closeDayDate = sdf.format(m_RootApp.getActiveDayDateStart());
        Date closeDate = null;
        Date activeDayDate = null;
        try {
            closeDate = sdf.parse(currentDate);
            activeDayDate = sdf.parse(closeDayDate);
        } catch (ParseException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }



        if (closeDate.compareTo(activeDayDate) >= 0) {
            m_RootApp.closeDay();
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.
            String accDate = new SimpleDateFormat("yyyy-MM-dd").format(m_App.getActiveDayDateStart());
            Date dateValue = java.sql.Date.valueOf(accDate);
            m_oTicket.setAccountDate(dateValue);
        }
        if (m_oTicket.getLinesCount() == 0) {
            return;
        }
        printBill();
        JPaymentEditor.showMessage(JRetailPanelTakeAway.this, dlReceipts, m_oTicket, this, "TakeAway");
        //  logger.info("settled successfully");
        logger.info("End Settle Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
}//GEN-LAST:event_m_jSettleBillActionPerformed

    public void splitBill() {
        int splitfunction = 0;
        if (m_oTicket.getLinesCount() > 0) {
            splitfunction = 1;
            if (m_oTicket.getLinesCount() == 1) {
                if (m_oTicket.getLine(0).getMultiply() > 1) {
                    splitfunction = 1;
                } else {
                    splitfunction = 0;
                }
            } else {
                splitfunction = 1;
            }
        }
        if (splitfunction == 1) {
            logger.info("splitfunction =1");
            RetailReceiptSplit splitdialog = RetailReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlSales, dlCustomers, taxeslogic);
            String placeId = m_oTicket.getPlaceId();
            String splitParentId = m_oTicket.getId();
            String splitSharedId = m_oTicket.getSplitSharedId();
            RetailTicketInfo cancelTIcket = m_oTicket;
            RetailTicketInfo ticket1 = m_oTicket.copySplitTicket(m_oTicket.getRate());
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replaceAll("-", "");
            ticket1.setId(uuid);
            ticket1.setOrderId(0);
            ticket1.setRate(m_oTicket.getRate());
            ticket1.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket1.setActiveCash(m_App.getActiveCashIndex());
            ticket1.setActiveDay(m_App.getActiveDayIndex());
            ticket1.setDate(new Date());
            ticket1.setSplitValue("Split");
            ticket1.setPlaceid(placeId);
            ticket1.setTicketId(0);
            ticket1.setParentId(splitParentId);
            ticket1.setSplitSharedId(splitSharedId);
            ticket1.setTicketOpen(false);
            ticket1.setDiscountMap(m_oTicket.getDiscountMap());
            ticket1.setCategoryDiscount(m_oTicket.iscategoryDiscount());
            RetailTicketInfo ticket2 = new RetailTicketInfo(m_oTicket.getRate());
            ticket2.setOrderId(0);
            ticket2.setCustomer(m_oTicket.getCustomer());
            ticket2.setRate(m_oTicket.getRate());
            ticket2.setUser(m_App.getAppUserView().getUser().getUserInfo());
            ticket2.setSplitValue("Split");
            ticket2.setPlaceid(placeId);
            ticket2.setTicketId(0);
            ticket2.setParentId(splitParentId);
            ticket2.setTicketOpen(false);
            ticket2.setDiscountMap(m_oTicket.getDiscountMap());
            ticket2.setCategoryDiscount(m_oTicket.iscategoryDiscount());
            ticket2.setStoreName(storeName);
            if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {
                try {
                    setKotAndServedOnSplit(ticket1);
                    if (splitdialog.window.equals("OK")) {
                        logger.info("splitfunction on  saying ok ");
                        setKotAndServedOnSplit(ticket2);
                    } else {
                        logger.info("splitfunction on saying print ");
                        m_oTicket = ticket2;
                        setKotServedAndPrintOnSplit(m_oTicket);
                    }
                    cancelTIcket.setSplitValue("Split");
                    //cancelTIcket.setParentId(splitParentId);
                    String ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + cancelTIcket.getTicketId();
                    dlSales.saveRetailCancelTicket(cancelTIcket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), "Split Bill", "", m_App.getProperties().getPosNo(), "Y");
                    // dlSales.updateBillNo(cancelTIcket.getTicketId());
                } catch (BasicException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception while splitting the bill" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }
                logger.info("cancelled splitted older bill ");
                JRetailTicketsBagRestaurant.setNewSplitTicket(ticket1, ticket2);

            }
//        else{
//               m_oTicket.setSplitValue("");
//             }
        }

    }
    private void m_jSplitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSplitBtnActionPerformed
//        logger.info("Start Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//        logger.info("Split Bill Action");
//        if (m_oTicket.getLinesCount() != 0) {
//            try {
//                if (kotaction == 1) {
//                    kotDisplay();
//                }
//                if (kotprintIssue == 0) {
//                    splitBill();
//                }
//            } catch (BasicException ex) {
//                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking split bill" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        logger.info("End Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jSplitBtnActionPerformed

    private void m_jBtnKotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnKotActionPerformed
//        logger.info("Start kot Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//        logger.info("No. of Line Items during Kot : " + m_oTicket.getLinesCount());
//        if (m_oTicket.getLinesCount() != 0) {
//            try {
//                //  if(m_oTicket.getParentId()==null)  {
//                System.out.println("printerissue" + kotprintIssue);
//                if (kotaction == 1) {
//                    logger.info("kotaction is 1");
//                    kotDisplay();
//                }
//                
//            } catch (BasicException ex) {
//                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking kot button" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        logger.info("End Kot Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnKotActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //  JRetailCatalog jCatalog = new JRetailCatalog(dlSales);
        // jCatalog.showCatalogPanel(null);
        //  refreshTicket();
        resetSouthComponent("Category");

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonAddonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddonActionPerformed
        System.out.println("m_oTicket.GET KOT DATE()" + m_oTicket.getLine(0).getKotdate());
        try {
            //jLayeredPanel
            String id = null;
            int i = m_ticketlines.getSelectedIndex();
            String prName = m_oTicket.getLine(i).getProductID();
            //    m_oTicket.getLine(i).setIsaddon('Y');
            ProductInfoExt getAddonProduct = null;
            if (menuStatus.equals("false")) {
                getAddonProduct = dlSales.CountAddonProduct(prName);
            } else {
                day = getWeekDay();
                currentMenuList = dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                getAddonProduct = dlSales.CountMenuAddonProduct(prName, menuId);
            }
            if (getAddonProduct == null) {
                showMessage(this, "There is no Addons for this Product");
            } else {
                ProductInfoExt productListDetails = RetailReceiptAddonList.showMessage(JRetailPanelTakeAway.this, dlCustomers, m_App, this, prName, menuId);
                if (productListDetails != null) {
                    if (m_oTicket.getLine(i).getAddonId() == null) {
                        addonId = UUID.randomUUID().toString();
                        addonId = addonId.replaceAll("-", "");
                        m_oTicket.getLine(i).setAddonId(addonId);
                    } else {
                        addonId = m_oTicket.getLine(i).getAddonId();
                    }
                    m_oTicket.getLine(i).setPrimaryAddon(1);
                    id = productListDetails.getID();
                    ProductInfoExt productListval = null;
                    if (menuStatus.equals("false")) {
                        productListval = dlSales.getProductInfoAddon(id);
                    } else {
                        productListval = dlSales.getMenuProductInfoAddon(id, menuId);
                    }
//            addonStatus='Y';
                    incProduct(productListval);
                }
            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking addon" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonAddonActionPerformed

    private void m_jBtnServedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnServedActionPerformed
        if (m_oTicket.getLinesCount() != 0) {
            try {
                int i = m_ticketlines.getSelectedIndex();
                //RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
                System.out.println(m_oTicket.getLine(i).getIsKot() + "getiskot newline");
                if (m_oTicket.getLine(i).getPreparationStatus() != 3) {
                    m_oTicket.getLine(i).setPreparationStatus(3);
                    if (kotaction == 1) {
                        kotDisplay();
                    }
                    if (m_oTicket.getLine(i).getIsKot() == 0) {
                        //this is condition is if printer fails then reverting back the status of serve
                        m_oTicket.getLine(i).setPreparationStatus(0);
                    }
                    m_oTicket.getLine(i).setTbl_orderId(m_oTicket.getLine(i).getTbl_orderId());
                    setServedStatus(1);
                    //   dlReceipts.updateSharedTicket(m_oTicket.getPlaceId(),m_oTicket);
                    paintKotTicketLine(i, m_oTicket.getLine(i));// TODO add your handling code here:
                    setServedStatus(0);
                }
            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking served" + ex.getMessage());
                Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_m_jBtnServedActionPerformed

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
        try {
            stateTransition('\u00a7');
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in m_jbtnScaleActionPerformed" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jBtnCancelBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancelBillActionPerformed
//        logger.info("Start Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
//        int linesCount = m_oTicket.getLinesCount();
//        for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
//            if (m_oTicket.getLine(i).getIsKot() == 1) {
//                JRetailTicketsBagRestaurant.clickCancel();
//                break;
//            }
//        }
//        logger.info("End Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnCancelBillActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetSouthComponent("SubCategory");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void m_jBtnBillOnHoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnBillOnHoldActionPerformed
//        if (!m_oTicket.isPrinted()) {
//            showMessage(this, "Hold bill is allowed after Bill is Printed");
//            logger.info("Hold bill is allowed after Bill is Printed");
//            return;
//        } else if (m_oTicket.isPrinted() & m_oTicket.isListModified()) {
//            showMessage(this, " Bill is Modified after previous print. So please print it again");
//            logger.info(" Bill is Modified after previous print. So please print it again");
//            return;
//        } else {
//            int res = JOptionPane.showConfirmDialog(this, "Do you want to hold the Bill", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (res == JOptionPane.YES_OPTION) {
//                JRetailTicketsBagRestaurant.setHoldTicket(m_oTicket);
//            }
//        }

    }//GEN-LAST:event_m_jBtnBillOnHoldActionPerformed

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
            //   Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (itemCount != 0) {

            try {
                productListValue = (ArrayList<ProductInfoExt>) dlSales.getProductInfoByItemCode(itemCode);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            m_jCboItemName.setSelectedItem(productListValue.get(0).getName());
            incProductByItemDetails(productListValue.get(0).getID());
            m_jTxtItemCode.setText("");
            itemName.setText("");
        } else {
            showMessage(this, "Please enter the valid item code");
            m_jTxtItemCode.setFocusable(true);
        }

        //action4Performed = false;// TODO add your handling code here:// TODO add your handling code here:
}//GEN-LAST:event_m_jTxtItemCodeActionPerformed

    private void m_jCboItemNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_m_jCboItemNameItemStateChanged
        if (evt.getStateChange() == evt.SELECTED) {
        }// TODO add your handling code here:
}//GEN-LAST:event_m_jCboItemNameItemStateChanged

    private void m_jCboItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCboItemNameActionPerformed

        if (action4Performed == true || action5Performed == true) {
            return;
        }
        action5Performed = true;
        String id = null;
        int index = m_jCboItemName.getSelectedIndex();//-1;

        if (index != -1) {
            if (typeId == 2) {
                try {
                    productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductName(itemName.getText());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                id = productListDetails.get(0).getID();
            } else {
                loadItemList();
                id = productList.get(index).getID();
            }
            try {
                productList = (ArrayList<ProductInfoExt>) dlSales.getProductInfoById(id);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            // m_jTxtItemCode.setText(productList.get(0).getItemCode());
            m_jCboItemName.setSelectedItem(productList.get(0).getName());
            pdtId = productList.get(0).getID();
        } else {
            itemName.setText(null);
            pdtId = "";
        }

        action5Performed = false;// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_m_jCboItemNameActionPerformed

    private void setKotAndServedOnSplit(RetailTicketInfo splitTicket) {
        int orderId = 0;
        try {
            orderId = dlSales.getNextTicketOrderNumber();
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        splitTicket.setOrderId(orderId);

        int numlines = splitTicket.getLinesCount();
        int servedStatus = 0;
        for (int i = 0; i < numlines; i++) {
            splitTicket.getLine(i).setIsKot(1);
            String tbl_orderitemId = UUID.randomUUID().toString();
            tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
            if (splitTicket.getLine(i).getPreparationStatus() != 3) {
                splitTicket.getLine(i).setPreparationStatus(4);
            } else {
                servedStatus = 1;
            }
            splitTicket.getLine(i).setTbl_orderId(tbl_orderitemId);
        }


    }

    private final synchronized void setKotServedAndPrintOnSplit(final RetailTicketInfo splitTicket) throws BasicException {

        Transaction t = new Transaction(m_App.getSession()) {
            @Override
            protected Object transact() throws BasicException {
                int orderId = 0;
                orderId = dlSales.getNextTicketOrderNumber();
                splitTicket.setOrderId(orderId);
                //doPrintValidationSplitTicket(splitTicket);
                doPrintValidation();
                String file;
                file = "Printer.TakeAway";
                try {
                    taxeslogic.calculateTakeAwayTaxes(m_oTicket);
                    //   chargeslogic.calculateCharges(m_oTicket);
                    //  staxeslogic.calculateTakeAwayServiceTaxes(m_oTicket);
                    // taxeslogic.calculateTaxes(m_oTicket);
                } catch (TaxesException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit calculateTaxes" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
                }
                // calculateServiceChargeSplitTicket(splitTicket);
//                              calculateServiceCharge();
                printTicket(file, m_oTicket, m_oTicketExt);
                splitTicket.setPrinted(true);
                final java.util.List<RetailTicketLineInfo> panelLines = splitTicket.getLines();
                for (final RetailTicketLineInfo l : panelLines) {
                    l.setIsKot(1);
                    l.setPreparationStatus(3);
                    String tbl_orderitemId = UUID.randomUUID().toString();
                    tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
                    l.setTbl_orderId(tbl_orderitemId);
                    setServedStatus(1);
                }
                for (int i = 0; i < panelLines.size(); i++) {
                    paintKotTicketLine(i, splitTicket.getLine(i));
                    setServedStatus(0);
                }
                return null;
            }
        };
        t.execute();
    }

    private void printKotTicket(String sresourcename, RetailTicketInfo ticket, kotInfo kot, Object ticketExt, String printerName) {


        String sresource = dlSystem.getResourceAsXML(sresourcename);

        kot.setkotName("Kitchen Order Ticket");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTakeAway.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                script.put("kot", kot);
                script.put("place", ticketExt);
                String args[] = null;

                m_TTP.printTicket(script.eval(sresource).toString());
//                aconfig.load();
//                aconfig.setProperty("machine.printer.2", null);
//                try {
//                    aconfig.save();
//                } catch (IOException ex) {
//                    Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                    System.out.println("aconfig--1111"+aconfig.getProperty("machine.printer.2"));
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTakeAway.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTakeAway.this);
            }
        }
    }

    public java.util.ArrayList<BuyGetPriceInfo> getPriceInfo() {
        return pdtBuyGetPriceList;
    }

    public void setPriceInfo(java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList) {
        this.pdtBuyGetPriceList = pdtBuyGetPriceList;
    }

    private synchronized void setBillNo() throws BasicException {    //acquire new bill no for bill print mutual exclusively
        int billNo = dlSales.getNextTicketIndex();  //read from db counter
        m_oTicket.setTicketId(billNo);  //set this billno in pojo & retain it during switchings of tables
    }

    private synchronized void setLoyalityCode() throws BasicException {    //get new LOYALITY CODE for bill print 
        String loyalcode = dlSales.getLoyalityCode();  //read from db 
        m_oTicket.setLoyalcode(loyalcode);  //set this LOYALITY CODE in pojo 
    }

    private synchronized void doPrintValidation() throws BasicException {  //determine suitable bill No for print  
        setLoyalityCode(); //get new LOYALITY CODE
        if (!m_oTicket.isPrinted()) {//CASE 1: determine is it first print for this table
            //first print confirmed
            setBillNo(); //get new bill no from database 
            dlSales.updateLoyalityCode(m_oTicket.getLoyalcode(), m_oTicket.getTicketId());
            m_oTicket.setPrintedLineItems(m_oTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now 
            logger.info("the first print of bill");
            closeInvoiceTicket(m_oTicket); //insert into invoice && invoicelines
        } else { //CASE 2: it is already printed once for sure.
            logger.info("Bill is already printed once for sure");
            dlSales.disableInvoiceTickets(m_oTicket.getTicketId()); //update existing record with inactive status
            m_oTicket.setBillParent(m_oTicket.getTicketId());//set current bill no as billParent
            String oldBillId = m_oTicket.getId();
            String newBillId = UUID.randomUUID().toString();
            newBillId = newBillId.replaceAll("-", "");
            m_oTicket.setId(newBillId);
            setBillNo(); //assign new bill no to this pojo
            dlSales.updateLoyalityCode(m_oTicket.getLoyalcode(), m_oTicket.getTicketId());
            m_oTicket.setPrintedLineItems(m_oTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now
            closeInvoiceTicket(m_oTicket); //insert into invoice && invoice lines
            m_oTicket.setId(oldBillId);
            m_oTicket.setModified(true);    //yes this bill is modified so don't print duplicate copy in receipt
        }
    }

//private synchronized void doPrintValidationSplitTicket(RetailTicketInfo splitTicket) throws BasicException{  //determine suitable bill No for print     
//
//        if(!splitTicket.isPrinted()){//CASE 1: determine is it first print for this table
//            //first print confirmed
//            setBillNo(); //get new bill no from database 
//           splitTicket.setPrintedLineItems(splitTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now           
//           closeInvoiceTicket(splitTicket); //insert into invoice && invoicelines
//        }
//        else{ //CASE 2: it is already printed once for sure.
//                dlSales.disableInvoiceTickets(splitTicket.getTicketId()); //update existing record with inactive status
//                splitTicket.setBillParent(splitTicket.getTicketId());//set current bill no as billParent
//                String oldBillId = splitTicket.getId();
//                String newBillId = UUID.randomUUID().toString();
//                newBillId = newBillId.replaceAll("-", "");
//                splitTicket.setId(newBillId);
//                setBillNo(); //assign new bill no to this pojo
//                splitTicket.setPrintedLineItems(splitTicket.getUniqueLines());   //fill duplicate-buffer with items just going to print now
//                closeInvoiceTicket(splitTicket); //insert into invoice && invoice lines
//                splitTicket.setId(oldBillId);
//                splitTicket.setModified(true);    //yes this bill is modified so don't print duplicate copy in receipt
//        }
//    }
    private void closeInvoiceTicket(RetailTicketInfo ticket) throws BasicException {

        try {
//               chargeslogic.calculateCharges(ticket);
            //  staxeslogic.calculateTakeAwayServiceTaxes(ticket);
            taxeslogic.calculateTakeAwayTaxes(ticket);
            // sbtaxeslogic.calculateTakeAwaySwachBharatTaxes(m_oTicket);
        } catch (TaxesException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in closeInvoiceTicket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTakeAwayTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }



        double creditAmt;
        creditAmt = 0;

        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
        ticket.setActiveCash(m_App.getActiveCashIndex());
        ticket.setDate(new Date()); // Le pongo la fecha de cobro


        //String[] ticketDocNoValue;
        String ticketDocNo = null;
        Integer ticketDocNoInt = null;
        String ticketDocument = null;

        if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
        }

        ticket.setDocumentNo(ticketDocument);
        String chequeNos = "";//m_jtxtChequeNo.getText();
        String deliveryBoy = "";
        double tipsAmt = 0;
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;
        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        String isCredit;
        double advanceissued;
        deliveryBoy = "";//deliveryBoyLines.get(m_jDeliveryBoy.getSelectedIndex() - 1).getId();
        if (creditAmt > 0) {
            isCredit = "Y";
        } else {
            isCredit = "N";
        }

        advanceissued = 0;

        String file;

        if (ticket.getLinesCount() == 0) {
            showMessage(this, "Please select the products");
        } else {
            dlSales.saveRetailInvoiceTicket(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmt, "Y", isCredit, isPaidStatus, tipsAmt, orderTaking, "N");
            logger.info("insertion to invoice table");
        }
    }

    public final synchronized void serveAllLines() throws BasicException {

        Transaction t = new Transaction(m_App.getSession()) {
            int isallServed = 0;

            @Override
            protected Object transact() throws BasicException {
                final java.util.List<RetailTicketLineInfo> panelLines = m_oTicket.getLines();
                logger.info("serveAllLines method items are " + panelLines.size());
                for (final RetailTicketLineInfo l : panelLines) {
                    if (l.getPreparationStatus() != 3 && l.getPreparationStatus() != 0) {
                        isallServed = 1;
                        l.setPreparationStatus(3);
                        l.setIsKot(1);
                        l.setTbl_orderId(l.getTbl_orderId());
                        setServedStatus(1);
                    }

                }
                for (int i = 0; i < panelLines.size(); i++) {
                    paintKotTicketLine(i, panelLines.get(i));
                }
                setServedStatus(0);
                return null;
            }
        };
        t.execute();
        logger.info("updated sharedticket");
    }

    private class RefreshTicket implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //  loadTicket();
        }
    }

    private void loadTicket() {
//        try {
//            System.out.println("TIMER STARTED "+tableId);
//            if(m_oTicket.getLinesCount()!=0){
//            // m_oTicket.getLines().clear();
//            // m_oTicket= dlReceipts.getRetailSharedTicket(tableId);
//             refreshTicket();
//            }
//            } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void enablePosActions() {

        try {
            posActions = (ArrayList<PosActionsInfo>) dlSales.getPosActions(m_App.getAppUserView().getUser().getRole());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTakeAway.class.getName()).log(Level.SEVERE, null, ex);
        }
//        if (posActions.size() != 0) {
//            if (posActions.get(0).getDiscountAccess().equals("Y")) {
//                m_jBtnDiscount.setVisible(true);
//            } else {
//                m_jBtnDiscount.setVisible(false);
//            }
//            if (posActions.get(0).getSplitAccess().equals("Y")) {
//                m_jSplitBtn.setVisible(true);
//            } else {
//                m_jSplitBtn.setVisible(false);
//            }
//            if (posActions.get(0).getCancelAccess().equals("Y")) {
//                m_jBtnCancelBill.setVisible(true);
//            } else {
//                m_jBtnCancelBill.setVisible(false);
//            }
//            if (posActions.get(0).getSettleAccess().equals("Y")) {
//                m_jSettleBill.setVisible(true);
//            } else {
//                m_jSettleBill.setVisible(false);
//            }
//            if (posActions.get(0).getPrintAccess().equals("Y")) {
//                m_jbtnPrintBill.setVisible(true);
//            } else {
//                m_jbtnPrintBill.setVisible(false);
//            }
//            if (posActions.get(0).getBillOnHoldAccess().equals("Y")) {
//                m_jBtnBillOnHold.setVisible(true);
//            } else {
//                m_jBtnBillOnHold.setVisible(false);
//            }
//        } else {
            m_jBtnDiscount.setVisible(true);
            m_jbtnPrintBill.setVisible(false);
            m_jSettleBill.setVisible(true);
            m_jBtnCancelBill.setVisible(false);
            m_jSplitBtn.setVisible(false);
            m_jBtnBillOnHold.setVisible(false);
      //  }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel catcontainer;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonAddon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel jLblPrinterStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jTaxPanel;
    private javax.swing.JButton m_jAction;
    private javax.swing.JButton m_jBtnBillOnHold;
    private javax.swing.JButton m_jBtnCancelBill;
    private javax.swing.JButton m_jBtnDiscount;
    private javax.swing.JButton m_jBtnServed;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JButton m_jCalculatePromotion;
    private javax.swing.JComboBox m_jCboItemName;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JButton m_jDelete;
    public static javax.swing.JLabel m_jDiscount1;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEraser;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JButton m_jKot;
    private javax.swing.JLabel m_jLblBillNo;
    private javax.swing.JLabel m_jLblCurrentDate;
    private javax.swing.JLabel m_jLblItemCode;
    private javax.swing.JLabel m_jLblItemName;
    private javax.swing.JLabel m_jLblTime;
    private javax.swing.JLabel m_jLblTotalEuros4;
    private javax.swing.JLabel m_jLblTotalEuros5;
    private javax.swing.JLabel m_jLblTotalEuros6;
    private javax.swing.JLabel m_jLblUserInfo;
    private javax.swing.JButton m_jLogout;
    private javax.swing.JButton m_jMinus;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JPanel m_jPanelScripts;
    private javax.swing.JButton m_jPlus;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JPanel m_jProducts;
    public static javax.swing.JLabel m_jPromoDiscount;
    public static javax.swing.JLabel m_jServiceTax;
    public static javax.swing.JLabel m_jServiceTaxLbl;
    private javax.swing.JButton m_jSettleBill;
    private javax.swing.JButton m_jSplitBtn;
    public static javax.swing.JLabel m_jSubtotalEuros1;
    private javax.swing.JLabel m_jSwachBharat;
    private javax.swing.JLabel m_jSwachBharatLbl;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JList m_jTaxList;
    public static javax.swing.JLabel m_jTaxesEuros1;
    public static javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JLabel m_jTxtChange;
    private javax.swing.JTextField m_jTxtItemCode;
    private javax.swing.JLabel m_jTxtTotalPaid;
    private javax.swing.JLabel m_jUser;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnPrintBill;
    private javax.swing.JButton m_jbtnScale;
    // End of variables declaration//GEN-END:variables
}
