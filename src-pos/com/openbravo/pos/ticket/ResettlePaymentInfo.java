/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import java.io.Serializable;

/**
 *
 * @author sysfore17
 */
public class ResettlePaymentInfo implements Serializable{
    private String paymentType;
    private Double amount;
    private String voucherNo;
    private String resettleReason;
    

   public  ResettlePaymentInfo(String paymentType,Double amount, String voucherNo,String resettleReason)
   {
       this.paymentType=paymentType;
       this.amount=amount;
       this.voucherNo=voucherNo;
       this.resettleReason=resettleReason;
   }
    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the voucherNo
     */
    public String getVoucherNo() {
        return voucherNo;
    }

    /**
     * @param voucherNo the voucherNo to set
     */
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    /**
     * @return the resettleReason
     */
    public String getResettleReason() {
        return resettleReason;
    }

    /**
     * @param resettleReason the resettleReason to set
     */
    public void setResettleReason(String resettleReason) {
        this.resettleReason = resettleReason;
    }
}
