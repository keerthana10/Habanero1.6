/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TicketServiceChargeInfo;
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
 * @author shilpa
 */
public class RetailServiceChargesLogic {
    
    private List<ServiceChargeInfo> chargelist;
    public AppView m_App;
    protected DataLogicSales dlSales;
    private Map<String, ChargesLogicElement> chargetrees;
    private RetailTicketInfo ticket=null;
    
    public RetailServiceChargesLogic(List<ServiceChargeInfo> chargelist, AppView app) {
      this.chargelist = chargelist;
      m_App = app;
      dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
      chargetrees = new HashMap<String, ChargesLogicElement>();
                
        // Order the chargelist by Application Order...
        List<ServiceChargeInfo> chargelistordered = new ArrayList<ServiceChargeInfo>();
        chargelistordered.addAll(chargelist);
        
        
        // Generate the chargetrees
        HashMap<String, ChargesLogicElement> chargeorphans = new HashMap<String, ChargesLogicElement>();
                for (ServiceChargeInfo t : chargelistordered) {
                       
            ChargesLogicElement te = new ChargesLogicElement(t);
            
            // get the parent
            ChargesLogicElement teparent = chargetrees.get(t.getId());
            if (teparent == null) {
                // orphan node
                teparent = chargeorphans.get(t.getId());
                if (teparent == null) {
                    teparent = new ChargesLogicElement(null);
                    chargeorphans.put(t.getId(), teparent);
                } 
            } 
            
            teparent.getSons().add(te);

            // Does it have orphans ?
            teparent = chargetrees.get(t.getId());
            if (teparent != null) {
                // get all the sons
                te.getSons().addAll(teparent.getSons());
                // remove the orphans
                chargeorphans.remove(t.getId());
            }          
            
            // Add it to the tree...
            chargetrees.put(t.getId(), te);
        }
       
    }
    
    public void calculateCharges(RetailTicketInfo ticket) throws TaxesException {
         this.ticket=ticket;  
        List<TicketServiceChargeInfo> ticketcharges = new ArrayList<TicketServiceChargeInfo>(); 
        
        for (RetailTicketLineInfo line: ticket.getLines()) {
         
            ticketcharges = sumLineCharges(ticketcharges, calculateCharges(line, ticket));
        }
        
        ticket.setCharges(ticketcharges);
    }
    
      public List<TicketServiceChargeInfo> calculateCharges(RetailTicketLineInfo line, RetailTicketInfo ticket) throws TaxesException {
         ChargesLogicElement chargesapplied = getChargesApplied(line.getChargeInfo());
         if(chargesapplied != null){
             if(ticket.iscategoryDiscount())
             return calculateLineCharges(line.getSubValueOnProductCat(), chargesapplied,ticket);
             else
             return calculateLineCharges(line.getSubValue(), chargesapplied,ticket);     
         }else{
             return null;
         }
          
    }
      
  private ChargesLogicElement getChargesApplied(ServiceChargeInfo t) throws TaxesException {
         String id = null;
        if (t == null) {
           id=null;
//            try {
//                id = dlSales.getChargeId();
//            } catch (BasicException ex) {
//                Logger.getLogger(RetailServiceChargesLogic.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }else{
            id = t.getId();
          }
        
        return chargetrees.get(id);
    }  
    
  private List<TicketServiceChargeInfo> calculateLineCharges(double base, ChargesLogicElement chargesapplied,RetailTicketInfo ticket) {

        List<TicketServiceChargeInfo> linecharges = new ArrayList<TicketServiceChargeInfo>();
        if (chargesapplied.getSons().isEmpty()) {
            TicketServiceChargeInfo ticketcharge = new TicketServiceChargeInfo(chargesapplied.getCharge());
            ticketcharge.add(base,ticket);
            linecharges.add(ticketcharge);
            ticketcharge.setSchargeAmount(ticket.getChargeValue());
        } else {
            double acum = base;

            for (ChargesLogicElement te : chargesapplied.getSons()) {

                List<TicketServiceChargeInfo> sublinecharges = calculateLineCharges(
                      base,
                        te);
                linecharges.addAll(sublinecharges);
                acum += sumCharges(sublinecharges);
            }
        }

        return linecharges;
    }
           
