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

package com.openbravo.pos.ticket;

import java.io.*;
import com.openbravo.pos.util.StringUtils;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BillPromoRuleInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.CrossProductInfo;
import com.openbravo.pos.forms.PromoRuleIdInfo;
import com.openbravo.pos.forms.PromoRuleInfo;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.JRetailPanelTicket;
import com.openbravo.pos.sales.JTicketLines;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class TicketLineInfo implements SerializableWrite, SerializableRead, Serializable {

    private static final long serialVersionUID = 6608012948284450199L;
    private String m_sTicket;
    private int m_iLine;
    private double multiply;
    private double freemultiply;
    private double price;
    private TaxInfo tax;
    private Properties attributes;
    private String productid;
    private String attsetinstid;
    private double discountValue;
    //private double discountValue;
   // protected AppView m_App;
    public java.util.ArrayList<PromoRuleInfo> promoRuleList = null;
    public java.util.ArrayList<CrossProductInfo> crossproductList = null;
    protected PromoRuleInfo promoDetails;
    public java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList;
    private double billDiscount;
  
    double qty =0;
    int buttonPlus;
    String pdtpromoType = "";
    protected transient DataLogicSales dlSales;
    protected transient TicketInfo m_oTicket;
//     protected TicketInfo m_oTicket1;
    protected transient JTicketLines m_ticketlines;
    protected transient JPanelTicket jPanel;
    protected transient JRetailPanelTicket jRetailPanel;
    private int index1;
    private double CurrentPrice;
    private int ticketType;
    private double taxRate;
   //private List<PaymentInfo> payments;
    public String ticketId;
    public String lineId;
    public int lineNo;
    private String campaignId;
    private String promoId;
    private String isCrossProduct;
    private String pName;
    /** Creates new TicketLineInfo */
    public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax,double discountValue, String pName, Properties props) {
        init(productid, null, dMultiply, dPrice, null,null,null,null,null,tax, discountValue, pName, props);
    }

    public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax, double discountValue, String pName ) {
        init(productid, null, dMultiply, dPrice, null,null,null,null,null,tax,discountValue, pName ,new Properties());
    }

    public TicketLineInfo(String productid, String productname, String producttaxcategory, double dMultiply, double dPrice, java.util.ArrayList<PromoRuleIdInfo> promoRule,DataLogicSales dlSales,TicketInfo m_oTicket,JTicketLines m_ticketlines,JPanelTicket jPanel,TaxInfo tax,double discountValue, String pName) {
        Properties props = new Properties();
        props.setProperty("product.name", productname);
        props.setProperty("product.taxcategoryid", producttaxcategory);
        setvalues(promoRule,dlSales,m_oTicket,m_ticketlines,jPanel);
        init(productid, null, dMultiply, dPrice,promoRule,dlSales,m_ticketlines,m_oTicket,jPanel,tax, discountValue,pName, props);
    }

    public TicketLineInfo(String productname, String producttaxcategory, double dMultiply, double dPrice, TaxInfo tax, double discountValue,String pName) {
        Properties props = new Properties();
        props.setProperty("product.name", productname);
        props.setProperty("product.taxcategoryid", producttaxcategory);
        init(null, null, dMultiply, dPrice, null,null,null,null,null,tax, discountValue,pName,props);
    }

    public TicketLineInfo() {
        init(null, null, 0.0, 0.0, null,null,null,null,null,null, 0.0,null,new Properties());
    }

    public TicketLineInfo(ProductInfoExt product, double dMultiply, double dPrice,  java.util.ArrayList<PromoRuleIdInfo> promoRule,DataLogicSales dlSales,TicketInfo m_oTicket,JTicketLines m_ticketlines,JPanelTicket jPanel, TaxInfo tax, double discountValue,String pName, Properties attributes) {

        String pid;
        double priceDiscount = 0;
     
        if (product == null) {
            pid = null;
        } else {
            pid = product.getID();
         
            attributes.setProperty("product.name", product.getName());
            attributes.setProperty("product.com", product.isCom() ? "true" : "false");
            if (product.getAttributeSetID() != null) {
                attributes.setProperty("product.attsetid", product.getAttributeSetID());
            }
            attributes.setProperty("product.taxcategoryid", product.getTaxCategoryID());
            if (product.getCategoryID() != null) {
                attributes.setProperty("product.categoryid", product.getCategoryID());
            }
            
        setvalues(promoRule,dlSales,m_oTicket,m_ticketlines,jPanel);
       
        }
        init(pid, null, dMultiply, dPrice,promoRule,dlSales,m_ticketlines,m_oTicket,jPanel,tax,discountValue, pName, attributes);
    }

    private void setvalues(ArrayList<PromoRuleIdInfo> promoRule, DataLogicSales dlSales, TicketInfo m_oTicket, JTicketLines m_ticketlines, JPanelTicket jPanel) {
            this.promoRuleIdList = promoRule;
            this.dlSales = dlSales;
            this.m_oTicket = m_oTicket;
            this.m_ticketlines = m_ticketlines;
            this.jPanel = jPanel;
    }

    public TicketLineInfo(ProductInfoExt oProduct, double dPrice, TaxInfo tax,double discountValue,Properties attributes) {
        this(oProduct, 1.0, dPrice,null,null,null,null,null,tax,discountValue,null,attributes);

    }

    public TicketLineInfo(TicketLineInfo line) {
        init(line.productid, line.attsetinstid, line.multiply, line.price,line.promoRuleIdList,line.dlSales,line.m_ticketlines, line.m_oTicket,line.jPanel,line.tax,line.discountValue,line.pName, (Properties) line.attributes.clone());
    }

    private void init(String productid, String attsetinstid, double dMultiply, double dPrice, java.util.ArrayList<PromoRuleIdInfo> promoRule,DataLogicSales dlSales,JTicketLines m_ticketlines,TicketInfo m_oTicket,JPanelTicket jPanel,TaxInfo tax, double discountValue,String pName, Properties attributes) {
        this.productid = productid;
        this.attsetinstid = attsetinstid;
        multiply = dMultiply;
        price = dPrice;
        promoRuleIdList = promoRule;
        this.dlSales = dlSales;
        this.m_ticketlines = m_ticketlines;
        this.m_oTicket = m_oTicket;
        this.jPanel = jPanel;
        this.tax = tax;
        this.attributes = attributes;
        this.discountValue = discountValue;
        this.pName = pName;
        //setDiscountValue(productid,promoRule,dlSales,m_oTicket,m_ticketlines,jPanel);
        m_sTicket = null;
        m_iLine = -1;
        
    }
