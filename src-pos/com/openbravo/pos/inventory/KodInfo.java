/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.inventory;

/**
 *
 * @author mateen
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class KodInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String name;
    private String color;
    private String sequence;
     private String isLastStatus;
    /** Creates a new instance of DiscountRateinfo */
   

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        name = dr.getString(2);
        color=dr.getString(3);
        sequence=dr.getString(4);
        isLastStatus=dr.getString(5);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, name);
        dp.setString(3, color);
        dp.setString(4, sequence);
        dp.setString(5, isLastStatus);
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
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

     public void setId(String id) {
        this.id = id;
    }
     
      public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the isLastStatus
     */
    public String getIsLastStatus() {
        return isLastStatus;
    }

    /**
     * @param isLastStatus the isLastStatus to set
     */
    public void setIsLastStatus(String isLastStatus) {
        this.isLastStatus = isLastStatus;
    } 
     


}
