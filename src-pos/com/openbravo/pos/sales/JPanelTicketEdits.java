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
//    You should have receiveivatd a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.sales;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.awt.Dimension;

public class JPanelTicketEdits extends JPanelTicket {
    
    private JTicketCatalogLines m_catandlines;
    
    /** Creates a new instance of JPanelTicketRefunds */
    public JPanelTicketEdits() {
        System.out.println("JPanelTicketEdits");
    }

      
    public String getTitle() {
        System.out.println("getTitle");
        return null;
    }
    
    @Override
    public void activate() throws BasicException {
     
        super.activate();
        m_catandlines.loadCatalog();
    }
 public void activate( AppView m_App,String total, String billNo) throws BasicException {
        super.init(m_App);
        super.activate(m_App,total,billNo);
       // m_catandlines.loadCatalog();
    }
    public void showCatalog() {
        System.out.println("showCatalog");
        m_jbtnconfig.setVisible(true);
        m_catandlines.showCatalog();
    }
    
    public void showRefundLines(List aRefundLines) {
        // anado las lineas de refund
        // m_reflines.setLines(aRefundLines);
        System.out.println("showRefundLines");
        m_jbtnconfig.setVisible(false);
        m_catandlines.showRefundLines(aRefundLines);
    }
    
    protected JTicketsBag getJTicketsBag() {
        System.out.println("getJTicketsBag");
        return new JTicketsBagTicket(m_App, this);
    }


    protected Component getSouthComponent() {
System.out.println("getSouthComponent");
        m_catandlines = new JTicketCatalogLines(m_App, this,                
                "true".equals(m_jbtnconfig.getProperty("pricevisible")),
                "true".equals(m_jbtnconfig.getProperty("taxesincluded")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-width", "64")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-height", "54")));
        m_catandlines.setPreferredSize(new Dimension(
                0,
                Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));
        m_catandlines.addActionListener(new CatalogListener());
        return m_catandlines;
    } 

    protected void resetSouthComponent() {
        System.out.println("resetSouthComponent");
    }
    
    private class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            buttonTransition((ProductInfoExt) e.getSource());
        }  
    }  
       
}
