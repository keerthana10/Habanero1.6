
/**
 *
 * @author preethi
 */

//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class kotPrintedInfo implements SerializableRead, SerializableWrite {

    private static long serialVersionUID = 7640633837719L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    private String productId;

    private double qty;
    private String isCancelled;

    private List<kotInfo> m_aLines;
    //private List<kotInfo> productname;


    /** Creates a new instance of SharedTicketInfo */

     public kotPrintedInfo() {

        productId = null;
        qty = 0;
        isCancelled = null;

    }

     public void writeExternal(ObjectOutput out) throws IOException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(productId);
        out.writeDouble(qty);
        out.writeObject(isCancelled);



    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes

        productId = null;
        qty = in.readDouble();
        isCancelled = null;


    }



    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }


    public double getQty() {
        return qty;
    }

    /**
     * @param productId the productId to set
     */
    public void setqty(double qty) {
        this.qty = qty;
    }

public String getisCancelled() {
        return isCancelled;
    }

    /**
     * @param productId the productId to set
     */
    public void setisCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }


   /* public void addLine(kotInfo oLine) {

        //oLine.setTicket(m_sId, m_aLines.size());
        m_aLines.add(oLine);
    }


     public int getLinesCount() {
        return kCount;
    }
      public void setLinesCount(int kCount) {
        this.kCount=kCount;
    }
    public String printKotId() {

            // valid ticket id
            return Formats.INT.formatValue(new Integer(m_iKotId));

    }
     public String printProductName() {

            // valid ticket id
            return productName;

    }
      public double printQty() {

            // valid ticket id
            return getQty();

    }
*/

    /**
     * @param isPrinted the isPrinted to set
     */

    public void readValues(DataRead dr) throws BasicException {

        productId = dr.getString(1);
        qty = dr.getDouble(2);
        isCancelled = dr.getString(3);
     

    }

    public void writeValues(DataWrite dp) throws BasicException {

        dp.setString(1, productId);
        dp.setDouble(2, qty);
       dp.setString(3, isCancelled);

    }


}
