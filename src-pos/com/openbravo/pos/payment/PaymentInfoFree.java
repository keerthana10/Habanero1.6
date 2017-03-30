//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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

package com.openbravo.pos.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentInfoFree extends PaymentInfo {
    
    private double m_dTotal;
    private String m_id;
    /** Creates a new instance of PaymentInfoFree */
    public PaymentInfoFree(double dTotal) {
        m_id=UUID.randomUUID().toString();
        m_dTotal = dTotal;
    }
    
    public PaymentInfo copyPayment(){
        return new PaymentInfoFree(m_dTotal);
    }    
    public String getName() {
        return "free";
    }   
    public double getTotal() {
        return m_dTotal;
    }
    public String getTransactionID(){
        return "no ID";
    }
    public String getID() {
        //throw new UnsupportedOperationException("Not supported yet.");
       return m_id;
    }
       public List getChequenumbers() {
        return new ArrayList();
    }
        public String getVoucherNo() {
             //   return null;
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPaymentMode() {
     return "";
    }

    @Override
    public String getDescription() {
      return "";
    }
}
