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
package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author adrianromero
 */
public class CustomersView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    private DirtyManager m_Dirty;
    private StringBuffer gBillAddress = new StringBuffer();
    private StringBuffer gShipAddress = new StringBuffer();
     DataLogicSales dlSales;
     DataLogicCustomers dlCustomers;
    protected AppView m_App;
    CustomersPanel p;
    /**
     * Creates new form CustomersView
     */
    public CustomersView(AppView app, DirtyManager dirty,CustomersPanel p) {
        System.out.println("CustomersView");
           m_App = app;
        dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlCustomers = (DataLogicCustomers)app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        this.p = p;
        initComponents();
        m_sentcat = dlSales.getTaxCustCategoriesList();
        m_CategoryModel = new ComboBoxValModel();

        m_Dirty = dirty;
        m_jEmailId.getDocument().addDocumentListener(dirty);
        m_jCusPhone.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jCategory.addActionListener(dirty);
        jcard.getDocument().addDocumentListener(dirty);
        txtMaxdebt.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        m_jisCustomer.addActionListener(dirty);

        txtFirstName.getDocument().addDocumentListener(dirty);
        txtLastName.getDocument().addDocumentListener(dirty);
        txtEmail.getDocument().addDocumentListener(dirty);
        txtPhone.getDocument().addDocumentListener(dirty);
        txtPhone2.getDocument().addDocumentListener(dirty);
        txtFax.getDocument().addDocumentListener(dirty);

        txtAddress.getDocument().addDocumentListener(dirty);
        txtAddress2.getDocument().addDocumentListener(dirty);

        jChkBillAddr.addActionListener(dirty);
        jChkShiftAddr.addActionListener(dirty);


        txtPostal.getDocument().addDocumentListener(dirty);
        txtCity.getDocument().addDocumentListener(dirty);
        txtRegion.getDocument().addDocumentListener(dirty);
        txtCountry.getDocument().addDocumentListener(dirty);
        jChkBillAddr.addActionListener(dirty);
        jChkShiftAddr.addActionListener(dirty);
        jTABillAddress.getDocument().addDocumentListener(dirty);
        jTAShiftAddress.getDocument().addDocumentListener(dirty);
        m_jCustomerId.getDocument().addDocumentListener(dirty);
        m_jisCreditCustomer.addActionListener(dirty);
         jReceivable.getDocument().addDocumentListener(dirty);
        jAdvance.getDocument().addDocumentListener(dirty);
     //   setCustomerId();
        writeValueEOF();
 
    }

    public void activate() throws BasicException {
        System.out.println("activate");
        List a = m_sentcat.list();
        a.add(0, null); // The null item
        m_CategoryModel = new ComboBoxValModel(a);
        m_jCategory.setModel(m_CategoryModel);
         //setCustomerId();
         txtPhone.setVisible(false);
         txtEmail.setVisible(false);
         jLabel17.setVisible(false);
         jLabel16.setVisible(false);
         jLabel1.setVisible(false);
        jLabel2.setVisible(false);
         jLabel6.setVisible(false);
         txtMaxdebt.setVisible(false);
         txtCurdebt.setVisible(false);
         txtCurdate.setVisible(false);
         jLabel5.setVisible(false);
        jButton2.setVisible(false);
        jcard.setVisible(false);
        jButton3.setVisible(false);
        jLabel9.setVisible(false);
        m_jCategory.setVisible(false);

    }

    @Override
    public void refresh() {
         
    }

    @Override
    public void writeValueEOF() {
        System.out.println("writeValueEOF-");
        m_oId = null;
        m_jEmailId.setText(null);
        m_jCusPhone.setText(null);
         jReceivable.setText(null);
         jAdvance.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        jcard.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        m_jVisible.setSelected(false);
        m_jisCustomer.setSelected(false);
        m_jisCreditCustomer.setSelected(false);
        jcard.setText(null);
//m_jisCreditCustomer.setSelected(false);
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);

        txtAddress.setText(null);
        txtAddress2.setText(null);

        jChkBillAddr.setSelected(false);
        jChkShiftAddr.setSelected(false);

        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);
        m_jisCustomer.setSelected(false);

        jTABillAddress.setText(null);
        jTAShiftAddress.setText(null);
        txtPhone.setVisible(false);
        txtEmail.setVisible(false);
        jLabel17.setVisible(false);
        jLabel16.setVisible(false);
        m_jEmailId.setEnabled(false);
        m_jCusPhone.setEnabled(false);
        m_jName.setEnabled(false);
        m_jCategory.setEnabled(false);
        jPanel5.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        txtCurdebt.setEnabled(false);
        txtCurdate.setEnabled(false);
        m_jVisible.setEnabled(false);
        m_jisCustomer.setEnabled(false);
        jcard.setEnabled(false);

        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPhone2.setEnabled(false);
        txtFax.setEnabled(false);

        txtAddress.setEnabled(false);
        txtAddress2.setEnabled(false);

        jChkBillAddr.setEnabled(false);
        jChkShiftAddr.setEnabled(false);

        txtPostal.setEnabled(false);
        txtCity.setEnabled(false);
        txtRegion.setEnabled(false);
        txtCountry.setEnabled(false);

        jTABillAddress.setEnabled(false);
        jTAShiftAddress.setEnabled(false);

        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
         jReceivable.setEnabled(false);
         jAdvance.setEnabled(false);
     //    setCustomerId();

    }

    @Override
    public void writeValueInsert() {
        System.out.println("writeValueInsert");
        setCustomerId();
        m_oId = null;
        m_jEmailId.setText(null);
        m_jCusPhone.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        jcard.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        m_jVisible.setSelected(true);
        m_jisCustomer.setSelected(false);
        m_jisCreditCustomer.setSelected(false);
        jcard.setText(null);
//m_jisCreditCustomer.setSelected(false);
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);

        txtAddress.setText(null);
        txtAddress2.setText(null);

        jChkBillAddr.setSelected(false);
        jChkShiftAddr.setSelected(false);


        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);

        jTABillAddress.setText(null);
        jTAShiftAddress.setText(null);

        m_jEmailId.setEnabled(true);
        m_jCusPhone.setEnabled(true);
        m_jName.setEnabled(true);
        m_jCategory.setEnabled(true);
        jPanel5.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);
        m_jVisible.setEnabled(true);
        m_jisCustomer.setEnabled(true);
        jcard.setEnabled(true);
