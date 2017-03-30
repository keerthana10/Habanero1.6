/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;
import java.util.Date;

/**
 *
 * @author ravi
 */





public class SerializerWriteTimeStamp implements SerializerWrite<Date> {

    public static final SerializerWrite INSTANCE = new SerializerWriteTimeStamp();

    /** Creates a new instance of SerializerWriteString */
    private SerializerWriteTimeStamp() {
    }



    public void writeValues(DataWrite dp, Date obj) throws BasicException {
        Datas.TIMESTAMP.setValue(dp, 1, obj);
    }
}