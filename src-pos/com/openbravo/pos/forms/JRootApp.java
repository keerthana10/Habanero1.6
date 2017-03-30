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
// Habanero SVN Test
package com.openbravo.pos.forms;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.swing.*;
import java.util.List;
import java.util.Timer;
import com.openbravo.pos.printer.*;

import com.openbravo.beans.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.loader.BatchSentence;
import com.openbravo.data.loader.BatchSentenceResource;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.sales.JRetailPanelTicket;
import com.openbravo.pos.scale.DeviceScale;
import com.openbravo.pos.scanpal2.DeviceScanner;
import com.openbravo.pos.scanpal2.DeviceScannerFactory;
import com.sysfore.pos.licensemanagement.DataLogicLicense;
import com.sysfore.pos.licensemanagement.LicenceManagementUtil;
import com.sysfore.pos.purchaseorder.PurchaseOrderReceipts;
import com.sysfore.pos.panels.RoundedPanel;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

//import org.json.simple.JSONObject;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sysfore.pos.cashmanagement.FloatCashSetupinfo;
import com.sysfore.pos.cashmanagement.PettyCashSetupinfo;
import com.sysfore.pos.licensemanagement.LicenceInfo;
import com.sysfore.pos.licensemanagement.ClientEntity;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author adrianromero
 */
public class JRootApp extends JPanel implements AppView {

    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;
    private AppProperties m_props;
    private Session session;
    private DataLogicSystem m_dlSystem;
    private Properties m_propsdb = null;
    private String m_sActiveCashIndex;
    private int m_iActiveCashSequence;
    private Date m_dActiveCashDateStart;
    private Date m_dActiveCashDateEnd;
    private String m_sActiveDayIndex;
    private int m_iActiveDaySequence;
    private Date m_dActiveDayDateStart;
    private Date m_dActiveDayDateEnd;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat changeFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String m_sInventoryLocation;
    private DataLogicSales m_dlSales;
    private StringBuffer inputtext;
    private DeviceScale m_Scale;
    private DeviceScanner m_Scanner;
    private DeviceTicket m_TP;
    private TicketParser m_TTP;
    private PurchaseOrderReceipts m_dlPOReceipts;
    private Map<String, BeanFactory> m_aBeanFactories;
    private DataLogicLicense m_dlLicense;
    String arg[];
    private JPrincipalApp m_principalapp = null;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private static HashMap<String, String> m_oldclasses; // This is for backwards compatibility purposes
    public ClientEntity lc = null;
    private List<LicenceInfo> license;
    int matchedrow = 0;
    boolean macAdd = false;
    int licenseCount = 0;

    static {

        initOldClasses();

    }
    private Timer timer;
    private String address;

    /** Creates new form JRootApp */
    public JRootApp() {
        m_aBeanFactories = new HashMap<String, BeanFactory>();

        //
        // Initialize visual components
        initComponents();

        m_jTxtUserName.setFocusable(true);

        InputMap im = m_jBtnCancel.getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

        InputMap im1 = m_jBtnLogin.getInputMap();
        im1.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im1.put(KeyStroke.getKeyStroke("released ENTER"), "released");
        jScrollUsers.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
    }

    public boolean initApp(AppProperties props) {

        m_props = props;
        m_jTxtUserName.setEnabled(true);
        m_jTxtUserName.setFocusable(true);
        m_jTxtUserName.setEditable(true);
        String logpath = getProperties().getProperty("machine.logfile");
        logpath = logpath + getLogDate() + ".txt";
        try {
            fh = new FileHandler(logpath, true);
        } catch (IOException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(JRetailPanelTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        // Database start
        try {
            session = AppViewConnection.createSession(m_props);
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, e.getMessage(), e));
            return false;
        }

        m_dlSystem = (DataLogicSystem) getBean("com.openbravo.pos.forms.DataLogicSystem");
        this.m_dlPOReceipts = (PurchaseOrderReceipts) getBean("com.sysfore.pos.purchaseorder.PurchaseOrderReceipts");
        m_dlLicense = (DataLogicLicense) getBean("com.sysfore.pos.licensemanagement.DataLogicLicense");
        m_dlSales = (DataLogicSales) getBean("com.openbravo.pos.forms.DataLogicSales");

        // Create or upgrade the database if database version is not the expected
        String sDBVersion = readDataBaseVersion();
        if (!AppLocal.APP_VERSION.equals(sDBVersion)) {

            // Create or upgrade database

            String sScript = sDBVersion == null
                    ? m_dlSystem.getInitScript() + "-create.sql"
                    : m_dlSystem.getInitScript() + "-upgrade-" + sDBVersion + ".sql";

            if (JRootApp.class.getResource(sScript) == null) {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, sDBVersion == null
                        ? AppLocal.getIntString("message.databasenotsupported", session.DB.getName()) // Create script does not exists. Database not supported
                        : AppLocal.getIntString("message.noupdatescript"))); // Upgrade script does not exist.
                session.close();
                return false;
            } else {
                // Create or upgrade script exists.
                if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString(sDBVersion == null ? "message.createdatabase" : "message.updatedatabase"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    try {
                        BatchSentence bsentence = new BatchSentenceResource(session, sScript);
                        bsentence.putParameter("APP_ID", Matcher.quoteReplacement(AppLocal.APP_ID));
                        bsentence.putParameter("APP_NAME", Matcher.quoteReplacement(AppLocal.APP_NAME));
                        bsentence.putParameter("APP_VERSION", Matcher.quoteReplacement(AppLocal.APP_VERSION));

                        java.util.List l = bsentence.list();
                        if (l.size() > 0) {
                            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("Database.ScriptWarning"), l.toArray(new Throwable[l.size()])));
                        }
                    } catch (BasicException e) {
                        JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("Database.ScriptError"), e));
                        session.close();
                        return false;
                    }
                } else {
                    session.close();
                    return false;
                }
            }
        }

        // Assign properties from database
        m_propsdb = m_dlSystem.getResourceAsProperties(m_props.getHost() + "/properties");
        m_TP = new DeviceTicket(this, m_props);
        m_TP = new DeviceTicket(this, m_props);
        // Inicializamos
        m_TTP = new TicketParser(getDeviceTicket(), m_dlSystem);
        printerStart();
        // Inicializamos la bascula
        m_Scale = new DeviceScale(this, m_props);
        // It initializes the ScanPal
        m_Scanner = DeviceScannerFactory.createInstance(m_props);

        //1 license logic starts here
        // Get the license details from database
