/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.creditsale;

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
public class CustomerPojo implements SerializableRead, SerializableWrite{
     private String id;
   private String name;

    /**
     * @return the id
     */
    public void readValues(DataRead dr) throws BasicException {
        setId(dr.getString(1));
        setName(dr.getString(2));
        
   
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, getId());
        dp.setString(2, getName());
        

    }
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
    


}