/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.stockreconciliation;

import java.util.List;

/**
 *
 * @author preethi
 */
public class StockLinesInfo {
    private String productId;
    private String productName;
    private double units;
    private List<StockDetailsInfo> m_aLines;

    public StockLinesInfo() {
    }

    public StockLinesInfo(String productName, double units) {
        this.productName = productName;
        this.units =units;
   //     throw new UnsupportedOperationException("Not yet implemented");
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

        public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }


 public List<StockDetailsInfo> getLines() {
//        System.out.println("enrtrr---m_aLines"+this.m_aLines.size());
        return this.m_aLines;
    }

    public void setLines(List<StockDetailsInfo> value) {

        m_aLines = value;

    }
   

}
