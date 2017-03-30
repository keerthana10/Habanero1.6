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

package com.openbravo.pos.sales;

import com.openbravo.basic.BasicException;
import com.openbravo.format.Formats;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.RetailTicketLineInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

/**
 *
 * @author adrianromero
 */
public class JRetailProductLineEdit extends javax.swing.JDialog {
    
    private RetailTicketLineInfo returnLine;
    private RetailTicketLineInfo m_oLine;
    private boolean m_bunitsok;
    private boolean m_bpriceok;
    private String buttonInstruction="";
    private String statInstruction="";
    private String savedButtonInstruction="";
    String[] splitInstructValue;
    String previousInstructValue="";
            
    /** Creates new form JRetailProductLineEdit */
    private JRetailProductLineEdit(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    /** Creates new form JRetailProductLineEdit */
    private JRetailProductLineEdit(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }
    
    private RetailTicketLineInfo init(AppView app, RetailTicketLineInfo oLine) throws BasicException {
        // Inicializo los componentes
        initComponents();
        jInsButton.setBackground(Color.white);
        m_jInstruction.setEnabled(false);
        jButtonIce.setBackground(Color.ORANGE); 
        jButtonSoda.setBackground(Color.ORANGE);
        jButtonSprite.setBackground(Color.ORANGE); 
        jButtonCoke.setBackground(Color.ORANGE);
        jButtonSS.setBackground(Color.ORANGE);
        jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE); 
        
        if (oLine.getTaxInfo() == null) {
            throw new BasicException(AppLocal.getIntString("message.cannotcalculatetaxes"));
        }
        InputMap im = m_jBtnCancel.getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

        InputMap im1 = m_jButtonOK.getInputMap();
        im1.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im1.put(KeyStroke.getKeyStroke("released ENTER"), "released");

        m_oLine = new RetailTicketLineInfo(oLine);
        m_bunitsok = true;
        m_bpriceok = true;

        m_jName.setEnabled(m_oLine.getProductID() == null && app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        m_jPrice.setEnabled(app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        m_jPriceTax.setEnabled(app.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        
        m_jName.setText(m_oLine.getProperty("product.name"));
        m_jUnits.setDoubleValue(oLine.getMultiply());
        m_jPrice.setText(Formats.CURRENCY.formatValue(oLine.getPrice()));
        m_jPriceTax.setText(Formats.CURRENCY.formatValue(oLine.getPriceTax()));
        m_jTaxrate.setText(oLine.getTaxInfo().getName());
        
        //Instruction logic addition by Shilpa
        String getInstructValue=oLine.getInstruction();
        if(getInstructValue!=null && getInstructValue!=""){
        splitInstructValue=getInstructValue.split(";");
        for(int i=1;i<splitInstructValue.length;i++){
            previousInstructValue=previousInstructValue+";"+splitInstructValue[i];
            if(i==1){
            savedButtonInstruction=savedButtonInstruction+splitInstructValue[i];
            }else{
            savedButtonInstruction=savedButtonInstruction+";"+splitInstructValue[i]; 
            }
          }
        statInstruction= splitInstructValue[0];
        }
         
        m_jInstruction.setText(statInstruction);
        m_jInstructionButton.setText(savedButtonInstruction);
        
        m_jName.addPropertyChangeListener("Edition", new RecalculateName());
        m_jUnits.addPropertyChangeListener("Edition", new RecalculateUnits());
        m_jPrice.addPropertyChangeListener("Edition", new RecalculatePrice());
        m_jPriceTax.addPropertyChangeListener("Edition", new RecalculatePriceTax());
        
        

        m_jName.addEditorKeys(m_jKeys);
        m_jUnits.addEditorKeys(m_jKeys);
//        m_jPrice.addEditorKeys(m_jKeys);
       // m_jPriceTax.addEditorKeys(m_jKeys);
        
        if (m_jName.isEnabled()) {
            m_jName.activate();
        } else {
            m_jUnits.activate();
        }
        
        printTotals();

    //    getRootPane().setDefaultButton(m_jButtonOK);
        returnLine = null;
        setVisible(true);
      
        return returnLine;
    }
    
    private void printTotals() {
      System.out.println("inside printtotals"+m_bunitsok+m_bpriceok);  
        if (m_bunitsok && m_bpriceok) {
            m_jSubtotal.setText(m_oLine.printSubValue());
            m_jTotal.setText(m_oLine.printValue());
            m_jButtonOK.setEnabled(true);
       } else {
            m_jSubtotal.setText(null);
            m_jTotal.setText(null);
            m_jButtonOK.setEnabled(false);
        }
    }
    
    private class RecalculateUnits implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
              System.out.println("inside RecalculateUnits");
            Double value = m_jUnits.getDoubleValue();
            if (value == null || value == 0.0) {
                m_bunitsok = false;
            } else {
                m_oLine.setMultiply(value);
                m_bunitsok = true;                
            }

            printTotals();
        }
    }
    
    private class RecalculatePrice implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {

            Double value = Double.parseDouble(m_jPrice.getText());
            if (value == null || value == 0.0) {
                m_bpriceok = false;
            } else {
                m_oLine.setPrice(value);
                m_jPriceTax.setText(Formats.CURRENCY.formatValue(m_oLine.getPriceTax()));
                m_bpriceok = true;
            }

            printTotals();
        }
    }    
    
