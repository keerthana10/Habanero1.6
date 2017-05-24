//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software FoundatioSELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT,n, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POailS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.forms;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.model.Field;
import com.openbravo.data.model.Row;
import com.openbravo.pos.admin.RoleInfo;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.inventory.AttributeSetInfo;
import com.openbravo.pos.inventory.ListInfo;
import com.openbravo.pos.inventory.TaxCustCategoryInfo;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.inventory.PrAreaMapInfo;

import com.openbravo.pos.inventory.ProductionAreaTypeInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.inventory.UomListInfo;
import com.openbravo.pos.mant.FloorsInfo;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.payment.VouchersList;
import com.openbravo.pos.sales.ProcessInfo;
import com.openbravo.pos.ticket.FindHoldTicketsInfo;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.ResettlePaymentInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TicketMergeTaxInfo;
import com.openbravo.pos.ticket.TicketServiceChargeInfo;
import com.openbravo.pos.ticket.TicketTaxInfo;
import com.openbravo.util.date.DateFormats;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeTaxInfo;
import com.sysfore.pos.panels.CardTypeInfo;
import com.sysfore.pos.panels.PosActionsInfo;
import com.sysfore.pos.salesdump.BillIdInfo;
import com.sysfore.pos.salesdump.SalesInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class DataLogicSales extends BeanFactoryDataSingle {

    protected Session s;
    protected Datas[] auxiliarDatas;
    protected Datas[] stockdiaryDatas;
    // protected Datas[] productcatDatas;
    protected Datas[] paymenttabledatas;
    protected Datas[] stockdatas;
    protected Row productsRow;
    java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList = null;
    Logger printlogger = Logger.getLogger("PrintLog");
    Logger settlelogger = Logger.getLogger("SettleLog");
    Logger logger = Logger.getLogger("MyLog");
Object[] record = null;
    Date transactionDate = null;
    /**
     * Creates a new instance of SentenceContainerGeneric
     */
    public DataLogicSales() {
        stockdiaryDatas = new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.TIMESTAMP};
        paymenttabledatas = new Datas[]{Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.DOUBLE};
        stockdatas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE};
        auxiliarDatas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};

        productsRow = new Row(
                new Field("ID", Datas.STRING, Formats.STRING),
                new Field(AppLocal.getIntString("label.prodref"), Datas.STRING, Formats.STRING, true, true, true),
                new Field(AppLocal.getIntString("label.prodbarcode"), Datas.STRING, Formats.STRING, false, true, true),
                new Field(AppLocal.getIntString("label.prodname"), Datas.STRING, Formats.STRING, true, true, true),
                new Field("ISCOM", Datas.BOOLEAN, Formats.BOOLEAN),
                new Field("ISSCALE", Datas.BOOLEAN, Formats.BOOLEAN),
                new Field(AppLocal.getIntString("label.prodpricebuy"), Datas.DOUBLE, Formats.CURRENCY, false, true, true),
                new Field(AppLocal.getIntString("label.prodpricesell"), Datas.DOUBLE, Formats.CURRENCY, false, true, true),
                new Field(AppLocal.getIntString("label.prodcategory"), Datas.STRING, Formats.STRING, false, false, true),
                new Field(AppLocal.getIntString("label.taxcategory"), Datas.STRING, Formats.STRING, false, false, true),
                new Field(AppLocal.getIntString("label.attributeset"), Datas.STRING, Formats.STRING, false, false, true),
                new Field("IMAGE", Datas.IMAGE, Formats.NULL),
                new Field("STOCKCOST", Datas.DOUBLE, Formats.CURRENCY),
                new Field("STOCKVOLUME", Datas.DOUBLE, Formats.DOUBLE),
                new Field("ISCATALOG", Datas.BOOLEAN, Formats.BOOLEAN),
                new Field("CATORDER", Datas.INT, Formats.INT),
                new Field("PROPERTIES", Datas.BYTES, Formats.NULL),
                new Field("MRP", Datas.DOUBLE, Formats.CURRENCY, false, true, true),
                new Field("ISACTIVE", Datas.STRING, Formats.STRING),
                new Field("ISSALESITEM", Datas.STRING, Formats.STRING),
                new Field("ISPURCHASEITEM", Datas.STRING, Formats.STRING),
                new Field("UOM", Datas.STRING, Formats.STRING, false, false, true),
                new Field("SERVICECHARGE", Datas.STRING, Formats.STRING, false, false, true),
                new Field("SERVICETAX", Datas.STRING, Formats.STRING, false, false, true),
                new Field("PRODUCTTYPE", Datas.STRING, Formats.STRING, false, false, true),
                new Field("PRODUCTIONAREATYPE", Datas.STRING, Formats.STRING, false, false, true),
                new Field("POPULAR", Datas.STRING, Formats.STRING),
                new Field("ISADDON", Datas.STRING, Formats.STRING));
    }

    public void init(Session s) {
        this.s = s;
    }

    public final Row getProductsRow() {
        return productsRow;
    }

    // Utilidades de productos
    public final ProductInfoExt getProductInfo(String id) throws BasicException {
        System.out.println("products selection 1");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND P.ID = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(id);
    }

    public final ProductInfoExt getMenuProductInfo(String id) throws BasicException {
        System.out.println("products selection 1");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY,M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C,MENUPRICELIST M  WHERE U.ID=P.UOM AND M.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND P.ID = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(id);
    }

    public final ProductInfoExt getProductInfoByCode(String sCode) throws BasicException {
        System.out.println("products selection 2");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX ,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND  P.CODE = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(sCode);
    }

    public final List<ProductInfoExt> getProductInfoByItemCode(String id) throws BasicException {
        System.out.println("products selection 3");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX ,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND  P.ITEMCODE = '" + id + "' AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", null, ProductInfoExt.getSerializerRead()).list();
    }

    //method called while loading tables  
    public final List<ProductInfoExt> getProductDetails() throws BasicException {
        System.out.println("products selection 4");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND  P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ORDER BY P.NAME", null, ProductInfoExt.getSerializerRead()).list();
    }

    //Item search filter
    public final List<ProductInfoExt> getProductName(String name) throws BasicException {
        System.out.println("products selection 5");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND  P.NAME LIKE '" + name + "%' AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ORDER BY P.NAME", null, ProductInfoExt.getSerializerRead()).list();
    }
    //Item search filter

    public final List<ProductInfoExt> getProductInfoById(String id) throws BasicException {
        System.out.println("products selection 6");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND  P.ID = '" + id + "' AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", null, ProductInfoExt.getSerializerRead()).list();
    }

    public String getProductId(String itemCode) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM PRODUCTS WHERE ITEMCODE ='" + itemCode + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);
    }

    public String getBusinessTypeId(String businessType) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM BUSINESSTYPE WHERE NAME='" + businessType + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);
    }

    public final List<BusinessServiceTaxInfo> getBusinessServiceTax(String businesstype) throws BasicException {
        return new PreparedSentence(s, "SELECT TML.TAXID,T.RATE, TM.ISSERVICETAXINCLUDED FROM TAXMAPPING TM,TAXMAPPINGLINES TML, TAXES T WHERE TM.ID=TML.TAXMAPID AND TML.TAXID=T.ID AND TM.BUSINESSTYPE='" + businesstype + "' ", null, new SerializerReadClass(BusinessServiceTaxInfo.class)).list();
    }

    public final List<BusinessServiceChargeInfo> getBusinessServiceCharge(String businesstype) throws BasicException {
        return new PreparedSentence(s, "SELECT TML.TAXID,S.RATE, TM.ISSERVICECHARGEINCLUDED FROM TAXMAPPING TM,TAXMAPPINGLINES TML, SERVICECHARGE S WHERE TM.ID=TML.TAXMAPID AND TML.TAXID=S.ID AND TM.BUSINESSTYPE='" + businesstype + "' ", null, new SerializerReadClass(BusinessServiceChargeInfo.class)).list();
    }

    public final List<BusinessServiceChargeInfo> getServiceCharge() throws BasicException {
        return new PreparedSentence(s, "SELECT id,rate FROM SERVICECHARGE ", null, new SerializerReadClass(BusinessServiceChargeInfo.class)).list();
    }

    public final List<BusinessServiceTaxInfo> getServiceTax() throws BasicException {
        return new PreparedSentence(s, "SELECT ID,RATE FROM TAXES WHERE ISSALESTAX='Y' ", null, new SerializerReadClass(BusinessServiceTaxInfo.class)).list();
    }

    //Item search filter
    public final ProductInfoExt getProductInfoByReference(String sReference) throws BasicException {
        System.out.println("products selection 7");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME  "
                + "FROM PRODUCTS P,UOM U  WHERE U.ID=list<reP.UOM AND  P.REFERENCE = ? AND P.ISACTIVE='Y'", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(sReference);
    }

    // Catalogo de productos
    public final List<CategoryInfo> getRootCategories() throws BasicException {
        return new PreparedSentence(s, "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE PARENTID IS NULL ORDER BY NAME", null, CategoryInfo.getSerializerRead()).list();
    }

    public final List<CustomerListInfo> getCustomerList() throws BasicException {
        return new PreparedSentence(s, "SELECT CUSTOMERID, NAME, PHONE, ID,ISCREDITCUSTOMER FROM CUSTOMERS WHERE ISCUSTOMER = 0 AND VISIBLE=1 ORDER BY NAME", null, new SerializerReadClass(CustomerListInfo.class)).list();
    }

    public final List<CustomerListInfo> getCustomerListName(String name) throws BasicException {
        return new PreparedSentence(s, "SELECT CUSTOMERID, NAME, PHONE, ID,ISCREDITCUSTOMER  FROM CUSTOMERS WHERE NAME LIKE '" + name + "%' ORDER BY NAME", null, new SerializerReadClass(CustomerListInfo.class)).list();
    }

    public final List<CustomerListInfo> getCustomerDetails(String name) throws BasicException {
        return new PreparedSentence(s, "SELECT CUSTOMERID, NAME, PHONE, ID,ISCREDITCUSTOMER  FROM CUSTOMERS WHERE " + name + " ", null, new SerializerReadClass(CustomerListInfo.class)).list();
    }

    public int getCustomerCount(String customerId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CUSTOMERS WHERE CUSTOMERID='" + customerId + "' AND ISCUSTOMER =0 AND VISIBLE=1 ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getBusinessTypeCount(String businessType) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT count(*) FROM BUSINESSTYPE WHERE NAME='" + businessType + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getCusCount(String customerId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CUSTOMERS C,TICKETS T WHERE C.ID=T.CUSTOMER AND C.ID='" + customerId + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getItemCount(String itemCode) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PRODUCTS WHERE ITEMCODE='" + itemCode + "' AND ISACTIVE='Y'  ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public final List<CategoryInfo> getSubcategories(String category) throws BasicException {
        return new PreparedSentence(s, "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE PARENTID = ? ORDER BY NAME", SerializerWriteString.INSTANCE, CategoryInfo.getSerializerRead()).list(category);
    }

    public List<ProductInfoExt> getProductCatalog(String category) throws BasicException {
        System.out.println("products selection 8");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP, U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, UOM U,CATEGORIES C WHERE P.ID = O.PRODUCT AND U.ID=P.UOM AND P.CATEGORY=C.ID AND P.CATEGORY = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(category);
    }
    //method called on clicking product 

    public List<ProductInfoExt> getProductComments(String id) throws BasicException {
        System.out.println("products selection 9");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, PRODUCTS_COM M, UOM U,CATEGORIES C  WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2  AND U.ID=P.UOM AND P.CATEGORY=C.ID AND M.PRODUCT = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' "
                + "AND P.ISCOM = " + s.DB.TRUE() + " "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(id);
    }

    // Products list
    public final SentenceList getProductList() {
        System.out.println("products selection 10");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P, UOM U,CATEGORIES C  WHERE  U.ID=P.UOM AND P.CATEGORY=C.ID  AND ?(QBF_FILTER) AND P.ISACTIVE='Y' ORDER BY P.REFERENCE", new String[]{"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }

    public final SentenceList getPurchaseProductList() {
        System.out.println("products selection 11");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P, UOM U,CATEGORIES C  WHERE  U.ID=P.UOM AND P.CATEGORY=C.ID AND ?(QBF_FILTER) AND P.ISACTIVE='Y' AND P.ISPURCHASEITEM = 'Y' ORDER BY P.REFERENCE", new String[]{"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }
    // Products list

    public SentenceList getProductListNormal() {
        System.out.println("products selection 12");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C WHERE P.ISCOM = " + s.DB.FALSE() + " AND U.ID=P.UOM AND P.CATEGORY=C.ID AND ?(QBF_FILTER) AND P.ISACTIVE='Y' ORDER BY P.REFERENCE", new String[]{"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }

    public SentenceList getPurchaseProductListNormal() {
        System.out.println("products selection 13");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX ,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, UOM U,CATEGORIES C WHERE P.ISCOM = " + s.DB.FALSE() + " AND U.ID=P.UOM AND P.CATEGORY=C.ID AND ?(QBF_FILTER) AND P.ISACTIVE='Y' AND P.ISPURCHASEITEM='Y' ORDER BY P.REFERENCE", new String[]{"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }

    //Auxiliar list for a filter
    public SentenceList getProductListAuxiliar() {
        System.out.println("products selection 14");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX ,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C WHERE P.ISCOM = " + s.DB.TRUE() + " AND U.ID=P.UOM AND  P.CATEGORY=C.ID AND ?(QBF_FILTER) AND P.ISACTIVE='Y' ORDER BY P.REFERENCE", new String[]{"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }

    public SentenceList getPurchaseProductListAuxiliar() {
        System.out.println("products selection 15");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX ,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P ,UOM U,CATEGORIES C  WHERE P.ISCOM = " + s.DB.TRUE() + " AND U.ID=P.UOM AND  P.CATEGORY=C.ID AND ?(QBF_FILTER) AND P.ISACTIVE='Y' AND P.ISPURCHASEITEM='Y' ORDER BY P.REFERENCE", new String[]{"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), ProductInfoExt.getSerializerRead());
    }
    // Tickets and Receipt list 

    public SentenceList getTicketsList() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String currentDate = dateformat.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterDay = format.format(cal.getTime());

        return new StaticSentence(s, new QBFBuilder(
                "SELECT T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME, SUM(PM.TOTAL),T.DOCUMENTNO "
                + "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PAYMENTS PM ON R.ID = PM.RECEIPT LEFT OUTER JOIN CUSTOMERS C ON C.ID = T.CUSTOMER LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID "
                + "WHERE  R.ISCREDITINVOICED = 'N' AND T.ISCANCELTICKET='N' AND T.DELIVERYSTATUS!='Y' AND ISTAKEAWAY='N' AND ?(QBF_FILTER) GROUP BY T.ID, T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME ORDER BY R.DATENEW DESC, T.TICKETID", new String[]{"T.TICKETID", "T.TICKETTYPE", "PM.TOTAL", "R.DATENEW", "R.DATENEW", "P.NAME", "C.NAME"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), new SerializerReadClass(FindTicketsInfo.class));
    }

    //Tickets and Receipt list older
