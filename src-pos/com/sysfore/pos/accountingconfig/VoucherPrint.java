
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppConfig;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author raghevendra
 */
public class VoucherPrint {
    
   public static void printJournal(Voucher v,List<ExportPojo> paymentSplits,List<AccountingPojo> accountDetails,Map<String,Double> myMap,Map<String,Double> myMapCharge) {
   BigDecimal grantTotal = new BigDecimal("0.00");
     // TODO Auto-generated method stub
    try {

      
      StringBuilder stringBuilder = new StringBuilder("");
      stringBuilder.append("<ENVELOPE>");
      stringBuilder.append("<HEADER>");
      stringBuilder.append("<TALLYREQUEST>Import Data</TALLYREQUEST>");
      stringBuilder.append("</HEADER>");
      stringBuilder.append("<BODY>");
      stringBuilder.append("<IMPORTDATA>");
      stringBuilder.append("<REQUESTDESC>");
      stringBuilder.append("<REPORTNAME>Vouchers</REPORTNAME>");
      stringBuilder.append("<STATICVARIABLES>");
      stringBuilder.append("<SVCURRENTCOMPANY>Sysfore Technologies</SVCURRENTCOMPANY>");
      stringBuilder.append("</STATICVARIABLES>");
      stringBuilder.append("</REQUESTDESC>");
      stringBuilder.append("<REQUESTDATA>");
      stringBuilder.append("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">");
      stringBuilder.append("<VOUCHER REMOTEID=\"" + v.getVoucherRemoteID() + "\" VCHKEY=\""
          + v.getVchKey()
        + "\" VCHTYPE=\"" + v.getVoucherType() +"\" ACTION=\"Create\" OBJVIEW=\"Accounting Voucher View\">");
      stringBuilder.append("<OLDAUDITENTRYIDS.LIST TYPE=\"Number\">");
      stringBuilder.append("<OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>");
      stringBuilder.append("</OLDAUDITENTRYIDS.LIST>");
      stringBuilder.append("<DATE>" + v.getAccountDate() + "</DATE>");
      stringBuilder.append("<GUID>" + v.getVoucherRemoteID() + "</GUID>");
      stringBuilder.append("<NARRATION>" + v.getNarration() + "</NARRATION>");
      stringBuilder.append("<VOUCHERTYPENAME>" + v.getVoucherType() + "</VOUCHERTYPENAME>");
      stringBuilder.append("<VOUCHERNUMBER>" + v.getVoucherNo() +"</VOUCHERNUMBER>");
      stringBuilder.append(" <PARTYLEDGERNAME>Cash</PARTYLEDGERNAME>");
      stringBuilder.append(" <BASICBASEPARTYNAME>Cash</BASICBASEPARTYNAME>");
      stringBuilder.append("<CSTFORMISSUETYPE/>");
      stringBuilder.append("<CSTFORMRECVTYPE/>");
      stringBuilder.append("<FBTPAYMENTTYPE>Default</FBTPAYMENTTYPE>");
      stringBuilder.append("<PERSISTEDVIEW>Accounting Voucher View</PERSISTEDVIEW>");
      stringBuilder.append("<BASICBUYERNAME>Cash</BASICBUYERNAME>");
      stringBuilder.append("<BASICDATETIMEOFINVOICE>" + v.getEffectiveDate() + "</BASICDATETIMEOFINVOICE>");
      stringBuilder.append("<BASICDATETIMEOFREMOVAL>" + v.getEffectiveDate() + "</BASICDATETIMEOFREMOVAL>");
      stringBuilder.append("<VCHGSTCLASS/>");
      stringBuilder.append("<ENTEREDBY>om</ENTEREDBY>");
      stringBuilder.append("<DIFFACTUALQTY>No</DIFFACTUALQTY>");
      stringBuilder.append("<AUDITED>No</AUDITED>");
      stringBuilder.append("<FORJOBCOSTING>No</FORJOBCOSTING>");
      stringBuilder.append("<ISOPTIONAL>No</ISOPTIONAL>");
      stringBuilder.append("<EFFECTIVEDATE>" + v.getAccountDate() + "</EFFECTIVEDATE>");
      stringBuilder.append("<ISFORJOBWORKIN>No</ISFORJOBWORKIN>");
      stringBuilder.append("<ALLOWCONSUMPTION>No</ALLOWCONSUMPTION>");
      stringBuilder.append("<USEFORINTEREST>No</USEFORINTEREST>");
      stringBuilder.append("<USEFORGAINLOSS>No</USEFORGAINLOSS>");
      stringBuilder.append("<USEFORGODOWNTRANSFER>No</USEFORGODOWNTRANSFER>");
      stringBuilder.append("<USEFORCOMPOUND>No</USEFORCOMPOUND>");
      stringBuilder.append("<ALTERID>" + v.getGuid() + "</ALTERID>");
      stringBuilder.append("<EXCISEOPENING>No</EXCISEOPENING>");
      stringBuilder.append("<USEFORFINALPRODUCTION>No</USEFORFINALPRODUCTION>");
      stringBuilder.append("<ISCANCELLED>No</ISCANCELLED>");
      stringBuilder.append("<HASCASHFLOW>No</HASCASHFLOW>");
      stringBuilder.append("<ISPOSTDATED>No</ISPOSTDATED>");
      stringBuilder.append("<USETRACKINGNUMBER>No</USETRACKINGNUMBER>");
      stringBuilder.append("<ISINVOICE>No</ISINVOICE>");
      stringBuilder.append("<MFGJOURNAL>No</MFGJOURNAL>");
      stringBuilder.append("<HASDISCOUNTS>No</HASDISCOUNTS>");
      stringBuilder.append("<ASPAYSLIP>No</ASPAYSLIP>");
      stringBuilder.append("<ISCOSTCENTRE>No</ISCOSTCENTRE>");
      stringBuilder.append("<ISSTXNONREALIZEDVCH>No</ISSTXNONREALIZEDVCH>");
      stringBuilder.append("<ISEXCISEMANUFACTURERON>No</ISEXCISEMANUFACTURERON>");
      stringBuilder.append("<ISBLANKCHEQUE>No</ISBLANKCHEQUE>");
      stringBuilder.append("<ISDELETED>No</ISDELETED>");
      stringBuilder.append("<ASORIGINAL>No</ASORIGINAL>");
      stringBuilder.append("<VCHISFROMSYNC>No</VCHISFROMSYNC>");
      stringBuilder.append("<MASTERID>" + v.getGuid() + "</MASTERID>");
      stringBuilder.append("<VOUCHERKEY>" + v.getJournalKey() + "</VOUCHERKEY>");
      stringBuilder.append("<OLDAUDITENTRIES.LIST>      </OLDAUDITENTRIES.LIST>");
      stringBuilder.append("<ACCOUNTAUDITENTRIES.LIST>      </ACCOUNTAUDITENTRIES.LIST>");
      stringBuilder.append("<AUDITENTRIES.LIST>      </AUDITENTRIES.LIST>");
      stringBuilder.append("<INVOICEDELNOTES.LIST>      </INVOICEDELNOTES.LIST>");
      stringBuilder.append("<INVOICEORDERLIST.LIST>      </INVOICEORDERLIST.LIST>");
      stringBuilder.append("<INVOICEINDENTLIST.LIST>      </INVOICEINDENTLIST.LIST>");
      stringBuilder.append("<ATTENDANCEENTRIES.LIST>      </ATTENDANCEENTRIES.LIST>");
      // Hello
      //Printing all debit value first
      if(paymentSplits != null) {
         for(ExportPojo exportPojo : paymentSplits) {
             if(exportPojo.getAmt()>0) {
                if(exportPojo.getPaymentName().equalsIgnoreCase("cash")) {
                    stringBuilder.append(printSubAccountDebit(accountDetails.get(0).getCashDebit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }else if(exportPojo.getPaymentName().equalsIgnoreCase("card")) {
                    stringBuilder.append(printSubAccountDebit(accountDetails.get(0).getCardDebit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }else if(exportPojo.getPaymentName().equalsIgnoreCase("cheque")) {
                    stringBuilder.append(printSubAccountDebit(accountDetails.get(0).getChequeDebit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }else if(exportPojo.getPaymentName().equalsIgnoreCase("discount sum")) {
                    stringBuilder.append(printSubAccountDebit(accountDetails.get(0).getDiscountDebit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }
                //else if(exportPojo.getPaymentName().equalsIgnoreCase("credit sum")) {
           //    stringBuilder.append(printSubAccountCredit(accountDetails.get(0).getCredit(), new BigDecimal(exportPojo.getAmt()).setScale(1, BigDecimal.ROUND_HALF_EVEN)));
               // }
             }
         }
      }
      
      //Printing all credit values
      if(paymentSplits != null) {
         for(ExportPojo exportPojo : paymentSplits) {
             if(exportPojo.getAmt()>0) {
                if(exportPojo.getPaymentName().equalsIgnoreCase("nontax sum")) {
                    stringBuilder.append(printSubAccountCredit(accountDetails.get(0).getNonTaxCredit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }else if(exportPojo.getPaymentName().equalsIgnoreCase("tax sum")) {
                    stringBuilder.append(printSubAccountCredit(accountDetails.get(0).getSalesCredit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }else if(exportPojo.getPaymentName().equalsIgnoreCase("total tax")) {
                    stringBuilder.append(printSubAccountCredit(accountDetails.get(0).getServiceTaxCredit(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }
                else if(exportPojo.getPaymentName().equalsIgnoreCase("Round Amount")) {
                    stringBuilder.append(printSubAccountCredit(accountDetails.get(0).getRoundOff(), new BigDecimal(exportPojo.getAmt()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
                }
                
             }
             
         }
      }
       if( myMap != null) {
           for(Map.Entry<String,Double> entry : myMap.entrySet())
           {
              System.out.println("getValue"+entry.getValue());
              System.out.println("getkey"+entry.getKey());
              System.out.println("size"+myMap.size());
             String prTax= accountDetails.get(0).getServiceTaxCredit();
             System.out.println("prtax"+prTax);
              if(!entry.getKey().equals("")&&!entry.getKey().equals("Sales Tax"))
              {
               if(entry.getValue()>0) {
                stringBuilder.append(printSubAccountCredit(entry.getKey(), new BigDecimal(entry.getValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));  
           }
              }
           }
       }
       
       if( myMapCharge != null) {
           for(Map.Entry<String,Double> entry : myMapCharge.entrySet())
           {
              System.out.println("getValue"+entry.getValue());
              System.out.println("getkey"+entry.getKey());
              System.out.println("size"+myMapCharge.size());
               if(entry.getValue()>0) {
                stringBuilder.append(printSubAccountCredit(entry.getKey(), new BigDecimal(entry.getValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN)));  
           }
           }
       }
       
      stringBuilder.append("<ATTDRECORDS.LIST>      </ATTDRECORDS.LIST>");
      stringBuilder.append("</VOUCHER>");
      stringBuilder.append("</TALLYMESSAGE>");
      stringBuilder.append("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">");
      stringBuilder.append("<COMPANY>");
      stringBuilder.append("<REMOTECMPINFO.LIST MERGE=\"Yes\">");
      stringBuilder.append("<NAME>" + v.getRemoteIDwithoutExtention() + "</NAME>");
      stringBuilder.append("<REMOTECMPNAME>Sysfore Technologies</REMOTECMPNAME>");
      stringBuilder.append("<REMOTECMPSTATE>Karnataka</REMOTECMPSTATE>");
      stringBuilder.append("</REMOTECMPINFO.LIST>");
      stringBuilder.append("</COMPANY>");
      stringBuilder.append("</TALLYMESSAGE>");
      stringBuilder.append("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">");
      stringBuilder.append("<COMPANY>");
      stringBuilder.append("<REMOTECMPINFO.LIST MERGE=\"Yes\">");
      stringBuilder.append("<NAME>" + v.getRemoteIDwithoutExtention() + "</NAME>");
      stringBuilder.append("<REMOTECMPNAME>Sysfore Technologies</REMOTECMPNAME>");
      stringBuilder.append("<REMOTECMPSTATE>Karnataka</REMOTECMPSTATE>");
      stringBuilder.append("</REMOTECMPINFO.LIST>");
      stringBuilder.append("</COMPANY>");
      stringBuilder.append("</TALLYMESSAGE>");
      stringBuilder.append("</REQUESTDATA>");
      stringBuilder.append("</IMPORTDATA>");
      stringBuilder.append("</BODY>");
      stringBuilder.append("</ENVELOPE>");
      Source xmlInput = new StreamSource(new StringReader(stringBuilder.toString()));
       Transformer transformer = TransformerFactory.newInstance().newTransformer(); // An identity
     
      Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("ddMMyyyy-hhmmss");
       String path=v.getPath();

        StreamResult result = new StreamResult(new File(path+"Sales"+ft.format(dNow)+".xml"));
      transformer.transform(xmlInput, result);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
 static StringBuilder printSubAccountCredit(String accountName, BigDecimal amount) {
      StringBuilder stringBuilder = new StringBuilder("");
      stringBuilder.append("<ALLLEDGERENTRIES.LIST>");
      stringBuilder.append("<OLDAUDITENTRYIDS.LIST TYPE=\"Number\">");
      stringBuilder.append("<OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>");
      stringBuilder.append("</OLDAUDITENTRYIDS.LIST>");
      stringBuilder.append("<LEDGERNAME>");
      stringBuilder.append(accountName);
      stringBuilder.append("</LEDGERNAME>");
      stringBuilder.append("<GSTCLASS/>");
      stringBuilder.append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>");
      stringBuilder.append("<LEDGERFROMITEM>No</LEDGERFROMITEM>");
      stringBuilder.append("<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>");
      stringBuilder.append("<ISPARTYLEDGER>No</ISPARTYLEDGER>");
      stringBuilder.append("<ISLASTDEEMEDPOSITIVE>No</ISLASTDEEMEDPOSITIVE>");
      stringBuilder.append("<AMOUNT>");
      stringBuilder.append(amount);
      stringBuilder.append("</AMOUNT>");
      stringBuilder.append("<BANKALLOCATIONS.LIST>       </BANKALLOCATIONS.LIST>");
      stringBuilder.append("<BILLALLOCATIONS.LIST>       </BILLALLOCATIONS.LIST>");
      stringBuilder.append("<INTERESTCOLLECTION.LIST>       </INTERESTCOLLECTION.LIST>");
      stringBuilder.append("<OLDAUDITENTRIES.LIST>       </OLDAUDITENTRIES.LIST>");
      stringBuilder.append("<ACCOUNTAUDITENTRIES.LIST>       </ACCOUNTAUDITENTRIES.LIST>");
      stringBuilder.append("<AUDITENTRIES.LIST>       </AUDITENTRIES.LIST>");
      stringBuilder.append("<TAXBILLALLOCATIONS.LIST>       </TAXBILLALLOCATIONS.LIST>");
      stringBuilder.append("<TAXOBJECTALLOCATIONS.LIST>       </TAXOBJECTALLOCATIONS.LIST>");
      stringBuilder.append("<TDSEXPENSEALLOCATIONS.LIST>       </TDSEXPENSEALLOCATIONS.LIST>");
      stringBuilder.append("<VATSTATUTORYDETAILS.LIST>       </VATSTATUTORYDETAILS.LIST>");
      stringBuilder.append("<COSTTRACKALLOCATIONS.LIST>       </COSTTRACKALLOCATIONS.LIST>");
      stringBuilder.append("</ALLLEDGERENTRIES.LIST>");
      return stringBuilder;
 }
  static StringBuilder printSubAccountDebit(String accountName, BigDecimal amount) {
    StringBuilder stringBuilder = new StringBuilder("");
    stringBuilder.append("<ALLLEDGERENTRIES.LIST>");
    stringBuilder.append("<OLDAUDITENTRYIDS.LIST TYPE=\"Number\">");
    stringBuilder.append("<OLDAUDITENTRYIDS>-1</OLDAUDITENTRYIDS>");
    stringBuilder.append("</OLDAUDITENTRYIDS.LIST>");
    stringBuilder.append("<LEDGERNAME>");
    stringBuilder.append(accountName);
    stringBuilder.append("</LEDGERNAME>");
    stringBuilder.append("<GSTCLASS/>");
    stringBuilder.append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>");
    stringBuilder.append("<LEDGERFROMITEM>No</LEDGERFROMITEM>");
    stringBuilder.append("<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>");
    stringBuilder.append("<ISPARTYLEDGER>No</ISPARTYLEDGER>");
    stringBuilder.append("<ISLASTDEEMEDPOSITIVE>Yes</ISLASTDEEMEDPOSITIVE>");
    stringBuilder.append("<AMOUNT>-");
    stringBuilder.append(amount);
    stringBuilder.append("</AMOUNT>");
    stringBuilder.append("<BANKALLOCATIONS.LIST>       </BANKALLOCATIONS.LIST>");
    stringBuilder.append("<BILLALLOCATIONS.LIST>       </BILLALLOCATIONS.LIST>");
    stringBuilder.append("<INTERESTCOLLECTION.LIST>       </INTERESTCOLLECTION.LIST>");
    stringBuilder.append("<OLDAUDITENTRIES.LIST>       </OLDAUDITENTRIES.LIST>");
    stringBuilder.append("<ACCOUNTAUDITENTRIES.LIST>       </ACCOUNTAUDITENTRIES.LIST>");
    stringBuilder.append("<AUDITENTRIES.LIST>       </AUDITENTRIES.LIST>");
    stringBuilder.append("<TAXBILLALLOCATIONS.LIST>       </TAXBILLALLOCATIONS.LIST>");
    stringBuilder.append("<TAXOBJECTALLOCATIONS.LIST>       </TAXOBJECTALLOCATIONS.LIST>");
    stringBuilder.append("<TDSEXPENSEALLOCATIONS.LIST>       </TDSEXPENSEALLOCATIONS.LIST>");
    stringBuilder.append("<VATSTATUTORYDETAILS.LIST>       </VATSTATUTORYDETAILS.LIST>");
    stringBuilder.append("<COSTTRACKALLOCATIONS.LIST>       </COSTTRACKALLOCATIONS.LIST>");
    stringBuilder.append("</ALLLEDGERENTRIES.LIST>");
    return stringBuilder;

  }
  
  

}