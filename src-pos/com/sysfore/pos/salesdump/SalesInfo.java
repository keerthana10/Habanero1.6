/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.salesdump;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.Date;

/**
 *
 * @author preethi
 */
public class SalesInfo implements SerializableRead, SerializableWrite{
    private String billNo;
    private String cusName;
    private String productName;
    private double qty;
    private double price;
    private double billValue;
    private String id;
    private Date billDate;

    public void readValues(DataRead dr) throws BasicException {
       billNo = dr.getString(1);
       cusName = dr.getString(2);
       productName = dr.getString(3);
       qty = dr.getDouble(4);
       price = dr.getDouble(5);
       billValue =dr.getDouble(6);
       id = dr.getString(7);
       billDate = dr.getTimestamp(8);

    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, billNo);
        dp.setString(2, cusName);
        dp.setString(3, productName);
        dp.setDouble(4, qty);
        dp.setDouble(5, price);
        dp.setDouble(6, billValue);
        dp.setString(7, id);
        dp.setTimestamp(8, billDate);
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
    public Date getBillDate() {
        return billDate;
    }
   public String getProductName() {
       if(productName.length()>10){
        productName = "<html>"+productName+"<br /></html>";
       }
       return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
public String getCusName() {
        return cusName;
    }
    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
    public double getUnits() {
        return qty;
    }

    public void setUnits(double qty) {
        this.qty = qty;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public double getBillValue() {
        return billValue;
    }

    public void setBillValue(double billValue) {
        this.billValue = billValue;
    }
}
