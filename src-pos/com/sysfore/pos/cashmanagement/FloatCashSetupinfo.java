package com.sysfore.pos.cashmanagement;


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
public class FloatCashSetupinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private Double floatCash;
    private String autofloat;
    private Date created;
    


    /** Creates a new instance of DiscountRateinfo */
    public FloatCashSetupinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        floatCash = dr.getDouble(1);
        autofloat = dr.getString(2);
        created = dr.getTimestamp(3);
      

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setDouble(1, floatCash);
        dp.setString(2, autofloat);
        dp.setTimestamp(3, created);
      
    }
    public Double getFloatCash() {
        return floatCash;
    }
     public void setFloatCash(Double floatCash) {
        this.floatCash = floatCash;
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


}
