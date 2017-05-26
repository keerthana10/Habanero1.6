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

//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Transaction;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.ComboInfo;
import com.openbravo.pos.inventory.KodInfo;
import com.openbravo.pos.inventory.ProductionAreaInfo;
import com.openbravo.pos.inventory.ProductionAreaTypeInfo;
import com.openbravo.pos.inventory.BeanInfo;
import com.openbravo.pos.inventory.SectionMappingInfo;
import com.openbravo.pos.sales.DiscountRateinfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.sysfore.pos.creditsale.ActiveInfo;
import com.sysfore.pos.creditsale.CreditSalePojo;
import com.sysfore.pos.creditsale.CustomerPojo;
import com.sysfore.pos.homedelivery.DeliveryBoyId;
import com.sysfore.pos.homedelivery.DeliveryBoyInfo;
//import com.sysfore.pos.purchaseorder.DeliveryBoyInfo;
import com.sysfore.pos.homedelivery.DeliveryBoyName;
import com.sysfore.pos.homedelivery.HomeDeliveryInfo;
import com.sysfore.pos.hotelmanagement.BusinessTypeInfo;
import com.sysfore.pos.hotelmanagement.PrinterDetailsInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeMapInfo;
import com.sysfore.pos.hotelmanagement.ServiceChargeMappedInfo;
import com.sysfore.pos.hotelmanagement.ServiceTaxInfo;
import com.sysfore.pos.hotelmanagement.ServiceTaxMapInfo;
import com.sysfore.pos.hotelmanagement.ServiceTaxMappedInfo;
import com.sysfore.pos.hotelmanagement.TaxMappingInfo;
import com.sysfore.pos.hotelmanagement.UOMInfo;
import com.sysfore.pos.purchaseorder.PurchasePojo;

import com.sysfore.pos.purchaseorder.VendorInfo;
import com.sysfore.pos.purchaseorder.WarehouseInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.PrinterInfo;

/**
 *
 * @author adrianromero
 */
public class DataLogicCustomers extends BeanFactoryDataSingle {
    