//        try {
//            license = m_dlLicense.getLicenseDetails();
//         } catch (BasicException ex) {
//            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //Get the current system mac address
//        address = new GetMACAddress().getCurrenetAddress();
//        for(int i=0;i<license.size();i++ ){
//        // Decrypting the database inserted mac address to verify with current mac address
//        String macAddress = LicenceManagementUtil.decrypt(license.get(i).getMachineAddress());
//        StringTokenizer st = new StringTokenizer(macAddress, "|");
//            while (st.hasMoreTokens()) {
//                String macCaptured = st.nextToken();
//                //comparing the system mac address and database stored mac address
//                    if (macCaptured.equalsIgnoreCase(address)) {
//                    matchedrow=i;
//                    macAdd=true;
//                    break;
//                }
//            }
//        }
//        if(macAdd==false){
//           licenseCount=0;
//        }else{
//            licenseCount=1;
//        }
//
//         if(licenseCount==0){
//            showLicense();
//            jLabel4.setText("Your license status is inactive.");
//        }else{
//
//            if(macAdd==true){
//            //Decrypting the database inserted validFrom & validTo date to verify license got expired or not
//            String validFrom = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivateFrom());
//            String validTo = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivateTo());
//
//            int licenseExpCount = 0;
//            try {
//                licenseExpCount = m_dlLicense.getExpiryLicenseCount(validFrom, validTo);
//                licenseExpCount = m_dlLicense.getMultiExpiryLicenseCount(validFrom, validTo,license.get(matchedrow).getMachineAddress());
//            } catch (BasicException ex) {
//                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            String activated = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivated());
//            String purpose = LicenceManagementUtil.decrypt(license.get(matchedrow).getStrPurposeValue());
//            if(licenseExpCount==0){
//                //Purpose is evaluation && trial license expired
//                if(purpose.equals("E")){
//                    showLicense();
//
//                    jLabel4.setText("Your Trial license is expired. Kindly request for a new license for continuing to use all the modules.");
//                }else{
//
//                //Purpose is production or testing && not activated
//                if(!purpose.equals("E") && activated.equals("N")){
//                    m_jRequest.setText("Activate");
//                     showLicense();
//                     jLabel4.setText("Your license is expired. Kindly request for a new license for continuing to use all the modules.");
//                }//Purpose is production or testing && activated
//                else if(!purpose.equals("E") && activated.equals("Y")){
//                    m_jRequest.setText("Renew");
//                    showLicense();
//                     jLabel4.setText("Your license is expired. Kindly request for a new license for continuing to use all the modules.");
//                }
//            }}else{
        //1 license logic ends here
        //Automatic close day logic. 
        closeDay();

        m_sInventoryLocation = m_propsdb.getProperty("location");
        if (m_sInventoryLocation == null) {
            m_sInventoryLocation = "0";
            m_propsdb.setProperty("location", m_sInventoryLocation);
            m_dlSystem.setResourceAsProperties(m_props.getHost() + "/properties", m_propsdb);
        }

        // Inicializo la impresora...
        m_TP = new DeviceTicket(this, m_props);

        // Inicializamos
        m_TTP = new TicketParser(getDeviceTicket(), m_dlSystem);
        printerStart();

        // Inicializamos la bascula
        m_Scale = new DeviceScale(this, m_props);

        // Inicializamos la scanpal
        m_Scanner = DeviceScannerFactory.createInstance(m_props);

        String sWareHouse;
        try {
            sWareHouse = m_dlSystem.findLocationName(m_sInventoryLocation);
        } catch (BasicException e) {
            sWareHouse = null; // no he encontrado el almacen principal
        }

        // Show Hostname, Warehouse and URL in taskbar
        String url;
        try {
            url = session.getURL();
        } catch (SQLException e) {
            url = "";
        }
        showLogin();
