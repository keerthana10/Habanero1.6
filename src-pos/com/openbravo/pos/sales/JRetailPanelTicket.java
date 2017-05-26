//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software FoundatFion, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WfARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//ivate
//    You should have receivecked a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.sales;

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
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.catalog.JRetailCatalog;
import com.openbravo.pos.catalog.JRetailCatalogTab;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
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
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentInterface;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.payment.PaymentInfoCard;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoChequeDetails;
import com.openbravo.pos.payment.PaymentInfoComp;
import com.openbravo.pos.payment.PaymentInfoFoodCoupon;
import com.openbravo.pos.payment.PaymentInfoList;
import com.openbravo.pos.payment.PaymentInfoOtherPayment;
import com.openbravo.pos.payment.PaymentInfoStaff;
import com.openbravo.pos.payment.PaymentInfoVcloud;
import com.openbravo.pos.payment.PaymentInfoVoucherDetails;
import com.openbravo.pos.printer.printer.ImageBillPrinter;
import com.openbravo.pos.printer.printer.KotBillPrinter;
import com.openbravo.pos.printer.printer.KotImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.sales.restaurant.Floor;
import com.openbravo.pos.sales.restaurant.JRetailBufferWindow;
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurant;
import com.openbravo.pos.sales.restaurant.Place;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.NameTaxMapInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.ThumbNailBuilderPopularItems;
import com.openbravo.util.date.DateFormats;
import com.sysfore.pos.homedelivery.DeliveryBoyInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import com.sysfore.pos.panels.PosActionsInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javax.print.PrintService;
import javax.swing.border.Border;
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
public abstract class JRetailPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, RetailTicketsEditor {

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
    private SentenceList sentSBtax;
    private ListKeyed sertaxcollection;
    private ListKeyed sbtaxcollection;
    java.util.List<ServiceChargeInfo> chargelist = null;
    java.util.List<TaxInfo> sertaxlist = null;
    private SentenceList senttaxcategories;
    java.util.List<TaxInfo> sbTaxlist = null;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    private static RetailTaxesLogic taxeslogic;
    private static RetailServiceChargesLogic chargeslogic;
    private static RetailSTaxesLogic staxeslogic;
    private static RetailSBTaxesLogic sbtaxeslogic;
    private String editSaleBillId;
    protected JRetailPanelButtons m_jbtnconfig;
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
    PaymentInfoOtherPayment amex = null;
    PaymentInfoVcloud vCloud = null;
    PaymentInfoOtherPayment otherPayment = null;
    PaymentInfoStaff staff = null;
    PaymentInfoComp comp = null;
    PaymentInfoChequeDetails chequetransaction = null;
    PaymentInfoVoucherDetails voucher = null;
    PaymentInfoFoodCoupon foodCoupon = null;
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
    protected kotInfo k_oInfo;
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
    private java.util.List<ProductionPrinterInfo> printerInfo;
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
    private RefreshPromotion autoRefreshPromotion = new RefreshPromotion();
    private Timer promoTimer = new Timer(1200, autoRefreshPromotion);
    private java.util.List<CampaignIdInfo> promotionList = null;
    boolean leastValuePromo = false;
    boolean buyGetPromo = false;
    private static DateFormat m_dateformat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat m_dateformattime = new SimpleDateFormat("HH:mm:ss");
    Date dbUpdatedDate = null;

