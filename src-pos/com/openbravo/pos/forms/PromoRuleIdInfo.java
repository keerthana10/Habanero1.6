/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class PromoRuleIdInfo implements SerializableRead, SerializableWrite , Serializable {

    private String promoRuleId;


  public PromoRuleIdInfo() {
     // this.promoRuleId = promoRuleId;
    }

  public void readValues(DataRead dr) throws BasicException {
       promoRuleId = dr.getString(1);

    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, promoRuleId);     
    }

    public String getpromoRuleId(){
        return promoRuleId;
    }
    public void setpromoRuleId(String promoRuleId){
        this.promoRuleId = promoRuleId;
    }
}