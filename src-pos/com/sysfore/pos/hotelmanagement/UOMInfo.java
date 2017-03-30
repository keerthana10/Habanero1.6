/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.hotelmanagement;

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
public class UOMInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String name;
    private String code;   
    /** Creates a new instance of DiscountRateinfo */
    public UOMInfo() {
    }
    
    

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        setName(dr.getString(2));
        setCode(dr.getString(3));
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, getName());
        dp.setString(3, getCode());
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
    
    
     public String getCode() {
        return code;
    }

    
    public void setCode(String code) {
        this.code = code;
    }


}
