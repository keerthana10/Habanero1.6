/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.stockreconciliation;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.ticket.CategoryInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderInfo;
import com.sysfore.pos.purchaseorder.PurchaseOrderProductInfo;
import com.sysfore.pos.salescomparison.SalesComparisonInfo;
import com.sysfore.pos.salesdump.BillIdInfo;
import com.sysfore.pos.salesdump.SalesInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mateen
 */
public class DataLogicStockReceipts extends BeanFactoryDataSingle {

    private Session s;

    @Override
    public void init(Session s) {
        this.s = s;
    }

    public java.util.List<StockDetailsInfo> getStockDetails() {

        List<StockDetailsInfo> lines = new ArrayList<StockDetailsInfo>();
        String query = "SELECT STOCKCURRENT.PRODUCT,PRODUCTS.NAME,STOCKCURRENT.UNITS, PRODUCTS.PRICEBUY FROM STOCKCURRENT,PRODUCTS WHERE PRODUCTS.ID=STOCKCURRENT.PRODUCT AND  PRODUCTS.ISACTIVE='Y'";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(StockDetailsInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
    public java.util.List<SalesInfo> getSalesDetails() {

        List<SalesInfo> lines = new ArrayList<SalesInfo>();
        String query = "SELECT T.BILLNO,C.NAME,P.NAME,TL.UNITS,TL.PRICE,(TL.PRICE*TL.UNITS) AS BILLVALUE,T.ID FROM TICKETS T, TICKETLINES TL, PRODUCTS P, CUSTOMER C WHERE T.ID=TL.TICKET AND TL.PRODUCT = P.ID AND T.CUSTOMER = C.ID AND T.COMPLETED='N' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(SalesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
  public java.util.List<StockDetailsInfo> getStockDetails(String wareHouse) {

        List<StockDetailsInfo> lines = new ArrayList<StockDetailsInfo>();
        String query = "SELECT STOCKCURRENT.PRODUCT,PRODUCTS.NAME,STOCKCURRENT.UNITS,PRODUCTS.PRICEBUY FROM STOCKCURRENT,PRODUCTS WHERE PRODUCTS.ID=STOCKCURRENT.PRODUCT AND STOCKCURRENT.LOCATION='"+wareHouse+"' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(StockDetailsInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
  public java.util.List<StockDocumentLinesInfo> getStockLinesInfo(String documentNo) {

        List<StockDocumentLinesInfo> lines = new ArrayList<StockDocumentLinesInfo>();
        String query = "SELECT SL.ITEM,P.NAME,SL.SYSTEMQTY,SL.PHYSICALQTY,SL.VARIANCE,SL.REMARKS,SL.PRICE FROM STOCKRECONCILIATION S,STOCKRECONCILIATIONLINES SL,PRODUCTS P WHERE S.ID = SL.ID AND P.ID = SL.ITEM AND S.DOCUMENTNO ='"+documentNo+"' ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(StockDocumentLinesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
 public java.util.List<StockDetailsInfo> getStockInfo(String value) {

        List<StockDetailsInfo> lines = new ArrayList<StockDetailsInfo>();
        String query = "SELECT STOCKCURRENT.PRODUCT,PRODUCTS.NAME,STOCKCURRENT.UNITS,PRODUCTS.PRICEBUY FROM STOCKCURRENT,PRODUCTS WHERE PRODUCTS.ID=STOCKCURRENT.PRODUCT "+value+" ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(StockDetailsInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
 public java.util.List<BillIdInfo> getBillIdInfo(String value) {

        List<BillIdInfo> lines = new ArrayList<BillIdInfo>();
        String query = "SELECT ID FROM TICKETS WHERE DOCUMENTNO IN ("+value+") ORDER BY DOCUMENTNO ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(BillIdInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
  public final SentenceExec updateTicketCompleted(String billNo) {


         return new PreparedSentence(s, "UPDATE TICKETS SET COMPLETED = 'Y' WHERE DOCUMENTNO='"+billNo+"'", SerializerWriteParams.INSTANCE);

    }
   public java.util.List<SalesComparisonInfo> getSalesComparison(String sourceMonth,String sourceYear, String destinationMonth, String destinationYear) {

        List<SalesComparisonInfo> lines = new ArrayList<SalesComparisonInfo>();

        String query =  "select (select  coalesce(sum(tickets.billamount),0)   from receipts ,tickets "+
                    "where tickets.id=receipts.id and monthname(receipts.datenew) = '"+sourceMonth+"' and YEAR(receipts.datenew) = '"+sourceYear+"')  as sourceamt,"+
                    " (select  coalesce(sum(tickets.billamount),0)   from receipts ,tickets "+
                    "where tickets.id=receipts.id and monthname(receipts.datenew) = '"+destinationMonth+"' and YEAR(receipts.datenew) = '"+destinationYear+"') as destinationamt from dual";


        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(SalesComparisonInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
 public java.util.List<SalesInfo> getSalesDumpDetails(String queryValue) {
  
        List<SalesInfo> lines = new ArrayList<SalesInfo>();

        String query = "SELECT T.DOCUMENTNO,C.NAME,P.NAME,TL.UNITS,(TL.UNITS*TL.PRICE) AS BILLVALUE, T.BILLAMOUNT, R.ID, R.DATENEW FROM RECEIPTS R "+
        "LEFT JOIN  TICKETS T ON R.ID = T.ID "+
        "LEFT JOIN  TICKETLINES TL ON T.ID=TL.TICKET "+
        "LEFT JOIN  PAYMENTS PY ON R.ID = PY.RECEIPT "+
        "LEFT JOIN  CUSTOMERS C ON T.CUSTOMER = C.ID "+
        "LEFT JOIN  PRODUCTS P ON  P.ID = TL.PRODUCT "+
        "WHERE T.COMPLETED='N' AND T.BILLAMOUNT = PY.TOTAL AND "+queryValue+" ORDER BY T.DOCUMENTNO";
      

        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(SalesInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }
       public List<LocationInfo> getLocationsList() throws BasicException {

       String query = "SELECT ID, NAME, ADDRESS FROM LOCATIONS ORDER BY NAME" ;
       return (List<LocationInfo>) new StaticSentence(s, query, null, new SerializerReadClass(LocationInfo.class)).list();

    }
       public List<CategoryDetailsInfo> getCategoryList() throws BasicException {
         List<CategoryDetailsInfo> lines = new ArrayList<CategoryDetailsInfo>();
        String query = "SELECT ID, NAME FROM CATEGORIES ORDER BY NAME ";
        try {
            lines = new StaticSentence(s, query, null, new SerializerReadClass(CategoryDetailsInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
     

    }
 public int getDocumentNo() throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " SELECT MAX(DOCUMENTNO) FROM STOCKRECONCILIATION "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
   public int getDocumentNoCount(String documentno) throws BasicException {

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
      String dateValue= sdf.format(new Date()).toString();

            Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT count(*) FROM STOCKRECONCILIATION WHERE DOCUMENTNO = '"+documentno+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(dateValue);
           int i = Integer.parseInt(record[0].toString());
            return (i == 0 ? 0 : i);

    }

public void insertStockReconciliation(List<StockReconciliationInfo> dTable, final String documentNo,  final Date stockDate) throws BasicException {

        final String id = UUID.randomUUID().toString();
  if(getDocumentNoCount(documentNo)==0){
  new PreparedSentence(s
                    , "INSERT INTO STOCKRECONCILIATION (ID, DOCUMENTNO, DATE) VALUES (?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, id);
                        setString(2, documentNo);
                        setTimestamp(3, stockDate);
                      //  setString(4, category);
                        //setString(5, warehouse);

                    }});
        for (StockReconciliationInfo den : dTable) {
            Object[] values = new Object[]{id, den.getProductId(), den.getSystemQty(), den.getPhysicalQty(),den.getVariance(),den.getPrice(),den.getRemarks()};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE,Datas.DOUBLE, Datas.STRING};
             new PreparedSentence(s, "INSERT INTO STOCKRECONCILIATIONLINES(ID, ITEM, SYSTEMQTY, PHYSICALQTY, VARIANCE, PRICE, REMARKS) VALUES (?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6})).exec(values);
        }

       }else{
            for (StockReconciliationInfo den : dTable) {
            Object[] values = new Object[]{id, den.getProductId(), den.getSystemQty(), den.getPhysicalQty(),den.getVariance(),den.getPrice(),den.getRemarks()};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE,Datas.DOUBLE, Datas.STRING};
            new PreparedSentence(s, "UPDATE STOCKRECONCILIATIONLINES SET SYSTEMQTY = ?,PHYSICALQTY =?,VARIANCE=?,PRICE = ?,REMARKS=? WHERE ITEM = ? ", new SerializerWriteBasicExt(datas, new int[]{2 ,3 ,4, 5, 6, 1})).exec(values);
        }

       }
    }

public void updateStockReconciliation(List<StockReconciliationInfo> dTable, String warehouse) throws BasicException {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd Hh:mm:ss");
    Date currentDate = new Date();
    String sysDate = sdf.format(currentDate);
    
    int reason = 1;
    double pendingQty = 0;
    double price=0;
    String poid="";

        for (StockReconciliationInfo den : dTable) {
            String id = UUID.randomUUID().toString();
            Object[] values = new Object[]{id,currentDate,reason, warehouse,den.getProductId(),den.getPhysicalQty(),pendingQty,price,poid,((-1) * den.getVariance())};
            Datas[] datas = new Datas[]{Datas.STRING, Datas.TIMESTAMP,Datas.INT, Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE};
            new PreparedSentence(s, "UPDATE STOCKCURRENT SET UNITS = ? WHERE PRODUCT = ? ", new SerializerWriteBasicExt(datas, new int[]{5, 4})).exec(values);
           if(den.getVariance()!=0){
                 new PreparedSentence(s, "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, UNITS, PENDINGQUANTITY, RECEIVINGQUANTITY, PRICE, POID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 9, 6, 9, 7, 8})).exec(values);
           //}
           }

        }
       
    }

 

    public List<StockHeaderInfo> getStockHeaderInfo(String documentNo) {
        List<StockHeaderInfo> stockInfoList = null;
        String query = "SELECT STOCKRECONCILIATION.DOCUMENTNO, CATEGORIES.NAME,LOCATIONS.NAME,STOCKRECONCILIATION.DATE FROM STOCKRECONCILIATION,CATEGORIES,LOCATIONS " +
        "WHERE STOCKRECONCILIATION.CATEGORY=CATEGORIES.ID AND STOCKRECONCILIATION.WAREHOUSE=LOCATIONS.ID AND  STOCKRECONCILIATION.DOCUMENTNO='"+documentNo+"' ";
        try {
            stockInfoList = new StaticSentence(s, query, null, new SerializerReadClass(StockHeaderInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stockInfoList;
    }
public String getStockCategory(String documentNo) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT CATEGORY FROM STOCKRECONCILIATION WHERE DOCUMENTNO ='"+documentNo+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = (record[0]==null ? null :record[0].toString());
            return (i == null ? null : i);


    }
public String getLeastBillNo(String documentNo) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT DOCUMENTNO FROM TICKETS WHERE DOCUMENTNO IN ("+documentNo+")  ORDER BY DOCUMENTNO ASC LIMIT 1"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = (record[0]==null ? null :record[0].toString());
            return (i == null ? null : i);


    }
public String getDateNew(String documentNo) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT R.DATENEW FROM RECEIPTS R,TICKETS T WHERE R.ID = T.ID AND T.DOCUMENTNO ='"+documentNo+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = (record[0]==null ? null :record[0].toString());
            return (i == null ? null : i);


    }
public String getWarehouse() throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT ID FROM LOCATIONS "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = (record[0]==null ? null :record[0].toString());
            return (i == null ? null : i);


    }
public double getDiscountTotal(String documentNo) throws BasicException {
          Object[] record = ( Object[]) new StaticSentence(s
                    , "SELECT SUM(CUSTOMERDISCOUNT) FROM TICKETS WHERE DOCUMENTNO IN ("+documentNo+") "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            double i = Double.parseDouble((record[0]==null ? "0" :record[0].toString()));
            return (i == 0.0 ? 0.0 : i);


    }

    public List<StockHeaderInfo> getStockHeaderCategoryInfo(String documentNo) {
        List<StockHeaderInfo> stockInfoList = null;
        String query = "SELECT STOCKRECONCILIATION.DOCUMENTNO, STOCKRECONCILIATION.CATEGORY,STOCKRECONCILIATION.WAREHOUSE,STOCKRECONCILIATION.DATE FROM STOCKRECONCILIATION " +
        "WHERE STOCKRECONCILIATION.DOCUMENTNO='"+documentNo+"' ";
        try {
            stockInfoList = new StaticSentence(s, query, null, new SerializerReadClass(StockHeaderInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stockInfoList;
    }
       public List<StockDocumentInfo> getStockDocumentInfo() {
        List<StockDocumentInfo> stockInfoList = null;
        String query = "SELECT STOCKRECONCILIATION.DOCUMENTNO FROM STOCKRECONCILIATION WHERE STOCKRECONCILIATION.RECONCILE='N' ORDER BY DOCUMENTNO ";

        try {
            stockInfoList = new StaticSentence(s, query, null, new SerializerReadClass(StockDocumentInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicStockReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stockInfoList;
    }
}
