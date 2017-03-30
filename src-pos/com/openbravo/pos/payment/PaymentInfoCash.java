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

import com.openbravo.format.Formats;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentInfoCash extends PaymentInfo {

    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private double m_dchange;
    int voucherNo = 0;
    ArrayList<VouchersList> li = new ArrayList<VouchersList>();

    /**
     * Creates a new instance of PaymentInfoCash
     */
    public PaymentInfoCash(double dTotal, double dPaid, double change) {
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
        m_dchange = change;
    }

    public PaymentInfo copyPayment() {
        return new PaymentInfoCash(m_dTotal, m_dPaid, m_dchange);
    }

    public String getName() {
        return "Cash";
    }

    public double getTotal() {
        return m_dPaid-m_dchange;
    }

    public double getPaid() {
        return m_dPaid;
    }

    public String getTransactionID() {
        return "no ID";
    }

    public String getID() {
        return m_id;
    }

    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid));
    }

    public String printChange() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal));
    }

    public List getChequenumbers() {
        return new ArrayList();
    }

    public String getVoucherNo() {
        return null;
    }

    public List getCardnumbers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {

        VouchersList vlist = new VouchersList(voucherNo + "", m_dPaid);
        li.add(vlist);
        return li;
        
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