    protected Session s;
    private TableDefinition tcustomers;
    private static Datas[] customerdatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.BOOLEAN, Datas.STRING};
    
    @Override
    public void init(Session s){
        
        this.s = s;
         tcustomers = new TableDefinition(s
            , "CUSTOMERS"
            , new String[] { "ID", "TAXID", "SEARCHKEY", "NAME", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"
                           , "FIRSTNAME", "LASTNAME", "EMAIL", "PHONE", "PHONE2", "FAX"
                           , "ADDRESS", "ADDRESS2", "ISADDRESS1BILLADDRESS", "ISADDRESS2SHIFTADDRESS", "POSTAL", "CITY", "REGION", "COUNTRY"
                           , "TAXCATEGORY","ISCUSTOMER", "BILLADDRESS", "SHIFTADDRESS", "CUSTOMERID","ISCREDITCUSTOMER","ISPOSCUSTOMER", "CUSTOMERRECEIVABLE","CUSTOMERADVANCE" }
            , new String[] { "ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.searchkey"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")
                           , AppLocal.getIntString("label.firstname"), AppLocal.getIntString("label.lastname"), AppLocal.getIntString("label.email"), AppLocal.getIntString("label.phone"), AppLocal.getIntString("label.phone2"), AppLocal.getIntString("label.fax")
                           , AppLocal.getIntString("label.address"), AppLocal.getIntString("label.address2"), "ISBILLADDRESS", "ISSHIPADDRESS",AppLocal.getIntString("label.postal"), AppLocal.getIntString("label.city"), AppLocal.getIntString("label.region"), AppLocal.getIntString("label.country")
                           , "TAXCATEGORY",AppLocal.getIntString("label.iscustomer"), "BILLADDRESS", "SHIFTADDRESS", "CUSTOMERID","ISCREDITCUSTOMER","ISPOSCUSTOMER", "RECEIVABLE","ADVANCE"}
            , new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING,Datas.STRING ,Datas.BOOLEAN, Datas.STRING, Datas.STRING ,Datas.STRING}
            , new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.BOOLEAN, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.STRING,Formats.STRING, Formats.BOOLEAN, Formats.STRING,Formats.STRING,Formats.STRING}
            , new int[] {0}
        );   
    }
    
    // CustomerList list
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, SEARCHKEY, NAME FROM CUSTOMERS WHERE VISIBLE = " + s.DB.TRUE() + " AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"TAXID", "SEARCHKEY", "NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        CustomerInfo c = new CustomerInfo(dr.getString(1));
                        c.setTaxid(dr.getString(2));
                        c.setSearchkey(dr.getString(3));
                        c.setName(dr.getString(4));
                        return c;
                    }                
                });
    }
        public SentenceList getCustomerDetails() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, NAME, CUSTOMERID, PHONE, EMAIL FROM CUSTOMERS WHERE ISCUSTOMER=0 AND VISIBLE = " + s.DB.TRUE() + " AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"NAME", "PHONE", "EMAIL"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        CustomerDetailsInfo c = new CustomerDetailsInfo(dr.getString(1));
                        c.setName(dr.getString(2));
                        c.setCustomerId(dr.getString(3));
                        c.setPhone(dr.getString(4));
                        c.setEmail(dr.getString(5));
                        return c;
                    }
                });
    }
    public int updateCustomerExt(final CustomerInfoExt customer) throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET NOTES = ? WHERE ID = ?"
                , SerializerWriteParams.INSTANCE      
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, customer.getNotes());
                        setString(2, customer.getId());
                }});        
    }
    
    public final SentenceList getReservationsList() {
        return new PreparedSentence(s
            , "SELECT R.ID, R.CREATED, R.DATENEW, C.CUSTOMER, CUSTOMERS.TAXID, CUSTOMERS.SEARCHKEY, COALESCE(CUSTOMERS.NAME, R.TITLE),  R.CHAIRS, R.ISDONE, R.DESCRIPTION " +
              "FROM RESERVATIONS R LEFT OUTER JOIN RESERVATION_CUSTOMERS C ON R.ID = C.ID LEFT OUTER JOIN CUSTOMERS ON C.CUSTOMER = CUSTOMERS.ID " +
              "WHERE R.DATENEW >= ? AND R.DATENEW < ?"
            , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP})
            , new SerializerReadBasic(customerdatas));             
    }
    
    public final SentenceExec getReservationsUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return new PreparedSentence(s
                    , "UPDATE RESERVATIONS SET ID = ?, CREATED = ?, DATENEW = ?, TITLE = ?, CHAIRS = ?, ISDONE = ?, DESCRIPTION = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9, 0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM RESERVATIONS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                int i = new PreparedSentence(s
                    , "INSERT INTO RESERVATIONS (ID, CREATED, DATENEW, TITLE, CHAIRS, ISDONE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9})).exec(params);

                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return i;
            }
        };
    }
     public List<DeliveryBoyInfo> getDeliveryBoyList() throws BasicException {

       String query = "SELECT ID, NAME FROM PEOPLE WHERE ISDELIVERYBOY='Y' ORDER BY NAME" ;
       return (List<DeliveryBoyInfo>) new StaticSentence(s, query, null, new SerializerReadClass(DeliveryBoyInfo.class)).list();

    }
    public final TableDefinition getTableCustomers() {
        return tcustomers;
    }

   public List<VendorInfo> getVendorList() throws BasicException {

       String query = "SELECT ID, SEARCHKEY, TAXID, NAME, ADDRESS, ADDRESS2, ISADDRESS1BILLADDRESS, ISADDRESS2SHIFTADDRESS, BILLADDRESS, SHIFTADDRESS FROM CUSTOMERS WHERE ISCUSTOMER=1 AND VISIBLE=1 ORDER BY NAME" ;
       return (List<VendorInfo>) new StaticSentence(s, query, null, new SerializerReadClass(VendorInfo.class)).list();

    }
   public int getPhoneCount(String phone) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM CUSTOMERS WHERE PHONE ='"+phone+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
    public int getEmailCount(String email) throws BasicException {
        Object[] record = null;
    
      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM CUSTOMERS WHERE EMAIL ='"+email+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
    public List<AccountPojo> getAccountValue() throws BasicException {
        String query = "SELECT CUSTOMERDEBIT,CUSTOMERCREDIT FROM ACCOUNTINGCONFIG" ;
             return (List<AccountPojo>) new StaticSentence(s, query, null, new SerializerReadClass(AccountPojo.class)).list();
       }
   public void insertCustomer(final CustomerInfoExt customer, final String id) throws BasicException {
     
        new PreparedSentence(s,
                "INSERT INTO CUSTOMERS(ID, TAXID, SEARCHKEY, NAME, NOTES, VISIBLE, CARD, MAXDEBT, FIRSTNAME, "
                + "LASTNAME, EMAIL, PHONE, PHONE2, FAX, ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY, "
                + "TAXCATEGORY,CUSTOMERID,ISCUSTOMER,ISCREDITCUSTOMER,CUSTOMERRECEIVABLE,CUSTOMERADVANCE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?,?,?)",
                SerializerWriteParams.INSTANCE).exec(new DataParams() {

            @Override
            public void writeValues() throws BasicException {

                setString(1, id);
                setString(2, customer.getCustomerId());
                setString(3, customer.getCustomerId());
                setString(4, customer.getName());
                setString(5, customer.getNotes());
                setBoolean(6, customer.isVisible());
                setString(7, customer.getCard());
                setDouble(8, customer.getMaxdebt());
                setString(9, customer.getFirstname());
                setString(10, customer.getLastname());
                setString(11, customer.getEmail());
                setString(12, customer.getPhone());
                setString(13, customer.getPhone2());
                setString(14, customer.getFax());
                setString(15, customer.getAddress());
                setString(16, customer.getAddress2());
                setString(17, customer.getPostal());
                setString(18, customer.getCity());
                setString(19, customer.getRegion());
                setString(20, customer.getCountry());
                setString(21, customer.getTaxCustCategoryID());
                setString(22, customer.getCustomerId());
                setBoolean(23, Boolean.valueOf(false));
                setBoolean(24, Boolean.valueOf(false));
                setString(25,customer.getReceivable());
                setString(26,customer.getAdvance());

            }
        });
        int customerid = getCustomerIdCount();
        if(customerid==0){
             new PreparedSentence(s,
                "INSERT INTO CUSTOMERSNUM(CUSTOMERID) VALUES (?)",
                SerializerWriteParams.INSTANCE).exec(new DataParams() {

            @Override
            public void writeValues() throws BasicException {
               setString(1, customer.getCustomerId());
             }
        });
        }else{

        Object[] values = new Object[]{customer.getCustomerId()};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE CUSTOMERSNUM SET CUSTOMERID = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

        }
    }
   
    public void insertSetCustomer(final String id) throws BasicException {
     
       
        int customerid = getCustomerIdCount();
        if(customerid==0){
             new PreparedSentence(s,
                "INSERT INTO CUSTOMERSNUM (CUSTOMERID) VALUES (?)",
                SerializerWriteParams.INSTANCE).exec(new DataParams() {

            @Override
            public void writeValues() throws BasicException {
               setString(1, id);
             }
        });
        }else{
        int cusCount = getSetCusCount(id);
        if(cusCount==0){
            Object[] values = new Object[]{id};
            Datas[] datas = new Datas[]{Datas.STRING};
            new PreparedSentence(s, "UPDATE CUSTOMERSNUM SET CUSTOMERID = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
        }
        }
    }

   public int getCustomerIdCount() throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM CUSTOMERSNUM"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
   public int getSetCusCount(String customerid) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM CUSTOMERS WHERE CUSTOMERID = '"+customerid+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }


public String getRolebyName(String user) throws BasicException {
        if (user == null) {
            return null;
        } else {
            Object[] record = (Object[]) new StaticSentence(s, "SELECT R.NAME AS rolename FROM ROLES R,PEOPLE P WHERE P.ROLE=R.ID AND P.NAME=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(user);
            return record == null ? "" : record[0].toString();
        }
    }
public List<DiscountRateinfo> getDiscountList() throws BasicException {

        return (List<DiscountRateinfo>) new StaticSentence(s, "SELECT ID, RATE, NAME FROM DISCOUNTRATE ", null, new SerializerReadClass(DiscountRateinfo.class)).list();
    }
public List<ServiceChargeInfo> getServiceChargeList() throws BasicException {

        return (List<ServiceChargeInfo>) new StaticSentence(s, "SELECT ID, RATE, NAME, DEBITACCOUNT, CREDITACCOUNT FROM SERVICECHARGE ", null, new SerializerReadClass(ServiceChargeInfo.class)).list();
    }
public List<BusinessTypeInfo> getBusinessTypeList() throws BasicException {

        return (List<BusinessTypeInfo>) new StaticSentence(s, "SELECT ID, NAME FROM BUSINESSTYPE ", null, new SerializerReadClass(BusinessTypeInfo.class)).list();
    }
public List<PrinterDetailsInfo> getPrinterList() throws BasicException {

        return (List<PrinterDetailsInfo>) new StaticSentence(s, "SELECT ID, NAME, PATH FROM PRINTERCONFIG ", null, new SerializerReadClass(PrinterDetailsInfo.class)).list();
    }
public List<ServiceTaxInfo> getServiceTaxList() throws BasicException {

        return (List<ServiceTaxInfo>) new StaticSentence(s, "SELECT ID, NAME FROM TAXES WHERE ISSERVICETAX='Y' ", null, new SerializerReadClass(ServiceTaxInfo.class)).list();
    }
public List<ServiceTaxMappedInfo> getServiceTaxMapList(String businessType) throws BasicException {

        return (List<ServiceTaxMappedInfo>) new StaticSentence(s, "SELECT TAXES.ID, TAXES.NAME FROM TAXES,TAXMAPPING,TAXMAPPINGLINES WHERE TAXMAPPING.ID=TAXMAPPINGLINES.TAXMAPID AND TAXES.ID = TAXMAPPINGLINES.TAXID AND TAXMAPPING.BUSINESSTYPE='"+businessType+"' AND  TAXMAPPINGLINES.ISSERVICETAX='Y'  ", null, new SerializerReadClass(ServiceTaxMappedInfo.class)).list();
    }
public List<ServiceChargeMappedInfo> getServiceChargeMapList(String businessType) throws BasicException {

        return (List<ServiceChargeMappedInfo>) new StaticSentence(s, "SELECT SERVICECHARGE.ID, SERVICECHARGE.NAME FROM SERVICECHARGE,TAXMAPPING,TAXMAPPINGLINES WHERE TAXMAPPING.ID=TAXMAPPINGLINES.TAXMAPID AND SERVICECHARGE.ID = TAXMAPPINGLINES.TAXID AND TAXMAPPING.BUSINESSTYPE='"+businessType+"' AND TAXMAPPINGLINES.ISSERVICETAX='N'  ", null, new SerializerReadClass(ServiceChargeMappedInfo.class)).list();
    }
public java.util.List<CustomerListInfo> getCustomerList2() throws BasicException {

        return (List<CustomerListInfo>) new StaticSentence(s, "SELECT ID, SEARCHKEY, TAXID, NAME, TAXCATEGORY, "
                + "CARD, MAXDEBT, ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY,"
                + "FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX, NOTES, VISIBLE,"
                + "CURDATE, CURDEBT,CUSTOMERRECEIVABLE,CUSTOMERADVANCE FROM CUSTOMERS ", null, new SerializerReadClass(CustomerListInfo.class)).list();

    }
public List<ServiceChargeInfo> getServiceCharge(String serviceName) throws BasicException {

        return (List<ServiceChargeInfo>) new StaticSentence(s, "SELECT ID, RATE, NAME, DEBITACCOUNT, CREDITACCOUNT FROM SERVICECHARGE WHERE NAME='"+serviceName+"' ", null, new SerializerReadClass(ServiceChargeInfo.class)).list();
    }
    public String getDiscountLine(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT RATE FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? null : (String) record[0];
    }
     public void deleteDiscountLine(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
 public void deleteServiceCharge(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM SERVICECHARGE WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
      public void deleteBusinessType(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM BUSINESSTYPE WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
     public void insertDiscounts(String id, String percentage, double discount) throws BasicException {
        System.out.println("inside insertdiscount method");
        Object[] values = new Object[]{id, discount, percentage};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO DISCOUNTRATE (ID, RATE, NAME) VALUES (?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2})).exec(values);

    }
 public void insertServiceCharge(String id, String serviceName, double serviceCharge,String debitAccount, String creditaccount) throws BasicException {
        Object[] values = new Object[]{id, serviceCharge, serviceName,debitAccount,creditaccount};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.DOUBLE, Datas.STRING,Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "INSERT INTO SERVICECHARGE (ID, RATE, NAME,DEBITACCOUNT,CREDITACCOUNT) VALUES (?, ?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4})).exec(values);

    }
      public void insertBusinessType(String id, String name) throws BasicException {
        System.out.println("inside insertdiscount method");
        Object[] values = new Object[]{id, name};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO BUSINESSTYPE (ID, NAME) VALUES (?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);

    }
     //  public void insertTaxMapping(String id, String businessType,String isServiceTax,String isServiceTaxIncluded,String isServiceCharge,String isServiceChargeIncluded) throws BasicException {
        public void insertTaxMapping(String id, String businessType,String isServiceTaxIncluded,String isServiceChargeIncluded) throws BasicException {
        System.out.println("inside insertdiscount method");
        Object[] values = new Object[]{id, businessType,isServiceTaxIncluded,isServiceChargeIncluded};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO TAXMAPPING (ID, BUSINESSTYPE,ISSERVICETAXINCLUDED,ISSERVICECHARGEINCLUDED) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);

    }
         public void insertTaxMappingLines(String taxMapid, String taxId, String serviceTax) throws BasicException {

        Object[] values = new Object[]{UUID.randomUUID().toString(),taxMapid, taxId, serviceTax};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO TAXMAPPINGLINES (ID, TAXMAPID, TAXID, ISSERVICETAX) VALUES (?, ?, ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3})).exec(values);

    }
 public int getTaxCount(String taxid, String id) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM TAXMAPPINGLINES WHERE TAXID = '"+taxid+"' AND TAXMAPID = '"+id+"' AND ISSERVICETAX='Y'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
 public int getChargeCount(String taxid, String id) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM TAXMAPPINGLINES WHERE TAXID = '"+taxid+"' AND TAXMAPID = '"+id+"' AND ISSERVICETAX='N'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
       public void updateTaxMapping(String id, String businessType,String isServiceTaxIncluded,String isServiceChargeIncluded) {
        Object[] values = new Object[]{id, businessType,isServiceTaxIncluded,isServiceChargeIncluded};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        try {
            new PreparedSentence(s, "UPDATE TAXMAPPING SET BUSINESSTYPE= ?, ISSERVICETAXINCLUDED= ?,  ISSERVICECHARGEINCLUDED= ? WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{1, 2, 3, 0})).exec(values);

        } catch (BasicException ex) {
            Logger.getLogger(DataLogicCustomers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void deleteTaxMappingLines(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM TAXMAPPINGLINES WHERE TAXMAPID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
       }
        public void deleteTaxLines(String id, String taxid) throws BasicException {
        Object[] values = new Object[]{id,taxid};
        Datas[] datas = new Datas[]{Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "DELETE FROM TAXMAPPINGLINES WHERE TAXMAPID= ? AND TAXID=?", new SerializerWriteBasicExt(datas, new int[]{0,1})).exec(values);
       }
 public void deleteTaxMapping(String id) throws BasicException {
        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "DELETE FROM TAXMAPPING WHERE ID= ?", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);
       }
        public java.util.List<TaxMappingInfo> getTaxMapping() throws BasicException {

        return (List<TaxMappingInfo>) new StaticSentence(s, "SELECT TAXMAPPING.ID, TAXMAPPING.BUSINESSTYPE,TAXMAPPING.ISSERVICETAXINCLUDED, TAXMAPPING.ISSERVICECHARGEINCLUDED, BUSINESSTYPE.NAME FROM TAXMAPPING,BUSINESSTYPE WHERE TAXMAPPING.BUSINESSTYPE = BUSINESSTYPE.ID  ORDER BY BUSINESSTYPE.NAME ", null, new SerializerReadClass(TaxMappingInfo.class)).list();

    }
        public List<WarehouseInfo> getWarehouseList() throws BasicException {

       String query = "SELECT ID, NAME FROM LOCATIONS" ;
       return (List<WarehouseInfo>) new StaticSentence(s, query, null, new SerializerReadClass(WarehouseInfo.class)).list();

    }
    public String getWarehouseListOnid(String id) throws BasicException {

       Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM LOCATIONS WHERE ID=?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
            return record == null ? "" : record[0].toString();

}
        public void updateDiscountLine(String id, String name, Double discount) throws BasicException {

        Object[] values = new Object[]{id, name, discount};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE};
        new PreparedSentence(s, "UPDATE DISCOUNTRATE SET RATE = ?, NAME = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{2, 1, 0})).exec(values);

    }
        public void updatePrinterConfig(String id, String path) throws BasicException {

        Object[] values = new Object[]{id, path};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE PRINTERCONFIG SET path = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);

    }
 public void updateServiceCharge(String id, String name, Double serviceAmt,String debitAccount, String creditAccount) throws BasicException {

        Object[] values = new Object[]{id, name, serviceAmt,debitAccount,creditAccount};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.DOUBLE,Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "UPDATE SERVICECHARGE SET CREDITACCOUNT=?, DEBITACCOUNT =?, RATE = ?, NAME = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{4, 3, 2, 1, 0})).exec(values);

    }
        public void updateBusinessType(String id, String name) throws BasicException {

        Object[] values = new Object[]{id, name};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE BUSINESSTYPE SET NAME = ?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1, 0})).exec(values);

    }
          public void updateCreditInvoice(String id) throws BasicException {

        Object[] values = new Object[]{id};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE RECEIPTS SET ISCREDITINVOICED = 'Y'  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }
         public String getDiscountName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM DISCOUNTRATE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
       public String getServiceChargeName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM SERVICECHARGE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
 public String getBusinessTypeName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM BUSINESSTYPE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
  public double getCreditAmt(String id) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT CREDITAMOUNT FROM TICKETS WHERE DOCUMENTNO ='"+id+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            double i = Double.parseDouble(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
  }


