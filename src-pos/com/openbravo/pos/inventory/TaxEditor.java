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

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.util.UUID;
import javax.swing.*;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import java.util.ArrayList;
import java.util.List;

public class TaxEditor extends JPanel implements EditorRecord {
    
    private Object m_oId;
    
    private SentenceList taxcatsent;
    private ComboBoxValModel taxcatmodel;    
    
    private SentenceList taxcustcatsent;
    private ComboBoxValModel taxcustcatmodel;   
    
    private SentenceList taxparentsent;
    private ComboBoxValModel taxparentmodel;    
    
    /** Creates new form taxEditor */
    public TaxEditor(AppView app, DirtyManager dirty) {
        
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        
        initComponents();
        
        taxcatsent = dlSales.getTaxCategoriesList();
        taxcatmodel = new ComboBoxValModel();        
        
        taxcustcatsent = dlSales.getTaxCustCategoriesList();
        taxcustcatmodel = new ComboBoxValModel();    
        
        taxparentsent = dlSales.getTaxList();
        taxparentmodel = new ComboBoxValModel();    

        m_jName.getDocument().addDocumentListener(dirty);
        m_jTaxCategory.addActionListener(dirty);
        m_jCustTaxCategory.addActionListener(dirty);
        m_jTaxParent.addActionListener(dirty);
        m_jRate.getDocument().addDocumentListener(dirty);
        jCascade.addActionListener(dirty);
        jOrder.getDocument().addDocumentListener(dirty);
        m_jSalesTax.addActionListener(dirty);
        m_jPurchaseTax.addActionListener(dirty);
        m_jServiceTax.addActionListener(dirty);
        m_jDebitAccount.getDocument().addDocumentListener(dirty);
        m_jCreditAccount.getDocument().addDocumentListener(dirty);
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        
        List a = taxcatsent.list();
        taxcatmodel = new ComboBoxValModel(a);
        m_jTaxCategory.setModel(taxcatmodel);
        
        a = taxcustcatsent.list();
        a.add(0, null); // The null item
        taxcustcatmodel = new ComboBoxValModel(a);
        m_jCustTaxCategory.setModel(taxcustcatmodel);    
        
       
    }
    
    public void refresh() {
        
        List a;
        
        try {
            a = taxparentsent.list();
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotloadlists"), eD);
            msg.show(this);
            a = new ArrayList();
        }
        
        a.add(0, null); // The null item
        taxparentmodel = new ComboBoxValModel(a);
        m_jTaxParent.setModel(taxparentmodel);    
    }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jName.setText(null);
        taxcatmodel.setSelectedKey(null);
        taxcustcatmodel.setSelectedKey(null);
        taxparentmodel.setSelectedKey(null);
        m_jRate.setText(null);
        jCascade.setSelected(false);
        jOrder.setText(null);
         m_jDebitAccount.setText(null);
        m_jCreditAccount.setText(null);