    /**
     * Creates new form JTicketView
     */
    public JRetailPanelTicket() {
        initComponents();
        tnbbutton = new ThumbNailBuilderPopularItems(110, 57, "com/openbravo/images/bluetoit.png");
        TextListener txtL;
        itemName = (JTextField) m_jCboItemName.getEditor().getEditorComponent();
        m_jTxtItemCode.setFocusable(true);
        txtL = new TextListener();
        itemName.addFocusListener(txtL);
        Action doMorething = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                m_jKeyFactory.setFocusable(true);
                m_jKeyFactory.setText(null);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        m_jKeyFactory.requestFocus();
                    }
                });

            }
        };


        // Add Action listener for item name drop down field
        itemName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!itemName.getText().equals("") || !itemName.getText().equals(null)) {

                    incProductByItemDetails(pdtId);
                    ArrayList<String> itemCode = new ArrayList<String>();
                    ArrayList<String> itemName1 = new ArrayList<String>();

                    vItemName.removeAllElements();
                    try {
                        productListDetails = (ArrayList<ProductInfoExt>) dlSales.getProductDetails();
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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

    //Method is called when click on cash payment
    public void cashPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter cashPayment");
        m_aPaymentInfo = new PaymentInfoList();
        //Adding the cash payment details to PaymentInfoList for saving the payment details
        cash = new PaymentInfoCash(ticket.getTotal(), ticket.getTotal(), 0);
        if (cash != null) {
            m_aPaymentInfo.add(cash);
        }
        ticket.setPrintStatus(print);
        try {
            logger.info("Before close ticket button");
            //Method is for closing the ticket
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("After close ticket button");
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cashPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //Method is called when click on vCloud payment

    public void vCloudPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter vCloudPayment");
        m_aPaymentInfo = new PaymentInfoList();
        //Adding the vCloud payment details to PaymentInfoList for saving the payment details
        vCloud = new PaymentInfoVcloud(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (vCloud != null) {
            m_aPaymentInfo.add(vCloud);
        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            //Method is for closing the ticket
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method is called when click on other payment

    public void otherPayment(int print, RetailTicketInfo ticket, String paymentType, String otherPaymentMode, String description) {
        logger.info("Enter otherPayment");
        m_aPaymentInfo = new PaymentInfoList();
        //Adding the other payment details to PaymentInfoList for saving the payment details
        otherPayment = new PaymentInfoOtherPayment(m_oTicket.getTotal(), m_oTicket.getTotal(), paymentType, otherPaymentMode, description);
        if (otherPayment != null) {
            m_aPaymentInfo.add(otherPayment);
        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            //Method is for closing the ticket
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method is called when click on card payment

    public void cardPayment(int print, RetailTicketInfo ticket, String paymentMode) {
        logger.info("Enter cardPayment");
        m_aPaymentInfo = new PaymentInfoList();
        //Adding the card payment details to PaymentInfoList for saving the payment details
        card = new PaymentInfoCard(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (card != null) {
            m_aPaymentInfo.add(card);
        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("cardPayment Before close ticket button action");
            //Method is for closing the ticket
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("cardPayment After close ticket button action");
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in cardPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method is called when click on cheque payment

    public void chequePayment(int print, RetailTicketInfo ticket, String chequeNo) {
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            m_aPaymentInfo = new PaymentInfoList();
            //Adding the cheque payment details to PaymentInfoList for saving the payment details
            chequetransaction = new PaymentInfoChequeDetails(ticket.getTotal(), ticket.getTotal(), chequeNo);
            if (chequetransaction != null) {
                m_aPaymentInfo.add(chequetransaction);

            }
            ticket.setPrintStatus(print);
            try {
                logger.info("chequePayment Before close ticket button action");
                //Method is for closing the ticket
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("chequePayment after close ticket button action");
            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in chequePayment closeTicketWithButton" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //Method is called when click on staff payment

    public void staffPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter staffPayment method");
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            m_aPaymentInfo = new PaymentInfoList();
            //Adding the staff payment details to PaymentInfoList for saving the payment details
            staff = new PaymentInfoStaff(m_oTicket.getTotal(), m_oTicket.getTotal());
            if (staff != null) {
                m_aPaymentInfo.add(staff);

            }
            m_oTicket.setPrintStatus(print);
            try {
                logger.info("staffPayment Before close ticket button action");
                //Method is for closing the ticket
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("staffPayment after close ticket button action");
            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in staffPayment closeTicketWithButton" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //Method is called when click on nonchargeable payment

    public void complimentaryPayment(int print, RetailTicketInfo ticket) {
        logger.info("Enter complimentaryPayment method");
        m_aPaymentInfo = new PaymentInfoList();
        //Adding the non chargable payment details to PaymentInfoList for saving the payment details
        comp = new PaymentInfoComp(m_oTicket.getTotal(), m_oTicket.getTotal());
        if (comp != null) {
            m_aPaymentInfo.add(comp);
        }
        m_oTicket.setPrintStatus(print);
        try {
            logger.info("complimentaryPayment Before close ticket button action");
            //Method is for closing the ticket
            closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
            logger.info("complimentaryPayment after close ticket button action");
        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in complimentaryPayment closeTicketWithButton" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method is called when click on voucher payment

    public void voucherPayment(int print, RetailTicketInfo ticket, String voucherNo) {
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            logger.info("Enter voucherPayment method");
            m_aPaymentInfo = new PaymentInfoList();
            //Adding the voucher payment details to PaymentInfoList for saving the payment details
            voucher = new PaymentInfoVoucherDetails(ticket.getTotal(), ticket.getTotal(), voucherNo);
            if (voucher != null) {
                m_aPaymentInfo.add(voucher);

            }
            ticket.setPrintStatus(print);
            try {
                logger.info("voucherPayment Before close ticket button action");
                //Method is for closing the ticket
                closeTicketWithButton(ticket, m_oTicketExt, m_aPaymentInfo);
                logger.info("voucherPayment after close ticket button action");
            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in voucherPayment closeTicketWithButton" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void customerFocus() {

        m_jTxtItemCode.setFocusable(true);
        itemName.setFocusable(true);

    }

    public void setEditSale(String editSale) {
        this.editSale = editSale;
    }

    public String getEditSale() {
        return editSale;
    }

    //Method is called for populating discounts in drop down and product category when click on bill discount button
    private void populateDiscount(Map<String, DiscountInfo> discountMap) {
        if (discountMap != null) {
            Set<String> keys = discountMap.keySet();
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                if (m_oTicket.getLine(i).getActualPrice() != 0 && m_oTicket.getLine(i).getPrice() != 0) {
                    m_oTicket.getLine(i).setDiscountrate("");
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
                    }
                }
            }
        }
    }
    //Method is used for checking the whether the items have mandatory addon while adding the items

    private void checkMandatoryAddon(ProductInfoExt oProduct, String addonId) {
        java.util.List<ProductInfoExt> mandatoryProduct = null;
        if (menuStatus.equals("false")) {
            try {
                mandatoryProduct = dlSales.getMandatoryAddonProducts(oProduct.getID());
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                day = getWeekDay();
                currentMenuList = dlSales.getMenuId(day);
                if (currentMenuList.size() != 0) {
                    menuId = currentMenuList.get(0).getId();
                }
                mandatoryProduct = dlSales.getMenuMandatoryAddonProducts(oProduct.getID(), menuId);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!mandatoryProduct.isEmpty()) {
            for (ProductInfoExt mandatory : mandatoryProduct) {
                TaxInfo tax = taxeslogic.getTaxInfo(mandatory.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
                addTicketLine(new RetailTicketLineInfo(mandatory, mandatory.getMultiply(), mandatory.getMrp(), promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, mandatory.getName(), mandatory.getProductType(), mandatory.getProductionAreaType(), (java.util.Properties) (mandatory.getProperties().clone()), addonId, 0, null, 0, null, null, null, null, null, mandatory.getParentCatId(), mandatory.getPreparationTime(), null, null, null, null, null, 1, null, false));
            }
        }
    }

    //Method is used for reloading the promotion
    private class RefreshPromotion implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            loadPromotion();
        }
    }

    private void loadPromotion() {
        Date sysdate = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentTime = time.format(sysdate);

        //next promotion start and end time testing
        for (CampaignIdInfo p : promotionList) {

            if (p.getStartTime().equals(currentTime) || p.getEndTime().equals(currentTime)) {
                setPromotionData();

            }
        }
    }

    //Method is used for setting the valid promotions
    private String setPromotionData() {
        System.out.println("setpromotion data====");
        String day = getWeekDay();
        String promotionName = "";
        logger.info("after m_ticketsbag activate method");
        java.util.ArrayList<String> campaignId = new ArrayList<String>();
        promoRuleIdList = null;
        try {
            campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
            System.out.println("setpromotion data====" + campaignIdList.size());
            if (campaignIdList.size() != 0) {
            }
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        promoDetails = new PromoRuleInfo();
        if (campaignIdList.size() != 0) {
            for (int i = 0; i < campaignIdList.size(); i++) {
                campaignId.add("'" + campaignIdList.get(i).getcampaignId() + "'");
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
            if (campaignIdList != null) {
                try {
                    promoRuleIdList = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);
                    int leastPromotionCount = dlSales.getLeastPromo(Id);
                    int buyGetPromotionCount = dlSales.getBuyGetPromo(Id);
                    if (leastPromotionCount != 0) {
                        promotionName = "LeastValue";
                        m_jCalculatePromotion.setEnabled(true);
                    } else if (buyGetPromotionCount != 0) {
                        promotionName = "BuyGet";
                        m_jCalculatePromotion.setEnabled(true);
                    } else {
                        m_jCalculatePromotion.setEnabled(false);
                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            m_jCalculatePromotion.setEnabled(false);
        }
        return promotionName;
    }

    class TextListener implements FocusListener {

        public void focusLost(FocusEvent e) {
            if (e.getSource() == cusName) {
                cusName.setFocusable(true);
            } else if (e.getSource() == cusPhoneNo) {
            }

        } // close focusLost()

        public void focusGained(FocusEvent e) {
            final String type;
            if (e.getSource() == cusName) {
                type = "c";
                cusName.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                String text = cusName.getText();
                                if (text.length() == 0) {
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
                                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
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
                                    setModel(new DefaultComboBoxModel(vCusPhoneNo), "", type);
                                } else {
                                    DefaultComboBoxModel m = getContactNoSuggestedModel(vCusPhoneNo, text);
                                    if (m.getSize() == 0 || hide_flag) {
                                        hide_flag = false;
                                    } else {
                                        setModel(m, text, type);
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
                                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
//            m_jbtnScale.setVisible(false);
        }
        if (m_App.getProperties().getProperty("machine.ticketsbag").equals("restaurant")) {
            m_jbtnPrintBill.setVisible(true);
        } else {
            m_jbtnPrintBill.setVisible(false);
        }
        customerFocus();
        menuStatus = m_App.getProperties().getProperty("machine.menustatus");
        m_jPor.setVisible(false);
        m_ticketsbag = getJTicketsBag();
        m_oTicket.setCancelTicket(false);
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);
        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JRetailTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);

        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        // The configurable buttons
        m_jbtnconfig = new JRetailPanelButtons("Ticket.Buttons", this);
        m_jButtonsExt.add(m_jbtnconfig);

        // The panel product lines
        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);
        catcontainer.setVisible(true);
        m_jCalculatePromotion.setVisible(true);
        m_jCalculatePromotion.setEnabled(false);
        m_jBtnServed.setVisible(false);
        // The model tax
        senttax = dlSales.getRetailTaxList();
        sentcharge = dlSales.getRetailServiceChargeList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        sentsertax = dlSales.getRetailServiceTaxList();
        sentSBtax = dlSales.getRetailSwachBharatTaxList();
        taxcategoriesmodel = new ComboBoxValModel();

        // ponemos a cero el estado
        stateToZero();

        //creating log file for kot
        String logpath = m_App.getProperties().getProperty("machine.kotlogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh1 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        printlogger.addHandler(fh2);
        fh2.setFormatter(formatter);

        //creating log file for settle bill
        logpath = m_App.getProperties().getProperty("machine.settlelogfile");
        logpath = logpath + getLogDate() + "-POS" + m_App.getProperties().getPosNo() + ".txt";
        try {
            fh3 = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        settlelogger.addHandler(fh3);
        fh3.setFormatter(formatter);

    }
    //Method is used for loading the customer and items details in billing screen

    public void custItemLoad() {
        loadCusDetails();
        loadItemDetails();
        // Initialise
        m_oTicket = null;
        m_oTicketExt = null;

        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusId = new ArrayList<String>();
        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in custItemLoad" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (ProductInfoExt product : productList) {
            itemCode.add(product.getItemCode());
            itemName.add(product.getName());
        }

        String[] productName = itemName.toArray(new String[itemName.size()]);

        for (int i = 0; i < itemName.size(); i++) {
            vItemName.addElement(productName[i]);
        }
    }

    private void setModel(DefaultComboBoxModel mdl, String str, String type) {
        if (type == "c") {
//                m_jCboCustName.setModel(mdl);
            cusName.setText(str);
        } else if (type == "n") {
            //   m_jCboContactNo.setModel(mdl);
            cusPhoneNo.setText(str);
        } else {
            m_jCboItemName.setModel(mdl);
            itemName.setText(str);
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

    public void paymentDetail(double cashAmount, double cardAmount) {
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            m_aPaymentInfo = new PaymentInfoList();
            totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount;
            double change = totalAmount - m_oTicket.getTotal();
            cash = new PaymentInfoCash(m_oTicket.getTotal(), cashAmount, change);
            if (cash != null) {
                m_aPaymentInfo.add(cash);

            }
            card = new PaymentInfoCard(m_oTicket.getTotal(), cardAmount);
            if (card != null) {
                m_aPaymentInfo.add(card);


            }
            try {
                closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo);

            } catch (BasicException ex) {
                logger.info("Order No. " + m_oTicket.getOrderId() + "exception in paymentDetail closeTicket" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Method is called when click on split payment
    }

    public void paymentDetail(double cashAmount, double cardAmount, double voucherAmount, double vCloudAmount, String cardType, String decription) {
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            m_aPaymentInfo = new PaymentInfoList();


            totalAmount = cashAmount + chequeAmount + cardAmount + foodCouponAmount + voucherAmount + creditAmount + vCloudAmount;
            double change = totalAmount - m_oTicket.getTotal();
            cash = new PaymentInfoCash(m_oTicket.getTotal(), cashAmount, change);
            if (cash != null) {
                m_aPaymentInfo.add(cash);

            }
            otherPayment = new PaymentInfoOtherPayment(m_oTicket.getTotal(), cardAmount, "Card", cardType, decription);
            if (otherPayment != null) {
                m_aPaymentInfo.add(otherPayment);
            }
            voucher = new PaymentInfoVoucherDetails(m_oTicket.getTotal(), voucherAmount);
            if (voucher != null) {
                m_aPaymentInfo.add(voucher);
            }
            vCloud = new PaymentInfoVcloud(m_oTicket.getTotal(), vCloudAmount);
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
    }

    private void loadCusDetails() {
        ArrayList<String> cusNames = new ArrayList<String>();
        ArrayList<String> cusPhoneNo = new ArrayList<String>();
        ArrayList<String> cusName1 = new ArrayList<String>();


        try {
            customerList = (ArrayList<CustomerListInfo>) dlSales.getCustomerList();
        } catch (BasicException ex) {
            logger.info("exception in loadCusDetails" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
        custItemLoad();
        showProductPanel();
        enablePosActions();
        m_jbtnScale.setVisible(false);
        jLblPrinterStatus.setText("");
        String servedStatus = m_App.getProperties().getProperty("machine.servedstatus");
//        if (servedStatus.equals("true")) {
//            m_jBtnServed.setVisible(true);
//        } else {
//            m_jBtnServed.setVisible(false);
//        }
        //Added new logic to change tax calculations based on store name
        storeName = m_App.getProperties().getProperty("machine.StoreName");
        populateDeliveryBoy();
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(m_App);
        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // It initializes the taxes.
        java.util.List<TaxInfo> taxlist = null;
        try {
            taxlist = senttax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = null;
        try {
            taxcategorieslist = senttaxcategories.list();
        } catch (BasicException ex) {
            logger.info("exception in activate taxcategorieslist" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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

        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist, m_App);

        try {
            sertaxlist = sentsertax.list();
        } catch (BasicException ex) {
            logger.info("exception in activate" + ex.getMessage() + ";");
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sertaxcollection = new ListKeyed<TaxInfo>(sertaxlist);
        staxeslogic = new RetailSTaxesLogic(sertaxlist, m_App);

        try {
            sbTaxlist = sentSBtax.list();
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


        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());
        logger.info("before m_ticketsbag activate method");

        m_ticketsbag.activate();
        //call promotion campaign logic
        try {
            setPromotionData();
            String day = getWeekDay();
            promotionList = dlSales.getActivePromotionCampList(day);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }

        promoTimer.start();

        String role = m_App.getAppUserView().getUser().getRole();
        try {
            roleName = dlReceipts.getRoleByUser(role);
        } catch (BasicException ex) {
            logger.info("exception in roleName" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info("After main activate method");
    }

    public void getServiceCharge(String isHomeDelivery) {
        String businessTypeId = null;
        int businessTypeCount = 0;
        try {
            businessTypeCount = dlSales.getBusinessTypeCount(isHomeDelivery);
        } catch (BasicException ex) {
            logger.info("exception in getServiceCharge" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (businessTypeCount == 1) {
            try {
                businessTypeId = dlSales.getBusinessTypeId(isHomeDelivery);
            } catch (BasicException ex) {
                logger.info("exception in businessTypeId" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                serviceTaxList = (ArrayList<BusinessServiceTaxInfo>) dlSales.getBusinessServiceTax(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceTaxList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                serviceChargeList = (ArrayList<BusinessServiceChargeInfo>) dlSales.getBusinessServiceCharge(businessTypeId);
            } catch (BasicException ex) {
                logger.info("exception in serviceChargeList" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //method is called on logout action and closing the application
    public boolean deactivate() {
        int i = 0;
        if (m_oTicket != null) {
            try {
                String currentUpdated = m_dateformat.format(m_oTicket.getObjectUpdateDate()) + " " + m_dateformattime.format(m_oTicket.getObjectUpdateDate());
                String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                Date currentUpdatedDate = DateFormats.StringToDateTime(currentUpdated);
                Date dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                if (dbUpdated.equals(null) || dbUpdated.equals("")) {
                    showMessage(this, "This Bill is no longer exist");
                    logger.info("This Bill is no longer exist");
                } else if (dbUpdatedDate.compareTo(currentUpdatedDate) > 0) {
//                    if (m_oTicket != null) {
//                        RetailTicketInfo ticket = dlReceipts.getRetailSharedTicketSplit(m_PlaceCurrent.getId(), m_oTicket.getSplitSharedId());
//                        if (ticket != null) {
//                            ticket.setTicketOpen(false);
//                            dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), ticket);
//                        }
//
//
//                    }
                    logger.info("The Table is being accessed by another User!Cannot update the bill");
                    // showMessage(this, "The Table is being accessed by another User!Cannot update the bill");

                }
                System.out.println("within deactivate ");
                logger.info("within deactivate ");
                //if no kot done    
                if (m_oTicket.getOrderId() == 0) {
                    try {
                        logger.info("Order No." + m_oTicket.getOrderId() + "deleting 0 order no. Bill in deactivate method of " + m_oTicket.getTableName() + " id is " + m_oTicket.getPlaceId());
                        dlReceipts.deleteSharedTicket(m_oTicket.getPlaceId());
                        m_ticketsbag.deleteTicket();
                    } //if kot done but cancelled all lines
                    catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (m_oTicket.getOrderId() != 0 && m_oTicket.getLinesCount() == 0) {
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
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (m_oTicket.getSplitValue().equals("")) {
                        //     logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot bill in deactivate method");
                        m_ticketsbag.deleteTicket();
                    }
//                else {
//                    try {
//                        logger.info("Order No." + m_oTicket.getOrderId() + "deleting cancelled kot splitted Bill in deactivate method");
//                        dlReceipts.deleteSharedSplitTicket(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
//                    } catch (BasicException ex) {
//                        logger.info("ORDER NO. " + m_oTicket.getOrderId() + "exception in deleteSharedSplitTicket in deactivate method" + ex.getMessage());
//                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
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
                    m_oTicket.refreshTxtFields(0);
                    if (m_oTicket.getLinesCount() == 0) {
                        logger.info("Order No." + m_oTicket.getOrderId() + "deleting zero lines Bill in deactivate method");
                        if (m_oTicket.getOrderId() != 0) {
                            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
                            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
                            m_oTicket.setDate(new Date()); //
                            String ticketDocument;
                            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_oTicket.getTicketId();
                            String reason = "cancelled all kot lines";
                            try {
                                dlSales.saveRetailCancelTicket(m_oTicket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                            } catch (BasicException ex) {
                                logger.info("newTicket saveRetailCancelTicket exception 2" + ex.getMessage() + ";");
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        m_ticketsbag.deleteTicket();
                    }

                }
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        RefreshTicketTimer.stop();
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
        catcontainer.setVisible(true);
        if (m_oTicket != null) {
            JRetailTicketsBagRestaurant.stopReloadTimer();
            leastValuePromo = false;
            buyGetPromo = false;
            m_oTicket.setM_App(m_App);
            tableId = m_oTicket.getPlaceId();
            RefreshTicketTimer.start();
            m_oTicket.setM_App(m_App);
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            //  if (m_oTicket.getSplitValue().equals("")) {
            String splitId1 = m_oTicket.getSplitSharedId();
            System.out.println("splitId :" + splitId1);
            if (dbUpdatedDate == null) {
                try {
                    String splitId = m_oTicket.getSplitSharedId();
                    System.out.println("splitId :" + splitId);
                    Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                    if (record != null) {
                        m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));
                    }
                    System.out.println("m_oTicket.GET ObjectUpdateDate :  " + m_oTicket.getObjectUpdateDate());
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            kotaction = 0;
            m_oTicket.setTableName(oTicketExt.toString());
            try {
                logger.info("No. of Running Tables : " + dlReceipts.getSharedTicketCount());
            } catch (BasicException ex) {
                logger.info("exception in getSharedTicketCount" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (m_oTicket.getOldTableName() != null && m_oTicket.getOldTableName() != "") {
                try {
                    kotMoveTableDisplay();
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                m_oTicket.setOldTableName(null);
            }
            if (m_oTicket.getSplitValue().equals("Split")) {
                m_oTicket.setTicketOpen(true);
                m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
                //     try { 
                // dlReceipts.updateSharedTicket(m_oTicket.getPlaceId(), m_oTicket);
                if (dbUpdatedDate == null) {
                    try {
                        String splitId = m_oTicket.getSplitSharedId();
                        System.out.println("splitId :" + splitId);
                        Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                        if (record != null) {
                            m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));
                        }
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

//                } catch (BasicException ex) {
//                    logger.info("exception in setRetailActiveTicket while updatesharedticket" + ex.getMessage());
//                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }

            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.
            String accDate = new SimpleDateFormat("yyyy-MM-dd").format(m_App.getActiveDayDateStart());
            Date dateValue = java.sql.Date.valueOf(accDate);
            m_oTicket.setAccountDate(dateValue);

            for (RetailTicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setRetailPanel(this);
            }

            m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));

        }

        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");
        refreshTicket();

    }

    public void setRetailActiveTicket(RetailTicketInfo oTicket, Object oTicketExt, String editBillId) {
        this.editSaleBillId = editBillId;
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        if (getEditSale() == "Edit") {
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
                line.setRetailPanel(this);

            }

        }

        if (m_jTxtItemCode.getText() != null) {
            m_jTxtItemCode.setText("");
            m_jCboItemName.setSelectedIndex(-1);
            m_jCboItemName.setSelectedItem("");
        }
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
        } else {
            if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
            }

            String name = m_oTicket.getName(m_oTicketExt);
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();
            // Panel show tickets.
            cl.show(this, "ticket");

            m_jKeyFactory.setText(null);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
            m_jLblCurrentDate.setText(getDateForMe().toString());
            m_jUser.setText((m_oTicket.getUser()).getName());
            m_jLblTime.setText(getTime().toString() + "        ");
            m_jTable.setText(m_oTicket.getName(m_oTicketExt));
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
    //This method is used for loading the popular items in billing screen

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

        } catch (BasicException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + "exception in showProductPanel" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }


        JRetailCatalogTab jcurrTab = new JRetailCatalogTab();
        jcurrTab.applyComponentOrientation(getComponentOrientation());
        m_jProducts.add(jcurrTab, "PRODUCT.");

        // Add products
        for (ProductInfoExt prod : product) {
            tnbbutton = new ThumbNailBuilderPopularItems(97, 57, "com/openbravo/images/bluepopulartoit.png");
            jcurrTab.addPopularItemButton(new ImageIcon(tnbbutton.getThumbNailText(null, getProductLabel(prod))), new SelectedAction(prod));
        }

        CardLayout cl = (CardLayout) (m_jProducts.getLayout());
        cl.show(m_jProducts, "PRODUCT.");

    }
    // Method is for labeling the name of the product

    private String getProductLabel(ProductInfoExt product) {

        return "<html><center>" + product.getName() + "<br>";

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

        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros1.setText(null);
            m_jTaxesEuros1.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount1.setText(null);
            m_jPromoDiscount.setText(null);
            m_oTicket.setBillDiscount(0);
            m_jServiceTax.setText(null);
            m_jSwachBharat.setText(null);
            m_jTaxList.setModel(new DefaultListModel());
            m_jServiceTaxLbl.setVisible(false);
            m_jSwachBharatLbl.setVisible(false);
            leastValuePromo = false;
            buyGetPromo = false;
        } else {

            try {
                if (m_oTicket.getDiscountMap() != null && m_oTicket.iscategoryDiscount()) {
                    populateDiscount(m_oTicket.getDiscountMap());
                }

                populateTaxList();
            } catch (BasicException ex) {
                logger.info("exception in populateTaxList " + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            m_jSubtotalEuros1.setText(m_oTicket.printSubTotalValueBeforeDiscount());
            m_jTaxesEuros1.setText(m_oTicket.printTax());
            m_jTotalEuros.setText(m_oTicket.printTotal());
            m_jDiscount1.setText(m_oTicket.printDiscount());
            m_jPromoDiscount.setText(m_oTicket.printPromoDiscount());

        }
    }
    //consolidating taxes based on rate developed by Shilpa

    private void consolidateTaxes(RetailTicketInfo ticket) {
        Map<String, NameTaxMapInfo> taxMap = new HashMap<String, NameTaxMapInfo>();
        double rate;
        String taxName;
        double taxValue;
        //Grouping the taxes based on rate
        for (int i = 0; i < ticket.getTaxes().size(); i++) {
            rate = ticket.getTaxes().get(i).getTaxInfo().getRate();
            taxName = ticket.getTaxes().get(i).getTaxInfo().getSplitName();
            //     System.out.println("rate-----"+rate+" name--"+taxName);
//            if (taxName.startsWith("KRISHI")) {
//                rate = 0.0021;
//            }
            //   System.out.println("rate--after---"+rate);
            taxValue = ticket.getTaxes().get(i).getRetailTax();
            String strTaxValue = String.format("%.2f", taxValue);
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
            taxeslogic.calculateTaxes(m_oTicket);
        } catch (TaxesException ex) {
            logger.info("exception in populateTaxList " + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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

    }

    private void paintTicketLine(int index, RetailTicketLineInfo oLine) {
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
        if (executeEventAndRefreshForKot("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            m_oTicket.setLine(index, oLine);
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);
            visorTicketLine(oLine); // Y al visor tambien...

        }
    }

    //Method is used for adding ticket lines 
    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) {
        kotaction = 1;
        TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
        ServiceChargeInfo charge = chargeslogic.getChargeInfo(oProduct.getServiceChargeID(), m_oTicket.getCustomer());
        TaxInfo sertax = staxeslogic.getTaxInfo(oProduct.getServiceTaxID(), m_oTicket.getCustomer());
        TaxInfo sbTax = sbtaxeslogic.getTaxInfo(oProduct.getSwachBharatTaxId(), m_oTicket.getCustomer());
        if (oProduct.getComboProduct().equals("Y")) {
            addonId = UUID.randomUUID().toString();
            addonId = addonId.replaceAll("-", "");
            addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 1, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, sbTax, null, null, null, 1, null, false));
            checkMandatoryAddon(oProduct, addonId);
        } else {
            addTicketLine(new RetailTicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList, dlSales, m_oTicket, m_ticketlines, this, tax, 0, oProduct.getName(), oProduct.getProductType(), oProduct.getProductionAreaType(), (java.util.Properties) (oProduct.getProperties().clone()), addonId, 0, null, 0, null, null, null, charge, sertax, oProduct.getParentCatId(), oProduct.getPreparationTime(), null, sbTax, null, null, null, 0, null, false));
        }
        addonId = null;
        if (leastValuePromo) {
            checkLeastValuePromotion();
        }
    }

    public void addTicketLine(RetailTicketLineInfo oLine) {

        if (executeEventAndRefresh("ticket.addline", new ScriptArg("line", oLine)) == null) {

            if (oLine.isProductCom()) {

                int i = m_ticketlines.getSelectedIndex();

                if (i >= 0 && !m_oTicket.getLine(i).isProductCom()) {
                    i++;
                }

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

                m_oTicket.addLine(oLine);
                m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la vista...
                //Method is used for applying promotion for the selected item
                RetailTicketLineInfo newLine = setPromoLine(oLine);
                if (newLine != null && buyGetPromo) {
                    oLine.setPromoAction(true);
                    newLine.setPromoAction(true);
                    m_oTicket.addLine(newLine);
                    m_ticketlines.addTicketLine(newLine);
                }

            }
            visorTicketLine(oLine);
            printPartialTotals();
            stateToZero();

            // event receipt
            executeEventAndRefresh("ticket.change");
            m_jCboItemName.setSelectedItem("");
            m_jTxtItemCode.setText("");
            stateToItem();
        }
    }
    //Method is used to verify any promotion is applicable for selected item & calculate the promotion

    public RetailTicketLineInfo setPromoLine(RetailTicketLineInfo line) {

        int productqty = 0;
        productqty = (int) line.getMultiply();
        RetailTicketLineInfo newLine = null;

        java.util.ArrayList<String> promoId = new ArrayList<String>();
        double productDiscount = 0;
        if (line.promoRuleIdList != null) {

            for (int i = 0; i < line.promoRuleIdList.size(); i++) {
                promoId.add("'" + line.promoRuleIdList.get(i).getpromoRuleId() + "'");
            }
            StringBuilder b = new StringBuilder();
            Iterator<?> it = promoId.iterator();
            while (it.hasNext()) {
                b.append(it.next());
                if (it.hasNext()) {
                    b.append(',');
                }
            }
            String promoRuleId = b.toString();

            int productCount = 0;
            String promoType = null;
            String promoTypeId = null;
            int priceOffCount = 0;
            int percentageOffCount = 0;
            String isPrice;
            String isPromoProduct;
            //Checking whether the selected product is belongs to any valid promotion
            try {
                productCount = dlSales.getProductCount(line.getProductID(), promoRuleId);
            } catch (BasicException ex) {
                Logger.getLogger(RetailTicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
            //if the product is belongs to any valid promotion, Select the promotion rule type for that particular product
            if (productCount != 0) {
                try {
                    promoType = dlSales.getPromoType(line.getProductID());
                } catch (BasicException ex) {
                    Logger.getLogger(RetailTicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (promoType.equals("PDPrice")) {
                    isPrice = "Y";
                    isPromoProduct = "Y";
                    //Method for calculation price off promotion discount value
                    productDiscount = line.getPriceoffDiscount(line.getProductID(), isPrice, isPromoProduct, promoRuleId, productqty);

                } else if (promoType.equals("PDPercent")) {
                    isPrice = "N";
                    isPromoProduct = "Y";
                    //Method for calculation Percentage off promotion discount value
                    productDiscount = line.getPercentageoffDiscount(line.getProductID(), isPrice, isPromoProduct, promoRuleId, productqty);
                } else //Product promotion type is SIBG
                if (promoType.equals("SIBG")) {
                    line.setSibgId(UUID.randomUUID().toString());
                    isPrice = "N";
                    isPromoProduct = "Y";
                    //Method is for calculating the buy one get one promotion
                    newLine = line.getBuyGetDiscount(line.getProductID(), isPrice, isPromoProduct, promoRuleId, productqty);
                    System.out.println("newline---" + newLine);
                    productDiscount = 0;

                }
            }
        }

        return newLine;
    }
    //Method is used for removing the ticketline

    private void removeTicketLine(int i) {

        if (executeEventAndRefresh("ticket.removeline", new ScriptArg("index", i)) == null) {

            if (m_oTicket.getLine(i).isProductCom()) {
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);
            } else {

                if (m_oTicket.getLine(i).getPromoType().equals("SIBG") && m_oTicket.getLine(i).getIsPromoSku().equals("Y") && m_oTicket.getLine(i).isPromoAction()) {
                    if (m_oTicket.getLine(i).getActualPrice() != 0) {

                        m_oTicket.removeLine(i);
                        m_ticketlines.removeTicketLine(i);
                        int index = i;
                        m_oTicket.removeLine(index);
                        m_ticketlines.removeTicketLine(index);
                    }
                } else {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }


                while (i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).isProductCom()) {
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i);
                }
            }

            visorTicketLine(null);
            printPartialTotals();
            stateToZero();

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
            TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getCustomer(), "N");
            double dTaxRate = tax == null ? 0.0 : tax.getRate();
            return dValue / (1.0 + dTaxRate);
        } else {
            return dValue;
        }
    }

    private double getInputValue() {

        return 0.0;

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
        m_sBarcode = new StringBuffer();
        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }

    public void stateToBarcode() {

        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        m_jSplitBtn.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }

    public void stateToHomeDelivery() {
        cusName.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        jLblPrinterStatus.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);


    }

    public void stateToItem() {

        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jAction.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        JRetailTicketsBagRestaurant.setFocusable();
        jLblPrinterStatus.setFocusable(false);
        catcontainer.setFocusable(false);
        m_jSettleBill.setFocusable(false);
        m_jEraser.setFocusable(false);
        m_jbtnPrintBill.setFocusable(false);
    }

    public void stateToPay() {
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
    }

    public void stateToPayment() {
        itemName.setFocusable(false);
        m_jTxtItemCode.setFocusable(false);
        m_jPor.setText("");
        m_jPor.setFocusable(false);
        m_jKeyFactory.setFocusable(false);
        m_jTax.setFocusable(false);
        m_jaddtax.setFocusable(false);
    }
    //Adding the items based on barcode

    private void incProductByCode(String sCode) {

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
    //Adding the items based on product id

    public void incProductByItemDetails(String id) {
        // precondicion: sCode != null

        try {
            ProductInfoExt oProduct = dlSales.getProductInfo(id);
            if (oProduct == null) {
                Toolkit.getDefaultToolkit().beep();
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
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer(), "N");
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
        } catch (java.text.ParseException ex) {
            logger.info("Order No. " + m_oTicket.getOrderId() + " exception in getCreditDate" + ex.getMessage());
            Logger.getLogger(RetailTicketInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

        m_oTicket.setNewDate(Currentdate);



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
    //Method for getting the day of week

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
        buttonPlus = 1;

        if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(prod);
        } else if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(getInputValue(), prod);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

//    private void stateTransition(char cTrans) throws BasicException {
//
//        if (cTrans == '\n') {
//            // Barcode length
//            if (m_sBarcode.length() > 0) {
//                String sCode = m_sBarcode.toString();
//                if (sCode.startsWith("c")) {
//                    // barcode of a customers card
//                    try {
//                        CustomerInfoExt newcustomer = dlSales.findCustomerExt(sCode);
//                        if (newcustomer == null) {
//                            Toolkit.getDefaultToolkit().beep();
//                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer")).show(this);
//                        } else {
//                            m_oTicket.setCustomer(newcustomer);
////                            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
//                        }
//                    } catch (BasicException e) {
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e).show(this);
//                    }
//                    stateToZero();
//                } else if (sCode.length() == 13 && sCode.startsWith("250")) {
//                    // barcode of the other machine
//                    ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
//                    oProduct.setReference(null); // para que no se grabe
//                    oProduct.setCode(sCode);
//                    oProduct.setName("Ticket " + sCode.substring(3, 7));
//                    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7, 12)) / 100);
//                    oProduct.setTaxCategoryID(((TaxCategoryInfo) taxcategoriesmodel.getSelectedItem()).getID());
//                    // Se anade directamente una unidad con el precio y todo
//                    addTicketLine(oProduct, 1.0, includeTaxes(oProduct.getTaxCategoryID(), oProduct.getPriceSell()));
//                } else if (sCode.length() == 13 && sCode.startsWith("210")) {
//                    // barcode of a weigth product
//                    incProductByCodePrice(sCode.substring(0, 7), Double.parseDouble(sCode.substring(7, 12)) / 100);
//                } else {
//                    incProductByCode(sCode);
//                }
//            } else {
//                Toolkit.getDefaultToolkit().beep();
//            }
//        } else {
//            // otro caracter
//            // Esto es para el codigo de barras...
//            m_sBarcode.append(cTrans);
//
//            // Esto es para el los productos normales...
//            if (cTrans == '\u007f') {
//                stateToZero();
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
////                m_jPrice.setText("0");
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
//                // Un numero entero
//                //       m_jPrice.setText(Character.toString(cTrans));
//                m_iNumberStatus = NUMBER_INPUTINT;
//                m_iNumberStatusInput = NUMBERVALID;
//            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTINT)) {
//                // Un numero entero
//                //      m_jPrice.setText(m_jPrice.getText() + cTrans);
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
//                //    m_jPrice.setText("0.");
//                m_iNumberStatus = NUMBER_INPUTZERODEC;
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
//                //    m_jPrice.setText(m_jPrice.getText() + ".");
//                m_iNumberStatus = NUMBER_INPUTDEC;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                // Un numero decimal
//                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                // Un numero decimal
//                //   m_jPrice.setText(m_jPrice.getText() + cTrans);
//                m_iNumberStatus = NUMBER_INPUTDEC;
//                m_iNumberStatusInput = NUMBERVALID;
//
//            } else if (cTrans == '*'
//                    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
//                m_jPor.setText("x");
//                m_iNumberStatus = NUMBER_PORZERO;
//            } else if (cTrans == '*'
//                    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
//                //  m_jPrice.setText("0");
//                m_jPor.setText("x");
//                m_iNumberStatus = NUMBER_PORZERO;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_PORZERO)) {
//                m_jPor.setText("x0");
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORZERO)) {
//                // Un numero entero
//                m_jPor.setText("x" + Character.toString(cTrans));
//                m_iNumberStatus = NUMBER_PORINT;
//                m_iNumberStatusPor = NUMBERVALID;
//            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORINT)) {
//                // Un numero entero
//                m_jPor.setText(m_jPor.getText() + cTrans);
//
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORZERO) {
//                m_jPor.setText("x0.");
//                m_iNumberStatus = NUMBER_PORZERODEC;
//            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORINT) {
//                m_jPor.setText(m_jPor.getText() + ".");
//                m_iNumberStatus = NUMBER_PORDEC;
//
//            } else if ((cTrans == '0')
//                    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
//                // Un numero decimal
//                m_jPor.setText(m_jPor.getText() + cTrans);
//            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
//                    && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) {
//                // Un numero decimal
//                m_jPor.setText(m_jPor.getText() + cTrans);
//                m_iNumberStatus = NUMBER_PORDEC;
//                m_iNumberStatusPor = NUMBERVALID;
//
//            } else if (cTrans == '\u00a7'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
//                // Scale button pressed and a number typed as a price
//                if (m_App.getDeviceScale().existsScale() && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                    try {
//                        Double value = m_App.getDeviceScale().readWeight();
//                        if (value != null) {
//                            ProductInfoExt product = getInputProduct();
//                            addTicketLine(product, value.doubleValue(), product.getPriceSell());
//                        }
//                    } catch (ScaleException e) {
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
//                        stateToZero();
//                    }
//                } else {
//                    // No existe la balanza;
//                    Toolkit.getDefaultToolkit().beep();
//                }
//            } else if (cTrans == '\u00a7'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
//                // Scale button pressed and no number typed.
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else if (m_App.getDeviceScale().existsScale()) {
//                    try {
//                        Double value = m_App.getDeviceScale().readWeight();
//                        if (value != null) {
//                            RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                            newline.setMultiply(value.doubleValue());
//                            newline.setPrice(Math.abs(newline.getPrice()));
//                            paintTicketLine(i, newline);
//                        }
//                    } catch (ScaleException e) {
//                        // Error de pesada.
//                        Toolkit.getDefaultToolkit().beep();
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);
//                        stateToZero();
//                    }
//                } else {
//                    // No existe la balanza;
//                    Toolkit.getDefaultToolkit().beep();
//                }
//
//                // Add one product more to the selected line
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    //If it's a refund + button means one unit less
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(newline.getMultiply() - 1.0);
//                        paintTicketLine(i, newline);
//                    } else {
//                        // add one unit to the selected line
//                        newline.setMultiply(newline.getMultiply() + 1.0);
//                        paintTicketLine(i, newline);
//
//                    }
//                }
//
//                // Delete one product of the selected line
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    //If it's a refund - button means one unit more
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(newline.getMultiply() + 1.0);
//                        if (newline.getMultiply() >= 0) {
//                            removeTicketLine(i);
//                        } else {
//                            paintTicketLine(i, newline);
//                        }
//                    } else {
//                        // substract one unit to the selected line
//                        newline.setMultiply(newline.getMultiply() - 1.0);
//                        if (newline.getMultiply() > 0.0) {
//                            paintTicketLine(i, newline);
//
//                        }
//                    }
//                }
//
//                // Set n products to the selected line
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    double dPor = getPorValue();
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        newline.setMultiply(-dPor);
//                        newline.setPrice(Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    } else {
//                        newline.setMultiply(dPor);
//                        newline.setPrice(Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    }
//                }
//
//                // Set n negative products to the selected line
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//
//                int i = m_ticketlines.getSelectedIndex();
//                if (i < 0) {
//                    Toolkit.getDefaultToolkit().beep();
//                } else {
//                    double dPor = getPorValue();
//                    RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
//                        newline.setMultiply(dPor);
//                        newline.setPrice(-Math.abs(newline.getPrice()));
//                        paintTicketLine(i, newline);
//                    }
//                }
//
//                //Add 1 product
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, 1.0, product.getPriceSell());
//
//                // Anadimos 1 producto con precio negativo
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, 1.0, -product.getPriceSell());
//
//                // Anadimos n productos
//            } else if (cTrans == '+'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, getPorValue(), product.getPriceSell());
//
//                // Anadimos n productos con precio negativo ?
//            } else if (cTrans == '-'
//                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
//                    && m_App.getAppUserView().getUser().hasPermission("sales.EditLines")) {
//                ProductInfoExt product = getInputProduct();
//                addTicketLine(product, getPorValue(), -product.getPriceSell());
//
//                // Totals() Igual;
//            } else if (cTrans == ' ' || cTrans == '=') {
//                if (m_oTicket.getLinesCount() > 0) {
//
//                    if (closeTicket(m_oTicket, m_oTicketExt, m_aPaymentInfo)) {
//                        // Ends edition of current receipt
//                        if (!m_oTicket.getSplitValue().equals("Split")) {
//                            m_ticketsbag.deleteTicket();
//                        } else {
//                        }
//
//                    } else {
//                        // repaint current ticket
//                        refreshTicket();
//                    }
//                } else {
//                    Toolkit.getDefaultToolkit().beep();
//                }
//            }
//        }
//    }
    private boolean closeSplitTicket(RetailTicketInfo ticket, Object ticketext) {
        ticket.setActiveDay(m_App.getActiveDayIndex());
        ticket.setActiveCash(m_App.getActiveCashIndex());
        boolean completed = JPaymentEditor.showMessage(JRetailPanelTicket.this, dlReceipts, ticket, this, "Sales");
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
    //Method is called when click on pay button in payment screen

    private boolean closeTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("closeTicket");
        boolean resultok = false;

        if (totalBillValue == 0 && m_oTicket.getTotal() != 0) {
            showMessage(this, "Please enter the tender types");
            setClosePayment(false);
        } else if (totalBillValue < m_oTicket.getTotal()) {
            showMessage(this, "Entered tender amount should be equal to total sale amount");
            setClosePayment(false);
        } else {

            try {
                // Calculate taxes.
                taxeslogic.calculateTaxes(ticket);
            } catch (TaxesException ex) {
                logger.info("Order No." + m_oTicket.getOrderId() + " exception while calculateTaxes in closeticket " + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (executeEvent(ticket, ticketext, "ticket.total") == null) {
                double creditAmt;
                creditAmt = 0;
                creditAmount = 0;
                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo());
                ticket.setActiveCash(m_App.getActiveCashIndex());
                ticket.setDate(new Date());
                ticket.setPayments(m_aPaymentInfo.getPayments());
                if (executeEvent(ticket, ticketext, "ticket.save") == null) {

                    String ticketDocNo = null;
                    Integer ticketDocNoInt = null;
                    String ticketDocument = null;
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {

                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
                    }

                    m_oTicket.setDocumentNo(ticketDocument);
                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {

                        getCreditDate();
                    }

                    String chequeNos = "";
                    String deliveryBoy = "";
                    String cod;
                    double advanceissued;
                    String isCredit;
                    String isPaidStatus;
                    deliveryBoy = "";
                    cod = "N";
                    isPaidStatus = "Y";
                    if (creditAmt > 0) {
                        isCredit = "Y";
                    } else {
                        isCredit = "N";
                    }

                    advanceissued = 0;
                    String file;
                    file = "Printer.Bill";
                    saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                    logger.info("bill has been settled");
                    resultok = true;
                }

            }

        }
        return resultok;
    }

//    private boolean closeTicketHomeDelivery(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
//        boolean resultok = false;
//        try {
//            taxeslogic.calculateTaxes(ticket);
//        } catch (TaxesException ex) {
//            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (ticket.getTotal() >= 0.0) {
//            ticket.resetPayments(); //Only reset if is sale
//        }
//
//        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
//
//            double creditAmt;
//            try {
//                creditAmt = m_oTicket.getTotal();
//            } catch (Exception ex) {
//                creditAmt = 0;
//            }
//            if ((cusName.getText().equals("") || cusName.getText() == null)) {
//                showMessage(this, "Please select the customer");
//            } else {
//
//                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
//                ticket.setActiveCash(m_App.getActiveCashIndex());
//                ticket.setDate(new Date()); // Le pongo la fecha de cobro
//
//                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
//                    String ticketDocNo = null;
//                    Integer ticketDocNoInt = null;
//                    String ticketDocument;
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
//                        try {
//                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
//                            String[] ticketDocNoValue = ticketDocNo.split("-");
//                            ticketDocNo = ticketDocNoValue[2];
//                        } catch (NullPointerException ex) {
//                            ticketDocNo = "10000";
//                        }
//                        if (ticketDocNo != null) {
//                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                            ticketDocNoInt = ticketDocNoInt + 1;
//
//                        }
//                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
//                    } else {
//                        ticketDocument = "0";
//                    }
//                    m_oTicket.setDocumentNo(ticketDocument);
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        getCreditDate();
//                    }
//                    String chequeNos = "";
//                    String deliveryBoy = "";
//                    double advanceissued;
//
//                    deliveryBoy = "";
//                    advanceissued = 0;
//
//                    String file;
//                    file = "Printer.HomeDelivery";
//
//                    if (m_oTicket.getLinesCount() == 0) {
//                        showMessage(this, "Please select the products");
//                    } else {
//                        saveHomeReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "N");
//                        resultok = true;
//                    }
//                }
//                if (!m_oTicket.getSplitValue().equals("Split")) {
//                    m_ticketsbag.deleteTicket();
//                } else {
//                }
//            }
//
//        }
//        return resultok;
//    }
    //Method is called when click on nonchargable button in payment screen
//    private boolean closeTicketNonChargable(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
//        boolean resultok = false;
//
//        try {
//            taxeslogic.calculateTaxes(ticket);
//        } catch (TaxesException ex) {
//            logger.info("Order No." + m_oTicket.getOrderId() + " exception in calculateTaxes of homedelivery" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (ticket.getTotal() >= 0.0) {
//            ticket.resetPayments(); //Only reset if is sale
//        }
//
//        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
//            double creditAmt;
//            try {
//                creditAmt = m_oTicket.getTotal();
//            } catch (Exception ex) {
//                creditAmt = 0;
//            }
//            if ((cusName.getText().equals(""))) {
//                showMessage(this, "Please select the customer");
//            } else {
//
//                ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
//                ticket.setActiveCash(m_App.getActiveCashIndex());
//                ticket.setDate(new Date()); // Le pongo la fecha de cobro
//
//                if (executeEvent(ticket, ticketext, "ticket.save") == null) {
//                    String ticketDocNo = null;
//                    Integer ticketDocNoInt = null;
//                    String ticketDocument;
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
//                        try {
//                            ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
//                            String[] ticketDocNoValue = ticketDocNo.split("-");
//                            ticketDocNo = ticketDocNoValue[2];
//                        } catch (NullPointerException ex) {
//                            ticketDocNo = "10000";
//                        }
//                        if (ticketDocNo != null) {
//                            ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                            ticketDocNoInt = ticketDocNoInt + 1;
//
//                        }
//                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticketDocNoInt;
//                    } else {
//                        ticketDocument = "0";
//                    }
//                    m_oTicket.setDocumentNo(ticketDocument);
//                    if (m_oTicket.getTicketType() == RetailTicketInfo.RECEIPT_REFUND) {
//                        getCreditDate();
//                    }
//                    String chequeNos = "";
//                    String deliveryBoy = "";
//                    double advanceissued;
//
//                    deliveryBoy = "";
//
//                    advanceissued = 0;
//
//                    ticket.setDocumentNo(ticketDocument);
//                    String file;
//                    file = "Printer.NonChargableBill";
//
//                    if (m_oTicket.getLinesCount() == 0) {
//                        showMessage(this, "Please select the products");
//                    } else {
//                        saveNonChargableReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, "N", "Y");
//
//                        resultok = true;
//                    }
//                }
//
//            }
//
//        }
//        return resultok;
//    }
    //Method is called when click on payment mode in payment screen
    private boolean closeTicketWithButton(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {
        logger.info("enter the closeTicketWithButton method" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        boolean resultok = false;
        //Calculating the taxes
        try {
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            logger.info("exception while calculateTaxes in closeTicketWithButton " + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments();
        }
        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            double creditAmt;
            creditAmt = 0;
            ticket.setPayments(m_aPaymentInfo.getPayments());

            ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket.setActiveCash(m_App.getActiveCashIndex());
            ticket.setDate(new Date()); // Le pongo la fecha de cobro

            if (executeEvent(ticket, ticketext, "ticket.save") == null) {
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
                String chequeNos = "";
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
                deliveryBoy = "";
                if (creditAmt > 0) {
                    isCredit = "Y";
                } else {
                    isCredit = "N";
                }

                advanceissued = 0;
                String file;
                if (ticket.getPrintStatus() == 1) {
                    file = "Printer.Bill";
                    if (ticket.getLinesCount() == 0) {
                        showMessage(this, "Please select the products");
                    } else {
                        logger.info("Before saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        //Method is used for saving the receipts
                        saveReceipt(ticket, m_App.getInventoryLocation(), m_App.getProperties().getPosNo(), m_App.getProperties().getStoreName(), ticketDocument, getPriceInfo(), chequeNos, deliveryBoy, advanceissued, creditAmt, ticketext, "Y", file, isCredit, "N");
                        logger.info("After saveReceipt if ticket.getPrintStatus()==1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        resultok = true;
                    }
                }

            }

        }
        logger.info("End of closeticketButton" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        return resultok;
    }

    private boolean closeCreditTicket(RetailTicketInfo ticket, Object ticketext, PaymentInfoList m_aPaymentInfo) throws BasicException {

        boolean resultok = false;
        try {
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }


        if (executeEvent(ticket, ticketext, "ticket.total") == null) {
            int inventoyCount = 0;
            try {
                inventoyCount = dlSales.getStopInventoryCount();
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inventoyCount == 1) {
                showMessage(this, "Stock Reconciliation in Progress. Please continue after sometime.");
            } else {
                double creditAmt;
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
                        String chequeNos = "";
                        String deliveryBoy = "";
                        String homeDelivery;
                        String isCredit;
                        homeDelivery = "Y";

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

                            resultok = true;

                        }
                    }
                }
            }
        }

        return resultok;
    }
    //Method is called for saving the receipt in database

    public void saveReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
        double tipsAmt = 0;
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;

        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        logger.info("inside save receipt");
        try {
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
            logger.info("settled successfully");
            m_jTxtTotalPaid.setText("");
            m_oTicket.setRate("0");
            m_oTicket.setdAmt(0);
            taxModel.removeAllElements();
            populateDeliveryBoy();
            m_jTxtChange.setText("");
            m_oTicket.resetCharges();
            m_oTicket.resetTaxes();
            m_oTicket.resetPayments();
            //  m_oTicket.setPromoAction(false);
            activate();
            logger.info("After activate method");
            setClosePayment(true);
            if (!ticket.getSplitValue().equals("Split")) {
                //    logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after settle bill");
                m_ticketsbag.deleteTicket();
            }
//            else {
//                logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after splitted settle bill ");
//                dlReceipts.deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
//            }
        } catch (Exception ex) {
            logger.info("Order No." + ticket.getOrderId() + " exception while calling saveRetailTicket " + ex.getMessage());
            showMessage(this, "Settlement is not happened ! Please check with the Network connection and resettle the bill");
        }
        leastValuePromo = false;
        buyGetPromo = false;
        logger.info("After save receipt completed ");
//        if (getEditSale() == "Edit") {
//            RetailTicketInfo editTicket = null;
//            try {
//                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
//            } catch (BasicException ex) {
//                logger.info("exception while edit ticket" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
//            } catch (BasicException ex) {
//                logger.info("exception while deleting ticket in savereceipt method" + ex.getMessage());
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//till here editsales
        setEditSale("");

    }
    //Method is called for saving the home delivery receipt in database

//    public void saveHomeReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
//
//        double tipsAmt = 0;
//        String homeDelivery;
//        String orderTaking;
//        String cod;
//        String isPaidStatus;
//
//        homeDelivery = "Y";
//        orderTaking = "Y";
//        cod = "N";
//        isPaidStatus = "Y";
//        try {
//            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
//        } catch (Exception ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (getEditSale() == "Edit") {
//            RetailTicketInfo editTicket = null;
//            try {
//                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        setEditSale("");
//        try {
//            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        m_jTxtTotalPaid.setText("");
//        m_oTicket.setRate("0");
//        m_oTicket.setdAmt(0);
//        populateDeliveryBoy();
//        m_jTxtChange.setText("");
//        m_oTicket.resetCharges();
//        m_oTicket.resetTaxes();
//        m_oTicket.resetPayments();
//        activate();
//
//    }
    //Method is called for saving the non chargable receipt
//    public void saveNonChargableReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status, String file, String isCredit, String nonChargable) {
//        double tipsAmt = 0;//Double.parseDouble(m_jTxtTips.getText());
//        String homeDelivery;
//        String orderTaking;
//        String cod;
//        String isPaidStatus;
//        homeDelivery = "N";
//        orderTaking = "N";
//        cod = "N";
//        isPaidStatus = "Y";
//        try {
//            dlSales.saveNonChargableTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, isCredit, isPaidStatus, tipsAmt, orderTaking, nonChargable);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (getEditSale() == "Edit") {
//            RetailTicketInfo editTicket = null;
//            try {
//                editTicket = dlSales.loadEditRetailTicket(0, editSaleBillId);
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                dlSales.deleteTicket(editTicket, m_App.getInventoryLocation());
//            } catch (BasicException ex) {
//                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        setEditSale("");
//        try {
//            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
//        } catch (BasicException ex) {
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        printTicket(file, ticket, ticketext);
//        m_jTxtTotalPaid.setText("");
//        m_oTicket.setRate("0");
//        m_oTicket.setdAmt(0);
//        populateDeliveryBoy();
//        m_oTicket.resetCharges();
//        m_oTicket.resetTaxes();
//        m_oTicket.resetPayments();
//        activate();
//
//    }
    public void saveNotReceipt(RetailTicketInfo ticket, String inventoryLocation, String posNo, String storeName, String ticketDocument, ArrayList<BuyGetPriceInfo> priceInfo, String chequeNos, String deliveryBoy, double advanceissued, double creditAmount, Object ticketext, String status) {
        double tipsAmt = 0;
        String homeDelivery;
        String orderTaking;
        String cod;
        String isPaidStatus;


        homeDelivery = "N";
        orderTaking = "N";
        cod = "N";
        isPaidStatus = "Y";
        try {
            dlSales.saveRetailTicket(ticket, inventoryLocation, posNo, storeName, ticketDocument, priceInfo, chequeNos, deliveryBoy, homeDelivery, cod, advanceissued, creditAmount, status, "N", "Y", tipsAmt, orderTaking, "N");
        } catch (Exception eData) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
            msg.show(this);
        }
        try {
            dlSales.insertRetailTicket(m_oTicket.getPriceInfo(), ticket);
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_jTxtChange.setText("");
        m_jTxtTotalPaid.setText("");
        m_oTicket.setRate("0");
        m_oTicket.setdAmt(0);
        populateDeliveryBoy();
        m_oTicket.resetCharges();
        m_oTicket.resetTaxes();
        m_oTicket.resetPayments();
        activate();

    }

    public void clearFocus(boolean value) {

        m_jTxtItemCode.setFocusable(value);
        m_jKeyFactory.setFocusable(true);
        m_jKeyFactory.setText(null);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_jKeyFactory.requestFocus();
            }
        });

        m_jTxtItemCode.setText("");

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
    //This method is used for adding the lines for printing the receipt

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
        allLines.add(new TicketLineConstructor("Table : " + getSpaces(1) + ticketext));
        allLines.add(new TicketLineConstructor("Date : " + getSpaces(1) + (ticket.printDateForReceipt()) + getSpaces(2) + "Time: " + getSpaces(1) + (ticket.printTime())));
        allLines.add(new TicketLineConstructor(getDottedLine(35)));

        allLines.add(new TicketLineConstructor("Item Name" + getSpaces(10) + "Qty" + getSpaces(5) + "Amount"));
        allLines.add(new TicketLineConstructor(getDottedLine(35)));

        for (RetailTicketLineInfo tLine : ticket.getUniqueLines()) {

            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            String total = Formats.DoubleValue.formatValue(tLine.getSubValueBeforeDiscount());
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
        }

        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        String subTotal = Formats.DoubleValue.formatValue(ticket.getSubTotal());
        String discount = ticket.printDiscount();//Formats.DoubleValue.formatValue(ticket.getLineDiscountOnCategory());
        String totalAfrDiscount = Formats.DoubleValue.formatValue(ticket.getSubtotalAfterDiscount());
        String roundoff = Formats.DoubleValue.formatValue(ticket.getRoundOffvalue());
        String total = Formats.DoubleValue.formatValue(ticket.getTotal());
        allLines.add(new TicketLineConstructor("SubTotal " + getSpaces(26 - subTotal.length()) + (subTotal)));
        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Discount " + getSpaces(25 - discount.length()) + ("-" + discount)));

        String aCount = ticket.printTicketCount();

        //calling consolidated tax (logic applied to bring erp tax configuration) by Shilpa
        if (ticket.getTaxes().size() != 0) {
            for (Map.Entry<String, NameTaxMapInfo> entry : m_oTicket.getNametaxMap().entrySet()) {
                String taxName = entry.getValue().getName();
                double taxValue = entry.getValue().getTaxValue();
                if (taxValue != 0.00) {
                    allLines.add(new TicketLineConstructor(taxName + getSpaces(35 - (Formats.DoubleValue.formatValue(taxValue).length() + taxName.length())) + (Formats.DoubleValue.formatValue(taxValue))));
                }
            }

        }

        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Total Amount : " + getSpaces(20 - total.length()) + (total)));
        allLines.add(new TicketLineConstructor(getSpaces(20) + getDottedLine(15)));
        allLines.add(new TicketLineConstructor("Thank you! Please Visit Us again"));
        return allLines;
    }
    //This method is used for adding the lines for printing the non chargable receipt

    private java.util.List<TicketLineConstructor> getNonChargeableLines(RetailTicketInfo ticket, Object ticketext) {
        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        allLines.add(new TicketLineConstructor("Bill No:" + getSpaces(8) + ticket.getDocumentNo()));
        allLines.add(new TicketLineConstructor("Bill Date:" + getSpaces(6) + (ticket.printDate())));
        allLines.add(new TicketLineConstructor("Customer:" + getSpaces(7) + (cusName.getText())));
        allLines.add(new TicketLineConstructor("Table: " + getSpaces(9) + ticketext));
        allLines.add(new TicketLineConstructor("Captain:" + getSpaces(8) + (ticket.printUser())));
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

        return allLines;
    }
    //Adding blank spaces between lines

    private void addlinesaddBlankLines(int count, java.util.List<TicketLineConstructor> allLines) {
        for (int i = 0; i < count; i++) {
            allLines.add(new TicketLineConstructor(""));
        }
    }
    //Method is called for printing the ticket for thermal printer

    private void printTicket(String sresourcename, RetailTicketInfo ticket, Object ticketext) {
        String sresource = dlSystem.getResourceAsXML(sresourcename);

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            }
        }


    }
    //Method is called for printing the ticket for generic text printer

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

        int divideLines = allLines.size() / 48;
        int remainder = allLines.size() % 48;
        int value = 48;
        int k = 0;
        if (divideLines > 0) {
            for (int i = 0; i < divideLines; i++) {
                for (int j = k; j < value; j++) {

                    startallLines.add(new TicketLineConstructor(allLines.get(j).getLine()));
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
                        startallLines.clear();
                    } else {
                        break;
                    }
                }

            }
        }
        if (remainder > 0) {
            startallLines = new ArrayList<TicketLineConstructor>();
            startallLines.clear();
            for (int m = k; m < k + remainder; m++) {

                startallLines.add(new TicketLineConstructor(allLines.get(m).getLine()));
            }
            try {
                printer.print(startallLines);
            } catch (PrinterException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JRetailPanelTicket.this);
            }
        }
    }

    private void showMessage(JRetailPanelTicket aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                JOptionPane.INFORMATION_MESSAGE);

    }

    private JPanel getLabelPanel(String msg) {
        JPanel panel = new JPanel();
        Font font = new Font("Verdana", Font.BOLD, 12);
        panel.setFont(font);
        panel.setOpaque(true);
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
                return JRetailPanelTicket.this.getInputValue();
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
            JRetailPanelTicket.this.printReport(resourcefile, ticket, ticketext);
        }

        public void printTicket(String sresourcename) {
            JRetailPanelTicket.this.printTicket(sresourcename, ticket, ticketext);
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
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
        m_jBtnDiscount = new javax.swing.JButton();
        m_jBtnCancelBill = new javax.swing.JButton();
        m_jBtnBillOnHold = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jLblUserInfo = new javax.swing.JLabel();
        m_jUser = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        m_jTable = new javax.swing.JLabel();
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
        jLabel9 = new javax.swing.JLabel();
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
        jPanel1.add(m_jbtnPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

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
        jPanel1.add(m_jSplitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 0, -1, -1));

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
        jPanel1.add(m_jBtnDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, -1));

        m_jBtnCancelBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/can-bill.png"))); // NOI18N
        m_jBtnCancelBill.setToolTipText("Cancel the Bill");
        m_jBtnCancelBill.setPreferredSize(new java.awt.Dimension(90, 40));
        m_jBtnCancelBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancelBillActionPerformed(evt);
            }
        });
        jPanel1.add(m_jBtnCancelBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 0, -1, -1));

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
        jPanel1.add(m_jBtnBillOnHold, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 90, 40));

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

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Table:");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 50, 50));

        m_jTable.setForeground(new java.awt.Color(252, 248, 0));
        m_jTable.setText("jLabel9");
        jPanel6.add(m_jTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 60, 50));

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
                    .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jPlus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jMinus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jDelete, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jCalculatePromotion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(m_jKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                        .add(m_jCalculatePromotion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(m_jKot, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jEraser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jBtnServed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
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
        jTaxPanel.add(m_jServiceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 70, 23));

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

        jLabel9.setText("PROMO DISCOUNT");
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 120, 20));

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
//        m_jKeyFactory.setText(null);
//        try {
//            stateTransition(evt.getKeyChar());
//        } catch (BasicException ex) {
//            logger.info("Order No." + m_oTicket.getOrderId() + " exception while calling stateTransition" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    //Action performed method is called when click on delete line button
    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed
        int i = m_ticketlines.getSelectedIndex();
        if (i < 0) {
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            String addonVal = m_oTicket.getLine(i).getAddonId();
            int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
            int comboAddon = m_oTicket.getLine(i).getComboAddon();
            String promoType = m_oTicket.getLine(i).getPromoType();
            String isPromoSku = m_oTicket.getLine(i).getIsPromoSku();
            //Checking whether the item is sent to Kot
            if (kotStatus != 0) {
                //Checking whether the role is admin or cashier. Only Admin or Cashier can cancel the item
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {
                    //For adding the cancel reason
                    boolean billUpdated = JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineDelete");
                    if (billUpdated) {
                        // showMessage(this, "The Table is being accessed by another User!Try after sometime");
                        RetailTicketInfo dbticket = null;
                        try {
                            dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                        } catch (BasicException ex) {
                            logger.info("Exception while selecting the dbticket on cancel item action ");
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (dbticket != null) {
                            //  String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                            // dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                            dbticket.setObjectUpdateDate(JReasonEditor.dbUpdatedDate);
                            for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                                if (m_oTicket.getLine(index).getIsKot() == 0) {
                                    m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                                    dbticket.addLine(m_oTicket.getLine(index));
                                }
                            }
                            JRetailBufferWindow.showMessage(this);
                            logger.info("This Bill is updated one.Loading.......");
                            setRetailActiveTicket(dbticket, m_oTicketExt);
                            dbUpdatedDate = null;
                            kotaction = 1;

                        } else {
                            showMessage(this, "This Bill is no longer exist");
                            logger.info("This Bill is no longer exist");
                            m_ticketsbag.activate();
                        }
                    } else {
                        if (JReasonEditor.getCancel() == true) {
                            String sessionId = null;
                            try {
                                sessionId = dlReceipts.getFloorId(m_oTicket.getPlaceId());
                                printerInfo = dlReceipts.getPrinterInfo(sessionId);
                            } catch (BasicException ex) {
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //Method is used for printing cancel kot items
                            printCancelKotTicket(m_oTicket, m_oTicket.getLine(i), m_oTicketExt, printerInfo);
                            //Removing the lines from ticketlines
                            removeTicketLine(i);
                            //adding the logic of deleting the addon items
                            if ((addonVal != null && primaryAddon == 1) || (addonVal != null && comboAddon == 1)) {
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
                            try {
                                if (promoType.equals("SIBG") && leastValuePromo == true && isPromoSku.equals("N")) {
                                    checkLeastValuePromotion();
                                }
                            } catch (Exception e) {
                            }
                            m_oTicket.refreshTxtFields(0);
                            if (m_oTicket.getLinesCount() == 0) {
                                m_oTicket.setPromoAction(false);
                                m_jTxtChange.setText("");
                                m_jTaxesEuros1.setText("Rs. 0.00");
                                m_jTotalEuros.setText("Rs. 0.00");
                                refreshTicket();
                                taxModel.removeAllElements();
                            }
                        }
                    }
                } else {
                    showMessage(this, "Item is sent to kot.Only Cashier or Manager can cancel the line.");
                }
            } else {
                removeTicketLine(i);
                if ((addonVal != null && primaryAddon == 1) || (addonVal != null && comboAddon == 1)) {
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
                try {
                    System.out.println("inside here>");
                    if (promoType.equals("SIBG") && leastValuePromo == true && isPromoSku.equals("N")) {
                        checkLeastValuePromotion();
                    }
                } catch (Exception e) {
                }
                m_oTicket.refreshTxtFields(0);
                if (m_oTicket.getLinesCount() == 0) {
                    m_oTicket.setPromoAction(false);
                    m_jTxtChange.setText("");
                    m_jTaxesEuros1.setText("Rs. 0.00");
                    m_jTotalEuros.setText("Rs. 0.00");
                    refreshTicket();
                    taxModel.removeAllElements();
                }

                // Date updated = new Date();
                Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                try {
                    new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?, UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    if (m_oTicket.getTakeaway().equals("Y")) {
//                                        new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                String splitId = m_oTicket.getSplitSharedId();
                Object[] record;
                try {
                    record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                    if (record != null) {
                        m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                    }
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
        }
        m_jTxtItemCode.setText("");
        itemName.setText("");
        //  }
    }//GEN-LAST:event_m_jDeleteActionPerformed
    //Method is called for print cancel kot lines

    private synchronized void printCancelKotTicket(RetailTicketInfo ticket, RetailTicketLineInfo kotLine, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo) {
        java.util.List<TicketLineConstructor> allLines = null;
        String productionAreaName = "";
        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        logger.info("cancel  kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        com.openbravo.pos.printer.printer.KotBillPrinter printerKot = new KotBillPrinter();
        for (int j = 0; j < printerInfo.size(); j++) {
            if (printerInfo.get(j).getProductionAreaType().equals(kotLine.getProductionAreaType())) {
                productionAreaName = printerInfo.get(j).getName();
                allLines = getCancelKotLines(ticket, ticketExt, kotLine, productionAreaName);
                try {
                    if (storeLocation.equals("BlrIndranagar")) {
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
    //This method is called for setting the cancel kot lines for printing

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
    //Action performed method is called when click on plus button in billing screen and is used for increasing the quantity of the selected item
    private void m_jPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPlusActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        if (newline.getComboAddon() == 0) {
            //Checking whether the selected item is non kot item
            if (m_oTicket.getLine(i).getIsKot() != 1) {
                //Checking whether the selected item having SIBG promotion.If promotion is applicable, then it will increase the free item quantity also
                if (m_oTicket.getLine(i).getPromoType().equals("SIBG") && m_oTicket.getLine(i).getIsPromoSku().equals("Y") && m_oTicket.getLine(i).isPromoAction()) {
                    if (m_oTicket.getLine(i).getActualPrice() != 0) {
                        newline.setMultiply(newline.getMultiply() + 1.0);
                        int index = i + 1;
                        m_oTicket.getLine(index).setMultiply(m_oTicket.getLine(index).getMultiply() + 1);
                        m_oTicket.getLine(index).setOfferDiscount(m_oTicket.getLine(index).getPrice() * m_oTicket.getLine(index).getMultiply());
                    }

                } else {
                    //Increasing the quantity of the selected item
                    newline.setMultiply(newline.getMultiply() + 1.0);
                }
                //Setting the changes in ticket lines
                paintTicketLine(i, newline);
            }

        }
    }//GEN-LAST:event_m_jPlusActionPerformed
    //Action performed method is called when click on minus button in billing screen
    private void m_jMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jMinusActionPerformed
        int i = m_ticketlines.getSelectedIndex();
        RetailTicketLineInfo newline = new RetailTicketLineInfo(m_oTicket.getLine(i));
        //Checking whether product quantity is greater than one, then only system will allow to decrease the quantity
        if (newline.getMultiply() > 1.0 && newline.getComboAddon() == 0) {
            String addonVal = m_oTicket.getLine(i).getAddonId();
            int primaryAddon = m_oTicket.getLine(i).getPrimaryAddon();
            String selectedProduct = m_oTicket.getLine(i).getProductID();
            int kotStatus = m_oTicket.getLine(i).getIsKot();
            //Checking whether the selected item is kot item
            if (kotStatus != 0) {
                //Checking whether the role is admin or cashier. Only Admin or Cashier can cancel the item
                if (roleName.equals("Admin") || roleName.equals("Cashier")) {
                    //For adding the cancel reason
                    boolean billUpdated = JReasonEditor.showMessage(this, dlReceipts, m_oTicket, selectedProduct, i, "lineMinus");

                    if (billUpdated) {
                        // showMessage(this, "The Table is being accessed by another User!Try after sometime");
                        RetailTicketInfo dbticket = null;
                        try {
                            dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                        } catch (BasicException ex) {
                            logger.info("Exception while selecting the dbticket on cancel item action ");
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (dbticket != null) {
                            //      String  dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                            // dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
                            dbticket.setObjectUpdateDate(JReasonEditor.dbUpdatedDate);
                            for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                                if (m_oTicket.getLine(index).getIsKot() == 0) {
                                    m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                                    dbticket.addLine(m_oTicket.getLine(index));
                                }
                            }
                            JRetailBufferWindow.showMessage(this);
                            logger.info("This Bill is updated one.Loading.......");
                            setRetailActiveTicket(dbticket, m_oTicketExt);
                            dbUpdatedDate = null;
                            kotaction = 1;
                        } else {
                            showMessage(this, "This Bill is no longer exist");
                            logger.info("This Bill is no longer exists");
                            m_ticketsbag.activate();
                        }
                    } else {

                        if (JReasonEditor.getCancel() == true) {
                            //Checking whether the selected item having SIBG promotion.If promotion is applicable, then it will decrease the free item quantity also
                            if (m_oTicket.getLine(i).getPromoType().equals("SIBG") && m_oTicket.getLine(i).getIsPromoSku().equals("Y") && m_oTicket.getLine(i).isPromoAction()) {
                                if (m_oTicket.getLine(i).getActualPrice() != 0) {
                                    newline.setMultiply(newline.getMultiply() - 1.0);
                                    int index = i + 1;
                                    m_oTicket.getLine(index).setMultiply(m_oTicket.getLine(index).getMultiply() - 1);
                                    m_oTicket.getLine(index).setOfferDiscount(m_oTicket.getLine(index).getPrice() * (m_oTicket.getLine(index).getMultiply()));
                                }
                            } else {//Decreasing the quantity of the selected item
                                newline.setMultiply(newline.getMultiply() - 1.0);
                            }
                            if (addonVal != null && primaryAddon == 1) {
                                int j = 0;
                                while (j < m_oTicket.getLinesCount()) {
                                    //if its addon item
                                    if (addonVal.equals(m_oTicket.getLine(j).getAddonId()) && m_oTicket.getLine(j).getPrimaryAddon() == 0) {
                                        removeTicketLine(j);
                                        j = 0;
                                    } else {
                                        j++;
                                    }
                                }
                            }

                            // Date updated = new Date();
                            Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                            try {
                                try {
                                    new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    if (m_oTicket.getTakeaway().equals("Y")) {
//                                        new PreparedSentence(m_App.getSession(), "UPDATE TAKEAWAYTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
//                                    }
                                } catch (BasicException ex) {
                                    logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  MINUS action updating shared ticket" + ex.getMessage());
                                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                String splitId = m_oTicket.getSplitSharedId();
                                Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                                if (record != null) {
                                    m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                                }
                            } catch (BasicException ex) {
                                logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  printRetailKotTicket updating shared ticket" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                } else {
                    showMessage(this, "Item is sent to kot.Only Cashier or Manager can cancel the line.");
                }
            } else { //Checking whether the selected item having SIBG promotion.If promotion is applicable, then it will decrease the free item quantity also
                if (m_oTicket.getLine(i).getPromoType().equals("SIBG") && m_oTicket.getLine(i).getIsPromoSku().equals("Y") && m_oTicket.getLine(i).isPromoAction()) {
                    if (m_oTicket.getLine(i).getActualPrice() != 0) {
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        int index = i + 1;
                        m_oTicket.getLine(index).setMultiply(m_oTicket.getLine(index).getMultiply() - 1);
                        System.out.println("m_oTicket.getLine(index).getPrice()" + m_oTicket.getLine(index).getPrice() + "m_oTicket.getLine(index).getMultiply()" + m_oTicket.getLine(index).getMultiply());
                        m_oTicket.getLine(index).setOfferDiscount(m_oTicket.getLine(index).getPrice() * (m_oTicket.getLine(index).getMultiply()));
                    }
                } else {//Decreasing the quantity of the selected item
                    newline.setMultiply(newline.getMultiply() - 1.0);
                }

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
                //Setting the changes in ticket lines
                paintTicketLine(i, newline);
            }
        }
    }//GEN-LAST:event_m_jMinusActionPerformed
    //Action Performed method is called when click on edit line button in sales screen
    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
        int i = m_ticketlines.getSelectedIndex();
        RetailTicketLineInfo newline = null;
        //Checking whether the item is non kot item. If it is zero means non kot
        if (m_oTicket.getLine(i).getIsKot() == 0) {
            if (i < 0) {
                Toolkit.getDefaultToolkit().beep(); // no line selected
            } else {
                try {

                    //add promotion logic
                    //Checking whether the selected item having SIBG promotion.If promotion is applicable, then it will edit the free item quantity also
                    if (m_oTicket.getLine(i).getPromoType().equals("SIBG") && m_oTicket.getLine(i).getIsPromoSku().equals("Y") && buyGetPromo) {
                        if (m_oTicket.getLine(i).getActualPrice() != 0) {
                            //Opening the popup screen for editing the quantity
                            newline = JRetailProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
                            int index = i + 1;
                            m_oTicket.getLine(index).setMultiply(newline.getMultiply());
                        }
                    } else {
                        //Opening the popup screen for editing the quantity
                        newline = JRetailProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
                    }
                    if (newline != null) {
                        //Setting the changes in ticket lines
                        paintTicketLine(i, newline);
                    }
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                }
            }
        }
    }//GEN-LAST:event_m_jEditLineActionPerformed
    //Action Performed method is called when click on logout button in sales screen
    private void m_jLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jLogoutActionPerformed
        logger.info("Start Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        getActiveTicket().setTicketOpen(false);
        try {
            dlReceipts.updateSharedTicket(getActiveTicket().getPlaceId(), getActiveTicket());
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  logout updating shared ticket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_RootApp = (JRootApp) m_App;
        //Method is used for closing the application panel
        m_RootApp.closeAppView();
        logger.info("End Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));


}//GEN-LAST:event_m_jLogoutActionPerformed

    private void m_jCalculatePromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCalculatePromotionActionPerformed
        String promotionName = setPromotionData();
        if (promotionName.equals("BuyGet")) {
            buyGetPromo = true;
            m_oTicket.setPromoAction(true);
        } else {
            checkLeastValuePromotion();
        }
}//GEN-LAST:event_m_jCalculatePromotionActionPerformed

    private void calculateLeastValueDiscount(ArrayList<BuyGetPriceInfo> pdtLeastPriceList1) {
        for (final RetailTicketLineInfo l : m_oTicket.getLines()) {
            System.out.println("pdtBuyGetPriceLis--------checkvalue---" + pdtBuyGetPriceList.size() + "===" + pdtBuyGetPriceList);
            if (pdtBuyGetPriceList.size() != 0) {
                for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {

                    String productId = pdtBuyGetPriceList.get(i).getProductID();
                    if (l.getProductID().equals(productId)) {
                        pdtBuyGetPriceList.remove(i);
                        l.setPromodiscountPercent(100);
                        l.setPromoType("SIBG");
                        l.setOfferDiscount(l.getPrice());
                        break;
                    }

                }
            } else {
                System.out.println("enetre ----- sibg----");
                if (l.getPromoType().equals("SIBG") && l.getIsPromoSku().equals("N")) {
                    l.setPromodiscountPercent(0);
                    l.setPromoType("SIBG");
                    l.setOfferDiscount(0);
                }
            }
        }
    }

    public void checkLeastValuePromotion() {
        leastValuePromo = true;
        ArrayList<BuyGetPriceInfo> leastProductList = new ArrayList<BuyGetPriceInfo>();
        StringBuilder b = new StringBuilder();
        StringBuilder b1 = new StringBuilder();


        //Least value promotion
        try {
            dlSales.deleteTempTicketlines();
            // System.out.println("m_oTicket----" + m_oTicket.getLine(0).getCampaignId());
            dlSales.saveRetailTempTicketlines(m_oTicket);
            java.util.ArrayList<String> campaignId = new ArrayList<String>();
            // m_oTicket.setLeastValueDiscount(0.00);
            pdtCampaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getPdtCampaignId();
            for (int i = 0; i < pdtCampaignIdList.size(); i++) {
                campaignId.add("'" + pdtCampaignIdList.get(i).getcampaignId() + "'");
            }

            Iterator<?> it = campaignId.iterator();
            while (it.hasNext()) {
                b.append(it.next());
                if (it.hasNext()) {
                    b.append(',');
                }
            }
            String promoCampaignId = b.toString();
            if (!promoCampaignId.equals("")) {
                double price = 0;
                double taxAmount = 0;
                double serviceTaxAmount = 0;
                double sbtaxAmount = 0;

                pdtBuyGetList = (ArrayList<BuyGetInfo>) dlSales.getbuyGetTotalQty(promoCampaignId);

                for (int i = 0; i < pdtBuyGetList.size(); i++) {

                    pdtBuyGetQtyList = (ArrayList<BuyGetQtyInfo>) dlSales.getbuyGetQty(pdtBuyGetList.get(i).getCampaignId(), pdtBuyGetList.get(i).getQty());
                    int sumOfBuyGet = (int) pdtBuyGetQtyList.get(0).getBuyQty() + (int) pdtBuyGetQtyList.get(0).getQty();
                    int remaining = 0;
                    remaining = pdtBuyGetList.get(0).getQty() / (int) sumOfBuyGet;
                    int qty = remaining * pdtBuyGetQtyList.get(0).getQty();
                    if (pdtBuyGetQtyList.size() != 0) {
                        pdtBuyGetPriceList = (ArrayList<BuyGetPriceInfo>) dlSales.getbuyGetLeastPrice(pdtBuyGetList.get(i).getCampaignId(), qty);
                        pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                        for (int j = 0; j < pdtBuyGetPriceList.size(); j++) {
                            price = price + pdtBuyGetPriceList.get(j).getPrice();
//                            taxAmount = taxAmount + pdtBuyGetPriceList.get(j).getTaxRate();
//                            serviceTaxAmount = serviceTaxAmount + pdtBuyGetPriceList.get(j).getServiceTax();
//                            sbtaxAmount = sbtaxAmount + pdtBuyGetPriceList.get(j).getSbtax();
                            if (pdtLeastPriceList.size() == 0) {
                                pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                            } else {
                                boolean flag = false;
                                for (int k = 0; k < pdtLeastPriceList.size(); k++) {
                                    if (pdtLeastPriceList.get(k).getProductID() == pdtBuyGetPriceList.get(j).getProductID()) {
                                        BuyGetPriceInfo info = pdtLeastPriceList.get(k);
                                        info.setQuantity((info.getQuantity() + 1));
                                        pdtLeastPriceList.remove(k);
                                        pdtLeastPriceList.add(info);
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag == false) {
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(j));
                                }
                            }
                        }
                        leastProductList.addAll(pdtLeastPriceList);
                    }

                }
                setPriceInfo(leastProductList);
                //    m_oTicket.setLeastValueDiscount(price);
//                m_oTicket.setTaxValue(taxAmount);
//                m_oTicket.setServiceTaxLeastAmount(serviceTaxAmount);
//                m_oTicket.setSbTaxLeastAmount(sbtaxAmount);
                calculateLeastValueDiscount(pdtLeastPriceList);
                m_oTicket.setPromoAction(true);
                m_oTicket.refreshTxtFields(1);
            }
            //  m_oTicket.billValuePromotion(promoRuleIdList,dlSales);
            printPartialTotals();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }
    //Action Performed method is called when click on bill discount button in sales screen
    private void m_jBtnDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDiscountActionPerformed

        if (m_oTicket.getLinesCount() != 0) {
            try {
                logger.info("Start Discount Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                //Fetching from property file to know the discount to be applied from line level or bill level
                String categoryDiscount = m_App.getProperties().getProperty("machine.categorydiscount");
                String user = m_oTicket.getUser().getName();
                String type = "sales";
                String role = dlCustomers.getRolebyName(user);
                //If it is bill level discount
                if (categoryDiscount.equals("false")) {
                    //Billwise Discount
                    m_oTicket.setCategoryDiscount(false);

                    if ("Cashier".equalsIgnoreCase(role)) {// for admin
                        //Popup box asking for login credentials for cashier user before applying the discount
                        boolean login = JDiscountAssign.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, m_oTicket);
                        if (login) {
                            m_oTicket.setdPerson(m_oTicket.getUser().getId());
                            JRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, role, type);
                        }
                    } else {
                        m_oTicket.setdPerson(m_oTicket.getUser().getId());
                        //Billwise Discount
                        //Popup box for applying the bill level discount
                        JRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, role, type);

                    }

                } else {
                    //Line level Discount
                    m_oTicket.setCategoryDiscount(true);
                    if ("Cashier".equalsIgnoreCase(role)) {// for admin
                        boolean login = JDiscountAssign.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, m_oTicket);
                        if (login) {
                            m_oTicket.setdPerson(m_oTicket.getUser().getId());
                            //Linewise Discount
                            java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
                            Map<String, DiscountInfo> dRateMap = new HashMap();
                            for (DiscountRateinfo dis : list) {
                                dRateMap.put(dis.getName(), new DiscountInfo(dis.getRate(), "", dis.getId()));
                            }
                            //Popup box for applying the line level discount
                            discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, "", list, dRateMap);
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
                        discountMap = JLineDiscountRateEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, "", list, dRateMap);
                        m_oTicket.setDiscountMap(discountMap);
                        if (discountMap != null) {
                            populateDiscount(discountMap);
                        }
                    }
                }
                populateTaxList();
                m_oTicket.refreshTxtFields(1);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_m_jBtnDiscountActionPerformed

    private void m_jActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jActionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jActionActionPerformed

    //Method is called when click on kot button, setting all the non kot lines based on
    //production area and session and sending the lines for printing
    public final synchronized void kotDisplay() throws BasicException {
        //   System.out.println("ticket.setObjectUpdateDate KOT1  : " + m_oTicket.getObjectUpdateDate());

        boolean updated = checkTicketUpdation();
        System.out.println("updated " + updated);

        if (!updated) {
            dbUpdatedDate = null;
            kotprintIssue = 0;
            logger.info("kotaction value" + kotaction);
            int orderId = 0;
            if (m_oTicket.getOrderId() == 0) {
                if (m_oTicket.getLinesCount() != 0) {
                    //Setting the new order no
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

            }

            RetailTicketInfo info = getActiveTicket();
            java.util.List<kotPrintedInfo> kPrintedInfolist = null;
            //Getting all the lines from current bill
            final java.util.List<RetailTicketLineInfo> panelLines = info.getLines();
            final java.util.List<RetailTicketLineInfo> panelNonKotLines = new ArrayList();

            String sessionId = null;
            String floorName = null;
            java.util.List<Floor> m_afloors;
            //Fetch the floor id and based on floor id get all the printer details

            SentenceList sent = new StaticSentence(
                    m_App.getSession(),
                    "SELECT FLOORS.ID, FLOORS.NAME,'' FROM PLACES LEFT JOIN FLOORS ON PLACES.FLOOR=FLOORS.ID WHERE  PLACES.ID='" + m_oTicket.getPlaceId() + "'",
                    null,
                    new SerializerReadClass(Floor.class));
            m_afloors = sent.list();
            //  sessionId = dlReceipts.getFloorId(m_oTicket.getPlaceId());
            sessionId = m_afloors.get(0).getID();
            floorName = m_afloors.get(0).getName();
            printerInfo = dlReceipts.getPrinterInfo(sessionId);
            for (int i = 0; i < panelLines.size(); i++) {
                //Add all the non kot lines to the list for printing the kot based on production area
                if (panelLines.get(i).getIsKot() == 0) {
                    String tbl_orderitemId = UUID.randomUUID().toString();
                    tbl_orderitemId = tbl_orderitemId.replaceAll("-", "");
                    panelLines.get(i).setTbl_orderId(tbl_orderitemId);
                    panelLines.get(i).setKotid(kotTicket);
                    panelLines.get(i).setKotdate(m_oTicket.getDate());
                    panelLines.get(i).setKottable(m_oTicket.getPlaceId());
                    panelLines.get(i).setKotuser(m_oTicket.getUser().getId());
                    panelNonKotLines.add(panelLines.get(i));
                }

            }
            //Method is used for print the non kot lines
            printRetailKotTicket("Printer.Kot", m_oTicket, panelNonKotLines, m_oTicketExt, printerInfo, kotTicket, floorName);
            Runtime.getRuntime().gc();

        }
    }
    //This method is called when click on move table button for sending the move table details to kot printers

    public final synchronized void kotMoveTableDisplay() throws BasicException {

        RetailTicketInfo info = getActiveTicket();
        final java.util.List<RetailTicketLineInfo> panelLines = info.getLines();
        String sessionId = null;
        sessionId = dlReceipts.getFloorId(m_oTicket.getPlaceId());
        printerInfo = dlReceipts.getPrinterInfo(sessionId);
        printRetailMoveTableTicket(m_oTicket, panelLines, m_oTicketExt, printerInfo);
        Runtime.getRuntime().gc();
    }
    //Method is called for printing the kot items

    private synchronized void printRetailKotTicket(String sresourcename, RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> kot, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo, int kotTicket, String floorName) {
        java.util.List<TicketLineConstructor> allLines = null;
        logger.info("start printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        com.openbravo.pos.printer.printer.KotBillPrinter printerKot = new KotBillPrinter();

        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        kotTicketlist = kot;
        //iterate the printers with non kot items
        for (int j = 0; j < printerInfo.size(); j++) {
            // list for saving the items having same production area type
            java.util.List<RetailTicketLineInfo> uniqueProductionAreas = new ArrayList<RetailTicketLineInfo>();
            for (int i = 0; i < kotTicketlist.size(); i++) {
                // if printer production area type matches with item production area type , add the line to the list
                if (printerInfo.get(j).getProductionAreaType().equals(kotTicketlist.get(i).getProductionAreaType())) {
                    uniqueProductionAreas.add(kotTicketlist.get(i));
                    kotTicketlist.get(i).setProductionArea(printerInfo.get(j).getProductionArea());
                }
            }
            logger.info("kot print count based on production areas" + uniqueProductionAreas.size());
            if (uniqueProductionAreas.size() != 0) {
                allLines = getRetailAllLines(ticket, ticketExt, uniqueProductionAreas, kotTicket, floorName);
                try {
                    // sending for kot print based on store
                    if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("BlrKoramangala") || storeLocation.equals("Chennai") || storeLocation.equals("Hyderabad")) {
                        //Thermal ?
                        printer.printKot(allLines, printerInfo.get(j).getPath());
                    } else {
                        printerKot.print(allLines, printerInfo.get(j).getPath());
                    }
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        kotlogger.info("KOT Printed Successfully " + "," + "Username: " + m_oTicket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + m_oTicketExt.toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(1);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(4);
                        }

                    }
                } catch (PrinterException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + " The printer action" + ex.getMessage());
                    kotprintIssue = 1;
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
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
        // Date updated = new Date();
        Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
        try {
            try {
                new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?,UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);

            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  updating shared ticket" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
            String splitId = m_oTicket.getSplitSharedId();
            Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
            if (record != null) {

                m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + " exception in  printRetailKotTicket updating shared ticket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("kot lines passing to print" + kotTicketlist.size());
        logger.info("end printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }
    //Method is called for printing move table details when click on move table button

    private synchronized void printRetailMoveTableTicket(RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> kot, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo) {
        java.util.List<TicketLineConstructor> allLines = null;
        logger.info("start printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        kotTicketlist = kot;
        for (int j = 0; j < printerInfo.size(); j++) {
            java.util.List<RetailTicketLineInfo> uniqueProductionAreas = new ArrayList<RetailTicketLineInfo>();
            for (int i = 0; i < kotTicketlist.size(); i++) {
                if (printerInfo.get(j).getProductionAreaType().equals(kotTicketlist.get(i).getProductionAreaType())) {
                    uniqueProductionAreas.add(kotTicketlist.get(i));
                }
            }
            logger.info("kot print count based on production areas" + uniqueProductionAreas.size());

            if (uniqueProductionAreas.size() != 0) {
                allLines = getMoveTableLines(ticket, ticketExt, uniqueProductionAreas);
                try {
                    printer.printKot(allLines, printerInfo.get(j).getPath());
                } catch (PrinterException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + " The printer action" + ex.getMessage());
                    showMessage(this, "Print Unsucessfull! Please retry");
                }
            }

        }

    }
    //This method is called for aligning and setting the kot lines for printing

    private java.util.List<TicketLineConstructor> getRetailAllLines(RetailTicketInfo ticket, Object ticketext, java.util.List<RetailTicketLineInfo> kot, int kotTicket, String floorName) {

        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        if ("Habanero-Jarota".equals(m_App.getProperties().getStoreName()) || "Habanero-Goa".equals(m_App.getProperties().getStoreName())) {
            allLines.add(new TicketLineConstructor("         " + floorName.toUpperCase()));
        } else {
            allLines.add(new TicketLineConstructor("          KITCHEN ORDER TICKET"));
        }
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(13 - (ticket.printDate().substring(0, 12).length())) + "Kot No: " + kotTicket));
        allLines.add(new TicketLineConstructor("Time: " + (ticket.printTime()) + getSpaces(13 - (ticket.printTime().length())) + "Table No:" + getSpaces(1) + ticket.getName(m_oTicketExt)));
        allLines.add(new TicketLineConstructor("User Name: " + (m_oTicket.getUser()).getName()));
        allLines.add(new TicketLineConstructor(getDottedLine(40)));
        for (RetailTicketLineInfo tLine : kot) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            qtySum = qtySum + tLine.getMultiply();
            String instruction = tLine.printInstruction();
            if (prodName.length() > 28) {
                prodName = WordUtils.wrap(prodName, 28);
                String[] prodNameArray = prodName.split("\n");
                for (int i = 0; i < prodNameArray.length - 1; i++) {
                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
                }

                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(30 - prodNameArray[prodNameArray.length - 1].length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        // each instruction will be added/differentiated with the ';'
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            } else {
                allLines.add(new TicketLineConstructor(prodName + getSpaces(30 - prodName.length()) + qty));
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
        allLines.add(new TicketLineConstructor("  "));
        allLines.add(new TicketLineConstructor("  "));
        allLines.add(new TicketLineConstructor(" "));
        allLines.add(new TicketLineConstructor(" "));
        allLines.add(new TicketLineConstructor("------------------ "));
        logger.info("sum of kot quantities = " + qtySum);
        return allLines;
    }
    //This method is called for aligning and setting move table details for printing

    private java.util.List<TicketLineConstructor> getMoveTableLines(RetailTicketInfo ticket, Object ticketext, java.util.List<RetailTicketLineInfo> kot) {

        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        allLines.add(new TicketLineConstructor("          Move Table Ticket"));
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(16 - (ticket.printDate().substring(0, 12).length())) + "Time: " + (ticket.printTime())));
        allLines.add(new TicketLineConstructor("Table " + ticket.getOldTableName() + " is moved to table " + ticket.getTableName()));
        allLines.add(new TicketLineConstructor(getDottedLine(70)));
        return allLines;
    }
    //Action performed method is used for removing all the lines from bill screen
    private void m_jEraserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEraserActionPerformed
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
            m_oTicket.refreshTxtFields(0);
        }
        kotaction = 0;
        //  } 
    }//GEN-LAST:event_m_jEraserActionPerformed
    //Action Performed method is called when click on print bill button
    private void m_jbtnPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnPrintBillActionPerformed
        logger.info("Start Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        try {
            if (m_oTicket.getLinesCount() != 0) {
                boolean updated = checkTicketUpdation();
                if (!updated) {
                    dbUpdatedDate = null;
                    //if exist non kot items
                    if (kotaction == 1) {
                        //Method is used for printing non kot items
                        kotDisplay();
                    } else {
                        Runtime.getRuntime().gc();
                    }
                    //Method is for setting all the lines to be served status
                    serveAllLines();
                    if (kotprintIssue == 0) {
                        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaPrint"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (res == JOptionPane.YES_OPTION) {
                            boolean reasonStatus = true;
                            try {
                                logger.info("Start Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                                logger.info("No. of Line Items during Print Bill : " + m_oTicket.getLinesCount());
                                reasonStatus = doPrintValidation();
                            } catch (BasicException ex) {
                                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill doPrintValidation" + ex.getMessage());
                                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (reasonStatus) {
                                String file;
                                file = "Printer.Bill";
                                m_jLblBillNo.setText(m_oTicket.getTicketId() == 0 ? "--" : String.valueOf(m_oTicket.getTicketId()));
                                try {

                                    taxeslogic.calculateTaxes(m_oTicket);

                                } catch (TaxesException ex) {
                                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill calculateTaxes" + ex.getMessage());
                                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //Based on store location
                                if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("ITPL")) {
                                    //Method is used for generic text printer
                                    printTicketGeneric(file, m_oTicket, m_oTicketExt);
                                } else {
                                    //Method is used for thermal printer
                                    printTicket(file, m_oTicket, m_oTicketExt);
                                }
                                logger.info("bill has been printed");
                                m_oTicket.setPrinted(true);
                                Object[] values = new Object[]{m_oTicket.getPlaceId(), m_oTicket.getName(), m_oTicket, m_oTicket.getSplitSharedId(), m_oTicket.isPrinted(), m_oTicket.isListModified()};
                                Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.SERIALIZABLE, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN};
                                new PreparedSentence(m_App.getSession(), "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ?, ISPRINTED = ?, ISMODIFIED = ?, UPDATED=NOW()  WHERE ID = ? AND SPLITID=? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 4, 5, 0, 3})).exec(values);
                                String splitId = m_oTicket.getSplitSharedId();
                                Object[] record = (Object[]) new StaticSentence(m_App.getSession(), "SELECT UPDATED FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(m_oTicket.getPlaceId());
                                if (record != null) {
                                    m_oTicket.setObjectUpdateDate(DateFormats.StringToDateTime((String) record[0]));

                                }
                                logger.info("End Printing :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                            }
                        }
                    }
                }
            }
            if (kotprintIssue == 0) {
                if (roleName.equals("Bartender")) {
                    logger.info("Role Bartender");
                    IsSteward = 1;
                    JRetailTicketsBagRestaurant.setNewTicket();
                    m_RootApp = (JRootApp) m_App;
                    m_RootApp.closeAppView();
                }
            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking print bill" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("End Print Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jbtnPrintBillActionPerformed
    //Action Performed method is called when click on settle bill button
    private void m_jSettleBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSettleBillActionPerformed

        logger.info("m_jSettleBillActionPerformed");
        logger.info("Start Settle Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        //Below code is used for closing the day automatically if the application is not closed yesterday
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
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Comparing the actual close day date and current active close day date. if its greater than 0, then close the day automatically
        if (closeDate.compareTo(activeDayDate) > 0) {
            m_RootApp.closeDay();
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date());
            String accDate = new SimpleDateFormat("yyyy-MM-dd").format(m_App.getActiveDayDateStart());
            Date dateValue = java.sql.Date.valueOf(accDate);
            m_oTicket.setAccountDate(dateValue);
        }
        if (m_oTicket.getLinesCount() == 0) {
            return;
        }
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            //Checking whether non kot items is there in the bill
            if (kotaction == 1) {
                try {
                    kotDisplay();
                } catch (BasicException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking settle bill kot action" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Checking whether the bill is printed
            if (!m_oTicket.isPrinted()) {
                showMessage(this, "Settle bill is allowed after Bill is Printed");
                logger.info("Settle bill is allowed after Bill is Printed");
                return;
                //Checking whether bill is modified after the print
            } else if (m_oTicket.isPrinted() && m_oTicket.isListModified()) {
                showMessage(this, "Bill is Modified after previous print. So please print it again");
                logger.info("Bill is Modified after previous print. So please print it again");
                return;
            }
            JPaymentEditor.showMessage(JRetailPanelTicket.this, dlReceipts, m_oTicket, this, "Sales");
        }
}//GEN-LAST:event_m_jSettleBillActionPerformed
    //Method is used for splitting the bill

    public void splitBill() {
        int splitfunction = 0;
        if (m_oTicket.getLinesCount() > 0) {
            splitfunction = 1;
            //if linescount=1 and line count greater than 1 then allow split bill
            if (m_oTicket.getLinesCount() == 1) {
                if (m_oTicket.getLine(0).getMultiply() > 1) {
                    splitfunction = 1;
                } else {
                    splitfunction = 0;
                }
            } //if linescount greater than 1 then allow to print
            else {
                splitfunction = 1;
            }
        }
        if (splitfunction == 1) {
            logger.info("splitfunction =1");
            //Open the dialog box for spliting the bill
            RetailReceiptSplit splitdialog = RetailReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlSales, dlCustomers, taxeslogic);
            //Copy the existing bill parameters to two splitted bills
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
                boolean updated = checkTicketUpdation();
                System.out.println("updated 222: " + updated);

                if (!updated) {
                    dbUpdatedDate = null;
                    try {
                        //Method is used for setting the order no for first bill
                        setKotAndServedOnSplit(ticket1);
                        if (splitdialog.window.equals("OK")) {
                            //Method is used for setting the order no for second bill
                            logger.info("splitfunction on  saying ok ");
                            setKotAndServedOnSplit(ticket2);
                        } //on clicking print button
                        else {
                            //Sending the second bill for printing
                            logger.info("splitfunction on saying print ");
                            m_oTicket = ticket2;
                            setKotServedAndPrintOnSplit(m_oTicket);
                        }
                        cancelTIcket.setSplitValue("Split");
                        String ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + cancelTIcket.getTicketId();
                        //Cancel the existing bill to db
                        dlSales.saveRetailCancelSplitTicket(cancelTIcket, m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), "Split Bill", "", m_App.getProperties().getPosNo(), "Y");
                    } catch (BasicException ex) {
                        logger.info("Order NO." + m_oTicket.getOrderId() + "exception while splitting the bill" + ex.getMessage());
                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    logger.info("cancelled splitted older bill ");
                    JRetailTicketsBagRestaurant.setNewSplitTicket(ticket1, ticket2);

                }

            }
        }


    }
    //Action Performed method is called when click on split bill button
    private void m_jSplitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSplitBtnActionPerformed
        logger.info("Start Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        logger.info("Split Bill Action");
        if (m_oTicket.getLinesCount() != 0) {
            try {
                boolean updated = checkTicketUpdation();
                System.out.println("updated " + updated);

                if (!updated) {
                    dbUpdatedDate = null;
                    //if exist non kot items
                    if (kotaction == 1) {
                        //Method is used for printing non kot items
                        kotDisplay();
                    }
                    if (kotprintIssue == 0) {
                        splitBill();
                    }
                }




            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking split bill" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.info("End Split Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jSplitBtnActionPerformed
    //Action Performed method is called when click on kot button
    private void m_jBtnKotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnKotActionPerformed
        try {
            dbUpdatedDate = null;
            logger.info("Start kot Button:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
            logger.info("No. of Line Items during Kot : " + m_oTicket.getLinesCount());
            if (m_oTicket.getLinesCount() != 0) {
                if (kotaction == 1) {
                    logger.info("kotaction is 1");
                    // System.out.println("ticket.setObjectUpdateDate KOT ACTION : " + m_oTicket.getObjectUpdateDate());
                    kotDisplay();
                }

                if (roleName.equals("Steward") || roleName.equals("Bartender")) {
                    logger.info("Start Logout Button : for Steward User" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                    IsSteward = 1;
                    if (kotprintIssue == 0) {
                        m_RootApp = (JRootApp) m_App;
                        m_RootApp.closeAppView();
                    }
                    logger.info("End Logout Button : for Steward User" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }

            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking kot button" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("End Kot Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnKotActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        resetSouthComponent("Category");

    }//GEN-LAST:event_jButton2ActionPerformed
    //Action Performed method is called when click on addon button
    private void jButtonAddonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddonActionPerformed

        try {
            //jLayeredPanel
            String id = null;
            int i = m_ticketlines.getSelectedIndex();
            String prName = m_oTicket.getLine(i).getProductID();
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
                ProductInfoExt productListDetails = RetailReceiptAddonList.showMessage(JRetailPanelTicket.this, dlCustomers, m_App, this, prName, menuId);
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
                    incProduct(productListval);
                }
            }
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking addon" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonAddonActionPerformed
    //Action Performed method is called when click on served button
    private void m_jBtnServedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnServedActionPerformed
        if (m_oTicket.getLinesCount() != 0) {
            try {
                int i = m_ticketlines.getSelectedIndex();
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
                    paintKotTicketLine(i, m_oTicket.getLine(i));// TODO add your handling code here:
                    setServedStatus(0);
                }
            } catch (BasicException ex) {
                logger.info("Order NO." + m_oTicket.getOrderId() + "exception on clicking served" + ex.getMessage());
                Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_m_jBtnServedActionPerformed

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
//        try {
//            stateTransition('\u00a7');
//        } catch (BasicException ex) {
//            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in m_jbtnScaleActionPerformed" + ex.getMessage());
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_m_jbtnScaleActionPerformed
    //Action Performed method is called when click on cancel bill button
    private void m_jBtnCancelBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancelBillActionPerformed
        boolean updated = checkTicketUpdation();
        if (!updated) {
            dbUpdatedDate = null;
            logger.info("Start Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
            int linesCount = m_oTicket.getLinesCount();
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                if (m_oTicket.getLine(i).getIsKot() == 1) {
                    JRetailTicketsBagRestaurant.clickCancel();
                    break;
                }
            }
        }
        logger.info("End Cancel Bill Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnCancelBillActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetSouthComponent("SubCategory");
    }//GEN-LAST:event_jButton3ActionPerformed
    //Action Performed method is called when click on bill on hold button
    private void m_jBtnBillOnHoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnBillOnHoldActionPerformed
        if (!m_oTicket.isPrinted()) {
            showMessage(this, "Hold bill is allowed after Bill is Printed");
            logger.info("Hold bill is allowed after Bill is Printed");
            return;
        } else if (m_oTicket.isPrinted() & m_oTicket.isListModified()) {
            showMessage(this, " Bill is Modified after previous print. So please print it again");
            logger.info(" Bill is Modified after previous print. So please print it again");
            return;
        } else {
            int res = JOptionPane.showConfirmDialog(this, "Do you want to hold the Bill", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                JRetailTicketsBagRestaurant.setHoldTicket(m_oTicket);
            }
        }

    }//GEN-LAST:event_m_jBtnBillOnHoldActionPerformed
    //Action Performed method is called when click on item code text field
    private void m_jTxtItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtItemCodeActionPerformed

        int index = m_jCboItemName.getSelectedIndex();//-1;
        String itemCode = null;
        itemCode = m_jTxtItemCode.getText();
        int itemCount = 0;
        try {
            itemCount = dlSales.getItemCount(itemCode);
        } catch (BasicException ex) {
            showMessage(this, "Please enter the valid item code");
            m_jTxtItemCode.setFocusable(true);
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

    }//GEN-LAST:event_m_jTxtItemCodeActionPerformed

    private void m_jCboItemNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_m_jCboItemNameItemStateChanged
        if (evt.getStateChange() == evt.SELECTED) {
        }// TODO add your handling code here:
    }//GEN-LAST:event_m_jCboItemNameItemStateChanged
    //Action Performed method is called when click on item name drop down
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
    //Method is called when click on split bill button

    private void setKotAndServedOnSplit(RetailTicketInfo splitTicket) {
        int orderId = 0;
        try {
            orderId = dlSales.getNextTicketOrderNumber();
        } catch (BasicException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
    //Method is called when click on split bill button

    private final synchronized void setKotServedAndPrintOnSplit(final RetailTicketInfo splitTicket) throws BasicException {

        Transaction t = new Transaction(m_App.getSession()) {
            @Override
            protected Object transact() throws BasicException {
                int orderId = 0;
                orderId = dlSales.getNextTicketOrderNumber();
                splitTicket.setOrderId(orderId);
                String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
                doPrintValidation();
                String file;
                file = "Printer.Bill";
                try {
                    taxeslogic.calculateTaxes(m_oTicket);
                    consolidateTaxes(m_oTicket);
                } catch (TaxesException ex) {
                    logger.info("Order NO." + m_oTicket.getOrderId() + "exception in setKotAndServedOnSplit calculateTaxes" + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("ITPL")) {
                    printTicketGeneric(file, m_oTicket, m_oTicketExt);
                } else {
                    printTicket(file, m_oTicket, m_oTicketExt);
                }
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
    //Method is called for printing kot lines for thermal printer

    private void printKotTicket(String sresourcename, RetailTicketInfo ticket, kotInfo kot, Object ticketExt, String printerName) {

        String sresource = dlSystem.getResourceAsXML(sresourcename);

        kot.setkotName("Kitchen Order Ticket");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JRetailPanelTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                script.put("kot", kot);
                script.put("place", ticketExt);
                String args[] = null;

                m_TTP.printTicket(script.eval(sresource).toString());

            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JRetailPanelTicket.this);
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
    //Method is called when click on print bill button for validating bill, setting the bill no and saving the invoice to database

    private synchronized boolean doPrintValidation() throws BasicException {  //determine suitable bill No for print
        boolean reasonStatus = true;
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
            //    dlSales.disableInvoiceTickets(m_oTicket.getTicketId()); //update existing record with inactive status
            reasonStatus = JRetailSecondprintReason.showMessage(this, dlSales, m_oTicket.getTicketId(), "sales");
            if (reasonStatus) {
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
        return reasonStatus;
    }

    //Method is called for saving the invoice to the database
    private void closeInvoiceTicket(RetailTicketInfo ticket) throws BasicException {

        try {

            taxeslogic.calculateTaxes(ticket);

        } catch (TaxesException ex) {
            logger.info("Order NO." + m_oTicket.getOrderId() + "exception in closeInvoiceTicket" + ex.getMessage());
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ticket.getTotal() >= 0.0) {
            ticket.resetPayments(); //Only reset if is sale
        }



        double creditAmt;
        creditAmt = 0;

        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
        ticket.setActiveCash(m_App.getActiveCashIndex());
        ticket.setDate(new Date()); // Le pongo la fecha de cobro

        String ticketDocNo = null;
        Integer ticketDocNoInt = null;
        String ticketDocument = null;

        if (ticket.getTicketType() == RetailTicketInfo.RECEIPT_NORMAL) {
            ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + ticket.getTicketId();
        }

        ticket.setDocumentNo(ticketDocument);
        String chequeNos = "";
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
        deliveryBoy = "";
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
    //Method is called when click on serve button for setting all the lines to be served status

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
            loadTicket();
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
//            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    //Method is called for enabling the all POS actions button based on user role

    public void enablePosActions() {

        try {
            posActions = (ArrayList<PosActionsInfo>) dlSales.getPosActions(m_App.getAppUserView().getUser().getRole());
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (posActions.size() != 0) {
            if (posActions.get(0).getDiscountAccess().equals("Y")) {
                m_jBtnDiscount.setVisible(true);
            } else {
                m_jBtnDiscount.setVisible(false);
            }
            if (posActions.get(0).getSplitAccess().equals("Y")) {
                m_jSplitBtn.setVisible(true);
            } else {
                m_jSplitBtn.setVisible(false);
            }
            if (posActions.get(0).getCancelAccess().equals("Y")) {
                m_jBtnCancelBill.setVisible(true);
            } else {
                m_jBtnCancelBill.setVisible(false);
            }
            if (posActions.get(0).getSettleAccess().equals("Y")) {
                m_jSettleBill.setVisible(true);
            } else {
                m_jSettleBill.setVisible(false);
            }
            if (posActions.get(0).getPrintAccess().equals("Y")) {
                m_jbtnPrintBill.setVisible(true);
            } else {
                m_jbtnPrintBill.setVisible(false);
            }
            if (posActions.get(0).getBillOnHoldAccess().equals("Y")) {
                m_jBtnBillOnHold.setVisible(true);
            } else {
                m_jBtnBillOnHold.setVisible(false);
            }
        } else {
            m_jBtnDiscount.setVisible(false);
            m_jbtnPrintBill.setVisible(false);
            m_jSettleBill.setVisible(false);
            m_jBtnCancelBill.setVisible(false);
            m_jSplitBtn.setVisible(false);
            m_jBtnBillOnHold.setVisible(false);
        }
    }

    public boolean checkTicketUpdation() {
        System.out.println("INSIDE TICKET UPDATION ");
        boolean updated = false;
        try {
            System.out.println("m_oTicket.getObjectUpdateDate() : " + m_oTicket.getObjectUpdateDate());
            String currentUpdated = m_dateformat.format(m_oTicket.getObjectUpdateDate()) + " " + m_dateformattime.format(m_oTicket.getObjectUpdateDate());
            System.out.println("my current updated date" + currentUpdated);
            String dbUpdated = dlReceipts.getUpdatedTime(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
            System.out.println("dbUpdated----" + dbUpdated);
            Date currentUpdatedDate = DateFormats.StringToDateTime(currentUpdated);
            dbUpdatedDate = DateFormats.StringToDateTime(dbUpdated);
            System.out.println("dbUpdatedDate : " + dbUpdatedDate + "currentUpdatedDate : " + currentUpdatedDate);
            //if the table is deleted by (settlement/cancel/move)
            if (dbUpdated.equals(null) || dbUpdated.equals("")) {
                logger.info("This Bill is no longer exist");
                showMessage(this, "This Bill is no longer exist");
                updated = true;
                m_ticketsbag.activate();
            }//if the table is updated
            else if (dbUpdatedDate.compareTo(currentUpdatedDate) > 0) {
                //Taking the updated sharedticket
                System.out.println("CURRENT -------");
                RetailTicketInfo dbticket = dlReceipts.getRetailSharedTicketSplit(m_oTicket.getPlaceId(), m_oTicket.getSplitSharedId());
                dbticket.setObjectUpdateDate(dbUpdatedDate);
                //assigning the discount percentage to fetched sharedticket from existing sharedticket (if applied)
                for (int index = 0; index < m_oTicket.getLinesCount(); index++) {
                    if (m_oTicket.getLine(index).getIsKot() == 0) {
                        m_oTicket.getLine(index).setDiscount(Double.parseDouble(dbticket.getRate()) * 100);
                        dbticket.addLine(m_oTicket.getLine(index));
                    }
                }
                //showing loading popup for 1 sec
                JRetailBufferWindow.showMessage(this);
                logger.info("This Bill is updated one.Loading.......");
                //setting the updated sharedticket to billing screen
                setRetailActiveTicket(dbticket, m_oTicketExt);
                dbUpdatedDate = null;
                //allowing to further kots
                kotaction = 1;
                updated = true;
            } else {
                updated = false;
            }

        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return updated;
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
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JLabel m_jTable;
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
