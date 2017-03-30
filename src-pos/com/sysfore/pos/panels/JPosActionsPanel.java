//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://wm_jCboRolesww.openbravo.com/product/pos
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

package com.sysfore.pos.panels;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.pos.admin.DataLogicAdmin;
import com.openbravo.pos.admin.RoleInfo;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.PrAreaMapInfo;
import com.openbravo.pos.inventory.ProductionAreaTypeInfo;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DiscountRateinfo;
import com.openbravo.pos.sales.shared.JTicketsBagShared;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 *
 * @author adrianromero
 */
public class JPosActionsPanel extends JPanel implements JPanelView,BeanFactoryApp{
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
     private DataLogicSales m_dlSales;
    String[] input;
   ArrayList<String> chkClassName = new ArrayList<String>();
    ArrayList<String> classValue = new ArrayList<String>();

    private TicketParser m_TTP;

     static PurchaseOrderReceipts PurchaseOrder = null;
    protected DataLogicCustomers dlCustomers;
    public javax.swing.JDialog dEdior = null;
    public DataLogicCustomers dlCustomers2 = null;
      public DataLogicAdmin dlAdmin = null;
    public String[] strings = {""};
    public DefaultListModel model = null;
    public java.util.List<DiscountRateinfo> list = null;
    public boolean updateMode = false;
    java.util.List<RoleInfo> rolesList;
    java.util.List<PosActionsInfo> posActions;
   java.util.List<ProductionAreaTypeInfo> totalProductionAreaTypeList;
   JCheckBox[] checkBoxList=null;
   private java.util.List<String> selectedList;
   private java.util.List<String> prAreaIdlist = null;
  private java.util.List<PrAreaMapInfo> prAreaMapList=null;;
  String roleId;

   // static int x = 400;
  //  static int y = 200;
    /** Creates new form JPanelCloseMoney */
    public JPosActionsPanel() {
        
        initComponents();
        
     
    }
    
    public void init(AppView app) throws BeanFactoryException{
        
        m_App = app;        
        
         PurchaseOrder = (PurchaseOrderReceipts) m_App.getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        dlAdmin = (DataLogicAdmin) m_App.getBean("com.openbravo.pos.admin.DataLogicAdmin");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomers");
     //   m_dlPurchase = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
   // JOptionPane.showMessageDialog(this, "The user has to close all pending bills before doing the close shift", AppLocal.getIntString("message.header"), JOptionPane.INFORMATION_MESSAGE);
         initComponents();
       
        //m_jDiscountPercentage.setText(dlCustomers.getMaxPercentage() + "%");
      
        setVisible(true);
        //add(m_jDiscountList);
        File file = new File(System.getProperty("user.home") + "/openbravopos.properties");
        AppConfig ap = new AppConfig(file);
        ap.load();
    }
    
    

    public Object getBean() {
         return this;
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.PosActions");
    }

    public void activate() throws BasicException {
        populateRoles();
        setCheckBoxValues();
        m_jCboRoles.setSelectedIndex(-1);
        System.out.println("roleID"+roleId);
        setPrArea();
        }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }


   
