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

import com.openbravo.format.Formats;


public class TicketServiceChargeInfo {
    
    private ServiceChargeInfo scharge;
    public TicketInfo ticket;
    public RetailTicketInfo retailTicket;
    private double subtotal;
    private double sChargetotal;
    private double sChargeAmount;
    private double retailSChargeTotal;
            
  
    public TicketServiceChargeInfo(ServiceChargeInfo charge) {
        this.scharge = charge;
        subtotal = 0.0;
        sChargetotal = 0.0;
        retailSChargeTotal = 0.0;
    }
    
    public ServiceChargeInfo getServiceChargeInfo() {
        return scharge;
    }

    public TicketInfo getTicketInfo() {
        return ticket;
    }
      public RetailTicketInfo getRetailTicketInfo() {
        return retailTicket;
    }
      
      public void setRetailTicketInfo(RetailTicketInfo ticket) {
         retailTicket=ticket;
    }
    
    public void add(double dValue,RetailTicketInfo ticket) {
        subtotal += dValue;
        ticket.setServiceChargeRate(scharge.getRate());
        System.out.println("subtotal after discount"+subtotal);
        System.out.println("service charge after discount"+scharge.getRate()+"---"+ticket.getServiceChargeRate());
        sChargetotal = (subtotal * scharge.getRate());
        retailSChargeTotal =  sChargetotal;
      

    }
    
    public double getSubTotal() {    
        return subtotal;
    }
    
    public double getSCharge() {       
        return sChargetotal;
    }
    public double getRetailSCharge() {
        return retailSChargeTotal;
    }
  
    public double getTotal() {         
        return subtotal + sChargetotal;
    }
    public double getRetailTotal() {
        return subtotal + retailSChargeTotal;
    }
    public double getSchargeAmount() {
        return sChargeAmount;
    }
    public void setSchargeAmount(double Amount){
        this.sChargeAmount = Amount;
    }
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }
    public String printSCharge() {
        return Formats.CURRENCY.formatValue(new Double(getSCharge()));
    }
    public String printRetailSCharge() {
        return Formats.CURRENCY.formatValue(new Double(getRetailSCharge()));
    }
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }
     public String printRetailTotal() {
        return Formats.CURRENCY.formatValue(new Double(getRetailTotal()));
    }
}
