
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
import com.openbravo.format.Formats;
import java.util.List;

/**
 *
 * @author preethi
 */
public class StockDocumentLinesInfo implements SerializableRead, SerializableWrite {
   private long serialVersionUID = 123456789L;
    private String productId;
    private String productName;
    private double systemQty;
    private double physicalQty;
    private double variance;
    private String remarks;
    private double price;

    public StockDocumentLinesInfo() {
    }

//  public  StockDocumentLinesInfo(List<StockDocumentLinesInfo> i) {
//      productId = i.get(0).getProductId();
//      productName = i.get(0).getProductName();
//      systemQty = i.get(0).getSystemQty();
//      physicalQty = i.get(0).getPhysicalQty();
//      variance = i.get(0).getVariance();
//      price = i.get(0).getPrice();
//      remarks = i.get(0).getRemarks();
//    }

   public StockDocumentLinesInfo(StockDocumentLinesInfo stock) {
        productId = stock.getProductId();
      productName = "<html>"+stock.getProductName()+"<br /></html>";
      systemQty = stock.getSystemQty();
      physicalQty = stock.getPhysicalQty();
      variance = stock.getVariance();
      price = stock.getPrice();
      remarks = stock.getRemarks();
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

    public double getSystemQty() {
        return systemQty;
    }
 public String printName() {
        return productName;
    }
 public String printSystemQty() {
        return Formats.DOUBLE.formatValue(new Double(systemQty));
    }
  public String printPhysicalQty() {
        return Formats.DOUBLE.formatValue(new Double(physicalQty));
    }
  public String printVariance() {
        return Formats.DOUBLE.formatValue(new Double(variance));
    }
    public void setSystemQty(double systemQty) {
        this.systemQty = systemQty;
    }
    public double getPhysicalQty() {
        return physicalQty;
    }

    public void setPhysicalQty(double physicalQty) {
        this.physicalQty = physicalQty;
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
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public void readValues(DataRead dr) throws BasicException {
       productId = dr.getString(1);
       productName = dr.getString(2);
       systemQty = dr.getDouble(3);
       physicalQty= dr.getDouble(4);
       variance= dr.getDouble(5);
       remarks= dr.getString(6);
       price =  dr.getDouble(7);
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, productId);
        dp.setString(2, productName);
        dp.setDouble(3, systemQty);
        dp.setDouble(4, physicalQty);
        dp.setDouble(5, variance);
        dp.setString(6, remarks);
        dp.setDouble(7, price);
    }


}
