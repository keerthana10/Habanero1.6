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
public class BusinessServiceChargeInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
       private double rate;
       private String isTaxincluded;


    /** Creates a new instance of DiscountRateinfo */
    public BusinessServiceChargeInfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
       rate = (dr.getDouble(2));
       isTaxincluded = dr.getString(3);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setDouble(2, rate);
          dp.setString(3, isTaxincluded);
    }

    public String getId() {
        return id;
    }

  public String getIsTaxincluded() {
        return isTaxincluded;
    }

    public void setIsTaxincluded(String isTaxincluded) {
        this.isTaxincluded = isTaxincluded;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }



}
