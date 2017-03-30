/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

import com.openbravo.format.Formats;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author shilpa
 */
public class PaymentInfoChequeDetails extends PaymentInfo {

    private String m_id;
    private double m_dPaid;
    private double m_dTotal;
    private String chequeNo;
    private ArrayList<PaymentDetails> cardnumbers;

    /** Creates a new instance of PaymentInfoVoucherDetails */
    public PaymentInfoChequeDetails(double dTotal, double dPaid,String chequeNum){//, List cardno) {
        m_id = UUID.randomUUID().toString();
        m_dTotal = dTotal;
        m_dPaid = dPaid;
        chequeNo=chequeNum;
        //cardnumbers = (ArrayList<PaymentDetails>) cardno;
    }
    


    @Override
    public PaymentInfo copyPayment() {
        return new PaymentInfoVoucherDetails(m_dTotal, m_dPaid,chequeNo);//, getCardnumbers());
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
       return chequeNo;
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
