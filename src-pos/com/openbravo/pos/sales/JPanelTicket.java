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
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
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
import com.openbravo.pos.forms.PromoRuleIdInfo;
import com.openbravo.pos.forms.PromoRuleInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.util.JRPrinterAWT300;
import com.openbravo.pos.util.ReportUtils;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.print.PrintService;
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
public abstract class JPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, TicketsEditor {
   
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

    protected JTicketLines m_ticketlines;
        
    // private Template m_tempLine;
    private TicketParser m_TTP;
    
    protected TicketInfo m_oTicket; 
    protected Object m_oTicketExt; 
    
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
    public double billTotal;
    private JTicketsBag m_ticketsbag;
    
    private SentenceList senttax;
    private ListKeyed taxcollection;
    // private ComboBoxValModel m_TaxModel;
    
    private SentenceList senttaxcategories;
    private ListKeyed taxcategoriescollection;
    private ComboBoxValModel taxcategoriesmodel;
    
    private TaxesLogic taxeslogic;
    
//    private ScriptObject scriptobjinst;
    protected JPanelButtons m_jbtnconfig;
    
    protected AppView m_App;
    protected DataLogicSystem dlSystem;
    protected DataLogicSales dlSales;
    protected DataLogicCustomers dlCustomers;
    
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;
    public java.util.ArrayList<PromoRuleInfo> promoRuleList = null;
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
    double qty =0;
    int buttonPlus=1;
    public double productDiscount=0;
    String selectedProduct;
    public TicketInfo ticketValue;
    private ArrayList<PromoRuleIdInfo> promoRuleLeastList;
    public BuyGetPriceInfo buyGet;
    public String paymentMode;
    /** Creates new form JTicketView */
    public JPanelTicket() {
        
        initComponents ();
    }
   
    public void init(AppView app) throws BeanFactoryException {
        
        m_App = app;
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        
        // borramos el boton de bascula si no hay bascula conectada
        if (!m_App.getDeviceScale().existsScale()) {
            m_jbtnScale.setVisible(false);
        }
        
        m_ticketsbag = getJTicketsBag();
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.LINE_START);
        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);
        
        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);
               
        // Los botones configurables...
        m_jbtnconfig = new JPanelButtons("Ticket.Buttons", this);
        m_jButtonsExt.add(m_jbtnconfig);           
       m_jPanelBag.setVisible(false);
       btnCustomer.setVisible(false);
       m_jBtnCalculate.setVisible(false);
       catcontainer.setVisible(false);
       m_jButtonsExt.setVisible(false);
       m_jPor.setVisible(false);
        // El panel de los productos o de las lineas...        
        catcontainer.add(getSouthComponent(), BorderLayout.CENTER);
        
        // El modelo de impuestos
        senttax = dlSales.getTaxList();
        senttaxcategories = dlSales.getTaxCategoriesList();
        
        taxcategoriesmodel = new ComboBoxValModel();    
              
        // ponemos a cero el estado
        stateToZero();  
        
        // inicializamos
        m_oTicket = null;
        m_oTicketExt = null;
//        m_jBtnCalculate.setVisible(false);
    }
    
    public Object getBean() {
        return this;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public void activate(AppView view, String total, String billNo) throws BasicException {
        this.m_App = view;
        double billValue = Double.parseDouble(total);
        this.billTotal = billValue;
        m_jBillDiscount.setText(null);
        m_jTicketId.setText(billNo);

        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this);
        paymentdialogrefund.init(m_App);

        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = senttax.list();
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = senttaxcategories.list();
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

        taxeslogic = new TaxesLogic(taxlist);

        // Show taxes options
        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }
       // jLabel2.setVisible(false);
        // Authorization for buttons
//        btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());

        m_ticketsbag.activate();
        String day = getWeekDay();
        java.util.ArrayList<String> campaignId = new ArrayList<String>();

        campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
        promoDetails = new PromoRuleInfo();
        if(campaignIdList.size()!=0){
        for(int i=0;i<campaignIdList.size();i++){
         campaignId.add(campaignIdList.get(i).getcampaignId());
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
             promoRuleIdList  = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);

        //     ticketLine.setPromotionRule(promoRuleIdList);

        }
        }
    }


    public void activate() throws BasicException {

         m_jBillDiscount.setText(null);
    
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this); 
        paymentdialogrefund.init(m_App);
        
        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = senttax.list();
        taxcollection = new ListKeyed<TaxInfo>(taxlist);
        java.util.List<TaxCategoryInfo> taxcategorieslist = senttaxcategories.list();
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
                
        taxeslogic = new TaxesLogic(taxlist);
        
        // Show taxes options
        if (m_App.getAppUserView().getUser().hasPermission("sales.ChangeTaxOptions")) {
            m_jTax.setVisible(true);
            m_jaddtax.setVisible(true);
        } else {
            m_jTax.setVisible(false);
            m_jaddtax.setVisible(false);
        }
       // jLabel2.setVisible(false);
        // Authorization for buttons
