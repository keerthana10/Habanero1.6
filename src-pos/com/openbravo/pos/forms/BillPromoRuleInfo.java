

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author preethi
 */
public class BillPromoRuleInfo implements SerializableRead, SerializableWrite {

    private String promotypeId;
    private double billAmount;
    private double value;
    private String isPrice;

  public BillPromoRuleInfo() {
    }

 public void readValues(DataRead dr) throws BasicException {
       promotypeId = dr.getString(1);
       billAmount = dr.getDouble(2);
       value = dr.getDouble(3);
       isPrice = dr.getString(4);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, promotypeId);
        dp.setDouble(2, billAmount);
        dp.setDouble(3, value);
        dp.setString(4, isPrice);
    }

    public String getpromotypeId(){
        return promotypeId;
    }
    public void setpromotypeId(String promotypeId){
        this.promotypeId = promotypeId;
    }

    public double getBillAmount(){
        return billAmount;
    }
    public void setBillAmount(double billAmount){
        this.billAmount = billAmount;
    }

    public double getValue(){
        return value;
    }
    public void setValue(double value){
         this.value = value;
    }
    public String getisPrice(){
        return isPrice;
    }
    public void setisPrice(String isPrice){
        this.isPrice = isPrice;
    }

}