// //2 license logic starts here
//          if(!purpose.equals("E") && activated.equals("N")){
//                m_jRequest.setText("Activate");
//                showLicense();
//                jLabel4.setText("Your license is not activated yet, kindly email license@sysforehrms.com for any queries. ");
//            }else if(!purpose.equals("E") && activated.equals("Y")){
//                m_jRequest.setText("Renew");
//                showLogin();
//                String activatedToDate = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivateTo());
//                Date activation = null;
//            try {
//                activation = changeFormat.parse(activatedToDate);
//           } catch (java.text.ParseException ex) {
//                Logger.getLogger(LicenceInfo.class.getName()).log(Level.SEVERE, null, ex);
//           }
//            activatedToDate = dateformat.format(activation);
//          //  jLblLicenseValid.setText("Your license is active and is valid till " +activatedToDate);
//            disableLicense();
//            }
//
//            }
//        }
//          else{
//            showLicense();
//            jLabel4.setText("Your license status is inactive.");
//            //deletion should not happen as the multi system using single db
////                try {
////                    m_dlLicense.deleteLicenseKey();
////                } catch (BasicException ex) {
////                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
////                }
//        }
//        }

        //2 license logic ends here
        return true;
    }

    public String getLogDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String readDataBaseVersion() {
        try {
            return m_dlSystem.findVersion();
        } catch (BasicException ed) {
            return null;
        }
    }
    //Used for showing the license panel

    private void showLicense() {
        jScrollUsers.setVisible(false);
        jLicensePanel.setVisible(true);
        m_jLoginPanel.setVisible(false);
        m_jLblUserName.setVisible(false);
        m_jLblPassword.setVisible(false);
        jLblUserNameIsNull.setVisible(false);
        m_jTxtUserName.setVisible(false);
        m_jTxtPassword.setVisible(false);
        m_jTxtCardNo.setVisible(false);
        jLblPasswordIsNull.setVisible(false);
        jLblInvalidNamePwd.setVisible(false);
        jLblLicenseValid.setVisible(false);
        showComboBox();
        printerStart();
        inputtext = new StringBuffer();
    }

    //Used for loading the license purpose in combo box in license panel
    private void showComboBox() {
        String purpose;
        if (licenseCount == 0) {
            purpose = "";
        } else {
            purpose = LicenceManagementUtil.decrypt(license.get(matchedrow).getStrPurposeValue());
        }

        if (purpose.equals("")) {
            m_jCboStrPurpose.addItem("Evaluation");
            m_jCboStrPurpose.addItem("Testing");
            m_jCboStrPurpose.addItem("Production");
        } else if (purpose.equals("E")) {
            m_jCboStrPurpose.removeAllItems();
            m_jCboStrPurpose.addItem("Evaluation");
            m_jCboStrPurpose.addItem("Testing");
            m_jCboStrPurpose.addItem("Production");
        } else if (purpose.equals("P")) {
            m_jCboStrPurpose.removeAllItems();
            m_jCboStrPurpose.addItem("Production");
        } else {
            m_jCboStrPurpose.removeAllItems();
            m_jCboStrPurpose.addItem("Testing");
            m_jCboStrPurpose.addItem("Production");
        }

    }

    public void tryToClose() {

        if (closeAppView()) {

            // success. continue with the shut down

            // apago el visor
            m_TP.getDeviceDisplay().clearVisor();
            // me desconecto de la base de datos.
            session.close();

            // Download Root form
            SwingUtilities.getWindowAncestor(this).dispose();
        }
    }

    // Interfaz de aplicacion
    public DeviceTicket getDeviceTicket() {
        return m_TP;
    }

    public DeviceScale getDeviceScale() {
        return m_Scale;
    }

    public DeviceScanner getDeviceScanner() {
        return m_Scanner;
    }

    public Session getSession() {
        return session;
    }

    public String getInventoryLocation() {
        return m_sInventoryLocation;
    }

    public String getActiveCashIndex() {
        return m_sActiveCashIndex;
    }

    public int getActiveCashSequence() {
        return m_iActiveCashSequence;
    }

    public Date getActiveCashDateStart() {
        return m_dActiveCashDateStart;
    }

    public Date getActiveCashDateEnd() {
        return m_dActiveCashDateEnd;
    }

    public void setActiveCash(String sIndex, int iSeq, Date dStart, Date dEnd) {
        m_sActiveCashIndex = sIndex;
        m_iActiveCashSequence = iSeq;
        m_dActiveCashDateStart = dStart;
        m_dActiveCashDateEnd = dEnd;

        m_propsdb.setProperty("activecash", m_sActiveCashIndex);
        m_dlSystem.setResourceAsProperties(m_props.getHost() + "/properties", m_propsdb);
    }

    public String getActiveDayIndex() {
        return m_sActiveDayIndex;
    }

    public int getActiveDaySequence() {
        return m_iActiveDaySequence;
    }

    public Date getActiveDayDateStart() {
        return m_dActiveDayDateStart;
    }

    public Date getActiveDayDateEnd() {
        return m_dActiveDayDateEnd;
    }

    public void setActiveDay(String sIndex, int iSeq, Date dStart, Date dEnd) {
        m_sActiveDayIndex = sIndex;
        m_iActiveDaySequence = iSeq;
        m_dActiveDayDateStart = dStart;
        m_dActiveDayDateEnd = dEnd;

        m_propsdb.setProperty("activeday", m_sActiveDayIndex);
        m_dlSystem.setResourceAsProperties(m_props.getHost() + "/properties", m_propsdb);
    }

    public AppProperties getProperties() {
        return m_props;
    }

    public Object getBean(String beanfactory) throws BeanFactoryException {

        // For backwards compatibility
        beanfactory = mapNewClass(beanfactory);


        BeanFactory bf = m_aBeanFactories.get(beanfactory);
        if (bf == null) {

            // testing sripts
            if (beanfactory.startsWith("/")) {
                bf = new BeanFactoryScript(beanfactory);
            } else {
                // Class BeanFactory
                try {
                    Class bfclass = Class.forName(beanfactory);

                    if (BeanFactory.class.isAssignableFrom(bfclass)) {
                        bf = (BeanFactory) bfclass.newInstance();
                    } else {
                        // the old construction for beans...
                        Constructor constMyView = bfclass.getConstructor(new Class[]{AppView.class});
                        Object bean = constMyView.newInstance(new Object[]{this});

                        bf = new BeanFactoryObj(bean);
                    }

                } catch (Exception e) {
                    // ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
                    throw new BeanFactoryException(e);
                }
            }

            // cache the factory
            m_aBeanFactories.put(beanfactory, bf);

            // Initialize if it is a BeanFactoryApp
            if (bf instanceof BeanFactoryApp) {
                ((BeanFactoryApp) bf).init(this);
            }
        }
        return bf.getBean();
    }

    private static String mapNewClass(String classname) {
        String newclass = m_oldclasses.get(classname);
        return newclass == null
                ? classname
                : newclass;
    }

    private static void initOldClasses() {
        m_oldclasses = new HashMap<String, String>();

        // update bean names from 2.00 to 2.20    
        m_oldclasses.put("com.openbravo.pos.reports.JReportCustomers", "/com/openbravo/reports/customers.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportCustomersB", "/com/openbravo/reports/customersb.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportClosedPos", "/com/openbravo/reports/closedpos.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportClosedProducts", "/com/openbravo/reports/closedproducts.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JChartSales", "/com/openbravo/reports/chartsales.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportInventory", "/com/openbravo/reports/inventory.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportInventory2", "/com/openbravo/reports/inventoryb.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportInventoryBroken", "/com/openbravo/reports/inventorybroken.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportInventoryDiff", "/com/openbravo/reports/inventorydiff.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportPeople", "/com/openbravo/reports/people.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportTaxes", "/com/openbravo/reports/taxes.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportUserSales", "/com/openbravo/reports/usersales.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportProducts", "/com/openbravo/reports/products.bs");
        m_oldclasses.put("com.openbravo.pos.reports.JReportCatalog", "/com/openbravo/reports/productscatalog.bs");

        // update bean names from 2.10 to 2.20
        m_oldclasses.put("com.openbravo.pos.panels.JPanelTax", "com.openbravo.pos.inventory.TaxPanel");

    }

    public void waitCursorBegin() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void waitCursorEnd() {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public AppUserView getAppUserView() {
        return m_principalapp;
    }

    private void printerStart() {

        String sresource = m_dlSystem.getResourceAsXML("Printer.Start");
        if (sresource == null) {
            m_TP.getDeviceDisplay().writeVisor(AppLocal.APP_NAME, AppLocal.APP_VERSION);
        } else {
            try {
                m_TTP.printTicket(sresource);
            } catch (TicketPrinterException eTP) {
                m_TP.getDeviceDisplay().writeVisor(AppLocal.APP_NAME, AppLocal.APP_VERSION);
            }
        }
    }

    private void listPeople() {

        try {

            jScrollUsers.getViewport().setView(null);

            JFlowPanel jPeople = new JFlowPanel();
            jPeople.applyComponentOrientation(getComponentOrientation());

            java.util.List people = m_dlSystem.listPeopleVisible();

            for (int i = 0; i < people.size(); i++) {

                AppUser user = (AppUser) people.get(i);

                JButton btn = new JButton(new AppUserAction(user));

                btn.applyComponentOrientation(getComponentOrientation());
                btn.setFocusPainted(false);

                btn.setFocusable(false);
                btn.setRequestFocusEnabled(false);
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setMaximumSize(new Dimension(80, 60));
                btn.setPreferredSize(new Dimension(120, 60));
                btn.setMinimumSize(new Dimension(80, 60));

                jPeople.add(btn);
            }
            jScrollUsers.getViewport().setView(jPeople);

        } catch (BasicException ee) {
            ee.printStackTrace();
        }
    }

    public int getCloseCashsequence() {
        String sActiveCashIndex = m_propsdb.getProperty("activecash");
        Object[] valcash = null;
        try {
            valcash = sActiveCashIndex == null ? null : m_dlSystem.findActiveCash(sActiveCashIndex);
        } catch (BasicException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (valcash != null) {
            int acthostsequence = ((Integer) valcash[1]).intValue();
            Date actualdate = (Date) valcash[2];
            int day = actualdate.getDay();
            int now = (new Date()).getDay();

            if (day == now) {
                return acthostsequence + 1;
            } else {
                return (new Integer(1)).intValue();
            }
        } else {
            return (new Integer(1)).intValue();
        }
    }

    // La accion del selector
    private class AppUserAction extends AbstractAction {

        private AppUser m_actionuser;

        public AppUserAction(AppUser user) {
            m_actionuser = user;
            // putValue(Action.SMALL_ICON, m_actionuser.getIcon());
            putValue(Action.NAME, m_actionuser.getName());
        }

        public AppUser getUser() {
            return m_actionuser;
        }

        public void actionPerformed(ActionEvent evt) {

            if (m_actionuser.authenticate()) {
                // p'adentro directo, no tiene password        
                openAppView(m_actionuser);
            } else {
                m_jTxtUserName.setText(m_actionuser.getName());
                m_jTxtPassword.requestFocus(true);
                m_jTxtCardNo.setText("");
                // comprobemos la clave antes de entrar...


            }
        }
    }

    public void showView(String view) {
        CardLayout cl = (CardLayout) (m_jPanelContainer.getLayout());
        cl.show(m_jPanelContainer, view);
        m_jTxtUserName.setEnabled(true);
        m_jTxtUserName.setFocusable(true);


    }

    public void openAppView(AppUser user) {

        int floatCount = 0;
        String posNo = m_props.getPosNo();
        if (closeAppView()) {

            m_principalapp = new JPrincipalApp(this, user);
            try {

                m_dlSystem.peoplelogInsert(UUID.randomUUID().toString(), m_principalapp.getUser().getId(), now(), "Login", posNo);

            } catch (BasicException ex) {

                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<FloatCashSetupinfo> floatCashInfo = null;
            List<PettyCashSetupinfo> pettyCashInfo = null;

            if (m_principalapp.getUser().getId() != "0") {
                try {
                    floatCashInfo = (List<FloatCashSetupinfo>) m_dlPOReceipts.getFloatCashSetupDetails();
                } catch (BasicException ex) {
                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (floatCashInfo.size() != 0) {
                    try {
                        floatCount = m_dlPOReceipts.getFloatCount(posNo);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if ((floatCount == 0 && floatCashInfo.get(0).getAutoFloat().equals("Y"))) {

                        try {
                            try {
                                m_dlPOReceipts.insertFloatAmt(floatCashInfo.get(0).getFloatCash(), posNo);
                            } catch (ParseException ex) {
                                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (BasicException ex) {
                            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                SimpleDateFormat month = new SimpleDateFormat("MM");
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                SimpleDateFormat date = new SimpleDateFormat("dd");
                SimpleDateFormat day = new SimpleDateFormat("MM");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date sysDate = new Date();
                String CurrentMonth = month.format(new Date()).toString();
                String currentYear = year.format(new Date()).toString();
                String currentDate = date.format(new Date()).toString();
                String pettyCashDate = format.format(sysDate).toString();
                Date pettyDate = null;
                try {
                    pettyDate = format.parse(pettyCashDate);
                } catch (ParseException ex) {
                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                Calendar calendar = Calendar.getInstance();
                String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
                int pettyCashCount = 0;
                try {
                    pettyCashInfo = (List<PettyCashSetupinfo>) m_dlPOReceipts.getPettyCashSetupDetails();
                } catch (BasicException ex) {
                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (pettyCashInfo.size() != 0) {
                    try {
                        pettyCashCount = m_dlPOReceipts.getPettyCashCount();
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (pettyCashInfo.get(0).getResetPeriod().equals("M")) {
                        String resetDate = pettyCashInfo.get(0).getResetDate();
                        if (resetDate.equals(currentDate) && pettyCashCount == 0) {
                            try {
                                m_dlPOReceipts.insertPettyCash("Reset", " ", pettyCashInfo.get(0).getPettyCash(), 0, pettyCashInfo.get(0).getPettyCash(), sysDate, m_principalapp.getUser().getId(), pettyDate);
                            } catch (BasicException ex) {
                                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else {
                        String resetDay = pettyCashInfo.get(0).getResetDay();
                        if (dayOfWeek.equals(resetDay) && pettyCashCount == 0) {
                            try {
                                m_dlPOReceipts.insertPettyCash("Reset", " ", pettyCashInfo.get(0).getPettyCash(), 0, pettyCashInfo.get(0).getPettyCash(), sysDate, m_principalapp.getUser().getId(), pettyDate);
                            } catch (BasicException ex) {
                                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                // }
            }

            // The main panel
            m_jPanelContainer.add(m_principalapp, "_" + m_principalapp.getUser().getId());
            showView("_" + m_principalapp.getUser().getId());

            m_principalapp.activate();
        }
    }

    public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }

    public boolean closeAppView() {

        if (m_principalapp == null) {

            return true;
        } else if (!m_principalapp.deactivate()) {
            m_jTxtUserName.setFocusable(true);
            return false;
        } else {

            m_jPanelContainer.remove(m_principalapp);

            showView("Login");
            showLogin();
            return true;


        }
    }

    public void closeAppView(boolean val) {


        if (m_principalapp == null) {
            //return true;
        } else if (!m_principalapp.deactivate()) {
            m_jTxtUserName.setFocusable(true);
            //return false;
        } else {

            m_jPanelContainer.remove(m_principalapp);

            showView("Login");
            showLogin();


        }
    }

    public void showLogin() {

        String args[] = null;
        AppConfig config = new AppConfig(args);
        config.load();
        String dburl = config.getProperty("db.URL");
        dburl = dburl.substring(13);
        m_jTxtDBName.setText(dburl);
        m_jTxtDBName.setHorizontalAlignment(JTextField.CENTER);
        m_jTxtCardNo.requestFocusInWindow();
        jLicensePanel.setVisible(false);
        disableLicense();
        jScrollUsers.setVisible(false); //display all usernames at login screen in table
        m_jLoginPanel.setVisible(true);
        m_jTxtLicenseKey.setText("");
        m_jTxtEmailid.setText("");
        m_jLblUserName.setVisible(true);
        m_jLblPassword.setVisible(true);
        jLblUserNameIsNull.setVisible(true);
        m_jTxtUserName.setVisible(true);
        m_jTxtPassword.setVisible(true);
        m_jTxtCardNo.setVisible(true);
        jLblPasswordIsNull.setVisible(true);
        jLblInvalidNamePwd.setVisible(true);
        jLblLicenseValid.setVisible(true);


        listPeople();

        printerStart();

        // keyboard listener activation
        inputtext = new StringBuffer();

        try {
            m_jTxtUserName.setFocusable(true);
        } catch (Exception e) {
        }
    }

    public void disableLicense() {
        jLabel4.setVisible(false);
        jLabel3.setVisible(false);
        jLabel2.setVisible(false);
        m_jLbl.setVisible(false);
        m_jTxtLicenseKey.setVisible(false);
        m_jTxtEmailid.setVisible(false);
        m_jCboStrPurpose.setVisible(false);
        m_jRequest.setVisible(false);
        m_jBtnCancel1.setVisible(false);
        m_jCboStrPurpose.setEnabled(false);

    }

    private void processKey(char c) {

        if (c == '\n') {

            AppUser user = null;
            try {
                user = m_dlSystem.findPeopleByCard(inputtext.toString());
            } catch (BasicException e) {
                e.printStackTrace();
            }

            if (user == null) {
                // user not found
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocard"));
                msg.show(this);
            } else {
                openAppView(user);
            }

            inputtext = new StringBuffer();
        } else {
            inputtext.append(c);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanelContainer = new javax.swing.JPanel();
        m_jPanelLogin = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jLoginPanel = new javax.swing.JPanel();
        jLblPasswordIsNull = new javax.swing.JLabel();
        jPanel1 = new RoundedPanel();
        jLabel6 = new javax.swing.JLabel();
        m_jTxtCardNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jTxtUserName = new javax.swing.JTextField();
        m_jTxtPassword = new javax.swing.JPasswordField();
        m_jBtnLogin = new javax.swing.JButton();
        m_jBtnCancel = new javax.swing.JButton();
        m_jLblPassword = new javax.swing.JLabel();
        m_jLblUserName = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLblUserNameIsNull = new javax.swing.JLabel();
        jLicensePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jTxtLicenseKey = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        m_jTxtEmailid = new javax.swing.JTextField();
        m_jLbl = new javax.swing.JLabel();
        m_jCboStrPurpose = new javax.swing.JComboBox();
        m_jRequest = new javax.swing.JButton();
        m_jBtnCancel1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollUsers = new javax.swing.JScrollPane();
        jLblInvalidNamePwd = new javax.swing.JLabel();
        jLblLicenseValid = new javax.swing.JLabel();
        m_jTxtDBName = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1024, 768));
        setLayout(new java.awt.BorderLayout());

        m_jPanelContainer.setLayout(new java.awt.CardLayout());

        m_jPanelLogin.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(222, 232, 231));
        jPanel4.setMinimumSize(new java.awt.Dimension(608, 100));
        jPanel4.setPreferredSize(new java.awt.Dimension(608, 400));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jLoginPanel.setBackground(new java.awt.Color(222, 232, 231));
        m_jLoginPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                m_jLoginPanelMouseMoved(evt);
            }
        });

        jLblPasswordIsNull.setForeground(new java.awt.Color(255, 51, 51));
        jLblPasswordIsNull.setFocusable(false);

        jPanel1.setBackground(new java.awt.Color(255, 251, 252));
        jPanel1.setPreferredSize(new java.awt.Dimension(365, 455));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 240, 240));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/swipe-card.png"))); // NOI18N
        jLabel6.setOpaque(true);

        m_jTxtCardNo.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        m_jTxtCardNo.setForeground(new java.awt.Color(153, 153, 153));
        m_jTxtCardNo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        m_jTxtCardNo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createMatteBorder(0, 10, 0, 0, new java.awt.Color(255, 255, 255))));
        m_jTxtCardNo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        m_jTxtCardNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CardMouseAction(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                CardMousePressed(evt);
            }
        });
        m_jTxtCardNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtCardNoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(247, 242, 242));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/login-details.png"))); // NOI18N
        jLabel5.setOpaque(true);

        m_jTxtUserName.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        m_jTxtUserName.setForeground(new java.awt.Color(153, 153, 153));
        m_jTxtUserName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        m_jTxtUserName.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createMatteBorder(0, 10, 0, 0, new java.awt.Color(255, 255, 255))));
        m_jTxtUserName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UserMouseListener(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                UNameMousePressed(evt);
            }
        });
        m_jTxtUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtUserNameActionPerformed(evt);
            }
        });

        m_jTxtPassword.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        m_jTxtPassword.setForeground(new java.awt.Color(153, 153, 153));
        m_jTxtPassword.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        m_jTxtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createMatteBorder(0, 10, 0, 0, new java.awt.Color(255, 255, 255))));
        m_jTxtPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PasswordMouseAction(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PasswordMousePressed(evt);
            }
        });
        m_jTxtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTxtPasswordActionPerformed(evt);
            }
        });

        m_jBtnLogin.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        m_jBtnLogin.setForeground(new java.awt.Color(249, 244, 244));
        m_jBtnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/login.png"))); // NOI18N
        m_jBtnLogin.setMnemonic(KeyEvent.VK_ENTER);
        m_jBtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnLoginActionPerformed(evt);
            }
        });
        m_jBtnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_jBtnLoginKeyPressed(evt);
            }
        });

        m_jBtnCancel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        m_jBtnCancel.setForeground(new java.awt.Color(251, 244, 244));
        m_jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/cancel.png"))); // NOI18N
        m_jBtnCancel.setFocusPainted(false);
        m_jBtnCancel.setFocusable(false);
        m_jBtnCancel.setMaximumSize(new java.awt.Dimension(71, 25));
        m_jBtnCancel.setMinimumSize(new java.awt.Dimension(71, 25));
        m_jBtnCancel.setPreferredSize(new java.awt.Dimension(83, 25));
        m_jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancelActionPerformed(evt);
            }
        });

        m_jLblPassword.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        m_jLblPassword.setForeground(new java.awt.Color(74, 93, 107));
        m_jLblPassword.setText("Password "); // NOI18N
        m_jLblPassword.setFocusable(false);

        m_jLblUserName.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        m_jLblUserName.setForeground(new java.awt.Color(74, 93, 107));
        m_jLblUserName.setText("Username"); // NOI18N
        m_jLblUserName.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(74, 93, 107));
        jLabel7.setText("Card No ");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/pos_habanero.png"))); // NOI18N
        jLabel1.setAlignmentX(0.5F);
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setMaximumSize(new java.awt.Dimension(800, 1024));
        jLabel1.setPreferredSize(new java.awt.Dimension(608, 100));
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/pass.png"))); // NOI18N
        jLabel8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 0, 1, 1)));
        jLabel8.setOpaque(true);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/pos/templates/user.png"))); // NOI18N
        jLabel9.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 0, 1, 1)));
        jLabel9.setOpaque(true);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jTxtCardNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(m_jTxtUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 270, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(m_jBtnLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(75, 75, 75)
                        .add(m_jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(261, 261, 261)
                                .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(m_jTxtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 270, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(m_jLblPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(m_jLblUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel5)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel6)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .add(11, 11, 11)
                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(m_jTxtCardNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17)
                .add(jLabel5)
                .add(11, 11, 11)
                .add(m_jLblUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(m_jLblPassword)
                .add(3, 3, 3)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jTxtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jBtnLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLblUserNameIsNull.setForeground(new java.awt.Color(255, 51, 51));
        jLblUserNameIsNull.setFocusable(false);

        org.jdesktop.layout.GroupLayout m_jLoginPanelLayout = new org.jdesktop.layout.GroupLayout(m_jLoginPanel);
        m_jLoginPanel.setLayout(m_jLoginPanelLayout);
        m_jLoginPanelLayout.setHorizontalGroup(
            m_jLoginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jLoginPanelLayout.createSequentialGroup()
                .addContainerGap(343, Short.MAX_VALUE)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(90, 90, 90)
                .add(m_jLoginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLblUserNameIsNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLblPasswordIsNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19))
        );
        m_jLoginPanelLayout.setVerticalGroup(
            m_jLoginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jLoginPanelLayout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .add(jLblUserNameIsNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLblPasswordIsNull, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(78, 78, 78))
            .add(m_jLoginPanelLayout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 95, Short.MAX_VALUE))
        );

        jPanel4.add(m_jLoginPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 1050, 550));

        jLicensePanel.setVerifyInputWhenFocusTarget(false);
        jLicensePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jTxtLicenseKey.setColumns(20);
        m_jTxtLicenseKey.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        m_jTxtLicenseKey.setLineWrap(true);
        m_jTxtLicenseKey.setRows(5);
        m_jTxtLicenseKey.setWrapStyleWord(true);
        m_jTxtLicenseKey.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane1.setViewportView(m_jTxtLicenseKey);

        jLicensePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 190, 90));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Email Id:");
        jLicensePanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 90, 25));

        m_jTxtEmailid.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")));
        jLicensePanel.add(m_jTxtEmailid, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 190, 25));

        m_jLbl.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        m_jLbl.setText("Purpose");
        jLicensePanel.add(m_jLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 160, 90, 25));

        jLicensePanel.add(m_jCboStrPurpose, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 190, 25));

        m_jRequest.setBackground(new java.awt.Color(255, 255, 255));
        m_jRequest.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        m_jRequest.setText("Initiate Activation");
        m_jRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jRequestActionPerformed(evt);
            }
        });
        jLicensePanel.add(m_jRequest, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 160, -1));

        m_jBtnCancel1.setBackground(new java.awt.Color(255, 255, 255));
        m_jBtnCancel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        m_jBtnCancel1.setText("Cancel");
        m_jBtnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnCancel1ActionPerformed(evt);
            }
        });
        jLicensePanel.add(m_jBtnCancel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 200, 90, -1));

        jLabel4.setForeground(new java.awt.Color(255, 51, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLicensePanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 770, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText("License Key");
        jLicensePanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 100, 30));

        jPanel4.add(jLicensePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 1050, 270));

        jScrollUsers.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollUsers.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollUsers.setPreferredSize(new java.awt.Dimension(510, 118));
        jPanel4.add(jScrollUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 690, 800, 50));

        jLblInvalidNamePwd.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLblInvalidNamePwd.setForeground(new java.awt.Color(255, 51, 51));
        jLblInvalidNamePwd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLblInvalidNamePwd.setFocusable(false);
        jLblInvalidNamePwd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel4.add(jLblInvalidNamePwd, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 400, 40));

        jLblLicenseValid.setForeground(new java.awt.Color(255, 51, 51));
        jLblLicenseValid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLblLicenseValid.setFocusable(false);
        jLblLicenseValid.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel4.add(jLblLicenseValid, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 952, 22));

        m_jTxtDBName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jPanel4.add(m_jTxtDBName, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 290, 30));

        m_jPanelLogin.add(jPanel4, java.awt.BorderLayout.CENTER);

        m_jPanelContainer.add(m_jPanelLogin, "login");

        add(m_jPanelContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    //Used for validating the card, username,password text fields

    private void validateFunction() {
        String expression = "";
        String TxtUserName = m_jTxtUserName.getText();
        String TxtPassword = m_jTxtPassword.getText();
        String TxtCard = m_jTxtCardNo.getText();

        AppUser user = null;
        String dbUserName = null;
        String dbPasssword;

        if (TxtUserName.equals("") && TxtPassword.equals("") && TxtCard.equals("")) {
            jLblUserNameIsNull.setText("");
            jLblInvalidNamePwd.setText("");
            jLblInvalidNamePwd.setText("Please Enter User Name and Password Or Card No.");

        } else if (!TxtUserName.equals("") && TxtPassword.equals("")) {
            jLblUserNameIsNull.setText("");
            jLblInvalidNamePwd.setText("");
            jLblInvalidNamePwd.setText("Please Enter Password");
        } else if (TxtUserName.equals("") && !TxtPassword.equals("")) {
            jLblPasswordIsNull.setText("");
            jLblInvalidNamePwd.setText("");
            jLblInvalidNamePwd.setText("Please Enter User Name");
            m_jTxtPassword.setText("");

        } else if (!TxtCard.equals("")) {

            try {

                user = (AppUser) m_dlSystem.findPeopleByCard(TxtCard);

            } catch (BasicException ex) {
                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user == null) {
                // user not found
                jLblInvalidNamePwd.setText("Invalid Card No.");
                m_jTxtCardNo.setText("");
            } else {
                openAppView(user);
                m_jTxtCardNo.setText("");
            }

        } else {

            try {
                user = (AppUser) m_dlSystem.findPeopleByName(TxtUserName);
                if (user != null) {

                    dbUserName = user.getName();
                    dbPasssword = user.getPassword();
                } else {
                    dbUserName = null;
                    dbPasssword = null;
                }

                if (TxtUserName.trim().length() != TxtUserName.length()) {
                    jLblInvalidNamePwd.setText("Please Enter User Name Without Space");
                    m_jTxtUserName.setFocusable(true);
                    m_jTxtUserName.setText("");
                    m_jTxtPassword.setText("");
                    jLblPasswordIsNull.setText("");
                    jLblUserNameIsNull.setText("");
                } else {
                    if (user == null) {
                        jLblInvalidNamePwd.setText("Invalid User Name/Password");
                        m_jTxtUserName.setFocusable(true);
                        m_jTxtUserName.setText("");
                        m_jTxtPassword.setText("");
                        jLblUserNameIsNull.setText("");
                        jLblPasswordIsNull.setText("");
                    } else {
                        if (TxtUserName.equals(dbUserName) && user.authenticate(TxtPassword)) {

                            openAppView(user);
                            jLblInvalidNamePwd.setText("");
                            m_jTxtUserName.setFocusable(true);
                            m_jTxtUserName.setText("");
                            m_jTxtPassword.setText("");
                            jLblPasswordIsNull.setText("");
                            jLblUserNameIsNull.setText("");


                        } else if (TxtUserName.equals(dbUserName) && !user.authenticate(TxtPassword)) {

                            jLblInvalidNamePwd.setText("Invalid Password");
                            m_jTxtPassword.setFocusable(true);
                            jLblUserNameIsNull.setText("");
                            jLblPasswordIsNull.setText("");
                            m_jTxtPassword.setText("");
                        } else {
                            jLblUserNameIsNull.setText("");
                            jLblPasswordIsNull.setText("");
                            jLblInvalidNamePwd.setText("Invalid User Name/Password");
                            m_jTxtUserName.setFocusable(true);
                            m_jTxtUserName.setText("");
                            m_jTxtPassword.setText("");

                        }
                    }
                }

            } catch (Exception ex) {

                jLblUserNameIsNull.setText("");
                jLblPasswordIsNull.setText("");
                jLblInvalidNamePwd.setText("Invalid User Name/Password");
                m_jTxtUserName.setText("");
                m_jTxtPassword.setText("");
                m_jTxtCardNo.setText("");
                ex.printStackTrace();
                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public Date getActivationToDate() {
        Date Currentdate = null;
        Calendar now = Calendar.getInstance();
        int days = Integer.parseInt(lc.getDuration());
        String activationToDate;
        now.add(Calendar.DATE, days);
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + (now.get(Calendar.YEAR));
        try {
            Currentdate = (Date) formatter.parse(str_date);

        } catch (ParseException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Currentdate;

    }
    private void m_jRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jRequestActionPerformed
        String licenseKey = m_jTxtLicenseKey.getText();
        Pattern pEmail = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
        if (licenseKey.length() != 160) {
            JOptionPane.showMessageDialog(this, "Please enter the valid license key");
        } else if (!pEmail.matcher(m_jTxtEmailid.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter the valid e-mail");
        } else {
            String callType;
            if (m_jRequest.getText().equals("Initiate Activation")) {
                callType = "P";
            } else if (m_jRequest.getText().equals("Activation")) {
                callType = "A";
            } else {
                callType = "R";
            }
            callWebService(callType);
            if (lc.getStatus().equals("N")) {
                jLabel4.setText("The license key provided is not valid, Kindly email license@sysfore.com for any queries");
            } else {
                // if(lc.getActivated().equals("Y")){
                String id = UUID.randomUUID().toString();

                //Date sysDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date activationToDate = null;
                Date activationFromDate = null;
                try {
                    activationToDate = sdf.parse(lc.getActivateTo());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(LicenceInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (lc.getStrPurposeValue().equals("E")) {
                    activationFromDate = new Date();
                } else {
                    try {
                        activationFromDate = sdf.parse(lc.getActivateFrom());
                    } catch (ParseException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                String currentDate = LicenceManagementUtil.encrypt(sdf.format(activationFromDate));
                String activationTo = LicenceManagementUtil.encrypt(sdf.format(activationToDate));

                String licenseTypeId = LicenceManagementUtil.encrypt(lc.getUsageType());
                String Active = LicenceManagementUtil.encrypt(lc.getActivated());
                String address = new GetMACAddress().getAddress();

                String macId = LicenceManagementUtil.encrypt(address);
                String strPurpose;
                if (m_jCboStrPurpose.getSelectedItem() == "Evaluation") {
                    strPurpose = "E";
                } else if (m_jCboStrPurpose.getSelectedItem() == "Testing") {
                    strPurpose = "T";
                } else {
                    strPurpose = "P";
                }
                strPurpose = LicenceManagementUtil.encrypt(lc.getStrPurposeValue());
                callType = LicenceManagementUtil.encrypt(lc.getCallType());
                String email = LicenceManagementUtil.encrypt(m_jTxtEmailid.getText());
                String info10 = LicenceManagementUtil.encrypt(UUID.randomUUID().toString());

                try {
                    license = m_dlLicense.getLicenseDetails();
                } catch (BasicException ex) {
                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                address = new GetMACAddress().getCurrenetAddress();
                for (int i = 0; i < license.size(); i++) {
                    String macAddress = LicenceManagementUtil.decrypt(license.get(i).getMachineAddress());
                    StringTokenizer st = new StringTokenizer(macAddress, "|");
                    while (st.hasMoreTokens()) {
                        String macCaptured = st.nextToken();
                        if (macCaptured.equalsIgnoreCase(address)) {
                            matchedrow = i;
                            macAdd = true;
                            break;
                        }
                    }
                }

                if (macAdd == false) {
                    licenseCount = 0;
                } else {
                    licenseCount = 1;
                }

                if (licenseCount == 0) {
                    try {
                        m_dlLicense.insertLicenseKey(id, licenseKey, currentDate, activationTo, licenseTypeId, Active, macId, strPurpose, callType, email, info10);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    showFields();
                } else {
                    try {

                        m_dlLicense.updateMultiSysLicense(id, licenseKey, currentDate, activationTo, licenseTypeId, Active, macId, strPurpose, callType, email, info10, license.get(matchedrow).getMachineAddress());
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    showFields();
                }
            }
        }

}//GEN-LAST:event_m_jRequestActionPerformed
    public void showFields() {
        try {
            license = m_dlLicense.getLicenseDetails();
        } catch (BasicException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        address = new GetMACAddress().getCurrenetAddress();
        for (int i = 0; i < license.size(); i++) {
            String macAddress = LicenceManagementUtil.decrypt(license.get(i).getMachineAddress());
            StringTokenizer st = new StringTokenizer(macAddress, "|");
            while (st.hasMoreTokens()) {
                String macCaptured = st.nextToken();
                if (macCaptured.equalsIgnoreCase(address)) {
                    matchedrow = i;
                    macAdd = true;
                    break;
                }
            }
        }
        int licenseExpCount = 0;
        String purpose = "";
        String activated = "";
        if (macAdd == true) {
            String validFrom = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivateFrom());
            String validTo = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivateTo());
            purpose = LicenceManagementUtil.decrypt(license.get(matchedrow).getStrPurposeValue());
            try {
                licenseExpCount = m_dlLicense.getMultiExpiryLicenseCount(validFrom, validTo, license.get(matchedrow).getMachineAddress());
            } catch (BasicException ex) {
                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (licenseExpCount == 0) {
            showLicense();
            if (macAdd == true) {
                activated = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivated());
            }
            if (!purpose.equals("E") && activated.equals("N")) {
                showLicense();
                m_jRequest.setText("Activate");
                m_jTxtLicenseKey.setText("");
                m_jTxtEmailid.setText("");

            } else if (!purpose.equals("E") && activated.equals("Y")) {
                m_jRequest.setText("Renew");
                jLabel4.setText("Your license is expired. Kindly request for a new license for continuing to use all the modules.");

            }
            //   }
        } else {
            if (purpose.equals("E")) {
                showLogin();
            } else {
                if (macAdd == true) {
                    activated = LicenceManagementUtil.decrypt(license.get(matchedrow).getActivated());
                }
                if (!purpose.equals("E") && activated.equals("N")) {
                    showLicense();
                    m_jTxtLicenseKey.setText("");
                    m_jTxtEmailid.setText("");
                    m_jRequest.setText("Activate");
                    jLabel4.setText("Your license is not activated yet, kindly email license@sysfore.com for any queries. ");
                } else if (!purpose.equals("E") && activated.equals("Y")) {
                    showLogin();
                    m_jRequest.setText("Renew");
                }
            }
        }
    }
    //Method is used for closing the day automatically

    public void closeDay() {
        //Reading and assigning the close day time to variable from properties file
        String closeTime = getProperties().getProperty("machine.closetime");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH");
        Date sysDate = new Date();
        //Reading and assigning the current close cash and close day id to the variables from resource properties file
        String sActiveCashIndex = m_propsdb.getProperty("activecash");
        String sActiveDayIndex = m_propsdb.getProperty("activeday");
        String currentDate = format.format(sysDate).toString() + " " + closeTime;
        int closeDayTime = Integer.parseInt(closeTime.substring(0, 2));
        int systemTime = Integer.parseInt(hoursFormat.format(sysDate).toString());
        int closeDayCount = 0;
        //If system time is greater than close time, Then try to close the previous day and open the day with system date
        if (closeDayTime <= systemTime) {
            try {//Checking whether the previous day is open
                closeDayCount = m_dlSales.getCloseDayCount(currentDate, m_props.getHost());
            } catch (BasicException ex) {
                Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            //If count is greater than 1, then close the previous day by updating the end date and setting new day.
            if (closeDayCount >= 1) {

                try {

                    if (getActiveCashDateEnd() == null) {
                        new StaticSentence(getSession(), "UPDATE CLOSEDCASH SET DATEEND = ? WHERE  MONEY = ? ", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{new Date(), sActiveCashIndex});
                    }
                    if (getActiveDayDateEnd() == null) {
                        new StaticSentence(getSession(), "UPDATE CLOSEDDAY SET DATEEND = ? WHERE  MONEY = ? ", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING})).exec(new Object[]{new Date(), sActiveDayIndex});
                    }
                    //Setting the active cash and active day
                    setActiveCash(UUID.randomUUID().toString(), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                    setActiveDay(UUID.randomUUID().toString(), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                } catch (BasicException e) {
                    MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotcloseday"), e);
                    msg.show(this);
                }

            } else {//If count less than 1, then checking whether any close day entry is there in db table with start date is today's date and end date is null
                int currentCloseDayCount = 0;
                try {
                    currentCloseDayCount = m_dlSales.getCurrentCloseDayCount(format.format(sysDate).toString(), m_props.getHost());
                } catch (BasicException ex) {
                    Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (currentCloseDayCount != 0) {
                    try {
                        //Setting the active cash and active day
                        setActiveCash(m_dlSales.getCurrentCloseCash(format.format(sysDate).toString(), m_props.getHost()), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                        setActiveDay(m_dlSales.getCurrentCloseDay(format.format(sysDate).toString(), m_props.getHost()), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        //Setting the active cash and active day
                        setActiveCash(UUID.randomUUID().toString(), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                        setActiveDay(UUID.randomUUID().toString(), m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);
                    } catch (BasicException ex) {
                        Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }


            }
            try {
                //Below code is for inserting close cash and close day
                sActiveCashIndex = m_propsdb.getProperty("activecash");

                Object[] valcash = sActiveCashIndex == null
                        ? null
                        : m_dlSystem.findActiveCash(sActiveCashIndex);
                if (valcash == null || !m_props.getHost().equals(valcash[0])) {
                    setActiveCash(sActiveCashIndex, m_dlSystem.getSequenceCash(m_props.getHost()) + 1, new Date(), null);

                    m_dlSystem.execInsertCash(
                            new Object[]{getActiveCashIndex(), m_props.getHost(), getActiveCashSequence(), m_props.getPosNo(), getActiveCashDateStart(), getActiveCashDateEnd()});
                } else {
                    setActiveCash(sActiveCashIndex, (Integer) valcash[1], (Date) valcash[2], (Date) valcash[3]);
                }
            } catch (BasicException e) {

                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
                session.close();

            }
            sActiveDayIndex = m_propsdb.getProperty("activeday");
            try {

                Object[] valcash = sActiveDayIndex == null
                        ? null
                        : m_dlSystem.findActiveDay(sActiveDayIndex);
                if (valcash == null || !m_props.getHost().equals(valcash[0])) {
                    // no la encuentro o no es de mi host por tanto creo una...
                    setActiveDay(sActiveDayIndex, m_dlSystem.getSequenceDay(m_props.getHost()) + 1, new Date(), null);

                    // creamos la caja activa
                    m_dlSystem.execInsertDay(
                            new Object[]{getActiveDayIndex(), m_props.getHost(), getActiveDaySequence(), m_props.getPosNo(), getActiveDayDateStart(), getActiveDayDateEnd()});
                } else {
                    setActiveDay(sActiveDayIndex, (Integer) valcash[1], (Date) valcash[2], (Date) valcash[3]);
                }
            } catch (BasicException e) {
                // Casco. Sin caja no hay pos
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
                session.close();
                //  return false;
            }
        }
    }

    private void m_jBtnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancel1ActionPerformed
        tryToClose();        // TODO add your handling code here:

}//GEN-LAST:event_m_jBtnCancel1ActionPerformed

    private void m_jLoginPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_jLoginPanelMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jLoginPanelMouseMoved

    private void m_jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnCancelActionPerformed
        tryToClose();        // TODO add your handling code here:
    }//GEN-LAST:event_m_jBtnCancelActionPerformed

    private void m_jBtnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jBtnLoginKeyPressed
        logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        validateFunction();        // TODO add your handling code here:
        logger.info("End Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnLoginKeyPressed

    private void m_jBtnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnLoginActionPerformed
        logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        validateFunction();        // TODO add your handling code here:
        logger.info("End Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jBtnLoginActionPerformed

    private void m_jTxtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtPasswordActionPerformed
        logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        validateFunction();        // TODO add your handling code here:
        logger.info("End Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));          // TODO add your handling code here:
    }//GEN-LAST:event_m_jTxtPasswordActionPerformed

    private void PasswordMouseAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PasswordMouseAction
        m_jTxtCardNo.setText("");
    }//GEN-LAST:event_PasswordMouseAction

    private void UserMouseListener(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserMouseListener
        m_jTxtCardNo.setText("");
    }//GEN-LAST:event_UserMouseListener

    private void m_jTxtCardNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtCardNoActionPerformed
        logger.info("Start Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
        validateFunction();        // TODO add your handling code here:
        logger.info("End Login Action :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
    }//GEN-LAST:event_m_jTxtCardNoActionPerformed

    private void CardMouseAction(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CardMouseAction
        m_jTxtUserName.setText("");
        m_jTxtPassword.setText("");
    }//GEN-LAST:event_CardMouseAction

    private void m_jTxtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTxtUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jTxtUserNameActionPerformed

    private void CardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CardMousePressed
        m_jTxtUserName.setText("");
        m_jTxtPassword.setText("");
    }//GEN-LAST:event_CardMousePressed

    private void UNameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UNameMousePressed
        m_jTxtCardNo.setText("");
    }//GEN-LAST:event_UNameMousePressed

    private void PasswordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PasswordMousePressed
        m_jTxtCardNo.setText("");
    }//GEN-LAST:event_PasswordMousePressed

    private String getMacAddress() {
        StringBuilder sb = new StringBuilder();
        List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            Collections.sort(interfaces, new Comparator<NetworkInterface>() {

                @Override
                public int compare(NetworkInterface o1, NetworkInterface o2) {
                    try {
                        if (o1.isLoopback() && !o2.isLoopback()) {
                            return 1;
                        }
                        if (!o1.isLoopback() && o2.isLoopback()) {
                            return -1;
                        }
                    } catch (SocketException e) {
                        return 0;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (NetworkInterface iface : interfaces) {
                if (iface.getHardwareAddress() != null) {
                    // get the first not null hw address and CRC it
                    byte[] mac = iface.getHardwareAddress();

                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    sb.append("|");

                }
            }

            if (interfaces.isEmpty()) {
                logger.info("Not found mac adress");
            }
            return sb.toString();
            // return "";
        } catch (SocketException e) {
            logger.info("Error getting mac address");
            return "";
        }


    }

    public void callWebService(String callType) {
        JAXBContext jaxbContext = null;
        Unmarshaller unmarshaller = null;
        ClientResponse response;
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());

        String licenseKey = m_jTxtLicenseKey.getText();
        licenseKey = licenseKey.substring(64, 96);
        String stremail = m_jTxtEmailid.getText();
        String strPurpose;
        if (m_jCboStrPurpose.getSelectedItem() == "Evaluation") {
            strPurpose = "E";
        } else if (m_jCboStrPurpose.getSelectedItem() == "Testing") {
            strPurpose = "T";
        } else {
            strPurpose = "P";
        }
        String id = UUID.randomUUID().toString();
        Form form = new Form();
        form.add("id", id);
        form.add("macaddress", getMacAddress());
        form.add("key", licenseKey);
        form.add("csinfo", "");
        form.add("productname", "POS-PROFESSIONAL");
        form.add("callType", callType);//R-Renew,A-Activation,P-Request for process
        form.add("strPurposeValue", strPurpose);//P-Production,T-Testing,E-Evaluation
        form.add("stremail", stremail);
        form.add("noofOrg", "20");
        form.add("contextName", "conLicence1");
        form.add("dbName", "licencedb");
        form.add("activated", "licence");
        form.add("customername", "jj");
        response = service.path("rest").path("todos").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
        String xml = response.getEntity(String.class);

        try {
            jaxbContext = JAXBContext.newInstance(ClientEntity.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            lc = (ClientEntity) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static URI getBaseURI() {

        //Added new logic to fetch license address from config file
        String args[] = null;
        AppConfig config = new AppConfig(args);
        config.load();
        String url = config.getProperty("machine.license");
        String myurl = url;

        HttpURLConnection con = null;

        try {
            con = (HttpURLConnection) new URL(myurl).openConnection();
        } catch (IOException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.setInstanceFollowRedirects(false);
        try {
            con.connect();
        } catch (IOException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            InputStream is = (InputStream) con.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (con.getResponseCode() == 301) {
                myurl = con.getHeaderField("Location");
            }
        } catch (IOException ex) {
            Logger.getLogger(JRootApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return UriBuilder.fromUri(myurl).build();


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLblInvalidNamePwd;
    private javax.swing.JLabel jLblLicenseValid;
    private javax.swing.JLabel jLblPasswordIsNull;
    private javax.swing.JLabel jLblUserNameIsNull;
    private javax.swing.JPanel jLicensePanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollUsers;
    private javax.swing.JButton m_jBtnCancel;
    private javax.swing.JButton m_jBtnCancel1;
    private javax.swing.JButton m_jBtnLogin;
    private javax.swing.JComboBox m_jCboStrPurpose;
    private javax.swing.JLabel m_jLbl;
    private javax.swing.JLabel m_jLblPassword;
    private javax.swing.JLabel m_jLblUserName;
    private javax.swing.JPanel m_jLoginPanel;
    private javax.swing.JPanel m_jPanelContainer;
    private javax.swing.JPanel m_jPanelLogin;
    private javax.swing.JButton m_jRequest;
    public javax.swing.JTextField m_jTxtCardNo;
    private javax.swing.JLabel m_jTxtDBName;
    private javax.swing.JTextField m_jTxtEmailid;
    private javax.swing.JTextArea m_jTxtLicenseKey;
    private javax.swing.JPasswordField m_jTxtPassword;
    public javax.swing.JTextField m_jTxtUserName;
    // End of variables declaration//GEN-END:variables
}