//        btnSplit.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setMinusEnabled(m_App.getAppUserView().getUser().hasPermission("sales.EditLines"));
        m_jNumberKeys.setEqualsEnabled(m_App.getAppUserView().getUser().hasPermission("sales.Total"));
        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());  
               
        m_ticketsbag.activate();
        String day = getWeekDay();
        java.util.ArrayList<String> campaignId = new ArrayList<String>();

        campaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getCampaignId(day);
        promoDetails = new PromoRuleInfo();
        if(campaignIdList.size()!=0){
        for(int i=0;i<campaignIdList.size();i++){
         campaignId.add(campaignIdList.get(i).getcampaignId());
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
             promoRuleIdList  = (ArrayList<PromoRuleIdInfo>) dlSales.getPromoRuleId(Id);
        
        //     ticketLine.setPromotionRule(promoRuleIdList);
         
        }
        }
    }
    
    public boolean deactivate() {

        return m_ticketsbag.deactivate();
    }
    
    protected abstract JTicketsBag getJTicketsBag();
    protected abstract Component getSouthComponent();
    protected abstract void resetSouthComponent();
     
    public void setActiveTicket(TicketInfo oTicket, Object oTicketExt) {

        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        
        if (m_oTicket != null) {            
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.

            for (TicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setPanel(this);

            }

           
//            m_oTicket.setticketLine(m_oTicket);;
//            m_oTicket.setDatalogic(dlSales);
//            m_oTicket.setJTicketLines(m_ticketlines);
//            m_oTicket.setPromoList(promoRuleIdList);
//            m_oTicket.setPanel(this);
        }
        
        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");
        
        refreshTicket();               
    }
 public void setTicket(TicketInfo ticketValue){
        this.ticketValue = ticketValue;
    }

    public TicketInfo getTicket(){
        return ticketValue;
    }
    public void setPaymentMode(String paymentMode){
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode(){
        return paymentMode;
    }
    public void setActiveTicket(TicketInfo oTicket, Object oTicketExt,AppView view) {
        this.m_App = view;
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;

        if (m_oTicket != null) {
            // Asign preeliminary properties to the receipt
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
            m_oTicket.setActiveCash(m_App.getActiveCashIndex());
            m_oTicket.setActiveDay(m_App.getActiveDayIndex());
            m_oTicket.setDate(new Date()); // Set the edition date.

            for (TicketLineInfo line : m_oTicket.getLines()) {
                line.setDatalogic(dlSales);
                line.setJTicketLines(m_ticketlines);
                line.setticketLine(m_oTicket);
                line.setPanel(this);

            }


//            m_oTicket.setticketLine(m_oTicket);;
//            m_oTicket.setDatalogic(dlSales);
//            m_oTicket.setJTicketLines(m_ticketlines);
//            m_oTicket.setPromoList(promoRuleIdList);
//            m_oTicket.setPanel(this);
        }

        executeEvent(m_oTicket, m_oTicketExt, "ticket.show");

        refreshTicket();
    }


    public TicketInfo getActiveTicket() {
        return m_oTicket;
    }
    
    public void refreshTicket() {
        CardLayout cl = (CardLayout)(getLayout());
        
        if (m_oTicket == null) {        
           // m_jTicketId.setText(null);
            m_ticketlines.clearTicketLines();
           
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null); 
        
            stateToZero();
            
            // Muestro el panel de nulos.
            cl.show(this, "null");  
            resetSouthComponent();

        } else {
            if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
                //Make disable Search and Edit Buttons
                m_jEditLine.setVisible(false);
                m_jList.setVisible(false);
            //    setRefundCount("Refund");
            }else{
            //    setRefundCount("Edit Sales");
            }
            
            // Refresh ticket taxes
//            for (TicketLineInfo line : m_oTicket.getLines()) {
//             line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), m_oTicket.getCustomer()));
//
//            }
//
            // The ticket name
       //     m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));

            // Limpiamos todas las filas y anadimos las del ticket actual
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
        }
    }
       private void setRefundCount(String processName) {
      int processCount = 0;
      processCount = ProcessInfo.setProcessCount(processName, dlSales);
        if(processCount>=10){
          m_jNumberKeys.setEnabled(false);
          jLabel2.setVisible(true);
        }
    }
    public void printPartialTotals(){


        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount.setText(null);
            m_jBillDiscount.setText(null);
             m_oTicket.setBillDiscount(0);
        } else {
          if(m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND){
            m_jSubtotalEuros.setText(m_oTicket.printSubTotalAfterDiscount());
            m_jTaxesEuros.setText(m_oTicket.printTaxAfterDiscount());
            m_jTotalEuros.setText(m_oTicket.printTotalAfterDiscount());
            m_jDiscount.setText(m_oTicket.printRefundDiscount());

          }else{
            m_jSubtotalEuros.setText(m_oTicket.printSubTotalValue());
            m_jTaxesEuros.setText(m_oTicket.printTax());
            m_jTotalEuros.setText(m_oTicket.printTotal());
            m_jDiscount.setText(m_oTicket.printDiscount());
          }
          
            
        }
    }

