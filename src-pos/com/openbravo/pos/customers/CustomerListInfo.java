/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.customers;

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
public class CustomerListInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 1234433837719L;
    private String id;
    private String searchkey;
    private String taxid;
    private String name;
    private String taxcustomerid;
    private String card;
    private Double maxdebt;
    private String address;
    private String address2;
    private String postal;
    private String city;
    private String region;
    private String country;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String phone2;
    private String fax;
    private String notes;
    private boolean visible;
    private Date curdate;
    private Double curdebt;
private String receivable;
    private String advance;

    @Override
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        searchkey = dr.getString(2);
        taxid = dr.getString(3);
        name = dr.getString(4);
        taxcustomerid = dr.getString(5);
        card = dr.getString(6);
        maxdebt = dr.getDouble(7);
        address = dr.getString(8);
        address2 = dr.getString(9);
        postal = dr.getString(10);
        city = dr.getString(11);
        region = dr.getString(12);
        country = dr.getString(13);
        firstname = dr.getString(14);
        lastname = dr.getString(15);
        email = dr.getString(16);
        phone = dr.getString(17);
        phone2 = dr.getString(18);
        fax = dr.getString(19);
        notes = dr.getString(20);
        visible = dr.getBoolean(21);
        curdate = dr.getTimestamp(22);
        curdebt = dr.getDouble(23);
         setReceivable(dr.getString(24));
        setAdvance(dr.getString(25));


    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, searchkey);
        dp.setString(3, taxid);
        dp.setString(4, name);
        dp.setString(5, taxcustomerid);
        dp.setString(6, card);
        dp.setDouble(7, maxdebt);
        dp.setString(8, address);
        dp.setString(9, address2);
        dp.setString(10, postal);
        dp.setString(11, city);
        dp.setString(12, region);
        dp.setString(13, country);
        dp.setString(14, firstname);
        dp.setString(15, lastname);
        dp.setString(16, email);
        dp.setString(17, phone);
        dp.setString(18, phone2);
        dp.setString(19, fax);
        dp.setString(20, notes);
        dp.setBoolean(21, visible);
        dp.setTimestamp(22, curdate);
        dp.setDouble(23, curdebt);
         dp.setString(23,getReceivable());
        dp.setString(24, getAdvance());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchkey() {
        return searchkey;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxcustomerid() {
        return taxcustomerid;
    }

    public void setTaxcustomerid(String taxcustomerid) {
        this.taxcustomerid = taxcustomerid;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Double getMaxdebt() {
        return maxdebt;
    }

    public void setMaxdebt(Double maxdebt) {
        this.maxdebt = maxdebt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Date getCurdate() {
        return curdate;
    }

    public void setCurdate(Date curdate) {
        this.curdate = curdate;
    }

    public Double getCurdebt() {
        return curdebt;
    }

    public void setCurdebt(Double curdebt) {
        this.curdebt = curdebt;
    }
    /**
     * @return the receivable
     */
    public String getReceivable() {
        return receivable;
    }

    /**
     * @param receivable the receivable to set
     */
    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    /**
     * @return the advance
     */
    public String getAdvance() {
        return advance;
    }

    /**
     * @param advance the advance to set
     */
    public void setAdvance(String advance) {
        this.advance = advance;
    }
}
