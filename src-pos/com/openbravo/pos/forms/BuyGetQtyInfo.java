
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
public class BuyGetQtyInfo implements SerializableRead, SerializableWrite , Serializable {

    private int buyQty;
     private int getQty;


  public BuyGetQtyInfo() {
     // this.promoRuleId = promoRuleId;
    }

  public void readValues(DataRead dr) throws BasicException {
      buyQty = dr.getInt(1);
      getQty = dr.getInt(2);
    }

 public void writeValues(DataWrite dp) throws BasicException {
        dp.setInt(1, buyQty);
        dp.setInt(2, getQty);
    }
    public int getBuyQty(){
        return buyQty;
    }
    public void setBuyQty(int buyQty){
        this.buyQty = buyQty;
    }

   public int getQty(){
        return getQty;
    }
    public void setQty(int getQty){
        this.getQty = getQty;
    }


}
