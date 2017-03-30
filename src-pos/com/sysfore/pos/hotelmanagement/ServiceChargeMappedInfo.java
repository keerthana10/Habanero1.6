/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.hotelmanagement;


import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class ServiceChargeMappedInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String name;
  
    /** Creates a new instance of DiscountRateinfo */
    public ServiceChargeMappedInfo() {
     
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
 //       rate = dr.getString(2);
        setName(dr.getString(2));
//        debitAccount = dr.getString(4);
 //       creditAccount = dr.getString(5);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
     //   dp.setString(2, rate);
        dp.setString(2, getName());
      //   dp.setString(4, debitAccount);
     //   dp.setString(5, creditAccount);
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
//    public String getDebitAccount() {
//        return debitAccount;
//    }
//
//    public void setDebitAccount(String debitAccount) {
//        this.debitAccount = debitAccount;
//    }
//    public String getCreditAccount() {
//        return creditAccount;
//    }

//    public void setCreditAccount(String creditAccount) {
//        this.creditAccount = creditAccount;
//    }
}
