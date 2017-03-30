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
public class SectionMappingInfo implements SerializableRead, SerializableWrite {

    private static long serialVersionUID = 7640633837719L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    private String id;
    private String section;
    private String pareatype;
    private String parea;
   private String kitchendisplay;
   private String floor;
   

    public void readValues(DataRead dr) throws BasicException {
        id=dr.getString(1);
        section=dr.getString(2);
        pareatype=dr.getString(3);
        parea=dr.getString(4);
        kitchendisplay=dr.getString(5);
        floor=dr.getString(6);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, section);
        dp.setString(3, pareatype);
        dp.setString(4, parea);
        dp.setString(5, kitchendisplay);
        dp.setString(6, floor);
    }

    public String getId() {
        return id;
    }


    
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
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

    /**
     * @return the parea
     */
    public String getParea() {
        return parea;
    }

    /**
     * @param parea the parea to set
     */
    public void setParea(String parea) {
        this.parea = parea;
    }

    /**
     * @return the kitchendisplay
     */
    public String getKitchendisplay() {
        return kitchendisplay;
    }

    /**
     * @param kitchendisplay the kitchendisplay to set
     */
    public void setKitchendisplay(String kitchendisplay) {
        this.kitchendisplay = kitchendisplay;
    }

    /**
     * @return the floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * @param floor the floor to set
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }




}