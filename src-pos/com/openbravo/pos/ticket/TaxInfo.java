//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
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

package com.openbravo.pos.ticket;

import java.io.Serializable;
import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author adrianromero
 */
public class TaxInfo implements Serializable, IKeyed {
    
    private static final long serialVersionUID = -2705212098856473043L;
    private String id;
    private String name;
    private String taxcategoryid;
    private String taxcustcategoryid;
    private String parentid;
    
    private double rate;
    private boolean cascade;
    private Integer order;
    private String isSalesTax;
    private String isPurchaseTax;
     private String isServiceTax;
    private String debitAccount;
    private String creditAccount;
    private String isServiceCharge;
   // implementing erp tax calculation
    private String baseAmt;
    private String taxBaseId;
    private String takeAway;
    
    /** Creates new TaxInfo */
    public TaxInfo(String id, String name, String taxcategoryid, String taxcustcategoryid, String parentid, double rate, boolean cascade, Integer order, String isSalesTax,String isPurchaseTax,String isServiceTax,String debitAccount,String creditAccount, String isServiceCharge,String baseAmt,String taxbaseId,String takeAway) {
        this.id = id;
        this.name = name;
        this.taxcategoryid = taxcategoryid;
        this.taxcustcategoryid = taxcustcategoryid;
        this.parentid = parentid;
        
        this.rate = rate;
        this.cascade = cascade;
        this.order = order;
        this.isSalesTax = isSalesTax;
        this.isPurchaseTax = isPurchaseTax;
        this.isServiceTax = isServiceTax;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.isServiceCharge = isServiceCharge;
        this.baseAmt=baseAmt;
        this.taxBaseId=taxbaseId;
        this.takeAway= takeAway;        
    }
     
             
    
    public Object getKey() {
        return id;
    }
    
    public void setID(String value) {
        id = value;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
      // Splitting the name vat 
//        String[] taxName = name.split("_");
//        name=taxName[0];
        return name;
    }
    
   public String getSplitName() {
      // Splitting the name vat 
        String[] taxName = name.split("_");
       return  taxName[0];
    }
    
    public void setName(String value) {
        name = value;
    }

    public String getTaxCategoryID() {
        return taxcategoryid;
    }
    
    public void setTaxCategoryID(String value) {
        taxcategoryid = value;
    }

    public String getTaxCustCategoryID() {
        return taxcustcategoryid;
    }
    
    public void setTaxCustCategoryID(String value) {
        taxcustcategoryid = value;
    }    

    public String getParentID() {
        return parentid;
    }
    
    public void setParentID(String value) {
        parentid = value;
    }
    public String getIsSalesTax() {
        return isSalesTax;
    }

    public void setIsSalesTax(String isSalestax) {
        this.isSalesTax = isSalestax;
    }
    public String getIsPurchaseTax() {
        return isPurchaseTax;
    }

    public void setIsPurchaseTax(String isPurchaseTax) {
        this.isPurchaseTax = isPurchaseTax;
    }
      public String getIsServiceTax() {
        return isServiceTax;
    }

    public void setIsServiceTax(String isServiceTax) {
        this.isServiceTax = isServiceTax;
    }
        public String getIsServiceCharge() {
        return isServiceCharge;
    }

    public void setIsServiceCharge(String isServiceCharge) {
        this.isServiceCharge = isServiceCharge;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }
      public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public double getRate() {
        return rate;
    }
    
    public void setRate(double value) {
        rate = value;
    }

    public boolean isCascade() {
        return cascade;
    }
    
    public void setCascade(boolean value) {
        cascade = value;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public Integer getApplicationOrder() {
        return order == null ? Integer.MAX_VALUE : order.intValue();
    }    
    
    public void setOrder(Integer value) {
        order = value;
    }
    
    @Override
    public String toString(){
        return name;
    }

    /**
     * @return the baseAmt
     */
    public String getBaseAmt() {
        return baseAmt;
    }

    /**
     * @param baseAmt the baseAmt to set
     */
    public void setBaseAmt(String baseAmt) {
        this.baseAmt = baseAmt;
    }

    /**
     * @return the taxBaseId
     */
    public String getTaxBaseId() {
        return taxBaseId;
    }

    /**
     * @param taxBaseId the taxBaseId to set
     */
    public void setTaxBaseId(String taxBaseId) {
        this.taxBaseId = taxBaseId;
    }
    
   public String getIsSwachBharatTax() {
        return "";
    }

    public void setIsSwachBharatTax(String isSwachBharatTax) {
      //  this.isSwachBharatTax = isSwachBharatTax;
    }

    /**
     * @return the takeAway
     */
    public String getTakeAway() {
        return takeAway;
    }

    /**
     * @param takeAway the takeAway to set
     */
    public void setTakeAway(String takeAway) {
        this.takeAway = takeAway;
    }
}
