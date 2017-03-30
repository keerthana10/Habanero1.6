/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.purchaseorder;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

public class PurchasePojo implements SerializableRead, SerializableWrite {
    private String Documentno;
    private String status;
    private String id;

    /**
     * @return the Documentno
     */
    public String getDocumentno() {
        System.out.println(Documentno);
        return Documentno;
    }

    /**
     * @param Documentno the Documentno to set
     */
    public void setDocumentno(String Documentno) {
        this.Documentno = Documentno;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
     setDocumentno(dr.getString(1));
      setId(dr.getString(2));
        setStatus(dr.getString(3)); 
       
    }
    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1,getDocumentno());
        dp.setString(2, getId());
         dp.setString(3,getStatus()); 
}

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}