m_jisCreditCustomer.setEnabled(true);
        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPhone2.setEnabled(true);
        txtFax.setEnabled(true);

        txtAddress.setEnabled(true);
        txtAddress2.setEnabled(true);

        jChkBillAddr.setEnabled(true);
        jChkShiftAddr.setEnabled(true);

        txtPostal.setEnabled(true);
        txtCity.setEnabled(true);
        txtRegion.setEnabled(true);
        txtCountry.setEnabled(true);

        jTABillAddress.setEnabled(true);
        jTAShiftAddress.setEnabled(true);

        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
      
    }

    @Override
    public void writeValueDelete(Object value) {
        
       Object[] customer = (Object[]) value;
       System.out.println("writeValueDelete---"+customer[30]);
       if(customer[30].equals("N")){
           showMsg(this,"Record cannot be deleted, as it has sync to central system.");
       }else{
           int customerCount = 0;
        try {
            customerCount = dlSales.getCusCount(m_oId.toString());

        } catch (BasicException ex) {
            Logger.getLogger(CustomersView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customerCount==1){
            showMsg(this,"Record Cannot be delete, as it has linked transaction.");
        }else{
           m_oId = customer[0];
            try {
                dlSales.deleteCustomer(m_oId.toString());
            } catch (BasicException ex) {
                Logger.getLogger(CustomersView.class.getName()).log(Level.SEVERE, null, ex);
            }

               
        m_jCustomerId.setText((String) customer[1]);
        m_jCustomerId.setText((String) customer[2]);
        m_jName.setText((String) customer[3]);
        jcard.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

        txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
        txtLastName.setText(Formats.STRING.formatValue(customer[11]));
        m_jEmailId.setText(Formats.STRING.formatValue(customer[12]));
        m_jCusPhone.setText(Formats.STRING.formatValue(customer[13]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
        txtFax.setText(Formats.STRING.formatValue(customer[15]));

        txtAddress.setText(Formats.STRING.formatValue(customer[16]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[17]));

        jChkBillAddr.setSelected(((Boolean) customer[18]).booleanValue());
        jChkShiftAddr.setSelected(((Boolean) customer[19]).booleanValue());

        txtPostal.setText(Formats.STRING.formatValue(customer[20]));
        txtCity.setText(Formats.STRING.formatValue(customer[21]));
        txtRegion.setText(Formats.STRING.formatValue(customer[22]));
        txtCountry.setText(Formats.STRING.formatValue(customer[23]));

        m_CategoryModel.setSelectedKey(customer[24]);
        m_jisCustomer.setSelected(((Boolean) customer[25]).booleanValue());

        jTABillAddress.setText(Formats.STRING.formatValue(customer[26]));
        jTAShiftAddress.setText(Formats.STRING.formatValue(customer[27]));
        m_jCustomerId.setText(Formats.STRING.formatValue(customer[1]));
        m_jisCreditCustomer.setSelected(((Boolean) customer[29]).booleanValue());
        jReceivable.setText(Formats.STRING.formatValue(customer[30]));
        jAdvance.setText(Formats.STRING.formatValue(customer[31]));
       }
       }
        
        
    }
    private void showMessageGreen(CustomersView aThis,String msg) {
     //   JOptionPane.showMessageDialog(aThis, msg);
        JOptionPane.showMessageDialog(aThis,getLabelGreenPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
 }
    private JPanel getLabelGreenPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
  //  panel.setBackground(Color.BLUE);
    JLabel helloLabel = new JLabel(msg, JLabel.LEFT);
    helloLabel.setForeground(Color.decode("#206e10"));
     helloLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(helloLabel);

    return panel;
}
private void showMsg(CustomersView aThis,String msg) {
         JOptionPane.showMessageDialog(aThis,getLabelRedPanel(msg),"Message",
                JOptionPane.INFORMATION_MESSAGE);
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
    @Override
    public void writeValueEdit(Object value) {
         Object[] customer = (Object[]) value;
        System.out.println("save---"+((Boolean) customer[25]).booleanValue());
        
      //  Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jCustomerId.setText((String) customer[1]);
        m_jCustomerId.setText((String) customer[2]);
        m_jName.setText((String) customer[3]);
        jcard.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));

        txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
        txtLastName.setText(Formats.STRING.formatValue(customer[11]));
        m_jEmailId.setText(Formats.STRING.formatValue(customer[12]));
        m_jCusPhone.setText(Formats.STRING.formatValue(customer[13]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
        txtFax.setText(Formats.STRING.formatValue(customer[15]));

        txtAddress.setText(Formats.STRING.formatValue(customer[16]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[17]));

        boolean isBillSelected = ((Boolean) customer[18]).booleanValue();
        boolean isShipSelected = ((Boolean) customer[19]).booleanValue();

        jChkBillAddr.setSelected(isBillSelected);
        if(isBillSelected){
            jTABillAddress.setEditable(false);
              jTABillAddress.setEnabled(false);

        }else{
            jTABillAddress.setEditable(true);
            jTABillAddress.setEnabled(true);
        }
        if(isShipSelected){
            jTAShiftAddress.setEditable(false);
            jTAShiftAddress.setEnabled(false);

        }else{
            jTAShiftAddress.setEditable(true);
              jTAShiftAddress.setEnabled(true);
        }
        if(((Boolean) customer[25]).booleanValue()==false){
            m_jisCreditCustomer.setEnabled(true);
        }else{
            m_jisCreditCustomer.setEnabled(false);
        }
        jChkShiftAddr.setSelected(isShipSelected);

        txtPostal.setText(Formats.STRING.formatValue(customer[20]));
        txtCity.setText(Formats.STRING.formatValue(customer[21]));
        txtRegion.setText(Formats.STRING.formatValue(customer[22]));
        txtCountry.setText(Formats.STRING.formatValue(customer[23]));

        m_CategoryModel.setSelectedKey(customer[24]);
        m_jisCustomer.setSelected(((Boolean) customer[25]).booleanValue());
        jTABillAddress.setText(Formats.STRING.formatValue(customer[26]));
        jTAShiftAddress.setText(Formats.STRING.formatValue(customer[27]));
        m_jCustomerId.setText(Formats.STRING.formatValue(customer[28]));
         m_jisCreditCustomer.setSelected(((Boolean) customer[29]).booleanValue());
           if(jReceivable.getText().toString().equals("")&&customer[25].equals(false))
       {
            System.out.println("within writvalue rec if");
             try {
        List<AccountPojo> list=dlCustomers.getAccountValue();
         System.out.println("INSIDE TRY");
        if(list!=null && list.size()!=0 ){
            jReceivable.setText(list.get(0).getReceivable().toString());
         }
           }
       catch(Exception e){
          System.out.println("exception");
       }

        }
     else if(jReceivable.getText().toString().equals("")&&customer[25].equals(true))
       {
            System.out.println("within rec if");
             try {
        List<AccountPojo> list=dlCustomers.getAccountValue();
         System.out.println("INSIDE TRY");
        if(list!=null && list.size()!=0 ){
            jReceivable.setText(list.get(0).getReceivable().toString());
         }
           }
       catch(Exception e){
          System.out.println("exception");
     }

        }
        if(jAdvance.getText().toString().equals("")&&customer[25].equals(false))
       {
          System.out.println("within if");
           try {
                System.out.println("within try");
         List<AccountPojo> list=dlCustomers.getAccountValue();
         if(list!=null && list.size()!=0 ){
             jAdvance.setText(list.get(0).getAdvance().toString());
 }
           }
         catch(Exception e){
           System.out.println("exception");
        }

       }
          else if(jAdvance.getText().toString().equals("")&&customer[25].equals(true))
       {
          System.out.println("within if");
           try {
                System.out.println("within try");
         List<AccountPojo> list=dlCustomers.getVendorAccountValue();
         if(list!=null && list.size()!=0 ){
             jAdvance.setText(list.get(0).getAdvance().toString());
 }
           }
         catch(Exception e){
           System.out.println("exception");
         }

       }
        jReceivable.setText(Formats.STRING.formatValue(customer[31]));
        jAdvance.setText(Formats.STRING.formatValue(customer[32]));

  //      m_jisCreditCustomer.setSelected(((Boolean) customer[29]).booleanValue());
        m_jEmailId.setEnabled(true);
        m_jCusPhone.setEnabled(true);
        m_jName.setEnabled(true);
        jPanel5.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);
        m_jVisible.setEnabled(true);
        m_jisCustomer.setEnabled(true);
        jcard.setEnabled(true);

        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPhone2.setEnabled(true);
        txtFax.setEnabled(true);
jReceivable.setEnabled(true);
        jAdvance.setEnabled(true);
        txtAddress.setEnabled(true);
        txtAddress2.setEnabled(true);

        jChkBillAddr.setEnabled(true);
        jChkShiftAddr.setEnabled(true);

        txtPostal.setEnabled(true);
        txtCity.setEnabled(true);
        txtRegion.setEnabled(true);
        txtCountry.setEnabled(true);

        m_jCategory.setEnabled(true);

        jTABillAddress.setEnabled(true);
        jTAShiftAddress.setEnabled(true);

        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
      
    }
    @Override
    public Object createValue() throws BasicException {
        Object[] customer = new Object[33];
        System.out.println("createvalue");
     // Object[] customer = new Object[33];
        String customerid= UUID.randomUUID().toString();
        String custId = customerid.replaceAll("-", "");
        customer[0] = m_oId == null ? custId : m_oId;
        customer[1] = m_jCustomerId.getText();
        customer[2] = m_jCustomerId.getText();
        customer[3] = m_jName.getText();
        customer[4] = jcard.getText();
        customer[5] = Boolean.valueOf(m_jVisible.isSelected());
        customer[6] = Formats.STRING.parseValue(jcard.getText()); // Format to manage NULL values
        customer[7] = Formats.CURRENCY.parseValue(txtMaxdebt.getText(), new Double(0.0));
        customer[8] = Formats.TIMESTAMP.parseValue(txtCurdate.getText()); // not saved
        customer[9] = Formats.CURRENCY.parseValue(txtCurdebt.getText()); // not saved

        customer[10] = Formats.STRING.parseValue(txtFirstName.getText());
        customer[11] = Formats.STRING.parseValue(txtLastName.getText());
        customer[12] = Formats.STRING.parseValue(m_jEmailId.getText());
        customer[13] = Formats.STRING.parseValue(m_jCusPhone.getText());
        customer[14] = Formats.STRING.parseValue(txtPhone2.getText());
        customer[15] = Formats.STRING.parseValue(txtFax.getText());

        customer[16] = Formats.STRING.parseValue(txtAddress.getText());
        customer[17] = Formats.STRING.parseValue(txtAddress2.getText());

        boolean isBillChecked = jChkBillAddr.isSelected();
        boolean isShiftChecked = jChkShiftAddr.isSelected();


        customer[18] = Boolean.valueOf(isBillChecked);
        customer[19] = Boolean.valueOf(isShiftChecked);


        customer[20] = Formats.STRING.parseValue(txtPostal.getText());
        customer[21] = Formats.STRING.parseValue(txtCity.getText());
        customer[22] = Formats.STRING.parseValue(txtRegion.getText());
        customer[23] = Formats.STRING.parseValue(txtCountry.getText());

        customer[24] = m_CategoryModel.getSelectedKey();
        customer[25] = Boolean.valueOf(m_jisCustomer.isSelected());
        customer[26] = Formats.STRING.parseValue(jTABillAddress.getText());
        customer[27] = Formats.STRING.parseValue(jTAShiftAddress.getText());
        customer[28] = Formats.STRING.parseValue(m_jCustomerId.getText());
        customer[29] = Boolean.valueOf(m_jisCreditCustomer.isSelected());
        customer[30] = "Y";
         if(jReceivable.getText().toString().equals("")&&customer[25].equals(false))
       {
            System.out.println("within rec if");
             try {
        List<AccountPojo> list=dlCustomers.getAccountValue();
         System.out.println("INSIDE TRY");
        if(list!=null && list.size()!=0 ){
            jReceivable.setText(list.get(0).getReceivable().toString());
         }
           }
       catch(Exception e){
          System.out.println("exception");
     }

         }

          else if(jReceivable.getText().toString().equals("")&&customer[25].equals(true))
       {
            System.out.println("within rec if");
             try {
        List<AccountPojo> list=dlCustomers.getVendorAccountValue();
         System.out.println("INSIDE TRY");
        if(list!=null && list.size()!=0 ){
            jReceivable.setText(list.get(0).getReceivable().toString());
         }
           }
       catch(Exception e){
          System.out.println("exception");
     }

        }

        if(jAdvance.getText().toString().equals("")&&customer[25].equals(false))
       {
          System.out.println("within if");
           try {
                System.out.println("within try");
         List<AccountPojo> list=dlCustomers.getAccountValue();
         if(list!=null && list.size()!=0 ){
             jAdvance.setText(list.get(0).getAdvance().toString());
 }
           }
         catch(Exception e){
           System.out.println("exception");
        }
       }
       else if(jAdvance.getText().toString().equals("")&&customer[25].equals(true))
       {
          System.out.println("within if");
           try {
                System.out.println("within try");
         List<AccountPojo> list=dlCustomers.getVendorAccountValue();
         if(list!=null && list.size()!=0 ){
             jAdvance.setText(list.get(0).getAdvance().toString());
 }
           }
         catch(Exception e){
           System.out.println("exception");
        }

       }
        for(int i=0;i<1;i++)
        {
          if(m_jName.getText().toString().equals(""))
        {
             showMsg(this,"Please Enter Customer Name");
             break;
         }
        }
       if(m_jCusPhone.getText().toString().equals(""))
        {
             showMsg(this,"Please Enter Phone No.");
        }
        customer[31] =Formats.STRING.parseValue(jReceivable.getText());
        customer[32] =Formats.STRING.parseValue(jAdvance.getText());
        if(isBillChecked){
            customer[26] = appendAddresses();
        }else{
            customer[26] = jTABillAddress.getText();
        }

        if(isShiftChecked){
            customer[27] = appendAddresses();
        }else{
            customer[27] = jTAShiftAddress.getText();
        }

        try {
            dlCustomers.insertSetCustomer(customer[28].toString());
        } catch (BasicException ex) {
            Logger.getLogger(CustomersView.class.getName()).log(Level.SEVERE, null, ex);
        } 
      
        return customer;
      
    }

    public Component getComponent() {
        return this;
    }
 private void setCustomerId(){

  String customerId = null;
        try {
            try {
                customerId = dlSales.getCustomerId();
            } catch (BasicException ex) {
                Logger.getLogger(CustomersView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullPointerException ex) {
            customerId = "";
        }
            String storeName = m_App.getProperties().getStoreName();
            String[] cusValue;
        if(customerId.equals(null) || customerId.equals("")){
            customerId = storeName+"-"+10001;
        }else {
           cusValue = customerId.split("-");
            int value = (Integer.parseInt(cusValue[1]))+1;
            customerId = storeName+"-"+value;
        }
        m_jCustomerId.setText(customerId);
        m_jCustomerId.setEditable(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        m_jEmailId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        m_jCusPhone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMaxdebt = new javax.swing.JTextField();
        JtabbedAccount = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCountry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAddress2 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtPostal = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtRegion = new javax.swing.JTextField();
        jChkBillAddr = new javax.swing.JCheckBox();
        jChkShiftAddr = new javax.swing.JCheckBox();
        Country = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTABillAddress = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTAShiftAddress = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTANotes = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtFax = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtPhone2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jAdvance = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jReceivable = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCurdebt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCurdate = new javax.swing.JTextField();
        m_jisCustomer = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        m_jCustomerId = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        m_jisCreditCustomer = new javax.swing.JCheckBox();

        jButton1.setText("jButton1");

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 66, 120, -1));
        add(m_jEmailId, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 63, 130, 20));

        jLabel8.setText(AppLocal.getIntString("label.searchkey")); // NOI18N
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 120, -1));
        add(m_jCusPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 89, 130, 20));

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, -1));
        add(m_jName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 37, 130, 20));

        jLabel4.setText("Active");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 116, 90, -1));
        add(m_jVisible, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, -1, 20));

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 164, 120, -1));

        jcard.setEditable(false);
        add(jcard, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 130, 20));

        jLabel9.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 80, 120, -1));
        add(m_jCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 130, 20));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, 20));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 9));
        jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 120, 20));

        txtMaxdebt.setFont(new java.awt.Font("Tahoma", 0, 9));
        txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtMaxdebt, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 130, 20));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 16, 140, -1));
        jPanel2.add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 11, 130, -1));
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 141, 140, -1));
        jPanel2.add(txtCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 141, 130, -1));

        jLabel21.setText(AppLocal.getIntString("label.address2")); // NOI18N
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, 140, -1));
        jPanel2.add(txtAddress2, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 37, 130, -1));

        jLabel22.setText(AppLocal.getIntString("label.postal")); // NOI18N
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 68, 140, -1));
        jPanel2.add(txtPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 63, 130, -1));

        jLabel23.setText(AppLocal.getIntString("label.city")); // NOI18N
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 94, 140, -1));
        jPanel2.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 89, 130, -1));

        jLabel24.setText(AppLocal.getIntString("label.region")); // NOI18N
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 140, -1));
        jPanel2.add(txtRegion, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 115, 130, -1));

        jChkBillAddr.setText("Is Bill Address");
        jChkBillAddr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChkBillAddrActionPerformed(evt);
            }
        });
        jPanel2.add(jChkBillAddr, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 11, -1, -1));

        jChkShiftAddr.setText("Is Ship Address");
        jChkShiftAddr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jChkShiftAddrActionPerformed(evt);
            }
        });
        jPanel2.add(jChkShiftAddr, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 37, -1, -1));

        Country.setText("Country");
        jPanel2.add(Country, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 146, 140, -1));

        JtabbedAccount.addTab(AppLocal.getIntString("label.location"), jPanel2); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTABillAddress.setColumns(20);
        jTABillAddress.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTABillAddress.setRows(5);
        jTABillAddress.setPreferredSize(new java.awt.Dimension(160, 94));
        jScrollPane1.setViewportView(jTABillAddress);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );

        JtabbedAccount.addTab(AppLocal.getIntString("label.notes"), jPanel3); // NOI18N

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTAShiftAddress.setColumns(20);
        jTAShiftAddress.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTAShiftAddress.setRows(5);
        jTAShiftAddress.setPreferredSize(new java.awt.Dimension(160, 94));
        jScrollPane2.setViewportView(jTAShiftAddress);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );

        JtabbedAccount.addTab("Ship Address", jPanel4);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTANotes.setColumns(20);
        jTANotes.setFont(new java.awt.Font("Tahoma", 0, 11));
        jTANotes.setRows(5);
        jScrollPane3.setViewportView(jTANotes);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );

        JtabbedAccount.addTab("Notes", jPanel5);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText(AppLocal.getIntString("label.fax")); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 140, -1));
        jPanel1.add(txtFax, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 89, 130, -1));

        jLabel15.setText(AppLocal.getIntString("label.lastname")); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 140, -1));
        jPanel1.add(txtLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 37, 130, -1));

        jLabel16.setText(AppLocal.getIntString("label.email")); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 118, 140, -1));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 115, 130, -1));

        jLabel17.setText(AppLocal.getIntString("label.phone")); // NOI18N
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 140, -1));
        jPanel1.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 141, 130, -1));

        jLabel18.setText(AppLocal.getIntString("label.phone2")); // NOI18N
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 66, 140, -1));
        jPanel1.add(txtPhone2, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 63, 130, -1));

        jLabel19.setText(AppLocal.getIntString("label.firstname")); // NOI18N
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 140, -1));
        jPanel1.add(txtFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 11, 130, 20));

        JtabbedAccount.addTab(AppLocal.getIntString("label.contact"), jPanel1); // NOI18N

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setText(AppLocal.getIntString("label.lastname")); // NOI18N
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 140, -1));
        jPanel6.add(jAdvance, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 37, 130, -1));

        jLabel30.setText(AppLocal.getIntString("label.firstname")); // NOI18N
        jPanel6.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 140, -1));
        jPanel6.add(jReceivable, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 11, 130, 20));

        JtabbedAccount.addTab("Account", jPanel6);

        add(JtabbedAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 480, 210));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 9));
        jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 120, 20));

        txtCurdebt.setEditable(false);
        txtCurdebt.setFont(new java.awt.Font("Tahoma", 0, 9));
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtCurdebt, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 130, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 9));
        jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 120, 10));

        txtCurdate.setEditable(false);
        txtCurdate.setFont(new java.awt.Font("Tahoma", 0, 9));
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        add(txtCurdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 130, 10));

        m_jisCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jisCustomerActionPerformed(evt);
            }
        });
        add(m_jisCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 131, 20, 30));

        jLabel10.setText("Vendor");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 142, 80, -1));

        m_jCustomerId.setEnabled(false);
        add(m_jCustomerId, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 11, 130, 20));

        jLabel11.setText("Customer/Vendor Id");
        add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 140, -1));

        jLabel12.setText("Is Credit Customer");
        add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 166, 130, -1));
        add(m_jisCreditCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 20, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText("c" + StringUtils.getCardNumber());
            m_Dirty.setDirty(true);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText(null);
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jChkBillAddrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChkBillAddrActionPerformed
        // TODO add your handling code here:
        if (jChkBillAddr.isSelected()) {
            enableBillAddressTab(!true);
            //String billaddress = appendBillAddresses();
            //jTABillAddress.setText(billaddress);
        } else {
            enableBillAddressTab(!false);
            //jTABillAddress.setText("");
        }
    }//GEN-LAST:event_jChkBillAddrActionPerformed

    private void jChkShiftAddrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jChkShiftAddrActionPerformed
        // TODO add your handling code here:
        if (jChkShiftAddr.isSelected()) {
            enableShiftAddressTab(!true);
            //String shipaddress = appendShipAddress();
            //jTAShiftAddress.setText(shipaddress);
        } else {
            enableShiftAddressTab(!false);
            jTAShiftAddress.setText("");
        }
    }//GEN-LAST:event_jChkShiftAddrActionPerformed

    private void m_jisCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jisCustomerActionPerformed
        if(m_jisCustomer.isSelected()){
            m_jisCreditCustomer.setEnabled(false);
            m_jisCreditCustomer.setSelected(false);
        }else{
            m_jisCreditCustomer.setEnabled(true);

        }
    }//GEN-LAST:event_m_jisCustomerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Country;
    private javax.swing.JTabbedPane JtabbedAccount;
    private javax.swing.JTextField jAdvance;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jChkBillAddr;
    private javax.swing.JCheckBox jChkShiftAddr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jReceivable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTABillAddress;
    private javax.swing.JTextArea jTANotes;
    private javax.swing.JTextArea jTAShiftAddress;
    private javax.swing.JTextField jcard;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jCusPhone;
    private javax.swing.JTextField m_jCustomerId;
    private javax.swing.JTextField m_jEmailId;
    private javax.swing.JTextField m_jName;
    private javax.swing.JCheckBox m_jVisible;
    private javax.swing.JCheckBox m_jisCreditCustomer;
    private javax.swing.JCheckBox m_jisCustomer;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAddress2;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtCurdate;
    private javax.swing.JTextField txtCurdebt;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFax;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMaxdebt;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPhone2;
    private javax.swing.JTextField txtPostal;
    private javax.swing.JTextField txtRegion;
    // End of variables declaration//GEN-END:variables

    private void enableBillAddressTab(boolean b) {
        jTABillAddress.setEnabled(b);
         jTABillAddress.setEditable(b);
        jTABillAddress.setText("");
    }

    private void enableShiftAddressTab(boolean b) {
        jTAShiftAddress.setEnabled(b);
         jTAShiftAddress.setEditable(b);
        jTAShiftAddress.setText("");
    }

    private String appendAddresses() {

        gBillAddress.replace(0, gBillAddress.length(), "");
        String addr1 = txtAddress.getText();
        String addr2 = txtAddress2.getText();
        String postal = txtPostal.getText();
        String city = txtCity.getText();
        String region = txtRegion.getText();
        String country = txtCountry.getText();
        if (!addr1.isEmpty()) {
            gBillAddress.append(addr1+" ,");
        }
        if (!addr2.isEmpty()) {
            gBillAddress.append(addr2+" ,");
        }

        if (!city.isEmpty()) {
            gBillAddress.append(city+" ,");
        }
        if (!region.isEmpty()) {
            gBillAddress.append(region+" ,");
        }
        if (!country.isEmpty()) {
            gBillAddress.append(country);
        }
        return gBillAddress.toString();
    }

}
