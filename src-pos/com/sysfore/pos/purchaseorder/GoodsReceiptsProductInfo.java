/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.purchaseorder;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author mateen
 */
public class GoodsReceiptsProductInfo implements SerializableRead, SerializableWrite {

    private long serialVersionUID = 14253678343434L;
    private String id;
    private String poid;
    private String productid;
    private double quantity;
    private String productname;
    private double price;
    private String taxid;
    private String taxCatId;
    private String uom;

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public GoodsReceiptsProductInfo() {
    }

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

    /**
     * @return the productid
     */
    public String getProductid() {
        return productid;
    }

    /**
     * @param productid the productid to set
     */
    public void setProductid(String productid) {
        this.productid = productid;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the productname
     */
    public String getProductname() {
        return productname;
    }

    /**
     * @param productname the productname to set
     */
    public void setProductname(String productname) {
        this.productname = productname;
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
     * @return the taxid
     */
    public String getTaxid() {
        return taxid;
    }
     public String getTaxCatId() {
        return taxCatId;
    }

    /**
     * @param taxCatId the taxCatId to set
     */
    public void setTaxCatId(String taxCatId) {
        this.taxCatId = taxCatId;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public void readValues(DataRead dr) throws BasicException {

        id = dr.getString(1);
        poid = dr.getString(2);
        productid = dr.getString(3);
        quantity = dr.getDouble(4);
        productname = dr.getString(5);
        price = dr.getDouble(6);
        taxid = dr.getString(7);
        taxCatId = dr.getString(8);
        uom = dr.getString(9);

    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, poid);
        dp.setString(3, productid);
        dp.setDouble(4, quantity);
        dp.setString(5, productname);
        dp.setDouble(6, price);
        dp.setString(7, taxid);
        dp.setString(8, taxCatId);
        dp.setString(9, uom);
    }

    /**
     * @return the taxCatId
     */
   
}
