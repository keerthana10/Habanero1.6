/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.cashmanagement;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author mateen
 */
public class CurrencyTableListener implements TableModelListener {

    private JTable table;
    private String[] currList;
    private Object changedValue;
    private int row;
    private int column;

    public CurrencyTableListener(JTable table, String[] currList) {
        this.table = table;
        this.currList = currList;

    }

    public void tableChanged(TableModelEvent tme) {
        int col = table.getEditingColumn();
        int rows = table.getEditingRow();
        if(col>0){
            setChangedValue(table.getValueAt(rows, col));
            setRow(rows);
            setColumn(column);
            //System.out.println(recalCulateTotal()+" "+ getRow()+" "+getColumn()+1);
        }

    }

    public int recalCulateTotal() {

        int total = 0;
        int count = Integer.parseInt(getChangedValue().toString());
        int value = Integer.parseInt(table.getValueAt(this.getRow(), this.getColumn()).toString());
        total = value * count;
        return total;
    }

    /**
     * @return the changedValue
     */
    public Object getChangedValue() {
        return changedValue;
    }

    /**
     * @param changedValue the changedValue to set
     */
    public void setChangedValue(Object changedValue) {
        this.changedValue = changedValue;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }
}
