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
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.ServiceChargeInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.BusinessServiceTaxInfo;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  adrian
 */
public class RetailSimpleReceipt extends javax.swing.JPanel {

    protected DataLogicCustomers dlCustomers;
    protected DataLogicSales dlSales;
    protected RetailTaxesLogic taxeslogic;
    private JRetailTicketLines ticketlines;
    private RetailTicketInfo ticket;
    private Object ticketext;
    public static java.util.ArrayList<BusinessServiceTaxInfo> serviceTaxList = null;
    public static java.util.ArrayList<BusinessServiceChargeInfo> serviceChargeList = null;
    static double serviceChargeAmt;
    private static RetailServiceChargesLogic chargeslogic;
    private static RetailSTaxesLogic staxeslogic;
    private static RetailSBTaxesLogic sbtaxeslogic;
    java.util.List<ServiceChargeInfo> chargelist = null;
    private ListKeyed chargecollection;
    private SentenceList sentcharge;
    java.util.List<TaxInfo> sertaxlist = null;
    java.util.List<TaxInfo> sbtaxlist = null;
    private ListKeyed sertaxcollection;
    private ListKeyed sbtaxcollection;
    private SentenceList sentsertax;
    private SentenceList sentSBtax;

    /** Creates new form RetailSimpleReceipt */
    public RetailSimpleReceipt(String ticketline, DataLogicSales dlSales, DataLogicCustomers dlCustomers, RetailTaxesLogic taxeslogic) {

        initComponents();

        // dlSystem.getResourceAsXML("Ticket.Line")
        ticketlines = new JRetailTicketLines(ticketline);
        this.dlCustomers = dlCustomers;
        this.dlSales = dlSales;
        this.taxeslogic = taxeslogic;

        sentcharge = dlSales.getRetailServiceChargeList();
        try {
            chargelist = sentcharge.list();
        } catch (BasicException ex) {

            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargecollection = new ListKeyed<ServiceChargeInfo>(chargelist);
        chargeslogic = new RetailServiceChargesLogic(chargelist, taxeslogic.m_App);

        sentsertax = dlSales.getRetailServiceTaxList();
        try {
            sertaxlist = sentsertax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sertaxcollection = new ListKeyed<TaxInfo>(sertaxlist);
        staxeslogic = new RetailSTaxesLogic(sertaxlist, taxeslogic.m_App);

        sentSBtax = dlSales.getRetailSwachBharatTaxList();
        try {
            sbtaxlist = sentSBtax.list();
        } catch (BasicException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        sbtaxcollection = new ListKeyed<TaxInfo>(sbtaxlist);
        sbtaxeslogic = new RetailSBTaxesLogic(sbtaxlist, taxeslogic.m_App);

        jPanel2.add(ticketlines, BorderLayout.CENTER);
    }

    public void setCustomerEnabled(boolean value) {
        btnCustomer.setEnabled(value);
    }

    public void setTicket(RetailTicketInfo ticket, Object ticketext) {

        this.ticket = ticket;
        this.ticketext = ticketext;

        // The ticket name
        m_jTicketId.setText(ticket.getName(ticketext));

        ticketlines.clearTicketLines();
        for (int i = 0; i < ticket.getLinesCount(); i++) {
            ticketlines.addTicketLine(ticket.getLine(i));

        }

        if (ticket.getLinesCount() > 0) {
            ticketlines.setSelectedIndex(0);
        }

        printTotals();

    }

    private void refreshTicketTaxes() {

        for (RetailTicketLineInfo line : ticket.getLines()) {
            line.setTaxInfo(taxeslogic.getTaxInfo(line.getProductTaxCategoryID(), ticket.getCustomer(), "N"));
        }
    }

    public void printTotals() {
        calculateDiscount(ticket.getDiscountMap());
        calculateTax();
        if (ticket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
        } else {
            m_jSubtotalEuros.setText(ticket.printSubTotalValue());
            m_jTaxesEuros.setText(ticket.printTax());
            m_jTotalEuros.setText(ticket.printTotal());
        }
    }

    private void printTotals(RetailTicketInfo ticket) {
        calculateDiscount(ticket.getDiscountMap());
        calculateTax();
        if (ticket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
        } else {
            m_jSubtotalEuros.setText(ticket.printSubTotal());
            m_jTaxesEuros.setText(ticket.printTax());
            m_jTotalEuros.setText(ticket.printTotal());
        }
    }

    public RetailTicketInfo getTicket() {
        return ticket;
    }

    private int findFirstNonAuxiliarLine() {

        int i = ticketlines.getSelectedIndex();
        while (i >= 0 && ticket.getLine(i).isProductCom()) {
            i--;
        }
        return i;
    }

    private int findFoodFirstNonAuxiliarLine() {

        int i = ticketlines.getSelectedIndex();
        while (i >= 0 && ticket.getLine(i).isProductCom()) {
            i--;
        }
        return i;
    }

    public RetailTicketLineInfo[] getSelectedLines() {

        // never returns an empty array, or null, or an array with at least one element.

        int i = findFirstNonAuxiliarLine();

        if (i >= 0) {

            List<RetailTicketLineInfo> l = new ArrayList<RetailTicketLineInfo>();

            RetailTicketLineInfo line = ticket.getLine(i);
            l.add(line);
            ticket.removeLine(i);
            ticketlines.removeTicketLine(i);

            // add also auxiliars
            while (i < ticket.getLinesCount() && ticket.getLine(i).isProductCom()) {
                l.add(ticket.getLine(i));
                ticket.removeLine(i);
                ticketlines.removeTicketLine(i);
            }
            printTotals();
            return l.toArray(new RetailTicketLineInfo[l.size()]);
        } else {
            return null;
        }
    }
    //Method is used to select all the food items and moved to right panel

    public RetailTicketLineInfo[] getFoodSelectedLines() {

        int i = findFoodFirstNonAuxiliarLine();

        if (i >= 0) {

            RetailTicketLineInfo line = ticket.getLine(i);
            if (line.getMultiply() >= 1.0) {

                List<RetailTicketLineInfo> l = new ArrayList<RetailTicketLineInfo>();

                if (line.getProductType().equals("Food")) {

                    l.add(line);
                    ticket.removeLine(i);
                    ticketlines.removeTicketLine(i);

                }

                // add also auxiliars
                while (i < ticket.getLinesCount() && ticket.getLine(i).isProductCom()) {
                    l.add(ticket.getLine(i));
                    ticket.removeLine(i);
                    ticketlines.removeTicketLine(i);
                }
                printTotals();
                return l.toArray(new RetailTicketLineInfo[l.size()]);
            } else { // < 1.0
                return null;
            }
        } else {
            return null;
        }
    }

    public void removeTicket(int index) {
        ticket.removeLine(index);
    }

    public void removeTicketLine(int index) {
        ticketlines.removeTicketLine(index);
    }
    //Method is used to list all products based on product type

    public ArrayList<String> getFoodSelectedLinesData(RetailTicketInfo retail, String productType) {

        int insertpoint = ticket.getLinesCount();
        ArrayList<String> productId = new ArrayList<String>();
        for (int i = 0; i < retail.getLinesCount(); i++) {
            if (retail.getLine(i).getProductType() != null) {
                if (retail.getLine(i).getProductType().equals(productType)) {
                    ticket.insertLine(insertpoint, retail.getLine(i));
                    ticketlines.insertTicketLine(insertpoint, retail.getLine(i));
                    String crossProduct = retail.getLine(i).getProductID();
                    productId.add(crossProduct);

                }
            }

        }
        printTotals();
        return productId;

    }
    //Method is used to add the selected item to list for moving from left panel to right panel or vice versa

    public RetailTicketLineInfo[] getSelectedLinesUnit() {

        // never returns an empty array, or null, or an array with at least one element.

        int i = findFirstNonAuxiliarLine();

        if (i >= 0) {

            RetailTicketLineInfo line = ticket.getLine(i);
            //if quantity more than 0
            if (line.getMultiply() >= 0) {
                //if it is normal item/main addon item/non promotion item/ primary promotion item , then allow to move
                if ((line.getAddonId() == null || line.getPrimaryAddon() == 1) && (line.getPromoType().equals("") || ((line.getPromoType().equals("SIBG")&& line.getIsCrossProduct().equals("Y")) && line.getActualPrice() != 0))) {

                    List<RetailTicketLineInfo> l = new ArrayList<RetailTicketLineInfo>();
                    // checking for add on item/promotion item(If not)
                    if (line.getMultiply() > 1.0 && line.getAddonId() == null && line.getPromoType().equals("")) {
                        line.setMultiply(line.getMultiply() - 1.0);
                        ticketlines.setTicketLine(i, line);
                        line = line.copyTicketLine();
                        line.setMultiply(1.0);
                        l.add(line);
                        i++;
                    } else { // == 1.0
                        l.add(line);
                        ticket.removeLine(i);
                        ticketlines.removeTicketLine(i);
                    }

                    // add also auxiliars
                    while (i < ticket.getLinesCount() && ticket.getLine(i).isProductCom()) {
                        l.add(ticket.getLine(i));
                        ticket.removeLine(i);
                        ticketlines.removeTicketLine(i);
                    }
                    //Addon logic
                    // Move the child add on items too(if exists)
                    if (line.getPrimaryAddon() == 1) {
                        // get the main primary addon id
                        String addonId = line.getAddonId();
                        int t = 0;
                        // loop the lines and check for child addons having same addon id
                        while (t < ticket.getLinesCount()) {
                            if (ticket.getLine(t).getAddonId() != null) {
                                // if it matches move the item
                                if (ticket.getLine(t).getAddonId().equals(addonId)) {
                                    l.add(ticket.getLine(t));
                                    ticket.removeLine(t);
                                    ticketlines.removeTicketLine(t);
                                } else {
                                    t++;
                                }
                            } else {
                                t++;
                            }
                        }

                    } else {
                        // Promotion logic

                        String sibgId = line.getSibgId();
                        int t = 0;
                        while (t < ticket.getLinesCount()) {

                            if (ticket.getLine(t).getSibgId() != null) {
                                if (ticket.getLine(t).getSibgId().equals(sibgId)) {

                                    l.add(ticket.getLine(t));
                                    ticket.removeLine(t);
                                    ticketlines.removeTicketLine(t);

                                } else {
                                    t++;
                                }
                            } else {
                                t++;
                            }
                        }

                    } // Promotion logic ends
                    printTotals();
                    return l.toArray(new RetailTicketLineInfo[l.size()]);
                } else {
                    return null;
                }
            } else { // < 1.0

                return null;
            }
        } else {
            return null;
        }
    }
    //Method is used for inserting the selected lines to right panel or to left panel

    public void addSelectedLines(RetailTicketLineInfo[] lines) {

        int i = findFirstNonAuxiliarLine();

        RetailTicketLineInfo firstline = lines[0];
// checking same item exist in the right panel, if yes increase the quantity.
        if (i >= 0
                && ticket.getLine(i).getProductID() != null && firstline.getProductID() != null && ticket.getLine(i).getProductID().equals(firstline.getProductID())
                && ticket.getLine(i).getTaxInfo().getId().equals(firstline.getTaxInfo().getId())
                && ticket.getLine(i).getActualPrice() == firstline.getActualPrice() && ticket.getLine(i).getSibgId() == null) {
            // add the auxiliars.
            for (int j = 1; j < lines.length; j++) {
                ticket.insertLine(i + 1, lines[j]);
                ticketlines.insertTicketLine(i + 1, lines[j]);
            }

            // inc the line
            ticket.getLine(i).setMultiply(ticket.getLine(i).getMultiply() + firstline.getMultiply());
            ticketlines.setTicketLine(i, ticket.getLine(i));
            ticketlines.setSelectedIndex(i);

        } else {
            // add all at the end in inverse order.
            int insertpoint = ticket.getLinesCount();
            for (int j = lines.length - 1; j >= 0; j--) {
                ticket.insertLine(insertpoint, lines[j]);
                ticketlines.insertTicketLine(insertpoint, lines[j]);
            }
        }

        printTotals();
    }

    public void addFoodSelectedLines(RetailTicketLineInfo[] lines) {


        int i = findFirstNonAuxiliarLine();

        RetailTicketLineInfo firstline = lines[0];

        if (i >= 0
                && ticket.getLine(i).getProductID() != null && firstline.getProductID() != null && ticket.getLine(i).getProductID().equals(firstline.getProductID())
                && ticket.getLine(i).getTaxInfo().getId().equals(firstline.getTaxInfo().getId())
                && ticket.getLine(i).getPrice() == firstline.getPrice()) {

            // add the auxiliars.
            for (int j = 1; j < lines.length; j++) {
                ticket.insertLine(i + 1, lines[j]);
                ticketlines.insertTicketLine(i + 1, lines[j]);
            }

            // inc the line
            ticket.getLine(i).setMultiply(ticket.getLine(i).getMultiply() + firstline.getMultiply());
            ticketlines.setTicketLine(i, ticket.getLine(i));
            ticketlines.setSelectedIndex(i);

        } else {
            // add all at the end in inverse order.
            int insertpoint = ticket.getLinesCount();
            for (int j = lines.length - 1; j >= 0; j--) {
                if (lines[j].getProductType().equals("Food")) {
                    ticket.insertLine(insertpoint, lines[j]);
                    ticketlines.insertTicketLine(insertpoint, lines[j]);
                }
            }
        }

        printTotals();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        btnCustomer = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        m_jPanTotals.setLayout(new java.awt.GridBagLayout());

        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jTotalEuros, gridBagConstraints);

        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros1, gridBagConstraints);

        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        m_jPanTotals.add(m_jSubtotalEuros, gridBagConstraints);

        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jTaxesEuros, gridBagConstraints);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros2, gridBagConstraints);

