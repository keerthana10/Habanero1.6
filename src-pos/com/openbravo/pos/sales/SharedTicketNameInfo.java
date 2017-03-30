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
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.pos.ticket.RetailTicketInfo;

/**
 *
 * @author adrianromero
 */
public class SharedTicketNameInfo implements SerializableRead, SerializableWrite {
    
    private static final long serialVersionUID = 7640633837719L;
    private String id;
    private String name;
    private RetailTicketInfo content;
    
    /** Creates a new instance of SharedTicketInfo */
    public SharedTicketNameInfo() {
    }
    
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        name = dr.getString(2);
        content= (RetailTicketInfo) dr.getObject(3);
    }   
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, name);
        dp.setObject(3, (RetailTicketInfo)content);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }  
    
    public RetailTicketInfo getContent(){
        return content;
    }
    
     public String toString() {
        return "name "+name+" RetailTicketInfo "+content;
    }
    
//    public static SerializerRead getSerializerRead() {
//         return new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
//             SharedTicketNameInfo info = new SharedTicketNameInfo();
//            info.id=dr.getString(1);
//            info.name=dr.getString(2);
//            info.content= (RetailTicketInfo) dr.getObject(3);
//             return info;
//         }};
//         
//         } 
}
