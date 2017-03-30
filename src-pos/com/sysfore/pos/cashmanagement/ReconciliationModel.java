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

package com.sysfore.pos.cashmanagement;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 *
 * @author adrianromero
 */
public class ReconciliationModel {

    private String m_sHost;
    private static String m_sPosNo;
    private int m_iSeq;
    private Date m_dDateStart;
     private String m_dCreated;
    private Date m_dDateEnd;       
            
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private java.util.List<PaymentsLine> m_lpayments;
    
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Double m_dSalesTaxes;
    private Double m_dFloatAmt;
    private static String m_dMoneyvalue;
    private Double m_dCashAmt;
    private Double m_dAmt;
    private Double m_dCreditAmt;
    private java.util.List<SalesLine> m_lsales;
    private Double m_iDiscountSales;
    private Double m_iBillDiscountSales;
      private Double m_iCustomerDiscountSales;
    private Double m_LeastValueDiscount;
    private Integer m_itotalitems;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    private Date todayDate;
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};
    String PosNo;

    
   
    AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));

    private ReconciliationModel() {
     aconfig.load();


   
    
    }    
    
    public static ReconciliationModel emptyInstance() {
        
        ReconciliationModel p = new ReconciliationModel();
        
        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_lpayments = new ArrayList<PaymentsLine>();
        
        p.m_iSales = null;
        p.m_dSalesBase = null;
        p.m_dSalesTaxes = null;
        p.m_dFloatAmt = 0.0;
        p.m_dCashAmt = null;
        p.m_dCreditAmt = null;
        p.m_dAmt = null;
        p.m_dMoneyvalue = null;
        p.m_iBillDiscountSales = 0.0;
        p.m_iCustomerDiscountSales = 0.0;
        p.m_LeastValueDiscount =0.0;
        p.m_iDiscountSales = 0.0;

        p.m_lsales = new ArrayList<SalesLine>();

        return p;
    }
    
    public static ReconciliationModel loadInstance(AppView app) throws BasicException {
        
        ReconciliationModel p = new ReconciliationModel();
       Calendar cal = Calendar.getInstance();
        // Propiedades globales

        p.m_sHost = app.getProperties().getHost();
         p.m_iSeq = app.getActiveDaySequence();
        p.m_dDateStart = app.getActiveDayDateStart();
        p.m_sPosNo = app.getProperties().getPosNo();

  
        p.m_dDateEnd = null;

       
        // Pagos
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
            
        if (valtickets == null) {
            p.m_iPayments = new Integer(0);
            p.m_dPaymentsTotal = new Double(0.0);
        } else {
            p.m_iPayments = (Integer) valtickets[0];
            p.m_dPaymentsTotal = (Double) valtickets[1];
        }

        Object[] moneyValue = (Object []) new StaticSentence(app.getSession()
            , "SELECT MONEY " +
              "FROM CLOSEDDAY " +
              "WHERE DATEEND!='null' AND POSNO = ? ORDER BY DATEEND DESC LIMIT 1"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.STRING}))
            .find(m_sPosNo);

        if (moneyValue == null) {
            p.m_dMoneyvalue = null;
          
        } else {
            p.m_dMoneyvalue =  (String) moneyValue[0];
           
        }
 
        Object[] floatAmt = (Object []) new StaticSentence(app.getSession()
            , "SELECT FLOATCASH.FLOATCASH " +
              "FROM FLOATCASH " +
              "WHERE FLOATCASH.POSNO = '"+m_sPosNo+"' AND CREATED = ? "
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(now());

        if (floatAmt == null) {
          
            p.m_dFloatAmt = new Double(0.0);
        } else {
           
            p.m_dFloatAmt = (Double) floatAmt[0];

        }
      
          Object[] cashAmt = (Object []) new StaticSentence(app.getSession()
            , "SELECT sum(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT = 'CASH' AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"' "
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());

        if (cashAmt == null) {

            p.m_dCashAmt = new Double(0.0);
        } else {
            p.m_dCashAmt = (Double) cashAmt[0];

        }

          Object[] totatAmt = (Object []) new StaticSentence(app.getSession()
            , "SELECT SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"' "
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());

        if (totatAmt == null) {

            p.m_dAmt = new Double(0.0);
        } else {
            p.m_dAmt = (Double) totatAmt[0];

        }     
          Object[] creditAmt = (Object []) new StaticSentence(app.getSession()
            , "SELECT SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"' AND RECEIPTS.ISCREDITSALE='Y'"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());

        if (creditAmt == null) {

            p.m_dCreditAmt = new Double(0.0);
        } else {
            p.m_dCreditAmt = (Double) creditAmt[0];

        }

        List l = new StaticSentence(app.getSession()            
            , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL),COUNT(*) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEYDAY = ?  AND PAYMENTS.PAYMENT != 'CASHIN' AND RECEIPTS.POSNO = '"+m_sPosNo+"'" +
              "GROUP BY PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ReconciliationModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveDayIndex());
        
        if (l == null) {
            p.m_lpayments = new ArrayList();
        } else {
            p.m_lpayments = l;
        }


        
        // Sales
        Object[] recsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT COUNT(DISTINCT RECEIPTS.ID),SUM(TICKETLINES.UNITS), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) " +
            "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.INT, Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recsales == null) {
            p.m_iSales = null;
            p.m_itotalitems = null;
            p.m_dSalesBase = null;
        } else {
            p.m_iSales = (Integer) recsales[0];
            p.m_itotalitems=(Integer) recsales[1];
            p.m_dSalesBase = (Double) recsales[2];
        }

            Object[] recDiscountsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TICKETLINES.DISCOUNT) " +
            "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recDiscountsales == null) {
            p.m_iDiscountSales = null;

        } else {
            p.m_iDiscountSales = (Double) recDiscountsales[0];

        }

            Object[] recBillDiscountsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TICKETS.BILLDISCOUNT) " +
            "FROM RECEIPTS, TICKETS WHERE RECEIPTS.ID = TICKETS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recBillDiscountsales == null) {
            p.m_iBillDiscountSales = null;

        } else {
            p.m_iBillDiscountSales = (Double) recBillDiscountsales[0];

        }
               Object[] recCustomerDiscountsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TICKETS.CUSTOMERDISCOUNT) " +
            "FROM RECEIPTS, TICKETS WHERE RECEIPTS.ID = TICKETS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recCustomerDiscountsales == null) {
            p.m_iCustomerDiscountSales = null;

        } else {
            p.m_iCustomerDiscountSales = (Double) recCustomerDiscountsales[0];

        }
           Object[] recLeastValue = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TICKETS.LEASTVALUEDISCOUNT) " +
            "FROM RECEIPTS, TICKETS WHERE RECEIPTS.ID = TICKETS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recLeastValue == null) {
            p.m_LeastValueDiscount = null;

        } else {
            p.m_LeastValueDiscount = (Double) recLeastValue[0];

        }
        
        // Taxes
        Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TAXLINES.AMOUNT) " +
            "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (rectaxes == null) {
            p.m_dSalesTaxes = null;
        } else {
            p.m_dSalesTaxes = (Double) rectaxes[0];
        } 
                
        List<SalesLine> asales = new StaticSentence(app.getSession(),
                "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT),TAXES.RATE " +
                "FROM RECEIPTS,TICKETS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
                "AND TICKETS.ID=RECEIPTS.ID AND RECEIPTS.MONEYDAY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'" +
                "GROUP BY TAXCATEGORIES.NAME"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(ReconciliationModel.SalesLine.class))
                .list(app.getActiveDayIndex());
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }
         
        return p;
    }

    public int getPayments() {
        return m_iPayments.intValue();
    }
   public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());

  }
    public double getCashAmt(){
        return m_dCashAmt;
    }
    public double getTotal() {
        return m_dPaymentsTotal.doubleValue();
    }
    public String getHost() {
        return m_sHost;
    }
    public int getSequence() {
        return m_iSeq;
    }
    public Date getDateStart() {
        return m_dDateStart;
    }
    public void setDateEnd(Date dValue) {
        m_dDateEnd = dValue;
    }
    public Date getDateEnd() {
        return m_dDateEnd;
    }
    public String getDateForPrint() {
       java.util.Date m_dDate = new Date();
      // m_dDate =
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      return sdf.format(m_dDate).toString();
    }
    public String printDateNow() {
        return getDateForPrint();
    }
    public String printHost() {
        return m_sHost;
    }
    public String printSequence() {
        return Formats.INT.formatValue(m_iSeq);
    }
    public String printDateStart() {
        return Formats.TIMESTAMP.formatValue(m_dDateStart);
    }
    public String printDateEnd() {
        return Formats.TIMESTAMP.formatValue(m_dDateEnd);
    }  
    
    public String printPayments() {
        return Formats.INT.formatValue(m_iPayments);
    }

    public String printCashAmt() {
        return Formats.CURRENCY.formatValue(m_dCashAmt);
    }
    public String printAmt() {
        return Formats.CURRENCY.formatValue(m_dAmt);
    }
     public String printCreditAmt() {
        return Formats.CURRENCY.formatValue(m_dCreditAmt);
    }

    public String printPaymentsTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }
    public String printTotalCollection() {
        return Formats.CURRENCY.formatValue(m_dSalesBase + m_dSalesTaxes+m_dFloatAmt-(m_iBillDiscountSales+m_iDiscountSales+m_iCustomerDiscountSales));
    }
    public String printTotal() {
        return Formats.CURRENCY.formatValue(m_dSalesBase + m_dSalesTaxes+m_LeastValueDiscount-(m_iBillDiscountSales+m_iDiscountSales+m_iCustomerDiscountSales));
    }
      public String printFloatAmt() {
        return Formats.CURRENCY.formatValue(m_dFloatAmt);
    }
       public String printDiscountAmt() {
        return Formats.CURRENCY.formatValue(m_iBillDiscountSales+m_iDiscountSales+m_LeastValueDiscount+m_iCustomerDiscountSales);
    }