public String setPromotionType(String id){

    int productqty=0;
    productqty =(int)getQty();
    java.util.ArrayList<String> promoId = new ArrayList<String>();
    if(promoRuleIdList!=null){

    for(int i=0;i<promoRuleIdList.size();i++){
         promoId.add(getPromotionRule().get(i).getpromoRuleId());
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
    try {
        productCount = dlSales.getProductCount(id, promoRuleId);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    if(productCount!=0){
        try {
            pdtpromoType = dlSales.getPromoType(id);
        } catch (BasicException ex) {
            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   }

        return pdtpromoType;
}

 public double setDiscountValue(String id){
    int productqty=0;
    productqty =(int)getQty();


    java.util.ArrayList<String> promoId = new ArrayList<String>();
     double productDiscount = 0;
 if(promoRuleIdList!=null){

     for(int i=0;i<promoRuleIdList.size();i++){
         promoId.add(promoRuleIdList.get(i).getpromoRuleId());
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

   // m_oTicket.setPromotionRule(promoRuleId);
    int productCount = 0;
    String promoType=null;
    String promoTypeId= null;
    int priceOffCount =0;
    int percentageOffCount = 0;
    String isPrice;
    String isPromoProduct;
    try {
        productCount = dlSales.getProductCount(id, promoRuleId);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
   

    if(productCount!=0){
        try {
            promoType = dlSales.getPromoType(id);
        } catch (BasicException ex) {
            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(promoType.equals("Price off")){
            isPrice = "Y";
            isPromoProduct = "Y";
            productDiscount = getPriceoffDiscount(id,isPrice,isPromoProduct,promoRuleId,productqty);

        }else if(promoType.equals("Percentage off")){
            isPrice = "N";
            isPromoProduct = "Y";
            productDiscount = getPercentageoffDiscount(id,isPrice,isPromoProduct,promoRuleId,productqty);
        }else if(promoType.equals("BuyGet")){
            isPrice = "N";
            isPromoProduct = "Y";
            getBuyGetDiscount(id,isPrice,isPromoProduct,promoRuleId,productqty);
            productDiscount=0;

        }
    }
 }
  return productDiscount;
    }
 public double getPriceoffDiscount(String id,String isPrice, String isPromoProduct, String promoRuleId, int productqty){
      int priceOffCount =0;
      String promoTypeId = null;
      int productDiscount=0;
        try {
            promoTypeId = dlSales.getPromoTypeId(id);
        } catch (BasicException ex) {
            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            priceOffCount = dlSales.getPriceOffCount(promoTypeId, promoRuleId,id);
        } catch (BasicException ex) {
            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
             promoRuleList = (ArrayList<PromoRuleInfo>) dlSales.getPromoRuleDetails(promoRuleId, isPrice, isPromoProduct,id);
        } catch (BasicException ex) {
                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
      if(promoRuleList.get(0).getBuyQty()==0){
           int discountValue=0;
            try {
                productDiscount = dlSales.getRangePriceOffValue(id, promoRuleId, (int)productqty);
            } catch (BasicException ex) {
                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
           if(productDiscount==0){
                try {
                productDiscount = dlSales.getMaxRangePriceOffValue(id, promoRuleId);
            } catch (BasicException ex) {
                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
          
           //productDiscount =discountValue;
      }else{
        if(priceOffCount==1){
             int result = 0;
             result =  (int)(productqty / promoRuleList.get(0).getBuyQty()) * (int)promoRuleList.get(0).getValue();
             productDiscount = productDiscount + result;
        }else{
             for (PromoRuleInfo pp : promoRuleList) {
                  int result = 0;
                  result =  (int)(productqty / pp.getBuyQty()) * (int)pp.getValue();
                  productDiscount = productDiscount + result;

                  int remaining = 0;
                  remaining = productqty % (int)pp.getBuyQty();

                  if (remaining == 0) {
                    break;
                  } else {
                    productqty = remaining;
                  }

                }
              }
      }
     return productDiscount;
 }
 public double getPercentageoffDiscount(String id,String isPrice, String isPromoProduct, String promoRuleId, int productqty){
   //  System.out.println("getperDiscount"+m_ticketlines.getSelectedIndex());
    int perOffCount =0;
    String promoTypeId = null;
    double productDiscount=0;
    try {
        promoTypeId = dlSales.getPromoTypeId(id);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        perOffCount = dlSales.getPercentageOffCount(promoTypeId, promoRuleId,id);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
            promoRuleList = (ArrayList<PromoRuleInfo>) dlSales.getPromoRuleDetails(promoRuleId, isPrice, isPromoProduct,id);
        } catch (BasicException ex) {
            Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
     if(promoRuleList.get(0).getBuyQty()==0){
         int discountPercentage = 0;
            try {
                discountPercentage = dlSales.getRangePerOffValue(id, promoRuleId, (int)productqty);
            } catch (BasicException ex) {
                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }

           if(discountPercentage==0){
                try {
                discountPercentage = dlSales.getMaxRangePerOffValue(id, promoRuleId);
            } catch (BasicException ex) {
                Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
           }
          productDiscount = productqty* (price* discountPercentage/100);
     }else{
        if(perOffCount==1){
                 double result = 0;
                 result =  (int)(productqty / promoRuleList.get(0).getBuyQty()) * (price* promoRuleList.get(0).getValue()/100);
                 productDiscount = productDiscount + result;
        }else{
             for (PromoRuleInfo pp : promoRuleList) {
                  double result = 0;
                  int remaining = 0;
                  remaining = productqty % (int)pp.getBuyQty();
                  result = (price * (productqty-remaining)* pp.getValue()/100);
                  productDiscount = productDiscount + result;

                  if (remaining == 0) {
                    break;
                  } else {
                    productqty = remaining;
                  }

                }
              }
     }
     return productDiscount;
 }

  public void getBuyGetDiscount(String id,String isPrice, String isPromoProduct, String promoRuleId, int productqty){


    int buyGetCount =0;
    String promoTypeId = null;
    double productDiscount=0;
//    if(getLineNo()==0){
//       setLineNo((int) UUID.randomUUID().getLeastSignificantBits());
//
//    }
 //     System.out.println("getLineId ------"+getLineNo()+"------"+UUID.randomUUID().getLeastSignificantBits()+"----"+UUID.randomUUID().getMostSignificantBits());
    try {
        promoTypeId = dlSales.getPromoTypeId(id);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        buyGetCount = dlSales.getBuyGetCount(promoTypeId, promoRuleId,id);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        promoRuleList = (ArrayList<PromoRuleInfo>) dlSales.getPromoRuleDetails(promoRuleId, isPrice, isPromoProduct,id);
    } catch (BasicException ex) {
        Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    String promoTypeName = null;
     int index;
     if(getButton()==2){
      index = getIndex();
     }else{
         index = m_ticketlines.getSelectedIndex();
     }
    promoTypeName = setPromotionType(getProductID());
    double freeqty=0;
  
    if(buyGetCount!=0){

             for (PromoRuleInfo pp : promoRuleList) {
              double result = 0;
              int remaining = 0;
              result =(int)(productqty / pp.getBuyQty()) * (int)pp.getQty();
              freeqty = freeqty + result;
              remaining = (int)getQty() % (int)pp.getBuyQty();
              if (remaining == 0) {
                break;
              } else {
                productqty = remaining;
              }

          }

       if(promoRuleList.get(0).getIsSku().equals("Y")){
          if(freeqty!=0){
               
              TicketLineInfo newLine = new TicketLineInfo(
              getProductID(),getProductName(),getProductTaxCategoryID(),freeqty, 0, promoRuleIdList,dlSales,m_oTicket, m_ticketlines,jPanel, getTaxInfo(),0.0,"");
              newLine.setPromoType(promoTypeName);
              newLine.setPromoRule(promoRuleList);
//            m_oTicket.insertLine(index + 1, newLine);
              m_oTicket.addLine(newLine);
              m_ticketlines.addTicketLine(newLine);
              jPanel.setSelectedIndex(index + 1);

              jPanel.refreshTicket();

          }
        }else{

          String promoPdtId;
          try {
              promoPdtId = dlSales.getPdtPromoRuleId(productid);
              crossproductList = (ArrayList<CrossProductInfo>) dlSales.getCrossProductDetails(promoPdtId);
          } catch (BasicException ex) {
              Logger.getLogger(TicketLineInfo.class.getName()).log(Level.SEVERE, null, ex);
          }
          if(freeqty!=0){
              if(crossproductList.size()==1){
                  TicketLineInfo newLine = new TicketLineInfo(
                  crossproductList.get(0).getProductId(),crossproductList.get(0).getProductName(),getProductTaxCategoryID(),freeqty, 0,promoRuleIdList,dlSales,m_oTicket, m_ticketlines,jPanel, getTaxInfo(),0,"");
                  newLine.setPromoType(promoTypeName);
                  newLine.setPromoRule(promoRuleList);
//                  m_oTicket.insertLine(index + 1, newLine);
                  m_oTicket.addLine(newLine);
                  m_ticketlines.addTicketLine(newLine);
                  jPanel.setSelectedIndex(index+1);
                  jPanel.refreshTicket();
              }else{
                  for(int i=0;i<crossproductList.size();i++){
                      TicketLineInfo newLine = new TicketLineInfo(
                      crossproductList.get(i).getProductId(),crossproductList.get(i).getProductName(),getProductTaxCategoryID(),freeqty, 0, promoRuleIdList,dlSales,m_oTicket, m_ticketlines,jPanel,getTaxInfo(),0,null);
                      newLine.setPromoType(promoTypeName);
                      newLine.setPromoRule(promoRuleList);
                      m_oTicket.insertLine(index + i, newLine);
                      jPanel.setSelectedIndex(index+i);
                      jPanel.refreshTicket();
                 }
              }
           }
        }
     }
  }

 public void setTicketId(String ticketId){
    this.ticketId = ticketId;
 }
 public String getTicketId(){
     return ticketId;
 }
 public void setLineId(String lineId){
    this.lineId = lineId;
 }
 public String getLineId(){
     return lineId;
 }

 public void setLineNo(int lineNo){
    this.lineNo = lineNo;
 }
 public int getLineNo(){
     return lineNo;
 }
 public void setQty(double qty){
     this.qty = qty;
 }

 public double getQty(){
     return qty;
 }

  public void setIndex(int index1){
     this.index1 = index1;
 }

 public int getIndex(){
     return index1;
 }

 public void setticketLine(TicketInfo m_oTicket) {
     this.m_oTicket = m_oTicket;
 }
 public TicketInfo getticketLine(){
     return m_oTicket;
 }
 public void setPanel(JPanelTicket panel) {
     this.jPanel = panel;
 }
  public JPanelTicket getPanel(){
     return jPanel;
 }
   public void setRetailPanel(JRetailPanelTicket jRetailPanel) {
     this.jRetailPanel = jRetailPanel;
 }
  public JRetailPanelTicket getRetailPanel(){
     return jRetailPanel;
 }
 public java.util.ArrayList<PromoRuleIdInfo> getPromoList(){
      return promoRuleIdList;
  }

 public void setPromoList(java.util.ArrayList<PromoRuleIdInfo> promoRuleIdList){
     this.promoRuleIdList = promoRuleIdList;
 }

 public java.util.ArrayList<PromoRuleInfo> getPromoRule(){
     return promoRuleList;
 }

 public void setPromoRule(java.util.ArrayList<PromoRuleInfo> promoRuleList){
     this.promoRuleList = promoRuleList;
 }
 public void setJTicketLines(JTicketLines m_ticketlines) {
     this.m_ticketlines = m_ticketlines;
 }
 public JTicketLines getJTicketLines(){
    return m_ticketlines;
 }
 public void setDatalogic(DataLogicSales dlSales) {
    this.dlSales = dlSales;
 }
 public DataLogicSales getDatalogic(){
     return dlSales;
 }
 public int getButton(){
     return buttonPlus;
 }
  public void setButton(int buttonPlus){
     this.buttonPlus = buttonPlus;
 }
 public void setPromoType(String pdtpromoType){
     this.pdtpromoType = pdtpromoType;
 }
 public String getPromoType(){
     return pdtpromoType==null?"":pdtpromoType;
 }
//  public void setProdType(String pdtType){
//     this.pdtType = pdtType;
// }
// public String getProdType(){
//     return pdtType==null?"":pdtType;
// }
    void setTicket(String ticket, int line) {
        m_sTicket = ticket;
        m_iLine = line;
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sTicket);
        dp.setInt(2, new Integer(m_iLine));
        dp.setString(3, productid);
        dp.setString(4, attsetinstid);

        dp.setDouble(5, new Double(multiply));
        dp.setDouble(6, new Double(price));

        dp.setString(7, tax.getId());
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            attributes.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dp.setBytes(8, o.toByteArray());
        } catch (IOException e) {
            dp.setBytes(8, null);
        }
        dp.setDouble(9, new Double(discountValue));
        dp.setString(10, pName);
    }

    public void readValues(DataRead dr) throws BasicException {
        m_sTicket = dr.getString(1);
        m_iLine = dr.getInt(2).intValue();
        productid = dr.getString(3);
        attsetinstid = dr.getString(4);

        multiply = dr.getDouble(5);
        price = dr.getDouble(6);
       
        tax = new TaxInfo(dr.getString(7), dr.getString(8), dr.getString(9), dr.getString(10), dr.getString(11), dr.getDouble(12), dr.getBoolean(13), dr.getInt(14), dr.getString(15), dr.getString(16),dr.getString(17), dr.getString(18),dr.getString(19),dr.getString(20),null,null,null);
        attributes = new Properties();
        try {
            byte[] img = dr.getBytes(17);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }
        discountValue = dr.getDouble(16);
        pName = dr.getString(17);
    }

    public TicketLineInfo copyTicketLine() {
        TicketLineInfo l = new TicketLineInfo();
        // l.m_sTicket = null;
        // l.m_iLine = -1;
        l.productid = productid;
        l.attsetinstid = attsetinstid;
        l.multiply = multiply;
        l.price = price;
       
        l.tax = tax;
        l.attributes = (Properties) attributes.clone();
        l.discountValue = discountValue;
        l.pName = pName;
        return l;
    }

    public int getTicketLine() {
        return m_iLine;
    }
    public void setTicketLine(int m_iLine){
        this.m_iLine=m_iLine;
    }
     public int getTicketType() {
        return ticketType;
    }
     public void setTicketType(int ticketType){
         this.ticketType = ticketType;
     }

    public String getProductID() {
        return productid;
    }
    public void setProductId(String productid){
        this.productid = productid;
    }

    public String getProductName() {
        return pName;
    }
     public String getRetailProductName() {
        return pName;
    }
     public void setRetailProductName(String pName){
         this.pName = pName;
     }

    public String getProductAttSetId() {
        return attributes.getProperty("product.attsetid");
    }

    public String getProductAttSetInstDesc() {
        return attributes.getProperty("product.attsetdesc", "");
    }

    public void setProductAttSetInstDesc(String value) {
        if (value == null) {
            attributes.remove(value);
        } else {
            attributes.setProperty("product.attsetdesc", value);
        }
    }

    public String getProductAttSetInstId() {
        return attsetinstid;
    }

    public void setProductAttSetInstId(String value) {
        attsetinstid = value;
    }

    public boolean isProductCom() {
        return "true".equals(attributes.getProperty("product.com"));
    }

    public String getProductTaxCategoryID() {
        return (attributes.getProperty("product.taxcategoryid"));
    }

    public String getProductCategoryID() {
        return (attributes.getProperty("product.categoryid"));
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double dValue) {
        multiply = dValue;
    }
    public double getFreeMultiply() {
        return freemultiply;
    }

    public void setFreeMultiply(double dValue) {
        freemultiply = dValue;
    }
    public double getPrice() {
        return price;
    }
  public double getDiscount() {
     //   System.out.println("discount inside---"+discountValue);
        return discountValue;
    }
    public void setDiscount(double discount) {
        this.discountValue = discount;

    }

   public void setPrice(double dValue) {
        price = dValue;
    }

    public double getPriceTax() {
        return price * (1.0 + getTaxRate());
    }

    public void setPriceTax(double dValue) {
        price = dValue / (1.0 + getTaxRate());
    }

    public TaxInfo getTaxInfo() {
        return tax;
    }

    public void setTaxInfo(TaxInfo value) {
        tax = value;
    }

    public String getProperty(String key) {
        return attributes.getProperty(key);
    }

    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }

    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }

    public Properties getProperties() {
        return attributes;
    }

    public double getTaxRate() {
        return tax == null ? 0.0 : tax.getRate();
    }
    public double getTaxValue() {
        return taxRate;
    }
    public void setTaxValue(double taxRate) {
        this.taxRate = taxRate;
    }

    public java.util.ArrayList<PromoRuleIdInfo> getPromotionRule() {
      return promoRuleIdList;
    }

    public void setPromotionRule(java.util.ArrayList<PromoRuleIdInfo> l) {
      promoRuleIdList = l;
    }
    public double getSubValue() {
        double value= (price * multiply);
        return value;
    }
    public double getDiscountValue() {
        double value;
        if(getTicketType()==0){
            value= (price * multiply) - getDiscount();
        }else{
             value= (price * multiply) + getDiscount();
        }
        return value;
    }

    public double getDiscountPrice() {
        double value= getDiscountValue()/multiply;
        return value;
    }

    public double getTax() {
        double taxvalue;
//        System.out.println("ticketType getTax---"+ticketType);
        if(ticketType==0){
             taxvalue= price * multiply * getTaxRate();
        }else{
            taxvalue= price * multiply*getTaxValue();
        }
         return taxvalue;
    }

    public double getValue() {
        return price * multiply * (1.0 + getTaxRate());
    }
    public String getCampaignId() {
        try{
            campaignId = getPromoRule().get(0).getCampaignId();
        }catch(NullPointerException ex){
            campaignId =null;
        }
        return campaignId == null?"":campaignId;
    }

     public void setCampaignId(String campaignId) {

        this.campaignId =campaignId;
    }
    public String getpromoId() {
        try{
            promoId = getPromoRule().get(0).getpromoId();
        }catch(NullPointerException ex){
            promoId =null;
        }
        return promoId == null?"":promoId;
    }
     public String getIsCrossProduct() {
        try{
            isCrossProduct = getPromoRule().get(0).getIsCrossProduct();
        }catch(NullPointerException ex){
            isCrossProduct =null;
        }
        return isCrossProduct == null?"":isCrossProduct;
    }
    public String printName() {
        
        return pName;//;StringUtils.encodeXML(attributes.getProperty("product.name"));
    }
    public String printPdtName() {
        return StringUtils.encodeXML(attributes.getProperty("product.name"));
    }
    public String printProductName(){
        return pName;
    }

    public String printMultiply() {
        return Formats.DOUBLE.formatValue(multiply);
    }

    public String printDiscount() {
        return Formats.DOUBLE.formatValue(getDiscount());
    }


    public String printPrice() {
        return Formats.CURRENCY.formatValue(getPrice());
    }
    public String printPriceValue() {
        return Formats.DoubleValue.formatValue(getPrice());
    }

    public String printPriceTax() {
        return Formats.CURRENCY.formatValue(getPriceTax());
    }

    public String printTax() {
        return Formats.CURRENCY.formatValue(getTax());
    }

    public String printTaxRate() {
        double taxRateValue;
         if(ticketType==0){
             taxRateValue= getTaxRate();
        }else{
            taxRateValue= getTaxValue();
        }
        return Formats.PERCENT.formatValue(taxRateValue);
    }

    public String printSubValue() {
        return Formats.CURRENCY.formatValue(getSubValue());
    }
    public String printSubTotalValue() {
        return Formats.DoubleValue.formatValue(getSubValue());
    }
    public String printDiscountValue() {
        return Formats.CURRENCY.formatValue(getDiscountValue());
    }

    public String printValue() {
        return Formats.CURRENCY.formatValue(getValue());
    }
    public String printPromoType() {
          
       return getPromoType();
    }

 
}
