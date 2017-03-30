/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.homedelivery;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author raghevendra
 */
public class DeliveryBoyId implements SerializableRead, SerializableWrite{
    private String Id;

    /**
     * @return the Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        //To change body of generated methods, choose Tools | Templates.
        setId(dr.getString(1));
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
         //To change body of generated methods, choose Tools | Templates.
          dp.setString(1, getId());
    }
    
}
