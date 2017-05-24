/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author archana
 */
public class ServedTransactionInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String servedBy;
    private String servedTime;
    private int preparationStatus;


    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        setServedBy(dr.getString(2));
        setServedTime(dr.getString(3));
         setPreparationStatus(dr.getInt(4));

    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, getServedBy());
        dp.setString(3, getServedTime());
        dp.setInt(4, getPreparationStatus());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getServedTime() {
        return servedTime;
    }

    public void setServedTime(String servedTime) {
        this.servedTime = servedTime;
    }
    
        public int getPreparationStatus() {
        return preparationStatus;
    }

    public void setPreparationStatus(int preparationStatus) {
        this.preparationStatus = preparationStatus;
    }

}
