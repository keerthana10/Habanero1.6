/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sales;

import java.awt.Component;

/**
 *
 * @author shilpa
 */
    public class JRetailHoldBillEdit extends JRetailPanelEditPrintTicket {

    

    @Override
    protected Component getSouthComponent() {
        return null;
    }

    @Override
    protected void resetSouthComponent() {
      
    }

    @Override
    protected JRetailTicketsBag getJTicketsPrintBag() {
     return new JRetailHoldTicket(m_App, this);
       
    }

    @Override
    public String getTitle() {
        return " Bills on Hold";
    }
    
}
