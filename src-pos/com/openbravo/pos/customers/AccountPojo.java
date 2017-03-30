/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.customers;

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
public class AccountPojo implements SerializableRead, SerializableWrite, Serializable {
    private String receivable;
    private String advance;

    @Override
    public void readValues(DataRead dr) throws BasicException {
        setReceivable(dr.getString(1));
        setAdvance(dr.getString(2));
    }
    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1,getReceivable());
        dp.setString(2,getAdvance());
        
    }

    /**
     * @return the receivable
     */
    public String getReceivable() {
        System.out.println(receivable);
        return receivable;
    }

    /**
     * @param receivable the receivable to set
     */
    public void setReceivable(String receivable) {
          System.out.println(receivable);
        this.receivable = receivable;
    }

    /**
     * @return the advance
     */
    public String getAdvance() {
        return advance;
    }

    /**
     * @param advance the advance to set
     */
    public void setAdvance(String advance) {
        this.advance = advance;
    }
    
}