    private class RecalculatePriceTax implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {

            Double value = Double.parseDouble(m_jPriceTax.getText());
            if (value == null || value == 0.0) {
                // m_jPriceTax.setValue(m_oLine.getPriceTax());
                m_bpriceok = false;
            } else {
                m_oLine.setPriceTax(value);
                m_jPrice.setText(Formats.CURRENCY.formatValue(m_oLine.getPrice()));
                m_bpriceok = true;
            }

            printTotals();
        }
    }   
    
    private class RecalculateName implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            m_oLine.setProperty("product.name", m_jName.getText());
        }
    }   
    
//        private class GenerateInstruction implements PropertyChangeListener {
//        public void propertyChange(PropertyChangeEvent evt) {
//            System.out.println("inside GenerateInstruction");
//            
//        }
//    }  
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }       
    
    public static RetailTicketLineInfo showMessage(Component parent, AppView app, RetailTicketLineInfo oLine) throws BasicException {
         
        Window window = getWindow(parent);
        
        JRetailProductLineEdit myMsg;
        if (window instanceof Frame) { 
            myMsg = new JRetailProductLineEdit((Frame) window, true);
        } else {
            myMsg = new JRetailProductLineEdit((Dialog) window, true);
        }
        return myMsg.init(app, oLine);
    }        

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jName = new com.openbravo.editor.JEditorString();
        m_jTaxrate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        m_jTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        m_jSubtotal = new javax.swing.JLabel();
        m_jPrice = new javax.swing.JLabel();
        m_jPriceTax = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jUnits = new com.openbravo.editor.JEditorCurrencyPositive();
        m_jKeys = new com.openbravo.editor.JEditorNumberKeys();
        jLabel9 = new javax.swing.JLabel();
        m_jInstruction = new javax.swing.JTextField();
        jButtonSS = new javax.swing.JButton();
        jButtonOil = new javax.swing.JButton();
        jButtonSoda = new javax.swing.JButton();
        jButtonSprite = new javax.swing.JButton();
        jButtonTAkeaway = new javax.swing.JButton();
        jButtonCoke = new javax.swing.JButton();
        jButtonIce = new javax.swing.JButton();
        jButtonSalt = new javax.swing.JButton();
        jButtonHot = new javax.swing.JButton();
        jButtonUrgent = new javax.swing.JButton();
        jButtonSweet = new javax.swing.JButton();
        jButtonSpicy = new javax.swing.JButton();
        jButtonVHot = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        m_jButtonOK = new javax.swing.JButton();
        m_jBtnCancel = new javax.swing.JButton();
        jInsButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        m_jInstructionButton = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("label.editline")); // NOI18N

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.price")); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 80, 90, 14);

        jLabel2.setText(AppLocal.getIntString("label.units")); // NOI18N
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 50, 90, 14);

        jLabel3.setText(AppLocal.getIntString("label.pricetax")); // NOI18N
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 110, 90, 14);

        jLabel4.setText(AppLocal.getIntString("label.item")); // NOI18N
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 20, 90, 14);
        jPanel2.add(m_jName);
        m_jName.setBounds(100, 20, 270, 25);

        m_jTaxrate.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jTaxrate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxrate.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxrate.setOpaque(true);
        m_jTaxrate.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxrate.setRequestFocusEnabled(false);
        jPanel2.add(m_jTaxrate);
        m_jTaxrate.setBounds(100, 140, 210, 25);

        jLabel5.setText("Sub Total");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 170, 90, 14);

        jLabel6.setText("Instruction2");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 270, 90, 14);

        m_jTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotal.setOpaque(true);
        m_jTotal.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotal.setRequestFocusEnabled(false);
        jPanel2.add(m_jTotal);
        m_jTotal.setBounds(100, 200, 210, 25);

        jLabel7.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        jPanel2.add(jLabel7);
        jLabel7.setBounds(10, 170, 90, 0);

        m_jSubtotal.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotal.setOpaque(true);
        m_jSubtotal.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotal.setRequestFocusEnabled(false);
        jPanel2.add(m_jSubtotal);
        m_jSubtotal.setBounds(100, 170, 210, 25);

        m_jPrice.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPrice.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPrice.setOpaque(true);
        m_jPrice.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jPrice.setRequestFocusEnabled(false);
        jPanel2.add(m_jPrice);
        m_jPrice.setBounds(100, 80, 210, 25);

        m_jPriceTax.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        m_jPriceTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPriceTax.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jPriceTax.setOpaque(true);
        m_jPriceTax.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jPriceTax.setRequestFocusEnabled(false);
        jPanel2.add(m_jPriceTax);
        m_jPriceTax.setBounds(100, 110, 210, 25);

        jLabel8.setText("Tax Rate");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 140, 90, 14);
        jPanel2.add(m_jUnits);
        m_jUnits.setBounds(100, 50, 240, 25);
        jPanel2.add(m_jKeys);
        m_jKeys.setBounds(390, 0, 175, 240);

        jLabel9.setText("Total");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 200, 90, 14);

        m_jInstruction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m_jInstructionMousePressed(evt);
            }
        });
        m_jInstruction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInstructionActionPerformed(evt);
            }
        });
        m_jInstruction.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                InstructionChanged(evt);
            }
        });
        m_jInstruction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jInstructionKeyPressed(evt);
            }
        });
        jPanel2.add(m_jInstruction);
        m_jInstruction.setBounds(100, 230, 210, 20);

        jButtonSS.setText("Sweet & Salt");
        jButtonSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSSActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSS);
        jButtonSS.setBounds(360, 310, 130, 23);

        jButtonOil.setText("Less Oil");
        jButtonOil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOilActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonOil);
        jButtonOil.setBounds(280, 350, 90, 23);

        jButtonSoda.setText("Soda");
        jButtonSoda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSodaActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSoda);
        jButtonSoda.setBounds(100, 310, 70, 23);

        jButtonSprite.setText("Sprite");
        jButtonSprite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpriteActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSprite);
        jButtonSprite.setBounds(180, 310, 80, 23);

        jButtonTAkeaway.setText("Takeaway");
        jButtonTAkeaway.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTAkeawayActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonTAkeaway);
        jButtonTAkeaway.setBounds(470, 350, 110, 23);

        jButtonCoke.setText("Coke");
        jButtonCoke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCokeActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonCoke);
        jButtonCoke.setBounds(270, 310, 80, 23);

        jButtonIce.setText("No Ice");
        jButtonIce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIceActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonIce);
        jButtonIce.setBounds(10, 310, 80, 23);

        jButtonSalt.setText("Salt");
        jButtonSalt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaltActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSalt);
        jButtonSalt.setBounds(10, 350, 70, 23);

        jButtonHot.setText("Hot");
        jButtonHot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHotActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonHot);
        jButtonHot.setBounds(90, 350, 70, 23);

        jButtonUrgent.setText("Urgent");
        jButtonUrgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrgentActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonUrgent);
        jButtonUrgent.setBounds(220, 390, 90, 23);

        jButtonSweet.setText("Sweet");
        jButtonSweet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSweetActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSweet);
        jButtonSweet.setBounds(500, 310, 80, 23);

        jButtonSpicy.setText("Spicy");
        jButtonSpicy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpicyActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonSpicy);
        jButtonSpicy.setBounds(380, 350, 80, 23);

        jButtonVHot.setText("Very Hot");
        jButtonVHot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVHotActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonVHot);
        jButtonVHot.setBounds(170, 350, 100, 23);

        m_jButtonOK.setBackground(new java.awt.Color(255, 255, 255));
        m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        m_jButtonOK.setText("Ok");
        m_jButtonOK.setPreferredSize(new java.awt.Dimension(80, 40));
        m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonOKActionPerformed(evt);
            }
        });

        m_jBtnCancel.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jBtnCancel.setText("Cancel");
        m_jBtnCancel.setPreferredSize(new java.awt.Dimension(101, 40));
        m_jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancelActionPerformed(evt);
            }
        });

        jInsButton.setText("Open Items");
        jInsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jInstrButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addComponent(jInsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jBtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jBtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jInsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.add(jPanel1);
        jPanel1.setBounds(0, 420, 580, 50);

        jLabel10.setText("Instruction1");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 230, 90, 14);

        m_jInstructionButton.setEditable(false);
        m_jInstructionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m_jInstructionButtonMousePressed(evt);
            }
        });
        m_jInstructionButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jInstructionButtonInstructionChanged(evt);
            }
        });
        m_jInstructionButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jInstructionButtonKeyPressed(evt);
            }
        });
        jPanel2.add(m_jInstructionButton);
        m_jInstructionButton.setBounds(100, 262, 210, 30);

        jPanel5.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));
        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-587)/2, (screenSize.height-502)/2, 587, 502);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonOKActionPerformed
    String instruction=m_jInstruction.getText()+previousInstructValue+buttonInstruction;
       m_oLine.setInstruction(instruction);
       returnLine = m_oLine;
       dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_m_jButtonOKActionPerformed

    private void m_jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancelActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnCancelActionPerformed

    private void InstructionChanged(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_InstructionChanged
//        String instruction=m_jInstruction.getText()+previousInstructValue+buttonInstruction;
//        m_oLine.setInstruction(instruction);
    }//GEN-LAST:event_InstructionChanged

    private void m_jInstructionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jInstructionKeyPressed
       if(m_jInstruction.getText().length() > 30)
            try {
            m_jInstruction.setText(m_jInstruction.getText(0, 30));
        } catch (BadLocationException ex) {
            Logger.getLogger(JRetailProductLineEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_m_jInstructionKeyPressed

    private void m_jInstructionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jInstructionMousePressed
      System.out.println("mouse pressed event") ;
      if(m_jInstruction.isEnabled()){
        if( System.getProperty("os.name").equalsIgnoreCase("Linux"))
            return;
        else
            try {
            Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
        } catch (IOException ex) {
            Logger.getLogger(JRetailProductLineEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_m_jInstructionMousePressed

    private void jButtonIceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIceActionPerformed
       if(jButtonIce.getBackground()==Color.ORANGE){
        jButtonIce.setBackground(Color.YELLOW);
        jButtonSoda.setBackground(Color.ORANGE);
        jButtonSprite.setBackground(Color.ORANGE); 
        jButtonCoke.setBackground(Color.ORANGE);
        jButtonSS.setBackground(Color.ORANGE);
        jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE); 
         buttonInstruction=";No Ice";
       }else{
        jButtonIce.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonIceActionPerformed

    private void jButtonSodaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSodaActionPerformed
       if(jButtonSoda.getBackground()==Color.ORANGE){
         jButtonSoda.setBackground(Color.YELLOW);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE); 
        jButtonCoke.setBackground(Color.ORANGE);
        jButtonSS.setBackground(Color.ORANGE);
        jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Soda";
       }else{
        jButtonSoda.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSodaActionPerformed

    private void jButtonSpriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpriteActionPerformed
        if(jButtonSprite.getBackground()==Color.ORANGE){
         jButtonSprite.setBackground(Color.YELLOW);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonCoke.setBackground(Color.ORANGE);
        jButtonSS.setBackground(Color.ORANGE);
        jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Sprite";
       }else{
        jButtonSprite.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSpriteActionPerformed

    private void jButtonCokeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCokeActionPerformed
        if(jButtonCoke.getBackground()==Color.ORANGE){
         jButtonCoke.setBackground(Color.YELLOW);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
        jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         
         buttonInstruction=";Coke";
       }else{
        jButtonCoke.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonCokeActionPerformed

    private void jButtonSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSSActionPerformed
        if(jButtonSS.getBackground()==Color.ORANGE){
         jButtonSS.setBackground(Color.YELLOW);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
        jButtonSalt.setBackground(Color.ORANGE); 
        jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Sweet and Salt";
       }else{
        jButtonSS.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSSActionPerformed

    private void jButtonSweetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSweetActionPerformed
        if(jButtonSweet.getBackground()==Color.ORANGE){
         jButtonSweet.setBackground(Color.YELLOW);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonSalt.setBackground(Color.ORANGE); 
         jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Sweet";
       }else{
        jButtonSweet.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSweetActionPerformed

    private void jButtonSaltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaltActionPerformed
    if(jButtonSalt.getBackground()==Color.ORANGE){
         jButtonSalt.setBackground(Color.YELLOW);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonHot.setBackground(Color.ORANGE); 
        jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Salt";
       }else{
        jButtonSalt.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSaltActionPerformed

    private void jButtonHotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHotActionPerformed
       if(jButtonHot.getBackground()==Color.ORANGE){
         jButtonHot.setBackground(Color.YELLOW);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonVHot.setBackground(Color.ORANGE); 
        jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Hot";
       }else{
        jButtonHot.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonHotActionPerformed

    private void jButtonVHotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVHotActionPerformed
       if(jButtonVHot.getBackground()==Color.ORANGE){
         jButtonVHot.setBackground(Color.YELLOW);
         jButtonHot.setBackground(Color.ORANGE);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonOil.setBackground(Color.ORANGE); 
        jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Very Hot";
       }else{
        jButtonVHot.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonVHotActionPerformed

    private void jButtonOilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOilActionPerformed
       if(jButtonOil.getBackground()==Color.ORANGE){
         jButtonOil.setBackground(Color.YELLOW);
         jButtonVHot.setBackground(Color.ORANGE);
         jButtonHot.setBackground(Color.ORANGE);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonSpicy.setBackground(Color.ORANGE); 
        jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Less Oil";
       }else{
        jButtonOil.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonOilActionPerformed

    private void jButtonSpicyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpicyActionPerformed
       if(jButtonSpicy.getBackground()==Color.ORANGE){
         jButtonSpicy.setBackground(Color.YELLOW);
         jButtonOil.setBackground(Color.ORANGE);
         jButtonVHot.setBackground(Color.ORANGE);
         jButtonHot.setBackground(Color.ORANGE);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonTAkeaway.setBackground(Color.ORANGE); 
        jButtonUrgent.setBackground(Color.ORANGE);
         
         buttonInstruction=";Spicy";
       }else{
        jButtonSpicy.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonSpicyActionPerformed

    private void jButtonTAkeawayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTAkeawayActionPerformed
      if(jButtonTAkeaway.getBackground()==Color.ORANGE){
         jButtonTAkeaway.setBackground(Color.YELLOW);
         jButtonSpicy.setBackground(Color.ORANGE);
         jButtonOil.setBackground(Color.ORANGE);
         jButtonVHot.setBackground(Color.ORANGE);
         jButtonHot.setBackground(Color.ORANGE);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         jButtonUrgent.setBackground(Color.ORANGE);
         buttonInstruction=";Takeaway";
       }else{
        jButtonTAkeaway.setBackground(Color.ORANGE);
         buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonTAkeawayActionPerformed

    private void jButtonUrgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrgentActionPerformed
       if(jButtonUrgent.getBackground()==Color.ORANGE){
         jButtonUrgent.setBackground(Color.YELLOW);
         jButtonTAkeaway.setBackground(Color.ORANGE);
         jButtonSpicy.setBackground(Color.ORANGE);
         jButtonOil.setBackground(Color.ORANGE);
         jButtonVHot.setBackground(Color.ORANGE);
         jButtonHot.setBackground(Color.ORANGE);
           jButtonSalt.setBackground(Color.ORANGE);
         jButtonSweet.setBackground(Color.ORANGE);
         jButtonSS.setBackground(Color.ORANGE);
          jButtonCoke.setBackground(Color.ORANGE);
         jButtonSprite.setBackground(Color.ORANGE);
         jButtonSoda.setBackground(Color.ORANGE);
         jButtonIce.setBackground(Color.ORANGE);
         buttonInstruction=";Urgent";
       }else{
        jButtonUrgent.setBackground(Color.ORANGE);
        buttonInstruction="";
       }
    }//GEN-LAST:event_jButtonUrgentActionPerformed

    private void jInstrButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jInstrButtonActionPerformed
        m_jInstruction.setEnabled(true);
      
    }//GEN-LAST:event_jInstrButtonActionPerformed

    private void m_jInstructionButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jInstructionButtonMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jInstructionButtonMousePressed

    private void m_jInstructionButtonInstructionChanged(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jInstructionButtonInstructionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jInstructionButtonInstructionChanged

    private void m_jInstructionButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jInstructionButtonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jInstructionButtonKeyPressed

    private void m_jInstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInstructionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jInstructionActionPerformed
    

        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCoke;
    private javax.swing.JButton jButtonHot;
    private javax.swing.JButton jButtonIce;
    private javax.swing.JButton jButtonOil;
    private javax.swing.JButton jButtonSS;
    private javax.swing.JButton jButtonSalt;
    private javax.swing.JButton jButtonSoda;
    private javax.swing.JButton jButtonSpicy;
    private javax.swing.JButton jButtonSprite;
    private javax.swing.JButton jButtonSweet;
    private javax.swing.JButton jButtonTAkeaway;
    private javax.swing.JButton jButtonUrgent;
    private javax.swing.JButton jButtonVHot;
    private javax.swing.JButton jInsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JButton m_jBtnCancel;
    private javax.swing.JButton m_jButtonOK;
    private javax.swing.JTextField m_jInstruction;
    private javax.swing.JTextField m_jInstructionButton;
    private com.openbravo.editor.JEditorNumberKeys m_jKeys;
    private com.openbravo.editor.JEditorString m_jName;
    private javax.swing.JLabel m_jPrice;
    private javax.swing.JLabel m_jPriceTax;
    private javax.swing.JLabel m_jSubtotal;
    private javax.swing.JLabel m_jTaxrate;
    private javax.swing.JLabel m_jTotal;
    private com.openbravo.editor.JEditorCurrencyPositive m_jUnits;
    // End of variables declaration//GEN-END:variables
    
}
