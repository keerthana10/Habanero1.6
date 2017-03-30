/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.panels;

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
public class PosActionsInfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private String printAccess;
     private String settleAccess;
      private String cancelAccess;
      private String discountAccess;
      private String splitAccess;
      private String moveTableAccess;
        private String billOnHoldAccess;


       private String name;
    /** Creates a new instance of DiscountRateinfo */
    public PosActionsInfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        printAccess = dr.getString(1);
        settleAccess = dr.getString(2);
        cancelAccess = dr.getString(3);
        discountAccess = dr.getString(4);
        splitAccess = dr.getString(5);
        moveTableAccess = dr.getString(6);
        billOnHoldAccess = dr.getString(7);
        
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, printAccess);
        dp.setString(2, settleAccess);
         dp.setString(3, cancelAccess);
        dp.setString(4, discountAccess);
         dp.setString(5, splitAccess);
        dp.setString(6, moveTableAccess);
         dp.setString(7, billOnHoldAccess);
    }

    public String getCancelAccess() {
        return cancelAccess;
    }

    public void setCancelAccess(String cancelAccess) {
        this.cancelAccess = cancelAccess;
    }

    public String getDiscountAccess() {
        return discountAccess;
    }

    public void setDiscountAccess(String discountAccess) {
        this.discountAccess = discountAccess;
    }

    public String getMoveTableAccess() {
        return moveTableAccess;
    }

    public void setMoveTableAccess(String moveTableAccess) {
        this.moveTableAccess = moveTableAccess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintAccess() {
        return printAccess;
    }

    public void setPrintAccess(String printAccess) {
        this.printAccess = printAccess;
    }

    public String getSettleAccess() {
        return settleAccess;
    }

    public void setSettleAccess(String settleAccess) {
        this.settleAccess = settleAccess;
    }

    public String getSplitAccess() {
        return splitAccess;
    }

    public void setSplitAccess(String splitAccess) {
        this.splitAccess = splitAccess;
    }
  public String getBillOnHoldAccess() {
        return billOnHoldAccess;
    }

    public void setBillOnHoldAccess(String billOnHoldAccess) {
        this.billOnHoldAccess = billOnHoldAccess;
    }
   
}
