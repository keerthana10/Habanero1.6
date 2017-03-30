/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.purchaseorder;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
/**
 *
 * @author preethi
 */


public class Chargesinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String account;
    private String name;
    private String amount;
    private String isDiscounts;

    public Chargesinfo() {
    }
     Chargesinfo(Chargesinfo charge) {
        this.name = charge.getName();
     //   this.account = charge.getAccount()

    }


    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        name = dr.getString(2);
        account =dr.getString(3);
        isDiscounts = dr.getString(4);
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, name);
        dp.setString(3, account);
        dp.setString(4, isDiscounts);
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getAccount() {
        return account;
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
    public String getAmount() {
        return amount;
    }

    /**
     * @param name the name to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

public String getIsDiscount() {
        return isDiscounts;
    }

    /**
     * @param name the name to set
     */
    public void setIsDiscount(String isDiscounts) {
        this.isDiscounts = isDiscounts;
    }
}

