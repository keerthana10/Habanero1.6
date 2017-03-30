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
public class JSectionMapping extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<SectionMappingInfo> list = null;
    public boolean updateMode = false;
    private ComboBoxValModel pareamodel; 
    private ComboBoxValModel pareatypemodel;  
    private ComboBoxValModel sectionmodel;  
    private SentenceList printsent;
    private SentenceList areatypesent;
    private SentenceList sectionsent;
    private static volatile SecureRandom numberGenerator = null;
   private static final long MSB = 0x8000000000000000L;
   private static final JSectionMapping RandomUtil = null;
    
    
    public void init(AppView app) throws BeanFactoryException{
        try {
            m_App = app;
            
         
            m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
            dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
         //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
         
       
             initComponents();
             printsent=dlCustomers.getProductionAreaList();
             areatypesent = dlCustomers.getProductionAreaTypeList();
             sectionsent= dlCustomers.getSectionList();
             pareamodel=new ComboBoxValModel(printsent.list());
            m_jPArea.setModel(pareamodel);
            pareatypemodel = new ComboBoxValModel(areatypesent.list());
            m_jpatype.setModel(pareatypemodel);
            sectionmodel= new ComboBoxValModel(sectionsent.list());
            m_jSection.setModel(sectionmodel);
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JSectionMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jKitchenDisplay.setText("");
            m_jSection.setSelectedIndex(-1);
            m_jPArea.setSelectedIndex(-1);
            m_jpatype.setSelectedIndex(-1);
            setVisible(true);
            //add(m_jDiscountList);
            File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
            AppConfig ap = new AppConfig(file);
            ap.load();
        } catch (BasicException ex) {
            Logger.getLogger(JSectionMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return "Section Mapping ";
    }

    public void activate() throws BasicException {
//        pareamodel=new ComboBoxValModel(printsent.list());
//        m_jPArea.setModel(pareamodel);
//        pareatypemodel = new ComboBoxValModel(areatypesent.list());
//        m_jpatype.setModel(pareatypemodel);
//        sectionmodel= new ComboBoxValModel(sectionsent.list());
//        m_jSection.setModel(sectionmodel);
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
        m_jKitchenDisplay = new javax.swing.JTextField();
        jSaver = new javax.swing.JPanel();
        jbtnNew = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jbtnSave = new javax.swing.JButton();
        m_jlblName = new javax.swing.JLabel();
        m_jpatype = new javax.swing.JComboBox();
        m_jPArea = new javax.swing.JComboBox();
        m_jlblPArea = new javax.swing.JLabel();
        m_jlblPAreatype = new javax.swing.JLabel();
        m_jSection = new javax.swing.JComboBox();

        m_jPAreaList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jPAreaListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jPAreaList);

        m_jlblBusinessType.setText("Kitchen Display");

        m_jKitchenDisplay.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));

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

        m_jlblName.setText("*Section");

        m_jpatype.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        m_jpatype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jpatypeActionPerformed(evt);
            }
        });

        m_jPArea.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        m_jlblPArea.setText("*Productionarea");

        m_jlblPAreatype.setText("*Productionarea Type");

        m_jSection.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        m_jSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(m_jlblBusinessType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jlblPArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jlblPAreatype, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jlblName, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(m_jPArea, 0, 168, Short.MAX_VALUE)
                    .addComponent(m_jpatype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jSection, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jKitchenDisplay))
                .addGap(0, 76, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSaver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jlblName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jpatype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblPAreatype, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jPArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblPArea, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_jKitchenDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jlblBusinessType, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(209, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jPAreaListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jPAreaListValueChanged
        // TODO add your handling code here:
       
        if (evt.getValueIsAdjusting()) {
            System.out.println("inside value changed -- list selection event fired");
            int index=m_jPAreaList.getSelectedIndex();
            updateMode = true;
//            String PAreaName = null;
//            try {
//                PAreaName = m_jPAreaList.getSelectedValue().toString();
//            } catch (Exception ex) {
//                System.out.println("unknown exception");
//            }
            m_jKitchenDisplay.setText(list.get(index).getKitchendisplay());
            sectionmodel.setSelectedItem(list.get(index).getFloor());
            pareamodel.setSelectedKey(list.get(index).getParea());
            pareatypemodel.setSelectedKey(list.get(index).getPareatype());
        }
    }//GEN-LAST:event_m_jPAreaListValueChanged

    private void jbtnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNewActionPerformed

        m_jKitchenDisplay.setText("");
        m_jSection.setSelectedIndex(-1);
        m_jPArea.setSelectedIndex(-1);
        m_jpatype.setSelectedIndex(-1);
        m_jPAreaList.clearSelection();
        updateMode = false;
}//GEN-LAST:event_jbtnNewActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed

        int index = m_jPAreaList.getSelectedIndex();
        

        if (index == -1) {
            JOptionPane.showMessageDialog(JSectionMapping.this, "Please select the section");
        } else {
            try {
                String val = list.get(index).getId();
                dlCustomers.deleteSectionMapping(val);
                m_jKitchenDisplay.setText("");
                m_jSection.setSelectedIndex(-1);
                m_jPArea.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
           
            } catch (BasicException ex) {
                Logger.getLogger(JSectionMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                populateList();
            } catch (BasicException ex) {
                Logger.getLogger(JSectionMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateMode = false;
        }
}//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

        if (m_jSection.getSelectedIndex()==-1|| m_jPArea.getSelectedIndex()==-1||m_jpatype.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(JSectionMapping.this, "Please enter the Mandatory Fields");
        } else {
            try {
                saveButtonAction();
            } catch (BasicException ex) {
                Logger.getLogger(JSectionMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}//GEN-LAST:event_jbtnSaveActionPerformed

    private void m_jpatypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jpatypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jpatypeActionPerformed

    private void m_jSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jSectionActionPerformed
 public void populateList() throws BasicException {

        model = new DefaultListModel();
        m_jPAreaList.setModel(model);
        list = dlCustomers.getSectionMappingList();
        String[] dListId = null;
    
        for (int i = 0; i < list.size(); i++) {
             String listid = list.get(i).getFloor();
            model.add(i, listid);
        }
    }

private void saveButtonAction() throws BasicException {

       
        String section = sectionmodel.getSelectedKey().toString();
        String areatype=pareatypemodel.getSelectedKey().toString();
        String area=pareamodel.getSelectedKey().toString();
        String kdisplay = m_jKitchenDisplay.getText();
      
     
        if (section != null) {

            boolean avl = checkPAreaAvl(section,areatype,area);
            try {
                list = dlCustomers.getSectionMappingList();
           System.out.println("updatemode"+updateMode);
                if (updateMode == false) {
                    if (avl == false) {
                        dlCustomers.insertSectionMapping(RandomUtil.genRandom32Hex(),section,areatype,area,kdisplay);
                    } else {
                        JOptionPane.showMessageDialog(this, "Entered Combination already exists");
                    }
                } else {
                    String id = list.get(m_jPAreaList.getSelectedIndex()).getId();
                    dlCustomers.updateSectionMapping(id,section,areatype,area,kdisplay);
                    updateMode = false;
                }
                populateList();
                m_jKitchenDisplay.setText("");
                m_jSection.setSelectedIndex(-1);
                m_jPArea.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
                
             } catch (Exception npe) {
                npe.printStackTrace();
                JOptionPane.showMessageDialog(this, "Enter valid Section");
                m_jKitchenDisplay.setText("");
                m_jSection.setSelectedIndex(-1);
                m_jPArea.setSelectedIndex(-1);
                m_jpatype.setSelectedIndex(-1);
             }
        } else {
            JOptionPane.showMessageDialog(this, "Your msg.");
        }
    }


    private boolean checkPAreaAvl(String section,String pareatype,String parea) throws BasicException {
        String name = dlCustomers.getSectionName(section,pareatype,parea);
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnNew;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JTextField m_jKitchenDisplay;
    private javax.swing.JComboBox m_jPArea;
    private javax.swing.JList m_jPAreaList;
    private javax.swing.JComboBox m_jSection;
    private javax.swing.JLabel m_jlblBusinessType;
    private javax.swing.JLabel m_jlblName;
    private javax.swing.JLabel m_jlblPArea;
    private javax.swing.JLabel m_jlblPAreatype;
    private javax.swing.JComboBox m_jpatype;
    // End of variables declaration//GEN-END:variables
    
}