//    public SentenceList getTicketsList() {
//        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//        String currentDate = dateformat.format(new Date());
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        String yesterDay = format.format(cal.getTime());
//        
//        return new StaticSentence(s
//            , new QBFBuilder(
//            "SELECT T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME, SUM(PM.TOTAL),T.DOCUMENTNO "+
//            "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PAYMENTS PM ON R.ID = PM.RECEIPT LEFT OUTER JOIN CUSTOMERS C ON C.ID = T.CUSTOMER LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID " +
//            "WHERE T.STATUS=0 AND R.ISCREDITINVOICED = 'N' AND T.ISCANCELTICKET='N' AND T.DELIVERYSTATUS!='Y' AND R.DATENEW>='"+yesterDay+"' AND R.DATENEW<'"+currentDate+"'  AND ?(QBF_FILTER) GROUP BY T.ID, T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME ORDER BY R.DATENEW DESC, T.TICKETID", new String[] {"T.TICKETID", "T.TICKETTYPE", "PM.TOTAL", "R.DATENEW", "R.DATENEW", "P.NAME", "C.NAME"})
//            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
//            , new SerializerReadClass(FindTicketsInfo.class));
//    }
    //User list
    public final SentenceList getUserList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM PEOPLE ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCategoryInfo(
                        dr.getString(1),
                        dr.getString(2));
            }
        });
    }

    // Listados para combo
    public final SentenceList getTaxList() {
        return new StaticSentence(s, "SELECT ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER, ISSALESTAX, ISPURCHASETAX,ISSERVICETAX,DEBITACCOUNT,CREDITACCOUNT,ISSERVICECHARGE,BASEAMOUNT,TAXBASEID,ISTAKEAWAY FROM TAXES ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxInfo(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getString(3),
                        dr.getString(4),
                        dr.getString(5),
                        dr.getDouble(6).doubleValue(),
                        dr.getBoolean(7).booleanValue(),
                        dr.getInt(8),
                        dr.getString(9),
                        dr.getString(10),
                        dr.getString(11),
                        dr.getString(12),
                        dr.getString(13),
                        dr.getString(14),
                        dr.getString(15),
                        dr.getString(16),
                        dr.getString(17));

            }
        });
    }

    //Changed the query to implement erp tax calculation
    public final SentenceList getRetailTaxList() {
        return new StaticSentence(s, "SELECT ID, NAME, CATEGORY, "
                + "CUSTCATEGORY, PARENTID, RATE, RATECASCADE, "
                + "RATEORDER, ISSALESTAX, ISPURCHASETAX,ISSERVICETAX,"
                + "DEBITACCOUNT,CREDITACCOUNT,ISSERVICECHARGE,BASEAMOUNT,TAXBASEID,ISTAKEAWAY FROM TAXES WHERE ISSALESTAX='Y' ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxInfo(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getString(3),
                        dr.getString(4),
                        dr.getString(5),
                        dr.getDouble(6).doubleValue(),
                        dr.getBoolean(7).booleanValue(),
                        dr.getInt(8),
                        dr.getString(9),
                        dr.getString(10),
                        dr.getString(11),
                        dr.getString(12),
                        dr.getString(13),
                        dr.getString(14),
                        dr.getString(15),
                        dr.getString(16),
                        dr.getString(17));

            }
        });
    }

    public final SentenceList getCategoriesList() {
        return new StaticSentence(s, "SELECT ID, NAME, IMAGE FROM CATEGORIES ORDER BY NAME", null, CategoryInfo.getSerializerRead());
    }

    public final SentenceList getTaxCustCategoriesList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM TAXCUSTCATEGORIES ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCustCategoryInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public final SentenceList getTaxCategoriesList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM TAXCATEGORIES ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public final SentenceList getAttributeSetList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM ATTRIBUTESET ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new AttributeSetInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public final SentenceList getLocationsList() {
        return new StaticSentence(s, "SELECT ID, NAME, ADDRESS FROM LOCATIONS ORDER BY NAME", null, new SerializerReadClass(LocationInfo.class));
    }

    public final SentenceList getFloorsList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM FLOORS ORDER BY NAME", null, new SerializerReadClass(FloorsInfo.class));
    }

    public CustomerInfoExt findCustomerExt(String card) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s, "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, TAXCATEGORY, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT"
                + ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX"
                + ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY"
                + " FROM CUSTOMERS WHERE CARD = ? AND VISIBLE = " + s.DB.TRUE(), SerializerWriteString.INSTANCE, new CustomerExtRead()).find(card);
    }

    public CustomerInfoExt loadCustomerExt(String id) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s, "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, TAXCATEGORY, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT"
                + ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX"
                + ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY"
                + " FROM CUSTOMERS WHERE ID = ?", SerializerWriteString.INSTANCE, new CustomerExtRead()).find(id);
    }

    public final boolean isCashActive(String id) throws BasicException {

        return new PreparedSentence(s,
                "SELECT MONEY FROM CLOSEDCASH WHERE DATEEND IS NULL AND MONEY = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(id)
                != null;
    }

    public List<TicketMergeTaxInfo> getMergeTaxInfo(String billId) throws BasicException {

        return (List<TicketMergeTaxInfo>) new PreparedSentence(s, " SELECT TAXID, BASE,AMOUNT FROM TAXLINES WHERE RECEIPT IN (" + billId + ")", null, new SerializerReadClass(TicketMergeTaxInfo.class)).list();

    }

    public final TicketInfo loadTicketBasedOnBillNo(final String documentNo) throws BasicException {
        TicketInfo ticket = (TicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER,T.DISCOUNTRATE,T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.DOCUMENTNO= ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(TicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);
                //   setInt(2, ticketid);
            }
        });

        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, L.ATTRIBUTES, L.DISCOUNT, P.NAME "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT  AND L.TICKET = ? ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(TicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final RetailTicketInfo loadEditTicketBasedOnBillNo(final String documentNo) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER,T.CUSTOMERDISCOUNT,T.DISCOUNTRATE,T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.DOCUMENTNO= ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);
                //   setInt(2, ticketid);
            }
        });

        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT,L.ATTRIBUTES, L.DISCOUNT, P.NAME, 0 AS PREPARESTAUS  "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT  AND L.TICKET = ? ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final TicketInfo loadTicketBasedOnBillId(final String documentNo, final java.util.List<BillIdInfo> billId, final String billNo) throws BasicException {
        System.out.println("documentNo---" + documentNo + "---" + billId);

        TicketInfo ticket = (TicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER,T.DISCOUNTRATE,T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.DOCUMENTNO= ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(TicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);
                //   setInt(2, ticketid);
            }
        });

        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));
            List<TicketLineInfo> allLines = new ArrayList<TicketLineInfo>();
            for (BillIdInfo billInfo : billId) {
                List<TicketLineInfo> info = new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, L.ATTRIBUTES, L.DISCOUNT, P.NAME "
                        + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT  AND L.TICKET IN (?) GROUP BY L.PRODUCT ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(TicketLineInfo.class)).list(billInfo.getId());
                allLines.addAll(info);

            }
            ticket.setLines(allLines);
            List<PaymentInfo> allPaymentLines = new ArrayList<PaymentInfo>();
            for (BillIdInfo billInfo : billId) {
                List<PaymentInfo> payInfo = new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId());
                allPaymentLines.addAll(payInfo);
            }

        }
        return ticket;
    }

    public final TicketInfo loadTicket(final int tickettype, final int ticketid) throws BasicException {
        TicketInfo ticket = (TicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER,T.DISCOUNTRATE,T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.TICKETTYPE = ? AND T.TICKETID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(TicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setInt(1, tickettype);
                setInt(2, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER,T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT, L.ATTRIBUTES, L.DISCOUNT, P.NAME "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT  AND L.TICKET = ? ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(TicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final RetailTicketInfo loadRetailTicket(final int tickettype, final int ticketid) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER, T.CUSTOMERDISCOUNT, T.DISCOUNTRATE, T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.TICKETTYPE = ? AND T.TICKETID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setInt(1, tickettype);
                setInt(2, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));
            System.out.println("ticket.getid" + ticket.getId());
            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT,L.ATTRIBUTES, L.DISCOUNT, P.NAME,0 AS PREPARESTATUS "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT AND L.TICKET = ? AND T.ISSALESTAX='Y' ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final RetailTicketInfo loadHomeDeliveryTicket(final String documentNo) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER, T.CUSTOMERDISCOUNT, T.DISCOUNTRATE, T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.DOCUMENTNO = ? ", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);

            }
        });
        ServiceChargeTaxInfo service = (ServiceChargeTaxInfo) new PreparedSentence(s, "SELECT SC.RATE, T.SERVICECHARGEAMT, ST.RATE, T.SERVICETAXAMT  FROM TICKETS T LEFT OUTER JOIN SERVICECHARGE SC ON SC.ID = T.SERVICECHARGEID LEFT OUTER JOIN TAXES ST ON ST.ID = T.SERVICETAXID WHERE T.DOCUMENTNO = ? ", SerializerWriteParams.INSTANCE, new SerializerReadClass(ServiceChargeTaxInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);

            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT,L.ATTRIBUTES, L.DISCOUNT, P.NAME "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT AND L.TICKET = ? AND T.ISSALESTAX='Y' ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final ServiceChargeTaxInfo loadServiceChargeTax(final String documentNo) throws BasicException {

        ServiceChargeTaxInfo service = (ServiceChargeTaxInfo) new PreparedSentence(s, "SELECT COALESCE(SC.RATE, 0), T.SERVICECHARGEAMT, COALESCE(ST.RATE, 0), T.SERVICETAXAMT  FROM TICKETS T LEFT OUTER JOIN SERVICECHARGE SC ON SC.ID = T.SERVICECHARGEID LEFT OUTER JOIN TAXES ST ON ST.ID = T.SERVICETAXID WHERE T.DOCUMENTNO = ? ", SerializerWriteParams.INSTANCE, new SerializerReadClass(ServiceChargeTaxInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, documentNo);

            }
        });

        return service;
    }

    public final RetailTicketInfo loadEditRetailTicket(final int tickettype, final String ticketid) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT T.ID, T.TICKETTYPE, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER, T.CUSTOMERDISCOUNT, T.DISCOUNTRATE,T.DOCUMENTNO FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.TICKETTYPE = ? AND T.ID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                setInt(1, tickettype);
                setString(2, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT L.TICKET, L.LINE, L.PRODUCT, L.ATTRIBUTESETINSTANCE_ID, L.UNITS, L.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT, L.ATTRIBUTES, L.DISCOUNT, P.NAME,0 AS PREPARESTATUS "
                    + "FROM TICKETLINES L, TAXES T, PRODUCTS P WHERE L.TAXID = T.ID AND P.ID=L.PRODUCT AND L.TICKET = ? AND T.ISSALESTAX='Y' ORDER BY L.LINE", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s, "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public PreparedSentence getDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(CREDITNOTENO) FROM CREDITNOTE", null, SerializerReadString.INSTANCE);

    }

    public PreparedSentence getTicketDocumentNo() throws BasicException {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNO) FROM TICKETS", null, SerializerReadString.INSTANCE);

    }

    public final void saveTicket(final TicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String paymentMode) throws BasicException {
        System.out.println("saicketveT");
        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
                if (ticket.getTicketId() == 0) {
                    switch (ticket.getTicketType()) {
                        case TicketInfo.RECEIPT_NORMAL:
                            ticket.setTicketId(getNextTicketIndex().intValue());
                            break;
                        case TicketInfo.RECEIPT_REFUND:

                            new PreparedSentence(s, "INSERT INTO CREDITNOTE (ID, CREDITNOTENO, TICKETID, CUSTOMER, AMOUNT, STATUS, CREATEDBY, UPDATEDBY, CREATED, UPDATED, VALIDITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, creditNo);
                                    setInt(3, ticket.getTicketId());
                                    if (ticket.getCustomer() == null) {
                                        setString(4, null);
                                    } else {
                                        setString(4, ticket.getCustomer().getName());
                                    }
                                    //  }else{get
                                    //     setString(4, "");
                                    // }
                                    setDouble(5, (-1 * ticket.getTotalAfterDiscount()));
                                    setString(6, "N");
                                    setString(7, ticket.getUser().getId());
                                    setString(8, ticket.getUser().getId());
                                    setTimestamp(9, ticket.getDate());
                                    setTimestamp(10, ticket.getDate());
                                    setTimestamp(11, ticket.getNewDate());

                                    // setTimestamp(9, ticket.getDate());
                                    // setTimestamp(10, ticket.getDate());
                                    // setTimestamp(11, ticket.getDate());

                                }
                            });

                            break;
                        case TicketInfo.RECEIPT_PAYMENT:
                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
                            break;
                        default:
                            throw new BasicException();
                    }
                }

                // new receipt
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY, UPDATED) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,DISCOUNTRATE,BILLAMOUNT,CUSTOMERDISCOUNT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getTicketType());
                        setString(3, ticketDocument);
                        setInt(4, ticket.getTicketId());
                        setString(5, ticket.getUser().getId());
                        setString(6, ticket.getCustomerId());
                        setDouble(7, ticket.getBillDiscount());
                        setDouble(8, ticket.getLeastValueDiscount());
                        setString(9, ticket.getRate());
                        setDouble(10, ticket.getTotal());
                        setDouble(11, ticket.getRefundDiscount());
                    }
                });
                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final TicketLineInfo l : ticket.getLines()) {

                    double price = 0.0;
                    if (pdtBuyGetPriceList != null) {
                        for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {
                            System.out.println("DataLogic Sales "+ i);
                            String productId = pdtBuyGetPriceList.get(i).getProductID();
                            if (l.getProductID().equals(productId)) {
                                if (pdtBuyGetPriceList.get(i).getQuantity() == l.getMultiply()) {
                                    l.setPrice(0.0);
                                } else {


                                    double qty = l.getMultiply() - pdtBuyGetPriceList.get(i).getQuantity();
                                    l.setMultiply(qty);
                                    pdtBuyGetPriceList.get(i).setProductId(l.getProductID());
                                    pdtBuyGetPriceList.get(i).setPrice(0.0);
                                    pdtBuyGetPriceList.get(i).setQuantity(pdtBuyGetPriceList.get(i).getQuantity());
                                    pdtBuyGetPriceList.get(i).setTaxCat(l.getTaxInfo().getId());
                                    pdtBuyGetPriceList.get(i).setCategory(l.getProductCategoryID());
                                    pdtBuyGetPriceList.get(i).setAttributesId(l.getProductAttSetId());
                                    pdtBuyGetPriceList.get(i).setCampaignId(l.getCampaignId());
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(i));
                                    ticket.setPriceInfo(pdtLeastPriceList);
//                                     for(int k=0;k<pdtLeastPriceList.size();k++){
                                    // "CA73D86920FC491799A3F5F0EEC28E3F" if(l.getProductID()==pdtLeastPriceList.get(k).getProductID() && l.getMultiply()!=pdtLeastPriceList.get(k).getQuantity()){

                                    //   insertTicket(pdtLeastPriceList, ticket, l.getTicketLine()+k);
                                    //  }
//                                     }
                                    //insertTicket(ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId());
                                }
                            }

                        }
                    }
//                                            


                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            setDouble(6, l.getDiscount());
                            setDouble(7, l.getPrice());

                            setDouble(8, l.getDiscountPrice());
                            setString(9, l.getTaxInfo().getId());
                            setString(10, l.getProductAttSetId());
                            setString(11, l.getCampaignId());

                        }
                    });
                }

                SentenceExec ticketlineinsert = new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteBuilder.INSTANCE);

                for (TicketLineInfo l : ticket.getLines()) {
                    // ticketlineinsert.exec(l);
                    if (l.getProductID() != null) {
                        // update the stock
                        getStockDiaryInsert().exec(new Object[]{
                            (UUID.randomUUID().toString()).replaceAll("-", ""),
                            ticket.getDate(),
                            l.getMultiply() < 0.0
                            ? MovementReason.IN_REFUND.getKey()
                            : MovementReason.OUT_SALE.getKey(),
                            location,
                            l.getProductID(),
                            l.getProductAttSetInstId(),
                            new Double(-l.getMultiply()),
                            new Double(0.0),
                            new Double(0.0),
                            new Double(l.getPrice()),
                            null,
                            null,
                            null
                        });
                    }
                }
                final String paymentId = (UUID.randomUUID().toString()).replaceAll("-", "");
                new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG) VALUES (?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, paymentId);
                        setString(2, ticket.getId());
                        setString(3, paymentMode);
                        setDouble(4, ticket.getTotal());
                        setString(5, ticket.getTransactionID());
                        setBytes(6, (byte[]) Formats.BYTEA.parseValue(null));
                    }
                });
                // SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG) VALUES (?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);

                SentenceExec paymentsplits = new PreparedSentence(s, "INSERT INTO PAYMENTSPLITS (ID, PAYMENTS_ID, IDENTIFIER , AMOUNT) VALUES (?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                SentenceExec updateCreditNote = new PreparedSentence(s, "UPDATE CREDITNOTE SET STATUS = ? WHERE CREDITNOTENO=?", SerializerWriteParams.INSTANCE);

                //  for (final PaymentInfo p : ticket.getPayments()) {
//
//                    paymentinsert.exec(new DataParams() {
//
//                        public void writeValues() throws BasicException {
//                            setString(1, paymentId);
//                            setString(2, ticket.getId());
//                            setString(3, "Cash");
//                            setDouble(4, ticket.getTotal());
//                            setString(5, ticket.getTransactionID());
//                            setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
//                        }
//                    });

//
//                     if ("Voucher".equals(p.getName())) {
//
//
//                            for (final VouchersList v : paysplits) {
//                                paymentsplits.exec(new DataParams() {
//
//                                    @Override
//                                    public void writeValues() throws BasicException {
//                                        setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                                        setString(2, paymentId);
//                                        setString(3, v.getVoucher());
//                                        setDouble(4, v.getAmount());
//                                    }
//                                });
//                                updateCreditNote.exec(new DataParams() {
//
//                                @Override
//                                    public void writeValues() throws BasicException {
//                                        setString(1, "Y");
//                                        setString(2, v.getVoucher());
//
//                                    }
//                                });
//                            }
//                        paysplits.clear();
//                    }
//
//                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {
//
//                        // udate customer fields...
//                        ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());
//
//                        // save customer fields...
//                        getDebtUpdate().exec(new DataParams() { public void writeValues() throws BasicException {
//                            setDouble(1, ticket.getCustomer().getCurdebt());
//                            setTimestamp(2, ticket.getCustomer().getCurdate());
//                            setString(3, ticket.getCustomer().getId());
//                        }});
//                    }
//                }

                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        taxlinesinsert.exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                setString(2, ticket.getId());
                                setString(3, tickettax.getTaxInfo().getId());
                                setDouble(4, tickettax.getSubTotal() - ticket.getLeastValueDiscount());
                                setDouble(5, (tickettax.getTax() - tickettax.getTaxAmount()));
                            }
                        });
                    }
                }

                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveNonChargableTicket(final RetailTicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String chequeNos, final String deliveryBoy, final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status, final String isCredit, final String isPaidStatus, final double tips, final String orderTaking, final String nonChargable) throws BasicException {

        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
                if (ticket.getTicketId() == 0) {
                    switch (ticket.getTicketType()) {
                        case TicketInfo.RECEIPT_NORMAL:
                            ticket.setTicketId(getNextTicketIndex().intValue());
                            break;
                        case TicketInfo.RECEIPT_REFUND:


                            break;
                        case TicketInfo.RECEIPT_PAYMENT:
                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
                            break;
                        default:
                            throw new BasicException();
                    }
                }

                // new receipt
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED,ACCOUNTDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, isCredit);
                        setString(9, "N");
                        setTimestamp(10, ticket.getAccountDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getTicketType());
                        setString(3, ticketDocument);
                        setInt(4, ticket.getTicketId());
                        setString(5, ticket.getUser().getId());
                        setString(6, ticket.getCustomerId());
                        setDouble(7, 0.0);
                        setDouble(8, 0.0);
                        setDouble(9, 0.0);
                        setString(10, "");
                        setString(11, homeDelivery);
                        setString(12, deliveryBoy);
                        setDouble(13, advanceissued);
                        setString(14, cod);
                        setString(15, status);
                        setDouble(16, 0.0);
                        setDouble(17, 0.0);
                        setString(18, isCredit);
                        setString(19, isPaidStatus);
                        setDouble(20, 0.0);
                        setString(21, ticket.getPlaceId());
                        setString(22, ticket.getServiceTaxId());
                        setDouble(23, 0.0);
                        setString(24, ticket.getServiceChargeId());
                        setDouble(25, 0.0);
                        setString(26, orderTaking);
                        setString(27, nonChargable);
                        setInt(28, ticket.getNoOfCovers());

                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {


//


                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,SERVICECHARGEID,SERVICETAXID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            setDouble(6, 0.0);
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, 0.0);
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                            setString(11, l.getCampaignId());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(12, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(13, servicetaxid);

                        }
                    });
                }


                SentenceExec ticketlineinsert = new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteBuilder.INSTANCE);

                for (RetailTicketLineInfo l : ticket.getLines()) {
                    // ticketlineinsert.exec(l);
                    if (l.getProductID() != null) {
                        // update the stock
                        getStockDiaryInsert().exec(new Object[]{
                            (UUID.randomUUID().toString()).replaceAll("-", ""),
                            ticket.getDate(),
                            l.getMultiply() < 0.0
                            ? MovementReason.IN_REFUND.getKey()
                            : MovementReason.OUT_SALE.getKey(),
                            location,
                            l.getProductID(),
                            l.getProductAttSetInstId(),
                            new Double(-l.getMultiply()),
                            new Double(0.0),
                            new Double(0.0),
                            new Double(l.getPrice()),
                            null,
                            null,
                            null
                        });
                    }
                }




                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT,ISSERVICETAX)  VALUES (?, ?, ?, ?, ?,?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        taxlinesinsert.exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                setString(2, ticket.getId());
                                setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal()- ticket.getLeastValueDiscount());
//                            setDouble(5, (tickettax.getTax()-tickettax.getTaxAmount()));
                                setDouble(4, 0.0);
                                setDouble(5, 0.0);
                                setString(6, tickettax.getTaxInfo().getIsServiceTax());
                            }
                        });
                    }
                }
                if (ticket.getServiceTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getServiceTaxes()) {
                        if (tickettax.getTaxInfo().getId() != null) {
                            taxlinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal()- ticket.getLeastValueDiscount());
//                            setDouble(5, (tickettax.getTax()-tickettax.getTaxAmount()));
                                    setDouble(4, 0.0);
                                    setDouble(5, 0.0);
                                    setString(6, tickettax.getTaxInfo().getIsServiceTax());
                                }
                            });
                        }
                    }
                }
                SentenceExec chargelinesinsert = new PreparedSentence(s, "INSERT INTO SERVICECHARGELINES (ID, RECEIPT, SERVICECHARGEID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getCharges() != null) {
                    for (final TicketServiceChargeInfo ticketcharge : ticket.getCharges()) {
                        if (ticketcharge.getServiceChargeInfo().getId() != null) {
                            chargelinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, ticketcharge.getServiceChargeInfo().getId());
                                    setDouble(4, ticketcharge.getSubTotal());
                                    setDouble(5, (ticketcharge.getRetailSCharge()));
                                }
                            });
                        }
                    }
                }
                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveRetailTicket(final RetailTicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String chequeNos, final String deliveryBoy, final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status, final String isCredit, final String isPaidStatus, final double tips, final String orderTaking, final String nonChargable) throws Exception {
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;
            int line = -1;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
//                if (ticket.getTicketId() == 0) {
//                    switch (ticket.getTicketType()) {
//                        case TicketInfo.RECEIPT_NORMAL:
//                            ticket.setTicketId(getNextTicketIndex().intValue());
//                            break;
//                        case TicketInfo.RECEIPT_REFUND:
//
//                              new PreparedSentence(s
//                            , "INSERT INTO CREDITNOTE (ID, CREDITNOTENO, TICKETID, CUSTOMER, AMOUNT, STATUS, CREATEDBY, UPDATEDBY, CREATED, UPDATED, VALIDITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
//                            , SerializerWriteParams.INSTANCE
//                            ).exec(new DataParams() { public void writeValues() throws BasicException {
//                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                                setString(2, creditNo);
//                                setInt(3,ticket.getTicketId());
//                                setString(4, ticket.getCustomer().getName());
//                                setDouble(5, Double.parseDouble(ticket.getRefundTotal()));
//                                setString(6, "N");
//                                setString(7,  ticket.getUser().getId());
//                                setString(8,  ticket.getUser().getId());
//                                setTimestamp(9, ticket.getDate());
//                                setTimestamp(10, ticket.getDate());
//                                setTimestamp(11, ticket.getNewDate());
//
//                               // setTimestamp(9, ticket.getDate());
//                               // setTimestamp(10, ticket.getDate());
//                               // setTimestamp(11, ticket.getDate
//                            }
//
//
//                            });
//                            break;
//                        case TicketInfo.RECEIPT_PAYMENT:
//                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
//                            break;
//                        default:
//                            throw new BasicException();
//                    }
//                }

                // new receipt
                record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED,ACCOUNTDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, isCredit);
                        setString(9, "N");
                        setTimestamp(10, ticket.getAccountDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,ROUNDOFFVALUE,PARENTBILLNO,DISCOUNTREASONID,CONTENT,DISCOUNTREASON,SOSORDERID,SWACHBHARATTAXAMT,NCCOMMENTS) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, deliveryBoy);
                        setDouble(14, advanceissued);
                        setString(15, cod);
                        setString(16, status);
                        setDouble(17, ticket.getTotal());
                        setDouble(18, creditAmt);
                        setString(19, isCredit);
                        setString(20, isPaidStatus);
                        setDouble(21, ticket.getTipAmt());
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        if (ticket.getIsNonChargable() == null) {
                            setString(28, nonChargable);
                        } else {
                            setString(28, ticket.getIsNonChargable());
                        }
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, ticket.getParentId());
                        setString(32, ticket.getDiscountReasonId());
                        setObject(33, ticket);
                        setString(34, ticket.getDiscountReasonText());
                        setString(35, ticket.getSosOrderId());
                        setDouble(36, ticket.getSwatchBharatTax());
                        setString(37, ticket.getNcComments());
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {
                    line = line + 1;
                    double price = 0.0;
//                    if (pdtBuyGetPriceList != null) {
//                        for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {
//                            String productId = pdtBuyGetPriceList.get(i).getProductID();
//                            if (l.getProductID().equals(productId)) {
//                                if (pdtBuyGetPriceList.get(i).getQuantity() == l.getMultiply()) {
//                                    l.setPrice(0.0);
//                                } else {
//
//
//                                    double qty = l.getMultiply() - pdtBuyGetPriceList.get(i).getQuantity();
//                                    l.setMultiply(qty);
//                                    pdtBuyGetPriceList.get(i).setProductId(l.getProductID());
//                                    pdtBuyGetPriceList.get(i).setPrice(0.0);
//                                    pdtBuyGetPriceList.get(i).setQuantity(pdtBuyGetPriceList.get(i).getQuantity());
//                                    pdtBuyGetPriceList.get(i).setTaxCat(l.getTaxInfo().getId());
//                                    pdtBuyGetPriceList.get(i).setCategory(l.getProductCategoryID());
//                                    pdtBuyGetPriceList.get(i).setAttributesId(l.getProductAttSetId());
//                                    pdtBuyGetPriceList.get(i).setCampaignId(l.getCampaignId());
//                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(i));
//                                    ticket.setPriceInfo(pdtLeastPriceList);
////                                     for(int k=0;k<pdtLeastPriceList.size();k++){
//                                    // "CA73D86920FC491799A3F5F0EEC28E3F" if(l.getProductID()==pdtLeastPriceList.get(k).getProductID() && l.getMultiply()!=pdtLeastPriceList.get(k).getQuantity()){
//
//                                    //   insertTicket(pdtLeastPriceList, ticket, l.getTicketLine()+k);
//                                    //  }
////                                     }
//                                    //insertTicket(ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId());
//                                }
//                            }
//
//                        }
//                    }
//

                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SOSORDERLINEID,SWACHBHARATTAXID,PRODUCTIONAREA,PRODUCTIONAREATYPE,PRIMARYADDON,ADDONID,SALEPRICE,PROMODISCOUNT,PROMODISCOUNTPRICE,LINEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, line);
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                if (l.getPromodiscountPercent() == 100.00) {
                                    setDouble(6, 0.00);
                                } else {
                                    setDouble(6, l.getDiscount());
                                }
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                            if(l.getPromoType().equals("BOGO")){
                                 //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            setString(19, l.getSosorderLineid());
                            String swachTaxId = null;

                            try {
                                swachTaxId = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swachTaxId = null;
                            }
                            setString(20, swachTaxId);
                            setString(21, l.getProductionArea());
                            setString(22, l.getProductionAreaType());
                            setInt(23, l.getPrimaryAddon());
                            setString(24, l.getAddonId());
                            setDouble(25, (l.getSalePrice()));
                            setDouble(26, l.getPromodiscountPercent());
                            setDouble(27, l.getOfferDiscount());
                            setString(28, UUID.randomUUID().toString().replaceAll("-", ""));
                        }
                    });
                    settlelogger.info("Bill Settled Successfully " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + ticket.getLinesCount() + "," + "Kot No: " + l.getKotid() + "," + "Table: " + ticket.getTableName() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + l.getProductName() + "," + "Qty: " + l.getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }


                /*    SentenceExec ticketlineinsert = new PreparedSentence(s
                 , "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                 , SerializerWriteBuilder.INSTANCE);

                 for (RetailTicketLineInfo l : ticket.getLines()) {
                 // ticketlineinsert.exec(l);
                 if (l.getProductID() != null)  {
                 // update the stock
                 getStockDiaryInsert().exec(new Object[] {
                 (UUID.randomUUID().toString()).replaceAll("-", ""),
                 ticket.getDate(),
                 l.getMultiply() < 0.0
                 ? MovementReason.IN_REFUND.getKey()
                 : MovementReason.OUT_SALE.getKey(),
                 location,
                 l.getProductID(),
                 l.getProductAttSetInstId(),
                 new Double(-l.getMultiply()),
                 new Double(0.0),
                 new Double(0.0),
                 new Double(l.getPrice()),
                 null,
                 null,
                 null
                 });
                 }
                 }
                 */

                System.out.println("ticket.getCustomerId()--" + ticket.getCustomerId());
                if (creditAmt != 0 && isCredit.equals("Y")) {
                    new PreparedSentence(s, "INSERT INTO CREDITSALE (ID, TICKETID, BILLNO, BILLDATE, CUSTOMER,BILLAMOUNT,CREDITAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        public void writeValues() throws BasicException {
                            setString(1, UUID.randomUUID().toString());
                            setString(2, ticket.getId());
                            setString(3, ticketDocument);
                            setTimestamp(4, ticket.getDate());
                            setString(5, ticket.getCustomerId());
                            setDouble(6, ticket.getTotal());
                            setDouble(7, creditAmt);

                        }
                    });
                }
