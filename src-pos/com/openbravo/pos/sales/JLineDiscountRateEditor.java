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
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.inventory.BeanInfo;
import com.openbravo.pos.ticket.RetailTicketInfo;
import com.openbravo.pos.sales.DiscountReasonInfo;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class JLineDiscountRateEditor extends JDialog {

    public javax.swing.JDialog dEdior = null;
    private Properties dbp = new Properties();
    private DataLogicReceipts dlReceipts = null;
    private DataLogicCustomers dlCustomers = null;
    private AppView m_app;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public boolean updateMode = false;
    static Component parentLocal = null;
    static RetailTicketInfo tinfoLocal = null;
    public static String userRole = null;
    private static DataLogicReceipts localDlReceipts = null;
    public JRetailPanelTicket JRetailPanelTicket;
    private boolean enablity;
    int x = 500;
    int y = 300;
    int width = 350;
    int height = 280;
    public static String tinfotype;
    private java.util.List<DiscountReasonInfo> drList = null;
    private java.util.List<BeanInfo> catList;
    JCheckBox[] checkBoxList=null;
    private javax.swing.JComboBox m_jDiscountPercentage;
    private javax.swing.JTextField m_jDiscountValue;
    DefaultTableModel tbModel =null;
    Map<String,  DiscountInfo> discountMap=null;
    public static Map<String ,DiscountInfo> dRateMap =null;
    public static java.util.List<DiscountRateinfo> list =null;

    public static Map<String,  DiscountInfo> showMessage(Component parent, DataLogicReceipts dlReceipts, RetailTicketInfo tinfo, String role, java.util.List<DiscountRateinfo> dlist,Map<String ,DiscountInfo> rateMap) {
        localDlReceipts = dlReceipts;
        parentLocal = parent;
        tinfoLocal = tinfo;
        userRole = role;
        list = dlist;
        dRateMap=rateMap;
        return showMessage(parent, dlReceipts, 1);
    }

    private static Map<String,  DiscountInfo> showMessage(Component parent, DataLogicReceipts dlReceipts, int x) {

        Window window = getWindow(parent);
        JLineDiscountRateEditor myMsg;
        if (window instanceof Frame) {
            myMsg = new JLineDiscountRateEditor((Frame) window, true);
        } else {
            myMsg = new JLineDiscountRateEditor((Dialog) window, true);
        }
        return myMsg.init(dlReceipts);
    }

    private JLineDiscountRateEditor(Frame frame, boolean b) {
        super(frame, true);
        setBounds(x, y, width, height);

    }

    private JLineDiscountRateEditor(Dialog dialog, boolean b) {
        super(dialog, true);
        setBounds(x, y, width, height);

    }

  public Map<String,  DiscountInfo> init(DataLogicReceipts dlReceipts) {
       initComponents();
       this.setResizable(false);
       this.setSize(400, 300);
       jCategoryTable.setRowHeight(30);
       jCategoryTable.getTableHeader().setBackground(Color.ORANGE);
       jCategoryTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
       tbModel = (DefaultTableModel) jCategoryTable.getModel();
        m_jDiscountPercentage = new javax.swing.JComboBox();
        m_jDiscountValue = new javax.swing.JTextField();
        //Column for discount rate having combo box
        TableColumn discountComboColumn = jCategoryTable.getColumnModel().getColumn(1);
        //column for manually adding discount rate field
        TableColumn discountTextColumn = jCategoryTable.getColumnModel().getColumn(2);
        //Saving the product category id
        TableColumn hiddenColumn = jCategoryTable.getColumnModel().getColumn(3);
        //Column for product category having combo box
        TableColumn catColumn = jCategoryTable.getColumnModel().getColumn(0);
       catColumn.setCellRenderer(new ColorColumnRenderer(Color.decode("#869702"), Color.white));
        m_jDiscountPercentage.addItem("");
        //Putting all the discount
         for (DiscountRateinfo dis : list) {
                m_jDiscountPercentage.addItem(dis.getName());
            }
          
         discountComboColumn.setCellEditor(new DefaultCellEditor(m_jDiscountPercentage));
         discountTextColumn.setCellEditor(new DefaultCellEditor(m_jDiscountValue));
        try {
            catList = dlReceipts.getCategoriesList();
        } catch (BasicException ex) {
            Logger.getLogger(JLineDiscountRateEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numberCheckBox = catList.size();
        int dimension=40;
        Map<String,  DiscountInfo> oldDiscountMap=tinfoLocal.getDiscountMap();
        if(oldDiscountMap !=null && !oldDiscountMap.isEmpty()){
         if(oldDiscountMap.size()>0){
             for (int i = 0; i < numberCheckBox; i++) {
                  if(oldDiscountMap.containsKey(catList.get(i).getId())){
                     String catId= catList.get(i).getId();
                     String discountId=oldDiscountMap.get(catId).getDiscountId();
                     String discountName=(String) getKeyFromValue(dRateMap,discountId);
                     String discountValue=oldDiscountMap.get(catId).getDiscountValue();
                     if(!discountValue.equals("")){
                     Double discount=Double.parseDouble(discountValue)*100;
                     discountValue=discount.toString();
                     }
                     tbModel.addRow(new Object[]{catList.get(i).getName(), discountName,discountValue,catList.get(i).getId()});
                  }else{
                     tbModel.addRow(new Object[]{catList.get(i).getName(), "", null,catList.get(i).getId()});  
                  }
                  dimension=dimension+40; 
            }
        }
     }else{
            for (int i = 0; i < numberCheckBox; i++) {
              tbModel.addRow(new Object[]{catList.get(i).getName(), "", null,catList.get(i).getId()});
              dimension=dimension+40; 
              }
        }
        //calls only for m_jDiscountPercentage combobox action
        m_jDiscountPercentage.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                  System.out.println("addItemListener");
                if (e.getStateChange() == ItemEvent.SELECTED) {
                  int row = jCategoryTable.getSelectedRow();
                    int column = jCategoryTable.getSelectedColumn();
                    if(column==1){
                      jCategoryTable.setValueAt("", row, 2);
                    }
                }
            }
         });
        
        //It is also called for other column actions
        m_jDiscountValue.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
         System.out.println("focuslistener");
        int row = jCategoryTable.getSelectedRow();
             int column = jCategoryTable.getSelectedColumn();
              if(column==2){
              jCategoryTable.setValueAt("", row, 1);    
              }
           }
         });
          try {
            java.util.List<DiscountReasonInfo> list = dlReceipts.getDiscountReason();
            drList = list;
            for (DiscountReasonInfo dis : list) {
                m_jReason.addItem(dis.getReason());
            }
            m_jReason.setSelectedIndex(-1);
        } catch (BasicException ex) {
            Logger.getLogger(JRateEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTitle("Discounts Editor");
        setVisible(true);
        return discountMap;
  }  
//    public void init(DataLogicReceipts dlReceipts) {
//
//        initComponents();
//
//
//        try {
//            java.util.List<DiscountRateinfo> list = dlReceipts.getDiscountList();
//            for (DiscountRateinfo dis : list) {
//                m_jDiscountPercentage.addItem(dis.getName());
//            }
//            m_jDiscountPercentage.setSelectedIndex(-1);
//        } catch (BasicException ex) {
//            Logger.getLogger(JLineDiscountRateEditor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//  try {
//            java.util.List<DiscountReasonInfo> list = dlReceipts.getDiscountReason();
//            drList = list;
//            for (DiscountReasonInfo dis : list) {
//                m_jReason.addItem(dis.getReason());
//            }
//            m_jReason.setSelectedIndex(-1);
//        } catch (BasicException ex) {
//            Logger.getLogger(JLineDiscountRateEditor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//  
//        setTitle("Discounts Editor");
//        setVisible(true);
//        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
//        AppConfig ap = new AppConfig(file);
//        ap.load();
//
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jCategoryTable = new javax.swing.JTable();
        jPanelButtons = new javax.swing.JPanel();
        okay = new javax.swing.JButton();
        jButtonCancel1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        m_jReason = new javax.swing.JComboBox();
        m_jReasonText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosingEvent(evt);
            }
        });

        jCategoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Discount Rate", "Manual Discount Rate", "Id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jCategoryTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane.setViewportView(jCategoryTable);
        jCategoryTable.getColumnModel().getColumn(0).setResizable(false);
        jCategoryTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        jCategoryTable.getColumnModel().getColumn(1).setResizable(false);
        jCategoryTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        jCategoryTable.getColumnModel().getColumn(2).setMinWidth(0);
        jCategoryTable.getColumnModel().getColumn(2).setPreferredWidth(0);
        jCategoryTable.getColumnModel().getColumn(2).setMaxWidth(0);
        jCategoryTable.getColumnModel().getColumn(3).setMinWidth(0);
        jCategoryTable.getColumnModel().getColumn(3).setPreferredWidth(0);
        jCategoryTable.getColumnModel().getColumn(3).setMaxWidth(0);

        getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);

        jPanelButtons.setPreferredSize(new java.awt.Dimension(400, 130));

        okay.setText("Ok");
        okay.setMaximumSize(new java.awt.Dimension(60, 30));
        okay.setMinimumSize(new java.awt.Dimension(60, 30));
        okay.setPreferredSize(new java.awt.Dimension(60, 30));
        okay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayActionPerformed(evt);
            }
        });

        jButtonCancel1.setText("Cancel");
        jButtonCancel1.setAlignmentX(2.0F);
        jButtonCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancel1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Reason*");

        m_jReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        m_jReason.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), null), null));
        m_jReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReasonActionPerformed(evt);
            }
        });

        m_jReasonText.setPreferredSize(new java.awt.Dimension(10, 30));
        m_jReasonText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReasonTextActionPerformed(evt);
            }
        });
        m_jReasonText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CommentsKeyPressed(evt);
            }
        });

        jLabel2.setText("Comments");

        org.jdesktop.layout.GroupLayout jPanelButtonsLayout = new org.jdesktop.layout.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(188, Short.MAX_VALUE)
                .add(okay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel1)
                .add(163, 163, 163))
            .add(jPanelButtonsLayout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(18, 18, 18)
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(m_jReasonText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(m_jReason, 0, 210, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelButtonsLayout.setVerticalGroup(
            jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(m_jReason, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(m_jReasonText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(jPanelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonCancel1)
                    .add(okay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanelButtons, java.awt.BorderLayout.PAGE_END);

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void okayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okayActionPerformed
      int dReasonIndex = m_jReason.getSelectedIndex();
      if (dReasonIndex < 0) {
       JOptionPane.showMessageDialog(null, "Please select the discount reason");
      }else{
      tinfoLocal.setDiscountReasonId(drList.get(dReasonIndex).getId());    
      tinfoLocal.setDiscountReasontText(m_jReasonText.getText());
       discountMap=new HashMap();
       System.out.println(tbModel.getRowCount()+"getRowCount");
        for(int i=0;i<tbModel.getRowCount();i++){
       //Retriving Category id from table     
       String catId= (String) tbModel.getValueAt(i, 3);
       String discountName="";
       String discountVal = "";
       //System.out.println("tbModel.getValueAt(i, 1)"+tbModel.getValueAt(i, 1));
        if(tbModel.getValueAt(i, 1)!="" && tbModel.getValueAt(i, 1) !=null){
            discountName=tbModel.getValueAt(i, 1).toString() ;
        } else if(tbModel.getValueAt(i, 2)!="" && tbModel.getValueAt(i, 2) !=null){
            discountVal=tbModel.getValueAt(i, 2).toString() ;
        }
        
        if(!discountName.equals("") ){
          discountMap.put(catId, new DiscountInfo( dRateMap.get(discountName).getDiscountRate(), discountVal,dRateMap.get(discountName).getDiscountId()));
        }else if(!discountVal.equals("")){
            Double discount=(Double.parseDouble(discountVal))/100;
            discountVal=discount.toString();
            discountMap.put(catId, new DiscountInfo( "", discountVal,""));
        }
       }
//        Set<String> keys = discountMap.keySet();
//        for(String key: keys){
//            System.out.println("key"+key);
//             System.out.println("value "+discountMap.get(key).getDiscountId()+" "+discountMap.get(key).getDiscountValue());
//        }
         dispose();
       }
     
    }//GEN-LAST:event_okayActionPerformed

    private void jButtonCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancel1ActionPerformed
      discountMap=tinfoLocal.getDiscountMap();
        dispose();
    }//GEN-LAST:event_jButtonCancel1ActionPerformed

    private void m_jReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReasonActionPerformed
        jCategoryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }//GEN-LAST:event_m_jReasonActionPerformed

    private void CommentsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CommentsKeyPressed
       if( System.getProperty("os.name").equalsIgnoreCase("Linux"))
            return;
       else{
            try {
            Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
        } catch (IOException ex) {
            Logger.getLogger(JLineDiscountRateEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
    }//GEN-LAST:event_CommentsKeyPressed

    private void m_jReasonTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReasonTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jReasonTextActionPerformed

    private void WindowClosingEvent(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosingEvent
         discountMap=tinfoLocal.getDiscountMap();
        dispose();
    }//GEN-LAST:event_WindowClosingEvent

     
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel1;
    private javax.swing.JTable jCategoryTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JComboBox m_jReason;
    private javax.swing.JTextField m_jReasonText;
    private javax.swing.JButton okay;
    // End of variables declaration//GEN-END:variables



    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

  
    public Object getKeyFromValue(Map hm, Object value) {
    for (Object o : hm.keySet()) {
     if(hm.get(o) instanceof DiscountInfo){
        DiscountInfo dinfo=  (DiscountInfo) hm.get(o) ;
        if (dinfo.getDiscountId().equals(value)) {
        return o;
        }
      }
    }
    return null;
  }
}
    
