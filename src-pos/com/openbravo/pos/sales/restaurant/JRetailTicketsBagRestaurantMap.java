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
package com.openbravo.pos.sales.restaurant;

import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.forms.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.printer.printer.KotBillPrinter;
import com.openbravo.pos.printer.printer.KotImagePrinter;
import com.openbravo.pos.printer.printer.TicketLineConstructor;
import com.openbravo.pos.ticket.MenuInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.apache.commons.lang.WordUtils;

public class JRetailTicketsBagRestaurantMap extends JRetailTicketsBag {

    private static boolean status;
    private static String tableId;
    private static String newTableId;
    private static String newTableName;
    private java.util.List<Place> m_aplaces;
    private java.util.List<Floor> m_afloors;
    private JRetailTicketsBagRestaurant m_restaurantmap;
    private JRetailTicketsBagRestaurantRes m_jreservations;
    private Place m_PlaceCurrent;
    private java.util.List<ProductionPrinterInfo> printerInfo;
    // State vars
    private Place m_PlaceClipboard;
    private CustomerInfo customer;
    private java.util.List<RetailTicketLineInfo> kotTicketLinelist;
    private DataLogicReceipts dlReceipts = null;
    private DataLogicSales dlSales = null;
    private DataLogicSystem dlSystem = null;
    private AppView m_app;
    private static String tableName;
    private int noOfCovers;
    private RetailTicketInfo retailTicket;
    private String oldTableId;
    private static RetailTicketInfo ticket = null;
    private java.util.List<kotInfo> kotTicketlist;
    Logger kotlogger = Logger.getLogger("KotLog");
    //Timer for loading the table details for every 2 minutes
    private RefreshTickets autoRefresh = new RefreshTickets();
    private Timer timer = new Timer(120000, autoRefresh);
    private JRootApp m_RootApp;
    Logger logger = Logger.getLogger("MyLog");
    private String SplitTableMoveId;
    private boolean movedSplitTable;
    private static String splitId;

