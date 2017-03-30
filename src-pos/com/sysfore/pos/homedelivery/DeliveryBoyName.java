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
public class DeliveryBoyName implements SerializableRead, SerializableWrite{
    private String Id;
    public String name;
   
    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
         setName(dr.getString(2));
    }
     public void writeValues(DataWrite dp) throws BasicException {
         dp.setString(1,getId());
         dp.setString(2, getName());
     }
    
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the Id
     */
    public String getId() {
        System.out.println("getid should be executed");
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        System.out.println("setid should be executed");
        this.Id = Id;
    }
    
}
