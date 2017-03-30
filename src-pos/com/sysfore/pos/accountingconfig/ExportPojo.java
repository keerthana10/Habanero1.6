/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.accountingconfig;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;

/**
 *
 * @author raghevendra
 */
public class ExportPojo implements SerializableRead, SerializableWrite, Serializable {
    private Double amt;
    private String paymentName;
    

    @Override
    public void readValues(DataRead dr) throws BasicException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        setPaymentName(dr.getString(1));
        setAmt(dr.getDouble(2));
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        dp.setString(1, getPaymentName());
        dp.setDouble(2, getAmt());
    }

    /**
     * @return the amt
     */
    public Double getAmt() {
        return amt;
    }

    /**
     * @param amt the amt to set
     */
    public void setAmt(Double amt) {
        this.amt = amt;
    }

    /**
     * @return the paymentName
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     * @param paymentName the paymentName to set
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     * @return the amt
     */
    
    
}
