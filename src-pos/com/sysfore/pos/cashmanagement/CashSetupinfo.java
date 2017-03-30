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
public class CashSetupinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private Double floatCash;
    private Double pettycash;
    private String autofloat;
    private Date created;
    private String resetPeriod;
    private String resetDate;
    private String resetDay;
   
    /** Creates a new instance of DiscountRateinfo */
    public CashSetupinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        floatCash = dr.getDouble(1);
        pettycash = dr.getDouble(2);
        autofloat = dr.getString(3);
        created = dr.getTimestamp(4);
        resetPeriod = dr.getString(5);
        resetDate = dr.getString(6);
        resetDay = dr.getString(7);

        
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setDouble(1, floatCash);
        dp.setDouble(2, pettycash);
        dp.setString(3, autofloat);
        dp.setTimestamp(4, created);
        dp.setString(5, resetPeriod);
        dp.setString(6, resetDate);
        dp.setString(7, resetDay);

     
    }
    public Double getFloatCash() {
        return floatCash;
    }
     public void setFloatCash(Double floatCash) {
        this.floatCash = floatCash;
    }
     public Double getPettyCash() {
        return pettycash;
    }
     public void setPettyCash(Double pettycash) {
        this.pettycash = pettycash;
    }
     public String getAutoFloat() {
        return autofloat==null?null:autofloat;
    }
     public void setAutoFloat(String autofloat) {
        this.autofloat = autofloat;
    }
     public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
    public String getCreatedDate(){

       SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
       return sdf.format(getCreated()).toString();

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