        m_jName.setEnabled(false);
        m_jTaxCategory.setEnabled(false);
        m_jCustTaxCategory.setEnabled(false);
        m_jTaxParent.setEnabled(false);
        m_jRate.setEnabled(false);
        jCascade.setEnabled(false);
        jOrder.setEnabled(false);
        m_jSalesTax.setEnabled(false);
        m_jPurchaseTax.setEnabled(false);
        m_jServiceTax.setEnabled(false);
        m_jDebitAccount.setEnabled(false);
        m_jCreditAccount.setEnabled(false);
    }
    public void writeValueInsert() {
        m_oId = UUID.randomUUID().toString();
        m_jName.setText(null);
        taxcatmodel.setSelectedKey(null);
        taxcustcatmodel.setSelectedKey(null);
        taxparentmodel.setSelectedKey(null);
        m_jRate.setText(null);
        jCascade.setSelected(false);
        jOrder.setText(null);
          m_jDebitAccount.setText(null);
        m_jCreditAccount.setText(null);
        
        m_jName.setEnabled(true);
        m_jTaxCategory.setEnabled(true);
        m_jCustTaxCategory.setEnabled(true);
        m_jTaxParent.setEnabled(true);        
        m_jRate.setEnabled(true);
        jCascade.setEnabled(true);
        m_jSalesTax.setEnabled(true);
        m_jPurchaseTax.setEnabled(true);
        jOrder.setEnabled(true);
         m_jServiceTax.setEnabled(true);
        m_jDebitAccount.setEnabled(true);
        m_jCreditAccount.setEnabled(true);
    }
    public void writeValueDelete(Object value) {

        Object[] tax = (Object[]) value;
        m_oId = tax[0];
        m_jName.setText(Formats.STRING.formatValue(tax[1]));
        taxcatmodel.setSelectedKey(tax[2]);
        taxcustcatmodel.setSelectedKey(tax[3]);
        taxparentmodel.setSelectedKey(tax[4]);        
        m_jRate.setText(Formats.PERCENT.formatValue(tax[5]));
        jCascade.setSelected((Boolean) tax[6]);
        jOrder.setText(Formats.INT.formatValue(tax[7]));
        if(tax[8]=="Y"){
            m_jSalesTax.setSelected(true);
        }else{
            m_jSalesTax.setSelected(false);
        }
        if(tax[9]=="Y"){
            m_jPurchaseTax.setSelected(true);
        } else{
            m_jPurchaseTax.setSelected(false);
            }
        if(tax[10]=="Y"){
            m_jServiceTax.setSelected(true);
        }else{
            m_jServiceTax.setSelected(false);
        }
        m_jDebitAccount.setText(Formats.STRING.formatValue(tax[11]));
         m_jCreditAccount.setText(Formats.STRING.formatValue(tax[12]));

        m_jName.setEnabled(false);
        m_jTaxCategory.setEnabled(false);
        m_jCustTaxCategory.setEnabled(false);
        m_jTaxParent.setEnabled(false);
        m_jRate.setEnabled(false);
        jCascade.setEnabled(false);
        jOrder.setEnabled(false);
        m_jPurchaseTax.setEnabled(false);
        m_jSalesTax.setEnabled(false);
        m_jServiceTax.setEnabled(false);
        m_jDebitAccount.setEnabled(false);
        m_jCreditAccount.setEnabled(false);
    }    
    public void writeValueEdit(Object value) {

        Object[] tax = (Object[]) value;
        m_oId = tax[0];
        m_jName.setText(Formats.STRING.formatValue(tax[1]));
        taxcatmodel.setSelectedKey(tax[2]);
        taxcustcatmodel.setSelectedKey(tax[3]);
        taxparentmodel.setSelectedKey(tax[4]);        
        m_jRate.setText(Formats.PERCENT.formatValue(tax[5]));
        jCascade.setSelected((Boolean) tax[6]);
        jOrder.setText(Formats.INT.formatValue(tax[7]));
        if(tax[8].equals("Y")){
            m_jSalesTax.setSelected(true);
        }else{
            m_jSalesTax.setSelected(false);
        }
        if(tax[9].equals("Y")){
            m_jPurchaseTax.setSelected(true);
        } else{
            m_jPurchaseTax.setSelected(false);
            }
        if(tax[10].equals("Y")){
            m_jServiceTax.setSelected(true);
        }else{
            m_jServiceTax.setSelected(false);
        }
           m_jDebitAccount.setText(Formats.STRING.formatValue(tax[11]));
         m_jCreditAccount.setText(Formats.STRING.formatValue(tax[12]));

        m_jName.setEnabled(true);
        m_jTaxCategory.setEnabled(true);
        m_jCustTaxCategory.setEnabled(true);
        m_jTaxParent.setEnabled(true);        
        m_jRate.setEnabled(true);
        jCascade.setEnabled(true);
        jOrder.setEnabled(true);
        m_jPurchaseTax.setEnabled(true);
    m_jSalesTax.setEnabled(true);
 m_jServiceTax.setEnabled(true);
        m_jDebitAccount.setEnabled(true);
        m_jCreditAccount.setEnabled(true);
    }

    public Object createValue() throws BasicException {
        
        Object[] tax = new Object[13];

        tax[0] = m_oId;
        tax[1] = m_jName.getText();
        tax[2] = taxcatmodel.getSelectedKey();
        tax[3] = taxcustcatmodel.getSelectedKey(); 
        tax[4] = taxparentmodel.getSelectedKey(); 
        tax[5] = Formats.PERCENT.parseValue(m_jRate.getText());
        tax[6] = Boolean.valueOf(jCascade.isSelected());
        tax[7] = Formats.INT.parseValue(jOrder.getText());

        String salesTax;
        String purchaseTax;
        String serviceTax;
        String debitAccount;
        String creditAccount;
        if(m_jSalesTax.isSelected()){
            salesTax = "Y";
        }else{
            salesTax = "N";
        }
        if(m_jPurchaseTax.isSelected()){
            purchaseTax = "Y";
        }else{
            purchaseTax = "N";
        }
          if(m_jServiceTax.isSelected()){
            serviceTax = "Y";
        }else{
            serviceTax = "N";
        }
  
        tax[8] = salesTax;
        tax[9] = purchaseTax;
         tax[10] = serviceTax;
        tax[11] =  m_jDebitAccount.getText();
        tax[12] =  m_jCreditAccount.getText();

        return tax;
    }    
     
    public Component getComponent() {
        return this;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jRate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCascade = new javax.swing.JCheckBox();
        m_jTaxCategory = new javax.swing.JComboBox();
        m_jTaxParent = new javax.swing.JComboBox();
        m_jCustTaxCategory = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jOrder = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jSalesTax = new javax.swing.JCheckBox();
        m_jPurchaseTax = new javax.swing.JCheckBox();
        m_jLblService = new javax.swing.JLabel();
        m_jServiceTax = new javax.swing.JCheckBox();
        m_jLblDebit = new javax.swing.JLabel();
        m_jLblDebit1 = new javax.swing.JLabel();
        m_jDebitAccount = new javax.swing.JTextField();
        m_jCreditAccount = new javax.swing.JTextField();

        setLayout(null);
        add(m_jName);
        m_jName.setBounds(240, 20, 200, 20);

        jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 20, 220, 14);

        jLabel3.setText(AppLocal.getIntString("label.dutyrate")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 140, 220, 14);
        add(m_jRate);
        m_jRate.setBounds(240, 140, 60, 20);

        jLabel1.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 50, 220, 14);

        jLabel4.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 80, 220, 14);

        jLabel5.setText(AppLocal.getIntString("label.taxparent")); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(20, 110, 220, 14);

        jCascade.setText(AppLocal.getIntString("label.cascade")); // NOI18N
        add(jCascade);
        jCascade.setBounds(320, 140, 110, 23);
        add(m_jTaxCategory);
        m_jTaxCategory.setBounds(240, 50, 200, 20);
        add(m_jTaxParent);
        m_jTaxParent.setBounds(240, 110, 200, 20);
        add(m_jCustTaxCategory);
        m_jCustTaxCategory.setBounds(240, 80, 200, 20);

        jLabel6.setText(AppLocal.getIntString("label.order")); // NOI18N
        add(jLabel6);
        jLabel6.setBounds(20, 170, 220, 14);
        add(jOrder);
        jOrder.setBounds(240, 170, 60, 20);

        jLabel7.setText("Sales tax");
        add(jLabel7);
        jLabel7.setBounds(20, 200, 200, 20);

        jLabel8.setText("Purchase Tax");
        add(jLabel8);
        jLabel8.setBounds(20, 230, 200, 20);
        add(m_jSalesTax);
        m_jSalesTax.setBounds(240, 200, 21, 21);
        add(m_jPurchaseTax);
        m_jPurchaseTax.setBounds(240, 230, 21, 21);

        m_jLblService.setText("Service tax");
        add(m_jLblService);
        m_jLblService.setBounds(20, 260, 200, 20);
        add(m_jServiceTax);
        m_jServiceTax.setBounds(240, 260, 21, 21);

        m_jLblDebit.setText("Debit Account");
        add(m_jLblDebit);
        m_jLblDebit.setBounds(20, 290, 200, 20);

        m_jLblDebit1.setText("Credit Account");
        add(m_jLblDebit1);
        m_jLblDebit1.setBounds(20, 320, 200, 20);
        add(m_jDebitAccount);
        m_jDebitAccount.setBounds(240, 290, 200, 20);
        add(m_jCreditAccount);
        m_jCreditAccount.setBounds(240, 320, 200, 20);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCascade;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jOrder;
    private javax.swing.JTextField m_jCreditAccount;
    private javax.swing.JComboBox m_jCustTaxCategory;
    private javax.swing.JTextField m_jDebitAccount;
    private javax.swing.JLabel m_jLblDebit;
    private javax.swing.JLabel m_jLblDebit1;
    private javax.swing.JLabel m_jLblService;
    private javax.swing.JTextField m_jName;
    private javax.swing.JCheckBox m_jPurchaseTax;
    private javax.swing.JTextField m_jRate;
    private javax.swing.JCheckBox m_jSalesTax;
    private javax.swing.JCheckBox m_jServiceTax;
    private javax.swing.JComboBox m_jTaxCategory;
    private javax.swing.JComboBox m_jTaxParent;
    // End of variables declaration//GEN-END:variables
    
}
