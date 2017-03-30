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
package com.sysfore.pos.purchaseorder;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.sysfore.pos.cashmanagement.CashSetupinfo;
import com.sysfore.pos.cashmanagement.CreditNoteinfo;
import com.sysfore.pos.cashmanagement.CurrencyInfo;
import com.sysfore.pos.cashmanagement.CurrencyValueInfo;
import com.sysfore.pos.cashmanagement.DenominationTable;
import com.sysfore.pos.cashmanagement.FloatCashSetupinfo;
import com.sysfore.pos.cashmanagement.PettyCashSetupinfo;
import com.sysfore.pos.cashmanagement.Posinfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateen
 */
public class PurchaseOrderReceipts extends BeanFactoryDataSingle {

    private Session s;

    /**
     * Creates a new instance of PurchaseOrderReceipts
     */
    public PurchaseOrderReceipts() {
    }

    public void init(Session s) {
        this.s = s;
    }

    public void insertPO(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subTotal, String tax, String createdby) {

        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subTotal, tax, createdby};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO PURCHASEORDER (ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void insertGoodsReceipts(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subTotal, String tax, String createdby,String warehouse) {

        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subTotal, tax, createdby,warehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO GOODSRECEIPTS (ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX, CREATEDBY,LOCATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void insertGoodsReturn(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subTotal, String tax, String createdby,String warehouse) {

        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subTotal, tax, createdby,warehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO GOODSRETURN (ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX, CREATEDBY,LOCATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void insertPOLines(ProductInfoExt product, String poId) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, product.getID(), product.getMultiply(), product.getPriceSell(), product.getTaxCategoryID()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PURCHASEORDERLINES (ID, POID, PRODUCTID, QUANTITY, PRICE, TAXID) VALUES (?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5})).exec(values);

    }

    public void insertPOLines2(String poId, String productId, double qty, double priceSell, String tax, String taxCatId) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, productId, qty, priceSell, tax, taxCatId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PURCHASEORDERLINES (ID, POID, PRODUCTID, QUANTITY, PRICE, TAXID, TAXCATEGORYID) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);

    }
     public void insertGRNLines(String poId, String productId, double qty, double priceSell, String tax, String taxCatId) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, productId, qty, priceSell, tax, taxCatId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO GOODSRECEIPTLINES (ID, RECEIPTID, PRODUCTID, QUANTITY, PRICE, TAXID, TAXCATEGORYID) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);

    }
