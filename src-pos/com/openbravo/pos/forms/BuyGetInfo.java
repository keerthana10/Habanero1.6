

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
public class BuyGetInfo implements SerializableRead, SerializableWrite , Serializable {

    private int quantity;
    private String campaignId;


  public BuyGetInfo() {
     // this.promoRuleId = promoRuleId;
    }

  public void readValues(DataRead dr) throws BasicException {
      quantity = dr.getInt(1);
       campaignId = dr.getString(2);

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setInt(1, quantity);
        dp.setString(2, campaignId);
    }

    public String getCampaignId(){
        return campaignId;
    }
    public void setCampaignId(String campaignId){
        this.campaignId = campaignId;
    }

    public int getQty(){
        return quantity;
    }
    public void setQty(int quantity){
        this.quantity = quantity;
    }


}
