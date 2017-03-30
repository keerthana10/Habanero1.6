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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import java.security.SecureRandom;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class ProductsEditor extends JPanel implements EditorRecord {
       
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;

    private SentenceList taxcatsent;
    private ComboBoxValModel taxcatmodel;  
    private SentenceList serchargesent;
    private ComboBoxValModel serchargemodel; 
    private SentenceList sertaxsent;
    private ComboBoxValModel sertaxmodel; 
    private SentenceList attsent;
    private ComboBoxValModel attmodel;
    private SentenceList m_sentuom;
    private ComboBoxValModel m_UomModel;
    private SentenceList taxsent;
    private TaxesLogic taxeslogic;
    
    private ComboBoxValModel m_CodetypeModel;
    
    private Object m_id;
    private Object pricesell;
    private boolean priceselllock = false;
    
    private boolean reportlock = false;
    private ComboBoxValModel m_PTypeModel;
    private SentenceList m_sentparea;
    private ComboBoxValModel m_PareaModel;
    private static final ProductsEditor RandomUtil = null;
    private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;
    /** Creates new form JEditProduct */
    public ProductsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();
        
        // The taxes sentence
        taxsent = dlSales.getTaxList();
             
        // The categories model
        m_sentcat = dlSales.getCategoriesList();
        m_CategoryModel = new ComboBoxValModel();
        
        // The taxes model
        taxcatsent = dlSales.getTaxCategoriesList();
        taxcatmodel = new ComboBoxValModel();
        serchargesent = dlSales.getServiceChargeList();
        serchargemodel = new ComboBoxValModel();
        sertaxsent = dlSales.getServiceTaxList();
        sertaxmodel = new ComboBoxValModel();
        m_sentuom=dlSales.getUomList();
        m_sentparea=dlSales.getProductionareatypeList();
        m_UomModel=new ComboBoxValModel();
         m_PTypeModel=new ComboBoxValModel();
        m_PareaModel=new ComboBoxValModel();
        // The attributes model
        attsent = dlSales.getAttributeSetList();
        attmodel = new ComboBoxValModel();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
               
        m_jRef.getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
         m_jUom.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jServiceCharge.addActionListener(dirty);
        m_jServiceTax.addActionListener(dirty);
        m_jAtt.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        isActiveCheckBox.addActionListener(dirty);
         m_jChkSalesItem.addActionListener(dirty);
          m_jChkPurchaseItem.addActionListener(dirty);
        m_jMrp.getDocument().addDocumentListener(dirty);
        FieldsManager fm = new FieldsManager();
        m_jPriceBuy.getDocument().addDocumentListener(fm);
        m_jPriceSell.getDocument().addDocumentListener(new PriceSellManager());
        m_jTax.addActionListener(fm);
        m_jServiceCharge.addActionListener(fm);
        m_jServiceTax.addActionListener(fm);
        m_jPType.addActionListener(dirty);
        m_jPAreaType.addActionListener(dirty);
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        m_jmargin.getDocument().addDocumentListener(new MarginManager());
        m_jChkpopular.addActionListener(dirty);
        m_jChkaddon.addActionListener(dirty);
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
         System.out.println("activate");  
        // Load the taxes logic
        taxeslogic = new TaxesLogic(taxsent.list());        
        
        m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
        m_jCategory.setModel(m_CategoryModel);

        taxcatmodel = new ComboBoxValModel(taxcatsent.list());
        m_jTax.setModel(taxcatmodel);
        serchargemodel = new ComboBoxValModel(serchargesent.list());
        serchargemodel.add(0,null);
        m_jServiceCharge.setModel(serchargemodel);
        sertaxmodel = new ComboBoxValModel(sertaxsent.list());
        sertaxmodel.add(0,null);
         m_jServiceTax.setModel(sertaxmodel);
         m_UomModel=new ComboBoxValModel(m_sentuom.list());
        m_jUom.setModel(m_UomModel);
        m_PTypeModel=new ComboBoxValModel();
        //m_PTypeModel.add("");
        m_PTypeModel.add("FOOD");
        m_PTypeModel.add("ALCOHOL");
        m_PTypeModel.add("MERCHANDIZE");
        m_PTypeModel.add("TOBACCO");
        m_PTypeModel.add("SOFTDRINKS");
        m_jPType.setModel(m_PTypeModel);
        m_PareaModel=new ComboBoxValModel(m_sentparea.list());
        m_jPAreaType.setModel(m_PareaModel);
        attmodel = new ComboBoxValModel(attsent.list());
        attmodel.add(0, null);
        m_jAtt.setModel(attmodel);
        isActiveCheckBox.setSelected(true);
        jLabel13.setVisible(false);
        m_jAtt.setVisible(false);
    }
    
    public void refresh() {
    }    
    
    public void writeValueEOF() {
          System.out.println("writeValueEOF");   
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_UomModel.setSelectedKey(null);
        m_PTypeModel.setSelectedKey(null);
        m_PareaModel.setSelectedKey(null);
        taxcatmodel.setSelectedKey(null);
        serchargemodel.setSelectedKey(null);
        sertaxmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        setPriceSell(null);         
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(false);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        m_jMrp.setText(null);
        reportlock = false;
        m_jPType.setEnabled(false);
        m_jPAreaType.setEnabled(false);
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jUom.setEnabled(false);
        //isActiveCheckBox.setEnabled(false);
       // m_jChkSalesItem.setEnabled(false);
      //  m_jChkPurchaseItem.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jServiceCharge.setEnabled(false);
        m_jServiceTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);

        txtAttributes.setEnabled(false);
        
        calculateMargin();
        calculatePriceSellTax();
    }
    public void writeValueInsert() {
        System.out.println("writeValueInsert");    
        reportlock = true;
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
       // m_id = UUID.randomUUID().toString();
        m_id = RandomUtil.genRandom32Hex();
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        //.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
         m_UomModel.setSelectedKey(null);
         m_PTypeModel.setSelectedKey(null);
        m_PareaModel.setSelectedKey(null);
        taxcatmodel.setSelectedKey(null);
        serchargemodel.setSelectedKey(null);
        sertaxmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        m_jMrp.setText(null);
        setPriceSell(null);                     
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(true);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        reportlock = false;
         m_jPType.setEnabled(true);
        m_jPAreaType.setEnabled(true);
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        isActiveCheckBox.setEnabled(true);
        m_jUom.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jServiceCharge.setEnabled(true);
        m_jServiceTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true);
        m_jMrp.setEnabled(true);
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
         isActiveCheckBox.setEnabled(true);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(true);
        m_jChkSalesItem.setEnabled(true);
        m_jChkPurchaseItem.setEnabled(true);
          m_jChkpopular.setEnabled(true);
            m_jChkaddon.setEnabled(true);
        calculateMargin();
        calculatePriceSellTax();
   }
    public void writeValueDelete(Object value) {
        System.out.println("writeValueDelete");    
        reportlock = true;       
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        setPriceSell(myprod[7]);                    
        m_CategoryModel.setSelectedKey(myprod[8]);
        taxcatmodel.setSelectedKey(myprod[9]);
        attmodel.setSelectedKey(myprod[10]);
        m_jImage.setImage((BufferedImage) myprod[11]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[12]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[13]));
        m_jInCatalog.setSelected(((Boolean)myprod[14]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[15]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        txtAttributes.setCaretPosition(0);
        m_jMrp.setText(Formats.CURRENCY.formatValue(myprod[17]));
         if(myprod[18]=="Y"){
             isActiveCheckBox.setSelected(true);
        }else{
             isActiveCheckBox.setSelected(false);
        }
         if(myprod[19]=="Y"){
             m_jChkSalesItem.setSelected(true);
        }else{
             m_jChkSalesItem.setSelected(false);
        }
         if(myprod[20]=="Y"){
             m_jChkPurchaseItem.setSelected(true);
        }else{
             m_jChkPurchaseItem.setSelected(false);
        }
  m_UomModel.setSelectedKey(myprod[21]);
 serchargemodel.setSelectedKey(myprod[22]);
 sertaxmodel.setSelectedKey(myprod[23]);
 m_PTypeModel.setSelectedKey(myprod[24]);
 m_PareaModel.setSelectedKey(myprod[25]);
 if(myprod[26]=="Y"){
     m_jChkpopular.setSelected(true);
   }else{
       m_jChkpopular.setSelected(false); 
   }
 if(myprod[27]=="Y"){
      m_jChkaddon.setSelected(true);
   }else{
       m_jChkaddon.setSelected(false); 
   }
        reportlock = false;
        
        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
         m_jUom.setEnabled(false);
       // isActiveCheckBox.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jServiceCharge.setEnabled(false);
        m_jServiceTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        m_jMrp.setEnabled(false);
         m_jPType.setEnabled(false);
         m_jPAreaType.setEnabled(false);
        calculateMargin();
        calculatePriceSellTax();
    }    
    
    public void writeValueEdit(Object value) {
        System.out.println("writeValueEdit");    
        reportlock = true;
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[3]));
        m_id = myprod[0];
        m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[6]));
        setPriceSell(myprod[7]);                               
        m_CategoryModel.setSelectedKey(myprod[8]);
        taxcatmodel.setSelectedKey(myprod[9]);
        attmodel.setSelectedKey(myprod[10]);
        m_jImage.setImage((BufferedImage) myprod[11]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[12]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[13]));
        m_jInCatalog.setSelected(((Boolean)myprod[14]).booleanValue());
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[15]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        txtAttributes.setCaretPosition(0);
        m_jMrp.setText(Formats.CURRENCY.formatValue(myprod[17]));
        if(myprod[18].equals("Y")){
             isActiveCheckBox.setSelected(true);
        }else{
             isActiveCheckBox.setSelected(false);
        }
         if(myprod[19].equals("Y")){
             m_jChkSalesItem.setSelected(true);
        }else{
             m_jChkSalesItem.setSelected(false);
        }
         if(myprod[20].equals("Y")){
             m_jChkPurchaseItem.setSelected(true);
        }else{
             m_jChkPurchaseItem.setSelected(false);
        }
 m_UomModel.setSelectedKey(myprod[21]);
 serchargemodel.setSelectedKey(myprod[22]);
 sertaxmodel.setSelectedKey(myprod[23]);
 m_PTypeModel.setSelectedItem(myprod[24]);
 m_PareaModel.setSelectedKey(myprod[25]);
 if(myprod[26].equals("Y")){
     m_jChkpopular.setSelected(true);
   }else{
       m_jChkpopular.setSelected(false); 
   }
   if(myprod[27].equals("Y")){
      m_jChkaddon.setSelected(true);
   }else{
       m_jChkaddon.setSelected(false); 
   }
        reportlock = false;
         m_jPType.setEnabled(true);
        m_jPAreaType.setEnabled(true);
        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jServiceCharge.setEnabled(true);
        m_jServiceTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jUom.setEnabled(true);
       // isActiveCheckBox.setEnabled(false);
        m_jPriceSell.setEnabled(true); 
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());  
        txtAttributes.setEnabled(true);
        m_jMrp.setEnabled(true);
        calculateMargin();
        calculatePriceSellTax();
    }

    public Object createValue() throws BasicException {
        System.out.println("createValue");    
        Object[] myprod = new Object[28];
        myprod[0] = m_id;
        myprod[1] = m_jRef.getText();
        myprod[2] = m_jCode.getText();
        myprod[3] = m_jName.getText();
        myprod[4] = Boolean.valueOf(m_jComment.isSelected());
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
        myprod[7] = pricesell;
        myprod[8] = m_CategoryModel.getSelectedKey();
        myprod[9] = taxcatmodel.getSelectedKey();
        myprod[10] = attmodel.getSelectedKey();
        myprod[11] = m_jImage.getImage();
        myprod[12] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
        myprod[13] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
        myprod[14] = Boolean.valueOf(m_jInCatalog.isSelected());
        myprod[15] = Formats.INT.parseValue(m_jCatalogOrder.getText());
        myprod[16] = Formats.BYTEA.parseValue(txtAttributes.getText());
        myprod[17] = Formats.CURRENCY.parseValue(m_jMrp.getText());
        String isActive;
        String isSales;
        String isPurchase;
        String isPopular;
        String isAddon;
        if(isActiveCheckBox.isSelected()==true){
            isActive = "Y";
        }else{
            isActive = "N";
        }
         if(m_jChkSalesItem.isSelected()==true){
            isSales = "Y";
        }else{
            isSales = "N";
        }
         if(m_jChkPurchaseItem.isSelected()==true){
            isPurchase = "Y";
        }else{
            isPurchase = "N";
        }
        myprod[18] = isActive;
        myprod[19] = isSales;
        myprod[20] = isPurchase;
        myprod[21] = m_UomModel.getSelectedKey();
        myprod[22] =serchargemodel.getSelectedKey();
        myprod[23] =sertaxmodel.getSelectedKey();
        myprod[24]=m_PTypeModel.getSelectedItem();
        myprod[25]=m_PareaModel.getSelectedKey();
         if(m_jChkpopular.isSelected()==true){
            isPopular="Y";
        }else{
           isPopular="N";
        }
        if(m_jChkaddon.isSelected()==true){
            isAddon="Y";
        }else{
            isAddon="N";
        }
         myprod[26]=isPopular;
          myprod[27]=isAddon;
        return myprod;
    }    
    
    public Component getComponent() {
        return this;
    }
    
    private void calculateMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dPriceSell = (Double) pricesell;

            if (dPriceBuy == null || dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(new Double(dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
            }    
            reportlock = false;
        }
    }
    
    private void calculatePriceSellTax() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceSell = (Double) pricesell;
            
            if (dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {               
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem());
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }            
            reportlock = false;
        }
    }
    
    private void calculatePriceSellfromMargin() {
        
        if (!reportlock) {
            reportlock = true;
            
            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dMargin = readPercent(m_jmargin.getText());  
            
            if (dMargin == null || dPriceBuy == null) {
                setPriceSell(null);
            } else {
                setPriceSell(new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue())));
            }                        
            
            reportlock = false;
        }
      
    }
    
    private void calculatePriceSellfromPST() {
        
        if (!reportlock) {
            reportlock = true;
                    
            Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  

            if (dPriceSellTax == null) {
                setPriceSell(null);
            } else {
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem()); 
                setPriceSell(new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate)));
            }   
                        
            reportlock = false;
        }    
    }
    
    private void setPriceSell(Object value) {
        
        if (!priceselllock) {
            priceselllock = true;
            pricesell = value;
            m_jPriceSell.setText(Formats.CURRENCY.formatValue(pricesell));  
            priceselllock = false;
        }
    }
    
    private class PriceSellManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
        }  
    }
    
    private class FieldsManager implements DocumentListener, ActionListener {
        public void changedUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }         
        public void actionPerformed(ActionEvent e) {
            calculateMargin();
            calculatePriceSellTax();
        }
    }

    private class PriceTaxManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
        }         
    }
    
    private class MarginManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }    
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
        }         
    }
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        m_jCode = new javax.swing.JTextField();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabelServiceCharge = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        m_jmargin = new javax.swing.JTextField();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        m_jCodetype = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        m_jAtt = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        m_jMrp = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        isActiveCheckBox = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        m_jChkSalesItem = new javax.swing.JCheckBox();
        m_jChkPurchaseItem = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        m_jUom = new javax.swing.JComboBox();
        jLabelTax = new javax.swing.JLabel();
        m_jServiceCharge = new javax.swing.JComboBox();
        jLabelServiceCharge1 = new javax.swing.JLabel();
        m_jServiceTax = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        m_jPType = new javax.swing.JComboBox();
        m_jPAreaType = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        m_jChkpopular = new javax.swing.JCheckBox();
        m_jChkaddon = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jstockvolume = new javax.swing.JTextField();
        m_jScale = new javax.swing.JCheckBox();
        m_jComment = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        m_jCatalogOrder = new javax.swing.JTextField();
        m_jInCatalog = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(10, 50, 80, 18);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(180, 50, 70, 18);
        add(m_jRef);
        m_jRef.setBounds(90, 50, 70, 28);
        add(m_jName);
        m_jName.setBounds(250, 50, 220, 28);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18)); // NOI18N
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 320, 30);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, -1));
        jPanel1.add(m_jCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 170, -1));
        jPanel1.add(m_jImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 200, 180));

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, -1));

        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 80, -1));

        jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 150, -1));

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 80, -1));

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 150, 20));
        jPanel1.add(m_jCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 170, -1));

        jLabelServiceCharge.setText("Service Charge");
        jPanel1.add(jLabelServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 150, -1));
        jPanel1.add(m_jTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 170, -1));

        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jmargin, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 80, -1));

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSellTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 80, -1));

        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 150, -1));
        jPanel1.add(m_jCodetype, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 80, -1));

        jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 150, 20));
        jPanel1.add(m_jAtt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 520, 170, 20));

        jLabel14.setText("MRP");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, -1));

        m_jMrp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jMrp, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 80, -1));

        jLabel15.setText("Active");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 100, -1));

        isActiveCheckBox.setSelected(true);
        jPanel1.add(isActiveCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, -1, -1));

        jLabel17.setText("Is Purchase Item");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 120, 20));

        jLabel19.setText("Is Sales Item");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 130, 20));
        jPanel1.add(m_jChkSalesItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, -1, -1));
        jPanel1.add(m_jChkPurchaseItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, -1, -1));

        jLabel20.setText("UOM");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 110, 20));

        jPanel1.add(m_jUom, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 170, -1));

        jLabelTax.setText("Tax category");
        jPanel1.add(jLabelTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 150, -1));
        jPanel1.add(m_jServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 170, -1));

        jLabelServiceCharge1.setText("Service Tax");
        jPanel1.add(jLabelServiceCharge1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 150, -1));
        jPanel1.add(m_jServiceTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 170, -1));

        jLabel21.setText("Product Type");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 150, -1));
        jPanel1.add(m_jPType, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 170, -1));
        jPanel1.add(m_jPAreaType, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 170, -1));

        jLabel22.setText("ProductionArea Type");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 150, -1));

        jLabel23.setText("Is Popular Item");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 130, -1));
        jPanel1.add(m_jChkpopular, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 460, -1, -1));
        jPanel1.add(m_jChkaddon, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 490, -1, -1));

        jLabel24.setText("Is Addon Item");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 130, -1));

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 20, 150, 18);

        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(160, 20, 80, 28);

        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 50, 150, 18);

        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockvolume);
        m_jstockvolume.setBounds(160, 50, 80, 28);
        jPanel2.add(m_jScale);
        m_jScale.setBounds(160, 140, 80, 24);
        jPanel2.add(m_jComment);
        m_jComment.setBounds(160, 110, 80, 24);

        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(250, 80, 60, 18);

        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(310, 80, 80, 28);

        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel2.add(m_jInCatalog);
        m_jInCatalog.setBounds(160, 80, 50, 24);

        jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 80, 150, 18);

        jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 110, 150, 18);

        jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
        jPanel2.add(jLabel12);
        jLabel12.setBounds(10, 140, 150, 18);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(AppLocal.getIntString("label.properties"), jPanel3); // NOI18N

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 90, 560, 590);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
 
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);   
        } else {
            m_jCatalogOrder.setEnabled(false);   
            m_jCatalogOrder.setText(null);   
        }

    }//GEN-LAST:event_m_jInCatalogActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox isActiveCheckBox;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelServiceCharge;
    private javax.swing.JLabel jLabelServiceCharge1;
    private javax.swing.JLabel jLabelTax;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox m_jAtt;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JCheckBox m_jChkPurchaseItem;
    private javax.swing.JCheckBox m_jChkSalesItem;
    private javax.swing.JCheckBox m_jChkaddon;
    private javax.swing.JCheckBox m_jChkpopular;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private javax.swing.JCheckBox m_jComment;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private javax.swing.JCheckBox m_jInCatalog;
    private javax.swing.JTextField m_jMrp;
    private javax.swing.JTextField m_jName;
    private javax.swing.JComboBox m_jPAreaType;
    private javax.swing.JComboBox m_jPType;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jRef;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JComboBox m_jServiceCharge;
    private javax.swing.JComboBox m_jServiceTax;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JComboBox m_jUom;
    private javax.swing.JTextField m_jmargin;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables
   
    public static String genRandom32Hex() {
    SecureRandom ng = numberGenerator;
    if (ng == null) {
      numberGenerator = ng = new SecureRandom();
    }

    return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
  }
}
