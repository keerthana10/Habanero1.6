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
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;




    public class CloseShiftModel {

    private String m_sHost;
    private int m_iSeq;
    private Date m_dDateStart;
    private Date m_dDateEnd;
    private Date m_dDateDayfirst;
    private static String m_sPosNo;

    private java.util.List<CloseCashProperties> cashproperties;
    private Integer m_iPayments;
    private Double m_dPaymentsTotal;
    private  java.util.List<PaymentsByUser> m_lpaymentsUser;
    private java.util.List<PaymentsLine> m_lpayments;
    private java.util.List<PaymentsLineSplit> m_lpaymentsplits;
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    private Integer m_iSales;
    private Double m_dSalesBase;
    private Double m_dSalesPriceAdjustments;
    private Integer m_dSalesPriceAdjustmentscount;
    private Double m_dSalesTaxes;
    private Integer m_dTotalItemsSold;
    private double p_totalDiscount;
    private double p_totalBillDiscount;
    private java.util.List<SalesLine> m_lsales;
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};
    private Double m_dFloatAmt;
     String PosNo;
    String floatAmt;
    AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
    private CloseShiftModel() {
    aconfig.load();
    }

    public static CloseShiftModel emptyInstance() {

    CloseShiftModel p = new CloseShiftModel();
    p.m_iPayments = new Integer(0);
    p.m_dPaymentsTotal = new Double(0.0);
    p.m_lpayments = new ArrayList<PaymentsLine>();
    p.cashproperties= new ArrayList<CloseCashProperties>() ;
    p.m_iSales = null;
    p.m_dSalesBase = null;
    p.m_dSalesPriceAdjustments=null;
    p.m_dSalesTaxes = null;
    p.m_lpaymentsUser=null;
    p.m_lsales = new ArrayList<SalesLine>();
    p.m_lpaymentsplits=new ArrayList<PaymentsLineSplit>();
    p.m_dTotalItemsSold=null;
    p.m_dSalesPriceAdjustmentscount=new Integer(0);
    p.p_totalDiscount = new Double(0.0);
    p.p_totalBillDiscount = new Double(0.0);
    return p;
    }

    public static CloseShiftModel loadInstance(AppView app) throws BasicException {

    CloseShiftModel p = new CloseShiftModel();

    // Propiedades globales
    p.m_sHost = app.getProperties().getHost();
    p.m_iSeq = app.getActiveCashSequence();
    p.m_dDateStart = app.getActiveCashDateStart();
     p.m_sPosNo = app.getProperties().getPosNo();
    p.m_dDateEnd = null;



   Object[] valtickets = (Object []) new StaticSentence(app.getSession()
    , "SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) " +
    "FROM RECEIPTS,TICKETS,TICKETLINES WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"
    , SerializerWriteString.INSTANCE
    , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}))
    .find(app.getActiveCashIndex());

    if (valtickets == null) {
    p.m_iPayments = new Integer(0);
    p.m_dPaymentsTotal = new Double(0.0);
    } else {
    p.m_iPayments = (Integer) valtickets[0];
    p.m_dPaymentsTotal = (Double) valtickets[1];
    }




   Object[] st =  (Object[]) new StaticSentence(app.getSession(),
        "SELECT DATESTART FROM CLOSEDCASH WHERE MONEY = ? AND POSNO='"+m_sPosNo+"'",

    SerializerWriteString.INSTANCE,
    new SerializerReadBasic(new Datas[] {Datas.TIMESTAMP}))
    .find(app.getActiveCashIndex());

    Date date;
    if (st == null) {
    date=null;

    } else {
    date= (Date) st[0];

    }


    Object[] dayfirst =  (Object[]) new StaticSentence(app.getSession(),
        "SELECT MIN(STARTTIME) FROM PEOPLELOG WHERE TO_DAYS(STARTTIME)=TO_DAYS(NOW()) AND REASON='Login' AND POSNO='"+m_sPosNo+"' ",
    null,
    new SerializerReadBasic(new Datas[] {Datas.TIMESTAMP}))
    .find(app.getActiveCashIndex());


    if (dayfirst == null) {
    p.m_dDateDayfirst=null;

    } else {
    p.m_dDateDayfirst= (Date) dayfirst[0];

    }




  List lsu = (List) new StaticSentence(app.getSession()
    ,"SELECT PEOPLE_ID,STARTTIME AS LOGIN,(SELECT MIN(STARTTIME) FROM PEOPLELOG WHERE  STARTTIME >LOGIN AND "+
     "REASON='Logout' ) FROM PEOPLELOG WHERE REASON='Login' "+
     "AND STARTTIME >= ?  AND POSNO='"+m_sPosNo+"' "+
     "AND STARTTIME <= CURRENT_TIMESTAMP ORDER BY STARTTIME"

    , SerializerWriteTimeStamp.INSTANCE
    , new SerializerReadClass(CloseShiftModel.PaymentsByUser.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
    .list(date);

    if (lsu == null) {
    p.m_lpaymentsUser = new ArrayList();
    } else {
    p.m_lpaymentsUser = lsu;
    // p.ss = new UserPayments[lsu.size()];

    }

   java.util.ArrayList<CloseShiftModel.CloseCashProperties> cashcpp = new java.util.ArrayList<CloseShiftModel.CloseCashProperties>();
   Iterator<PaymentsByUser> iterator = p.m_lpaymentsUser.iterator();

  ////////////////////////////////////////////////////  Starting While loop


    while(iterator.hasNext())
    {
    CloseShiftModel.CloseCashProperties ccp = new CloseShiftModel.CloseCashProperties();
    PaymentsByUser pbu=iterator.next();

    Date sdate = pbu.getStime();
    Date edate = pbu.getEtime();
    String id= pbu.getId();

    ccp.setP_DateEnd((edate==null?p.m_dDateEnd:edate));
    ccp.setP_DateStart(sdate);

   Object[] username =  (Object[]) new StaticSentence(app.getSession(),
        " SELECT NAME FROM PEOPLE WHERE ID = ? ",
    SerializerWriteString.INSTANCE,
    new SerializerReadBasic(new Datas[] {Datas.STRING}))
    .find(id);


    if (username == null) {
    ccp.setP_username(null);

    } else {
    ccp.setP_username((String) username[0]);

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

// Sales
    Object[] recsales = (Object []) new StaticSentence(app.getSession(),
    "SELECT COUNT(DISTINCT RECEIPTS.ID),SUM(TICKETLINES.UNITS* TICKETLINES.PRICE),SUM(TICKETLINES.UNITS)" +
    "FROM RECEIPTS,TICKETS,TICKETLINES,PRODUCTS WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET  AND  TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
    new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP}),
    new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE,Datas.INT}))
    .find(app.getActiveCashIndex(),sdate,(edate==null?new Date():edate));
    if (recsales == null) {
    ccp.setP_totalnoreceipts(null);
    ccp.setP_totalsales(null);
    ccp.setP_totalitems(null);
    } else {
    ccp.setP_totalnoreceipts((Integer) recsales[0]);
    ccp.setP_totalsales( (Double) recsales[1]);
    ccp.setP_totalitems((Integer) recsales[2]);
    }

     Object[] recDiscountsales = (Object []) new StaticSentence(app.getSession(),
    "SELECT SUM(TICKETLINES.DISCOUNT) " +
    "FROM RECEIPTS,TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
    new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP}),
    new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
    .find(app.getActiveCashIndex(),sdate,(edate==null?new Date():edate));
    if (recDiscountsales == null) {
    ccp.setP_totalDiscount(0.0);

    } else {
    //ccp.setP_totalDiscount( (Double)recDiscountsales[0]);
ccp.setP_totalDiscount(0.0);
    }
     Object[] recBillDiscountsales = (Object []) new StaticSentence(app.getSession(),
    "SELECT SUM(TICKETS.CUSTOMERDISCOUNT) " +
    "FROM RECEIPTS,TICKETS WHERE RECEIPTS.ID = TICKETS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
    new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP}),
    new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
    .find(app.getActiveCashIndex(),sdate,(edate==null?new Date():edate));
    if (recBillDiscountsales == null) {
    ccp.setP_totalBillDiscount(0.0);

    } else {
  //  ccp.setP_totalBillDiscount( (Double)recBillDiscountsales[0]);
ccp.setP_totalBillDiscount(0.0);
    }

   //price adjustments
    PreparedSentence psd = new PreparedSentence(app.getSession()
      ,"SELECT SUM(TICKETLINES.PRICE),SUM(TICKETLINES.UNITS)" +
      "FROM TICKETS, RECEIPTS,TICKETLINES,PRODUCTS " +
      "WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND TICKETLINES.PRODUCT=PRODUCTS.ID AND PRODUCTS.NAME LIKE '%Price Adjustments%' AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"

        , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
        , new SerializerReadBasic(new Datas[] {Datas.DOUBLE,Datas.INT}));

    Object[] priceadj = (Object []) psd.find(new Object[]{
        app.getActiveCashIndex(),sdate,(edate==null?new Date():edate)
    });
   if (priceadj == null) {
    ccp.setP_priceadjustments(new Double(0));
    ccp.setP_totalnoadjreceipts(new Integer(0));

    } else {

    ccp.setP_priceadjustments((Double) priceadj[0]);
    ccp.setP_totalnoadjreceipts((Integer)priceadj[1]);
    }

    //cancellation count and amount

    PreparedSentence psdl = new PreparedSentence(app.getSession()
     ,"SELECT COUNT(DISTINCT RECEIPTS.ID),SUM(TICKETLINES.UNITS * TICKETLINES.PRICE)" +
      "FROM TICKETS, RECEIPTS,TICKETLINES,PRODUCTS " +
      "WHERE TICKETS.ID=RECEIPTS.ID AND TICKETS.ID=TICKETLINES.TICKET  AND TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"

        , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
        , new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE}));

    Object[] cancelbill = (Object []) psdl.find(new Object[]{
        app.getActiveCashIndex(),sdate,(edate==null?new Date():edate)
    });
   if (cancelbill == null) {
    ccp.setP_totalcanceledreceipts(new Integer(0));
    ccp.setP_totalcanceledamount(new Double(0));

    } else {

    ccp.setP_totalcanceledreceipts((Integer) cancelbill[0]);
    ccp.setP_totalcanceledamount((Double) cancelbill[1]);
    }


    //payments
    List l = new StaticSentence(app.getSession()
    , "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL),COUNT(*) " +
      "FROM PAYMENTS,RECEIPTS,TICKETS,PEOPLE " +
      " WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND TICKETS.ID=RECEIPTS.ID AND PEOPLE.ID=TICKETS.PERSON AND  RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'" +
      " GROUP BY PAYMENTS.PAYMENT"
    , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadClass(CloseShiftModel.PaymentsLine.class)) //new SeriaStaticSentencelizerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
    .list(new Object[] {app.getActiveCashIndex(),sdate,(edate==null?new Date():edate)} );
        System.out.println("enetrr --");
    if (l == null) {
      ccp.setP_payments(new ArrayList());
    }  else {
      ccp.setP_payments(l);

       for (int i = 0; i < ccp.getP_payments().size(); i++) {
             //  if(( ccp.getP_payments().get(i)).m_PaymentType.equalsIgnoreCase("cash")) {
             //      (ccp.getP_payments().get(i)).m_PaymentValue = (ccp.getP_payments().get(i)).m_PaymentValue-(ccp.getPaymentsTotalDouble()-ccp.getP_totalsalesDouble());
             //      break;
              // }
            }
      }

    //payments splits


  /*  List ls = new StaticSentence(app.getSession()
    , "SELECT PAYMENTS.PAYMENT, PAYMENTSPLITS.IDENTIFIER,PAYMENTSPLITS.AMOUNT " +
      "FROM PAYMENTS, RECEIPTS,PAYMENTSPLITS,TICKETS " +
      "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.ID=PAYMENTSPLITS.PAYMENTS_ID AND TICKETS.ID=RECEIPTS.ID AND TICKETS.ISCANCELLED='N' AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? "

    , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadClass(CloseShiftModel.PaymentsLineSplit.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
    .list(new Object[] {app.getActiveCashIndex(),sdate,(edate==null?new Date():edate) });

    if (ls == null) {
     ccp.setP_paymentssplit(new ArrayList());
    } else {
    ccp.setP_paymentssplit(ls);
    }*/

    List<SalesLine> asales = new StaticSentence(app.getSession(),
        "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT),TAXES.RATE " +
        "FROM RECEIPTS,TICKETS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
        "AND TICKETS.ID=RECEIPTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ?  AND RECEIPTS.POSNO = '"+m_sPosNo+"'" +
        "GROUP BY TAXCATEGORIES.NAME"
        , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
        , new SerializerReadClass(CloseShiftModel.SalesLine.class))
        .list(app.getActiveCashIndex(),sdate,(edate==null?new Date():edate));
    if (asales == null) {
    ccp.setTaxes(new ArrayList());
    } else {
    ccp.setTaxes(asales);
    }


    Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
    "SELECT SUM(TAXLINES.AMOUNT) " +
    "FROM RECEIPTS,TICKETS,TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TICKETS.ID=RECEIPTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"
    , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
    .find(app.getActiveCashIndex(),sdate,(edate==null?new Date():edate));
    if (rectaxes == null) {
    ccp.setP_totaltax(null);
    } else {
    ccp.setP_totaltax((Double) rectaxes[0]);
    }

     PreparedSentence cpsd = new PreparedSentence(app.getSession()
      ,"SELECT SUM(TICKETLINES.PRICE)" +
      "FROM TICKETS, RECEIPTS,TICKETLINES,PRODUCTS " +
      "WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.DATENEW >= ? AND RECEIPTS.DATENEW <= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"

        , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.TIMESTAMP,Datas.TIMESTAMP})
        , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}));

    Object[] calcelpriceadj = (Object []) cpsd.find(new Object[]{
        app.getActiveCashIndex(),sdate,(edate==null?new Date():edate)
    });
   if (calcelpriceadj == null) {
    ccp.setP_cancelpriceadj(new Double(0));


    } else {

    ccp.setP_cancelpriceadj((Double) calcelpriceadj[0]);
    }


    cashcpp.add(ccp);
    }

    p.cashproperties=cashcpp;
   ////////////////////////////////////////////////////  Starting While loop





 List l = new StaticSentence(app.getSession()
   ,"SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL),COUNT(*) "+
     "FROM PAYMENTS, RECEIPTS,TICKETS,PEOPLE "+
     "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND TICKETS.ID=RECEIPTS.ID "+
     "AND PEOPLE.ID=TICKETS.PERSON AND RECEIPTS.MONEY= ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"+
     "GROUP BY PEOPLE.NAME,PAYMENTS.PAYMENT"

    , SerializerWriteString.INSTANCE
    , new SerializerReadClass(CloseShiftModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
    .list(app.getActiveCashIndex());
 System.out.println("enetrr1 --");
    if (l == null) {
    p.m_lpayments = new ArrayList();
    } else {
    p.m_lpayments = l;
    }





//
 // Sales
    Object[] recsales = (Object []) new StaticSentence(app.getSession(),
    "SELECT COUNT(DISTINCT RECEIPTS.ID),SUM(TICKETLINES.UNITS * TICKETLINES.PRICE),SUM(TICKETLINES.UNITS)"+
    "FROM RECEIPTS,TICKETS,TICKETLINES,PRODUCTS WHERE RECEIPTS.ID=TICKETS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND  TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'",
    SerializerWriteString.INSTANCE,
    new SerializerReadBasic(new Datas[] {Datas.INT, Datas.DOUBLE,Datas.INT}))
    .find(app.getActiveCashIndex());
    if (recsales == null) {
    p.m_iSales = null;
    p.m_dSalesBase = null;
    p.m_dTotalItemsSold=null;
    } else {
    p.m_iSales = (Integer) recsales[0];
    p.m_dSalesBase = (Double) recsales[1];
    p.m_dTotalItemsSold=(Integer) recsales[2];
    }



  // Price adjustments
    PreparedSentence psd = new PreparedSentence(app.getSession()
      ,"SELECT SUM(TICKETLINES.PRICE),SUM(TICKETLINES.UNITS) "+
      "FROM TICKETS,RECEIPTS,TICKETLINES,PRODUCTS "+
      "WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TICKETLINES.TICKET AND TICKETLINES.PRODUCT=PRODUCTS.ID AND RECEIPTS.MONEY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"

        , new SerializerWriteBasic(new Datas[]{ Datas.STRING })
        , new SerializerReadBasic(new Datas[] {Datas.DOUBLE,Datas.INT}));

    Object[] priceadj = (Object []) psd.find(new Object[]{
        app.getActiveCashIndex()
    });

   if (priceadj == null) {
    p.m_dSalesPriceAdjustments=new Double(0);
    p.m_dSalesPriceAdjustmentscount=new Integer(0);

    } else {
   p.m_dSalesPriceAdjustments=(Double) priceadj[0];
   p.m_dSalesPriceAdjustmentscount=(Integer) priceadj[1];
    }


    // Taxes
    Object[] rectaxes = (Object []) new StaticSentence(app.getSession(),
    "SELECT SUM(TAXLINES.AMOUNT) " +
    "FROM RECEIPTS,TICKETS,TAXLINES WHERE TICKETS.ID=RECEIPTS.ID AND RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'"
    , SerializerWriteString.INSTANCE
    , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
    .find(app.getActiveCashIndex());
    if (rectaxes == null) {
    p.m_dSalesTaxes = null;
    } else {
    p.m_dSalesTaxes = (Double) rectaxes[0];
    }


    List<SalesLine> asales = new StaticSentence(app.getSession(),
        "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT) ,COUNT(*) " +
        "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " +
        "AND RECEIPTS.MONEY = ? AND RECEIPTS.POSNO = '"+m_sPosNo+"'" +
        "GROUP BY TAXCATEGORIES.NAME"
        , SerializerWriteString.INSTANCE
        , new SerializerReadClass(CloseShiftModel.SalesLine.class))
        .list(app.getActiveCashIndex());
    if (asales == null) {
    p.m_lsales = new ArrayList<SalesLine>();
    } else {
    p.m_lsales = asales;
    }

    return p;
    }





    public List<CloseCashProperties> getUsersDetails() {
    return cashproperties;
    }

 public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());

  }
    public List<PaymentsByUser> getUsers() {
    return m_lpaymentsUser;
    }
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
    public Date getDateStart() {
        int dateofdatabase = m_dDateStart.getDay();
        int presentdate=(new Date()).getDay();
        if(dateofdatabase==presentdate)
        return m_dDateStart;
        else
        return m_dDateDayfirst;
    }
    public void setDateEnd(Date dValue) {
    m_dDateEnd = dValue;
    }
    public void setSeq(int dValue) {
    m_iSeq = dValue;
    }
    public Date getDateEnd() {
    return m_dDateEnd;
    }

    public String printHost() {
    return m_sHost;
    }
    public String printSequence() {
    return Formats.INT.formatValue(m_iSeq);
    }
    public String printDateStart() {
    return getStartDateTimeForPrint();
    }

    public String getStartDateTimeForPrint() {
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      return sdf.format(m_dDateStart).toString();
    }
    public String printDateEnd() {
    return getEndDateTimeForPrint();
    }
    public String getEndDateTimeForPrint() {
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      return sdf.format(m_dDateEnd).toString();
    }
    public String printPayments() {
    return Formats.INT.formatValue(m_iPayments);
    }

    public String printPaymentsTotal() {
    return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
    }
    public String printFloatAmt() {
        return Formats.CURRENCY.formatValue(m_dFloatAmt);
    }

    public List<PaymentsLine> getPaymentLines() {
    return m_lpayments;
    }

    public List<PaymentsLineSplit> getPaymentSplitLines() {
    return m_lpaymentsplits;
    }

    public int getSales() {
    return m_iSales == null ? 0 : m_iSales.intValue();
    }

    public String printSales() {
    return Formats.INT.formatValue(m_iSales);
    }

    public String printSalesBase(){

       return Formats.CURRENCY.formatValue((m_dSalesBase==null?new Double(0).doubleValue():m_dSalesBase.doubleValue()));


    }
