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

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DiscountRateinfo;

import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.io.File;
import java.security.SecureRandom;

/**
 *
 * @author adrianromero
 */
public class JProductionArea extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<ProductionAreaInfo> list = null;
    public boolean updateMode = false;
    private ComboBoxValModel printermodel; 
    private ComboBoxValModel pareatypemodel;  
    private SentenceList printsent;
    private SentenceList areatypesent;
   private static volatile SecureRandom numberGenerator = null;
   private static final long MSB = 0x8000000000000000L;
   private static final JProductionArea RandomUtil = null;
    
    
    public void init(AppView app) throws BeanFactoryException{
        try {
            m_App = app;
            
         
            m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
            dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
         //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
         
       
             initComponents();
             printsent=dlCustomers.getPrinterConfigList();
            areatypesent = dlCustomers.getProductionAreaTypeList();
            printermodel=new ComboBoxValModel(printsent.list());
            m_jPrinter.setModel(printermodel);
            pareatypemodel = new ComboBoxValModel(areatypesent.list());
            m_jpatype.setModel(pareatypemodel);
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JProductionArea.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jSearchKey.setText("");
            m_jName.setText("");
            m_jPrinter.setSelectedIndex(-1);
            m_jpatype.setSelectedIndex(-1);
            setVisible(true);
            //add(m_jDiscountList);
            File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
            AppConfig ap = new AppConfig(file);
            ap.load();
        } catch (BasicException ex) {
            Logger.getLogger(JProductionArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return "Productionarea ";
    }

    public void activate() throws BasicException {
//        printermodel=new ComboBoxValModel(printsent.list());
//        m_jPrinter.setModel(printermodel);
//        pareatypemodel = new ComboBoxValModel(areatypesent.list());
//        m_jpatype.setModel(pareatypemodel);
    }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


   

      
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jPAreaList = new javax.swing.JList();
        m_jlblBusinessType = new javax.swing.JLabel();
        m_jSearchKey = new javax.swing.JTextField();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jlblBusinessType1 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        m_jlblBusinessType2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jDesc = new javax.swing.JTextArea();
        m_jpatype = new javax.swing.JComboBox();
        m_jPrinter = new javax.swing.JComboBox();
        m_jlblBusinessType3 = new javax.swing.JLabel();
        m_jlblBusinessType4 = new javax.swing.JLabel();

        m_jPAreaList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jPAreaListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jPAreaList);

        m_jlblBusinessType.setText("*Search Key");

        m_jSearchKey.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

        jbtnNew.setBackground(new java.awt.Color(255, 255, 255));
        jbtnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        jbtnNew.setFocusPainted(false);
        jbtnNew.setFocusable(false);
        jbtnNew.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnNew.setRequestFocusEnabled(false);
        jbtnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNewActionPerformed(evt);
            }
        });

        jbtnDelete.setBackground(new java.awt.Color(255, 255, 255));
        jbtnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editdelete.png"))); // NOI18N
        jbtnDelete.setFocusPainted(false);
        jbtnDelete.setFocusable(false);
        jbtnDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });

        jbtnSave.setBackground(new java.awt.Color(255, 255, 255));
        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png"))); // NOI18N
        jbtnSave.setFocusPainted(false);
        jbtnSave.setFocusable(false);
        jbtnSave.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnSave.setRequestFocusEnabled(false);
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSaverLayout = new javax.swing.GroupLayout(jSaver);
        jSaver.setLayout(jSaverLayout);
        jSaverLayout.setHorizontalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(330, 330, 330)
                .addComponent(jbtnNew)
                .addGap(4, 4, 4)
                .addComponent(jbtnDelete)
                .addGap(4, 4, 4)
                .addComponent(jbtnSave))
        );
        jSaverLayout.setVerticalGroup(
            jSaverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSaverLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jbtnNew)
            .addComponent(jbtnDelete)
            .addComponent(jbtnSave)
        );

        m_jlblBusinessType1.setText("*Name");

        m_jName.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jNameActionPerformed(evt);
            }
        });

        m_jlblBusinessType2.setText("Description");

        m_jDesc.setColumns(20);
        m_jDesc.setRows(5);
        m_jDesc.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jScrollPane2.setViewportView(m_jDesc);

        m_jpatype.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        m_jpatype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jpatypeActionPerformed(evt);
            }
        });

        m_jPrinter.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        m_jlblBusinessType3.setText("*Restaurant");

        m_jlblBusinessType4.setText("*Productionarea Type");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(m_jlblBusinessType1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblBusinessType2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(m_jlblBusinessType3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(m_jlblBusinessType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                            .addComponent(m_jlblBusinessType4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(m_jSearchKey, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m_jName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jpatype, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jSearchKey, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblBusinessType, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(m_jlblBusinessType1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblBusinessType3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jpatype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblBusinessType4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(m_jlblBusinessType2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(209, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jPAreaListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jPAreaListValueChanged
        // TODO add your handling code here:
        if (evt.getValueIsAdjusting()) {
            System.out.println("inside value changed -- list selection event fired");
            int index=m_jPAreaList.getSelectedIndex();
            updateMode = true;
            String PAreaName = null;
            try {
                PAreaName = m_jPAreaList.getSelectedValue().toString();
            } catch (Exception ex) {
                System.out.println("unknown exception");
            }
            m_jSearchKey.setText(list.get(index).getSearchkey());
            m_jName.setText(PAreaName);
            m_jDesc.setText(list.get(index).getDescription());
            printermodel.setSelectedKey(list.get(index).getRestaurent());
            pareatypemodel.setSelectedKey(list.get(index).getPareatype());
        }
    }//GEN-LAST:event_m_jPAreaListValueChanged

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed

        m_jSearchKey.setText("");
        m_jName.setText("");
        m_jPrinter.setSelectedIndex(-1);
        m_jpatype.setSelectedIndex(-1);
        m_jPAreaList.clearSelection();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jPAreaList.getSelectedIndex();
        System.out.println("index is " + index);

        if (index == -1) {
            JOptionPane.showMessageDialog(JProductionArea.this, "Please select the productionarea");
        } else {
            try {
                String val = m_jPAreaList.getSelectedValue().toString();
                dlCustomers.deleteProductionArea(val);
                m_jSearchKey.setText("");
                m_jName.setText("");
                m_jDesc.setText("");
                m_jPrinter.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
           
            } catch (BasicException ex) {
                Logger.getLogger(JProductionArea.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JProductionArea.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

        if (m_jSearchKey.getText().equals("") || m_jName.getText().equals("")|| m_jPrinter.getSelectedIndex()==-1||m_jpatype.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(JProductionArea.this, "Please enter the Mandatory Fields");
        } else {
            try {
                saveButtonAction();
            } catch (BasicException ex) {
                Logger.getLogger(JProductionArea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed

    private void m_jNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jNameActionPerformed

    private void m_jpatypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jpatypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jpatypeActionPerformed
 public void populateList() throws BasicException {

        model = new DefaultListModel();
        m_jPAreaList.setModel(model);
        list = dlCustomers.getPAreaList();
        String[] dListId = null;
    
        for (int i = 0; i < list.size(); i++) {
             String listid = list.get(i).getName();
            model.add(i, listid);
        }
    }

private void saveButtonAction() throws BasicException {

        String skey = m_jSearchKey.getText();
        String name = m_jName.getText();
        String printer=printermodel.getSelectedKey().toString();
        String area=pareatypemodel.getSelectedKey().toString();
         String desc = m_jDesc.getText();
     
        if (name != null) {

            boolean avl = checkPAreaAvl(name);
            try {
                list = dlCustomers.getPAreaList();
           System.out.println("updatemode"+updateMode);
                if (updateMode == false) {
                    if (avl == false) {
                        dlCustomers.insertProductionArea(RandomUtil.genRandom32Hex(),skey,name,printer,area,desc);
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered Productionarea already exists");
                    }
                } else {
                    String id = list.get(m_jPAreaList.getSelectedIndex()).getId();
                    dlCustomers.updateProductionArea(id,skey,name,printer,area,desc);
                    updateMode = false;
                }
                populateList();
                m_jSearchKey.setText("");
                m_jName.setText("");
                m_jDesc.setText("");
                m_jPrinter.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
                
             } catch (Exception npe) {
              //   npe.printStackTrace();
                JOptionPane.showMessageDialog(this, "Enter valid Productionarea");
                m_jSearchKey.setText("");
                m_jName.setText("");
                m_jDesc.setText("");
                 m_jPrinter.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
             }
        } else {
            JOptionPane.showMessageDialog(this, "Your msg.");
        }
    }


    private boolean checkPAreaAvl(String pname) throws BasicException {
        String name = dlCustomers.getProductionAreaName(pname);
        if ("NONAME".equalsIgnoreCase(name)) {
            return false;
        } else {
            return true;
        }
    } 
    
   public static String genRandom32Hex() {
    SecureRandom ng = numberGenerator;
    if (ng == null) {
      numberGenerator = ng = new SecureRandom();
    }

    return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
  }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jSaver;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JTextArea m_jDesc;
    private javax.swing.JTextField m_jName;
    private javax.swing.JList m_jPAreaList;
    private javax.swing.JComboBox m_jPrinter;
    private javax.swing.JTextField m_jSearchKey;
    private javax.swing.JLabel m_jlblBusinessType;
    private javax.swing.JLabel m_jlblBusinessType1;
    private javax.swing.JLabel m_jlblBusinessType2;
    private javax.swing.JLabel m_jlblBusinessType3;
    private javax.swing.JLabel m_jlblBusinessType4;
    private javax.swing.JComboBox m_jpatype;
    // End of variables declaration//GEN-END:variables
    
}
