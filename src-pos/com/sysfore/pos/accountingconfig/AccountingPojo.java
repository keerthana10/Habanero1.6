/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;

/**
 *
 * @author raghevendra
 */
public class AccountingPojo implements SerializableRead, SerializableWrite, Serializable {
 private   String Id;
 private   String salesCredit;
 private   String cashDebit;
 private   String cardDebit;
 private   String chequeDebit;
 private   String purDebit;
 private   String purCredit;
 private   String jourDebit;
 private   String jourCrebit;
 private   String venDebit;
 private   String venCrebit;
 private   String cusDebit;
 private   String cusCrebit;
 private   String proDebit;
 private   String proCrebit;
 private   String DiscountDebit;
 private   String NonTaxCredit;
 private   String ServiceTaxCredit;
 private String credit;
 private String purchaseNonTax;
 private String purchaseDiscount;
 private String roundOff;
 
    @Override
    public void readValues(DataRead dr) throws BasicException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setId(dr.getString(1));
        setSalesCredit(dr.getString(2));
        setCashDebit(dr.getString(3));
        setCardDebit(dr.getString(4));
        setChequeDebit(dr.getString(5));
        setPurDebit(dr.getString(6));
        setPurCredit(dr.getString(7));
        setJourDebit(dr.getString(8));
        setJourCrebit(dr.getString(9));
        setVenDebit(dr.getString(10));
        setVenCrebit(dr.getString(11));
        setCusDebit(dr.getString(12));
        setCusCrebit(dr.getString(13));
        setProDebit(dr.getString(14));
        setProCrebit(dr.getString(15));
        setDiscountDebit(dr.getString(16));
        setNonTaxCredit(dr.getString(17));
        setServiceTaxCredit(dr.getString(18));
        setCredit(dr.getString(19));
        setPurchaseNonTax(dr.getString(20));
        setPurchaseDiscount(dr.getString(21));
        setRoundOff(dr.getString(22));
                }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
               dp.setString(1,getId());
               dp.setString(2,getSalesCredit());
               dp.setString(3,getCashDebit());
               dp.setString(4,getCardDebit());
               dp.setString(5,getChequeDebit());
               dp.setString(6,getPurDebit());
               dp.setString(7,getPurCredit());
               dp.setString(8,getJourDebit());
               dp.setString(9,getJourCrebit());
               dp.setString(10,getVenDebit());
               dp.setString(11,getVenCrebit());
               dp.setString(12,getCusDebit());
               dp.setString(13,getCusCrebit());
               dp.setString(14,getProDebit());
               dp.setString(15,getProCrebit());
               dp.setString(16,getDiscountDebit());
               dp.setString(17, getNonTaxCredit());
               dp.setString(18,getServiceTaxCredit());
               dp.setString(19, getCredit());
               dp.setString(20, getPurchaseNonTax());
               dp.setString(21,getPurchaseDiscount());
               dp.setString(22, getRoundOff());
    }

    /**
     * @return the salesCredit
     */
    public String getSalesCredit() {
        return salesCredit;
    }

    /**
     * @param salesCredit the salesCredit to set
     */
    public void setSalesCredit(String salesCredit) {
        this.salesCredit = salesCredit;
    }

    /**
     * @return the cashDebit
     */
    public String getCashDebit() {
        return cashDebit;
    }

    /**
     * @param cashDebit the cashDebit to set
     */
    public void setCashDebit(String cashDebit) {
        this.cashDebit = cashDebit;
    }

    /**
     * @return the cardDebit
     */
    public String getCardDebit() {
        return cardDebit;
    }

    /**
     * @param cardDebit the cardDebit to set
     */
    public void setCardDebit(String cardDebit) {
        this.cardDebit = cardDebit;
    }

    /**
     * @return the chequeDebit
     */
    public String getChequeDebit() {
        return chequeDebit;
    }

    /**
     * @param chequeDebit the chequeDebit to set
     */
    public void setChequeDebit(String chequeDebit) {
        this.chequeDebit = chequeDebit;
    }

    /**
     * @return the purDebit
     */
    public String getPurDebit() {
        return purDebit;
    }

    /**
     * @param purDebit the purDebit to set
     */
    public void setPurDebit(String purDebit) {
        this.purDebit = purDebit;
    }

    /**
     * @return the purCredit
     */
    public String getPurCredit() {
        return purCredit;
    }

    /**
     * @param purCredit the purCredit to set
     */
    public void setPurCredit(String purCredit) {
        this.purCredit = purCredit;
    }

    /**
     * @return the jourCrebit
     */
    public String getJourCrebit() {
        return jourCrebit;
    }

    /**
     * @param jourCrebit the jourCrebit to set
     */
    public void setJourCrebit(String jourCrebit) {
        this.jourCrebit = jourCrebit;
    }

    /**
     * @return the venDebit
     */
    public String getVenDebit() {
        return venDebit;
    }

    /**
     * @param venDebit the venDebit to set
     */
    public void setVenDebit(String venDebit) {
        this.venDebit = venDebit;
    }

    /**
     * @return the venCrebit
     */
    public String getVenCrebit() {
        return venCrebit;
    }

    /**
     * @param venCrebit the venCrebit to set
     */
    public void setVenCrebit(String venCrebit) {
        this.venCrebit = venCrebit;
    }

    /**
     * @return the cusDebit
     */
    public String getCusDebit() {
        return cusDebit;
    }

    /**
     * @param cusDebit the cusDebit to set
     */
    public void setCusDebit(String cusDebit) {
        this.cusDebit = cusDebit;
    }

    /**
     * @return the cusCrebit
     */
    public String getCusCrebit() {
        return cusCrebit;
    }

    /**
     * @param cusCrebit the cusCrebit to set
     */
    public void setCusCrebit(String cusCrebit) {
        this.cusCrebit = cusCrebit;
    }

    /**
     * @return the proDebit
     */
    public String getProDebit() {
        return proDebit;
    }

    /**
     * @param proDebit the proDebit to set
     */
    public void setProDebit(String proDebit) {
        this.proDebit = proDebit;
    }

    /**
     * @return the proCrebit
     */
    public String getProCrebit() {
        return proCrebit;
    }

    /**
     * @param proCrebit the proCrebit to set
     */
    public void setProCrebit(String proCrebit) {
        this.proCrebit = proCrebit;
    }

    /**
     * @return the Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return the jourDebit
     */
    public String getJourDebit() {
        return jourDebit;
    }

    /**
     * @param jourDebit the jourDebit to set
     */
    public void setJourDebit(String jourDebit) {
        this.jourDebit = jourDebit;
    }

    /**
     * @return the DiscountDebit
     */
    public String getDiscountDebit() {
        return DiscountDebit;
    }

    /**
     * @param DiscountDebit the DiscountDebit to set
     */
    public void setDiscountDebit(String DiscountDebit) {
        this.DiscountDebit = DiscountDebit;
    }

    /**
     * @return the NonTaxCredit
     */
    public String getNonTaxCredit() {
        return NonTaxCredit;
    }

    /**
     * @param NonTaxCredit the NonTaxCredit to set
     */
    public void setNonTaxCredit(String NonTaxCredit) {
        this.NonTaxCredit = NonTaxCredit;
    }

    /**
     * @return the ServiceTaxCredit
     */
    public String getServiceTaxCredit() {
        return ServiceTaxCredit;
    }

    /**
     * @param ServiceTaxCredit the ServiceTaxCredit to set
     */
    public void setServiceTaxCredit(String ServiceTaxCredit) {
        this.ServiceTaxCredit = ServiceTaxCredit;
    }

    /**
     * @return the credit
     */
    public String getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(String credit) {
        this.credit = credit;
    }

    /**
     * @return the purchaseNonTax
     */
    public String getPurchaseNonTax() {
        return purchaseNonTax;
    }

    /**
     * @param purchaseNonTax the purchaseNonTax to set
     */
    public void setPurchaseNonTax(String purchaseNonTax) {
        this.purchaseNonTax = purchaseNonTax;
    }

    /**
     * @return the purchaseDiscount
     */
    public String getPurchaseDiscount() {
        return purchaseDiscount;
    }

    /**
     * @param purchaseDiscount the purchaseDiscount to set
     */
    public void setPurchaseDiscount(String purchaseDiscount) {
        this.purchaseDiscount = purchaseDiscount;
    }

    /**
     * @return the roundOff
     */
    public String getRoundOff() {
        return roundOff;
    }

    /**
     * @param roundOff the roundOff to set
     */
    public void setRoundOff(String roundOff) {
        this.roundOff = roundOff;
    }
    
}
