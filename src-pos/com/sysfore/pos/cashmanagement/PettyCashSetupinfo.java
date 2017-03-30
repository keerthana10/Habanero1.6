/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.cashmanagement;

/**
 *
 * @author mateen
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author adrianromero
 */
public class PettyCashSetupinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private Double pettycash;
    private Date created;
    private String resetPeriod;
    private String resetDate;
    private String resetDay;

    /** Creates a new instance of DiscountRateinfo */
    public PettyCashSetupinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        pettycash = dr.getDouble(1);
        resetPeriod = dr.getString(2);
        resetDate = dr.getString(3);
        resetDay = dr.getString(4);


    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setDouble(1, pettycash);
        dp.setString(2, resetPeriod);
        dp.setString(3, resetDate);
        dp.setString(4, resetDay);


    }

     public Double getPettyCash() {
        return pettycash;
    }
     public void setPettyCash(Double pettycash) {
        this.pettycash = pettycash;
    }
   
  
    public String getResetPeriod() {
        return resetPeriod==null?null:resetPeriod;
    }
    public void setResetPeriod(String resetPeriod) {
        this.resetPeriod = resetPeriod;
    }
    public String getResetDate() {
        return resetDate==null?null:resetDate;
    }
    public void setResetDate(String resetDate) {
        this.resetDate = resetDate;
    }
    public String getResetDay(){
        return resetDay==null?null:resetDay;
    }
    public void setResetDay(String resetDay) {
        this.resetDay = resetDay;
    }


}
