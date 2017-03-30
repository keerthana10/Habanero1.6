/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

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
public class DiscountReasonInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String reason;
    /** Creates a new instance of DiscountRateinfo */
    public DiscountReasonInfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        reason = dr.getString(2);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, reason);
       
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    /**
     * @return the name
     */
  
    /**
     * @param name the name to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


}
