/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.stockreconciliation;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.Date;
import java.util.List;

/**
 *
 * @author preethi
 */
public class StockDocumentInfo  implements SerializableRead, SerializableWrite {
private String documentNo;
private String category;
private String wareHouse;
private Date stockDate;

    private List<StockDetailsInfo> m_aLines;

    public StockDocumentInfo() {
    }

    public StockDocumentInfo(String productName, double units) {
      //  this.productName = productName;
       
   //     throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @return the id
     */
    public String getDocumentNo() {
        return documentNo;
    }

    /**
     * @param id the id to set
     */
    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }
  

    public void readValues(DataRead dr) throws BasicException {
       documentNo = dr.getString(1);
     //  category = dr.getString(2);
    //   wareHouse = dr.getString(3);
    //   stockDate = dr.getTimestamp(4);
    }

    public void writeValues(DataWrite dp) throws BasicException {
       dp.setString(1, documentNo);
      // dp.setString(2, category);
      // dp.setString(3, wareHouse);
     //  dp.setTimestamp(4, stockDate);
    }
 
   

}
