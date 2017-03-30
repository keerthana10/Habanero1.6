/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.employeemanagement;

import java.awt.Component;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author mateen
 */
class CustomRadioButtonEditor extends JRadioButton implements TableCellRenderer {

    public CustomRadioButtonEditor(JTable jTAttendance) {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        try {
            if (isSelected) {
                setSelected(isSelected);
            } else {
                setSelected(isSelected);
            }
            setHorizontalAlignment(JRadioButton.CENTER);
        } catch (Exception ex) {
            System.out.println("exception occurred in rendering table" + this.getClass().getName());
        }
        return this;
    }

}