public void insertGRRLines(String poId, String productId, double qty, double priceSell, String tax, String taxCatId) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, productId, qty, priceSell, tax, taxCatId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO GOODSRETURNLINES (ID, RETURNID, PRODUCTID, QUANTITY, PRICE, TAXID, TAXCATEGORYID) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);

    }
    public PreparedSentence getDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM PURCHASEORDER", null, SerializerReadString.INSTANCE);

    }
    public PreparedSentence getGRNDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM GOODSRECEIPTS", null, SerializerReadString.INSTANCE);

    }
      public PreparedSentence getGRRDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM GOODSRETURN", null, SerializerReadString.INSTANCE);

    }
 public PreparedSentence getDocumentPINo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM PURCHASEINVOICE", null, SerializerReadString.INSTANCE);

    }
       public final List<CurrencyInfo> getCurrencyInfobyCountry(String country) throws BasicException {

        return (List<CurrencyInfo>) new StaticSentence(s, "SELECT ID, CURRENCYNAME, DENOMINATIONS FROM CURRENCYDENOMINATIONS WHERE CURRENCYNAME='" + country + "' ", null, new SerializerReadClass(CurrencyInfo.class)).list();
    }
 public final List<CurrencyValueInfo> getCurrencyValue() throws BasicException {

        return (List<CurrencyValueInfo>) new StaticSentence(s, "SELECT ID, RUPEES, COUNT, DENOMINATIONVALUE FROM currencyreconciliation WHERE COMMENT=NULL ", null, new SerializerReadClass(CurrencyValueInfo.class)).list();
    }
    public void insertReconciliation(List<DenominationTable> dTable, double total, String posNo) throws BasicException {

        String denominationId = UUID.randomUUID().toString();

        for (DenominationTable den : dTable) {
            Object[] values = new Object[]{UUID.randomUUID().toString(), denominationId, den.getRupees(), den.getCount(), total, new Date(),posNo,den.getTotal()};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.INT, Datas.DOUBLE, Datas.TIMESTAMP, Datas.STRING,Datas.DOUBLE};
            new PreparedSentence(s, "INSERT INTO CURRENCYRECONCILIATION(ID, DENOMINATIONID, RUPEES, COUNT, TOTAL, DATE,POSNO,DENOMINATIONVALUE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);
        }
    }

    public void updatePO(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subtotal, String tax) {
        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subtotal, tax};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE PURCHASEORDER SET DOCUMENTNUMBER= ?, VENDOR=?, CREATED= ?, DELIVERY= ?, STATUS= ?, TOTAL=?, SUBTOTAL=?, TAX=? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void updateGRN(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subtotal, String tax,String warehouse) {
        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subtotal, tax,warehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE GOODSRECEIPTS SET DOCUMENTNUMBER= ?, VENDOR=?, CREATED= ?, DELIVERY= ?, STATUS= ?, TOTAL=?, SUBTOTAL=?, TAX=?, LOCATION=? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7, 8,9,0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  public void updateGRR(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subtotal, String tax,String warehouse) {
        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subtotal, tax,warehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE GOODSRETURN SET DOCUMENTNUMBER= ?, VENDOR=?, CREATED= ?, DELIVERY= ?, STATUS= ?, TOTAL=?, SUBTOTAL=?, TAX=?,LOCATION=? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7, 8,9, 0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  /*  public java.util.List<PurchaseOrderInfo> getPurchaseOrders() throws BasicException {

        return (List<PurchaseOrderInfo>) new StaticSentence(s, "SELECT ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX FROM PURCHASEORDER WHERE ISCLOSED = 'N' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(PurchaseOrderInfo.class)).list();

    }*/
     public java.util.List<PurchaseOrderInfo> getPurchaseOrders() throws BasicException {

        return (List<PurchaseOrderInfo>) new StaticSentence(s, "SELECT PURCHASEORDER.ID, PURCHASEORDER.DOCUMENTNUMBER, CUSTOMERS.NAME, PURCHASEORDER.CREATED, PURCHASEORDER.DELIVERY, PURCHASEORDER.STATUS,PURCHASEORDER.TOTAL, PURCHASEORDER.SUBTOTAL, PURCHASEORDER.TAX,CUSTOMERS.BILLADDRESS FROM PURCHASEORDER,CUSTOMERS WHERE PURCHASEORDER.VENDOR = CUSTOMERS.ID AND ISCLOSED = 'N' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(PurchaseOrderInfo.class)).list();

    }
 public java.util.List<GoodsReceiptsInfo> getGRN() throws BasicException {

        return (List<GoodsReceiptsInfo>) new StaticSentence(s, "SELECT GOODSRECEIPTS.ID, GOODSRECEIPTS.DOCUMENTNUMBER, CUSTOMERS.NAME, GOODSRECEIPTS.CREATED, GOODSRECEIPTS.DELIVERY, GOODSRECEIPTS.STATUS,GOODSRECEIPTS.TOTAL, GOODSRECEIPTS.SUBTOTAL, GOODSRECEIPTS.TAX,CUSTOMERS.BILLADDRESS,LOCATION FROM GOODSRECEIPTS,CUSTOMERS WHERE GOODSRECEIPTS.VENDOR = CUSTOMERS.ID AND ISCLOSED = 'N' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(GoodsReceiptsInfo.class)).list();

    }


   public java.util.List<GoodsReturnInfo> getGRR() throws BasicException {

        return (List<GoodsReturnInfo>) new StaticSentence(s, "SELECT GOODSRETURN.ID, GOODSRETURN.DOCUMENTNUMBER, CUSTOMERS.NAME, GOODSRETURN.CREATED, GOODSRETURN.DELIVERY, GOODSRETURN.STATUS,GOODSRETURN.TOTAL, GOODSRETURN.SUBTOTAL, GOODSRETURN.TAX,CUSTOMERS.BILLADDRESS,GOODSRETURN.LOCATION FROM GOODSRETURN,CUSTOMERS WHERE GOODSRETURN.VENDOR = CUSTOMERS.ID AND ISCLOSED = 'N' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(GoodsReturnInfo.class)).list();

    }


    public void deletePurchaseOrder(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM PURCHASEORDER WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        new PreparedSentence(s, "DELETE FROM PURCHASEORDERLINES WHERE POID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    public void deleteGRN(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM GOODSRECEIPTS WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        new PreparedSentence(s, "DELETE FROM GOODSRECEIPTLINES WHERE RECEIPTID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
public void deleteGRR(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM GOODSRETURN WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        new PreparedSentence(s, "DELETE FROM GOODSRETURNLINES WHERE RETURNID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    public void closePurchaseOrder(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE PURCHASEORDER SET ISCLOSED='Y' WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }

     public void closeGRN(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE GOODSRECEIPTS SET ISCLOSED='Y' WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
public void closeGRR(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE GOODSRETURN SET ISCLOSED='Y' WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    public void deletePurchaseOrderLines(String POID) {
        try {
            Object[] values = new Object[]{POID};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM PURCHASEORDERLINES WHERE POID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deleteGRNLines(String id) {
        try {
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM GOODSRECEIPTLINES WHERE RECEIPTID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void deleteGRRLines(String id) {
        try {
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM GOODSRETURNLINES WHERE RETURNID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deletePurchaseOrderLinesByProduct(String opId, String productId) throws BasicException {
        Object[] values = new Object[]{opId, productId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "DELETE FROM PURCHASEORDERLINES WHERE POID= ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
    }
    public void deleteGoodsReceiptsLinesByProduct(String opId, String productId) throws BasicException {
        Object[] values = new Object[]{opId, productId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "DELETE FROM GOODSRECEIPTLINES WHERE RECEIPTID= ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
    }
public void deleteGoodsReturnLinesByProduct(String opId, String productId) throws BasicException {
        Object[] values = new Object[]{opId, productId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "DELETE FROM GOODSRETURNLINES WHERE RETURNID= ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
    }
    public java.util.List<PurchaseOrderProductInfo> getPOLines(String id) {
        List<PurchaseOrderProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID,"
                + " PLINES.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, PLINES.PRICE AS PRICE, "
                + " PLINES.TAXID AS TAX, PLINES.TAXCATEGORYID AS TAXCATID, U.NAME FROM PURCHASEORDERLINES PLINES"
                + " LEFT JOIN PRODUCTS P ON PLINES.PRODUCTID = P.ID LEFT JOIN UOM U ON U.ID = P.UOM WHERE PLINES.POID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseOrderProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    public java.util.List<GoodsReceiptsProductInfo> getGRNLines(String id) {
        List<GoodsReceiptsProductInfo> lines = null;
        String query = "SELECT grn.ID AS ID, grn.RECEIPTID AS GRNID, grn.PRODUCTID AS PRODUCTID,"
                + " grn.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, grn.PRICE AS PRICE, "
                + " grn.TAXID AS TAX, grn.TAXCATEGORYID AS TAXCATID, U.NAME FROM GOODSRECEIPTLINES grn"
                + " LEFT JOIN PRODUCTS P ON grn.PRODUCTID = P.ID LEFT JOIN UOM U ON P.UOM=U.ID WHERE grn.RECEIPTID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReceiptsProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
public java.util.List<GoodsReturnProductInfo> getGRRLines(String id) {
        List<GoodsReturnProductInfo> lines = null;
        String query = "SELECT grn.ID AS ID, grn.RETURNID AS GRNID, grn.PRODUCTID AS PRODUCTID,"
                + " grn.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, grn.PRICE AS PRICE, "
                + " grn.TAXID AS TAX, grn.TAXCATEGORYID AS TAXCATID,U.NAME FROM GOODSRETURNLINES grn"
                + " LEFT JOIN PRODUCTS P ON grn.PRODUCTID = P.ID LEFT JOIN UOM U ON U.ID = P.UOM WHERE grn.RETURNID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReturnProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    public void deletePurchaseOrderSingle(String id) {
        try {
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM PURCHASEORDER WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StaticSentence getTaxListbyProduct(String ptaxid) {

        return new StaticSentence(s, "SELECT ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER, ISSALESTAX,ISPURCHASETAX, ISSERVICETAX, DEBITACCOUNT, CREDITACCOUNT,ISSERVICECHARGE,BASEAMOUNT,TAXBASEID,ISTAKEAWAY FROM TAXES WHERE TAXES.CATEGORY='" + ptaxid + "' AND TAXES.ISPURCHASETAX='Y' ORDER BY NAME", null, new SerializerRead() {

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
                           dr.getString(13),null,null,null,"N");
            }
        });
    }

    public void updatePOLines(String documentNo, String prodId, double qty, double price, String taxid) {
        Object[] values = new Object[]{documentNo, prodId, qty, price, taxid};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        String query = "UPDATE PURCHASEORDERLINES SET  QUANTITY = ?,  PRICE = ?, TAXID=? WHERE POID = ? AND PRODUCTID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{2, 3, 4, 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateGRNLines(String documentNo, String prodId, double qty, double price, String taxid) {
        Object[] values = new Object[]{documentNo, prodId, qty, price, taxid};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        String query = "UPDATE GOODSRECEIPTLINES SET  QUANTITY = ?,  PRICE = ?, TAXID=? WHERE RECEIPTID = ? AND PRODUCTID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{2, 3, 4, 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  public void updateGRRLines(String documentNo, String prodId, double qty, double price, String taxid) {
        Object[] values = new Object[]{documentNo, prodId, qty, price, taxid};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        String query = "UPDATE GOODSRETURNLINES SET  QUANTITY = ?,  PRICE = ?, TAXID=? WHERE RETURNID = ? AND PRODUCTID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{2, 3, 4, 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public java.util.List<PurchaseOrderProductInfo> getPOLinesByPoidAndProduct(String poid, String productId) {
        List<PurchaseOrderProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,'' FROM PURCHASEORDERLINES PLINES "
                + " WHERE PLINES.POID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseOrderProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    public java.util.List<PurchaseInvoiceProductInfo> getPILinesByPoidAndProduct(String poid, String productId) {
        List<PurchaseInvoiceProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,PLINES.DISCOUNT,'' FROM PURCHASEINVOICELINES PLINES "
                + " WHERE PLINES.POID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
     public java.util.List<GoodsReceiptsProductInfo> getGRNLinesByPoidAndProduct(String poid, String productId) {
        List<GoodsReceiptsProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.RECEIPTID AS GRNID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,'' FROM GOODSRECEIPTLINES PLINES "
                + " WHERE PLINES.RECEIPTID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReceiptsProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
  public java.util.List<GoodsReturnProductInfo> getGRRLinesByPoidAndProduct(String poid, String productId) {
        List<GoodsReturnProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.RETURNID AS GRNID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,'' FROM GOODSRETURNLINES PLINES "
                + " WHERE PLINES.RETURNID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReturnProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
     public void insertAmt(double diffInAmt, String comment, String denominationId, String posNo) throws BasicException {


            Object[] values = new Object[]{UUID.randomUUID().toString(), denominationId, 0, 0, diffInAmt, new Date(),comment,posNo};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.INT, Datas.INT, Datas.DOUBLE, Datas.TIMESTAMP, Datas.STRING, Datas.STRING};
            new PreparedSentence(s, "INSERT INTO CURRENCYRECONCILIATION(ID, DENOMINATIONID, RUPEES, COUNT, TOTAL, DATE, COMMENT, POSNO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);
        }

       public void deleteDiscountLine(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
         public void deleteDenomination() throws BasicException {

        new StaticSentence(s, "DELETE FROM CURRENCYRECONCILIATION WHERE COMMENTS = NULL", SerializerWriteString.INSTANCE).exec();

    }
         public List<CreditNoteinfo> getCreditNoteList() throws BasicException {

        return (List<CreditNoteinfo>) new StaticSentence(s, "SELECT ID, CREDITNOTENO, CUSTOMER, AMOUNT,STATUS,VALIDITY,CREATED FROM CREDITNOTE ORDER BY CREDITNOTENO ", null, new SerializerReadClass(CreditNoteinfo.class)).list();
    }

    public int getFloatCount(String posNo) throws BasicException {

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
      String dateValue= sdf.format(new Date()).toString();

            Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM FLOATCASH WHERE POSNO = '"+posNo+"' AND CREATED =?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(dateValue);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);

    }
     
        public int getPettyCashCount() throws BasicException {

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
      String dateValue= sdf.format(new Date()).toString();

            Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM PETTYCASH WHERE PETTYSTATUS= 'Reset' AND PETTYCASHDATE =?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(dateValue);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);

    }


    public List<CreditNoteinfo> getCreditNotedataList(String creditNo) throws BasicException {
       return (List<CreditNoteinfo>) new StaticSentence(s, "SELECT ID, CREDITNOTENO, CUSTOMER, AMOUNT,STATUS,VALIDITY,CREATED FROM CREDITNOTE WHERE CREDITNOTENO = '"+creditNo+"'", null, new SerializerReadClass(CreditNoteinfo.class)).list();
    }

    public int getValidCreditNote(String creditNo){
        String query = "SELECT COUNT(*) FROM CREDITNOTE WHERE CREDITNOTENO = ? AND STATUS='N' AND VALIDITY > NOW()";
        Object[] obj = null;
        int retVal;

        try {
            obj = (Object[])new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(creditNo);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            retVal = Integer.parseInt(obj[0].toString());
        }catch(NullPointerException npe){
            retVal = 0;
        }

        return retVal;
    }

    public void insertPettyCash(String statusValue, String Reason, double pettcash, double amount, double balanceAmt, Date created, String user, Date pettyDate)  throws BasicException{
         Object[] values = new Object[]{UUID.randomUUID().toString(), statusValue, Reason, pettcash, amount, balanceAmt, created, user, pettyDate};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE,  Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP};
        try {
            new PreparedSentence(s, "INSERT INTO PETTYCASH (ID, PETTYSTATUS, REASON, PETTYCASH, AMOUNT, BALANCEAMT, CREATED, CREATEDBY, PETTYCASHDATE) VALUES (?, ?, ?, ?, ?,  ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public PreparedSentence getBalanceAmt() {

       return new PreparedSentence(s, "SELECT BALANCEAMT FROM PETTYCASH ORDER BY CREATED DESC LIMIT 1", null, SerializerReadString.INSTANCE);

    }

     public PreparedSentence getPettyAmt() {

        return new PreparedSentence(s, "SELECT PETTYCASH FROM PETTYCASH ORDER BY CREATED DESC LIMIT 1", null, SerializerReadString.INSTANCE);

    }
     public PreparedSentence getPettyCash() {

        return new PreparedSentence(s, "SELECT PETTYCASH FROM PETTYCASHSETUP ORDER BY CREATED DESC LIMIT 1", null, SerializerReadString.INSTANCE);

    }

    public void insertMasterCashSetup(double floatCash, double pettyCash, String autoFloat, Date createdDate, String user,Date pettyCashDate, String resetPeriod, String resetDate, String resetDay) {
         Object[] values = new Object[]{UUID.randomUUID().toString(), floatCash, pettyCash, autoFloat, pettyCashDate, resetPeriod, resetDate, resetDay, createdDate, user};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO CASHSETUP (ID, FLOATCASH, PETTYCASH, AUTOFLOAT,DATE,RESETPERIOD,RESETDATE,RESETDAY, CREATED, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void insertPettyCashSetup(double pettyCash, Date createdDate, String user,String resetPeriod, String resetDate, String resetDay) {
         Object[] values = new Object[]{UUID.randomUUID().toString(), pettyCash, resetPeriod, resetDate, resetDay, createdDate, user};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO PETTYCASHSETUP (ID, PETTYCASH, RESETPERIOD,RESETDATE,RESETDAY, CREATED, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public void insertFloatCashSetup(Date floatDate,double floatCash, String autoFloat, Date createdDate, String user) {
         Object[] values = new Object[]{UUID.randomUUID().toString(), floatDate, floatCash, autoFloat,  createdDate, user};
        Datas[] datas = new Datas[]{Datas.STRING,  Datas.TIMESTAMP, Datas.DOUBLE,  Datas.STRING,  Datas.TIMESTAMP, Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO FLOATCASHSETUP (ID, DATE, FLOATCASH, AUTOFLOAT, CREATED, CREATEDBY) VALUES (?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public void insertFloatAmt(double floatcash, String posNo) throws BasicException, ParseException {

      SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      String created = sdf.format(new Date()).toString();
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      Date newDate = null;
        try {
            newDate = (Date) format.parse(created);
        } catch (ParseException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

            Object[] values = new Object[]{UUID.randomUUID().toString(), floatcash, posNo, newDate};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP};
            new PreparedSentence(s, "INSERT INTO FLOATCASH(ID, FLOATCASH, POSNO, CREATED) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);
        }

     public void updateFloatCash(double floatcash, String posNo) {
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
      String created = sdf.format(new Date()).toString();
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
      Date newDate = null;
        try {
            newDate = (Date) format.parse(created);
        } catch (ParseException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[] values = new Object[]{floatcash, posNo, newDate};
        Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP};
        try {
            new PreparedSentence(s, "UPDATE FLOATCASH SET FLOATCASH= ? WHERE POS= ? AND CREATED=?", new SerializerWriteBasicExt(datas, new int[]{0,1,2})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public List<CashSetupinfo> getCashSetupDetails() throws BasicException {
       return (List<CashSetupinfo>) new StaticSentence(s, "SELECT FLOATCASH, PETTYCASH, AUTOFLOAT, DATE, RESETPERIOD,RESETDATE,RESETDAY FROM CASHSETUP ORDER BY CREATED DESC LIMIT 1", null, new SerializerReadClass(CashSetupinfo.class)).list();
    }
  public List<FloatCashSetupinfo> getFloatCashSetupDetails() throws BasicException {
       return (List<FloatCashSetupinfo>) new StaticSentence(s, "SELECT FLOATCASH, AUTOFLOAT, DATE FROM FLOATCASHSETUP ORDER BY CREATED DESC LIMIT 1", null, new SerializerReadClass(FloatCashSetupinfo.class)).list();
    }
  public List<PettyCashSetupinfo> getPettyCashSetupDetails() throws BasicException {
       return (List<PettyCashSetupinfo>) new StaticSentence(s, "SELECT PETTYCASH, RESETPERIOD,RESETDATE,RESETDAY FROM PETTYCASHSETUP ORDER BY CREATED DESC LIMIT 1", null, new SerializerReadClass(PettyCashSetupinfo.class)).list();
    }
 public int getCreditCount(int creditNo) throws BasicException {


            Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM CREDITNOTE WHERE STATUS = 'N' AND CREDITNOTENO = ?" , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.INT})).find(creditNo);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);

    }
 public List<Posinfo> getPosList() throws BasicException {

        return (List<Posinfo>) new StaticSentence(s, "SELECT ID, POSNO FROM POSDETAILS ", null, new SerializerReadClass(Posinfo.class)).list();
    }
 public List<CreditNoteinfo> getValidOpenCreditNoteList(String creditNo) {
        String query = "SELECT ID, CREDITNOTENO, CUSTOMER, AMOUNT,STATUS,VALIDITY,CREATED FROM CREDITNOTE WHERE CREDITNOTENO = ? AND STATUS='N' AND VALIDITY > NOW()";
        List<CreditNoteinfo> c_info = null;
        try {
            c_info = (List<CreditNoteinfo>) new StaticSentence(s, "SELECT ID, CREDITNOTENO, CUSTOMER, AMOUNT,STATUS,VALIDITY,CREATED FROM CREDITNOTE ORDER BY CREDITNOTENO ", null, new SerializerReadClass(CreditNoteinfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c_info;
    }
 public java.util.List<PurchaseOrderInfo> getPO(String id) {
        List<PurchaseOrderInfo> lines = null;
        String query = "SELECT PURCHASEORDER.ID, PURCHASEORDER.DOCUMENTNUMBER, CUSTOMERS.NAME, PURCHASEORDER.CREATED, "
                + " PURCHASEORDER.DELIVERY, PURCHASEORDER.STATUS,PURCHASEORDER.TOTAL, PURCHASEORDER.SUBTOTAL, PURCHASEORDER.TAX, CUSTOMERS.BILLADDRESS "
                + " FROM PURCHASEORDER ,CUSTOMERS WHERE PURCHASEORDER.VENDOR = CUSTOMERS.ID "
                + " AND PURCHASEORDER.DOCUMENTNUMBER='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseOrderInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
 public java.util.List<GoodsReceiptsInfo> getGRN(String id) {
        List<GoodsReceiptsInfo> lines = null;
        String query = "SELECT GOODSRECEIPTS.ID, GOODSRECEIPTS.DOCUMENTNUMBER, CUSTOMERS.NAME, GOODSRECEIPTS.CREATED, "
                + " GOODSRECEIPTS.DELIVERY, GOODSRECEIPTS.STATUS,GOODSRECEIPTS.TOTAL, GOODSRECEIPTS.SUBTOTAL, GOODSRECEIPTS.TAX, CUSTOMERS.BILLADDRESS, LOCATIONS.NAME "
                + " FROM GOODSRECEIPTS ,CUSTOMERS, LOCATIONS WHERE GOODSRECEIPTS.VENDOR = CUSTOMERS.ID AND LOCATIONS.ID=GOODSRECEIPTS.LOCATION"
                + " AND GOODSRECEIPTS.DOCUMENTNUMBER='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReceiptsInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

 public java.util.List<GoodsReturnInfo> getGRR(String id) {
        List<GoodsReturnInfo> lines = null;
        String query = "SELECT GOODSRETURN.ID, GOODSRETURN.DOCUMENTNUMBER, CUSTOMERS.NAME, GOODSRETURN.CREATED, "
                + " GOODSRETURN.DELIVERY, GOODSRETURN.STATUS,GOODSRETURN.TOTAL, GOODSRETURN.SUBTOTAL, GOODSRETURN.TAX, CUSTOMERS.BILLADDRESS, LOCATIONS.NAME "
                + " FROM GOODSRETURN ,CUSTOMERS, LOCATIONS WHERE GOODSRETURN.VENDOR = CUSTOMERS.ID AND LOCATIONS.ID=GOODSRETURN.LOCATION"
                + " AND GOODSRETURN.DOCUMENTNUMBER='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsReturnInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
//   public void insertPurchaseInvoice(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subTotal, String tax, String createdby, Double roundOff) {
//
//        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subTotal, tax, createdby,roundOff};
//        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.DOUBLE};
//        try {
//            new PreparedSentence(s, "INSERT INTO PURCHASEINVOICE (ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX, CREATEDBY,ROUNDOFFVALUE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})).exec(values);
//        } catch (BasicException ex) {
//            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
     public void insertPurchaseInvoiceLines(ProductInfoExt product, String poId) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, product.getID(), product.getMultiply(), product.getPriceSell(), product.getTaxCategoryID()};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PURCHASEINVOICELINES (ID, POID, PRODUCTID, QUANTITY, PRICE, TAXID) VALUES (?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5})).exec(values);

    }
     public void insertPurchaseInvoiceLines2(String poId, String productId, double qty, double priceSell, String tax, String taxCatId, double discount) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, productId, qty, priceSell, tax, taxCatId,discount};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.DOUBLE};
        new PreparedSentence(s, "INSERT INTO PURCHASEINVOICELINES (ID, POID, PRODUCTID, QUANTITY, PRICE, TAXID, TAXCATEGORYID,DISCOUNT) VALUES (?, ?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);

    }
        public PreparedSentence getPIDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM PURCHASEINVOICE", null, SerializerReadString.INSTANCE);

    }
        public void updatePI(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subtotal, String tax, Double chargestotal, double discountTotal,Double roundOff) {
        Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subtotal, tax, chargestotal,discountTotal,roundOff};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.DOUBLE,Datas.DOUBLE,Datas.DOUBLE};
        try {
            new PreparedSentence(s, "UPDATE PURCHASEINVOICE SET DOCUMENTNUMBER= ?, VENDOR=?, CREATED= ?, DELIVERY= ?, STATUS= ?, TOTAL=?, SUBTOTAL=?, TAX=?, CHARGESTOTAL=?, DISCOUNTTOTAL=?, ROUNDOFFVALUE=? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9,10,11, 0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           public java.util.List<PurchaseInvoiceInfo> getPurchaseInvoices() throws BasicException {

        return (List<PurchaseInvoiceInfo>) new StaticSentence(s, "SELECT PURCHASEINVOICE.ID, PURCHASEINVOICE.DOCUMENTNUMBER, CUSTOMERS.NAME, PURCHASEINVOICE.CREATED, PURCHASEINVOICE.DELIVERY, PURCHASEINVOICE.STATUS,PURCHASEINVOICE.TOTAL, PURCHASEINVOICE.SUBTOTAL, PURCHASEINVOICE.TAX,CUSTOMERS.BILLADDRESS,PURCHASEINVOICE.CHARGESTOTAL,PURCHASEINVOICE.DISCOUNTTOTAL FROM PURCHASEINVOICE,CUSTOMERS WHERE PURCHASEINVOICE.VENDOR = CUSTOMERS.ID AND ISCLOSED = 'N' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(PurchaseInvoiceInfo.class)).list();

    }
    public void deletePurchaseInvoice(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM PURCHASEINVOICE WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        new PreparedSentence(s, "DELETE FROM PURCHASEINVOICELINES WHERE POID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
         new PreparedSentence(s, "DELETE FROM PURCHASEINVOICECHARGES WHERE PIID= ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    public void deletePurchaseInvoiceCharges(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
           new PreparedSentence(s, "DELETE FROM PURCHASEINVOICECHARGES WHERE PIID= ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
    public void closePurchaseInvoice(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE PURCHASEINVOICE SET ISCLOSED='Y' WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
     public void deletePurchaseInvoiceLines(String POID) {
        try {
            Object[] values = new Object[]{POID};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM PURCHASEINVOICELINES WHERE POID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
            } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void deletePurchaseInvoiceLinesByProduct(String opId, String productId) throws BasicException {
        Object[] values = new Object[]{opId, productId};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "DELETE FROM PURCHASEINVOICELINES WHERE POID= ? AND PRODUCTID = ?", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);
       // new PreparedSentence(s, "DELETE FROM PURCHASEINVOICECHARGES WHERE PIID= ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
    }
        public java.util.List<PurchaseInvoiceProductInfo> getPurchaseInvoiceLines(String id) {
        List<PurchaseInvoiceProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID,"
                + " PLINES.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, PLINES.PRICE AS PRICE, "
                + " PLINES.TAXID AS TAX, PLINES.TAXCATEGORYID AS TAXCATID, PLINES.DISCOUNT, U.NAME FROM PURCHASEINVOICELINES PLINES"
                + " LEFT JOIN PRODUCTS P ON PLINES.PRODUCTID = P.ID LEFT JOIN UOM U ON U.ID = P.UOM WHERE PLINES.POID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
        public java.util.List<PurchaseChargesInfo> getPurchaseChargesLines(String id) {
        List<PurchaseChargesInfo> lines = null;
        String query = "SELECT P.CHARGEID,C.NAME,P.AMOUNT FROM CHARGESMASTER C, PURCHASEINVOICECHARGES P WHERE P.CHARGEID=C.ID AND P.PIID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseChargesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
        public java.util.List<PurchaseChargesInfo> getPurchaseCharges(String id) {
        List<PurchaseChargesInfo> lines = null;
        String query = "SELECT P.CHARGEID,C.NAME,P.AMOUNT FROM CHARGESMASTER C, PURCHASEINVOICECHARGES P WHERE P.CHARGEID=C.ID AND P.PIID= '" + id + "' AND P.AMOUNT!=0";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseChargesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
         public void deletePurchaseInvoiceSingle(String id) {
        try {
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM PURCHASEINVOICE WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void updatePurchaseInvoiceLines(String documentNo, String prodId, double qty, double price, String taxid,double discount) {
        Object[] values = new Object[]{documentNo, prodId, qty, price, taxid,discount};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.DOUBLE};
        String query = "UPDATE PURCHASEINVOICELINES SET  QUANTITY = ?,  PRICE = ?, TAXID=?, DISCOUNT = ? WHERE POID = ? AND PRODUCTID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{2, 3, 4, 5, 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public java.util.List<PurchaseInvoiceProductInfo> getPurchaseInvoiceLinesByPoidAndProduct(String poid, String productId) {
        List<PurchaseInvoiceProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,PLINES.DISCOUNT,'' FROM PURCHASEINVOICELINES PLINES "
                + " WHERE PLINES.POID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

 public java.util.List<PurchaseInvoiceInfo> getPI(String id) {
        List<PurchaseInvoiceInfo> lines = null;
        String query = "SELECT PURCHASEINVOICE.ID, PURCHASEINVOICE.DOCUMENTNUMBER, CUSTOMERS.NAME, PURCHASEINVOICE.CREATED, "
                + " PURCHASEINVOICE.DELIVERY, PURCHASEINVOICE.STATUS,PURCHASEINVOICE.TOTAL, PURCHASEINVOICE.SUBTOTAL, PURCHASEINVOICE.TAX, CUSTOMERS.BILLADDRESS,PURCHASEINVOICE.CHARGESTOTAL,PURCHASEINVOICE.DISCOUNTTOTAL "
                + " FROM PURCHASEINVOICE ,CUSTOMERS WHERE PURCHASEINVOICE.VENDOR = CUSTOMERS.ID "
                + " AND PURCHASEINVOICE.DOCUMENTNUMBER='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
 public java.util.List<PurchaseInvoiceInfo> getPOValue(String id) {
        List<PurchaseInvoiceInfo> lines = null;
        String query = "SELECT PURCHASEORDER.ID, PURCHASEORDER.DOCUMENTNUMBER, CUSTOMERS.NAME, PURCHASEORDER.CREATED, "
                + " PURCHASEORDER.DELIVERY, PURCHASEORDER.STATUS,PURCHASEORDER.TOTAL, PURCHASEORDER.SUBTOTAL, PURCHASEORDER.TAX, CUSTOMERS.BILLADDRESS "
                + " FROM PURCHASEORDER ,CUSTOMERS WHERE PURCHASEORDER.VENDOR = CUSTOMERS.ID "
                + " AND PURCHASEORDER.ID='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
public java.util.List<PurchaseInvoiceInfo> getGRNValue(String id) {
        List<PurchaseInvoiceInfo> lines = null;
        String query = "SELECT GOODSRECEIPTS.ID, GOODSRECEIPTS.DOCUMENTNUMBER, CUSTOMERS.NAME, GOODSRECEIPTS.CREATED, "
                + " GOODSRECEIPTS.DELIVERY, GOODSRECEIPTS.STATUS,GOODSRECEIPTS.TOTAL, GOODSRECEIPTS.SUBTOTAL, GOODSRECEIPTS.TAX, CUSTOMERS.BILLADDRESS,0,0 "
                + " FROM GOODSRECEIPTS,CUSTOMERS WHERE GOODSRECEIPTS.VENDOR = CUSTOMERS.ID "
                + " AND GOODSRECEIPTS.ID='"+id+"'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseInvoiceInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }




void updateInvoiceStatus(String val,String id) {
         Object[] values = new Object[]{val,id};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        String query = "UPDATE GOODSRECEIPTS SET  ISINVOICE = ? WHERE ID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{ 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
 public void deleteMovementlines(String id) {
        try {
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "DELETE FROM GOODSMOVEMENTLINES WHERE RECEIPTID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



public String getWarehouse(String grnId) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT LOCATIONS.NAME FROM GOODSMOVEMENT LEFT JOIN LOCATIONS ON GOODSMOVEMENT.FROMLOCATION=LOCATIONS.ID WHERE GOODSMOVEMENT.ID=? "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(grnId);
            return record == null ? "NONAME" : (String) record[0];

    }



    public void setWarehouse(String id, String warehouse) throws BasicException {

        Object[] values = new Object[]{id,warehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE  GOODSMOVEMENT SET FROMLOCATION = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{ 1, 0})).exec(values);

    }
 public String getChargeName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM CHARGESMASTER WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
 public void deleteChargeLine(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM CHARGESMASTER WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
     public void insertCharges(String id, String name, String account, String isDiscounts) throws BasicException {
        System.out.println("inside insertdiscount method");
        Object[] values = new Object[]{id, name, account, isDiscounts};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO CHARGESMASTER(ID, NAME, ACCOUNT, ISDISCOUNTS ) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);

    }
        public void updateCharges(String id, String name, String account,String isDiscounts) throws BasicException {

        Object[] values = new Object[]{id, name, account, isDiscounts};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE CHARGESMASTER SET ACCOUNT = ?, NAME = ?, ISDISCOUNTS = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{2, 1, 3, 0})).exec(values);

    }
        public List<Chargesinfo> getChargesList() throws BasicException {

        return (List<Chargesinfo>) new StaticSentence(s, "SELECT ID, NAME, ACCOUNT, ISDISCOUNTS FROM CHARGESMASTER ", null, new SerializerReadClass(Chargesinfo.class)).list();
    }
        public int getDocumentNoCount(String id) throws BasicException {

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
      String dateValue= sdf.format(new Date()).toString();

            Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM PURCHASEINVOICECHARGES WHERE PIID = '"+id+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);

    }
        public void insertPurchaseCharges(List<PurchaseChargesInfo> dTable, final String piId) throws BasicException {

       
        if(getDocumentNoCount(piId)==0){

        for (PurchaseChargesInfo den : dTable) {
             final String id = UUID.randomUUID().toString();
            Object[] values = new Object[]{id, piId, den.getChargeId(), den.getName(),den.getAmount()};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING, Datas.DOUBLE};
             new PreparedSentence(s, "INSERT INTO PURCHASEINVOICECHARGES (ID, PIID, CHARGEID,CHARGENAME,AMOUNT) VALUES (?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4})).exec(values);
        }

       }else{
            for (PurchaseChargesInfo den : dTable) {
            Object[] values = new Object[]{piId, den.getChargeId(), den.getAmount()};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE};
            new PreparedSentence(s, "UPDATE PURCHASEINVOICECHARGES SET AMOUNT = ? WHERE CHARGEID = ? AND PIID=? ", new SerializerWriteBasicExt(datas, new int[]{2 ,1, 0})).exec(values);
        }

       }
    }

 public void insertPurchaseInvoice(String id, String documentNo, String vendor, Date dateCreate, Date dateDelivered, String status, Double total, Double subTotal, String tax, String createdby,String poid, double chargesTotal, double discount, Double roundOff) {

       Object[] values = new Object[]{id, documentNo, vendor, dateCreate, dateDelivered, status, total, subTotal, tax, createdby,poid, chargesTotal,discount,roundOff};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE,Datas.DOUBLE,Datas.DOUBLE};
        try {
            new PreparedSentence(s, "INSERT INTO PURCHASEINVOICE (ID, DOCUMENTNUMBER, VENDOR, CREATED, DELIVERY, STATUS, TOTAL, SUBTOTAL, TAX, CREATEDBY,GRNID, CHARGESTOTAL,DISCOUNTTOTAL,ROUNDOFFVALUE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10, 11,12, 13})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

 }

 public int getProductStockCount(String productId) throws BasicException{
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM STOCKCURRENT WHERE PRODUCT = ?"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(productId);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);
 }
  public int getGMProductStockCount(String productId, String location) throws BasicException{
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM STOCKCURRENT WHERE PRODUCT = ?  AND LOCATION='"+location+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(productId);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);
 }
public String getWarehouse() throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT ID FROM LOCATIONS "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = (record[0]==null ? null :record[0].toString());
            return (i == null ? null : i);


    }
public double getUnits(String productId) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT UNITS FROM STOCKCURRENT WHERE PRODUCT=? "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(productId);
            double i = Double.parseDouble(record[0].toString());
            return (i == 0 ? 0 : i);


    }

public double getGMUnits(String productId, String location) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT UNITS FROM STOCKCURRENT WHERE PRODUCT=? AND LOCATION='"+location+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(productId);
            double i = Double.parseDouble(record[0].toString());
            return (i == 0 ? 0 : i);


    }
public PreparedSentence getGMDocumentNo() {

        return new PreparedSentence(s, "SELECT MAX(DOCUMENTNUMBER) FROM GOODSMOVEMENT", null, SerializerReadString.INSTANCE);

    }

public void insertGoodsMovement(String id, String documentNo,Date dateCreate, Double total, Double subTotal, String tax, String createdby,String toWarehouse,String fromWarehouse) {

        Object[] values = new Object[]{id, documentNo, dateCreate,   total, subTotal, tax, createdby,toWarehouse,fromWarehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,  Datas.TIMESTAMP,  Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "INSERT INTO GOODSMOVEMENT (ID, DOCUMENTNUMBER, CREATED,  TOTAL, SUBTOTAL, TAX, CREATEDBY,TOLOCATION,FROMLOCATION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8})).exec(values);
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

public double getMovementUnits(String productId,String warehouse) throws BasicException {
        Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT UNITS FROM STOCKCURRENT WHERE PRODUCT=?  AND LOCATION='"+warehouse+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(productId);
        //  double i = Double.parseDouble(record[0].toString());
        double i=0.0;
//           System.out.println("i record value"+(record=null));
          if(record!=null)
           {
            i = Double.parseDouble(record[0].toString());

             System.out.println("inside if");
           }
           else
           {
            i=0;
           }
            return (i == 0 ? 0 : i);


    }
public void insertGoodsMovementLines(String poId, String productId, double qty, double priceSell, String tax, String taxCatId ,double availableQty) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(), poId, productId, qty, priceSell, tax, taxCatId,availableQty};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING,Datas.DOUBLE};
        new PreparedSentence(s, "INSERT INTO GOODSMOVEMENTLINES (ID, RECEIPTID, PRODUCTID, QUANTITY, PRICE, TAXID, TAXCATEGORYID,availableQty) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7})).exec(values);

    }

 public void updateMovement(String id, String documentNo, Date dateCreate, Double total, Double subtotal, String tax,String toWarehouse,String fromWarehouse) {
        Object[] values = new Object[]{id, documentNo, dateCreate, total, subtotal, tax,toWarehouse,fromWarehouse};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,  Datas.TIMESTAMP, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.STRING,Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE GOODSMOVEMENT SET DOCUMENTNUMBER= ?,  CREATED= ?, TOTAL=?, SUBTOTAL=?, TAX=?, TOLOCATION=?,FROMLOCATION=? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 4, 5, 6, 7,0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
 }

 public void updateProcess(String id) {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE GOODSMOVEMENT SET PROCESSED='1' WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 public java.util.List<GoodsMovementProductInfo> getMovementLines(String id) {
        List<GoodsMovementProductInfo> lines = null;
        String query = "SELECT grn.ID AS ID, grn.RECEIPTID AS GRNID, grn.PRODUCTID AS PRODUCTID,"
                + " grn.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, grn.PRICE AS PRICE, "
                + " grn.TAXID AS TAX, grn.TAXCATEGORYID AS TAXCATID, U.NAME,grn.AVAILABLEQTY FROM GoodsMovementlines grn"
                + " LEFT JOIN PRODUCTS P ON grn.PRODUCTID = P.ID LEFT JOIN UOM U ON P.UOM=U.ID WHERE grn.RECEIPTID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsMovementProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }


public java.util.List<GoodsMovementProductInfo> getMovementLinesByPoidAndProduct(String poid, String productId) {
        List<GoodsMovementProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.RECEIPTID AS GRNID, PLINES.PRODUCTID AS PRODUCTID, PLINES.QUANTITY AS QUANTITY, "
                + "  PLINES.TAXCATEGORYID AS TAXCATID, '', '', PLINES.TAXCATEGORYID,'',PLINES.AVAILABLEQTY FROM GOODSMOVEMENTLINES PLINES "
                + " WHERE PLINES.RECEIPTID= '" + poid + "' AND PLINES.PRODUCTID = '" + productId + "' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(GoodsMovementProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }


public void updateMovementLines(String documentNo, String prodId, double qty, double price, String taxid,double availableQty) {
        Object[] values = new Object[]{documentNo, prodId, qty, price, taxid,availableQty};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING,Datas.DOUBLE};
        String query = "UPDATE GOODSMOVEMENTLINES SET  QUANTITY = ?,  PRICE = ?, TAXID=? ,AVAILABLEQTY=? WHERE RECEIPTID = ? AND PRODUCTID = ? ";
        try {

            new PreparedSentence(s, query, new SerializerWriteBasicExt(datas, new int[]{2, 3, 4, 5, 0, 1})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

public java.util.List<GoodsMovementInfo> getMovement() throws BasicException {

        return (List<GoodsMovementInfo>) new StaticSentence(s, "SELECT ID, DOCUMENTNUMBER,CREATED,TOTAL, SUBTOTAL, TAX,TOLOCATION,FROMLOCATION FROM GOODSMOVEMENT WHERE PROCESSED='0' ORDER BY DOCUMENTNUMBER ", null, new SerializerReadClass(GoodsMovementInfo.class)).list();

    }

    public void updateGoodsReceipts(List<GoodsReceiptsLine> lines1, final String warehouse) throws BasicException {
      
         for (final GoodsReceiptsLine l : lines1) {
             if(getProductStockCount(l.getProductID())==0){
                new PreparedSentence(s
                    , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, warehouse);
                        setString(2, l.getProductID());
                        setString(3, null);
                        setDouble(4, l.getMultiply());

                    }});
             }else{
                 double units = getUnits(l.getProductID())+l.getMultiply();
                    Object[] values = new Object[]{units, l.getProductID()};
                    Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING};
                    try {
                        new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS= ? WHERE PRODUCT= ?", new SerializerWriteBasicExt(datas, new int[]{0 , 1})).exec(values);

                    } catch (BasicException ex) {
                        Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }

             new PreparedSentence(s
                    , "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID,TPNO,TPDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setTimestamp(2, new Date());
                        setInt(3, 1);
                        setString(4, warehouse);
                        setString(5, l.getProductID());
                        setString(6, null);
                        setDouble(7, l.getMultiply());
                         setDouble(8, 0.0);
                         setDouble(9, l.getMultiply());
                         setDouble(10, l.getPrice());
                         setString(11, "");
                         setString(12, "");
                         setTimestamp(13, null);
                    }});
         }
    }

       public void updateSourceGoodsMovement(List<GoodsMovementLine> lines1,final String location) throws BasicException {
       final String wareHouse = getWarehouse();
         for (final GoodsMovementLine l : lines1) {
             if(getGMProductStockCount(l.getProductID(),location)==0){
                new PreparedSentence(s
                    , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, location);
                        setString(2, l.getProductID());
                        setString(3, null);
                        setDouble(4, (-1 * l.getMultiply()));

                    }});
             }else{
                 double units = getGMUnits(l.getProductID(),location)+(-1* l.getMultiply());
                    Object[] values = new Object[]{units, l.getProductID()};
                    Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING};
                    try {
                        new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS= ? WHERE PRODUCT= ?  AND LOCATION = '"+location+"'", new SerializerWriteBasicExt(datas, new int[]{0 , 1})).exec(values);

                    } catch (BasicException ex) {
                        Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }

             new PreparedSentence(s
                    , "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID,TPNO,TPDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setTimestamp(2, new Date());
                        setInt(3, -4);
                        setString(4, location);
                        setString(5, l.getProductID());
                        setString(6, null);
                        setDouble(7, (-1 * l.getMultiply()));
                         setDouble(8, 0.0);
                         setDouble(9, (-1 *l.getMultiply()));
                         setDouble(10, l.getPrice());
                         setString(11, "");
                         setString(12, "");
                         setTimestamp(13, null);
                    }});
         }
    }

       public void updateDestnGoodsMovement(List<GoodsMovementLine> lines1,final String location) throws BasicException {
       final String wareHouse = getWarehouse();
         for (final GoodsMovementLine l : lines1) {
             if(getGMProductStockCount(l.getProductID(),location)==0){
                new PreparedSentence(s
                    , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, location);
                        setString(2, l.getProductID());
                        setString(3, null);
                        setDouble(4, l.getMultiply());

                    }});
             }else{
                 double units = getGMUnits(l.getProductID(),location)+l.getMultiply();
                    Object[] values = new Object[]{units, l.getProductID()};
                    Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING};
                    try {
                        new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS= ? WHERE PRODUCT= ? AND LOCATION = '"+location+"'", new SerializerWriteBasicExt(datas, new int[]{0 , 1})).exec(values);

                    } catch (BasicException ex) {
                        Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }

             new PreparedSentence(s
                    , "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID,TPNO,TPDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setTimestamp(2, new Date());
                        setInt(3, 4);
                        setString(4, location);
                        setString(5, l.getProductID());
                        setString(6, null);
                        setDouble(7, l.getMultiply());
                         setDouble(8, 0.0);
                         setDouble(9, l.getMultiply());
                         setDouble(10, l.getPrice());
                         setString(11, "");
                         setString(12, "");
                         setTimestamp(13, null);
                    }});
         }
    }
public void updateGoodsReturn(List<GoodsReturnLine> lines1, final String warehouse) throws BasicException {
       
         for (final GoodsReturnLine l : lines1) {
             if(getProductStockCount(l.getProductID())==0){
                new PreparedSentence(s
                    , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, warehouse);
                        setString(2, l.getProductID());
                        setString(3, null);
                        setDouble(4, (-1)*l.getMultiply());

                    }});
             }else{
                 double units = getUnits(l.getProductID())-l.getMultiply();
                    Object[] values = new Object[]{units, l.getProductID()};
                    Datas[] datas = new Datas[]{Datas.DOUBLE, Datas.STRING};
                    try {
                        new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS= ? WHERE PRODUCT= ?", new SerializerWriteBasicExt(datas, new int[]{0 , 1})).exec(values);

                    } catch (BasicException ex) {
                        Logger.getLogger(PurchaseOrderReceipts.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }

             new PreparedSentence(s
                    , "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID,TPNO,TPDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setTimestamp(2, new Date());
                        setInt(3, 2);
                        setString(4, warehouse);
                        setString(5, l.getProductID());
                        setString(6, null);
                        setDouble(7, (-1)*l.getMultiply());
                         setDouble(8, 0.0);
                         setDouble(9, (-1)*l.getMultiply());
                         setDouble(10, l.getPrice());
                         setString(11, "");
                         setString(12, "");
                         setTimestamp(13, null);
                    }});
         }
    }

}

