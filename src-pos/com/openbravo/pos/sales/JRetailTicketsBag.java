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

import com.openbravo.pos.sales.simple.JTicketsBagSimple;
import com.openbravo.pos.forms.*; 
import com.openbravo.pos.sales.restaurant.JRetailTicketsBagRestaurantMap;

import javax.swing.*;
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurantMap;
import com.openbravo.pos.sales.shared.JRetailTicketsBagShared;
import com.openbravo.pos.sales.shared.JTicketsBagShared;

public abstract class JRetailTicketsBag extends JPanel {
    
    protected static AppView m_App;
    protected DataLogicSales m_dlSales;
    protected RetailTicketsEditor m_panelticket;
    
    /** Creates new form JTicketsBag */
    public JRetailTicketsBag(AppView oApp, RetailTicketsEditor panelticket) {
        m_App = oApp;     
        m_panelticket = panelticket;        
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
    }
    
    public abstract void activate();
    public abstract boolean deactivate();
    public abstract void deleteTicket();
  //  public abstract void deleteTicket(String splitId);
    
    protected abstract JComponent getBagComponent();
    protected abstract JComponent getNullComponent();
    
    public static JRetailTicketsBag createTicketsBag(String sName, AppView app, RetailTicketsEditor panelticket, String businessType) {
        System.out.println("dddddd---");
        if ("standard".equals(sName)) {
            return new JRetailTicketsBagShared(app, panelticket,"Sales");

       } else if ("restaurant".equals(sName)) {
           if(businessType.equals("HomeDelivery")){
                return new JRetailTicketsBagShared(app, panelticket,"HomeDelivery");
           }else if(businessType.equals("TakeAway")){
                return new JRetailTicketsBagShared(app, panelticket,"TakeAway");
           }else{
                return new JRetailTicketsBagRestaurantMap(app, panelticket,"Sales");
           }
           
        }
            else { 
            return null;
         
        }
    }   
}