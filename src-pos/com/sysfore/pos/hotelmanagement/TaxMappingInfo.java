/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.hotelmanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateen
 */
public class TaxMappingInfo implements SerializableRead, SerializableWrite{

    private long serialVersionUID = 123546456837719L;
    private String id;
    private String businessTypeId;
    private String isServiceTax;
    private String isServiceTaxIncluded;
    private String isServiceCharge;
    private String isServiceChargeIncluded;
    private String businessType;
  

    public TaxMappingInfo() {
    }

    public String getId() {
        return id;
    }

  
    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getIsServiceCharge() {
        return isServiceCharge;
    }

    public void setIsServiceCharge(String isServiceCharge) {
        this.isServiceCharge = isServiceCharge;
    }

    public String getIsServiceChargeIncluded() {
        return isServiceChargeIncluded;
    }

    public void setIsServiceChargeIncluded(String isServiceChargeIncluded) {
        this.isServiceChargeIncluded = isServiceChargeIncluded;
    }

    public String getIsServiceTax() {
        return isServiceTax;
    }

    public void setIsServiceTax(String isServiceTax) {
        this.isServiceTax = isServiceTax;
    }

    public String getIsServiceTaxIncluded() {
        return isServiceTaxIncluded;
    }

    public void setIsServiceTaxIncluded(String isServiceTaxIncluded) {
        this.isServiceTaxIncluded = isServiceTaxIncluded;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

  

    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
        setBusinessTypeId(dr.getString(2));
        setIsServiceTaxIncluded(dr.getString(3));
        setIsServiceChargeIncluded(dr.getString(4));
        setBusinessType(dr.getString(5));
       
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, getId());
        dp.setString(2, getBusinessTypeId());
        dp.setString(3, getIsServiceTaxIncluded());
        dp.setString(4, getIsServiceChargeIncluded());
        dp.setString(5, getBusinessType());
     
    }

}