public ArrayList<DeliveryBoyId>  getDeliveryBoyID(String deliveryBoyName) throws BasicException {

       String query = "select ID from PEOPLE where NAME='"+ deliveryBoyName +"' and ISDELIVERYBOY='Y'" ;
       return (ArrayList<DeliveryBoyId>) new StaticSentence(s, query, null, new SerializerReadClass(DeliveryBoyId.class)).list();

    }
  public int setCreditAllValue(final ActiveInfo status, final String id) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE C, RECEIPTS R SET C.CREDITAMOUNT = ? , "
                  +  "PAYMENTTYPE = ?, "
                  +  "BANKNAME = ?, "
                  +  "CHEQUENO = ?, "
                  +  "CHEQUEDATE = ?, "
                  +  "REMARKS = ?  "
                    + "WHERE R.ID=C.TICKETID AND  C.ID= ? "
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setDouble(1,status.getCredit());
                      setString(2,status.getTendorType());
                      setString(3,status.getBankname());
                      setString(4,status.getChequeno());
                      setTimestamp(5,status.getChequedate());
                      setString(6,status.getRemarks());
                      setString(7,id);
                   
                }});

    }

 public int updateHomeTrackingInfo(final HomeDeliveryInfo  info) throws BasicException {

            return new PreparedSentence(s, "UPDATE TICKETS SET ADVANCEISSUED = ?, DELIVEREDTO = ?, COLLECTEDAMOUNT = ?, ADVANCERETURNED  = ?, DELIVERYBOY =?, DELIVEREDDATE =?, HDCUSTOMERADDRESS=? WHERE DOCUMENTNO=? ", SerializerWriteParams.INSTANCE).exec(new DataParams() {

            public void writeValues() throws BasicException {
                setDouble(1, info.getAdvanceIssued());
                setString(2, info.getDeliveryTo());
                setDouble(3, info.getCollectedAmount());
                setDouble(4, info.getAdvanceReturned());
                setString(5, info.getDeliveryBoy());
                setTimestamp(6, info.getDeliveryDate());
                setString(7, info.getHdCusAddress());
                 setString(8, info.getHomeDeliveryNO());
             }
        });

 }


 public ArrayList getSearchFromDate(String fromDate,String status) throws BasicException
        {
            System.out.println("query is executed");
     String query ="select T.DOCUMENTNO,R.DATENEW,PE.NAME as DeliveryBoy ,T.BILLAMOUNT, T.COLLECTEDAMOUNT,T.ADVANCEISSUED,T.ADVANCERETURNED,T.DELIVERYSTATUS,T.DELIVEREDTO,T.DELIVEREDDATE, T.CREDITAMOUNT,C.NAME,C.ADDRESS,C.ADDRESS2,C.CITY,T.ISCOD,T.ISPAIDSTATUS,T.ID,C.ID,T.HDCUSTOMERADDRESS  from TICKETS T left join RECEIPTS R ON(T.id=R.id)\n" +
            "left join PEOPLE PE ON(PE.ID=T.DELIVERYBOY)\n" +
             "left join CUSTOMERS C ON(C.ID=T.CUSTOMER)\n" +
            "where T.ISHOMEDELIVERY='Y'AND R.DATENEW >='"+ fromDate +"' AND T.DELIVERYSTATUS='"+status+"'" +
            "group by T.DOCUMENTNO";
            System.out.println("query is returned");
     return (ArrayList<HomeDeliveryInfo>) new StaticSentence(s, query, null, new SerializerReadClass(HomeDeliveryInfo.class)).list();

        }

   public ArrayList<HomeDeliveryInfo> getSearchDate1(String fromDate, String toDate, String deliveryBoyName,String status) {
        ArrayList<HomeDeliveryInfo> lines = new ArrayList<HomeDeliveryInfo>();
        String query = "select T.DOCUMENTNO,R.DATENEW,PE.NAME as DeliveryBoy ,T.BILLAMOUNT, T.COLLECTEDAMOUNT,T.ADVANCEISSUED,T.ADVANCERETURNED,T.DELIVERYSTATUS,T.DELIVEREDTO,T.DELIVEREDDATE,T.CREDITAMOUNT,C.NAME,C.ADDRESS,C.ADDRESS2,C.CITY,T.ISCOD,T.ISPAIDSTATUS,T.ID,C.ID,T.HDCUSTOMERADDRESS from TICKETS T left join RECEIPTS R ON(T.id=R.id)\n" +
            "left join PEOPLE PE ON(PE.ID=T.DELIVERYBOY)\n" +
             "left join CUSTOMERS C ON(C.ID=T.CUSTOMER)\n" +
            "where  R.DATENEW >='"+ fromDate +"' AND R.DATENEW<'"+ toDate +"'"+
            "AND T.ISHOMEDELIVERY='Y'"+
            "AND PE.NAME='"+ deliveryBoyName +"' AND T.DELIVERYSTATUS='"+status+"'  "+
            "group by T.DOCUMENTNO";

        try {
            lines = (ArrayList<HomeDeliveryInfo>) new StaticSentence(s, query, null, new SerializerReadClass(HomeDeliveryInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicCustomers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

  public ArrayList<HomeDeliveryInfo> getSearchDate(String fromDate, String toDate, String status) {
        ArrayList<HomeDeliveryInfo> lines = new ArrayList<HomeDeliveryInfo>();
        String query = "select T.DOCUMENTNO,R.DATENEW,PE.NAME as DeliveryBoy ,T.BILLAMOUNT, T.COLLECTEDAMOUNT,T.ADVANCEISSUED,T.ADVANCERETURNED,T.DELIVERYSTATUS,T.DELIVEREDTO,T.DELIVEREDDATE,T.CREDITAMOUNT,C.NAME,C.ADDRESS,C.ADDRESS2,C.CITY, T.ISCOD,T.ISPAIDSTATUS,T.ID,C.ID,T.HDCUSTOMERADDRESS   from TICKETS T left join RECEIPTS R ON(T.id=R.id)\n" +
            "left join PEOPLE PE ON(PE.ID=T.DELIVERYBOY)\n" +
             "left join CUSTOMERS C ON(C.ID=T.CUSTOMER)\n" +
            "where  R.DATENEW >='"+ fromDate +"' AND R.DATENEW<'"+ toDate +"' AND T.DELIVERYSTATUS='"+status+"'"+
            "AND  T.ISHOMEDELIVERY='Y' "+
            "group by T.DOCUMENTNO";

        try {
            lines = (ArrayList<HomeDeliveryInfo>) new StaticSentence(s, query, null, new SerializerReadClass(HomeDeliveryInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicCustomers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

 public ArrayList getSearchFromAndName(String fromDate,String deliveryBoyName,String status) throws BasicException
        {
            System.out.println("query is executed");
     String query ="select T.DOCUMENTNO,R.DATENEW,NAME as DeliveryBoy ,T.BILLAMOUNT, T.COLLECTEDAMOUNT,T.ADVANCEISSUED,T.ADVANCERETURNED,T.DELIVERYSTATUS,T.DELIVEREDTO,T.DELIVEREDDATE,T.CREDITAMOUNT,C.NAME,C.ADDRESS,C.ADDRESS2,C.CITY,T.ISCOD,T.ISPAIDSTATUS,T.ID,C.ID,T.HDCUSTOMERADDRESS   from TICKETS T left join RECEIPTS R ON(T.id=R.id)\n" +
            "left join PEOPLE PE ON(PE.ID=T.DELIVERYBOY)\n" +
             "left join CUSTOMERS C ON(C.ID=T.CUSTOMER)\n" +
            "where R.DATENEW >='"+ fromDate +"'\n" +
             "AND PE.NAME='"+ deliveryBoyName +"' AND T.ISHOMEDELIVERY='Y'  AND T.DELIVERYSTATUS='"+status+"'" +
            "group by DOCUMENTNO";
            System.out.println("query is returned");
     return (ArrayList<HomeDeliveryInfo>) new StaticSentence(s, query, null, new SerializerReadClass(HomeDeliveryInfo.class)).list();

        }

public void deleteUOM(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM UOM WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }

public List<UOMInfo> getUOMList() throws BasicException {

        return (List<UOMInfo>) new StaticSentence(s, "SELECT ID, NAME,CODE FROM UOM ", null, new SerializerReadClass(UOMInfo.class)).list();
    }

public String getUOMname(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM UOM WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }

public void insertUom(String id, String name,String code) throws BasicException {
        Object[] values = new Object[]{id, name,code};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "INSERT INTO UOM (ID, NAME,CODE) VALUES (?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1,2})).exec(values);

    }

public void updateUom(String id, String name,String code) throws BasicException {

        Object[] values = new Object[]{id, name,code};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "UPDATE UOM SET CODE = ?,NAME=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{2,1, 0})).exec(values);

    }

public String getPerticularUom(String name) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT CODE FROM UOM WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(name);
        return record == null ? null : (String) record[0];
    }

public ArrayList<DeliveryBoyName> getDeliveryBoyList1() throws BasicException {

       String query = "SELECT ID,NAME FROM PEOPLE WHERE ISDELIVERYBOY='Y' ORDER BY NAME" ;
       return (ArrayList<DeliveryBoyName>) new StaticSentence(s, query, null, new SerializerReadClass(DeliveryBoyName.class)).list();

    }


public int updateDeliveredStatus(String DoucmentNo, String Status) throws BasicException
         {
             Object[] values = new Object[]{DoucmentNo,Status};
             Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
             return new PreparedSentence(s, "UPDATE TICKETS SET DELIVERYSTATUS = ? WHERE DOCUMENTNO= ?", new SerializerWriteBasicExt(datas, new int[]{1,0})).exec(values);
         }
public int updateHomeCreditStatus(String DoucmentNo, String Status) throws BasicException
         {
             Object[] values = new Object[]{DoucmentNo,Status};
             Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
             return new PreparedSentence(s, "UPDATE TICKETS SET DELIVERYSTATUS = ?, ISCREDITSALE = 'Y' WHERE DOCUMENTNO= ?", new SerializerWriteBasicExt(datas, new int[]{1,0})).exec(values);
         }
public int updateHomeDelivery(String documentNo, String Status) throws BasicException
         {
             Object[] values = new Object[]{documentNo,Status};
             Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
             return new PreparedSentence(s, "UPDATE TICKETS SET DELIVERYSTATUS = ?, CREDITAMOUNT='0.00', ISPAIDSTATUS='Y' WHERE DOCUMENTNO= ?", new SerializerWriteBasicExt(datas, new int[]{1,0})).exec(values);
         }

public ArrayList<HomeDeliveryInfo> getBasedOnDeliveryBoyName(String deliveryBoyName) {
        ArrayList<HomeDeliveryInfo> lines = new ArrayList<HomeDeliveryInfo>();
        String query = "select T.DOCUMENTNO,R.DATENEW,PE.NAME as DeliveryBoy ,T.BILLAMOUNT,T.COLLECTEDAMOUNT,T.ADVANCEISSUED,T.ADVANCERETURNED,T.DELIVERYSTATUS,T.DELIVEREDTO,T.DELIVEREDDATE,T.CREDITAMOUNT,C.NAME,C.ADDRESS,C.ADDRESS2,C.CITY,T.ISCOD,T.ISPAIDSTATUS,T.ID,C.ID,T.HDCUSTOMERADDRESS   from TICKETS T left join RECEIPTS R ON(T.id=R.id)\n" +
            "left join PEOPLE PE ON(PE.ID=T.DELIVERYBOY)\n" +
             "left join CUSTOMERS C ON(C.ID=T.CUSTOMER)\n" +
            "where  PE.NAME='"+ deliveryBoyName +"' "+
            "AND T.ISHOMEDELIVERY='Y' "+
            "group by DOCUMENTNO";

        try {
            lines = (ArrayList<HomeDeliveryInfo>) new StaticSentence(s, query, null, new SerializerReadClass(HomeDeliveryInfo.class)).list();
        } catch (BasicException ex) {
            Logger.getLogger(DataLogicCustomers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
List<AccountPojo> getVendorAccountValue() throws BasicException {
        String query = "SELECT VENDORDEBIT,VENDORCREBIT FROM ACCOUNTINGCONFIG" ;
             return (List<AccountPojo>) new StaticSentence(s, query, null, new SerializerReadClass(AccountPojo.class)).list();
    }

 public List<CreditSalePojo> getCreditSale2List(String id,String billno) throws BasicException {
        String query = "select cs.id,c.name,cs.billno,cs.billdate,cs.billamount,cs.creditamount, " +
                "cs.ticketid " +
                "from creditsale cs left join customers c on cs.customer=c.customerid "+
               "where cs.customer= '"+id+"'" +
                 "and cs.isactive='Y' " +
                "order by cs.billdate";
              return (List<CreditSalePojo>) new StaticSentence(s, query, null, new SerializerReadClass(CreditSalePojo.class)).list();
}
 public List<CustomerPojo> getCustomersList() throws BasicException {

       String query = "SELECT ID, NAME FROM CUSTOMERS WHERE ISCUSTOMER='0' ORDER BY NAME" ;
       return (List<CustomerPojo>) new StaticSentence(s, query, null, new SerializerReadClass(CustomerPojo.class)).list();

    }
     public List<CreditSalePojo> getCreditSaleList(String id,String billno) throws BasicException {
        String query = "select cs.id, c.name,cs.billno,cs.billdate,cs.billamount,cs.creditamount, " +
                "cs.ticketid  " +
                "from creditsale cs left join customers c on cs.customer=c.customerid "+
               "where cs.customer= '"+id+"'" +
                "and (cs.billno='"+billno+"')" +
                "and cs.isactive='Y' " +
                "order by cs.billdate";
              return (List<CreditSalePojo>) new StaticSentence(s, query, null, new SerializerReadClass(CreditSalePojo.class)).list();
       }
       public int setActivevALUE( final ActiveInfo status, final String id) throws BasicException {
      System.out.println("inside query");
      return new PreparedSentence(s
                , "UPDATE CREDITSALE SET ISACTIVE = 'N' "+
            " WHERE ID = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, id);

                }});
    }
           public int setCreditValue(final ActiveInfo status) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET CREDITAMOUNT = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setDouble(1,status.getCredit());
                        setString(2,status.getStatus());

                }});

    }
            public int setTendorValueTOdB(final ActiveInfo tendor) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET PAYMENTTYPE = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setString(1,tendor.getTendorType());
                        setString(2,tendor.getStatus());

                }});


    }

             public int setChequeValueTOdB(final ActiveInfo tendor) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET CHEQUENO = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setString(1,tendor.getChequeno());
                        setString(2,tendor.getStatus());

                }});


    }

               public int setChequeDateValueTOdB(final ActiveInfo tendor) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET CHEQUEDATE = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setTimestamp(1,tendor.getChequedate());
                        setString(2,tendor.getStatus());

                }});


    }

                  public int setBankValueTOdB(final ActiveInfo tendor) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET BANKNAME = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setString(1,tendor.getBankname());
                        setString(2,tendor.getStatus());

                }});


    }
     public List<PurchasePojo>getPurchaseOrder() throws BasicException {

        String query = "select documentnumber,id, " +
                "status " +
                "from purchaseorder "+
               "where status= 'Complete' " +
               "and isinvoice='N'" ;
              return (List<PurchasePojo>) new StaticSentence(s, query, null, new SerializerReadClass(PurchasePojo.class)).list();
       }
