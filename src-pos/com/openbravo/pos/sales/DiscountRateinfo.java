/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

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
public class DiscountRateinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String rate;
    private String name;
    /** Creates a new instance of DiscountRateinfo */
    public DiscountRateinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        rate = dr.getString(2);
        setName(dr.getString(3));
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, rate);
        dp.setString(3, getName());
    }

    public String getId() {
        return id;
    }

    public String getRate() {
        return rate;
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


}
