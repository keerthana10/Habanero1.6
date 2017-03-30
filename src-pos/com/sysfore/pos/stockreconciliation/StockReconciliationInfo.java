/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.stockreconciliation;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.List;

/**
 *
 * @author preethi
 */
public class StockReconciliationInfo  {
   private long serialVersionUID = 123456789L;
    private String productId;
    private String productName;
    private double systemQty;
    private double physicalQty;
    private String remarks;
    private double variance;
    private double units;
    private double price;
    //private String productId;

    private List<StockDetailsInfo> m_aLines;

    public StockReconciliationInfo(String productName,double systemQty,double physicalQty ,double variance,String remarks,double price,String productId) {
        this.productName = productName;
        this.systemQty = systemQty;
        this.physicalQty = physicalQty;
        this.variance = variance;
        this.remarks = remarks;
        this.price = price;
        this.productId = productId;

    }

    

    /**
     * @return the id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param id the id to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

        public double getPhysicalQty() {
        return physicalQty;
    }

    public void setPhysicalQty(double physicalQty) {
        this.physicalQty = physicalQty;
    }
    public double getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(double systemQty) {
        this.systemQty = systemQty;
    }
    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }
     public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
     public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        units = (-1*getVariance());
    }
     public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
       return remarks;
    }
 public List<StockDetailsInfo> getLines() {
//        System.out.println("enrtrr---m_aLines"+this.m_aLines.size());
        return this.m_aLines;
    }

    public void setLines(List<StockDetailsInfo> value) {

        m_aLines = value;

    }
 
}