public List<PurchasePojo>getGRNno() throws BasicException {

        String query = "SELECT DOCUMENTNUMBER,ID, " +
                "STATUS " +
                "FROM GOODSRECEIPTS "+
               "WHERE STATUS= 'Complete' " +
               "AND ISINVOICE='N' AND ISCLOSED='N' " ;
              return (List<PurchasePojo>) new StaticSentence(s, query, null, new SerializerReadClass(PurchasePojo.class)).list();
       }

    public int setRemarksTOdB(final ActiveInfo tendor) throws BasicException{
            return new PreparedSentence(s
                , "UPDATE CREDITSALE SET REMARKS = ? WHERE BILLNO = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                     setString(1,tendor.getRemarks());
                        setString(2,tendor.getStatus());

                }});


    }
    public void insertConsiliation(String id, String status) throws BasicException {
        System.out.println("inside insert method");
        Object[] values = new Object[]{id, status};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO STOCKRECONCILIATIONSTATUS VALUES ( ?, ?)", new SerializerWriteBasicExt(datas, new int[]{0, 1})).exec(values);

    }

public void updateInventory(String status) throws BasicException {
System.out.println(status);
        Object[] values = new Object[]{status};
        Datas[] datas = new Datas[]{Datas.STRING};
        new PreparedSentence(s, "UPDATE STOCKRECONCILIATIONSTATUS SET ISSTOPINVENTORY = ? ", new SerializerWriteBasicExt(datas, new int[]{0})).exec(values);

    }
