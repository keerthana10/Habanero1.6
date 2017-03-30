/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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
public class kotInfo implements SerializableRead, SerializableWrite {

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
    private String id;
    private String ticketId;
    private String productId;
    private String isPrinted;
    private double qty;
    private int m_iKotId;
    private int kCount;
    private String productName;
    private List<kotInfo> m_aLines;
    private  String kotName;
    private String instruction;
    //private List<kotInfo> productname;


    /** Creates a new instance of SharedTicketInfo */

     public kotInfo() {
        id = UUID.randomUUID().toString();

        ticketId = null; // incrementamos

        productId = null;
        isPrinted = null;
        qty = 0;
        m_iKotId = 0;
        productName = null;
        instruction=null;

    }

     public void writeExternal(ObjectOutput out) throws IOException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(id);
        out.writeObject(ticketId);
        out.writeObject(productId);
        out.writeObject(isPrinted);
        out.writeDouble(qty);
        out.writeObject(m_iKotId);
        out.writeObject(productName);
        out.writeObject(instruction);



    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        id = (String) in.readObject();
        ticketId = (String) in.readObject();
        productId = null;
        isPrinted = null;
        qty = in.readDouble();
       m_iKotId = 0;
      productName = null;
      instruction=null;  


    }


    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the ticketId
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * @param ticketId the ticketId to set
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * @return the productId
     */
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

    /**
     * @return the isPrinted
     */
    public String getIsPrinted() {
        return isPrinted;
    }
  
    public int getKotId() {
        return m_iKotId;
    }

    public void setKotId(int ikotId) {

        m_iKotId = ikotId;

    }
     public int printKotId() {

            // valid ticket id
            return m_iKotId;

    }
     public String getProductName() {
        return productName;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void addLine(kotInfo oLine) {

        //oLine.setTicket(m_sId, m_aLines.size());
        m_aLines.add(oLine);
    }
    
  
     public int getLinesCount() {
        return kCount;
    }
      public void setLinesCount(int kCount) {
        this.kCount=kCount;
    }
   
     public String printProductName() {

            // valid ticket id
            return productName;

    }
      public String printQty() {

       return Formats.DOUBLE.formatValue(qty);

    }

    public String getKotName() {
        return kotName;
    }

    public void setkotName(String kotName) {
        this.kotName = kotName;
    }
  public String printkotName() {

            // valid ticket id
            return getKotName();

    }


    /**
     * @param isPrinted the isPrinted to set
     */
    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        ticketId = dr.getString(2);
        productId = dr.getString(3);
        isPrinted = dr.getString(4);
        qty = dr.getDouble(5);
        m_iKotId = dr.getInt(6);
        productName = dr.getString(7);
        instruction=dr.getString(8);
       }

    public void writeValues(DataWrite dp) throws BasicException {

        dp.setString(1, id);
        dp.setString(2, ticketId);
        dp.setString(3, productId);
        dp.setString(4, isPrinted);
         dp.setDouble(5, qty);
         dp.setInt(6, m_iKotId);
         dp.setString(7, productName);
         dp.setString(8, instruction);
        
    }

    /**
     * @return the instruction
     */
    public String getInstruction() {
        if(instruction==null){
            return "";
        }else{
        return instruction;
        }
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

      public String printInstruction() {

            return getInstruction();

    }
}
