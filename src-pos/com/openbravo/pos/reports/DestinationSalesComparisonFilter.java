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

import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.pos.forms.AppLocal;

import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.util.List;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.reports.ReportEditorCreator;
import java.util.ArrayList;

public class DestinationSalesComparisonFilter extends javax.swing.JPanel implements ReportEditorCreator {
    
   // private SentenceList m_sentcat;
    private ComboBoxValModel m_SourceMonthModel;
      private ComboBoxValModel m_SourceYearModel;
       private ComboBoxValModel m_DestinationMonthModel;
      private ComboBoxValModel m_DestinationYearModel;

    private String sourceMonth=null;
    private String sourceYear=null;
    private String destinationMonth=null;
    private String destinationYear=null;
    /** Creates new form JQBFProduct */
    public DestinationSalesComparisonFilter() {

        initComponents();
    }
    
    public void init(AppView app) {
         
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

         m_DestinationMonthModel= new ComboBoxValModel();
         m_DestinationYearModel= new ComboBoxValModel();
        }
    
    public void activate() throws BasicException {

        List monthList = new ArrayList();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        List yearList = new ArrayList();
        yearList.add("2014");
        yearList.add("2015");
        yearList.add("2016");
        yearList.add("2017");
        yearList.add("2018");
        yearList.add("2019");
        yearList.add("2020");
        yearList.add("2021");
        yearList.add("2022");
        yearList.add("2023");
        yearList.add("2024");
      
//        catlist.add(0, null);
   //     m_SourceMonthModel = new ComboBoxValModel(monthList);
    //    m_jSourceMonth.setModel(m_SourceMonthModel);

         m_DestinationMonthModel = new ComboBoxValModel(monthList);
        m_jDestinationMonth.setModel(m_DestinationMonthModel);

     //   m_SourceYearModel = new ComboBoxValModel(yearList);
     //   m_jSourceYear.setModel(m_SourceYearModel);

        m_DestinationYearModel = new ComboBoxValModel(yearList);
        m_jDestinationYear.setModel(m_DestinationYearModel);

 
    }
    
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(
                new Datas[] {Datas.OBJECT, Datas.STRING,Datas.OBJECT, Datas.STRING });
    }

    public Component getComponent() {
        return this;
    }
   
  //  public Object createValue() throws BasicException {
//        
//        if (m_jBarcode.getText() == null || m_jBarcode.getText().equals("")) {
//            // Filtro por formulario
//            return new Object[] {
//                 m_CategoryModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_CategoryModel.getSelectedKey(),
//                QBFCompareEnum.COMP_NONE, null         
//            };
//        } else {            
//            // Filtro por codigo de barras.
//            return new Object[] {
//              
//                QBFCompareEnum.COMP_RE, "%" + m_jBarcode.getText() + "%"
//            };
//        }
  //  } 
// 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        m_jDestinationMonth = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        m_jDestinationYear = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(514, 32));

        jLabel3.setText("Destination Month");

        m_jDestinationMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDestinationMonthActionPerformed(evt);
            }
        });

        jLabel4.setText("Destination Year");

        m_jDestinationYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDestinationYearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_jDestinationMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_jDestinationYear, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(354, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jDestinationYear, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jDestinationMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jDestinationMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDestinationMonthActionPerformed
        destinationMonth=m_jDestinationMonth.getSelectedItem().toString();// TODO add your handling code here:
    }//GEN-LAST:event_m_jDestinationMonthActionPerformed

    private void m_jDestinationYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDestinationYearActionPerformed
       destinationYear=m_jDestinationYear.getSelectedItem().toString(); // TODO add your handling code here:
    }//GEN-LAST:event_m_jDestinationYearActionPerformed
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JComboBox m_jDestinationMonth;
    private javax.swing.JComboBox m_jDestinationYear;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object createValue() throws BasicException {
            return new Object[] {
                //  m_jSourceMonth.getSelectedItem() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, sourceMonth,
               //   m_jSourceYear.getSelectedItem() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, sourceYear,
                  m_jDestinationMonth.getSelectedItem() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, destinationMonth,
                  m_jDestinationYear.getSelectedItem() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, destinationYear
            };
     
    }
    
}
