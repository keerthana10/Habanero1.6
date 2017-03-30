/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author preethi
 */
public class KotTicketListInfo implements SerializableRead, SerializableWrite {

 private static long serialVersionUID = 7640633837719L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    private int m_iKotId;
    private ArrayList m_iKotIdList;

    public KotTicketListInfo() {

        m_iKotId = 0;

    }
public int getKotId() {
        return m_iKotId;
    }

    public void setKotId(int ikotId) {
        m_iKotId = ikotId;
    // refreshLines();
    }

    public ArrayList getKotlist() {
        return m_iKotIdList;
    }

    public void setKotList(ArrayList kotList) {
        m_iKotIdList = kotList;
    // refreshLines();
    }
 public String printKotTicket() {
        return getKotlist().toString();
    }

  public void readValues(DataRead dr) throws BasicException {
        m_iKotId = dr.getInt(1);

    }

    public void writeValues(DataWrite dp) throws BasicException {

        dp.setInt(1, m_iKotId);


    }


}



/**
 *
 * @author adrianromero
 */
