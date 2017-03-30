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
 * @author shilpa
 */
public class ProductionAreaInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String searchkey;
    private String name;
    private String restaurent;
    private String pareatype;
    private String description;
   

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        searchkey=dr.getString(2);
        name=dr.getString(3);
        restaurent=dr.getString(4);
        pareatype=dr.getString(5);
        description=dr.getString(6);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, searchkey);
        dp.setString(3, name);
        dp.setString(4, restaurent);
        dp.setString(5, pareatype);
        dp.setString(6, description);
    }

    public String getId() {
        return id;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the searchkey
     */
    public String getSearchkey() {
        return searchkey;
    }

    /**
     * @param searchkey the searchkey to set
     */
    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the restaurent
     */
    public String getRestaurent() {
        return restaurent;
    }

    /**
     * @param restaurent the restaurent to set
     */
    public void setRestaurent(String restaurent) {
        this.restaurent = restaurent;
    }

    /**
     * @return the pareatype
     */
    public String getPareatype() {
        return pareatype;
    }

    /**
     * @param pareatype the pareatype to set
     */
    public void setPareatype(String pareatype) {
        this.pareatype = pareatype;
    }



}
