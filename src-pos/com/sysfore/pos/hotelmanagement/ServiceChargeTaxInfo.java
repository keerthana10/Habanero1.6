/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.hotelmanagement;

/**
 *
 * @author mateen
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class ServiceChargeTaxInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
   
    private double serviceChargerate;
    private double serviceChargeAmt;
    private double serviceTaxrate;
    private double serviceTaxAmt;
    /** Creates a new instance of DiscountRateinfo */
    public ServiceChargeTaxInfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        serviceChargerate = dr.getDouble(1);
        serviceChargeAmt = dr.getDouble(2);
        serviceTaxrate = dr.getDouble(3);
        serviceTaxAmt = dr.getDouble(4);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setDouble(1, serviceChargerate);
        dp.setDouble(2, serviceChargeAmt);
        dp.setDouble(3, serviceTaxrate);
         dp.setDouble(4, serviceTaxAmt);
        
    }

    public double getServiceChargeAmt() {
        return serviceChargeAmt;
    }

    public void setServiceChargeAmt(double serviceChargeAmt) {
        this.serviceChargeAmt = serviceChargeAmt;
    }

    public double getServiceChargerate() {
        return serviceChargerate;
    }

    public void setServiceChargerate(double serviceChargerate) {
        this.serviceChargerate = serviceChargerate;
    }

    public double getServiceTaxAmt() {
        return serviceTaxAmt;
    }

    public void setServiceTaxAmt(double serviceTaxAmt) {
        this.serviceTaxAmt = serviceTaxAmt;
    }

    public double getServiceTaxrate() {
        return serviceTaxrate;
    }

    public void setServiceTaxrate(double serviceTaxrate) {
        this.serviceTaxrate = serviceTaxrate;
    }

   
}