public void writeXml(){
         try {
        //    String[] input = {"John Doe,123-456-7890", "Bob Smith,123-555-1212"};
          //  String[] line = new String[2];
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();
            Element root = doc.createElement("permission");
            doc.appendChild(root);
          //  Element memberList = doc.createElement("members");
          //  root.appendChild(memberList);
            for (int i = 0; i < chkClassName.size(); i++) {
               // line = input[i].split(",");
                Element member = doc.createElement("class");
                root.appendChild(member);
                Attr attr = doc.createAttribute("name");
		attr.setValue(chkClassName.get(i).toString());
                 Attr attr1 = doc.createAttribute("value");
		attr1.setValue(classValue.get(i).toString());
		member.setAttributeNode(attr);
                member.setAttributeNode(attr1);
               
            }
            TransformerFactory tFact = TransformerFactory.newInstance();
            Transformer trans = tFact.newTransformer();

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
             System.out.println("writer.toString()--"+writer.toString());
            byte[] xmlData = null;
            try {
                xmlData = (byte[]) Formats.BYTEA.parseValue(writer.toString());
            } catch (BasicException ex) {
                Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
             System.out.println("xml   "+xmlData.toString());
             
        //     m_dlSystem.updateRolesPermission(rolesList.get(m_jCboRoles.getSelectedIndex()).getID(), xmlData);
    //      dlCustomers.updateRoles(rolesList.get(m_jCboRoles.getSelectedIndex()).getID(), xmlData);

        Object[] values = new Object[]{roleId,xmlData };
             System.out.println("xml data--"+xmlData);
        Datas[] datas = new Datas[]{Datas.STRING, Datas.BYTES};
            try {
                new PreparedSentence(m_App.getSession(), "UPDATE ROLES SET PERMISSIONS=? WHERE ID = ?  ", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);
            } catch (BasicException ex) {
                Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }


        } catch (TransformerException ex) {
            System.out.println("Error outputting document");
        } catch (ParserConfigurationException ex) {
            System.out.println("Error building document");
        }
        
}

   public void readXml(){
System.out.println("test readxml");
        String permissions = m_dlSystem.findRolePermissions(rolesList.get(m_jCboRoles.getSelectedIndex()).getID());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document document = null;
        if(permissions!=""){
        try {
            document = builder.parse(new InputSource(new StringReader(permissions)));
        } catch (SAXException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        classValue.clear();
        Element rootElement = document.getDocumentElement();
        NodeList nodeList = document.getElementsByTagName("class");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.hasAttributes()) {
                Attr attr = (Attr) node.getAttributes().getNamedItem("value");
                if (attr != null) {
                    String attribute= attr.getValue();
                    classValue.add(attribute);
                    //System.out.println("attribute: " + attribute);
                }
            }
        }
        }

}

    private void setPrArea() {
        try {
             jPanelPArea.setLayout(new GridLayout(20, 0, 0, 0));
             jPanelPArea.removeAll();
             //Adding logic to fetch production areas based on roles
             totalProductionAreaTypeList=m_dlSales.getProductionAreaTypeList();
             //Get Production Area role access details
             prAreaMapList=m_dlSales.getProductionAreaRoadMapList(roleId);
             //Add all the present Production Area role access ids tp arraylist
             if(prAreaMapList!=null){
             prAreaIdlist=new ArrayList();
             for(PrAreaMapInfo pr:prAreaMapList){
                 System.out.println("test 1");
                prAreaIdlist.add(pr.getPrArea());
              }
             }
             int prAreaCheckBox = totalProductionAreaTypeList.size();
            checkBoxList = new JCheckBox[prAreaCheckBox];
           // int dimension=40;
             //Dynamically adding checkboxes based on number of production Areas (By Shilpa)
           for (int i = 0; i < prAreaCheckBox; i++) {
               javax.swing.JCheckBox  m_jChkPrName= new javax.swing.JCheckBox();    
            m_jChkPrName.setLabel(totalProductionAreaTypeList.get(i).getName());
            //assigning each checkbox a unique Id(Role Id) 
            m_jChkPrName.setActionCommand(totalProductionAreaTypeList.get(i).getId());
            jPanelPArea.add(m_jChkPrName);//, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, dimension, -1, -1));
           // dimension=dimension+40;  
            if(prAreaIdlist!=null){
               if(prAreaIdlist.contains(totalProductionAreaTypeList.get(i).getId())){
                m_jChkPrName.setSelected(true);
                }
              }
           }
        } catch (BasicException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private void SavePrArea() {
    selectedList=new ArrayList();
     for(Component component : jPanelPArea.getComponents()) {
         if(component instanceof JCheckBox){
               if(((JCheckBox)component).isSelected()){
                   javax.swing.JCheckBox  m_jChkPrName= (JCheckBox)component;
                    selectedList.add(m_jChkPrName.getActionCommand());
                }
            }
        }
        try {
            m_dlSales.updatePrAreaRoadMap(roleId,selectedList);
        } catch (BasicException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Component component : jPanelPArea.getComponents()) {
         if(component instanceof JCheckBox){
           javax.swing.JCheckBox  m_jChkPrName= (JCheckBox)component;
           m_jChkPrName.setSelected(false);
         }
        }
    }
    
    private class FormatsPayment extends Formats {
        protected String formatValueInt(Object value) {
            return AppLocal.getIntString("transpayment." + (String) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }         
    }    
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        m_jChkCustomerMenu = new javax.swing.JCheckBox();
        m_jChkReprint = new javax.swing.JCheckBox();
        m_jChkBilling = new javax.swing.JCheckBox();
        m_jChkItemMaster = new javax.swing.JCheckBox();
        m_jChkCustomers = new javax.swing.JCheckBox();
        m_jChkItemCategories = new javax.swing.JCheckBox();
        m_jChkItem = new javax.swing.JCheckBox();
        m_jChkTaxation = new javax.swing.JCheckBox();
        m_jChkTaxCategories = new javax.swing.JCheckBox();
        m_jChkCusTax = new javax.swing.JCheckBox();
        m_jChkTaxRate = new javax.swing.JCheckBox();
        m_jChkDiscountMenu = new javax.swing.JCheckBox();
        m_jChkAddDiscounts = new javax.swing.JCheckBox();
        m_jChkWarehouseMasters = new javax.swing.JCheckBox();
        m_jChkStockMain = new javax.swing.JCheckBox();
        m_jChkWarehouse1 = new javax.swing.JCheckBox();
        m_jChkPettyExpenses = new javax.swing.JCheckBox();
        m_jChkFloatCash = new javax.swing.JCheckBox();
        m_jChkPettyCash = new javax.swing.JCheckBox();
        m_jChkIUserManagement = new javax.swing.JCheckBox();
        m_jChkuser = new javax.swing.JCheckBox();
        m_jChkServiceCharge = new javax.swing.JCheckBox();
        m_jChkResources = new javax.swing.JCheckBox();
        m_jChkRoles = new javax.swing.JCheckBox();
        m_jChkFloors = new javax.swing.JCheckBox();
        m_jChkTables = new javax.swing.JCheckBox();
        m_jChkBusinessType = new javax.swing.JCheckBox();
        m_jChkPosActions = new javax.swing.JCheckBox();
        m_jChkPrinterConfig = new javax.swing.JCheckBox();
        m_jChkPurDiscountMaster = new javax.swing.JCheckBox();
        m_jChkChargesMaster = new javax.swing.JCheckBox();
        m_jChkTaxMapping = new javax.swing.JCheckBox();
        m_jChkCashManagement = new javax.swing.JCheckBox();
        m_jChkCloseDay = new javax.swing.JCheckBox();
        m_jChkSalesReports = new javax.swing.JCheckBox();
        m_jSalesItemWise = new javax.swing.JCheckBox();
        m_jChkDiscountRegister = new javax.swing.JCheckBox();
        m_jChkKotAnalysis = new javax.swing.JCheckBox();
        m_jChkKotCancel = new javax.swing.JCheckBox();
        m_jChkCancelledBills = new javax.swing.JCheckBox();
        m_jChkSalesBillWise = new javax.swing.JCheckBox();
        m_jChkSettlement = new javax.swing.JCheckBox();
        m_jChkSectionWise = new javax.swing.JCheckBox();
        m_jChkTaxSummary = new javax.swing.JCheckBox();
        m_jChkCashReports = new javax.swing.JCheckBox();
        m_jChkPettyExpenseReports = new javax.swing.JCheckBox();
        m_jChkDailyCollection = new javax.swing.JCheckBox();
        m_jChkConfiguration = new javax.swing.JCheckBox();
        m_jchkBillPreview = new javax.swing.JCheckBox();
        m_jChkNcReports = new javax.swing.JCheckBox();
        m_jChkUnlock = new javax.swing.JCheckBox();
        m_jChkKodStatus = new javax.swing.JCheckBox();
        m_jChkBillOnHold = new javax.swing.JCheckBox();
        m_jChkTakeAway = new javax.swing.JCheckBox();
        m_jChkMoveTableReport = new javax.swing.JCheckBox();
        m_jChkSalesSummary = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        m_jChkPrintBill = new javax.swing.JCheckBox();
        m_jChkSettleBill = new javax.swing.JCheckBox();
        m_jChkMoveTable = new javax.swing.JCheckBox();
        m_jChkCancel = new javax.swing.JCheckBox();
        m_jChkIBillDiscount = new javax.swing.JCheckBox();
        m_jChkSplitBill = new javax.swing.JCheckBox();
        m_jBtnBillOnHold = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanelPArea = new javax.swing.JPanel();
        jPanelPrArea = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jCboRoles = new javax.swing.JComboBox();
        jbtnSave = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1024, 768));

        jTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane.setAutoscrolls(true);

        jScrollPane1.setAutoscrolls(true);

        jPanel2.setAutoscrolls(true);
        jPanel2.setLayout(null);

        m_jChkCustomerMenu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkCustomerMenu.setText("Customers/Vendors Menu");
        m_jChkCustomerMenu.setToolTipText("u");
        jPanel2.add(m_jChkCustomerMenu);
        m_jChkCustomerMenu.setBounds(50, 220, 250, 24);

        m_jChkReprint.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkReprint.setText("Reprint Bill");
        jPanel2.add(m_jChkReprint);
        m_jChkReprint.setBounds(50, 40, 120, 24);

        m_jChkBilling.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkBilling.setText("Billing");
        jPanel2.add(m_jChkBilling);
        m_jChkBilling.setBounds(50, 10, 120, 24);

        m_jChkItemMaster.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkItemMaster.setText("Item Master");
        jPanel2.add(m_jChkItemMaster);
        m_jChkItemMaster.setBounds(50, 130, 140, 24);

        m_jChkCustomers.setText("Customers/Vendors");
        jPanel2.add(m_jChkCustomers);
        m_jChkCustomers.setBounds(70, 250, 160, 24);

        m_jChkItemCategories.setText("Item Categories");
        jPanel2.add(m_jChkItemCategories);
        m_jChkItemCategories.setBounds(70, 190, 130, 24);

        m_jChkItem.setText("Items");
        jPanel2.add(m_jChkItem);
        m_jChkItem.setBounds(70, 160, 80, 24);

        m_jChkTaxation.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkTaxation.setText("Taxation");
        jPanel2.add(m_jChkTaxation);
        m_jChkTaxation.setBounds(50, 280, 140, 24);

        m_jChkTaxCategories.setText("Tax Categories");
        jPanel2.add(m_jChkTaxCategories);
        m_jChkTaxCategories.setBounds(70, 310, 140, 24);

        m_jChkCusTax.setText("Customer Tax Categories");
        jPanel2.add(m_jChkCusTax);
        m_jChkCusTax.setBounds(70, 340, 200, 24);

        m_jChkTaxRate.setText("Tax Rates");
        jPanel2.add(m_jChkTaxRate);
        m_jChkTaxRate.setBounds(70, 370, 120, 24);

        m_jChkDiscountMenu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkDiscountMenu.setText("Discount Setup");
        m_jChkDiscountMenu.setToolTipText("");
        jPanel2.add(m_jChkDiscountMenu);
        m_jChkDiscountMenu.setBounds(50, 400, 210, 24);

        m_jChkAddDiscounts.setText("Add Discounts");
        jPanel2.add(m_jChkAddDiscounts);
        m_jChkAddDiscounts.setBounds(70, 430, 130, 24);

        m_jChkWarehouseMasters.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkWarehouseMasters.setText("Warehouse Masters");
        m_jChkWarehouseMasters.setToolTipText("");
        jPanel2.add(m_jChkWarehouseMasters);
        m_jChkWarehouseMasters.setBounds(50, 460, 210, 24);

        m_jChkStockMain.setText("Stock Maintainance");
        jPanel2.add(m_jChkStockMain);
        m_jChkStockMain.setBounds(70, 520, 170, 24);

        m_jChkWarehouse1.setText("Warehouses");
        jPanel2.add(m_jChkWarehouse1);
        m_jChkWarehouse1.setBounds(70, 490, 130, 24);

        m_jChkPettyExpenses.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkPettyExpenses.setText("Petty Cash Expenses");
        jPanel2.add(m_jChkPettyExpenses);
        m_jChkPettyExpenses.setBounds(340, 430, 200, 24);

        m_jChkFloatCash.setText("Float Cash Setup");
        jPanel2.add(m_jChkFloatCash);
        m_jChkFloatCash.setBounds(360, 40, 140, 24);

        m_jChkPettyCash.setText("Petty Cash setup");
        jPanel2.add(m_jChkPettyCash);
        m_jChkPettyCash.setBounds(360, 70, 170, 24);

        m_jChkIUserManagement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkIUserManagement.setText("Users & Roles");
        m_jChkIUserManagement.setToolTipText("");
        jPanel2.add(m_jChkIUserManagement);
        m_jChkIUserManagement.setBounds(340, 100, 140, 24);

        m_jChkuser.setText("Users");
        jPanel2.add(m_jChkuser);
        m_jChkuser.setBounds(360, 130, 140, 24);

        m_jChkServiceCharge.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkServiceCharge.setText("Service Charge");
        jPanel2.add(m_jChkServiceCharge);
        m_jChkServiceCharge.setBounds(340, 310, 210, 24);

        m_jChkResources.setText("Resources");
        jPanel2.add(m_jChkResources);
        m_jChkResources.setBounds(360, 190, 120, 24);

        m_jChkRoles.setText("Roles");
        jPanel2.add(m_jChkRoles);
        m_jChkRoles.setBounds(360, 160, 170, 24);

        m_jChkFloors.setText("Floors");
        jPanel2.add(m_jChkFloors);
        m_jChkFloors.setBounds(360, 220, 170, 24);

        m_jChkTables.setText("Tables");
        jPanel2.add(m_jChkTables);
        m_jChkTables.setBounds(360, 250, 170, 24);

        m_jChkBusinessType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkBusinessType.setText("Business Type");
        jPanel2.add(m_jChkBusinessType);
        m_jChkBusinessType.setBounds(340, 340, 210, 24);

        m_jChkPosActions.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkPosActions.setText("Pos Actions");
        jPanel2.add(m_jChkPosActions);
        m_jChkPosActions.setBounds(340, 280, 210, 24);

        m_jChkPrinterConfig.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkPrinterConfig.setText("Printer Config");
        m_jChkPrinterConfig.setToolTipText("");
        jPanel2.add(m_jChkPrinterConfig);
        m_jChkPrinterConfig.setBounds(340, 400, 210, 24);

        m_jChkPurDiscountMaster.setText("Purchase Charges & Discount Masters");
        jPanel2.add(m_jChkPurDiscountMaster);
        m_jChkPurDiscountMaster.setBounds(360, 490, 250, 24);

        m_jChkChargesMaster.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkChargesMaster.setText("Charges Master");
        m_jChkChargesMaster.setToolTipText("");
        jPanel2.add(m_jChkChargesMaster);
        m_jChkChargesMaster.setBounds(340, 460, 210, 24);

        m_jChkTaxMapping.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkTaxMapping.setText("Tax Mapping");
        m_jChkTaxMapping.setToolTipText("");
        jPanel2.add(m_jChkTaxMapping);
        m_jChkTaxMapping.setBounds(340, 370, 210, 24);

        m_jChkCashManagement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkCashManagement.setText("Cash Management");
        jPanel2.add(m_jChkCashManagement);
        m_jChkCashManagement.setBounds(340, 10, 190, 24);

        m_jChkCloseDay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkCloseDay.setText("Close Day");
        jPanel2.add(m_jChkCloseDay);
        m_jChkCloseDay.setBounds(340, 520, 190, 24);

        m_jChkSalesReports.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkSalesReports.setText("Sales Reports");
        jPanel2.add(m_jChkSalesReports);
        m_jChkSalesReports.setBounds(620, 10, 150, 24);

        m_jSalesItemWise.setText("Sales-ItemWise");
        jPanel2.add(m_jSalesItemWise);
        m_jSalesItemWise.setBounds(640, 40, 170, 24);

        m_jChkDiscountRegister.setText("Discount Register Report");
        jPanel2.add(m_jChkDiscountRegister);
        m_jChkDiscountRegister.setBounds(640, 70, 200, 24);

        m_jChkKotAnalysis.setText("Kot Analysis Report");
        jPanel2.add(m_jChkKotAnalysis);
        m_jChkKotAnalysis.setBounds(640, 100, 170, 24);

        m_jChkKotCancel.setText("Kot Cancel Report");
        jPanel2.add(m_jChkKotCancel);
        m_jChkKotCancel.setBounds(640, 130, 170, 24);

        m_jChkCancelledBills.setText("Cancelled Bills");
        jPanel2.add(m_jChkCancelledBills);
        m_jChkCancelledBills.setBounds(640, 160, 160, 24);

        m_jChkSalesBillWise.setText("Sales Billwise Report");
        jPanel2.add(m_jChkSalesBillWise);
        m_jChkSalesBillWise.setBounds(640, 190, 170, 24);

        m_jChkSettlement.setText("Settlement Report");
        jPanel2.add(m_jChkSettlement);
        m_jChkSettlement.setBounds(640, 220, 290, 24);

        m_jChkSectionWise.setText("Settlement Report Section Wise");
        jPanel2.add(m_jChkSectionWise);
        m_jChkSectionWise.setBounds(640, 250, 290, 24);

        m_jChkTaxSummary.setText("Tax Summary Report");
        jPanel2.add(m_jChkTaxSummary);
        m_jChkTaxSummary.setBounds(640, 280, 190, 24);

        m_jChkCashReports.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkCashReports.setText("Cash Report");
        jPanel2.add(m_jChkCashReports);
        m_jChkCashReports.setBounds(620, 400, 160, 24);

        m_jChkPettyExpenseReports.setText("Petty Cash Expense Report");
        jPanel2.add(m_jChkPettyExpenseReports);
        m_jChkPettyExpenseReports.setBounds(640, 430, 210, 24);

        m_jChkDailyCollection.setText("Daily Collection Report");
        jPanel2.add(m_jChkDailyCollection);
        m_jChkDailyCollection.setBounds(640, 460, 190, 24);

        m_jChkConfiguration.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkConfiguration.setText("Configuration");
        jPanel2.add(m_jChkConfiguration);
        m_jChkConfiguration.setBounds(340, 550, 210, 24);

        m_jchkBillPreview.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jchkBillPreview.setText("Bill Preview");
        jPanel2.add(m_jchkBillPreview);
        m_jchkBillPreview.setBounds(50, 70, 180, 24);

        m_jChkNcReports.setText("Non Chargeable  Report");
        jPanel2.add(m_jChkNcReports);
        m_jChkNcReports.setBounds(640, 310, 240, 24);

        m_jChkUnlock.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkUnlock.setText("Unlock Table");
        jPanel2.add(m_jChkUnlock);
        m_jChkUnlock.setBounds(620, 490, 160, 24);

        m_jChkKodStatus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkKodStatus.setText("KDS Status Master");
        jPanel2.add(m_jChkKodStatus);
        m_jChkKodStatus.setBounds(50, 550, 170, 24);

        m_jChkBillOnHold.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkBillOnHold.setText("Bill On Hold");
        jPanel2.add(m_jChkBillOnHold);
        m_jChkBillOnHold.setBounds(620, 520, 110, 24);

        m_jChkTakeAway.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkTakeAway.setText("Take Away");
        jPanel2.add(m_jChkTakeAway);
        m_jChkTakeAway.setBounds(50, 100, 140, 24);

        m_jChkMoveTableReport.setText("Move Table Report");
        jPanel2.add(m_jChkMoveTableReport);
        m_jChkMoveTableReport.setBounds(640, 340, 210, 24);

        m_jChkSalesSummary.setText("Sales Summary Report");
        jPanel2.add(m_jChkSalesSummary);
        m_jChkSalesSummary.setBounds(640, 370, 240, 20);

        jScrollPane1.setViewportView(jPanel2);

        jTabbedPane.addTab("Menu", jScrollPane1);

        jScrollPane2.setAutoscrolls(true);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setAutoscrolls(true);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jChkPrintBill.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkPrintBill.setText("Print Bill");
        m_jChkPrintBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jChkPrintBillActionPerformed(evt);
            }
        });
        jPanel4.add(m_jChkPrintBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 100, 20));

        m_jChkSettleBill.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkSettleBill.setText("Settle Bill");
        jPanel4.add(m_jChkSettleBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 100, 20));

        m_jChkMoveTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkMoveTable.setText("Move Table");
        jPanel4.add(m_jChkMoveTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 100, 20));

        m_jChkCancel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkCancel.setText("Cancel Bill");
        jPanel4.add(m_jChkCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 100, 20));

        m_jChkIBillDiscount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkIBillDiscount.setText("Bill Discount");
        jPanel4.add(m_jChkIBillDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 100, 20));

        m_jChkSplitBill.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jChkSplitBill.setText("Split Bill");
        jPanel4.add(m_jChkSplitBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 100, 20));

        m_jBtnBillOnHold.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        m_jBtnBillOnHold.setText("Bill On Hold");
        jPanel4.add(m_jBtnBillOnHold, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        jScrollPane2.setViewportView(jPanel4);

        jTabbedPane.addTab("Pos Actions", jScrollPane2);

        jScrollPane3.setAutoscrolls(true);

        jPanelPArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanelPArea.setAutoscrolls(true);
        jPanelPArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelPrArea.setLayout(new java.awt.GridLayout(1, 0));
        jPanelPArea.add(jPanelPrArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 460, 320));

        jScrollPane3.setViewportView(jPanelPArea);

        jTabbedPane.addTab("Production Area", jScrollPane3);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Roles");

        m_jCboRoles.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                m_jCboRolesItemStateChanged(evt);
            }
        });

        jbtnSave.setBackground(new java.awt.Color(255, 255, 255));
        jbtnSave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jbtnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png"))); // NOI18N
        jbtnSave.setText("Save");
        jbtnSave.setFocusPainted(false);
        jbtnSave.setFocusable(false);
        jbtnSave.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jbtnSave.setRequestFocusEnabled(false);
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(m_jCboRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jCboRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
  public void populateRoles(){
          m_jCboRoles.removeAllItems();
        try {
            rolesList = (List<RoleInfo>) m_dlSales.getRoleList();
        } catch (BasicException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            for (RoleInfo dis : rolesList) {
                m_jCboRoles.addItem(dis.getName());

            }
            m_jCboRoles.setSelectedIndex(-1);
            roleId=null;
    }
    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed

   if(m_jCboRoles.getSelectedIndex()==-1){
       showMessage(this, "Please select the roles");
   }else{
      setCheckBox();
      writeXml();
      SavePrArea();
      saveButtonActions();
      setCheckBoxValues();
      m_jCboRoles.setSelectedIndex(-1);
   }
   

}//GEN-LAST:event_jbtnSaveActionPerformed
public void saveButtonActions(){
    String printBill ="N";
    String settleBill="N";
    String cancelBill="N";
    String billDiscount="N";
    String splitBill="N";
    String kot="N";
    String moveTable="N";
    String billOnHold="N";
    int posActionsCount = 0;
    if(m_jChkPrintBill.isSelected()){
        printBill ="Y";
    }else{
        printBill ="N";
    }
    if(m_jChkSettleBill.isSelected()){
        settleBill ="Y";
    }else{
        settleBill ="N";
    }
    if(m_jChkCancel.isSelected()){
        cancelBill ="Y";
    }else{
        cancelBill ="N";
    }
    if(m_jChkIBillDiscount.isSelected()){
        billDiscount ="Y";
    }else{
        billDiscount ="N";
    }
    if(m_jChkSplitBill.isSelected()){
        splitBill ="Y";
    }else{
        splitBill ="N";
    }
  
     if(m_jChkMoveTable.isSelected()){
        moveTable ="Y";
    }else{
        moveTable ="N";
    }
    if(m_jBtnBillOnHold.isSelected()){
        billOnHold ="Y";
    }else{
        billOnHold ="N";
    }

            try {
                posActionsCount = m_dlSales.getPosActionsCount(roleId);
            } catch (BasicException ex) {
                Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    
    if(posActionsCount==0){
       m_dlSales.insertPosActionsAccess(roleId, printBill, settleBill, cancelBill, billDiscount, splitBill, moveTable,billOnHold);
    }else{
       m_dlSales.updatePosActionsAccess(roleId, printBill, settleBill, cancelBill, billDiscount, splitBill, moveTable,billOnHold);
    }
}
 public void setCheckBox(){
    chkClassName.clear();
    classValue.clear();
    if(m_jChkBilling.isSelected()){
        chkClassName.add("com.openbravo.pos.sales.JRetailPanelTicketSales");
        classValue.add("Billing");
    }else{
     chkClassName.remove("com.openbravo.pos.sales.JRetailPanelTicketSales");
    }
    if(m_jChkReprint.isSelected()){
        chkClassName.add("com.openbravo.pos.sales.JRetailRePrintTicket"); // TODO add your handling code here:
        classValue.add("Reprint");
    }else{
        chkClassName.remove("com.openbravo.pos.sales.JRetailRePrintTicket");
    }
    if(m_jChkItemMaster.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuItemManagement"); // TODO add your handling code here:
        classValue.add("Itemmaster");
    }else{
        chkClassName.remove("com.openbravo.pos.forms.MenuItemManagement");
    }
    if(m_jChkItem.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.ProductsPanel"); // TODO add your handling code here:
        classValue.add("Item");
    }else{
         chkClassName.remove("com.openbravo.pos.inventory.ProductsPanel");
       } // TODO add your handling code here:
     if(m_jChkItemCategories.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.CategoriesPanel"); // TODO add your handling code here:
        classValue.add("ItemCategories");
     }else{
        chkClassName.remove("com.openbravo.pos.inventory.CategoriesPanel");
     }   // TODO add your handling code here:
     if(m_jChkCustomerMenu.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuCustomerManagement"); // TODO add your handling code here:
        classValue.add("CustomerMenu");
     }else{
        chkClassName.remove("com.openbravo.pos.forms.MenuCustomerManagement");
      }
     if(m_jChkCustomers.isSelected()){
        chkClassName.add("com.openbravo.pos.customers.CustomersPanel"); // TODO add your handling code here:
        classValue.add("Customers");
     }else{
        chkClassName.remove("com.openbravo.pos.customers.CustomersPanel");
     }
     if(m_jChkTaxation.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuTaxation"); // TODO add your handling code here:
        classValue.add("Taxation");
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuTaxation"); // TODO add your handling code here:
     }
     if(m_jChkTaxCategories.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.TaxCategoriesPanel"); // TODO add your handling code here:
        classValue.add("TaxCategories");
     }else{
	chkClassName.remove("com.openbravo.pos.inventory.TaxCategoriesPanel"); // TODO add your handling code here:
     }
     if(m_jChkCusTax.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.TaxCustCategoriesPanel"); // TODO add your handling code here:
        classValue.add("CusTax");
     }else{
	chkClassName.remove("com.openbravo.pos.inventory.TaxCustCategoriesPanel"); // TODO add your handling code here:
     }         // TODO add your handling code here:
     if(m_jChkTaxRate.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.TaxPanel"); // TODO add your handling code here:
        classValue.add("TaxRate");
     }else{
	chkClassName.remove("com.openbravo.pos.inventory.TaxPanel"); // TODO add your handling code here:
     }               // TODO add your handling code here:
     if(m_jChkDiscountMenu.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuDiscount"); // TODO add your handling code here:
        classValue.add("DiscountMenu");
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuDiscount"); // TODO add your handling code here:
     }
     if(m_jChkAddDiscounts.isSelected()){
        chkClassName.add("com.sysfore.pos.cashmanagement.JDiscountPanel");
        classValue.add("AddDiscount");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.cashmanagement.JDiscountPanel"); // TODO add your handling code here:
     }
     if(m_jChkWarehouseMasters.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuWarehouseMasters");
        classValue.add("WarehouseMasters");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuWarehouseMasters"); // TODO add your handling code here:
     }       // TODO add your handling code here:
     if(m_jChkWarehouse1.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.LocationsPanel");
        classValue.add("Locations");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.openbravo.pos.inventory.LocationsPanel"); // TODO add your handling code here:
     }      // TODO add your handling code here:
     if(m_jChkStockMain.isSelected()){
        chkClassName.add("com.openbravo.pos.inventory.StockManagement");
        classValue.add("StockManagement");// TODO add your handling code here:
     }  else{
	chkClassName.remove("com.openbravo.pos.inventory.StockManagement"); // TODO add your handling code here:
     }
         // TODO add your handling code here:
     if(m_jChkCashManagement.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuCashManagement");
        classValue.add("MenuCashManagement");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuCashManagement");
     }
   // TODO add your handling code here:
     if(m_jChkFloatCash.isSelected()){
        chkClassName.add("com.sysfore.pos.cashmanagement.JFloatCashPanel");
        classValue.add("FloatCash");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.sysfore.pos.cashmanagement.JFloatCashPanel");
     }  // TODO add your handling code here:
     if(m_jChkPettyCash.isSelected()){
        chkClassName.add("com.sysfore.pos.cashmanagement.JPettyCashPanel");
        classValue.add("PettyCash");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.cashmanagement.JPettyCashPanel");
     }    // TODO add your handling code here:
     if(m_jChkIUserManagement.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuUsersRoles");
        classValue.add("UsersRoles");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuUsersRoles");
     }    // TODO add your handling code here:
     if(m_jChkuser.isSelected()){
        chkClassName.add("com.openbravo.pos.admin.PeoplePanel");
        classValue.add("Users");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.admin.PeoplePanel");
     }    // TODO add your handling code here:
     if(m_jChkRoles.isSelected()){
        chkClassName.add("com.openbravo.pos.admin.RolesPanel");
        classValue.add("Roles");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.openbravo.pos.admin.RolesPanel");
     }   // TODO add your handling code here:
     if(m_jChkResources.isSelected()){
        chkClassName.add("com.openbravo.pos.admin.ResourcesPanel");
        classValue.add("Resources");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.admin.ResourcesPanel");
     }   // TODO add your handling code here:
     if(m_jChkFloors.isSelected()){
        chkClassName.add("com.openbravo.pos.mant.JPanelFloors");
        classValue.add("Floors");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.openbravo.pos.mant.JPanelFloors");
     }     // TODO add your handling code here:
     if(m_jChkTables.isSelected()){
        chkClassName.add("com.openbravo.pos.mant.JPanelPlaces");
        classValue.add("Places");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.openbravo.pos.mant.JPanelPlaces");
     }    // TODO add your handling code here:
     if(m_jChkPosActions.isSelected()){
        chkClassName.add("com.sysfore.pos.panels.JPosActionsPanel");
        classValue.add("PosActions");// TODO add your handling code here:
     } else{
	chkClassName.remove("com.sysfore.pos.panels.JPosActionsPanel");
     }    // TODO add your handling code here:
     if(m_jChkServiceCharge.isSelected()){
        chkClassName.add("com.sysfore.pos.hotelmanagement.JServiceChargePanel");
        classValue.add("ServiceCharge");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.hotelmanagement.JServiceChargePanel");
     }     // TODO add your handling code here:
     if(m_jChkBusinessType.isSelected()){
        chkClassName.add("com.sysfore.pos.hotelmanagement.JBusinessTypePanel");
        classValue.add("BusinessType");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.hotelmanagement.JBusinessTypePanel");
     }
     if(m_jChkChargesMaster.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuChargesmaster");
        classValue.add("ChargesMaster");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuChargesmaster");
     }     // TODO add your handling code here:
     if(m_jChkPurDiscountMaster.isSelected()){
        chkClassName.add("com.sysfore.pos.purchaseorder.JExtraChargePanel");
        classValue.add("PurchaseCharges");// TODO add your handling code here:
     }else{
        chkClassName.remove("com.sysfore.pos.purchaseorder.JExtraChargePanel");
     }
     if(m_jChkTaxMapping.isSelected()){
        chkClassName.add("com.sysfore.pos.hotelmanagement.JTaxMappingPanel");
        classValue.add("TaxMapping");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.hotelmanagement.JTaxMappingPanel");
     }
     if(m_jChkPrinterConfig.isSelected()){
        chkClassName.add("com.sysfore.pos.hotelmanagement.JPrinterConfigEditor");
        classValue.add("PrinterConfig");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.hotelmanagement.JPrinterConfigEditor");
     }
     if(m_jChkCloseDay.isSelected()){
        chkClassName.add("com.sysfore.pos.cashmanagement.JPanelCashReconciliation");
        classValue.add("CloseDay");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.cashmanagement.JPanelCashReconciliation");
     }
     if(m_jChkPettyExpenses.isSelected()){
        chkClassName.add("com.sysfore.pos.cashmanagement.JPettyCashEditorPanel");
        classValue.add("PettyExpenses");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.sysfore.pos.cashmanagement.JPettyCashEditorPanel");
     }
     if(m_jChkSalesReports.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuSalesReports");
        classValue.add("SalesReports");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuSalesReports");
     }
     if(m_jSalesItemWise.isSelected()){
        chkClassName.add("/com/sysfore/SalesByPeriodItemwise.bs");
        classValue.add("SalesItemWise");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/SalesByPeriodItemwise.bs");
     }
     if(m_jChkKotAnalysis.isSelected()){
        chkClassName.add("/com/sysfore/KotReport.bs");
        classValue.add("KotAnalysis");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/KotReport.bs");
     }
     if(m_jChkDiscountRegister.isSelected()){
        chkClassName.add("/com/sysfore/DiscountReport.bs");
        classValue.add("DiscountRegister");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/DiscountReport.bs");
     }
     if(m_jChkKotCancel.isSelected()){
        chkClassName.add("/com/sysfore/KotCancelReport.bs");
        classValue.add("KotCancel");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/KotCancelReport.bs");
     }
     if(m_jChkCancelledBills.isSelected()){
        chkClassName.add("/com/sysfore/CancelledBill.bs");
        classValue.add("CancelledBills");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/CancelledBill.bs");
     }
     if(m_jChkSalesBillWise.isSelected()){
        chkClassName.add("/com/sysfore/SalesDetailsReport.bs");
        classValue.add("Salesbillwise");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/SalesDetailsReport.bs");
     }
     if(m_jChkSettlement.isSelected()){
        chkClassName.add("/com/sysfore/SettlementWithSectionReport.bs");
        classValue.add("Settlement");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/SettlementWithSectionReport.bs");
     }
     if(m_jChkSectionWise.isSelected()){
        chkClassName.add("/com/sysfore/SettlementSectionReport.bs");
        classValue.add("Sectionwise");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/SettlementSectionReport.bs");
     }
     if(m_jChkTaxSummary.isSelected()){
        chkClassName.add("/com/sysfore/TaxSummaryReport.bs");
        classValue.add("TaxSummary");// TODO add your handling code here:
     }else{
	chkClassName.remove("/com/sysfore/TaxSummaryReport.bs");
     }
     if(m_jChkCashReports.isSelected()){
        chkClassName.add("com.openbravo.pos.forms.MenuCashReports");
        classValue.add("CashReports");// TODO add your handling code here:
     }else{
	chkClassName.remove("com.openbravo.pos.forms.MenuCashReports");
     }
     if(m_jChkPettyExpenseReports.isSelected()){
         chkClassName.add("/com/openbravo/reports/pettyExpense.bs");
         classValue.add("PettyReport");// TODO add your handling code here:
     }else{
	 chkClassName.remove("/com/openbravo/reports/pettyExpense.bs");
     }
     if(m_jChkDailyCollection.isSelected()){
         chkClassName.add("/com/sysfore/DailyReport.bs");
         classValue.add("DailyCollection");// TODO add your handling code here:
     }else{
	 chkClassName.remove("/com/sysfore/DailyReport.bs");
     }
     if(m_jChkConfiguration.isSelected()){
         chkClassName.add("com.openbravo.pos.config.JPanelConfiguration");
         classValue.add("Configuration");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.config.JPanelConfiguration");
     }
     if(m_jchkBillPreview.isSelected()){
         chkClassName.add("com.openbravo.pos.sales.JRetailTicketPrintEdit");
         classValue.add("BillPreview");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.sales.JRetailTicketPrintEdit");
     }
    if(m_jChkNcReports.isSelected()){
         chkClassName.add("/com/sysfore/NCReport.bs");
         classValue.add("NCReport");// TODO add your handling code here:
     }else{
	 chkClassName.remove("/com/sysfore/NCReport.bs");
     }
    if(m_jChkUnlock.isSelected()){
         chkClassName.add("com.openbravo.pos.sales.restaurant.JRetailTicketUnlock");
         classValue.add("UnlockTable");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.sales.restaurant.JRetailTicketUnlock");
     }
     if(m_jChkKodStatus.isSelected()){
         chkClassName.add("com.openbravo.pos.inventory.JPanelKodMaster");
         classValue.add("KodStatusMaster");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.inventory.JPanelKodMaster");
     }
      if(m_jChkBillOnHold.isSelected()){
         chkClassName.add("com.openbravo.pos.sales.JRetailHoldBillEdit");
         classValue.add("BillOnHold");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.sales.JRetailHoldBillEdit");
     }
     if(m_jChkTakeAway.isSelected()){
         chkClassName.add("com.openbravo.pos.sales.JRetailPanelTakeAwaySales");
         classValue.add("TakeAway");// TODO add your handling code here:
     }else{
	 chkClassName.remove("com.openbravo.pos.sales.JRetailPanelTakeAwaySales");
     }
       if(m_jChkMoveTableReport.isSelected()){
         chkClassName.add("/com/sysfore/MoveTableLogReport.bs");
         classValue.add("MoveTableReport");// TODO add your handling code here:
     }else{
	 chkClassName.remove("/com/sysfore/MoveTableLogReport.bs");
     }
             if(m_jChkSalesSummary.isSelected()){
         chkClassName.add("/com/sysfore/SalesSummaryReport.bs");
         classValue.add("SalesSummaryReport");// TODO add your handling code here:
     }else{
	 chkClassName.remove("/com/sysfore/SalesSummaryReport.bs");
     }


    }
    private void showMessage(JPosActionsPanel aThis, String msg) {
        JOptionPane.showMessageDialog(aThis, getLabelPanel(msg), "Message",
                                        JOptionPane.INFORMATION_MESSAGE);

    }
private JPanel getLabelPanel(String msg) {
    JPanel panel = new JPanel();
    Font font = new Font("Verdana", Font.BOLD, 12);
    panel.setFont(font);
    panel.setOpaque(true);
   // panel.setBackground(Color.BLUE);
    JLabel label = new JLabel(msg, JLabel.LEFT);
    label.setForeground(Color.RED);
    label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    panel.add(label);

    return panel;
}

    private void m_jChkPrintBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jChkPrintBillActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_m_jChkPrintBillActionPerformed

    private void m_jCboRolesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_m_jCboRolesItemStateChanged
      if(m_jCboRoles.getSelectedIndex()!=-1){
         roleId = rolesList.get(m_jCboRoles.getSelectedIndex()).getID();
         readXml();
         setPrArea();
         System.out.println(classValue.size());
         if(classValue.size()==0){
             System.out.println("enetr size--"+classValue.size());
            setCheckBoxValues();
         }else{
            for (int i = 0; i < classValue.size(); i++) {
                String checkBoxName = classValue.get(i).toString();
               if(checkBoxName.equals("Billing")){
                   m_jChkBilling.setSelected(true);
               }
               if(checkBoxName.equals("Reprint")){
                   m_jChkReprint.setSelected(true);
               }
               if(checkBoxName.equals("Itemmaster")){
                   m_jChkItemMaster.setSelected(true);
               }
                 if(checkBoxName.equals("Item")){
                   m_jChkItem.setSelected(true);
               }
                 if(checkBoxName.equals("ItemCategories")){
                   m_jChkItemCategories.setSelected(true);
               }
                 if(checkBoxName.equals("CustomerMenu")){
                   m_jChkCustomerMenu.setSelected(true);
               }
                 if(checkBoxName.equals("Customers")){
                   m_jChkCustomers.setSelected(true);
               }
                 if(checkBoxName.equals("Taxation")){
                   m_jChkTaxation.setSelected(true);
               }
                 if(checkBoxName.equals("TaxCategories")){
                   m_jChkTaxCategories.setSelected(true);
               }
                 if(checkBoxName.equals("CusTax")){
                   m_jChkCusTax.setSelected(true);
               }
                 if(checkBoxName.equals("TaxRate")){
                   m_jChkTaxRate.setSelected(true);
               }
                 if(checkBoxName.equals("DiscountMenu")){
                   m_jChkDiscountMenu.setSelected(true);
               }
                 if(checkBoxName.equals("AddDiscount")){
                   m_jChkAddDiscounts.setSelected(true);
               }
                if(checkBoxName.equals("PosActions")){
                    m_jChkPosActions.setSelected(true);
                }
                 if(checkBoxName.equals("WarehouseMasters")){
                   m_jChkWarehouseMasters.setSelected(true);
               }
                 if(checkBoxName.equals("Locations")){
                   m_jChkWarehouse1.setSelected(true);
               }
                 if(checkBoxName.equals("StockManagement")){
                   m_jChkStockMain.setSelected(true);
               }
                if(checkBoxName.equals("MenuCashManagement")){
                   m_jChkCashManagement.setSelected(true);
               }
                 if(checkBoxName.equals("FloatCash")){
                   m_jChkFloatCash.setSelected(true);
               }
                 if(checkBoxName.equals("PettyCash")){
                   m_jChkPettyCash.setSelected(true);
               }
                 if(checkBoxName.equals("UsersRoles")){
                   m_jChkIUserManagement.setSelected(true);
               }
                 if(checkBoxName.equals("Users")){
                   m_jChkuser.setSelected(true);
               }
                 if(checkBoxName.equals("Roles")){
                   m_jChkRoles.setSelected(true);
               }
                 if(checkBoxName.equals("Resources")){
                   m_jChkResources.setSelected(true);
               }
                 if(checkBoxName.equals("Floors")){
                   m_jChkFloors.setSelected(true);
               }
                 if(checkBoxName.equals("Places")){
                   m_jChkTables.setSelected(true);
               }
                 if(checkBoxName.equals("PosActions")){
                   m_jChkBusinessType.setSelected(true);
               }
                 if(checkBoxName.equals("ServiceCharge")){
                   m_jChkServiceCharge.setSelected(true);
               }
                if(checkBoxName.equals("BusinessType")){
                   m_jChkBusinessType.setSelected(true);
               }
                if(checkBoxName.equals("ChargesMaster")){
                   m_jChkPrinterConfig.setSelected(true);
               }
                 if(checkBoxName.equals("PurchaseCharges")){
                   m_jChkPurDiscountMaster.setSelected(true);
               }
                if(checkBoxName.equals("TaxMapping")){
                   m_jChkTaxMapping.setSelected(true);
               }
                if(checkBoxName.equals("PrinterConfig")){
                   m_jChkPrinterConfig.setSelected(true);
               }
                if(checkBoxName.equals("CloseDay")){
                    m_jChkPettyExpenses.setSelected(true);
                }
                if(checkBoxName.equals("PettyExpenses")){
                    m_jChkPettyExpenses.setSelected(true);
                }
                if(checkBoxName.equals("SalesReports")){
                    m_jChkSalesReports.setSelected(true);
                }
                if(checkBoxName.equals("SalesItemWise")){
                    m_jSalesItemWise.setSelected(true);
                }
                 if(checkBoxName.equals("KotAnalysis")){
                    m_jChkKotAnalysis.setSelected(true);
                }
                if(checkBoxName.equals("DiscountRegister")){
                    m_jChkDiscountRegister.setSelected(true);
                }
                if(checkBoxName.equals("KotCancel")){
                    m_jChkKotCancel.setSelected(true);
                }
                if(checkBoxName.equals("CancelledBills")){
                    m_jChkCancelledBills.setSelected(true);
                }
                 if(checkBoxName.equals("Salesbillwise")){
                    m_jChkSalesBillWise.setSelected(true);
                }
                if(checkBoxName.equals("Settlement")){
                    m_jChkSettlement.setSelected(true);
                }
                if(checkBoxName.equals("Sectionwise")){
                    m_jChkSectionWise.setSelected(true);
                }
                 if(checkBoxName.equals("TaxSummary")){
                    m_jChkTaxSummary.setSelected(true);
                }
                if(checkBoxName.equals("CashReports")){
                    m_jChkCashReports.setSelected(true);
                }
                if(checkBoxName.equals("PettyReport")){
                    m_jChkPettyExpenseReports.setSelected(true);
                }
                if(checkBoxName.equals("DailyCollection")){
                    m_jChkDailyCollection.setSelected(true);
                }
                if(checkBoxName.equals("Configuration")){
                    m_jChkConfiguration.setSelected(true);
                }
                if(checkBoxName.equals("ChargesMaster")){
                    m_jChkChargesMaster.setSelected(true);
                }
                if(checkBoxName.equals("CloseDay")){
                    m_jChkCloseDay.setSelected(true);
                }
                if(checkBoxName.equals("BillPreview")){
                    m_jchkBillPreview.setSelected(true);
                }
                if(checkBoxName.equals("NCReport")){
                    m_jChkNcReports.setSelected(true);
                }
                if(checkBoxName.equals("UnlockTable")){
                    m_jChkUnlock.setSelected(true);
                }
                 if(checkBoxName.equals("KodStatusMaster")){
                    m_jChkKodStatus.setSelected(true);
                }
                if(checkBoxName.equals("BillOnHold")){
                    m_jChkBillOnHold.setSelected(true);
                }
                if(checkBoxName.equals("TakeAway")){
                    m_jChkTakeAway.setSelected(true);
                }
                if(checkBoxName.equals("MoveTableReport")){
                    m_jChkMoveTableReport.setSelected(true);
                }
                if(checkBoxName.equals("SalesSummaryReport")){
                    m_jChkSalesSummary.setSelected(true);
                }
            }
            }
          try {
            posActions = (List<PosActionsInfo>) m_dlSales.getPosActions(roleId);
        } catch (BasicException ex) {
            Logger.getLogger(JPosActionsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
         if(posActions.size()!=0){
         if(posActions.get(0).getPrintAccess().equals("Y")){
             m_jChkPrintBill.setSelected(true);
         }else{
             m_jChkPrintBill.setSelected(false);
         }
          if(posActions.get(0).getSettleAccess().equals("Y")){
             m_jChkSettleBill.setSelected(true);
         }else{
             m_jChkSettleBill.setSelected(false);
         }
          if(posActions.get(0).getCancelAccess().equals("Y")){
             m_jChkCancel.setSelected(true);
         }else{
             m_jChkCancel.setSelected(false);
         }
          if(posActions.get(0).getDiscountAccess().equals("Y")){
             m_jChkIBillDiscount.setSelected(true);
         }else{
             m_jChkIBillDiscount.setSelected(false);
         }
          if(posActions.get(0).getSplitAccess().equals("Y")){
             m_jChkSplitBill.setSelected(true);
         }else{
             m_jChkSplitBill.setSelected(false);
         }
          if(posActions.get(0).getMoveTableAccess().equals("Y")){
             m_jChkMoveTable.setSelected(true);
         }else{
             m_jChkMoveTable.setSelected(false);
         }
         if(posActions.get(0).getBillOnHoldAccess().equals("Y")){
             m_jBtnBillOnHold.setSelected(true);
         }else{
             m_jBtnBillOnHold.setSelected(false);
         }

         }
        
      //   if(int i=0;
      //  String permissions = m_dlSystem.findRolePermissions(rolesList.get(m_jCboRoles.getSelectedIndex()).getID());
       // System.out.println("permissions---"+permissions);// TODO add your handling code here:
        // TODO add your handling code here:
    }    
    }//GEN-LAST:event_m_jCboRolesItemStateChanged
    public void setCheckBoxValues(){
           m_jChkBilling.setSelected(false);
           m_jChkReprint.setSelected(false);
           m_jChkItemMaster.setSelected(false);
           m_jChkItem.setSelected(false);
           m_jChkItemCategories.setSelected(false);
           m_jChkCustomerMenu.setSelected(false);
           m_jChkCustomers.setSelected(false);
           m_jChkTaxation.setSelected(false);
           m_jChkTaxCategories.setSelected(false);
           m_jChkCusTax.setSelected(false);
           m_jChkTaxRate.setSelected(false);
           m_jChkDiscountMenu.setSelected(false);
           m_jChkAddDiscounts.setSelected(false);
           m_jChkWarehouseMasters.setSelected(false);
           m_jChkWarehouse1.setSelected(false);
           m_jChkStockMain.setSelected(false);
           m_jChkPettyExpenses.setSelected(false);
           m_jChkFloatCash.setSelected(false);
           m_jChkPettyCash.setSelected(false);
           m_jChkIUserManagement.setSelected(false);
           m_jChkuser.setSelected(false);
           m_jChkRoles.setSelected(false);
           m_jChkResources.setSelected(false);
           m_jChkFloors.setSelected(false);
           m_jChkTables.setSelected(false);
           m_jChkBusinessType.setSelected(false);
           m_jChkServiceCharge.setSelected(false);
           m_jChkMoveTable.setSelected(false);
           m_jChkCashManagement.setSelected(false);
           m_jChkPosActions.setSelected(false);
           m_jChkSplitBill.setSelected(false);
           m_jChkIBillDiscount.setSelected(false);
           m_jChkCancel.setSelected(false);
           m_jChkSettleBill.setSelected(false);
           m_jChkPrintBill.setSelected(false);
            m_jChkBusinessType.setSelected(false);
            m_jChkTaxMapping.setSelected(false);
            m_jChkPrinterConfig.setSelected(false);
            m_jChkChargesMaster.setSelected(false);
            m_jChkPurDiscountMaster.setSelected(false);
            m_jChkPettyExpenses.setSelected(false);
            m_jChkPettyExpenses.setSelected(false);
            m_jChkSalesReports.setSelected(false);
            m_jSalesItemWise.setSelected(false);
            m_jChkKotAnalysis.setSelected(false);
            m_jChkDiscountRegister.setSelected(false);
            m_jChkKotCancel.setSelected(false);
            m_jChkCancelledBills.setSelected(false);
            m_jChkSalesBillWise.setSelected(false);
            m_jChkSettlement.setSelected(false);
            m_jChkSectionWise.setSelected(false);
            m_jChkTaxSummary.setSelected(false);
            m_jChkCashReports.setSelected(false);
            m_jChkPettyExpenseReports.setSelected(false);
            m_jChkDailyCollection.setSelected(false);
            m_jChkConfiguration.setSelected(false);
            m_jChkCloseDay.setSelected(false);
            m_jchkBillPreview.setSelected(false);
            m_jChkNcReports.setSelected(false);
            m_jChkUnlock.setSelected(false);
            m_jChkKodStatus.setSelected(false);
            m_jChkBillOnHold.setSelected(false);
            m_jBtnBillOnHold.setSelected(false);
            m_jChkTakeAway.setSelected(false);
            m_jChkMoveTableReport.setSelected(false);
              m_jChkSalesSummary.setSelected(false);
            classValue.clear();
            chkClassName.clear();
         //   prAreaIdlist.clear();
}
    
    
  //  }
    
   private boolean checkDiscountNameAvl(String percentage) throws BasicException {
        String name = dlCustomers.getDiscountName(percentage);
        if ("NONAME".equalsIgnoreCase(name)) {
            return false;
        } else {
            return true;
        }
    } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelPArea;
    private javax.swing.JPanel jPanelPrArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JCheckBox m_jBtnBillOnHold;
    private javax.swing.JComboBox m_jCboRoles;
    private javax.swing.JCheckBox m_jChkAddDiscounts;
    private javax.swing.JCheckBox m_jChkBillOnHold;
    private javax.swing.JCheckBox m_jChkBilling;
    private javax.swing.JCheckBox m_jChkBusinessType;
    private javax.swing.JCheckBox m_jChkCancel;
    private javax.swing.JCheckBox m_jChkCancelledBills;
    private javax.swing.JCheckBox m_jChkCashManagement;
    private javax.swing.JCheckBox m_jChkCashReports;
    private javax.swing.JCheckBox m_jChkChargesMaster;
    private javax.swing.JCheckBox m_jChkCloseDay;
    private javax.swing.JCheckBox m_jChkConfiguration;
    private javax.swing.JCheckBox m_jChkCusTax;
    private javax.swing.JCheckBox m_jChkCustomerMenu;
    private javax.swing.JCheckBox m_jChkCustomers;
    private javax.swing.JCheckBox m_jChkDailyCollection;
    private javax.swing.JCheckBox m_jChkDiscountMenu;
    private javax.swing.JCheckBox m_jChkDiscountRegister;
    private javax.swing.JCheckBox m_jChkFloatCash;
    private javax.swing.JCheckBox m_jChkFloors;
    private javax.swing.JCheckBox m_jChkIBillDiscount;
    private javax.swing.JCheckBox m_jChkIUserManagement;
    private javax.swing.JCheckBox m_jChkItem;
    private javax.swing.JCheckBox m_jChkItemCategories;
    private javax.swing.JCheckBox m_jChkItemMaster;
    private javax.swing.JCheckBox m_jChkKodStatus;
    private javax.swing.JCheckBox m_jChkKotAnalysis;
    private javax.swing.JCheckBox m_jChkKotCancel;
    private javax.swing.JCheckBox m_jChkMoveTable;
    private javax.swing.JCheckBox m_jChkMoveTableReport;
    private javax.swing.JCheckBox m_jChkNcReports;
    private javax.swing.JCheckBox m_jChkPettyCash;
    private javax.swing.JCheckBox m_jChkPettyExpenseReports;
    private javax.swing.JCheckBox m_jChkPettyExpenses;
    private javax.swing.JCheckBox m_jChkPosActions;
    private javax.swing.JCheckBox m_jChkPrintBill;
    private javax.swing.JCheckBox m_jChkPrinterConfig;
    private javax.swing.JCheckBox m_jChkPurDiscountMaster;
    private javax.swing.JCheckBox m_jChkReprint;
    private javax.swing.JCheckBox m_jChkResources;
    private javax.swing.JCheckBox m_jChkRoles;
    private javax.swing.JCheckBox m_jChkSalesBillWise;
    private javax.swing.JCheckBox m_jChkSalesReports;
    private javax.swing.JCheckBox m_jChkSalesSummary;
    private javax.swing.JCheckBox m_jChkSectionWise;
    private javax.swing.JCheckBox m_jChkServiceCharge;
    private javax.swing.JCheckBox m_jChkSettleBill;
    private javax.swing.JCheckBox m_jChkSettlement;
    private javax.swing.JCheckBox m_jChkSplitBill;
    private javax.swing.JCheckBox m_jChkStockMain;
    private javax.swing.JCheckBox m_jChkTables;
    private javax.swing.JCheckBox m_jChkTakeAway;
    private javax.swing.JCheckBox m_jChkTaxCategories;
    private javax.swing.JCheckBox m_jChkTaxMapping;
    private javax.swing.JCheckBox m_jChkTaxRate;
    private javax.swing.JCheckBox m_jChkTaxSummary;
    private javax.swing.JCheckBox m_jChkTaxation;
    private javax.swing.JCheckBox m_jChkUnlock;
    private javax.swing.JCheckBox m_jChkWarehouse1;
    private javax.swing.JCheckBox m_jChkWarehouseMasters;
    private javax.swing.JCheckBox m_jChkuser;
    private javax.swing.JCheckBox m_jSalesItemWise;
    private javax.swing.JCheckBox m_jchkBillPreview;
    // End of variables declaration//GEN-END:variables
    
    
}
