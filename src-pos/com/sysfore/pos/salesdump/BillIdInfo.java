/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.salesdump;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.util.Date;
import java.util.List;

/**
 *
 * @author preethi
 */
public class BillIdInfo  implements SerializableRead, SerializableWrite {
private String id;


    public BillIdInfo() {
    }

   
    /**
     * @return the id
     */
		public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
    public void readValues(DataRead dr) throws BasicException {
       id = dr.getString(1);
      
    }

    public void writeValues(DataWrite dp) throws BasicException {
       dp.setString(1, id);

    }
 
   

}