//
                SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS,PAYMENTTYPE,DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);

                SentenceExec paymentsplits = new PreparedSentence(s, "INSERT INTO PAYMENTSPLITS (ID, PAYMENTS_ID, IDENTIFIER , AMOUNT) VALUES (?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                SentenceExec updateCreditNote = new PreparedSentence(s, "UPDATE CREDITNOTE SET STATUS = ? WHERE CREDITNOTENO=?", SerializerWriteParams.INSTANCE);

                for (final PaymentInfo p : ticket.getPayments()) {
                    final String paymentId = p.getID().replaceAll("-", "");
                    //     if(p.getTotal()!=0){
                    paymentinsert.exec(new DataParams() {
                        public void writeValues() throws BasicException {

                            setString(1, paymentId);
                            setString(2, ticket.getId());
                            setString(3, p.getName());
                            setDouble(4, p.getTotal());
                            setString(5, ticket.getTransactionID());
                            setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
                            if (p.getName().equals("Cheque") || p.getName().equals("Voucher")) {
                                setString(7, p.getVoucherNo());
                            } else {
                                setString(7, "");
                            }
                            System.out.println("p.getPaymentMode()" + p.getPaymentMode());
                            setString(8, p.getPaymentMode());
                            System.out.println("p.getPaymentMode()" + p.getDescription());

                            setString(9, p.getDescription());
                        }
                    });


                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

                        // udate customer fields...
                        ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());

                        // save customer fields...
                        getDebtUpdate().exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setDouble(1, ticket.getCustomer().getCurdebt());
                                setTimestamp(2, ticket.getCustomer().getCurdate());
                                setString(3, ticket.getCustomer().getId());
                            }
                        });
                    }
                    //}
                }

                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT,ISSERVICETAX,ISSWACHBHARATTAX)  VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (!tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            taxlinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                    setString(6, tickettax.getTaxInfo().getIsServiceTax());
                                    setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
                                }
                            });
                        }
                    }
                }

                SentenceExec chargelinesinsert = new PreparedSentence(s, "INSERT INTO SERVICECHARGELINES (ID, RECEIPT, SERVICECHARGEID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            chargelinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    System.out.println("constraint error======" + tickettax.getTaxInfo().getId());
                                    setString(3, tickettax.getTaxInfo().getId());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                }
                            });
                        }
                    }
                }
//                 if (ticket.getServiceTaxes() != null) {
//                    for (final TicketTaxInfo tickettax: ticket.getServiceTaxes()) {
//                        if(tickettax.getTaxInfo().getId()!= null){
//                        taxlinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, tickettax.getTaxInfo().getId());
//                            //setDouble(4, tickettax.getSubTotal());
//                            setDouble(4, tickettax.getServiceTaxBaseTotal());
//                            setDouble(5, (tickettax.getRetailServiceTax()));
//                            setString(6, tickettax.getTaxInfo().getIsServiceTax());
//                            setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
//                        }});}
//                    }
//                }
//                if (ticket.getSwachBharatTaxes() != null) {
//                    for (final TicketTaxInfo tickettax: ticket.getSwachBharatTaxes()) {
//                        if(tickettax.getTaxInfo().getId()!= null){
//                        taxlinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, tickettax.getTaxInfo().getId());
//                            //setDouble(4, tickettax.getSubTotal());
//                            setDouble(4, tickettax.getSwachBharatBaseTotal());
//                            setDouble(5, (tickettax.getRetailSwachBharatTax()));
//                            setString(6, tickettax.getTaxInfo().getIsServiceTax());
//                             setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
//                        }});}
//                    }
//                }
                  if (!ticket.getSplitValue().equals("Split")) {
                    logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after settle bill of table " + ticket.getTableName() + " id is " + ticket.getPlaceId());
                    deleteSharedTicket(ticket.getPlaceId());

                } else {
                    logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after splitted settle bill of table " + ticket.getTableName() + " id is " + ticket.getPlaceId());
                    deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
                }
                if (ticket.getPlaceId() != null && !(ticket.getSplitValue().equals("Split"))) {
                    deleteTableCovers(ticket.getPlaceId());
                }
                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveRetailTakeAwayTicket(final RetailTicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String chequeNos, final String deliveryBoy, final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status, final String isCredit, final String isPaidStatus, final double tips, final String orderTaking, final String nonChargable) throws Exception {
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;
            int line = -1;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
//                if (ticket.getTicketId() == 0) {
//                    switch (ticket.getTicketType()) {
//                        case TicketInfo.RECEIPT_NORMAL:
//                            ticket.setTicketId(getNextTicketIndex().intValue());
//                            break;
//                        case TicketInfo.RECEIPT_REFUND:
//
//                              new PreparedSentence(s
//                            , "INSERT INTO CREDITNOTE (ID, CREDITNOTENO, TICKETID, CUSTOMER, AMOUNT, STATUS, CREATEDBY, UPDATEDBY, CREATED, UPDATED, VALIDITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
//                            , SerializerWriteParams.INSTANCE
//                            ).exec(new DataParams() { public void writeValues() throws BasicException {
//                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                                setString(2, creditNo);
//                                setInt(3,ticket.getTicketId());
//                                setString(4, ticket.getCustomer().getName());
//                                setDouble(5, Double.parseDouble(ticket.getRefundTotal()));
//                                setString(6, "N");
//                                setString(7,  ticket.getUser().getId());
//                                setString(8,  ticket.getUser().getId());
//                                setTimestamp(9, ticket.getDate());
//                                setTimestamp(10, ticket.getDate());
//                                setTimestamp(11, ticket.getNewDate());
//
//                               // setTimestamp(9, ticket.getDate());
//                               // setTimestamp(10, ticket.getDate());
//                               // setTimestamp(11, ticket.getDate
//                            }
//
//
//                            });
//                            break;
//                        case TicketInfo.RECEIPT_PAYMENT:
//                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
//                            break;
//                        default:
//                            throw new BasicException();
//                    }
//                }

                // new receipt
                 record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED,ACCOUNTDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, isCredit);
                        setString(9, "N");
                        setTimestamp(10, ticket.getAccountDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,ROUNDOFFVALUE,PARENTBILLNO,DISCOUNTREASONID,CONTENT,DISCOUNTREASON,SOSORDERID,SWACHBHARATTAXAMT,NCCOMMENTS) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, deliveryBoy);
                        setDouble(14, advanceissued);
                        setString(15, cod);
                        setString(16, status);
                        setDouble(17, ticket.getTakeAwayTotal());
                        setDouble(18, creditAmt);
                        setString(19, isCredit);
                        setString(20, isPaidStatus);
                        setDouble(21, ticket.getTipAmt());
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        if (ticket.getIsNonChargable() == null) {
                            setString(28, nonChargable);
                        } else {
                            setString(28, ticket.getIsNonChargable());
                        }
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, ticket.getParentId());
                        setString(32, ticket.getDiscountReasonId());
                        setObject(33, ticket);
                        setString(34, ticket.getDiscountReasonText());
                        setString(35, ticket.getSosOrderId());
                        setDouble(36, ticket.getSwatchBharatTax());
                        setString(37, ticket.getNcComments());

                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {
                    line = line + 1;
                    double price = 0.0;
                    if (pdtBuyGetPriceList != null) {
                        for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {
                            String productId = pdtBuyGetPriceList.get(i).getProductID();
                            if (l.getProductID().equals(productId)) {
                                if (pdtBuyGetPriceList.get(i).getQuantity() == l.getMultiply()) {
                                    l.setPrice(0.0);
                                } else {


                                    double qty = l.getMultiply() - pdtBuyGetPriceList.get(i).getQuantity();
                                    l.setMultiply(qty);
                                    pdtBuyGetPriceList.get(i).setProductId(l.getProductID());
                                    pdtBuyGetPriceList.get(i).setPrice(0.0);
                                    pdtBuyGetPriceList.get(i).setQuantity(pdtBuyGetPriceList.get(i).getQuantity());
                                    pdtBuyGetPriceList.get(i).setTaxCat(l.getTaxInfo().getId());
                                    pdtBuyGetPriceList.get(i).setCategory(l.getProductCategoryID());
                                    pdtBuyGetPriceList.get(i).setAttributesId(l.getProductAttSetId());
                                    pdtBuyGetPriceList.get(i).setCampaignId(l.getCampaignId());
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(i));
                                    ticket.setPriceInfo(pdtLeastPriceList);
//                                     for(int k=0;k<pdtLeastPriceList.size();k++){
                                    // "CA73D86920FC491799A3F5F0EEC28E3F" if(l.getProductID()==pdtLeastPriceList.get(k).getProductID() && l.getMultiply()!=pdtLeastPriceList.get(k).getQuantity()){

                                    //   insertTicket(pdtLeastPriceList, ticket, l.getTicketLine()+k);
                                    //  }
//                                     }
                                    //insertTicket(ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId());
                                }
                            }

                        }
                    }
//

                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SOSORDERLINEID,SWACHBHARATTAXID,PRODUCTIONAREA,PRODUCTIONAREATYPE,PRIMARYADDON,ADDONID,SALEPRICE,PROMODISCOUNT,PROMODISCOUNTPRICE,LINEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, line);
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                if (l.getPromodiscountPercent() == 100.00) {
                                    setDouble(6, 0.00);
                                } else {
                                    setDouble(6, l.getDiscount());
                                }
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                           if(l.getPromoType().equals("BOGO")){
                                //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            setString(19, l.getSosorderLineid());
                            String swachTaxId = null;

                            try {
                                swachTaxId = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swachTaxId = null;
                            }
                            setString(20, swachTaxId);
                            setString(21, l.getProductionArea());
                            setString(22, l.getProductionAreaType());
                            setInt(23, l.getPrimaryAddon());
                            setString(24, l.getAddonId());
                            setDouble(25, (l.getSalePrice()));
                            setDouble(26, l.getPromodiscountPercent());
                            setDouble(27, l.getOfferDiscount());
                            setString(28, UUID.randomUUID().toString().replaceAll("-", ""));
                        }
                    });
                    settlelogger.info("Bill Settled Successfully " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + ticket.getLinesCount() + "," + "Kot No: " + l.getKotid() + "," + "Table: " + ticket.getTableName() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + l.getProductName() + "," + "Qty: " + l.getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }


                /*    SentenceExec ticketlineinsert = new PreparedSentence(s
                 , "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                 , SerializerWriteBuilder.INSTANCE);

                 for (RetailTicketLineInfo l : ticket.getLines()) {
                 // ticketlineinsert.exec(l);
                 if (l.getProductID() != null)  {
                 // update the stock
                 getStockDiaryInsert().exec(new Object[] {
                 (UUID.randomUUID().toString()).replaceAll("-", ""),
                 ticket.getDate(),
                 l.getMultiply() < 0.0
                 ? MovementReason.IN_REFUND.getKey()
                 : MovementReason.OUT_SALE.getKey(),
                 location,
                 l.getProductID(),
                 l.getProductAttSetInstId(),
                 new Double(-l.getMultiply()),
                 new Double(0.0),
                 new Double(0.0),
                 new Double(l.getPrice()),
                 null,
                 null,
                 null
                 });
                 }
                 }
                 */

                System.out.println("ticket.getCustomerId()--" + ticket.getCustomerId());
                if (creditAmt != 0 && isCredit.equals("Y")) {
                    new PreparedSentence(s, "INSERT INTO CREDITSALE (ID, TICKETID, BILLNO, BILLDATE, CUSTOMER,BILLAMOUNT,CREDITAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        public void writeValues() throws BasicException {
                            setString(1, UUID.randomUUID().toString());
                            setString(2, ticket.getId());
                            setString(3, ticketDocument);
                            setTimestamp(4, ticket.getDate());
                            setString(5, ticket.getCustomerId());
                            setDouble(6, ticket.getTotal());
                            setDouble(7, creditAmt);

                        }
                    });
                }
//
                SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS,PAYMENTTYPE,DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);

                SentenceExec paymentsplits = new PreparedSentence(s, "INSERT INTO PAYMENTSPLITS (ID, PAYMENTS_ID, IDENTIFIER , AMOUNT) VALUES (?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                SentenceExec updateCreditNote = new PreparedSentence(s, "UPDATE CREDITNOTE SET STATUS = ? WHERE CREDITNOTENO=?", SerializerWriteParams.INSTANCE);

                for (final PaymentInfo p : ticket.getPayments()) {
                    final String paymentId = p.getID().replaceAll("-", "");
                    //     if(p.getTotal()!=0){
                    paymentinsert.exec(new DataParams() {
                        public void writeValues() throws BasicException {

                            setString(1, paymentId);
                            setString(2, ticket.getId());
                            setString(3, p.getName());
                            setDouble(4, p.getTotal());
                            setString(5, ticket.getTransactionID());
                            setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
                            if (p.getName().equals("Cheque") || p.getName().equals("Voucher")) {
                                setString(7, p.getVoucherNo());
                            } else {
                                setString(7, "");
                            }
                            System.out.println("p.getPaymentMode()" + p.getPaymentMode());
                            setString(8, p.getPaymentMode());
                            System.out.println("p.getPaymentMode()" + p.getDescription());

                            setString(9, p.getDescription());
                        }
                    });



                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

                        // udate customer fields...
                        ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());

                        // save customer fields...
                        getDebtUpdate().exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setDouble(1, ticket.getCustomer().getCurdebt());
                                setTimestamp(2, ticket.getCustomer().getCurdate());
                                setString(3, ticket.getCustomer().getId());
                            }
                        });
                    }
                    //}
                }

                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT,ISSERVICETAX,ISSWACHBHARATTAX)  VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (!tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            taxlinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                    setString(6, tickettax.getTaxInfo().getIsServiceTax());
                                    setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
                                }
                            });
                        }
                    }
                }
                SentenceExec chargelinesinsert = new PreparedSentence(s, "INSERT INTO SERVICECHARGELINES (ID, RECEIPT, SERVICECHARGEID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            chargelinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                }
                            });
                        }
                    }
                }
