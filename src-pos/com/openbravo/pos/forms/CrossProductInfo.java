
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
public class CrossProductInfo implements SerializableRead, SerializableWrite , Serializable {

    private String productId;
    private String productName;


  public CrossProductInfo() {
    }

 public void readValues(DataRead dr) throws BasicException {

       productId = dr.getString(1);
       productName = dr.getString(2);

    }
    public void writeValues(DataWrite dp) throws BasicException {

        dp.setString(1, productId);
        dp.setString(2, productName);

    }

    public String getProductId(){
        return productId;
    }
    public void setProductId(String productId){
        this.productId = productId;
    }
     public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }

}
