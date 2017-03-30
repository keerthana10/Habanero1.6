//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008-2009 Openbravo, S.L.
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
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketTaxInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrianromero
 */
public class RetailSTaxesLogic {
    
    private List<TaxInfo> taxlist;
    public AppView m_App;
    protected DataLogicSales dlSales;
    private Map<String, TaxesLogicElement> taxtrees;
    RetailTicketInfo ticket=null;
    
    public RetailSTaxesLogic(List<TaxInfo> taxlist, AppView app) {
        this.taxlist = taxlist;
      m_App = app;
      dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        taxtrees = new HashMap<String, TaxesLogicElement>();
                
        // Order the taxlist by Application Order...
        List<TaxInfo> taxlistordered = new ArrayList<TaxInfo>();
        taxlistordered.addAll(taxlist);
        Collections.sort(taxlistordered, new Comparator<TaxInfo>() {
            public int compare(TaxInfo o1, TaxInfo o2) {
                if (o1.getApplicationOrder() < o2.getApplicationOrder()) {
                    return -1;
                } else if (o1.getApplicationOrder() == o2.getApplicationOrder()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        
        // Generate the taxtrees
        HashMap<String, TaxesLogicElement> taxorphans = new HashMap<String, TaxesLogicElement>();
        
        for (TaxInfo t : taxlistordered) {
                       
            TaxesLogicElement te = new TaxesLogicElement(t);
            
            // get the parent
            TaxesLogicElement teparent = taxtrees.get(t.getParentID());
            if (teparent == null) {
                // orphan node
                teparent = taxorphans.get(t.getParentID());
                if (teparent == null) {
                    teparent = new TaxesLogicElement(null);
                    taxorphans.put(t.getParentID(), teparent);
                } 
            } 
            
            teparent.getSons().add(te);

            // Does it have orphans ?
            teparent = taxorphans.get(t.getId());
            if (teparent != null) {
                // get all the sons
                te.getSons().addAll(teparent.getSons());
                // remove the orphans
                taxorphans.remove(t.getId());
            }          
            
            // Add it to the tree...
            taxtrees.put(t.getId(), te);
        }
    }
    
    public void calculateServiceTaxes(RetailTicketInfo ticket) throws TaxesException {
        this.ticket=ticket;
        List<TicketTaxInfo> tickettaxes = new ArrayList<TicketTaxInfo>(); 
        for (RetailTicketLineInfo line: ticket.getLines()) {
           ticket.setServiceChargeRate(line.getServiceChargeRate());
            tickettaxes = sumLineTaxes(tickettaxes, calculateTaxes(line, ticket));
        }
        
        ticket.setServiceTaxes(tickettaxes);
    }
      public void calculateTakeAwayServiceTaxes(RetailTicketInfo ticket) throws TaxesException {
        this.ticket=ticket;
        List<TicketTaxInfo> tickettaxes = new ArrayList<TicketTaxInfo>();
        for (RetailTicketLineInfo line: ticket.getLines()) {
           ticket.setServiceChargeRate(0.00);
            tickettaxes = sumLineTakeAwayTaxes(tickettaxes, calculateTaxes(line, ticket));
        }

        ticket.setServiceTaxes(tickettaxes);
    }
    
    public List<TicketTaxInfo> calculateTaxes(RetailTicketLineInfo line, RetailTicketInfo ticket) throws TaxesException {


        TaxesLogicElement staxesapplied = getTaxesApplied(line.getServiceTaxInfo());
        if(staxesapplied != null){
            if(ticket.iscategoryDiscount())   
                return calculateLineTaxes(line.getSubValueOnProductCat(), staxesapplied,ticket);
            else
                return calculateLineTaxes(line.getSubValue(), staxesapplied,ticket);
        }else{
            return null;
        }
    }
    
    public List<TicketTaxInfo> calculateTaxes(RetailTicketLineInfo line) throws TaxesException {

        TaxesLogicElement taxesapplied = getTaxesApplied(line.getServiceTaxInfo());
        return calculateLineTaxes(line.getSubValue(), taxesapplied);
    }

     
    
    private List<TicketTaxInfo> calculateLineTaxes(double base, TaxesLogicElement taxesapplied) {
 
        List<TicketTaxInfo> linetaxes = new ArrayList<TicketTaxInfo>();
        
        if (taxesapplied.getSons().isEmpty()) {           
            TicketTaxInfo tickettax = new TicketTaxInfo(taxesapplied.getTax());
               if(ticket.getStoreName().equals("WMC- Brewpub") || ticket.getStoreName().equals("Toit")  || ticket.getStoreName().equals("Habanero"))
                  tickettax.addServiceTax(base,ticket);
               else
                  tickettax.addServiceTaxOnServiceCharge(base,ticket); 
            linetaxes.add(tickettax);
        } else {
            double acum = base;
            
            for (TaxesLogicElement te : taxesapplied.getSons()) {
                
                List<TicketTaxInfo> sublinetaxes = calculateLineTaxes(
                        te.getTax().isCascade() ? acum : base, 
                        te);
                linetaxes.addAll(sublinetaxes);
                acum += sumTaxes(sublinetaxes);
            }
        }
        
        return linetaxes;       
    }
     private List<TicketTaxInfo> calculateLineTaxes(double base, TaxesLogicElement taxesapplied,RetailTicketInfo ticket) {

        List<TicketTaxInfo> linetaxes = new ArrayList<TicketTaxInfo>();
        if (taxesapplied.getSons().isEmpty()) {
           TicketTaxInfo tickettax = new TicketTaxInfo(taxesapplied.getTax());
            //tickettax.add(base);
            if(ticket.getStoreName().equals("WMC- Brewpub") || ticket.getStoreName().equals("Toit") || ticket.getStoreName().equals("Habanero") )
              tickettax.addServiceTax(base,ticket); 
            else
              tickettax.addServiceTaxOnServiceCharge(base,ticket); 
            linetaxes.add(tickettax);
            tickettax.setTaxAmount(ticket.getTaxValue());
        } else {
            double acum = base;
           for (TaxesLogicElement te : taxesapplied.getSons()) {

                List<TicketTaxInfo> sublinetaxes = calculateLineTaxes(
                        te.getTax().isCascade() ? acum : base,
                        te);
                linetaxes.addAll(sublinetaxes);
                acum += sumTaxes(sublinetaxes);
            }
        }

        return linetaxes;
    }
    
      private TaxesLogicElement getTaxesApplied(TaxInfo t) throws TaxesException {
         String id = null;
        if (t == null) {
         id=null;  
         }else{
            id = t.getId();
        }
        
        return taxtrees.get(id);
    }
        
    private double sumTaxes(List<TicketTaxInfo> linetaxes) {
        
        double taxtotal = 0.0;
        
        for (TicketTaxInfo tickettax : linetaxes) {
            taxtotal += tickettax.getTax();
            
        }
        return  taxtotal;
    }
    
    private List<TicketTaxInfo> sumLineTaxes(List<TicketTaxInfo> list1, List<TicketTaxInfo> list2) {
     if(list2 != null){
        for (TicketTaxInfo tickettax : list2) {
            //Changed the sum logic based on tax rate 
           TicketTaxInfo i = searchTicketTax(list1, tickettax.getTaxInfo().getRate());
            if (i == null) {
              list1.add(tickettax);
            } else {
              //  i.add(tickettax.getSubTotal();
                 if(ticket.getStoreName().equals("WMC- Brewpub") || ticket.getStoreName().equals("Toit") || ticket.getStoreName().equals("Habanero") )
                  i.addServiceTax(tickettax.getSubTotal(),ticket); 
                 else
                   i.addServiceTaxOnServiceCharge(tickettax.getSubTotal(),ticket);
            }
        }
     }
        return list1;
    }
     private List<TicketTaxInfo> sumLineTakeAwayTaxes(List<TicketTaxInfo> list1, List<TicketTaxInfo> list2) {
     if(list2 != null){
        for (TicketTaxInfo tickettax : list2) {
            //Changed the sum logic based on tax rate
           
           TicketTaxInfo i = searchTicketTax(list1, tickettax.getTaxInfo().getRate());
            if (i == null) {
              list1.add(tickettax);
            } else {
              //  i.add(tickettax.getSubTotal();
                 if(ticket.getStoreName().equals("WMC- Brewpub") || ticket.getStoreName().equals("Habanero")){
                  System.out.println("sumLineTakeAwayTaxes----if");
                  i.addServiceTax(tickettax.getSubTotal(),ticket);
                 }else{
                      System.out.println("sumLineTakeAwayTaxes----elseif");
                   i.addServiceTaxOnServiceCharge(tickettax.getSubTotal(),ticket);
                 }
            }
        }
     }
        return list1;
    }
    //Changed the sum logic based on tax rate 
    private TicketTaxInfo searchTicketTax(List<TicketTaxInfo> l, double rate) {
    for (TicketTaxInfo tickettax : l) {
         if (rate==(tickettax.getTaxInfo().getRate())) {
               return tickettax;
          }
        }    
        return null;
    }
    
    public double getTaxRate(String tcid) {
        return getTaxRate(tcid, null);
    }
    
    public double getTaxRate(TaxCategoryInfo tc) {
        return getTaxRate(tc, null);
    }
    
    public double getTaxRate(TaxCategoryInfo tc, CustomerInfoExt customer) {
        
        if (tc == null) {
            return 0.0;
        } else {
            return getTaxRate(tc.getID(), customer);          
        }
    }
    
    public double getTaxRate(String tcid, CustomerInfoExt customer) {
        
        if (tcid == null) {
            return 0.0;
        } else {
            TaxInfo tax = getTaxInfo(tcid, customer);
            if (tax == null) {
                return 0.0;
            } else {
                return tax.getRate();
            }            
        }
    }
    
    public TaxInfo getTaxInfo(String tcid) {
        return getTaxInfo(tcid, null);
    }
    
    public TaxInfo getTaxInfo(TaxCategoryInfo tc) {
        return getTaxInfo(tc.getID(), null);
    }
    
    public TaxInfo getTaxInfo(TaxCategoryInfo tc, CustomerInfoExt customer) {  
        return getTaxInfo(tc.getID(), customer);
    }    
    
    public TaxInfo getTaxInfo(String tcid, CustomerInfoExt customer) {
       
        TaxInfo defaulttax = null;
        for (TaxInfo tax : taxlist) {
            if (tax.getParentID() == null && tax.getTaxCategoryID().equals(tcid)) {
               if ((customer == null || customer.getTaxCustCategoryID() == null) && tax.getTaxCustCategoryID() == null) {
                    return tax;
                } else if (customer != null && customer.getTaxCustCategoryID() != null && customer.getTaxCustCategoryID().equals(tax.getTaxCustCategoryID())) {
                return tax;
                }
                
                if (tax.getTaxCustCategoryID() == null) {
                    defaulttax = tax;
                }
            }
        }
        
        // No tax found
        return defaulttax;
    }
}
