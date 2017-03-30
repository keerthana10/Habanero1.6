/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.printer.printer;

/**
 *
 * @author Admin
 */
public class TicketLineConstructor {
  
    private String line;


    public TicketLineConstructor(String line) {
        this.line = line;
    }

    /**
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * @param line the line to set
     */
    public void setLine(String line) {
        this.line = line;
    }
    
    
    
}
