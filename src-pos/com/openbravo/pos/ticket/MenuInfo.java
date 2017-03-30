/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author sysfore17
 */
public class MenuInfo  implements SerializableRead, SerializableWrite {
private String id;
private String startTime;
private String endTime;

   

    /**
     * @return the m_sID
     */
    public String getId() {
        return id;
    }

    /**
     * @param m_sID the m_sID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return getStartTime();
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.setStartTime(time);
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        startTime=dr.getString(2);
        endTime=dr.getString(3);
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, startTime); 
        dp.setString(3, endTime);
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
}
