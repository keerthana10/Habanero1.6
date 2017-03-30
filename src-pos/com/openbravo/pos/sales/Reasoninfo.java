/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

/**
 *
 * @author preethi
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
public class Reasoninfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;

    private String reason;
    private String status;
    /** Creates a new instance of DiscountRateinfo */
    public Reasoninfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        setReason(dr.getString(2));
          setStatus(dr.getString(3));
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, getReason());
        dp.setString(3, getStatus());
    }

    public String getId() {
        return id;
    }


    /**
     * @return the name
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param name the name to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
     public String getStatus() {
        return status;
    }

    /**
     * @param name the name to set
     */
    public void setStatus(String status) {
        this.status = status ;
    }


}