//                 if (ticket.getServiceTaxes() != null) {
//                    for (final TicketTaxInfo tickettax: ticket.getServiceTaxes()) {
//                        if(tickettax.getTaxInfo().getId()!= null){
//                        taxlinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, tickettax.getTaxInfo().getId());
//                            //setDouble(4, tickettax.getSubTotal());
//                            setDouble(4, tickettax.getServiceTaxBaseTotal());
//                            setDouble(5, (tickettax.getRetailServiceTax()));
//                            setString(6, tickettax.getTaxInfo().getIsServiceTax());
//                            setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
//                        }});}
//                    }
//                }
//                if (ticket.getSwachBharatTaxes() != null) {
//                    for (final TicketTaxInfo tickettax: ticket.getSwachBharatTaxes()) {
//                        if(tickettax.getTaxInfo().getId()!= null){
//                        taxlinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, tickettax.getTaxInfo().getId());
//                            //setDouble(4, tickettax.getSubTotal());
//                            setDouble(4, tickettax.getSwachBharatBaseTotal());
//                            setDouble(5, (tickettax.getRetailSwachBharatTax()));
//                            setString(6, tickettax.getTaxInfo().getIsServiceTax());
//                             setString(7, tickettax.getTaxInfo().getIsSwachBharatTax());
//                        }});}
//                    }
//                }
//                SentenceExec chargelinesinsert = new PreparedSentence(s
//                        , "INSERT INTO SERVICECHARGELINES (ID, RECEIPT, SERVICECHARGEID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)"
//                        , SerializerWriteParams.INSTANCE);
//                if (ticket.getCharges() != null) {
//                    for (final TicketServiceChargeInfo ticketcharge: ticket.getCharges()) {
//                        if(ticketcharge.getServiceChargeInfo().getId()!= null){
//                        chargelinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, ticketcharge.getServiceChargeInfo().getId());
//                            setDouble(4, ticketcharge.getSubTotal());
//                            setDouble(5, (ticketcharge.getRetailSCharge()));
//                        }
//                        });}
//                    }
//                }
                if (ticket.getPlaceId() != null && !(ticket.getSplitValue().equals("Split"))) {
                    deleteTableCovers(ticket.getPlaceId());
                }
                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveCancelTicket(final RetailTicketInfo ticket, final String reason, final String reasonId, final String posNo, final String homeDelivery) throws BasicException {

        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                new PreparedSentence(s, "INSERT INTO CANCELTICKETS (ID, TICKETID, PERSON, CUSTOMER,ISHOMEDELIVERY,BILLAMOUNT,DATENEW,REASON,REASONID,POSNO,TABLEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getTicketId());
                        setString(3, ticket.getUser().getId());
                        setString(4, ticket.getCustomerId());
                        setString(5, homeDelivery);
                        setDouble(6, ticket.getTotal());
                        setTimestamp(7, ticket.getDate());
                        setString(8, reason);
                        setString(9, reasonId);
                        setString(10, posNo);
                        setString(11, ticket.getPlaceId());
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {

                    new PreparedSentence(s, "INSERT INTO CANCELTICKETLINES (TICKET, LINE, PRODUCT, UNITS, PRICE, TAXID) VALUES (?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setDouble(4, l.getMultiply());
                            setDouble(5, l.getPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(6, taxid);
                            // }


                        }
                    });
                }

                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveRetailCancelTicket(final RetailTicketInfo ticket, final String StoreName, final String ticketDocument, final String orderTaking, final String location, final String reason, final String reasonId, final String posNo, final String homeDelivery) throws BasicException {
        System.out.println(ticket.getTotal() + "total testing 4 ");
        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        System.out.println("saving bill data into database---- +");
        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
//        if(ticket.getParentId()!=null){
//              String m_sId = UUID.randomUUID().toString();
//               m_sId = m_sId.replaceAll("-", "");
//               ticket.setId(m_sId);
//           }
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
//                if (ticket.getTicketId() == 0) {
//                    switch (ticket.getTicketType()) {
//                        case TicketInfo.RECEIPT_NORMAL:
//                            ticket.setTicketId(getNextTicketIndex().intValue());
//                            break;
//                        case TicketInfo.RECEIPT_REFUND:
//
//
//                            break;
//                        case TicketInfo.RECEIPT_PAYMENT:
//                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
//                            break;
//                        default:
//                            throw new BasicException();
//                    }
//                }
//             
//            final String ticketId = (UUID.randomUUID().toString()).replaceAll("-", "");

                // new receipt
                 record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, "N");
                        setString(9, "N");
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,ROUNDOFFVALUE,ISCANCELTICKET,CANCELREASONID,CANCELCOMMENTS,PARENTBILLNO,DISCOUNTREASONID,DISCOUNTREASON,SOSORDERID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, null);
                        setDouble(14, 0.00);
                        setString(15, "N");
                        setString(16, "N");
                        setDouble(17, ticket.getTotal());
                        setDouble(18, 0.00);
                        setString(19, "N");
                        setString(20, "N");
                        setDouble(21, 0.00);
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        setString(28, "N");
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, "Y");
                        setString(32, reasonId);
                        setString(33, reason);
                        setString(34, ticket.getParentId());
                        setString(35, ticket.getDiscountReasonId());
                        setString(36, ticket.getDiscountReasonText());
                        setString(37, ticket.getSosOrderId());
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {




                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SOSORDERLINEID,SWACHBHARATTAXID,PRODUCTIONAREA,PRODUCTIONAREATYPE,PRIMARYADDON,ADDONID,SALEPRICE,PROMODISCOUNT,PROMODISCOUNTPRICE,LINEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            //setDouble(6, l.getDiscount());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                if (l.getPromodiscountPercent() == 100.00) {
                                    setDouble(6, 0.00);
                                } else {
                                    setDouble(6, l.getDiscount());
                                }
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                             if(l.getPromoType().equals("BOGO")){
                                  //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            setString(19, l.getSosorderLineid());
                            String swtaxid = null;
                            try {
                                swtaxid = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swtaxid = null;
                            }
                            setString(20, swtaxid);
                            setString(21, l.getProductionArea());
                            setString(22, l.getProductionAreaType());
                            setInt(23, l.getPrimaryAddon());
                            setString(24, l.getAddonId());
                            setDouble(25, (l.getSalePrice()));
                            setDouble(26, l.getPromodiscountPercent());
                            setDouble(27, l.getOfferDiscount());
                            setString(28, UUID.randomUUID().toString().replaceAll("-", ""));
                        }
                    });
                }

                if (!ticket.getSplitValue().equals("Split")) {
                    logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after cancel bill of table " + ticket.getTableName() + " id is " + ticket.getPlaceId());
                    deleteSharedTicket(ticket.getPlaceId());


                } else {

                    logger.info("Order No." + ticket.getOrderId() + " deleting shared ticket after cancel bill of splitted table " + ticket.getTableName() + " id is " + ticket.getPlaceId());
                    deleteSharedSplitTicket(ticket.getPlaceId(), ticket.getSplitSharedId());
                }

                if (ticket.getPlaceId() != null && !(ticket.getSplitValue().equals("Split"))) {
                    deleteTableCovers(ticket.getPlaceId());
                }
                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveMergeTicket(final TicketInfo ticket, final String posNo, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final String status, final String location, final String paymentMode, final String moneyCash, final String moneyDay, final List<TicketMergeTaxInfo> mergeTaxInfo, final Date processDate, final double discountTotal) throws BasicException {

        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;


                if (ticket.getTicketId() == 0) {
                    switch (ticket.getTicketType()) {
                        case TicketInfo.RECEIPT_NORMAL:
                            ticket.setTicketId(getNextTicketIndex().intValue());
                            break;
                        case TicketInfo.RECEIPT_REFUND:

                            break;
                        case TicketInfo.RECEIPT_PAYMENT:
                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
                            break;
                        default:
                            throw new BasicException();
                    }
                }
                final String ticketid = UUID.randomUUID().toString();


                System.out.println("ticketid---" + ticketid);
                // new receipt
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticketid);
                        setString(2, moneyCash);
                        setTimestamp(3, processDate);
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, moneyDay);
                        setTimestamp(7, ticket.getDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,COMPLETED,BILLAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticketid);
                        setInt(2, ticket.getTicketType());
                        setString(3, ticketDocument);
                        setInt(4, ticket.getTicketId());
                        setString(5, ticket.getUser().getId());
                        setString(6, ticket.getCustomerId());
                        setDouble(7, ticket.getBillDiscount());
                        setDouble(8, ticket.getLeastValueDiscount());
                        setDouble(9, discountTotal);
                        setString(10, ticket.getRate());

                        setString(11, status);
                        setDouble(12, ticket.getTotal());
                    }
                });

                // pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (int i = 0; i < ticket.getLines().size(); i++) {

                    final TicketLineInfo l = ticket.getLines().get(i);
                    final int line = i;
                    int count = 0;
                    count = getPdtCount(l.getProductID(), ticketid);
                    if (count == 0) {
                        new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                            @SuppressWarnings("element-type-mismatch")
                            public void writeValues() throws BasicException {
                                setString(1, ticketid);
                                setInt(2, line);
                                setString(3, l.getProductID());
                                setString(4, l.getProductAttSetInstId());
                                setDouble(5, l.getMultiply());
                                setDouble(6, l.getDiscount());
                                setDouble(7, l.getPrice());
                                //setDouble(8, l.getDiscountPrice());
                                setDouble(8, 0.0);
                                setString(9, l.getTaxInfo().getId());
                                setString(10, l.getProductAttSetId());
                                setString(11, l.getCampaignId());

                            }
                        });
                    } else {
                        updateProduct(l.getProductID(), l.getMultiply(), ticketid);
                    }
                }

                SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);

                SentenceExec paymentsplits = new PreparedSentence(s, "INSERT INTO PAYMENTSPLITS (ID, PAYMENTS_ID, IDENTIFIER , AMOUNT) VALUES (?, ?, ?, ?)", SerializerWriteParams.INSTANCE);


                //  for (final PaymentInfo p : ticket.getPayments()) {
                final String paymentId = (UUID.randomUUID().toString()).replaceAll("-", "");
                paymentinsert.exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, paymentId);
                        setString(2, ticketid);
                        setString(3, paymentMode);
                        setDouble(4, ticket.getTotal());
                        setString(5, ticket.getTransactionID());
                        setBytes(6, null);
                        setString(7, "");

                    }
                });


//                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {
//
//                        // udate customer fields...
//                        ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());
//
//                        // save customer fields...
//                        getDebtUpdate().exec(new DataParams() { public void writeValues() throws BasicException {
//                            setDouble(1, ticket.getCustomer().getCurdebt());
//                            setTimestamp(2, ticket.getCustomer().getCurdate());
//                            setString(3, ticket.getCustomer().getId());
//                        }});
//                    }
                // }
//
                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                //   if (ticket.getTaxes() != null) {
                for (final TicketMergeTaxInfo tickettax : mergeTaxInfo) {
                    if (tickettax.getTaxAmt() != 0) {
                        taxlinesinsert.exec(new DataParams() {
                            public void writeValues() throws BasicException {

                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                setString(2, ticketid);
                                setString(3, tickettax.getTaxid());
//                            setDouble(4, tickettax.getSubTotal()- ticket.getLeastValueDiscount());
//                            setDouble(5, (tickettax.getTax()-tickettax.getTaxAmount()));
                                setDouble(4, tickettax.getSubTotal());
                                setDouble(5, (tickettax.getTaxAmt()));

                            }
                        });
                    }
                }
                //   }

                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveDraftTicket(final RetailTicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String chequeNos, final String deliveryBoy, final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status, final String orderTaking) throws BasicException {

        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
                if (ticket.getTicketId() == 0) {
                    switch (ticket.getTicketType()) {
                        case TicketInfo.RECEIPT_NORMAL:
                            ticket.setTicketId(getNextTicketIndex().intValue());
                            break;
                        case TicketInfo.RECEIPT_REFUND:

                            new PreparedSentence(s, "INSERT INTO CREDITNOTE (ID, CREDITNOTENO, TICKETID, CUSTOMER, AMOUNT, STATUS, CREATEDBY, UPDATEDBY, CREATED, UPDATED, VALIDITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, creditNo);
                                    setInt(3, ticket.getTicketId());
                                    setString(4, ticket.getCustomer().getName());
                                    setDouble(5, Double.parseDouble(ticket.getRefundTotal()));
                                    setString(6, "N");
                                    setString(7, ticket.getUser().getId());
                                    setString(8, ticket.getUser().getId());
                                    setTimestamp(9, ticket.getDate());
                                    setTimestamp(10, ticket.getDate());
                                    setTimestamp(11, ticket.getNewDate());

                                    // setTimestamp(9, ticket.getDate());
                                    // setTimestamp(10, ticket.getDate());
                                    // setTimestamp(11, ticket.getDate());

                                }
                            });
                            break;
                        case TicketInfo.RECEIPT_PAYMENT:
                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
                            break;
                        default:
                            throw new BasicException();
                    }
                }

                // new receipt
                new PreparedSentence(s, "INSERT INTO RECEIPTSHISTORY (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY) VALUES (?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETSHISTORY (ID, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISTAKEAWAY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getTicketType());
                        setString(3, ticketDocument);
                        setInt(4, ticket.getTicketId());
                        setString(5, ticket.getUser().getId());
                        setString(6, ticket.getCustomerId());
                        setDouble(7, ticket.getBillDiscount());
                        setDouble(8, ticket.getLeastValueDiscount());
                        setDouble(9, ticket.getdAmt());
                        setString(10, ticket.getRate());
                        setString(11, homeDelivery);
                        setString(12, deliveryBoy);
                        setDouble(13, advanceissued);
                        setString(14, cod);
                        setString(15, status);
                        setDouble(16, ticket.getTotal());
                        setDouble(17, creditAmt);
                        setString(18, orderTaking);
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {

                    double price = 0.0;
                    if (pdtBuyGetPriceList != null) {
                        for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {
                            String productId = pdtBuyGetPriceList.get(i).getProductID();
                            if (l.getProductID().equals(productId)) {
                                if (pdtBuyGetPriceList.get(i).getQuantity() == l.getMultiply()) {
                                    l.setPrice(0.0);
                                } else {


                                    double qty = l.getMultiply() - pdtBuyGetPriceList.get(i).getQuantity();
                                    l.setMultiply(qty);
                                    pdtBuyGetPriceList.get(i).setProductId(l.getProductID());
                                    pdtBuyGetPriceList.get(i).setPrice(0.0);
                                    pdtBuyGetPriceList.get(i).setQuantity(pdtBuyGetPriceList.get(i).getQuantity());
                                    pdtBuyGetPriceList.get(i).setTaxCat(l.getTaxInfo().getId());
                                    pdtBuyGetPriceList.get(i).setCategory(l.getProductCategoryID());
                                    pdtBuyGetPriceList.get(i).setAttributesId(l.getProductAttSetId());
                                    pdtBuyGetPriceList.get(i).setCampaignId(l.getCampaignId());
                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(i));
                                    ticket.setPriceInfo(pdtLeastPriceList);
//                                     for(int k=0;k<pdtLeastPriceList.size();k++){
                                    // "CA73D86920FC491799A3F5F0EEC28E3F" if(l.getProductID()==pdtLeastPriceList.get(k).getProductID() && l.getMultiply()!=pdtLeastPriceList.get(k).getQuantity()){

                                    //   insertTicket(pdtLeastPriceList, ticket, l.getTicketLine()+k);
                                    //  }
//                                     }
                                    //insertTicket(ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId());
                                }
                            }

                        }
                    }
//


                    new PreparedSentence(s, "INSERT INTO TICKETLINESHISTORY (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            setDouble(6, l.getDiscount());
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());

                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            setString(10, l.getProductAttSetId());
                            setString(11, l.getCampaignId());

                        }
                    });
                }


                SentenceExec ticketlineinsert = new PreparedSentence(s, "INSERT INTO TICKETLINESHISTORY (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteBuilder.INSTANCE);

//                for (RetailTicketLineInfo l : ticket.getLines()) {
//                   // ticketlineinsert.exec(l);
//                    if (l.getProductID() != null)  {
//                        // update the stock
//                        getStockDiaryInsert().exec(new Object[] {
//                            (UUID.randomUUID().toString()).replaceAll("-", ""),
//                            ticket.getDate(),
//                            l.getMultiply() < 0.0
//                                ? MovementReason.IN_REFUND.getKey()
//                                : MovementReason.OUT_SALE.getKey(),
//                            location,
//                            l.getProductID(),
//                            l.getProductAttSetInstId(),
//                            new Double(-l.getMultiply()),
//                            new Double(0.0),
//                            new Double(0.0),
//                            new Double(l.getPrice()),
//                            null,
//                            null,
//                            null
//                        });
//                    }
//                }


//                System.out.println("ticket.getCustomerId()--"+ticket.getCustomerId());
//                if(creditAmt!=0){
//                  new PreparedSentence(s
//                    , "INSERT INTO CREDITSALE (ID, TICKETID, BILLNO, BILLDATE, CUSTOMER,BILLAMOUNT,CREDITAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?)"
//                    , SerializerWriteParams.INSTANCE
//                    ).exec(new DataParams() { public void writeValues() throws BasicException {
//                        setString(1, UUID.randomUUID().toString());
//                        setString(2, ticket.getId());
//                        setString(3, ticketDocument);
//                        setTimestamp(4, ticket.getDate());
//                        setString(5, ticket.getCustomerId());
//                        setDouble(6, ticket.getTotal());
//                        setDouble(7, creditAmt);
//
//                    }});
//                }
////
                SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTSHISTORY (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);

                SentenceExec paymentsplits = new PreparedSentence(s, "INSERT INTO PAYMENTSPLITS (ID, PAYMENTS_ID, IDENTIFIER , AMOUNT) VALUES (?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                //  SentenceExec updateCreditNote = new PreparedSentence(s, "UPDATE CREDITNOTE SET STATUS = ? WHERE CREDITNOTENO=?", SerializerWriteParams.INSTANCE);

                for (final PaymentInfo p : ticket.getPayments()) {
                    final String paymentId = p.getID().replaceAll("-", "");
                    paymentinsert.exec(new DataParams() {
                        public void writeValues() throws BasicException {
                            setString(1, paymentId);
                            setString(2, ticket.getId());
                            setString(3, p.getName());
                            setDouble(4, p.getTotal());
                            setString(5, ticket.getTransactionID());
                            setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
                            if (p.getName().equals("cheque")) {
                                setString(7, chequeNos);
                            } else {
                                setString(7, "");
                            }
                        }
                    });


                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

                        // udate customer fields...
                        ticket.getCustomer().updateCurDebt(p.getTotal(), ticket.getDate());

                        // save customer fields...
                        getDebtUpdate().exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setDouble(1, ticket.getCustomer().getCurdebt());
                                setTimestamp(2, ticket.getCustomer().getCurdate());
                                setString(3, ticket.getCustomer().getId());
                            }
                        });
                    }
                }

                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINESHISTORY (ID, RECEIPT, TAXID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        taxlinesinsert.exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                setString(2, ticket.getId());
                                setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal()- ticket.getLeastValueDiscount());
