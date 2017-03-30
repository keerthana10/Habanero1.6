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
public class VendorInfo implements SerializableRead, SerializableWrite{

    private static final long serialVersionUID = 76406123456837719L;

    private String id;
    private String searchkey;
    private String taxid;
    private String name;
    
    private String address1;
    private String address2;
    
    private boolean isAddress1BillAddress;
    private boolean isAddress2ShipAddress;
    
    private String billaddress;
    private String shipaddress;

    public VendorInfo(){
        
    }


    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
        setSearchkey(dr.getString(2));
        setTaxid(dr.getString(3));
        setName(dr.getString(4));
        
        setAddress1(dr.getString(5));
        setAddress2(dr.getString(6));
        
        setIsAddress1BillAddress(dr.getBoolean(7).booleanValue());
        setIsAddress2ShipAddress(dr.getBoolean(8).booleanValue());
        
        setBilladdress(dr.getString(9));
        setShipaddress(dr.getString(10));
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, getId());
        dp.setString(2, getSearchkey());
        dp.setString(3, getTaxid());
        dp.setString(4, getName());
        
        dp.setString(5, getAddress1());
        dp.setString(6, getAddress2());
        
        dp.setBoolean(7, isAddress1BillAddress);
        dp.setBoolean(8, isAddress2ShipAddress);
        
        dp.setString(9, getBilladdress());
        dp.setString(10, getShipaddress());
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
     * @return the searchkey
     */
    public String getSearchkey() {
        return searchkey;
    }

    /**
     * @param searchkey the searchkey to set
     */
    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    /**
     * @return the taxid
     */
    public String getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(String taxid) {
        this.taxid = taxid;
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
     * @return the billaddress
     */
    public String getBilladdress() {
        return billaddress;
    }

    /**
     * @param billaddress the billaddress to set
     */
    public void setBilladdress(String billaddress) {
        this.billaddress = billaddress;
    }

    /**
     * @return the shipaddress
     */
    public String getShipaddress() {
        return shipaddress;
    }

    /**
     * @param shipaddress the shipaddress to set
     */
    public void setShipaddress(String shipaddress) {
        this.shipaddress = shipaddress;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the isAddress1BillAddress
     */
    public boolean isIsAddress1BillAddress() {
        return isAddress1BillAddress;
    }

    /**
     * @param isAddress1BillAddress the isAddress1BillAddress to set
     */
    public void setIsAddress1BillAddress(boolean isAddress1BillAddress) {
        this.isAddress1BillAddress = isAddress1BillAddress;
    }

    /**
     * @return the isAddress2ShipAddress
     */
    public boolean isIsAddress2ShipAddress() {
        return isAddress2ShipAddress;
    }

    /**
     * @param isAddress2ShipAddress the isAddress2ShipAddress to set
     */
    public void setIsAddress2ShipAddress(boolean isAddress2ShipAddress) {
        this.isAddress2ShipAddress = isAddress2ShipAddress;
    }
}
