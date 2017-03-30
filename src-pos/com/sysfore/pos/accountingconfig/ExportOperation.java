/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author raghevendra
 */
public class ExportOperation extends BeanFactoryDataSingle {
    protected Session s;
//    
    @Override
    public void init(Session s){
        
        this.s = s;

}

   public ArrayList getAmount(String vocherDate,String incrementDateByOne) throws BasicException
        {
            System.out.println("query is executed");
     String query ="select PAYMENTS.PAYMENT,SUM(TOTAL) from PAYMENTS left join receipts on(PAYMENTS.RECEIPT=receipts.id) "
             + "left join TICKETS TK ON (receipts.id=TK.ID) " 
             + "where  DATENEW >='"+ vocherDate +"'AND DATENEW<'"+ incrementDateByOne +"' and TK.COMPLETED='Y' "
             +"group by payment";
            System.out.println("query is returned");
     return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();

        }
     
    public ArrayList<AccountingPojo> getAccountingValues() throws BasicException
         {
             String query="select ID,SALESTAXCREDIT,SALESCASHDEBIT,SALESCARDDEBIT,SALESCHEQUEDEBIT,PURCHASEDEBIT," +
            "PURCHASECREDIT,JOURNALDEBIT,JOURNALCREDIT,CUSTOMERDEBIT,CUSTOMERCREDIT,VENDORDEBIT,VENDORCREBIT," +
            "PORDUCTDEBIT,PORDUCTCREDIT,SALESDISCOUNTDEBIT,SALESNONTAXCREDIT,SALESSERVICETAXCREDIT,CREDIT,PURCHASENONTAXDEBIT,PURCHASEDISCOUNT,ROUNDOFFVALUE FROM ACCOUNTINGCONFIG";
         return (ArrayList<AccountingPojo>) new StaticSentence(s, query, null, new SerializerReadClass(AccountingPojo.class)).list();
         }