public int getConsiliationCount() throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM STOCKRECONCILIATIONSTATUS "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
public String getIsPaidStatus(String documentNo) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT ISPAIDSTATUS FROM TICKETS WHERE DOCUMENTNO='"+documentNo+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            String i = record[0]==null ? "" :record[0].toString();
            return (i ==null ? "" : i);

    }
public SentenceList getAddonProduct(String product) {
         return new StaticSentence(s
            ,
            "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, P.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME ,P.SWACHBHARATTAX,ISCOMBOPRODUCT " +
              "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C ON P.CATEGORY=C.ID WHERE L.LINKEDTO ='" + product + "'   AND P.ISACTIVE='Y' ORDER BY P.REFERENCE",
             null
            , ProductInfoExt.getSerializerRead());
    }


    public int updateAllCustomerInfo(final CustomerInfoExt customer) throws BasicException {

        return new PreparedSentence(s, "UPDATE CUSTOMERS SET SEARCHKEY = ?, TAXID = ?, NAME = ?, TAXCATEGORY = ?,"
                + "CARD = ?, MAXDEBT = ?, ADDRESS = ?, ADDRESS2 = ?, POSTAL = ?, CITY = ?, REGION = ?, COUNTRY = ?,"
                + "FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PHONE = ?, PHONE2 = ?, FAX = ?, NOTES = ?, VISIBLE = ?,"
                + "CURDATE = ?, CURDEBT = ? ,CUSTOMERRECEIVABLE= ?,CUSTOMERADVANCE=? WHERE ID = ?", SerializerWriteParams.INSTANCE).exec(new DataParams() {

            public void writeValues() throws BasicException {
                setString(1, customer.getSearchkey());
                setString(2, customer.getTaxid());
                setString(3, customer.getName());
                setString(4, customer.getTaxCustCategoryID());
                setString(5, customer.getCard());
                setDouble(6, customer.getMaxdebt());
                setString(7, customer.getAddress());
                setString(8, customer.getAddress2());
                setString(9, customer.getPostal());
                setString(10, customer.getCity());
                setString(11, customer.getRegion());
                setString(12, customer.getCountry());
                setString(13, customer.getFirstname());
                setString(14, customer.getLastname());
                setString(15, customer.getEmail());
                setString(16, customer.getPhone());
                setString(17, customer.getPhone2());
                setString(18, customer.getFax());
                setString(19, customer.getNotes());
                setBoolean(20, customer.isVisible());
                setTimestamp(21, customer.getCurdate());
                setDouble(22, customer.getCurdebt());
                setString(23, customer.getId());
                setString(24,customer.getReceivable());
                setString(25,customer.getAdvance());
            }
        });
    }

     //newly added methods by shilpa for production area and areatype masters
    public List<ProductionAreaTypeInfo> getPAreaTypeList() throws BasicException {

        return (List<ProductionAreaTypeInfo>) new StaticSentence(s, "SELECT ID,SEARCHKEY,NAME,DESCRIPTION FROM PRODUCTIONAREATYPE ", null, new SerializerReadClass(ProductionAreaTypeInfo.class)).list();
    }
    
    public void deleteProductionAreaType(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM PRODUCTIONAREATYPE WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
    
     public void insertProductionAreaType(String id, String skey,String name,String desc) throws BasicException {
       
        Object[] values = new Object[]{id,skey, name,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PRODUCTIONAREATYPE (ID,SEARCHKEY,NAME,DESCRIPTION) VALUES (?,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1,2,3})).exec(values);

    }
     
     public String getProductionAreaTypeName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM PRODUCTIONAREATYPE WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
     
     public void updateProductionAreaType(String id, String skey,String name,String desc) throws BasicException {

        Object[] values = new Object[]{id,skey,name,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE PRODUCTIONAREATYPE SET SEARCHKEY=?,NAME = ?,DESCRIPTION=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3, 0})).exec(values);

    }
     
    public List<ProductionAreaInfo> getPAreaList() throws BasicException {

        return (List<ProductionAreaInfo>) new StaticSentence(s, "SELECT ID,SEARCHKEY,NAME,RESTUARANTPRINTER,PRODUCTIONAREATYPE,DESCRIPTION FROM PRODUCTIONAREA ", null, new SerializerReadClass(ProductionAreaInfo.class)).list();
    }
    
    public void deleteProductionArea(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM PRODUCTIONAREA WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }
    
     public void insertProductionArea(String id, String skey,String name,String printer,String area,String desc) throws BasicException {
       
        Object[] values = new Object[]{id,skey, name,printer,area,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PRODUCTIONAREA (ID,SEARCHKEY,NAME,RESTUARANTPRINTER,PRODUCTIONAREATYPE,DESCRIPTION) VALUES (?,?,?,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1,2,3,4,5})).exec(values);

    }
     
     public String getProductionAreaName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM PRODUCTIONAREA WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
     
     public void updateProductionArea(String id, String skey,String name,String printer,String area,String desc) throws BasicException {

        Object[] values = new Object[]{id,skey,name,printer,area,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "UPDATE PRODUCTIONAREA SET SEARCHKEY=?,NAME = ?,RESTUARANTPRINTER=?,PRODUCTIONAREATYPE=?,DESCRIPTION=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3,4,5, 0})).exec(values);

    }
     
      public final SentenceList getProductionAreaTypeList() {
        return new StaticSentence(s
            , "SELECT ID,NAME FROM PRODUCTIONAREATYPE ORDER BY NAME"
            , null
            , new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
                return new ComboInfo(dr.getString(1), dr.getString(2));
            }});
    }
      
       public final SentenceList getPrinterConfigList() {
        return new StaticSentence(s
            , "SELECT ID,NAME FROM PRINTERCONFIG ORDER BY NAME"
            , null
            , new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
                return new ComboInfo(dr.getString(1), dr.getString(2));
            }});
    }
       
        public final SentenceList getProductionAreaList() {
        return new StaticSentence(s
            , "SELECT ID,NAME FROM PRODUCTIONAREA ORDER BY NAME"
            , null
            , new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
                return new ComboInfo(dr.getString(1), dr.getString(2));
            }});
    }
        
        
        //Section Mapping queries   
        public List<SectionMappingInfo> getSectionMappingList() throws BasicException {

        return (List<SectionMappingInfo>) new StaticSentence(s, "SELECT S.ID,S.SECTION,S.PRODUCTIONAREATYPE,S.PRODUCTIONAREA,S.KITCHENDISPLAY,F.NAME FROM SECTIONMAPPING S LEFT JOIN FLOORS F ON S.SECTION=F.ID ORDER BY F.NAME", null, new SerializerReadClass(SectionMappingInfo.class)).list();
    }
        
         public void updateSectionMapping(String id, String section,String pareatype,String parea,String kdisplay) throws BasicException {

        Object[] values = new Object[]{id,section,pareatype,parea,kdisplay};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "UPDATE SECTIONMAPPING SET SECTION=?,PRODUCTIONAREATYPE = ?,PRODUCTIONAREA=?,KITCHENDISPLAY=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3,4, 0})).exec(values);

    }
         
 public void insertSectionMapping(String id,String section,String pareatype,String parea,String kdisplay) throws BasicException {
       
        Object[] values = new Object[]{id,section, pareatype,parea,kdisplay};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "INSERT INTO SECTIONMAPPING (ID,SECTION,PRODUCTIONAREATYPE,PRODUCTIONAREA,KITCHENDISPLAY) VALUES (?,?,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1,2,3,4})).exec(values);

    }
 
  public String getSectionName(String section,String areatype,String area) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT SECTION FROM SECTIONMAPPING WHERE SECTION = ? AND PRODUCTIONAREATYPE='"+areatype+"' AND PRODUCTIONAREA='"+area+"' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(section);
        return record == null ? "NONAME" : (String) record[0];

    }
  
   public void deleteSectionMapping(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM SECTIONMAPPING WHERE ID = ?", SerializerWriteString.INSTANCE).exec(val);

    }
        
          public final SentenceList getSectionList() {
        return new StaticSentence(s
            , "SELECT ID,NAME FROM FLOORS ORDER BY NAME"
            , null
            , new SerializerRead() { public Object readValues(DataRead dr) throws BasicException {
                return new ComboInfo(dr.getString(1), dr.getString(2));
            }});
    }
        
          
      //Printer Config queries    
     public void insertPrinter(String id, String skey,String name,String desc) throws BasicException {
       
        Object[] values = new Object[]{id,skey, name,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "INSERT INTO PRINTERCONFIG (ID,SEARCHKEY,NAME,DESCRIPTION) VALUES (?,?,?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1,2,3})).exec(values);

    }   
     
      public String getPrinterName(String id) throws BasicException {

        Object[] record = (Object[]) new StaticSentence(s, "SELECT NAME FROM PRINTERCONFIG WHERE NAME = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING})).find(id);
        return record == null ? "NONAME" : (String) record[0];

    }
          
    public void deletePrinter(String val) throws BasicException {

        new StaticSentence(s, "DELETE FROM PRINTERCONFIG WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);

    }

    public List<ProductionAreaTypeInfo> getPrinterConfigurationList() throws BasicException {

        return (List<ProductionAreaTypeInfo>) new StaticSentence(s, "SELECT ID,SEARCHKEY,NAME,DESCRIPTION FROM PRINTERCONFIG ", null, new SerializerReadClass(ProductionAreaTypeInfo.class)).list();
    }
    
    public void updatePrinter(String id, String skey,String name,String desc) throws BasicException {

        Object[] values = new Object[]{id,skey,name,desc};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE PRINTERCONFIG SET SEARCHKEY=?,NAME = ?,DESCRIPTION=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3, 0})).exec(values);

    }
    //Menu based product addon
    public SentenceList getMenuAddonProduct(String product,String menuId) {
         return new StaticSentence(s
            ,
            "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, M.PRICEBUY, M.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES, P.ITEMCODE, M.MRP,'',P.PRODUCTTYPE,P.PRODUCTIONAREATYPE,P.SERVICECHARGE,P.SERVICETAX,C.PARENTID,P.PREPARATIONTIME,P.SWACHBHARATTAX,ISCOMBOPRODUCT  " +
              "FROM PRODUCTS P LEFT JOIN PRODUCTLINKEDADDON L ON L.ADDON=P.ID LEFT JOIN CATEGORIES C ON P.CATEGORY=C.ID RIGHT JOIN MENUPRICELIST M ON M.PRODUCTID=P.ID WHERE L.LINKEDTO ='" + product + "' AND M.MENUID='"+menuId+"'  AND P.ISACTIVE='Y' GROUP BY P.ID ORDER BY P.REFERENCE",
             null
            , ProductInfoExt.getSerializerRead());
    }
    //Kod Master
