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
import com.openbravo.format.Formats;
import java.text.Format;
/**
 *
 * @author preethi
 */


public class PurchaseChargesInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String chargeId;
    private String name;
    private double amount;
    private String piId;

    public PurchaseChargesInfo() {
    }
     PurchaseChargesInfo(PurchaseChargesInfo charge) {
         this.chargeId = charge.getChargeId();
        this.name = charge.getName();
        this.amount = charge.getAmount();
     //   this.account = charge.getAccount()

    }

    PurchaseChargesInfo(String chargeId, String name, double amount) {
        this.chargeId = chargeId;
        this.name = name;
        this.amount = amount;
    }


    public void readValues(DataRead dr) throws BasicException {
        chargeId = dr.getString(1);
        name = dr.getString(2);
        amount =dr.getDouble(3);

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, chargeId);
        dp.setString(2, name);
        dp.setDouble(3, amount);
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getChargeId() {
        return chargeId;
    }
    public void setChargeId(String chargeId){
        this.chargeId =chargeId;
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
    public double getAmount() {
        return amount;
    }

    /**
     * @param name the name to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String printName(){
        return getName();
    }
    public String printAmount(){
        return Formats.CURRENCY.formatValue(new Double(getAmount()));
    }

}

