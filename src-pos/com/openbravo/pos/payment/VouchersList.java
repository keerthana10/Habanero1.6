/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment;

/**
 *
 * @author mateen
 */
public class VouchersList {
    
    
    private String voucher;
    private double amount;

    public VouchersList() {
    }

    public VouchersList(String voucher, double amount) {
        this.voucher = voucher;
        this.amount = amount;
    }


    
    /**
     * @return the voucher
     */
    public String getVoucher() {
        return voucher;
    }

    /**
     * @param voucher the voucher to set
     */
    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
