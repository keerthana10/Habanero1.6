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

public class PaymentInfoVoucher extends PaymentInfo {

    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private double v_Total;
    private String voucherNo;
    private ArrayList<PaymentDetails> vouchernumbers;

    /**
     * Creates a new instance of PaymentInfoCash
     */
    public PaymentInfoVoucher() {
        m_id = UUID.randomUUID().toString();
        //  m_dTotal = dTotal;
        // m_dPaid = dPaid;
    }

    public PaymentInfo copyPayment() {
        return new PaymentInfoVoucherDetails(m_dTotal, m_dPaid);
    }

    public String getName() {
        return "Voucher";
    }

    public double getTotal() {
        return v_Total;
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

    public String getVoucherNo() {
        return voucherNo;
    }

    /**
     * @param voucherNo the voucherNo to set
     */
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public double getV_Total() {
        return v_Total;
    }

    public List getChequenumbers() {
        return vouchernumbers;
    }

    /**
     * @param v_Total the v_Total to set
     */
    public void setV_Total(double v_Total) {
        this.v_Total = v_Total;
    }

    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid));
    }

    public String printChange() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal));
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {
        return null;
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
