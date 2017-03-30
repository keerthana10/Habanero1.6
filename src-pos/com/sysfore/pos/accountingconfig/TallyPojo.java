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
public class TallyPojo implements SerializableRead, SerializableWrite, Serializable {
    private int remoteid;
    private int vchkey;
    private String voucherkey;

    @Override
    public void readValues(DataRead dr) throws BasicException {
      setRemoteid(dr.getInt(1)) ;
      setVchkey(dr.getInt(2));
      setVoucherkey(dr.getString(3));
              
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
       dp.setInt(1, getRemoteid());
       dp.setInt(2, getVchkey());
       dp.setString(3, getVoucherkey());
}

    /**
     * @return the remoteid
     */
    public int getRemoteid() {
        return remoteid;
    }

    /**
     * @param remoteid the remoteid to set
     */
    public void setRemoteid(int remoteid) {
        this.remoteid = remoteid;
    }

    /**
     * @return the vchkey
     */
    public int getVchkey() {
        return vchkey;
    }

    /**
     * @param vchkey the vchkey to set
     */
    public void setVchkey(int vchkey) {
        this.vchkey = vchkey;
    }

    /**
     * @return the voucherkey
     */
    public String getVoucherkey() {
        return voucherkey;
    }

    /**
     * @param voucherkey the voucherkey to set
     */
    public void setVoucherkey(String voucherkey) {
        this.voucherkey = voucherkey;
    }
}