//    public List<KodInfo> getKodList() throws BasicException {
//
//        return (List<KodInfo>) new StaticSentence(s, "SELECT ID, NAME, COLORCODE,SEQUENCENO FROM KODSTATUSMASTER ", null, new SerializerReadClass(KodInfo.class)).list();
//    }
    
//    public void insertKodStatus(String id, String name, String color,String sequence) throws BasicException {
//        System.out.println("inside insertKodStatus method");
//        Object[] values = new Object[]{id, name, color,sequence};
//        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING};
//        new PreparedSentence(s, "INSERT INTO KODSTATUSMASTER (ID, NAME, COLORCODE,SEQUENCENO) VALUES (?, ?, ?,?)", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2,3})).exec(values);
//
//    }
    
    
 public final synchronized void insertKodStatus(final String id,final String name,final String color,final String sequence,final String isLastStatus,final java.util.List<String> selectedList) 
					throws BasicException {
       
        Transaction t = new Transaction(s) {            
           public Object transact() throws BasicException {                      

                // new kod status                    
               new PreparedSentence(s
                    , "INSERT INTO KODSTATUSMASTER (ID, NAME, COLORCODE,SEQUENCENO,ISLASTSTATUS) "
                        + "VALUES(?, ?, ?,?,?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, id);
                        setString(2, name);
			setString(3, color);
                        setString(4, sequence);
                        setString(5, isLastStatus);
                    }});
               if(selectedList!=null){
               for(final String l:selectedList){
                new PreparedSentence(s
                    , "INSERT INTO KODSTATUSROLEACCESS (ID, KDSSTATUS_ID, ROLEID) "
                        + "VALUES(?,?,?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setString(2, id);
			setString(3, l);
                    }});   
               }
                }
               return null;
                 }
               };
            t.execute();
           
      }
    public void updateKod(String id,String name, String color,String sequence) throws BasicException {

        Object[] values = new Object[]{id,name,color,sequence};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING};
        new PreparedSentence(s, "UPDATE KODSTATUSMASTER SET NAME=?,COLORCODE = ?,SEQUENCENO=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3,0})).exec(values);

    }
    
