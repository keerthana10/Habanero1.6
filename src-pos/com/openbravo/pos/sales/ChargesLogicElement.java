/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import com.openbravo.pos.ticket.ServiceChargeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shilpa
 */
public class ChargesLogicElement {
     private ServiceChargeInfo charge;
    private List<ChargesLogicElement> chargesons;
    
    public ChargesLogicElement(ServiceChargeInfo charge) {
        this.charge = charge;
        this.chargesons = new ArrayList<ChargesLogicElement>();
    }
    
    public ServiceChargeInfo getCharge() {
        return charge;
    }
    
    public List<ChargesLogicElement> getSons() {
        return chargesons;
    }
}