//                            setDouble(5, (tickettax.getTax()-tickettax.getTaxAmount()));
                                setDouble(4, tickettax.getSubTotal() - (tickettax.getSubTotal() * ticket.convertRatetodouble(ticket.getRate())));
                                setDouble(5, (tickettax.getTax() - (tickettax.getTax() * ticket.convertRatetodouble(ticket.getRate()))));
                            }
                        });
                    }
                }

                return null;
            }
        };


        t.execute();
    }

    public final synchronized void saveHomeCreditSale(final String ticketId, final String ticketDocument, final Date ticketDate, final double total, final String customerId) throws BasicException {
        new PreparedSentence(s, "INSERT INTO CREDITSALE (ID, TICKETID, BILLNO, BILLDATE, CUSTOMER,BILLAMOUNT,CREDITAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, UUID.randomUUID().toString());
                setString(2, ticketId);
                setString(3, ticketDocument);
                setTimestamp(4, ticketDate);
                setString(5, customerId);
                setDouble(6, total);
                setDouble(7, total);

            }
        });

    }

    public final synchronized void saveCreditTicket(final String posNo, final String closeCash, final String closeDay, final Date sysDate, final double total, final String tenderType, final String chequeNO) throws BasicException {
        final String id = UUID.randomUUID().toString();
        new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, id);
                setString(2, closeCash);
                setTimestamp(3, sysDate);
                setString(4, posNo);

                //ByteArrayOutputStream o = new ByteArrayOutputStream();
                //ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                setBytes(5, null);

                setString(6, closeDay);
                setTimestamp(7, sysDate);
                setString(8, "Y");
                setString(9, "Y");
            }
        });
        new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS)  VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
            public void writeValues() throws BasicException {
                setString(1, UUID.randomUUID().toString());
                setString(2, id);
                setString(3, tenderType);
                setDouble(4, total);
                setString(5, "");
                setBytes(6, (byte[]) Formats.BYTEA.parseValue(""));
                setString(7, "");
            }
        });

    }

    //ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId()
    public final void insertTicket(ArrayList<BuyGetPriceInfo> pdtLeastPriceList, final TicketInfo ticket) throws BasicException {
        if (pdtLeastPriceList != null) {

            for (int j = 0; j < pdtLeastPriceList.size(); j++) {
                final int lineNo;
                final String product = pdtLeastPriceList.get(j).getProductID();
                final double qty = pdtLeastPriceList.get(j).getQuantity();
                final String taxcat = pdtLeastPriceList.get(j).getTaxCat();
                if (j == 0) {
                    lineNo = getTicketLineNo(ticket.getId()) + (j + 1);
                } else {
                    lineNo = getTicketLineNo(ticket.getId()) + j;
                }
                final String attributes = pdtLeastPriceList.get(j).getAttributesID();
                final String campaignId = pdtLeastPriceList.get(j).getCampaignId();

                new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    @SuppressWarnings("element-type-mismatch")
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, lineNo);
                        setString(3, product);
                        setString(4, attributes);
                        setDouble(5, qty);
                        setDouble(6, 0.0);
                        setDouble(7, 0.0);
                        setDouble(8, 0.0);
                        setString(9, taxcat);
                        setString(10, "");
                        setString(11, campaignId);

                    }
                });
            }
        }
    }

    public final void insertRetailTicket(ArrayList<BuyGetPriceInfo> pdtLeastPriceList, final RetailTicketInfo ticket) throws BasicException {
        if (pdtLeastPriceList != null) {

            for (int j = 0; j < pdtLeastPriceList.size(); j++) {
                final int lineNo;
                final String product = pdtLeastPriceList.get(j).getProductID();
                final double qty = pdtLeastPriceList.get(j).getQuantity();
                final String taxcat = pdtLeastPriceList.get(j).getTaxCat();
                if (j == 0) {
                    lineNo = getTicketLineNo(ticket.getId()) + (j + 1);
                } else {
                    lineNo = getTicketLineNo(ticket.getId()) + j;
                }
                final String attributes = pdtLeastPriceList.get(j).getAttributesID();
                final String campaignId = pdtLeastPriceList.get(j).getCampaignId();

                new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    @SuppressWarnings("element-type-mismatch")
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, lineNo);
                        setString(3, product);
                        setString(4, attributes);
                        setDouble(5, qty);
                        setDouble(6, 0.0);
                        setDouble(7, 0.0);
                        setDouble(8, 0.0);
                        setString(9, taxcat);
                        setString(10, "");
                        setString(11, campaignId);

                    }
                });
            }
        }
    }

    public final SentenceExec updateCreditnote(String creditNote) {
        System.out.println("enrtr---" + creditNote);

        return new PreparedSentence(s, "UPDATE CREDITNOTE SET STATUS = 'Y' WHERE CREDITNOTENO='" + creditNote + "'", SerializerWriteParams.INSTANCE);

    }

    public void deleteCustomer(String customerId) throws BasicException {
        new StaticSentence(s, "DELETE FROM CUSTOMERS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(customerId);
    }

    public void deleteTableCovers(String tableId) throws BasicException {
        new StaticSentence(s, "DELETE FROM TABLECOVERS WHERE TABLEID = ?", SerializerWriteString.INSTANCE).exec(tableId);
    }

    public final void deleteTicket(final RetailTicketInfo ticket, final String location) throws BasicException {

        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {

                // update the inventory
                Date d = new Date();
                for (int i = 0; i < ticket.getLinesCount(); i++) {
                    if (ticket.getLine(i).getProductID() != null) {
                        // Hay que actualizar el stock si el hay producto
                        getStockDiaryInsert().exec(new Object[]{
                            UUID.randomUUID().toString(),
                            d,
                            ticket.getLine(i).getMultiply() >= 0.0
                            ? MovementReason.IN_REFUND.getKey()
                            : MovementReason.OUT_SALE.getKey(),
                            location,
                            ticket.getLine(i).getProductID(),
                            ticket.getLine(i).getProductAttSetInstId(),
                            new Double(ticket.getLine(i).getMultiply()),
                            0.0,
                            0.0,
                            new Double(ticket.getLine(i).getPrice()),
                            null,
                            null,
                            null
                        });
                    }
                }

                // update customer debts
                for (PaymentInfo p : ticket.getPayments()) {
                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

                        // udate customer fields...
                        ticket.getCustomer().updateCurDebt(-p.getTotal(), ticket.getDate());

                        // save customer fields...
                        getDebtUpdate().exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setDouble(1, ticket.getCustomer().getCurdebt());
                                setTimestamp(2, ticket.getCustomer().getCurdate());
                                setString(3, ticket.getCustomer().getId());
                            }
                        });
                    }
                }

                // and delete the receipt
                new StaticSentence(s, "DELETE FROM TAXLINES WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETLINES WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM CREDITSALE WHERE TICKETID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM RECEIPTS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TAXLINESHISTORY WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM PAYMENTSHISTORY WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETLINESHISTORY WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETSHISTORY WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());

                new StaticSentence(s, "DELETE FROM RECEIPTSHISTORY WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                return null;
            }
        };
        t.execute();
    }

    public final void deleteTicket(final TicketInfo ticket, final String location) throws BasicException {
        System.out.println("enter----delete");
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {

                // update the inventory
                Date d = new Date();
                for (int i = 0; i < ticket.getLinesCount(); i++) {
                    if (ticket.getLine(i).getProductID() != null) {
                        // Hay que actualizar el stock si el hay producto
                        getStockDiaryInsert().exec(new Object[]{
                            UUID.randomUUID().toString(),
                            d,
                            ticket.getLine(i).getMultiply() >= 0.0
                            ? MovementReason.IN_REFUND.getKey()
                            : MovementReason.OUT_SALE.getKey(),
                            location,
                            ticket.getLine(i).getProductID(),
                            ticket.getLine(i).getProductAttSetInstId(),
                            new Double(ticket.getLine(i).getMultiply()),
                            0.0,
                            0.0,
                            new Double(ticket.getLine(i).getPrice()),
                            null,
                            null,
                            null
                        });
                    }
                }

                // update customer debts
                for (PaymentInfo p : ticket.getPayments()) {
                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {

                        // udate customer fields...
                        ticket.getCustomer().updateCurDebt(-p.getTotal(), ticket.getDate());

                        // save customer fields...
                        getDebtUpdate().exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setDouble(1, ticket.getCustomer().getCurdebt());
                                setTimestamp(2, ticket.getCustomer().getCurdate());
                                setString(3, ticket.getCustomer().getId());
                            }
                        });
                    }
                }

                // and delete the receipt
                new StaticSentence(s, "DELETE FROM TAXLINES WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM SERVICECHARGELINES WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM PAYMENTS WHERE RECEIPT = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETLINES WHERE TICKET = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM TICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s, "DELETE FROM RECEIPTS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(ticket.getId());
                return null;
            }
        };
        t.execute();
    }

    public final void deleteMergeTicket(final String billId) throws BasicException {

        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {

                new StaticSentence(s, "DELETE FROM TAXLINES WHERE RECEIPT IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                new StaticSentence(s, "DELETE FROM SERVICECHARGELINES WHERE RECEIPT IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                new StaticSentence(s, "DELETE FROM PAYMENTS WHERE RECEIPT IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                new StaticSentence(s, "DELETE FROM TICKETLINES WHERE TICKET IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                new StaticSentence(s, "DELETE FROM TICKETS WHERE ID IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                new StaticSentence(s, "DELETE FROM RECEIPTS WHERE ID IN (" + billId + ")", SerializerWriteString.INSTANCE).exec();
                return null;
            }
        };
        t.execute();
    }

    public final Integer getNextTicketIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "TICKETSNUM").find();
    }

    public final Integer getNextTicketRefundIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "TICKETSNUM_REFUND").find();
    }

    public final Integer getNextTicketPaymentIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "TICKETSNUM_PAYMENT").find();
    }

    public final Integer getNextKotIndex() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "KOTNUM").find();
    }

    public final SentenceList getProductCatQBF() {
        System.out.println("used for product master");
        System.out.println("products selection 16");
        return new StaticSentence(s, new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAXCAT, P.ATTRIBUTESET_ID, P.IMAGE, P.STOCKCOST, P.STOCKVOLUME, CASE WHEN C.PRODUCT IS NULL THEN " + s.DB.FALSE() + " ELSE " + s.DB.TRUE() + " END, C.CATORDER, P.ATTRIBUTES, P.MRP, P.ISACTIVE, P.ISSALESITEM, P.ISPURCHASEITEM, P.UOM,P.SERVICECHARGE,P.SERVICETAX,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.POPULAR,P.ISADDON "
                + "FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT "
                + "WHERE ?(QBF_FILTER) "
                + "ORDER BY P.REFERENCE", new String[]{"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}), productsRow.getSerializerRead());
    }

    public final SentenceExec getProductCatInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;
                int i = new PreparedSentence(s, "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, IMAGE, STOCKCOST, STOCKVOLUME, ATTRIBUTES,ITEMCODE,MRP, ISACTIVE, ISSALESITEM, ISPURCHASEITEM,UOM,SERVICECHARGE,SERVICETAX,PRODUCTTYPE,PRODUCTIONAREATYPE,POPULAR,ISADDON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 1, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27})).exec(params);
                if (i > 0 && ((Boolean) values[14]).booleanValue()) {
                    return new PreparedSentence(s, "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0, 15})).exec(params);
                } else {
                    return i;
                }
            }
        };
    }

    public final SentenceExec getProductCatUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;
                int i = new PreparedSentence(s, "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAXCAT = ?, ATTRIBUTESET_ID = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ?, ITEMCODE = ?, MRP = ? , ISACTIVE = ?, ISSALESITEM = ?, ISPURCHASEITEM = ?, UOM = ?,SERVICECHARGE=?,SERVICETAX=?,PRODUCTTYPE=?,PRODUCTIONAREATYPE=?,POPULAR=?,ISADDON=? WHERE ID = ?", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 1, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 0})).exec(params);
                if (i > 0) {
                    if (((Boolean) values[14]).booleanValue()) {
                        if (new PreparedSentence(s, "UPDATE PRODUCTS_CAT SET CATORDER = ? WHERE PRODUCT = ?", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{15, 0})).exec(params) == 0) {
                            new PreparedSentence(s, "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0, 15})).exec(params);
                        }
                    } else {
                        new PreparedSentence(s, "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0})).exec(params);
                    }
                }
                return i;
            }
        };
    }

    public final SentenceExec getProductCatDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s, "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0})).exec(params);
                return new PreparedSentence(s, "DELETE FROM PRODUCTS WHERE ID = ?", new SerializerWriteBasicExt(productsRow.getDatas(), new int[]{0})).exec(params);
            }
        };
    }

    public final SentenceExec getDebtUpdate() {

        return new PreparedSentence(s, "UPDATE CUSTOMERS SET CURDEBT = ?, CURDATE = ? WHERE ID = ?", SerializerWriteParams.INSTANCE);
    }

    public final SentenceExec getStockDiaryInsert() {

        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                int updateresult = ((Object[]) params)[5] == null // si ATTRIBUTESETINSTANCE_ID is null
                        ? new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS = (UNITS + ?) WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID IS NULL", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{6, 3, 4})).exec(params)
                        : new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS = (UNITS + ?) WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID = ?", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{6, 3, 4, 5})).exec(params);

                if (updateresult == 0) {
                    new PreparedSentence(s, "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{3, 4, 5, 6})).exec(params);
                }
                return new PreparedSentence(s, "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID,TPNO,TPDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})).exec(params);
            }
        };
    }

    public int getProcessCount(String processName) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT FROM PROCESSINFO WHERE PROCESSNAME = '" + processName + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public String getTaxId() throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT ID FROM TAXES WHERE RATE=0", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = record[0] == null ? "0" : record[0].toString();
        return i == null ? "" : i;

    }

    public int getPurchaseCount(String processName) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROCESSINFO WHERE PROCESSNAME = '" + processName + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getCancelTicketCount(int ticketId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CANCELTICKETS WHERE TICKETID = '" + ticketId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.INT})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getHomeDeliveryCount(String billNo) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM TICKETS WHERE DOCUMENTNO = '" + billNo + "' AND ISHOMEDELIVERY='Y'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getPdtCount(String pdtId, String ticketid) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM TICKETLINES WHERE PRODUCT = '" + pdtId + "' AND TICKET = '" + ticketid + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getCusCreditCount(String cusId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CUSTOMERS WHERE ISCREDITCUSTOMER = 1 AND ID = '" + cusId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public void insertProcessInfo(String id, String processName, int count) {

        Object[] values = new Object[]{id, processName, count};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.INT};
        try {
            new PreparedSentence(s, "INSERT INTO PROCESSINFO (ID, PROCESSNAME, COUNT) VALUES (?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final SentenceExec getStockDiaryDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                int updateresult = ((Object[]) params)[5] == null // if ATTRIBUTESETINSTANCE_ID is null
                        ? new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS = (UNITS - ?) WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID IS NULL", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{6, 3, 4})).exec(params)
                        : new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS = (UNITS - ?) WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID = ?", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{6, 3, 4, 5})).exec(params);

                if (updateresult == 0) {
                    new PreparedSentence(s, "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, -(?))", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{3, 4, 5, 6})).exec(params);
                }
                return new PreparedSentence(s, "DELETE FROM STOCKDIARY WHERE ID = ?", new SerializerWriteBasicExt(stockdiaryDatas, new int[]{0})).exec(params);
            }
        };
    }

    public final SentenceExec getPaymentMovementInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW) VALUES (?, ?, ?)", new SerializerWriteBasicExt(paymenttabledatas, new int[]{0, 1, 2})).exec(params);
                return new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(paymenttabledatas, new int[]{3, 0, 4, 5})).exec(params);
            }
        };
    }

    public final SentenceExec getPaymentMovementDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s, "DELETE FROM PAYMENTS WHERE ID = ?", new SerializerWriteBasicExt(paymenttabledatas, new int[]{3})).exec(params);
                return new PreparedSentence(s, "DELETE FROM RECEIPTS WHERE ID = ?", new SerializerWriteBasicExt(paymenttabledatas, new int[]{0})).exec(params);
            }
        };
    }

    public final double findProductStock(String warehouse, String id, String attsetinstid) throws BasicException {

        PreparedSentence p = attsetinstid == null
                ? new PreparedSentence(s, "SELECT UNITS FROM STOCKCURRENT WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID IS NULL", new SerializerWriteBasic(Datas.STRING, Datas.STRING), SerializerReadDouble.INSTANCE)
                : new PreparedSentence(s, "SELECT UNITS FROM STOCKCURRENT WHERE LOCATION = ? AND PRODUCT = ? AND ATTRIBUTESETINSTANCE_ID = ?", new SerializerWriteBasic(Datas.STRING, Datas.STRING, Datas.STRING), SerializerReadDouble.INSTANCE);

        Double d = (Double) p.find(warehouse, id, attsetinstid);
        return d == null ? 0.0 : d.doubleValue();
    }

    public final SentenceExec getCatalogCategoryAdd() {
        return new StaticSentence(s, "INSERT INTO PRODUCTS_CAT(PRODUCT, CATORDER) SELECT ID, " + s.DB.INTEGER_NULL() + " FROM PRODUCTS WHERE CATEGORY = ?", SerializerWriteString.INSTANCE);
    }

    public final SentenceExec getCatalogCategoryDel() {
        return new StaticSentence(s, "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ANY (SELECT ID FROM PRODUCTS WHERE CATEGORY = ?)", SerializerWriteString.INSTANCE);
    }

    public final TableDefinition getTableCategories() {
        return new TableDefinition(s,
                "CATEGORIES", new String[]{"ID", "NAME", "PARENTID", "IMAGE", "ISDISCOUNTAPPLICABLE"}, new String[]{"ID", AppLocal.getIntString("Label.Name"), "", AppLocal.getIntString("label.image")}, new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.STRING}, new Formats[]{Formats.STRING, Formats.STRING, Formats.STRING, Formats.NULL, Formats.STRING}, new int[]{0});
    }

    public final TableDefinition getTableTaxes() {
        return new TableDefinition(s,
                "TAXES", new String[]{"ID", "NAME", "CATEGORY", "CUSTCATEGORY", "PARENTID", "RATE", "RATECASCADE", "RATEORDER", "ISSALESTAX", "ISPURCHASETAX", "ISSERVICETAX", "DEBITACCOUNT", "CREDITACCOUNT"}, new String[]{"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.taxcategory"), AppLocal.getIntString("label.custtaxcategory"), AppLocal.getIntString("label.taxparent"), AppLocal.getIntString("label.dutyrate"), AppLocal.getIntString("label.cascade"), AppLocal.getIntString("label.order"), "ISSALESTAX", "ISPURCHASETAX", "ISSERVICETAX", "DEBITACCOUNT", "CREDITACCOUNT"}, new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.BOOLEAN, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING}, new Formats[]{Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.PERCENT, Formats.BOOLEAN, Formats.INT, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING}, new int[]{0});
    }

    public final TableDefinition getTableTaxCustCategories() {
        return new TableDefinition(s,
                "TAXCUSTCATEGORIES", new String[]{"ID", "NAME"}, new String[]{"ID", AppLocal.getIntString("Label.Name")}, new Datas[]{Datas.STRING, Datas.STRING}, new Formats[]{Formats.STRING, Formats.STRING}, new int[]{0});
    }

    public final TableDefinition getTableTaxCategories() {
        return new TableDefinition(s,
                "TAXCATEGORIES", new String[]{"ID", "NAME"}, new String[]{"ID", AppLocal.getIntString("Label.Name")}, new Datas[]{Datas.STRING, Datas.STRING}, new Formats[]{Formats.STRING, Formats.STRING}, new int[]{0});
    }

    public final TableDefinition getTableLocations() {
        return new TableDefinition(s,
                "LOCATIONS", new String[]{"ID", "NAME", "ADDRESS"}, new String[]{"ID", AppLocal.getIntString("label.locationname"), AppLocal.getIntString("label.locationaddress")}, new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING}, new Formats[]{Formats.STRING, Formats.STRING, Formats.STRING}, new int[]{0});
    }

    protected static class CustomerExtRead implements SerializerRead {

        public Object readValues(DataRead dr) throws BasicException {
            CustomerInfoExt c = new CustomerInfoExt(dr.getString(1));
            c.setTaxid(dr.getString(2));
            c.setSearchkey(dr.getString(3));
            c.setName(dr.getString(4));
            c.setCard(dr.getString(5));
            c.setTaxCustomerID(dr.getString(6));
            c.setNotes(dr.getString(7));
            c.setMaxdebt(dr.getDouble(8));
            c.setVisible(dr.getBoolean(9).booleanValue());
            c.setCurdate(dr.getTimestamp(10));
            c.setCurdebt(dr.getDouble(11));
            c.setFirstname(dr.getString(12));
            c.setLastname(dr.getString(13));
            c.setEmail(dr.getString(14));
            c.setPhone(dr.getString(15));
            c.setPhone2(dr.getString(16));
            c.setFax(dr.getString(17));
            c.setAddress(dr.getString(18));
            c.setAddress2(dr.getString(19));
            c.setPostal(dr.getString(20));
            c.setCity(dr.getString(21));
            c.setRegion(dr.getString(22));
            c.setCountry(dr.getString(23));

            return c;
        }
    }

    //Promotion Related Queries
    public List<CampaignIdInfo> getCampaignId(String day) throws BasicException {
        Date sysdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//       SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentDate = sdf.format(sysdate);
        String currentTime = time.format(sysdate);
        return (List<CampaignIdInfo>) new PreparedSentence(s, "SELECT ID,STARTTIME,ENDTIME FROM PROMOTIONCAMPAIGN WHERE ACTIVE='Y' AND STARTDATE<='" + currentDate + "' AND STARTTIME<='" + currentTime + "' AND ENDDATE>='" + currentDate + "' AND ENDTIME>'" + currentTime + "' AND (ALLDAY='Y' OR " + day + "='Y') ", null, new SerializerReadClass(CampaignIdInfo.class)).list();
    }

    public List<CampaignIdInfo> getPdtCampaignId() throws BasicException {
        return (List<CampaignIdInfo>) new PreparedSentence(s, "SELECT DISTINCT CAMPAIGNID,null,null FROM TEMPTICKETLINES WHERE ISCROSSPROMOPRODUCT='N' AND PROMOTYPE='SIBG' ", null, new SerializerReadClass(CampaignIdInfo.class)).list();
    }
