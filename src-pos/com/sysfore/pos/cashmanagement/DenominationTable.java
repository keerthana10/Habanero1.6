/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sysfore.pos.cashmanagement;

/**
 *
 * @author mateen
 */
public class DenominationTable {

    private double rupees;
    private int count;
    private double total;

    public DenominationTable(double rupees, int count, double total) {
        this.rupees = rupees;
        this.count = count;
        this.total = total;
    }


    /**
     * @return the rupees
     */
    public double getRupees() {
        return rupees;
    }

    /**
     * @param rupees the rupees to set
     */
    public void setRupees(double rupees) {
        this.rupees = rupees;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

}