public String printPosNo() {

    return aconfig.getProperty("machine.PosNo");
    }
    public String printFloatAmount() {
     // AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));

    return aconfig.getProperty("machine.FloatAmt");
    }
    
    public List<PaymentsLine> getPaymentLines() {
        return m_lpayments;
    }
      public String getTotalItemsSold() {
      //  return Formats.INT.formatValue((m_itotalitems==null?0:m_itotalitems)+(m_dSalesPriceAdjustmentscount==null?0:m_dSalesPriceAdjustmentscount));
          return Formats.INT.formatValue((m_itotalitems==null?0:m_itotalitems));
    }
    public int getSales() {
        return m_iSales == null ? 0 : m_iSales.intValue();
    }    
    public String printSales() {
        return Formats.INT.formatValue(m_iSales);
    }
    public String printSalesBase() {
        return Formats.CURRENCY.formatValue(m_dSalesBase);
    }     
    public String printSalesTaxes() {
        return Formats.CURRENCY.formatValue(m_dSalesTaxes);
    }     
    public String printSalesTotal() {            
        return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
                ? null
                : m_dSalesBase + m_dSalesTaxes+m_LeastValueDiscount);
    }     
    public List<SalesLine> getSaleLines() {
        return m_lsales;
    }
    
    public AbstractTableModel getPaymentsModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(PAYMENTHEADERS[column]);
            }
            public int getRowCount() {
                return m_lpayments.size();
            }
            public int getColumnCount() {
                return PAYMENTHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                PaymentsLine l = m_lpayments.get(row);
                switch (column) {
                case 0: return l.getType();
                case 1: return l.getValue();
                default: return null;
                }
            }  
        };
    }
    public String getTotalPaymentLine()
        {

         double dTotal = 0.0;
        Iterator i = m_lpayments.iterator();
        while (i.hasNext()) {
         PaymentsLine p = (PaymentsLine) i.next();
            dTotal += p.getValue();
        }

        return Formats.CURRENCY.formatValue(dTotal);
        }

      public String getTotalSaleLines()
        {



         double dTotal = 0.0;
        Iterator i = m_lsales.iterator();
        while (i.hasNext()) {
         SalesLine p = (SalesLine) i.next();
            dTotal += p.getTaxes();
        }

        return Formats.CURRENCY.formatValue(dTotal);
        }

    
    public static class SalesLine implements SerializableRead {
        
        private String m_SalesTaxName;
        private Double m_SalesTaxes;
        private Double m_SalesTaxesRate;
        
        public void readValues(DataRead dr) throws BasicException {
            m_SalesTaxName = dr.getString(1);
            m_SalesTaxes = dr.getDouble(2);
             m_SalesTaxesRate = dr.getDouble(3);
        }
        public String printTaxName() {
            return m_SalesTaxName;
        }      
        public String printTaxes() {
            return Formats.CURRENCY.formatValue(m_SalesTaxes);
        }
        public String getTaxName() {
            return m_SalesTaxName;
        }
        public Double getTaxes() {
            return m_SalesTaxes;
        }
        public String getSalesTaxesRate() {
            return Formats.PERCENT.formatValue(m_SalesTaxesRate) ;
        }
    }

    public AbstractTableModel getSalesModel() {
        return new AbstractTableModel() {
            public String getColumnName(int column) {
                return AppLocal.getIntString(SALEHEADERS[column]);
            }
            public int getRowCount() {
                return m_lsales.size();
            }
            public int getColumnCount() {
                return SALEHEADERS.length;
            }
            public Object getValueAt(int row, int column) {
                SalesLine l = m_lsales.get(row);
                switch (column) {
                case 0: return l.getTaxName();
                case 1: return l.getTaxes();
                default: return null;
                }
            }  
        };
    }
    
    public static class PaymentsLine implements SerializableRead {
        
        private String m_PaymentType;
        private Double m_PaymentValue;
        private Integer m_Paymentcount;
        
        public void readValues(DataRead dr) throws BasicException {
            m_PaymentType = dr.getString(1);
            m_PaymentValue = dr.getDouble(2);
             m_Paymentcount = dr.getInt(3);
        }
        
        public String printType() {
            return m_PaymentType;
        }
        public String getType() {
            return m_PaymentType;
        }
        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
        }
        public Double getValue() {
            return m_PaymentValue;
        }
        public String getPaymentCount() {
            return Formats.INT.formatValue(m_Paymentcount);
        }
    }
}    