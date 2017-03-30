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

package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.format.Formats;


public class TicketMergeTaxInfo implements SerializableRead, SerializableWrite{
    
    
    private double subtotal;
    private double taxAmount;
    private String taxid;
            
    /** Creates a new instance of TicketTaxInfo */
    public TicketMergeTaxInfo() {

        taxid = null;
        subtotal = 0.0;
        taxAmount = 0.0;
    }
    
   public void readValues(DataRead dr) throws BasicException {
       taxid = dr.getString(1);
       subtotal = dr.getDouble(2);
       taxAmount = dr.getDouble(3);

    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, taxid);
        dp.setDouble(2, subtotal);
        dp.setDouble(3, taxAmount);

    }
    
    
    public double getSubTotal() {    
        return subtotal;
    }
    
    public double getTaxAmt() {
        return taxAmount;
    }
    
   
    public String getTaxid() {
        return taxid;
    }
    public void setTaxid(String taxid){
        this.taxid = taxid;
    }
   
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }
    
}
