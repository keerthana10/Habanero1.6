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

package com.sysfore.pos.cashmanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author mateen
 */
public class CurrencyValueInfo implements SerializableRead, SerializableWrite {
    
    private static final long serialVersionUID = 7641234537719L;
    private String id;
   // private String currencyname;
    private String denominations;
    private int currencyCount;
    private double currencyValue;

    /** Creates a new instance of CurrencyInfo */
    public CurrencyValueInfo() {
    }
    
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
     //   currencyname = dr.getString(2);
        denominations = dr.getString(2);
        currencyCount = dr.getInt(3);
        currencyValue = dr.getDouble(4);
    }   
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
       // dp.setString(2, getCurrencyname());
        dp.setString(2, getDenominations());
        dp.setInt(3, getcurrencyCount());
        dp.setDouble(4, getcurrencyValue());
    }
    
    public String getId() {
        return id;
    }


//    public String getCurrencyname() {
//        return currencyname;
//    }

    public String getDenominations() {
        return denominations;
    }
    

    public int getcurrencyCount() {
        return currencyCount;
    }

    public double getcurrencyValue() {
        return currencyValue;
    }
}
