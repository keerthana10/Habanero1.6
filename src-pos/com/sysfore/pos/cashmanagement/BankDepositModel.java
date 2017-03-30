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
public class BankDepositModel {


   
    private final static String[] PAYMENTHEADERS = {"Label.Payment", "label.totalcash"};
    

    private static String m_dMoneyvalue;
    private Double m_dCashAmt;

    private Integer m_itotalitems;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    private static String todayDate;
    private final static String[] SALEHEADERS = {"label.taxcash", "label.totalcash"};
    String PosNo;

   
    AppConfig aconfig = new AppConfig(new File(System.getProperty("user.home") + "/openbravopos.properties"));
    private BankDepositModel() {
     aconfig.load();


   
    
    }    
    
    public static BankDepositModel emptyInstance() {
        
        BankDepositModel p = new BankDepositModel();
        
    

        p.m_dCashAmt = null;
        p.m_dMoneyvalue = null;

   
        return p;
    }
    
    public static BankDepositModel loadInstance(AppView app) throws BasicException {
        
        BankDepositModel p = new BankDepositModel();
        Calendar cal = Calendar.getInstance();
        java.util.Date m_dDate = new Date();
      // m_dDate =
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        todayDate = sdf.format(m_dDate).toString();
        System.out.println("enrtr----"+todayDate);
      
 
          Object[] cashAmt = (Object []) new StaticSentence(app.getSession()
            , "SELECT sum(PAYMENTS.TOTAL) " +
              "FROM PAYMENTS, RECEIPTS " +
              "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND PAYMENTS.PAYMENT = 'CASH' AND RECEIPTS.DATENEW >= ?  "
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.DOUBLE}))
            .find(p.todayDate);

        if (cashAmt == null) {

            p.m_dCashAmt = new Double(0.0);
        } else {
            p.m_dCashAmt = (Double) cashAmt[0];

        }

 
        return p;
    }

   public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());

  }
    public double getCashAmt(){
        return m_dCashAmt;
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
   

    public String printCashAmt() {
        return Formats.CURRENCY.formatValue(m_dCashAmt);
    }

   
 
}    