//      public String getCampaignId(String day) throws BasicException {
//
//       Date sysdate = new Date();
//       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//       SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
//       String currentDate = sdf.format(sysdate);
//       String currentTime = time.format(sysdate);
//       Object[] record = ( Object[]) new StaticSentence(s
//                    , "SELECT ID FROM PROMOTIONCAMPAIGN WHERE ACTIVE='Y' AND ENDDATE>='"+currentDate+"' AND ENDTIME>='"+currentTime+"' AND (ALLDAY='Y' OR "+day+"='Y') "
//                    , SerializerWriteString.INSTANCE
//                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
//            String i = (record[0]==null ? null :record[0].toString());
//            return (i == null ? null : i);
//
//
//    }

    public List<PromoRuleInfo> getPromoRuleDetails(String ruleId, String isPrice, String isPromoProduct, String productId) throws BasicException {
        return (List<PromoRuleInfo>) new PreparedSentence(s, "SELECT PR.PROMOTIONTYPEID,PR.BUYQTY,PR.GETQTY,PR.BILLAMOUNT,PR.MINVALUE,PR.VALUE, PR.ISSKU, PR.ISPROMOPRODUCT, PR.ISCROSSPROMOPRODUCT, PR.ISMULTIPLEPROMOPRODUCT, PR.ISMULTIPLECROSSPROMOPRODUCT, PR.ID, PR.PROMOTIONCAMPAIGNID FROM PROMOTIONRULE PR, PROMOPRODUCT PP WHERE  PP.PROMOTIONRULEID=PR.ID AND PR.ID IN (" + ruleId + ")"
                + " AND PR.ISPRICE='" + isPrice + "' AND PR.ISPROMOPRODUCT='" + isPromoProduct + "' AND PP.PRODUCTID='" + productId + "' ORDER BY PR.BUYQTY DESC", null, new SerializerReadClass(PromoRuleInfo.class)).list();

    }

    public List<BillPromoRuleInfo> getBillPromoRuleDetails(String ruleId, double amount) throws BasicException {
        return (List<BillPromoRuleInfo>) new PreparedSentence(s, "SELECT PR.PROMOTIONTYPEID,PR.BILLAMOUNT,PR.VALUE,PR.ISPRICE FROM PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE  PR.PROMOTIONTYPEID=PT.ID AND PR.ID IN (" + ruleId + ")"
                + "AND PT.NAME='BillvaluePromotion' AND PR.BILLAMOUNT<='" + amount + "' ORDER BY PR.BILLAMOUNT DESC", null, new SerializerReadClass(BillPromoRuleInfo.class)).list();

    }

    public List<CrossProductInfo> getCrossProductDetails(String ruleId) throws BasicException {
        System.out.println("products selection 17");
        return (List<CrossProductInfo>) new PreparedSentence(s, "SELECT P.ID,P.NAME FROM PRODUCTS P, CROSSPROMOPRODUCT CP WHERE P.ID = CP.PRODUCTID AND CP.PROMOTIONRULEID = '" + ruleId + "'", null, new SerializerReadClass(CrossProductInfo.class)).list();

    }

    public List<PromoRuleIdInfo> getPromoRuleId(String campaignId) throws BasicException {
        return (List<PromoRuleIdInfo>) new PreparedSentence(s, "SELECT ID FROM PROMOTIONRULE WHERE PROMOTIONCAMPAIGNID IN (" + campaignId + ") ", null, new SerializerReadClass(PromoRuleIdInfo.class)).list();

    }

    public List<BuyGetInfo> getbuyGetTotalQty(String campaignId) throws BasicException {

        return (List<BuyGetInfo>) new PreparedSentence(s, " SELECT SUM(QUANTITY), CAMPAIGNID FROM TEMPTICKETLINES WHERE CAMPAIGNID IN (" + campaignId + ") AND PROMOTYPE='SIBG' AND ISCROSSPROMOPRODUCT ='N' GROUP BY CAMPAIGNID  ", null, new SerializerReadClass(BuyGetInfo.class)).list();

    }

    public final SentenceList getUomList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM UOM ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new UomListInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public List<BuyGetQtyInfo> getbuyGetQty(String campaignId, int qty) throws BasicException {

        return (List<BuyGetQtyInfo>) new PreparedSentence(s, " SELECT BUYQTY,GETQTY FROM PROMOTIONRULE WHERE PROMOTIONCAMPAIGNID ='" + campaignId + "' AND BUYQTY<=" + qty + " ORDER BY BUYQTY DESC LIMIT 1  ", null, new SerializerReadClass(BuyGetQtyInfo.class)).list();

    }

    public List<BuyGetPriceInfo> getbuyGetLeastPrice(String campaignId, int qty) throws BasicException {
        System.out.println("sumOfBuyGet -remaining -qty-getbuygetLeastPrice call"+campaignId+"quantity"+qty);

        return (List<BuyGetPriceInfo>) new PreparedSentence(s, " SELECT T.PRODUCTID, T.PRICE, P.CATEGORY, P.TAXCAT, T.QUANTITY, P.ATTRIBUTESET_ID, T.CAMPAIGNID, T.TAXRATE FROM TEMPTICKETLINES T, PRODUCTS P WHERE P.ID=T.PRODUCTID AND T.CAMPAIGNID ='" + campaignId + "' ORDER BY PRICE ASC  LIMIT " + qty + "  ", null, new SerializerReadClass(BuyGetPriceInfo.class)).list();

    }

    public List<BuyGetPriceInfo> getLeastProduct(String campaignId, int qty) throws BasicException {
System.out.println("BuyGetprice Info Bean -getLeast Product call"+campaignId+"quantity"+qty);
        return (List<BuyGetPriceInfo>) new PreparedSentence(s, " SELECT T.PRODUCTID, T.PRICE, P.CATEGORY, P.TAXCAT, SUM(T.QUANTITY), T.TAXRATE FROM TEMPTICKETLINES T, PRODUCTS P WHERE P.ID=T.PRODUCTID AND T.CAMPAIGNID ='" + campaignId + "' GROUP BY PRODUCTID, CAMPAIGNID ORDER BY PRICE ASC LIMIT " + qty + "  ", null, new SerializerReadClass(BuyGetPriceInfo.class)).list();

    }
    //method called while loading tables  

    public List<ProductInfoExt> getPopularProduct(String popular) throws BasicException {
        System.out.println("products selection 18");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP, U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, UOM U,CATEGORIES C WHERE P.ID = O.PRODUCT AND U.ID=P.UOM AND P.CATEGORY=C.ID AND P.POPULAR =? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(popular);
    }

    public List<PromoRuleIdInfo> getPromoLeastRuleId(String campaignId) throws BasicException {
        return (List<PromoRuleIdInfo>) new PreparedSentence(s, "SELECT PR.ID FROM PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE PT.ID=PR.PROMOTIONTYPEID AND PR.PROMOTIONCAMPAIGNID IN (" + campaignId + ") AND PR.ISCROSSPROMOPRODUCT='N' AND PT.NAME = 'SIBG'", null, new SerializerReadClass(PromoRuleIdInfo.class)).list();

    }

    public String getPdtPromoRuleId(String productId) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT PROMOTIONRULEID FROM PROMOPRODUCT WHERE PRODUCTID ='" + productId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);


    }

    public String getTableId(String billNo) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT PLACES.NAME FROM PLACES,TICKETS WHERE TICKETS.TABLEID=PLACES.ID AND TICKETS.DOCUMENTNO='" + billNo + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);


    }

    public int getProductCount(String productId, String ruleId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOPRODUCT WHERE PROMOTIONRULEID IN (" + ruleId + ") AND PRODUCTID='" + productId + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getRangePriceOffValue(String productId, String ruleId, int qty) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT DISTINCT PR.VALUE FROM PROMOTIONRULE PR, PROMOPRODUCT PP WHERE PP.PROMOTIONRULEID=PR.ID AND PR.ID IN (" + ruleId + ") AND PP.PRODUCTID='" + productId + "' AND (" + qty + " BETWEEN MINIMUMRANGE  AND MAXIMUMRANGE )", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getMaxRangePriceOffValue(String productId, String ruleId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT PR.VALUE FROM PROMOTIONRULE PR, PROMOPRODUCT PP WHERE PP.PROMOTIONRULEID=PR.ID AND PR.ID IN (" + ruleId + ") AND PP.PRODUCTID='" + productId + "' ORDER BY MAXIMUMRANGE DESC LIMIT 1", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getRangePerOffValue(String productId, String ruleId, int qty) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT DISTINCT PR.VALUE FROM PROMOTIONRULE PR, PROMOPRODUCT PP WHERE PP.PROMOTIONRULEID=PR.ID AND PR.ID IN (" + ruleId + ") AND PP.PRODUCTID='" + productId + "' AND (" + qty + " BETWEEN MINIMUMRANGE  AND MAXIMUMRANGE )", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getMaxRangePerOffValue(String productId, String ruleId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT PR.VALUE FROM PROMOTIONRULE PR, PROMOPRODUCT PP WHERE PP.PROMOTIONRULEID=PR.ID AND PR.ID IN (" + ruleId + ") AND PP.PRODUCTID='" + productId + "' ORDER BY MAXIMUMRANGE DESC LIMIT 1", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getBillPromotionCount(String ruleId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE  PR.PROMOTIONTYPEID=PT.ID AND PR.ID IN (" + ruleId + ") AND PT.NAME = 'BillvaluePromotion' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getPromoProductCount(String productId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOPRODUCT WHERE PRODUCTID='" + productId + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public String getPromoType(String productId) throws BasicException {


        Object[] record = (Object[]) new StaticSentence(s, "SELECT DISTINCT PT.ID FROM PROMOTIONTYPE PT, PROMOPRODUCT PP, PROMOTIONRULE PR WHERE PR.ID=PP.PROMOTIONRULEID AND PT.ID=PR.PROMOTIONTYPEID AND PP.PRODUCTID='" + productId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);


    }

    public String getPromoTypeId(String productId) throws BasicException {


        Object[] record = (Object[]) new StaticSentence(s, "SELECT DISTINCT PT.ID FROM PROMOTIONTYPE PT, PROMOPRODUCT PP, PROMOTIONRULE PR WHERE PR.ID=PP.PROMOTIONRULEID AND PT.ID=PR.PROMOTIONTYPEID AND PP.PRODUCTID='" + productId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);


    }

    public String getDiscountRate(String id) throws BasicException {


        Object[] record = (Object[]) new StaticSentence(s, "SELECT DISCOUNTRATE FROM TICKETS WHERE ID='" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == "0" ? "0" : i);


    }

    public String getCrossPromoProduct(String promotionId) throws BasicException {


        Object[] record = (Object[]) new StaticSentence(s, "SELECT PRODUCTID FROM CROSSPROMOPRODUCT WHERE PROMOTIONRULEID ='" + promotionId + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);


    }

    public int getStopInventoryCount() throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT count(*) FROM STOCKRECONCILIATIONSTATUS WHERE ISSTOPINVENTORY='Y' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0].toString());
        return (i == 0 ? 0 : i);

    }

    public int getPriceOffCount(String typeId, String ruleId, String id) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOPRODUCT PP, PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE "
                + " PR.ID=PP.PROMOTIONRULEID AND PT.ID=PR.PROMOTIONTYPEID AND PP.PROMOTIONRULEID IN (" + ruleId + ") AND PT.ID='" + typeId + "' AND PP.PRODUCTID='" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getPercentageOffCount(String typeId, String ruleId, String id) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOPRODUCT PP, PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE "
                + " PR.ID=PP.PROMOTIONRULEID AND PT.ID=PR.PROMOTIONTYPEID AND PP.PROMOTIONRULEID IN (" + ruleId + ") AND PT.ID='" + typeId + "' AND PP.PRODUCTID='" + id + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);

    }

    public final SentenceList getCustomerNameList() {
        return new StaticSentence(s, "SELECT ID, NAME,NAME FROM CUSTOMERS WHERE ISCUSTOMER=b'0'", null, CategoryInfo.getSerializerRead());
    }

    public int getBuyGetCount(String typeId, String ruleId, String id) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM PROMOPRODUCT PP, PROMOTIONRULE PR, PROMOTIONTYPE PT WHERE "
                + " PR.ID=PP.PROMOTIONRULEID AND PT.ID=PR.PROMOTIONTYPEID AND PP.PROMOTIONRULEID IN (" + ruleId + ") AND PT.ID='" + typeId + "' AND PP.PRODUCTID='" + id + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getTicketLineNo(String id) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT MAX(LINE) FROM TICKETLINES WHERE TICKET='" + id + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);



    }

    public final void saveTempTicketlines(final TicketInfo ticket) {
        for (final TicketLineInfo l : ticket.getLines()) {
            for (int i = 0; i < l.getMultiply(); i++) {
                try {
                    new PreparedSentence(s, "INSERT INTO TEMPTICKETLINES (PRODUCTID, CAMPAIGNID, PROMOTIONID,  QUANTITY,ISCROSSPROMOPRODUCT,PROMOTYPE, PRICE, TAXRATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        public void writeValues() throws BasicException {

                            setString(1, l.getProductID());
                            setString(2, l.getCampaignId());
                            setString(3, l.getpromoId());
                            setDouble(4, 1.0);
                            setString(5, l.getIsCrossProduct());
                            setString(6, l.getPromoType());
                            setDouble(7, l.getPrice());
                            setDouble(8, (l.getTax() / l.getMultiply()));
                        }
                    });
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public final void saveRetailTempTicketlines(final RetailTicketInfo ticket) {
        for (final RetailTicketLineInfo l : ticket.getLines()) {
            for (int i = 0; i < l.getMultiply(); i++) {
              if( l.getPromoType().equals("SIBG")&& l.getIsPromoSku().equals("N")){
                  l.setPromoAction(true);
                try {
                    new PreparedSentence(s, "INSERT INTO TEMPTICKETLINES (PRODUCTID, CAMPAIGNID, PROMOTIONID,  QUANTITY,ISCROSSPROMOPRODUCT,PROMOTYPE, PRICE, TAXRATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        public void writeValues() throws BasicException {

                            setString(1, l.getProductID());
                            setString(2, l.getCampaignId());
                            setString(3, l.getpromoId());
                            setDouble(4, 1.0);
                            setString(5, l.getIsCrossProduct());
                            setString(6, l.getPromoType());
                            setDouble(7, l.getPrice());
                            setDouble(8, (l.getTax() / l.getMultiply()));
                        }
                    });
                } catch (BasicException ex) {
                    Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        }
    }

    public final synchronized void deleteTempTicketlines() throws BasicException {

        new StaticSentence(s, "DELETE FROM TEMPTICKETLINES", SerializerWriteString.INSTANCE).exec();
    }

    public void updateTempTicketLines(String campaignId, double buyQty, double getQty) throws BasicException {

        Object[] values = new Object[]{campaignId, buyQty, getQty};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.DOUBLE};
        new PreparedSentence(s, "UPDATE TEMPTICKETLINES SET BUYQTY = ?,  GETQTY = ? WHERE CAMPAIGNID = ? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 0})).exec(values);

    }

    public void updateCreditTicket(String billNo) throws BasicException {

        Object[] values = new Object[]{billNo};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE RECEIPTS R, TICKETS T SET R.ISCREDITINVOICED='Y' WHERE R.ID=T.ID AND T.DOCUMENTNO = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }

    public void updateBillNo(int billNo) throws BasicException {

        Object[] values = new Object[]{billNo};
        Datas[] datas = new Datas[]{Datas.INT};
        new PreparedSentence(s, "UPDATE TICKETSNUM SET ID=? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }

    public void updateProduct(String pdtid, double multiply, String ticketid) throws BasicException {

        Object[] values = new Object[]{pdtid, multiply, ticketid};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.STRING};
        new PreparedSentence(s, "UPDATE TICKETLINES SET UNITS = (UNITS + ?) WHERE PRODUCT = ? AND TICKET =? ", new SerializerWriteBasicExt(datas, new int[]{1, 0, 2})).exec(values);

    }

    public void updateCompletedTickets(String billNo) throws BasicException {

        Object[] values = new Object[]{billNo};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE TICKETS SET COMPLETED='Y' WHERE DOCUMENTNO = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }

    public void updateProcessInfo(String procesName, int count) throws BasicException {

        Object[] values = new Object[]{procesName, count};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT};
        new PreparedSentence(s, "UPDATE PROCESSINFO SET COUNT = ? WHERE PROCESSNAME = ? ", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);

    }

    public String getCustomerId() throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, " SELECT CUSTOMERID FROM CUSTOMERSNUM ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();

        return (record[0] == null ? "" : record[0].toString());

    }

    public final FindTicketsInfo getLatestTicketId() throws BasicException {
        return (FindTicketsInfo) new StaticSentence(s, "SELECT T.TICKETID, T.TICKETTYPE, R.DATENEW, P.NAME, C.NAME, SUM(PM.TOTAL), T.DOCUMENTNO "
                + "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PAYMENTS PM ON R.ID = PM.RECEIPT LEFT OUTER JOIN CUSTOMERS C ON C.ID = T.CUSTOMER LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID "
                + "WHERE T.ID=R.ID AND R.DATENEW=(SELECT max(DATENEW) FROM RECEIPTS,TICKETS WHERE RECEIPTS.ID=TICKETS.ID) GROUP BY T.TICKETID, T.TICKETTYPE,R.DATENEW, P.NAME, C.NAME", null, new SerializerReadClass(FindTicketsInfo.class)).find();
    }

    public java.util.List<SalesInfo> getSalesDumpDetails(String orderDate, String paymentMode) {

        List<SalesInfo> lines = new ArrayList<SalesInfo>();

        String query = "SELECT DISTINCT T.DOCUMENTNO,C.NAME,P.NAME,TL.UNITS,(TL.UNITS*TL.PRICE) AS BILLVALUE, T.BILLAMOUNT, R.ID, R.DATENEW FROM RECEIPTS R "
                + "LEFT JOIN  TICKETS T ON R.ID = T.ID "
                + "LEFT JOIN  TICKETLINES TL ON T.ID=TL.TICKET "
                + "LEFT JOIN  PAYMENTS PY ON R.ID = PY.RECEIPT "
                + "LEFT JOIN  CUSTOMERS C ON T.CUSTOMER = C.ID "
                + "LEFT JOIN  PRODUCTS P ON  P.ID = TL.PRODUCT "
                + "WHERE T.COMPLETED='N' AND PY.PAYMENT='" + paymentMode + "' AND T.BILLAMOUNT = PY.TOTAL AND R.DATENEW>='" + orderDate + "' ORDER BY T.DOCUMENTNO";


        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(SalesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }

    public final RetailTicketInfo loadOrderTicket(final String ticketid, final String money) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT O.ORDER_ID, 0 AS TICKETTYPE, O.TOKEN_ID, O.ORDERTIME, '" + money + "' AS MONEY, '' AS ATTRIBUTES, P.ID, P.NAME, O.CUSTOMER_ID, 0 AS CUSTOMERDISCOUNT,  0 AS DISCOUNTRATE, O.TOKEN_ID FROM TBL_ORDER O JOIN PEOPLE P ON O.USER_ID = P.ID WHERE O.TOKEN_ID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                // setInt(1, tickettype);
                setString(1, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT OI.TOKEN_ID, 0 AS LINE,OI.ITEM_ID, P.ATTRIBUTES AS ATTRIBUTES, OI.QUANTITY, OI.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT, '' AS ATTRIBUTES, 0 AS DISCOUNT, P.NAME, OI.PREPARESTATUS "
                    + "FROM tbl_orderitem OI LEFT JOIN PRODUCTS P ON  P.ID=OI.ITEM_ID LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT=TC.ID LEFT OUTER JOIN TAXES T ON TC.ID = T.CATEGORY AND  T.ISSALESTAX='Y' WHERE OI.TOKEN_ID=? ", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticketid));
//            ticket.setPayments(new PreparedSentence(s
//                , "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?"
//                , SerializerWriteString.INSTANCE
//                , new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final RetailTicketInfo loadSharedOrderTicket(final String ticketid, final String money) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT O.TICKETID, 0 AS TICKETTYPE, O.TOKEN_ID, O.ORDERTIME, '" + money + "' AS MONEY, '' AS ATTRIBUTES, P.ID, P.NAME, O.CUSTOMER_ID, 0 AS CUSTOMERDISCOUNT,  0 AS DISCOUNTRATE, O.TOKEN_ID FROM TBL_ORDER O JOIN PEOPLE P ON O.USER_ID = P.ID WHERE O.TOKEN_ID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                // setInt(1, tickettype);
                setString(1, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT OI.TOKEN_ID, 0 AS LINE,OI.ITEM_ID, P.ATTRIBUTES AS ATTRIBUTES, OI.QUANTITY, OI.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT, '' AS ATTRIBUTES, 0 AS DISCOUNT, P.NAME, OI.PREPARESTATUS "
                    + "FROM tbl_orderitem OI LEFT JOIN PRODUCTS P ON  P.ID=OI.ITEM_ID LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT=TC.ID LEFT OUTER JOIN TAXES T ON TC.ID = T.CATEGORY AND  T.ISSALESTAX='Y' WHERE OI.TOKEN_ID=? ", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticketid));
//            ticket.setPayments(new PreparedSentence(s
//                , "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?"
//                , SerializerWriteString.INSTANCE
//                , new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final RetailTicketInfo loadRetailOrderTicket(final String ticketid, final String money) throws BasicException {
        RetailTicketInfo ticket = (RetailTicketInfo) new PreparedSentence(s, "SELECT O.TICKETID, 0 AS TICKETTYPE, O.TOKEN_ID, O.ORDERTIME, '" + money + "' AS MONEY, '' AS ATTRIBUTES, P.ID, P.NAME, O.CUSTOMER_ID, 0 AS CUSTOMERDISCOUNT,  0 AS DISCOUNTRATE, O.TOKEN_ID FROM TBL_ORDER O JOIN PEOPLE P ON O.USER_ID = P.ID WHERE O.TOKEN_ID = ?", SerializerWriteParams.INSTANCE, new SerializerReadClass(RetailTicketInfo.class))
                .find(new DataParams() {
            public void writeValues() throws BasicException {
                // setInt(1, tickettype);
                setString(1, ticketid);
            }
        });
        if (ticket != null) {

            String customerid = ticket.getCustomerId();
            ticket.setCustomer(customerid == null
                    ? null
                    : loadCustomerExt(customerid));

            ticket.setLines(new PreparedSentence(s, "SELECT OI.TOKEN_ID, 0 AS LINE,OI.ITEM_ID, P.ATTRIBUTES AS ATTRIBUTES, sum(OI.QUANTITY), OI.PRICE, T.ID, T.NAME, T.CATEGORY, T.CUSTCATEGORY, T.PARENTID, T.RATE, T.RATECASCADE, T.RATEORDER, T.ISSALESTAX,T.ISPURCHASETAX, T.ISSERVICETAX, T.DEBITACCOUNT,T.CREDITACCOUNT, '' AS ATTRIBUTES, 0 AS DISCOUNT, P.NAME, OI.PREPARESTATUS "
                    + "FROM tbl_orderitem OI LEFT JOIN PRODUCTS P ON  P.ID=OI.ITEM_ID LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT=TC.ID LEFT OUTER JOIN TAXES T ON TC.ID = T.CATEGORY AND  T.ISSALESTAX='Y' WHERE OI.TOKEN_ID=? GROUP BY  OI.ITEM_ID", SerializerWriteString.INSTANCE, new SerializerReadClass(RetailTicketLineInfo.class)).list(ticketid));
//            ticket.setPayments(new PreparedSentence(s
//                , "SELECT PAYMENT, TOTAL, TRANSID FROM PAYMENTS WHERE RECEIPT = ?"
//                , SerializerWriteString.INSTANCE
//                , new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));
        }
        return ticket;
    }

    public final ProductInfoExt getProductInfoAddon(String id) throws BasicException {
        System.out.println("products selection 19");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE ,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,CATEGORIES C  WHERE U.ID=P.UOM AND P.CATEGORY=C.ID AND P.id = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' ", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(id);
    }

    public ProductInfoExt CountAddonProduct(String product) throws BasicException {
        System.out.println("products selection 20");
        return (ProductInfoExt) new PreparedSentence(s,
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C ON P.CATEGORY=C.ID WHERE L.LINKEDTO = ?   AND P.ISACTIVE='Y' ORDER BY P.REFERENCE",
                SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(product);
    }

    public int getOrderPreparationStatus(String tokenId, int status) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM TBL_ORDER WHERE TOKEN_ID='" + tokenId + "' AND PREPARESTATUS=" + status + " ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getOrderItemPreparationStatus(String tokenId) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM TBL_ORDERITEM WHERE TOKEN_ID='" + tokenId + "' AND PREPARESTATUS!=3", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.INT})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getticketsnum() throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT ID FROM TICKETSNUM", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.INT})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    //method to save ticket kinda entries in invoice and invoicelines successively
    public final synchronized void saveRetailInvoiceTicket(final RetailTicketInfo ticket, final String location,
            final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.ArrayList<BuyGetPriceInfo> pdtBuyGetPriceList, final String chequeNos, final String deliveryBoy,
            final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status,
            final String isCredit, final String isPaidStatus, final double tips, final String orderTaking, final String nonChargable)
            throws BasicException {

        Transaction t = new Transaction(s) {
            int line = -1;

            public Object transact() throws BasicException {

                // new ticket     
                 record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO INVOICE (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,"
                        + "LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,"
                        + "ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,"
                        + "SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,"
                        + "ROUNDOFFVALUE,PARENTBILLNO,DISCOUNTREASONID,ISACTIVE,BILLPARENT,PRINTDATE,SWACHBHARATTAXAMT,ACCOUNTDATE) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, deliveryBoy);
                        setDouble(14, advanceissued);
                        setString(15, cod);
                        setString(16, status);
                        setDouble(17, ticket.getTotal());
                        setDouble(18, creditAmt);
                        setString(19, isCredit);
                        setString(20, isPaidStatus);
                        setDouble(21, tips);
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        setString(28, nonChargable);
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, ticket.getParentId());
                        setString(32, ticket.getDiscountReasonId());
                        setString(33, "Y"); //while inserting invoice tuple, ticket will always treates as active
                        setInt(34, ticket.getBillParent()); //get the  bill parent from whom it may be derived
                        setTimestamp(35, ticket.getDate());
                        setDouble(36, ticket.getSwatchBharatTax());
                        setTimestamp(37, ticket.getAccountDate());
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                //insert all line items of bill
                for (final RetailTicketLineInfo l : ticket.getLines()) {
                    line = line + 1;
                    double price = 0.0;
//                    if (pdtBuyGetPriceList != null) {
//                        for (int i = 0; i < pdtBuyGetPriceList.size(); i++) {
//                            String productId = pdtBuyGetPriceList.get(i).getProductID();
//                            if (l.getProductID().equals(productId)) {
//                                if (pdtBuyGetPriceList.get(i).getQuantity() == l.getMultiply()) {
//                                    l.setPrice(0.0);
//                                } else {
//
//
//                                    double qty = l.getMultiply() - pdtBuyGetPriceList.get(i).getQuantity();
//                                    l.setMultiply(qty);
//                                    pdtBuyGetPriceList.get(i).setProductId(l.getProductID());
//                                    pdtBuyGetPriceList.get(i).setPrice(0.0);
//                                    pdtBuyGetPriceList.get(i).setQuantity(pdtBuyGetPriceList.get(i).getQuantity());
//                                    pdtBuyGetPriceList.get(i).setTaxCat(l.getTaxInfo().getId());
//                                    pdtBuyGetPriceList.get(i).setCategory(l.getProductCategoryID());
//                                    pdtBuyGetPriceList.get(i).setAttributesId(l.getProductAttSetId());
//                                    pdtBuyGetPriceList.get(i).setCampaignId(l.getCampaignId());
//                                    pdtLeastPriceList.add(pdtBuyGetPriceList.get(i));
//                                    ticket.setPriceInfo(pdtLeastPriceList);
////                                     for(int k=0;k<pdtLeastPriceList.size();k++){
//                                    // "CA73D86920FC491799A3F5F0EEC28E3F" if(l.getProductID()==pdtLeastPriceList.get(k).getProductID() && l.getMultiply()!=pdtLeastPriceList.get(k).getQuantity()){
//
//                                    //   insertTicket(pdtLeastPriceList, ticket, l.getTicketLine()+k);
//                                    //  }
////                                     }
//                                    //insertTicket(ticket, l.getTicketLine(), l.getProductID(),qty,l.getProductAttSetInstId(),l.getTaxInfo().getId(),l.getProductAttSetId());
//                                }
//                            }
//
//                        }
//                    }
//

                    new PreparedSentence(s, "INSERT INTO INVOICELINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID,"
                            + " UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SWACHBHARATTAXID) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, line);
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                setDouble(6, l.getDiscount());
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                            if(l.getPromoType().equals("BOGO")){
                                //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            String swachTaxid = null;
                            try {
                                swachTaxid = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swachTaxid = null;
                            }
                            setString(19, swachTaxid);
                        }
                    });
                    printlogger.info("Bill Printed Successfully  " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + ticket.getLinesCount() + "," + "Kot No: " + l.getKotid() + "," + "Table: " + ticket.getTableName() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + l.getProductName() + "," + "Qty: " + l.getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }

                return null;
            }
        };
        t.execute();
    }

    public void disableInvoiceTickets(int billNo) throws BasicException {

        Object[] values = new Object[]{billNo};
        Datas[] datas = new Datas[]{Datas.INT};
        new PreparedSentence(s, "UPDATE INVOICE SET ISCANCELTICKET='Y',CANCELCOMMENTS='Cancelled & Reprinted', ISACTIVE='N' WHERE TICKETID = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }

    public void disableInvoiceTickets(String comments, int billNo) throws BasicException {
        Object[] values = new Object[]{comments, billNo};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT};
        new PreparedSentence(s, "UPDATE INVOICE SET ISCANCELTICKET='Y',CANCELCOMMENTS=?, ISACTIVE='N' WHERE TICKETID = ? ", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
    }

    public final Integer getNextTicketOrderNumber() throws BasicException {
        return (Integer) s.DB.getSequenceSentence(s, "ORDERNUM").find();
    }

    public final SentenceList getSectionList() {
        return new StaticSentence(s, "SELECT ID, NAME,NAME FROM FLOORS ", null, CategoryInfo.getSerializerRead());
    }

    //this method to reprint the settled bill
    public final RetailTicketInfo getRetailPrintedTicket(int Id) throws BasicException {
        System.out.println("retriving content from  db" + Id);
        if (Id == 0) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT CONTENT FROM TICKETS WHERE TICKETID = ?", SerializerWriteInteger.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }
    //This method to save the reprint reason

    public void SaveReprintReason(String reason, int billNo) throws BasicException {
        Object[] values = new Object[]{reason, billNo};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT};
        new PreparedSentence(s, "UPDATE TICKETS SET REPRINTREASON= ? WHERE TICKETID= ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
    }

    //Loyality Code Queries      
    public String getLoyalityCode() throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM LOYALITYCOUPON WHERE BILLNO=? LIMIT 1", SerializerWriteInteger.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(0);
        return record == null ? "" : record[0].toString();
    }

    public void updateLoyalityCode(String id, int billNo) throws BasicException {
        Object[] values = new Object[]{id, billNo};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.INT};
        new PreparedSentence(s, "UPDATE LOYALITYCOUPON SET BILLNO= ? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);

    }
    //newly added methods for line level service charge and service tax

    public final SentenceList getRetailServiceChargeList() {
        return new StaticSentence(s, "SELECT ID, NAME, RATE FROM SERVICECHARGE ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new ServiceChargeInfo(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getDouble(3));
            }
        });
    }

    public final SentenceList getRetailServiceTaxList() {
        return new StaticSentence(s, "SELECT ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER, ISSALESTAX, ISPURCHASETAX,ISSERVICETAX,DEBITACCOUNT,CREDITACCOUNT,ISSERVICECHARGE,BASEAMOUNT,TAXBASEID,ISTAKEAWAY FROM TAXES WHERE ISSALESTAX='Y' AND ISSERVICETAX='Y' ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxInfo(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getString(3),
                        dr.getString(4),
                        dr.getString(5),
                        dr.getDouble(6).doubleValue(),
                        dr.getBoolean(7).booleanValue(),
                        dr.getInt(8),
                        dr.getString(9),
                        dr.getString(10),
                        dr.getString(11),
                        dr.getString(12),
                        dr.getString(13),
                        dr.getString(14),
                        dr.getString(15),
                        dr.getString(16),
                        dr.getString(17));
            }
        });
    }

    public final SentenceList getRetailSwachBharatTaxList() {
        return new StaticSentence(s, "SELECT ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER, ISSALESTAX, ISPURCHASETAX,ISSERVICETAX,DEBITACCOUNT,CREDITACCOUNT,ISSERVICECHARGE,BASEAMOUNT,TAXBASEID,ISTAKEAWAY  FROM TAXES WHERE ISSALESTAX='Y' AND ISSWACHBHARATTAX='Y' ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxInfo(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getString(3),
                        dr.getString(4),
                        dr.getString(5),
                        dr.getDouble(6).doubleValue(),
                        dr.getBoolean(7).booleanValue(),
                        dr.getInt(8),
                        dr.getString(9),
                        dr.getString(10),
                        dr.getString(11),
                        dr.getString(12),
                        dr.getString(13),
                        dr.getString(14),
                        dr.getString(15),
                        dr.getString(16),
                        dr.getString(17));
            }
        });
    }
    //products service charge master

    public final SentenceList getServiceChargeList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM SERVICECHARGE ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
            }
        });
    }
    //products service tax master

    public final SentenceList getServiceTaxList() {
        return new StaticSentence(s, "SELECT C.ID, C.NAME FROM TAXES T LEFT JOIN TAXCATEGORIES C ON T.CATEGORY=C.ID WHERE T.ISSERVICETAX='Y' GROUP BY C.ID ORDER BY C.NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public final SentenceList getSwachBharatTaxList() {
        return new StaticSentence(s, "SELECT C.ID, C.NAME FROM TAXES T LEFT JOIN TAXCATEGORIES C ON T.CATEGORY=C.ID WHERE T.ISSWACHBHARATTAX='Y' GROUP BY C.ID ORDER BY C.NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new TaxCategoryInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    public final SentenceList getProductionareatypeList() {
        return new StaticSentence(s, "SELECT ID, NAME FROM PRODUCTIONAREATYPE ORDER BY NAME", null, new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new ListInfo(dr.getString(1), dr.getString(2));
            }
        });
    }

    //Time based Menu selection
    public List<MenuInfo> getMenuId(String day) throws BasicException {
        Date sysdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentDate = sdf.format(sysdate);
        String currentTime = time.format(sysdate);
        return (List<MenuInfo>) new PreparedSentence(s, "SELECT ID,STARTTIME,ENDTIME FROM MENU WHERE ACTIVE='Y' AND STARTDATE<='" + currentDate + "' AND STARTTIME<='" + currentTime + "' AND ENDDATE>='" + currentDate + "' AND ENDTIME>'" + currentTime + "'  AND (ALLDAY='Y' OR " + day + "='Y') ", null, new SerializerReadClass(MenuInfo.class)).list();
    }

    // 1st level
