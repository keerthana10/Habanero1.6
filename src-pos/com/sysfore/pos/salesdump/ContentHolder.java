/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.salesdump;

import com.openbravo.pos.sales.JPanelTicketEdits;
import com.openbravo.pos.sales.JTicketsBagTicket;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author binesh
 */
public class ContentHolder extends JDialog{


    public ContentHolder(JFrame frame, JPanelTicketEdits jTicketBagTicket) {
        super(frame,"",true);
        JPanel messagePane = new JPanel();
        messagePane.add(jTicketBagTicket);
        getContentPane().add(messagePane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }


}
