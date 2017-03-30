/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import java.util.Date;

/**
 *
 * @author sysfore17
 */
public class FindHoldTicketsInfo implements SerializableRead {
    private String id;
    private String tableId;
    private int ticketid;
    private double billamount;
    private Date date;
    private String person;
   
    
   
    
    @Override
    public void readValues(DataRead dr) throws BasicException {
       id=dr.getString(1);
       tableId=dr.getString(2);
       ticketid=dr.getInt(3);
       billamount=(dr.getObject(4) == null) ? 0.0 : dr.getDouble(4).doubleValue();
       date=dr.getTimestamp(5);
       person=dr.getString(6);
    }
    
    @Override
    public String toString(){

        

        String sHtml = "<tr><td width=\"30\">"+ "["+ ticketid +"]" +"</td>" +
                "<td width=\"100\">"+ Formats.TIMESTAMP.formatValue(date) +"</td>" +
                "<td align=\"right\" width=\"100\">"+ Formats.CURRENCY.formatValue(billamount) +"</td>"
               // +"<td width=\"100\">"+ Formats.STRING.formatValue(person) +"</td></tr>"
                ;

        return sHtml;
    }
    
    public int getTicketId(){
        return this.ticketid;
    }
    
     public String getId(){
        return this.id;
    }
     
     public String getTableId(){
        return this.tableId;
     }
}