    public List<TicketServiceChargeInfo> calculateCharges(RetailTicketLineInfo line) throws TaxesException {

        ChargesLogicElement chargesapplied = getChargesApplied(line.getChargeInfo());
        return calculateLineCharges(line.getSubValue(), chargesapplied);
    }

   
    
    private List<TicketServiceChargeInfo> calculateLineCharges(double base, ChargesLogicElement chargesapplied) {
 
        List<TicketServiceChargeInfo> linecharges = new ArrayList<TicketServiceChargeInfo>();
        
        if (chargesapplied.getSons().isEmpty()) {           
            TicketServiceChargeInfo ticketcharge = new TicketServiceChargeInfo(chargesapplied.getCharge());
            ticketcharge.add(base,ticket);
            linecharges.add(ticketcharge);
        } else {
            double acum = base;
            
            for (ChargesLogicElement te : chargesapplied.getSons()) {
                
                List<TicketServiceChargeInfo> sublinecharges = calculateLineCharges(
                       base,
                        te);
                linecharges.addAll(sublinecharges);
                acum += sumCharges(sublinecharges);
            }
        }
        
        return linecharges;       
    }

    

        
    private double sumCharges(List<TicketServiceChargeInfo> linecharges) {
        
        double chargetotal = 0.0;
        
        for (TicketServiceChargeInfo ticketcharge : linecharges) {
            chargetotal += ticketcharge.getSCharge();
            
        }
        return  chargetotal;
    }
    
    private List<TicketServiceChargeInfo> sumLineCharges(List<TicketServiceChargeInfo> list1, List<TicketServiceChargeInfo> list2) {
     if(list2 != null){
        for (TicketServiceChargeInfo ticketcharge : list2) {
            //Changed the sum logic based on service rate 
           TicketServiceChargeInfo i = searchTicketTax(list1, ticketcharge.getServiceChargeInfo().getRate());
            if (i == null) {
                list1.add(ticketcharge);
            } else {
                i.add(ticketcharge.getSubTotal(),ticket);
            }
        }
     }
        return list1;
    }
    //Changed the sum logic based on service rate 
    private TicketServiceChargeInfo searchTicketTax(List<TicketServiceChargeInfo> l, double rate) {
        
        for (TicketServiceChargeInfo ticketcharge : l) {
            if (rate==(ticketcharge.getServiceChargeInfo().getRate())) {
                return ticketcharge;
            }
        }    
        return null;
    }
    
    public double getTaxRate(String tcid) {
        return getChargeRate(tcid, null);
    }
    
    
    public double getChargeRate(String tcid, CustomerInfoExt customer) {
        
        if (tcid == null) {
            return 0.0;
        } else {
            ServiceChargeInfo charge = getChargeInfo(tcid, customer);
            if (charge == null) {
                return 0.0;
            } else {
                return charge.getRate();
            }            
        }
    }
    
    public ServiceChargeInfo getChargeInfo(String tcid) {
        return getChargeInfo(tcid, null);
    }
    
    public ServiceChargeInfo getChargeInfo(TaxCategoryInfo tc) {
        return getChargeInfo(tc.getID(), null);
    }
    
    public ServiceChargeInfo getChargeInfo(TaxCategoryInfo tc, CustomerInfoExt customer) {  
        return getChargeInfo(tc.getID(), customer);
    }    
    
    public ServiceChargeInfo getChargeInfo(String tcid, CustomerInfoExt customer) {
        
      ServiceChargeInfo defaultcharge = null;
   
        for (ServiceChargeInfo charge : chargelist) {
             //    System.out.println("getChargeInfo tcid--"+tcid+"---"+charge.getId());
              if ( charge.getId().equals(tcid)) {
            //   System.out.println("charge val and charge name "+charge.getId()+" "+charge.getRate());
               return charge;  
              }
              if (charge.getId() == null) {
                    defaultcharge = charge;
                }
            }
      
        // No tax found
        return defaultcharge;
    }
    
}
