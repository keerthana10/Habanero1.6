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
 * @author shilpa
 */
public class ExportValPojo implements SerializableRead, SerializableWrite, Serializable {
   private Double payment;
    private String paymenttype;
   
  

    @Override
    public void readValues(DataRead dr) throws BasicException {
       setPayment(dr.getDouble(1));
      setPaymenttype(dr.getString(2));
    
      
      
      
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
     dp.setDouble(1,getPayment());
    dp.setString(2, getPaymenttype());
    
    
    }

    /**
     * @return the taxsales
     */
   
    /**
     * @param taxsales the taxsales to set
     */
    

    /**
     * @return the nontaxsales
     */
   
    /**
     * @param nontaxsales the nontaxsales to set
     */
   
    /**
     * @return the paymenttype
     */
    public String getPaymenttype() {
        
        return paymenttype;
    }

    /**
     * @param paymenttype the paymenttype to set
     */
    public void setPaymenttype(String paymenttype) {
       
        this.paymenttype = paymenttype;
    }

   
    public Double getPayment() {
        return payment;
    }

    
    public void setPayment(Double payment) {
        
        this.payment = payment;
    }

   
   
}
