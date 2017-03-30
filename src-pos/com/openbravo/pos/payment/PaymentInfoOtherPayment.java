/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

/**
 *
 * @author ravi
 */
import com.openbravo.format.Formats;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentInfoOtherPayment extends PaymentInfo {

    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private String paymentMode;
    private String description;
    private String paymentType;
    private ArrayList<PaymentDetails> cardnumbers;

    /** Creates a new instance of PaymentInfoCash */
    public PaymentInfoOtherPayment(double dTotal, double dPaid, String paymentType,String paymentMode, String description){//, List cardno) {
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
        this.paymentMode = paymentMode;
        this.description = description;
        this.paymentType = paymentType;

        //cardnumbers = (ArrayList<PaymentDetails>) cardno;g
    }

    @Override
    public PaymentInfo copyPayment() {
        return new PaymentInfoOtherPayment(m_dTotal, m_dPaid,paymentType,paymentMode,description);//, getCardnumbers());
    }

    public String getName() {
        return paymentType;
    }
     public String getPaymentMode() {
        return paymentMode;
    }
 public String getDescription() {
        return description;
    }

    public double getTotal() {
        return m_dPaid;
    }

    public double getPaid() {
        return m_dPaid;
    }

    public String getTransactionID() {
        return "no ID";
    }

    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid));
    }

    public String printChange() {
        return Formats.CURRENCY.formatValue(new Double(m_dPaid - m_dTotal));
    }

    public String getID() {
        return m_id;
    }

 
    public List getChequenumbers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List getCardnumbers() {
        return cardnumbers;
    }

    @Override
    public String getVoucherNo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<VouchersList> getPaymentSplits() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