//public void billValuePromotion() {
//    int billPromotionCount =0;
//    double billDiscount = 0;
//     java.util.ArrayList<String> promoId = new ArrayList<String>();
//
//     for(int i=0;i<promoRuleIdList.size();i++){
//         promoId.add(promoRuleIdList.get(i).getpromoRuleId());
//     }
//     StringBuilder b = new StringBuilder();
//     Iterator<?> it = promoId.iterator();
//     while (it.hasNext()) {
//     b.append(it.next());
//     if (it.hasNext()) {
//        b.append(',');
//      }
//    }
//    String promoRuleId = b.toString();
//
//    try {
//        billPromotionCount = dlSales.getBillPromotionCount(promoRuleId);
//    } catch (BasicException ex) {
//        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//    }
//     double billTotal =(m_oTicket.getTotal()-m_oTiavteicket.getDiscount());
//    if(billPromotionCount!=0){
//            try {
//                billPromoRuleList = (ArrayList<BillPromoRuleInfo>) dlSales.getBillPromoRuleDetails(promoRuleId, billTotal);
//            } catch (BasicException ex) {
//                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//               for(BillPromoRuleInfo bp:billPromoRuleList){
//               double billPromoValue = billPromoRuleList.get(0).getBillAmount();
//               double value = billPromoRuleList.get(0).getValue();
//               double billDiscountAmount=0;
//               double totalPrice = 0;
//
//               if(billPromoRuleList.get(0).getisPrice().equals("Y")){
//                      if(billTotal>=billPromoValue){
//
//                     // setBillDiscount(billPromoRuleList.get(0).getValue());
//                       billDiscount =  billPromoRuleList.get(0).getValue();
//                       if(billDiscount!=0){
//                           m_jBillDiscount.setText(Formats.CURRENCY.formatValue(billDiscount));
//                            m_oTicket.setBillDiscount(billDiscount);
//                       }else{
//                           m_oTicket.setBillDiscount(0);
//                       }
//                 //       if(m_jBillDiscount.getText()==null){
//
//
//                  //      }else{
////                            if(m_oTicket.getBillDiscount()!=billDiscount){
////                               m_jBillDiscount.setText(Formats.CURRENCY.formatValue(billDiscount));
////                     //       }
//
//                        //}
//                    }
//                    }else{
//                       System.out.println("billValuePromotion---"+m_oTicket.printTotal());
//                          if(billTotal>=billPromoValue){
////                            if(m_jBillDiscount.getText()==null){
//                                billDiscount = billTotal* (value/100);
//                                if(billDiscount!=0){
//                                    m_jBillDiscount.setText(Formats.CURRENCY.formatValue(billDiscount));
//                                    m_oTicket.setBillValue(value);
//                                    m_oTicket.setBillDiscount(billDiscount);
//                                }else{
//                                    m_oTicket.setBillValue(0);
//                                    m_oTicket.setBillDiscount(0);
//                                }
////                          }else{
////                                //billDiscount =  billPromoRuleList.get(0).getValue();
////
////                                //System.out.println("billDisci9un---"+billDiscount+"--"+billTotal);
////                                if(m_oTicket.getBillValue()!=value){
////                                    billDiscount = billTotal* (value/100);
////                                    m_jBillDiscount.setText(Formats.CURRENCY.formatValue(billDiscount));
////                                    m_oTicket.setBillDiscount(billDiscount);
////                                    m_oTicket.setBillValue(value);
////
////                            }
////                        }
//
//                          }else{
//                               billDiscount=0;
//                          }
//
//                    }
//               }
//    }
//    }

    private void paintTicketLine(int index, TicketLineInfo oLine){
        
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

        TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer());       
         addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this,tax, 0,null,(java.util.Properties) (oProduct.getProperties().clone())));

    }
    
    public void addTicketLine(TicketLineInfo oLine) {

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
          
            visorTicketLine(oLine);
            printPartialTotals();   
            stateToZero();  

            // event receipt
            executeEventAndRefresh("ticket.change");        

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
                            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
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
            TaxInfo tax = taxeslogic.getTaxInfo(tcid, m_oTicket.getCustomer());
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
                    TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer());
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
                Logger.getLogger(TicketInfo.class.getName()).log(Level.SEVERE, null, ex);
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
//    public String setPromotionType(String id){
//     String pdtpromoType=null;
//        int productqty=0;
//       productqty =(int)setQty();
//        java.util.ArrayList<String> promoId = new ArrayList<String>();
//
//         for(int i=0;i<promoRuleIdList.size();i++){
//             promoId.add(promoRuleIdList.get(i).getpromoRuleId());
//         }
//         StringBuilder b = new StringBuilder();
//         Iterator<?> it = promoId.iterator();
//         while (it.hasNext()) {
//         b.append(it.next());
//         if (it.hasNext()) {
//            b.append(',');
//          }
//}
//        String promoRuleId = b.toString();
//        int productCount = 0;
//      //  String promoType=null;
//        try {
//            productCount = dlSales.getProductCount(id, promoRuleId);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if(productCount!=0){
//            try {
//                pdtpromoType = dlSales.getPromoType(id);
//            } catch (BasicException ex) {
//                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//    }
//        return pdtpromoType;
//}
//
//     public double setDiscountValue(String id,double price){
//        int productqty=0;
//      //  int discount=0;
//     //dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
//       productqty =(int)setQty();
//        System.out.println("Product qty----"+productqty);
//    //    productqty = (int)qty;
////        System.out.println("prod--"+prod.getID()+promoRuleIdList.size()+"--qty--"+);
//        java.util.ArrayList<String> promoId = new ArrayList<String>();
//
//         for(int i=0;i<promoRuleIdList.size();i++){
//             promoId.add(promoRuleIdList.get(i).getpromoRuleId());
//         }
//         StringBuilder b = new StringBuilder();
//         Iterator<?> it = promoId.iterator();
//         while (it.hasNext()) {
//         b.append(it.next());
//         if (it.hasNext()) {
//            b.append(',');
//          }
//}
//        String promoRuleId = b.toString();
//        int productCount = 0;
//        String promoType=null;
//        String promoTypeId= null;
//        int priceOffCount =0;
//        int percentageOffCount = 0;
//        String isPrice;
//        String isPromoProduct;
//        try {
//            productCount = dlSales.getProductCount(id, promoRuleId);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        double productDiscount = 0;
//
//        if(productCount!=0){
//            try {
//                promoType = dlSales.getPromoType(id);
//            } catch (BasicException ex) {
//                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            //setPromoType(promoType);
//            if(promoType.equals("Price off")){
//                isPrice = "Y";
//                isPromoProduct = "Y";
//                productDiscount = getPriceoffDiscount(id,isPrice,isPromoProduct,promoRuleId,productqty);
//            }else if(promoType.equals("Percentage off")){
//                 isPrice = "N";
//                 isPromoProduct = "Y";
//                 productDiscount = getPercentageoffDiscount(id,isPrice,isPromoProduct,promoRuleId,productqty,price);
//                    //int qty =(int)prod.getMultiply();
//
//            }
//        }
//      //   System.out.println("discount ticketinfo= " + productDiscount);
//
//  return productDiscount;
//    }
//public double getPriceoffDiscount(String id,String isPrice, String isPromoProduct, String promoRuleId, int productqty){
//      int priceOffCount =0;
//      String promoTypeId = null;
//      int productDiscount=0;
//     try {
//            promoTypeId = dlSales.getPromoTypeId(id);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            priceOffCount = dlSales.getPriceOffCount(promoTypeId, promoRuleId, id);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//                promoRuleList = (ArrayList<promoRuleInfo>) dlSales.getPromoRuleDetails(promoRuleId, isPrice, isPromoProduct,id);
//            } catch (BasicException ex) {
//                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        if(priceOffCount==1){
//             for (promoRuleInfo pp : promoRuleList) {
//                   int result = 0;
//                  result =  (int)(productqty / pp.getBuyQty()) * (int)pp.getValue();
//                  productDiscount = productDiscount + result;
//
//             }
//        }else{
//            //int qty =(int)prod.getMultiply();
//             for (promoRuleInfo pp : promoRuleList) {
//                  int result = 0;
//                  result =  (int)(productqty / pp.getBuyQty()) * (int)pp.getValue();
//                  productDiscount = productDiscount + result;
//
//                  int remaining = 0;
//                  remaining = productqty % (int)pp.getBuyQty();
//
//                  if (remaining == 0) {
//                    break;
//                  } else {
//                    productqty = remaining;
//                  }
//
//                }
//              }
//     return productDiscount;
// }
// public double getPercentageoffDiscount(String id,String isPrice, String isPromoProduct, String promoRuleId, int productqty, double price){
//      int perOffCount =0;
//      String promoTypeId = null;
//      double productDiscount=0;
//     try {
//            promoTypeId = dlSales.getPromoTypeId(id);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            perOffCount = dlSales.getPercentageOffCount(promoTypeId, promoRuleId,id);
//        } catch (BasicException ex) {
//            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//                promoRuleList = (ArrayList<promoRuleInfo>) dlSales.getPromoRuleDetails(promoRuleId, isPrice, isPromoProduct, id);
//            } catch (BasicException ex) {
//                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        if(perOffCount==1){
//             for (promoRuleInfo pp : promoRuleList) {
//                 double result = 0;
//                 result =  (int)(productqty / pp.getBuyQty()) * (price* pp.getValue()/100);
//                 productDiscount = productDiscount + result;
//             }
//        }else{
//            //int qty =(int)prod.getMultiply();
//             for (promoRuleInfo pp : promoRuleList) {
//                 double result = 0;
//                  result =  (int)(productqty / pp.getBuyQty()) * (price* pp.getValue()/100);
//                  productDiscount = productDiscount + result;
//
//                  int remaining = 0;
//                  remaining = productqty % (int)pp.getBuyQty();
//
//                  if (remaining == 0) {
//                    break;
//                  } else {
//                    productqty = remaining;
//                  }
//
//                }
//              }
//     return productDiscount;
// }
//
//    public double setQty(){
//        int numlines = m_oTicket.getLinesCount();
//        TicketLineInfo current_ticketline;
//        double current_unit;
//        TicketLineInfo loop_ticketline;
//        double loop_unit;
//        for (int i = 0 ; i < numlines ; i++) {
// 	current_ticketline = m_oTicket.getLine(i);
//	current_unit  = current_ticketline.getMultiply();
//
//	if ( current_unit != 0){
//		for (int j = i + 1 ; j < numlines ; j++) {
//			loop_ticketline = m_oTicket.getLine(j);
//			loop_unit  = loop_ticketline.getMultiply();
// 			String current_productid = current_ticketline.getProductID();
//                      //  String current_ProductName = current_ticketline.getProductName();
//			String loop_productid    = loop_ticketline.getProductID();
//                        if ( loop_productid.equals(current_productid)){
//                                current_unit = current_unit + loop_unit;
//                                loop_ticketline.setMultiply(0);
////
//                            }
//		}
//
//	// current_ticketline.setVariousQuantities(current_unit);
//	current_ticketline.setMultiply(current_unit);
//        if(buttonPlus==2){
//            qty = current_ticketline.getMultiply()+1;
//        }else if(buttonPlus==3){
//            qty = current_ticketline.getMultiply()-1;
//        }else{
//            qty = current_ticketline.getMultiply();
//        }
//
//
//
//   }
// }
//
//return qty;
//
//    }

     
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
                     //       m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
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
                            TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
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
                    TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund + button means one unit less
                    if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND){
                        newline.setMultiply(newline.getMultiply() - 1.0);
                        paintTicketLine(i, newline);                   
                    }
                    else {
                
            //    ProductInfoExt product = new ProductInfoExt();
                        // add one unit to the selected line
                        newline.setMultiply(newline.getMultiply()+ 1.0);
                       // buttonPlus = 2;

                        paintTicketLine(i, newline);
                        ProductInfoExt product = getInputProduct();
                       // addTicketLine(product, 1.0, product.getPriceSell());
//                 TaxInfo tax = taxeslogic.getTaxInfo(oProduct.getTaxCategoryID(), m_oTicket.getCustomer());
//                  addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this, tax, 0,(java.util.Properties) (oProduct.getProperties().clone())));
////                 addTicketLine(new TicketLineInfo(oProduct, dMul, dPrice, promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this, tax, 0,(java.util.Properties) (oProduct.getProperties().clone())));

                       //   
                      //  buttonPlus=2;
//                        double discount = setDiscountValue(newline.getProductID(),newline.getPrice());
//                        System.out.println("discount ++"+discount);
//                        newline.setDiscount(discount);
//                        String promotion = setPromotionType(newline.getProductID());
//                        newline.setPromoType(promotion);


                     //   buttonPlus=1;
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
                    TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
                    //If it's a refund - button means one unit more
                    if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND){
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
                      //      System.out.println("newLine---"+newline.getLineId()+"---"+newline.getPromoType());
                         // removeTicketLine(i); // elimino la linea
                       // } else {

                            paintTicketLine(i, newline);
                            ProductInfoExt product = getInputProduct();
                        //    addTicketLine(product, newline.getMultiply(), product.getPriceSell());
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
                    TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i)); 
                    if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND) {
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
                    TicketLineInfo newline = new TicketLineInfo(m_oTicket.getLine(i));
                    if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_NORMAL) {
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
                    System.out.println("enter---getlines");
                    if (closeTicket(m_oTicket, m_oTicketExt)) {
                        System.out.println("enetr closeTicket");
                        // Ends edition of current receip
                            m_ticketsbag.deleteTicket(ticketValue);
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
    
    private boolean closeTicket(TicketInfo ticket, Object ticketext) throws BasicException {
    System.out.println("enetr closeTicket1---"+m_ticketsbag.getDocumentNo());

        boolean resultok = false;
        System.out.println("ticket---"+ticket.getId()+"----" +ticket.getTotal());
        if (m_App.getAppUserView().getUser().hasPermission("sales.Total")) {  
            
            try {
                // reset the payment info
                taxeslogic.calculateTaxes(ticket);
                if (ticket.getTotal()>=0.0){
                    ticket.resetPayments(); //Only reset if is sale
                }
                
                if (executeEvent(ticket, ticketext, "ticket.total") == null) {

                    // Muestro el total
                    printTicket("Printer.TicketTotal", ticket, ticketext);
                    
                //     if(m_jTicketId.getText().equals(null) || m_jTicketId.getText().equals("")){
                        
                   //     JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.selectCustomer"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);

                 //   }else{
                    // Select the Payments information
//                    JPaymentSelect paymentdialog = ticket.getTicketType() == TicketInfo.RECEIPT_NORMAL
//                            ? paymentdialogreceipt
//                            : paymentdialogrefund;
//                    paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));
//
//                    paymentdialog.setTransactionID(ticket.getTransactionID());
//
//                    if (paymentdialog.showDialog((-1* ticket.getTotalAfterDiscount()), ticket.getCustomer())) {

                        // assign the payments selected and calculate taxes.         
                       // ticket.setPayments(paymentdialog.getSelectedPayments());

                        // Asigno los valores definitivos del ticket...
                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        ticket.setActiveCash(m_App.getActiveCashIndex());
                        ticket.setDate(new Date()); // Le pongo la fecha de cobro

                        if (executeEvent(ticket, ticketext, "ticket.save") == null) {
                             //String[] ticketDocNoValue;
                             String ticketDocNo = null;
                            Integer ticketDocNoInt = null;
                            String ticketDocument;
                         if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_NORMAL){

//                          try {
//                            try {
//                                ticketDocNo = dlSales.getTicketDocumentNo().list().get(0).toString();
//                            } catch (BasicException ex) {
//                                ticketDocNo = null;
//                                Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            //  ticketDocNoValue = ticketDocNo.split("-");
//                           //   ticketDocNo=ticketDocNoValue[2];
//                              ticketDocNoInt = Integer.parseInt(ticketDocNo);
//                              ticketDocNoInt = ticketDocNoInt + 1;
//                              } catch (NullPointerException ex) {
//                                //  System.out.println("ticketDocNo==="+ticketDocNo);
//                              ticketDocNoInt = 10000;
//                              Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                             }

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

                    // docNo = docNo+1;
                }
                             System.out.println("ente----m_oTicket"+m_oTicket.getDocumentNo()+m_oTicket.getId());
               
                             ticketDocument =m_oTicket.getDocumentNo();// m_App.getProperties().getStoreName()+"-"+m_App.getProperties().getPosNo()+"-"+ticketDocNoInt;
                         }else{
                                ticketDocument = "0";
                         }
                          if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND){
                                getCreditDate();
                          }
                          if(getPaymentMode().equals("Card") && billTotal!=m_oTicket.getTotal()){
                                showMsg("Amount is not matching");
                            }else{
                            // Save the receipt and iassign a receipt number
                            try {
                                dlSales.saveTicket(ticket, m_App.getInventoryLocation(),m_App.getProperties().getPosNo(),m_App.getProperties().getStoreName(),ticketDocument,null,getPriceInfo(),getPaymentMode());
                            } catch (BasicException eData) {
                                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                                msg.show(this);
                            }
                            dlSales.insertTicket(m_oTicket.getPriceInfo(), ticket);
                            Window w = SwingUtilities.getWindowAncestor(this);
                            w.setVisible(false);
                      //      executeEvent(ticket, ticketext, "ticket.close", new ScriptAr m_App.getBean("com.sysfore.pos.salesdump.JSalesDump");g("print", paymentdialog.isPrintSelected()));
                            //System.out.println("m_oTicket.getTicketType()--"+m_oTicket.getTicketType());
                           String file = null;
                            if (m_oTicket.getTicketType() == TicketInfo.RECEIPT_REFUND){
                            ProcessInfo.setProcessInfo("Refund", dlSales);
                                file = "Printer.CreditNote";
                            }else{
                               ProcessInfo.setProcessInfo("Edit Sales", dlSales);
                                file = "Printer.Ticket";
                            }
                            // Print receipt.
                        //    printTicket(paymentdialog.isPrintSelected()
                          //          ? file
                            //        : "Printer.Ticket2", ticket, ticketext);
                            resultok = true;
                  //      }

                    }}
              //  }
                }
            } catch (TaxesException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotcalculatetaxes"));
                msg.show(this);
                resultok = false;
            }
            
            // reset the payment info
            m_oTicket.resetTaxes();
            m_oTicket.resetPayments();
            
        }
  
        // cancelled the ticket.total script
        // or canceled the payment dialog
        // or canceled the ticket.close script
        return resultok;        
    }
       
    private void printTicket(String sresourcename, TicketInfo ticket, Object ticketext) {

        String sresource = dlSystem.getResourceAsXML(sresourcename);

        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JPanelTicket.this);
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
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }
     private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    private void printReport(String resourcefile, TicketInfo ticket, Object ticketext) {
        
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


    public void visorTicketLine(TicketLineInfo oLine){
        if (oLine == null) { 
             m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {                 
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            }
        } 
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
   
    private Object executeEvent(TicketInfo ticket, Object ticketext, String eventkey, ScriptArg ... args) {
        
        String resource = m_jbtnconfig.getEvent(eventkey);
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
        
        private TicketInfo ticket;
        private Object ticketext;
        
        private int selectedindex;
        
        private ScriptObject(TicketInfo ticket, Object ticketext) {
            this.ticket = ticket;
            this.ticketext = ticketext;
        }
        
        public double getInputValue() {
            if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                return JPanelTicket.this.getInputValue();
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
            JPanelTicket.this.printReport(resourcefile, ticket, ticketext);
        }
       
        
        public void printTicket(String sresourcename) {
            JPanelTicket.this.printTicket(sresourcename, ticket, ticketext);   
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
     
/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        btnCustomer = new javax.swing.JButton();
        m_jPanelScripts = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        m_jList = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        m_jDiscount = new javax.swing.JLabel();
        m_jBillDiscount = new javax.swing.JLabel();
        m_jLblTotalEuros4 = new javax.swing.JLabel();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        m_jNumberKeys = new com.openbravo.beans.JNumberKeys();
        jPanel9 = new javax.swing.JPanel();
        m_jPrice = new javax.swing.JLabel();
        m_jEnter = new javax.swing.JButton();
        m_jKeyFactory = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        m_jBtnCalculate = new javax.swing.JButton();
        m_jPor = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        m_jaddtax = new javax.swing.JToggleButton();
        catcontainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 204, 153));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jTicketId.setBackground(java.awt.Color.white);
        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        btnCustomer.setFocusPainted(false);
        btnCustomer.setFocusable(false);
        btnCustomer.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnCustomer.setRequestFocusEnabled(false);
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        m_jButtons.add(btnCustomer);

        m_jOptions.add(m_jButtons, java.awt.BorderLayout.LINE_START);

        m_jPanelScripts.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

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
        jPanel1.add(m_jbtnScale);

        m_jButtonsExt.add(jPanel1);

        m_jPanelScripts.add(m_jButtonsExt, java.awt.BorderLayout.LINE_END);

        m_jOptions.add(m_jPanelScripts, java.awt.BorderLayout.LINE_END);

        m_jPanelBag.setLayout(new java.awt.BorderLayout());
        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setPreferredSize(new java.awt.Dimension(643, 10));
        m_jOptions.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel2.setMinimumSize(new java.awt.Dimension(66, 338));
        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/arow-1.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jUp.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel2.add(m_jUp);

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/arow-2.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDown.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDown);

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/close2.png"))); // NOI18N
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelete.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDelete);

        m_jList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search121.png"))); // NOI18N
        m_jList.setFocusPainted(false);
        m_jList.setFocusable(false);
        m_jList.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jList.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jList.setRequestFocusEnabled(false);
        m_jList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jListActionPerformed(evt);
            }
        });
        jPanel2.add(m_jList);

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/edit12.png"))); // NOI18N
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel2.add(m_jEditLine);

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jPanTicket.add(jPanel5, java.awt.BorderLayout.LINE_END);

        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        m_jPanTotals.setPreferredSize(new java.awt.Dimension(450, 113));

        m_jTotalEuros.setBackground(java.awt.Color.white);
        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);

        m_jLblTotalEuros1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N

        m_jSubtotalEuros.setBackground(java.awt.Color.white);
        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);

        m_jTaxesEuros.setBackground(java.awt.Color.white);
        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N

        m_jLblTotalEuros3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N

        jLabel1.setText("Discount");

        m_jDiscount.setBackground(java.awt.Color.white);
        m_jDiscount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jDiscount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jDiscount.setOpaque(true);
        m_jDiscount.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jDiscount.setRequestFocusEnabled(false);

        m_jBillDiscount.setBackground(java.awt.Color.white);
        m_jBillDiscount.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jBillDiscount.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jBillDiscount.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jBillDiscount.setOpaque(true);
        m_jBillDiscount.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jBillDiscount.setRequestFocusEnabled(false);

        m_jLblTotalEuros4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jLblTotalEuros4.setText("Bill Discount"); // NOI18N

        org.jdesktop.layout.GroupLayout m_jPanTotalsLayout = new org.jdesktop.layout.GroupLayout(m_jPanTotals);
        m_jPanTotals.setLayout(m_jPanTotalsLayout);
        m_jPanTotalsLayout.setHorizontalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .addContainerGap()
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(m_jLblTotalEuros2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(m_jTaxesEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(m_jLblTotalEuros3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(m_jDiscount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jLblTotalEuros4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jLblTotalEuros1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(m_jPanTotalsLayout.createSequentialGroup()
                            .add(m_jBillDiscount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .add(m_jPanTotalsLayout.createSequentialGroup()
                            .add(m_jSubtotalEuros, 0, 0, Short.MAX_VALUE)
                            .add(30, 30, 30)))))
        );
        m_jPanTotalsLayout.setVerticalGroup(
            m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanTotalsLayout.createSequentialGroup()
                .add(11, 11, 11)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(m_jSubtotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jLblTotalEuros2)
                            .add(m_jTaxesEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(m_jLblTotalEuros3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jLblTotalEuros4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, m_jDiscount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(m_jBillDiscount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(m_jPanTotalsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jTotalEuros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jPanTotalsLayout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(m_jLblTotalEuros1)))
                .add(15, 15, 15))
        );

        jPanel4.add(m_jPanTotals, java.awt.BorderLayout.LINE_END);

        m_jPanelCentral.add(jPanel4, java.awt.BorderLayout.SOUTH);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setLayout(new javax.swing.BoxLayout(m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

        m_jNumberKeys.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                m_jNumberKeysKeyPerformed(evt);
            }
        });
        m_jPanEntries.add(m_jNumberKeys);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        m_jPrice.setBackground(java.awt.Color.white);
        m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPrice.setOpaque(true);
        m_jPrice.setPreferredSize(new java.awt.Dimension(162, 20));
        m_jPrice.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(m_jPrice, gridBagConstraints);

        m_jEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode.png"))); // NOI18N
        m_jEnter.setFocusPainted(false);
        m_jEnter.setFocusable(false);
        m_jEnter.setPreferredSize(new java.awt.Dimension(51, 42));
        m_jEnter.setRequestFocusEnabled(false);
        m_jEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel9.add(m_jEnter, gridBagConstraints);

        m_jPanEntries.add(jPanel9);

        m_jKeyFactory.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setForeground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setBorder(null);
        m_jKeyFactory.setCaretColor(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });
        m_jPanEntries.add(m_jKeyFactory);

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.NORTH);

        jPanel3.setPreferredSize(new java.awt.Dimension(228, 135));

        m_jBtnCalculate.setText("Calculate Promotion");
        m_jBtnCalculate.setFocusable(false);
        m_jBtnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCalculateActionPerformed(evt);
            }
        });

        m_jPor.setBackground(java.awt.Color.white);
        m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPor.setOpaque(true);
        m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        m_jPor.setRequestFocusEnabled(false);

        m_jTax.setFocusable(false);
        m_jTax.setRequestFocusEnabled(false);

        m_jaddtax.setText("+");
        m_jaddtax.setFocusPainted(false);
        m_jaddtax.setFocusable(false);
        m_jaddtax.setRequestFocusEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(31, 31, 31)
                        .add(m_jBtnCalculate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(112, Short.MAX_VALUE)
                                .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(26, 26, 26))
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 22, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jaddtax)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(m_jBtnCalculate)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(m_jPor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(33, 33, 33)
                        .add(m_jaddtax)))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        m_jContEntries.add(jPanel3, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jContEntries, java.awt.BorderLayout.LINE_END);

        catcontainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        catcontainer.setLayout(new java.awt.BorderLayout());
        m_jPanContainer.add(catcontainer, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed
        try {
            stateTransition('\u00a7');
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
        
        int i = m_ticketlines.getSelectedIndex();
       
        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // no line selected
        } else {
            try {
                TicketLineInfo newline = JProductLineEdit.showMessage(this, m_App, m_oTicket.getLine(i));
                if (newline != null) {
                    // line has been modified
                 //    new TicketLineInfo(promoRuleIdList,dlSales,m_oTicket,m_ticketlines,this);
                    // newline.setButton(2);
                   //  newline.setIndex(i);
                    paintTicketLine(i, newline);
                    ProductInfoExt product = getInputProduct();
                    addTicketLine(product, 1.0, product.getPriceSell());
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
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jEnterActionPerformed

    private void m_jNumberKeysKeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_m_jNumberKeysKeyPerformed
        try {
            stateTransition(evt.getKey());
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jNumberKeysKeyPerformed

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped

        m_jKeyFactory.setText(null);
        try {
            stateTransition(evt.getKeyChar());
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            removeTicketLine(i); // elimino la linea
                    
        }   
        
    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed
        
        m_ticketlines.selectionUp();

    }//GEN-LAST:event_m_jUpActionPerformed

    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed

        m_ticketlines.selectionDown();

    }//GEN-LAST:event_m_jDownActionPerformed

    private void m_jListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jListActionPerformed

        ProductInfoExt prod = JProductFinder.showMessage(JPanelTicket.this, dlSales);    
        if (prod != null) {
            buttonTransition(prod);
        }
        
    }//GEN-LAST:event_m_jListActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed

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

        refreshTicket();
        
}//GEN-LAST:event_btnCustomerActionPerformed


    private void m_jBtnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCalculateActionPerformed

        ArrayList<BuyGetPriceInfo> leastProductList = new ArrayList<BuyGetPriceInfo>();
        StringBuilder b = new StringBuilder();
            StringBuilder b1 = new StringBuilder();
        try {
             dlSales.deleteTempTicketlines();
             dlSales.saveTempTicketlines(m_oTicket);
             java.util.ArrayList<String> campaignId = new ArrayList<String>();

             pdtCampaignIdList = (ArrayList<CampaignIdInfo>) dlSales.getPdtCampaignId();
             for(int i=0;i<pdtCampaignIdList.size();i++){
             campaignId.add(pdtCampaignIdList.get(i).getcampaignId());
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
        m_jBillDiscount.setText(m_oTicket.printBillDiscount());
        printPartialTotals();
        } catch (BasicException ex) {
            Logger.getLogger(JPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_m_jBtnCalculateActionPerformed

    public java.util.ArrayList<BuyGetPriceInfo> getPriceInfo(){
        return pdtBuyGetPriceList;
    }
    public void setPriceInfo(java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList){
        this.pdtBuyGetPriceList = pdtBuyGetPriceList;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JPanel catcontainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel m_jBillDiscount;
    private javax.swing.JButton m_jBtnCalculate;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JLabel m_jDiscount;
    private javax.swing.JButton m_jDown;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEnter;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JLabel m_jLblTotalEuros4;
    private javax.swing.JButton m_jList;
    private com.openbravo.beans.JNumberKeys m_jNumberKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JPanel m_jPanelScripts;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JLabel m_jPrice;
    private javax.swing.JLabel m_jSubtotalEuros;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTaxesEuros;
    private javax.swing.JLabel m_jTicketId;
    private javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JButton m_jUp;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnScale;
    // End of variables declaration//GEN-END:variables

}
