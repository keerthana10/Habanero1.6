/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.cashmanagement;

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
public class Posinfo implements SerializableRead, SerializableWrite {

    private static final long serialVersionUID = 7640633837719L;
    private int id;
    private int posNo;
    
   
    /** Creates a new instance of DiscountRateinfo */
    public Posinfo() {
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getInt(1);
        posNo = dr.getInt(2);
        
        
    }
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setInt(1, id);
        dp.setInt(2, posNo);
      
     
    }
    public int getId() {
        return id;
    }
     public void setid(int id) {
        this.id = id;
    }
     public int getPosNo() {
        return posNo;
    }
     public void setPosNo(int posNo) {
        this.posNo = posNo;
    }
}
   