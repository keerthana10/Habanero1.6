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

package com.openbravo.pos.reports;

import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.util.Date;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.pos.ticket.CustomerFilter;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JParamsDates extends javax.swing.JPanel implements ReportEditorCreator {
    public static final String DATE_FORMAT_NOW = "d MM yyyy ";
    
     Calendar cal = Calendar.getInstance();
      Calendar calt = Calendar.getInstance();
     
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    SimpleDateFormat cd = new SimpleDateFormat("d MMM, yyyy");
    
   /** Creates new form JParamsClosedPos */
    public JParamsDates() {
        initComponents();
    }
    
    public void setStartDate(Date d) {
        jTxtStartDate.setText(Formats.DATE.formatValue(d));
    }
    
    public String now() {
        
        //System.out.println("formatted date"+cd.format(new Date()));
    return cd.format(new Date());
    }
    public String startTime()
    {
         calt.set(Calendar.AM_PM, Calendar.AM);
calt.set(Calendar.HOUR, 0);
calt.set(Calendar.MINUTE, 0);
calt.set(Calendar.SECOND, 0);
      return sdf.format(calt.getTime());   
    }
    public void setEndDate(Date d) {
       
        jTxtEndDate.setText(Formats.DATE.formatValue(d));
    }

    public void init(AppView app) {
    }

    public void activate() throws BasicException {
    }
    
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP});
    }

    public Component getComponent() {
        return this;
    }
      

    
      private JPanel getLabelRedPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.red);
    helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
 private void showMsg(JParamsDates aThis, String msg) {
       JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
     }
    public Object createValue() throws BasicException {
        Object startdate = Formats.DATE.parseValue(jTxtStartDate.getText());
        Object enddate = Formats.DATE.parseValue(jTxtEndDate.getText()); 
        if(jTxtStartDate.getText().equals(""))
         {
           jTxtStartDate.setText(now()); 
         String startDateVal= jTxtStartDate.getText();
         
          Date date=null;
            try {
                date = cd.parse(startDateVal);
            } catch (ParseException ex) {
                Logger.getLogger(JParamsDefaultDates.class.getName()).log(Level.SEVERE, null, ex);
            }
            startdate=date; 
        }
       if(jTxtEndDate.getText().equals(""))
        {
           jTxtEndDate.setText(now()); 
          String endDateVal=jTxtEndDate.getText();
          Date date=null;
            try {
                date = cd.parse(endDateVal);
            } catch (ParseException ex) {
                Logger.getLogger(JParamsDefaultDates.class.getName()).log(Level.SEVERE, null, ex);
            }
		//System.out.println(sdf.format(date));
            System.out.println(date);
                enddate=date;
        }
        return new Object[] {
            startdate == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_GREATEROREQUALS,
            startdate,
            enddate == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_LESS,
            enddate
        };
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTxtStartDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTxtEndDate = new javax.swing.JTextField();
        btnDateStart = new javax.swing.JButton();
        btnDateEnd = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bydates"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(0, 100));
        setLayout(null);
        add(jTxtStartDate);
        jTxtStartDate.setBounds(140, 20, 200, 20);

        jLabel2.setText(AppLocal.getIntString("Label.EndDate")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 50, 120, 14);
        add(jTxtEndDate);
        jTxtEndDate.setBounds(140, 50, 200, 20);

        btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        btnDateStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateStartActionPerformed(evt);
            }
        });
        add(btnDateStart);
        btnDateStart.setBounds(350, 20, 49, 25);

        btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        btnDateEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateEndActionPerformed(evt);
            }
        });
        add(btnDateEnd);
        btnDateEnd.setBounds(350, 50, 49, 25);

        jLabel3.setText("Start Date");
        add(jLabel3);
        jLabel3.setBounds(20, 20, 80, 14);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateStartActionPerformed

        Date date;
        try {
            date = (Date) Formats.DATE.parseValue(jTxtStartDate.getText());
        } catch (BasicException e) {
            date = null;
        }        
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtStartDate.setText(Formats.DATE.formatValue(date));
        }             
    }//GEN-LAST:event_btnDateStartActionPerformed

    private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateEndActionPerformed

        Date date;
        try {
            date = (Date) Formats.DATE.parseValue(jTxtEndDate.getText());
        } catch (BasicException e) {
            date = null;
        }        
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtEndDate.setText(Formats.DATE.formatValue(date));
        }          
    }//GEN-LAST:event_btnDateEndActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDateEnd;
    private javax.swing.JButton btnDateStart;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTxtEndDate;
    private javax.swing.JTextField jTxtStartDate;
    // End of variables declaration//GEN-END:variables
    
}

