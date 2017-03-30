/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.payment;

import com.openbravo.format.Formats;

/**
 *
 * @author ravi
 */
public class PaymentDetails {
private String m_paymentno;
private double m_paymentamt;

    /**
     * @return the m_paymentno
     */
    public String getPaymentno() {
        return m_paymentno;
    }

    /**
     * @param m_paymentno the m_paymentno to set
     */
    public void setPaymentno(String m_paymentno) {
        this.m_paymentno = m_paymentno;
    }

    /**
     * @return the m_paymentamt
     */
    public double getPaymentamt() {
        return m_paymentamt;
    }

    /**
     * @param m_paymentamt the m_paymentamt to set
     */
    public void setPaymentamt(double m_paymentamt) {
        this.m_paymentamt = m_paymentamt;
    }

    public String printPaymentAmt(){
        return Formats.CURRENCY.formatValue(new Double(getPaymentamt()));
    }



}
