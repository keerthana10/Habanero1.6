/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.goodsreceipt;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.sysfore.pos.purchaseorder.PurchaseOrderInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderProductInfo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateen
 */
public class DataLogicGoodsReceipt extends BeanFactoryDataSingle {

    private Session s;

    @Override
    public void init(Session s) {
        this.s = s;
    }

    public List<PurchaseOrderInfo> getCompletedPO() {
        List<PurchaseOrderInfo> pinfoList = null;
        String query = "SELECT P.ID, P.DOCUMENTNUMBER, C.NAME, P.CREATED, P.DELIVERY, P.STATUS, "
                + " P.TOTAL, P.SUBTOTAL, P.TAX , '' FROM PURCHASEORDER AS P, CUSTOMERS AS C"
                + " WHERE STATUS='Complete' AND ISCLOSED='N' AND GOODSRECEIPTSTATUS='N' AND C.ID = P.VENDOR ORDER BY DOCUMENTNUMBER ";
        try {
            pinfoList = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseOrderInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pinfoList;
    }

    public java.util.List<PurchaseOrderProductInfo> getPOLines(String id) {
        List<PurchaseOrderProductInfo> lines = null;
        String query = "SELECT PLINES.ID AS ID, PLINES.POID AS POID, PLINES.PRODUCTID AS PRODUCTID,"
                + " PLINES.QUANTITY AS QUANTITY, P.NAME AS PRODUCTNAME, P.PRICESELL AS PRICE, "
                + " P.TAXCAT AS TAX, '' FROM PURCHASEORDERLINES PLINES"
                + " LEFT JOIN PRODUCTS P ON PLINES.PRODUCTID = P.ID WHERE PLINES.POID= '" + id + "'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(PurchaseOrderProductInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    public List<StockLineInfo> getStockLines(String poid) {
        String query = "SELECT S.ID, S.DATENEW, S.REASON, S.LOCATION, "
                + "S.PRODUCT, S.ATTRIBUTESETINSTANCE_ID, S.UNITS,"
                + " S.PENDINGQUANTITY, S.RECEIVINGQUANTITY, S.PRICE, S.POID, L.NAME"
                + " FROM STOCKDIARY AS S, LOCATIONS AS L WHERE S.POID='" + poid + "' AND L.ID = S.LOCATION";
        List<StockLineInfo> lines = null;
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(StockLineInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
//
//    public void insertGoodReceiptLines(String id, String d, String reason, String location, String product, double aQty, double pQty, double price, String poidCur) {
//        String query = "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID, ACTUALQUANTITY, PENDINGQUANTITY, PRICE, POID) "
//                + " VALUES ('" + id + "', '" + d + "', '" + reason + "', '" + location + "', '" + product + "', NULL , "
//                + "'" + aQty + "', '" + pQty + "', '" + price + "', '" + poidCur + "')";
//        try {
//            new StaticSentence(s, query).exec();
//        } catch (BasicException ex) {
//            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public double getReceivedQuantity(String poid, String product) throws BasicException {
        double val = 0.0;
        if (poid == null) {
            return 0.0;
        } else {
            String query = "SELECT SUM(RECEIVINGQUANTITY) AS COUNT FROM STOCKDIARY WHERE  POID=? AND PRODUCT= '" + product + "'";
            Object[] record = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(poid);
            try {
                val = Double.parseDouble(record[0].toString());
            } catch (NullPointerException e) {
                val = 0;
            }
            return record == null ? 0.0 : val;
        }
    }

    public double getPendingQuantity(String poid, String product) throws BasicException {
        double val = 0.0;
        if (poid == null) {
            return 0.0;
        } else {
            String query = "SELECT SUM(PENDINGQUANTITY) AS COUNT FROM STOCKDIARY WHERE  POID=? AND PRODUCT= '" + product + "'";
            Object[] record = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(poid);
            try {
                val = Double.parseDouble(record[0].toString());
            } catch (NullPointerException e) {
                val = 0;
            }
            return record == null ? 0.0 : val;
        }
    }

    public boolean checkPurchaseOrderInStock(String poid) {

        String query = "SELECT ID, POID FROM STOCKDIARY WHERE POID= ?";
        Object[] obj = new Object[]{};
        try {
            obj = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(poid);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj == null ? true : false;
    }

    public void setPurchaseOrderStatusComplete(String poid) {
        String query = "UPDATE PURCHASEORDER SET GOODSRECEIPTSTATUS='Y' WHERE DOCUMENTNUMBER='" + poid + "'";
        try {
            new StaticSentence(s, query).exec();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean chekMissingProductsInStock(String docNo) {
        Object o = new Object();
        try {
            String query = "SELECT PRODUCTID FROM PURCHASEORDERLINES WHERE PRODUCTID NOT IN("
                    + " SELECT PRODUCT FROM STOCKDIARY WHERE POID='" + docNo + "') AND POID= ? ";
            o = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(docNo);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return o == null ? false : true;
    }

    public String getReceivedQuantityStatus(String poid, String product) {

        String query = "SELECT ISPENDINGQUANTITYMODIFIED AS STATUS FROM PURCHASEORDERLINES WHERE  POID=? AND PRODUCTID= '" + product + "'";
        Object[] record = null;
        try {
            record = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(poid);
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record[0] == null ? "N" : record[0].toString();
    }

    public void changeReceivedStatus(String status, String docno, String productid) {
        try {
            String query = "UPDATE PURCHASEORDERLINES SET ISPENDINGQUANTITYMODIFIED='Y' WHERE POID='" + docno + "' AND PRODUCTID='" + productid + "'";
            new StaticSentence(s, query).exec();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getMaxReceivedQty(String poid, String product) {
        double val = 0.0;
        Object[] record = null;
        try {

            String query = "SELECT SUM(PENDINGQUANTITY) AS COUNT FROM STOCKDIARY WHERE  POID=? AND PRODUCT= '" + product + "'";
            record = (Object[]) new StaticSentence(s, query, SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(poid);
            try {
                val = Double.parseDouble(record[0].toString());
            } catch (NullPointerException e) {
                val = 0;
            }

        } catch (BasicException ex) {
            Logger.getLogger(DataLogicGoodsReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record == null ? 0.0 : val;
    }
}
