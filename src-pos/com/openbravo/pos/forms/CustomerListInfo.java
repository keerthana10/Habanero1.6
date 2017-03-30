

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.io.Serializable;

/**
 *
 * @author preethi
 */
public class CustomerListInfo implements SerializableRead, SerializableWrite , Serializable {

    private String id;
    private String customerId;
    private String name;
    private String phoneNo;
    private int isCreditCustomer;

  public CustomerListInfo() {
     // this.promoRuleId = promoRuleId;
    }

  public void readValues(DataRead dr) throws BasicException {
      customerId = dr.getString(1);
      name = dr.getString(2);
      phoneNo = dr.getString(3);
      id = dr.getString(4);
      isCreditCustomer =  dr.getInt(5);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, customerId);
        dp.setString(2, name);
        dp.setString(3, phoneNo);
        dp.setString(4, id);
        dp.setInt(5, isCreditCustomer);
    }

    public String getCustomerId(){
        return customerId;
    }
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    public int getIsCreditCustomer(){
        return isCreditCustomer;
    }
    public void setIsCreditCustomer(int isCreditCustomer){
        this.isCreditCustomer = isCreditCustomer;
    }
     public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNo(){
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }


}