        m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        m_jPanTotals.add(m_jLblTotalEuros3, gridBagConstraints);

        jLabel1.setText("Subtotal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        m_jPanTotals.add(jLabel1, gridBagConstraints);

        jPanel1.add(m_jPanTotals, java.awt.BorderLayout.EAST);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

        m_jButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        btnCustomer.setFocusPainted(false);
        btnCustomer.setFocusable(false);
        btnCustomer.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnCustomer.setRequestFocusEnabled(false);
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        m_jButtons.add(btnCustomer);

        add(m_jButtons, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setLayout(new java.awt.BorderLayout());
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.search(ticket.getCustomer());
        finder.setVisible(true);

        try {
            ticket.setCustomer(finder.getSelectedCustomer() == null
                    ? null
                    : dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()));
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
            msg.show(this);
        }

        // The ticket name
        m_jTicketId.setText(ticket.getName(ticketext));

        refreshTicketTaxes();

        // refresh the receipt....
        setTicket(ticket, ticketext);

    }//GEN-LAST:event_btnCustomerActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JLabel m_jSubtotalEuros;
    private javax.swing.JLabel m_jTaxesEuros;
    private javax.swing.JLabel m_jTicketId;
    private javax.swing.JLabel m_jTotalEuros;
    // End of variables declaration//GEN-END:variables

    //Method is used for calculating the tax
    private void calculateTax() {
        try {
            taxeslogic.calculateTaxes(ticket);
        } catch (TaxesException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method is used for calculating the discount

    private void calculateDiscount(Map<String, DiscountInfo> discountMap) {
        if (discountMap != null) {
            Set<String> keys = discountMap.keySet();
            for (int i = 0; i < ticket.getLinesCount(); i++) {
                ticket.getLine(i).setDiscountrate("");
                String parentCatId = ticket.getLine(i).getParentCatId();
                String discount = "";
                //Checking whether discount assigned for any product category 
                if (keys.contains(parentCatId)) {
                    //if its a rate
                    if (!discountMap.get(parentCatId).getDiscountRate().equals("") && !discountMap.get(parentCatId).getDiscountRate().equals(null)) {
                        discount = discountMap.get(parentCatId).getDiscountRate();
                        ticket.getLine(i).setDiscountrate(discount);
                    }//if its a amount
                    else {
                        discount = discountMap.get(parentCatId).getDiscountValue();
                        ticket.getLine(i).setDiscountrate(discount);
                    }
                }
            }
        }
    }
}
