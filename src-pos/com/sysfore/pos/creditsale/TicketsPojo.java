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

/**
 *
 * @author shilpa
 */
public class TicketsPojo implements SerializableRead, SerializableWrite{
    private String DocumentNo;

    @Override
    public void readValues(DataRead dr) throws BasicException {
        setDocumentNo(dr.getString(1));
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
       dp.setString(1, getDocumentNo());
    }

    /**
     * @return the DocumentNo
     */
    public String getDocumentNo() {
        return DocumentNo;
    }

    /**
     * @param DocumentNo the DocumentNo to set
     */
    public void setDocumentNo(String DocumentNo) {
        this.DocumentNo = DocumentNo;
    }
    
}