//     public void deleteKodLine(String val) throws BasicException {
//
//        new StaticSentence(s, "DELETE FROM KODSTATUSMASTER WHERE NAME = ?", SerializerWriteString.INSTANCE).exec(val);
//
//    }
     
     public List<BeanInfo> getRolesList() throws BasicException {

        return (List<BeanInfo>) new StaticSentence(s, "SELECT ID,NAME FROM ROLES ", null, new SerializerReadClass(BeanInfo.class)).list();
    }
     
     public List<BeanInfo> getKodRoleList(String kodId) throws BasicException {

        return (List<BeanInfo>) new StaticSentence(s, "SELECT  ID,ROLEID FROM KODSTATUSROLEACCESS WHERE KDSSTATUS_ID='"+kodId+"' ", null, new SerializerReadClass(BeanInfo.class)).list();
    }
     
     
public final synchronized void updateKodLines(final String id,final java.util.List<String> selectedList) 
					throws BasicException {
       
        Transaction t = new Transaction(s) {            
           public Object transact() throws BasicException {                      

                new StaticSentence(s, "DELETE FROM KODSTATUSROLEACCESS WHERE KDSSTATUS_ID = ?", SerializerWriteString.INSTANCE).exec(id);
               
              if(selectedList!=null){
               for(final String l:selectedList){
                new PreparedSentence(s
                    , "INSERT INTO KODSTATUSROLEACCESS (ID, KDSSTATUS_ID, ROLEID) "
                        + "VALUES(?,?,?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, UUID.randomUUID().toString());
                        setString(2, id);
			setString(3, l);
                    }});   
               }
                }
              return null;
                 }
               };
            t.execute();
   }

    public final synchronized void deleteKodLine(final String val) throws BasicException {
Transaction t = new Transaction(s) {            
           public Object transact() throws BasicException { 
               new StaticSentence(s, "DELETE FROM KODSTATUSROLEACCESS WHERE KDSSTATUS_ID = ?", SerializerWriteString.INSTANCE).exec(val);  
              new StaticSentence(s, "DELETE FROM KODSTATUSMASTER WHERE ID = ?", SerializerWriteString.INSTANCE).exec(val);
            return null;
           }
         };
        t.execute(); 
               
     }
    
   //Kds master role settings ( is last status queries)
    
     public List<KodInfo> getKodList() throws BasicException {

        return (List<KodInfo>) new StaticSentence(s, "SELECT ID, NAME, COLORCODE,SEQUENCENO,ISLASTSTATUS FROM KODSTATUSMASTER ", null, new SerializerReadClass(KodInfo.class)).list();
    }
         public int getMaxSequenceNo()  throws BasicException
      {
         Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT MAX(SEQUENCENO) FROM  KODSTATUSMASTER "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
         
          
      }
         
      public void updateKodStatus() throws BasicException {
        Datas[] datas = new Datas[]{};
        new PreparedSentence(s, "UPDATE KODSTATUSMASTER SET ISLASTSTATUS='N' ", new SerializerWriteBasicExt(datas, new int[]{})).exec();
    }
      
       public int getLastStatusSequenceNo()  throws BasicException {
      Object[] record = null;
      record = (Object[]) new StaticSentence(s,"SELECT SEQUENCENO  FROM  KODSTATUSMASTER WHERE ISLASTSTATUS='Y' ", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
      if(record!=null){
         int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
         return (i == 0 ? 0 : i);
      }else{
         return 0;
      }
     }
       
        public int getMaxSequenceNoInUpdation(String id)  throws BasicException
      {
         Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT MAX(SEQUENCENO) FROM  KODSTATUSMASTER WHERE ID <> ? "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(id);
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);
         
          
      }
         public void updateKod(String id,String name, String color,String sequence,String lastStatus) throws BasicException {
        Object[] values = new Object[]{id,name,color,sequence,lastStatus};
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING,Datas.STRING, Datas.STRING,Datas.STRING};
        new PreparedSentence(s, "UPDATE KODSTATUSMASTER SET NAME=?,COLORCODE = ?,SEQUENCENO=? , ISLASTSTATUS=?  WHERE ID= ? ", new SerializerWriteBasicExt(datas, new int[]{1,2,3,4,0})).exec(values);
      }
         
       public int getLastStatusSequenceNoInUpdation(String id)  throws BasicException {
         Object[] record = null;
         record = (Object[]) new StaticSentence(s,"SELECT SEQUENCENO  FROM  KODSTATUSMASTER WHERE ISLASTSTATUS='Y' AND   ID <> ? " , SerializerWriteString.INSTANCE , new SerializerReadBasic(new Datas[] {Datas.STRING})).find(id);
         if(record!=null){
             int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
             return (i == 0 ? 0 : i);
         } else {
             return 0;
         }
      }
        
}
