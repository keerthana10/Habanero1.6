/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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


//Rav Thota

package com.openbravo.pos.panels;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author adrianromero
 */
public class PaymentDayModel {

    private String m_sHost;
    private int m_iSeq;
    private Date m_dDateStart;
    private Date m_dDateEnd;
    private Date m_dDateDayfirst;

    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private java.util.List<PaymentsLine> m_lpayments;

    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};

    private Integer m_iSales;
    private Integer m_itotalitems;
    private Double m_dSalesBase;
    private Integer m_iSalesCanceled;
    private Double m_dSalesAmtCanceled;
    private Double m_dSalesAdjustments;
    private Double m_dSalesAmtCanceledAdjust;
    private Integer m_dSalesPriceAdjustmentscount;
    private Double m_dSalesTaxes;
    private java.util.List<SalesLine> m_lsales;

    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};

    private PaymentDayModel() {
    }

    public static PaymentDayModel emptyInstance() {

        PaymentDayModel p = new PaymentDayModel();

        p.m_iPayments = new Integer(0);
        p.m_dPaymentsTotal = new Double(0.0);
        p.m_lpayments = new ArrayList<PaymentsLine>();

        p.m_iSales = null;
        p.m_dSalesAdjustments=null;
        p.m_iSalesCanceled=null;
        p.m_dSalesAmtCanceled=null;
        p.m_dSalesBase = null;
        p.m_itotalitems=null;
        p.m_dSalesTaxes = null;
        p.m_dSalesAmtCanceledAdjust=null;
        p.m_lsales = new ArrayList<SalesLine>();
        p.m_dSalesPriceAdjustmentscount=new Integer(0);

        return p;
    }

    public static PaymentDayModel loadInstance(AppView app) throws BasicException {

        PaymentDayModel p = new PaymentDayModel();

        // Propiedades globales
        p.m_sHost = app.getProperties().getHost();
        p.m_iSeq = app.getActiveDaySequence();
        p.m_dDateStart = app.getActiveDayDateStart();
        p.m_dDateEnd = null;



        Object[] dayfirst =  (Object[]) new StaticSentence(app.getSession(),
        "SELECT MIN(STARTTIME) FROM PEOPLELOG WHERE TO_DAYS(STARTTIME)=TO_DAYS(NOW()) AND REASON='Login' ",
       null,
      new SerializerReadBasic(new Datas[] {Datas.TIMESTAMP}))
      .find(app.getActiveCashIndex());


    if (dayfirst == null) {
    p.m_dDateDayfirst=null;

    } else {
    p.m_dDateDayfirst= (Date) dayfirst[0];

    }
        // Pagos
        Object[] valtickets = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS ,TICKETS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND TICKETS.ID=RECEIPTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ?"
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

        
        Object[] priceadj = (Object []) new StaticSentence(app.getSession()
        ,"SELECT SUM(TICKETLINES.UNITQTY * TICKETLINES.PRICE),SUM(TICKETLINES.UNITS) "+
        "FROM TICKETS,RECEIPTS,TICKETLINES,PRODUCTS "+
        "WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND TICKETLINES.PRODUCT=PRODUCTS.ID AND PRODUCTS.PRODUCTTYPE='Y' AND PRODUCTS.NAME LIKE '%Price Adjustments%' AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] { Datas.DOUBLE,Datas.INT}))
            .find(app.getActiveDayIndex());

        if (priceadj == null) {
            p.m_dSalesAdjustments = new Double(0);
            p.m_dSalesPriceAdjustmentscount= new Integer(0);
        } else {
            
            p.m_dSalesAdjustments = (Double) priceadj[0];
            p.m_dSalesPriceAdjustmentscount= (Integer)priceadj[1];;
        }


        Object[] cancelbill = (Object []) new StaticSentence(app.getSession()
            , "SELECT COUNT(DISTINCT RECEIPTS.ID),SUM((TICKETLINES.UNITQTY * TICKETLINES.PRICE)+(TICKETLINES.LOTQTY * TICKETLINES.LOTUNITPRICE)+(TICKETLINES.BOXQTY * TICKETLINES.BOXUNITPRICE)) " +
              "FROM TICKETS, RECEIPTS,TICKETLINES,PRODUCTS " +
              "WHERE TICKETS.ID=RECEIPTS.ID AND TICKETS.ID=TICKETLINES.TICKET  AND TICKETS.ISCANCELLED='Y' AND TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEYDAY = ? "
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveDayIndex());

        if (cancelbill == null) {
            p.m_iSalesCanceled = new Integer(0);
            p.m_dSalesAmtCanceled=new Double(0);

        } else {

            p.m_iSalesCanceled  = (Integer) cancelbill[0];
            p.m_dSalesAmtCanceled=(Double) cancelbill[1];
        }



       

        // Sales
        Object[] recsales = (Object []) new StaticSentence(app.getSession(),
            "SELECT COUNT(DISTINCT RECEIPTS.ID),SUM(TICKETLINES.UNITQTY+TICKETLINES.LOTQTY +TICKETLINES.BOXQTY),SUM((TICKETLINES.UNITQTY * TICKETLINES.PRICE)+(TICKETLINES.LOTQTY * TICKETLINES.LOTUNITPRICE)+(TICKETLINES.BOXQTY * TICKETLINES.BOXUNITPRICE)) "+
           "FROM RECEIPTS,TICKETS,TICKETLINES,PRODUCTS WHERE RECEIPTS.ID=TICKETS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND  TICKETLINES.PRODUCT=PRODUCTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ?",
            SerializerWriteString.INSTANCE,
            new SerializerReadBasic(new Datas[] {Datas.INT,Datas.INT, Datas.DOUBLE}))
            .find(app.getActiveDayIndex());
        if (recsales == null) {
            p.m_iSales = null;
            p.m_itotalitems=null;
            p.m_dSalesBase = null;
        } else {
            p.m_iSales = (Integer) recsales[0];
            p.m_itotalitems=(Integer) recsales[1];
            p.m_dSalesBase = (Double) recsales[2];
        }

        // Taxes
        Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
            "SELECT SUM(TAXLINES.AMOUNT) " +
            "FROM RECEIPTS,TICKETS,TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TICKETS.ID=RECEIPTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ?"
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
                "AND TICKETS.ID=RECEIPTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ?" +
                "GROUP BY TAXCATEGORIES.NAME"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentDayModel.SalesLine.class))
                .list(app.getActiveDayIndex());
        if (asales == null) {
            p.m_lsales = new ArrayList<SalesLine>();
        } else {
            p.m_lsales = asales;
        }



        
         List l = new StaticSentence(app.getSession()
            , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL),COUNT(*) " +
              "FROM PAYMENTS, RECEIPTS ,TICKETS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND TICKETS.ID=RECEIPTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEYDAY = ? " +
              "GROUP BY PAYMENTS.PAYMENT"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(PaymentDayModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
            .list(app.getActiveDayIndex());

        if (l == null) {
            p.m_lpayments = new ArrayList();
        } else {

            p.m_lpayments = l;
             // System.out.println("size of list"+l.size());
            //System.out.println("------------------>"+l+"p.m_dSalesAdjustments"+p.m_dSalesAdjustments);
            for (int i = 0; i < p.m_lpayments.size(); i++) {
               if(( p.m_lpayments.get(i)).m_PaymentType.equalsIgnoreCase("cash")) {
                   //System.out.println(" p.m_lpayments.get(i)).m_PaymentType.equalsIgnoreCase"+( p.m_lpayments.get(i)).m_PaymentType.equalsIgnoreCase("cash"));
                   //(p.m_lpayments.get(i)).m_PaymentValue = (p.m_lpayments.get(i)).m_PaymentValue-(p.m_dPaymentsTotal-(p.m_dSalesBase+(p.m_dSalesAdjustments==null?new Double(0).doubleValue():p.m_dSalesAdjustments.doubleValue())));
                   (p.m_lpayments.get(i)).m_PaymentValue = (p.m_lpayments.get(i)).m_PaymentValue-(p.m_dPaymentsTotal-(p.m_dSalesBase));
                   break;
               }
            }
          
        }


         Object[] cpriceadj = (Object []) new StaticSentence(app.getSession()
        ,"SELECT SUM(TICKETLINES.PRICE) "+
        "FROM TICKETS,RECEIPTS,TICKETLINES,PRODUCTS "+
        "WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND TICKETLINES.PRODUCT=PRODUCTS.ID AND PRODUCTS.PRODUCTTYPE='Y' AND PRODUCTS.NAME LIKE '%Price Adjustments%' AND TICKETS.ISCANCELLED='Y' AND RECEIPTS.MONEYDAY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] { Datas.DOUBLE}))
            .find(app.getActiveDayIndex());

        if (cpriceadj == null) {
            p.m_dSalesAmtCanceledAdjust = new Double(0);

        } else {

            p.m_dSalesAmtCanceledAdjust = (Double) cpriceadj[0];
        }





        return p;
    }

    /////////////// For the Change


    public int getPayments() {
        return m_iPayments.intValue();
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
    public String getDateStart() {

        SimpleDateFormat fm=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        int dateofdatabase = m_dDateStart.getDay();
        int presentdate=(new Date()).getDay();
        if(dateofdatabase==presentdate)
        return fm.format(m_dDateStart);
        else
        return fm.format(m_dDateDayfirst);
    }
    public void setDateEnd(Date dValue) {
        m_dDateEnd = dValue;
    }
    public String getDateEnd() {

       SimpleDateFormat fm=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

       return fm.format(m_dDateEnd);
     // return  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(m_dDateEnd);

      //  return m_dDateEnd;
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

    public String printPaymentsTotal() {
        return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }

    public List<PaymentsLine> getPaymentLines() {
        return m_lpayments;
    }

    public int getSales() {
        return m_iSales == null ? 0 : m_iSales.intValue();
    }
    public String printSales() {
        return Formats.INT.formatValue(m_iSales);
    }


    public String printSalesBase() {
        //return Formats.CURRENCY.formatValue((m_dSalesBase==null?new Double(0).doubleValue():m_dSalesBase.doubleValue())+
                             //(m_dSalesAdjustments==null?new Double(0).doubleValue():m_dSalesAdjustments.doubleValue()));
        return Formats.CURRENCY.formatValue((m_dSalesBase==null?new Double(0).doubleValue():m_dSalesBase.doubleValue()));

    }





    public String printSalesTaxes() {
        return Formats.CURRENCY.formatValue((m_dSalesTaxes==null?new Double(0):m_dSalesTaxes));
    }
    public String printSalesTotal() {
        return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
                ? null
                : m_dSalesBase + m_dSalesTaxes);
    }
    public List<SalesLine> getSaleLines() {
        return m_lsales;
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

    /**
     * @return the m_itotalitems
     */
    public String getTotalItemsSold() {
      //  return Formats.INT.formatValue((m_itotalitems==null?0:m_itotalitems)+(m_dSalesPriceAdjustmentscount==null?0:m_dSalesPriceAdjustmentscount));
          return Formats.INT.formatValue((m_itotalitems==null?0:m_itotalitems));
    }

    /**
     * @return the m_iSalesCanceled
     */
    public String getSalesCanceled() {
        return Formats.INT.formatValue((m_iSalesCanceled==null?0:m_iSalesCanceled));
    }

    /**
     * @return the m_dSalesAmtCanceled
     */
    public String getSalesAmtCanceled() {
        return Formats.CURRENCY.formatValue((m_dSalesAmtCanceled==null?0:m_dSalesAmtCanceled)+(m_dSalesAmtCanceledAdjust==null?0:m_dSalesAmtCanceledAdjust));
    }

    /**
     * @return the m_dSalesAdjustments
     */
    public String getSalesAdjustments() {
        return Formats.CURRENCY.formatValue((m_dSalesAdjustments==null?0:m_dSalesAdjustments));
    }

    public String printTotalSalesCollection() {
        // return Formats.CURRENCY.formatValue((m_dSalesBase==null?new Double(0).doubleValue():m_dSalesBase.doubleValue())+
                            // (m_dSalesAdjustments==null?new Double(0).doubleValue():m_dSalesAdjustments.doubleValue()));
        return Formats.CURRENCY.formatValue((m_dSalesBase==null?new Double(0).doubleValue():m_dSalesBase.doubleValue()));

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

        /**
         * @return the m_SalesTaxesRate
         */
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
            return  m_PaymentType;
        }
        public String getType() {
            return m_PaymentType;
        }

//          public   double getChange()
//          {
//            return (new PaymentDayModel().m_dPaymentsTotal-new PaymentDayModel().m_dSalesBase);
//          }

        public String printValue() {
            return Formats.CURRENCY.formatValue(m_PaymentValue);
//            if(m_PaymentType=="cash"||m_PaymentType.equals("cash"))
//            {
//                System.out.println("--------------------------->"+m_PaymentValue);
//            return Formats.CURRENCY.formatValue(m_PaymentValue-getChange());
//            }
//            else
//            {
//                System.out.println(""+m_PaymentValue);
//
//            }
        }
        public Double getValue() {
            return m_PaymentValue;
        }

        /**
         * @return the m_Paymentcount
         */
        public String getPaymentCount() {
            return Formats.INT.formatValue(m_Paymentcount);
        }
    }
}