    /**
     * Creates new form JTicketsBagRestaurant
     */
    public JRetailTicketsBagRestaurantMap(AppView app, RetailTicketsEditor panelticket, String businessType) {

        super(app, panelticket);
        this.m_App = app;
        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");

        m_restaurantmap = new JRetailTicketsBagRestaurant(app, this);
        m_PlaceCurrent = null;
        m_PlaceClipboard = null;
        customer = null;
        //Select all the floor details from db for showing the floor name in screen
        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    "SELECT ID, NAME, IMAGE FROM FLOORS ORDER BY NAME",
                    null,
                    new SerializerReadClass(Floor.class));
            m_afloors = sent.list();



        } catch (BasicException eD) {
            m_afloors = new ArrayList<Floor>();
        }
        //Select all the tables details from db for showing the table names in screen
        try {
            SentenceList sent = new StaticSentence(
                    app.getSession(),
                    "SELECT ID, NAME, X, Y, FLOOR FROM PLACES WHERE NAME NOT LIKE 'takeaway' ORDER BY FLOOR",
                    null,
                    new SerializerReadClass(Place.class));
            m_aplaces = sent.list();
        } catch (BasicException eD) {
            m_aplaces = new ArrayList<Place>();
        }
        //Initialise the components
        initComponents();

        // add the Floors containers
        if (m_afloors.size() > 1) {
            // A tab container for 2 or more floors
            JTabbedPane jTabFloors = new JTabbedPane();
            jTabFloors.applyComponentOrientation(getComponentOrientation());
            jTabFloors.setBorder(new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)));
            jTabFloors.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            jTabFloors.setFocusable(false);
            jTabFloors.setRequestFocusEnabled(false);
            m_jPanelMap.add(jTabFloors, BorderLayout.CENTER);

            for (Floor f : m_afloors) {
                f.getContainer().applyComponentOrientation(getComponentOrientation());

                JScrollPane jScrCont = new JScrollPane();
                jScrCont.applyComponentOrientation(getComponentOrientation());
                JPanel jPanCont = new JPanel();
                jPanCont.applyComponentOrientation(getComponentOrientation());

                jTabFloors.addTab(f.getName(), f.getIcon(), jScrCont);
                jScrCont.setViewportView(jPanCont);
                jPanCont.add(f.getContainer());
            }
        } else if (m_afloors.size() == 1) {
            // Just a frame for 1 floor
            Floor f = m_afloors.get(0);
            f.getContainer().applyComponentOrientation(getComponentOrientation());

            JPanel jPlaces = new JPanel();
            jPlaces.applyComponentOrientation(getComponentOrientation());
            jPlaces.setLayout(new BorderLayout());

            jPlaces.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.EmptyBorder(new Insets(5, 5, 5, 5)),
                    new javax.swing.border.TitledBorder(f.getName())));
            JScrollPane jScrCont = new JScrollPane();
            jScrCont.applyComponentOrientation(getComponentOrientation());
            JPanel jPanCont = new JPanel();
            jPanCont.applyComponentOrientation(getComponentOrientation());
            m_jPanelMap.add(jPlaces, BorderLayout.CENTER);
            jPlaces.add(jScrCont, BorderLayout.CENTER);
            jScrCont.setViewportView(jPanCont);
            jPanCont.add(f.getContainer());
        }

        // Add all the Table buttons.
        Floor currfloor = null;

        for (Place pl : m_aplaces) {
            int iFloor = 0;

            if (currfloor == null || !currfloor.getID().equals(pl.getFloor())) {
                // Look for a new floor
                do {
                    currfloor = m_afloors.get(iFloor++);
                } while (!currfloor.getID().equals(pl.getFloor()));
            }

            currfloor.getContainer().add(pl.getButton());
            pl.setButtonBounds();
            pl.getButton().addActionListener(new MyActionListener(pl));
        }

        // Add the reservations panel
        m_jreservations = new JRetailTicketsBagRestaurantRes(app, this);
        add(m_jreservations, "res");
    }

    public void activate() {

        m_PlaceClipboard = null;
        customer = null;
        m_jbtnReservations.setVisible(false);
        loadTickets();
        timer.start();
        printState();
        m_panelticket.setRetailActiveTicket(null, null);
        m_restaurantmap.activate(dlReceipts);
        showView("map"); // arrancamos en la vista de las mesas.

        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }

    public boolean deactivate() {

        // precondicion es que tenemos ticket activado aqui y ticket en el panel

        if (viewTables()) {

            // borramos el clipboard
            m_PlaceClipboard = null;
            customer = null;

            // guardamos el ticket
            if (m_PlaceCurrent != null) {
                try {
                    //Setting the table as open so that other user can enter to table
                    m_panelticket.getActiveTicket().setTicketOpen(false);
                    dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
                } catch (BasicException e) {
                    new MessageInf(e).show(this);
                } catch (NullPointerException e) {
                }

                m_PlaceCurrent = null;
            }

            // desactivamos cositas.
            printState();
            m_panelticket.setRetailActiveTicket(null, null);
            timer.stop();
            return true;
        } else {
            timer.stop();
            return false;
        }
        // postcondicion es que no tenemos ticket activado       
    }

    protected JComponent getBagComponent() {
        return m_restaurantmap;
    }

    protected JComponent getNullComponent() {
        return this;
    }

    public void moveTicket() {
        logger.info("Move Ticket Action Table name: " + m_panelticket.getActiveTicket().getTableName());
        //adding condition for move table not to allow if the table having zero line items
        if (m_panelticket.getActiveTicket().getLinesCount() == 0) {
            showMessage(this, "Table cannot be moved because it has no items");
        } //Checking whether bill is printed
        else if ((m_panelticket.getActiveTicket().isPrinted()) && !(m_panelticket.getActiveTicket().isListModified())) {
            showMessage(this, "Table cannot be moved because bill is printed");
        } else {
            //checking any kot item present?
            int kotcount = 0;
            for (int k = 0; k < m_panelticket.getActiveTicket().getLinesCount(); k++) {
                if (m_panelticket.getActiveTicket().getLine(k).getIsKot() == 1) {
                    kotcount = 1;
                    break;
                }
            }
// if non kot bill then table cannot be moved
            if (kotcount == 0) {
                showMessage(this, "Table cannot be moved because it has non kot items");
            } else {
                //removing all non kot items while moving the table
                int i = 0;
                while (i < m_panelticket.getActiveTicket().getLinesCount()) {
                    if (m_panelticket.getActiveTicket().getLine(i).getIsKot() == 0) {
                        m_panelticket.getActiveTicket().removeLine(i);
                        i = 0;
                    } else {
                        i++;
                    }
                }
                m_panelticket.getActiveTicket().refreshTxtFields(0);
                if (m_PlaceCurrent != null) {
                    try {//Checking whether we are moving splitted bill
                        if (m_panelticket.getActiveTicket().getSplitValue().equals("Split")) {
                            movedSplitTable = true;
                            m_panelticket.getActiveTicket().setSplitValue("");
                        }
                        //Saving into a variable move table id
                        SplitTableMoveId = m_panelticket.getActiveTicket().getSplitSharedId();
                        dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
                    } catch (BasicException e) {
                        logger.info("move table exception " + e.getMessage());
                        new MessageInf(e).show(this);
                    }

                    // me guardo el ticket que quiero copiar.
                    m_PlaceClipboard = m_PlaceCurrent;
                    customer = null;
                    m_PlaceCurrent = null;
                }

                printState();
                m_panelticket.setRetailActiveTicket(null, null);
                loadTickets();
                startTimer();
            }
        }
    }

    private void showMessage(JRetailTicketsBagRestaurantMap aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                JOptionPane.INFORMATION_MESSAGE);

    }
    //Setting font and color of the label

    private JPanel getLabelPanel(String msg) {
        JPanel panel = new JPanel();
        Font font = new Font("Verdana", Font.BOLD, 12);
        panel.setFont(font);
        panel.setOpaque(true);
        JLabel label = new JLabel(msg, JLabel.LEFT);
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panel.add(label);
        return panel;
    }

    public boolean viewTables(CustomerInfo c) {
        if (m_jreservations.deactivate()) {
            showView("map"); // arrancamos en la vista de las mesas.

            m_PlaceClipboard = null;
            customer = c;
            printState();

            return true;
        } else {
            return false;
        }
    }

    public boolean viewTables() {
        return viewTables(null);
    }
    //Method is called when click on table button inside sales screen

    public void newTicket() {

        if (m_PlaceCurrent != null) {
            // if there is no kot items
            logger.info("exit Table Action Table name: " + m_panelticket.getActiveTicket().getTableName());
            if (m_panelticket.getActiveTicket().getOrderId() == 0) {
                try {
                    logger.info("Order No." + m_panelticket.getActiveTicket().getOrderId() + " deleting 0 order no. Bill in newticket method of  " + m_panelticket.getActiveTicket().getTableName() + " id is " + m_PlaceCurrent.getId());
                    dlReceipts.deleteSharedTicket(m_PlaceCurrent.getId());
                    deleteTicket();
                } catch (BasicException ex) {
                    Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
                }
            }//if kot done but cancelled all lines then save the bill as cancel ticket and remove from shared tickets
            else if (m_panelticket.getActiveTicket().getOrderId() != 0 && m_panelticket.getActiveTicket().getLinesCount() == 0) {
                m_panelticket.getActiveTicket().setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                m_panelticket.getActiveTicket().setActiveCash(m_App.getActiveCashIndex());
                m_panelticket.getActiveTicket().setActiveDay(m_App.getActiveDayIndex());
                m_panelticket.getActiveTicket().setDate(new Date()); //
                String ticketDocument;
                ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_panelticket.getActiveTicket().getTicketId();
                String reason = "Splitted with zero lines/cancelled all kot lines";
                try {
                    dlSales.saveRetailCancelTicket(m_panelticket.getActiveTicket(), m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                } catch (BasicException ex) {
                    logger.info("newTicket saveRetailCancelTicket exception " + ex.getMessage());
                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (m_panelticket.getActiveTicket().getSplitValue().equals("")) {
                    deleteTicket();
                }
//                else {
//                    try {
//                        dlReceipts.deleteSharedSplitTicket(m_panelticket.getActiveTicket().getPlaceId(), m_panelticket.getActiveTicket().getSplitSharedId());
//                    } catch (BasicException ex) {
//                        logger.info("newTicket deleteSharedSplitTicket exception " + ex.getMessage());
//                        Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
            }//if partially kot and partially non kot items present
            else {
                int i = 0;
                //Removing non kot items
                while (i < m_panelticket.getActiveTicket().getLinesCount()) {
                    if (m_panelticket.getActiveTicket().getLine(i).getIsKot() == 0) {
                        m_panelticket.getActiveTicket().removeLine(i);
                        i = 0;
                    } else {
                        i++;
                    }
                }
                m_panelticket.getActiveTicket().refreshTxtFields(0);
                m_panelticket.getActiveTicket().setTicketOpen(false);

                if (m_panelticket.getActiveTicket().getLinesCount() == 0) {
                    logger.info("Order No." + m_panelticket.getActiveTicket().getOrderId() + "deleting partially kot and partially non kot items Bill in newticket method of " + m_panelticket.getActiveTicket().getTableName());
                    if (m_panelticket.getActiveTicket().getOrderId() != 0) {
                        m_panelticket.getActiveTicket().setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
                        m_panelticket.getActiveTicket().setActiveCash(m_App.getActiveCashIndex());
                        m_panelticket.getActiveTicket().setActiveDay(m_App.getActiveDayIndex());
                        m_panelticket.getActiveTicket().setDate(new Date()); //
                        String ticketDocument;
                        ticketDocument = m_App.getProperties().getStoreName() + "-" + m_App.getProperties().getPosNo() + "-" + m_panelticket.getActiveTicket().getTicketId();
                        String reason = "cancelled all kot lines";
                        try {
                            dlSales.saveRetailCancelTicket(m_panelticket.getActiveTicket(), m_App.getProperties().getStoreName(), ticketDocument, "Y", m_App.getInventoryLocation(), reason, "", m_App.getProperties().getPosNo(), "N");
                        } catch (BasicException ex) {
                            logger.info("newTicket saveRetailCancelTicket exception 2" + ex.getMessage() + ";");
                            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                   if (m_panelticket.getActiveTicket().getSplitValue().equals("")) {
                            deleteTicket();
                          }
              } else {
                    try {
                        dlReceipts.updateSharedTicket(m_PlaceCurrent.getId(), m_panelticket.getActiveTicket());
                    } catch (BasicException e) {
                        logger.info("newTicket updateSharedTicket exception " + e.getMessage());
                        new MessageInf(e).show(this); // maybe other guy deleted it
                    }
                    m_PlaceCurrent = null;
                }
            }
            printState();
            m_panelticket.setRetailActiveTicket(null, null);
        }
    }
    //Method is used for holding the bill

    public void newHoldTicket(RetailTicketInfo ticket) {
        if (m_PlaceCurrent != null) {
            try {
                dlSales.SaveHoldTicket(ticket);
            } catch (BasicException ex) {
                Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setRetailActiveTicket(null, null);
    }
    //Method is called when click on split bill button inside sales screen

    public void newsplitTicket(RetailTicketInfo ticket1, RetailTicketInfo ticket2) {

        if (m_PlaceCurrent != null) {
            logger.info("newsplitTicket Action Table name: " + m_panelticket.getActiveTicket().getTableName());
            m_panelticket.getActiveTicket().refreshTxtFields(0);
            try {
                ticket1.setTicketOpen(false);
                ticket2.setTicketOpen(false);
                // one bill will be updated and another will be newly inserted for the same table id
                dlReceipts.updateSharedSplitTicket(m_PlaceCurrent.getId(), ticket1);
                dlReceipts.insertRetailSharedTicket(m_PlaceCurrent.getId(), ticket2);
                //table covers are inserted for the second bill with new split id
                dlReceipts.insertTableCovers(UUID.randomUUID().toString(), m_PlaceCurrent.getId(), ticket1.getNoOfCovers(), ticket2.getSplitSharedId());
            } catch (BasicException e) {
                logger.info("newsplitTicket insertRetailSharedTicket exception " + e.getMessage());
                new MessageInf(e).show(this); // maybe other guy deleted it
            }
            m_PlaceCurrent.setIsSplit(1);
            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setRetailActiveTicket(null, null);
    }
    //Method is called after the ticket settled

    public void deleteTicket() {

        if (m_PlaceCurrent != null) {
            logger.info("clearing the table in deleteTicket method of map ");
//            String id = m_PlaceCurrent.getId();
//            try {
//                dlReceipts.deleteSharedTicket(id);
//                m_PlaceCurrent.setPeople(false);
//
//            } catch (BasicException e) {
//                logger.info("deleteTicket in map class exception " + e.getMessage());
//                new MessageInf(e).show(this);
//            }
            m_PlaceCurrent = null;
        }

        printState();
        m_panelticket.setRetailActiveTicket(null, null);
        loadTickets();
    }

    //This method is called when the cancel ticket functionality
    public void deleteCancelTicket() {
        String id = m_PlaceCurrent.getId();
        if (m_PlaceCurrent != null) {
            logger.info("deleteCancelTicket Action Table name: " + m_panelticket.getActiveTicket().getTableName());
            RetailTicketInfo ticket = m_panelticket.getActiveTicket();

            java.util.List<RetailTicketLineInfo> panelLines = ticket.getLines();
            //Popup window for enetring the cancel reason
            JCancelReasonEditor.showMessage(this, dlReceipts, ticket, "Y", "N", dlSales, dlSystem, m_App, m_PlaceCurrent.getName(), m_PlaceCurrent);

            /*if setFlag is true for JCancelReasonEditor, then delete records from sharedTickets & set its People as false
             , this Place object as null and this Active ticket as null*/
            if (JCancelReasonEditor.getFlag() == true) {
                try {
                    if (ticket.getSplitValue().equals("Split")) {
                      //  dlReceipts.deleteSharedSplitTicket(id, ticket.getSplitSharedId());
                        //added 22/09/2016
                        kotCancelDisplay(ticket, panelLines);
                        m_PlaceCurrent.setPeople(true);
                    } else {
                     //   dlReceipts.deleteSharedTicket(id);
                        //Method is used for printing the cancel kot items
                        kotCancelDisplay(ticket, panelLines);

                        m_PlaceCurrent.setPeople(false);
                    }
                    m_PlaceCurrent = null;
                    printState();
                    m_panelticket.setRetailActiveTicket(null, null);
                    loadTickets();

                } catch (BasicException e) {
                    logger.info("deletecancelTicket in map class exception " + e.getMessage());
                    new MessageInf(e).show(this);
                }
            }
        }
    }
    //This method is called when kot items cancelled or on cancelling the ticket

    public final synchronized void kotCancelDisplay(RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> panelLines) throws BasicException {

        final int kotTicket = 0;
        final java.util.List<RetailTicketLineInfo> panelNonKotLines = new ArrayList();


        String sessionId = null;
        sessionId = dlReceipts.getFloorId(ticket.getPlaceId());
        printerInfo = dlReceipts.getPrinterInfo(sessionId);
        for (int i = 0; i < panelLines.size(); i++) {
            if (panelLines.get(i).getIsKot() == 1) {
                panelLines.get(i).setKotdate(ticket.getDate());
                panelLines.get(i).setKottable(ticket.getPlaceId());
                panelLines.get(i).setKotuser(ticket.getUser().getId());
                panelNonKotLines.add(panelLines.get(i));

            }

        }
        //This Method is called for printing cancel kot items
        printRetailCancelKotTicket("Printer.Kot", ticket, panelNonKotLines, null, printerInfo, kotTicket);
        Runtime.getRuntime().gc();
    }
    //This Method is called for printing cancel kot items

    private synchronized void printRetailCancelKotTicket(String sresourcename, RetailTicketInfo ticket, java.util.List<RetailTicketLineInfo> kot, Object ticketExt, java.util.List<ProductionPrinterInfo> printerInfo, int kotTicket) {
        java.util.List<TicketLineConstructor> allLines = null;
        logger.info("start printing the kot" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        com.openbravo.pos.printer.printer.KotImagePrinter printer = new KotImagePrinter();
        com.openbravo.pos.printer.printer.KotBillPrinter printerKot = new KotBillPrinter();

        String storeLocation = m_App.getProperties().getProperty("machine.storelocation");
        kotTicketLinelist = kot;
        for (int j = 0; j < printerInfo.size(); j++) {
            java.util.List<RetailTicketLineInfo> uniqueProductionAreas = new ArrayList<RetailTicketLineInfo>();
            for (int i = 0; i < kotTicketLinelist.size(); i++) {
                if (printerInfo.get(j).getProductionAreaType().equals(kotTicketLinelist.get(i).getProductionAreaType())) {
                    uniqueProductionAreas.add(kotTicketLinelist.get(i));
                    kotTicketLinelist.get(i).setProductionArea(printerInfo.get(j).getProductionArea());
                }
            }
            logger.info("kot print count based on production areas" + uniqueProductionAreas.size());
            if (uniqueProductionAreas.size() != 0) {
                allLines = getRetailAllLines(ticket, ticketExt, uniqueProductionAreas, kotTicket);
                try {//Based on storelocation
                    if (storeLocation.equals("BlrIndranagar") || storeLocation.equals("BlrKoramangala") || storeLocation.equals("Chennai") || storeLocation.equals("Hyderabad")) {

                        printer.printKot(allLines, printerInfo.get(j).getPath());
                    } else {
                        printerKot.print(allLines, printerInfo.get(j).getPath());
                    }
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        kotlogger.info("Cancel Bill KOT Printed Successfully " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + ticket.getTableName().toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(1);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(4);
                        }

                    }
                } catch (PrinterException ex) {
                    logger.info("Order NO." + ticket.getOrderId() + " The printer action" + ex.getMessage());

                    Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
                    for (int i = 0; i < uniqueProductionAreas.size(); i++) {
                        logger.info("Cancel Bill KOT Print Failed  " + "," + "Username: " + ticket.printUser() + "," + "Total kot count: " + uniqueProductionAreas.size() + "," + "Printer Name: " + printerInfo.get(j).getPath() + "," + "Kot No: " + kotTicket + "," + "Table: " + ticket.getTableName().toString() + "," + "Order No: " + ticket.getOrderId() + "," + "Product Name: " + uniqueProductionAreas.get(i).getProductName() + "," + "Qty: " + uniqueProductionAreas.get(i).getMultiply() + "," + "Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                        uniqueProductionAreas.get(i).setIsKot(0);
                        if (uniqueProductionAreas.get(i).getPreparationStatus() != 3) {
                            uniqueProductionAreas.get(i).setPreparationStatus(0);
                        }
                    }

                    showMessage(this, "KOT action not happened! Please retry");
                }
            }
        }
    }
    //Setting all the lines for printing and aligning the data

    private java.util.List<TicketLineConstructor> getRetailAllLines(RetailTicketInfo ticket, Object ticketext, java.util.List<RetailTicketLineInfo> kot, int kotTicket) {

        java.util.List<TicketLineConstructor> allLines = new ArrayList<TicketLineConstructor>();
        double qtySum = 0;
        allLines.add(new TicketLineConstructor("      KITCHEN CANCEL ORDER TICKET"));
        allLines.add(new TicketLineConstructor("Date: " + (ticket.printDate().substring(0, 12)) + getSpaces(13 - (ticket.printDate().substring(0, 12).length()))));
        allLines.add(new TicketLineConstructor("Time: " + (ticket.printTime()) + getSpaces(13 - (ticket.printTime().length())) + "Table No:" + getSpaces(1) + ticket.getTableName()));
        allLines.add(new TicketLineConstructor("User Name: " + (ticket.getUser()).getName()));
        allLines.add(new TicketLineConstructor(getDottedLine(40)));
        for (RetailTicketLineInfo tLine : kot) {
            String prodName = tLine.printName();
            String qty = tLine.printMultiply();
            qtySum = qtySum + tLine.getMultiply();
            String instruction = tLine.printInstruction();
            if (prodName.length() > 28) {
                prodName = WordUtils.wrap(prodName, 28);
                String[] prodNameArray = prodName.split("\n");
                for (int i = 0; i < prodNameArray.length - 1; i++) {
                    allLines.add(new TicketLineConstructor(prodNameArray[i]));
                }

                allLines.add(new TicketLineConstructor(prodNameArray[prodNameArray.length - 1] + getSpaces(30 - prodNameArray[prodNameArray.length - 1].length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            } else {
                allLines.add(new TicketLineConstructor(prodName + getSpaces(30 - prodName.length()) + qty));
                if (instruction != null) {
                    if (instruction.length() > 0) {
                        String[] splitInstructValue = instruction.split(";");
                        for (int i = 0; i < splitInstructValue.length; i++) {
                            if (splitInstructValue[i].length() > 0) {
                                allLines.add(new TicketLineConstructor("I-[" + splitInstructValue[i] + "]"));
                            }
                        }
                    }
                }
            }

        }
        allLines.add(new TicketLineConstructor("  "));
        allLines.add(new TicketLineConstructor("  "));
        allLines.add(new TicketLineConstructor(" "));
        allLines.add(new TicketLineConstructor(" "));
        allLines.add(new TicketLineConstructor("------------------ "));
        logger.info("sum of kot quantities = " + qtySum);
        return allLines;
    }

    private String getDottedLine(int len) {
        String dotLine = "";
        for (int i = 0; i < len; i++) {
            dotLine = dotLine + "-";
        }
        return dotLine;
    }

    private String getSpaces(int len) {
        String spaces = "";
        for (int i = 0; i < len; i++) {
            spaces = spaces + " ";
        }
        return spaces;
    }

    public void loadTickets() {
        // list for all sharedtickets database table entries
        java.util.List<SharedTicketInfo> runningTablesList = null;
        // Map for Partial detail of SharedTicketInfo Object i.e <ID as key and ISPRINTED as value> 
        Map<String, String> runningTablesMap = new HashMap<String, String>();
        String key = null;
        String value = null;
        Boolean isPrinted = null;
        Boolean isModified = null;
        try {
            runningTablesList = dlReceipts.getSharedTicketList();//retrieve sharedticketes data from db 
            //Iterate over all sharedtickets and create HashMap for <ID,ISPRINTED> details
            for (SharedTicketInfo sharedTicket : runningTablesList) {

                key = sharedTicket.getId();
                isPrinted = sharedTicket.getPrinted();
                isModified = sharedTicket.getModified();
                if ((isPrinted & isModified) || !(isPrinted)) {
                    value = "RED";
                } else {
                    value = "BLUE";
                }
                runningTablesMap.put(key, value); //add partial detail in Map.
            }
        } catch (BasicException e) {
            logger.info("loadTickets in map class exception " + e.getMessage());
            // new MessageInf(e).show(this);
            checkDbConnection();
        }

        //Iterate of all the tables present in this floor.
        for (Place table : m_aplaces) {
            //table.setPeople(atickets.contains(table.getId()));
            if (runningTablesMap.containsKey(table.getId())) {//if current table is present in my map as key
                //retrieve the value of same key entry from map of shared tickets
                String busyTableColor = runningTablesMap.get(table.getId());
                //If current table is already being printed ,display yellow button ,if not then red button.
                table.setPeople(true);
                table.setColor(busyTableColor);
            } else {
                //if table is not busy or occupied , then display button as Green color.
                table.setPeople(false);
                table.setColor("CYAN");
            }
        }
    }
    //Timer for loading the ticket

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    private void printState() {

        if (m_PlaceClipboard == null) {
            if (customer == null) {
                // Select a table
                m_jText.setText(null);
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(true);
                }
                m_jbtnReservations.setEnabled(true);
            } else {
                // receive a customer
                m_jText.setText(AppLocal.getIntString("label.restaurantcustomer", new Object[]{customer.getName()}));
                // Enable all tables
                for (Place place : m_aplaces) {
                    place.getButton().setEnabled(!place.hasPeople());
                }
                m_jbtnReservations.setEnabled(false);
            }
        } else {
            // Moving or merging the receipt to another table
            m_jText.setText(AppLocal.getIntString("label.restaurantmove", new Object[]{m_PlaceClipboard.getName()}));
            // Enable all empty tables and origin table.
            for (Place place : m_aplaces) {
                place.getButton().setEnabled(true);
            }
            m_jbtnReservations.setEnabled(false);
        }
    }
    //Method for getting the ticket information based on table

    public RetailTicketInfo getTicketInfo(Place place) {
        RetailTicketInfo ticket = null;
        SharedTicketNameInfo sharedticket = null;
        try {
            ticket = dlReceipts.getRetailSharedTicket(place.getId());
        } catch (BasicException e) {
            logger.info("getTicketInfo in map class exception " + e.getMessage());
            //new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this);
            return null;
        }
        return ticket;
    }

    //method to get multiple tickets belongs to same table
    public java.util.List<SharedSplitTicketInfo> getSplitTicketInfo(Place place) {
        java.util.List<SharedSplitTicketInfo> splitticket = null;
        try {
            splitticket = dlReceipts.getRetailSharedSplitTicket(place.getId());
        } catch (BasicException e) {
            logger.info("getSplitTicketInfo in map class exception " + e.getMessage());
            new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this);
            return null;
        }
        return splitticket;
    }

    //method to get content of selected ticket of split bill pop up
    public RetailTicketInfo getTicketInfo(Place place, String splitId) {
        RetailTicketInfo ticket = null;
        try {
            ticket = dlReceipts.getRetailSharedTicketSplit(place.getId(), splitId);
        } catch (BasicException e) {
            logger.info("getTicketInfo in map class exception " + e.getMessage());
            //  new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this);
            return null;
        }
        return ticket;
    }
    //Method is called on click on table action method

    private void setActivePlace(Place place, RetailTicketInfo ticket) {

        this.retailTicket = ticket;
        m_PlaceCurrent = place;
        //set place id to the newly adding shared ticket
        ticket.setPlaceid(place.getId());
        //when no people
        if (place.hasPeople() == false) {
            setStatus(true);
            JTableCover.showMessage(JRetailTicketsBagRestaurantMap.this, dlReceipts, ticket, place, m_panelticket, this, true);

        } else {
            //when move table
            if (getNewTableId() != null) {
                ticket.setOldTableName(ticket.getTableName());
                // setRetailActiveTicket= entering to billing screen
                m_panelticket.setRetailActiveTicket(ticket, m_PlaceCurrent.getName());
                ticket.setPlaceid(getNewTableId());
                setTable(getNewTableName());
                //Saving the details of move table action into the actionlog table
                dlSales.insertActionsLog("Move Table", ticket.getUser().getId(), m_App.getProperties().getPosNo(), ticket.getTicketId(), new Date(), getOldTableId(), getNewTableId(), null);
                m_restaurantmap.setTableName(getNewTableId(), ticket.getSplitSharedId());
                //If try to move the splitted table
                if (movedSplitTable) {
                    try {
                        dlReceipts.insertTableCovers(UUID.randomUUID().toString(), getOldTableId(), ticket.getNoOfCovers(), ticket.getSplitSharedId());
                    } catch (BasicException ex) {
                        Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    movedSplitTable = false;
                } else {
                    dlReceipts.updateTableName(getOldTableId(), getNewTableId(), ticket.getSplitSharedId());
                }
                ticket.setNoOfCovers(getTableCovers(getNewTableId(), ticket.getSplitSharedId()));
                //added new line to indicate that the table has been moved
                setNewTableId(null);

            }//when people
            else {
                //adding logic to split ticket to hold multiple tickets of same table
                java.util.List<SharedSplitTicketInfo> splitticket = getSplitTicketInfo(place);
                if (splitticket.size() > 1) {
                    int status = 0;
                    for (SharedSplitTicketInfo splitInfo : splitticket) {
                        ticket = getTicketInfo(place, splitInfo.getSplitId());
                        // checking any of the splited bill is occupied by user
                        if (ticket.isTicketOpen()) {
                            status = 1;
                            JOptionPane.showMessageDialog(JRetailTicketsBagRestaurantMap.this, ticket.printUser() + " has already logged in this Table!", "Order in Progress", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                    if (status == 0) {
                        // show splited bills panel which returns selected bill splitid
                        String splitId = JSplitBillPanel.showMessage(JRetailTicketsBagRestaurantMap.this, dlReceipts, splitticket, place, m_panelticket);
                        ticket = getTicketInfo(place, splitId);
                        ticket.setPlaceid(place.getId());
                    } else {//somebody is accessing
                        ticket = null;
                    }
                } else {
                    //if there is only last split bill ticket
                    ticket.setSplitValue("");
                }
                if (ticket != null) {
                    setStatus(false);
                    m_panelticket.setRetailActiveTicket(ticket, m_PlaceCurrent.getName());
                    ticket.setPlaceid(m_PlaceCurrent.getId());
                    setTable(m_PlaceCurrent.getName());
                    ticket.setNoOfCovers(getTableCovers(m_PlaceCurrent.getId(), ticket.getSplitSharedId()));
                }
            }
        }
        if (ticket != null) {
            setSplitId(ticket.getSplitSharedId());
            retailTicket = ticket;
        }
        setTableId(place.getId());
        m_restaurantmap.setTableName(place.getId(), getSplitId());


    }

    public int getTableCovers(String tableId, String splitId) {
        int noOfCovers = 0;
        try {
            try {
                noOfCovers = dlReceipts.getTableCovers(tableId, splitId);
            } catch (NullPointerException ex) {
                noOfCovers = 2;
            }
        } catch (BasicException ex) {
            logger.info("getTableCovers in map class exception " + ex.getMessage());
            Logger.getLogger(JTableCover.class.getName()).log(Level.SEVERE, null, ex);
        }

        return noOfCovers;
    }

    public static String getTable() {
        return tableName;
    }

    public void setTable(String tableName) {
        this.tableName = tableName;
    }

    public static String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public static String getNewTableId() {
        return newTableId;
    }

    public void setNewTableId(String newTableId) {
        this.newTableId = newTableId;
    }

    public static String getNewTableName() {
        return newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

    public String getOldTableId() {
        return oldTableId;
    }

    public void setOldTableId(String oldTableId) {
        this.oldTableId = oldTableId;
    }

    public static boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNoOfCovers() {
        return noOfCovers;
    }

    public void setNoOfCovers(int noOfCovers) {
        this.noOfCovers = noOfCovers;
    }

    public static String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    private void showView(String view) {
        CardLayout cl = (CardLayout) (getLayout());
        cl.show(this, view);
    }

    public String getDocumentNo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //Updating the new table cover
    public void updateCovers() {
        setStatus(false);
        JTableCover.showMessage(JRetailTicketsBagRestaurantMap.this, dlReceipts, retailTicket, m_PlaceCurrent, m_panelticket, this, false);

    }
    //Action listener when click on table button

    private class MyActionListener implements ActionListener {

        private Place m_place;

        public MyActionListener(Place place) {
            m_place = place;
        }

        public void actionPerformed(ActionEvent evt) {
            if (m_PlaceClipboard == null) {
                if (customer == null) {
                    // check if the sharedticket is the same
                    //adding logic to split ticket to hold multiple tickets of same table
                    RetailTicketInfo ticket = getTicketInfo(m_place);
                    // if not people or new table
                    if (ticket == null && !m_place.hasPeople()) {
                        // Empty table and checked
                        logger.info(m_App.getAppUserView().getUser().getName().toString() + " is trying to login the new table");
                        ticket = new RetailTicketInfo();
                        // table occupied
                        ticket.setTicketOpen(true);
                        ticket.setUser(m_App.getAppUserView().getUser().getUserInfo());
                        try {
                            dlReceipts.insertRetailSharedTicket(m_place.getId(), ticket);
                        } catch (BasicException e) {
                            logger.info("actionPerformed in map class exception 1 " + e.getMessage());
                            // new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this); // Glup. But It was empty.
                            checkDbConnection();
                        }

                        setActivePlace(m_place, ticket);

                    } //will never occur? occurs before refreshing
                    else if (ticket == null && m_place.hasPeople()) {
                        loadTickets();
                        return;

                    } else if (ticket != null && !m_place.hasPeople()) {
                        logger.info("Condition 1 " + m_place.getName() + " Table is occupied by " + ticket.printUser() + " printed the lines " + ticket.getLinesCount() + " and " + m_App.getAppUserView().getUser().getName().toString() + " is trying to login the table");
                        // The table is now full
//                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JRetailTicketsBagRestaurantMap.this);
                        JOptionPane.showMessageDialog(JRetailTicketsBagRestaurantMap.this, ticket.printUser() + " has already logged in this Table!", "Order in Progress", JOptionPane.INFORMATION_MESSAGE);
                        m_place.setPeople(true);
                        loadTickets();
                        return;

                    } else { // both != null
                        java.util.List<SharedSplitTicketInfo> splitticket = getSplitTicketInfo(m_place);
                        if (splitticket.size() == 1) {
                            if (ticket.isTicketOpen()) {
                                logger.info("Condition 2 " + m_place.getName() + " Table is occupied by " + ticket.printUser() + " printed the lines " + ticket.getLinesCount() + " and " + m_App.getAppUserView().getUser().getName().toString() + " is trying to login the table");
                                JOptionPane.showMessageDialog(JRetailTicketsBagRestaurantMap.this, ticket.printUser() + " has already logged in this Table!", "Order in Progress", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            logger.info("Condition 3 " + m_place.getName() + " Table is occupied by " + ticket.printUser() + " printed the lines " + ticket.getLinesCount() + " and " + m_App.getAppUserView().getUser().getName().toString() + " is trying to login the table");
                            ticket.setUser(m_App.getAppUserView().getUser().getUserInfo());
                            ticket.setTicketOpen(true);
                            try {
                                dlReceipts.updateSharedTicket(m_place.getId(), ticket);
                            } catch (BasicException ex) {
                                logger.info("actionPerformed in map class exception 2 " + ex.getMessage());
                                Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }


                        String kitchendisplay = m_App.getProperties().getProperty("machine.kitchendisplay");
                        if (kitchendisplay.equals("true")) {
                            String tokenId = null;
                            try {
                                tokenId = dlReceipts.getTokenId(m_place.getId());
                            } catch (BasicException ex) {
                                logger.info("actionPerformed in map class exception 3 " + ex.getMessage());
                                Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String money = m_App.getActiveCashIndex();
                            try {
                                // BEGIN TRANSACTION
                                ticket = dlSales.loadSharedOrderTicket(tokenId, money);
                            } catch (BasicException ex) {
                                logger.info("actionPerformed in map class exception 4 " + ex.getMessage());
                                Logger.getLogger(JRetailTicketsBagRestaurantMap.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (ticket != null) {
                                try {
                                    dlReceipts.updateOrderSharedTicket(m_place.getId(), ticket);
                                } catch (BasicException e) {
                                    logger.info("actionPerformed in map class exception 5" + e.getMessage());
                                    new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this); // Glup. But It was empty.
                                }
                                setActivePlace(m_place, ticket);
                            } else {
                                ticket = getTicketInfo(m_place);
                                try {
                                    dlReceipts.updateSharedTicket(m_place.getId(), ticket);
                                } catch (BasicException e) {
                                    logger.info("actionPerformed in map class exception 6 " + e.getMessage());
                                    new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this); // Glup. But It was empty.
                                }
                            }
                        } else {
                            setActivePlace(m_place, ticket);
                        }
                    }
                }//till here 13
                //will never execute as there is no concept of customer
                else {
                    // receiving customer.
                    // check if the sharedticket is the same
                    RetailTicketInfo ticket = getTicketInfo(m_place);
                    if (ticket == null) {
                        // receive the customer
                        // table occupied
                        ticket = new RetailTicketInfo();

                        try {
                            ticket.setCustomer(customer.getId() == null
                                    ? null
                                    : dlSales.loadCustomerExt(customer.getId()));
                        } catch (BasicException e) {
                            logger.info("actionPerformed in map class exception 7 " + e.getMessage());
                            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                            msg.show(JRetailTicketsBagRestaurantMap.this);
                        }

                        try {
                            dlReceipts.insertRetailSharedTicket(m_place.getId(), ticket);
                        } catch (BasicException e) {
                            logger.info("actionPerformed in map class exception 8 " + e.getMessage());
                            checkDbConnection();
                            //new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this); // Glup. But It was empty.
                        }
                        m_place.setPeople(true);
                        m_PlaceClipboard = null;
                        customer = null;
                        setActivePlace(m_place, ticket);
                    } else {
                        // TODO: msg: The table is now full
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JRetailTicketsBagRestaurantMap.this);
                        m_place.setPeople(true);
                        m_place.getButton().setEnabled(false);
                    }
                }
            } // till 12
            //concept of m_PlaceClipboard
            else {
                //move table action moving to empty table
                // check if the sharedticket is the same
                //changed it because of split bill issue (15/04/16)
                RetailTicketInfo ticketclip = getTicketInfo(m_PlaceClipboard, SplitTableMoveId);
                if (ticketclip == null) {
                    new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JRetailTicketsBagRestaurantMap.this);
                    m_PlaceClipboard.setPeople(false);
                    m_PlaceClipboard = null;
                    customer = null;
                    printState();
                } else {
                    // moving to same table , then no changes.
                    if (m_PlaceClipboard == m_place) {
                        // the same button. Canceling.
                        Place placeclip = m_PlaceClipboard;
                        m_PlaceClipboard = null;
                        customer = null;
                        printState();
                        setActivePlace(placeclip, ticketclip);
                    } else if (!m_place.hasPeople()) {
                        // Moving the receipt to an empty table
                        RetailTicketInfo ticket = getTicketInfo(m_place);

                        if (ticket == null) {
                            try {
                                dlReceipts.insertRetailSharedTicket(m_place.getId(), ticketclip);
                                m_place.setPeople(true);
                                // dlReceipts.deleteSharedTicket(m_PlaceClipboard.getId());
                                if (movedSplitTable) {
                                    dlReceipts.deleteTableCovers(m_PlaceClipboard.getId(), SplitTableMoveId);
                                }
                                // deleting moved table object
                                dlReceipts.deleteSharedSplitTicket(m_PlaceClipboard.getId(), SplitTableMoveId);
                                m_PlaceClipboard.setPeople(false);
                                // this will be set oly in case of move table
                                setNewTableId(m_place.getId());
                                setNewTableName(m_place.getName());
                                setOldTableId(m_PlaceClipboard.getId());
                            } catch (BasicException e) {
                                logger.info("actionPerformed in map class exception 9 " + e.getMessage());
                                new MessageInf(e).show(JRetailTicketsBagRestaurantMap.this); // Glup. But It was empty.
                            }

                            m_PlaceClipboard = null;
                            customer = null;
                            printState();

                            // No hace falta preguntar si estaba bloqueado porque ya lo estaba antes
                            // activamos el ticket seleccionado
                            setActivePlace(m_place, ticketclip);
                        } else {
                            // Full table
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tablefull")).show(JRetailTicketsBagRestaurantMap.this);
                            m_PlaceClipboard.setPeople(true);
                            printState();
                        }
                    } else {
                        // Merge the lines with the receipt of the table
                        RetailTicketInfo ticket = getTicketInfo(m_place);

                        if (ticket == null) {
                            // The table is now empty
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.tableempty")).show(JRetailTicketsBagRestaurantMap.this);
                            m_place.setPeople(false); // fixed
                        } else {
                            JOptionPane.showMessageDialog(JRetailTicketsBagRestaurantMap.this, getLabelPanel("Cannot Merge the Tables"), "Message",
                                    JOptionPane.INFORMATION_MESSAGE);

                        }
                    }
                }
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanelMap = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jbtnReservations = new javax.swing.JButton();
        m_jbtnRefresh = new javax.swing.JButton();
        m_jText = new javax.swing.JLabel();
        m_jbtnLogout = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        m_jPanelMap.setLayout(new java.awt.BorderLayout());

        m_jbtnReservations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        m_jbtnReservations.setText(AppLocal.getIntString("button.reservations")); // NOI18N
        m_jbtnReservations.setFocusPainted(false);
        m_jbtnReservations.setFocusable(false);
        m_jbtnReservations.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnReservations.setRequestFocusEnabled(false);
        m_jbtnReservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnReservationsActionPerformed(evt);
            }
        });

        m_jbtnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        m_jbtnRefresh.setText("Reload");
        m_jbtnRefresh.setFocusPainted(false);
        m_jbtnRefresh.setFocusable(false);
        m_jbtnRefresh.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnRefresh.setRequestFocusEnabled(false);
        m_jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnRefreshActionPerformed(evt);
            }
        });

        m_jbtnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/logoutapp.png"))); // NOI18N
        m_jbtnLogout.setText(AppLocal.getIntString("button.reloadticket")); // NOI18N
        m_jbtnLogout.setFocusPainted(false);
        m_jbtnLogout.setFocusable(false);
        m_jbtnLogout.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnLogout.setRequestFocusEnabled(false);
        m_jbtnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(m_jbtnReservations)
                .addGap(5, 5, 5)
                .addComponent(m_jbtnRefresh)
                .addGap(5, 5, 5)
                .addComponent(m_jText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(m_jbtnLogout)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(m_jbtnReservations))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jbtnRefresh)
                    .addComponent(m_jbtnLogout)))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(m_jText))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        m_jPanelMap.add(jPanel1, java.awt.BorderLayout.NORTH);

        add(m_jPanelMap, "map");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnRefreshActionPerformed

        m_PlaceClipboard = null;
        customer = null;
        loadTickets();
        printState();

    }//GEN-LAST:event_m_jbtnRefreshActionPerformed

    private class RefreshTickets implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            loadTickets();
        }
    }

    private void m_jbtnReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnReservationsActionPerformed

        showView("res");
        m_jreservations.activate();

    }//GEN-LAST:event_m_jbtnReservationsActionPerformed
    public void checkDbConnection() {
        showMessage(this, "LAN connection is down. Please do the operations after sometime");
        m_RootApp = (JRootApp) m_App;
        m_RootApp.closeAppView();
        logger.info("End Logout Button :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));

    }
    private void m_jbtnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnLogoutActionPerformed
        m_RootApp = (JRootApp) m_App;
        m_RootApp.closeAppView();
    }//GEN-LAST:event_m_jbtnLogoutActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jPanelMap;
    private javax.swing.JLabel m_jText;
    private javax.swing.JButton m_jbtnLogout;
    private javax.swing.JButton m_jbtnRefresh;
    private javax.swing.JButton m_jbtnReservations;
    // End of variables declaration//GEN-END:variables
}
