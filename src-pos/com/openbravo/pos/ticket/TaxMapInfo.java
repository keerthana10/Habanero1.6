/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import com.openbravo.format.Formats;
import java.io.Serializable;

/**
 *
 * @author shilpa
 */
public class TaxMapInfo implements Serializable {
     private String name;
     private double taxValue;
     
     
      public TaxMapInfo(String name,double taxValue){
         this.name=name;
         this.taxValue=taxValue;
     }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the taxValue
     */
    public double getTaxValue() {
        return taxValue;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

     public String printTaxValue() {
        return  Formats.DoubleValue.formatValue(taxValue);
      //return taxValue;
    }
    
    
}
