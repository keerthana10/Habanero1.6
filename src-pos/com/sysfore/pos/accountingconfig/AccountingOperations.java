/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import java.util.ArrayList;

/**
 *
 * @author raghevendra
 */
public class AccountingOperations extends BeanFactoryDataSingle {
    
    protected Session s;
    private TableDefinition tcustomers;
    private static Datas[] customerdatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.BOOLEAN, Datas.STRING};
    
    @Override
    public void init(Session s){
        
        this.s = s;
        tcustomers = new TableDefinition(s
            , "CUSTOMERS"
            , new String[] { "ID", "TAXID", "SEARCHKEY", "NAME", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"
                           , "FIRSTNAME", "LASTNAME", "EMAIL", "PHONE", "PHONE2", "FAX"
                           , "ADDRESS", "ADDRESS2", "ISADDRESS1BILLADDRESS", "ISADDRESS2SHIFTADDRESS", "POSTAL", "CITY", "REGION", "COUNTRY"
                           , "TAXCATEGORY","ISCUSTOMER", "BILLADDRESS", "SHIFTADDRESS","CUSTOMERID" }
            , new String[] { "ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.searchkey"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")
                           , AppLocal.getIntString("label.firstname"), AppLocal.getIntString("label.lastname"), AppLocal.getIntString("label.email"), AppLocal.getIntString("label.phone"), AppLocal.getIntString("label.phone2"), AppLocal.getIntString("label.fax")
                           , AppLocal.getIntString("label.address"), AppLocal.getIntString("label.address2"), "ISBILLADDRESS", "ISSHIPADDRESS",AppLocal.getIntString("label.postal"), AppLocal.getIntString("label.city"), AppLocal.getIntString("label.region"), AppLocal.getIntString("label.country")
                           , "TAXCATEGORY",AppLocal.getIntString("label.iscustomer"), "BILLADDRESS", "SHIFTADDRESS","CUSTOMERID"}
            , new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING,Datas.STRING}
            , new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.BOOLEAN, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.STRING,Formats.STRING}
            , new int[] {0}
        );   
        
    
}
      public void insertAccountingValues(final AccountingPojo pojo) throws BasicException{
                  new PreparedSentence(s,
                "INSERT INTO ACCOUNTINGCONFIG(ID, SALESTAXCREDIT,"                       
                +"SALESCASHDEBIT,SALESCARDDEBIT,SALESCHEQUEDEBIT,PURCHASEDEBIT," 
                +"PURCHASECREDIT,JOURNALDEBIT,JOURNALCREDIT,"
                +"CUSTOMERDEBIT,CUSTOMERCREDIT,VENDORDEBIT,VENDORCREBIT,PORDUCTDEBIT,PORDUCTCREDIT,SALESDISCOUNTDEBIT,SALESNONTAXCREDIT,SALESSERVICETAXCREDIT,CREDIT,PURCHASENONTAXDEBIT,PURCHASEDISCOUNT,ROUNDOFFVALUE)"
                +"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", SerializerWriteParams.INSTANCE).exec(new DataParams() {
            @Override
            public void writeValues() throws BasicException {

                setString(1,pojo.getId());
                setString(2,pojo.getSalesCredit());
                setString(3,pojo.getCashDebit());
                setString(4,pojo.getCardDebit());
                setString(5,pojo.getChequeDebit());
                setString(6,pojo.getPurDebit());
                setString(7,pojo.getPurCredit());
                setString(8,pojo.getJourDebit());
                setString(9,pojo.getJourCrebit());
                setString(10,pojo.getVenDebit());
                setString(11,pojo.getVenCrebit());
                setString(12,pojo.getCusDebit());
                setString(13,pojo.getCusCrebit());
                setString(14,pojo.getProDebit());
                setString(15,pojo.getProCrebit());
                setString(16,pojo.getDiscountDebit());
                setString(17,pojo.getNonTaxCredit());
                setString(18,pojo.getServiceTaxCredit());
                setString(19,pojo.getCredit());
                setString(20,pojo.getPurchaseNonTax());
                setString(21,pojo.getPurchaseDiscount());
                setString(22,pojo.getRoundOff());
                
}
      });
         }
               public void updateAccounting(final AccountingPojo pojo) throws BasicException{
                  new PreparedSentence(s,
                "update ACCOUNTINGCONFIG SET ID=?,SALESTAXCREDIT=?,SALESCASHDEBIT=?,SALESCARDDEBIT=?,SALESCHEQUEDEBIT=?,PURCHASEDEBIT=?,"
                +"PURCHASECREDIT=?,JOURNALDEBIT=?,JOURNALCREDIT=?,CUSTOMERDEBIT=?,CUSTOMERCREDIT=?,VENDORDEBIT=?,VENDORCREBIT=?,"
                +"PORDUCTDEBIT=?,PORDUCTCREDIT=?, SALESDISCOUNTDEBIT=?, SALESNONTAXCREDIT=?, SALESSERVICETAXCREDIT=?, CREDIT=?, PURCHASENONTAXDEBIT=?,PURCHASEDISCOUNT=?,ROUNDOFFVALUE=?", SerializerWriteParams.INSTANCE).exec(new DataParams() {
            @Override
            public void writeValues() throws BasicException {

                setString(1,pojo.getId());
                setString(2,pojo.getSalesCredit());
                setString(3,pojo.getCashDebit());
                setString(4,pojo.getCardDebit());
                setString(5,pojo.getChequeDebit());
                setString(6,pojo.getPurDebit());
                setString(7,pojo.getPurCredit());
                setString(8,pojo.getJourDebit());
                setString(9,pojo.getJourCrebit());
                setString(10,pojo.getVenDebit());
                setString(11,pojo.getVenCrebit());
                setString(12,pojo.getCusDebit());
                setString(13,pojo.getCusCrebit());
                setString(14,pojo.getProDebit());
                setString(15,pojo.getProCrebit());
                setString(16,pojo.getDiscountDebit());
                setString(17,pojo.getNonTaxCredit());
                setString(18,pojo.getServiceTaxCredit());
                setString(19,pojo.getCredit());
                setString(20,pojo.getPurchaseNonTax());
                setString(21,pojo.getPurchaseDiscount());
                setString(22,pojo.getRoundOff());
}
      });
         }
               
  public ArrayList<AccountingPojo> getAccountingValues() throws BasicException
         {
             String query="select ID,SALESTAXCREDIT,SALESCASHDEBIT,SALESCARDDEBIT,SALESCHEQUEDEBIT,PURCHASEDEBIT," +
            "PURCHASECREDIT,JOURNALDEBIT,JOURNALCREDIT,CUSTOMERDEBIT,CUSTOMERCREDIT,VENDORDEBIT,VENDORCREBIT," +
            "PORDUCTDEBIT,PORDUCTCREDIT,SALESDISCOUNTDEBIT,SALESNONTAXCREDIT,SALESSERVICETAXCREDIT,CREDIT,PURCHASENONTAXDEBIT,PURCHASEDISCOUNT,ROUNDOFFVALUE FROM ACCOUNTINGCONFIG";
         return (ArrayList<AccountingPojo>) new StaticSentence(s, query, null, new SerializerReadClass(AccountingPojo.class)).list();
         }
     


    }
    
    
