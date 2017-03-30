/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.goodsreceipt;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.Date;

/**
 *
 * @author mateen
 */
public class StockLineInfo implements SerializableRead, SerializableWrite{
    
    
    private long serialVersionUID = 223456789L;
    
    private String id;
    private Date date;
    private String reason;
    private String location;
    private String product;
    private String attid;
    private double aqty;
    private double pqty;
    private double rqty;
    private double price;
    private String poid;
    private String locationName;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the attid
     */
    public String getAttid() {
        return attid;
    }

    /**
     * @param attid the attid to set
     */
    public void setAttid(String attid) {
        this.attid = attid;
    }

    /**
     * @return the aqty
     */
    public Double getAqty() {
        return aqty;
    }

    /**
     * @param aqty the aqty to set
     */
    public void setAqty(double aqty) {
        this.aqty = aqty;
    }

    /**
     * @return the pqty
     */
    public double getPqty() {
        return pqty;
    }

    /**
     * @param pqty the pqty to set
     */
    public void setPqty(double pqty) {
        this.pqty = pqty;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the poid
     */
    public String getPoid() {
        return poid;
    }

    /**
     * @param poid the poid to set
     */
    public void setPoid(String poid) {
        this.poid = poid;
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        date = dr.getTimestamp(2);
        reason = dr.getString(3);
        location = dr.getString(4);
        product = dr.getString(5);
        attid = dr.getString(6);
        aqty = dr.getDouble(7);
        pqty = dr.getDouble(8);
        rqty = dr.getDouble(9);
        price = dr.getDouble(10);
        poid = dr.getString(11);
        locationName = dr.getString(12);
        
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setTimestamp(2, date);
        dp.setString(3, reason);
        dp.setString(4, location);
        dp.setString(5, product);
        dp.setString(6, attid);
        dp.setDouble(7, aqty);
        dp.setDouble(8, pqty);
        dp.setDouble(9, rqty);
        dp.setDouble(10, price);
        dp.setString(11, poid);
        dp.setString(12, locationName);
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the rqty
     */
    public double getRqty() {
        return rqty;
    }

    /**
     * @param rqty the rqty to set
     */
    public void setRqty(double rqty) {
        this.rqty = rqty;
    }
    
}
