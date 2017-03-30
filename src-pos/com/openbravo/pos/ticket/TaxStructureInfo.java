/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

/**
 *
 * @author shilpa
 */
public class TaxStructureInfo {
   private String calculationType;
     private double taxRate;
    private double taxValue;
    private boolean calculated;
    
    
   public  TaxStructureInfo(String calculationType, double taxRate,double taxValue,boolean calculated){
       this.calculationType=calculationType;
         this.taxRate=taxRate;
        this.taxValue=taxValue;
        this.calculated=calculated;
    }

    /**
     * @return the taxId
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxId the taxId to set
     */
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return the calculationType
     */
    public String getCalculationType() {
        return calculationType;
    }

    /**
     * @param calculationType the calculationType to set
     */
    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
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

    /**
     * @return the calculated
     */
    public boolean isCalculated() {
        return calculated;
    }

    /**
     * @param calculated the calculated to set
     */
    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }
    
    
}
