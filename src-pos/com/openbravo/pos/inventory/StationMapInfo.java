/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author archana
 */
public class StationMapInfo implements SerializableRead, SerializableWrite {
    
     private String id;
     private String roleId;
     private String userId;
     private String station;

    
     public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
       roleId= dr.getString(2);
       userId= dr.getString(3);
       station=dr.getString(4);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, roleId);
        dp.setString(3, userId);
        dp.setString(4, station);
    }

    /**
     * @return the prArea
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param prArea the prArea to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
    
}
