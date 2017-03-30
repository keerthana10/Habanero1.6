/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysfore.pos.licensemanagement;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import java.util.List;


/**
 *
 * @author mateen
 */
public class DataLogicLicense extends BeanFactoryDataSingle {

    private Session s;
private SentenceExec m_InsertLicense;
    @Override
    public void init(Session s) {
        this.s = s;
         Datas[] m_LicenseValue = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING };
        m_InsertLicense = new PreparedSentence(s
                , "INSERT INTO SLMLICENSEHISTORY (ID, INFO1, INFO2, INFO3, INFO4 , INFO5, INFO6, INFO7, INFO8, INFO9, INFO10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                , new SerializerWriteBasic(m_LicenseValue));
    }

     public int getLicenseCount() throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM SLMLICENSEHISTORY "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
     

     public int getExpiryLicenseCount(String validFrom,String validTo) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM SLMLICENSEHISTORY WHERE CURDATE() BETWEEN '"+validFrom+"' AND '"+validTo+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
     
   
      
     public final void insertLicenseKey(String Id,String licenseKey, String activatedFrom,String activatedto,String licenceType,String active,String macId, String strPurpose, String calltype, String email,String info9) throws BasicException {

       Object[] value = new Object[] {Id,licenseKey,activatedFrom,activatedto,licenceType,active,macId, strPurpose,calltype, email,info9};

       m_InsertLicense.exec(value);

   }
//     public final void insertLicenseKey1(final ClientEntity lc) throws BasicException {
//         new PreparedSentence(s
//                    , "INSERT INTO SLMLICENSEHISTORY (ID, INFO1, INFO2, INFO3, INFO4 , INFO5, INFO6, INFO7, INFO8, INFO9, INFO10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
//                    , SerializerWriteParams.INSTANCE
//                    ).exec(new DataParams() { public void writeValues() throws BasicException {
//                        setString(1, lc.getId());
//                        setString(2, lc.getLicesneKey());
//                        setString(3, lc.getActivateFrom());
//                        setString(4, lc.getActivateTo());
//                        setString(5, lc.getUsageType());
//                        setString(6, lc.getActivated());
//                        setString(7, lc.getMachineAddress());
//                        setString(8, lc.getStrPurposeValue());
//                        setString(9, lc.getCallType());
//                        setString(10, lc.getInfo9());
//                        setString(11, lc.getInfo10());
//                    }});
//     }
//

         public final List<LicenceInfo> getLicenseDetails() throws BasicException {
        return new PreparedSentence(s
            , "SELECT ID,INFO6,INFO1,INFO4,INFO8,INFO7,INFO9,INFO2,INFO3,INFO5,INFO10 FROM SLMLICENSEHISTORY "
            , null
            , new SerializerReadClass(LicenceInfo.class)).list();
    }
         
   
         
       public void updateLicense(String id, String licenseKey, String currentDate, String activationTo, String licenseTypeId, String Active, String macId, String strPurpose, String callType, String email, String info10) throws BasicException {

        Object[] values = new Object[]{id, licenseKey, currentDate, activationTo, licenseTypeId, Active, macId, strPurpose, callType, email, info10 };
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING };
        new PreparedSentence(s, "UPDATE SLMLICENSEHISTORY SET ID = ?, INFO1 = ?, INFO2= ?, INFO3 = ?, INFO4 = ?, INFO5 = ?, INFO6 = ?, INFO7 = ?, INFO8 = ?, INFO9 = ?, INFO10 = ? ", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})).exec(values);

    }
       public final synchronized void deleteLicenseKey() throws BasicException {

        new StaticSentence(s
            , "DELETE FROM SLMLICENSEHISTORY"
            , SerializerWriteString.INSTANCE).exec();
    }
      
    //Querries of multi POS System license logic (By Shilpa)   
       
       public int getMultiLicenseCount(String mac) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM SLMLICENSEHISTORY WHERE INFO WHERE INFO6='"+mac+"'"
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
       
    public int getMultiExpiryLicenseCount(String validFrom,String validTo,String mac) throws BasicException {
        Object[] record = null;

      record = (Object[]) new StaticSentence(s
                    ,"SELECT COUNT(*) FROM SLMLICENSEHISTORY WHERE CURDATE() BETWEEN '"+validFrom+"' AND '"+validTo+"' AND INFO6='"+mac+"' "
                    , SerializerWriteString.INSTANCE
                    , new SerializerReadBasic(new Datas[] {Datas.STRING})).find();
            int i = Integer.parseInt(record[0]==null ? "0" :record[0].toString());
            return (i == 0 ? 0 : i);


    }
    
     public void updateMultiSysLicense(String id, String licenseKey, String currentDate, String activationTo, String licenseTypeId, String Active, String macId, String strPurpose, String callType, String email, String info10,String mac) throws BasicException {

        Object[] values = new Object[]{id, licenseKey, currentDate, activationTo, licenseTypeId, Active, macId, strPurpose, callType, email, info10,mac };
        Datas[] datas = new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,Datas.STRING };
        new PreparedSentence(s, "UPDATE SLMLICENSEHISTORY SET ID = ?, INFO1 = ?, INFO2= ?, INFO3 = ?, INFO4 = ?, INFO5 = ?, INFO6 = ?, INFO7 = ?, INFO8 = ?, INFO9 = ?, INFO10 = ? WHERE INFO6=? ", new SerializerWriteBasicExt(datas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11})).exec(values);

    }
       

}