    public Double getNonTaxSum(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    ," select sum(t.units*t.price) from receipts r  left join " +
                      "tickets tk on (r.id=tk.id) left join ticketlines t on (t.ticket=tk.id) "+
                      "left join taxes tx on(t.taxid=tx.id) where tx.rate=0 " +    
                      "and r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' and tk.completed='Y'  and TK.CREDITAMOUNT=0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }
    public Double getTaxSum(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " select sum(t.units*t.price) from receipts r  left join " +
                      "tickets tk on (r.id=tk.id) left join ticketlines t on (t.ticket=tk.id) "+
                      "left join taxes tx on(t.taxid=tx.id) where tx.rate>0 " +    
                      "and r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' and tk.completed='Y'  and TK.CREDITAMOUNT=0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }
            
     public List<ExportValPojo> getPayment(String vocherDate,String inDate) throws BasicException
         {
             String query="SELECT SUM(TOTAL),P.PAYMENT FROM PAYMENTS P "+
                     "LEFT JOIN RECEIPTS R ON (P.RECEIPT=R.ID) LEFT JOIN TICKETS TK ON (R.ID=TK.ID) "+
                     "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' "+
                      " GROUP BY PAYMENT " ;
          
         return (List<ExportValPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportValPojo.class)).list();
         }          

     public Double getTax(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " SELECT SUM(tx.AMOUNT) AS AMOUNT FROM TAXLINES TX LEFT JOIN RECEIPTS R ON (TX.RECEIPT=R.ID) "+
                      "LEFT JOIN TICKETS TK ON (R.ID=TK.ID) " +
                      "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' and TK.CREDITAMOUNT=0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }
      public Double getRoundOffAmt(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " SELECT SUM(ROUNDOFFVALUE) AS AMOUNT FROM RECEIPTS R  "+
                      "LEFT JOIN TICKETS TK ON (R.ID=TK.ID) " +
                      "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' and TK.CREDITAMOUNT=0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }
     public List<ExportPojo> getServiceTax(String vocherDate,String inDate) throws BasicException {

       String query = 
                     " SELECT tx.creditaccount,sum(tk.servicetaxamt) from tickets tk left join taxes tx on tx.id=tk.servicetaxid "+
                      "left join receipts r on r.id=tk.id " +
                      "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' and TK.CREDITAMOUNT=0 and tx.isservicetax='Y'"+
                      " GROUP BY tx.creditaccount ";
             return  (List<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();       
 }
     
     public List<ExportPojo> getServiceCharge(String vocherDate,String inDate) throws BasicException {

       String query = 
                     " SELECT ch.creditaccount,sum(tk.servicechargeamt) from tickets tk left join servicecharge ch on ch.id=tk.servicechargeid "+
                      "left join receipts r on r.id=tk.id " +
                      "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' and TK.CREDITAMOUNT=0 "+
                      " GROUP BY ch.creditaccount ";
             return  (List<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();       
 }
     Double getLineDiscount(String vDate, String vochuerDate) throws BasicException {
      Object[] record = ( Object[]) new StaticSentence(s
              ,"select sum(discounttotal) from purchaseinvoice "+
              "where status='Complete' and "+
              "created>='"+ vDate +"'AND created<'"+ vochuerDate +"' "
               , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
       Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
   }

     public Double getDiscount(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " select sum(t.customerdiscount) as discount from tickets t left join receipts r on (t.id=r.id) where r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND T.COMPLETED='Y' AND t.creditamount=0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }

    Double getCreditAmount(String vocherDate,String inDate) throws BasicException  {
      Object[] record = ( Object[]) new StaticSentence(s
                    , " select sum(t.creditamount) as credit from tickets t left join receipts r on (t.id=r.id) where r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 } 

    
   
   public void updateVoucher(int id,int vch,String voucher) throws BasicException {
        Object[] values = new Object[]{id,vch,voucher};
        Datas[] datas = new Datas[]{Datas.INT, Datas.INT,Datas.STRING};
        new PreparedSentence(s, "UPDATE TALLYEXPORT SET REMOTEID = ?,VCHKEY= ?,VOUCHERKEY=?  ", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);
                
       }
     
             
     public List<TallyPojo> getTallyParameters() throws BasicException
         {
             String query="SELECT REMOTEID,VCHKEY,VOUCHERKEY FROM TALLYEXPORT " ;
          
         return (List<TallyPojo>) new StaticSentence(s, query, null, new SerializerReadClass(TallyPojo.class)).list();
         }    

   public Double getNonTaxCreditSum(String vocherDate,String inDate) throws BasicException {
       Object[] record = ( Object[]) new StaticSentence(s
                    ," select sum(t.units*t.price) from receipts r  left join " +
                      "tickets tk on (r.id=tk.id) left join ticketlines t on (t.ticket=tk.id) "+
                      "left join taxes tx on(t.taxid=tx.id) where tx.rate=0 " +    
                      "and r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' and tk.completed='Y' and tk.creditamount>0"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }
     public Double getTaxCreditSum(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    ," select sum(t.units*t.price) from receipts r  left join " +
                      "tickets tk on (r.id=tk.id) left join ticketlines t on (t.ticket=tk.id) "+
                      "left join taxes tx on(t.taxid=tx.id) where tx.rate>0 " +    
                      "and r.datenew>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' and tk.completed='Y' and tk.creditamount>0"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 } 
      public Double getCreditTax(String vocherDate,String inDate) throws BasicException {

       Object[] record = ( Object[]) new StaticSentence(s
                    , " SELECT SUM(tx.AMOUNT) AS AMOUNT FROM TAXLINES TX LEFT JOIN RECEIPTS R ON (TX.RECEIPT=R.ID) "+
                      "LEFT JOIN TICKETS TK ON (R.ID=TK.ID) " +
                      "WHERE R.DATENEW>='"+ vocherDate +"'AND DATENEW<'"+ inDate +"' AND TK.COMPLETED='Y' and TK.CREDITAMOUNT>0 "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
 }

    List<ExportPojo> getCreditSaleAmount(String vDate, String vochuerDate) throws BasicException {
        
            System.out.println("query is executed");
     String query ="select C.CUSTOMERRECEIVABLE, SUM(TK.CREDITAMOUNT) from receipts left join TICKETS TK ON (receipts.id=TK.ID) "
             + "left join CUSTOMERS C ON (TK.CUSTOMER=C.ID) " 
             + "where  DATENEW >='"+ vDate +"'AND DATENEW<'"+ vochuerDate +"' and TK.COMPLETED='Y' and TK.CREDITAMOUNT>0 "
             +"group by C.CUSTOMERRECEIVABLE";
            System.out.println("query is returned");
     return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();

        } 

    Double getExpenses(String vDate, String inDate) throws BasicException {
      Object[] record = ( Object[]) new StaticSentence(s
                    , " select distinct((select SUM(amount) from pettycash "+
                      "where pettystatus='Issue')-(select SUM(amount) " +
                      "from pettycash where pettystatus='Return')) as expense from pettycash "+
                      "WHERE created>='"+ vDate +"'AND created<'"+ inDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]== null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i); 
    }
     
   public double getExpensesIssue(String vDate, String inDate) throws BasicException {
      Object[] record = ( Object[]) new StaticSentence(s
                    , " select SUM(amount) "+
                      "from pettycash where pettystatus='Issue' "+
                      "AND created>='"+ vDate +"'AND created<'"+ inDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }
   Double getExpensesReturn(String vDate, String inDate) throws BasicException {
      Object[] record = ( Object[]) new StaticSentence(s
                    , " select SUM(amount)  "+
                      "from pettycash where pettystatus='Return' "+
                      "AND created>='"+ vDate +"'AND created<'"+ inDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
           Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }

    Double getNonTaxPurchaseSum(String vDate, String vochuerDate) throws BasicException {
       Object[] record = ( Object[]) new StaticSentence(s
                    , " select SUM(pl.quantity*pl.price) from purchaseinvoice p left join purchaseinvoicelines pl on ( p.id=pl.poid) "+
                      " where p.tax='Rs. 0.00' and p.status='Complete' "+
                      "AND created>='"+ vDate +"'AND created<'"+ vochuerDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }
    
    Double getRoundAmtSum(String vDate, String vochuerDate) throws BasicException {
       Object[] record = ( Object[]) new StaticSentence(s
                    , " select SUM(roundoffvalue) from purchaseinvoice "+
                      " where  status='Complete' "+
                      "AND created>='"+ vDate +"'AND created<'"+ vochuerDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }
    Double getTaxPurchaseSum(String vDate, String vochuerDate) throws BasicException {
       Object[] record = ( Object[]) new StaticSentence(s
                    , " select SUM(pl.quantity*pl.price) from purchaseinvoice p left join purchaseinvoicelines pl on ( p.id=pl.poid) "+
                      " where p.tax!='Rs. 0.00' and p.status='Complete' "+
                      "AND created>='"+ vDate +"'AND created<'"+ vochuerDate +"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            Double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
    }

    List<ExportPojo> getPurchaseDiscount(String vDate, String vochuerDate) throws BasicException {
       String query=" select m.account,sum(pc.amount) from purchaseinvoice p left join purchaseinvoicecharges pc  "+
                      "on (p.id=pc.piid)  left join chargesmaster m on(pc.chargeid=m.id) where m.isdiscounts='Y' "+
                      "AND p.created>='"+ vDate +"'AND p.created<'"+ vochuerDate +"' "
                      +"group by m.account";
                   return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();
        
    }

    List<ExportPojo> getPurchaseAmount(String vDate, String vochuerDate) throws BasicException  {
         String query ="select c.customerreceivable, SUM(p.total) from purchaseinvoice p left join customers c ON (p.vendor=c.id) "
            + "where  p.created >='"+ vDate +"'AND p.created<'"+ vochuerDate +"' and p.status='Complete' "
             +"group by c.customerreceivable";
         return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();
    }

    List<ExportPojo> getPurchaseCharges(String vDate, String vochuerDate) throws BasicException {
         String query ="select m.account,sum(pc.amount) from purchaseinvoice p left join purchaseinvoicecharges pc on (p.id=pc.piid) "
             + "left join chargesmaster m on(pc.chargeid=m.id) " 
             + "where p.created >='"+ vDate +"'AND p.created<'"+ vochuerDate +"' and  m.isdiscounts='N' "
             +"group by pc.chargename";
         return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();
    }
     
     List<ExportPojo> getPurchaseTaxes(String vDate, String vochuerDate) throws BasicException {
         String query ="select tax,total from purchaseinvoice "
            + "where created >='"+ vDate +"'AND created<'"+ vochuerDate +"' and status='Complete' ";
           return (ArrayList<ExportPojo>) new StaticSentence(s, query, null, new SerializerReadClass(ExportPojo.class)).list();
    }
    }
    

    
    

