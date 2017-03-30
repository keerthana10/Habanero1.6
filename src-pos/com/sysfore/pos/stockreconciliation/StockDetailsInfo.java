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
public class StockDetailsInfo implements SerializableRead, SerializableWrite { 
   private long serialVersionUID = 123456789L;
    private String productId;
    private String productName;
    private double units;
    private double price;
    private List<StockDetailsInfo> m_aLines;
    private String phyQty;
    private String remarks;

    public StockDetailsInfo() {
    }

    StockDetailsInfo(StockDetailsInfo stock) {
        this.productId = stock.getProductId();
        this.productName = "<html>"+stock.getProductName()+"<br /></html>";
        this.units = stock.getUnits();
        this.price = stock.getPrice();
        this.phyQty = stock.getPhyQty();
        this.remarks = stock.getRemarks();
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
 public void setPhyQty(String phyQty) {
        this.phyQty = phyQty;
    }
    public String getPhyQty() {
        return phyQty;
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
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
  public String getRemarks() {
       return remarks;
    }
public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

 public List<StockDetailsInfo> getLines() {
//        System.out.println("enrtrr---m_aLines"+this.m_aLines.size());
        return this.m_aLines;
    }

    public void setLines(List<StockDetailsInfo> value) {

        m_aLines = value;

    }
    public void readValues(DataRead dr) throws BasicException {
       productId = dr.getString(1);
       productName = dr.getString(2);
       units = dr.getDouble(3);
       price = dr.getDouble(4);

    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, productId);
        dp.setString(2, productName);
        dp.setDouble(3, units);
        dp.setDouble(4, price);

    }



}
