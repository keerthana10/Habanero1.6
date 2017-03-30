/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import java.io.Serializable;

/**
 *
 * @author sysfore17
 */
public class DiscountInfo implements Serializable{
    private String discountRate;
    private String discountValue;
    private String discountId;
    
    DiscountInfo(String id,String val,String name){
       discountRate=id;
       discountValue=val;
       discountId=name;
    }
    

    /**
     * @return the discountId
     */
    public String getDiscountRate() {
        return discountRate;
    }

    /**
     * @param discountId the discountId to set
     */
    public void setDiscountRate(String discount) {
        this.discountRate = discount;
    }

    /**
     * @return the discountValue
     */
    public String getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the discountName
     */
    public String getDiscountId() {
        return discountId;
    }

    /**
     * @param discountName the discountName to set
     */
    public void setDiscountId(String discount) {
        this.discountId = discount;
    }
    
}