//      public final List<CategoryInfo> getMenuRootCategories(String menuId) throws BasicException {
//        return new PreparedSentence(s
//            , "SELECT CATEGORIES.ID, CATEGORIES.NAME, CATEGORIES.IMAGE FROM MENUPRICELIST LEFT JOIN CATEGORIES ON MENUPRICELIST.PRODUCTCAT=CATEGORIES.ID  WHERE PARENTID IS NULL AND MENUID='"+menuId+"' GROUP BY CATEGORIES.ID ORDER BY CATEGORIES.NAME"
//            , null
//            , CategoryInfo.getSerializerRead()).list();
//    }
//      
    //2 level wrong
//  public final List<CategoryInfo> getMenuRootCategories(String menuId) throws BasicException {
//        return new PreparedSentence(s
//            , "SELECT CATEGORIES.ID, CATEGORIES.NAME, CATEGORIES.IMAGE FROM CATEGORIES WHERE ID IN ( SELECT c.PARENTID FROM CATEGORIES c LEFT JOIN products p ON p.category=c.id LEFT JOIN menupricelist m ON  m.productcat=c.id AND  c.parentid IS NULL AND MENUID='"+menuId+"' " +
//              "GROUP BY c.ID ORDER BY c.NAME)"
//            , null
//            , CategoryInfo.getSerializerRead()).list();
//    }
    //2 level
    public final List<CategoryInfo> getMenuRootCategories(String menuId) throws BasicException {
        return new PreparedSentence(s, "SELECT CATEGORIES.ID, CATEGORIES.NAME, CATEGORIES.IMAGE FROM CATEGORIES WHERE ID IN ( SELECT coalesce(c.PARENTID,c.id) FROM menupricelist m LEFT JOIN CATEGORIES c  ON  m.productcat=c.id  AND MENUID='" + menuId + "' "
                + "GROUP BY c.ID ORDER BY c.NAME)", null, CategoryInfo.getSerializerRead()).list();
    }

    public final List<CategoryInfo> getMenuSubcategories(String category, String menuId) throws BasicException {
        return new PreparedSentence(s, "SELECT CATEGORIES.ID, CATEGORIES.NAME, CATEGORIES.IMAGE FROM MENUPRICELIST LEFT JOIN CATEGORIES ON MENUPRICELIST.PRODUCTCAT=CATEGORIES.ID WHERE PARENTID = ? AND MENUID='" + menuId + "' GROUP BY CATEGORIES.ID ORDER BY CATEGORIES.NAME ", SerializerWriteString.INSTANCE, CategoryInfo.getSerializerRead()).list(category);
    }

    //method called on clicking product 
    public List<ProductInfoExt> getMenuProductCatalog(String category, String menuId) throws BasicException {
        System.out.println("products selection 21");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP, U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, UOM U,MENUPRICELIST M,CATEGORIES C  WHERE P.ID = O.PRODUCT AND U.ID=P.UOM AND M.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND P.CATEGORY = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' AND MENUID='" + menuId + "' "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(category);
    }
    //method called on clicking product 

    public List<ProductInfoExt> getMenuProductComments(String id, String menuId) throws BasicException {
        System.out.println("products selection 22");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, MN.PRICEBUY, MN.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, MN.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, PRODUCTS_COM M, UOM U,MENUPRICELIST MN,CATEGORIES C WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2  AND U.ID=P.UOM AND MN.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND M.PRODUCT = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' AND MENUID='" + menuId + "' "
                + "AND P.ISCOM = " + s.DB.TRUE() + " "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(id);
    }

    //method called while loading tables (as of now this method not been called anywhere)
    public final List<ProductInfoExt> getMenuProductDetails(String menuId) throws BasicException {
        System.out.println("products selection 23");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P,UOM U,MENUPRICELIST M,CATEGORIES C  WHERE U.ID=P.UOM AND M.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' AND M.MENUID='" + menuId + "' ORDER BY P.NAME", null, ProductInfoExt.getSerializerRead()).list();
    }

    //method called while loading tables  
    public List<ProductInfoExt> getMenuPopularProduct(String popular, String menuId) throws BasicException {
        System.out.println("products selection 24");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP, U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P, PRODUCTS_CAT O, UOM U,MENUPRICELIST M,CATEGORIES C WHERE P.ID = O.PRODUCT AND U.ID=P.UOM AND M.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND P.POPULAR =? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' AND M.MENUID='" + menuId + "' "
                + "ORDER BY O.CATORDER, P.NAME", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(popular);
    }

    public final ProductInfoExt getMenuProductInfoAddon(String id, String menuId) throws BasicException {
        System.out.println("products selection 25");
        return (ProductInfoExt) new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,U.NAME,P.PRODUCTTYPE,P.PRODUCTIONAREATYPE ,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P,UOM U,MENUPRICELIST M,CATEGORIES C  WHERE U.ID=P.UOM AND M.PRODUCTID=P.ID AND P.CATEGORY=C.ID AND P.id = ? AND P.ISACTIVE='Y' AND P.ISSALESITEM='Y' AND M.MENUID='" + menuId + "' ", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(id);
    }

    public ProductInfoExt CountMenuAddonProduct(String product, String menuId) throws BasicException {
        System.out.println("products selection 26");
        return (ProductInfoExt) new PreparedSentence(s,
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C ON P.CATEGORY=C.ID RIGHT JOIN MENUPRICELIST M ON M.PRODUCTID=P.ID WHERE L.LINKEDTO = ?   AND P.ISACTIVE='Y' AND M.MENUID='" + menuId + "' GROUP BY P.ID ORDER BY P.REFERENCE",
                SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).find(product);
    }

    public List<MenuInfo> getMenuList() throws BasicException {
        Date sysdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(sysdate);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentTime = time.format(sysdate);
        return (List<MenuInfo>) new StaticSentence(s, "SELECT ID,STARTTIME,ENDTIME FROM MENU WHERE  STARTDATE<='" + currentDate + "' AND ENDDATE>='" + currentDate + "' AND ACTIVE='Y' ", null, new SerializerReadClass(MenuInfo.class)).list();
    }

    //Pos Action Access queries 
    public List<PosActionsInfo> getPosActions(String roleId) throws BasicException {

        return (List<PosActionsInfo>) new StaticSentence(s, "SELECT PRINTACCESS, SETTLEACCESS, CANCELACCESS,DISCOUNTACCESS,SPLITACCESS, MOVETABLEACCESS,BILLONHOLDACCESS FROM POSACTIONSACCESS WHERE ROLEID='" + roleId + "' ", null, new SerializerReadClass(PosActionsInfo.class)).list();
    }

    public int getPosActionsCount(String roleId) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM POSACTIONSACCESS WHERE ROLEID=? ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(roleId);
        int i = Integer.parseInt(record[0].toString());
        return (i == 0 ? 0 : i);

    }

    public List<RoleInfo> getRoleList() throws BasicException {

        return (List<RoleInfo>) new StaticSentence(s, "SELECT ID, NAME FROM ROLES ", null, new SerializerReadClass(RoleInfo.class)).list();
    }

    public void insertPosActionsAccess(String roleid, String printAccess, String settleAccess, String cancelAccess, String discountAccess, String splitAccess, String moveTableAccess, String billOnHoldAccess) {
        String id = UUID.randomUUID().toString();
        Object[] values = new Object[]{id, roleid, printAccess, settleAccess, cancelAccess, discountAccess, splitAccess, moveTableAccess, billOnHoldAccess};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO POSACTIONSACCESS (ID, ROLEID, PRINTACCESS,SETTLEACCESS,CANCELACCESS,DISCOUNTACCESS,SPLITACCESS,MOVETABLEACCESS,BILLONHOLDACCESS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePosActionsAccess(String roleId, String printAccess, String settleAccess, String cancelAccess, String discountAccess, String splitAccess, String moveTableAccess, String billOnHoldAccess) {

        Object[] values = null;
        values = new Object[]{roleId, printAccess, settleAccess, cancelAccess, discountAccess, splitAccess, moveTableAccess, billOnHoldAccess};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};
        try {
            new StaticSentence(s, "UPDATE POSACTIONSACCESS SET PRINTACCESS =? ,SETTLEACCESS =? ,CANCELACCESS =? ,DISCOUNTACCESS =?, SPLITACCESS =?, MOVETABLEACCESS =?,BILLONHOLDACCESS = ? WHERE ROLEID = ? ", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7, 0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicSystem.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public List<ProductionAreaTypeInfo> getProductionAreaTypeList() throws BasicException {

        return (List<ProductionAreaTypeInfo>) new StaticSentence(s, "SELECT ID,SEARCHKEY,NAME,DESCRIPTION FROM PRODUCTIONAREATYPE ", null, new SerializerReadClass(ProductionAreaTypeInfo.class)).list();
    }

    //method to update production area type access  
    public final synchronized void updatePrAreaRoadMap(final String id, final java.util.List<String> selectedList)
            throws BasicException {

        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {

                new StaticSentence(s, "DELETE FROM PRODUCTIONAREAROLEMAP WHERE ROLEID = ?", SerializerWriteString.INSTANCE).exec(id);

                if (selectedList != null) {
                    for (final String l : selectedList) {
                        new PreparedSentence(s, "INSERT INTO PRODUCTIONAREAROLEMAP (ID, ROLEID, PRODUCTIONAREATYPE) "
                                + "VALUES(?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                            public void writeValues() throws BasicException {
                                setString(1, UUID.randomUUID().toString());
                                setString(2, id);
                                setString(3, l);
                            }
                        });
                    }
                }
                return null;
            }
        };
        t.execute();
    }

    public List<PrAreaMapInfo> getProductionAreaRoadMapList(String roleId) throws BasicException {

        return (List<PrAreaMapInfo>) new StaticSentence(s, "SELECT ID,ROLEID,PRODUCTIONAREATYPE FROM PRODUCTIONAREAROLEMAP WHERE ROLEID ='" + roleId + "'", null, new SerializerReadClass(PrAreaMapInfo.class)).list();
    }

    public SentenceList getHoldTicketsList() {
        return new StaticSentence(s, new QBFBuilder(
                "SELECT ID,TABLEID,TICKETID,BILLAMOUNT,DATENEW,PERSON "
                + " FROM BILLONHOLDTICKETS "
                + "WHERE ?(QBF_FILTER) ", new String[]{"TICKETID", "BILLAMOUNT", "DATENEW", "DATENEW"}), new SerializerWriteBasic(new Datas[]{Datas.OBJECT, Datas.INT, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP}), new SerializerReadClass(FindHoldTicketsInfo.class));
    }

    public final RetailTicketInfo getRetailHoldTicket(int Id) throws BasicException {
        if (Id == 0) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT CONTENT FROM BILLONHOLDTICKETS WHERE TICKETID = ?", SerializerWriteInteger.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.SERIALIZABLE})).find(Id);
            return record == null ? null : (RetailTicketInfo) record[0];
        }
    }

    public final synchronized void SaveHoldTicket(final RetailTicketInfo ticket)
            throws BasicException {

        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                String id = ticket.getPlaceId();
                String splitId = ticket.getSplitSharedId();
                new StaticSentence(s, "DELETE FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE).exec(id);

                new PreparedSentence(s, "INSERT INTO BILLONHOLDTICKETS (ID,TABLEID,CONTENT,TICKETID,BILLAMOUNT,PERSON,DATENEW) "
                        + "VALUES(?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getPlaceId());
                        setObject(3, ticket);
                        setInt(4, ticket.getTicketId());
                        setDouble(5, ticket.getTotal());
                        setString(6, ticket.getUser().getId());
                        setTimestamp(7, ticket.getDate());
                    }
                });


                return null;
            }
        };
        t.execute();
    }

    public final synchronized void saveRetailHoldTicket(final RetailTicketInfo ticket, final String location, final String posNo, final String StoreName, final String ticketDocument, //final ArrayList<VouchersList> paysplits,
            final java.util.List<ResettlePaymentInfo> paymentList, final String chequeNos, final String deliveryBoy, final String homeDelivery, final String cod, final double advanceissued, final double creditAmt, final String status, final String isCredit, final String isPaidStatus, final double tips, final String orderTaking, final String nonChargable, final String holdTicketId) throws Exception {
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;
            int line = -1;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }


                // new receipt
                 record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED,ACCOUNTDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, isCredit);
                        setString(9, "N");
                        setTimestamp(10, ticket.getAccountDate());
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,ROUNDOFFVALUE,PARENTBILLNO,DISCOUNTREASONID,CONTENT,DISCOUNTREASON) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, deliveryBoy);
                        setDouble(14, advanceissued);
                        setString(15, cod);
                        setString(16, status);
                        setDouble(17, ticket.getTotal());
                        setDouble(18, creditAmt);
                        setString(19, isCredit);
                        setString(20, isPaidStatus);
                        setDouble(21, tips);
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        setString(28, nonChargable);
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, ticket.getParentId());
                        setString(32, ticket.getDiscountReasonId());
                        setObject(33, ticket);
                        setString(34, ticket.getDiscountReasonText());

                    }
                });


                for (final RetailTicketLineInfo l : ticket.getLines()) {
                    line = line + 1;
                    double price = 0.0;
                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SOSORDERLINEID,SWACHBHARATTAXID,PRODUCTIONAREA,PRODUCTIONAREATYPE,PRIMARYADDON,ADDONID,SALEPRICE,PROMODISCOUNT,PROMODISCOUNTPRICE,LINEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, line);
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                if (l.getPromodiscountPercent() == 100.00) {
                                    setDouble(6, 0.00);
                                } else {
                                    setDouble(6, l.getDiscount());
                                }
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                           if(l.getPromoType().equals("BOGO")){
                               //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            setString(19, l.getSosorderLineid());
                            String swtaxid = null;
                            try {
                                swtaxid = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swtaxid = null;
                            }
                            setString(20, swtaxid);
                            setString(21, l.getProductionArea());
                            setString(22, l.getProductionAreaType());
                            setInt(23, l.getPrimaryAddon());
                            setString(24, l.getAddonId());
                            setDouble(25, (l.getSalePrice()));
                            setDouble(26, l.getPromodiscountPercent());
                            setDouble(27, l.getOfferDiscount());
                            setString(28, UUID.randomUUID().toString().replaceAll("-", ""));
                            System.out.println("LineId from DATALogic Sales+TicketLines Table -LineId"+l.getLineId());
                        }
                    });
                    settlelogger.info("Bill Settled Successfully " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + ticket.getLinesCount() + "," + "Kot No: " + l.getKotid() + "," + "Table: " + ticket.getTableName() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + l.getProductName() + "," + "Qty: " + l.getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                }

                System.out.println("ticket.getCustomerId()--" + ticket.getCustomerId());
                if (creditAmt != 0 && isCredit.equals("Y")) {
                    new PreparedSentence(s, "INSERT INTO CREDITSALE (ID, TICKETID, BILLNO, BILLDATE, CUSTOMER,BILLAMOUNT,CREDITAMOUNT) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        public void writeValues() throws BasicException {
                            setString(1, UUID.randomUUID().toString());
                            setString(2, ticket.getId());
                            setString(3, ticketDocument);
                            setTimestamp(4, ticket.getDate());
                            setString(5, ticket.getCustomerId());
                            setDouble(6, ticket.getTotal());
                            setDouble(7, creditAmt);

                        }
                    });
                }
