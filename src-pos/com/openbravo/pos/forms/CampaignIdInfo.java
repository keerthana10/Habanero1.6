/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author preethi
 */
public class CampaignIdInfo implements SerializableRead, SerializableWrite {

    private String campaignId;
    private String startTime;
    private String endTime;

  public CampaignIdInfo() {
    }

  public void readValues(DataRead dr) throws BasicException {
       campaignId = dr.getString(1);
       startTime=dr.getString(2);
        endTime=dr.getString(3);
        

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, campaignId);    
        dp.setString(2, startTime);
        dp.setString(3, endTime);
    }

    public String getcampaignId(){
        return campaignId;
    }
    public void setcampaignId(String campaignId){
        this.campaignId = campaignId;
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