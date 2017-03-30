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

public class PaymentInfoCheque extends PaymentInfo {
    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private String chequeNo;
    private ArrayList<PaymentDetails> chequenumbers;


    /** Creates a new instance of PaymentInfoCash */
    public PaymentInfoCheque(double dTotal, double dPaid, List dchno) {
        m_id=UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
        chequenumbers=(ArrayList)dchno;
    }

      
      
     
    public PaymentInfo copyPayment(){
        return new PaymentInfoCheque(m_dTotal, m_dPaid, getChequenumbers());
    }

    public String getName() {
        return "Cheque";
    }
    public double getTotal() {
        return m_dPaid;
    }
    public double getPaid() {
        return m_dPaid;
    }
    public String getTransactionID(){
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

    /**
     * @return the chequenumbers
     */
    public List getChequenumbers() {
        return chequenumbers;
    }

    public List getCardnumbers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getVoucherNo() {
        return this.chequeNo;
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