//
                SentenceExec paymentinsert = new PreparedSentence(s, "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL, TRANSID, RETURNMSG, CHEQUENOS) VALUES (?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);



                if (paymentList != null) {
                    for (final ResettlePaymentInfo p : paymentList) {
                        final String paymentId = UUID.randomUUID().toString().replaceAll("-", "");;
                        //     if(p.getTotal()!=0){
                        paymentinsert.exec(new DataParams() {
                            public void writeValues() throws BasicException {

                                setString(1, paymentId);
                                setString(2, ticket.getId());
                                setString(3, p.getPaymentType());
                                setDouble(4, p.getAmount());
                                setString(5, ticket.getTransactionID());
                                setBytes(6, (byte[]) Formats.BYTEA.parseValue(ticket.getReturnMessage()));
                                if (p.getPaymentType().equals("Cheque") || p.getPaymentType().equals("Voucher")) {
                                    setString(7, p.getVoucherNo());
                                } else {
                                    setString(7, "");
                                }
                            }
                        });


                        //}
                    }
                }
                SentenceExec taxlinesinsert = new PreparedSentence(s, "INSERT INTO TAXLINES (ID, RECEIPT, TAXID, BASE, AMOUNT,ISSERVICETAX)  VALUES (?, ?, ?, ?, ?,?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (!tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            taxlinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
//                            setDouble(4, tickettax.getSubTotal());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                    setString(6, tickettax.getTaxInfo().getIsServiceTax());
                                }
                            });
                        }
                    }
                }
//                 if (ticket.getServiceTaxes() != null) {
//                    for (final TicketTaxInfo tickettax: ticket.getServiceTaxes()) {
//                        if(tickettax.getTaxInfo().getId()!= null){
//                        taxlinesinsert.exec(new DataParams() { public void writeValues() throws BasicException {
//                            setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
//                            setString(2, ticket.getId());
//                            setString(3, tickettax.getTaxInfo().getId());
//                            //setDouble(4, tickettax.getSubTotal());
//                            setDouble(4, tickettax.getServiceTaxBaseTotal());
//                            setDouble(5, (tickettax.getRetailServiceTax()));
//                            setString(6, tickettax.getTaxInfo().getIsServiceTax());
//                        }});}
//                    }
//                }

                SentenceExec chargelinesinsert = new PreparedSentence(s, "INSERT INTO SERVICECHARGELINES (ID, RECEIPT, SERVICECHARGEID, BASE, AMOUNT)  VALUES (?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE);
                if (ticket.getTaxes() != null) {
                    for (final TicketTaxInfo tickettax : ticket.getTaxes()) {
                        if (tickettax.getTaxInfo().getIsServiceCharge().equals("Y")) {
                            chargelinesinsert.exec(new DataParams() {
                                public void writeValues() throws BasicException {
                                    setString(1, ((UUID.randomUUID().toString()).replaceAll("-", "")));
                                    setString(2, ticket.getId());
                                    setString(3, tickettax.getTaxInfo().getId());
                                    setDouble(4, tickettax.getTaxBaseTotal());
                                    setDouble(5, (tickettax.getRetailTax()));
                                }
                            });
                        }
                    }
                }
                if (ticket.getPlaceId() != null) {
                    deleteHoldTicket(holdTicketId);
                }
                return null;
            }
        };


        t.execute();
    }

    public void deleteHoldTicket(String tableId) throws BasicException {
        new StaticSentence(s, "DELETE FROM BILLONHOLDTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(tableId);
    }

    public void insertActionsLog(String action, String user, String posNo, int ticketId, Date dateNew, String sTable, String dTable, String reason) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        Object[] values = new Object[]{uuid, action, user, posNo, ticketId, dateNew, sTable, dTable, reason};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO ACTIONSLOG (ID,ACTION,USER,POSNO,TICKETID, DATENEW,SOURCETABLE, DESTINATIONTABLE, REASON) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicSales.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<CampaignIdInfo> getActivePromotionCampList(String day) throws BasicException {
        Date sysdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(sysdate);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentTime = time.format(sysdate);
        return (List<CampaignIdInfo>) new StaticSentence(s, "SELECT ID,STARTTIME,ENDTIME FROM PROMOTIONCAMPAIGN WHERE  STARTDATE<='" + currentDate + "' AND ENDDATE>='" + currentDate + "' AND ACTIVE='Y' AND (ALLDAY='Y' OR " + day + "='Y') ", null, new SerializerReadClass(CampaignIdInfo.class)).list();
    }

    public int getCloseDayCount(String date, String host) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CLOSEDDAY WHERE DATESTART<'" + date + "' AND DATEEND IS NULL AND HOST='" + host + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public int getCurrentCloseDayCount(String date, String host) throws BasicException {
        Object[] record = null;

        record = (Object[]) new StaticSentence(s, "SELECT COUNT(*) FROM CLOSEDDAY WHERE DATESTART>='" + date + "' AND DATEEND IS NULL AND HOST='" + host + "' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        int i = Integer.parseInt(record[0] == null ? "0" : record[0].toString());
        return (i == 0 ? 0 : i);


    }

    public String getCurrentCloseDay(String date, String host) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT MONEY FROM CLOSEDDAY  WHERE DATESTART>='" + date + "' AND DATEEND IS NULL AND HOST='" + host + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);
    }

    public String getCurrentCloseCash(String date, String host) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT MONEY FROM CLOSEDCASH  WHERE DATESTART>='" + date + "' AND DATEEND IS NULL AND HOST='" + host + "'", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
        String i = (record[0] == null ? null : record[0].toString());
        return (i == null ? null : i);
    }

    //method called on clicking combo product 
    public List<ProductInfoExt> getMenuMandatoryAddonProducts(String product, String menuId) throws BasicException {
        System.out.println("products selection getMenuMandatoryAddonProducts");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT  "
                + "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C  ON P.CATEGORY=C.ID RIGHT JOIN MENUPRICELIST M ON M.PRODUCTID=P.ID WHERE L.LINKEDTO =? AND M.MENUID='" + menuId + "'  AND P.ISACTIVE='Y' AND P.MANDATORYADDON='Y' "
                + "GROUP BY P.ID ORDER BY P.REFERENCE", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(product);
    }

    public List<ProductInfoExt> getMandatoryAddonProducts(String product) throws BasicException {
        System.out.println("products selection getMandatoryAddonProducts");
        return new PreparedSentence(s, "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME ,P.SWACHBHARATTAX,P.ISCOMBOPRODUCT "
                + "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C ON P.CATEGORY=C.ID WHERE L.LINKEDTO ='" + product + "'   AND P.ISACTIVE='Y' AND P.MANDATORYADDON='Y' "
                + "GROUP BY P.ID ORDER BY P.REFERENCE", SerializerWriteString.INSTANCE, ProductInfoExt.getSerializerRead()).list(product);
    }

    public String getLatestParentTax(String taxCatId, String istakeAway) throws BasicException {
        Object[] record = (Object[]) new StaticSentence(s, "SELECT ID FROM TAXES WHERE CATEGORY = ? AND PARENTID IS NULL AND ISTAKEAWAY='" + istakeAway + "'  AND VALIDFROM <=NOW() ORDER  BY VALIDFROM DESC LIMIT 1 ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(taxCatId);
        return record == null ? null : (String) record[0];
    }

    public final void deleteSharedTicket(final String id) throws BasicException {
        new StaticSentence(s, "DELETE FROM SHAREDTICKETS WHERE ID = ?", SerializerWriteString.INSTANCE).exec(id);
    }

    public final void deleteSharedSplitTicket(final String id, String splitId) throws BasicException {

        new StaticSentence(s, "DELETE FROM SHAREDTICKETS WHERE ID = ? AND SPLITID='" + splitId + "'", SerializerWriteString.INSTANCE).exec(id);
    }
    
        public final synchronized void saveRetailCancelSplitTicket(final RetailTicketInfo ticket, final String StoreName, final String ticketDocument, final String orderTaking, final String location, final String reason, final String reasonId, final String posNo, final String homeDelivery) throws BasicException {
        System.out.println(ticket.getTotal() + "total testing 4 ");
        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
        System.out.println("saving bill data into database---- +");
        // java.util.ArrayList<BuyGetPriceInfo> pdtLeastPriceList;
//        if(ticket.getParentId()!=null){
//              String m_sId = UUID.randomUUID().toString();
//               m_sId = m_sId.replaceAll("-", "");
//               ticket.setId(m_sId);
//           }
        Transaction t = new Transaction(s) {
            private String docNo;
            Integer docNoInt;

            public Object transact() throws BasicException {
                //String customer;k
                try {
                    docNo = getDocumentNo().list().get(0).toString();
                    String[] docNoValue = docNo.split("-");
                    docNo = docNoValue[2];
                } catch (NullPointerException ex) {
                    docNo = "10000";
                }
                if (docNo != null) {
                    docNoInt = Integer.parseInt(docNo);
                    docNoInt = docNoInt + 1;

                    // docNo = docNo+1;
                }

                final String creditNo = StoreName + "-" + posNo + "-" + docNoInt;
                ticket.setCreditNote(creditNo);
//                if (ticket.getTicketId() == 0) {
//                    switch (ticket.getTicketType()) {
//                        case TicketInfo.RECEIPT_NORMAL:
//                            ticket.setTicketId(getNextTicketIndex().intValue());
//                            break;
//                        case TicketInfo.RECEIPT_REFUND:
//
//
//                            break;
//                        case TicketInfo.RECEIPT_PAYMENT:
//                            ticket.setTicketId(getNextTicketPaymentIndex().intValue());
//                            break;
//                        default:
//                            throw new BasicException();
//                    }
//                }
//             
//            final String ticketId = (UUID.randomUUID().toString()).replaceAll("-", "");

                // new receipt
                 record = (Object[]) new StaticSentence(s, "SELECT NOW() FROM DUAL ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find();
                if (record != null) {
                    transactionDate = DateFormats.StringToDateTime((String) record[0]);
                    //  System.out.println("transactionDate"+transactionDate);
                }
                ticket.setDate(transactionDate);
                new PreparedSentence(s, "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, POSNO, ATTRIBUTES, MONEYDAY,UPDATED,ISCREDITSALE,ISCREDITINVOICED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());
                        setString(4, posNo);
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(5, o.toByteArray());
                        } catch (IOException e) {
                            setBytes(5, null);
                        }
                        setString(6, ticket.getActiveDay());
                        setTimestamp(7, ticket.getDate());
                        setString(8, "N");
                        setString(9, "N");
                    }
                });

                // ticket.setCreditNote(creditNo);

                //   System.out.println("emnnr---"+ticketDocument);
                // new ticket
                final double billDiscount = ticket.getLeastValueDiscount() + ticket.getBillDiscount();
                new PreparedSentence(s, "INSERT INTO TICKETS (ID, ORDERNUM, TICKETTYPE, DOCUMENTNO, TICKETID, PERSON, CUSTOMER,BILLDISCOUNT,LEASTVALUEDISCOUNT,CUSTOMERDISCOUNT,DISCOUNTRATE,ISHOMEDELIVERY,DELIVERYBOY,ADVANCEISSUED,ISCOD,COMPLETED,BILLAMOUNT,CREDITAMOUNT,ISCREDITSALE,ISPAIDSTATUS,TIPS,TABLEID,SERVICETAXID,SERVICETAXAMT,SERVICECHARGEID,SERVICECHARGEAMT,ISTAKEAWAY,ISNONCHARGABLE,NOOFCOVERS,ROUNDOFFVALUE,ISCANCELTICKET,CANCELREASONID,CANCELCOMMENTS,PARENTBILLNO,DISCOUNTREASONID,DISCOUNTREASON,SOSORDERID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                    public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getOrderId());
                        setInt(3, ticket.getTicketType());
                        setString(4, ticketDocument);
                        setInt(5, ticket.getTicketId());
                        setString(6, ticket.getUser().getId());
                        setString(7, ticket.getCustomerId());
                        setDouble(8, ticket.getBillDiscount());
                        setDouble(9, ticket.getLeastValueDiscount());
                        setDouble(10, ticket.getDiscountValue());
                        setString(11, ticket.getRate());
                        setString(12, homeDelivery);
                        setString(13, null);
                        setDouble(14, 0.00);
                        setString(15, "N");
                        setString(16, "N");
                        setDouble(17, ticket.getTotal());
                        setDouble(18, 0.00);
                        setString(19, "N");
                        setString(20, "N");
                        setDouble(21, 0.00);
                        setString(22, ticket.getPlaceId());
                        setString(23, ticket.getServiceTaxId());
                        setDouble(24, ticket.getServiceTax());
                        setString(25, ticket.getServiceChargeId());
                        setDouble(26, ticket.getServiceCharge());
                        setString(27, orderTaking);
                        setString(28, "N");
                        setInt(29, ticket.getNoOfCovers());
                        setDouble(30, ticket.getRoundOffvalue());
                        setString(31, "Y");
                        setString(32, reasonId);
                        setString(33, reason);
                        setString(34, ticket.getParentId());
                        setString(35, ticket.getDiscountReasonId());
                        setString(36, ticket.getDiscountReasonText());
                        setString(37, ticket.getSosOrderId());
                    }
                });

                pdtLeastPriceList = new ArrayList<BuyGetPriceInfo>();
                for (final RetailTicketLineInfo l : ticket.getLines()) {




                    new PreparedSentence(s, "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS,DISCOUNT, PRICE, DISCOUNTPRICE, TAXID, ATTRIBUTES, PROMOTIONCAMPAIGNID,KOTID,TABLEID,INSTRUCTION,PERSON,KOTDATE,SERVICECHARGEID,SERVICETAXID,SOSORDERLINEID,SWACHBHARATTAXID,PRODUCTIONAREA,PRODUCTIONAREATYPE,PRIMARYADDON,ADDONID,SALEPRICE,PROMODISCOUNT,PROMODISCOUNTPRICE,LINEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
                        @SuppressWarnings("element-type-mismatch")
                        public void writeValues() throws BasicException {
                            setString(1, ticket.getId());
                            setInt(2, l.getTicketLine());
                            setString(3, l.getProductID());
                            setString(4, l.getProductAttSetInstId());
                            setDouble(5, l.getMultiply());
                            //setDouble(6, l.getDiscount());
                            if (ticket.iscategoryDiscount() && l.getCampaignId().equals("")) {
                                setDouble(6, Double.parseDouble(l.getDiscountrate()) * 100);
                            } else {
                                if (l.getPromodiscountPercent() == 100.00) {
                                    setDouble(6, 0.00);
                                } else {
                                    setDouble(6, l.getDiscount());
                                }
                            }
                            setDouble(7, l.getPrice());
                            //setDouble(8, l.getDiscountPrice());
                            setDouble(8, l.getLineDiscountPrice());
                            String taxid = null;
                            try {
                                taxid = l.getTaxInfo().getId();
                            } catch (NullPointerException e) {
                                taxid = getTaxId();
                            }
                            setString(9, taxid);
                            // }

                            setString(10, l.getProductAttSetId());
                          if(l.getPromoType().equals("BOGO")){
                               //condtion need to b changed to l.isPromoAction once integrating bogo to Habanero
                            if(ticket.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            }
                        }else if(l.getPromoType().equals("SIBG")){
                           if(l.getPromoRule()!=null){
                             //  if(l.getPromoRule().get(0).getIsCrossProduct().equals("N")&& l.getPromoRule().get(0).getIsSku().equals("N")){
                                   if(l.isPromoAction()){
                                setString(11, l.getCampaignId());
                            }else{
                               setString(11,null );  
                            } 
//                               }else{
//                                 setString(11, l.getCampaignId());
//                               }
                           }else{
                               setString(11,null );
                           } 
                        }else{
                        setString(11, l.getCampaignId());
                      }
                            setInt(12, l.getKotid());
                            setString(13, l.getKottable());
                            setString(14, l.getInstruction());
                            setString(15, l.getKotuser());
                            setTimestamp(16, l.getKotdate());
                            String chargeid = null;
                            try {
                                chargeid = l.getChargeInfo().getId();
                            } catch (NullPointerException e) {
                                chargeid = null;
                            }
                            setString(17, chargeid);
                            String servicetaxid = null;
                            try {
                                servicetaxid = l.getServiceTaxInfo().getId();
                            } catch (NullPointerException e) {
                                servicetaxid = null;
                            }
                            setString(18, servicetaxid);
                            setString(19, l.getSosorderLineid());
                            String swtaxid = null;
                            try {
                                swtaxid = l.getSBTaxInfo().getId();
                            } catch (NullPointerException e) {
                                swtaxid = null;
                            }
                            setString(20, swtaxid);
                            setString(21, l.getProductionArea());
                            setString(22, l.getProductionAreaType());
                            setInt(23, l.getPrimaryAddon());
                            setString(24, l.getAddonId());
                            setDouble(25, (l.getSalePrice()));
                            setDouble(26, l.getPromodiscountPercent());
                            setDouble(27, l.getOfferDiscount());
                            setString(28, UUID.randomUUID().toString().replaceAll("-", ""));
                        }
                    });
                }

              

                if (ticket.getPlaceId() != null && !(ticket.getSplitValue().equals("Split"))) {
                    deleteTableCovers(ticket.getPlaceId());
                }
                return null;
            }
        };


        t.execute();
    }
        public int getLeastPromo(String campaignId) throws BasicException {
         Object[] record = null;
        record = (Object[]) new StaticSentence(s
                , "SELECT COUNT(*)  FROM PROMOTIONRULE WHERE PROMOTIONCAMPAIGNID IN ("+campaignId+") AND (PROMOTIONTYPEID='SIBG' )AND ISPROMOPRODUCT='Y' AND ISCROSSPROMOPRODUCT='N' AND ISSKU='N' "
                , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);

    }
        public int getBuyGetPromo(String campaignId) throws BasicException {
         Object[] record = null;
        record = (Object[]) new StaticSentence(s
                , "SELECT COUNT(*)  FROM PROMOTIONRULE WHERE PROMOTIONCAMPAIGNID IN ("+campaignId+") AND (PROMOTIONTYPEID='SIBG' )AND ISPROMOPRODUCT='Y' AND ISCROSSPROMOPRODUCT='N' AND ISSKU='Y' "
                , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);

    }
}