//+(m_dSalesPriceAdjustments==null?new Double(0):m_dSalesPriceAdjustments)
    public String printSalesTaxes() {
    return Formats.CURRENCY.formatValue(m_dSalesTaxes);
    }

    public String printSalesTotal() {
    return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null)
        ? null
        : m_dSalesBase + m_dSalesTaxes-(p_totalDiscount+p_totalBillDiscount));
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

    /**
    * @return the m_dTotalItemsSold
    */
    public String printNumberOfItemsSold() {
   // return Formats.INT.formatValue((m_dTotalItemsSold ==null?0:m_dTotalItemsSold)+(m_dSalesPriceAdjustmentscount==null?0:m_dSalesPriceAdjustmentscount));
         return Formats.INT.formatValue(m_dTotalItemsSold ==null?0:m_dTotalItemsSold);
    }

 public String printPosNo() {
       
    return aconfig.getProperty("machine.PosNo");
    }
 public String printFloatAmount() {
     // AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
        //  aconfig.load();
    return aconfig.getProperty("machine.FloatAmt");
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

//    public String printTaxTotal(){
//
//    }

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
    return  m_PaymentType;
    }
    public String getType() {
    return m_PaymentType;
    }
    public String printValue() {
        System.out.println("enrtrr ------"+m_PaymentValue);
    return Formats.CURRENCY.formatValue(m_PaymentValue);
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

    public static class PaymentsByUser implements SerializableRead {

    private String userid;
    private Date stime;
    private Date etime;

    public void readValues(DataRead dr) throws BasicException {
    userid = dr.getString(1);
    stime = dr.getTimestamp(2);
    etime = dr.getTimestamp(3);

    }


    public String getId() {
    return userid;
    }

    public Date getStime() {
    return stime;
    }
    public Date getEtime() {
    return etime;
    }
    public String printValue() {
    return userid;
    }

    public String printUserName() {
    return Formats.TIMESTAMP.formatValue(stime);
    }
    public String getUserName() {
    return Formats.TIMESTAMP.formatValue(etime);
    }
    }

    public static class PaymentsLineSplit implements SerializableRead {

    private String m_PaymentType;
    private String m_PaymentNo;
    private Double m_PaymentAmt;

    public void readValues(DataRead dr) throws BasicException {
    m_PaymentType=dr.getString(1);
    m_PaymentNo = dr.getString(2);
    m_PaymentAmt =dr.getDouble(3);
    }

    public String printSplitType() {
    return AppLocal.getIntString("transpayment." + m_PaymentType);
    }
    public String getSplitType() {
    return m_PaymentType;
    }
    public String printSplitNo() {
    return m_PaymentNo;
    }
    public String getSplitNo() {
    return m_PaymentNo;
    }
    public String printSplitAmt() {
    return Formats.CURRENCY.formatValue(m_PaymentAmt);
    }
    public Double getSplitAmt() {
    return m_PaymentAmt;
    }
 }

    public static class CloseCashProperties  {

    private Date p_DateStart;
    private Date p_DateEnd;
    private String p_username;
    private Double p_totalsales;
    private Double p_priceadjustments;
    private Integer p_totalnoreceipts;
    private double p_totalDiscount;
    private double p_totalBillDiscount;
    private Integer p_totalnoadjreceipts;
    private Integer p_totalcanceledreceipts;
    private Double p_totalcanceledamount;
    private Double p_totaltaX;
    private Integer p_totalitems;
    private List<PaymentsLine> p_payments;
    private List<PaymentsLineSplit> p_paymentssplit;
    private List<SalesLine> taxes;
    private Double p_cancelpriceadj;





    public String getP_DateStart() {
    SimpleDateFormat fm=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return fm.format(p_DateStart);
    }

    /**
    * @param p_DateStart the p_DateStart to set
    */
    public void setP_DateStart(Date p_DateStart) {
    this.p_DateStart = p_DateStart;
    }

    /**
    * @return the p_DateEnd
    */
    public String getP_DateEnd() {
    SimpleDateFormat fm=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return (p_DateEnd==null?fm.format(new Date()):fm.format(p_DateEnd));
    }

    /**
    * @param p_DateEnd the p_DateEnd to set
    */
    public void setP_DateEnd(Date p_DateEnd) {
    this.p_DateEnd = p_DateEnd;
    }
    public String getTotalSaleLines()
        {

         double dTotal = 0.0;
        Iterator i = taxes.iterator();
        while (i.hasNext()) {
         SalesLine p = (SalesLine) i.next();
            dTotal += p.getTaxes();
        }

        return Formats.CURRENCY.formatValue(dTotal);
        }
    /**
    * @return the p_username
    */
    public String getP_username() {
    return p_username;
    }

    /**
    * @param p_username the p_username to set
    */
    public void setP_username(String p_username) {
    this.p_username = p_username;
    }

    /**
    * @return the p_totalsales
    */
    public String getP_totalsales() {
   // return Formats.CURRENCY.formatValue(p_totalsales);
    return Formats.CURRENCY.formatValue((p_totalsales==null?new Double(0).doubleValue():p_totalsales.doubleValue()));
    }



    /**
    * @param p_totalsales the p_totalsales to set
    */
    public void setP_totalsales(Double p_totalsales) {
    this.p_totalsales = p_totalsales;
    }

    /**
    * @return the p_priceadjustments
    */
    public String getP_priceadjustments() {
    return Formats.CURRENCY.formatValue((p_priceadjustments==null?new Integer(0):p_priceadjustments));
    }

    /**
    * @param p_priceadjustments the p_priceadjustments to set
    */
    public void setP_priceadjustments(Double p_priceadjustments) {
    this.p_priceadjustments = p_priceadjustments;
    }

    public Double getP_totalsalesDouble() {
   // return Formats.CURRENCY.formatValue(p_totalsales);
    return (p_totalsales==null?new Double(0).doubleValue():p_totalsales.doubleValue());
    }
    public Double getPaymentsTotalDouble()
        {

         double dTotal = 0.0;
        Iterator i = p_payments.iterator();
        while (i.hasNext()) {
         PaymentsLine p = (PaymentsLine) i.next();
            dTotal += p.getValue();
        }

        return dTotal;
        }
    /**
    * @return the p_totalnoreceipts
    */
    public String getP_totalnoreceipts() {
    return Formats.INT.formatValue(p_totalnoreceipts);
    }

    /**
    * @param p_totalnoreceipts the p_totalnoreceipts to set
    */
    public void setP_totalnoreceipts(Integer p_totalnoreceipts) {
    this.p_totalnoreceipts = p_totalnoreceipts;
    }
    public double getP_totalDiscount() {
    return p_totalDiscount;
    }

    public void setP_totalDiscount(double p_totalDiscount) {
    this.p_totalDiscount = p_totalDiscount;
    }
     public double getP_totalBillDiscount() {
    return p_totalBillDiscount;
    }

    public void setP_totalBillDiscount(double p_totalBillDiscount) {
    this.p_totalBillDiscount = p_totalBillDiscount;
    }
    public String printDiscount(){
       return Formats.CURRENCY.formatValue(p_totalDiscount+p_totalBillDiscount);
    }

    /**
    * @return the p_totalcanceledreceipts
    */
    public String getP_totalcanceledreceipts() {
    return Formats.INT.formatValue(p_totalcanceledreceipts);
    }

    /**
    * @param p_totalcanceledreceipts the p_totalcanceledreceipts to set
    */
    public void setP_totalcanceledreceipts(Integer p_totalcanceledreceipts) {
    this.p_totalcanceledreceipts = p_totalcanceledreceipts;
    }

    /**
    * @return the p_totalcanceledamount
    */
    public String getP_totalcanceledamount() {
    return Formats.CURRENCY.formatValue((p_totalcanceledamount==null?new Integer(0):p_totalcanceledamount));
    }

    /**
    * @param p_totalcanceledamount the p_totalcanceledamount to set
    */
    public void setP_totalcanceledamount(Double p_totalcanceledamount) {
    this.p_totalcanceledamount = p_totalcanceledamount;
    }

    /**
    * @return the p_payments
    */
    public List<PaymentsLine> getP_payments() {
    return p_payments;
    }

    /**
    * @param p_payments the p_payments to set
    */
    public void setP_payments(List<PaymentsLine> p_payments) {
    this.p_payments = p_payments;
    }

    /**
    * @return the p_paymentssplit
    */
    public List<PaymentsLineSplit> getP_paymentssplit() {
    return p_paymentssplit;
    }

    /**
    * @param p_paymentssplit the p_paymentssplit to set
    */
    public void setP_paymentssplit(List<PaymentsLineSplit> p_paymentssplit) {
    this.p_paymentssplit = p_paymentssplit;
    }

    /**
    * @return the taxes
    */
    public List<SalesLine> getTaxes() {
    return taxes;
    }

    /**
    * @param taxes the taxes to set
    */
    public void setTaxes(List<SalesLine> taxes) {
    this.taxes = taxes;
    }

    /**
    * @return the p_totaltaX
    */
    public String getP_totaltax() {
    return Formats.CURRENCY.formatValue(p_totaltaX);
    }

    /**
    * @param p_totaltaX the p_totaltaX to set
    */
    public void setP_totaltax(Double p_totaltaX) {
    this.p_totaltaX = p_totaltaX;
    }


    public String userTotalSalesCollection() {
    return Formats.CURRENCY.formatValue((p_totalsales==null?new Double(0).doubleValue():((p_totalsales.doubleValue())+ p_totaltaX)-(p_totalDiscount+p_totalBillDiscount)));
    }

    /**
    * @return the p_totalitems
    */
    public String getP_totalitems() {
    //return Formats.INT.formatValue((p_totalitems==null?new Integer(0):p_totalitems)+(p_totalnoadjreceipts==null?new Integer(0):p_totalnoadjreceipts));
        return Formats.INT.formatValue((p_totalitems==null?new Integer(0):p_totalitems));
    }

    /**
    * @param p_totalitems the p_totalitems to set
    */
    public void setP_totalitems(Integer p_totalitems) {
    this.p_totalitems = p_totalitems;
    }


      public String getPaymentsTotal()
        {



         double dTotal = 0.0;
        Iterator i = p_payments.iterator();
        while (i.hasNext()) {
         PaymentsLine p = (PaymentsLine) i.next();
            dTotal += p.getValue();
        }

        return Formats.CURRENCY.formatValue(dTotal);
        }

        /**
         * @return the p_cancelpriceadj
         */
        public Double getP_cancelpriceadj() {
            return p_cancelpriceadj;
        }

        /**
         * @param p_cancelpriceadj the p_cancelpriceadj to set
         */
        public void setP_cancelpriceadj(Double p_cancelpriceadj) {
            this.p_cancelpriceadj = p_cancelpriceadj;
        }

        /**
         * @return the p_totalnoadjreceipts
         */
        public Integer getP_totalnoadjreceipts() {
            return p_totalnoadjreceipts;
        }

        /**
         * @param p_totalnoadjreceipts the p_totalnoadjreceipts to set
         */
        public void setP_totalnoadjreceipts(Integer p_totalnoadjreceipts) {
            this.p_totalnoadjreceipts = p_totalnoadjreceipts;
        }







    }
    }
